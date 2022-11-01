package com.trn.ns.drivers;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class RemoteDriver implements IDriver {

	private static final Logger LOGGER = Logg.createLogger();
	
	private static final String HUBADDRESS = TEST_PROPERTIES.get("hubIP");
	private static final String HUBPORT = TEST_PROPERTIES.get("hubPort");

	@Override
	public WebDriver getDriver(Browser browser) {
		WebDriver driver = null;
		DesiredCapabilities capabilities = null;
		try {
			if ("internet explorer".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote Internet Explorer Browser**");
				capabilities = WebCapabilities.getDesiredCapability(browser);

			} else if ("firefox".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote FireFox Browser**");
				capabilities = WebCapabilities.getDesiredCapability(browser);
			} else if ("Chrome".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote Chrome Browser**");
				capabilities = WebCapabilities.getDesiredCapability(browser);
			}
			driver = new RemoteWebDriver(
					new URL("http://" + HUBADDRESS + ":" + HUBPORT + "/wd/hub"), capabilities);
			LOGGER.info(Utilities.getCurrentThreadId() + "Returning the remote instance of:"
					+ driver);
		} catch (MalformedURLException me) {
			LOGGER.info(
					"MalformedURLException in the getDriver() method of the RemoteDriver class", me);
		}
		return driver;
	}
}
