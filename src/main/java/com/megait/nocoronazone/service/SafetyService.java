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

    public int getConfirmedSUM() {
        String[] city = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종", "검역"};
        int confirmedSUM = 0;
        for (int i = 0; i < city.length; ++i) {
            int confirmed = safetyIndexRepository.getById(city[i]).getConfirmed();
            confirmedSUM += confirmed;
        }
        return confirmedSUM;
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
