import io
from fastapi.responses import StreamingResponse
from fastapi.routing import APIRouter

from src import context as ctx
from logging import getLogger

from .models import *
logger = getLogger(__name__)

router = APIRouter()

@router.on_event("startup")
def startup():
    ctx.tts.init()

@router.post("/speak/v1")
def speak(req: SpeakRequest.v1):
    logger.debug(f"Received speak request: {req}")

    audio = ctx.tts.synthesize(
        req.text,
        length_scale= 1.0 / req.params.speed
    )

    return StreamingResponse(io.BytesIO(audio.raw_bytes), media_type="audio/wav")


