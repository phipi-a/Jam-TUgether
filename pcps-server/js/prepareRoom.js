// creates default JSON for new Room
exports.fillRoom = function (roomID, password) {
  const jsonRoom = {
    roomID: roomID,
    password: password,
    numberOfUser: 1,
    adminBytes: 'non-existing',
    soundtracks: []
  }
  return jsonRoom
}
