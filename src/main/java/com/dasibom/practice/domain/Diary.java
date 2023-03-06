package com.dasibom.practice.domain;

import java.time.LocalDate;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Slf4j
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diaryId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "petId")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "authorId")
    private User author;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryStamp> diaryStamps = new ArrayList<>();

    private String title;
    private String content;

    @CreationTimestamp
    private LocalDate createdAt;

    @CreationTimestamp
    private LocalDate updatedAt;

    public void addDiaryStamp(DiaryStamp diaryStamp) {
        diaryStamps.add(diaryStamp);
        diaryStamp.setDiary(this);
    }

    public static Diary createDiary(User user, Pet pet, String title, String content, List<DiaryStamp> stamps) {
        Diary diary = new Diary();
        diary.setAuthor(user);
        diary.setPet(pet);
        for (DiaryStamp stamp : stamps) {
            diary.addDiaryStamp(stamp);
        }
        diary.setTitle(title);
        diary.setContent(content);
        return diary;
    }

    public void updateDiary(String title, String content, List<DiaryStamp> diaryStamps) {

        if (StringUtils.isNotBlank(title)) {
            this.title = title;
        }
        if (StringUtils.isNotBlank(content)) {
            this.content = content;
        }
        if (diaryStamps != null) {
            for (DiaryStamp diaryStamp : diaryStamps) {
                this.addDiaryStamp(diaryStamp);
            }
        }
    }
}
