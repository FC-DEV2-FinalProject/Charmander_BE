FROM python:3.6-slim

ARG DEBIAN_FRONTEND=noninteractive

ENV FLASK_APP=main
ENV PORT=5000

WORKDIR /app

RUN apt-get update && \
    apt-get install -y \
    ffmpeg build-essential \
    libsm6 libxext6 libxrender-dev && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY ./requirements.txt /app/requirements.txt

RUN pip install --no-cache-dir -r /app/requirements.txt

COPY . /app

EXPOSE $PORT

VOLUME [ "/app/assets" ]
VOLUME [ "/app/checkpoints" ]
VOLUME [ "/app/results" ]

CMD ["flask", "run", "--host", "0.0.0.0", "--port", "$PORT"]