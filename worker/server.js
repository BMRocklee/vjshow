import express from 'express'
import { Queue } from 'bullmq'
import IORedis from 'ioredis'

const app = express()
app.use(express.json())

// Redis connection
const connection = new IORedis({
  // host: '127.0.0.1',
  host: 'redis',
  port: 6379,
  maxRetriesPerRequest: null
})

// Queue
const queue = new Queue('media', { connection })

// Unified API
app.post('/queue/job', async (req, res) => {
  const { key, type } = req.body

  if (!key || !type) {
    return res.status(400).json({ error: 'Missing key or type' })
  }

  await queue.add(type, { key }, {
    attempts: 3,
    backoff: {
      type: 'exponential',
      delay: 2000
    },
    removeOnComplete: true
  })

  console.log(`📩 Job received: ${type} - ${key}`)

  res.json({ status: 'queued' })
})

app.listen(3001, () => {
  console.log('🚀 Queue API running on port 3001')
})
