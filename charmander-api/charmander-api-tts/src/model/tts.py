from enum import Enum
from typing import Optional

from pydantic import BaseModel, Field


class TTSStatus(Enum):
    PENDING = 'pending'
    PROCESSING = 'processing'
    ERROR = 'error'
    DONE = 'done'


class TTSRequest(BaseModel):
    text: str
    lang: str = 'ko'
    callback: str = Field(pattern=r'^https?://', description='Callback URL')


class TTSResponse(BaseModel):
    status: TTSStatus
    obj_key: Optional[str] = None
    download_url: Optional[str] = None
    message: Optional[str] = None

    @staticmethod
    def pending() -> 'TTSResponse':
        return TTSResponse(
            status=TTSStatus.PENDING
        )

    @staticmethod
    def success(obj_key: str, download_url: str) -> 'TTSResponse':
        return TTSResponse(
            status=TTSStatus.DONE,
            obj_key=obj_key,
            download_url=download_url
        )

    @staticmethod
    def failure(message: str) -> 'TTSResponse':
        return TTSResponse(
            status=TTSStatus.ERROR,
            message=message
        )
