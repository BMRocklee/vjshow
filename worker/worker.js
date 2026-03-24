import { Worker } from 'bullmq'
import IORedis from 'ioredis'
import axios from 'axios'
import { processVideo } from './video.js'
import { processImage } from './image.js'

// 🔌 Redis connection
const connection = new IORedis({
  host: '127.0.0.1',
  port: 6379,
  maxRetriesPerRequest: null
})

console.log('🚀 Worker started...')

// 🎯 MAIN WORKER
const worker = new Worker(
  'media',
  async (job) => {
    try {
      let payload = {
        fileKey: job.data.url,
        type: job.name
      }

      if (job.name === 'IMAGE') {
        const result = await processImage(job.data)

        payload = { ...payload, ...result }
      }

      if (job.name === 'VIDEO') {
        const result = await processVideo(job.data)

        payload = { ...payload, ...result }
      }

      console.log('📤 Payload gửi BE:', payload)

      await axios.post(
        'http://localhost:8080/api/products/done',
        payload
      )

      console.log('✅ DONE API CALLED')

    } catch (err) {
      console.error('❌ WORKER ERROR:', err.message)
      throw err
    }
  },
  { connection }
)

// 📊 EVENTS
worker.on('completed', (job) => {
  console.log(`🎉 Job completed: ${job.name}`)
})

worker.on('failed', (job, err) => {
  console.error(`💥 Job failed: ${job?.name}`, err.message)
})