const mongoose = require('mongoose')
const Schema = mongoose.Schema

const roomSchema = new Schema({
  roomID: Number,
  password: String,
  updated: { type: Date, default: Date.now },
  adminBytes: String,
  numberOfUser: Number,
  soundtracks: [{ userID: Number, instrument: String, soundSequence: [{ starttime: Number, endtime: Number, pitch: Number }] }]
}, {
  collection: 'rooms'
})

module.exports = mongoose.model('RoomSchema', roomSchema)
