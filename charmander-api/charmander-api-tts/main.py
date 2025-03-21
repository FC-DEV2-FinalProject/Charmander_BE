from dotenv import load_dotenv
load_dotenv(override=True)

#
from fastapi import FastAPI
from fastapi.responses import JSONResponse

from src.routes.api import router as router_api

app = FastAPI()

@app.exception_handler(RuntimeError)
async def runtime_error_handler(request, exc: RuntimeError):
    return JSONResponse(
        status_code=500,
        content={"message": str(exc)}
    )

@app.get("/health")
def health():
    return {"status": "ok"}

app.include_router(router_api, prefix="/api")


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000, log_level="info")