package com.demo.automation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.automation.config.DataConfig;
import com.demo.automation.constants.AutomationConstants;
import com.demo.automation.constants.AutomationEnum;

import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AutomationUtils {

	@Autowired
	private DataConfig config;

	/**
	 * Method to get directories
	 * 
	 * @param path
	 * @return List<String>
	 */
	public List<String> getDirectories(String path) {
		File filePath = new File(path);
		List<String> directories = new ArrayList<String>();
		if (filePath.exists()) {
			List<File> files = Arrays.asList(filePath.listFiles());
			files.stream().forEach(file -> {
				if (file.isDirectory()) {
					directories.add(file.getName());
				}
			});
		} else {
			log.info("File path does not exist");
		}
		return directories;
	}

	public List<File> getFiles(String path, String extension) {
		List<File> files = new ArrayList<File>();
		File filePath = new File(path);
		if (filePath.exists()) {
			List<File> fileList = Arrays.asList(filePath.listFiles());
			fileList.stream().forEach(file -> {
				if (file.getName().endsWith(extension)) {
					files.add(file);
				}
			});
		} else {
			log.info("File path does not exist");
		}
		return files;
	}

	public void executeFile(String command) {
		Process proc = null;
		try {
			InputStream in, error;
			String line;
			proc = Runtime.getRuntime().exec(command);
			in = proc.getInputStream();
			error = proc.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			line = null;
			log.info("Output");
			while ((line = reader.readLine()) != null) {
				log.info(line);
			}

			log.info("Errors");
			reader = new BufferedReader(new InputStreamReader(error));
			line = null;
			while ((line = reader.readLine()) != null) {
				log.info(line);
			}

			proc.waitFor();
			log.info(proc.exitValue()+"");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (proc != null) {
				long pid = proc.pid();
				try {
					Runtime.getRuntime().exec("taskkill /F /IM " + pid);
				} catch (IOException e) {
					e.printStackTrace();
				}
				proc.destroy();
			}
		}
	}

	public AutomationEnum getAutomationEnum(List<String> directories) {
		if (directories.contains(AutomationConstants.JAVA_FOLDER)) {
			return AutomationEnum.JAVA;
		} else if (directories.contains(AutomationConstants.PYTHON_FOLDER)) {
			return AutomationEnum.PYTHON;
		} else if (directories.contains(AutomationConstants.BATCH_FOLDER)) {
			return AutomationEnum.BAT;
		}
		return null;
	}

	public List<String> getListFromJSON(String folderName) throws IOException {
		FileInputStream fis = new FileInputStream(config.getFolderRelativePath() + AutomationConstants.PATH_SEPARATOR
				+ folderName + AutomationConstants.PATH_SEPARATOR + AutomationConstants.SEQUENCE_JSON);
		String json = new String(fis.readAllBytes());
		fis.close();
		JsonPath j = new JsonPath(json);
		String jsonKey = getKeyFromJson(folderName);
		List<String> file1 = j.getList(jsonKey);
		return file1;
	}

	public String getKeyFromJson(String folderName) {
		if (folderName.equals(AutomationConstants.JAVA_FOLDER)) {
			return AutomationConstants.JAR;
		} else if (folderName.equals(AutomationConstants.PYTHON_FOLDER)) {
			return AutomationConstants.PYTHON_FOLDER;
		} else {
			return AutomationConstants.BATCH_FOLDER;
		}
	}
}
