const express = require('express')
const swaggerJsDoc = require('swagger-jsdoc')
const bcrypt = require('bcrypt')
const { checkPwdLen, createToken, verify, verifyAdmin, PwErr, whoAmI } = require('../js/auth.js')
const { jsonRoom, createJSON } = require('../js/prepareResponse.js')
const { receiveTrack, sendTracks, checkAdmin, deleteTracks, setBeat } = require('../js/room.js')
const { fillRoom } = require('../js/prepareRoom.js')

const app = express()

// Inititate express route
const roomRoute = express.Router()

// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// New errors
const ROOM_LIMIT_ERROR = new Error('Limit for number of rooms is reached!')

// Create room function
async function createRoom (roomID, password, object) {
  await RoomSchema.create(fillRoom(roomID, password), (error, data, next) => {
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
  await RoomSchema.updateOne({ roomID: roomID }, { updated: newDate }).exec()
}
// Update "lastAccessAdmin" to current time
async function updateAdminAccess (roomID) {
  const newDate = new Date(Date.now())
  await RoomSchema.updateOne({ roomID: roomID }, { lastAccessAdmin: newDate }).exec()
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
 *       503:
 *         description: Limit for number of rooms reached
 *       500:
 *         description: Failure
 */
roomRoute.post('/create-room', async (req, res, next) => {
  try {
    // Check if the number of rooms is below limit (limit: 10) TODO TESTING 50
    const numberOfRooms = await RoomSchema.countDocuments().exec()
    if ((Number(numberOfRooms) + 1) > 500) {
      throw ROOM_LIMIT_ERROR
    }
    // Check password, limit to n characters
    checkPwdLen(req.body.password, res)
    // Create salt and hash password
    const salt = await bcrypt.genSalt()
    req.body.password = await bcrypt.hash(req.body.password, salt)
    // Generate roomID
    let newRoomID = 0
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
    const room = await RoomSchema.findOne({ roomID: newRoomID }).exec()
    const token = await createToken('Admin', newRoomID, room._id)
    const userID = 1
    res.status(201).send(createJSON(newRoomID.toString(), token, userID.toString()))
  } catch (err) {
    if (err === PwErr) {
      res.status(413).json({ description: 'Password too large.' })
    } else if (err === ROOM_LIMIT_ERROR) {
      res.status(503).json({ description: 'Limit for number of rooms is reached.' })
    } else {
      console.log(err)
      res.status(500).json({ description: 'Couldn\'t create room.' })
    }
  }
})

/**
 * @openapi
 * /api/room:
 *   delete:
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
      return res.status(401).json({ description: 'No room with matching roomId found' })
    }
    // Check pw length
    checkPwdLen(req.body.password, res)
    // compare passwords and delete room
    if (await bcrypt.compare(req.body.password, room.password)) {
      RoomSchema.deleteOne({ roomID: req.body.roomID }, (err, obj) => {
        if (err) {
          res.status(501).json({ description: 'Error, cannot delete room' })
        }
        res.status(200).json({ description: 'Deleted room' })
      })
    } else {
      res.status(401).json({ description: 'Wrong Password.' })
    }
  } catch (err) {
    if (err === PwErr) {
      res.status(413).json({ description: 'Password too large.' })
    } else {
      res.status(500).json({ description: 'Couldn\'t delete room.' })
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
 *       410:
 *         description: Wrong roomID
 *       500:
 *         description: Failure
 */
roomRoute.post('/login', async (req, res) => {
  // Compare passwords
  try {
    // Search room with roomID in database
    const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()

    if (!room) {
      return res.status(410).json({ description: 'No room with matching roomId found' })
    }
    checkPwdLen(req.body.password, res)
    if (await bcrypt.compare(req.body.password, room.password)) {
      await updateRoom(req.body.roomID)
      const token = await createToken('User', req.body.roomID, room._id)
      // update number of user
      const userID = room.numberOfUser + 1
      const update = { numberOfUser: userID }
      await room.updateOne(update)
      // create default sound for new user
      // await room.updateOne({ $push: { soundtracks: { userID: userID, soundseq: [], volume: 1 } } })
      res.status(201).send(createJSON(req.body.roomID.toString(), token, userID.toString()))
    } else {
      res.status(401).json({ description: 'Wrong Password.' })
    }
  } catch (err) {
    if (err === PwErr) {
      res.status(413).json({ description: 'Password too large.' })
    } else {
      res.status(500).json({ description: 'Couldn\'t log in room.' })
    }
  }
})

/**
 * @openapi
 * paths:
 *   /api/room/:id:
 *     get:
 *       summary: Get tracks of room
 *       parameters:
 *         - in: path
 *           name: id
 *           schema:
 *               type: integer
 *           required: true
 *           description: id of room to get tracks from
 *       responses:
 *         200:
 *           description: object containing roomID, soundtracks, description returned
 *           schema:
 *             type: object
 *             properties:
 *               roomID:
 *                 type: number
 *                 example: 1
 *               soundtracks:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   userID:
 *                     type: number
 *                     example: 1
 *                   instrument:
 *                     type: string
 *                     example: flute
 *                   number:
 *                     type: number
 *                     example: 1
 *                   soundsequence:
 *                     type: array
 *                     items:
 *                       type: object
 *                       properties:
 *                         starttime:
 *                           type: number
 *                           example: 4
 *                         endtime:
 *                           type: endtime
 *                           example: 12
 *                         pitch:
 *                           type: number
 *                           example: 23
 *               description:
 *                 type: string
 *                 example: success
 *         410:
 *           description: room does not exist
 *           schema:
 *             type: object
 *             properties:
 *               description:
 *                 type: string
 *                 example: Room does not exist!
 */
roomRoute.get('/room/:id', verify, async (req, res) => {
  const room = await RoomSchema.findOne({ roomID: req.params.id }).exec()
  if (room == null) {
    res.status(410).json({ description: 'Room does not exist!' })
  } else {
    await updateRoom(req.params.id)
    sendTracks(req, res, room)
  }
})

/**
 * @openapi
 * /api/room/:id:
 *   post:
 *     summary: Sends tracks to room
 *     consumes:
 *       -application/json
 *     parameters:
 *       - in: body
 *         name: soundtracks
 *         description: soundtracks to send to room
 *         schema:
 *           type: object
 *           required:
 *             - soundtracks
 *           properties:
 *             soundtracks:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   userID:
 *                     type: number
 *                     example: 1
 *                   instrument:
 *                     type: string
 *                     example: flute
 *                   number:
 *                     type: number
 *                     example: 1
 *                   soundsequence:
 *                     type: array
 *                     items:
 *                       type: object
 *                       properties:
 *                         starttime:
 *                           type: number
 *                           example: 4
 *                         endtime:
 *                           type: endtime
 *                           example: 12
 *                         pitch:
 *                           type: number
 *                           example: 23
 *     responses:
 *       200:
 *         description: soundtracks successfully sent to server
 *         schema:
 *           type: object
 *           properties:
 *             roomID:
 *               type: number
 *             description:
 *               type: string
 *               example: success
 *       410:
 *         description: room does not exist
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: Room does not exist!
 */
roomRoute.post('/room/:id', verify, async (req, res) => {
  const room = await RoomSchema.findOne({ roomID: req.params.id }).exec()
  if (room == null) {
    res.status(410).json({ description: 'Room does not exist!' })
  } else {
    await updateRoom(req.params.id)
    receiveTrack(req, res, req.params.id)
  }
})

/**
 * @openapi
 * /api/room/:id:
 *   delete:
 *     summary: Deletes specified track
 *     description: Returns "success" if specified track has been deleted
 *     consumes:
 *       -application/json
 *     parameters:
 *       - in: body
 *         name: specifyTrack
 *         description: specifies which track to delete
 *         schema:
 *           type: object
 *           required:
 *             - roomID
 *             - userID
 *             - instrument
 *             - number
 *           properties:
 *             roomID:
 *               type: number
 *               example: 1
 *             userID:
 *               type: number
 *               example: 1
 *             instrument:
 *               type: string
 *               example: flute
 *             number:
 *               type: number
 *               example: 1
 *     responses:
 *       200:
 *         description: room is deleted
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: success
 *       410:
 *         description: room does not exist
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: Room does not exist!
 */
roomRoute.delete('/room/:id', verify, async (req, res) => {
  const room = await RoomSchema.findOne({ roomID: req.params.id }).exec()
  if (room == null) {
    res.status(410).json({ description: 'Room does not exist!' })
  } else {
    await updateRoom(req.params.id)
    deleteTracks(req, res, req.params.id)
  }
})

/**
 * @openapi
 * /api/room/:id/admin:
 *   get:
 *     summary: Returns "Admin" if admin else it returns "not Admin" with flag= true for new admin token or "not Admin" with flag = false
 *     description: Checks if user is Admin and if new Admin is needed. In case Admin is needed sends new Admin token
 *     consumes:
 *       - application/json
 *     parameters:
 *       - in: body
 *         name: roomID
 *         description: Room ID
 *         schema:
 *           type: object
 *           required:
 *             - roomID
 *           properties:
 *             roomID:
 *               type: number
 *     responses:
 *       200:
 *         description: if flag true than token else no token and description Admin
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: new Admin
 *             flag:
 *               type: boolean
 *             token:
 *               type: string
 *               description: Success
 *
 *       202:
 *         description: Not Admin
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: Not Admin
 *             flag:
 *               type: boolean
 *               example: false
 *               description: Success
 *       500:
 *         description: Failure
 */

roomRoute.get('/room/:id/admin', verify, async (req, res) => {
  const room = await RoomSchema.findOne({ roomID: req.params.id }).exec()
  if (room == null) {
    res.status(410).json({ description: 'Room does not exist!' })
  }
  await updateRoom(room.roomID)
  const priviliges = await whoAmI(req, res, room)
  console.log(priviliges)
  if (priviliges === 'Admin') {
    await updateAdminAccess(room.roomID)
    const answer = { description: priviliges }
    res.status(202).json(answer)
  } else {
    const answer = await checkAdmin(room.lastAccessAdmin, room.roomID)
    answer.description = answer.flag ? 'new Admin' : 'Not Admin'
    res.status(200).json(answer)
  }
})
/**
 * @openapi
 * /api/room/:id/admin:
 *   delete:
 *     summary: Admin leaves room.
 *     consumes:
 *       - application/json
 *     parameters:
 *       - in: body
 *         name: roomID
 *         description: Room ID
 *         schema:
 *           type: object
 *           required:
 *             - roomID
 *           properties:
 *             roomID:
 *               type: number
 *     responses:
 *       200:
 *         description: Admin left successfully
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: Success
 *               description: Success
 *       500:
 *         description: Failure
 */
roomRoute.delete('/room/:id/admin', verifyAdmin, async (req, res) => {
  // update last access of admin to high
  const newDate = new Date(Date.now() - 18000000)
  await RoomSchema.updateOne({ roomID: req.body.roomID }, { lastAccessAdmin: newDate }).exec()
  res.status(200).json({ description: 'Success' })
}
)
/**
 * @openapi
 * /api/:id/beat:
 *   post:
 *     summary: Updates room beat (only Admin).
 *     consumes:
 *       - application/json
 *     parameters:
 *       - in: body
 *         name: roomID
 *         description: Room ID
 *         schema:
 *           type: object
 *           required:
 *             - roomID
 *             - beat
 *           properties:
 *             roomID:
 *               type: number
 *             beat:
 *               type: object
 *               properties:
 *                 ticksPerTact:
 *                   type: number
 *                 tempo:
 *                   type: number
 *     responses:
 *       200:
 *         description: Successfully updated room's beat
 *         schema:
 *           type: object
 *           properties:
 *             description:
 *               type: string
 *               example: Success
 *       403:
 *         description: Not Admin of this room
 *       408:
 *         description: Old Admin, access denied
 */
roomRoute.post('/room/:id/beat', verifyAdmin, async (req, res) => {
  setBeat(req, res)
}
)

module.exports = roomRoute
