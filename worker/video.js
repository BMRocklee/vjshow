import {
  S3Client,
  GetObjectCommand,
  PutObjectCommand
} from "@aws-sdk/client-s3";
import { spawn } from "child_process";
import ffmpegPath from "ffmpeg-static";
import ffprobePath from "ffprobe-static";
import sharp from "sharp";
import fs from "fs";
import path from "path";
import os from "os";

// =====================
// 🔥 CONFIG
// =====================
const BUCKET = "vjshow";

const s3 = new S3Client({
  region: "auto",
  endpoint: "https://f2cf3a00591888eeaac9767d3bc1881a.r2.cloudflarestorage.com",
  forcePathStyle: true,
  credentials: {
    accessKeyId: "7476a0b5b996b0246f2b0f1fb2d3eeb8",
    secretAccessKey:
      "1f68525330a3f3241abc7361a6dc6eb3abd6740d8fa2cc875dc9b079418b8073"
  }
});

// =====================
// ⏱ HELPER
// =====================
const now = () => new Date().toISOString();

const log = (msg) => {
  console.log(`[${now()}] ${msg}`);
};

// =====================
// 🔥 STREAM → FILE
// =====================
const streamToFile = (stream, filePath) =>
  new Promise((resolve, reject) => {
    const write = fs.createWriteStream(filePath);
    stream.pipe(write);
    stream.on("error", reject);
    write.on("finish", resolve);
  });

// =====================
// 🔥 UPLOAD STREAM
// =====================
const uploadToR2 = async (key, filePath, contentType) => {
  log(`⬆️ Uploading: ${key}`);

  const stream = fs.createReadStream(filePath);

  await s3.send(
    new PutObjectCommand({
      Bucket: BUCKET,
      Key: key,
      Body: stream,
      ContentType: contentType
    })
  );

  log(`✅ Uploaded: ${key}`);
};

// =====================
// 🔥 RUN FFMPEG (ANTI TREO)
// =====================
const runFFmpeg = (args, label = "") =>
  new Promise((resolve, reject) => {
    log(`🎬 FFmpeg start: ${label}`);

    const start = Date.now();
    const ff = spawn(ffmpegPath, args);

    ff.stderr.on("data", (d) => {
      console.log(`[ffmpeg ${label}]`, d.toString());
    });

    const timeout = setTimeout(
      () => {
        ff.kill("SIGKILL");
        reject(new Error(`FFmpeg timeout: ${label}`));
      },
      90 * 60 * 1000
    ); // 15 phút

    ff.on("close", (code) => {
      clearTimeout(timeout);

      const time = ((Date.now() - start) / 1000).toFixed(2);
      log(`🎬 FFmpeg done (${label}) in ${time}s`);

      if (code === 0) resolve();
      else reject(new Error(`FFmpeg failed (${label}): ${code}`));
    });

    ff.on("error", reject);
  });

// =====================
// 🔥 METADATA
// =====================
const getVideoMetadata = (input) =>
  new Promise((resolve, reject) => {
    log("📊 Reading metadata...");

    const ff = spawn(ffprobePath.path, [
      "-v",
      "error",
      "-print_format",
      "json",
      "-show_streams",
      "-show_format",
      input
    ]);

    let data = "";
    ff.stdout.on("data", (c) => (data += c));

    ff.on("close", () => {
      try {
        const json = JSON.parse(data);
        const video = json.streams.find((s) => s.codec_type === "video");

        const result = {
          width: video?.width || 1280,
          height: video?.height || 720,
          duration: parseFloat(json.format.duration || 0)
        };

        log(`📊 Metadata: ${JSON.stringify(result)}`);
        resolve(result);
      } catch (e) {
        reject(e);
      }
    });

    ff.on("error", reject);
  });

// =====================
// 🔥 WATERMARK SVG
// =====================
const createWatermarkSvg = (width, height, opacity = 0.25) => {
  const patternSize = Math.floor(width / 2.5);
  const fontSize = Math.floor(width / 14);

  return `
  <svg width="${width}" height="${height}">
    <defs>
      <pattern id="wm" width="${patternSize}" height="${patternSize}" patternUnits="userSpaceOnUse" patternTransform="rotate(-30)">
        <text x="${patternSize * 0.1}" y="${patternSize * 0.6}" font-size="${fontSize}" fill="white" opacity="${opacity}" font-weight="bold">
          VJSHOW
        </text>
      </pattern>
    </defs>
    <rect width="100%" height="100%" fill="url(#wm)" />
  </svg>
  `;
};

// =====================
// 🚀 MAIN
// =====================
export const processVideo = async ({ key }) => {
  const tmp = os.tmpdir();
  const id = Date.now();

  const input = path.join(tmp, `input-${id}.mp4`);
  const preview = path.join(tmp, `preview-${id}.mp4`);
  const full = path.join(tmp, `full-${id}.mp4`);
  const thumbRaw = path.join(tmp, `thumb-raw-${id}.jpg`);
  const thumb = path.join(tmp, `thumb-${id}.jpg`);
  const wmImage = path.join(tmp, `wm-${id}.png`);
  const hlsDir = path.join(tmp, `hls-${id}`);

  try {
    log(`🎬 START processing: ${key}`);

    // =====================
    // DOWNLOAD
    // =====================
    log("⬇️ Downloading video...");
    const object = await s3.send(
      new GetObjectCommand({ Bucket: BUCKET, Key: key })
    );
    await streamToFile(object.Body, input);
    log(`✅ Downloaded: ${input}`);

    // =====================
    // METADATA
    // =====================
    const { width, height, duration } = await getVideoMetadata(input);

    // =====================
    // WATERMARK
    // =====================
    log("🎨 Creating watermark...");
    const svg = createWatermarkSvg(width, height, 0.25);
    await sharp(Buffer.from(svg)).png().toFile(wmImage);

    // =====================
    // PREVIEW
    // =====================
    await runFFmpeg(
      [
        "-i",
        input,
        "-i",
        wmImage,
        "-t",
        "10",
        "-filter_complex",
        "[0:v]scale=iw*0.5:ih*0.5[v0];[v0][1:v]overlay=0:0",
        "-c:v",
        "libx264",
        "-preset",
        "ultrafast",
        "-crf",
        "30",
        "-an",
        preview
      ],
      "preview"
    );

    const previewKey = key
      .replace("original/", "preview/")
      .replace(/\.\w+$/, ".mp4");
    await uploadToR2(previewKey, preview, "video/mp4");

    // =====================
    // FULL VIDEO
    // =====================
    await runFFmpeg(
      [
        "-i",
        input,
        "-i",
        wmImage,
        "-filter_complex",
        "[0:v][1:v] overlay=0:0",
        "-c:v",
        "libx264",
        "-preset",
        "veryfast",
        "-crf",
        "23",
        "-c:a",
        "aac",
        "-b:a",
        "128k",
        full
      ],
      "full"
    );

    // =====================
    // THUMB
    // =====================
    log("🖼 Creating thumbnail...");
    const captureTime = Math.max(1, Math.floor(duration / 3));

    await runFFmpeg(
      ["-i", input, "-ss", `${captureTime}`, "-vframes", "1", thumbRaw],
      "thumb"
    );

    const img = sharp(fs.readFileSync(thumbRaw));
    const meta = await img.metadata();

    const svgThumb = createWatermarkSvg(meta.width, meta.height, 0.25);

    const finalThumb = await img
      .composite([{ input: Buffer.from(svgThumb) }])
      .jpeg({ quality: 90 })
      .toBuffer();

    fs.writeFileSync(thumb, finalThumb);

    const thumbKey = key
      .replace("original/", "thumbnails/")
      .replace(/\.\w+$/, ".jpg");
    await uploadToR2(thumbKey, thumb, "image/jpeg");

    // =====================
    // HLS
    // =====================
    log("📺 Generating HLS...");
    fs.mkdirSync(hlsDir);

    await runFFmpeg(
      [
        "-i",
        full,
        "-preset",
        "ultrafast",
        "-g",
        "48",
        "-sc_threshold",
        "0",
        "-map",
        "0:v",
        "-map",
        "0:a?",
        "-c:v",
        "libx264",
        "-c:a",
        "aac",
        "-f",
        "hls",
        "-hls_time",
        "10",
        "-hls_playlist_type",
        "vod",
        "-hls_segment_filename",
        `${hlsDir}/segment_%03d.ts`,
        `${hlsDir}/index.m3u8`
      ],
      "hls"
    );

    const files = fs.readdirSync(hlsDir);

    for (const file of files) {
      const filePath = path.join(hlsDir, file);

      const keyHls = key
        .replace("original/", "hls/")
        .replace(/\.\w+$/, `/${file}`);

      const contentType = file.endsWith(".m3u8")
        ? "application/vnd.apple.mpegurl"
        : "video/mp2t";

      await uploadToR2(keyHls, filePath, contentType);
    }

    const hlsKey = key
      .replace("original/", "hls/")
      .replace(/\.\w+$/, "/index.m3u8");

    log("🎉 DONE ALL");

    return {
      previewUrl: previewKey,
      thumbUrl: thumbKey,
      hlsUrl: hlsKey,
      width,
      height,
      duration
    };
  } catch (err) {
    log(`❌ ERROR: ${err.message}`);
    throw err;
  } finally {
    log("🧹 Cleaning up temp files...");

    const files = [input, preview, full, thumbRaw, thumb, wmImage];

    files.forEach((f) => {
      if (fs.existsSync(f)) {
        try {
          fs.unlinkSync(f);
          log(`🗑 Deleted: ${f}`);
        } catch (e) {
          log(`⚠️ Cannot delete: ${f}`);
        }
      }
    });

    if (fs.existsSync(hlsDir)) {
      try {
        fs.readdirSync(hlsDir).forEach((f) => {
          const p = path.join(hlsDir, f);
          fs.unlinkSync(p);
        });
        fs.rmdirSync(hlsDir);
        log("🗑 Deleted HLS temp folder");
      } catch (e) {
        log("⚠️ Cannot delete HLS folder");
      }
    }
  }
};
