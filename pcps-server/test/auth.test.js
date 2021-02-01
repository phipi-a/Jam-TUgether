/* initialize chai http test framework */
const chai = require('chai')
const chaiHttp = require('chai-http')
chai.use(chaiHttp)
const should = chai.should()
const app = require('../server.js')

describe('Test create-room, checkAdmin und login', () => {
  it('Should create room, check if admin is admin, then login as normal user and check if normaler user', function (done) {
    chai.request(app)
      .post('/api/create-room')
      .send({ password: '123' })
      .end((err, res) => { // when we get a resonse from the endpoint
        // the res object should have a status of 201
        if (err) {
          done(err)
        }
        res.should.have.status(201)
        const apiEndpoint = '/api/room/' + res.body.roomID + '/admin'
        const admToken = res.body.token
        const roomID = res.body.roomID
        // Test if Admin is displayed as admin
        chai.request(app)
          .get(apiEndpoint)
          .set({ Authorization: `Bearer ${admToken}` })
          .send({ roomID: res.body.roomID })
          .end((err, res) => {
            if (err) {
              done(err)
            }
            res.should.have.status(202)
          })
        describe('Login and test if user becomes admin after 1 min', () => {
          it('Test if admin changes', function (done) {
            chai.request(app)
              .post('/api/login')
              .send({ roomID: roomID, password: '123' })
              .end(async (err, res) => { // when we get a response from the endpoint
              // the res object should have a status of 201
                if (err) {
                  done(err)
                }
                res.should.have.status(201)
                const normalToken = res.body.token
                // Test if Client is displayed as client
                chai.request(app)
                  .get(apiEndpoint)
                  .set({ Authorization: `Bearer ${normalToken}` })
                  .send({ roomID: res.body.roomID })
                  .end((err, res) => {
                    if (err) {
                      done(err)
                    }
                    res.should.have.status(200)
                  })
                // sleep for 1.5 minute should set Admin new
                await new Promise(resolve => setTimeout(resolve, 61000))
                chai.request(app)
                  .get(apiEndpoint)
                  .set({ Authorization: `Bearer ${normalToken}` })
                  .send({ roomID: res.body.roomID })
                  .end((err, res) => {
                    if (err) {
                      done(err)
                    }
                    if (!res.body.flag) {
                      console.log('error')
                    }
                    res.should.have.status(200)
                    // Test if Admin is displayed as admin
                    chai.request(app)
                      .get(apiEndpoint)
                      .set({ Authorization: `Bearer ${admToken}` })
                      .send({ roomID: res.body.roomID })
                      .end((err, res) => {
                        if (err) {
                          done(err)
                        }
                        res.should.have.status(200)
                        done()
                      })
                    done()
                  })
                //done()
              })
            done()
          })//.timeout(630000)
          done()
        })
      })
  })
})
