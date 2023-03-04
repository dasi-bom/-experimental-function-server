package com.dasibom.practice.dto;

import com.dasibom.practice.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryBriefInfoDto {

    private Long diaryId;
    private String petName;
    private String title;
    private String content;
    private String author;
    private String createdAt;

    public DiaryBriefInfoDto(Diary diary) {
        this.diaryId = diary.getId();
        this.petName = diary.getPet().getPetName();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.author = diary.getAuthor().getUsername();
        this.createdAt = diary.getCreatedAt().toString();
    }
}
