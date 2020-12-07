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

exports.createToken = function () {
  // random bytes
  const rndBytes = crypto.randomBytes(10).toString('hex')
  /* expires after half an hour (1800 seconds = 30 minutes) */
  return jwt.sign(rndBytes, process.env.ACCESS_TOKEN_SECRET, {}) + ''
}

exports.verify = function (req, res, next) {
  // Gather the jwt access token from the request header
  const authHeader = req.headers['authorization']
  const token = authHeader && authHeader.split(' ')[1]
  if (token == null) return res.sendStatus(401) // if there isn't any token

  jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, id) => {
    if (err) return res.sendStatus(403)
    req.id = id
    next()
  })
}
