package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.JavascriptExecutor;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.DataReader;

public class ViewerOrientation extends ViewerPage{

	private WebDriver driver;

	public ViewerOrientation(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	/* #########################################
	 *  Orientation markers
	 *  ########################################
	 */
	public WebElement getTopOrientationMarker(int viewNum){

		return getElement(By.xpath("//*[@id='0"+(viewNum-1)+"']"));
	}	

	public WebElement getBottomOrientationMarker(int viewNum){


		return getElement(By.xpath("//*[@id='1"+(viewNum-1)+"']"));
	}

	public WebElement getLeftOrientationMarker(int viewNum){

		return getElement(By.xpath("//*[@id='2"+(viewNum-1)+"']"));
	}

	public WebElement getOrientationMarker(int viewNum, String whichMarker){

		return getElement(By.xpath("//*[normalize-space(text())='"+whichMarker+"'][@fill='"+ViewerPageConstants.COLOUR_WHITE+"'][substring(@id,2,2)='"+(viewNum-1)+"']"));
	}


	public WebElement getRightOrientationMarker(int viewNum){

		return getElement(By.xpath("//*[@id='3"+(viewNum-1)+"']"));
	}

	public boolean verifyTopOrientationMarkerPresence(int viewNum){

		return isElementPresent(By.xpath("//*[@id='0"+(viewNum-1)+"']"));
	}	

	public boolean verifyBottomOrientationMarkerPresence(int viewNum){

		return isElementPresent(By.xpath("//*[@id='1"+(viewNum-1)+"']"));
	}	

	public boolean verifyLeftOrientationMarkerPresence(int viewNum){

		return isElementPresent(By.xpath("//*[@id='2"+(viewNum-1)+"']"));
	}	

	public boolean verifyRightOrientationMarkerPresence(int viewNum){

		return isElementPresent(By.xpath("//*[@id='3"+(viewNum-1)+"']"));
	}	
	
	// Counter-Clockwise Rotation Arrow for the Top Orientation Marker of the passed viewbox
	public WebElement topCounterClockwiseRotMarker(int viewboxNum) {
		String id = "4" + (viewboxNum-1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	public By topCounterClockwiseRotationMarker(int viewboxNum) {
		String id = "4" + (viewboxNum-1);
		return By.xpath("//*[@id='" + id + "']");
	}


	public WebElement topClockwiseRotMarker(int viewboxNum) {
		String id = "8" + (viewboxNum-1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	public By topClockwiseRotationMarker(int viewboxNum) {
		String id = "8" + (viewboxNum-1);
		return By.xpath("//*[@id='" + id + "']");
	}

	// Counter-Clockwise Rotation Arrow for the Bottom Orientation Marker of the passed viewbox
	public WebElement bottomCounterClockwiseRotationMarker(int viewboxNum) {
		String id = "6" + (viewboxNum-1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	// Clockwise Rotation Arrow for the Bottom Orientation Marker of the passed viewbox
	public WebElement bottomClockwiseRotationMarker(int viewboxNum) {
		String id = "10" + (viewboxNum -1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	//	// Left Orientation Marker for the passed viewbox
	//	public WebElement leftOrientationMarker(int viewboxNum) {
	//		String id = "2" + viewboxNum;
	//		return getElement(By.xpath("//*[@id='" + id + "']"));
	//	}

	// Counter-Clockwise Rotation Arrow for the Left Orientation Marker of the passed viewbox
	public WebElement leftCounterClockwiseRotationMarker(int viewboxNum) {
		String id = "7" + (viewboxNum -1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	public By leftCounterClockwiseRotMarker(int viewboxNum) {
		String id = "7" + (viewboxNum -1);
		return By.xpath("//*[@id='" + id + "']");
	}

	// Clockwise Rotation Arrow for the Left Orientation Marker of the passed viewbox
	public WebElement leftClockwiseRotationMarker(int viewboxNum) {
		String id = "11" + (viewboxNum -1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	public By leftClockwiseRotMarker(int viewboxNum) {
		String id = "11" + (viewboxNum -1);
		return By.xpath("//*[@id='" + id + "']");
	}
	//	// Right Orientation Marker for the passed viewbox
	//	public WebElement rightOrientationMarker(int viewboxNum) {
	//		String id = "3" + viewboxNum;
	//		return getElement(By.xpath("//*[@id='" + id + "']"));
	//	}

	// Counter-Clockwise Rotation Arrow for the Right Orientation Marker of the passed viewbox
	public WebElement rightCounterClockwiseRotationMarker(int viewboxNum) {
		String id = "5" + (viewboxNum -1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}
	public By rightCounterClockwiseRotMarker(int viewboxNum) {
		String id = "5" + (viewboxNum -1);
		return By.xpath("//*[@id='" + id + "']");
	}

	// Clockwise Rotation Arrow for the Top Orientation Marker of the passed viewbox
	public WebElement rightClockwiseRotationMarker(int viewboxNum) {
		String id = "9" + (viewboxNum -1);
		return getElement(By.xpath("//*[@id='" + id + "']"));
	}

	public By rightClockwiseRotMarker(int viewboxNum) {
		String id = "9" + (viewboxNum -1);
		return By.xpath("//*[@id='" + id + "']");
	}
	
		public void mouseHover(WebElement element) throws InterruptedException {

		mouseHover(CLICKABILITY, element);
	}

	/**
	 * @author jtranfaglia
	 * @param WebElement viewbox
	 * @param WebElement orientationMarker
	 * Description: This method will wait for the passed element for an orientation marker to be visible
	 * hover over it, then click on the passed orientation marker element. 
	 * It will then wait for the viewbox containing the orientation marker to be visible.
	 */	
	public void flipSeries(WebElement orientationMarker) {
		try {
			//			this.waitForElementVisibility(orientationMarker);
			this.mouseHover(orientationMarker);
			this.click(orientationMarker);
		} catch (InterruptedException e) {
			printStackTrace(e.getMessage());
		}
	}

	/**
	 * @author jtranfaglia
	 * @param WebElement viewbox
	 * @param WebElement orientationMarker
	 * @param WebElement rotationArrow
	 * Description: This method will wait for the passed element for an orientation marker to be visible
	 * hover over it, then click on the passed rotation arrow element. 
	 * It will then wait for the viewbox containing the orientation marker to be visible.
	 */	
	public void rotateSeries(WebElement orientationMarker, WebElement rotationArrow) {

		try {
			//			this.waitForElementVisibility(orientationMarker);
			this.mouseHover(orientationMarker);
			this.click(rotationArrow);
		} catch (InterruptedException e) {
			printStackTrace(e.getMessage());
		}
	}
	/**
	 * @author payal
	 * @param WebElement viewbox
	 * @param WebElement orientationMarker
	 * @param WebElement rotationArrow
	 * Description: This method will wait for an orientation marker to be visible
	 * hover on it and click on rotation arrow
	 */
	public void rotateSeries(WebElement orientationMarker, By rotationArrow) {

		try {

			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			((JavascriptExecutor) driver).executeScript(mouseOverScript,
					orientationMarker);
			this.mouseHover(orientationMarker);
			driver.findElement(rotationArrow).click();

		} catch (InterruptedException e) {
			printStackTrace(e.getMessage());
		}
	}

	
	public void mouseHover(By elementBy) throws InterruptedException {
		mouseHover(VISIBILITY, elementBy);
	}

	
	public WebElement getOrientation(String name,int viewNum){
		List<WebElement> elements =null;
		elements = driver.findElements(By.cssSelector("svg#svg-"+(viewNum-1)+" text[font-size='20px']["+NSGenericConstants.FILL+"='White']"));
		for(int i =0;i<elements.size();i++)
		{
			if(elements.get(i).getText().trim().equalsIgnoreCase(name))
			{	
				return elements.get(i);
			}
		}
		return null;
	}
	
	public boolean isOrientationDisplayed(int viewNum, String filePath) {
		Boolean status = false;

		String UpOrientation = DataReader.getSeriesDesc(PatientXMLConstants.UP_ORIENTATION_TEXTOVERLAY, "STUDY01","STUDY01_SERIES01", filePath);
		String DownOrientation = DataReader.getSeriesDesc(PatientXMLConstants.DOWN_ORIENTATION_TEXTOVERLAY, "STUDY01","STUDY01_SERIES01", filePath);
		String LeftOrientation = DataReader.getSeriesDesc(PatientXMLConstants.LEFT_ORIENTATION_TEXTOVERLAY, "STUDY01","STUDY01_SERIES01", filePath);
		String RightOrientation = DataReader.getSeriesDesc(PatientXMLConstants.RIGHT_ORIENTATION_TEXTOVERLAY, "STUDY01","STUDY01_SERIES01", filePath);

		if(getOrientation(UpOrientation,viewNum).isDisplayed() && getOrientation(DownOrientation,viewNum).isDisplayed() 
				&& getOrientation(LeftOrientation,viewNum).isDisplayed() && getOrientation(RightOrientation,viewNum).isDisplayed()){
			status = true;
		}
		return status;
	}

	/******************************
	 * ORIENTATION MARKERS 
	 ******************************
	 */
	
	public String getTopOrientationMarkerText(int viewNum){

		return getText(VISIBILITY,getElement(By.xpath("//*[@id='0"+(viewNum-1)+"']")));
	}

	public String getBottomOrientationMarkerText(int viewNum){


		return getText(VISIBILITY,getElement(By.xpath("//*[@id='1"+(viewNum-1)+"']")));
	}

	public String getLeftOrientationMarkerText(int viewNum){

		return getText(VISIBILITY,getElement(By.xpath("//*[@id='2"+(viewNum-1)+"']")));
	}

	public String getRightOrientationMarkerText(int viewNum){

		return getText(VISIBILITY,getElement(By.xpath("//*[@id='3"+(viewNum-1)+"']")));
	}

	public boolean verifyOrientationForPlane(int whichViewbox,String plane) {

		boolean status =true;
		plane = plane.toUpperCase();
		switch (plane) {
		case ViewerPageConstants.AXIAL_KEY:{

			status = status && getLeftOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.RIGHT_OR_OVERLAY);
			status = status && getRightOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.LEFT_OR_OVERLAY);
			status = status && getTopOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.ANTERIOR_OR_OVERLAY);
			status = status && getBottomOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.POSTERIOR_OR_OVERLAY);

		}
		break;
		case ViewerPageConstants.SAGITTAL_KEY:{

			status = status && getLeftOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.ANTERIOR_OR_OVERLAY);
			status = status && getRightOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.POSTERIOR_OR_OVERLAY);
			status = status && getTopOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.HEAD_OR_OVERLAY);
			status = status && getBottomOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.FOOT_OR_OVERLAY);
			
		}
		break;		
		case ViewerPageConstants.CORONAL_KEY:{

			status = status && getLeftOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.RIGHT_OR_OVERLAY);
			status = status && getRightOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.LEFT_OR_OVERLAY);
			status = status && getTopOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.HEAD_OR_OVERLAY);
			status = status && getBottomOrientationMarkerText(whichViewbox).equalsIgnoreCase(ViewerPageConstants.FOOT_OR_OVERLAY);

		}
		break;
		default:status=false;break;
		}
		
		return status;
	}

	
	
}