const jwt = require('jsonwebtoken')
const crypto = require('crypto')

// define Error for too large password
const PwErr = new Error('too large')
exports.PwErr = PwErr

exports.checkPwdLen = function (password) {
  if (password.length > 30) {
    throw PwErr
  }
}

exports.createToken = function (permission, roomID) {
  // random bytes
  const rndBytes = crypto.randomBytes(10).toString('hex')
  // For expires after half an hour (1800 s = 30 min)
  return jwt.sign({ rndmPayload: '' + rndBytes, room: roomID, role: permission }, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '1800s' }) + ''
}

function decodeToken (token) {
  const decoded = jwt.decode(token, { json: true })
  console.log(decoded)
  return decoded
}

exports.verifyAdmin = function (req, res, next) {
  const decodedToken = decodeToken(getToken(req, res))
  if (!decodedToken) {
    res.status(401).send('Decoding problems')
  }
  if (decodedToken.role !== 'Admin' || decodedToken.room !== req.body.roomID) {
    return res.status(403).send('Not Admin of this room!')
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
