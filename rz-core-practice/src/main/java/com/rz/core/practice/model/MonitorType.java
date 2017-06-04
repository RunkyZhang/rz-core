package com.rz.core.practice.model;

public enum MonitorType {
//	RED, GREEN, BLANK, YELLOW
	RED("red", 1), GREEN("grenn", 2), BLANK("blank", 3), YELLO("yello", 4);
	
	private String _name;
    private int _code;
	
	MonitorType(String name, int code){
		this._name = name;
		this._code = code;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public int getCode() {
		return _code;
	}

	public void setCode(int code) {
		this._code = code;
	}
}
