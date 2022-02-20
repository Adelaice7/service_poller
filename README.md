# Service Poller - Assignment

## Description
This is a small Full Stack application that polls a list of given services as defined by URLs. 
It uses Java with the Spring framework, and a simple interface on the frontend to interact with the API. The database is MySQL.

### Notes
- The MySQL server is running on port `3309` outside of the container.
- User: `dev` Password: `secret`

## Run the application
1. Build application jar using command `./gradlew bootJar`
2. Start up application with Docker using `docker-compose up --build`

## REST API Endpoints
The application provides multiple REST API endpoints to interact with the database.

### Service endpoints
1. `GET /users/services` Gets all the services from the database.
2. `GET /users/{userId}/services` Gets the services of a given user based on the provided `userId`
3. `POST /users/{userId}/services/add` Adds the `service` provided in the request body to the user of `userId`
4. `PUT /users/services/{id}` Updates the service given by `id` with the `updatedService` provided in the request body. 
5. `DELETE /users/services/{id}` Deletes service of given `id`

### User endpoints
1. `GET /users` Gets the list of all users. There is an optional request parameter `username` to find a given user.
2. `GET /users/{id}` Gets the user of given `id`
3. `POST /users/add` Adds a user to the database based on the `user` provided in the request body
4. `PUT /users/{id}` Updates a user of `id` with the user provided in the response body
5. `DELETE /users/{id}` Deletes a user of `id`
6. `DELETE /users/deleteAll` Deletes all users

### Poller endpoints
1. `GET /poll/{id}` Polls service of given `id`.

## GUI
I have decided to go with a basic user interface without any particular frameworks.
