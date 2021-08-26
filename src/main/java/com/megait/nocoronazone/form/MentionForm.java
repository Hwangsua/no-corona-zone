package com.megait.nocoronazone.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class MentionForm {

    private String content;

    private Double latitude;

    private Double longitude;

    private String location;

}
