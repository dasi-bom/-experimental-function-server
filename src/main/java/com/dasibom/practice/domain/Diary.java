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
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Setter
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
    private List<DiaryStamp> stamps = new ArrayList<>();

    private String title;
    private String content;

    @CreationTimestamp
    private LocalDate createdAt;

    @CreationTimestamp
    private LocalDate updatedAt;

    public void addDiaryStamp(DiaryStamp diaryStamp) {
        stamps.add(diaryStamp);
        diaryStamp.setDiary(this);
    }

    public static Diary createDiary(User user,Pet pet, String title, String content, List<DiaryStamp> stamps) {
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
}
