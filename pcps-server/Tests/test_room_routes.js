const chai = require('chai')
const chaiHttp = require('chai-http')
const expect = chai.expect
const should = chai.should()
chai.use(chaiHttp)
const RoomSchema = require('../model/room.model')
const app = require('../server')

// Test routes
// const requester = chai.request(app).keepOpen()
function emptyDatabase (done) {
  RoomSchema.deleteMany({}, (err) => {
    if (err) console.log('ERROR: ' + err)
    done()
  })
}

describe('JamTUgether Tests', () => {
  before((done) => {
    // Empty database
    emptyDatabase(done)
  })
  afterEach((done) => {
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
    it('should return status 201', (done) => { // one room successful
      chai.request(app)
        .post('/api/create-room')
        .set('content-type', 'application/json')
        .type('json')
        .send({
          password: '1234'
        })
        .end((err, res) => {
          res.should.have.status(201)
          res.body.should.be.a('object')
          res.body.should.have.property('roomID')
          res.body.should.have.property('token')
          res.body.should.have.property('userID')
          should.not.exist(err)
        })
      done()
    })
    it('should return status 413', (done) => { // password too long
      chai.request(app)
        .post('/api/create-room')
        .send({
          password: 'LgNvPuV971aqfLO_rbWZfh2Eju47Xpx0I'
        })
        // .buffer(true)
        .end((err, res) => {
          res.should.have.status(413)
          res.body.should.be.an('object')
          res.body.should.have.property('description').eql('Password too large.')
          should.not.exist(err)
        })
      done()
    })
  })

  /*
  describe('POST /create-room stress', () => {
    // TODO: not working yet (doesn't create any rooms, possibly beacause of timeout)
    // this.slow(300000)
    it('should return status 503', (done) => {
      // Create maximum number of rooms
      const requester = chai.request(app).keepOpen()
      // Promise.all(
      for (let i = 0; i < 500; i++) {
        requester
          .post('/api/create-room')
          .send({
            "password": "1234"
          })
          .end((err, res) => {
            res.should.have.status(201)
            should.not.exist(err)
          })
      }
      // Create one to many rooms
      requester
        .post('/api/create-room')
        .send({
          "password": "1234"
        })
        .end((err, res) => {
          res.should.have.status(503)
          should.not.exist(err)
        })

      done()
      requester.close()
    })
  }) */

  /**
   * TEST delete /room
   */
  describe('DELETE /room', () => {
    before((done) => {
      chai.request(app)
        .post('/api/create-room')
        .send({
          password: '1234'
        })
        .end((err, res) => {
          console.log('res: ' + res.body)
          res.should.have.status(201)
          should.not.exist(err)
        })
      console.log('Created room to delete')
      done()
    })

    it('should return status 200', (done) => {
      chai.request(app)
        .delete('/api/room')
        .send({
          roomID: '1',
          password: '1234'
        })
        .end((err, res) => {
          res.should.have.status(200)
          res.body.should.be.a('object')
          res.body.should.have.property('description')
          should.not.exist(err)
        })
      done()
    })
  })

  /**
   * TEST post /login
   */
  describe('POST /login', () => {
    before('Create room to login to', (done) => {
      // Empty database
      RoomSchema.deleteMany({}, (err) => {
        if (err) console.log('ERROR: ' + err)
      })
      chai.request(app)
        .post('/api/create-room')
        .send({
          password: '1234'
        })
        .end((err, res) => {
          console.log('res: ' + res.body)
          res.should.have.status(201)
          should.not.exist(err)
        })
      done()
    })

    it('should return 201', (done) => {
      // does nothing
      chai.request(app)
        .post('/api/create-room')
        .send({
          password: '1234'
        })

      chai.request(app)
        .post('/api/login')
        .send({
          roomID: '1',
          password: '1234'
        })
        .end((err, res) => {
          console.log('response: ' + res.body.text)
          res.should.have.status(201)
          res.body.should.be.a('object')
          res.body.should.have.property('description')
          should.not.exist(err)
        })

      done()
    })

    it('should return 401', (done) => {
      chai.request(app)
        .post('/api/login')
        .send({
          roomID: '9999',
          password: '1234'
        })
        .end((err, res) => {
          console.log('response: ' + res.body.text)
          res.should.have.status(401)
          res.body.should.be.a('object')
          res.body.should.have.property('description')
          should.not.exist(err)
        })
      done()
    })
  })
})
