package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;

public class SimpleLine extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public SimpleLine(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	public void selectLineFromQuickToolbar(WebElement viewbox) throws InterruptedException{
		openQuickToolbar(viewbox);
		waitForElementVisibility(lineIcon);
		click(lineIcon);
		waitForElementInVisibility(quickToolbar);
		
	}
	
	public void selectLineFromQuickToolbar(int viewbox) throws InterruptedException{
		selectLineFromQuickToolbar(getViewPort(viewbox));
	}


	//get selected and non selected all lines
	public List<WebElement> getAllLines(int whichViewbox){

		List<WebElement> line = new ArrayList<WebElement>();

		List<WebElement> list = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g[ns-line]"));


		for(int i =0 ;i<list.size();i++){
			try {
				if(!isElementPresent(list.get(i).findElement(By.cssSelector("text"))))
					line.add(list.get(i));

			}catch(NoSuchElementException e) {

				line.add(list.get(i));
				continue;
			}

		}

		return line;


	}

	public void moveLine(int whichViewbox, int whichLine,int xOffset, int yOffset) throws InterruptedException{

		Actions action = new Actions(driver);
		action.clickAndHold(getAllLines(whichViewbox).get(whichLine-1)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);

	}

	public void drawLine(int whichViewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws TimeoutException, InterruptedException {

		dragAndReleaseOnViewer(getViewPort(whichViewbox), from_xOffset, from_yOffset, to_xOffset, to_yOffset);
	}

	public List<WebElement> getLine(int whichViewbox, int whichLine){


		List<WebElement> line = getAllLines(whichViewbox);
		List<WebElement> myLine = new ArrayList<WebElement>();

		if(line.size()>0)
			myLine=line.get(whichLine-1).findElements(By.cssSelector("line"));

		return myLine;
	}

	//	Get All Line in passed viewbox
	public List<WebElement> getLines(int whichViewbox){

		//		return getLinesOrPoints(whichViewbox, true, false);

		return getAllLines(whichViewbox);

	}

	/**
	 * @author payal
	 * @param Viewbox number
	 * @return: null
	 * Description: This method return true if line is present
	 * 	 */
	public boolean isLinesPresent(int whichViewbox){
		boolean status = false;
		try{	
			if(getLines(whichViewbox).size()>0)
				status = true;
		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}

	public boolean isLineSelected(int whichViewbox, int whichLine){
		boolean status = false ;

		List<WebElement> line = getLinesOrPoints(whichViewbox, true, false);
		List<WebElement> myLine = new ArrayList<WebElement>();

		for(int i =0 ;i<line.size();i++){

			try {	

				if(isElementPresent(line.get(i).findElement(By.cssSelector("g text"))))
					continue;
			}	
			catch(NoSuchElementException e)
			{
				myLine.add(line.get(i));
			}
		}
		try
		{
			if(isElementPresent(myLine.get(whichLine-1).findElement(By.cssSelector("g circle+ circle"))))
				status=true;
		}catch(NoSuchElementException e)
		{

		}

		return status;
	}

	public List<WebElement> getLineHandles(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g line + line +  g > circle+circle"));
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number and handle number of selected line
	 * @return: boolean
	 * Description: This method is used to check whether line handle disappear on resizing. 
	 */ 
	public boolean verifyLineHandleDisappearOnResizing(int whichViewbox,int handleNumber,int xOffset, int yOffset) throws InterruptedException{

		boolean status = false;
		Actions action = new Actions(driver);
		action.clickAndHold(getResizeHandleForSelectedLinearMeasurement(whichViewbox,handleNumber));
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(100);
		action.moveByOffset(xOffset, yOffset);
		try {
			status =getResizeHandleForSelectedLinearMeasurement(whichViewbox,handleNumber).isDisplayed();
		}

		catch(NoSuchElementException e)
		{
			status=true;
		}
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(100);
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);
		return status;
	}


	/**
	 * @author Vivek
	 * @param  Viewbox Number and handle number od selected line
	 * @return: boolean
	 * Description: This method is used to check whether line handle disappear on resizing. 
	 */ 
	public boolean verifyLineHandleDisappearOnDrawing(int whichViewbox,int handleNumber,int from_xOffset, int from_yOffset ,int xOffset, int yOffset) throws InterruptedException{

		mouseHover(VISIBILITY, getViewPort(whichViewbox), from_xOffset, from_yOffset);
		boolean status = false;
		Actions action = new Actions(driver);
		action.clickAndHold().perform();
		LOGGER.info("drag and drop...clickAndHold");
		action.moveByOffset(xOffset, yOffset).perform();
		waitForTimePeriod(100);
		LOGGER.info("drag and drop...moveByOffset");
		//Check that opposite handle handle is visible on DOM while user is still drawing 
		status=isElementPresent(By.cssSelector("#svg-"+(whichViewbox-1)+" > * > g > g > circle:nth-child("+(handleNumber)+")"));
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		return status;
	}

	//Get all the resize handle for selected linear measurement. This handle is used for resising ruler
	public WebElement getResizeHandleForSelectedLinearMeasurement(int whichViewbox, int resizeNumber){
			return driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g[ns-line] circle:nth-of-type("+(resizeNumber+1)+")"));

	}

	/**
	 * @author Payal
	 * @param  Viewbox Number and line Number as per as occurence or drawn
	 * @return: void 
	 * Description: This method is used to select line on Viewbox
	 * @throws InterruptedException 
	 */
	public void selectLine(int whichViewbox, int whichLine) throws InterruptedException{	

		List<WebElement> lines = getAllLines(whichViewbox);

		Actions action = new Actions(driver);
		action.moveToElement(getViewPort(whichViewbox)).moveToElement(lines.get(whichLine-1)).click().build().perform();

		waitForTimePeriod(2000);
		action.moveByOffset(2, 2).perform();
		LOGGER.info("Move...click");


	}
	
	
	public void selectLine(int whichViewbox, int whichLine, int x, int y) throws InterruptedException{	

		List<WebElement> lines = getAllLines(whichViewbox);

		Actions action = new Actions(driver);
		action.moveToElement(getViewPort(whichViewbox)).moveToElement(lines.get(whichLine-1),x,y).click().build().perform();

		waitForTimePeriod(2000);
		action.moveByOffset(2, 2).perform();
		LOGGER.info("Move...click");


	}

	public List<WebElement> getLinesOrPoints (int whichViewbox , boolean lineBoolean , boolean pointBoolean){

		List<WebElement> line = new ArrayList<WebElement>();
		List<WebElement> list = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g > g > g"));

		for(int i =0 ;i<list.size();i++){
			try{
				if(list.get(i).findElements(By.cssSelector("g > line:first-child+line:last-child")).size()>0) 
					line.add(list.get(i)); 


				else if(list.get(i).findElements(By.cssSelector("line:nth-of-type(2):last-of-type + circle")).size()>0)
					line.add(list.get(i));

			}catch(NoSuchElementException e){
				continue;
			}
		}

		return line;


	}

	public boolean verifyLineAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichMeasurement){

		return verifyLineIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyLinesAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichMeasurement){

		return verifyLineIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public boolean verifyLinesAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichMeasurement){

		return verifyLineIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public boolean verifyLineAnnotationIsRejectedGSPS(int whichViewbox, int whichMeasurement){
		return verifyLineIsInActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		

	}

	public boolean verifyLineAnnotationIsAcceptedGSPS(int whichViewbox, int whichMeasurement){

		return verifyLineIsInActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public void resizeSelectedLinearMeasurement(int whichViewbox,int handleNumber,int xOffset, int yOffset) throws InterruptedException{


		Actions action = new Actions(driver);
		action.clickAndHold(getResizeHandleForSelectedLinearMeasurement(whichViewbox,handleNumber)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(2000);
	}

	public void deleteSelectedLine() throws InterruptedException {

		Actions action = new Actions(driver);
		action.sendKeys(Keys.DELETE).build().perform();
		waitForTimePeriod(4000);
	}

	public void deleteLine(int whichViewbox, int whichLine) throws TimeoutException, InterruptedException{

		selectLine(whichViewbox,whichLine);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(2000);
	}

	public boolean verifyLineIsActiveGSPS(int whichViewbox, int whichMeasurement,String whichColor,String opacity) {

		boolean status = false;
		try{
			WebElement line =getAllLines(whichViewbox).get(whichMeasurement-1);

			status = isElementPresent(line.findElement(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+whichColor+"']")));
			status = status && isElementPresent(line.findElement(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));

			status = getAttributeValue(line.findElement(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+whichColor+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			status = getAttributeValue(line.findElement(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;

	}

	public boolean verifyLineIsInActiveGSPS(int whichViewbox, int whichMeasurement,String whichColor,String opacity) {

		boolean status = false;
		WebElement measurement =getLine(whichViewbox, whichMeasurement).get(0);
		status = getAttributeValue(measurement,NSGenericConstants.STROKE).equals(whichColor);		
		status = status && measurement.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
		status = status && getAttributeValue(measurement,NSGenericConstants.STROKE_OPACITY).equals(opacity);

		return status;

	}
	
	
}
