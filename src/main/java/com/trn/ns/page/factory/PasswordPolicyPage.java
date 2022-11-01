package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PasswordPolicyConstants;
import com.trn.ns.web.page.WebActions;

public class PasswordPolicyPage extends WebActions{
	
	private WebDriver driver;

	@FindBy(id = "enforcePasswordHistory")
	public WebElement checkbox;

	@FindBy(xpath = "//form/div[1]/label")
	public WebElement checkboxLabel;

	@FindBy(css = "div.alert")
	public WebElement message;

	@FindBy(id = "maxFailedAttemptsBeforeLock")
	public WebElement maxFailedAttemptsBeforeLock;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.MAXIMUM_FAILED_ATTEMPTS_BEFORE_LOCK+"']")
	public WebElement maxFailedAttemptsBeforeLockLabel;

	@FindBy(id = "maximumPasswordAge")
	public WebElement maximumPasswordAge;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.MAXIMUM_PASSWORD_AGE+"']")
	public WebElement maximumPasswordAgeLabel;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.MINIMUM_PASSWORD_LENGTH+"']")
	public WebElement minimumPasswordLengthLabel;

	@FindBy(id = "minimumPasswordLength")
	public WebElement minimumPasswordLength;

	@FindBy(xpath ="//*[text()='"+PasswordPolicyConstants.REQUIRED_LOWERCASE_CHARACTERS+"']")
	public WebElement requiredLowercaseCharactersCountLabel;

	@FindBy(xpath = "//*[contains(text(),'"+PasswordPolicyConstants.REQUIRED_UPPERCASE_CHARACTERS+"')]")
	public WebElement requiredUppercaseCharactersCountLabel;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.REQUIRED_DIGITS+"']")
	public WebElement requiredDigitsCountLabel;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.REQUIRED_SPECIAL_CHARACTERS+"']")
	public WebElement requiredSpecialCharactersCountLabel;

	@FindBy(xpath = "//*[text()='"+PasswordPolicyConstants.ALLOWED_SPECIAL_CHARACTERS+"']")
	public WebElement specialCharactersLabel;

	@FindBy(id = "requiredLowercaseCharactersCount")
	public WebElement requiredLowercaseCharactersCount;

	@FindBy(id = "requiredUppercaseCharactersCount")
	public WebElement requiredUppercaseCharactersCount;

	@FindBy(id = "requiredDigitsCount")
	public WebElement requiredDigitsCount;

	@FindBy(id = "requiredSpecialCharactersCount")
	public WebElement requiredSpecialCharactersCount;

	@FindBy(id = "specialCharacters")
	public WebElement specialCharacters;

	@FindBy(css = ".btn.btn-secondary.mat-raised-button")
	public WebElement updatePolicyButton;

	@FindBy(tagName = "html")
	public WebElement passwordPolicyLabel;

	public By loadingText= By.xpath("//*[contains(text(),'Loading')]");

	@FindBys(@FindBy(css=".form-group input.form-control"))
	public List<WebElement> allInputBoxes;
	
	@FindBys(@FindBy(css=".form-group label"))
	public List<WebElement> allLabels;
		
	@FindBy(css= "div.ps__thumb-y")
	public WebElement scrollbar;
	
	@FindBy(css= "div.ps__thumb-y")
	public WebElement scrollbarBox;
	
	@FindBy(css= ".ns-passwordpolicy-form")
	public WebElement form;
	
	public PasswordPolicyPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForPasswordPolicyToLoad();
	}

	public void waitForPasswordPolicyToLoad(){
		
		waitForElementInVisibility(loadingText);
		waitForElementVisibility(checkboxLabel);
	}
	
	public String updatePassPolicyFieldValue(String maxFailAtpts,String maxPwdAge,String minPwdLnth,String lowCaseChars, String uppCaseChars
			, String reqDigits, String reqSplChars,String allowSplChars ) throws InterruptedException {
		
		// Update Maximum Failed Attempts Before Lock field
		enterTextWithView(maxFailedAttemptsBeforeLock,maxFailAtpts);
		enterTextWithView(maximumPasswordAge,maxFailAtpts);
		enterTextWithView(minimumPasswordLength,maxFailAtpts);		
		enterTextWithView(requiredLowercaseCharactersCount,maxFailAtpts);
		enterTextWithView(requiredUppercaseCharactersCount,maxFailAtpts);
		enterTextWithView(requiredDigitsCount,maxFailAtpts);
		enterTextWithView(requiredSpecialCharactersCount,maxFailAtpts);
		enterTextWithView(specialCharacters,maxFailAtpts);
		click(updatePolicyButton);
		waitForTimePeriod(1000);
		Actions dragger = new Actions(driver);
		dragger.moveToElement(scrollbar).clickAndHold().moveByOffset(0, 40).release(scrollbar).build().perform();
		waitForElementVisibility(message);
		
		return getText(VISIBILITY, message);	
		
	}
		
	public boolean verifySuccessMessage() {
		
		boolean status = getColorOfRows(message).equals(PasswordPolicyConstants.SUCCESS_TEXT_COLOR);
		status = status && getBackgroundColor(message).equals(PasswordPolicyConstants.SUCCESS_BACKGROUND_COLOR);
		status = status && getBorderColorOfWebElemnt(message).equals(PasswordPolicyConstants.SUCCESS_BORDER_COLOR);
	
		return status;
	
	}
	
	public boolean verifyFailedMessage() {
		
		boolean status = getColorOfRows(message).equals(PasswordPolicyConstants.FAILED_TEXT_COLOR);
		status = status && getBackgroundColor(message).equals(PasswordPolicyConstants.FAILED_BACKGROUND_COLOR);
		status = status && getBorderColorOfWebElemnt(message).equals(PasswordPolicyConstants.FAILED_BORDER_COLOR);
	
		return status;
	
	}
	
	
	
	
}
