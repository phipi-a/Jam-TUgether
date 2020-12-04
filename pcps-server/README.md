# README

## Installation

* install node
* install npm
* install mongodb
* start mongodb
* change to the directory *pcps-server*
* type: ```npm i bcrypt express standard path nodemon dotenv jsonwebtoken mongoose swagger-jsdoc swagger-ui-express --save``` in your terminal

## Executing

* To start the server type: ```npm start```

## Example requests

```curl -H "Content-Type: application/json" -d '{"roomID":"4","password":"1234"}' http://localhost:3000/api/create-room/```
```curl -H "Content-Type: application/json" -d '{"roomID":"4","password":"1234"}' http://localhost:3000/api/login/```