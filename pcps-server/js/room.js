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
  res.status(200).json({ description: 'success!' })
}

exports.sendTracks = async function (req, res, room) {
  res.status(200).json({ roomID: room.roomID, soundtracks: room.soundtracks })
}
