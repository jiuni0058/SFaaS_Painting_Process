// src/main/java/com/team1/sfaas/model/DefectLog.java
package com.team1.sfaas.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DefectLog {
    private int id;
    private LocalDateTime inspectionTime; // 검수일시
    private String vin;                   // 차대번호
    private String color;                 // 색상
    private String errorName;             // 불량 유형
    private String machineName;           // 발생 설비
    private String inspectorName;         // 검수 담당자
}