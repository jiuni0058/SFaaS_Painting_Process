package com.team1.sfaas.service;

import com.team1.sfaas.mapper.DowntimeMapper;
import com.team1.sfaas.model.DowntimeLog;
import com.team1.sfaas.model.DowntimeKpi;
import com.team1.sfaas.model.ChartData;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class DowntimeService {

    private final DowntimeMapper downtimeMapper;

    public DowntimeService(DowntimeMapper downtimeMapper) {
        this.downtimeMapper = downtimeMapper;
    }

    public DowntimeKpi getKpiData(String machineId) {
        Long totalDowntime = downtimeMapper.selectTotalDowntimeToday(machineId);
        String mostFrequentError = downtimeMapper.selectMostFrequentError(machineId);
        String longestDowntimeMachine = downtimeMapper.selectLongestDowntimeMachine();

        return new DowntimeKpi(
                totalDowntime != null ? totalDowntime : 0,
                mostFrequentError != null ? mostFrequentError : "N/A",
                longestDowntimeMachine != null ? longestDowntimeMachine : "N/A"
        );
    }

    public ChartData getWeeklyTrends(String machineId) {
        List<Map<String, Object>> trends = downtimeMapper.selectWeeklyDowntimeTrends(machineId);
        ChartData chartData = new ChartData();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        // 지난 7일간의 날짜 레이블 생성
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            labels.add(today.minusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE));
        }

        // 데이터 채우기 (해당 날짜에 데이터 없으면 0)
        Map<String, Long> trendMap = trends.stream()
                .collect(Collectors.toMap(
                        m -> m.get("day_label").toString(),
                        m -> ((Number) m.get("total_minutes")).longValue()
                ));

        for (String label : labels) {
            data.add(trendMap.getOrDefault(label, 0L));
        }

        chartData.setLabels(labels);
        chartData.setData(data);
        return chartData;
    }

    public List<DowntimeLog> getDowntimeLogs(String machineId) {
        return downtimeMapper.selectDowntimeLogs(machineId);
    }

    public Map<String, Long> getErrorDistributionByMachine() {
        return downtimeMapper.selectErrorDistributionByMachine().stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("name"),
                        m -> (Long) m.get("value")
                ));
    }

    public Map<String, Long> getErrorDistributionByType() {
        return downtimeMapper.selectErrorDistributionByType().stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("name"),
                        m -> (Long) m.get("value")
                ));
    }
}