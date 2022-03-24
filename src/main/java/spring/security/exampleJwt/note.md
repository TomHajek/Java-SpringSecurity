This example shows how to implement json web token.


    User                             Server
            1. userId/password    ->    *
            2. authentication           *
            3. jwt                      *
     *  <-  4. jwt to client
     *      5. saved on client side 
             (local storage/cookie)
            6. jwt in http headeer ->   *
             (authorization:
                  Bearer + jwt)
           7. validate signature (jwt)  *