package com.trn.ns.drivers;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.trn.ns.enums.BrowserType;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class WebCapabilities {

	private static DesiredCapabilities capabilities;
	protected static final Logger LOGG = Logg.createLogger();

	private static String language;
	private static String inputZipFilePath;

	private WebCapabilities() {

	}

	public static DesiredCapabilities getDesiredCapability(Browser browser){

		setBrowserLanguage(browser.getLanguage());
		capabilities = getCapability(browser.getName());    	
		capabilities.setBrowserName(browser.getName());
		capabilities.setPlatform(browser.getPlatform());
		capabilities.setVersion(browser.getVersion());     
		capabilities.setJavascriptEnabled(true);
		return capabilities;
	}

	private static DesiredCapabilities getCapability(String browserName) {

		inputZipFilePath = Configurations.TEST_PROPERTIES.get("logFolderPath");
		inputZipFilePath = System.getProperty("user.dir") + inputZipFilePath;
		
		File f = new File(inputZipFilePath);
	
		Map<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("download.default_directory", f.getAbsolutePath());
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);
		
		
		

		if(BrowserType.CHROME.getBrowserValue().contentEquals(browserName)){


			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			// Below settings are for headless - kept it as it might require in future , for use we just need to un comment it
//			options.addArguments("--headless");
//			options.addArguments("--disable-gpu");
//			options.addArguments("--window-size=1920,969");
//			options.addArguments("--window-size=1916,925");
//			options.addArguments("--window-size=1920,1080");

			
			options.addArguments("--incognito");
			options.addArguments("start-maximized", "forced-maximize-mode","no-default-browser-check", "always-authorize-plugins","test-type");
			options.addArguments("--lang=" + WebCapabilities.language);  			
			options.setExperimentalOption("useAutomationExtension", false);
			options.setExperimentalOption("excludeSwitches",Collections.singletonList("enable-automation")); 
			options.addArguments("--test-type");
			options.addArguments("--disable-extensions");
			
					
			LoggingPreferences loggingprefs = new LoggingPreferences();
			loggingprefs.enable(LogType.BROWSER, Level.ALL);
			loggingprefs.enable(LogType.PERFORMANCE, Level.WARNING);
		    options.setCapability( "goog:loggingPrefs", loggingprefs );
			
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
			


		} else if(BrowserType.FIREFOX.getBrowserValue().contentEquals(browserName)){   		

			capabilities = DesiredCapabilities.firefox();		
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("intl.accept_languages", WebCapabilities.language);       	
			firefoxProfile.setPreference("browser.download.folderList", 2);
			firefoxProfile.setPreference("browser.download.dir", inputZipFilePath);
			firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/gzip, application/x-zip-compressed, application/download, application/octet-stream");
			LoggingPreferences loggingprefs = new LoggingPreferences();
			loggingprefs.enable(LogType.BROWSER, Level.ALL);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
			capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

		}	   
		else if(BrowserType.INTERNETEXPLORER.getBrowserValue().contentEquals(browserName)){


			capabilities = DesiredCapabilities.internetExplorer();

			LoggingPreferences loggingprefs = new LoggingPreferences();
			loggingprefs.enable(LogType.BROWSER, Level.ALL);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
//			capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
//			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setCapability("nativeEvents", true);
			
			


		}else if(BrowserType.EDGE.getBrowserValue().contentEquals(browserName)){
			capabilities = DesiredCapabilities.edge();
			LoggingPreferences loggingprefs = new LoggingPreferences();
			loggingprefs.enable(LogType.BROWSER, Level.ALL);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);

		}

		else {
			capabilities = DesiredCapabilities.chrome();
		}
		return capabilities;
	}

	private static String setBrowserLanguage(String lang) {

		LOGG.debug(Utilities.getCurrentThreadId() + "Browser locale : " + lang);

		language= lang;	
		return language;
	}
}

