package com.dasibom.practice.domain;

public enum StampType {
    WALK("산책"),
    TREAT("간식"),
    TOY("장난감");

    private String keyword;

    StampType(String keyword) {
        this.keyword = keyword;
    }
}
