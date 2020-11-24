const path = require('path')

function startRoom () {
  const htmlPath = __dirname + '' + '/room.html'
  return path.join(htmlPath)
}
module.exports = { startRoom }
