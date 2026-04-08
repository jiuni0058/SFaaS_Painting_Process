package com.team1.sfaas.mapper;

import com.team1.sfaas.model.DetailModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetailMapper {
    // get dataName, dataValue, dataDate, dataThreshold
    List<DetailModel> getPLCData(@Param("machine_id") String machine_id);
    List<DetailModel> getMachineInformation(@Param("machine_id") String machine_id);
    List<DetailModel> getMachineData();
    DetailModel getMachineName(@Param("machine_name") String machine_name);
    long getDowntimeSecondsLastHour(@Param("machine_id") String machine_id);
}
