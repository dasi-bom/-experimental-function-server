package com.dasibom.practice.service;

import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryImage;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

//    List<String> uploadImage(List<MultipartFile> multipartFile, String dirName, Diary diary);

    List<String> uploadImageByDiaryId(List<MultipartFile> multipartFile, String dirName, Long diaryId);

    void deletePostImages(List<DiaryImage> images);

}
