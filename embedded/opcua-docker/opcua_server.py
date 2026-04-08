import time
import threading
from opcua import Server
from opcua import ua
import sys

server = Server()

# 서버 네임스페이스 설정
url = "opc.tcp://0.0.0.0:4001"
server.set_endpoint(url)

name = "OPCUA_PYTHON_SERVER"
addspace = server.register_namespace(name)

node = server.get_objects_node()

# 객체 생성
obj = node.add_object(addspace, "MyObject")

# 변수 추가 (여기서는 "Temperature"라는 변수 추가)
# Variant를 사용하여 값을 지정하고, 데이터 타입은 "Int16"
temperature = obj.add_variable(addspace, "Temperature", ua.Variant(0, ua.VariantType.Int16))
humidity = obj.add_variable(addspace, "Humidity", ua.Variant(0, ua.VariantType.Int16))

# 이 변수 값을 읽고 쓸 수 있도록 설정
temperature.set_writable()  # 이 변수는 쓰기 가능
humidity.set_writable()

# 서버 시작
server.start()

print("서버가 시작되었습니다. Ctrl + C를 눌러 종료하세요.")

# 주기적으로 Temperature 값을 증가시키는 함수
def increase_temperature():
    while True:
        current_temperature = temperature.get_value()
        temperature.set_value(current_temperature + 1)
        time.sleep(1)  # 1초 주기로 값 증가

# 주기적인 작업을 위한 스레드 생성
temperature_thread = threading.Thread(target=increase_temperature, daemon=True)
temperature_thread.start()

# 예외 처리하여 Ctrl + C로 서버를 종료하도록 하기
try:
    while True:
        pass  # 서버는 계속 실행되고 클라이언트 요청을 처리함

except KeyboardInterrupt:
    print("Ctrl + C가 눌렸습니다. 서버를 종료합니다.")
    server.stop()
    print("서버가 종료되었습니다.")
    sys.exit(0)
