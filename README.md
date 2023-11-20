# Spring Boot 3.0 Security with Spring MVC and Thymeleaf
This is a Spring Boot 3.0 demo project to demonstrate basic security implementation
using Spring Security, Spring MVC and Thymeleaf.


## Getting Started
To get started with this project, you will need to have the following installed on your local
machine:

* JDK 17+
* Maven 3+

To build and run the project, follow these steps:

* Clone the repository (jwt branch)
* Navigate to the project directory
* Add database "spring_security" to postgres
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8080.


## Technologies
* Spring Boot 3.0
* Spring Security
* Spring MVC
* BCrypt
* Maven
* Thymeleaf


## Features
* User registration and login
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Logout mechanism
* Remember me functionality
* Forgot password
* One time password (OTP)
* Two-factor authentication (2FA/MFA)


## Authentication / Authorization
<b>Authentication</b> verifies, who are you saying you are. In other words, it let you in the
application. Method:
* login form
* http authentication
* custom authentication method

<b>Authorization </b> is when everybody has access to application. But decides if you have
permission to access particular resource. Method:
* access control urls
* access control list (acl)

### Basic authorization

![basic_auth.png](src%2Fmain%2Fresources%2Fstatic%2Fbasic_auth.png)


## Remember Me
In Spring Security, the "remember me" functionality is often implemented using a cookie. The purpose of the "remember 
me" feature is to allow users to remain authenticated even after they close and reopen their browsers. When a user 
selects the "remember me" option during login, a cookie is typically set on their browser with a token or some 
identifier that allows the application to recognize and automatically authenticate the user in subsequent sessions.

The cookie contains information that helps the application remember the user's identity and authentication details. 
This way, the user can bypass the regular login process during subsequent visits until the "remember me" token expires 
or is revoked.

### Simple hash-based token approach
This approach uses hashing to achieve a useful remember-me strategy. In essence, a cookie is sent to the browser upon 
successful interactive authentication, with the cookie being composed as follows:

    base64(username + ":" + expirationTime + ":" + algorithmName + ":"
    algorithmHex(username + ":" + expirationTime + ":" password + ":" + key))
    
    username:          As identifiable to the UserDetailsService
    password:          That matches the one in the retrieved UserDetails
    expirationTime:    The date and time when the remember-me token expires, expressed in milliseconds
    key:               A private key to prevent modification of the remember-me token
    algorithmName:     The algorithm used to generate and to verify the remember-me token signature

The remember-me token is valid only for the period specified, and only if the username, password, and key do not change. 
Notably, this has a potential security issue, in that a captured remember-me token is usable from any user agent until 
such time as the token expires. This is the same issue as with digest authentication. If a principal is aware that 
a token has been captured, they can easily change their password and immediately invalidate all remember-me tokens on 
issue. If more significant security is needed, you should use the approach described in the next section. Alternatively, 
remember-me services should not be used at all.

### Persistent token approach
Essentially, the username is not included in the cookie, to prevent exposing a valid login name unnecessarily. To use 
this approach with namespace configuration, the database should contain a persistent_logins table, created by using the 
following SQL (or equivalent):

    create table persistent_logins (
        username varchar(64) not null,
        series varchar(64) primary key,
        token varchar(64) not null,
        last_used timestamp not null
    )

Series identifier:
* This identifies the initial login of a user and remains consistent each time the user is automatically logged in
  with the remember-me cookie.

Token value:
* A unique value that changes each time a user is authenticated using the remember-me cookie.

## Forgot password
The 'Forgot Password' feature is a crucial component of web applications, enabling users to regain access to their 
accounts in the event of forgotten passwords.

This functionality is implemented by generating a unique, one-time-use token with a limited validity period. This token 
is then securely delivered to the user's email address. Upon clicking the provided URL containing the token, the user is 
seamlessly redirected to a dedicated page where they can securely set up a new password for their account. This process 
ensures a secure and user-friendly means of password recovery, enhancing the overall experience for users who may 
encounter access issues due to forgotten credentials.


## One Time Password
It is a single-use password or code that is valid for only one login session or transaction, on a computer system or 
other digital device. It is often sent to the user's mobile device or email and is typically valid for a short period 
of time. The idea is that even if the password is intercepted, it won't be useful for future logins.

OTP can also be one of the factors in a 2FA or MFA system (typically falling under the "something you have" category).

## Two (Multi) Factor Authentication
These terms refer to the use of two or more independent factors to authenticate a user. These factors fall into three 
main categories:

* Something you know: This could be a password, PIN, or answers to security questions.
* Something you have: This involves a physical device, such as a smartphone or a hardware token, which generates or 
receives codes (like OTP) or serves as a second form of authentication.
* Something you are: This involves biometric factors such as fingerprints, retina scans, or voice recognition
