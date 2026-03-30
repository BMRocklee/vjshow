import { Worker } from 'bullmq'
import IORedis from 'ioredis'
import axios from 'axios'
import { processImage } from './image.js'
import { processVideo } from './video.js'

// Redis
const connection = new IORedis({
  // host: '127.0.0.1',
  host: 'redis',
  port: 6379,
  maxRetriesPerRequest: null
})

console.log('🚀 Worker started...')

const worker = new Worker(
  'media',
  async (job) => {
    const { key } = job.data
    const type = job.name

    try {
      let result = {}

      if (type === 'IMAGE') {
        result = await processImage({ key })
      }

      if (type === 'VIDEO') {
        result = await processVideo({ key })
      }

      const payload = {
        fileKey: key,
        type,
        ...result
      }

      console.log('📤 Callback payload:', payload)

      await axios.post(
        // 'http://localhost:8080/api/products/done',
        'http://be:8080/api/products/done',
        payload
      )

    } catch (err) {
      console.error('❌ WORKER ERROR:', err)
      throw err
    }
  },
  {
    connection,
    concurrency: 5
  }
)

worker.on('completed', (job) => {
  console.log(`🎉 Job done: ${job.name}`)
})

worker.on('failed', (job, err) => {
  console.error(`💥 Job failed: ${job?.name}`, err.message)
})
