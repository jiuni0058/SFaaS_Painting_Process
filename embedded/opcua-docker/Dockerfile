# 1. 베이스 이미지 선택 (ARM 아키텍처를 지원하는 python 이미지)
FROM python:3.9-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 필요한 라이브러리 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 4. 소스코드 복사
COPY opcua_server.py .

# 5. 서버 실행 명령어 (로그가 바로 보이도록 -u 옵션 추가)
CMD ["python", "-u", "opcua_server.py"]