package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.SocialDistancing;
import com.megait.nocoronazone.repository.DistancingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class DistancingSchduler {

    private final DistancingRepository distancingRepository;

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0 * * * * *")
    public void setSocialDistancingFile() {

        String socialCsvPath = "csv/social_distancing.csv";
        String socialBatPath = "sh/social_distancing.sh";
//        Runtime runtime = Runtime.getRuntime();
//        Process process = null;
        List<String> stringList;

        try {

//            new FileOutputStream(socialCsvPath).close();
//
//            String batPath = new File(socialBatPath).getCanonicalPath();
//
//            process = runtime.exec(batPath + " " +   new File(socialCsvPath).getCanonicalPath());
//
//            StringBuffer stdMsg = new StringBuffer();
//
//            ProcessOutputThread outputThread = new ProcessOutputThread(process.getInputStream(), stdMsg);
//            outputThread.start();
//
//            StringBuffer errMsg = new StringBuffer();
//
//            outputThread = new ProcessOutputThread(process.getErrorStream(),errMsg);
//            outputThread.start();
//
//            process.waitFor();
//            process.destroy();


            stringList = Files.readAllLines(Path.of(socialCsvPath), StandardCharsets.UTF_8);

            List<SocialDistancing> distancingList = new ArrayList<>();

            for(String s : stringList){
                String[] arr = s.replaceAll("^\"|\"$", "").split("\\|");
                SocialDistancing socialDistancing = SocialDistancing.builder()
                        .localName(arr[0].replace(new String(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF}), ""))
                        .distancingNumber(arr[1])
                        .build();
                distancingList.add(socialDistancing);
            }
            if (distancingRepository.findAll().isEmpty()){
                distancingRepository.saveAll(distancingList);
            }else{
                updateDistancing(distancingList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateDistancing(List<SocialDistancing> distancingList){
        for (SocialDistancing s : distancingList){
            distancingRepository.updateDistancing(s.getDistancingNumber(), s.getLocalName());
        }
    }



}
