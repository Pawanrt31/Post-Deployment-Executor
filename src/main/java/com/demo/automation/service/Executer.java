package com.demo.automation.service;

import com.demo.automation.constants.AutomationEnum;

public interface Executer {

	public void automateExecution(String name);

	public AutomationEnum getExecuteType();
}
