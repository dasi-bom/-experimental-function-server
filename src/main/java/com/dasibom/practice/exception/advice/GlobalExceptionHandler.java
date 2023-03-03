package com.dasibom.practice.exception.advice;


import static com.dasibom.practice.exception.ErrorCode.CONSTRAINT_VIOLATION;
import static com.dasibom.practice.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.dasibom.practice.exception.ErrorCode.METHOD_ARG_NOT_VALID;

import com.dasibom.practice.exception.CustomException;
import com.dasibom.practice.exception.ErrorResponse;
import java.util.Objects;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 모든 예외를 핸들링하여 ErrorResponse 형식으로 반환한다.
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
        log.error("handleAllException throw Exception : {}", ex.getClass().getName());
        return ErrorResponse.toResponseEntity(INTERNAL_SERVER_ERROR);
    }


    // @Valid 검증 실패 시 Catch
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException throw Exception : {}", METHOD_ARG_NOT_VALID);
        String defaultMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ErrorResponse.toResponseEntity(METHOD_ARG_NOT_VALID, defaultMessage);
    }

    // 제약 조건 위배 시 Catch
    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", CONSTRAINT_VIOLATION);
        return ErrorResponse.toResponseEntity(CONSTRAINT_VIOLATION);
    }

    // CustomException 을 상속받은 클래스가 예외를 발생 시킬 시, Catch 하여 ErrorResponse 를 반환한다.
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("handleCustomException throw CustomException : {}", ex.getErrorCode());
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }
}
