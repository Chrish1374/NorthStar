package com.trn.ns.page.factory;

import java.util.List;

import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.Utilities;
import com.trn.ns.web.page.WebActions;

public class LoginPage extends WebActions{

	private WebDriver driver;

	@FindBy(name ="username")
	public WebElement usernameTextbox;

	@FindBy(name ="password")
	public WebElement passwordTextbox;
	
	@FindBy(xpath="//div[@id='loginMain']")
	public WebElement loginFrame;

	@FindBy(xpath ="//div[contains(@class,'left-image overlap')]")
	public WebElement brainImage;
	
	@FindBy(xpath ="//ns-login-form")
	public WebElement loginContainer;
	

	
	@FindBy(css ="button.btn.eur-Login-button")
	public WebElement signInButton;

	@FindBy(css ="div.ns-login-text")
	public WebElement signInText;

	@FindBy(xpath ="//*[@id='mtUsername']/..//table//td[2]")
	public WebElement usernameWarningMsg;

	@FindBy(xpath ="//*[@id='mtPassword']/..//table//td[2]")
	public WebElement passwordWarningMsg;

	@FindBy(css=".tooltip.show.eur-tooltip.bs-tooltip-bottom[id*='ngb-tooltip'] .tooltip-inner td:nth-child(2)")
	public WebElement loginFailedErrorMsg;
	
	@FindBy(css="div.container-fluid.ns-login-parent")
	public WebElement parentComponent;

	@FindBy(xpath ="(//button[contains(text(),'Advanced')])")
	public WebElement advancedButton;

	@FindBy(xpath="//*[(contains(@class,'slide userNameDiv'))]//*[text()='Logout'] ")
	public WebElement logOut;

	public By loader = By.cssSelector("div.loader");

	@FindBy(css="#header button#dropdownMenu")
	public WebElement userInfo;

	@FindBy(css="#dropdownMenu + div a")
	public WebElement userInfoMenuList;
	
	@FindBy(css="div.loader")
	public WebElement loadingIndicator;

	public By infoPopup = By.cssSelector(".info-content.bottom");
	
	@FindBy(css =".btn.info-icon #icn-info")
	public WebElement iIconButton;

	@FindBy(css ="div.ns-login-version-div > label")
	public WebElement buildVersion;

	@FindBy(css=".dropdown-item.eur-info[href*='licenseInfo'] span")
	public WebElement about;
	
	@FindBy(css=" a[href='#/cemarking']")
	public WebElement ceMarkingLink;
	
	@FindBy(css ="[title='CE Marking'] svg")
	public WebElement ceMarkingImg;
	
	@FindBy(css=".loader")
	public WebElement spinnerIcon;
	
	@FindBy(css="div.licenseInfoDiv")
	public WebElement licenceInfo;

	@FindBy(css="#versionFooter label")
	public WebElement copyRightInfo;
	
	@FindBy(css=".dropdown-item.eur-info[href*='patent']")
	public WebElement patentLink;
	
	@FindBy(css ="#patent svg")
	public WebElement patentImg;
	
	@FindBy(css ="div.ps__rail-y")
	public WebElement verticalScroll;
	
	@FindBy(css =".ns-remove-padding.eur-info span")
	public WebElement buildVersionInAboutSection;
	
	@FindBy(css ="#versionFooter > div:nth-child(1)")
	public WebElement iIconWithInfoPopUp;
	
	@FindBy(css ="#logo-welcometoeureka-white")
	public WebElement welcomeToEurekaLogo;
	
	@FindBy(css ="[title='Version'] svg")
	public WebElement versionImg;
	 
	@FindBys({@FindBy(css =".info-content.bottom span")})
	public List<WebElement> textFromInfoPopUp;
	
	@FindBys({@FindBy(css =".info-content.bottom ns-icon")})
	public List<WebElement> iconsFromInfoPopUp;
	
	@FindBy(css ="#loginMain")
	public WebElement loginPage;
	
	@FindBy(css ="#version > div")
	public WebElement versionIcon;
		
	@FindBy(css =".licenseInfoDiv")
	public WebElement aboutPage;
		
	@FindBy(css =".cemarking-div")
	public WebElement ceMarkingPage;
	
	public LoginPage(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
		if(System.getProperty("build")!=null) 
			Configurations.TEST_PROPERTIES.put("build", System.getProperty("build")); 
	}

	public void waitForLoginPageToLoad(){
		waitForElementVisibility(usernameTextbox);
		waitForElementVisibility(passwordTextbox);
	}

	public void login(String uname,String pwd){

		waitForLoginPageToLoad();
		enterText(usernameTextbox, uname);
		if(getAttributeValue(usernameTextbox, NSGenericConstants.VALUE).isEmpty())
			enterText(usernameTextbox, uname);
		enterText(passwordTextbox, pwd);
		if(getAttributeValue(passwordTextbox, NSGenericConstants.VALUE).isEmpty())
			enterText(passwordTextbox, pwd);
		click(signInButton);
		
		if(isElementPresent(loginFailedErrorMsg))
		if(getText(loginFailedErrorMsg).equalsIgnoreCase("Exception Caught"))
			click(signInButton);
		waitForEndOfAllAjaxes();
	}

	public void loginWithKeyPress(String uname,String pwd){

		enterText(usernameTextbox, uname);
		enterText(passwordTextbox, pwd);
		pressKeys(Keys.ENTER);
		waitForEndOfAllAjaxes();

	}

	public void advancedLogin(String uname,String pwd){

		enterText(usernameTextbox, uname);
		if (usernameTextbox.getAttribute(NSGenericConstants.VALUE).isEmpty()) {
			enterText(usernameTextbox, uname);
		}
		enterText(passwordTextbox, pwd);
		click(advancedButton);
		waitForEndOfAllAjaxes();
	}

	
	public void logout(){

		userInfo.isDisplayed();
		click(userInfo);
		//waitForElementVisibility(userInfoMenuList);
		//waitForElementVisibility(logOut);
		click(logOut);
		waitForEndOfAllAjaxes();
		try {
			waitForTimePeriod(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void viewAboutPage(){
		about.isDisplayed();
		click(about);
		waitForEndOfAllAjaxes();
	}

	public void waitForLoadingIndicatorToDisappear() 
	{
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the loading Indicator to disappear");

		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(loader)));			
		}
		catch(TimeoutException e)
		{}
	}
	
	public boolean verifyPasswordEncryption(String currentURL){

		return getAuthenticateURLFromNetworkTab().contains(currentURL);
		
	}

	//MFA support
	
	public By mfaPopup = By.cssSelector("#tokenForm");
	
	@FindBy(css="#tokenForm")
	public WebElement mfaDialog;
	
	@FindBy(css="h2.mat-dialog-title")
	public WebElement mfaTitleMessage;
	
	@FindBy(css="#tokenNumber")
	public WebElement mfaTextbox;
	
	@FindBy(css=".btn.btn-secondary.mat-raised-button")
	public WebElement continueButton;
	
	@FindBy(css=".tooltip.show.eur-tooltip .tooltip-inner td:nth-child(2)")
	public WebElement errorMessage;
	
	public By mfaErrorMessage = By.cssSelector(".tooltip.show.eur-tooltip .tooltip-inner td:nth-child(2)");
	
	public String getOTP(String secretKey) {
		
		Totp totp = new Totp(secretKey);
		return totp.now(); 				
	}
	
	public void enterOTP(String otp) throws InterruptedException {
		
		enterText(mfaTextbox,otp);
		continueButton.click();
		waitForTimePeriod(2000);
	}
	
	public boolean verifyMFAPopup() {
		
		boolean status = isElementPresent(mfaPopup);
		status = status && isElementPresent(mfaTitleMessage) && getText(mfaTitleMessage).equalsIgnoreCase(LoginPageConstants.MFA_POPUP_TITLE);		
		status = status && isElementPresent(continueButton) && getCssValue(continueButton,NSGenericConstants.BACKGROUND_IMAGE).equalsIgnoreCase(LoginPageConstants.MFA_CONTINUE_BUTTON_COLOR) && getText(continueButton).equalsIgnoreCase(LoginPageConstants.MFA_CONTINUE_BUTTON_TEXT);
		status = status && isElementPresent(mfaTextbox);
	
		return status;
	}
	
	
	public boolean verifyErrorMessage(WebElement element,WebElement errorMsgElement, String message) throws InterruptedException {
		
		Actions action = new Actions(driver);
		action.moveToElement(element).moveByOffset(0, 0).clickAndHold().build().perform();
		waitForTimePeriod(500);
		boolean status = getText(errorMsgElement).equals(message);
		action.release().build().perform();
		return status;	
	}
	
	public boolean verifyTextIsHighlightedInInfoPopUp(WebElement element) {
		
			return getColorOfRows(element).equalsIgnoreCase(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
		}
	 
    public boolean verifyIconIsHighlightedInInfoPopUp(WebElement element) {
		boolean status=false;
    	if(isElementPresent(element.findElement(By.cssSelector("#V7_Login_Brain_Gradient"))))
    		
    	{
		 status = element.findElement(By.cssSelector("#V7_Login_Brain_Gradient")).getCssValue(NSGenericConstants.FILL).equalsIgnoreCase(LoginPageConstants.HIGHLIGHTED_COLOR);
    	}
		return status;
	}
    
    public boolean verifyIconHighlightedForBuildVersion(WebElement element) {
		
    	boolean status=false;
    	if(isElementPresent(element.findElement(By.cssSelector("g g#P_V5_Header-Color_Purple-Copy-3"))))
    	{
		 status = element.findElement(By.cssSelector("g g#P_V5_Header-Color_Purple-Copy-3")).getCssValue(NSGenericConstants.FILL).equalsIgnoreCase(LoginPageConstants.HIGHLIGHTED_COLOR);
    	}
		return status;
	}
  
    public boolean verifyLinksPresentInsideInfoPopUp(List<WebElement> element)
    {
    	boolean status=true;
    	
    	for(int i=0;i<element.size();i++)
    	 status = status && isElementPresent(element.get(i));
		return status;
    	
    	
    }
}