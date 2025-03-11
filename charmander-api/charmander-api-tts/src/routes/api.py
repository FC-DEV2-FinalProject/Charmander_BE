import logging
from datetime import datetime

from fastapi import APIRouter
from nanoid import generate as nanoid

from .. import context as ctx
from ..model.tts import TTSRequest, TTSResponse

logger = logging.getLogger(__name__)

router = APIRouter()

__all__ = ["router"]


def speak_task(request: TTSRequest) -> TTSResponse:
    try:
        logger.info(f"Generating TTS for {request.text}")

        stream = ctx.tts.speak(request)

        dt = datetime.now().strftime("%Y%m%d")
        id = nanoid()
        file_name = f"{dt}-{id}.wav"
        obj_key = ctx.s3.put_obj(f"{file_name}", stream)

        url = ctx.s3.get_presigned_url(obj_key)
        cb = request.callback

        ctx.http.post(cb, json=TTSResponse.success(obj_key, url))
    except Exception as e:
        logger.error(f"Failed to generate TTS for {request}", exc_info=e)
        ctx.http.post(cb, json=TTSResponse.failure(str(e)))


@router.post("/speak")
def speak(request: TTSRequest) -> TTSResponse:
    # TODO: use persistence and queue middleware
    ctx.queue.submit(speak_task, request)

    return TTSResponse.pending()


@router.get("/speak/{obj_key}")
async def get_speech(obj_key: str) -> bytes:
    try:
        return ctx.s3.get_obj(obj_key)
    except Exception as e:
        return str(e)
