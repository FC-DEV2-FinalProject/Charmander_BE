from fastapi import FastAPI
from dotenv import load_dotenv

from src.routes.api import router as api_router

load_dotenv(override=True)

app = FastAPI()

app.include_router(api_router, prefix="/api")
