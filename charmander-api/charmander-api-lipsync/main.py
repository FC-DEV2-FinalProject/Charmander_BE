from logging import Formatter, StreamHandler, getLogger
from tempfile import TemporaryDirectory

from ai import wav2lip
from flask import Flask, request

logger = getLogger(__name__)
logger.setLevel("INFO")
logger.addHandler(StreamHandler())
formatter = Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
for handler in logger.handlers:
    handler.setFormatter(formatter)

logger.info("Initializing Wav2Lip application")

app = Flask(__name__)


@app.post("/generate")
def generate():
    with TemporaryDirectory() as tmpdir:
        video_path = f"{tmpdir}/video.mp4"
        audio_path = f"{tmpdir}/audio.wav"
        output_path = f"{tmpdir}/output.mp4"

        with open(video_path, "wb") as video_file:
            video_file.write(request.files["video"].read())
        with open(audio_path, "wb") as audio_file:
            audio_file.write(request.files["audio"].read())

        logger.info(f"Received video: {video_path}, audio: {audio_path}")
        wav2lip.generate(video_path, audio_path, output_path)

        with open(output_path, "rb") as output_file:
            return output_file.read(), {"Content-Type": "video/mp4"}
