package com.springboot.rocky.controller;

import com.springboot.rocky.dto.CodeRequest;
import com.springboot.rocky.dto.BuildResponse;
import com.springboot.rocky.service.DockerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/build")
@RequiredArgsConstructor
public class BuildController {

    private final DockerService dockerService;

    @PostMapping("/run")
    public BuildResponse runBuild(@RequestBody CodeRequest request) {
        
        // [선택사항 반영] 모든 필수 파라미터 null 및 빈값 체크
        if (isInvalid(request.getUserId()) || isInvalid(request.getLanguage()) || isInvalid(request.getCode())) {
            return BuildResponse.builder()
                    .success(false)
                    .error("Missing required fields: userId, language, and code are all required.")
                    .build();
        }

        try {
            // DockerService 실행
            String result = dockerService.run(
                request.getUserId(), 
                request.getLanguage(), 
                request.getCode()
            );

            // 결과 분석
            boolean isSuccess = result != null && 
                               !result.startsWith("Error") && 
                               !result.startsWith("Server Error");

            return BuildResponse.builder()
                    .success(isSuccess)
                    .output(isSuccess ? result : null)
                    .error(isSuccess ? null : result)
                    .build();

        } catch (Exception e) {
            return BuildResponse.builder()
                    .success(false)
                    .error("Controller Exception: " + e.getMessage())
                    .build();
        }
    }

    // 편의를 위한 검증 메서드
    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}