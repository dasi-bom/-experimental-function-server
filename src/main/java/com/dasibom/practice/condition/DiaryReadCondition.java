package com.dasibom.practice.condition;

import com.dasibom.practice.domain.StampType;
import com.dasibom.practice.domain.User;
import lombok.Data;

@Data
public class DiaryReadCondition {

    private User user; // 작성자
    private String petName;

    private String searchKeyword; // 검색 키워드

    private Boolean isDeleted;

    private StampType stampType;

    // 전체 일기 조회
    public DiaryReadCondition() {
        this.isDeleted = false;
    }

    // 전체 일기 검색
    public DiaryReadCondition(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        this.isDeleted = false;
    }

    // 스탬프 별 (내가 쓴) 일기 조회
    public DiaryReadCondition(StampType stampType, User user, String petName) {
        this.user = user;
        this.petName = petName;
        this.stampType = stampType;
        this.isDeleted = false;
    }

}
