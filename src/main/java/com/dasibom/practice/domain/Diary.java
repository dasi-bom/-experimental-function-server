package com.dasibom.practice.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Diary {
    @Id
    @GeneratedValue
    @Column(name = "diaryId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId")
    private Pet pet;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryStamp> stamps;

    private String title;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDate createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDate updatedAt;

    //@ManyToOne
    //private User author;
}
