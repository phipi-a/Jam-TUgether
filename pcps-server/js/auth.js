const jwt = require('jsonwebtoken')

exports.createToken = function (room) {
  /* expires after half an hour (1800 seconds = 30 minutes) */
  return jwt.sign(room, process.env.ACCESS_TOKEN_SECRET, { expiresIn: '1800s' })
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
