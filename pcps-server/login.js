/**
 * Module dependencies.
 */
/* Load token config from dotenv */
require('dotenv').config()

const express = require('express')
const bodyParser = require('body-parser')
const path = require('path')
const bcrypt = require('bcrypt')
const { createToken, verify } = require('./auth.js')
const { receiveTrack, sendTracks } = require('./room.js')

const app = express()

app.use(bodyParser.urlencoded({ extended: true }))

const rooms = []

app.get('/signup', function (req, res) {
  const htmlPath = __dirname + '' + '/signup.html'
  res.sendFile(path.join(htmlPath))
})

app.post('/signup', async function (req, res) {
  /* encrypts passwords */
  try {
    const salt = await bcrypt.genSalt()
    const hashedPassword = await bcrypt.hash(req.body.password, salt)
    const newRoom = { room: req.body.room, password: hashedPassword }
    if (rooms.map(x => x.room).includes(newRoom.room)) {
      return res.send('Room already exists!')
    }
    rooms.push(newRoom)
    const token = createToken(newRoom)
    res.json(token)
    res.redirect('/room')
  } catch {
    res.status(500).send()
  }
})

app.get('/login', function (req, res) {
  const htmlPath = __dirname + '' + '/login.html'
  res.sendFile(htmlPath)
})

app.post('/login', async function (req, res) {
  const newRoom = rooms.find(newRoom => newRoom.room === req.body.room)
  /* Checks if newRoom is valid and if valid compares it with dummy database (timing attacks resistant) */
  if (newRoom == null) {
    return res.redirect('/login')
  }
  try {
    if (await bcrypt.compare(req.body.password, newRoom.password)) {
      res.redirect('/room')
    } else {
      /* TODO error message like 'wrong password' */
      res.redirect('/login')
    }
  } catch {
    res.status(500).send()
  }
})

app.get('/room', verify, function (req, res) {
  sendTracks(res)
})
app.post('/room', verify, function (req, res) {
  receiveTrack(req, res)
})

app.listen(3000, function () {
  console.log('Server started on port 3000')
})
// config
