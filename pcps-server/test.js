/* initialize chai http test framework */
const chai = require('chai')
const chaiHttp = require('chai-http')
chai.use(chaiHttp)
const should = chai.should()

describe('Test create-room, checkAdmin und login', () => {
  it('Should create room, check if admin is admin, then login as normal user and check if normaler user', function (done) {
    chai.request('http://localhost:3000')
      .post('/api/create-room')
      .send({ password: '123' })
      .end((err, res) => { // when we get a resonse from the endpoint
        // the res object should have a status of 201
        if (err) {
          console.log(err)
        }
        res.should.have.status(201)
        const apiEndpoint = '/api/room/' + res.body.roomID + '/admin'
        const admToken = res.body.token
        const roomID = res.body.roomID
        // Test if Admin is displayed as admin
        chai.request('http://localhost:3000')
          .get(apiEndpoint)
          .set({ Authorization: `Bearer ${admToken}` })
          .send({ roomID: res.body.roomID })
          .end((err, res) => {
            if (err) {
              console.log(err)
            }
            res.should.have.status(202)
          })
        // Login and test if admin status change to user after 
        chai.request('http://localhost:3000')
          .post('/api/login')
          .send({ roomID: roomID, password: '123' })
          .end(async (err, res) => { // when we get a resonse from the endpoint
            // the res object should have a status of 201
            if (err) {
              console.log(err)
            }
            res.should.have.status(201)
            const normalToken = res.body.token
            // Test if Admin is displayed as admin
            chai.request('http://localhost:3000')
              .get(apiEndpoint)
              .set({ Authorization: `Bearer ${normalToken}` })
              .send({ roomID: res.body.roomID })
              .end((err, res) => {
                if (err) {
                  console.log(err)
                }
                res.should.have.status(200)
              })
            // sleep for 1.5 minute should set Admin new
            await new Promise(resolve => setTimeout(resolve, 90000))
            chai.request('http://localhost:3000')
              .get(apiEndpoint)
              .set({ Authorization: `Bearer ${normalToken}` })
              .send({ roomID: res.body.roomID })
              .end((err, res) => {
                if (err) {
                  console.log(err)
                }
                res.should.have.status(202)
              })
            done()
          })
      })
  }).timeout(150000)
})
