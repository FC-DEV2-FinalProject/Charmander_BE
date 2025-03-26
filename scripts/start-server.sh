#!/bin/bash

set -e

IMAGE_NAME="536697268731.dkr.ecr.ap-northeast-2.amazonaws.com/charmander-api-server-ecr:latest"
NGINX_CONF="/etc/nginx/nginx.conf"
APP_NAME="charmander-api-server-ecr"

echo "--------------- 블루/그린 배포 스크립트 시작 -----------------"

CURRENT_PORT=0

if curl -s http://localhost:8080/actuator/health | grep -q '"status":"UP"'; then
  CURRENT_PORT=8080
elif curl -s http://localhost:8081/actuator/health | grep -q '"status":"UP"'; then
  CURRENT_PORT=8081
fi

if [ $CURRENT_PORT -eq 0 ]; then
  CURRENT_PORT=8081
fi

# 반대 포트를 IDLE_PORT로 결정
if [ $CURRENT_PORT -eq 8080 ]; then
  IDLE_PORT=8081
else
  IDLE_PORT=8080
fi

########################################
# 2) 최신 Docker 이미지 Pull
########################################
echo "[1/5] 최신 Docker 이미지를 pull: $IMAGE_NAME"
docker pull $IMAGE_NAME

########################################
# 3) 새 컨테이너 실행 (IDLE_PORT)
########################################
echo "[2/5] 새 컨테이너를 포트 $IDLE_PORT 에 실행..."

# 혹시 이미 동일 이름의 컨테이너가 있으면 정리
docker stop ${APP_NAME}-$IDLE_PORT 2>/dev/null || true
docker rm   ${APP_NAME}-$IDLE_PORT 2>/dev/null || true

# 새 컨테이너 실행: 컨테이너 내부 포트는 8080, 호스트 포트는 IDLE_PORT
docker run -d \
  --name "${APP_NAME}-$IDLE_PORT" \
  -p $IDLE_PORT:8080 \
  -e SPRING_PROFILES_ACTIVE=aws-secretsmanager,prod \
  $IMAGE_NAME

########################################
# 4) 새 컨테이너 헬스체크 (최대 60초, 2초 간격)
########################################
echo "[3/5] 새 컨테이너 헬스체크 중 (포트 $IDLE_PORT)..."
HEALTHY=false
for i in {1..30}; do
  if curl -s http://localhost:$IDLE_PORT/actuator/health | grep -q '"status":"UP"'; then
    echo " > 헬스체크 성공!"
    HEALTHY=true
    break
  else
    echo " > 헬스체크 대기 중... ($i/30)"
    sleep 2
  fi
done

if [ "$HEALTHY" != "true" ]; then
  echo "[Error] 새 컨테이너가 정상 구동되지 않음. 롤백 진행"
  docker stop ${APP_NAME}-$IDLE_PORT
  docker rm ${APP_NAME}-$IDLE_PORT
  exit 1
fi

########################################
# 5) Nginx 설정 수정 (proxy_pass 변경) & reload
########################################
echo "[4/5] Nginx proxy_pass를 $CURRENT_PORT → $IDLE_PORT 로 변경..."

# sed 명령으로 nginx.conf 내 proxy_pass 포트를 치환합니다.
sudo sed -i "s|proxy_pass http://localhost:$CURRENT_PORT;|proxy_pass http://localhost:$IDLE_PORT;|g" $NGINX_CONF

# Nginx 설정 문법 체크
sudo nginx -t
if [ $? -ne 0 ]; then
  echo "[Error] Nginx 설정에 오류가 발생. 원복을 시도."
  sudo sed -i "s|proxy_pass http://localhost:$IDLE_PORT;|proxy_pass http://localhost:$CURRENT_PORT;|g" $NGINX_CONF
  sudo nginx -t && sudo nginx -s reload
  docker stop ${APP_NAME}-$IDLE_PORT
  docker rm ${APP_NAME}-$IDLE_PORT
  exit 1
fi

# Nginx reload (무중단 reload)
sudo nginx -s reload
echo " > Nginx가 포트 $IDLE_PORT 로 트래픽을 전환 완료."

########################################
# 6) 기존 컨테이너 종료 및 제거
########################################
echo "[5/5] 기존 컨테이너(포트 $CURRENT_PORT) 중지 및 제거..."
docker stop ${APP_NAME}-$CURRENT_PORT 2>/dev/null || true
docker rm   ${APP_NAME}-$CURRENT_PORT 2>/dev/null || true

echo "--------------- 블루/그린 배포 완료! 현재 활성 포트: $IDLE_PORT -----------------"
exit 0
