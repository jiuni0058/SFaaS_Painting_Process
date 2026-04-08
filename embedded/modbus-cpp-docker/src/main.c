#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <modbus.h>
#include <time.h>

int getTemperature(void) {
    static int temp = 250; // 25.0도 (0.1도 단위)
    static int initialized = 0;
    if (!initialized) {
        srand((unsigned int)time(NULL));
        initialized = 1;
    }
    int delta = (rand() % 5) - 2; // -2 ~ +2
    temp += delta;
    if (temp < 150) temp = 150; // 15.0도
    if (temp > 350) temp = 350; // 35.0도
    return temp;
}

int getHumid(void) {
    static int humid = 500; // 50.0% (0.1% 단위)
    static int initialized = 0;
    if (!initialized) {
        srand((unsigned int)time(NULL) + 1);
        initialized = 1;
    }
    int delta = (rand() % 5) - 2; // -2 ~ +2
    humid += delta;
    if (humid < 300) humid = 300; // 30.0%
    if (humid > 800) humid = 800; // 80.0%
    return humid;
}

int main(void) {
    modbus_t *ctx;
    modbus_mapping_t *mb_mapping;
    int rc;

    // Modbus TCP 컨텍스트 생성 (IP 주소: 0.0.0.0, 포트: 502)
    // 0.0.0.0은 모든 네트워크 인터페이스에서 연결을 허용한다는 의미입니다.
    ctx = modbus_new_tcp("0.0.0.0", 502);
    if (ctx == NULL) {
        fprintf(stderr, "Unable to create the libmodbus context: %s\n", modbus_strerror(errno));
        return 1;
    }

    // Modbus 맵핑 생성
    // 0~9 Holding Registers를 가상으로 생성 (총 10개)
    mb_mapping = modbus_mapping_new(0, 0, 10, 0);
    if (mb_mapping == NULL) {
        fprintf(stderr, "Failed to create the mapping: %s\n", modbus_strerror(errno));
        modbus_free(ctx);
        return 1;
    }

    // 서버 소켓 바인딩 및 리스닝
    int server_socket = modbus_tcp_listen(ctx, 1);
    if (server_socket == -1) {
        fprintf(stderr, "Failed to listen: %s\n", modbus_strerror(errno));
        modbus_mapping_free(mb_mapping);
        modbus_free(ctx);
        return 1;
    }

    printf("Modbus TCP Server is running on port 502...\n");

    while (1) {
        modbus_t *slave_ctx;
        uint8_t query[MODBUS_TCP_MAX_ADU_LENGTH];

        // 클라이언트 연결 수락
        modbus_tcp_accept(ctx, &server_socket);
        printf("Client connected.\n");

        while (1) {
            // 클라이언트로부터 요청 수신
            rc = modbus_receive(ctx, query);
            if (rc == -1) {
                // 연결이 끊어진 경우
                break;
            }

            // 가상의 값 설정 (예: 0번 Holding Register에 1234, 1번 Holding Register에 5678)
            mb_mapping->tab_registers[0] = 1234;
            mb_mapping->tab_registers[1] = 5678;
            mb_mapping->tab_registers[2] = 90; // 가상의 온도 값 (0.1도를 표현)
            mb_mapping->tab_registers[3] = 45; // 가상의 습도 값 (0.1도를 표현)


            // 요청에 대한 응답 전송
            modbus_reply(ctx, query, rc, mb_mapping);
        }
    }

    // 리소스 해제
    modbus_mapping_free(mb_mapping);
    modbus_free(ctx);

    return 0;
}