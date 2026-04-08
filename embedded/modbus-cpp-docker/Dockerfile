# ----- 1단계: 빌드(Build) 환경 -----
FROM --platform=linux/arm64 arm64v8/gcc:12.3.0 AS builder

# [수정] pkg-config를 추가로 설치합니다.
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y libmodbus-dev pkg-config

# 소스 코드를 컨테이너 안으로 복사
WORKDIR /app
COPY src/ .

# [수정] pkg-config를 사용하여 컴파일 옵션을 자동으로 설정합니다.
RUN gcc -o app main.c $(pkg-config --cflags --libs libmodbus)


# ----- 2단계: 최종 실행(Final) 환경 -----
# (이 부분은 수정할 필요 없습니다)
FROM --platform=linux/arm64 ubuntu:22.04

COPY --from=builder /usr/lib/aarch64-linux-gnu/libmodbus.so.5 /usr/lib/aarch64-linux-gnu/
COPY --from=builder /app/app .

CMD ["./app"]