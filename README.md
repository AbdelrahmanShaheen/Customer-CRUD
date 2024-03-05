# Customer CRUD API

## Description

Customer CRUD restful api where you can create ,update ,delete and get a customer.

## Features ✨

- Customer CRUD Operations
- Flyway for db migration
- Unit and Integration testing
- Containarize the app using `jib`
- Ensure proper validation and error handling is implemented for the API endpoints.
- Well-organized and well-documented code (Clean code).
- Used MVC Architecture

## About the database layer :
- You can query the database using `Spring data jpa` or `Spring JDBC Template` by switching the DAO implementation in the service layer   
-  Note: I can directly use the `CustomerRepository` that implements `JpaRepository` but for learning purposes i made the dao layer to query the db in different ways. 


## More about Unit & Integration testing :
- `JUnit5` and `mockito` for unit testing
- `Testcontainers` for testing data access layer. It provides throwaway, lightweight instances of databases.
- `WebTestClient` for integrations testing. It is an HTTP client designed for testing server applications. 

## More about Containarizing the app using `jib`
- Containarize the app and push it into docker hub. Here is the [Image-link](https://hub.docker.com/repository/docker/shaheenabdelrahman/customercrud-api/general)
- To use the app in your machine i made a `docker-compose.yml` file. Just run this command in the dir that contains the file :
```bash
> docker-compose up
```
- About  [jib](https://github.com/GoogleContainerTools/jib)
- How to use & configure jib with maven blugin [link](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)
## ToDo


- [ ] Deploy on AWS

- [ ] Continuos Integration

- [ ] Continous Delivery

