-> javaguides

Implementing the Remember-Me Feature
---------

Spring Security provides the Remember-Me feature so that applications can remember the identity of a user between sessions. To use the Remember-Me functionality, you just need to send the HTTP parameter remember-me.

        <input type="checkbox" name="remember-me"> Remember Me

Spring Security provides the following two implementations of the Remember-Me feature out-of-the-box:

* Simple hash-based token as a cookie —This approach creates a token by hashing the user identity information and setting it as a cookie on the client browser.
* Persistent token —This approach uses a persistent store like a relational database to store the tokens.

In this tutorial, we will use the Persistent token approach.


Persistent Tokens
---------------

We will implement Spring Security Remember-Me feature, which can be used to store the generated tokens in persistent storage such as a database. The persistent tokens approach is implemented using org.springframework.security.web.authentication.rememberme. PersistentTokenBasedRememberMeServices, which internally uses the PersistentTokenRepository interface to store the tokens. Spring provides the following two implementations of PersistentTokenRepository out-of-the-box.

* InMemoryTokenRepositoryImpl can be used to store tokens in-memory (not recommended for production use).
* JdbcTokenRepositoryImpl can be used to store tokens in a database.

The JdbcTokenRepositoryImpl stores the tokens in the persistent_logins table.

persistent_logins table

    create table persistent_logins (
        username varchar(64) not null,
        series varchar(64) primary key,
        token varchar(64) not null,
        last_used timestamp not null
    );

Now that we have all the configuration ready, it’s time to create views using Thymeleaf.