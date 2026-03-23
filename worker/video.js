import ffmpeg from 'fluent-ffmpeg'
import ffmpegPath from 'ffmpeg-static'
import fs from 'fs'
import axios from 'axios'

ffmpeg.setFfmpegPath(ffmpegPath)
 /*
export const processVideo = async ({ url }) => {
  const input = 'input.mp4'
  const output = 'preview.mp4'

  console.log('⬇️ Download video...')

  const res = await axios.get(url, { responseType: 'stream' })
  const writer = fs.createWriteStream(input)
  res.data.pipe(writer)

  await new Promise(r => writer.on('finish', r))

  console.log('✂️ Cutting video...')

  await new Promise((resolve, reject) => {
    ffmpeg(input)
      .setStartTime(0)
      .setDuration(10)
      .output(output)
      .on('end', resolve)
      .on('error', reject)
      .run()
  })

  console.log('🎬 Done video:', output)

  return true
}
*/

export const processVideo = async ({ url }) => {
  const input = url   // 👉 dùng file local
  const output = 'preview.mp4'

  console.log('🎬 Processing local video...')

  await new Promise((resolve, reject) => {
    ffmpeg(input)
      .setStartTime(0)
      .setDuration(10)
      .output(output)
      .on('end', resolve)
      .on('error', reject)
      .run()
  })

  console.log('✅ Done video:', output)

  return true
}