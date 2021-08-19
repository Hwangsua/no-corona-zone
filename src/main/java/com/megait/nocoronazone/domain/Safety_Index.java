package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Safety_Index {
    @Id @GeneratedValue
    private int no;

    private String city;

    private double index;

    private int confirmed;
}
