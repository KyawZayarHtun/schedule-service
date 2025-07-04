# Quartz Scheduler with Spring Boot and PostgreSQL (Multi-Cluster)

This project provides a robust and scalable job scheduling solution built with Spring Boot and Quartz, utilizing PostgreSQL as the persistent store. It's designed to operate effectively in a multi-cluster environment, ensuring high availability and fault tolerance for your scheduled tasks.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Database Setup](#database-setup)
    - [Application Configuration](#application-configuration)
    - [Building and Running](#building-and-running)
- [API Endpoints](#api-endpoints)
    - [Schedule Management](#schedule-management)
    - [Job Management](#job-management)
    - [Trigger Management](#trigger-management)
    - [Job Execution History](#job-execution-history)
- [Multi-Cluster Considerations](#multi-cluster-considerations)
- [Contributing](#contributing)
- [License](#license)

## Features

* **Job Scheduling:** Create and manage various types of schedules (simple and cron-based).
* **Job Management:** Pause, resume, and delete scheduled jobs.
* **Trigger Management:** Pause, resume, and delete individual triggers associated with jobs.
* **Job Execution Tracking:** Monitor the history of job executions, including misfired and failed attempts.
* **RESTful API:** Interact with the scheduler via well-defined REST endpoints.
* **Swagger/OpenAPI Documentation:** Automatically generated API documentation for easy consumption.
* **Multi-Cluster Support:** Designed to work seamlessly across multiple instances for high availability.
* **PostgreSQL Persistence:** Utilizes PostgreSQL to store job, trigger, and history data, ensuring data durability across restarts and instances.

## Technologies Used

* **Spring Boot:** Framework for building robust, production-ready Spring applications.
* **Quartz Scheduler:** A powerful, open-source job scheduling library.
* **PostgreSQL:** A powerful, open-source object-relational database system.
* **Flyway:** Database migration tool for managing database schema changes.
* **Lombok:** Reduces boilerplate code for Java classes.
* **Swagger UI / OpenAPI 3:** For API documentation and testing.
* **Maven:** Build automation tool.

## Getting Started

Follow these instructions to set up and run the Quartz Scheduler application locally.

### Prerequisites

Before you begin, ensure you have the following installed:

* **Java Development Kit (JDK) 17 or higher**
* **Maven 3.6.0 or higher**
* **PostgreSQL database server**
* **Git** (optional, for cloning the repository)

### Database Setup

1.  **Create a PostgreSQL Database:**
    You'll need a dedicated database for Quartz to store its information. You can create one using `psql` or a GUI tool like DBeaver.

    ```sql
    CREATE DATABASE quartz_scheduler;
    ```

2.  **Database User (Optional but Recommended):**
    Create a dedicated user for your application with appropriate permissions for the `quartz_scheduler` database.

    ```sql
    CREATE USER quartz_user WITH PASSWORD 'your_password';
    GRANT ALL PRIVILEGES ON DATABASE quartz_scheduler TO quartz_user;
    ```

3.  **Flyway Migrations:**
    This project uses Flyway to manage database schema migrations, including the Quartz tables. Upon application startup, Flyway will automatically apply any pending migrations found in `src/main/resources/db/migration`. Ensure your Quartz schema scripts (e.g., from the Quartz distribution) are placed as Flyway migration files (e.g., `V1__quartz_tables.sql`, `V2__initial_schema.sql`).

    **Note:** You might need to disable Hibernate's DDL auto-generation in `application.properties` when using Flyway to prevent conflicts.

### Application Configuration

1.  **Clone the Repository (if not already done):**

    ```bash
    git clone https://github.com/KyawZayarHtun/schedule-service.git
    cd schedule-service
    ```

2.  **Configure `application.yml`:**
    Open `src/main/resources/application.properties` (or `application.yml`) and update the database connection details to match your PostgreSQL setup.

    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/quartz_scheduler
    spring.datasource.username=quartz_user
    spring.datasource.password=your_password
    spring.datasource.driver-class-name=org.postgresql.Driver

    # JPA/Hibernate Configuration (for Quartz persistence)
    spring.jpa.hibernate.ddl-auto=none # Important: Set to 'none' when using Flyway for schema management
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    # Flyway Configuration
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:db/migration # Default location for Flyway scripts

    # Quartz Configuration
    # These are basic configurations; you might need more for multi-cluster setup
    # Refer to Quartz documentation for advanced clustering properties
    spring.quartz.job-store-type=jdbc
    spring.quartz.jdbc.initialize-schema=never # Let Flyway manage schema
    spring.quartz.properties.org.quartz.jobStore.isClustered=true
    spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval=20000
    spring.quartz.properties.org.quartz.jobStore.tablePrefix=QRTZ_
    spring.quartz.properties.org.quartz.scheduler.instanceId=AUTO
    ```

    **Important for Multi-Cluster:** The `spring.quartz.properties.org.quartz.jobStore.isClustered=true` line is crucial for enabling Quartz clustering. You may need to add more advanced Quartz properties for robust multi-cluster operation, such as `org.quartz.jobStore.selectWithLockSQL` for specific database locking strategies. Refer to the official Quartz documentation for detailed clustering configurations.

### Building and Running

1.  **Build the Project:**

    ```bash
    mvn clean install
    ```

2.  **Run the Application:**

    ```bash
    java -jar target/schedule-service-1.0.0.jar
    ```

    Alternatively, if you're in an IDE like IntelliJ IDEA or Eclipse, you can run the `main` method of your Spring Boot application.

The application will start on port 8080 by default (or as configured in `application.properties`).

## API Endpoints

The API documentation can be accessed via Swagger UI at `http://localhost:8080/swagger-ui.html` once the application is running.

Here's a summary of the available endpoints:

### Schedule Management (`/schedules`)

* **GET `/schedules`**
    * **Description:** Retrieve a list of all schedules in the scheduler.
    * **Response:** `200 OK` with a list of `ScheduleInfoDTO` objects.

* **POST `/schedules/simple`**
    * **Description:** Create a new simple schedule.
    * **Request Body:** `ScheduleRequest` (requires `jobName`, `jobGroup`, `triggerDataMap`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Job not found), `400 Bad Request` (Invalid parameters).

* **POST `/schedules/cron`**
    * **Description:** Create a new cron schedule.
    * **Request Body:** `ScheduleRequest` (requires `jobName`, `jobGroup`, `triggerDataMap`, `cronExpression`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Job not found), `400 Bad Request` (Invalid parameters, Invalid Cron Expression).

### Job Management (`/jobs`)

* **GET `/jobs`**
    * **Description:** Retrieve a list of all defined jobs in the scheduler.
    * **Response:** `200 OK` with a list of `JobDetailDto` objects.

* **GET `/jobs/groups`**
    * **Description:** Retrieve a list of all job groups.
    * **Response:** `200 OK` with a set of job group names.

* **GET `/jobs/names-by-group/{group}`**
    * **Description:** Retrieve a list of job names for a specific job group.
    * **Path Variable:** `group` (String) - The name of the job group.
    * **Response:** `200 OK` with a set of job names.

* **GET `/jobs/parameters/{group}/{name}`**
    * **Description:** Retrieve a list of necessary parameters for a specific job.
    * **Path Variables:** `name` (String) - The job name, `group` (String) - The job group.
    * **Response:** `200 OK` with a list of `JobDataParameter` objects.
    * **Errors:** `404 Not Found` (Job not found).

* **POST `/jobs/pause`**
    * **Description:** Pause a specific job by its name and group.
    * **Request Body:** `JobIdentifier` (requires `name`, `group`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Job not found), `500 Internal Server Error`.

* **POST `/jobs/resume`**
    * **Description:** Resume a specific job by its name and group.
    * **Request Body:** `JobIdentifier` (requires `name`, `group`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Job not found), `500 Internal Server Error`.

* **DELETE `/jobs/delete/{group}?name={name}`**
    * **Description:** Delete a specific job by its name and group.
    * **Path Variable:** `group` (String) - The job group.
    * **Query Parameter:** `name` (String) - The job name.
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Job not found), `409 Conflict` (Job has associated triggers), `500 Internal Server Error`.

### Trigger Management (`/triggers`)

* **POST `/triggers/pause`**
    * **Description:** Pause a specific trigger by its name and group.
    * **Request Body:** `JobIdentifier` (requires `name`, `group`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Trigger not found), `500 Internal Server Error`.

* **POST `/triggers/resume`**
    * **Description:** Resume a specific trigger by its name and group.
    * **Request Body:** `JobIdentifier` (requires `name`, `group`).
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Trigger not found), `500 Internal Server Error`.

* **DELETE `/triggers/delete/{group}?name={name}`**
    * **Description:** Delete a specific trigger by its name and group.
    * **Path Variable:** `group` (String) - The trigger group.
    * **Query Parameter:** `name` (String) - The trigger name.
    * **Response:** `200 OK` with a success message.
    * **Errors:** `404 Not Found` (Trigger not found), `500 Internal Server Error`.

### Job Execution History (`/job-execution`)

* **GET `/job-execution/history/{jobName}/{jobGroup}`**
    * **Description:** Get the execution history of a job by its name and group.
    * **Path Variables:** `jobName` (String), `jobGroup` (String).
    * **Response:** `200 OK` with a list of `JobExecutionHistory` objects.

* **GET `/job-execution/misfired`**
    * **Description:** Get all misfired job execution records.
    * **Response:** `200 OK` with a list of `JobExecutionHistory` objects.

* **GET `/job-execution/failed/{jobName}/{jobGroup}`**
    * **Description:** Get all failed job execution records for a specific job.
    * **Path Variables:** `jobName` (String), `jobGroup` (String).
    * **Response:** `200 OK` with a list of `JobExecutionHistory` objects.

* **GET `/job-execution/history/range`**
    * **Description:** Get the execution history of jobs within a specified date range.
    * **Query Parameters:** `startDate` (LocalDateTime, ISO_DATE_TIME format), `endDate` (LocalDateTime, ISO_DATE_TIME format).
    * **Example:** `GET /job-execution/history/range?startDate=2023-01-01T00:00:00&endDate=2023-12-31T23:59:59`
    * **Response:** `200 OK` with a list of `JobExecutionHistory` objects.

## Multi-Cluster Considerations

This scheduler is designed to support multi-cluster deployments through Quartz's JDBC-JobStore clustering capabilities. Key aspects for a multi-cluster setup include:

* **Shared Database:** All instances of the scheduler application must connect to the same PostgreSQL database. This is how Quartz instances communicate and coordinate.
* **Quartz Clustering Configuration:** Ensure `spring.quartz.properties.org.quartz.jobStore.isClustered=true` is set. Further fine-tuning of Quartz properties (e.g., `clusterCheckinInterval`, `maxMisfireCount`, `selectWithLockSQL`) is essential for optimal performance and reliability in a clustered environment.
* **Unique Instance IDs:** Quartz uses `instanceId` to identify scheduler instances. `AUTO` is generally suitable for clustered environments, allowing Quartz to generate unique IDs.
* **Load Balancing:** Deploy multiple instances of this Spring Boot application behind a load balancer to distribute API requests and ensure high availability.
* **Distributed Locks:** Quartz uses database-level locks to manage job and trigger states in a clustered setup, preventing race conditions.
