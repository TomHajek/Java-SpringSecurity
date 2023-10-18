# Spring Boot 3.0 Security with JWT Implementation
This is a Spring Boot 3.0 demo project to demonstrate security implementation
using json web tokens (JWT).


## Technologies
* Spring Boot 3.0
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven


## Features
* User registration and login with JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling
* Logout mechanism
* Refresh token


## What is JWT?
JWT, or JSON Web Token, is an open standard used to share security information 
between two parties — a client and a server. Each JWT contains encoded JSON objects, 
including a set of claims. JWTs are signed using a cryptographic algorithm to ensure 
that the claims cannot be altered after the token is issued.


## How JWT works?
JWTs differ from other web tokens in that they contain a set of claims. Claims are used 
to transmit information between two parties. What these claims are depends on the use 
case at hand. For example, a claim may assert who issued the token, how long it is valid 
for, or what permissions the client has been granted.

A JWT is a string made up of three parts, separated by dots (.), and serialized using base64. 
In the most common serialization format, compact serialization, the JWT looks something like 
this: <b>xxxxx.yyyyy.zzzzz</b>.

Once decoded, you will get two JSON strings:

1. The header and the payload.
2. The signature.

The JOSE (JSON Object Signing and Encryption) header contains the type of token — JWT in this 
case — and the signing algorithm.

The payload contains the claims. This is displayed as a JSON string, usually containing no more 
than a dozen fields to keep the JWT compact. This information is typically used by the server to 
verify that the user has permission to perform the action they are requesting.

There are no mandatory claims for a JWT, but overlaying standards may make claims mandatory. For 
example, when using JWT as bearer access token under OAuth2.0, iss, sub, aud, and exp must be 
present, some are more common than others.

The signature ensures that the token hasn’t been altered. The party that creates the JWT signs the 
header and payload with a secret that is known to both the issuer and receiver, or with a private 
key known only to the sender. When the token is used, the receiving party verifies that the header 
and payload match the signature.

![jwt.png](src%2Fmain%2Fresources%2Fstatic%2Fjwt.png)


## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Maven 3+

To build and run the project, follow these steps:

* Clone the repository
* Navigate to the project directory
* Add database "jwt_security" to postgres
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8080.

