package com.springboot.rocky.service;

import com.springboot.rocky.config.AppConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

	public Path saveCode(String userId, String language, String code) throws IOException {
		Path userWorkPath = Paths.get(AppConfig.HOST_WORK_DIR, userId);

		if (!Files.exists(userWorkPath)) {
			Files.createDirectories(userWorkPath);
		}

		String fileName = switch (language.toLowerCase()) {
		case "cpp" -> "main.cpp";
		case "java" -> "Main.java";
		case "python" -> "script.py";
		default -> "code.txt";
		};

		Path filePath = userWorkPath.resolve(fileName);
		Files.writeString(filePath, code);

		return filePath;
	}
}