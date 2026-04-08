package com.team1.sfaas.model;

public class DetailModel {
    // ---- machine information
        // from machine table
    private String machine_id;
    private String machine_name;
    private int is_machine_run;
    private String machine_date;

    // ---- data information
        // from data table
    private String data_name;
    private double threshold_up;
    private double threshold_down;
        // from plc table
    private double value;
    private String plc_create_dt;

    // ---- for rate information
        // from downtime table
    private String start_dt;
    private String end_dt;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getMachine_name() {

        System.out.println("twsr"+machine_name);
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public int getIs_machine_run() {
        return is_machine_run;
    }

    public void setIs_machine_run(int is_machine_run) {
        this.is_machine_run = is_machine_run;
    }

    public String getMachine_date() {
        return machine_date;
    }

    public void setMachine_date(String getMachine_date) {
        this.machine_date = getMachine_date;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public double getThreshold_up() {
        return threshold_up;
    }

    public void setThreshold_up(double threshold_up) {
        this.threshold_up = threshold_up;
    }

    public double getThreshold_down() {
        return threshold_down;
    }

    public void setThreshold_down(double threshold_down) {
        this.threshold_down = threshold_down;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getPlc_create_dt() {
        return plc_create_dt;
    }

    public void setPlc_create_dt(String plc_create_dt) {
        this.plc_create_dt = plc_create_dt;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(String end_dt) {
        this.end_dt = end_dt;
    }


}
