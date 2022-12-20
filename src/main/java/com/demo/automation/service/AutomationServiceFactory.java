package com.demo.automation.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.automation.constants.AutomationEnum;

@Service
public class AutomationServiceFactory {

	@Autowired
	private List<Executer> executerList;

	private final Map<AutomationEnum, Executer> executerMap = new EnumMap<>(AutomationEnum.class);

	@PostConstruct
	public void initAutomationExecuter() {
		for (Executer executer : executerList) {
			executerMap.put(executer.getExecuteType(), executer);
		}
	}

	public void setExecuterList(List<Executer> executer) {
		this.executerList = executer;
	}

	public Executer getAutomationServiceInstance(AutomationEnum automationEnum) {
		return executerMap.get(automationEnum);
	}
}
