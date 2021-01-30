# README

## Installation

* install node
* install npm
* install mongodb
* start mongodb
* change to the directory *pcps-server*
* type: ```npm i bcrypt express standard path nodemon dotenv jsonwebtoken mongoose swagger-jsdoc swagger-ui-express crypto --save``` in your terminal
* then type ```node configure-script.js``` in your terminal
# optional for testing
type: ```npm i chai chai-http mocha --save```
## Executing

* To start the server type: ```npm start```
# optional if test dependencies installed
To run tests: ```npm test```


## Example requests

```curl -H "Content-Type: application/json" -d '{"roomID":"4","password":"1234"}' http://localhost:3000/api/create-room/```
```curl -H "Content-Type: application/json" -d '{"roomID":"4","password":"1234"}' http://localhost:3000/api/login/```
