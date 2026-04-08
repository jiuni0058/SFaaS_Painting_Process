package com.team1.sfaas.controller;

import com.team1.sfaas.model.DowntimeLog;
import com.team1.sfaas.model.DowntimeKpi;
import com.team1.sfaas.model.ChartData;
import com.team1.sfaas.service.DowntimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/downtime")
public class DowntimeController {

    private final DowntimeService downtimeService;

    public DowntimeController(DowntimeService downtimeService) {
        this.downtimeService = downtimeService;
    }

    @GetMapping("/kpi")
    public DowntimeKpi getKpiData(@RequestParam(defaultValue = "all") String machine_id) {
        return downtimeService.getKpiData(machine_id);
    }

    @GetMapping("/trends")
    public ChartData getWeeklyTrends(@RequestParam(defaultValue = "all") String machine_id) {
        return downtimeService.getWeeklyTrends(machine_id);
    }

    @GetMapping("/logs")
    public List<DowntimeLog> getDowntimeLogs(@RequestParam(defaultValue = "all") String machine_id) {
        return downtimeService.getDowntimeLogs(machine_id);
    }

    @GetMapping("/errors/machine")
    public Map<String, Long> getErrorDistributionByMachine(@RequestParam(defaultValue = "all") String machine_id) {
        // machine_id는 현재 로직에서 사용되지 않지만, 향후 확장을 위해 파라미터를 유지합니다.
        return downtimeService.getErrorDistributionByMachine();
    }

    @GetMapping("/errors/type")
    public Map<String, Long> getErrorDistributionByType(@RequestParam(defaultValue = "all") String machine_id) {
        // machine_id는 현재 로직에서 사용되지 않지만, 향후 확장을 위해 파라미터를 유지합니다.
        return downtimeService.getErrorDistributionByType();
    }
}