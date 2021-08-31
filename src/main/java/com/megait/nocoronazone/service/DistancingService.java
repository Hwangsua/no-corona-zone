package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.SocialDistancing;
import com.megait.nocoronazone.repository.DistancingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistancingService {

    private final DistancingRepository distancingRepository;

    public String getDistancing(String localName) {
        String socialDistancing = distancingRepository.getById(localName).getDistancingNumber();
        return socialDistancing;
    }

    public double getDistancingtoAlpha(String localName) {
        double distancingMax = Double.parseDouble(String.valueOf(distancingRepository.maxDistancing()));
        double getDistancingBylocalname = Double.parseDouble(String.valueOf(distancingRepository.getById(localName).getDistancingNumber()));

        double result = getDistancingBylocalname / distancingMax;

        return result;
    }

}
