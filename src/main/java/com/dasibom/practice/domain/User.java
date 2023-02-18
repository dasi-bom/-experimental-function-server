package com.dasibom.practice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Long id;

    private String username;
    private String password;
}
