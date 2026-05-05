package com.springboot.rocky.exception;

import com.springboot.rocky.dto.BuildResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역 컨트롤러 에러 핸들러 선언
public class GlobalExceptionHandler {

    /**
     * 프로젝트 내부에서 발생하는 모든 RuntimeException을 처리합니다.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BuildResponse> handleRuntimeException(RuntimeException e) {
        BuildResponse response = BuildResponse.builder()
                .success(false)
                .error("Runtime Error: " + e.getMessage())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 파일 입출력이나 시스템 레벨의 모든 Exception을 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BuildResponse> handleAllException(Exception e) {
        BuildResponse response = BuildResponse.builder()
                .success(false)
                .error("Unexpected System Error: " + e.getMessage())
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}