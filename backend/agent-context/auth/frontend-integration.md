# Frontend Agent Brief — Auth & Subscriber Signup Integration

**Audience:** Frontend AI agent implementing Fleeting Jobs auth UI.  
**Do not** open or reverse-engineer backend source. Use **this document + Swagger/OpenAPI** as your contract. If Swagger and this brief disagree on a field name, prefer **Swagger**.

**Last updated:** 2026-09-29

---

## Product context (what you are building)

Fleeting Jobs is a job-application platform. End users who sign up themselves are **subscribers** (`SUBSCRIBER` role). Admins/operators are seeded separately and are out of scope for public signup.

Your job is to implement:

1. Email/password **signup** + **email confirmation** UX
2. **Login**
3. **Google** signup/sign-in
4. **LinkedIn** signup/sign-in
5. Optional: **set-password** for seeded OTP accounts (admin/demo), if that screen exists

After a successful authenticated session, store the JWT and send it on API calls. Use `permissions` from the auth response to gate UI if the app already does RBAC; do not invent permission strings.

---

## Base URL & CORS

- Local API base (typical): `http://localhost:8080`
- Swagger UI (typical): `http://localhost:8080/swagger-ui/index.html`
- CORS allows: `http://localhost:5173`, `http://127.0.0.1:5173`
- Content-Type: `application/json`
- Auth header for protected routes: `Authorization: Bearer <token>`

All routes below are under `/auth` and are **public** (no Bearer required).

---

## Shared response types

### Success session payload — `AuthResponseDto`

Returned by: login (normal), confirm-email, Google signup, LinkedIn signup, set-password (success).

```ts
type AuthResponse = {
  token: string | null;          // JWT; null when requiresPasswordSetup is true
  userId: number;
  email: string;
  role: {
    id: number;
    name: string;                // e.g. "SUBSCRIBER", "SUPER_ADMIN"
    createdAt?: string;
    updatedAt?: string;
  };
  permissions?: Array<{
    id: number;
    module: string;              // e.g. "PROFILE", "JOBS"
    submodule?: string;          // e.g. "EDUCATION", "DEFAULT"
    action: string;              // e.g. "LIST", "CREATE"
    permitted: boolean;
    // may include nested/ignored fields from serialization — ignore extras
  }>;
  requiresPasswordSetup: boolean;
};
```

**Frontend rules after receiving `AuthResponse`:**

| Condition | UI action |
|---|---|
| `requiresPasswordSetup === true` | Do **not** treat as logged in. Send user to set-password screen with their email. `token` is null. |
| `token` is a non-empty string | Persist token (memory + secure storage of your choice). Treat user as logged in. Navigate to app home / onboarding. |
| `role.name === "SUBSCRIBER"` | Normal end-user experience. |

### Email signup-only payload — `SignupResponseDto`

Returned by: `POST /auth/signup` (`201 Created`).

```ts
type SignupResponse = {
  message: string;
  email: string;
  requiresEmailConfirmation: boolean; // true after email signup
};
```

**Do not** expect `token` here. Show “check your email” UI.

### Error payload — `APIErrorResponse`

Typical error body:

```ts
type ApiError = {
  success: false;
  message: string;
  errors?: Record<string, string>; // field → message (validation)
};
```

Map HTTP status to UX (see each endpoint). Prefer showing `message`; for `400` validation, also surface `errors` on form fields.

---

## Flows to implement

### A) Email / password signup (subscriber)

```
User fills form → POST /auth/signup → show "confirm your email"
User opens email link or SPA confirm page → confirm-email → store JWT → enter app
```

#### `POST /auth/signup`

**Request**

```json
{
  "fullName": "Abtahi Tajwar",
  "email": "user@example.com",
  "password": "at-least-8-chars"
}
```

| Field | Rules |
|---|---|
| `fullName` | Required. Backend splits into first/last name. |
| `email` | Required, valid email. |
| `password` | Required, **min 8 characters**. |

**Success:** `201` + `SignupResponse`

**Errors (typical):**

| Status | Meaning | UX |
|---|---|---|
| `400` | Validation failed | Show field errors |
| `409` | Email already registered | Link to login / forgot flow |
| `5xx` | Mail send / server failure | Retry message |

**UI after success:** “We sent a confirmation link to `{email}`.” Offer “Resend” only if/when a resend API exists (it does **not** exist yet — do not invent one).

#### Confirm email

Confirmation emails include a token. Frontend should support a route such as:

`/confirm-email?token=<token>`

Call either:

**Preferred for SPA:** `POST /auth/confirm-email`

```json
{ "token": "<from query string>" }
```

**Also available:** `GET /auth/confirm-email?token=<token>`

**Success:** `200` + `AuthResponse` with JWT → log user in.

**Errors:**

| Status | Meaning | UX |
|---|---|---|
| `400` | Invalid or expired token | “Link expired or invalid. Sign up again or contact support.” |

Token lifetime: **24 hours**.

---

### B) Login

#### `POST /auth/login`

**Request**

```json
{
  "email": "user@example.com",
  "password": "their-password"
}
```

**Success:** `200` + `AuthResponse`

Branch on response:

1. If `requiresPasswordSetup === true` → set-password screen (seeded OTP accounts).
2. Else store `token` and enter app.

**Errors:**

| Status | Meaning | UX |
|---|---|---|
| `400` | Validation | Field errors |
| `401` | Bad email/password | Generic auth error |
| `403` | Email not confirmed (local signup) | “Confirm your email first” + tip to check inbox |

---

### C) Google signup / sign-in

Frontend owns Google Identity / OAuth UI. Backend does **not** start Google OAuth and does **not** use a client secret for this flow.

1. Obtain a Google **ID token** (`credential` / `id_token`) from Google Identity Services (or equivalent).
2. Send it to the backend.

#### `POST /auth/signup/google`

**Request**

```json
{
  "idToken": "<google-id-token-jwt>"
}
```

**Success:** `200` + `AuthResponse` with JWT (email is treated as verified). Same endpoint is used for first-time signup **and** returning Google users (upsert/sign-in).

**Errors:**

| Status | Meaning | UX |
|---|---|---|
| `400` | Google email not verified | Ask user to use a verified Google account |
| `401` | Invalid / rejected Google token | Retry Google sign-in |
| `409` | Email already registered with a **different** method | “This email uses email/password or LinkedIn — sign in that way” |

**Env for frontend:** Google OAuth **Web Client ID** (public). Backend may also be configured with the same client ID to validate token audience.

---

### D) LinkedIn signup / sign-in

Frontend owns LinkedIn OAuth (authorization code → access token on the client, or your BFF if you add one later). Backend expects an **access token**, then calls LinkedIn userinfo.

#### `POST /auth/signup/linkedin`

**Request**

```json
{
  "accessToken": "<linkedin-access-token>"
}
```

**Success:** `200` + `AuthResponse` with JWT (signup or returning user).

**Errors:** same pattern as Google (`400` / `401` / `409`).

Request OpenID scopes that provide email (e.g. `openid profile email`) so LinkedIn returns an email.

---

### E) Set password (seeded OTP users only)

Used when login returns `requiresPasswordSetup: true` (e.g. demo/admin seed accounts). Not part of normal subscriber email signup.

#### `POST /auth/set-password`

**Request**

```json
{
  "email": "user@example.com",
  "otp": "0000",
  "password": "new-secure-password"
}
```

**Success:** `200` + `AuthResponse` with JWT.

**Errors:** `400` already set / bad state; `401` invalid email or OTP.

---

## Endpoint cheat sheet

| Method | Path | Request body | Success |
|---|---|---|---|
| POST | `/auth/signup` | `fullName`, `email`, `password` | `201` SignupResponse |
| POST | `/auth/confirm-email` | `token` | `200` AuthResponse |
| GET | `/auth/confirm-email?token=` | — | `200` AuthResponse |
| POST | `/auth/login` | `email`, `password` | `200` AuthResponse |
| POST | `/auth/signup/google` | `idToken` | `200` AuthResponse |
| POST | `/auth/signup/linkedin` | `accessToken` | `200` AuthResponse |
| POST | `/auth/set-password` | `email`, `otp`, `password` | `200` AuthResponse |

Verify paths/schemas in Swagger under the **auth** tag.

---

## Session handling (required behavior)

1. On JWT success, persist:
   - `token`
   - `userId`
   - `email`
   - `role.name`
   - `permissions` (optional but recommended for UI gating)
2. Attach `Authorization: Bearer <token>` to all non-auth API calls.
3. On `401` from protected APIs, clear session and redirect to login.
4. Do not call protected APIs until email is confirmed (for email signup) or OAuth returns a token.
5. Self-serve signup always creates **subscribers**. Do not send a role field — the API does not accept one.

---

## Suggested screens / routes

| Route | Purpose |
|---|---|
| `/signup` | Email form + Google + LinkedIn buttons |
| `/login` | Email/password + Google + LinkedIn |
| `/confirm-email` | Reads `?token=`, calls confirm API, then redirects into app |
| `/check-email` | Post-signup waiting state |
| `/set-password` | Only if login returns `requiresPasswordSetup` |

Copy guidance:

- Signup success: “Check your inbox to confirm your email before signing in.”
- Login `403`: “Please confirm your email before logging in.”
- OAuth `409`: “An account with this email already exists using another sign-in method.”

---

## What NOT to implement / invent

- Do not invent `/auth/resend-confirmation` unless Swagger shows it.
- Do not send Google/LinkedIn **client secrets** from the browser.
- Do not implement backend OAuth redirect/callback URLs unless product asks for a BFF later; current API is token-exchange only.
- Do not treat `POST /users` as subscriber signup — use `/auth/signup`.
- Do not skip email confirmation UX for local signup.

---

## Implementation checklist for the frontend agent

1. Read Swagger auth endpoints and align TypeScript types with schemas.
2. Build signup form (`fullName`, `email`, `password` ≥ 8) → `POST /auth/signup` → check-email page.
3. Build `/confirm-email?token=` → `POST /auth/confirm-email` → store JWT → home.
4. Build login → handle `403` unverified, `401` bad credentials, `requiresPasswordSetup`.
5. Wire Google button → ID token → `POST /auth/signup/google` → store JWT.
6. Wire LinkedIn button → access token → `POST /auth/signup/linkedin` → store JWT.
7. Centralize auth header + logout on `401`.
8. Manually test: signup → Mailtrap/inbox confirm → login; Google; LinkedIn; duplicate-email `409`.

---

## Quick curl examples (for manual verification)

```bash
# Signup
curl -s -X POST http://localhost:8080/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Test User","email":"test@example.com","password":"password123"}'

# Confirm (token from email)
curl -s -X POST http://localhost:8080/auth/confirm-email \
  -H 'Content-Type: application/json' \
  -d '{"token":"PASTE_TOKEN"}'

# Login
curl -s -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"test@example.com","password":"password123"}'

# Google
curl -s -X POST http://localhost:8080/auth/signup/google \
  -H 'Content-Type: application/json' \
  -d '{"idToken":"GOOGLE_ID_TOKEN"}'

# LinkedIn
curl -s -X POST http://localhost:8080/auth/signup/linkedin \
  -H 'Content-Type: application/json' \
  -d '{"accessToken":"LINKEDIN_ACCESS_TOKEN"}'
```
