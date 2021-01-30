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
  res.status(200).json({ roomID: room.roomID, soundtracks: room.soundtracks , description: 'success' })
}

function prepareSoundtrack (soundtrack) {
  return {
    userID: soundtrack[0].userID,
    instrument: soundtrack[0].instrument,
    soundSequence: soundtrack[0].soundSequence,
    userName: soundtrack[0].userName,
    number: soundtrack[0].number
  }
}
// controls if newAdmin is needed
exports.checkAdmin = async function (adminTime, roomID) {
  // cast current Date - 1 minutes (60000 ms) to Date
  const time = new Date(Date.now() - 60000)
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

exports.deleteTracks = async function (req, res, roomID) {
  const query = { roomID: roomID }

  const soundTracks = await RoomSchema.find(query, { _id: 0, soundtracks: 1 })
  const s = soundTracks[0].soundtracks

  const result = s.filter(element => !(element.userID === req.body.userID && element.instrument === req.body.instrument && element.number === req.body.number))

  const updateDocument = {
    $set: {
      soundtracks: result
    }
  }

  await RoomSchema.updateMany(query, updateDocument)

  res.status(200).json({ description: 'success' })
}
