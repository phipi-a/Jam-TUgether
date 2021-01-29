const chai = require('chai')
const chaiHttp = require('chai-http')

chai.use(chaiHttp)
const should = chai.should()
const app = require('../server.js')

describe('JAMTUgether Tests 2', function () {
  describe('POST /create-room', function () {
    it('should return status 201', function (done) { // one room successful
      chai.request(app)
        .post('/api/create-room')
        .send({ password: '1234' })
        .end((err, res) => {
          res.should.have.status(201)
          res.body.should.be.an('object')
          res.body.should.have.property('roomID')
          res.body.should.have.property('token')
          res.body.should.have.property('userID')
          done(err)
        })
    })
  })
})
