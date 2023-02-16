package com.dasibom.practice.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Pet {
    @Id
    @GeneratedValue
    @Column(name = "petId")
    private Long id;

    private String petName;

    private LocalDate protectionStartedAt;

    private LocalDate protectionEndedAt;

    @OneToMany(mappedBy = "pet")
    private List<Diary> diaryList = new ArrayList<>();
}
