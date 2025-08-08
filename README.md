# Baseball Stats Visualizer

Baseball Stats Visualizer is a Java Spring Boot application designed to manage, visualize, and analyze baseball statistics. This project uses real-world data from Retrosheet and stores it in a PostgreSQL database. The application allows the maintainer to upload team and roster data for further processing and analysis.

## Features

- **Data Import**
  - Upload team data in CSV format.
  - Upload player rosters in `.ROS` files.
- **Database Management**
  - Store and manage team, player, and roster data in a PostgreSQL database.
- **Authentication**
  - Basic authentication is configured for administrative endpoints.
- **User Interface**
  - Provides a web-based interface for browsing player stats, team data, and rosters.
  
## Project Structure

### Backend
- **Java Spring Boot Application**
  - Packages: `com.dojonate.statsvisualizer`
  - Controllers:
    - `TeamUploadController`: Handles uploading team data.
    - `RosterUploadController`: Handles uploading roster files.
  - Services:
    - `TeamService`: Handles team-related operations.
    - `RosterEntryService`: Manages roster data and relationships.
    - `PlayerService`: Manages player data.
  - Repositories:
    - `TeamRepository`
    - `RosterEntryRepository`
    - `PlayerRepository`
  - Utilities:
    - `CsvTeamParser`: Parses team CSV files.
    - `RosFileParser`: Parses `.ROS` roster files.
- **Security**
  - Configured with basic authentication for administrative tasks.

### Frontend
- **Thymeleaf Templates**
  - `index.html`: Homepage for the application.
  - `players.html`: Displays player statistics.

### Database
- **PostgreSQL**
  - Configured with Hibernate and JPA.
  - Stores data for teams, players, and rosters.

## Testing

- **Kotlin-based Tests**
  - Tests are written in Kotlin using JUnit and Spring Boot testing utilities.
  - Example test cases:
    - `TeamUploadControllerTest`: Verifies team CSV upload functionality.
    - `RosterUploadControllerTest`: Tests `.ROS` file upload and processing.
    - `TeamServiceTest`: Ensures team-related business logic works correctly.
    - `StatsVisualizerApplicationTests`: Validates application context loading.

## Configuration

### `application.properties`
```properties
spring.application.name=StatsVisualizer
spring.datasource.url=jdbc:postgresql://localhost:5432/baseball_stats
spring.datasource.username=bbnerd
spring.datasource.password=patriots,croaking,nesting
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
file.upload.temp-dir=/tmp
```

## How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-repo/baseball-stats-visualizer.git
   cd baseball-stats-visualizer
   ```

2. **Set up PostgreSQL**
   - Create a database named `baseball_stats`.
   - Update `application.properties` with your database credentials.

3. **Run the Application**
   ```bash
   ./gradlew bootRun
   ```

4. **Access the Application**
   - Open your browser and navigate to `http://localhost:8080`.

5. **Run Tests**
   ```bash
   ./gradlew test
   ```

## Roadmap

### Short-Term

- Add support for importing full play-by-play data from Retrosheet event files.
- Enhance data visualization with interactive charts and tables.
- Include core statistics calculations (e.g. OBP, SLG, AVG) and custom ones like “Hitting Average” (OBP minus walks).

### Mid-Term

- Expose a REST API for programmatic access to teams, players, and stats.
- Introduce role-based authentication and user accounts.
- Expand filtering and analytics tools for more advanced queries.
- Publish data-stream or API hooks to support external analytic modules.

### Long-Term

- Provide real-time game updates and live stat tracking.
- Deploy containerized environments and a public demo.
- Establish extension hooks so external modules can provide advanced projections and analysis.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
