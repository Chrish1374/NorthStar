package com.trn.ns.drivers;

import io.appium.java_client.AppiumDriver;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class MobileDriver implements IDriver {
	
	private static final Logger LOGGER = Logg.createLogger();
	private static final String APPIUMSERVER = TEST_PROPERTIES.get("appiumServerIP");
	private static final String APPIUMPORT = TEST_PROPERTIES.get("appiumServerPort");

	@Override
	public WebDriver getDriver(Browser browser) {
		WebDriver driver = null;
		DesiredCapabilities capabilities = MobileCapabilities.setAppiumNativeAppCapability();
		try {
			LOGGER.info(Utilities.getCurrentThreadId() + "**Mobile Driver**");
			LOGGER.info(Utilities.getCurrentThreadId() + "Instantiating the Appium Driver");
			driver = new AppiumDriver(new URL("http://" + APPIUMSERVER + ":" + APPIUMPORT
					+ "/wd/hub"), capabilities);
			LOGGER.info(Utilities.getCurrentThreadId() + "Returning the mobile instance of:"
					+ driver);
		} catch (MalformedURLException me) {
			LOGGER.info(
					"MalformedURLException in the getDriver() method of the MobileDriver class", me);
		}
		return driver;
	}
}