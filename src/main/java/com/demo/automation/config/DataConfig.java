package com.demo.automation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DataConfig {

	@Value("${folder.path}")
	private String folderRelativePath;

}
