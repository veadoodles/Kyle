package com.openai.model;

import java.nio.file.Path;

public class AudioModel {

	private Path filePath;
	private String fileName;
	public Path getFilePath() {
		return filePath;
	}
	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
