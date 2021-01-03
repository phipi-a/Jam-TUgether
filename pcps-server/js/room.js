// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// simple push of DB array
exports.receiveTrack = async function (req, res, roomID) {
  const query = { roomID: roomID }
  const updateDocument = {
    $push: {
      soundtracks: req.body.soundtracks
    }
  }
  await RoomSchema.updateMany(query, updateDocument)
  res.send('success!')
}

exports.sendTracks = async function (req, res) {
  const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()
  res.status(200).json({ roomID: req.body.roomID, soundtracks: room.soundtracks })
}
