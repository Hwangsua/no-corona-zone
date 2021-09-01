package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialDistancing {

    @Id
    private String localName;

    private String distancingNumber;


}
