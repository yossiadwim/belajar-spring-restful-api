# Address API Spec

## Create Address
Endpoint : POST /api/contacts/{idContact}/addresses

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "street" : "Nama jalan",
  "city" : "kota",
  "province" : "provinsi",
  "country" : "negara",
  "postalCode" : "1234"
}

```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "street" : "Nama jalan",
    "city" : "kota",
    "province" : "provinsi",
    "country" : "negara",
    "postalCode" : "1234"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address not found"
}
```

## Update Address
Endpoint : PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "street" : "Nama jalan",
  "city" : "kota",
  "province" : "provinsi",
  "country" : "negara",
  "postalCode" : "1234"
}

```

Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "street" : "Nama jalan",
    "city" : "kota",
    "province" : "provinsi",
    "country" : "negara",
    "postalCode" : "1234"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address not found"
}
```


## Get Address
Endpoint : GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "id" : "random string",
    "street" : "Nama jalan",
    "city" : "kota",
    "province" : "provinsi",
    "country" : "negara",
    "postalCode" : "1234"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Address not found"
}
```

## Remove Address
Endpoint : DELETE /api/contacts/{idContacts}/addresses/{idAddress}

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
  "errors" : "Address not found"
}
```

## List Address
Endpoint : GET /api/contacts/{idContacts}/addresses

Request Header:
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : [
    {
      "id" : "random string",
      "street" : "Nama jalan",
      "city" : "kota",
      "province" : "provinsi",
      "country" : "negara",
      "postalCode" : "1234"
    }
  ] 
}
```

Response Body (Failed) :

```json
{
  "errors" : "Contact not found"
}
```