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

exports.deleteTracks = async function (req, res, roomID) {
  const query = { roomID: roomID }
  
  const soundTracks = await RoomSchema.find(query, {_id: 0, soundtracks: 1})
  const s = soundTracks[0].soundtracks;

  s.forEach(element => {
    if(element.userID == req.body.userID && element.instrument == req.body.instrument && element.number == req.body.number){
      element.soundSequence = []
    }
  });

  const updateDocument = {
    $set: {
      soundtracks: s
    }
  }

  await RoomSchema.updateMany(query, updateDocument)

  res.status(200).json({ description: 'track deleted'})
}