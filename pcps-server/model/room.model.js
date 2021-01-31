const mongoose = require('mongoose')
const Schema = mongoose.Schema

const roomSchema = new Schema({
  roomID: Number,
  password: String,
  updated: { type: Date, default: Date.now },
  lastAccessAdmin: { type: Date, default: Date.now },
  adminBytes: String,
  numberOfUser: Number,
  beat: { ticksPerTact: { type: Number, default: 4 }, tempo: { type: Number, default: 60 } },
  soundtracks: [{ _id: false, userName: String, userID: Number, instrument: String, number: Number, soundSequence: [{ _id: false, starttime: Number, endtime: Number, pitch: Number }] }]
}, {
  collection: 'rooms'
})

module.exports = mongoose.model('RoomSchema', roomSchema)
