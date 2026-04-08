package com.team1.sfaas.service;

import com.team1.sfaas.mapper.DefectMapper;
import com.team1.sfaas.model.ChartData;
import com.team1.sfaas.model.DefectKpi;
import com.team1.sfaas.model.DefectLog;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefectService {

    private final DefectMapper defectMapper;

    public DefectService(DefectMapper defectMapper) {
        this.defectMapper = defectMapper;
    }

    public DefectKpi getDefectKpi() {
        long totalInspections = defectMapper.selectTotalInspectionsToday();
        long totalDefects = defectMapper.selectTotalDefectsToday();
        long inspectionsLast7Days = defectMapper.selectTotalInspectionsLast7Days();
        long defectsLast7Days = defectMapper.selectTotalDefectsLast7Days();
        String mostFrequentDefect = defectMapper.selectMostFrequentDefect();

        double defectRate = 0.0;
        if (inspectionsLast7Days > 0) {
            defectRate = Math.round(((double) defectsLast7Days / inspectionsLast7Days) * 1000) / 10.0;
        }

        return new DefectKpi(totalInspections, totalDefects, defectRate, mostFrequentDefect != null ? mostFrequentDefect : "N/A");
    }

    public ChartData getDailyDefectTrends() {
        List<Map<String, Object>> trends = defectMapper.selectDailyDefectTrends();
        ChartData chartData = new ChartData();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            labels.add(today.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd")));
        }

        Map<String, Long> trendMap = trends.stream()
                .collect(Collectors.toMap(
                        m -> LocalDate.parse(m.get("day_label").toString()).format(DateTimeFormatter.ofPattern("MM-dd")),
                        m -> ((Number) m.get("count")).longValue()
                ));

        for (String label : labels) {
            data.add(trendMap.getOrDefault(label, 0L));
        }

        chartData.setLabels(labels);
        chartData.setData(data);
        return chartData;
    }

    public Map<String, Long> getDefectDistributionByType() {
        return defectMapper.selectDefectDistributionByType().stream()
                .collect(Collectors.toMap(m -> (String) m.get("name"), m -> (Long) m.get("value")));
    }

    public Map<String, Long> getDefectDistributionByMachine() {
        return defectMapper.selectDefectDistributionByMachine().stream()
                .collect(Collectors.toMap(m -> (String) m.get("name"), m -> (Long) m.get("value")));
    }

    public List<DefectLog> getDefectLogs() {
        return defectMapper.selectDefectLogs();
    }
}