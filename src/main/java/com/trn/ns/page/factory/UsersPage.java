package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.web.page.WebActions;

public class UsersPage extends WebActions{

	@SuppressWarnings("unused")
	private WebDriver driver;
	
	@FindBy(tagName = "body")
	public WebElement body;
	
	@FindBy(xpath=".//th[@id='1']")
	public WebElement usernameCol;

	@FindBys({@FindBy(xpath ="//td[@id='1']")})
	public List<WebElement> usernameColList;
	
	public By loadingText= By.xpath("//*[contains(text(),'Loading')]");
	
	@FindBys(@FindBy(css="div.columnHeaderOverFlow"))
	public List<WebElement> columnHeadersTextOverFlow;
	
	@FindBy(css ="ns-tablelayout > fieldset > div")
	public WebElement usersTable;
		
	@FindBy(css =".btn.ns-adduser-button")
	public WebElement addUserButton;
	
	@FindBy(css ="div.userTable")
	public WebElement userTableSection;
	
	
	public UsersPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForUsersPageToLoad();
	}

	public void waitForUsersPageToLoad(){

		waitForElementInVisibility(loadingText);
		waitForElementVisibility(usernameCol);		
	}
	
	public List<String> getListOfAllUsers(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < usernameColList.size(); i++) {
			list.add(usernameColList.get(i).getText());
		}
		return list;
	}
	

	
}