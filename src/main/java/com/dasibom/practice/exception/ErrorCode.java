package com.dasibom.practice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (constraint violation)"),
    METHOD_ARG_NOT_VALID(HttpStatus.BAD_REQUEST, "제약 조건을 위배한 요청입니다 (method argument not valid)"),
    STAMP_LIST_SIZE_ERROR(HttpStatus.BAD_REQUEST, "최대 3개의 스탬프를 선택할 수 있습니다"),
    INVALID_FILE_ERROR(HttpStatus.BAD_REQUEST, "확장자가 jpg, jpeg, png 인 파일만 업로드 가능합니다."),
    FILE_NOT_EXIST_ERROR(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다"),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기를 찾을 수 없습니다"),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND, "반려 동물을 찾을 수 없습니다"),
    STAMP_NOT_FOUND(HttpStatus.NOT_FOUND, "스탬프를 찾을 수 없습니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다."),
    FILE_CAN_NOT_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_CAN_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
