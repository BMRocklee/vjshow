import sharp from 'sharp'
import path from 'path'
import fs from 'fs'

export const processImage = async ({ url }) => {
  try {
    const root = process.cwd()
    const cleanUrl = url.replace(/\\/g, '/')

    const input = path.join(root, '..', 'vjshow', 'marketplace', cleanUrl)

    const fileName = path.basename(url)
    const outputRelative = `uploads/wm_${fileName}`
    const output = path.join(root, '..', 'vjshow', 'marketplace', outputRelative)

    if (!fs.existsSync(input)) {
      throw new Error('File not found: ' + input)
    }

    const image = sharp(input)
    const metadata = await image.metadata()

    const svg = `
    <svg width="${metadata.width}" height="${metadata.height}">
      <defs>
        <pattern id="wm" width="300" height="300" patternUnits="userSpaceOnUse" patternTransform="rotate(-30)">
          <text x="0" y="150" font-size="60" fill="white" opacity="0.15" font-weight="bold">
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

    const stats = fs.statSync(input)

    return {
      previewUrl: outputRelative,
      width: metadata.width,
      height: metadata.height,
      format: metadata.format,
      size: stats.size,
      space: metadata.space,
      channels: metadata.channels
    }

  } catch (err) {
    console.error('❌ ERROR IMAGE:', err)
    throw err
  }
}