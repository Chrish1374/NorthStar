package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.web.page.WebActions;

public class Tooltip extends WebActions{

	private WebDriver driver;

	public Tooltip(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;

	@FindBys(@FindBy(css="div.tooltip-inner"))
	public List<WebElement> tooltipList;
	
	public WebElement getTooltip(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return tooltip;		

	}
	
	public List<WebElement> getTooltips(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return tooltipList;		

	}
	
	
	public boolean verifyTooltipOnResize(WebElement element, int x, int y) throws InterruptedException {
		
		
		WebElement tooltip = getTooltip(element);		
		boolean status = isElementPresent(tooltip);
	
		int beforeHeight = getHeightOfWebElement(tooltip);
		int beforeWidth =getWidthOfWebElement(tooltip);
			
		Dimension dimension = new Dimension(x, y);
		String parentWindow = getCurrentWindowID();
		setWindowSize(parentWindow, dimension);
		
		tooltip = getTooltip(element);
		status = status && isElementPresent(tooltip);
		
		int afterHeight = getHeightOfWebElement(tooltip);
		int afterWidth =getWidthOfWebElement(tooltip);
		
		status = status && beforeHeight==afterHeight;
			
		float max =(float) (afterWidth+(afterWidth*0.01));
		float min =(float) (afterWidth-(afterWidth*0.01));
		if(min <= beforeWidth && beforeWidth <=max)
			status = status && true;
		
	
		maximizeWindow();	
		
		return status;
		
	}
	
	public boolean verifyTooltipOnDiffResizeLevels(WebElement element, int x1, int y1,int x2, int y2) throws InterruptedException {
		
		
				
		Dimension dimension = new Dimension(x1, y1);
		String parentWindow = getCurrentWindowID();
		setWindowSize(parentWindow, dimension);
		
		WebElement tooltip = getTooltip(element);		
		boolean status = isElementPresent(tooltip);
	
		int beforeHeight = getHeightOfWebElement(tooltip);
		int beforeWidth =getWidthOfWebElement(tooltip);
	
		maximizeWindow();	
		dimension = new Dimension(x2, y2);
		setWindowSize(parentWindow, dimension);
		
		tooltip = getTooltip(element);
		status = status && isElementPresent(tooltip);
		
		int afterHeight = getHeightOfWebElement(tooltip);
		int afterWidth =getWidthOfWebElement(tooltip);
		
		status = status && beforeHeight==afterHeight;
			
		float max =(float) (afterWidth+(afterWidth*0.01));
		float min =(float) (afterWidth-(afterWidth*0.01));
		if(min <= beforeWidth && beforeWidth <=max)
			status = status && true;
		
	
		maximizeWindow();	
		
		return status;
		
	}
	
}