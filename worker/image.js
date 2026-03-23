import sharp from 'sharp'
import path from 'path'
import fs from 'fs'

export const processImage = async ({ url }) => {
  try {
    console.log('📂 Input (relative):', url)

    // 🔥 resolve từ worker folder
    const root = process.cwd() // vjshow/worker

    const input = path.join(root, '..', 'vjshow', 'marketplace', url)
    const outputRelative = `uploads/wm_${path.basename(url)}`
    const output = path.join(root, '..', 'vjshow', 'marketplace', outputRelative)

    console.log('📂 Absolute input:', input)
    console.log('📂 Output:', output)

    // 🔥 check tồn tại file
    console.log('📁 file exists?', fs.existsSync(input))

    const image = sharp(input)
    const metadata = await image.metadata()

    const width = metadata.width
    const height = metadata.height

    const svg = `
    <svg width="${width}" height="${height}">
      <defs>
        <pattern id="wm"
                 width="300"
                 height="300"
                 patternUnits="userSpaceOnUse"
                 patternTransform="rotate(-30)">
          <text x="0" y="150"
                font-size="60"
                fill="white"
                opacity="0.15"
                font-weight="bold">
            VJSHOW
          </text>
        </pattern>
      </defs>
      <rect width="100%" height="100%" fill="url(#wm)" />
    </svg>
    `

    await image
      .composite([{ input: Buffer.from(svg), top: 0, left: 0 }])
      .toFile(output)

    console.log('✅ Done image:', outputRelative)

    return outputRelative

  } catch (err) {
    console.error('❌ ERROR IMAGE:', err)
    throw err
  }
}