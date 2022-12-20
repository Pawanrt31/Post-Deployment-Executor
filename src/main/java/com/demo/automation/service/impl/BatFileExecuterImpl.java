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
public class BatFileExecuterImpl implements Executer {

	@Autowired
	private AutomationUtils automationUtils;

	@Autowired
	private DataConfig config;
	

	/**
	 * Primary method to execute scripts
	 * 
	 * @param name
	 * @return void
	 */
	@Override
	public void automateExecution(String name) {

		List<File> files = automationUtils.getFiles(name + AutomationConstants.PATH_SEPARATOR
				+ AutomationConstants.BATCH_FOLDER + AutomationConstants.PATH_SEPARATOR,
				AutomationConstants.BAT_EXTENSION);
		List<String> fileNames = new ArrayList<String>();
		files.stream().forEach(file -> {
			if (file.isFile()) {
				fileNames.add(file.getName());
			}
		});
		try {
			List<String> file1 = automationUtils.getListFromJSON(AutomationConstants.BATCH_FOLDER);
			file1.stream().forEach(name2 -> {
				log.info("---------BATCH SCRIPTS EXECUTION-------------");
				log.info("Executing "+name2);
				automationUtils.executeFile(config.getFolderRelativePath() + AutomationConstants.PATH_SEPARATOR
						+ AutomationConstants.BATCH_FOLDER + AutomationConstants.PATH_SEPARATOR + name2);
			});
			if ((fileNames.size() - file1.size()) != 0) {
				List<String> remainingFiles = fileNames.stream().filter(element -> !file1.contains(element))
						.collect(Collectors.toList());
				remainingFiles.stream().forEach(nameFile -> {
					log.info("---------BATCH SCRIPTS EXECUTION-------------");
					log.info("Executing "+nameFile);
					automationUtils.executeFile(config.getFolderRelativePath() + AutomationConstants.PATH_SEPARATOR
							+ AutomationConstants.BATCH_FOLDER + AutomationConstants.PATH_SEPARATOR + nameFile);
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Method to retrieve the Enum of the type of script
	 * 
	 * 
	 * @return AutomationEnum
	 */
	@Override
	public AutomationEnum getExecuteType() {
		return AutomationEnum.BAT;
	}
}
