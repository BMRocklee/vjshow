import ffmpeg from 'fluent-ffmpeg'
import ffmpegPath from 'ffmpeg-static'
import ffprobePath from 'ffprobe-static'
import path from 'path'
import fs from 'fs'

ffmpeg.setFfmpegPath(ffmpegPath)
ffmpeg.setFfprobePath(ffprobePath.path) // 🔥 phải là .path

const getVideoMetadata = (input) => {
  return new Promise((resolve, reject) => {
    ffmpeg.ffprobe(input, (err, data) => {
      if (err) return reject(err)

      const stream = data.streams.find(s => s.width)

      resolve({
        width: stream?.width,
        height: stream?.height,
        duration: data.format.duration,
        format: data.format.format_name
      })
    })
  })
}

export const processVideo = async ({ url }) => {
  try {
    const root = process.cwd()
    const cleanUrl = url.replace(/\\/g, '/')

    const input = path.join(root, '..', 'vjshow', 'marketplace', cleanUrl)

    const fileName = path.basename(url, path.extname(url))

    const previewRelative = `uploads/preview_${fileName}.mp4`
    const previewOutput = path.join(root, '..', 'vjshow', 'marketplace', previewRelative)

    const thumbRelative = `uploads/thumb_${fileName}.jpg`
    const thumbOutput = path.join(root, '..', 'vjshow', 'marketplace', thumbRelative)

    if (!fs.existsSync(input)) {
      throw new Error('File not found')
    }

    // 🎥 preview
    await new Promise((resolve, reject) => {
      ffmpeg(input)
        .setStartTime(0)
        .setDuration(10)
        .output(previewOutput)
        .on('end', resolve)
        .on('error', reject)
        .run()
    })

    // 🖼 thumbnail
    await new Promise((resolve, reject) => {
      ffmpeg(input)
        .screenshots({
          timestamps: ['2'],
          filename: path.basename(thumbRelative),
          folder: path.dirname(thumbOutput),
          size: '640x?'
        })
        .on('end', resolve)
        .on('error', reject)
    })

    const stats = fs.statSync(input)
    const meta = await getVideoMetadata(input)

    return {
      previewUrl: previewRelative,
      thumbnailUrl: thumbRelative,
      width: meta.width,
      height: meta.height,
      duration: meta.duration,
      format: meta.format,
      size: stats.size
    }

  } catch (err) {
    console.error('❌ ERROR VIDEO:', err)
    throw err
  }
}