package com.demo.automation.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.automation.config.DataConfig;
import com.demo.automation.constants.AutomationConstants;
import com.demo.automation.constants.AutomationEnum;
import com.demo.automation.service.Executer;
import com.demo.automation.utils.AutomationUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PythonFileExecuterImpl implements Executer {

	@Autowired
	private AutomationUtils automationUtils;

	@Autowired
	private DataConfig config;

	@Override
	public void automateExecution(String name) {

		List<File> files = automationUtils.getFiles(name + AutomationConstants.PATH_SEPARATOR
				+ AutomationConstants.PYTHON_FOLDER + AutomationConstants.PATH_SEPARATOR,
				AutomationConstants.PY_EXTENSION);
		List<String> fileNames = new ArrayList<String>();
		files.stream().forEach(file -> {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		});
		try {
			List<String> file1 = automationUtils.getListFromJSON(AutomationConstants.PYTHON_FOLDER);
			file1.stream().forEach(name2 -> {
				log.info("---------PYTHON SCRIPTS EXECUTION-------------");
				log.info("Executing "+name2);
				automationUtils.executeFile(AutomationConstants.PYTHON_EXECUTE + config.getFolderRelativePath()
						+ AutomationConstants.PATH_SEPARATOR + AutomationConstants.PYTHON_FOLDER
						+ AutomationConstants.PATH_SEPARATOR + name2);
			});
			if ((fileNames.size() - file1.size()) != 0) {
				List<String> remainingFiles = fileNames.stream().filter(element -> !file1.contains(element))
						.collect(Collectors.toList());
				remainingFiles.stream().forEach(nameFile -> {
					log.info("---------PYTHON SCRIPTS EXECUTION-------------");
					log.info("Executing "+nameFile);
					automationUtils.executeFile(AutomationConstants.PYTHON_EXECUTE + config.getFolderRelativePath()
							+ AutomationConstants.PATH_SEPARATOR + AutomationConstants.PYTHON_FOLDER
							+ AutomationConstants.PATH_SEPARATOR + nameFile);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public AutomationEnum getExecuteType() {
		return AutomationEnum.PYTHON;
	}

}
