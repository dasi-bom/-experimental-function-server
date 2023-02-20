package com.dasibom.practice.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Long id;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> pets;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Diary> diaries;

    private String username;
    private String password;
}
