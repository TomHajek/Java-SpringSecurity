# Spring Boot 3.0 Security with JWT Implementation
This is a Spring Boot 3.0 demo project to demonstrate security implementation
using OAuth2.


## Getting Started
To get started with this project, you will need to have the following installed on your local
machine:

* JDK 17+
* Maven 3+

To build and run the project, follow these steps:

* Clone the repository (jwt branch)
* Navigate to the project directory
* Add database "jwt_security" to postgres
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run


## Technologies
* Spring Boot 3.0
* Spring Security
* Maven
* OAuth2


## Features
* User authorization (login) with OAuth2


## What is OAuth2?
OAuth stands for “Open Authorization.” It is a standard designed to allow a website or application to access 
resources hosted by other web apps on behalf of a user. Simply put, it is checking, if user exist in database of third 
party application like Github, Google etc.


## How OAuth2 authorization works?

![oauth2.png](src%2Fmain%2Fresources%2Fstatic%2Foauth2.png)


## OAuth2 example with Github:

![oauth-detailed](src/main/resources/static/oauth-detailed.png?raw=true "oauth-detailed")

* Red path describes for a full authentication and authorization process
* Green path describes getting a response from our backend


## OAuth2 vs. JWT:

* JWT is suitable for stateless applications, as it allows the application to authenticate users and authorize access to
  resources without maintaining a session state on the server.


* OAuth2, on the other hand, maintains a session state on the server and uses a unique token to grant access to the
  user's resources.


