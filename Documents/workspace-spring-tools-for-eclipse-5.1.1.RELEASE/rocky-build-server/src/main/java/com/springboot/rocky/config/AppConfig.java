package com.springboot.rocky.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    // 1. 메인 OS(Rocky Linux)의 개인 작업 디렉토리 베이스
    public static final String HOST_WORK_DIR = "/opt/rocky-build/work";
    
    // 2. 메인 OS(Rocky Linux)의 공용 상호교류 디렉토리
    public static final String HOST_SHARE_DIR = "/opt/rocky-build/share";
    
    // 3. 도커 컨테이너 내부의 소스코드 작업 경로
    public static final String CONTAINER_APP_DIR = "/app";

    // 4. 도커 컨테이너 내부의 공용 데이터 공유 경로
    public static final String CONTAINER_SHARE_DIR = "/app/share";
}