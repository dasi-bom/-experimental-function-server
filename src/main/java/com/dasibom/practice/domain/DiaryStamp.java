package com.dasibom.practice.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "diary_stamp_tb")
public class DiaryStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static void removeDiaryStamp(List<DiaryStamp> stamps) {
        // diaryId 와 stampId 의 매핑 제거
        for (DiaryStamp stamp : stamps) {
            stamp.setDiary(null);
            stamp.setStamp(null);
        }
    }

}
