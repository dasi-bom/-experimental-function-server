package com.dasibom.practice.controller;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.Response;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiarySaveReqDto;
import com.dasibom.practice.service.DiaryService;
import com.dasibom.practice.service.S3Service;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diary")
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;
    private final S3Service s3Service;

    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response save(@RequestPart @Valid DiarySaveReqDto requestDto,
            @RequestPart(required = false) List<MultipartFile> multipartFile) {

        Diary diary = diaryService.save(requestDto);
        if (multipartFile != null) {
            s3Service.uploadImage(multipartFile, "Diary", diary);
        }

        return new Response("OK", "일기 등록에 성공했습니다");
    }

    @GetMapping("/detail/{diaryId}")
    public DiaryDetailResDto getDetailedDiary(@PathVariable("diaryId") long diaryId) {
        return diaryService.getDetailedDiary(diaryId);
    }

}
