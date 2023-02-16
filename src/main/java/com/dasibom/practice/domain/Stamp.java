package com.dasibom.practice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stamp {
    @Id
    @GeneratedValue
    @Column(name = "stampId")
    private Long id;

    //private StampType stampType;
}
