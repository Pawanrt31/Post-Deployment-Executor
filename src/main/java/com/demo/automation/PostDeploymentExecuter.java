package com.demo.automation;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.demo.automation.config.DataConfig;
import com.demo.automation.constants.AutomationEnum;
import com.demo.automation.service.AutomationServiceFactory;
import com.demo.automation.service.Executer;
import com.demo.automation.utils.AutomationUtils;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class PostDeploymentExecuter implements CommandLineRunner {

	@Autowired
	private DataConfig config;

	@Autowired
	private AutomationServiceFactory automationServiceFactory;

	@Autowired
	private AutomationUtils automationUtils;

	public static void main(String[] args) {
		new SpringApplicationBuilder(PostDeploymentExecuter.class).web(WebApplicationType.NONE).run(args);
	}

	@PostConstruct
	public void startupApplication() {
		log.info("::::::::::::::Started CI-CD Post Deployment Service (PDS) Application::::::::::::::");
		List<String> directories = automationUtils.getDirectories(config.getFolderRelativePath());
		AutomationEnum automationEnum = automationUtils.getAutomationEnum(directories);
		if (automationEnum != null) {
			Executer executer = automationServiceFactory.getAutomationServiceInstance(automationEnum);
			executer.automateExecution(config.getFolderRelativePath());
		}
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("::::::::::::::CI-CD Post Deployment Service (PDS) Application Stopped::::::::::::::");
	}

}
