const express = require('express')
const swaggerJsDoc = require('swagger-jsdoc')
const bcrypt = require('bcrypt')
const { checkPwdLen, createToken, verify, verifyAdmin, PwErr } = require('../js/auth.js')
const { jsonRoom, createJSON } = require('../js/prepareResponse.js')

const app = express()

// Inititate express route
const roomRoute = express.Router()

// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// Create room function
async function createRoom (roomID, password, object) {
  await RoomSchema.create({ roomID: roomID, password: password, adminBytes: 'non-existing' }, (error, data, next) => {
    if (error) {
      return next(error)
    }
    console.log('Created room with ID: ' + roomID)
    return roomID
  })
}

// Update "updated" to current time
async function updateRoom (roomID) {
  const newDate = new Date(Date.now())
  const room = RoomSchema.updateOne({ roomID: roomID }, { updated: newDate }).exec()
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
 *         description: roomID + JWToken
 *       500:
 *         description: Failure
 */
roomRoute.post('/create-room', async (req, res, next) => {
  try {
    // Check if the number of rooms is below limit (limit: 10)
    const numberOfRooms = await RoomSchema.countDocuments().exec()
    if ((Number(numberOfRooms) + 1) > 10) {
      throw new Error('Limit for number of rooms is reached')
    }

    // Check password, limit to n characters
    checkPwdLen(req.body.password, res)

    // Create salt and hash password
    const salt = await bcrypt.genSalt()
    req.body.password = await bcrypt.hash(req.body.password, salt)

    // Generate roomID
    let newRoomID = 0
    console.log('numberofrooms = ' + numberOfRooms)
    if (numberOfRooms === 0) {
      newRoomID = 1
      // Create db entry
      await createRoom(newRoomID, req.body.password)
    } else {
      // default value
      newRoomID = numberOfRooms + 1
      // Returns all _id and roomID values from db sorted
      const allIndexes = await RoomSchema.find({}, { roomID: 1 }).sort({ roomID: 'asc' })
      // Find free roomID
      for (let i = 0; i < numberOfRooms; i++) {
        if (Number(allIndexes[i]['roomID']) > i + 1) {
          newRoomID = i + 1
          break
        }
      }
      // Create db entry
      await createRoom(newRoomID, req.body.password)
    }
    // sleep for 0.1 milisecond
    await new Promise(resolve => setTimeout(resolve, 0.1))
    const token = await createToken('Admin', newRoomID)
    res.status(201).send(createJSON(newRoomID.toString(), token))
  } catch (err) {
    if (err === PwErr) {
      res.status(413).send('Password too large.')
    } else {
      console.log(err)
      res.status(500).send('Couldn\'t create room.')
    }
  }
})

/**
 * @openapi
 * /api/delete-room:
 *   post:
 *     summary: Delete existing room.
 *     parameters:
 *       - in: body
 *         name: room
 *         description: Room to delete.
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
 *       408:
 *          description: expired admin, Sends new token
 *       413:
 *         description: Password too long
 *       500:
 *         description: Failure
 */
roomRoute.delete('/room', verify, verifyAdmin, async (req, res) => {
  try {
    // check if room to delete exists
    const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()

    if (!room) {
      return res.status(401).send('No room with matching roomId found')
    }
    // Check pw length
    checkPwdLen(req.body.password, res)
    // compare passwords and delete room
    if (await bcrypt.compare(req.body.password, room.password)) {
      RoomSchema.deleteOne({ roomID: req.body.roomID }, (err, obj) => {
        if (err) {
          res.status(501).send(' Error, cannot delete room')
        }
        res.status(200).json({ description: 'Deleted room' })
      })
    } else {
      res.status(401).send('Wrong Password.')
    }
  } catch (err) {
    if (err === PwErr) {
      res.status(413).send('Password too large.')
    } else {
      res.status(500).send('Couldn\'t delete room.')
    }
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
 *         description: Success + JWToken
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
    console.log(req.body.roomID, room)

    if (!room) {
      return res.status(401).send('No room with matching roomId found')
    }
    checkPwdLen(req.body.password, res)
    if (await bcrypt.compare(req.body.password, room.password)) {
      await updateRoom(req.body.roomID)
      const token = await createToken('User', req.body.roomID)
      res.status(201).send(createJSON(req.body.roomID.toString(), token))
    } else {
      res.status(401).send('Wrong Password.')
    }
  } catch (err) {
    if (err === PwErr) {
      res.status(413).send('Password too large.')
    } else {
      res.status(500).send('Couldn\'t create room.')
    }
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
    await updateRoom(req.params.id)
    res.status(200).send(room.track)
  }
})
/**
 * @openapi
 * /api/test:
 *   post:
 *     summary:
 *      Test request, simulates posting.
 *     parameters:
 *       - in: body
 *         name: test
 *         description: Send a Test Post.
 *         schema:
 *           type: object
 *           required:
 *             - JWToken in header field 'Authorization'
 *     responses:
 *       200:
 *         description: 'test'
 *       403:
 *         description: Forbidden (invalid token)
 */
roomRoute.post('/test', verify, async (req, res) => {
  res.status(200).send('blub')
})

roomRoute.post('/test2', async (req, res) => {
  updateRoom(req.body.roomID)
  res.status(200).send('test2')
})

module.exports = roomRoute
