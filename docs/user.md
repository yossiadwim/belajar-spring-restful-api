# User API Spec

## Register User
- Endpoint : POST /api/users

Request Body : username, password
```json
{
  "username": "yossia",
  "password": "password",
  "name" : "Yossia Dwi Mahardika"
}
```

Response Body (Success) :
```json
{
   "data" : "OK" 
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username must not blank, ???"
}
```

## Login User
- Endpoint : POST /api/auth/login

Request Body : username, password
```json
{
  "username": "yossia",
  "password": "password"
}
```

Response Body (Success) :
```json
{
   "data" : "TOKEN",
  "expiredAt" : 1000000 // milliseconds
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username or password is wrong"
}
```

## Update User
- Endpoint : PATCH /api/users/current
Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body : username, password
```json
{
  "name" : "Yossia Dwi Mahardika", // if only want to update name
  "password" : "new assword" // if only want to update password
}
```

Response Body (Success) :
```json
{
   "data" : "OK" 
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username must not blank, ???"
}
```



## Get User
Endpoint : GET /api/users/current

Request Header:
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data" : {
    "username": "yossia",
    "name": "Yossia Dwi Mahardika"
  }

}
```

Response Body (Failed, 401) :
```json
{
  "errors" : "Unauthorized"
}
```


## Logout User
Endpoint : DELETE /api/auth/logout

Request Header:
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data" : "OK"
}
```