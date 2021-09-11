package com.megait.nocoronazone.form;

import lombok.Data;

@Data
public class LocationSearchForm {

    private Double latitude;

    private Double longitude;

    private String location;

    private int radius;



}
