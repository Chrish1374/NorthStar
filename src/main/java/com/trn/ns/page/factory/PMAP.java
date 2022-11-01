package com.trn.ns.page.factory;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;


public class PMAP extends ViewerPage{

	private WebDriver driver;

	public PMAP(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	
	public By lutbar = By.cssSelector("#viewbox-0 > ns-pmap-gradient-bar > div > div.ns-bar-labels span");
	
	@FindBy(css="div.row.templateHeader:last-child")
	public WebElement resetButton;	

	@FindBy(css=".ps-content div.ns-gradientselector-container")
	public WebElement lutColorContainer;

	@FindBys(@FindBy(css ="div.row.templateHeader"))
	public List<WebElement> lutColorTemplates;

	@FindBys(@FindBy(css ="div.ns-gradientselector-container>div"))
	public List<WebElement> lutColorTemplatesRow;
	
	

	public boolean verifyMaxMinValueForLutBar(int viewNum, int Max, int Middle, int Min){
		boolean status =false;

		status=getMaxMinAndMiddleValueFromLUTBar(viewNum, ViewerPageConstants.MAX)==Max;
		status=status && getMaxMinAndMiddleValueFromLUTBar(viewNum, ViewerPageConstants.MIDDLE)==(Middle);
		status=status && getMaxMinAndMiddleValueFromLUTBar(viewNum, ViewerPageConstants.MIN)==(Min);

		return status;

	}

	public boolean verifyPresenceOfGradientBar(int viewNum){

		return isElementPresent(By.cssSelector("#viewbox-"+(viewNum-1)+" > ns-pmap-gradient-bar > div"));	
	}	

	public int getMaxMinAndMiddleValueFromLUTBar(int viewNum,String MaxOrMinOrMiddle ){

		int value = 0;
		switch(MaxOrMinOrMiddle)
		{

		case ViewerPageConstants.MAX :	
			value=convertIntoInt(getText(getMaxMinValueForLUTBar(viewNum).get(0)));
			break;


		case ViewerPageConstants.MIDDLE :
			value=convertIntoInt(getText(getMaxMinValueForLUTBar(viewNum).get(1)));
			break;


		case ViewerPageConstants.MIN :
			value=convertIntoInt(getText(getMaxMinValueForLUTBar(viewNum).get(2)));
			break;

		}

		return value;

	}


public boolean verifyNoContentSpecifiedIsDisplayed(int whichViewbox) throws InterruptedException {

		boolean status = false;

		WebElement element = getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" ns-textoverlay svg"));
		if(Integer.parseInt(element.getAttribute(NSGenericConstants.HEIGHT)) > 0 && Integer.parseInt(element.getAttribute(NSGenericConstants.WIDTH))>0)
			status = true;

		return status;
	}

	public WebElement getGradientBar(int viewNum){
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+" > ns-pmap-gradient-bar > div"));
	}	

	public List<WebElement> getMaxMinValueForLUTBar(int viewNum){
		return getElements(By.cssSelector("#viewbox-"+(viewNum-1)+" > ns-pmap-gradient-bar > div > div.ns-bar-labels span"));
	}	
	
	
	public WebElement getGradientColor(int viewNum){
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+" div.ns-gradient-bar"));
	}

	public boolean verifyColorSelectedOnPmap(int viewNum, String color) throws InterruptedException{

		openOrCloseLutBar(viewNum, NSGenericConstants.OPEN);

		for(int i=0; i<lutColorTemplatesRow.size();i++){

			if(getAttributeValue(lutColorTemplatesRow.get(i),(NSGenericConstants.CLASS_ATTR)).contains(NSGenericConstants.SELECTEDCOLORTEMPLATE)&getText((lutColorTemplates).get(i)).contains(color))
			{

				openOrCloseLutBar(viewNum, NSGenericConstants.CLOSE);
				return true;

			}
		}
		return false;

	}

	public void openOrCloseLutBar(int viewNum,String openOrClose) throws InterruptedException {

		switch(openOrClose) {
		case NSGenericConstants.OPEN:
		{
			mouseHover(getGradientBar(viewNum));
			click(getGradientBar(viewNum));
			waitForElementVisibility(lutColorContainer);
			break;
		}

		case NSGenericConstants.CLOSE: 
		{
			click(getViewPort(viewNum));
			waitForElementVisibility(getGradientBar(viewNum));
			break;
		}

		}

	}

	public void selectColorFromLUT(int viewNum,String color) throws InterruptedException
	{

		openOrCloseLutBar(viewNum,NSGenericConstants.OPEN);
		WebElement ele =getElement(By.xpath("//div[normalize-space(text())='"+color+"']"));
		click(ele);
		waitForTimePeriod(2000);
		waitForViewerpageToLoad();


	}
	
}