import os

from .lib.queue import LocalTaskQueue, TaskQueue
from .lib.http import HTTPClient
from .lib.s3 import S3Storage
from .lib.tts import GTTS, TTSEngine

s3 = S3Storage(
    access_key=os.environ.get("S3_ACCESS_KEY"),
    secret_key=os.environ.get("S3_SECRET_KEY"),
    bucket_name=os.environ.get("S3_BUCKET_NAME", "tts"),
    region=os.environ.get("S3_REGION", "ap-northeast-2"),
    endpoint_url=os.environ.get("S3_ENDPOINT_URL", "https://tts.s3.ap-northeast-2.amazonaws.com")
)
tts: TTSEngine = GTTS()
http = HTTPClient()
queue: TaskQueue = LocalTaskQueue()