const jsonRoom = { roomID: '', token: '', userID: '' }

exports.json_room = jsonRoom

exports.createJSON = function (roomID, token, userID) {
  jsonRoom.roomID = roomID
  jsonRoom.token = token
  jsonRoom.userID = userID
  return jsonRoom
}
