const jwt = require('jsonwebtoken')
const crypto = require('crypto')

// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// define Error for too large password
const PwErr = new Error('too large')
exports.PwErr = PwErr

exports.checkPwdLen = function (password) {
  if (password.length > 30) {
    throw PwErr
  }
}

exports.createToken = async function (permission, roomID) {
  // random bytes
  const rndBytes = crypto.randomBytes(10).toString('hex')
  // save random bytes if user is admin
  if (permission === 'Admin') {
    const room = await RoomSchema.findOne({ roomID: roomID }).exec()
    const update = { adminBytes: rndBytes }
    await room.updateOne(update)
  }
  // For expires after half an hour (1800 s = 30 min)
  return jwt.sign({ rndmPayload: '' + rndBytes, room: roomID, role: permission }, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '1800s' }) + ''
}

function decodeToken (token) {
  const decoded = jwt.decode(token, { json: true })
  return decoded
}

exports.verifyAdmin = async function (req, res, next) {
  const decodedToken = decodeToken(getToken(req, res))
  if (!decodedToken) {
    res.status(401).send('Decoding problems')
  }
  if (decodedToken.role !== 'Admin' || decodedToken.room.toString() !== req.body.roomID) {
    return res.status(403).send('Not Admin of this room!')
  }
  // prevent old Admin to access
  const room = await RoomSchema.findOne({ roomID: decodedToken.room }).exec()

  if (decodedToken.rndmPayload !== room.adminBytes) {
    // create new Token for old Admin
    const token = jwt.sign({ rndmPayload: '' + decodedToken.rndBytes, room: decodedToken.roomID, role: 'User' }, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '1800s' }) + ''
    return res.status(408).send('Old Admin new Token: ' + token)
  }
  next()
}
function getToken (req, res) {
  // Gather the jwt access token from the request header
  const authHeader = req.headers['authorization']
  const token = authHeader && authHeader.split(' ')[1]
  if (token == null) return res.sendStatus(401) // if there isn't any token
  return token
}
exports.verify = function (req, res, next) {
  const token = getToken(req, res)
  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, id) => {
    if (err) {
      return res.sendStatus(403)
    }
    req.id = id
    next()
  })
}
exports.whoAmI = async function (req, res, room) {
  const decoded = decodeToken(getToken(req, res))
  if (decoded.rndmPayload === room.adminBytes) {
    return { description: 'Admin' }
  }
  return { description: 'Not Admin' }
}
