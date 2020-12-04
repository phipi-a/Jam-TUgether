const express = require('express')
const mongoose = require('mongoose')
const dbConfig = require('./db/database')
const swaggerJsDoc = require('swagger-jsdoc')
const swaggerUi = require('swagger-ui-express')
require('dotenv').config()

// Set port
const PORT = process.env.PORT || 3000

// Connecting MongoDB
mongoose.connect(dbConfig.db, { useNewUrlParser: true, useUnifiedTopology: true })
  .catch(error => console.log('Couldn\'t connect database ' + error))

const db = mongoose.connection
// Catch error after initial connection
db.on('error', err => {
  console.error.bind(console, 'connection error:' + err)
})

// Express setup
const app = express()
app.use(express.json())
app.use(express.urlencoded({ extended: false }))

// Documentation setup
const swaggerOptions = {
  swaggerDefinition: {
    info: {
      title: 'JamTugether API',
      version: '1.0.0'
    }
  },
  // In which files the documentation comments are
  apis: ['server.js', 'routes/room.route.js'],
  // Define endpoint in documentation (doesn't work in swagger-ui-express 4.1.5)
  servers: [{ url: '/api', description: 'The API server'}]
}

const swaggerDocument = swaggerJsDoc(swaggerOptions)

// Api root
const roomRoute = require('./routes/room.route')
app.use('/api', roomRoute)

// Documentation root
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument))

// Run Server on PORT
const server = app.listen(PORT, () => {
  console.log('Server running on port: ' + PORT)
})

// Handle 404s (has to be at the bottom of the code)
app.use((req, res, next) => {
  res.status(404).send('404 Couldn\'t find what you were looking for!')
})

// Error handler
app.use((err, req, res, next) => {
  console.error(err.message)
  if (!err.statusCode) err.statusCode = 500
  res.status(err.statusCode).send(err.message)
})
