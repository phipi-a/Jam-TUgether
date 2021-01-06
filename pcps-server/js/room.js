// DB schema
const mongoose = require('mongoose')
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')
const { createToken } = require('./auth.js')

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
  res.status(200).json({ soundtrackID: soundtracks._id })
}

exports.sendTracks = async function (req, res, room) {
  res.status(200).json({ roomID: room.roomID, soundtracks: room.soundtracks })
}

function prepareSoundtrack (soundtrack) {
  return {
    _id: mongoose.Types.ObjectId(),
    userID: soundtrack[0].userID,
    instrument: soundtrack[0].instrument,
    soundSequence: soundtrack[0].soundSequence
  }
}
// controls if newAdmin is needed
exports.checkAdmin = async function (adminTime, roomID) {
  // cast current Date - 1,5 minutes (90000 ms) to Date
  const time = new Date(Date.now() - 90000)
  const flag = adminTime <= time
  // flag true means new Admin is needed
  if (flag) {
    // update last access of admin
    const newDate = new Date(Date.now())
    await RoomSchema.updateOne({ roomID: roomID }, { lastAccessAdmin: newDate }).exec()
    const token = await createToken('Admin', roomID)
    console.log(token)
    return { description: '', flag: flag, token: token }
  }
  return { description: '', flag: flag }
}
