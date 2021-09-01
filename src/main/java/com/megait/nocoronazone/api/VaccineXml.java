package com.megait.nocoronazone.api;

import lombok.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

@Service
public class VaccineXml {

    public VaccineCountVo getVaccineCount() {
        try{
            String url = "https://nip.kdca.go.kr/irgd/cov19stats.do?list=all";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(url);
            Element root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("item");

            Element element1 = (Element)list.item(0);
            int todayFirstCnt = Integer.parseInt(getChildren(element1,"firstCnt"));
            int todaySecondCnt = Integer.parseInt(getChildren(element1, "secondCnt"));


            Element element2 = (Element)list.item(2);
            int totalFirstCnt = Integer.parseInt(getChildren(element2,"firstCnt"));
            int totalSecondCnt = Integer.parseInt(getChildren(element2, "secondCnt"));

            return new VaccineCountVo(todayFirstCnt, todaySecondCnt, totalFirstCnt, totalSecondCnt);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public static String getChildren(Element element, String tagName){
        NodeList list = element.getElementsByTagName(tagName);
        Element cElement = (Element)list.item(0);

        if(cElement.getFirstChild() != null){
            return cElement.getFirstChild().getNodeValue();
        }else{
            return "";
        }
    }

    public int getTotalPopulation() {
        try{
            String url = "https://kosis.kr/openapi/Param/statisticsParameterData.do?method=getList&apiKey=NjM5ZGVmMDJmOWFmYzBmYTFjNmM4OTg0NzhjZDhmMDY=&itmId=T20+&objL1=00+&objL2=&objL3=&objL4=&objL5=&objL6=&objL7=&objL8=&format=sdmx&jsonVD=Y&type=Generic&prdSe=M&newEstPrdCnt=1&loadGubun=2&orgId=101&tblId=DT_1B040A3&version=v2_1";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(url);
            Element root = doc.getDocumentElement();

            NodeList list = root.getElementsByTagName("generic:ObsValue");

            Element element = (Element)list.item(0);
            int totalPopulation = Integer.parseInt(element.getAttributes().getNamedItem("value").getNodeValue());

            return totalPopulation;

        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    public List<String> getCityPopulation() {
        try{
            String url = "https://kosis.kr/openapi/statisticsData.do?method=getList&apiKey=NjM5ZGVmMDJmOWFmYzBmYTFjNmM4OTg0NzhjZDhmMDY=&format=sdmx&jsonVD=Y&userStatsId=cmlh21/101/DT_1B040A3/2/1/20210826035602&type=Generic&prdSe=M&newEstPrdCnt=1&version=v2_1";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(url);

            Element root = doc.getDocumentElement();
            Node dataSet = root.getElementsByTagName("message:DataSet").item(0);

            NodeList series = dataSet.getChildNodes();

            List<String> cityPopulationList = new ArrayList<>();

            for(int i = 0; i < series.getLength(); ++i) {
                Node node = series.item(i);
                NodeList obsValue = ((Element) node).getElementsByTagName("generic:ObsValue");
                String cityPopulation = obsValue.item(0).getAttributes().getNamedItem("value").getNodeValue();
                cityPopulationList.add(cityPopulation);
            }
            return cityPopulationList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}