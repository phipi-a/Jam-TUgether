// DB schema
const RoomSchema = require('../model/room.model')
const { exists } = require('../model/room.model')

// simple push of DB array
exports.receiveTrack = async function (req, res) {
  const query = { roomID: req.body.roomID, 'soundtracks.userID': req.body.userID }
  const updateDocument = {
    $push: {
      'soundtracks.$[].soundseq': { instrument: req.body.instrument, starttime: req.body.starttime, pitch: req.body.pitch }
    }
  }
  await RoomSchema.updateOne(query, updateDocument)
  res.send('success!')
}

exports.sendTracks = async function (req, res) {
  const room = await RoomSchema.findOne({ roomID: req.body.roomID }).exec()
  res.status(200).json({ roomID: req.body.roomID, soundtracks: room.soundtracks })
}
