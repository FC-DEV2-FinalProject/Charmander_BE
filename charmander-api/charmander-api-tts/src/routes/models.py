from pydantic import BaseModel, Field

class SpeakRequestParams:
    class v1(BaseModel):
        speed: float = Field(1.0, ge=0.5, le=2.0)

class SpeakRequest:
    class v1(BaseModel):
        text: str = Field(min_length=1, max_length=300)
        params: SpeakRequestParams.v1 = SpeakRequestParams.v1()
        serverUrl: str
        fullPath: str
        fileName: str
        uploadId: str

class SpeakResponse:
    class v1(BaseModel):
        audio: bytes