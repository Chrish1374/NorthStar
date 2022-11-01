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


public class SauceDriver implements IDriver {

	private static final Logger LOGGER = Logg.createLogger();
	
	private static final String NAME = TEST_PROPERTIES.get("username");
	private static final String KEY = TEST_PROPERTIES.get("access_key");

	private static final String PLATFORM = TEST_PROPERTIES.get("os_platform");
	private static final String VERSION = TEST_PROPERTIES.get("browser_version");



	public WebDriver getDriver(Browser browser) {

		LOGGER.info(Utilities.getCurrentThreadId() + "**SAUCE Driver**");
		WebDriver driver = null;
		LOGGER.info(Utilities.getCurrentThreadId() + "** "+browser.getName()+ " Browser**"); 
		DesiredCapabilities capabilities = null;

		//        DesiredCapabilities caps = DesiredCapabilities.Chrome();
		//        caps.setCapability("platform", "Windows XP");
		//        caps.setCapability("version", "43.0");

		try {
			if ("Chrome".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Sauce " + browser + " Browser**");
				//                capabilities = SauceCapabilities.getDesiredCapability(browser);

				capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability("platform", PLATFORM);
				capabilities.setCapability("version", VERSION);



			} else if ("firefox".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote FireFox Browser**");
				capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability("platform", PLATFORM);
				capabilities.setCapability("version", VERSION);


			} else if ("internet explorer".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote internet explorer Browser**");
				capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability("platform", PLATFORM);
				capabilities.setCapability("version", VERSION);


			}

			else if ("safari".equals(browser.getName())) {
				LOGGER.info(Utilities.getCurrentThreadId() + "**Remote safari Browser**");
				capabilities = DesiredCapabilities.safari();
				capabilities.setCapability("platform", PLATFORM);
				capabilities.setCapability("version", VERSION);


			}
			driver = new RemoteWebDriver(
					new URL("http://" + NAME + ":" + KEY + "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
			LOGGER.info(Utilities.getCurrentThreadId() + "Returning the sauce instance of:"
					+ driver);


			//            driver = new RemoteWebDriver(
			//                    new URL("http://" + NAME + ":" + KEY + "@ondemand.saucelabs.com:80/wd/hub"), caps);
			//            LOGGER.info(Utilities.getCurrentThreadId() + "Returning the sauce instance of:"
			//                    + driver);
		} catch (MalformedURLException me) {
			LOGGER.info(
					"MalformedURLException in the getDriver() method of the SauceDriver class", me);
		}
		return driver;
	}
}