package com.dasibom.practice.condition;

import lombok.Data;

@Data
public class DiaryReadCondition {

    private String searchKeyword; // 검색 키워드

    private Boolean isDeleted;

    // 전체 일기 조회
    public DiaryReadCondition() {
        this.isDeleted = false;
    }

    // 전체 일기 검색
    public DiaryReadCondition(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        this.isDeleted = false;
    }

}
