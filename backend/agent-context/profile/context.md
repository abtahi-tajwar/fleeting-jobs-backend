# Profile module context

**Last updated:** 2026-09-24

Read `agent-context/architecture.md` first. Update this file after profile-module changes.

## Purpose

Structured profile data used to build CVs, cover letters, and other application documents. There is no parent `ProfileEntity`. Header fields (name, contact, summary) live on `UserEntity`. Everything else is a user-owned submodule.

## Package layout

```
profile/
├── seeder/
│   ├── ProfileSeeder.java
│   └── ProfileSeedData.java
└── _submodules/
    ├── education/      → table educations        → /users/{userId}/education
    ├── workexperience  → table work_experiences  → /users/{userId}/experiences
    ├── skill/          → table skills            → /users/{userId}/skills
    ├── certification/  → table certifications    → /users/{userId}/certifications
    └── award/          → table awards            → /users/{userId}/awards
```

Each submodule has `entity`, `repository`, `service`, `controller`, `mapper`, `dto`.

Submodule controller methods use `@Authorize` with `module = PROFILE` and the matching `AppModule.Submodule`. Actions follow `permissions.json` for that submodule: `CREATE` on POST, `LIST` on GET collection, `UPDATE` on PUT, `DELETE` on DELETE. There is no GET-by-id, so `READ` is not applied on these controllers.

## Demo profile seed

`ProfileSeeder.seedDemoProfile()` reads `src/main/resources/seeds/profile/demo-profile.json`.

The JSON matches the user header plus submodule fields:

- `email` — must already exist (created by `AuthSeeder` as `SUBSCRIBER`)
- `user` — phone, linkedin, github, portfolioWebsite, city, province, country, summary, name
- `educations`, `workExperiences`, `skills`, `awards`, `certifications`

`DatabaseSeeder` order:

```
authSeeder.seedRoles();
permissionSeeder.seedPermissions();
authSeeder.seedUser("admin@test.com", "0000");
authSeeder.seedUser("abtahitajwar@gmail.com", "0000", "SUBSCRIBER", "Abtahi", "Tajwar");
profileSeeder.seedDemoProfile();
```

Skip-if-exists: if the user already has rows in a submodule table, that section is not inserted again. Missing user email fails the seed.

There is no projects table. Project highlights from a resume belong in `user.summary` or a work-experience description.

## Dependencies

- `user` module: `UserEntity`, `UserRepository`
- Auth seeding must create the subscriber before this seeder runs
