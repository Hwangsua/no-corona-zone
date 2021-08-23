package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailSafetyIndex {

    @Id
    private String district;

    private int index;
}
