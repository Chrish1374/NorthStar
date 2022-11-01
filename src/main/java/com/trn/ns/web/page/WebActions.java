package com.trn.ns.web.page;

import static com.trn.ns.test.configs.Configurations.FAIL;
import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;

import com.google.common.base.Function;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.enums.BrowserType;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.ImageComparer;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class WebActions {

	protected WebDriver driver;
	private WebDriver WEB_DRIVER = null ;
	private static final ImageComparer comparer = new ImageComparer();
	protected static final Logger LOGGER = Logg.createLogger();
	private static final String SKIPLOGIN = "skipLogin";
	public  ExtentTest extentTest;
	private String APPLICATIONURL;

	private String APPLICATIONURLUSINGUSER;
	protected static final String ELEMENTSEARCHTIMEOUT = "elementSearchTimeOut";
	private static final String ELEMENTWAITTIME = "waitForElement";
	public static String extractedFolderPath;

	public static final String VISIBILITY = "visibility";
	public static final String PRESENCE = "presence";
	public static final String CLICKABILITY = "clickability";

	private JavascriptExecutor js = null;

	public WebActions(WebDriver driver) {

		WEB_DRIVER = driver;
		// URL to access the North Star Application
		APPLICATIONURL= "http://"+TEST_PROPERTIES.get("nsHostName").trim()+":"+TEST_PROPERTIES.get("nsPort").trim()+"/#";
		APPLICATIONURLUSINGUSER= APPLICATIONURL + "?user="+TEST_PROPERTIES.get("nsUserName")+"&pass="+TEST_PROPERTIES.get("nsPassword");
		this.driver = driver;
		extentTest=ExtentManager.getTestInstance();		

	}

	/**
	 * @author anilt
	 * @param key
	 * @param value
	 * Description: This function sets the cookie with key and value specified in arguments
	 */
	public void setCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		driver.manage().addCookie(cookie);
		LOGGER.info(Utilities.getCurrentThreadId() + "Successfully added cookie named " + key
				+ " to the HTML page");
	}

	/**
	 * @author anilt
	 * @param key - cookie name for which value is to be retrieved
	 * @return String containing cookie value
	 * Description: This function returns value for the cookie passed as argument
	 */
	public String getCookie(String key) {
		LOGGER.info(Utilities.getCurrentThreadId() + "Retrieving the value "
				+ driver.manage().getCookieNamed(key).getValue() + " stored in the cookie");
		return driver.manage().getCookieNamed(key).getValue();
	}

	/**
	 * @author abhishekr
	 * @param URL
	 * Description: This function is used to navigate to the url specified in argument
	 */
	public void navigateToURL(String URL) {
		try {
			LOGGER.info(Utilities.getCurrentThreadId() + "Navigating to URL: " + URL);	
			driver.navigate().to(URL);
			LOGGER.info(Utilities.getCurrentThreadId() + "Successfully navigated to Application URL on the Local Browser");
		} catch(Exception e) {

			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in navigateToURL()", e.toString());
			Assert.assertTrue(false, "Exception in navigateToURL(): " + e.getStackTrace());
		}
	}

	public void navigateToChangedURL(String URL) {
		try {
			LOGGER.info(Utilities.getCurrentThreadId() + "Navigating to URL: " + URL);	
			// Timeout exception in latest chrome driver 2.35 hence using javascript
			//driver.navigate().to(URL);
			((JavascriptExecutor) driver).executeScript("window.open(\""+URL+"\",\"_self\");");
			LOGGER.info(Utilities.getCurrentThreadId() + "Successfully navigated to Application URL on the Local Browser");
		} catch(Exception e) {

			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in navigateToURL()", e.toString());
			Assert.assertTrue(false, "Exception in navigateToURL(): " + e.getStackTrace());
		}
	}

	/**
	 * @author anilt
	 * Description: This function is used to navigate to the base url of the application
	 */
	public void navigateToBaseURL() {
		try {
			if ((TEST_PROPERTIES.get(SKIPLOGIN)).toUpperCase().equals("YES")) {
				LOGGER.info(Utilities.getCurrentThreadId() + "Navigating to Application URL on Local Browser: " + APPLICATIONURLUSINGUSER);
				driver.get(APPLICATIONURLUSINGUSER);
			} else {
				LOGGER.info(Utilities.getCurrentThreadId() + "Navigating to Application URL on Local Browser: " + APPLICATIONURL);

				if(!driver.getCurrentUrl().contains(APPLICATIONURL))
					driver.get(APPLICATIONURL);	
				waitForTimePeriod(100);
			}
			LOGGER.info(Utilities.getCurrentThreadId()	+ "Successfully navigated to Application URL on the Local Browser");
		} catch(Exception e) {
			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in navigateToBaseURL()", e.toString());
			Assert.assertTrue(false, "Exception in navigateToBaseURL(): " + e.toString());
		}
	}

	/**
	 * @author anilt
	 * @param context
	 * Description: This function closes the browser instance
	 */
	public void closeBrowser(ITestContext context) {
		driver.manage().deleteAllCookies();
		clearCache();
		try {
			driver.close();
			driver.quit();
			LOGGER.info(Utilities.getCurrentThreadId() + "Closing the browser");
			context.getAttribute(context.getCurrentXmlTest().getName());        
			LOGGER.info(Utilities.getCurrentThreadId() + "Sucessfully closed the browser" + "\n");
		} catch(Exception e) {
			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in closeBrowser()", e.toString());
			Assert.assertTrue(false, "Exception in closeBrowser(): " + e.getStackTrace());
		}

	}

	public void clearCache(){
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		ChromeDriver driver = new ChromeDriver(capabilities);
	}
	/**
	 * @param syncKey
	 * @param element
	 * @param value
	 * @throws TimeoutException
	 * @throws WaitException
	 * Description: This function enters the specified value in the webelement
	 */
	public void enterText(String syncKey, By element, String value) throws TimeoutException
	{
		WebElement webElement = null;
		webElement = syncElementUsing(syncKey,  element);
		LOGGER.info(Utilities.getCurrentThreadId() + "Clearing the content of the text box");
		webElement.clear();
		LOGGER.info(Utilities.getCurrentThreadId() + "Contents cleared");
		webElement.sendKeys(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Entered text:" + value
				+ " in text box with locator:" + element);
	}

	public void enterText(String syncKey, WebElement element, String value) throws TimeoutException
	{

		element.click();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clearing the content of the text box");
		element.clear();
		LOGGER.info(Utilities.getCurrentThreadId() + "Contents cleared");
		element.sendKeys(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Entered text:" + value
				+ " in text box with locator:" + element);
	}

	public void enterText(WebElement element, String value) throws TimeoutException
	{
		element.clear();
		LOGGER.info(Utilities.getCurrentThreadId() + "Contents cleared");
		element.sendKeys(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Entered text:" + value
				+ " in text box with locator:" + element);
	}

	public void enterTextWithView(WebElement element, String value) throws TimeoutException
	{
		if(!element.isDisplayed())
			scrollIntoView(element);
		element.clear();
		LOGGER.info(Utilities.getCurrentThreadId() + "Contents cleared");
		element.sendKeys(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Entered text:" + value
				+ " in text box with locator:" + element);
	}

	public void enterText(String value) throws TimeoutException
	{
		Actions ac = new Actions(driver);
		ac.sendKeys(value).perform();

	}

	public void click(String syncKey, By element) throws TimeoutException{
		syncElementUsing(syncKey, element).click();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on element with locator:" + element);
	}

	public void click(String syncKey, WebElement element) throws TimeoutException{
		syncElementUsing(syncKey, element).click();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on element with locator:" + element);
	}

	public void clickUsingAction(WebElement element) throws TimeoutException{

		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		action.click(element).perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on element with locator:" + element);
	}
	/**
	 * @param syncKey
	 * @param element
	 * @throws TimeoutException
	 * @throws WaitException
	 * Description: Function to right click on the given element
	 */
	public void contextClick(String syncKey, By element) throws TimeoutException {
		Actions action = new Actions(driver);
		action.contextClick(syncElementUsing(syncKey, element)).perform();
		LOGGER.info(Utilities.getCurrentThreadId()
				+ "Context click performed on element with locator:" + element + " using JQuery");
	}

	/**
	 * @author anilt
	 * @param element
	 * Description: Function to click on the given element using jquery
	 */
	public void clickByJQuery(String element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("$('" + element + "').click()");
		LOGGER.info("Clicked on element with locator:" + element + " using JQuery");
	}

	public boolean verifyPresenceOfVerticalScrollbar(WebElement element){

		boolean scrollBarPresent = false;
		String script = " return arguments[0].scrollHeight > arguments[0].clientHeight ;";
		scrollBarPresent = (boolean) ((JavascriptExecutor)driver).executeScript(script,element);
		return scrollBarPresent;

	}

	public boolean verifyPresenceOfHorizontalScrollbar(WebElement element){

		boolean scrollBarPresent = false;
		String script = "return arguments[0].scrollWidth > arguments[0].clientWidth;";
		scrollBarPresent = (boolean) ((JavascriptExecutor)driver).executeScript(script,element);
		return scrollBarPresent;

	}
	/**
	 * @author payala2
	 * @param element
	 * @throws null
	 * Description: Function to scroll down to the specific element
	 */
	public void scrollIntoView(WebElement element){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}

	public void scrollToEnd(){
		((JavascriptExecutor) driver)
		.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}



	/**
	 * @author anilt
	 * @param element
	 * @throws Exception
	 * Description: Function to click on the given element using javascript
	 */
	public void clickByJExecutor(By element){
		WebElement ele = driver.findElement(element);
		if (ele.isEnabled() && ele.isDisplayed()) {
			LOGGER.info("Clicking on element with locator:" + ele + " using JavaExecutor");
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
		} else {
			LOGGER.info("Unable to click on element with locator:" + ele + " using JavaExecutor");
		}
	}

	/**
	 * @param syncKey
	 * @param element
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public void submitForm(String syncKey, By element) throws TimeoutException{
		syncElementUsing(syncKey,  element).submit();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on form submit button:" + element);
	}

	/**
	 * @param windowTitle
	 * @throws WaitException
	 * @throws InterruptedException
	 */
	public void switchToSecondaryWindow(String windowTitle) throws  InterruptedException {
		waitForTimePeriod("High");
		Set<String> windows = driver.getWindowHandles();
		for (String strWindows : windows) {
			if (driver.switchTo().window(strWindows).getTitle().equals(windowTitle)) {
				driver.switchTo().window(strWindows);
				//				driver.switchTo().window(strWindows).manage().window().maximize();
				break;
			}
		}
	}

	/**
	 * @param syncKey
	 * @param parentLocator
	 * @param value
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public void selectOption(String syncKey, By parentLocator, String value) throws TimeoutException{
		List<WebElement> element = syncElementsUsing(syncKey, parentLocator);
		for (int i = 0; i < element.size(); i++) {
			String temp = element.get(i).getText().replace((char) 0x00a0, (char) 0x0020);
			if (Utilities.compareExactText(value, temp.trim())) {
				LOGGER.info(Utilities.getCurrentThreadId() + " " + "Clicking on option " + value);
				element.get(i).click();
				LOGGER.info(Utilities.getCurrentThreadId() + "Successfully Clicked on option "
						+ temp);
				break;
			}
		}
	}

	/**
	 * @param syncKey
	 * @param element
	 * @param value
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public void selectFromDropDown( By element, String value) throws TimeoutException {
		Select select = new Select(syncElementUsing("presence",  element));
		select.selectByVisibleText(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Selected:" + value + " from drop-down with locator:" + element);
	}

	public String getText(String syncKey, By element) throws TimeoutException {
		String actual = syncElementUsing(syncKey,  element).getText().trim();
		LOGGER.info(Utilities.getCurrentThreadId() + "Actual Value:" + actual);
		return actual;
	}

	public String getText(String syncKey, WebElement element) throws TimeoutException {
		String actual = syncElementUsing(syncKey,  element).getText().trim();
		LOGGER.info(Utilities.getCurrentThreadId() + "Actual Value:" + actual);
		return actual;
	}

	public String getText(WebElement element) throws TimeoutException {
		String actual = element.getText().trim();
		LOGGER.info(Utilities.getCurrentThreadId() + "Actual Value:" + actual);
		return actual;
	}

	/**
	 * @author anilt
	 * @return
	 * Description: This function returns the page title
	 */
	public String getTitle() {
		LOGGER.info(Utilities.getCurrentThreadId() + "Title of the page:" + driver.getTitle());
		return driver.getTitle();
	}

	/**
	 * @param syncKey
	 * @param element
	 * @param attribute
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public String getAttributeValue(String syncKey, By element, String attribute) throws TimeoutException {
		LOGGER.info(Utilities.getCurrentThreadId() + "Retrieving the attribute " + attribute + " of element " + element);
		String value = syncElementUsing(syncKey, element).getAttribute(attribute);
		return value;
	}

	/**
	 * @param webElement
	 * @param attribute
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public String getAttributeValue(WebElement webElement, String attribute) throws TimeoutException {
		LOGGER.info(Utilities.getCurrentThreadId() + "Retrieving the attribute " + attribute );
		String attributeValue= webElement.getAttribute(attribute);
		return attributeValue;
	}

	/**
	 * @param syncKey
	 * @param locator
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public List<String> getWebElementsTextInList(String syncKey, By locator) throws TimeoutException {
		List<WebElement> weblElementList = syncElementsUsing(syncKey, locator);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < weblElementList.size(); i++) {
			list.add(weblElementList.get(i).getText());
		}
		return list;
	}


	public String getCssValue(WebElement webElement, String property) throws TimeoutException {
		//		LOGGER.info(Utilities.getCurrentThreadId() + "Retrieving the property " + property );

		String attributeValue= webElement.getCssValue(property);
		return attributeValue;
	}


	public boolean verifyAttributePresence(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				result = true;
			}
		} catch (Exception e) {}

		return result;


	}


	public String getAllAttributes(WebElement element) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Object attributes=executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);
		return attributes.toString();
	}

	public boolean verifyCssPropertyPresence(WebElement element, String property) {

		Boolean result = false;
		try {
			String value = element.getCssValue(property);
			if ((!value.equalsIgnoreCase("none")) && value!= null){
				result = true;
			}
		} catch (Exception e) {}

		return result;


	}
	/**
	 * @param syncKey
	 * @param locator
	 * @return
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public List<WebElement> getWebElementsInList(String syncKey, By locator) throws TimeoutException {
		List<WebElement> weblElementList = syncElementsUsing(syncKey, locator);
		return weblElementList;
	}

	/**
	 * @param syncKey
	 * @param locator
	 * @return
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public int getWebElementsSize(String syncKey, By locator) throws TimeoutException {
		List<WebElement> weblElementList = syncElementsUsing(syncKey, locator);
		return weblElementList.size() ;
	}

	/**
	 * @param locator
	 * @return
	 * @throws WaitException
	 */
	public Boolean getVisibiltyOfElementLocatedBy(By locator)  {
		return checkForElementVisibility(locator);
	}

	/**
	 * @param locator
	 * @return
	 * @throws WaitException
	 */
	public Boolean getPresenceOfElementLocatedBy(By locator) {
		return checkForElementPresence(locator);
	}

	/**
	 * @param locator
	 * @return
	 * @throws WaitException
	 */
	public Boolean getClickabilityOfElementLocatedBy(By locator) {
		return checkForElementClickability( locator);
	}

	/**
	 * @param locator
	 * @return
	 * @throws WaitException
	 */
	public Boolean getInvisibilityOfElementLocatedy(By locator) {
		return waitForElementInVisibility(locator);
	}

	/**
	 * @param key
	 */
	public void pressModifierKey(Keys key) {
		Actions action = new Actions(driver);
		action.keyDown(key).perform();
	}

	/**
	 * @param key
	 */
	public void pressKeys(Keys key) {
		Actions action = new Actions(driver);
		action.sendKeys(key).moveByOffset(1, 1).build().perform();
	}

	/**
	 * @param String
	 */
	public void pressKeys(String asciiValue) {
		Actions action = new Actions(driver);
		action.sendKeys(asciiValue).perform();
	}

	/**
	 * @param syncKey
	 * @param locator
	 * @throws TimeoutException
	 * @throws WaitException
	 */

	public void switchToFrame(String syncKey, By locator) throws TimeoutException {
		driver.switchTo().frame(syncElementUsing(syncKey,  locator));
		LOGGER.info("Switched to frame with locator:" + locator);
	}

	/**
	 * @param element
	 * @throws TimeoutException
	 * @throws WaitException
	 */
	public void hitEnter(By element) throws TimeoutException {
		WebElement webElement = null;
		webElement = syncElementUsing("visibility", element);
		webElement.sendKeys(Keys.RETURN);
	}

	/**
	 * This method will take the default webdriver instance.
	 * @param give any specific Path with image name were you want to create the snapshot.
	 * @return the path of the screenshot name
	 */
	public boolean takeScreenShot(String destinationFilepath){
		boolean status= false; 
		try {
			File screenshot = null;
			if(WEB_DRIVER  != null){
				waitForTimePeriod(1000);
				screenshot= ((TakesScreenshot) WEB_DRIVER).getScreenshotAs(OutputType.FILE);
				LOGGER.info(Utilities.getCurrentThreadId() + "Screenshot taken successfully. Name of file :" + destinationFilepath);
				FileUtils.copyFile(screenshot, new File(destinationFilepath));
				status= true;
			}
			else 
				LOGGER.info("WEB_DRIVER is not initialised, can't take a screeshot");
		} catch (Exception e) {
			LOGGER.error(new StringBuilder()
					.append(Utilities.getCurrentThreadId() + "Failed to capture screenshot ----: ")
					.append(e.getMessage()),e);
		}
		return status;

	}

	/**
	 * @param element
	 * @param destinationFilepath
	 * @return true if screenshot is taken, else false
	 */
	public boolean takeElementScreenShot(By element,String destinationFilepath){
		boolean status= false;
		//		webActions.takeElementScreenShot(LOAD_BUTTON, "D:\\screenshot.png");
		try {
			File screenshot = null;
			if(WEB_DRIVER  != null){
				waitForTimePeriod(1000);
				screenshot = ((TakesScreenshot) WEB_DRIVER).getScreenshotAs(OutputType.FILE);
				WebElement snapElement = WEB_DRIVER.findElement(element);
				int ImageWidth = snapElement.getSize().getWidth();
				int ImageHeight = snapElement.getSize().getHeight();

				Point point = snapElement.getLocation();
				int xcord = point.getX();
				int ycord = point.getY();
				BufferedImage img = ImageIO.read(screenshot);

				BufferedImage dest = img.getSubimage(xcord, ycord, ImageWidth, ImageHeight);
				ImageIO.write(dest, "png", screenshot);

				FileUtils.copyFile(screenshot, new File(destinationFilepath));
				LOGGER.info(Utilities.getCurrentThreadId() + "Element Screenshot taken successfully. Name of file :" + destinationFilepath);
				status= true;
			}
			else 
				LOGGER.info("WEB_DRIVER is not initialised, can't take a screeshot");

		} catch (Exception e) {
			LOGGER.error(new StringBuilder()
					.append(Utilities.getCurrentThreadId() + "Failed to capture element screenshot ----: ")
					.append(e.getMessage()),e);
		}
		return status;

	}

	/**
	 * @param element
	 * @param destinationFilepath
	 * @return true if screenshot is taken, else false
	 */
	public boolean takeElementScreenShot(WebElement element, String destinationFilepath){
		boolean status= false;
		try {
			File screenshot = null;
			if(WEB_DRIVER  != null){
				waitForTimePeriod(1000);
				screenshot = ((TakesScreenshot) WEB_DRIVER).getScreenshotAs(OutputType.FILE);
				WebElement snapElement = element;
				int ImageWidth = snapElement.getSize().getWidth();
				int ImageHeight = snapElement.getSize().getHeight();

				Point point = snapElement.getLocation();
				int xcord = point.getX();
				int ycord = point.getY();
				BufferedImage img = ImageIO.read(screenshot);

				BufferedImage dest = img.getSubimage(xcord, ycord, ImageWidth, ImageHeight);
				ImageIO.write(dest, "png", screenshot);

				FileUtils.copyFile(screenshot, new File(destinationFilepath));
				LOGGER.info(Utilities.getCurrentThreadId() + "Element Screenshot taken successfully. Name of file :" + destinationFilepath);
				status= true;
			}
			else 
				LOGGER.info("WEB_DRIVER is not initialised, can't take a screeshot");

		} catch (Exception e) {
			LOGGER.error(new StringBuilder()
					.append(Utilities.getCurrentThreadId() + "Failed to capture element screenshot ----: ")
					.append(e.getMessage()),e);
		}
		return status;

	}

	public boolean takeElementScreenShot(WebElement element, String destinationFilepath, int[] coordinates){


		boolean status = takeElementScreenShot(element, destinationFilepath);

		try {
			Utilities.cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], destinationFilepath, destinationFilepath);
		} catch(IOException e) {
			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
			Assert.assertTrue(false, "Exception in compareElementCroppedImage() during image crop: " + e.toString());
		}		

		return status;
	}

	/**
	 * @param element
	 * @param index
	 * @return WebElement found using the specified locator and index
	 */
	public WebElement getElements(By element,int index){
		List<WebElement> list =driver.findElements(element);
		LOGGER.info(Utilities.getCurrentThreadId() + "Finding the element with locator:" + element);
		return list.get(index);
	}
	/**
	 * @author payala2
	 * @param element
	 * @return: WebElement found using the specified locator
	 * Description: This method catches the NoSuchElementException exception
	 */
	public WebElement getElement(By ele){

		WebElement element = null ;
		try {
			element =  driver.findElement(ele);

		} catch (NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}

		return element;
	}


	public List<WebElement> getElements(By ele){

		List<WebElement> element = new ArrayList<WebElement>() ;
		try {
			element =  driver.findElements(ele);

		} catch (NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}

		return element;
	}
	/**
	 * @author abhishekr
	 * @param syncKey
	 * @param element
	 * @throws TimeoutException
	 * @throws InterruptedException 
	 * @throws WaitException
	 * Description: Function to perform mouse hover on given element
	 */
	public void mouseHover(String syncKey, By element) throws TimeoutException, InterruptedException {

		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
		WebElement hoverElement = syncElementUsing(syncKey, element);
		if(browserName.equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} "
					+ "else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouse Focussing (hover) on element");
			js.executeScript(mouseOverScript, hoverElement);
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator: " + hoverElement);
			waitForTimePeriod("Low");
		}
		else {
			Actions action = new Actions(driver);
			action.moveToElement(hoverElement).build().perform();
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element);
		}
	}
	public void mouseHover(String syncKey, WebElement element) throws TimeoutException, InterruptedException {
		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
		if(browserName.equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} "
					+ "else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouce Focussing (hover) on element");
			js.executeScript(mouseOverScript, element);
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator: " + element);
			waitForTimePeriod("Low");
		}
		else {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element);
		}
	}
	/**
	 * @author abhishekr
	 * @param syncKey
	 * @param element
	 * @param xOffset
	 * @param yOffset
	 * @throws TimeoutException
	 * @throws InterruptedException 
	 * @throws WaitException
	 * Description: Function to perform mouse hover on given element and then move mouse to (xOffset, yOffset) pixel  
	 */
	public void mouseHover(String syncKey, By element, int xOffset,int yOffset) throws TimeoutException {

		WebElement hoverElement = syncElementUsing(syncKey, element);
		Actions action = new Actions(driver);
		action.moveToElement(hoverElement).moveByOffset( xOffset, yOffset).build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHover(String syncKey, WebElement element, int xOffset,int yOffset) throws TimeoutException {

		WebElement hoverElement = syncElementUsing(syncKey, element);
		Actions action = new Actions(driver);
		action.moveToElement(hoverElement).moveByOffset( xOffset, yOffset).build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHoverWithClick(String syncKey, By element, int xOffset,int yOffset) throws TimeoutException {

		WebElement hoverElement = syncElementUsing(syncKey, element);
		Actions action = new Actions(driver);
		action.moveToElement(hoverElement).moveByOffset( xOffset, yOffset).click().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHoverWithClick(String syncKey, WebElement element, int xOffset,int yOffset) throws TimeoutException {
		Actions action = new Actions(driver);
		action.moveToElement(element).moveByOffset(xOffset, yOffset).click().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}
	public void mouseHoverWithClick(String syncKey, WebElement element) throws TimeoutException {
		Actions action = new Actions(driver);
		action.moveToElement(element).click().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHoverWithContextClick(String syncKey, By element, int xOffset,int yOffset) throws TimeoutException {

		WebElement hoverElement = syncElementUsing(syncKey, element);
		Actions action = new Actions(driver);
		action.moveToElement(hoverElement).moveByOffset( xOffset, yOffset).contextClick().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHover(WebElement element) throws InterruptedException {

		Actions action = new Actions(driver);
		action.moveToElement(element).pause(100).build().perform();
		waitForTimePeriod(1000);
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on element with locator:" + element + "and Offset");

	}

	public void mouseHover(int xOffset, int yOffset) throws TimeoutException, InterruptedException {

		Actions action = new Actions(driver);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hovered on x offset="+ xOffset+ " y offset="+yOffset);

	}
	/**
	 * @author abhishekr
	 * @param xOffset : Destination XOffset point
	 * @param yOffset : Destination YOffset point
	 * @throws InterruptedException
	 * @Description : This function is used to perform dragAndDrop function
	 */
	public void clickAndHold_Release(int xOffset, int yOffset) throws InterruptedException {
		Actions action = new Actions(driver);
		int [] pixelArrayXoffset, pixelArrayYoffset;

		int hopValue= Integer.parseInt(TEST_PROPERTIES.get("hop"));
		pixelArrayXoffset= getSmallerPixelValuesForHopping(xOffset, hopValue);
		pixelArrayYoffset= getSmallerPixelValuesForHopping(yOffset, hopValue);
		action.clickAndHold().perform();
		LOGGER.info("drag and drop...clickAndHold");
		for(int i= 0; i < pixelArrayXoffset.length; i++) {
			action.moveByOffset(pixelArrayXoffset[i], pixelArrayYoffset[i]).perform();
			waitForTimePeriod(100);
			LOGGER.info("drag and drop...moveByOffset");
		}
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");

	}


	public void clickAndHoldRelease(int xOffset, int yOffset) throws InterruptedException {
		Actions action = new Actions(driver);

		action.clickAndHold().perform();
		LOGGER.info("drag and drop...clickAndHold");

		action.moveByOffset(xOffset, yOffset).perform();
		waitForTimePeriod(100);
		LOGGER.info("drag and drop...moveByOffset");

		//		action.release().build().perform();
		action.release().perform();
		waitForTimePeriod(1000);
		action.moveByOffset(10, 10).perform();
		LOGGER.info("drag and drop...completed");

	}

	public void clickAndHoldReleaseQuick(int xOffset, int yOffset) throws InterruptedException {
		Actions action = new Actions(driver);

		action.clickAndHold().perform();
		LOGGER.info("drag and drop...clickAndHold");

		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");

		action.release().build().perform();
		LOGGER.info("drag and drop...completed");

	}

	public int[] getSmallerPixelValuesForHopping(int coOrdinateValue, int expectedPart){

		int [] coOrdinate;
		coOrdinate= new int[expectedPart];
		int remainder, quotient, dividend;
		if (coOrdinateValue != 0) {
			if(coOrdinateValue < 0) { dividend = coOrdinateValue * -1; }
			else { dividend= coOrdinateValue;}
			remainder = dividend%expectedPart;
			quotient = dividend/expectedPart;
			for (int i = 0; i < expectedPart; i++) {
				if (i==expectedPart-1) {
					coOrdinate[i] = quotient+remainder;

				}
				else {
					coOrdinate[i] = quotient;
				}
			}
			if(coOrdinateValue < 0) {
				for(int i= 0; i < coOrdinate.length; i++) {
					coOrdinate[i] = coOrdinate[i] * -1;
				}
			}
		}
		else {
			for(int i= 0; i < expectedPart; i++){
				coOrdinate[i]= 0;
			}
		}
		return coOrdinate;
	} 

	/**
	 * @param element
	 * @param xOffset
	 * @param yOffset
	 * @throws InterruptedException
	 */
	public void moveElement(WebElement element,int xOffset, int yOffset) throws InterruptedException {

		Actions action = new Actions(driver);
		action.dragAndDropBy(element, xOffset, yOffset).build().perform();
		action.moveByOffset(2, 2).perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Moved element at Xoffset:" + xOffset +"Yoffset: "+yOffset);
		waitForTimePeriod("Low");

	}

	/**
	 * @author anilt
	 * @param checkpoint - Test protocol checkpoint
	 * @param imageName - Name of the captured image
	 * @param protocolName - name of the protocol
	 * Description : This function compares images of the browser window with baseline image 
	 */
	public void verifyImage(String checkpoint, String imageName, String... protocolName) {
		boolean cpStatus= false;
		boolean screenshotStatus= false;
		String osVersion= "Windows7";
		String newImagePath;
		String differenceImage;
		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}
		newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName[0];
		differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");
		LOGGER.info("Image Path : "+ newImagePath);
		if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
			Utilities.createDirsIfNotExists(newImagePath+"/goldImages/");
			takeScreenShot(newImagePath+"/goldImages/"+imageName+".png");
		}
		else {
			File file = new File(newImagePath+"/goldImages/"+imageName+".png");
			if(!file.exists()) { 
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in verifyImage()", "No baseline image found for comparison.");
				Assert.assertTrue(false,"Exception in verifyImage() - No baseline image found for comparison.");
			}
			Utilities.createDirsIfNotExists(newImagePath+"/actualImages/");
			Utilities.createDirsIfNotExists(newImagePath+"/diffImages/");
			Utilities.createDirsIfNotExists(differenceImage);
			screenshotStatus = takeScreenShot(newImagePath+"/actualImages/"+imageName+".png");
			if(screenshotStatus){
				try {
					cpStatus=comparer.compareImage(newImagePath+"/goldImages/"+imageName+".png",
							newImagePath+"/actualImages/"+imageName+".png", 
							newImagePath+"/diffImages/"+imageName+".png");

					Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + imageName+".png");
					ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + imageName+".png");
				} catch (Exception e){
					ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
					Assert.assertTrue(false, "Exception in verifyImage()" + e.getStackTrace());
				} catch(AssertionError e) {
					try {
						Utilities.copyFile(new File(newImagePath+"/diffImages/"+imageName+".png"), new File(differenceImage+imageName+".png"));
					} catch (IOException io) {
						LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
						Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
					}
					ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + imageName+".png");
					Assert.assertTrue(false, "Exception in verifyImage() - Verification failed for image "+imageName+".png");
				}
			}else{
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to take screenshot for " + imageName+".png");
				Assert.assertTrue(false, "Unable to take screenshot for " + imageName+".png");
			}
		}		
	}

	/**
	 * @author anilt
	 * @param checkpoint - Test protocol checkpoint
	 * @param protocolName - name of the protocol
	 * Description : This function compares the images extracted from zip file.
	 */
	public void verifyZipImages(String checkpoint, String protocolName){
		String goldFileName="";
		String newImagePath="";
		boolean cpStatus= false;
		String extractedFolderPath = "";
		List<String> fileNamesList = null;
		String differenceImage="";
		String osVersion= "Windows7";
		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}			
		try {
			differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");
			extractedFolderPath = TEST_PROPERTIES.get("extractedFileFolderPath");
			fileNamesList = Arrays.asList(Utilities.getFileNamesFromDirectory(extractedFolderPath));

			String[] goldImageFileNames = null;
			int numFiles = fileNamesList.size();
			LOGGER.info("no. of files extracted: " + numFiles);

			newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;

			LOGGER.info("Image Path : "+ newImagePath);

			if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
				Utilities.createDirsIfNotExists(newImagePath+"/goldImages/");
				Utilities.copyFileToDir(extractedFolderPath, newImagePath+"/goldImages/");
			}
			else {
				goldImageFileNames = Utilities.getFileNamesFromDirectory(newImagePath+"/goldImages/");
				Utilities.createDirsIfNotExists(newImagePath+"/actualImages/");
				Utilities.copyFileToDir(extractedFolderPath, newImagePath+"/actualImages/");
				Utilities.createDirsIfNotExists(newImagePath+"/diffImages/");
				Utilities.createDirsIfNotExists(differenceImage);
				LOGGER.info("no. of files in goldImage: " + goldImageFileNames.length);

				for(int i=0;i<goldImageFileNames.length;i++) {
					goldFileName = goldImageFileNames[i];
					if(fileNamesList.contains(goldFileName)) {
						cpStatus=comparer.compareImage(newImagePath+"/goldImages/"+goldFileName,
								newImagePath+"/actualImages/"+goldFileName, 
								newImagePath+"/diffImages/"+goldFileName);
						Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + goldFileName);
						ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + goldFileName);

					}
				}
			}
		}catch(AssertionError e) {
			try {
				Utilities.copyFile(new File(newImagePath+"/diffImages/"+goldFileName), new File(differenceImage+goldFileName));
			} catch (IOException io) {
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
				Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
			}
			ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + goldFileName);
			Assert.assertTrue(false, "Exception in verifyZipImages() - Verification failed for image "+goldFileName);
		}
		catch (Exception e){		
			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
			Assert.assertTrue(false, "Exception in verifyZipImages()" + e.getStackTrace());
		}

	}

	/**
	 * @author anilt
	 * @param protocolName - name of the protocol
	 * @param locator - Locator of the element to capture image
	 * @param checkpoint - Test protocol checkpoint
	 * @param imageName - Name of the captured image
	 * @param coordinates - Co-ordinates on which image has to be cropped
	 * @param cropImage - Optional Parameter - Set true if user has to crop image
	 * Description : This function compares images of the provided element with baseline image 
	 * @throws InterruptedException 
	 */
	public void compareElementImage(String protocolName,By locator,String checkpoint, String imageName, int coordinates[], Boolean... cropImage) throws InterruptedException {
		boolean cpStatus= false;
		boolean screenshotStatus= false;
		String osVersion= "Windows7";
		String newImagePath;
		String differenceImage;
		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}
		newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");

		LOGGER.info("Image Path : "+ newImagePath);
		if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
			Utilities.createDirsIfNotExists(newImagePath+"/goldImages/");
			takeElementScreenShot(locator, newImagePath+"/goldImages/"+imageName+".png");

			if(cropImage.length > 0) {
				if(cropImage[0] == true) {
					LOGGER.info("Cropping Base Image : " + imageName+".png");
					waitForTimePeriod("Low");
					try {
						Utilities.cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], newImagePath+"/goldImages/"+imageName+".png", newImagePath+"/goldImages/"+imageName+".png");
					} catch(IOException e) {
						ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
						Assert.assertTrue(false, "Exception in compareElementImage() during image crop: " + e.toString());
					}	
				}
			}
		}
		else {
			File file = new File(newImagePath+"/goldImages/"+imageName+".png");
			if(!file.exists()) { 
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in compareElementImage()", "No baseline image found for comparison.");
				Assert.assertTrue(false,"Exception in compareElementImage() - No baseline image found for comparison.");
			}

			Utilities.createDirsIfNotExists(newImagePath+"/actualImages/");
			Utilities.createDirsIfNotExists(newImagePath+"/diffImages/");
			Utilities.createDirsIfNotExists(differenceImage);

			screenshotStatus = takeElementScreenShot(locator, newImagePath+"/actualImages/"+imageName+".png");

			if(cropImage.length > 0) {
				if(cropImage[0] == true) {
					LOGGER.info("Cropping Actual Image : " + imageName+".png");
					waitForTimePeriod("Low");
					try {
						Utilities.cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], newImagePath+"/actualImages/"+imageName+".png", newImagePath+"/actualImages/"+imageName+".png");
					} catch(IOException e) {
						ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
						Assert.assertTrue(false, "Exception in compareElementImage() during image crop: " + e.toString());
					}	
				}
			}

			if(screenshotStatus){
				try {
					cpStatus=comparer.compareImage(newImagePath+"/goldImages/"+imageName+".png",
							newImagePath+"/actualImages/"+imageName+".png", 
							newImagePath+"/diffImages/"+imageName+".png");

					Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + imageName+".png" );
					ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + imageName+".png");
				} catch (Exception e){
					ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
					Assert.assertTrue(false, "Exception in compareElementImage()" + e.getStackTrace());
				} catch(AssertionError e) {
					try {
						Utilities.copyFile(new File(newImagePath+"/diffImages/"+imageName+".png"), new File(differenceImage+imageName+".png"));
					} catch (IOException io) {
						LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
						Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
					}
					ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + imageName+".png");
					Assert.assertTrue(false, "Exception in compareElementImage() - Verification failed for image "+imageName+".png");
				}
			}else{
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to take screenshot for " + imageName+".png");
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , "Failed to take screenshot");
				Assert.assertTrue(false, "Unable to take screenshot for " + imageName+".png");
			}
		}           
	}

	public void compareElementImage(String protocolName,By locator,String checkpoint, String imageName) throws InterruptedException {

		boolean cpStatus= false;
		boolean screenshotStatus= false;
		String osVersion= "Windows7";

		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}
		String newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;		
		String differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");

		String baseImageLocation = newImagePath+"/goldImages/";
		String actualImageLocation = newImagePath+"/actualImages/";
		String diffImageLocation = newImagePath+"/diffImages/";



		LOGGER.info("Image Path : "+ newImagePath);
		if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
			Utilities.createDirsIfNotExists(baseImageLocation);
			takeElementScreenShot(locator, baseImageLocation+imageName+".png");

		}
		else {
			File file = new File(baseImageLocation+imageName+".png");
			if(!file.exists()) { 
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in compareElementImage()", "No baseline image found for comparison.");
				Assert.assertTrue(false,"Exception in compareElementImage() - No baseline image found for comparison.");
			}

			Utilities.createDirsIfNotExists(actualImageLocation);
			Utilities.createDirsIfNotExists(diffImageLocation);
			Utilities.createDirsIfNotExists(differenceImage);
			screenshotStatus = takeElementScreenShot(locator, actualImageLocation+imageName+".png");

			if(screenshotStatus){
				try {
					cpStatus=comparer.compareImage(baseImageLocation+imageName+".png",
							actualImageLocation+imageName+".png", 
							diffImageLocation+imageName+".png");

					Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + imageName+".png" );
					ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + imageName+".png");
				} catch (Exception e){
					ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
					Assert.assertTrue(false, "Exception in compareElementImage()" + e.getStackTrace());
				} catch(AssertionError e) {
					try {
						Utilities.copyFile(new File(diffImageLocation+imageName+".png"), new File(differenceImage+imageName+".png"));
					} catch (IOException io) {
						LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
						Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
					}
					ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + imageName+".png");
					Assert.assertTrue(false, "Exception in compareElementImage() - Verification failed for image "+imageName+".png");
				}
			}else{
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to take screenshot for " + imageName+".png");
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , "Failed to take screenshot");
				Assert.assertTrue(false, "Unable to take screenshot for " + imageName+".png");
			}
		}           
	}

	public void compareElementImage(String protocolName, WebElement element, String checkpoint, String imageName) {
		boolean cpStatus= false;
		boolean screenshotStatus= false;
		String osVersion= "Windows7";			
		imageName = imageName+".png";

		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}
		String newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;		
		String differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");

		String baseImageLocation = newImagePath+"/goldImages/";
		String actualImageLocation = newImagePath+"/actualImages/";
		String diffImageLocation = newImagePath+"/diffImages/";

		LOGGER.info("Image Path : "+ newImagePath);
		if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
			Utilities.createDirsIfNotExists(baseImageLocation);
			takeElementScreenShot(element, baseImageLocation+imageName);
		}
		else {
			File file = new File(baseImageLocation+imageName);
			if(!file.exists()) { 
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in compareElementImage()", "No baseline image found for comparison.");
				Assert.assertTrue(false,"Exception in compareElementImage() - No baseline image found for comparison.");
			}

			Utilities.createDirsIfNotExists(actualImageLocation);
			Utilities.createDirsIfNotExists(diffImageLocation);
			Utilities.createDirsIfNotExists(differenceImage);

			screenshotStatus = takeElementScreenShot(element, actualImageLocation+imageName);
			if(screenshotStatus){
				try {
					cpStatus=comparer.compareImage(baseImageLocation+imageName,
							actualImageLocation+imageName, 
							diffImageLocation+imageName);

					Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + imageName+".png" );
					ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + imageName+".png");
				} catch (Exception e){
					ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
					Assert.assertTrue(false, "Exception in compareElementImage()" + e.getMessage());
				} catch(AssertionError e) {
					try {
						Utilities.copyFile(new File(diffImageLocation+imageName), new File(differenceImage+imageName));
					} catch (IOException io) {
						LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
						Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
					}
					ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + imageName+".png");
					Assert.assertTrue(false, "Exception in compareElementImage() - Verification failed for image "+imageName);
				}
			}else{
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to take screenshot for " + imageName);
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , "Failed to take screenshot");
				Assert.assertTrue(false, "Unable to take screenshot for " + imageName);
			}
		}           

	}

	/**
	 * @author abhishekr
	 * @param xOffset
	 * @param yOffset
	 * @throws TimeoutException
	 * @throws WaitException
	 * @throws InterruptedException
	 */
	public void clickAt(int xOffset,int yOffset) throws TimeoutException, InterruptedException {
		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
		Actions action = new Actions(driver);
		action.moveByOffset(xOffset, yOffset).build().perform();
		//		waitForTimePeriod("Low");

		if(browserName.equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) {
			action.clickAndHold().build().perform();
			waitForTimePeriod("Low");
			action.release().build().perform();
			waitForTimePeriod("Low");
		}
		else {
			action.click().build().perform();
		}
	}

	/**
	 * @author anilt
	 */
	public void doubleClick() {
		Actions action = new Actions(driver);
		action.doubleClick().build().perform();
	}

	public void doubleClick(WebElement element) {
		Actions action = new Actions(driver);
		action.doubleClick(element).build().perform();
	}

	/**
	 * @author payal
	 * @param Webelement
	 * @return: null
	 * Description: This method mouse hover on webelemnet and perform double click
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 * 	 */
	public void DoubleClickWithMouseHover(WebElement element) throws TimeoutException, InterruptedException{
		Actions action = new Actions(driver);
		mouseHover(element);
		action.doubleClick().build().perform();
	}

	/**
	 * @author anilt
	 * @param testCaseName -name of the rename files followed by _ and numbers e.g. T1_TC1_CTDataAnnotation_1 
	 * Description: Function to rename files which are extracted from zip. 
	 */
	public void renameFiles(String testCaseName) {
		try{
			Utilities.renameFile(TEST_PROPERTIES.get("extractedFileFolderPath"), testCaseName);
		}catch(Exception e){
			LOGGER.error(Utilities.getCurrentThreadId()+"Exception in renameFiles()"+e.getStackTrace(),e);
			ExtentManager.customExtentReportLog(FAIL, extentTest,"Exception in renameFiles()", e.toString());
			Assert.assertTrue(false,"Exception in renameFiles() "+e.toString());
		}
	}

	/**
	 * @author anilt
	 * Description: This function returns the baseurl
	 */
	public String getBaseURL() {
		if ((TEST_PROPERTIES.get(SKIPLOGIN)).toUpperCase().equals("YES")) {
			return APPLICATIONURLUSINGUSER;
		} else {
			return APPLICATIONURL;	
		}
	}

	/**
	 * @author anilt
	 * @param element
	 * @return
	 * Description: Function to verify if given element is enabled
	 */
	public boolean verifyButtonEnabled(WebElement element) {
		boolean status= false;
		if(element.isEnabled()){
			status = true;
		}else{
			status = false;
		}
		return status;
	}

	/**
	 * @author chiranjivb
	 * @param locator - locator of element whose presence is to be checked
	 * @return true if element is present
	 * Description: This function checks whether the given element is present in dom without waiting for any specific time for the element to load
	 */
	public boolean checkForElementPresenceWithoutWaitTime(By locator) {
		try {
			driver.findElement(locator);
			LOGGER.info(Utilities.getCurrentThreadId()+ "WebElement Present. Proceeding further...");
			return true;
		} catch (Exception e) {
			LOGGER.info("Element not found by locator "+locator);
			return false;
		}
	}
	/**
	 * @author chiranjivb
	 * @param protocolName - name of the protocol
	 * @param locator - Locator of the element to capture image
	 * @param checkpoint - Test protocol checkpoint
	 * @param imageName - Name of the captured image
	 * @param coordinates - coordinates to crop image
	 * Description : This function compares images of the provided element with baseline image after cropping the images by specified coordinates
	 * @throws InterruptedException 
	 */
	public void compareElementCroppedImage(String protocolName,By locator,String checkpoint, String imageName, int coordinates[]) throws InterruptedException {
		boolean cpStatus= false;
		boolean screenshotStatus= false;
		String osVersion= "Windows7";
		String newImagePath;
		String differenceImage;
		if (!System.getProperty("os.name").equalsIgnoreCase("Windows 7")) {
			osVersion= "Windows10";
		}
		newImagePath= TEST_PROPERTIES.get("imagesPath") +  osVersion + "/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		differenceImage= TEST_PROPERTIES.get("imageComparisondiffImages");

		LOGGER.info("Image Path : "+ newImagePath);
		if(TEST_PROPERTIES.get("run").equalsIgnoreCase("BASE")) {
			Utilities.createDirsIfNotExists(newImagePath+"/goldImages/");
			takeElementScreenShot(locator, newImagePath+"/goldImages/"+imageName+".png");
			waitForTimePeriod("Low");
			try {
				Utilities.cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], newImagePath+"/goldImages/"+imageName+".png", newImagePath+"/goldImages/"+imageName+".png");
			} catch(IOException e) {
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
				Assert.assertTrue(false, "Exception in compareElementCroppedImage() during image crop: " + e.toString());
			}		
		}
		else {
			File file = new File(newImagePath+"/goldImages/"+imageName+".png");
			if(!file.exists()) { 
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception in compareElementImage()", "No baseline image found for comparison.");
				Assert.assertTrue(false,"Exception in compareElementCroppedImage() - No baseline image found for comparison.");
			}

			Utilities.createDirsIfNotExists(newImagePath+"/actualImages/");
			Utilities.createDirsIfNotExists(newImagePath+"/diffImages/");
			Utilities.createDirsIfNotExists(differenceImage);
			screenshotStatus = takeElementScreenShot(locator, newImagePath+"/actualImages/"+imageName+".png");
			waitForTimePeriod("Low");
			try {
				Utilities.cropImage(coordinates[0], coordinates[1], coordinates[2], coordinates[3], newImagePath+"/actualImages/"+imageName+".png", newImagePath+"/actualImages/"+imageName+".png");
			} catch(IOException e) {
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
				Assert.assertTrue(false, "Exception in compareElementCroppedImage() during image crop: " + e.toString());
			}
			if(screenshotStatus){
				try {
					cpStatus=comparer.compareImage(newImagePath+"/goldImages/"+imageName+".png",
							newImagePath+"/actualImages/"+imageName+".png", 
							newImagePath+"/diffImages/"+imageName+".png");

					Assert.assertTrue(cpStatus, "The actual and Expected image are different. Image name: " + imageName+".png" );
					ExtentManager.customExtentReportLog(PASS, extentTest, checkpoint, "Successfully verified checkpoint with image comparison.<br>Image name is " + imageName+".png");
				} catch (Exception e){
					ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
					Assert.assertTrue(false, "Exception in compareElementCroppedImage()" + e.getStackTrace());
				} catch(AssertionError e) {
					try {
						Utilities.copyFile(new File(newImagePath+"/diffImages/"+imageName+".png"), new File(differenceImage+imageName+".png"));
					} catch (IOException io) {
						LOGGER.error(Utilities.getCurrentThreadId()+"Unable to copy [differenceImage]file to root diff folder");
						Assert.assertTrue(false, "Unable to copy [differenceImage]file to root diff folder");	
					}
					ExtentManager.customExtentReportLogWithScreenshot(FAIL, extentTest, checkpoint,"Verification failed. Click below to view image.<br>@" + differenceImage + imageName+".png");
					Assert.assertTrue(false, "Exception in compareElementCroppedImage() - Verification failed for image "+imageName+".png");
				}
			}else{
				LOGGER.error(Utilities.getCurrentThreadId()+"Unable to take screenshot for " + imageName+".png");
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , "Failed to take screenshot");
				Assert.assertTrue(false, "Unable to take screenshot for " + imageName+".png");
			}
		}           
	}

	/**
	 * @author abhishekr
	 * @param coOrdinateValue
	 * @param expectedPart
	 * Description : Functions used to convert pixels into small intervals
	 * @return
	 */
	public int[] getPartForPixel(int coOrdinateValue, int expectedPart) {
		int [] coOrdinate;
		coOrdinate= new int[expectedPart];
		if(coOrdinateValue != 0) { 
			int dividend, divisor, reminder, quotient, expectedPartCount;

			//Convert all values to +ve if coordinateValue is less than 0
			if(coOrdinateValue < 0) { dividend = coOrdinateValue * -1; }
			else                    { dividend= coOrdinateValue;       };

			expectedPartCount= expectedPart;
			divisor = expectedPartCount - 4;

			if(dividend > 6) {
				int coOrdinateIndex; coOrdinateIndex= 2; 
				coOrdinate[0]= 3; dividend= dividend - 3;
				coOrdinate[1]= 3; dividend= dividend - 3;
				coOrdinate[expectedPart - 2]= 3; dividend= dividend - 3;
				coOrdinate[expectedPart - 1]= 3; dividend= dividend - 3;

				reminder = dividend % divisor;

				if(dividend < divisor) {
					quotient = dividend - (dividend/divisor)*divisor;
				}
				else {
					quotient= dividend/divisor;
				}

				for(int i= 1; i < divisor; i++){
					dividend= dividend - quotient;
					coOrdinate[coOrdinateIndex]= quotient;
					coOrdinateIndex= coOrdinateIndex + 1;
				}
				if(reminder == 0) {
					coOrdinate[coOrdinateIndex]= quotient;
				}
				else { coOrdinate[coOrdinateIndex]= quotient + reminder; }
			} 
			else{
				coOrdinate[0]= 3;
				coOrdinate[1]= dividend - 3; 
				for(int i= 2; i < expectedPart; i++){
					coOrdinate[i]= 0;
				}
			}

			//Convert all values to -ve if coOrdinateValue is less than 0
			if(coOrdinateValue < 0) {
				for(int i= 0; i < coOrdinate.length; i++) {
					coOrdinate[i] = coOrdinate[i] * -1; 
				}
			}
		}
		else {
			for(int i= 0; i < expectedPart; i++){
				coOrdinate[i]= 0;
			}
		}
		LOGGER.info("Input CoOrdinates [" +coOrdinateValue+ "] => returned array [" + Arrays.toString(coOrdinate) +"]");
		return coOrdinate;
	}

	/**
	 * @author chiranjivb
	 * @param key - key which is to be pressed
	 * Description : Function used press keyboard keys
	 */
	public void pressNumpadKeys(String key) {
		Actions action = new Actions(driver);
		if(key.equalsIgnoreCase("Decimal")) 
			action.sendKeys(Keys.DECIMAL).perform();
		if(key.equalsIgnoreCase("Numpad0")) 
			action.sendKeys(Keys.NUMPAD0).perform();	
	}

	public void assertEquals(Set<String> set, Set<String> set2,String action, String message){

		Assert.assertEquals(set,set2,"Expected value="+set2+" but Found value="+set);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
	}

	public void assertEquals(List list, List list2,String action, String message){

		Assert.assertEquals(list,list2,"Expected value="+list2+" but Found value="+list);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
	}

	public void assertEquals(HashMap<String, String> map, HashMap<String, String> map2,
			String action, String message) {
		Assert.assertEquals(map,map2,"Expected value="+map2+" but Found value="+map);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}


	//nikita: Description: to verify value on both UI and database after update in DB


	public void assertEquals(boolean actual, boolean expected,String action, String message){

		Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}
	public void assertEquals(String actual, String expected,String action, String message){

		Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}
	public void verifyEquals(String actual, String expected,String action, String message){

		try{
			Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
			ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
		}catch(AssertionError error)
		{
			ExtentManager.customExtentReportLog(FAIL, extentTest, action, "ui verification failed");
		}


	}

	public void verifyTrue(boolean condition,String action, String message){

		try{
			Assert.assertTrue(condition,"Condition is:="+condition);
			ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
		}catch(AssertionError error)
		{
			ExtentManager.customExtentReportLog(FAIL, extentTest, action, "verification failed");
		}



	}
	public void verifyFalse(boolean condition,String action, String message){

		try{
			Assert.assertFalse(condition,"Condition is:="+condition);
			ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
		}catch(AssertionError error)
		{
			ExtentManager.customExtentReportLog(FAIL, extentTest, action, "verification failed");
		}



	}

	public void verifyEquals(int actual, int expected,String action, String message){

		try{
			Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
			ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
		}catch(AssertionError error)
		{
			ExtentManager.customExtentReportLog(FAIL, extentTest, action, "ui verification failed");
		}



	}

	public void verifyEquals(Boolean actual, Boolean expected,String action, String message){

		try{
			Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
			ExtentManager.customExtentReportLog(PASS, extentTest, action, message);
		}catch(AssertionError error)
		{
			ExtentManager.customExtentReportLog(FAIL, extentTest, action, "ui verification failed");
		}


	}

	public void assertEquals(int actual, int expected,String action, String message){

		Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertEquals(double actual, double expected,String action, String message){

		Assert.assertEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertNotEquals(double actual, double expected,String action, String message){

		Assert.assertNotEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}


	public void assertNotEquals(String actual, String expected,String action, String message){

		Assert.assertNotEquals(actual,expected ,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertNotEquals(int actual, int expected,String action, String message){

		Assert.assertNotEquals(actual,expected,"Expected value="+expected+" but Found value="+actual);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertTrue(boolean condition,String action, String message){

		Assert.assertTrue(condition,"Condition is:="+condition);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertNull(Object object,String action, String message){

		Assert.assertNull(object,"Object is not null");
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertNotNull(Object object,String action, String message){

		Assert.assertNotNull(object,"Object is not null");
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	public void assertFalse(boolean condition,String action, String message){
		Assert.assertFalse(condition,"Condition is:="+condition);
		ExtentManager.customExtentReportLog(PASS, extentTest, action, message);

	}

	/**
	 *  Description : Method used to open new browser window
	 */

	public void openNewWindow(){

		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
		js = (JavascriptExecutor) driver; 
		if(browserName.equalsIgnoreCase(BrowserType.CHROME.getBrowserValue())) {

			js.executeScript("window.open();");

		} else		
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");



	}

	public void openNewWindow(String newURL) throws InterruptedException{

		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
		js = (JavascriptExecutor) driver; 
		if(browserName.equalsIgnoreCase(BrowserType.CHROME.getBrowserValue())||browserName.equalsIgnoreCase(BrowserType.EDGE.getBrowserValue())){
			js.executeScript("window.open(\""+newURL+"\").focus();");
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			waitForTimePeriod("Low");
		}

		else  {      
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
			waitForTimePeriod("Low");
			driver.get(newURL);
		}

	}

	/**
	 * Description : Method to get the current window id
	 * @return 
	 */

	public String getCurrentWindowID(){

		return driver.getWindowHandle();
	}

	public Set<String> getAllOpenedWindowsID(){


		return driver.getWindowHandles();
	}

	public List<String> getAllOpenedWindowsIDs(){

		Set<String> allWindowHandles = driver.getWindowHandles();

		return new ArrayList<String>(Arrays.asList(allWindowHandles.toArray(new String[allWindowHandles.size()])));

	}

	/**
	 * 
	 * @return : return the monitor's resolution and its width as rectangle
	 */

	public ArrayList<Rectangle> getDetailsOfAttachedMonitors(){

		ArrayList<Rectangle> monitorDetails = new ArrayList<Rectangle>();
		final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment ().getDefaultScreenDevice ();
		final GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment ().getScreenDevices ();
		for ( final GraphicsDevice d : devices )
		{

			if ( d == device )

				monitorDetails.add(0,d.getDefaultConfiguration ().getBounds ());
			else
				monitorDetails.add(d.getDefaultConfiguration ().getBounds () );

		}

		return monitorDetails;
	}

	public void setWindowSize(String windowID, Dimension dimension){
		driver.switchTo().window(windowID);
		driver.manage().window().setSize(dimension);
	}

	public void setWindowPosition(String windowID, Point location){


		//		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.switchTo().window(windowID);

		if(TEST_PROPERTIES.get("Browser").equalsIgnoreCase("chrome"))
			driver.manage().window().setPosition(location);
		else if(TEST_PROPERTIES.get("Browser").equalsIgnoreCase("firefox"))
		{
			js.executeScript("window.moveTo("+location.getX()+",0);");
		}

		//		driver.manage().window().setPosition(location);
	}

	/**
	 * @param windowID
	 * @throws WaitException
	 * @throws InterruptedException
	 */
	public void switchToWindow(String windowID) throws  InterruptedException {

		waitForTimePeriod("Low");
		Set<String> windows = driver.getWindowHandles();
		for (String strWindows : windows) {
			if (strWindows.equalsIgnoreCase(windowID)){
				driver.switchTo().window(windowID);
				waitForTimePeriod("Low");
				break;
			}
		}
	}

	public Point getWindowPosition(String windowID){

		driver.switchTo().window(windowID);

		return driver.manage().window().getPosition();

	}

	public void maximizeWindow(){

		driver.manage().window().maximize();
	}

	public Dimension getWindowSize(String windowID){

		driver.switchTo().window(windowID);

		return driver.manage().window().getSize();

	}

	public void closeWindow(String windowID){

		JavascriptExecutor js = (JavascriptExecutor) driver;

		driver.switchTo().window(windowID);
		if(TEST_PROPERTIES.get("Browser").equalsIgnoreCase("chrome"))
			js.executeScript("window.close();");
		else if(TEST_PROPERTIES.get("Browser").equalsIgnoreCase("firefox"))
			driver.close();



	}

	/*
	 * Wait Methods
	 * 
	 */

	public WebElement waitForElementVisibility(WebDriver driver, By locator)
			throws TimeoutException {
		try {
			WebElement element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the visibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Present and Visible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the visibility of the element using By class:"
					+ locator + "\n", tm);
		}
	}

	public WebElement waitForElementVisibility(By locator)
			throws TimeoutException {
		try {
			WebElement element = null;
			LOGGER.info(Utilities.getCurrentThreadId()+ "Waiting for the visibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Present and Visible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the visibility of the element using By class:"
					+ locator + "\n", tm);
		}
	}



	public Boolean waitForElementInVisibility( By locator)
			throws TimeoutException {
		try {
			Boolean element = true;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the invisibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));

			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Invisible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the invisibility of the element using By class:"
					+ locator + "\n", tm);
		}
	}

	public Boolean waitForElementInVisibility(WebElement locator)
			throws TimeoutException {
		try {
			Boolean element = true;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the invisibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.invisibilityOf(locator));

			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Invisible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the invisibility of the element using By class:"
					+ locator + "\n", tm);
		}
	}


	public Boolean waitForElementInVisibility(WebElement locator, long timeInSec)
			throws TimeoutException {
		try {
			Boolean element = true;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the invisibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,timeInSec);

			element = wait.until(ExpectedConditions.invisibilityOf(locator));

			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Invisible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the invisibility of the element using By class:"
					+ locator + "\n", tm);
		}
	}

	private WebElement waitForElementPresence(By locator)
			throws TimeoutException {
		try {
			WebElement element = null;            
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the presence of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));            
			element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Present. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the presence of the element using By class:"
					+ locator + "\n", tm);
		}
	}

	public WebElement waitForElementVisibility(WebElement beforeVisibilityElement)
			throws TimeoutException {
		try {
			WebElement afterVisibilityElement = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the visibility of the web element:" + beforeVisibilityElement);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			afterVisibilityElement = wait.until(ExpectedConditions
					.visibilityOf(beforeVisibilityElement));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Visible. Proceeding further...");
			return afterVisibilityElement;
		} catch (TimeoutException tm) {
			throw new TimeoutException(Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the visibility of the web element:"
					+ beforeVisibilityElement + "\n", tm);
		}
	}

	private WebElement waitForElementClickability(By locator)
			throws TimeoutException{
		try {
			WebElement element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the clickability of the element:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Clickable. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the clickability of the element:"
					+ locator + "\n", tm);
		}
	}

	public WebElement waitForElementClickability(WebElement locator)
			throws TimeoutException{
		try {
			WebElement element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the clickability of the element:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Clickable. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the clickability of the element:"
					+ locator + "\n", tm);
		}
	}

	public List<WebElement> waitForElementsVisibility(By locator)
			throws TimeoutException{
		try {
			List<WebElement> element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the visibility of the elements:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElements Visible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the visibility of the elements:"
					+ locator + "\n", tm);
		}
	}

	private List<WebElement> waitForElementsPresence(By locator)
			throws TimeoutException{
		try {
			List<WebElement> element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the presence of the elements:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElements Present. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the presence of the elements:"
					+ locator + "\n", tm);
		}
	}

	public void waitForTimePeriod(int timeOut) throws InterruptedException {
		try {
			LOGGER.info(Utilities.getCurrentThreadId() + "Thread.sleep activated for " + timeOut/ 1000 + " seconds");
			Thread.sleep(timeOut);
			LOGGER.info(Utilities.getCurrentThreadId() + "Ended after waiting for " + timeOut
					/ 1000 + " seconds");
		} catch (InterruptedException ie) {
			throw new InterruptedException(
					"Thread Interrupted Exception in the waitForTimePeriod() method of WebDriverWaits class");
		}
	}

	public void waitForTimePeriod(String time) throws InterruptedException {
		int timeOut=0;
		if(time.equalsIgnoreCase("Low")){
			timeOut=Integer.parseInt(Configurations.TEST_PROPERTIES.get("waitForElementLowTime"));
			waitForTimePeriod(timeOut);
		}else if (time.equalsIgnoreCase("Medium")) {
			timeOut=Integer.parseInt(Configurations.TEST_PROPERTIES.get("waitForElementMediumTime"));
			waitForTimePeriod(timeOut);
		}else if (time.equalsIgnoreCase("High")) {
			timeOut=Integer.parseInt(Configurations.TEST_PROPERTIES.get("waitForElementHighTime"));
			waitForTimePeriod(timeOut);
		}else {
			timeOut=Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTWAITTIME));
			waitForTimePeriod(timeOut);
		}						
	}

	public Boolean checkForElementVisibility( final By locator)
	{
		try {
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Checking for the visibility of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return driver.findElement(locator).isDisplayed();
				}
			});
			LOGGER.info(Utilities.getCurrentThreadId() + "Element visible.");
			return true;
		} catch (TimeoutException tm) {
			LOGGER.error(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the visibility of the element using By class:"
					+ locator + "\n", tm);
			return false;
		}
	}

	public Boolean checkForElementPresence( By locator)
			throws TimeoutException {
		try {
			WebElement element = null;            
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the presence of the element using By class:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));            
			element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Present. Proceeding further...");
			return true;
		} catch (NoSuchElementException | TimeoutException tm) {
			return false;
		}
	}

	public Boolean checkForTextPresenceInElement(WebElement locator, String text) throws TimeoutException {
		try {
			Boolean element = true;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the presence of Text in Element value:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.textToBePresentInElement(locator, text));

			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Visible. Proceeding further...");
			return element;
		} catch (TimeoutException tm) {
			throw new TimeoutException(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for the presence of Text in Element:"
					+ locator + "\n", tm);
		}
	}

	public Boolean checkForElementClickability( By locator)
			throws TimeoutException {
		try {
			WebElement element = null;
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Waiting for the clickability of the element:" + locator);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "WebElement Clickable. Proceeding further...");
			return true;
		} catch (TimeoutException tm) {
			return false;
		}
	}

	public WebElement syncElementUsing(String syncKey,  By locator)
			throws TimeoutException {
		if ("visibility".equals(syncKey)) {
			return waitForElementVisibility(locator);
		} else if ("clickability".equals(syncKey)) {
			return waitForElementClickability(locator);

		} else if ("presence".equals(syncKey)) {
			return waitForElementPresence(locator);
		} else {
			return null;
		}
	}

	public WebElement syncElementUsing(String syncKey, WebElement locator)
			throws TimeoutException {
		if ("visibility".equals(syncKey)) {
			return waitForElementVisibility( locator);
		} else if ("clickability".equals(syncKey)) {
			return waitForElementClickability(locator);

			//		} else if ("presence".equals(syncKey)) {
			//			return waitForElementPresence(locator);
		} else {
			return null;
		}
	}

	public List<WebElement> syncElementsUsing(String syncKey,  By locator)
			throws TimeoutException{
		if ("visibility".equals(syncKey)) {
			return waitForElementsVisibility(locator);
		} else if ("presence".equals(syncKey)) {
			return waitForElementsPresence(locator);
		} else {
			return Collections.emptyList();
		}
	}


	/**
	 * Method wait for all Ajax Calls end or for GlobalTimeOut seconds
	 * TODO : waitForEndOfAllAjaxes method is incomplete
	 */
	public void waitForEndOfAllAjaxes(){
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the page to load...");
		WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				//				return (Boolean)((JavascriptExecutor)driver).executeScript("return jQuery.active == 0");
				return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
				//				 return Boolean.valueOf(((JavascriptExecutor) driver).executeScript("return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)").toString());

			}
		});

	}

	public void click(WebElement element)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(element));
		ele.click();

	}

	public void click(int xOffset, int yOffset)
	{

		Actions action = new Actions(driver);
		action.moveByOffset(xOffset, yOffset).click().build().perform();

	}


	public void click(String element)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(element)));
		ele.click();
	}

	public void clickUsingJavaScript(WebElement element){

		JavascriptExecutor executor = (JavascriptExecutor)driver;

		executor.executeScript("arguments[0].click();", element);


	}

	public String getCurrentPageURL(){

		return driver.getCurrentUrl();
	}

	public void refreshWebPage() throws InterruptedException{

		driver.navigate().refresh();
		waitForTimePeriod(3000);
	}

	public void browserBackWebPage(){

		driver.navigate().back();
	}

	public void browserForwardWebPage(){

		driver.navigate().forward();
	}

	public String getHexColorValue(String rgb_color)
	{
		int hexValue1=0,hexValue2=0,hexValue3=0;
		if(rgb_color.startsWith("rgba"))
		{
			String[] hexValue = rgb_color.replace("rgba(", "").replace(")", "").split(",");
			hexValue1=Integer.parseInt(hexValue[0]);
			hexValue[1] = hexValue[1].trim();
			hexValue2=Integer.parseInt(hexValue[1]);
			hexValue[2] = hexValue[2].trim();
			hexValue3=Integer.parseInt(hexValue[2]);
		}
		else if (rgb_color.startsWith("rgb"))
		{
			String[] hexValue = rgb_color.replace("rgb(", "").replace(")", "").split(",");
			hexValue1=Integer.parseInt(hexValue[0]);
			hexValue[1] = hexValue[1].trim();
			hexValue2=Integer.parseInt(hexValue[1]);
			hexValue[2] = hexValue[2].trim();
			hexValue3=Integer.parseInt(hexValue[2]);
		}

		return String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
	}

	public String formatDecimalNumber(String Size)
	{
		DecimalFormat df = new DecimalFormat("#,###,##");
		String	newSize = Size.replaceAll("[^\\d.]", "");
		double data = Double.parseDouble(newSize); 
		String number = df.format(data);
		return number+"px" ;
	}

	public void doubleClickJs(WebElement element) 
	{
		String jScript = "if(document.createEvent)"
				+ "{var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initEvent('dblclick',+true, false);arguments[0].dispatchEvent(evObj);"
				+ "}"
				+"else if(document.createEventObject)"
				+ "{ arguments[0].fireEvent('ondblclick');}";
		js = (JavascriptExecutor)driver;
		js.executeScript(jScript, element );
	}

	public HashMap<String, Integer> getImageCoordinate(WebElement element)
	{

		HashMap<String, Integer> imageCoordinates = new HashMap<String, Integer>();
		imageCoordinates.put("X", element.getLocation().getX());
		imageCoordinates.put("Y", element.getLocation().getY());
		imageCoordinates.put("Height", element.getSize().getHeight());
		imageCoordinates.put("Width", element.getSize().getWidth());
		return imageCoordinates ;

	}     

	// To zoom out page 
	public void zoomOut()  { 
		js.executeScript("document.body.style.zoom='50%'");

	}

	public void performMouseRightClickOnGSPS(WebElement ele) throws InterruptedException{

		Actions a = new Actions(driver);
		a.moveToElement(ele).perform();
		a.contextClick().build().perform();
		waitForTimePeriod(1000);
	}

	public void performMouseRightClick(WebElement ele){

		Actions a = new Actions(driver);
		a.moveToElement(ele).contextClick().pause(100).build().perform();


	}


	public void performMouseRightClick(WebElement viewbox ,int xOffset, int yOffset){

		Actions a = new Actions(driver);
		a.moveToElement(viewbox).moveByOffset(xOffset, yOffset).contextClick().build().perform();

	}

	public boolean isElementPresent(By ele){
		boolean status = false;
		try{
			driver.findElement(ele);
			status= true;

		}catch(NoSuchElementException e){
			status= false;
		}

		return status;
	}

	// To zoom in or outs  page 
	public void zoomInOrOut(int zoomPercent){ 
		js.executeScript("document.body.style.zoom='"+zoomPercent+"%'");
	}

	public String getFontsizeOfWebElement(WebElement element){
		return element.getCssValue(NSGenericConstants.FONT_SIZE_PROP);
	}

	public String getColorOfWebElemnt(WebElement element){
		return getHexColorValue(element.getCssValue(NSGenericConstants.CSS_PROP_COLOR));
	}

	public String getBackgroundColorOfRows(WebElement element){
		return getHexColorValue(element.getCssValue(NSGenericConstants.BACKGROUND_COLOR));
	}

	public String getBackgroundColor(WebElement element){
		return element.getCssValue(NSGenericConstants.BACKGROUND_COLOR);
	}

	public String getColorOfRows(WebElement element){
		return element.getCssValue(NSGenericConstants.CSS_PROP_COLOR);
	}

	public String getBorderColorOfWebElemnt(WebElement element){
		return element.getCssValue(NSGenericConstants.CSS_PROP_BORDER_COLOR);
	}

	/* @author vaishali
	 * @param webelement
	 * @return: null
	 * Description:Return the color of selected checkmark
	 */
	public String getColorOfSelectedCheckbox(WebElement element){
		return element.getCssValue("stroke");
	}

	public boolean verifyTextContainsString(String expectedValue, String actualValue){
		if(expectedValue.contains(actualValue)){
			LOGGER.info("Verified Pass : Expected value is present");
			return true;
		}
		LOGGER.info("Verified Fail : Expected value is present");
		return false;
	}

	public void navigateToBack() {
		driver.navigate().back();
	}

	public void navigateToForward() {
		driver.navigate().forward();
	}

	/**
	 * @author payala2
	 * @param Number1 and Number2
	 * @return: null
	 * Description: Moving mouse to specific co-ordinates
	 * @throws AWTException 
	 */
	public void mouseMove(int Num1, int Num2) throws AWTException  {
		Robot robo = new Robot();
		robo.mouseMove(Num1, Num2);
	}

	public void leftClick() throws AWTException  {
		Robot robot = new Robot();
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // press left click	
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // release left click	

	}


	public void mouseMovement(int Num1, int Num2)  {
		Actions action = new Actions(driver);
		action.moveByOffset(Num1, Num2).perform();
	}
	public String getFontFamilyOfWebElemnt(WebElement element){
		return element.getCssValue("font-family").replaceAll("^\"|\"$", "");
	}

	public boolean isElementPresent(WebElement ele) {

		boolean status = false;
		try{
			if(ele.isDisplayed())
				status= true;

		}catch(NoSuchElementException | NullPointerException e  ){
			status= false;
		}

		return status;
	}
	public boolean verifyDateFormat(String dateFormat, String date) {

		Date javaDate = null;
		try{
			javaDate = new SimpleDateFormat(dateFormat).parse(date); 
			LOGGER.info("Verified PASS : " + javaDate  + "  " + " is valid date ");
			return true;
		}
		catch (Exception e){
			ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
			LOGGER.info("Verified FAIL : " + javaDate  + "  " + " is invalid date ");
			return false;
		}
	}

	public boolean verifyDateFormat(DateFormat dateFormat, String dateToBeValidate){
		boolean checkformat = false;
		Date date = new Date();
		if(!dateToBeValidate.isEmpty()) {
			try {
				date = dateFormat.parse(dateToBeValidate.trim());
				if (dateFormat.format(date).equals(dateToBeValidate.trim())) {
					LOGGER.info("Verified PASS : " + date  + "  " + " is valid date ");
					checkformat = true;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				printStackTrace(e.getMessage());
				ExtentManager.customExtentReportLog(FAIL, extentTest, "Exception occured " , StringUtils.join(e.getStackTrace(), "<br>"));
				LOGGER.info("Verified FAIL : " + date  + "  " + " is invalid date ");
			}			
		}
		return checkformat;
	}

	public boolean isChecked(WebElement locator) {
		try {

			LOGGER.info(Utilities.getCurrentThreadId()+ "WebElement Present. Proceeding further...");
			return locator.isSelected();
		} catch (Exception e) {
			LOGGER.info("Element not found by locator "+locator);
			return false;
		}
	}

	public boolean isElementPresent(WebElement element, String id){
		try{
			element.findElement(By.id(id));
		}catch(NoSuchElementException e){
			return false;
		}
		return true;
	}


	/**
	 * @author ssravanth
	 * @param Integer value of number of windows
	 * @return: boolean
	 * Description: Introduces wait time until total windows equals input parameter
	 */

	public Boolean waitForNumberOfWindowsToEqual(int numberOfWindows)
	{
		try {
			LOGGER.info(Utilities.getCurrentThreadId()
					+ "Checking for number of windows to equal:" + numberOfWindows);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return (driver.getWindowHandles().size() == numberOfWindows);
				}
			});
			LOGGER.info(Utilities.getCurrentThreadId() + "Number of windows equals:"+numberOfWindows);
			return true;
		} catch (TimeoutException tm) {
			LOGGER.error(
					Utilities.getCurrentThreadId()
					+ "Time Out Exception while waiting for number of windows to be equal to:"
					+ numberOfWindows + "\n", tm);
			return false;
		}
	}

	/**
	 * @author payala2
	 * @param Exception message
	 * @return: warning while exception is catch
	 * Description: This method return exception warning in report
	 */
	public void printStackTrace(String e){
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Exception - observed", e);
		LOGGER.error(e);
	}

	protected void pressF5Key(){

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		executor.executeScript("location.reload()");

		waitForEndOfAllAjaxes();

	}

	protected void pressF11Key(){

		String f11Script ="var el = document.documentElement, rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;"+    
				"if(typeof rfs!=\"undefined\" && rfs){  rfs.call(el);} else if(typeof window.ActiveXObject!=\"undefined\"){"+
				" var wscript = new ActiveXObject(\"WScript.Shell\");"+
				"if (wscript!=null) {"+
				"wscript.SendKeys(\"{F11}\");}}";

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		executor.executeScript(f11Script);


	}

	public boolean isCSSValuePresent(WebElement element, String cssValue) {
		Boolean result = false;
		try {
			String value = element.getCssValue(cssValue);
			if (value != null){
				result = true;
			}
		} catch (Exception e) {
			printStackTrace(e.getMessage());
		}

		return result;
	}

	/**
	 * @author Vivek
	 * @param Column Header element
	 * @return: Boolean value
	 * Description: This method checks that ellipsis property are set for wrapping data 
	 */
	public boolean verifyTextOverFlowForDataWraping(WebElement element)
	{
		Boolean status = false;
		String temp = element.getCssValue("text-overflow");
		status=temp.equals(NSGenericConstants.TEXT_OVERFLOW);
		return status;
	}

	/**
	 * @author dheerajg
	 * @param x
	 * @param y
	 * Description: Method to resize browser's window
	 */
	public void resizeBrowserWindow(int x, int y){
		Dimension n = new Dimension(x,y);  
		driver.manage().window().setSize(n);
		try {
			waitForTimePeriod(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This method validates whether element is selected

	public boolean isSelected(WebElement element){

		return element.isSelected();

	}

	public boolean compareimages(String expectedImagePath, String actualImagePath, String diffImagePath){
		final ImageComparer comparer = new ImageComparer();
		boolean cpStatus = false;

		Path path = Paths.get(diffImagePath); 
		Utilities.createDirsIfNotExists(path.getParent().toString());

		cpStatus=comparer.compareImage(expectedImagePath, actualImagePath, diffImagePath);
		return cpStatus;
	}


	public boolean isElementSelected(WebElement ele){

		boolean status = false;
		try{
			if(ele.isSelected())
				status= true;

		}catch(NoSuchElementException e){
			status= false;
		}

		return status;
	}

	/**
	 * @author Vivek
	 * @param 
	 * @return: Boolean value
	 * Description: This method checks that Font of Web Element
	 */
	public boolean verifyFont(WebElement element)
	{  
		boolean status=false;
		String temp = element.getCssValue("font-family");
		status = temp.contains(NSGenericConstants.FONT_FAMILY);
		return status;
	}


	/**
	 * @author Vivek
	 * @param 
	 * @return: Boolean value
	 * Description: This method is used to get text of entire Web page
	 */
	public String getTextForPage()
	{  
		LOGGER.info(Utilities.getCurrentThreadId() + "Getting the text of entire page"); 
		String result=driver.getPageSource();
		return result;
	}

	/**
	 * @author Vivek
	 * @param Window Number to switch as per as Occurrence
	 * @return: 
	 * Description: This method is used to switch to a active window using Window number
	 */  
	public void switchToNewWindow(int windowNumber) 
	{
		Set < String > s = driver.getWindowHandles();   
		Iterator < String > ite = s.iterator();
		int i = 1;
		while (ite.hasNext() && i < 10) {
			String popupHandle = ite.next().toString();
			driver.switchTo().window(popupHandle);
			LOGGER.info(Utilities.getCurrentThreadId() + "Window title is : "+driver.getTitle());
			if (i == windowNumber) break;
			i++;
		}
	}

	/**
	 * @author Vivek
	 * @param 
	 * @return: Boolean value
	 * Description: This method checks whether given web element is editable
	 */
	public boolean isElementEditable(WebElement element)
	{  
		boolean status=false;
		String temp = element.getCssValue("user-select");
		status = temp.contains(NSGenericConstants.DISPLAY_NONE_VALUE);
		return status;
	}

	public String replaceSpecialCharacterFromText(String existingText, String replaceSplCharacter, String replaceWithSplCharacter) {
		return existingText.replace(replaceSplCharacter,replaceWithSplCharacter);

	}

	public int getValueOfXCoordinate(WebElement ele) {
		return ele.getLocation().getX();
	}

	public int getValueOfYCoordinate(WebElement ele) {
		return ele.getLocation().getY();
	}

	public int getHeightOfWebElement(WebElement ele) {
		return ele.getSize().height;
	}

	public int getWidthOfWebElement(WebElement ele) {
		return ele.getSize().width;
	}

	public void resetIISPostDBChanges() throws IOException, InterruptedException{

		String batchFile = System.getProperty("user.dir").replace("\\", "/")+Configurations.TEST_PROPERTIES.get("iisResetBatPath");
		String[] commands = {"cmd.exe", "/C" ,batchFile,Configurations.TEST_PROPERTIES.get("nsHostName")};
		LOGGER.info("IISReset command "+Arrays.asList(commands));

		ProcessBuilder builder = new ProcessBuilder(Arrays.asList(commands));
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) { break; }
			LOGGER.info("console Output ="+line);
		}
		waitForTimePeriod(20000);
	}

	public void performMouseWheelUp(int numberOfWheels) throws IOException, InterruptedException{

		String batchFile = System.getProperty("user.dir").replace("\\", "/")+Configurations.TEST_PROPERTIES.get("mouseWheelUp");
		Runtime.getRuntime().exec(batchFile+" "+numberOfWheels);
		waitForTimePeriod(2000);
	}

	public void performMouseWheelDown(int numberOfWheels) throws IOException, InterruptedException{

		String batchFile = System.getProperty("user.dir").replace("\\", "/")+Configurations.TEST_PROPERTIES.get("mouseWheelDown");
		Runtime.getRuntime().exec(batchFile+" "+numberOfWheels);
		waitForTimePeriod(2000);
	}

	/**
	 * @author Vivek
	 * @param  NA
	 * @return: void
	 * Description: This method is used press ALT and TAB together
	 * @throws AWTException 
	 * @throws InterruptedException 
	 * @ 
	 */ 
	public void pressTaskSwitcher() throws AWTException{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.delay(2000);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);


	}

	/**
	 * @author Vivek
	 * @param  NA
	 * @return: void
	 * Description: This method is used press TAB key together
	 */ 
	public void pressTabKey() {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.TAB).build().perform();
	}

	/**
	 * @param key comma
	 */
	public void pressKey(String value) {
		Actions action = new Actions(driver);
		action.sendKeys(String.valueOf(value)).perform();

	}

	public LogEntries getAllJSLogs(){
		return driver.manage().logs().get(LogType.BROWSER);
	}


	/**
	 * @author Vivek
	 * @param  NA
	 * @return: boolean
	 * Description: This method is used to check if there is any Console error
	 */ 

	public boolean isConsoleErrorPresent() {

		Boolean status=false;

		// Capture all JSerrors
		for (LogEntry error : getAllJSLogs())   
		{
			// check if the status of log is SEVERE
			if(error.getLevel().equals(Level.SEVERE)){
				status=true; 
				LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+error.getMessage());

			} }

		return status;
	}	

	public List<String> getConsoleLogs() {

		List<String> logs = new ArrayList<String>();
		for (LogEntry error : getAllJSLogs())   
		{
			//			if(error.getLevel().equals(Level.INFO))
			logs.add(error.getMessage());

		} 

		return logs;
	}	

	/**
	 * @author Payal
	 * @param  NA
	 * @return: boolean
	 * Description: This method is used to check if given Console error is present 
	 */ 
	public boolean isConsoleErrorPresent(String givenError) {

		Boolean status=false;

		for (LogEntry error : getAllJSLogs())   
		{
			// check if the status of log is SEVERE
			if(error.getLevel().equals(Level.SEVERE)){
				//Verifying the error message
				if(error.getMessage().contains(givenError))
					status=true; 
				LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+error.getMessage());
			} }


		return status;
	}	  


	public void waitForConsoleLogPresent(String givenLog) {

		int call = 0;
		for (String log : getConsoleLogs())   {
			if(log.contains(givenLog)) {
				call =-1;
				break;
			}
		}
		if(call!=-1)
			waitForConsoleLogPresent(givenLog);		



	}	  
	public void waitForURLToChange() 
	{
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		String currentURL = getCurrentPageURL();
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {

					return (!currentURL.equalsIgnoreCase(getCurrentPageURL()));}});
		}
		catch(TimeoutException e)
		{LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+e.getMessage());}
	}


	public String getNonUILaunchURL(String pageRelativeURL, LinkedHashMap<String, String> keyValue) {

		String baseURL;
		if(pageRelativeURL.substring(pageRelativeURL.length() - 1).equalsIgnoreCase("&"))
			baseURL = URLConstants.BASE_URL+pageRelativeURL;
		else
			if (pageRelativeURL.contains("?"))
				baseURL = URLConstants.BASE_URL+pageRelativeURL+"&";
			else if(keyValue.size()!=0)
				baseURL = URLConstants.BASE_URL+pageRelativeURL+"?";
			else
				baseURL = URLConstants.BASE_URL+pageRelativeURL;

		List<String> keys = keyValue.keySet().stream().collect(Collectors.toList());

		int i=0;

		for(String key : keys) {

			baseURL = baseURL + key+"="+keyValue.get(key);
			i++;
			if(i<keys.size()) {
				baseURL = baseURL + "&";
			}

		}
		return baseURL;
	}

	public void clearConsoleLogs() {
		JavascriptExecutor js = (JavascriptExecutor)driver;

		String script = "console.clear();";

		js.executeScript(script);
	}


	public boolean containsIgnoreCase(String srcString, String subString) {
		final int length = subString.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(subString.charAt(0));
		final char firstUp = Character.toUpperCase(subString.charAt(0));

		for (int i = srcString.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches() method:
			final char ch = srcString.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (srcString.regionMatches(true, i, subString, 0, length))
				return true;
		}

		return false;
	}

	public boolean isEnabled(WebElement element){

		return element.isEnabled();

	}
	public int getXCoordinate(WebElement element){

		Point p = element.getLocation();
		return p.getX();


	}

	public int getYCoordinate(WebElement element){

		Point p = element.getLocation();
		return p.getY();



	}

	public boolean isFileDownloaded(String downloadFilePath) {
		boolean flag = false;
		File file = new File(downloadFilePath);
		if (file.exists())
			flag=true;

		return flag;
	}

	public void deleteDownloadedFile(String downloadPath) {
		File file = new File(downloadPath);
		if(file.exists())
			file.delete();        
	}

	public boolean verifyExtensionOfFile(String downloadPath,String fileName,String fileExtension) {
		boolean flag = false;
		File file = new File(downloadPath);
		File[] dir_contents = file.listFiles();

		for (int i =0; i < dir_contents.length;i++) {
			if (dir_contents[i].getName().equals(fileName))
				dir_contents[i].getName().contains(fileExtension);
			return flag=true;

		}
		return flag; 
	}	

	public String toTitleCase(String str) {

		if(str == null || str.isEmpty())
			return "";

		if(str.length() == 1)
			return str.toUpperCase();

		//split the string by space
		String[] parts = str.split(" ");

		StringBuilder sb = new StringBuilder( str.length() );

		for(String part : parts){

			if(part.length() > 1 )                
				sb.append( part.substring(0, 1).toUpperCase() )
				.append( part.substring(1).toLowerCase() );                
			else
				sb.append(part.toUpperCase());

			sb.append(" ");
		}

		return sb.toString().trim();
	}

	public List<String> convertWebElementToStringList(List<WebElement> list){

		List<String> lst2 = new ArrayList<String>();
		list.stream().map(WebElement::getText).forEach(lst2::add);	

		return lst2;

	}

	public List<String> convertWebElementToTrimmedStringList(List<WebElement> list){

		List<String> lst2 = new ArrayList<String>();

		for(WebElement e : list){
			lst2.add(e.getText().trim());
		}	

		return lst2;

	}

	public void pressControlA() {		
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
	}

	public void pressControlLeft(WebElement element) {		
		Actions action = new Actions(driver);
		action.moveToElement(element).keyDown(Keys.CONTROL).click().keyUp(Keys.CONTROL).build().perform();
	}

	public void holdShiftKeyPressed() {		
		Actions action = new Actions(driver);
		action.keyDown(Keys.SHIFT).perform();;
	}

	public void releaseShiftKeyPressed() {		
		Actions action = new Actions(driver);
		action.keyUp(Keys.SHIFT).perform();
	}

	public boolean verifyAccuracyOfValues(int actual,int expected,int percentanage){

		boolean status = false;

		float max =(float) (expected+(expected*percentanage/100));
		float min =(float) (expected-(expected*percentanage/100));
		if(min <= actual && actual <=max)
			status=true;
		return status;

	}

	public void pressAltlLeftWithOffset(WebElement element,int x,int y) {		
		Actions action = new Actions(driver);
		action.moveToElement(element).moveByOffset(x, y).keyDown(Keys.ALT).click().keyUp(Keys.ALT).build().perform();
	}


	public Keys getNumberKeys(int number) {

		Keys numberKey = null ;
		switch(number){

		case 0: numberKey= Keys.NUMPAD0;break;
		case 1: numberKey= Keys.NUMPAD1;break;
		case 2: numberKey= Keys.NUMPAD2;break;
		case 3: numberKey= Keys.NUMPAD3;break;
		case 4: numberKey= Keys.NUMPAD4;break;
		case 5: numberKey= Keys.NUMPAD5;break;
		case 6: numberKey= Keys.NUMPAD6;break;
		case 7: numberKey= Keys.NUMPAD7;break;
		case 8: numberKey= Keys.NUMPAD8;break;
		case 9: numberKey= Keys.NUMPAD9;break;

		}

		return numberKey;


	}

	public Integer convertIntoInt(String value) {

		return Integer.parseInt(value);
	}


	public static WebDriver openNewChromeInstanceWithDisabledWebGL() {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-webgl");		
		options.addArguments("start-maximized", "forced-maximize-mode","no-default-browser-check", "always-authorize-plugins","test-type");	
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches",Collections.singletonList("enable-automation")); 
		ChromeDriver myDriver = new ChromeDriver(options);

		return myDriver;
	}

	public static void closeChromeBrowser(WebDriver driverInstance) {

		driverInstance.close();
		driverInstance.quit();

	}

	public void pressAndholdShiftKey(WebElement element) {		
		Actions action = new Actions(driver);
		action.moveToElement(element).keyDown(Keys.SHIFT).click().build().perform();
	}

	public void pressAndHoldControlKey(WebElement element) {		
		Actions action = new Actions(driver);
		action.moveToElement(element).keyDown(Keys.CONTROL).click().build().perform();
	}

	public void releaseControlKey() {		
		Actions action = new Actions(driver);
		action.keyUp(Keys.CONTROL).perform();
	}

	public String getAuthenticateURLFromNetworkTab() {


		LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);

		String messageUrl ="";

		for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();)
		{
			LogEntry entry = it.next();

			try
			{
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject(OrthancAndAPIConstants.MESSAGE);

				if(message.toString().contains(OrthancAndAPIConstants.AUTHENTICATE)) {

					String method = message.getString(OrthancAndAPIConstants.METHOD);
					if (method != null  && OrthancAndAPIConstants.NETWORK_METHOD.equals(method))
					{
						JSONObject params = message.getJSONObject(OrthancAndAPIConstants.PARAMS);
						JSONObject request = params.getJSONObject(OrthancAndAPIConstants.REQUEST);             
						messageUrl = request.getString(OrthancAndAPIConstants.URL);	    
						break;

					}
				}
			} catch (JSONException e)
			{
				LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+e.getMessage());


			}
		}
		return messageUrl;

	}

	public String getUsersURLFromNetworkTab() {


		LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);

		String postData ="";

		for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();)
		{
			LogEntry entry = it.next();

			try
			{
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject(OrthancAndAPIConstants.MESSAGE);
				if(message.toString().contains(OrthancAndAPIConstants.USERS)) {

					String method = message.getString(OrthancAndAPIConstants.METHOD);
					if (method != null  && OrthancAndAPIConstants.NETWORK_METHOD.equals(method))
					{
						JSONObject params = message.getJSONObject(OrthancAndAPIConstants.PARAMS);
						JSONObject request = params.getJSONObject(OrthancAndAPIConstants.REQUEST);             
						postData = request.getString(OrthancAndAPIConstants.POST_DATA);
						break;

					}
				}
			} catch (JSONException e)
			{
				LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+e.getMessage());


			}
		}
		return postData;

	}

	/**
	 * Returns true if the apiName(url string) is found in the network logs else false
	 * @param apiName - apiName or url string to be searched
	 * @return - boolean indicating if log is present
	 */
	public boolean verifyNetworkLogFound(String apiName) {
		try
		{
			List<LogEntry> logs = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
			for (LogEntry entry : logs)
			{
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject(OrthancAndAPIConstants.MESSAGE);
				String method = message.getString(OrthancAndAPIConstants.METHOD);
				if (method != null  && OrthancAndAPIConstants.NETWORK_METHOD.equals(method))
				{

					JSONObject params = message.getJSONObject(OrthancAndAPIConstants.PARAMS);
					JSONObject request = params.getJSONObject(OrthancAndAPIConstants.REQUEST);             
					String url = request.getString(OrthancAndAPIConstants.URL);
					if (url.toLowerCase().contains(apiName))
					{
						return true;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:" + e.getMessage());
		}
		return false;
	}


	public int getParamValFromNetworkRequest(String extractedMethod, String param, String paramVal) {

		int count =0;
		try
		{
			List<LogEntry> logs = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
			for (LogEntry entry : logs)
			{
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject(OrthancAndAPIConstants.MESSAGE);
				String method = message.getString(OrthancAndAPIConstants.METHOD);
				if (method != null && method.equalsIgnoreCase(extractedMethod))
				{					
					JSONObject params = message.getJSONObject(OrthancAndAPIConstants.PARAMS);
					JSONObject header =  params.getJSONObject(OrthancAndAPIConstants.HEADER);
					if(header.getString(param).contains(paramVal))
						count++;

				}
			}
		} catch (Exception e) {
			LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:" + e.getMessage());
		}

		return count;

	}


	public String  getParamValFromNetworkRequestHeader(String requestURL, String param) {

		String paramVal ="";
		LogEntries logs = driver.manage().logs().get(LogType.PERFORMANCE);

		for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();)
		{
			LogEntry entry = it.next();

			try
			{
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject(OrthancAndAPIConstants.MESSAGE);
				if(message.toString().contains(requestURL)) {

					String method = message.getString(OrthancAndAPIConstants.METHOD);
					if (method != null  && OrthancAndAPIConstants.NETWORK_METHOD.equals(method))
					{

						JSONObject params = message.getJSONObject(OrthancAndAPIConstants.PARAMS);
						JSONObject request = params.getJSONObject(OrthancAndAPIConstants.REQUEST);     
						JSONObject header = request.getJSONObject(OrthancAndAPIConstants.HEADER);     

						paramVal= header.get(param).toString();
						break;

					}
				}
			} catch (JSONException e)
			{
				LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+e.getMessage());


			}
		}

		return paramVal;

	}

	public Map<String, String> decodeQueryString(String query) {
		try {
			Map<String, String> params = new LinkedHashMap<>();
			String[] queryString = query .split("\\?");
			for (String param : queryString[1].split("&")) {
				String[] keyValue = param.split("=", 2);
				String key = URLDecoder.decode(keyValue[0], "UTF-8");
				String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";
				if (!key.isEmpty()) {
					params.put(key, value);
				}
			}
			return params;
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e); // Cannot happen with UTF-8 encoding.
		}
	}

	public void performCNTRLX() throws InterruptedException {

		waitForTimePeriod(1000);
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("x").keyUp(Keys.CONTROL).build().perform();
		waitForTimePeriod(1000);
	}

	public void performCNTRLV() throws InterruptedException {

		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).build().perform();
		waitForTimePeriod(1000);
	}
	public void performCNTRLC() throws InterruptedException {

		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).build().perform();
		waitForTimePeriod(1000);
	}

	public void pressESCKey() throws InterruptedException {

		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).perform();
		waitForTimePeriod(1000);
	}

	public Float convertIntoFloat(String value) {

		return Float.parseFloat(value);
	}
	public Double convertIntoDouble(String value) {

		return Double.parseDouble(value);
	}

	public int roundOffValue(float ele) {

		int value=Math.round(ele);
		LOGGER.info(Utilities.getCurrentThreadId() + "Float value "+ele+ " round off to int as "+value);
		return value;


	}

	public  boolean isAttribtuePresent(WebElement element, String attribute) {
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null){
				result = true;
			}
		} catch (Exception e) {}

		return result;
	}

	public void waitUntilCountChanges(By element, int count) {
		WebDriverWait wait = new WebDriverWait(driver, 1);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {

				int elementCount = driver.findElements(element).size();
				if (elementCount > count)
					return true;
				else
					return false;
			}
		});
	}

	public void waitUntilCountChanges(int time, By element, int count) {
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				
				int elementCount = driver.findElements(element).size();
				if (elementCount > count)
					return true;
				else
					return false;
			}
		});
	}
	
	public int getOccurenceCount(String sentence, String subString) {

		int count = 0, fromIndex = 0;

		while ((fromIndex = sentence.indexOf(subString, fromIndex)) != -1 ){

			count++;
			fromIndex++;

		}

		return count;


	}

	public void scrollUpUsingPageUp(WebElement elementToBeScrolled, WebElement scrollbar) {

		while(!elementToBeScrolled.isDisplayed())
			scrollbar.sendKeys(Keys.PAGE_UP);
	}

	public void scrollDownUsingPageDown(WebElement elementToBeScrolled, WebElement scrollbar) {

		try {
			mouseHover(scrollbar);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!elementToBeScrolled.isDisplayed())
			scrollbar.sendKeys(Keys.PAGE_DOWN);
	}

	public void scrollDownUsingPerfectScrollbar(WebElement elementToBeScrolled, WebElement scrollbar) {
		try {
			mouseHover(scrollbar);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!elementToBeScrolled.isDisplayed())
			scrollbar.sendKeys(Keys.ARROW_DOWN);


	}

	public void scrollUpUsingArrow(WebElement elementToBeScrolled, WebElement scrollbar) {

		try {
			mouseHover(scrollbar);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!elementToBeScrolled.isDisplayed())
			scrollbar.sendKeys(Keys.ARROW_UP);

	}

	public void scrollRightUsingPerfectScrollbar(WebElement elementToBeScrolled, WebElement scrollbar, int interval, int maxWidth) {

		Actions dragger = new Actions(driver);
		int numberOfPixelsToDragTheScrollbarUp = interval;


		if(interval>0)
			for (int j = 0; j <maxWidth; j = j + numberOfPixelsToDragTheScrollbarUp)
			{
				dragger.moveToElement(scrollbar).clickAndHold().moveByOffset(numberOfPixelsToDragTheScrollbarUp,0).release(scrollbar).build().perform();
				if(elementToBeScrolled.isDisplayed())
					break;
			}
		else
			for (int j = maxWidth; j >interval; j = j - numberOfPixelsToDragTheScrollbarUp)
			{
				dragger.moveToElement(scrollbar).clickAndHold().moveByOffset(numberOfPixelsToDragTheScrollbarUp,0).release(scrollbar).build().perform();
				if(elementToBeScrolled.isDisplayed())
					break;
			}

	}

	public void scrollRightUsingPerfectScrollbar(WebElement scrollbar, int x,int y) {

		Actions dragger = new Actions(driver);
		dragger.moveToElement(scrollbar).clickAndHold().moveByOffset(x,y).release(scrollbar).build().perform();
	}

	public void scrollDownUsingPerfectScrollbar(WebElement elementToBeScrolled, WebElement scrollbar, int interval, int maxHeight) {

		Actions dragger = new Actions(driver);
		int numberOfPixelsToDragTheScrollbarUp = interval;
		for (int j = 0; j <maxHeight; j = j + numberOfPixelsToDragTheScrollbarUp)
		{
			try {
				dragger.moveToElement(scrollbar).clickAndHold().perform();
				waitForTimePeriod(200);
				dragger.moveByOffset(0, numberOfPixelsToDragTheScrollbarUp).perform();
				waitForTimePeriod(200);
				dragger.release(scrollbar).build().perform();
				waitForTimePeriod(200);
				if(elementToBeScrolled.isDisplayed())
					break;
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		while(!elementToBeScrolled.isDisplayed())
			scrollbar.sendKeys(Keys.PAGE_UP);

	}


}
