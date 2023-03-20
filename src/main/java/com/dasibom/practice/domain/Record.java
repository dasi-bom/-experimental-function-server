package com.dasibom.practice.domain;

import com.dasibom.practice.dto.DiaryDetailResDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "record_tb")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "petId")
    private Pet pet;

    private StampType stampType;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>(); // 스탬프 별 다시보기 일기 (3개)

    public static Record createRecord(Pet pet, StampType stampType, List<Diary> diaries) {
        Record record = new Record();
        record.setPet(pet);
        record.setStampType(stampType);

        for (Diary diary : diaries) {
            diary.setRecord(record);
            record.diaries.add(diary);
        }

        return record;
    }
}
