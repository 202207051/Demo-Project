package com.springboot.rocky.service;

import com.springboot.rocky.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * CentOS 9 환경에서 Docker 컨테이너를 사용하여
 * 사용자의 코드를 격리된 환경에서 실행하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class DockerService {

    private final FileService fileService;

    public String run(String userId, String language, String code) {
        try {
            // 1. 소스 파일 저장 (FileService 활용)
            // AppConfig.HOST_WORK_DIR (/opt/rocky-build/work) 하위에 사용자별 파일 생성
            fileService.saveCode(userId, language, code);

            // 2. 도커 명령어 구성
            // 호스트의 사용자 작업 경로: /opt/rocky-build/work/{userId}
            String userHostPath = AppConfig.HOST_WORK_DIR + "/" + userId;
            
            // Docker 실행 옵션 분석:
            // --rm: 실행 후 컨테이너 자동 삭제
            // --memory, --cpus: 리소스 제한으로 서버 보호
            // -v: 호스트와 컨테이너 간 디렉토리 연결 (개인 영역 및 공용 영역)
            String[] command = {
                "docker", "run", "--rm",
                "--memory", "128m",
                "--cpus", ".5",
                "-v", userHostPath + ":" + AppConfig.CONTAINER_APP_DIR,
                "-v", AppConfig.HOST_SHARE_DIR + ":" + AppConfig.CONTAINER_SHARE_DIR,
                "rocky-build-" + language.toLowerCase() // 언어별 이미지 호출
            };

            // 3. 프로세스 빌더를 통한 외부 명령어 실행 (ProcessBuilder 사용)
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true); // 컴파일 에러 등 표준 에러를 출력 스트림에 합침
            
            Process process = pb.start();

            // 4. 실행 결과(Output) 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // 5. 실행 시간 제한 (Timeout 10초 설정)
            // 무한 루프나 과도한 실행으로부터 서버 자원을 보호합니다.
            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly(); // 시간 초과 시 강제 종료
                return "Error: Execution Timeout (Limit 10s)";
            }

            return output.toString().trim();

        } catch (Exception e) {
            // 예외 발생 시 GlobalExceptionHandler에서 처리하거나 직접 에러 메시지 반환
            return "Server Error: " + e.getMessage();
        }
    }
}