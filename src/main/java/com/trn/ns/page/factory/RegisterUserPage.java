package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.web.page.WebActions;

public class RegisterUserPage extends WebActions{

	@SuppressWarnings("unused")
	private WebDriver driver;

	@FindBy(id="firstname")
	public WebElement firstNameTextbox;

	@FindBy(id="middleinitial")
	public WebElement middleNameTextbox;

	@FindBy(id="lastname")
	public WebElement lastNameTextbox;

	@FindBy(id="email")
	public WebElement emailTextbox;


	@FindBy(id="username")
	public WebElement usernameTextbox;

	@FindBy(id="password")
	public WebElement passwordTextbox;

	@FindBy(id="confirmpassword")
	public WebElement confirmPasswordTextbox;

	@FindBy(xpath="//button[text()='Submit']")
	public WebElement submitButton;

	@FindBy(xpath="//button[text()='Cancel']")
	public WebElement cancelButton;

	@FindBy(css=".alert")
	public WebElement message;

	@FindBy(css= "div.row.nsTableRow >div:nth-child(2)")
	public WebElement usernameCol;

	public By loadingText= By.xpath("//*[contains(text(),'Loading')]");

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.DESKTOP_RENDERING_MODE_LABEL+"')]")
	public WebElement desktopRenderingModeLabel;

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.MOBILE_RENDERING_MODE_LABEL+"')]")
	public WebElement mobileRenderingModeLabel;

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.MOBILE_RENDERING_MODE_LABEL+"')]/../div/label[contains(text(),'Auto')]/input")
	public WebElement autoRenderingModeForMobile;

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.MOBILE_RENDERING_MODE_LABEL+"')]/../div/label[contains(text(),'Lossy')]/input")
	public WebElement lossyRenderingModeForMobile;

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.DESKTOP_RENDERING_MODE_LABEL+"')]/../div/label[contains(text(),'Auto')]/input")
	public WebElement autoRenderingModeForDesktop;

	@FindBy(xpath="//*[(text()='"+LoginPageConstants.DESKTOP_RENDERING_MODE_LABEL+"')]/../div/label[contains(text(),'Lossy')]/input")
	public WebElement lossyRenderingModeForDesktop;

	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;
	
	@FindBy(css=".ns-adduser-form")
	public WebElement registerPageForm;
	
	@FindBys(@FindBy(css=".form-group input.form-control"))
	public List<WebElement> allInputBoxes;
	
	@FindBys(@FindBy(css=".form-group label:not([class])"))
	public List<WebElement> allLabels;
	
	public RegisterUserPage(WebDriver driver) {
		
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForRegisterPageToLoad();
	}
	
	public RegisterUserPage(WebDriver driver, boolean loaded) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		if(loaded)
			waitForRegisterPageToLoad();
	}

	public void waitForRegisterPageToLoad(){
		waitForElementInVisibility(loadingText);
		waitForElementVisibility(firstNameTextbox);		


	}

	public String createNewUser(String fname, String lname, String email, String uname,String pwd, String cpwd) {

		inputValuesToForm(fname,"", lname, email, uname, pwd, cpwd);
		return clickOnSubmitButton();

	}
	
	public String createNewUserNonUIWay(String username, String password, String fname, String lname, String email, String uname,String pwd, String cpwd, boolean rpdMode) {

		navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL+"?username="+username+"&password="+password);
		waitForRegisterPageToLoad();
		inputValuesToForm(fname,"", lname, email, uname, pwd, cpwd);
		if(!rpdMode)
			click(lossyRenderingModeForDesktop);
		return clickOnSubmitButton();

	}
	
	
	
	public String createNewUser(String fname, String mname, String lname, String email, String uname,String pwd, String cpwd) {

		inputValuesToForm(fname, mname, lname, email, uname, pwd, cpwd);		
		return clickOnSubmitButton();

	}
	
	public String clickOnSubmitButton() {
		
		click(submitButton);
		waitForElementVisibility(message);
		scrollIntoView(message);
		return getText(VISIBILITY, message);	
		
	}

	public void clickOnCancelButton() {
		
		if(!cancelButton.isDisplayed())
			scrollIntoView(cancelButton);
		click(cancelButton);
		
	}
	public void inputValuesToForm(String fname, String mname,String lname, String email, String uname,String pwd, String cpwd) {

		scrollIntoView(firstNameTextbox);
		enterText(firstNameTextbox, fname);
		if(!mname.isEmpty())
			enterText(middleNameTextbox, mname);
		
		enterText(lastNameTextbox, lname);
		enterText(emailTextbox, email);

		scrollIntoView(passwordTextbox);
		enterText(usernameTextbox, uname);
		enterText(passwordTextbox, pwd);
		enterText(confirmPasswordTextbox, cpwd);

	}
	
	
	
	public WebElement getTooltipWebElement(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return tooltip ;		

	}

	public String getTooltip(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return getText(tooltip);		

	}

}