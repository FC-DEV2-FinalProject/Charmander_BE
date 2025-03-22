from logging import getLogger

import requests
from fastapi.routing import APIRouter
from ..lib.tts import Audio
from .. import context as ctx

from .models import *

logger = getLogger(__name__)

router = APIRouter()

@router.on_event("startup")
def startup():
    ctx.tts.init()

def get_presigned_url(req: SpeakRequest.v1, part_number: int):
    res = requests.get(
        f"{req.serverUrl}/api/v1/pre-signed-url/{req.fileName}/{req.uploadId}/{part_number}",
    )
    return res.text


def complete_uplaod(req: SpeakRequest.v1, part_number: int, eTag: str):
    requests.post(
        f"{req.serverUrl}/api/v1/tasks/pre-signed-url/complete-upload",
        json={
            "fileName": req.fileName,
            "uploadId": req.uploadId,
            "parts": [
                {
                    "partNumber": part_number,
                    "eTag": eTag
                }
            ]
        }
    )

def do_multipart_upload(req: SpeakRequest.v1, audio: Audio):
    logger.info(f"uploading to S3. uploadId: {req.uploadId}, fileName: {req.fileName}")

    part_number = 1
    presinged_url = get_presigned_url(req, part_number)

    res = requests.put(
        presinged_url, 
        data=audio.raw_bytes, 
        headers={
            'Content-Type': 'audio/wav'
        }
    )
    eTag = res.headers['ETag']

    complete_uplaod(req, part_number, eTag)

def synthesize(req: SpeakRequest.v1):
    audio = ctx.tts.synthesize(req.text, length_scale=req.params.speed)
    return audio

@router.post("/speak/v1")
def speak(req: SpeakRequest.v1):
    logger.debug(f"Received speak request: {req}")
    
    audio = synthesize(req)
    do_multipart_upload(req, audio)

    return {
        "size": len(audio.raw_bytes),
        "sampleRate": audio.sample_rate,
        "duration": int(audio.duration * 1000)
    }


