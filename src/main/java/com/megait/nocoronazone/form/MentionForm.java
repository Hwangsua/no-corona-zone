package com.megait.nocoronazone.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


@Data
public class MentionForm {

    @NotNull
    @Length(min = 1, max = 150)
    private String content;

    private Double latitude;

    private Double longitude;

    private String location;

}
