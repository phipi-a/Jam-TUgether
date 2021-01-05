// DB schema
const mongoose = require('mongoose')
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// simple push of DB array
exports.receiveTrack = async function (req, res, roomID) {
  const query = { roomID: roomID }
  const soundtracks = prepareSoundtrack(req.body.soundtracks)
  const updateDocument = {
    $push: {
      soundtracks: soundtracks
    }
  }
  await RoomSchema.updateMany(query, updateDocument)
  res.send(soundtracks._id)
}

exports.sendTracks = async function (req, res, room) {
  res.status(200).json({ roomID: room.roomID, soundtracks: room.soundtracks })
}

function prepareSoundtrack (soundtrack) {
  return {
    _id: mongoose.Types.ObjectId(),
    userID: soundtrack.userID,
    instrument: soundtrack.instrument,
    soundSequence: soundtrack.soundSequence
  }
}
