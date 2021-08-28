package com.megait.nocoronazone.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class Article {

    private String pressName;

    private String pressImgUrl;

    private String articleTitle;

    private String articleContent;

    private String articleLink;

    private String articleImgUrl;

}
