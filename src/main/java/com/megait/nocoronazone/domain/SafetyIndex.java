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
public class SafetyIndex {
    @Id
    private int no;

    private String city;

    private double index; // 밀집

    private int confirmed; // 확진
}
