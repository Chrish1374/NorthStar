package com.trn.ns.page.factory;


import java.awt.AWTException;
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

public class EllipseAnnotation extends ViewerSliderAndFindingMenu {


	private WebDriver driver;

	public EllipseAnnotation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	By ellipseCenter = By.cssSelector(".selectableEllipseArea");
	
	public void selectEllipseFromQuickToolbar(int whichViewbox){
		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(ellipseIcon);
		click(ellipseIcon);
		waitForElementInVisibility(quickToolbar);
	}
	

	public boolean verifyEllipseAnnotationIsRejectedGSPS(int whichViewbox, int whichEllipse){

	 return verifyEllipseIsCurrentInActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	public boolean verifyEllipseAnnotationIsAcceptedGSPS(int whichViewbox, int whichEllipse){

		return verifyEllipseIsCurrentInActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		
	} 
	
	
	public boolean verifyEllipseAnnotationIsPendingGSPS(int whichViewbox, int whichEllipse){

		return verifyEllipseIsCurrentInActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		

	} 

	public boolean verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichEllipse){

		return verifyEllipseIsCurrentActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichEllipse){

		return verifyEllipseIsCurrentActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		
	} 
	
	public boolean verifyEllipseAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichEllipse){

		return verifyEllipseIsCurrentActiveGSPS(whichViewbox, whichEllipse, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		
	} 


	public void deleteSelectedEllipse() throws InterruptedException{
		pressKeys(Keys.DELETE); 
	}

	public void deleteEllipse(int whichViewbox, int whichPoint) throws TimeoutException, InterruptedException{


		selectEllipse(whichViewbox,whichPoint);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(2000);
	}
	/**
	 * @author Vivek
	 * @param  Viewbox Number, Ellipse Annotation Number as per as occurence and co-rodinate
	 * @return: void
	 * Description: This method is used to move selected Ellipse Annotation on Viewbox
	 */ 
	public void moveSelectedEllipse(int whichViewbox, int xOffset, int yOffset) throws InterruptedException{

		
		Actions action = new Actions(driver);
//		action.moveToElement(getSelectedEllipse(whichViewbox).findElement(By.cssSelector(".selectableEllipseArea[id]")),1,1).clickAndHold().build().perform();
		
		action.moveByOffset(0, 0).moveToElement(getAllEllipses(whichViewbox).get(0).findElement(ellipseCenter),2,2).clickAndHold(getAllEllipses(whichViewbox).get(0).findElement(ellipseCenter)).build().perform();
		LOGGER.info("drag and drop...clickAndHold");
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(3000);
		
	}
	
	public void moveEllipse(int whichViewbox,int whichEllipse, int xOffset, int yOffset) throws InterruptedException{

		
		Actions action = new Actions(driver);
		selectEllipse(whichViewbox, whichEllipse);		
		List<WebElement> ellipses = getAllEllipses(whichViewbox);
		action.moveToElement(ellipses.get(whichEllipse-1).findElement(By.cssSelector(".selectableEllipseArea[id]")),1,1).clickAndHold().build().perform();
		
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(2000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(3000);
		
	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number
	 * @return: WebElement 
	 * Description: This method is used to get selected Ellipse Annotation on Viewbox
	 * @throws AWTException 
	 * @throws NumberFormatException 
	 */ 
	public WebElement getResizeHandleForSelectedEllipse(int whichViewbox) {	

		int radiusx = ((Double)Double.parseDouble(getEllipses(whichViewbox).get(0).getAttribute(NSGenericConstants.RX))).intValue();

		Actions a = new Actions(driver);
		a.moveToElement(getEllipses(whichViewbox).get(0)).perform();
		a.moveByOffset(radiusx-1, 0).perform();

		return driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g >g >g > ellipse + circle+ circle"));
	}

	public WebElement getResizeHandleForSelectedEllipse(int whichViewbox , int whichEllipse) {	

		int radiusx = ((Double)Double.parseDouble(getEllipses(whichViewbox).get(whichEllipse-1).getAttribute(NSGenericConstants.RX))).intValue();

		Actions a = new Actions(driver);
		a.moveToElement(getEllipses(whichViewbox).get(whichEllipse-1)).perform();
		a.pause(100);
		a.moveByOffset(radiusx-1, 0).pause(100).build().perform();

		return driver.findElement(By.cssSelector(".highlightEllipseHalo"));
	}


	/**
	 * @author Vivek
	 * @param  Viewbox Number and co-ordinate
	 * @return: void
	 * Description: This method is used to move selected Ellipse Annotation on Viewbox
	 */ 
	public void resizeSelectedEllipse(int whichViewbox,int xOffset, int yOffset) throws InterruptedException{


		Actions action = new Actions(driver);
		action.clickAndHold(getResizeHandleForSelectedEllipse(whichViewbox)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);
	}

	public void resizeEllipse(int whichViewbox,int whichEllipse,int xOffset, int yOffset) throws InterruptedException{


		Actions action = new Actions(driver);
		action.clickAndHold(getResizeHandleForSelectedEllipse(whichViewbox,whichEllipse)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);
	}


	/**
	 * @author Vivek
	 * @param  Viewbox Number
	 * @return: WebElement 
	 * Description: This method is used to get selected Ellipse Annotation on Viewbox
	 */ 
	public WebElement getSelectedEllipse(int whichViewbox){	
		
		WebElement element = null;
		if(isElementPresent(By.cssSelector("#svg-"+(whichViewbox-1)+" [ns-ellipse] ellipse[stroke='yellow']")))
			element =  driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1)+" [ns-ellipse]"));
		
		return element;
	}

	/**
	 * @author Payal
	 * @param  Viewbox Number and Ellipse Annotation Number as per as occurence
	 * @return: boolean value
	 * Description: This method is used to verify that the ellipse is selected or not
	 */
	public boolean isEllipseSelected(int whichViewbox, int whichEllipse){
		boolean status = false ;

		List<WebElement> allEllipses = getAllEllipses(whichViewbox);
		try{
			if(isElementPresent(allEllipses.get(whichEllipse-1).findElement((By.cssSelector("ellipse+circle"))))){

				status = true;
			}
		}
		catch(Exception e){
			printStackTrace(e.getMessage());
		}

		return status;
	}

	/**
	 * @author Payal
	 * @param  Viewbox Number 
	 * @return: list of all ellipse
	 * Description: This method is used to return list of all present ellipse
	 */
	public List<WebElement> getAllEllipses(int whichViewbox){

		List<WebElement> list = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g> g[ns-ellipse]"));

		List<WebElement> listOfEllipses = new ArrayList<WebElement>();

		for(int i =0 ;i<list.size();i++){
			try{

				listOfEllipses.add(list.get(i));

			}	catch(Exception e){
				continue;
			}	

		}
		return listOfEllipses;

	}

	/**
	 * @author Vivek
	 * @param  Viewbox Number and Ellipse Annotation Number as per as occurence
	 * @return: void
	 * Description: This method is used to select Ellipse Annotation on Viewbox
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	public void selectEllipse(int whichViewbox, int whichEllipse) throws TimeoutException, InterruptedException{	

		Actions a = new Actions(driver);
		a.moveToElement(getAllEllipses(whichViewbox).get(whichEllipse-1)).click().build().perform();

	}

	public void selectEllipseWithClick(int whichViewbox, int whichEllipse) throws TimeoutException, InterruptedException{	
		mouseHoverWithClick(PRESENCE,getAllEllipses(whichViewbox).get(whichEllipse-1));
		waitForTimePeriod(1000);
	}
	/**
	 * @author Vivek
	 * @param  Viewbox Number, starting and end co-ordinate for Circle Annotation
	 * @return: void
	 * Description: This method is used to draw a Ellipse Annotation on Viewbox
	 */
	public void drawEllipse(int whichViewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{
		dragAndReleaseOnViewer(getViewPort(whichViewbox), from_xOffset, from_yOffset, to_xOffset, to_yOffset);

	}


	/**
	 * @author payal
	 * @param Viewbox number
	 * @return: null
	 * Description: This method return true if ellipse is present
	 * 	 */
	public boolean isEllipsePresent(int whichViewbox){
		boolean status = false;
		try{	
			if(getEllipses(whichViewbox).size()>0)
				status = true;
		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}



	//	Get All eliipsis in passed viewbox

	public List<WebElement> getEllipses(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g> ellipse:first-child"));
	}
	
	public List<WebElement> getTextCommentsforAllEllipses(int whichViewbox){

		List<WebElement> list = getAllEllipses(whichViewbox);

		List<WebElement> listOfTextComments = new ArrayList<WebElement>();

		for(int i =0 ;i<list.size();i++){
			try{	

				listOfTextComments.add(list.get(i).findElement(By.cssSelector("g > text + text:last-child >tspan")));
			}

			catch(NoSuchElementException e){
				continue;

			}
		}
		return listOfTextComments;
	}
	
	public boolean verifyEllipseIsCurrentActiveGSPS(int whichViewbox, int whichEllipse, String whichColor, String opacity) {
		
		boolean status = false;
		try{

			WebElement ellipse = getAllEllipses(whichViewbox).get(whichEllipse-1);
			status=isElementPresent(ellipse.findElement(By.cssSelector("ellipse["+NSGenericConstants.STROKE+"='"+whichColor+"']")));
			status = status && isElementPresent(ellipse.findElement(By.cssSelector("ellipse["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));

			status = status &&  getAttributeValue(ellipse.findElement(By.cssSelector("ellipse["+NSGenericConstants.STROKE+"='"+whichColor+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			status = status && getAttributeValue(ellipse.findElement(By.cssSelector("ellipse["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			 
		
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}
	
	public boolean verifyEllipseIsCurrentInActiveGSPS(int whichViewbox, int whichEllipse, String whichColor, String opacity) {
		
		boolean status = false;
		WebElement ellipse = getEllipses(whichViewbox).get(whichEllipse-1);
		status = getAttributeValue(ellipse, NSGenericConstants.STROKE).equals(whichColor);		
		status = status && ellipse.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
		status = status &&getAttributeValue(ellipse ,NSGenericConstants.STROKE_OPACITY).equals(opacity);
		
		return status;
		
	}

	public int  getEllipseWhichIsActive(int whichViewbox, String color){

		List<WebElement> ellipse = getAllEllipses(whichViewbox);
		int index =0;

		for(int i =1;i<=ellipse.size();i++) {

			if(verifyEllipseIsCurrentActiveGSPS(whichViewbox, i, color, ViewerPageConstants.OPACITY_FOR_JUMP_ICON)){
				index = i;
				break;
			}

		}

		return index;
	}
	
	public boolean selectCutUsingContextMenu(int whichviewbox, int whichEllipse) throws InterruptedException {

		
		Actions action = new Actions(driver);
		WebElement circlean = getAllEllipses(1).get(whichEllipse-1).findElement(ellipseCenter);		
		Float radius = Float.parseFloat(circlean.getAttribute(NSGenericConstants.RX));
		action.moveToElement(circlean).moveByOffset(radius.intValue(),0).contextClick().build().perform();		
		
		boolean status = isElementPresent(cutOption);		
		WebElement cut = getElement(cutOption);
		status = status && getText(cut).equalsIgnoreCase(ViewerPageConstants.CUT);
		click(cut);
		
		return status;

	}
	
	public boolean verifyLabeling(int whichViewbox, int whichEllipse, String label) {
		
		return getLabel(whichViewbox, whichEllipse).equalsIgnoreCase(label);
		
	}
	
	public String getLabel(int whichViewbox, int whichEllipse) {
		
		WebElement tag = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-ellipse] + g[ns-roilabel] text:nth-child(2)")).get(whichEllipse-1);
		
		return getText(tag);
		
	}

}
