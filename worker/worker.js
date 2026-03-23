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
      console.log('👉 Processing:', job.name)
      console.log('📦 Data:', job.data)

      let previewPath = null

      // 🎥 VIDEO
      if (job.name === 'VIDEO') {
        previewPath = await processVideo(job.data)

        console.log('📡 Calling BE DONE (VIDEO)...')
      }

      // 🖼 IMAGE
      if (job.name === 'IMAGE') {
        previewPath = await processImage(job.data)

        console.log('📡 Calling BE DONE (IMAGE)...')
      }

      // 🔥 gọi BE (chung cho cả 2 loại)
      const res = await axios.post(
        'http://localhost:8080/api/products/done',
        {
          fileKey: job.data.url,        // relative path
          previewUrl: previewPath       // 🔥 path mới sau xử lý
        }
      )

      console.log('✅ DONE API CALLED:', res.status)

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