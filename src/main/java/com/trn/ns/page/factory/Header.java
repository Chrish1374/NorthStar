package com.trn.ns.page.factory;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.web.page.WebActions;

public class Header extends WebActions{

	@SuppressWarnings("unused")
	private WebDriver driver;

	public By logout = By.cssSelector(".dropdown-menu.username-dropdown-menu[style*='block'] #logout");
	
	@FindBy(css="#logo-terarecon > .defaultIcon > svg[enable-background]")
	public WebElement terareconLogoWithConfigKey;
	
	@FindBy(css ="#nsLogo > div.defaultIcon > #Layer_1")
	public WebElement northstarLogoWithConfigKey;	
	
	@FindBy(xpath="//*[@class='cls-splitScreen-1']")
	public WebElement optionGrid;

	@FindBy(css="#header button#dropdownMenu")
	public WebElement userInfo;
	
	@FindBys(@FindBy(css="#header button#dropdownMenu g path"))
	public List<WebElement> userInfoSandwichIcon;	

	@FindBy(xpath="//*[@class='dropdown-menu username-dropdown-menu'][@style]//*[text()='"+LoginPageConstants.ABOUTLABEL+"']")
	public WebElement about;

	@FindBy(xpath="//*[@class='dropdown-menu username-dropdown-menu'][@style]//*[text()='Logout']")
	public WebElement logOut;
	
	@FindBy(xpath="//*[@class='dropdown-menu username-dropdown-menu'][@style]//*[text()='"+LoginPageConstants.HELPLABEL+"']")
	public WebElement help;
	
	@FindBy(css= "#help svg path")
	public WebElement helpIcon;
	
	@FindBy(css= "#userManual svg path")
	public WebElement userManualIcon;
	
	@FindBy(css= "#logout svg path")
	public WebElement logoutIcon;

	@FindBy(css=".divContainer:not([hidden]) div.dropdown-menu.username-dropdown-menu")
	public WebElement optionMenu;	
	
	@FindBys(@FindBy(css=".divContainer:not([hidden]) div.dropdown-menu.username-dropdown-menu div.dropdown-item.username-dropdown-item"))
	public List<WebElement> usermenuOptions;	

	@FindBy(css="#header #headerDiv > div")
	public WebElement userNameID;
	
	public By userNameField = By.cssSelector("#header #headerDiv > div");
	
	//Dropdown which appears after clicking on user name
	@FindBy(css="div.slide div.dropdown-menu")
	public WebElement userDropDown;
	
	//User manual page which opens after clicking on Help menu from dropdown
	@FindBy(css="body > embed")
	public WebElement userManual;

	//Build version present on header
	@FindBy(css="div.header-text.product-version > div:nth-child(2) > label")
	public WebElement buildVersionOnHeader;
	
	@FindBy(css=".trim-info.displayLabel")
	public WebElement patientInfo;
	
	@FindBys(@FindBy(css = "#product-name-icon g#Guides>g>path"))
	public List<WebElement> eurekaNameLetters;
	
	@FindBys(@FindBy(css = "#product-logo-icon g#Guides>g>path"))
	public List<WebElement> eurekaLogoLetters;

	@FindBy(css = "div#product-toolbar")
	public WebElement eurekaProductName;


	public Header(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void waitForHeaderToLoad(){

		waitForElementVisibility(userInfo);
	}
	
	public void logout() throws InterruptedException{
		userInfo.isDisplayed();
		click(userInfo);
		waitForElementVisibility(logout);
		click(logOut);
		waitForEndOfAllAjaxes();
		waitForTimePeriod(100);

	}

	public void viewAboutPage(){
		userInfo.isDisplayed();
		click(userInfo);
		waitForElementVisibility(about);
		click(about);
		waitForEndOfAllAjaxes();
	}

	
	public void refreshPageUsingF5(){

		pressF5Key();
	}

	public void browserFullMode(){
		pressF11Key();
	}
	
	public void openLogoffMenu() {
		click(userInfo);
		waitForElementVisibility(about);
	}

	public boolean isEurekaProductNamePresent() {
		
		boolean status = eurekaNameLetters.size()==6;
		
		List<String> values = getEurekaLetters();
		 for(int i =0;i<eurekaNameLetters.size();i++) {
			 status = status && getAttributeValue(eurekaNameLetters.get(i),NSGenericConstants.PATH_ATTR_D).equals(values.get(i));
			 status = status && getCssValue(eurekaNameLetters.get(i),NSGenericConstants.FILL).equals(ViewerPageConstants.MACHINE_FILTER_BACKGROUND_W);
		 }
		 
		 status = status && eurekaLogoLetters.isEmpty();
		
		 return status;
	}
	
	public boolean isEurekaLogoPresent(int numberOfLetters) {
		
		boolean status = eurekaNameLetters.isEmpty();		
		status = status && eurekaLogoLetters.size()==numberOfLetters;
		status = status && getCssValue(eurekaLogoLetters.get(0),NSGenericConstants.FILL).equals(ViewerPageConstants.MACHINE_FILTER_BACKGROUND_W);
		 return status;
	}
	
	
		
	public List<String> getEurekaLetters() {
		
		// EUREKA LOGO DIRECTIONS
		List <String> eurekaLetters = new ArrayList<String>();
		eurekaLetters.add("M92.6,96.5v16.9H22.1V39c0-9.2,7.4-16.6,16.6-16.6c0,0,0,0,0,0h52.2v16.9H43v19.8h42.3v16.4H43v21.1H92.6z");		
		eurekaLetters.add("M114.2,104.2c-7.2-7.2-10.9-17.5-10.9-30.8v-51h21.1v50.2c0,16.3,6.8,24.4,20.3,24.4c6.6,0,11.6-2,15.1-5.9\n\t\t\tc3.5-3.9,5.2-10.1,5.2-18.5V22.4h20.8v51c0,13.3-3.6,23.6-10.9,30.8c-7.2,7.2-17.4,10.8-30.4,10.8\n\t\t\tC131.6,115,121.4,111.4,114.2,104.2z");
		eurekaLetters.add("M256.7,113.4L239.2,88h-19.4v25.4h-21.1v-91h39.4c8.1,0,15.1,1.3,21,4c5.6,2.4,10.4,6.4,13.7,11.4\n\t\t\tc3.3,5.3,5,11.4,4.8,17.5c0.2,6.2-1.5,12.3-4.9,17.5c-3.4,5-8.2,8.9-13.8,11.2l20.4,29.3H256.7z M251.4,43.7\n\t\t\tc-3.3-2.7-8.1-4.1-14.4-4.1h-17.2v31.7H237c6.3,0,11.1-1.4,14.4-4.2c3.3-2.8,4.9-6.7,4.9-11.7C256.4,50.3,254.7,46.4,251.4,43.7z");
		eurekaLetters.add("M362.8,96.5v16.9h-70.5V39c0-9.1,7.4-16.6,16.6-16.6h0h52.2v16.9h-47.8v19.8h42.3v16.4h-42.3v21.1H362.8z");
		eurekaLetters.add("M410.7,77.6l-12.2,12.7v23h-20.9v-91h20.9v42.5l40.3-42.5h23.4L424.5,63l39.9,50.4h-24.6L410.7,77.6z");
		eurekaLetters.add("M541.4,93.9h-42.3l-8.1,19.5h-21.6l40.6-91h20.8l40.7,91h-22.1L541.4,93.9z M534.8,77.9l-14.4-34.8\n\t\t\tl-14.4,34.8H534.8z");
		
		return eurekaLetters;
	}

	public boolean verifyEurekaProductNameOnMouseHover(WebElement radialMenuIcon) throws InterruptedException
	{
		mouseHover(radialMenuIcon);
		boolean status=isEurekaProductNamePresent();
		return status;
	}

}