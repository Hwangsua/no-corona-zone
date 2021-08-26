package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.SafetyIndex;
import com.megait.nocoronazone.repository.SafetyIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafetyService {

    private final SafetyIndexRepository safetyIndexRepository;

    public List<SafetyIndex> getSafetyList() {
        List<SafetyIndex> safetyList = safetyIndexRepository.findAll();
        return safetyList;
    }

    public double getSafety(String city) {
        return safetyIndexRepository.maxIndex();
    }

    public double getSafetytoAlpha(String city) {
        double safetyIndexMax = safetyIndexRepository.maxIndex();
        double getIndexByCity = safetyIndexRepository.getById(city).getIndex();

        double result = getIndexByCity / safetyIndexMax ;

        return result;
    }

    public double getConfirmedtoAlpha(String city) {
        int confirmedMax = safetyIndexRepository.maxConfirmed();
        int getConfirmedByCity = safetyIndexRepository.getById(city).getConfirmed();

        double result = (double)getConfirmedByCity / (double)confirmedMax * 10;

        return result;
    }


}
