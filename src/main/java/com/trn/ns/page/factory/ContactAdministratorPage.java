package com.trn.ns.page.factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.web.page.WebActions;

public class ContactAdministratorPage extends WebActions{

	@SuppressWarnings("unused")
	private WebDriver driver;

	@FindBy(css="body > ns-app > div > div > div > ng-component > div > div")
	public WebElement errorMessage;

	public ContactAdministratorPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	

}