package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Article;
import com.megait.nocoronazone.thread.ProcessOutputThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final String localCsvPath = new String("./local_article.csv".getBytes(),StandardCharsets.UTF_8);
    private final String localBatPath = new String("./local_article.sh".getBytes(),StandardCharsets.UTF_8);
    private final String vaccineCsvPath = new String("./vaccine_article.csv".getBytes(),StandardCharsets.UTF_8);
    private final String vaccineBatPath = new String("./vaccine_article.sh".getBytes(),StandardCharsets.UTF_8);

    public String getCommandLineArg(List<String> list){
        return String.join(" ", list);
    }

    public void  setArticleFile(String lineArg, String batPath, String csvPath) throws IOException {

        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try {
            new FileOutputStream(csvPath).close();

            String[] cmd = {"/bin/sh", "-c", "sh" +" "+ batPath + " " +  lineArg};
            process = runtime.exec(cmd);

            StringBuffer stdMsg = new StringBuffer();

            ProcessOutputThread outputThread = new ProcessOutputThread(process.getInputStream(), stdMsg);
            outputThread.start();

            StringBuffer errMsg = new StringBuffer();

            outputThread = new ProcessOutputThread(process.getErrorStream(),errMsg);
            outputThread.start();

            process.waitFor();

        }catch (FileNotFoundException | InterruptedException e){
            e.printStackTrace();
            log.error("csv file not found");
        }finally {
            if (process != null){
                process.destroy();
            }
        }

    }

    public List<Article> getArticleList(String csvPath) throws IOException{

        List<String> stringList = Files.readAllLines(Path.of(csvPath), StandardCharsets.UTF_8);

        List<Article> articleList = new ArrayList<>();


        for(String s : stringList){

            String[] arr = s.replaceAll("^\"|\"$", "").split("\\|");
            Article article = Article.builder()
                    .pressName(arr[0])
                    .pressImgUrl(arr[1])
                    .articleTitle(arr[2])
                    .articleContent(arr[3])
                    .articleLink(arr[4])
                    .articleImgUrl(arr[5])
                    .build();

            articleList.add(article);

        }
        return articleList;

    }


    public List<Article> getLocalArticleList(String mainCityName, String subCityName) throws IOException {

        String lineArg = getCommandLineArg(Arrays.asList(mainCityName,subCityName, new File(localCsvPath).getCanonicalPath()));
        setArticleFile(lineArg,new File(localBatPath).getCanonicalPath(),localCsvPath);
        return getArticleList(localCsvPath);

    }

    public List<Article> getVaccineArticleList() throws IOException {

        String lineArg = getCommandLineArg(Arrays.asList(new File(vaccineCsvPath).getCanonicalPath()));
        setArticleFile(lineArg,new File(vaccineBatPath).getCanonicalPath(),vaccineCsvPath);
        return getArticleList(vaccineCsvPath);

    }


}