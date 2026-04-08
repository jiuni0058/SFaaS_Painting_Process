package com.team1.sfaas.service;

import com.team1.sfaas.mapper.DetailMapper;
import com.team1.sfaas.model.DetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailService {

    private final DetailMapper detailMapper;

    @Autowired
    public DetailService(DetailMapper detailMapper){
        this.detailMapper = detailMapper;
    }

    public  List<DetailModel> getMachine(){
        return detailMapper.getMachineData();
    }

    public DetailModel getMachineName(String machine_name){
        return detailMapper.getMachineName(machine_name);
    }
    public List<DetailModel> getMachineInformation(String machine_id){
        return detailMapper.getMachineInformation(machine_id);
    }
    public List<DetailModel> getDetail(String machine_id) {
        List<DetailModel> detailData;
        detailData = detailMapper.getPLCData(machine_id);

        return detailData;
    }
    public double getUtilizationLastHour(String machine_id) {
        long downtimeSeconds = detailMapper.getDowntimeSecondsLastHour(machine_id);
        int totalSecondsInHour = 3600;

        // 비가동 시간이 1시간을 초과할 경우 0%로 처리
        if (downtimeSeconds >= totalSecondsInHour) {
            return 0.0;
        }

        long uptimeSeconds = totalSecondsInHour - downtimeSeconds;
        // (가동 시간 / 전체 시간) * 100
        return (double) uptimeSeconds / totalSecondsInHour * 100.0;
    }
}