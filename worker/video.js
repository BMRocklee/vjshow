import { S3Client, GetObjectCommand, PutObjectCommand } from '@aws-sdk/client-s3'
import { spawn } from 'child_process'
import ffmpegPath from 'ffmpeg-static'
import ffprobePath from 'ffprobe-static'
import sharp from 'sharp'
import fs from 'fs'
import path from 'path'
import os from 'os'

// =====================
// 🔥 CONFIG
// =====================
const BUCKET = 'vjshow'

const s3 = new S3Client({
  region: 'auto',
  endpoint: 'https://f2cf3a00591888eeaac9767d3bc1881a.r2.cloudflarestorage.com',
  forcePathStyle: true,
  credentials: {
    accessKeyId: '7476a0b5b996b0246f2b0f1fb2d3eeb8',
    secretAccessKey: '1f68525330a3f3241abc7361a6dc6eb3abd6740d8fa2cc875dc9b079418b8073'
  }
})

// =====================
// 🔥 STREAM → FILE
// =====================
const streamToFile = (stream, filePath) =>
  new Promise((resolve, reject) => {
    const write = fs.createWriteStream(filePath)
    stream.pipe(write)
    stream.on('error', reject)
    write.on('finish', resolve)
  })

// =====================
// 🔥 UPLOAD
// =====================
const uploadToR2 = async (key, filePath, contentType) => {
  const file = fs.readFileSync(filePath)

  await s3.send(new PutObjectCommand({
    Bucket: BUCKET,
    Key: key,
    Body: file,
    ContentType: contentType
  }))
}

// =====================
// 🔥 RUN FFMPEG
// =====================
const runFFmpeg = (args) =>
  new Promise((resolve, reject) => {
    const ff = spawn(ffmpegPath, args)

    ff.stderr.on('data', (d) => console.log(d.toString()))

    ff.on('close', (code) => {
      if (code === 0) resolve()
      else reject(new Error(`FFmpeg failed: ${code}`))
    })

    ff.on('error', reject)
  })

// =====================
// 🔥 METADATA
// =====================
const getVideoMetadata = (input) =>
  new Promise((resolve, reject) => {
    const ff = spawn(ffprobePath.path, [
      '-v', 'error',
      '-print_format', 'json',
      '-show_streams',
      '-show_format',
      input
    ])

    let data = ''
    ff.stdout.on('data', (c) => (data += c))

    ff.on('close', () => {
      try {
        const json = JSON.parse(data)
        const video = json.streams.find(s => s.codec_type === 'video')

        resolve({
          width: video?.width || 1280,
          height: video?.height || 720,
          duration: parseFloat(json.format.duration || 0)
        })
      } catch (e) {
        reject(e)
      }
    })

    ff.on('error', reject)
  })

// =====================
// 🔥 WATERMARK SVG (GIỐNG IMAGE)
// =====================
const createWatermarkSvg = (width, height, opacity = 0.08) => {
  const patternSize = Math.floor(width / 2.5)
  const fontSize = Math.floor(width / 14)

  return `
  <svg width="${width}" height="${height}">
    <defs>
      <pattern 
        id="wm" 
        width="${patternSize}" 
        height="${patternSize}" 
        patternUnits="userSpaceOnUse" 
        patternTransform="rotate(-30)"
      >
        <text 
          x="${patternSize * 0.1}" 
          y="${patternSize * 0.6}" 
          font-size="${fontSize}" 
          fill="white" 
          opacity="${opacity}" 
          font-weight="bold"
        >
          VJSHOW
        </text>
      </pattern>
    </defs>
    <rect width="100%" height="100%" fill="url(#wm)" />
  </svg>
  `
}

// =====================
// 🚀 MAIN
// =====================
export const processVideo = async ({ key }) => {
  const tmp = os.tmpdir()

  const input = path.join(tmp, `input-${Date.now()}.mp4`)
  const preview = path.join(tmp, `preview-${Date.now()}.mp4`)
  const thumbRaw = path.join(tmp, `thumb-raw-${Date.now()}.jpg`)
  const thumb = path.join(tmp, `thumb-${Date.now()}.jpg`)
  const wmImage = path.join(tmp, `wm-${Date.now()}.png`)

  try {
    console.log('🎬 Processing:', key)

    // ⬇️ download
    const object = await s3.send(new GetObjectCommand({
      Bucket: BUCKET,
      Key: key
    }))

    await streamToFile(object.Body, input)

    // 📊 metadata
    const { width, height, duration } = await getVideoMetadata(input)

    // =====================
    // 🎬 PREVIEW (FIX ĐEN VIDEO)
    // =====================

    // tạo watermark PNG từ SVG
    const svg = createWatermarkSvg(width, height, 0.12)

    await sharp(Buffer.from(svg))
      .png()
      .toFile(wmImage)

    await runFFmpeg([
      '-i', input,
      '-i', wmImage,
      '-t', '10',

      '-filter_complex',
      '[0:v][1:v] overlay=0:0',

      '-c:v', 'libx264',
      '-preset', 'fast',
      '-crf', '28',
      '-an',
      preview
    ])

    const previewKey = key
      .replace('original/', 'preview/')
      .replace(/\.\w+$/, '.mp4')

    await uploadToR2(previewKey, preview, 'video/mp4')

    // =====================
    // 🖼 THUMB
    // =====================
    const captureTime = Math.max(1, Math.floor(duration / 3))

    await runFFmpeg([
      '-i', input,
      '-ss', `${captureTime}`,
      '-vframes', '1',
      thumbRaw
    ])

    const buffer = fs.readFileSync(thumbRaw)
    const img = sharp(buffer)
    const meta = await img.metadata()

    const svgThumb = createWatermarkSvg(meta.width, meta.height, 0.08)

    const finalThumb = await img
      .composite([{ input: Buffer.from(svgThumb) }])
      .jpeg({ quality: 90 })
      .toBuffer()

    fs.writeFileSync(thumb, finalThumb)

    const thumbKey = key
      .replace('original/', 'thumb/')
      .replace(/\.\w+$/, '.jpg')

    await uploadToR2(thumbKey, thumb, 'image/jpeg')

    console.log('✅ Done:', previewKey, thumbKey)

    return {
      previewUrl: previewKey,
      thumbUrl: thumbKey,
      width,
      height,
      duration
    }

  } catch (err) {
    console.error('❌ VIDEO ERROR:', err)
    throw err
  } finally {
    ;[input, preview, thumbRaw, thumb, wmImage].forEach((f) => {
      if (fs.existsSync(f)) fs.unlinkSync(f)
    })
  }
}
