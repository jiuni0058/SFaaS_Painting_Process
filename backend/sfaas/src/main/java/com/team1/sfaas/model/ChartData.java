package com.team1.sfaas.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ChartData {
    // BarChart용
    private List<String> labels;
    private List<Long> data;

    // DoughnutChart용
    private Map<String, Long> distribution;
}