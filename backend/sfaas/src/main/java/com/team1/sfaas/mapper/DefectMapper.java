// src/main/java/com/team1/sfaas/mapper/DefectMapper.java
package com.team1.sfaas.mapper;

import com.team1.sfaas.model.DefectLog;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface DefectMapper {
    // KPI: 금일 총 검수량
    long selectTotalInspectionsToday();

    // KPI: 금일 불량 건수
    long selectTotalDefectsToday();

    // KPI: 최근 7일간 총 검수량
    long selectTotalInspectionsLast7Days();

    // KPI: 최근 7일간 총 불량 건수
    long selectTotalDefectsLast7Days();

    // KPI: 최다 발생 불량 유형
    String selectMostFrequentDefect();

    // 차트: 일별 불량 발생 추이
    List<Map<String, Object>> selectDailyDefectTrends();

    // 차트: 불량 유형별 분포
    List<Map<String, Object>> selectDefectDistributionByType();

    // 차트: 설비별 불량 발생 건수
    List<Map<String, Object>> selectDefectDistributionByMachine();

    // 상세 불량 내역 로그
    List<DefectLog> selectDefectLogs();
}