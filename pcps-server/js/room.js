const tracks = []

exports.receiveTrack = function (req, res) {
  const track = { instrument: req.instrument, pitch: req.pitch, startTime: req.startTime }
  tracks.push(track)
  console.log(req.body.instrument)
  res.send('success!')
}

exports.sendTracks = function (res) {
  res.json(tracks)
}
