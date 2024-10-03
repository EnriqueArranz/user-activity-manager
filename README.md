# User Activity Manager

A Spring Boot application for managing user activities, built with Thymeleaf for server-side HTML rendering.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 5.7 or higher

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Database Setup

1. Create a MySQL database named `activity_managerdb`:
   ```sql
   CREATE DATABASE activity_managerdb;
   ```

### Installing

1. Clone the repo
   ```
   git clone https://github.com/yourusername/user-activity-manager.git
   ```
2. Navigate to the project directory
   ```
   cd user-activity-manager
   ```
3. Configure the application

   Update the `src/main/resources/application.properties` file with your database credentials:

   ```properties
   spring.application.name=User Activity Manager
   spring.servlet.multipart.enabled=true
   spring.datasource.url=jdbc:mysql://localhost:3306/activity_managerdb
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   logging.level.org.hibernate.SQL=DEBUG
   ```

   Note: Make sure to replace `root` with your actual MySQL username and password if they're different.

4. Build the project
   ```
   mvn clean install
   ```
5. Run the application
   ```
   mvn spring-boot:run
   ```

## Usage

The User Activity Manager application will start running at `http://localhost:8080`. You can access and interact with the application using a web browser.

1. Open your preferred web browser (e.g., Chrome, Firefox, Safari)
2. Navigate to `http://localhost:8080`
3. You should see the home page of the User Activity Manager

The application uses Thymeleaf for server-side rendering of HTML pages, providing a user-friendly interface for managing user activities. You can navigate through different pages and perform various operations directly in your browser.

## Initial Data

On first run, the application automatically generates 10 sample records using Faker. This allows you to immediately interact with the application and explore its features without needing to input data manually.
The application uses Thymeleaf for server-side rendering of HTML pages, providing a user-friendly interface for managing user activities. You can navigate through different pages and perform various operations directly in your browser.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Thymeleaf](https://www.thymeleaf.org/) - Server-side Java template engine
* [Maven](https://maven.apache.org/) - Dependency Management
* [MySQL](https://www.mysql.com/) - Database

## Authors

* **Enrique Arranz**  [YourGithubUsername](https://github.com/EnriqueArranz)

## License

Almost all rights reserved.


