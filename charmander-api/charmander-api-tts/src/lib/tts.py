from abc import ABC, abstractmethod

from ..model.tts import TTSRequest


class TTSError(Exception):
    def __init__(self, cause: Exception):
        self.message = f"An error occurred while generating TTS"
        self.cause = cause


class TTSEngine(ABC):
    @abstractmethod
    def speak(self, request: TTSRequest) -> bytes:
        pass


class GTTS(TTSEngine):
    from gtts import gTTS

    def speak(self, request: TTSRequest):
        try:
            tts = self.gTTS(text=request.text, lang='ko')
            return b''.join(tts.stream())
        except Exception as e:
            raise TTSError(e)
