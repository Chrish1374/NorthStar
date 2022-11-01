package com.trn.ns.enums;

public enum BrowserType {
	
	FIREFOX("firefox"),
	INTERNETEXPLORER("internet explorer"),
	CHROME("chrome"),
	EDGE("edge");

	private String typeOfBrowser;

	private BrowserType(String type){
		typeOfBrowser = type ;
	}

	public String getBrowserValue(){
		return typeOfBrowser;
	}
}
