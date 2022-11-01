package com.trn.ns.page.factory;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.ExtentManager;


public class BumpToolCircle extends ViewerPage {

	private WebDriver driver;

	public BumpToolCircle(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}


	public By bumpCircle = By.cssSelector("circle.bumpingAnnotation");

	public boolean enableBumpTool(int whichViewbox) {

		return enableBumpToolWithOffset(whichViewbox, 0, 0);

	}
	
	public boolean enableBumpToolWithOffset(int whichViewbox,int x, int y) {

		Actions action = new Actions(driver);
		action.moveToElement(getViewPort(whichViewbox)).moveByOffset(x, y).perform();
		action.keyDown(Keys.SHIFT).clickAndHold().perform();
		boolean status = verifyBumpCircle();
		action.release().keyUp(Keys.SHIFT).perform();
		return status;

	}
	
	public HashMap<String, Double> enableBumpTool(int whichViewbox, int xOffset, int yOffset) {

		Actions action = new Actions(driver);

		action.moveToElement(getViewPort(whichViewbox)).moveByOffset(xOffset, xOffset).perform();
		action.keyDown(Keys.SHIFT).clickAndHold().perform();
		WebElement bumpCircleElement = getElement(bumpCircle);
		
		HashMap<String,Double> bumpCircleProp = new HashMap<String,Double>();		
		bumpCircleProp.put(NSGenericConstants.R,Double.parseDouble(bumpCircleElement.getAttribute(NSGenericConstants.R)));
		bumpCircleProp.put(NSGenericConstants.CX,Double.parseDouble(bumpCircleElement.getAttribute(NSGenericConstants.CX)));
		bumpCircleProp.put(NSGenericConstants.CY,Double.parseDouble(bumpCircleElement.getAttribute(NSGenericConstants.CY)));
		
		action.release().keyUp(Keys.SHIFT).perform();
		
		return bumpCircleProp;
		

	}
	
	
	
	public boolean moveBumpTool(int whichViewbox,int xOffset, int yOffset) throws InterruptedException {

		return moveBumpTool(whichViewbox, 0, 0, xOffset, yOffset);

	}
	
	public boolean moveBumpTool(int whichViewbox,int xOffset, int yOffset, int toxOffset, int toyOffset) throws InterruptedException {

	
		Actions action = new Actions(driver);

		action.moveToElement(getViewPort(whichViewbox)).moveByOffset(xOffset, yOffset).perform();
		action.keyDown(Keys.SHIFT).clickAndHold().perform();	
		String radius = getAttributeValue(getElement(bumpCircle),NSGenericConstants.R);
		
		action.moveByOffset(toxOffset, toyOffset).perform();		
		boolean status = verifyBumpCircle();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the bump circle is present "+status);
		
		status = status && getAttributeValue(getElement(bumpCircle),NSGenericConstants.R).equals(radius);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the bump circle radius is same post movement "+status);
		
		action.release().keyUp(Keys.SHIFT).perform();
		waitForTimePeriod(2000);
			
		return status;

	}
	
	
	public boolean moveBumpTool(int whichViewbox,int[] coordinates) throws InterruptedException {

		Actions action = new Actions(driver);

		action.moveToElement(getViewPort(whichViewbox)).moveByOffset(coordinates[0], coordinates[1]).perform();
		action.keyDown(Keys.SHIFT).clickAndHold().perform();	
		String radius = getAttributeValue(getElement(bumpCircle),NSGenericConstants.R);
		
		for(int i =2 ;i<coordinates.length-1;) {
			action.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}
		
		boolean status = verifyBumpCircle();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the bump circle is present "+status);
		
		status = status && getAttributeValue(getElement(bumpCircle),NSGenericConstants.R).equals(radius);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the bump circle radius is same post movement "+status);
		
		action.release().keyUp(Keys.SHIFT).perform();
		waitForTimePeriod(2000);
			
		return status;

	}

	public boolean verifyBumpCircle() {

		boolean status = false;
		if(isElementPresent(bumpCircle)) {
			
			WebElement bumpCircleElement = getElement(bumpCircle);
			String strokeValue = getAttributeValue(bumpCircleElement,NSGenericConstants.STROKE);
			String fillValue = getAttributeValue(bumpCircleElement,NSGenericConstants.FILL);
			String strokeWidthValue = getAttributeValue(bumpCircleElement,NSGenericConstants.STROKE_WIDTH);
			String strokeOpacity = getAttributeValue(bumpCircleElement,NSGenericConstants.STROKE_OPACITY);
			String fillOpacity = getAttributeValue(bumpCircleElement,NSGenericConstants.FILL_OPACITY);
			
			status = strokeValue.equalsIgnoreCase(ViewerPageConstants.COLOUR_WHITE) ;
			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Verifying the bump circle stroke properties", "bump circle condition "+status);
			
			status = status && fillValue.equalsIgnoreCase(ViewerPageConstants.COLOUR_WHITE) ;
			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Verifying the bump circle Fill properties", "bump circle condition "+status);
			
			status = status && strokeWidthValue.equalsIgnoreCase(ViewerPageConstants.BUMP_CIRCLE_STROKE_OPACITY+NSGenericConstants.PIXEL_TEXT);
			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Verifying the bump circle stroke width properties", "bump circle condition "+status);
			
			status = status && strokeOpacity.equalsIgnoreCase(ViewerPageConstants.BUMP_CIRCLE_STROKE_OPACITY);
			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Verifying the bump circle stroke opacity properties", "bump circle condition "+status);
			
			status = status && fillOpacity.equalsIgnoreCase(ViewerPageConstants.BUMP_CIRCLE_FILL_OPACITY);
			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Verifying the bump circle fill opacity properties", "bump circle condition "+status);
			
		}
		
		return status;
	}


	




}






