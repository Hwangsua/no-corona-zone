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
public class SafetyIndexInSeoul {
    @Id @GeneratedValue
    private int no;

    private String district;

    private double index;

    private int confirmed;
}
