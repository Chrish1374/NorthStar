package com.trn.ns.drivers;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.trn.ns.enums.BrowserType;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class LocalDriver implements IDriver {

	private static final Logger LOGGER = Logg.createLogger();

	@Override
	public WebDriver getDriver(Browser browser) {

		WebDriver driver = null;
		LOGGER.info(Utilities.getCurrentThreadId() + "** "+browser.getName()+ " Browser**");        

		if (BrowserType.INTERNETEXPLORER.getBrowserValue().equals(browser.getName())) {
			System.setProperty("webdriver.ie.driver",
					"src/main/resources/com/drivers/IEDriverServer.exe");

			DesiredCapabilities capabilities = WebCapabilities.getDesiredCapability(browser);
			capabilities.setCapability("requireWindowFocus", true);
			driver = new InternetExplorerDriver(capabilities);
			driver.manage().window().maximize();

		} else if (BrowserType.FIREFOX.getBrowserValue().equals(browser.getName())) {

			System.setProperty("webdriver.gecko.driver","src/main/resources/com/drivers/geckodriver.exe");
			DesiredCapabilities capabilities = WebCapabilities.getDesiredCapability(browser);
			capabilities.setCapability("marionette", true);
			capabilities.setAcceptInsecureCerts(true);
			driver = new FirefoxDriver(capabilities);
			driver.manage().window().maximize();

		} else if (BrowserType.CHROME.getBrowserValue().equals(browser.getName())) {

			System.setProperty("webdriver.chrome.driver",
					"src/main/resources/com/drivers/chromedriver.exe");
			DesiredCapabilities capabilities = WebCapabilities.getDesiredCapability(browser);

			driver = new ChromeDriver(capabilities);
		}
		
		else if (BrowserType.EDGE.getBrowserValue().equals(browser.getName())) {

			System.setProperty("webdriver.edge.driver",
					"src/main/resources/com/drivers/MicrosoftWebDriver.exe");
			DesiredCapabilities capabilities = WebCapabilities.getDesiredCapability(browser);

			driver = new EdgeDriver(capabilities);
			driver.manage().window().maximize();
		}

		LOGGER.info(Utilities.getCurrentThreadId()
				+ "Instantiating/Launching the "+browser.getName()+" Browser");
		if(driver!= null)
			LOGGER.info(Utilities.getCurrentThreadId() + "Returning the local instance of:"
					+ driver.toString());
		else
			LOGGER.error(Utilities.getCurrentThreadId() + " Driver is not initialised, null returned!");
		return driver;
	}

}
