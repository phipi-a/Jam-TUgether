//process.env.NODE_ENV = "test"

let chai = require('chai')
let chaiHttp = require('chai-http')
let should = chai.should()

const roomID = 1

chai.use(chaiHttp)

describe('Books', () => {
    // beforeEach((done) => { //Before each test we empty the database
    //     Book.remove({}, (err) => {
    //        done();
    //     });
    // });
 /*
  * Test sendTracks
  */
  describe('/GET track', () => {
      it('it should GET specified track', (done) => {
        chai.request('http://localhost:3000')
            .get('/api/room/1' + roomID)
            .end((err, res) => {
                  res.should.have.status(200)
                  res.body.should.be.a('object')
                  //res.body.soundtracks.should.be.a('list')
              done()
            })
      })
  })

  describe('/POST track', () => {
      it('it should POST track', (done) => {
          let track = {
            soundtracks : [{userID : 1, instrument : "retinstrument123", number : 1, soundSequence : [{starttime : 4, endtime : 12, pitch : 23}]}]
          }
        chai.request('http://localhost:3000')
            .post('/api/room/' + roomID)
            .send(track)
            .end((err, res) => {
                res.should.have.status(200)
                res.body.should.be.a('object')
            done()
            })
      })
  })

  describe('/DELETE track', () => {
      let track = {
          roomID : 1,
          userID : 1,
          instrument : "retinstrument123",
          number : 1
        }
      it('it should DELETE specified track', (done) => {
        chai.request('http://localhost:3000')
            .delete('/api/room/' + roomID)
            .auth('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJybmRtUGF5bG9hZCI6ImY4YTNjMDBmZTVmY2NhNjllYzA0Iiwicm9vbSI6MSwicm9sZSI6IkFkbWluIiwiaWF0IjoxNjExNTE0MDE0LCJleHAiOjE2MTE1MTU4MTR9.Umkhn33n2ipvRm8BM3fzfo3Qpr786ljM3tD1-4NjqZI')
            .query(track)
            .end((err, res) => {
                res.should.have.status(200)
                res.body.should.be.a('object')
            })
      })
  })

});