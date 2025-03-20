#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop charmander-api-server-ecr || true
docker rm charmander-api-server-ecr || true
docker pull 536697268731.dkr.ecr.ap-northeast-2.amazonaws.com/charmander-api-server-ecr:latest
docker run -d --name charmander-api-server-ecr -p 8080:8080 536697268731.dkr.ecr.ap-northeast-2.amazonaws.com/charmander-api-server-ecr/charmander-api-server-ecr:latest
echo "--------------- 서버 배포 끝 -----------------"
