package com.team1.sfaas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DowntimeKpi {
    private long totalDowntime;
    private String mostFrequentError;
    private String longestDowntimeMachine;
}