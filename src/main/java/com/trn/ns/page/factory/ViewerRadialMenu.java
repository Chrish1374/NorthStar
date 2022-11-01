package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.Utilities;

public class ViewerRadialMenu extends ViewerPage{

	private WebDriver driver;

	public ViewerRadialMenu(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	 /* Radial Menu components
	 * **********************
	 */
	 
	public By quickToolbar = By.cssSelector("#quickToolContainer");	
	
	@FindBy(css="#quickToolContainer")
	public WebElement quickToolbarMenu;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(1) ns-quick-toolbox-tile svg ")
	public WebElement scrollIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(2) ns-quick-toolbox-tile svg")
	public WebElement panIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(3) ns-quick-toolbox-tile svg")
	public WebElement zoomIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(4) ns-quick-toolbox-tile svg")
	public WebElement windowLevelingIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(5) ns-quick-toolbox-tile svg")
	public WebElement invertIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(6) ns-quick-toolbox-tile svg")
	public WebElement cinePlayIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(7) ns-quick-toolbox-tile svg")
	public WebElement cine4DPlayIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(8) ns-quick-toolbox-tile svg")
	public WebElement triangulationIcon;
	public void openQuickToolbar(WebElement viewbox) {
		performMouseRightClick(viewbox);
		waitForElementVisibility(quickToolbar);
	}

	public boolean verifyAllIconsPresenceInQuickToolbar(){
		boolean status = false ;

		if(isElementPresent(quickToolbar))
			if(panIcon.isDisplayed() && cinePlayIcon.isDisplayed() && windowLevelingIcon.isDisplayed() 
					&& zoomIcon.isDisplayed() && scrollIcon.isDisplayed())
				status = true;
		
		return status ;
	}

	public void selectScrollFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(cinePlayIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public void selectPanFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(panIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public void selectZoomFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(zoomIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public void selectWindowLevelFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(windowLevelingIcon);
		waitForElementInVisibility(quickToolbar);

	}	
	
	public void selectInvertFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(invertIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public void selectCineFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(cinePlayIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public void selectCine4DFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(cine4DPlayIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public boolean checkCurrentSelectedIcon(String iconName)
	{   
		boolean status = false;

		List<WebElement> elements = getElements(By.cssSelector(".defaultIcon.pushed div"));
		for (int i = 0; i < elements.size(); i++)
		{
			if(iconName.equalsIgnoreCase(elements.get(i).getAttribute("title"))){
				status=true;	   
				break;
			}
		}
		return status;
	}

	
	public Boolean isCineStopped(int ViewBoxNumber) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		int currentImage = getCurrentScrollPositionOfViewbox(ViewBoxNumber);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {
					return (currentImage!=getCurrentScrollPositionOfViewbox(ViewBoxNumber));}});
		}
		catch(TimeoutException e)
		{
			status=true;
		}
		return status;
	}

	public Boolean isCine4DStopped(int ViewBoxNumber) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		int currentImage = getValueOfCurrentPhase(ViewBoxNumber);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {
					return (currentImage!=getValueOfCurrentPhase(ViewBoxNumber));}});
		}
		catch(TimeoutException e)
		{
			status=true;
		}
		return status;
	}


	
}