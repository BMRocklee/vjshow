import sharp from 'sharp'
import { S3Client, GetObjectCommand, PutObjectCommand } from '@aws-sdk/client-s3'

// 🔥 CONFIG
const BUCKET = 'vjshow'

// 🔥 S3 client (Cloudflare R2)
const s3 = new S3Client({
  region: 'auto',
  endpoint: 'https://f2cf3a00591888eeaac9767d3bc1881a.r2.cloudflarestorage.com',
  forcePathStyle: true,
  credentials: {
    accessKeyId: '7476a0b5b996b0246f2b0f1fb2d3eeb8',
    secretAccessKey: '1f68525330a3f3241abc7361a6dc6eb3abd6740d8fa2cc875dc9b079418b8073'
  }
})

// 🔥 stream → buffer
const streamToBuffer = async (stream) => {
  return new Promise((resolve, reject) => {
    const chunks = []
    stream.on('data', (chunk) => chunks.push(chunk))
    stream.on('end', () => resolve(Buffer.concat(chunks)))
    stream.on('error', reject)
  })
}

// 🔥 create watermark SVG (responsive)
const createWatermarkSvg = (width, height, opacity = 0.12) => {
  const patternSize = Math.floor(width / 2.5)
  const fontSize = Math.floor(width / 14)

  return `
  <svg width="${width}" height="${height}">
    <defs>
      <pattern id="wm" width="${patternSize}" height="${patternSize}" patternUnits="userSpaceOnUse" patternTransform="rotate(-30)">
        <text x="0" y="${patternSize / 2}" font-size="${fontSize}" fill="white" opacity="${opacity}" font-weight="bold">
          VJSHOW
        </text>
      </pattern>
    </defs>
    <rect width="100%" height="100%" fill="url(#wm)" />
  </svg>
  `
}

// 🔥 upload helper
const uploadToR2 = async (key, buffer, contentType) => {
  await s3.send(new PutObjectCommand({
    Bucket: BUCKET,
    Key: key,
    Body: buffer,
    ContentType: contentType
  }))
}

// 🚀 MAIN PROCESS
export const processImage = async ({ key }) => {
  try {
    console.log('🖼 Processing:', key)

    // ⬇️ download original
    const object = await s3.send(new GetObjectCommand({
      Bucket: BUCKET,
      Key: key
    }))

    const buffer = await streamToBuffer(object.Body)

    const image = sharp(buffer)
    const metadata = await image.metadata()

    const width = metadata.width || 800
    const height = metadata.height || 800

    // =========================
    // 🟢 PREVIEW (FULL + WATERMARK)
    // =========================
    const previewSvg = createWatermarkSvg(width, height, 0.12)

    const previewBuffer = await sharp(buffer)
      .composite([{ input: Buffer.from(previewSvg) }])
      .webp({ quality: 85 })
      .toBuffer()

    const previewKey = key
      .replace('original/', 'preview/')
      .replace(/\.\w+$/, '.webp')

    await uploadToR2(previewKey, previewBuffer, 'image/webp')

    // =========================
    // 🔵 THUMB (300px + WATERMARK)
    // =========================
    const thumbWidth = 300
    const thumbHeight = Math.round((height / width) * thumbWidth)

    const thumbSvg = createWatermarkSvg(thumbWidth, thumbHeight, 0.08)

    const thumbBuffer = await sharp(buffer)
      .resize(thumbWidth)
      .composite([{ input: Buffer.from(thumbSvg) }])
      .webp({ quality: 80 })
      .toBuffer()

    const thumbKey = key
      .replace('original/', 'thumb/')
      .replace(/\.\w+$/, '.webp')

    await uploadToR2(thumbKey, thumbBuffer, 'image/webp')

    console.log('✅ Done:', previewKey, thumbKey)

    return {
      previewUrl: previewKey,
      thumbUrl: thumbKey,
      width,
      height
    }

  } catch (err) {
    console.error('❌ IMAGE ERROR:', err)
    throw err
  }
}
