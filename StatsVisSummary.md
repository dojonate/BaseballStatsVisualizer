# Baseball Stats Visualizer - Summary

## Architecture

- **Framework:** Spring Boot MVC
- **Persistence:** JPA (PostgreSQL in production, H2 for tests)
- **Templating:** Thymeleaf
- **Security:** HTTP Basic Authentication with a self-signed HTTPS certificate

## Security

- In-memory user: **maintainer**
- Upload endpoints secured via basic auth and HTTPS

## File Uploads

- **Events:** `POST /upload/events`
    - Accepts multipart event files containing multiple games
    - **Parsing:** Uses `EventFileParser` and `EventDetails`
        - Detects game boundaries via "id" records
        - Handles play tokens (e.g., special case for tokens like `F9LF`)
- **Rosters:** `POST /upload/rosters`
    - Processes `.ROS` files using `RosFileParser`
    - Persists roster entries
- **Teams:** `POST /upload/teams`
    - Accepts CSV files parsed by `CsvTeamParser`
    - Creates/updates team records

## Views (Thymeleaf)

- **Players, Teams, Rosters:** Paginated, sortable, and searchable tables
- **(Planned) Games:** Interfaces for listing games and detailed game views

## Domain Model

- **Entities:** `Game`, `Player`, `Team`, `PlayerEvent`, `RosterEntry`, `Site`
- **Enums:** `EventType`, `PitchType`, `GameType`, `Position`, Weather (FieldConditions, PrecipitationType, SkyType,
  WindDirection)
- **Services & Repositories:** Standard CRUD and business logic encapsulated in service layers
