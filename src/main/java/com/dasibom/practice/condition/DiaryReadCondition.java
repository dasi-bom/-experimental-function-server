package com.dasibom.practice.condition;

import lombok.Data;

@Data
public class DiaryReadCondition {

    private String searchKeyword; // 검색 키워드

    // 전체 일기 조회
    public DiaryReadCondition() {
    }

    // 전체 일기 검색
    public DiaryReadCondition(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

}
