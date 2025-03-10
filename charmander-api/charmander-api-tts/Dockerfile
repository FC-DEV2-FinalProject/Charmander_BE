FROM python:3.12-alpine AS builder

ARG VERSION_POETRY=2.0.1
ARG VERSION_POE=0.32.2

WORKDIR /builder

RUN pip install --no-cache-dir poetry==$VERSION_POETRY poethepoet==$VERSION_POE
COPY poetry.lock pyproject.toml /builder/
RUN poetry self add poetry-plugin-export
RUN poetry export -f requirements.txt > requirements.txt

##############################################

FROM python:3.12-alpine

ARG USER=app
ARG GROUP=app
ARG WORKDIR=/home/app

# non root user
RUN addgroup -S $GROUP && adduser -S $USER $GROUP
WORKDIR $WORKDIR
RUN chown -R $USER:$GROUP $WORKDIR
USER $USER:$GROUP

# copy files
COPY --from=builder /builder/requirements.txt $WORKDIR
COPY src $WORKDIR/src
COPY main.py $WORKDIR

RUN pip install --no-cache-dir -r requirements.txt

ENV PORT=8080
EXPOSE 8080

CMD [ "python", "-m", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "$PORT" ]