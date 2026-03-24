import express from 'express'
import { Queue } from 'bullmq'
import IORedis from 'ioredis'

const app = express()
app.use(express.json())

// connect Redis
const connection = new IORedis({
  host: '127.0.0.1',
  port: 6379,
  maxRetriesPerRequest: null
})

// tạo queue
const queue = new Queue('media', { connection })

// API push job VIDEO
app.post('/queue/video', async (req, res) => {
  await queue.add('VIDEO', req.body)
  console.log('📩 Received VIDEO job')
  res.send('ok')
})

// API push job IMAGE
app.post('/queue/image', async (req, res) => {
  await queue.add('IMAGE', req.body)
  console.log('📩 Received IMAGE job')
  res.send('ok')
})

// start server
app.listen(3001, () => {
  console.log('🚀 Worker API running at http://localhost:3001')
})