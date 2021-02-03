require('dotenv').config()

module.exports = {
  db: 'mongodb://localhost:27017/jamtugether',
  options: {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    user: 'server',
    pass: process.env.MONGODB_PASSWORD
  }
}
