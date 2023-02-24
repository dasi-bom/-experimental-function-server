package com.dasibom.practice.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Setter;

@Entity
@Setter
public class DiaryStamp {
    @Id
    @GeneratedValue
    @Column(name = "diaryStampId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "diaryId")
    private Diary diary;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stampId")
    private Stamp stamp;

    public static DiaryStamp createDiaryStamp(Stamp stamp) {
        DiaryStamp diaryStamp = new DiaryStamp();
        diaryStamp.setStamp(stamp);
        return diaryStamp;
    }

}
