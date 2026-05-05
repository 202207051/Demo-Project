package com.springboot.rocky.service;

import com.springboot.rocky.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DockerService {

	private final FileService fileService;

	public String run(String userId, String language, String code) {
		try {

			fileService.saveCode(userId, language, code);

			String userHostPath = AppConfig.HOST_WORK_DIR + "/" + userId;

			String[] command = { "docker", "run", "--rm", "--memory", "128m", "--cpus", ".5", "-v",
					userHostPath + ":" + AppConfig.CONTAINER_APP_DIR, "-v",
					AppConfig.HOST_SHARE_DIR + ":" + AppConfig.CONTAINER_SHARE_DIR,
					"rocky-build-" + language.toLowerCase() };

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);

			Process process = pb.start();

			StringBuilder output = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
				}
			}

			boolean finished = process.waitFor(10, TimeUnit.SECONDS);
			if (!finished) {
				process.destroyForcibly();
				return "Error: Execution Timeout (Limit 10s)";
			}

			return output.toString().trim();

		} catch (Exception e) {
			return "Server Error: " + e.getMessage();
		}
	}
}