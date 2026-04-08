// src/main/java/com/team1/sfaas/model/DefectKpi.java
package com.team1.sfaas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectKpi {
    private long totalInspections;      // 금일 총 검수량
    private long totalDefects;          // 금일 불량 건수
    private double defectRate;          // 최근 7일 불량률
    private String mostFrequentDefect;  // 최다 발생 불량 유형
}