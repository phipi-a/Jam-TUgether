/**
 * Module dependencies.
 */

const express = require("express");
const session = require('express-session');
const bodyParser = require("body-parser");

const app = express();
app.use(bodyParser.urlencoded({extended: true}));

var rooms = []

app.get("/signup", function(req, res){
    res.sendFile(__dirname + "/signup.html");
});

app.post("/signup", function(req, res){
    
    var newRoom = { room: req.body.room, password: req.body.password};
    console.log(newRoom);

    if(rooms.includes(newRoom)){
        res.send("Room already exists!");
    } else{
        rooms.push(newRoom);
        res.send("Signed up!")
    }
});

app.get("/login", function(req, res){
    res.sendFile(__dirname + "/login.html");
});

app.post("/login", function(req, res){
    var newRoom = { room: req.body.room, password: req.body.password};
    console.log(rooms);
    console.log(newRoom);
    var compare = rooms.filter(function(room){
                    if(room.room == newRoom.room && room.password == newRoom.password){
                        return true;
                    }
                });

    if(compare.length > 0){
        res.send("Logged In!")
    } else{
        res.send("Failed to log in!")
    }
});

app.listen(3000, function(){
    console.log("Server started on port 3000");
})
// config
