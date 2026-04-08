package com.team1.sfaas.controller;

import com.team1.sfaas.model.ChartData;
import com.team1.sfaas.model.DefectKpi;
import com.team1.sfaas.model.DefectLog;
import com.team1.sfaas.service.DefectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/defect")
public class DefectController {

    private final DefectService defectService;

    public DefectController(DefectService defectService) {
        this.defectService = defectService;
    }

    @GetMapping("/kpi")
    public DefectKpi getKpiData() {
        return defectService.getDefectKpi();
    }

    @GetMapping("/trends")
    public ChartData getDailyTrends() {
        return defectService.getDailyDefectTrends();
    }

    @GetMapping("/errors/type")
    public Map<String, Long> getDefectDistributionByType() {
        return defectService.getDefectDistributionByType();
    }

    @GetMapping("/errors/machine")
    public Map<String, Long> getDefectDistributionByMachine() {
        return defectService.getDefectDistributionByMachine();
    }

    @GetMapping("/logs")
    public List<DefectLog> getDefectLogs() {
        return defectService.getDefectLogs();
    }
}