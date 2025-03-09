# Contact API Spec

## Create Contact
Endpoint : POST /api/contacts

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
   "firstName" : "Yossia",
   "lastName" : "Dwi Mahardika",
   "email" : "yossia@me.com",
   "phone" : "08123456789"
}

```
Response Body (Success) :
```json

{
  "id" : "string random",
  "firstName" : "Yossia",
  "lastName" : "Dwi Mahardika",
  "email" : "yossia@me.com",
  "phone" : "08123456789"
}

```


Response Body (Failed) :
```json
{
   "errors" : "First Name must not blank, Last Name must not blank, Email must not blank, Phone must not blank"
}
```



## Update Contact
Endpoint : PUT /api/contacts/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
   "firstName" : "Yossia",
   "lastName" : "Dwi Mahardika",
   "email" : "yossia@me.com",
   "phone" : "08123456789"
}   
```

Response Body (Success) :
```json
{
   "firstName" : "Yossia",
   "lastName" : "Dwi Mahardika",
   "email" : "yossia@me.com",
   "phone" : "08123456789"
}   
```

Response Body (Failed) :
```json
{
   "errors" : "First Name must not blank, Last Name must not blank, Email must not blank, Phone must not blank"
}
```

## Get Contact
Endpoint : GET /api/contacts/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json

{
  "firstName" : "Yossia",
  "lastName" : "Dwi Mahardika",
  "email" : "yossia@me.com",
  "phone" : "08123456789"
} 

```

Response Body (Failed, 404) :
```json
{
   "errors" : "Contact not found"
}
```


## Search Contact
Endpoint : GET /api/contacts

Query :
- name : String, contact first name or last name, using like operator, optional
- email : String, contact email, using like operator, optional
- phone : String, contact phone, using like operator, optional
- page : Integer, page number start from 0 default 0, optional
- size : Integer, page size default 10, optional

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : [
    {
      "firstName" : "Yossia",
      "lastName" : "Dwi Mahardika",
      "email" : "yossia@me.com",
      "phone" : "08123456789"
    
    }
  ],
  "paging" : {
    "currentPage" : 1,
    "totalPage" : 1,
    "size" : 1
  }
}

```

Response Body (Failed) :
```json
{
   "errors" : "Contact not found"
}
```


## Remove Contact
Endpoint : DELETE /api/contacts/{idContact}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
   "errors" : "Contact not found"
}
```