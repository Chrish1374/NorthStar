package com.trn.ns.page.factory;

import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.page.constants.ViewerPageConstants;

public class ViewerLayout extends ViewerPage{

	private WebDriver driver;

	public ViewerLayout(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}



	public By iconGrid = By.cssSelector(" div.tool-bar-container div:nth-child(1) div span:nth-child(2)");
	public By gridLayoutBox= By.cssSelector("table.layoutSelectorTable");


	@FindBy(css="ns-tool-bar .tool-bar.ng-star-inserted div.tool-bar-container div:nth-child(1) div span:nth-child(2)")
	public WebElement gridIcon;

	@FindBy(css="div.ns-layoutselectorcontainer")
	public WebElement layoutContainer;	

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(1) > ns-icon svg path.iconColor")
	public WebElement oneByOneLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(2) > ns-icon svg path.iconColor")
	public WebElement twoByOneLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(3) > ns-icon svg path.iconColor")
	public WebElement oneByTwoLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(4) > ns-icon svg path.iconColor")
	public WebElement twoByTwoLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(12) > ns-icon svg path.iconColor")
	public WebElement threeByThreeLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(5) > ns-icon svg path.iconColor")
	public WebElement threeByTwoLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(6) > ns-icon svg path.iconColor")
	public WebElement twoByThreeLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(8) > ns-icon svg path.iconColor")
	public WebElement oneByOneLAndTwoByOneRLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(9) > ns-icon svg path.iconColor")
	public WebElement oneByOneTAndOneByTwoBLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(10) > ns-icon svg path.iconColor")
	public WebElement twoByOneLAndOneByOneRLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(7) > ns-icon svg path.iconColor")
	public WebElement oneByOneLAndThreeByOneRLayoutIcon;

	@FindBy(css="ns-layout-selector td:nth-child(1)  div:nth-child(11) > ns-icon svg path.iconColor")
	public WebElement threeByOneLAndOneByOneRLayoutIcon;


	@FindBys(@FindBy(xpath = "//div[@class='ns-layoutselectorcontainer']/div[1]/div"))
	public List<WebElement> numberOfRowsOfLayout;

	@FindBys(@FindBy(css = ".ns-layoutselectorcontainer div div:nth-child(1) span.layouttool"))
	public List<WebElement> numberOfLayoutIconInFirstRow;

	@FindBys(@FindBy(css = ".ns-layoutselectorcontainer div div:nth-child(2) span.layouttool"))
	public List<WebElement> numberOfLayoutIconInSecondRow;

	@FindBys(@FindBy(css = "div.layout-container div[class*='layout']"))
	public List<WebElement> totalNumberOfLayoutIcons;


	@FindBy(css="div.ns-layoutselectorcontainer div#saveWithContent")
	public WebElement saveLayoutWithContent;

	@FindBy(css="div.ns-layoutselectorcontainer div#saveWithOutContent")
	public WebElement saveLayoutOnly;

	@FindBy(css="div.ns-layoutselectorcontainer div#reset")
	public WebElement resetToDefaultLayout;

	@FindBys(@FindBy(css = "table.layoutSelectorTable tr td"))
	public List<WebElement> layoutOptionsContainer;
	
	
	public void selectLayout(WebElement layoutName) throws InterruptedException {

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		int canvasCountBeforeLayoutChange = getNumberOfCanvasForLayout();
		clickUsingAction(gridIcon);
		waitForElementVisibility(layoutName);
		waitForElementsVisibility(gridLayoutBox);
		click(layoutName);
		int canvasCountAfterLayoutChange=canvasCountBeforeLayoutChange;
		int counter = 0;
		do{
			canvasCountAfterLayoutChange = getNumberOfCanvasForLayout();
			waitForTimePeriod(500);
			counter++;

			if(counter>30)
				break;

		}while(canvasCountBeforeLayoutChange==canvasCountAfterLayoutChange);
		waitForAllImagesToLoad();
	}

	public void selectLayoutWithSaveByDefault(WebElement layoutName) throws InterruptedException {

		selectLayout(layoutName);		
		chooseOptionsForSaveOrResetLayout(ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT);
	}


	public int getExpectedNumberOfCanvasForLayout(String layout){
		int x1 = 0, y1 = 0;
		int x2, y2 = 0;
		if(layout.contains("icn-"))
			layout=layout.replace("icn-", "").trim();

		x2 = Character.getNumericValue(layout.charAt(0));
		y2 = Character.getNumericValue(layout.charAt(2));

		if (layout.contains("T")||layout.contains(ViewerPageConstants.LEFT_OR_OVERLAY)) {

			x1 = Character.getNumericValue(layout.charAt(5));
			y1 = Character.getNumericValue(layout.charAt(7));
		}
		return (x1*y1)+(x2*y2);
	}

	
	public void openLayoutContainer() throws InterruptedException {
		clickUsingAction(gridIcon);
		waitForElementVisibility(gridLayoutBox);
	}

	public void closeLayoutContainer() throws InterruptedException {
		click(EurekaLogo);
		waitForElementInVisibility(gridLayoutBox);
	}
	
	public boolean verifyBannerMsgUponLayoutChange(String username, String machineDesc) throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		List<String> machineName=db.getMachineName(machineDesc);
		boolean status = false;

		for(int i =0 ;i<machineName.size();i++)
		{
			if(getText(notificationMessage.get(0)).equals(ViewerPageConstants.BANNER_MESSAGE_WHEN_LAYOUT_SAVED +username+" for machine "+ machineName.get(i))){
				status = true;
				break;
			}
		}
		return status;


	}

	public void chooseOptionsForSaveOrResetLayout(String option) throws InterruptedException
	{
		openLayoutContainer();
		switch(option){

		case ViewerPageConstants.SAVE_LAYOUT_WITH_CONTENT:
			click(saveLayoutWithContent);	
			break;

		case ViewerPageConstants.SAVE_LAYOUT_WITHOUT_CONTENT: 
			click(saveLayoutOnly);
			break;

		case ViewerPageConstants.RESET_LAYOUT: 
			click(resetToDefaultLayout); 
			waitForTimePeriod(6000);
			break;

		}
		waitForElementVisibility(notificationDiv);

	}


	public List<WebElement> layoutOptionsbox(int optionbox){
		
		 return getElements(By.cssSelector("div.layout-container div[class*='layout']:nth-child("+optionbox+") g path"));
	}

}