package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.DetailSafetyIndex;
import com.megait.nocoronazone.repository.DetailSafetyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetailSafetyService {

    private final DetailSafetyRepository detailSafetyRepository;


//    public int getDetailSafetytoAlpha(String district) {
////        int detailSafetyIndexMax = detailSafetyRepository.maxIndex();
//
////        System.out.println(detailSafetyIndexMax);
//        return Integer.parseInt(String.valueOf(detailSafetyRepository.findIndexByDistrict(district)));
//    }

}
