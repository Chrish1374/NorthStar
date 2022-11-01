package com.trn.ns.drivers;

import org.openqa.selenium.Platform;

public class Browser {
	private String name;
	private String version;
	private Platform platform;
	private String lang;

	public Browser(String name, String version, Platform platform, String lang) {
		this.name = name;
		this.version = version;
		this.platform = platform;
		this.lang = lang == null || lang.trim().isEmpty() ? "en" : lang;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Platform getPlatform() {
		return platform;
	}

	public String getLanguage() {
		return lang;
	}
}
