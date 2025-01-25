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
   ./mvnw spring-boot:run
   ```

4. **Access the Application**
   - Open your browser and navigate to `http://localhost:8080`.

## Roadmap

- Add support for additional data sources such as Fangraphs, Baseball Reference, and Baseball Savant.
- Enhance data visualization features.
- Implement advanced filtering and analytics tools.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
