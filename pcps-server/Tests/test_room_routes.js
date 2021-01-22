const { expect } = require('chai')
const app = require('../server.js')
const chai = require('chai')
const chaiHttp = require('chai-http')
chai.use(chaiHttp)

// Test create-room
//chai.request('http://vm4.sese.tu-berlin.de:3000/api')
chai.request(app)
  .post('/api/create-room')
  .send({
    "password": "1234"
  })
  .then(function (res) {
    expect(res).to.have.status(201)
    return
  })
  .catch(function (err) {
    throw err
  })
