const chai = require('chai')
const chaiHttp = require('chai-http')
const should = chai.should()
chai.use(chaiHttp)

const app = require('../server.js')
const RoomSchema = require('../model/room.model')

const track = {
  soundtracks: [{
    userID: 1,
    instrument: "retinstrument123",
    number: 1,
    soundSequence: [{
      starttime: 4, endtime: 12, pitch: 23
    }]
  }]
}

const deleteTrack = {
  roomID: 1,
  userID: 1,
  instrument: "retinstrument123",
  number: 1
}

describe('Test send-tracks, receive-tracks and delete-tracks', () => {
  before( function (done) {
    // Empty database
    RoomSchema.deleteMany({}, (err) => {
      if (err) console.log('ERROR: ' + err)
      done()
    })
  })

  it('Should create room, send track to room, receive the track again and finally delete it', function (done) {
    chai.request(app)
      .post('/api/create-room')
      .send({ password: '123' })
      .end((err, res) => {
        if (err) console.log(err)

        const apiEndpoint = '/api/room/' + res.body.roomID
        const admToken = res.body.token

        chai.request(app)
          .post(apiEndpoint)
          .set({ Authorization: `Bearer ${admToken}` })
          .send(track)
          .end((err, res) => {
            if (err) {
              console.log(err)
            }
            res.should.have.status(200)
            res.body.should.be.a('object')
            chai.request(app)
              .get(apiEndpoint)
              .set({ Authorization: `Bearer ${admToken}` })
              .end((err, res) => {
                if (err) {
                  console.log(err)
                }
                res.should.have.status(200)
                res.body.should.be.a('object')
                res.body.description.should.be.eql('success')
                chai.request(app)
                  .delete(apiEndpoint)
                  .set({ Authorization: `Bearer ${admToken}` })
                  .send(deleteTrack)
                  .end((err, res) => {
                    if (err) console.log(err)
                    res.should.have.status(200)
                    res.body.should.be.a('object')
                    res.body.description.should.be.eql('success')
                    done()
                  })
              })
          })
      })
  })

  it('should send and receive track', function (done) {
    chai.request(app)
      .post('/api/create-room')
      .send({ password: '123' })
      .end((err, res) => {
        if (err) console.log(err)

        const apiEndpoint = '/api/room/' + res.body.roomID
        const admToken = res.body.token

        chai.request(app)
          .post(apiEndpoint)
          .set({ Authorization: `Bearer ${admToken}` })
          .send(track)
          .end((err, res) => {
            if (err) {
              console.log(err)
            }
            res.should.have.status(200)
            res.body.should.be.a('object')

            chai.request(app)
              .get(apiEndpoint)
              .set({ Authorization: `Bearer ${admToken}` })
              .end((err, res) => {
                if (err) {
                  console.log(err)
                }
                res.should.have.status(200)
                res.body.should.be.a('object')
                res.body.description.should.be.eql('success')
                done()
              })
          })
      })
  })

  it('should send and delete track', function (done) {
    chai.request(app)
      .post('/api/create-room')
      .send({ password: '123' })
      .end((err, res) => {
        if (err) console.log(err)

        const apiEndpoint = '/api/room/' + res.body.roomID
        const admToken = res.body.token

        chai.request(app)
          .post(apiEndpoint)
          .set({ Authorization: `Bearer ${admToken}` })
          .send(track)
          .end((err, res) => {
            if (err) {
              console.log(err)
            }
            res.should.have.status(200)
            res.body.should.be.a('object')

            chai.request(app)
              .delete(apiEndpoint)
              .set({ Authorization: `Bearer ${admToken}` })
              .send(deleteTrack)
              .end((err, res) => {
                if (err) console.log(err)
                res.should.have.status(200)
                res.body.should.be.a('object')
                res.body.description.should.be.eql('success')
                done()
              })
          })
      })
  })
})
