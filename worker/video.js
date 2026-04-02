import { S3Client, GetObjectCommand, PutObjectCommand } from '@aws-sdk/client-s3'
import { spawn } from 'child_process'
import ffmpegPath from 'ffmpeg-static'
import ffprobePath from 'ffprobe-static'
import sharp from 'sharp'
import fs from 'fs'
import path from 'path'
import os from 'os'

// =====================
// CONFIG
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
// STREAM UPLOAD (NO RAM)
// =====================
const uploadStream = async (key, stream, contentType) => {
await s3.send(new PutObjectCommand({
Bucket: BUCKET,
Key: key,
Body: stream,
ContentType: contentType
}))
}

// =====================
// FFMPEG RUN
// =====================
const spawnFFmpeg = (args) => {
const ff = spawn(ffmpegPath, args)
ff.stderr.on('data', d => console.log(d.toString()))
return ff
}

// =====================
// WATERMARK
// =====================
const createWatermark = async (width, height) => {
const svg = `   <svg width="${width}" height="${height}">     <text x="50%" y="50%" font-size="${Math.floor(width/12)}"
      fill="white" opacity="0.1"
      text-anchor="middle"
      transform="rotate(-30 ${width/2} ${height/2})">
      VJSHOW     </text>   </svg>`

const file = path.join(os.tmpdir(), `wm-${Date.now()}.png`)
await sharp(Buffer.from(svg)).png().toFile(file)
return file
}

// =====================
// MAIN
// =====================
export const processVideo = async ({ key }) => {
const tmp = os.tmpdir()
const hlsDir = path.join(tmp, `hls-${Date.now()}`)
fs.mkdirSync(hlsDir)

// 🔥 get stream từ R2
const object = await s3.send(new GetObjectCommand({
Bucket: BUCKET,
Key: key
}))

const inputStream = object.Body

// =====================
// PREVIEW (10s)
// =====================
const previewKey = key.replace('original/', 'preview/').replace(/.\w+$/, '.mp4')

const previewFF = spawnFFmpeg([
'-i', 'pipe:0',
'-t', '10',
'-vf', 'scale=1280:-1',
'-preset', 'veryfast',
'-threads', '1',
'-f', 'mp4',
'pipe:1'
])

inputStream.pipe(previewFF.stdin)

await uploadStream(previewKey, previewFF.stdout, 'video/mp4')

// =====================
// THUMBNAIL
// =====================
const thumbKey = key.replace('original/', 'thumb/').replace(/.\w+$/, '.jpg')

const thumbFF = spawnFFmpeg([
'-i', 'pipe:0',
'-ss', '2',
'-vframes', '1',
'-f', 'image2pipe',
'pipe:1'
])

inputStream.pipe(thumbFF.stdin)

await uploadStream(thumbKey, thumbFF.stdout, 'image/jpeg')

// =====================
// HLS FULL VIDEO + WATERMARK
// =====================
const wm = await createWatermark(1280, 720)

const hlsFF = spawnFFmpeg([
'-i', 'pipe:0',
'-i', wm,
'-filter_complex', '[0:v][1:v] overlay=0:0',
'-preset', 'veryfast',
'-threads', '1',
'-f', 'hls',
'-hls_time', '6',
'-hls_list_size', '0',
'-hls_segment_filename', `${hlsDir}/seg_%03d.ts`,
`${hlsDir}/index.m3u8`
])

inputStream.pipe(hlsFF.stdin)

await new Promise(resolve => hlsFF.on('close', resolve))

// upload HLS files
const files = fs.readdirSync(hlsDir)

for (const file of files) {
const filePath = path.join(hlsDir, file)
const stream = fs.createReadStream(filePath)

```
const keyHls = key.replace('original/', 'hls/') + '/' + file

await uploadStream(
  keyHls,
  stream,
  file.endsWith('.m3u8')
    ? 'application/vnd.apple.mpegurl'
    : 'video/mp2t'
)

fs.unlinkSync(filePath)
```

}

fs.rmdirSync(hlsDir)

return {
previewUrl: previewKey,
thumbUrl: thumbKey,
hlsUrl: key.replace('original/', 'hls/') + '/index.m3u8'
}
}
