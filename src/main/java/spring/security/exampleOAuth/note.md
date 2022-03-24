OAuth is only for Authorization between services -> access delegation

Terms
--------

Term 1: resource             -> protected resource

Term 2: resource owner       -> an entity capable of granting access to a protected resource

Term 3: resource server

Term 4: client               -> an application making protected resource request on behalf of 
                                the resource owner and with its authorization

Term 5: authorization server -> the server issuing access tokens to the client


OAuth flow
-----------

1. Authorization code flow 
   1. user (resource owner) ask photo printing service to print photos
   2. photo printing service ask authorization server to get access to google drive 
   3. authorization server asks resource owner 
   4. resource owner replies to authorization server 
   5. authorization server provides auth token to photo printing service 
   6. photo printing service makes api call to google drive 
   7. google drive validate token and provides the resource
 
2. Implicit flow (simplified, less secure) -> short-lived access token -> used with js apps
   1. user (resource owner) ask photo printing service to print photos 
   2. photo printing service ask authorization server to get access to google drive 
   3. authorization server asks resource owner 
   4. resource owner replies to authorization server 
   5. authorization server sends auth token directly to photo printing service
   6. photo printing service access to google drive
   7. google drive provides the resource

3. Client credentials flow -> well trusted (confidential clients) -> (auth between microservices)
   1. service1 call auth server
   2. auth server provides token to service1
   3. service1 calls service2 to get access
   4. service2 gives service1 access


To implement login with Facebook / Github / Google etc.. you have to go to fb developer page and 
create an application. In your developer account, you will have clientId and clientDeveloperKey, 
which has to be put inside application.yml, make sure you don´t share this file containing secret 
key in git!
