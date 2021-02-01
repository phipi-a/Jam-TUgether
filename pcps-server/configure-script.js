const crypto = require('crypto')
const fs = require('fs')
const shell = require('shelljs')

const atsecret = crypto.randomBytes(16).toString('hex')

fs.writeFile('.env', 'ACCESS_TOKEN_SECRET=' + atsecret, function (err, data) {
  if (err) {
    return console.log('Creating enviroment file failed!')
  }
  return console.log(' Configuration was successfull')
})

shell.exec('openssl req -nodes -new -x509 -keyout server.key -out server.cert', (error, stdout, stderr) => {
  if (error) {
    console.log('failed to create server key')
    return
  }
  if (stderr) {
    console.log(' failed to create server key')
    return
  }
  console.log('Success!')
})
