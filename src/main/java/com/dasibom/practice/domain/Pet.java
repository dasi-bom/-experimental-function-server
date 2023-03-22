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
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pet_tb")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "petId")
    private Long id;

    @OneToMany(mappedBy = "pet")
    private List<Diary> diaryList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;

    private String petName;

    private LocalDate protectionStartedAt;

    private LocalDate protectionEndedAt;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Record> records = new ArrayList<>();

    @Builder
    public Pet(Long id, User owner, String petName, LocalDate protectionStartedAt) {
        this.id = id;
        this.owner = owner;
        this.petName = petName;
        this.protectionStartedAt = protectionStartedAt;
    }

    public void updateProtectionEndedAt() {
        protectionEndedAt = LocalDate.now();
    }
}
