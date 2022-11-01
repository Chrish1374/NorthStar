package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;

public class ViewerToolbar extends ViewerPage{

	private WebDriver driver;

	public ViewerToolbar(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	public By moreViewerIcon = By.cssSelector("#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span ");

	
	@FindBy(css="div.tool-bar.ng-star-inserted")
	public WebElement viewerToolbar; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
	public WebElement lineIcon; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)")
	public WebElement circleIcon; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(3)")
	public WebElement textAnnotationIcon;

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(4)")
	public WebElement distanceIcon;	

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(5)")
	public WebElement pointIcon;

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(6)")
	public WebElement ellipseIcon ;

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span:nth-child(7)")
	public WebElement polylineIcon;   

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span:nth-child(1)")
	public WebElement zoomIcon; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span:nth-child(2)")
	public WebElement panIcon; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span:nth-child(3)")
	public WebElement windowWidthIcon; 

	@FindBy(css = "#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span:nth-child(4)")
	public WebElement scrollIcon; 

	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;

	@FindBy(css="#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span > ns-tool-bar-tile > div")
	public WebElement moreIcon;

	@FindBys(@FindBy(css="#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(1) > span > ns-tool-bar-tile > div"))
	public List<WebElement> allToolIcons;

	@FindBys(@FindBy(css="#viewer div.tool-bar.ng-star-inserted > div > div:nth-child(2) > div:nth-child(2) > span > ns-tool-bar-tile > div"))
	public List<WebElement> allViewerIcons;

	// this works only when browser size is 650 x 700

	@FindBys(@FindBy(css="#viewer ns-more-tools  ns-more-tile div"))
	public List<WebElement> allViewerIconsInMoreTile;
	
	@FindBys(@FindBy(css="#viewer ns-more-tools  ns-more-tile div.inactive-icon svg"))
	public List<WebElement> allInActiveIconsInMoreTile;
	
	@FindBys(@FindBy(css="#viewer ns-more-tools  ns-more-tile div.inactive-icon rect"))
	public List<WebElement> allInactiveRectIconsInMoreTile;
	
	@FindBy(css="#viewer ns-more-tools  ns-more-tile div.active-icon rect")
	public WebElement activeIconInMoreTile;
	


	@FindBy(css ="#viewer ns-more-tools > div")
	public WebElement morePopup;
	
	public WebElement getActiveIconInMoreTile() {
		
		return getElement(By.cssSelector("#viewer ns-more-tools  ns-more-tile div.active-icon"));
	}
	
	public void selectIconFromViewerToolbar(WebElement icon) throws InterruptedException
	{
		click(icon);
		waitForTimePeriod(500);
	}

	public boolean checkCurrentSelectedIconOnViewer(String iconName) throws InterruptedException
	{   
		boolean status = false;
		WebElement icon=null;
		if(getElements(By.cssSelector("#viewer  div.tool-bar.ng-star-inserted ns-tool-bar-tile > div.active-icon")).size()>0) {
			icon = getElement(By.cssSelector("#viewer div.tool-bar.ng-star-inserted  ns-tool-bar-tile > div.active-icon"));
			String text = getText(icon.findElement(By.cssSelector("section.tool-title")));
			status = iconName.equalsIgnoreCase(text);
		}
		else {
			click(moreIcon);
			mouseHover(activeIconInMoreTile);
			status = getText(tooltip).equals(iconName);

		}


		return status;
	}

	public boolean checkCurrentSelectedIconOnViewer(WebElement icon)
	{   
		boolean status = false;

		status =isElementPresent(icon.findElement(By.cssSelector("div.active-icon")));

		return status;
	}

	public String getTooltip(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return getText(tooltip);		

	}

	public boolean verifyViewerToolIconVisibility(WebElement icon, String theme) {

		WebElement path = icon.findElement(By.cssSelector("g path"));
		WebElement rect = icon.findElement(By.cssSelector("rect"));

		String backgroundColor = getCssValue(rect, NSGenericConstants.FILL);

		String toolColor = "";
		boolean status= false;

		if(!getCssValue(path, NSGenericConstants.FILL).equals(NSGenericConstants.DISPLAY_NONE_VALUE))
			toolColor = getCssValue(path, NSGenericConstants.FILL);
		else
		{
			path = icon.findElement(By.cssSelector("g.color-front"));
			toolColor =  getCssValue(path, NSGenericConstants.STROKE);
		}


		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = backgroundColor.equals(ThemeConstants.EUREKA_ICON_BACKGROUND_COLOR);
			status = status && toolColor.equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
		}else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = backgroundColor.equals(ThemeConstants.DARK_ICON_BACKGROUND_COLOR);
			status = status && toolColor.equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
		}

		return status;

	}

	public boolean verifyViewerToolIsSelected(WebElement icon, String theme) {

		WebElement path = icon.findElement(By.cssSelector("g path"));
		WebElement rect = icon.findElement(By.cssSelector("rect"));

		String backgroundColor = getCssValue(rect, NSGenericConstants.FILL);

		String toolColor = "";
		boolean status= false;

		if(!getCssValue(path, NSGenericConstants.FILL).equals(NSGenericConstants.DISPLAY_NONE_VALUE))
			toolColor = getCssValue(path, NSGenericConstants.FILL);
		else
		{
			path = icon.findElement(By.cssSelector("g.color-front"));
			toolColor =  getCssValue(path, NSGenericConstants.STROKE);
		}

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = backgroundColor.equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			status = status && toolColor.equals(ThemeConstants.EUREKA_ICON_BACKGROUND_COLOR);
		}else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = backgroundColor.equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			status = status && toolColor.equals(ThemeConstants.DARK_ICON_BACKGROUND_COLOR);
		}
		return status;

	}
	
	public boolean verifyIconsDisableForNonDicomInViewerToolbar(int viewbox){
		boolean status=false;
		click(getViewPort(viewbox));

		if(isElementPresent(viewerToolbar))
		{
			status=!(getAttributeValue(panIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status&& !(getAttributeValue(zoomIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && !(getAttributeValue(scrollIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			
			status=status && (getAttributeValue(windowWidthIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(pointIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(circleIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(ellipseIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(textAnnotationIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(distanceIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(lineIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(polylineIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
		}
		return status ;
	}
	
	
	
}