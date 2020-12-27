const crypto = require('crypto')
const fs = require('fs')

const atsecret = crypto.randomBytes(16).toString('hex')

fs.writeFile('.env', 'ACCESS_TOKEN_SECRET=' + atsecret, function (err, data) {
  if (err) {
    return console.log('Creating enviroment file failed!')
  }
  return console.log(' Configuration was successfull')
})
