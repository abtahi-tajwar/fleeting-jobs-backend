# Auth module context

**Last updated:** 2026-09-29

Read `agent-context/architecture.md` first. Update this file after auth/security changes.

## Purpose

Handles authentication, subscriber signup (email + Google + LinkedIn), email confirmation, JWT issue/validation, Spring Security, first-login password setup for seeded users, and the RBAC role/permission catalog. Users themselves live in the `user` module.

## Package layout

```
auth/
├── annotation/Authorize.java
├── config/SecurityConfig.java, PasswordConfig.java
├── controller/AuthController.java
├── dto/
│   ├── AuthResponseDto.java
│   ├── ConfirmEmailRequestDto.java
│   ├── GoogleSignupRequestDto.java
│   ├── LinkedInSignupRequestDto.java
│   ├── LoginRequestDto.java
│   ├── SetPasswordRequestDto.java
│   ├── SignupRequestDto.java
│   └── SignupResponseDto.java
├── entity/RoleEntity.java, PermissionEntity.java
├── enums/AuthProvider.java
├── filter/AuthenticationFilter.java, AuthorizationInterceptor.java
├── repository/RoleRepository.java, PermissionRepository.java
├── seeder/AuthSeeder.java, PermissionSeeder.java
└── service/
    ├── AuthService.java
    ├── EmailService.java
    ├── JwtService.java
    └── OAuthService.java
```

## Subscriber signup

Self-serve signup always creates users with the `SUBSCRIBER` role.

### Email / password

```
POST /auth/signup
{ "fullName": "...", "email": "...", "password": "..." }
```

1. Split `fullName` into first/last name.
2. Create LOCAL subscriber with hashed password, `emailVerified=false`, and a 24h confirmation token.
3. Send confirmation email via SMTP (Mailtrap in local/testing).
4. Response: `{ message, email, requiresEmailConfirmation: true }` — **no JWT**.

Confirm:

```
POST /auth/confirm-email  { "token": "..." }
GET  /auth/confirm-email?token=...
```

On success: mark verified, clear token, return `AuthResponseDto` with JWT.

Login with an unverified LOCAL account returns `403 Please confirm your email before logging in`.

### Google

```
POST /auth/signup/google
{ "idToken": "..." }
```

Backend verifies the Google ID token (`oauth2.googleapis.com/tokeninfo`). Creates or signs in a `SUBSCRIBER` with `authProvider=GOOGLE`, `emailVerified=true`, and returns JWT. Existing account with a different provider → `409`.

### LinkedIn

```
POST /auth/signup/linkedin
{ "accessToken": "..." }
```

Backend calls LinkedIn OpenID `userinfo` with the access token. Same upsert rules as Google with `authProvider=LINKEDIN`.

Frontend owns the OAuth consent UI; these routes only exchange provider tokens for app JWTs.

## User entity auth fields

| Column | Meaning |
|---|---|
| `password` | BCrypt permanent password (null for pure OAuth users). |
| `otp` | One-time password for seeded first login. |
| `auth_provider` | `LOCAL`, `GOOGLE`, or `LINKEDIN`. |
| `provider_subject` | Provider user id (`sub`). |
| `email_verified` | Must be true for LOCAL login / JWT filter. |
| `email_verification_token` | Confirmation token until verified. |
| `email_verification_expires_at` | Token expiry (24h). |

## Mailtrap (testing)

Set in `.env` (see `.env.example`):

```
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=...
MAIL_PASSWORD=...
MAIL_FROM=noreply@fleetingjobs.test
```

Optional: `GOOGLE_CLIENT_ID` (validates token `aud`), `FRONTEND_URL`, `BACKEND_URL`.

## Roles and permissions

Tables `roles` / `permissions` seeded from `seeds/auth/*.json`. Users have `role_id` → `RoleEntity`. Seeded admin is `SUPER_ADMIN`; demo and self-signup users are `SUBSCRIBER`.

## Password vs OTP (seeded admins)

Seeded users get OTP only (`password` null, `emailVerified=true`). Login with OTP returns `requiresPasswordSetup: true` and no JWT. `POST /auth/set-password` then sets the real password.

## Login flow

```
POST /auth/login
{ "email": "...", "password": "..." }
```

1. Match permanent password → require verified email → JWT + permissions.
2. Match OTP (seeded) → `requiresPasswordSetup: true`.
3. Else → `401`.

## Security

- Public: `/auth/**`, swagger, `POST /users`
- Everything else requires JWT
- Stateless, CSRF disabled
- Filter refuses JWT auth while `otp` is set or `emailVerified` is false
- Endpoint authorization uses `@Authorize` + permissions tables

## API

| Method | Path | Auth | Result |
|---|---|---|---|
| POST | `/auth/login` | public | `AuthResponseDto` |
| POST | `/auth/signup` | public | `SignupResponseDto` |
| POST | `/auth/confirm-email` | public | `AuthResponseDto` |
| GET | `/auth/confirm-email?token=` | public | `AuthResponseDto` |
| POST | `/auth/signup/google` | public | `AuthResponseDto` |
| POST | `/auth/signup/linkedin` | public | `AuthResponseDto` |
| POST | `/auth/set-password` | public | `AuthResponseDto` |

`AuthResponseDto`: `{ token, userId, email, role, permissions, requiresPasswordSetup }`

## Dependencies

- `user` module: `UserEntity`, `UserRepository`
- Spring Mail → Mailtrap SMTP
- WebClient → Google / LinkedIn token verification
