const express = require('express')
const swaggerJsDoc = require('swagger-jsdoc')
const bcrypt = require('bcrypt')
const { checkPwdLen, createToken, verify } = require('../js/auth.js')

const app = express()

// Inititate express route
const roomRoute = express.Router()

// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// Create room function
async function createRoom (roomID, password) {
  RoomSchema.create({ roomID: roomID, password: password }, (error, data, next) => {
    if (error) {
      return next(error)
    } else {
      console.log('Created room with ID: ' + roomID)
      return roomID
    }
  })
}

/**
 * @openapi
 * /api/create-room:
 *   post:
 *     summary: Creates a new room.
 *     consumes:
 *       - application/json
 *     parameters:
 *       - in: body
 *         name: password
 *         description: Room's password
 *         schema:
 *           type: object
 *           required:
 *             - password
 *           properties:
 *             password:
 *               type: string
 *     responses:
 *       201:
 *         description: Created
 *       500:
 *         description: Failure
 */
roomRoute.post('/create-room', async (req, res, next) => {
  try {
    // TODO: check password, limit to n characters
    checkPwdLen(req.body.password)
    // Create salt and hash password
    const salt = await bcrypt.genSalt()
    req.body.password = await bcrypt.hash(req.body.password, salt)

    // Generate roomID
    let newRoomID = 0
    const numberOfRooms = await RoomSchema.countDocuments().exec()
    console.log('numberofrooms = ' + numberOfRooms)
    if (numberOfRooms === 0) {
      newRoomID = 1
      // Create db entry
      createRoom(newRoomID, req.body.password)
    } else {
      let size = 0
      // Find free roomID
      // Returns all _id and roomID values from db sorted
      await RoomSchema.find({}, { roomID: 1 }).sort({ roomID: 'asc' }).exec((_err, document) => {
        size = Object.keys(document).length
        for (const key in document) {
        // if a roomID is not used yet the new room gets that id
          if (Number(key) + 1 !== Number(document[key].roomID)) {
            newRoomID = Number(key) + 1
            break
          }
        }
        if (newRoomID === 0) {
          newRoomID = size + 1
        }
        // Create db entry
        createRoom(newRoomID, req.body.password)
      })
    }

    const token = createToken(newRoomID + '')
    //TODO save token for each user (privileges)

    // TODO send new room id to client
    res.status(201).send(newRoomID.toString() + '\n' + token)
  } catch {
    res.status(500).send('Couldn\'t create room.')
  }
})

roomRoute.post('/create-rooms', async (req, res, next) => {
  try {
    // TODO: check password, limit to n characters
    checkPwdLen(req.body.password)
    // Create salt and hash password
    let password = '1234'
    const salt = await bcrypt.genSalt()
    password = await bcrypt.hash(password, salt)

    await RoomSchema.deleteMany({}).exec()
    // Create db entries
    let i
    for (i = 1; i <= 4; i++) {
      await RoomSchema.create({ roomID: i, password: password })
    }
    console.log(await RoomSchema.find({}))
    res.status(201).send('Created 4 rooms.')
  } catch {
    res.status(500).send('Couldn\'t create room.')
  }
})

/**
 * @openapi
 * /api/login:
 *   post:
 *     summary: Login to existing room.
 *     parameters:
 *       - in: body
 *         name: room
 *         description: The room to login to.
 *         schema:
 *           type: object
 *           required:
 *             - roomID
 *             - password
 *           properties:
 *             roomID:
 *               type: integer
 *             password:
 *               type: string
 *     responses:
 *       200:
 *         description: Success
 *       401:
 *         description: Wrong password or roomID
 *       500:
 *         description: Failure
 */
roomRoute.post('/login', async (req, res) => {
  // Compare passwords
  try {
    // Search room with roomID in database
    const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()

    if (!room) {
      return res.status(401).send('No room with matching roomId found')
    }
    if (await bcrypt.compare(req.body.password, room.password)) {
      const token = createToken(req.body.roomID + '')
      res.status(200).send('Success ' + token)
    } else {
      res.status(401).send('Wrong Password.')
    }
  } catch {
    res.status(500).send('Something went wrong')
  }
})

/**
 * @openapi
 * /api/room/:id:
 *   get:
 *     summary: Returns track of room with RoomID id
 *     description: Returns track of room
 *     parameters:
 *       - name: id
 *         in: path
 *         required: true
 *         schema:
 *           type: integer
 *           minimum: 1
 *     responses:
 *       200:
 *         description: A JSON array of our data structure
 *       500:
 *         description: Failure
 */
roomRoute.get('/room/{id}', async (req, res) => {
  const room = await RoomSchema.findOne({ roomID: req.params.id }).exec()
  if (room == null) {
    res.status(500).send('Room does not exist!')
  } else {
    res.status(200).send(room.track)
  }
})

roomRoute.post('/test', verify, async (req, res) => {
  res.status(200).send('test')
})

roomRoute.post('/test2', verify, async (req, res) => {
  const numberOfRooms = await RoomSchema.count().exec()
  console.log(numberOfRooms)
  res.status(200).send('test2')
})

module.exports = roomRoute
