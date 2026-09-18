# Auth module context

**Last updated:** 2026-08-19

Read `agent-context/architecture.md` first. Update this file after auth/security changes.

## Purpose

Handles authentication, JWT issue/validation, Spring Security, and first-login password setup for seeded users. Users themselves live in the `user` module. Auth has no entity or repository.

## Package layout

```
auth/
├── config/SecurityConfig.java
├── controller/AuthController.java
├── dto/
│   ├── AuthResponseDto.java
│   ├── LoginRequestDto.java
│   └── SetPasswordRequestDto.java
├── filter/JwtAuthenticationFilter.java
├── seeder/AuthSeeder.java
└── service/
    ├── AuthService.java
    └── JwtService.java
```

## Password vs OTP

`UserEntity` has two credential columns:

| Column | Meaning |
|---|---|
| `password` | Permanent password. Nullable until the user sets it. Hashed with BCrypt. |
| `otp` | One-time / temporary password used only for first setup. Hashed with BCrypt. Cleared (`null`) after a successful password set. |

Rules:

1. Seeded users get an OTP only. `password` stays `null`.
2. Registered users (`POST /users`) get a real password. `otp` stays `null`.
3. OTP and password are never returned in API responses.
4. Login with the real password issues a JWT.
5. Login with the OTP does **not** issue a JWT. The client must call set-password next.
6. Set-password requires `email`, `otp`, and the new `password`. On success the OTP is deleted, the password is stored hashed, and a JWT is issued.
7. While `otp` is still set, JWT authentication is ignored. The user cannot call protected APIs until password setup is complete.

## Login flow

```
POST /auth/login
{ "email": "...", "password": "..." }
```

`password` in this request is whichever secret the user currently has (OTP or real password).

1. Look up user by email. Unknown email → `401 Invalid email or password`.
2. If it matches `password` → `{ token, userId, email, role, requiresPasswordSetup: false }`
3. If it matches `otp` → `{ token: null, userId, email, role, requiresPasswordSetup: true }`
   No JWT. Client must send the user to set-password.
4. Otherwise → `401 Invalid email or password`

## Set password flow

```
POST /auth/set-password
{ "email": "...", "otp": "...", "password": "new-password" }
```

Public endpoint (`/auth/**` is permitAll).

1. Unknown email or wrong OTP → `401 Invalid email or OTP`
2. OTP already cleared → `400 Password has already been set for this account`
3. Success → hash new password, set `otp = null`, return the same payload as a normal login (`requiresPasswordSetup: false` + JWT)

After this, the old OTP cannot be used again. The user logs in with the new password.

## Seeding

`AuthSeeder.seedUser(email, rawOtp)` creates an `ADMIN` if the email does not exist:

- `otp` = BCrypt(rawOtp)
- `password` = null

`DatabaseSeeder` currently seeds `admin@test.com` with OTP `password123`.

The seeder is skip-if-exists. An admin already created with a real password (old behavior) will keep working via normal login and will not be migrated onto OTP automatically.

To re-test first-login on an existing database:

1. Ensure `users.password` is nullable (`ALTER TABLE users ALTER COLUMN password DROP NOT NULL` if Hibernate did not change it).
2. Delete `admin@test.com`.
3. Restart the app so the seeder recreates the user with OTP only.

## Security

- Public: `/auth/**`, `POST /users`
- Everything else requires JWT
- Stateless, CSRF disabled
- `AuthService` implements `UserDetailsService` (email as username)
- `JwtAuthenticationFilter` skips `/auth/**` and `POST /users`
- If the user still has an OTP, the filter does not set `SecurityContext` even when a Bearer token is present
- `loadUserByUsername` uses `{noop}LOCKED` when `password` is null so Spring `User` is never given a null password
- Login no longer uses `AuthenticationManager`; it compares secrets with `PasswordEncoder` directly
- Roles exist (`USER`, `ADMIN`) but endpoint authorization is still authenticated-vs-public, not role-based

## API

| Method | Path | Auth | Result |
|---|---|---|---|
| POST | `/auth/login` | public | `AuthResponseDto` |
| POST | `/auth/set-password` | public | `AuthResponseDto` |

`AuthResponseDto`: `{ token, userId, email, role, requiresPasswordSetup }`

## Dependencies

- `user` module: `UserEntity`, `UserRepository`
- `common.exception.GlobalExceptionHandler` maps `BadCredentialsException` → 401

Do not put user CRUD in this module. Do not store plaintext OTP or password.
