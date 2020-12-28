const mongoose = require('mongoose')
const Schema = mongoose.Schema

const roomSchema = new Schema({
  roomID: Number,
  password: String,
  updated: { type: Date, default: Date.now },
  adminBytes: String,
  track: {
      
    // richtiges Format einfügen
    spur_eins: String,
    spur_zwei: String
  }
}, {
  collection: 'rooms'
})

module.exports = mongoose.model('RoomSchema', roomSchema)
