# Service Poller - Assignment

### Notes
- The MySQL server is running on port `3309` outside of the container. User: `dev` Password: `secret`.
- I ended up not including pre-existing test data in the database, but this can be easily added over the REST API or 
the GUI.

## Run the application
1. Build application jar using command `./gradlew build bootJar`
2. Start up application with Docker using `docker-compose up --build`

The application can also be run by using command `./gradlew build bootRun` 
but a MySQL 5.7 database instance has to be running on port `3309`.

## Description
This is a small Full Stack application that polls a list of given services as defined by URLs.
It uses Java with the Spring framework, and a simple interface on the frontend to interact with the API.
The database is MySQL.

I used Spring Security for authenticating the user, implementing the authentication to use the database as a data source.
I use the Security features in Spring and Thymeleaf to connect the user authentication to the front end.
There are two models that I created: the User and the Service models.
Services are tied to users, as in a User owns a set of Services.
A user can register, then gets stored in the database, and log in to see their services. Services can be added 
to each user through the REST API or the GUI. There are endpoints and GUI options to edit and remove services.
Since services belong to users, along with the user authentication, there is support for multiple users.

The added service URLs are checked to match a URL format (which can be limiting) and to check if they are possibly valid.
The validity check is done by ensuring there is an HTTP response.

The poller uses the Java 11 Http Client to send HTTP requests to the service URLs. The response is `OK` if 
the status code is within the `2xx` range, and `FAIL {statuscode}` if not. Using these values, each given service is updated.

The services are stored in the database, and with having set the `spring.jpa.hibernate.ddl-auto=true` option in the
`application.properties` configuration file for Spring, I made sure they are kept upon server restart.

## REST API Endpoints
The application provides multiple REST API endpoints to interact with the database.

### Service endpoints
1. `GET /users/services` Gets all the services from the database.
2. `GET /users/{userId}/services` Gets the services of a given user based on the provided `userId`
3. `POST /users/{userId}/services/add` Adds the `service` provided in the request body to the user of `userId`
4. `PUT /users/services/{id}` Updates the service given by `id` with the `updatedService` provided in the request body. 
5. `DELETE /users/services/{id}` Deletes service of given `id`.
6. `DELETE /users/{userId}/services` Deletes all services of given user based on `userId`.

### User endpoints
1. `GET /users` Gets the list of all users. There is an optional request parameter `username` to find a given user.
2. `GET /users/{id}` Gets the user of given `id`
3. `POST /users/add` Adds a user to the database based on the `user` provided in the request body
4. `PUT /users/{id}` Updates a user of `id` with the user provided in the response body
5. `DELETE /users/{id}` Deletes a user of `id`
6. `DELETE /users/deleteAll` Deletes all users

### Poller endpoints
1. `GET /poll/{id}` Polls service of given `id`.
2. `POST /poll/{id}` Polls service of given `id` and saves it to the database.
3. `GET /pollServices` Polls all services.

## GUI / Frontend
I decided do a basic GUI without any particular frameworks, so I used HTML, CSS and JavaScript with JQuery. 
I used NPM to provide some packages easier. I also set up the Gradle build process to have webpack
create the packaged JS file. My reason for choosing the JQuery library was that it was commonly 
used along Thymeleaf and Spring templates, and I wanted to end up with shorter code compared to vanilla JS.

I used Bootstrap for some easy, basic styling, and also the plugin `DataTables` for rendering the Services table.
The original request upon loading the page uses an AJAX request, but the majority of the requests are handled by `axios`.
DataTables allows the user to sort the list of services according to any columns by clicking on the column headers.

The added services get separately polled, but globally, all services are polled every 60 seconds.

There are quite a few templates I ended up using for the GUI. The `index` contains the services when the user is logged in.
There are also templates for a login page and a registration page respectively. The `header` is stored separately.

## Ideas to improve

- I would love to use React JS to give a proper frontend to the application with prettier results.

- Some asynchronous implementations need to be realized in order to handle the unexpected behaviors of the service polling.

- Thymeleaf templates could be improved by the templates and fragments being called  in the controllers,
thus requiring less HTML templates.

- Less mixing of Spring/Thymeleaf based behavior in the frontend.

- There might be some issues with starting up the DB for the first time, and the app initializing sooner than the DB.
