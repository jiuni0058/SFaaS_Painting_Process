package com.team1.sfaas.mapper;

import com.team1.sfaas.model.DowntimeLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface DowntimeMapper {
    // KPI: 금일 총 다운타임 (분)
    Long selectTotalDowntimeToday(@Param("machineId") String machineId);

    // KPI: 가장 빈번한 에러
    String selectMostFrequentError(@Param("machineId") String machineId);

    // KPI: 최장 다운타임 설비
    String selectLongestDowntimeMachine();

    // 주간 다운타임 발생 추이
    List<Map<String, Object>> selectWeeklyDowntimeTrends(@Param("machineId") String machineId);

    // 상세 다운타임 로그
    List<DowntimeLog> selectDowntimeLogs(@Param("machineId") String machineId);

    // 설비별 에러 분포
    List<Map<String, Object>> selectErrorDistributionByMachine();

    // 기종별 에러 분포
    List<Map<String, Object>> selectErrorDistributionByType();
}