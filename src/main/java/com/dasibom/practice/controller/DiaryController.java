package com.dasibom.practice.controller;

import static com.dasibom.practice.exception.ErrorCode.FILE_NOT_EXIST_ERROR;
import static com.dasibom.practice.exception.ErrorCode.USER_NOT_FOUND;

import com.dasibom.practice.condition.DiaryReadCondition;
import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryImage;
import com.dasibom.practice.domain.Response;
import com.dasibom.practice.domain.StampType;
import com.dasibom.practice.domain.User;
import com.dasibom.practice.dto.DiaryBriefResDto;
import com.dasibom.practice.dto.DiaryDetailResDto;
import com.dasibom.practice.dto.DiarySaveReqDto;
import com.dasibom.practice.dto.DiaryUpdateReqDto;
import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.repository.UserRepository;
import com.dasibom.practice.service.DiaryService;
import com.dasibom.practice.service.S3Service;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // TODO: 로그인 기능 개발 이후 제거
    private final UserRepository userRepository;

    @GetMapping("/issue/id")
    public Response issueId() {
        Long id = diaryService.issueId();
        return new Response("OK", id.toString());
    }

    @PostMapping("/save/{diaryId}/text")
    public Response save_onlyText(@PathVariable("diaryId") Long diaryId,
            @RequestBody @Valid DiarySaveReqDto requestDto) {
        diaryService.save(diaryId, requestDto);
        return new Response("OK", "일기 등록에 성공했습니다");
    }

    @PostMapping(value = "/save/{diaryId}/file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response save_onlyFile(@PathVariable("diaryId") long diaryId,
            @RequestPart(required = false) List<MultipartFile> multipartFile) {
        if (multipartFile == null) {
            throw new CustomException(FILE_NOT_EXIST_ERROR);
        }
        s3Service.uploadImage_onlyFile(multipartFile, "Diary", diaryId);
        return new Response("OK", "파일 업로드에 성공했습니다");
    }

//    @PostMapping(value = "/save", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//    public Response save(@RequestPart @Valid DiarySaveReqDto requestDto,
//            @RequestPart(required = false) List<MultipartFile> multipartFile) {
//
//        Diary diary = diaryService.save(requestDto);
//        if (multipartFile != null) {
//            s3Service.uploadImage(multipartFile, "Diary", diary);
//        }
//
//        return new Response("OK", "일기 등록에 성공했습니다");
//    }

    @GetMapping("/detail/{diaryId}")
    public DiaryDetailResDto getDetailedDiary(@PathVariable("diaryId") long diaryId) {
        return diaryService.getDetailedDiary(diaryId);
    }

    @GetMapping("/list")
    public Slice<DiaryBriefResDto> list(Long cursor, String searchKeyword,
            @PageableDefault(size = 5, sort = "createAt") Pageable pageRequest) {
        if (StringUtils.hasText(searchKeyword)) {
            return diaryService.getDiaryList(cursor, new DiaryReadCondition(searchKeyword),
                    pageRequest); // searchKeyword 가 포함된 일기 조회
        }
        return diaryService.getDiaryList(cursor, new DiaryReadCondition(), pageRequest); // 모든 일기 조회
    }

    @PatchMapping("/{diaryId}")
    public Response update(@PathVariable("diaryId") long diaryId,
            @RequestBody @Valid DiaryUpdateReqDto updateRequestDto) {
        diaryService.update(diaryId, updateRequestDto);
        return new Response("OK", "일기 수정에 성공했습니다");
    }

    @DeleteMapping("/{diaryId}")
    public Response delete(@PathVariable("diaryId") long diaryId) {
        Diary deletedDiary = diaryService.deleteBeforeS3(diaryId);
        // 이미지 제거
        List<DiaryImage> images = deletedDiary.getImages();
        if (!images.isEmpty()) {
            s3Service.deletePostImages(images);
        }
        diaryService.deleteAfterS3(diaryId);

        return new Response("OK", "일기 삭제에 성공했습니다");
    }

    @ApiOperation(value = "다시 보기", notes = "스탬프 별 다시보기")
    @GetMapping("/list/record")
    public List<DiaryDetailResDto> recordList(@RequestParam StampType stampType, @RequestParam String petName) {

        // TODO: 하드 코딩 변경
        User user = userRepository.findByUsername("test")
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return diaryService.getRecordList(stampType, petName, user);
    }

}
