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

public class MeasurementWithUnit extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public MeasurementWithUnit(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	// Measurement with unit
	public void selectDistanceFromQuickToolbar(int whichViewbox){
		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(distanceIcon);
		click(distanceIcon);
		waitForElementInVisibility(quickToolbar);
	}


	public void drawLine(int whichViewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws TimeoutException, InterruptedException {

		mouseHover(getViewPort(whichViewbox));		
		waitForTimePeriod(1000);
		Actions action = new Actions(driver);

		action.moveToElement(getViewPort(whichViewbox)).moveByOffset(from_xOffset, from_yOffset).clickAndHold().build().perform();
		LOGGER.info("drag and drop...clickAndHold");

		action.moveByOffset(to_xOffset, to_yOffset).perform();
		waitForTimePeriod(100);
		LOGGER.info("drag and drop...moveByOffset");

		action.release().moveByOffset(1, 1).build().perform();
		waitForTimePeriod(2000);
		LOGGER.info("drag and drop...completed");


	}

	public void drawLineQuickVersion(int whichViewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws TimeoutException, InterruptedException {

		dragAndReleaseOnViewerQuickVersion(getViewPort(whichViewbox), from_xOffset, from_yOffset, to_xOffset, to_yOffset);
	}

	public List<WebElement> getAllLinearMeasurements(int whichViewbox){

		ArrayList<WebElement> listOfMeasurements = new ArrayList<WebElement>();
		List<WebElement> list = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g[ns-line]"));

		for(int i =0 ;i<list.size();i++){
			try{

				if(list.get(i).findElements(By.cssSelector("line:not([stroke-dasharray='none'])[stroke-opacity]")).size()>0)
					//if(isElementPresent(list.get(i).findElement(By.cssSelector("g:nth-child(2) > text + line"))))
					listOfMeasurements.add(list.get(i));

			}catch(NoSuchElementException e){
				continue;
			}
		}
		return listOfMeasurements;
	}

	//	Get Lines of Linear measurement
	public List<WebElement> getLinearMeasurements(int whichViewbox,int whichMeasurement){

		List<WebElement> measurements = new ArrayList<WebElement>();
		List<WebElement> elements = getAllLinearMeasurements(whichViewbox);

		if(isElementPresent(elements.get(whichMeasurement-1).findElement(By.cssSelector("text + line"))))
			measurements.addAll(elements.get(whichMeasurement-1).findElements(By.cssSelector("line")));

		return measurements;
	}

	//Get All the text of linear Measurements on Viewbox
	public List<WebElement> getLinearMeasurementsText(int whichViewbox){

		List<WebElement> measurementsText = new ArrayList<WebElement>();
		List<WebElement> elements = getAllLinearMeasurements(whichViewbox);

		for(int i =0 ;i<elements.size();i++){
			try{
				measurementsText.add(elements.get(i).findElement(By.cssSelector("text:nth-of-type(2)")));
			}catch(NoSuchElementException e){
				continue;
			}
		}
		return measurementsText;
	}

	public List<WebElement> getLinearMeasurementsBorderText(int whichViewbox){

		List<WebElement> measurementsText = new ArrayList<WebElement>();
		List<WebElement> elements = getAllLinearMeasurements(whichViewbox);

		for(int i =0 ;i<elements.size();i++){
			try{
				measurementsText.add(elements.get(i).findElement(By.cssSelector("text:nth-child(1)")));
			}catch(NoSuchElementException e){
				continue;
			}
		}
		return measurementsText;
	}

	public boolean isLinearMeasurementPresent(int whichViewbox){
		boolean status = false;
		try{	
			if(getAllLinearMeasurements(whichViewbox).size()>0)
				status = true;
		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}

	public boolean verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(int whichViewbox, int whichMeasurement){

		return verifyLinearMeasurementIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		
	} 

	public boolean verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(int whichViewbox, int whichMeasurement){
		
		return verifyLinearMeasurementIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	
	} 

	public boolean verifyLinearMeasurementAnnotationIsActivePendingGSPS(int whichViewbox, int whichMeasurement){

		return verifyLinearMeasurementIsActiveGSPS(whichViewbox, whichMeasurement, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyLinearMeasurementAnnotationIsRejectedGSPS(int whichViewbox, int whichMeasurement){

		boolean status = false;
		List<WebElement> measurement =getLinearMeasurements(whichViewbox,whichMeasurement);
		boolean condition_color = measurement.get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_COLOR)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.FILL_COLOR_HANDLER) ;

		boolean condition_width = measurement.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH)&& 
				measurement.get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);


		if((measurement.size()==3) && condition_color && condition_width)
			status=true;

		return status;
	}

	public boolean verifyLinearMeasurementAnnotationIsAcceptedGSPS(int whichViewbox, int whichMeasurement){

		boolean status = false;
		List<WebElement> measurement =getLinearMeasurements(whichViewbox,whichMeasurement);
		boolean condition_color = getLinearMeasurements(whichViewbox,whichMeasurement).get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.ACCEPTED_COLOR)&&
				getLinearMeasurements(whichViewbox,whichMeasurement).get(1).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.FILL_COLOR_HANDLER) ;

		boolean condition_width = getLinearMeasurements(whichViewbox,whichMeasurement).get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH)&& 
				getLinearMeasurements(whichViewbox,whichMeasurement).get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);


		if((measurement.size()==3) && condition_color && condition_width)
			status=true;

		return status;
	} 

	public boolean verifyLinearMeasurementAnnotationIsPendingGSPS(int whichViewbox, int whichMeasurement){

		boolean status = false;
		List<WebElement> measurement =getLinearMeasurements(whichViewbox,whichMeasurement);
		boolean condition_color = measurement.get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.FILL_COLOR_HANDLER) ;

		boolean condition_width = measurement.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH)&& 
				measurement.get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);


		if((measurement.size()==3) && condition_color && condition_width)
			status=true;

		return status;
	} 



	public void selectLinearMeasurement(int whichViewbox, int whichLinearMeasurement) throws TimeoutException, InterruptedException{
		selectLinearMeasurementWithLeftClick(whichViewbox, whichLinearMeasurement);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectLinearMeasurementWithLeftClick(int whichViewbox, int whichLinearMeasurement) throws TimeoutException, InterruptedException{
		mouseHoverWithClick(PRESENCE,getLinearMeasurements(whichViewbox, whichLinearMeasurement).get(0));
		waitForTimePeriod(2000);
	}


	public boolean isLinearMeasurementSelected(int whichViewbox, int whichLinearMeasurement){
		boolean status = false ;
		List<WebElement> allMeasurements = getAllLinearMeasurements(whichViewbox);

		try{
			if(isElementPresent(allMeasurements.get(whichLinearMeasurement-1).findElement((By.cssSelector("g:nth-child(3) g:nth-child(2) circle+circle"))))){

				status = true;
			}



		}
		catch(NoSuchElementException e){
			printStackTrace(e.getMessage());
		}

		return status;
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number, Linear measurement text as per as occurence and co-rodinate
	 * @return: void
	 * Description: This method is used to move selected linear measurement text on Viewbox
	 */ 
	public void moveLinearMeasurementText(int whichViewbox, int whichMeasurement,int xOffset, int yOffset) throws InterruptedException{


		Actions action = new Actions(driver);
		action.clickAndHold(getLinearMeasurementsText(whichViewbox).get(whichMeasurement-1)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(2000);

	}


	/**
	 * @author Vivek
	 * @param  Viewbox Number, Linear measurement ruler as per as occurence and co-rodinate
	 * @return: void
	 * Description: This method is used to move selected linear measurement ruler text on Viewbox
	 */ 
	public void moveLinearMeasurement(int whichViewbox, int whichMeasurement,int xOffset, int yOffset) throws InterruptedException{

		Actions action = new Actions(driver);
		action.clickAndHold(getLinearMeasurements(whichViewbox,whichMeasurement).get(0)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(3000);

	}


	public void deleteSelectedMeasurement() throws InterruptedException{
		
		Actions action = new Actions(driver);
		action.sendKeys(Keys.DELETE).perform();
		waitForTimePeriod(4000);
	}

	public void deleteLinearMeasurement(int whichViewbox, int whichPoint) throws TimeoutException, InterruptedException{
		selectLinearMeasurement(whichViewbox,whichPoint);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(4000);
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number and slice Number
	 * @return: void
	 * Description: This function is used to verify accuracy of a linear measurement with threshold of 1%
	 */ 
	public boolean verifyAccuracyOfLinearMeasurement(int viewbox,int whichMeasurement,String value) throws InterruptedException{

		boolean status = false;
		String[] tokens = getLinearMeasurementsText(viewbox).get(whichMeasurement-1).getText().split(" ");
		LOGGER.info("Measurement unit = "+ getLinearMeasurementsText(viewbox).get(whichMeasurement-1).getText());
		float actual =Float.parseFloat(tokens[0]);
		float expected= Float.parseFloat(value);
		float max =(float) (expected+(expected*0.01));
		float min =(float) (expected-(expected*0.01));
		if(min <= actual && actual <=max)
			status=true;
		return status;

	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number and handle number
	 * @return: void
	 * Description: This method is used to resize linear measurement rule on Viewbox on both direction
	 */ 
	public void resizeSelectedLinearMeasurement(int whichViewbox,int handleNumber,int xOffset, int yOffset) throws InterruptedException{


		Actions action = new Actions(driver);
		action.moveToElement(getResizeHandleForSelectedLinearMeasurement(whichViewbox,handleNumber)).clickAndHold().moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(3000);
	}

	//Get all the resize handle for selected linear measurement. This handle is used for resising ruler
	public WebElement getResizeHandleForSelectedLinearMeasurement(int whichViewbox, int resizeNumber){

		// return  getAllLinearMeasurements(whichViewbox).get(0).findElement(By.cssSelector("circle.handlerPoint:nth-of-type("+(resizeNumber+1)+")"));
		return  getAllLinearMeasurements(whichViewbox).get(0).findElement(By.cssSelector("circle:nth-of-type("+(resizeNumber+1)+")"));

	}

	public List<WebElement> getTextCommentAddedForLinearMeasurement(int whichViewbox) {

		List<WebElement> measurementsText = new ArrayList<WebElement>();
		List<WebElement> elements = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-line]"));

		for(int i =0 ;i<elements.size();i++){
			try{
				if(isElementPresent(elements.get(i).findElement(By.cssSelector("g > text + line")))) {
					measurementsText.add(elements.get(i).findElement(By.cssSelector("g > text + text:last-child >tspan")));
				}
			}catch(NoSuchElementException e){
				continue;
			}
		}
		return measurementsText;


	}




	public void openGSPSRadialMenu(WebElement gspsObject)  {

		try {
			performGSPSOperations(gspsObject, gspsAccept, false);
		} catch (InterruptedException e) {
			printStackTrace(e.getMessage());
		}
	}

	public void selectRejectfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{
		performGSPSOperations(gspsObject, gspsReject, true);
	}

	// Select Previous from Accept/Reject radial Menu
	public void selectPreviousfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{

		performGSPSOperations(gspsObject, gspsPrevious, true);
	}

	// Select Next from Accept/Reject radial Menu
	public void selectNextfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{

		performGSPSOperations(gspsObject, gspsNext, true);
	}

	public void selectAcceptfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{


		performGSPSOperations(gspsObject, gspsAccept, true);

	}

	private void performGSPSOperations(WebElement gspsObject, WebElement gspsOperation, boolean notPerformingGspsOpt) throws InterruptedException {


		Actions action = new Actions(driver);

		action.moveToElement(gspsObject).perform();
		LOGGER.info("Move to Element");

		action.click().moveByOffset(1, 1).build().perform();
		waitForTimePeriod(100);
		LOGGER.info("Performing left click on element");

		if(notPerformingGspsOpt) {
			click(gspsOperation);
			waitForTimePeriod(3000);
		}

	}

	public List<WebElement> getLinearMeasurementHandles(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g[ns-line] circle[fill='"+ViewerPageConstants.CIRCLE_GREY_HANDLE+"']"));
	}
	
	public boolean verifyLinearMeasurementIsActiveGSPS(int whichViewbox,int whichMeasurement , String whichColor, String opacity) {
		
		boolean status = false;
		List<WebElement> measurement =getLinearMeasurements(whichViewbox,whichMeasurement);
		
		System.out.println(measurement.get(0).getAttribute(NSGenericConstants.STROKE));
		System.out.println(measurement.get(1).getAttribute(NSGenericConstants.STROKE));
		
		try {
		status = measurement.get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.SHADOW_COLOR)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE).equals(whichColor) ;	
		
		status = status && measurement.get(0).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity) ;	
		
		status = status && measurement.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.SHADOW_GSPS_WIDTH)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH) ;
		}catch(Exception e) {status = false;}

		return status;
		
	}
	
	public boolean verifyLinearMeasurementAnnotationIsInActiveGSPS(int whichViewbox, int whichMeasurement, String whichColor, String opacity){

		boolean status = false;
		List<WebElement> measurement =getLinearMeasurements(whichViewbox,whichMeasurement);
		
		status = measurement.size()==3;
		status = status && measurement.get(0).getAttribute(NSGenericConstants.STROKE).equals(whichColor)&&
				measurement.get(1).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.FILL_COLOR_HANDLER) ;
		
		status = status && measurement.get(0).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);
				
		status = status && measurement.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH)&& 
				measurement.get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);


		return status;
	}
	
	public int getWhichMeasurementIsFocused(int whichViewbox,String whichColor,String opacity) {
		
		List<WebElement> myPoint = getAllLinearMeasurements(whichViewbox);
		int index =0;

		for(int i =1;i<=myPoint.size();i++) {

			if(verifyLinearMeasurementIsActiveGSPS(whichViewbox, i, whichColor,opacity)) {
				index = i;
				break;
			}

		}

		return index;
		
	}

}
