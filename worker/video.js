import ffmpeg from 'fluent-ffmpeg'
import ffmpegPath from 'ffmpeg-static'
import path from 'path'
import fs from 'fs'

ffmpeg.setFfmpegPath(ffmpegPath)

export const processVideo = async ({ url }) => {
  try {
    console.log('📂 Input (relative):', url)

    const root = process.cwd() // vjshow/worker
    const cleanUrl = url.replace(/\\/g, '/')

    // 👉 giống image
    const input = path.join(root, '..', 'vjshow', 'marketplace', cleanUrl)

    const outputRelative = `uploads/preview_${path.basename(url)}`
    const output = path.join(root, '..', 'vjshow', 'marketplace', outputRelative)

    console.log('📂 Absolute input:', input)
    console.log('📂 Output:', output)

    // check file tồn tại
    if (!fs.existsSync(input)) {
      throw new Error('File not found: ' + input)
    }

    console.log('📁 file exists? true')

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

    console.log('✅ Done video:', outputRelative)

    return outputRelative

  } catch (err) {
    console.error('❌ ERROR VIDEO:', err)
    throw err
  }
}