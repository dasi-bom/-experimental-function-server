package com.dasibom.practice.service;

import static com.dasibom.practice.exception.ErrorCode.DIARY_NOT_FOUND;
import static com.dasibom.practice.exception.ErrorCode.FILE_CAN_NOT_UPLOAD;
import static com.dasibom.practice.exception.ErrorCode.INVALID_FILE_ERROR;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dasibom.practice.domain.Diary;
import com.dasibom.practice.domain.DiaryImage;
import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.repository.DiaryImageRepository;
import com.dasibom.practice.repository.DiaryRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;
    private final DiaryImageRepository diaryImageRepository;
    private final DiaryRepository diaryRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    // 게시글 이미지 업로드
    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFile, String dirName, Diary diary) {

        List<String> fileNameList = new ArrayList<>();
        List<String> imageUrl = new ArrayList<>();

        uploadS3(multipartFile, dirName, fileNameList, imageUrl);

        try {
            storeInfoInDb(imageUrl, fileNameList, diary);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNameList;
    }

    @Override
    public List<String> uploadImage_onlyFile(List<MultipartFile> multipartFile, String dirName, Long diaryId) {

        List<String> fileNameList = new ArrayList<>();
        List<String> imageUrl = new ArrayList<>();

        uploadS3(multipartFile, dirName, fileNameList, imageUrl);

        try {
            storeInfoInDb_onlyFile(imageUrl, fileNameList, diaryId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNameList;
    }

    // S3 업로드 로직
    private void uploadS3(List<MultipartFile> multipartFile, String dirName, List<String> fileNameList,
            List<String> imageUrl) {

        multipartFile.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename(), dirName);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            // s3에 업로드
            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new CustomException(FILE_CAN_NOT_UPLOAD);
            }

            fileNameList.add(fileName);
            imageUrl.add(amazonS3Client.getUrl(bucket, fileName).toString());
        });
    }

    // db에 url 과 fileName 정보 저장
    public void storeInfoInDb(List<String> imageUrls, List<String> fileNameList, Diary diary) throws IOException {
        for (int i = 0; i < imageUrls.size(); i++) {
            DiaryImage img = new DiaryImage();
            img.setImgUrl(imageUrls.get(i));
            img.setFileName(fileNameList.get(i));
            img.setDiary(diary);

            diaryImageRepository.save(img);
        }
    }

    public void storeInfoInDb_onlyFile(List<String> imageUrls, List<String> fileNameList, Long diaryId) throws IOException {
        for (int i = 0; i < imageUrls.size(); i++) {
            DiaryImage img = new DiaryImage();
            img.setImgUrl(imageUrls.get(i));
            img.setFileName(fileNameList.get(i));
            Diary diary = diaryRepository.findById(diaryId)
                    .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
            if (diary.getIsDeleted()) {
                throw new CustomException(DIARY_NOT_FOUND);
            }
            img.setDiary(diary);

            diaryImageRepository.save(img);
        }
    }

    // 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + getFileExtension(originalName);
    }

    // 파일의 확장자 명을 가져오는 로직 (file 형식 확인)
    private String getFileExtension(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (fileExtension.equals(".jpeg") || fileExtension.equals(".jpg") || fileExtension.equals(".png")) {
            return fileExtension;
        } else {
            throw new CustomException(INVALID_FILE_ERROR);
        }
    }

}
