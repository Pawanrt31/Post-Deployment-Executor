package com.demo.automation.constants;

public enum AutomationEnum {

	JAVA("java"), PYTHON("python"), BAT("bat");
	private String executer;

	/**
	 *
	 * @param type
	 */
	AutomationEnum(String type) {
		this.executer = type;
	}

	public String getExecuter() {
		return executer;
	}
}
