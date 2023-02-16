package com.dasibom.practice.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Diary {
    @Id
    @GeneratedValue
    @Column(name = "diaryId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petId")
    private Pet pet;

    private String title;
    private String content;

    @ManyToMany
    private List<Stamp> stamps;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    //@ManyToOne
    //private User author;
}
