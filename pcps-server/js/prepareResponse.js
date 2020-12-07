const jsonRoom = { roomID: '', token: '' }

exports.json_room = jsonRoom

exports.createJSON = function (roomID, token) {
  jsonRoom.roomID = roomID
  jsonRoom.token = token
  return jsonRoom
}
