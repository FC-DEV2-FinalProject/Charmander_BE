FROM python:3.7 AS builder

RUN pip install --no-cache poetry==1.5.1

WORKDIR /builder

COPY . /builder

RUN poetry export --with infer,api -f requirements.txt --output /builder/requirements.txt

# END: BUILD STAGE

FROM python:3.7-slim

ENV DEBIAN_FRONTEND=noninteractive

WORKDIR /app

RUN apt update &&  \
    apt install -y --no-install-recommends \
    espeak espeak-ng \
    ffmpeg && \
    apt clean && rm -rf /var/lib/apt/lists/*

COPY --from=builder /builder/requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY . /app

EXPOSE 8000

CMD ["python", "main.py"]
