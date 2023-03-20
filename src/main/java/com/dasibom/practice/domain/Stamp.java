package com.dasibom.practice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "stamp_tb")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stampId")
    private Long id;

    @OneToMany(mappedBy = "stamp", cascade = CascadeType.ALL)
    private List<DiaryStamp> diaries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StampType stampType;
}
