Spring security
---------------

Handles common vulnerabilities:

* session fixation
* clickjacking
* click site request forgery


Can do:

* username/password authentication
* sso/okta/ldap
* app level authorization
* intra app authorization like OAuth
* microservice security (tokens, jwt)
* method level security


5 spring security concepts:

* authentication
* authorization
* principal
* how does authorization happen? Granted Authority
* roles


Authentication - who is this user? Can he enter the building?
--------------
verifies you, who you say you are

* knowledge based auth = password, pin code, answer to a secret question
* posession based auth = phone/text message, key cards and badges, access token device
* multifactor auth = combination of multiple auth methods

Methods:
* Login form
* HTTP authentication
* Custom authentication method


Authorization - are you allowed to do this? Are you allowed to access this room?
-------------

decides if you have permission to access a resource

Methods:
* Access Control URLs
* Access Control List (ACLs)


Principal - currently logged in user
------------

one user can have multiple IDs, but there is usually just one logged-in user (per request)


How does authorization happen? - Granted Authority
------------

Authorities are mostly "fine grained" permissions.

Authorities vs. Roles

