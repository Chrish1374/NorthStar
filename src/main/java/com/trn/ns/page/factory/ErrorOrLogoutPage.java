package com.trn.ns.page.factory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.web.page.WebActions;

public class ErrorOrLogoutPage extends WebActions{

	
	
	@SuppressWarnings("unused")
	private WebDriver driver;

	
	public By errorMessage = By.cssSelector(".err-msg");
	
	//Error message element
	@FindBy(css=".err-msg")
	public WebElement message;

	//Go back to Login button element
	@FindBy(css=".eur-text")
	public WebElement goBackToLoginbutton;
		
	//Copyright and version footer element
	@FindBy(id="versionFooter")
	public WebElement versionfooter;
		
	//Error header message element
	@FindBy(css="div.right-container > h2")
	public WebElement headerMessage;
		
	//Error message main container
	@FindBy(css="div.overlay")
	public WebElement errorMsgContainer;
		
	//Logout message
	@FindBy(css="div.right-container > div")
	public WebElement logoutMsg;

	public ErrorOrLogoutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForElementVisibility(message);
	}

	public void waitForErrorPageToLoad(){
		waitForElementVisibility(errorMessage);
	}
	

}