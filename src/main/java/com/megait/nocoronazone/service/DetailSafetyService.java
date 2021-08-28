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


    public double getDetailSafetytoAlpha(String district) {
        double detailSafetyIndexMax = detailSafetyRepository.maxIndex();
        double getIndexByDistrict = detailSafetyRepository.getById(district).getIndex();

        double result = getIndexByDistrict / detailSafetyIndexMax ;

        return result;
    }

    public List<DetailSafetyIndex> getDetailSafetyIndexList() {
        List<DetailSafetyIndex> detailSafetyIndexList = detailSafetyRepository.findAll();
        return detailSafetyIndexList;
    }

}
