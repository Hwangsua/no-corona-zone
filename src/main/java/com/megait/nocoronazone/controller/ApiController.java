package com.megait.nocoronazone.controller;

import com.megait.nocoronazone.domain.DetailSafetyIndex;
import com.megait.nocoronazone.domain.SafetyIndex;
import com.megait.nocoronazone.repository.DetailSafetyRepository;
import com.megait.nocoronazone.repository.SafetyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class Daily {
    String location;
    String localCnt;
    String totalConfirmedCnt;
    String dailyConfirmedCnt;
}

class LocaleCode {
    Object lDongCd;
    Object siDo;
    Object siGunGu;
    Object dong;
}

@EnableScheduling
@RestController
public class ApiController {

    @Autowired
    SafetyRepository safetyRepository;

    @Autowired
    DetailSafetyRepository detailSafetyRepository;

    int[] idx2 = new int[18];
    double[] contactDensityPercentile = new double[18];
    String localeName[] = {"강원", "경기", "경상남도", "경상북도", "광주", "대구", "대전", "부산", "서울", "세종", "울산", "인천", "전라남도", "전라북도", "제주", "충청남도", "충청북도"};
    Daily DayToDay[] = new Daily[19];
    LocaleCode lcode[][] = new LocaleCode[19][1000000];

    @GetMapping("/LatestStatusAPI")// http://localhost:8080/LatestStatusAPI
    public String callAPI() {
        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";
        int idx = 1, idxCompare = 0;
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            String url = "https://apis.openapi.sk.com/safecaster/v1/search/confirmed/new/location";

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + "?" + "appKey=l7xx1e9a9e235e1b4c54a4d0cf20abc304f5&locale=kr").build();

            // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

            result.put("statusCode", resultMap.getStatusCodeValue()); // http status code를 확인
            result.put("header", resultMap.getHeaders()); // 헤더 정보 확인
            result.put("body", resultMap.getBody()); // 실제 데이터 정보 확인

            ArrayList<Map> dboxoffList = (ArrayList<Map>) resultMap.getBody().get("data");
            LinkedHashMap mnList = new LinkedHashMap<>();
            for (int i = 0; i < dboxoffList.size(); i++) {
                DayToDay[i] = new Daily();
            }
            for (Map obj : dboxoffList) {
                if (idx == 2) {
                    idx++;
                    idxCompare++;
                    continue;
                }
                DayToDay[idx - idxCompare].location = obj.get("location").toString();
                DayToDay[idx - idxCompare].localCnt = obj.get("localCnt").toString();
                DayToDay[idx - idxCompare].totalConfirmedCnt = obj.get("totalConfirmedCnt").toString();
                DayToDay[idx - idxCompare].dailyConfirmedCnt = obj.get("dailyConfirmedCnt").toString();
                jsonInString += DayToDay[idx - idxCompare].location; // 현재 지역
                jsonInString += DayToDay[idx - idxCompare].localCnt; // 국내 감염
                jsonInString += DayToDay[idx - idxCompare].totalConfirmedCnt; // 누적 감염
                jsonInString += DayToDay[idx - idxCompare].dailyConfirmedCnt; // 일일 감염
                idx++;
            }


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            System.out.println(e.toString());
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", "excpetion오류");
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return jsonInString;

    }

    @GetMapping("/callAPI2")//http://localhost:8080/callAPI2
    @Scheduled(fixedRate = 86400000)//하루
    public String callAPI2() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String jsonInString = "";

        for (int i = 0; i <= 16; i++) {
            idx2[i] = 0;
            try {
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders header = new HttpHeaders();
                HttpEntity<?> entity = new HttpEntity<>(header);

                String url = "https://apis.openapi.sk.com/safecaster/v1/search/location";

                UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + "?" + "appKey=l7xx1e9a9e235e1b4c54a4d0cf20abc304f5&locale=en&searchText=" + localeName[i]).build();

                // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
                ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

                result.put("statusCode", resultMap.getStatusCodeValue()); // http status code를 확인
                result.put("header", resultMap.getHeaders()); // 헤더 정보 확인
                result.put("body", resultMap.getBody()); // 실제 데이터 정보 확인

                ArrayList<Map> dboxoffList = (ArrayList<Map>) resultMap.getBody().get("data");
                LinkedHashMap mnList = new LinkedHashMap<>();
                for (int j = 0; j < dboxoffList.size(); j++) {
                    lcode[i][j] = new LocaleCode();
                }
                for (Map obj : dboxoffList) {
                    lcode[i][idx2[i]].lDongCd = obj.get("lDongCd");
                    lcode[i][idx2[i]].siDo = obj.get("siDo");
                    lcode[i][idx2[i]].siGunGu = obj.get("siGunGu");
                    lcode[i][idx2[i]].dong = obj.get("dong");
                    jsonInString += lcode[i][idx2[i]].lDongCd; // 법정동 번호
                    jsonInString += lcode[i][idx2[i]].siDo; // 시
                    jsonInString += lcode[i][idx2[i]].siGunGu; // 구
                    jsonInString += lcode[i][idx2[i]].dong; // 동
                    idx2[i]++;
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                result.put("statusCode", e.getRawStatusCode());
                result.put("body", e.getStatusText());
                System.out.println(e.toString());

            } catch (Exception e) {
                result.put("statusCode", "999");
                result.put("body", "excpetion오류");
                System.out.println(e.toString());
            }

        }
        return jsonInString;
    }

    @GetMapping("/callAPI3")//http://localhost:8080/callAPI3
    @Scheduled(fixedRate = 1200000) //20분
    public String callAPI3() {
        HashMap<String, Object> result = new HashMap<String, Object>();

        String jsonInString = "";

        for (int i = 0; i <= 16; i++) {
            int cnt = idx2[i];
            int detail = 0;
            int cnt2 = 0;
            contactDensityPercentile[i] = 0;
            for (int j = 0; j < idx2[i]; j++) {
                try {
                    RestTemplate restTemplate = new RestTemplate();

                    HttpHeaders header = new HttpHeaders();
                    HttpEntity<?> entity = new HttpEntity<>(header);

                    String url = "https://apis.openapi.sk.com/safecaster/v1/search/safetyindex/ldongcd/all/current";

                    UriComponents uri = UriComponentsBuilder.fromHttpUrl(url + "?" + "appKey=l7xx1e9a9e235e1b4c54a4d0cf20abc304f5&locale=en&ldongCd=" + lcode[i][j].lDongCd).build();

                    // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
                    ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);

                    result.put("statusCode", resultMap.getStatusCodeValue()); // http status code를 확인
                    result.put("header", resultMap.getHeaders()); // 헤더 정보 확인
                    result.put("body", resultMap.getBody()); // 실제 데이터 정보 확인
                    if (!resultMap.getBody().get("code").equals("0000")) {
                        cnt--;
                        continue;
                    }
                    ArrayList<Map> dboxoffList = (ArrayList<Map>) resultMap.getBody().get("data");
                    LinkedHashMap mnList = new LinkedHashMap<>();
                    for (Map obj : dboxoffList) {
                        detail += Double.parseDouble(obj.get("contactDensityPercentile").toString());
                        contactDensityPercentile[i] += Double.parseDouble(obj.get("contactDensityPercentile").toString()); // 법정동\
                        cnt2++;
                    }
                    if (idx2[i] == j + 1 || !lcode[i][j].siGunGu.equals(lcode[i][j + 1].siGunGu))// 시군구 바뀌면 save
                    {
                        detail /= cnt2;
                        detailSafetyRepository.save(DetailSafetyIndex.builder()
                                .district(lcode[i][j].siGunGu.toString())
                                .index(detail)
                                .build());
                        detail = 0;
                        cnt2 = 0;
                    }


                } catch (HttpClientErrorException | HttpServerErrorException e) {
                    result.put("statusCode", e.getRawStatusCode());
                    result.put("body", e.getStatusText());
                    System.out.println(e.toString());

                } catch (Exception e) {
                    result.put("statusCode", "999");
                    result.put("body", "excpetion오류");
                    System.out.println(e.toString());
                }
            }
            contactDensityPercentile[i] /= cnt;
            safetyRepository.save(SafetyIndex.builder()
                    .city(DayToDay[i + 1].location)
                    .confirmed(Integer.parseInt(DayToDay[i + 1].dailyConfirmedCnt))
                    .index(contactDensityPercentile[i]).build());
        }
        return jsonInString;
    }

}