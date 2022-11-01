package com.trn.ns.page.factory;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.web.page.WebActions;

public class ActiveDirectoryPage extends WebActions{

	@SuppressWarnings("unused")
	private WebDriver driver;

	@FindBy(css=".ns-configure-header")
	public WebElement confiActiveDirectoryHeader;
	
	By addAndActiveDirDomain = By.cssSelector("div.add-domain");
	
	@FindBy(xpath="//*[@id='addbutton']")
	public WebElement addButton;

	@FindBy(xpath="//*[@id='removebutton']")
	public WebElement removeButton;
	
	@FindBy(xpath="//*[@id='editbutton']")
	public WebElement editButton;
	
	@FindBy(css="#domainName")
	public WebElement domainAddTextbox;
	
	@FindBy(css="#domainServer")
	public WebElement domainServerAddTextbox;
	
	@FindBy(css="#importbutton")
	public WebElement importButton;
	
	@FindBys({@FindBy(css="td[id='0']")})
	public List<WebElement> activeDirectoryDomainsList;
	
	@FindBy(css=".alert-warning")
	public WebElement activeDirectoryError;
	
	@FindBys({@FindBy(css=".selectable.nsTableRow")})
	public List<WebElement> availableGroupsList;
	
	@FindBys({@FindBy(css = ".inner-groups> div:nth-child(1) .selectable.nsTableRow")})
	public List<WebElement> grantedGroupsList;
	
	@FindBys({@FindBy(css=".selectable.nsTableRow input")})
	public List<WebElement> availableGroupsCheckboxList;
	
	@FindBys({@FindBy(css=".inner-groups> div:nth-child(1) .selectable.nsTableRow input")})
	public List<WebElement> grantedGroupsCheckboxList;
	
	@FindBys({@FindBy(css="label[for=group]")})
	public List<WebElement> groups;
	
	@FindBy(css="#searchbutton")
	public WebElement searchButton;
	
	@FindBy(css="#clearbutton")
	public WebElement clearButton;
	
	@FindBy(css="#searchTextfield")
	public WebElement searchTextfield;
	
	@FindBy(css="button.alert-close")
	public WebElement crossIcon;
	
	@FindBy(css=".form-group.popup-div.add-domain")
	public WebElement addDomainPopup;
	
	public By addRemoveDiv = By.cssSelector("div.add-bottom-div");
	
	@FindBy(css="button.btn[type='submit']")
	public WebElement updateButton;
	
	@FindBys(@FindBy(css="tr.columnHeaderBackground.showHeaderBorderTop th"))
	public List<WebElement> activeDirDomainTableHeader;
	
	@FindBy(css="div.ns-configure-form")
	public WebElement activeDirPage;
	
	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;
	
	@FindBys(@FindBy(css="tr.row.tableRow.selectedRow"))
	public List<WebElement> addedDomainList;
	

	public ActiveDirectoryPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForActiveDirectoryPageToLoad();
	}

	public void waitForActiveDirectoryPageToLoad(){
		waitForElementVisibility(addRemoveDiv);
	}
	
	public boolean importDomainToActiveDirectoryDomain(String domain) throws TimeoutException, InterruptedException
	{

		boolean flag = false;
		waitForActiveDirectoryPageToLoad();
		click(CLICKABILITY, addButton);
		waitForElementVisibility(addAndActiveDirDomain);
		waitForElementVisibility(domainAddTextbox);
		waitForTimePeriod(1000);
		enterText(domainAddTextbox, domain);
		waitForTimePeriod(1000);
		enterText(domainServerAddTextbox, domain);
		waitForElementClickability(importButton);
		click(CLICKABILITY, importButton);
		waitForElementsVisibility(By.cssSelector("td[id='0']"));
		waitForActiveDirectoryPageToLoad();
		for(WebElement webElement:activeDirectoryDomainsList){
			if(getText(webElement).contains(domain)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	
	public boolean verifyDomainRemoveFromActiveDirectory(String domain) throws TimeoutException, InterruptedException
	{
		boolean flag = false;
		waitForActiveDirectoryPageToLoad();
		for(WebElement webElement:activeDirectoryDomainsList){
			if(getText(webElement).contains(domain)){
			click(CLICKABILITY, removeButton);
			waitForActiveDirectoryPageToLoad();
			flag = true;
			}
		}
		return flag;
	}
	
	public boolean selectGroupsAndUpdate(String groups) throws TimeoutException, InterruptedException
	{
		boolean flag = false;
		enterText(CLICKABILITY,searchTextfield, groups);
		waitForActiveDirectoryPageToLoad();
		click(CLICKABILITY, searchButton);
		waitForActiveDirectoryPageToLoad();
		for(WebElement webElement:availableGroupsList){
			if(getText(webElement).contains(groups)){
				
				click(availableGroupsCheckboxList.get(availableGroupsList.indexOf(webElement)));
				flag = true;
				break;
			}
		}
		waitForActiveDirectoryPageToLoad();
		click(CLICKABILITY, updateButton);
		return flag;
	}
	
	public boolean deselectGroupsAndUpdate(String groups) throws InterruptedException
	{
		boolean flag = false;
		waitForActiveDirectoryPageToLoad();
		waitForTimePeriod(2000);
		for(WebElement webElement:grantedGroupsList){
			if(getText(webElement).contains(groups)){
				click(grantedGroupsCheckboxList.get(grantedGroupsList.indexOf(webElement)));
				flag = true;
				break;
			}
		}
		click(CLICKABILITY, updateButton);
		return flag;
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
