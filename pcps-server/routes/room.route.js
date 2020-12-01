const express = require('express')
const swaggerJsDoc = require('swagger-jsdoc')
const bcrypt = require('bcrypt')
const { createToken, verify } = require('../js/auth.js')

const app = express()

// Inititate express route
const roomRoute = express.Router()

// DB schema
const RoomSchema = require('../model/room.model')

/**
 * @openapi
 * /api/create-room:
 *   post:
 *     description: Create a new room
 *     parameters:
 *       - name: roomID
 *         description: RoomID to use for login
 *         in: json
 *         required: true
 *         type: integer
 *       - name: password
 *         description: Room's password
 *         in: json
 *         required: true
 *         type: string
 *     responses:
 *       200:
 *         description: Success
 *       500:
 *         description: Failure
 */
roomRoute.post('/create-room', async (req, res, next) => {
  try {
    // Check if room already exists
    if (await RoomSchema.findOne({ roomID: req.body.roomID }).exec() != null) {
      res.status(500).send('Room already exists')
      return
    }
    // Create salt and hash password
    const salt = await bcrypt.genSalt()
    // console.log(req.body)
    req.body.password = await bcrypt.hash(req.body.password, salt)
    // Create db entry
    RoomSchema.create(req.body, (error, data) => {
      if (error) {
        return next(error)
      } else {
        res.json(data)
      }
    })
    // const token = createToken()
  } catch {
    res.status(500).send('Couldn\'t create room.')
  }
})

roomRoute.post('/test', async (req, res) => {
  res.status(200).send('test')
})

/**
 * @openapi
 * /api/login:
 *   post:
 *     description: Login to existing room
 *     parameters:
 *       - name: roomID
 *         description: RoomID to use for login
 *         in: json
 *         required: true
 *         type: integer
 *       - name: password
 *         description: Room's password
 *         in: json
 *         required: true
 *         type: string
 *     responses:
 *       200:
 *         description: Success
 *       401:
 *         description: Wrong password or rommID
 *       500:
 *         description: Failure
 */
roomRoute.post('/login', async (req, res) => {
  // Compare passwords
  try {
    // Search room with roomID in database
    // console.log(req.body.roomID)
    const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()

    if (!room) {
      return res.status(401).send('No room with matching roomId found')
    }
    if (await bcrypt.compare(req.body.password, room.password)) {
      // TODO: token?
      res.status(200).send('Logged in successfuly')
    } else {
      res.status(401).send('Wrong Password.')
    }
  } catch {
    res.status(500).send('Something went wrong')
  }
})

module.exports = roomRoute
