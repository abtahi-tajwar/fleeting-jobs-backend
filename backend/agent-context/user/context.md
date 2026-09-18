# User module context

**Last updated:** 2026-08-19

Read `agent-context/architecture.md` first. For login, OTP, and password setup, also read `agent-context/auth/context.md`. Update this file after user-module changes.

## Purpose

CRUD for platform users regardless of role (job seekers and admins). Identity, contact, and profile-header fields live here. Authentication credentials (`password`, `otp`) are stored on `UserEntity` but are owned by the auth flow.

## Package layout

```
user/
├── controller/UserController.java
├── dto/
│   ├── UserCreateDto.java
│   ├── UserUpdateDto.java
│   └── UserResponseDto.java
├── entity/UserEntity.java
├── enums/Role.java
├── mapper/UserMapper.java
├── repository/UserRepository.java
└── service/UserService.java
```

## Entity (`users`)

- Identity PK, unique email
- Profile header: name, phone, linkedin, github, portfolioWebsite, city, province, country, summary
- `role`: `USER` (default) or `ADMIN`
- `password`: BCrypt hash of the permanent password. Nullable until first password set (seeded users)
- `otp`: BCrypt hash of the one-time / temporary password. Null after password is set, and null for self-registered users

Never expose `password` or `otp` in `UserResponseDto`. MapStruct already ignores `password` on response and ignores `otp` on create/update.

## API (`/users`)

- `POST /users` — public registration; sets hashed `password`, leaves `otp` null
- `GET /users`
- `GET /users/{id}`
- `PUT /users/{id}` — does not update password or otp through this DTO mapping
- `DELETE /users/{id}`

Password changes for seeded first-login go through `POST /auth/set-password`, not user update.

## Dependencies

- Auth module seeds and authenticates against `UserRepository`
- Profile submodules own child records via `user_id`
