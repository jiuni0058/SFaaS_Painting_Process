package com.team1.sfaas.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DowntimeLog {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration; // 분 단위
    private String errorCode;
    private String errorName;
    private String machineName;
}