/**
 * Module dependencies.
 */

const express = require('express')
const bodyParser = require('body-parser')
const path = require('path')
const bcrypt = require('bcrypt')

const app = express()
app.use(bodyParser.urlencoded({ extended: true }))

const rooms = []

app.get('/signup', function (req, res) {
  const htmlPath = __dirname + '' + '/signup.html'
  res.sendFile(path.join(htmlPath))
})

app.post('/signup', async function (req, res) {
  try {
    const salt = await bcrypt.genSalt()
    const hashedPassword = await bcrypt.hash(req.body.password, salt)
    const newRoom = { room: req.body.room, password: hashedPassword }
    if (rooms.includes(newRoom)) {
      return res.send('Room already exists!')
    }
    rooms.push(newRoom)
    // just for debugging
    console.log(newRoom)
    res.status(201).send()
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
  console.log(rooms)
  console.log(newRoom)
  if (newRoom == null) {
    return res.status(400).send('Cannot find room')
  }
  try {
    if (await bcrypt.compare(req.body.password, newRoom.password)) {
      res.send('Success login!')
    } else {
      res.send('Wrong password!')
    }
  } catch {
    res.status(500).send()
  }
})

app.listen(3000, function () {
  console.log('Server started on port 3000')
})
// config
