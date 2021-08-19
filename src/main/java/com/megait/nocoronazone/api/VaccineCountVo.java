package com.megait.nocoronazone.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor @NoArgsConstructor
public class VaccineCountVo {
    private int todayFirstCnt;
    private int todaySecondCnt;
    private int totalFirstCnt;
    private int totalSecondCnt;
}
