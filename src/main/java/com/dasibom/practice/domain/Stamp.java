package com.dasibom.practice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Stamp {
    @Id
    @GeneratedValue
    @Column(name = "stampId")
    private Long id;

    @OneToMany(mappedBy = "stamp", cascade = CascadeType.ALL)
    private List<DiaryStamp> diaries = new ArrayList<>();

    private StampType stampType;
}
