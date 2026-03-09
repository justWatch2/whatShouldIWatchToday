# 🎬 What Should I Watch Today (Backend)

JustWatch2 프로젝트의 백엔드 서비스입니다. REST API, WebSocket 추천 결과 전송, 추천 작업 워커를 제공합니다.

## 🔗 레포지토리
- 프론트엔드: <FRONT_REPO_URL>
- 백엔드: 이 레포지토리

## ✨ 주요 기능
- 회원/콘텐츠/추천 관련 REST API
- 추천 결과 실시간 전송(WebSocket)
- RabbitMQ 소비 워커
- Redis pub/sub 기반 결과 전달

## 🧰 기술 스택
- Java 17 / Spring Boot 3
- MariaDB (운영은 RDS)
- Redis
- RabbitMQ
- WebSocket
- Docker / Docker Compose

## 🧩 아키텍처(요약)
- `backend-web`: API + WebSocket
- `backend-worker`: MQ 소비 워커
- `redis`: 결과 pub/sub 채널
- `rabbitmq`: 작업 큐

## 🧪 로컬 실행 (Docker)
1. Docker 실행
2. 로컬 서비스 실행:

```bash
docker compose -f docker-compose.local.yml up --build
```

- API: `http://localhost:8080`
- Health: `http://localhost:8080/api/health`
- RabbitMQ UI: `http://localhost:15672`

## ⚙️ 환경 변수
운영 값은 서버에서 관리하고, 로컬 값은 아래 파일에 있습니다.
- `deploy/env/web.env.local`
- `deploy/env/worker.env.local`

주요 변수:
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PORT`
- `RABBIT_HOST`, `RABBIT_PORT`, `RABBIT_USERNAME`, `RABBIT_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION_TIME`
- `CORS_ALLOWED_ORIGINS`, `RECOMMENDATION_WS_ALLOWED_ORIGINS`

## 🔌 WebSocket
- 엔드포인트: `/ws/recommend`
- 재연결 시 `requestId` 재바인딩 필요

## 🚀 CI/CD (백엔드)
- GitHub Actions에서 JAR 빌드 및 Docker 이미지 생성
- ECR에 이미지 업로드
- S3에 배포 번들 업로드
- CodeDeploy가 EC2에서 Docker Compose 실행

## 📝 참고
- web/worker는 같은 이미지이며 Spring 프로파일로 분리됩니다.
- 운영 도메인 변경 시 `CORS_ALLOWED_ORIGINS`, `RECOMMENDATION_WS_ALLOWED_ORIGINS` 값을 업데이트하세요.
