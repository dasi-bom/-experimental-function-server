package com.dasibom.practice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Stamp {
    @Id
    @GeneratedValue
    @Column(name = "stampId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diaryStampId")
    private DiaryStamp diaries;

    private StampType stampType;
}
