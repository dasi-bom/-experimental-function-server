package com.dasibom.practice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DiaryStamp {
    @Id
    @GeneratedValue
    @Column(name = "diaryStampId")
    private Long id;

    @OneToMany(mappedBy = "stamps")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "diaries")
    private List<Stamp> stamps = new ArrayList<>();

}
