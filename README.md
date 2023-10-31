# Spring Boot 3.0 Security with Keycloak Implementation
This is a Spring Boot 3.0 demo project to demonstrate security implementation
using Keycloak, Identity and Access Management tool.


## Getting Started
To get started with this project, you will need to have the following installed on your local
machine:

* JDK 17+
* Maven 3+
* Docker

To build and run the project, follow these steps:

* Clone the repository (jwt branch)
* Navigate to the project directory
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

Keycloak setup:

* Run docker-compose file
* Navigate to Keycloak Admin UI Console 
* Create a new Realm name or update the application.yml file to specify your Realm name 
* Create Roles 
* Create Users 
* Assign roles to users


## Technologies
* Spring Boot 3.0
* Spring Security
* Maven
* OAuth2 Resource Server
* Keycloak Server


## Features
* Single Sign-On (SSO) with OpenID Connect
* Role-Based Access Control (RBAC)
* Fine-Grained Authorization


## What is Keycloak?
Keycloak is an open-source software product to allow single sign-on with identity and access management aimed at modern 
applications and services. 

Keycloak home page: https://www.keycloak.org

### Keycloak Features
* Single Sign-On (SSO) and Single Logout
* Identity brokering and social login
* User federation (LDAP)
* Fine-grained authorization services
* Centralized management and admin console
* Client adapters
* Standards-based

### Advantages
* Open-source
* Versatility
* Scalability
* Security
* Customizability
* Ease of use

### Keycloak terms
<b>Realm</b>: 

* A way to isolate and manage a set of users, roles, clients and groups. It is essentially a security domain 
that manages a group of users, their credentials, roles and groups. It has its own dedicated settings.

<b>Clients</b>: 

* Represent applications that can interact with Keycloak for user authentication and authorization.
Client can be a web app, mobile app, or even other servers.
Each client is configured within a specific realm.

<b>Client scopes</b>: 

* Set of public, default, or optional client roles, permissions and claims that can be included in the
access token or ID token.

<b>Users</b>: 

* Individuals that can authenticate with Keycloak.
Each user belongs to a specific realm and can be assigned roles and groups.

<b>Groups</b>: 

* A way to manage common attributes and roles mapping for a set of users.
You can use groups to simplify the management of a large number of users,
as it is easier to manage roles and attributes for a group than for individual users.
