#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop charmander-dev-server-ecr || true
docker rm charmander-dev-server-ecr || true
docker pull {536697268731.dkr.ecr.ap-northeast-2.amazonaws.com/charmander-dev-server-ecr}/charmander-dev-server-ecr:latest
docker run -d --name charmander-dev-server-ecr -p 8080:8080 {536697268731.dkr.ecr.ap-northeast-2.amazonaws.com/charmander-dev-server-ecr}/charmander-dev-server-ecr:latest
echo "--------------- 서버 배포 끝 -----------------"
