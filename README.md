# Online Book Store

## Project Overview

Welcome to the **Online Book Store** project! This application is designed to simplify the process of buying books online. 
It addresses the challenges of finding and purchasing books by offering a user-friendly interface for both customers and administrators. 
Users can browse books, add them to the shopping cart, and place orders, while administrators manage the catalog of available books.

## Technologies Used

- **Spring Boot** - Backend framework for building the core application
- **Spring Security** - For managing authentication and authorization
- **Spring Data JPA** - For interacting with the database
- **MySQL** - Database to store book and user information
- **Docker** - Containerization for easy deployment
- **Swagger** - API documentation and testing
- **MapStruct** - Object mapping
- **Liquibase** - Database change management
- **JUnit & Mockito** - For unit testing
- **Testcontainers** - For integration testing with real databases

## Key Functionalities

### User Features:
- **Book Browsing**: Users can view a list of available books with details such as title, author, price, and category.
- **Cart Management**: Users can add books to their shopping cart, view items, and adjust quantities.
- **Order Placement**: Once the cart is filled, users can place orders.
  
### Admin Features:
- **Book Management**: Admins can add, update, or delete books from the store.
- **Order Management**: Admins can view and update status of orders.

## How to Run the Project

### Prerequisites:
- **Java 17** or higher
- **Maven** for dependency management
- **Docker** and **Docker Compose** for setting up the environment

### Steps to Launch Application:

1. Clone the repository:
    ```bash
    git clone git@github.com:your-username/spring_boot_project.git
    cd online-book-store
    ```

2. Set up the environment by creating a `.env` file with the following variables:
    ```
    MYSQLDB_DATABASE=<your_database_name>
    MYSQLDB_USER=<your_username>
    MYSQLDB_PASSWORD=<your_password>
    MYSQLDB_ROOT_PASSWORD=<your_root_password>
    
    SPRING_DATASOURCE_PORT=<your_spring_datasource_port>
    MYSQLDB_PORT=<your_mysql_port>
    
    SPRING_DATASOURCE_PORT=<your_spring_datasource_port>
    MYSQLDB_LOCAL_PORT=<your_local_port>
    MYSQLDB_DOCKER_PORT=<your_docker_port>
    SPRING_LOCAL_PORT=<your_spring_local_port>
    SPRING_DOCKER_PORT=<your_spring_docker_port>
    ```

3. Build and start the containers using Docker Compose:
    ```bash
    docker-compose up --build
    ```

4. The application will be accessible at `http://localhost:<YOUR_PORT>/api`.

### Running Tests:

1. To run unit and integration tests using Testcontainers, execute:
    ```bash
    mvn clean test
    ```

## API Documentation

Swagger UI is available for testing the API and is accessible at `http://localhost:8080/swagger-ui.html`. 
It includes endpoints for all available operations, such as browsing books, managing the cart, and handling orders.

## Challenges Faced

During the development process, I faced challenges in:

  1. Database Migration Issues:
Description: While using Liquibase for database change management, I faced migration problems where the application was unable to create the necessary tables.
Solution: I carefully reviewed the YAML migration scripts for errors and tested them in a local environment, enabling the successful application of changes and avoiding errors.

  2. Testing with Docker:
Description: Setting up testing for the application using Docker and Testcontainers proved to be a challenging task, especially when working with the database.
Solution: I studied the documentation and examples for Testcontainers, allowing me to properly configure containers for tests and ensure they ran in an isolated environment.
 
 3. Security Configuration:
Description: During the security setup, I faced issues with the Spring Security configuration, resulting in access errors.
Solution: I analyzed and modified the security configuration by adding the necessary filters and settings, which resolved the errors and ensured appropriate security levels.
  
## Postman Collection

## Postman Collection

I have provided a Postman collection that contains all the necessary API requests for testing and interacting with the Online Book Store application. 
This collection is stored in the `postman` folder in the root of the project.

### How to Use the Postman Collection:
1. **Locate the collection**: The Postman collection is located in the `postman` folder of the project.
   - File path: `./postman/postman_collection.json`
2. **Import into Postman**:
   - Open Postman.
   - Click on the "Import" button in the top-left corner.
   - Choose a file from the `postman` folder.
   - The collection will be added to your Postman workspace for testing the API.


This Postman collection simplifies testing by providing pre-configured requests for all core functionalities of the API. 
Just import the collection and you can immediately start sending requests!


---

This project was a great learning experience and showcases my ability to build a back-end of application with modern Java technologies. 
Feel free to explore and contribute!

Also I'm adding the video where I show how my application works:
https://www.loom.com/share/28f23fd3422e475cab6ccef5e6946d1c?sid=3e09aff2-5020-44a9-9b7b-5cdf1d5c4315
