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

public class CircleAnnotation extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public CircleAnnotation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	By circleCenter = By.cssSelector(".selectableCircleArea");
	
	public void selectCircleFromQuickToolbar(int whichViewbox){
		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(circleIcon);
		click(circleIcon);
		waitForElementInVisibility(quickToolbar);
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number, starting and end co-ordinate for Circle Annotation
	 * @return: void
	 * Description: This method is used to draw a circle Annotation on Viewbox
	 */
	public void drawCircle(int whichViewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		dragAndReleaseOnViewer(getViewPort(whichViewbox), from_xOffset, from_yOffset, to_xOffset, to_yOffset);
		waitForTimePeriod(2000);
		}

	/**
	 * @author Vivek
	 * @param  Viewbox Number and circle annotation number as per as occurence
	 * @return: void
	 * Description: This method is used to get current selected Circle Annotation on Viewbox
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public void selectCircle(int whichViewbox, int whichCircle) throws InterruptedException{	
		
		click(getAllCircles(whichViewbox).get(whichCircle-1));
		waitForTimePeriod(1000);
		
	}
	/**
	 * @author Nikita
	 * @param  Viewbox Number and circle annotation number as per as occurrence
	 * @return: void
	 * Description: This method is used to click current selected Circle Annotation on Viewbox
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */

	public void selectCircleWithClick(int whichViewbox, int whichCircle) throws TimeoutException, InterruptedException{	
		mouseHoverWithClick(PRESENCE,getAllCircles(whichViewbox).get(whichCircle-1));
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number
	 * @return: WebElement
	 * Description: This method is used to get selected Circle Annotation on Viewbox
	 */
	public WebElement getSelectedCircle(int whichViewbox){	

		return driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1)+" > g >g >g[ns-circle] circle["+NSGenericConstants.FILL+"="));
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number, Circle Annotation Number as per as occurence and co-ordinate
	 * @return: void
	 * Description: This method is used to move selected Cicle Annotation on Viewbox
	 */ 
	public void moveSelectedCircle(int whichViewbox,int xOffset, int yOffset) throws InterruptedException{


		List<WebElement> circles = getAllCircles(whichViewbox);
		Actions action = new Actions(driver);
		action.moveByOffset(0, 0).moveToElement(circles.get(0).findElement(circleCenter),2,2).clickAndHold(circles.get(0).findElement(circleCenter)).build().perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(2000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(3000);

	}

	public boolean verifyCircleAnnotationIsRejectedGSPS(int whichViewbox, int whichCircle){

		return verifyCircleIsCurrentInActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	public boolean verifyCircleAnnotationIsAcceptedGSPS(int whichViewbox, int whichCircle){

		return verifyCircleIsCurrentInActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 
	
	public boolean verifyCircleAnnotationIsPendingGSPS(int whichViewbox, int whichCircle){

		return verifyCircleIsCurrentInActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}

	public boolean verifyCircleAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichCircle){

		return verifyCircleIsCurrentActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public boolean verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichCircle){
		
		return verifyCircleIsCurrentActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	/**
	 * @author payal
	 * @param Viewbox number
	 * @return: null
	 * Description: This method return true if circle is present
	 * 	 */
	public boolean isCirclePresent(int whichViewbox){
		boolean status = false;
		try{	
			if(getAllCircles(whichViewbox).size()>0)
				status = true;
		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}

	public boolean verifyCircleAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichCircle){

		return verifyCircleIsCurrentActiveGSPS(whichViewbox, whichCircle, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public List<WebElement> getAllCircles(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g[ns-circle]"));

	}

	public List<WebElement> getCircle(int whichViewbox, int whichCircle){

		return getAllCircles(whichViewbox).get(whichCircle-1).findElements(By.cssSelector("circle"));

	}

	public boolean isCircleSelected(int whichViewbox, int whichCircle){
		boolean status = false ;
		List<WebElement> allCircles = getAllCircles(whichViewbox);
		try{

			if(isElementPresent(allCircles.get(whichCircle-1).findElement((By.cssSelector("circle:last-child"))))){

				status = true;
			}
		}
		catch(Exception e){
			printStackTrace(e.getMessage());
		}

		return status;
	}

	public void deleteSelectedCircle() throws InterruptedException{

		pressKeys(Keys.DELETE);	

	}

	public void deleteCircle(int whichViewbox, int whichCircle) throws TimeoutException, InterruptedException{
		selectCircle(whichViewbox,whichCircle);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(2000);
	}
	
	public void deleteCircleUsingARBar(int whichViewbox, int whichCircle) throws TimeoutException, InterruptedException{
		selectCircle(whichViewbox,whichCircle);
		selectDeletefromGSPSRadialMenu();
		waitForTimePeriod(2000);
	}
	
	public List<WebElement> getTextCommentsforAllCircles(int whichViewbox){

		List<WebElement> list = getAllCircles(whichViewbox);

		List<WebElement> listOfCircles = new ArrayList<WebElement>();

		for(int i =0 ;i<list.size();i++){
			try{	

				listOfCircles.add(list.get(i).findElement(By.cssSelector("g > text + text:last-child >tspan")));
			}

			catch(NoSuchElementException e){
				continue;

			}
		}
		return listOfCircles;
	}

	public void resizeCircle(int xOffset,int yOffset) throws InterruptedException {

		Actions action = new Actions(driver);
		if(isElementPresent(By.cssSelector(".highlightCircleHalo"))){

			WebElement circle = getAllCircles(1).get(0).findElement(By.cssSelector(".highlightCircleHalo"));
			action.moveToElement(circle).clickAndHold(circle).perform();
//			action.clickAndHold(circle).perform();
			LOGGER.info("drag and drop...clickAndHold");
			waitForTimePeriod(3000);
			action.moveByOffset(xOffset, yOffset).perform();
			LOGGER.info("drag and drop...moveByOffset");
			waitForTimePeriod(1000);
			action.release().build().perform();
			LOGGER.info("drag and drop...completed");
			waitForTimePeriod(2000);
		}else {
			
			WebElement circlean = getAllCircles(1).get(0).findElement(circleCenter);		
			Float radius = Float.parseFloat(circlean.getAttribute(NSGenericConstants.R));
			action.moveToElement(circlean).moveByOffset(radius.intValue(),0).build().perform();
			waitForTimePeriod(2000);
			resizeCircle(xOffset, yOffset);
			
		}

	}

	public boolean verifyCircleIsCurrentActiveGSPS(int whichViewbox, int whichCircle, String whichColor, String opacity) {
		
		boolean status = false;
		try{

			WebElement circle =getAllCircles(whichViewbox).get(whichCircle-1);

			status=  isElementPresent(circle.findElement(By.cssSelector("circle.circleAnnotation["+NSGenericConstants.STROKE+"='"+whichColor+"']")));
			status = status && isElementPresent(circle.findElement(By.cssSelector("circle["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));
			status = status &&  getAttributeValue(circle.findElement(By.cssSelector("circle.circleAnnotation["+NSGenericConstants.STROKE+"='"+whichColor+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			status = status && getAttributeValue(circle.findElement(By.cssSelector("circle["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			 
		
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}
	
	public boolean verifyCircleIsCurrentInActiveGSPS(int whichViewbox, int whichCircle, String whichColor, String opacity) {
		
		boolean status = false;
		WebElement circle =getCircle(whichViewbox,whichCircle).get(0);
		status = getAttributeValue(circle, NSGenericConstants.STROKE).equals(whichColor);		
		status = status && circle.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
		status = status &&getAttributeValue(circle ,NSGenericConstants.STROKE_OPACITY).equals(opacity);
		
		return status;
	}

	public int  getCircleWhichIsActive(int whichViewbox, String color){

		List<WebElement> circle = getAllCircles(whichViewbox);
		int index =0;

		for(int i =1;i<=circle.size();i++) {

			if(verifyCircleIsCurrentActiveGSPS(whichViewbox, i, color, ViewerPageConstants.OPACITY_FOR_JUMP_ICON)){
				index = i;
				break;
			}

		}

		return index;
	}

	public void mouseHoverOnCircleBoundary(int whichviewbox, int whichCircle) {
		
		Actions action = new Actions(driver);
		WebElement circlean = getAllCircles(1).get(whichCircle-1).findElement(circleCenter);		
		Float radius = Float.parseFloat(circlean.getAttribute(NSGenericConstants.R));
		action.moveToElement(circlean).moveByOffset(radius.intValue(),0).build().perform();
		
				
	}
	
	public void performRightClickOnBoundary(int whichviewbox, int whichCircle) {
		
		
				
	}
	
	public boolean selectCutUsingContextMenu(int whichviewbox, int whichCircle) throws InterruptedException {

		Actions action = new Actions(driver);
		WebElement circlean = getAllCircles(whichviewbox).get(whichCircle-1).findElement(circleCenter);		
		Float radius = convertIntoFloat(circlean.getAttribute(NSGenericConstants.R));
		action.moveToElement(circlean).moveByOffset(radius.intValue(),0).contextClick().build().perform();		
		
		boolean status = isElementPresent(cutOption);		
		WebElement cut = getElement(cutOption);
		status = status && getText(cut).equalsIgnoreCase(ViewerPageConstants.CUT);
		click(cut);
		
		return status;

	}
	
	public boolean verifyLabeling(int whichViewbox, int whichCircle, String label) {
		
		return getLabel(whichViewbox, whichCircle).equalsIgnoreCase(label);
		
	}
	
	public String getLabel(int whichViewbox, int whichCircle) {
		
		WebElement tag = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-circle] + g[ns-roilabel] text:nth-child(2)")).get(whichCircle-1);
		
		return getText(tag);
		
	}
	
}
