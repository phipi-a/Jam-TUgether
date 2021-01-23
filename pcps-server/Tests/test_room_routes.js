const chai = require('chai')
const chaiHttp = require('chai-http')
const expect = chai.expect
const should = chai.should()
chai.use(chaiHttp)
const RoomSchema = require('../model/room.model')
const app = require('../server')

// Test routes
// const requester = chai.request(app).keepOpen()

describe('JamTUgether Tests', () => {
  beforeEach((done) => {
    // Empty database
    RoomSchema.deleteMany({}, (err) => {
      if (err) console.log('ERROR: ' + err)
      done()
    })
  })

  /**
   * Test create-room
   */
  describe('POST /create-room', () => {
    it('should return status 201', (done) => {
      chai.request(app)
        .post('/api/create-room')
        .send({
          "password": "1234"
        })
        .end((err, res) => {
          res.should.have.status(201)
          res.body.should.be.a('json')
          res.body.should.have.property('roomID')
          res.body.should.have.property('token')
          res.body.should.have.property('userID')
          err.should.be.null
        })
      done()
    })
    it('should return status 413', (done) => { // password too long
      chai.request(app)
        .post('/api/create-room')
        .send({
          "password": "LgNvPuV971aqfLO_rbWZfh2Eju47Xpx0I"
        })
        .end((err, res) => {
          res.should.have.status(413)
          res.body.should.be.a('json')
          // res.body.should.have.property('description').eql('Password too large.')
          err.should.be.null
        })
      done()
    })
  })
})
