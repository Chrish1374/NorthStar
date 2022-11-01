package com.trn.ns.page.factory;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;

public class PolyLineAnnotation extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public PolyLineAnnotation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void drawFreehandPolyLine(int whichViewbox, int[] coordinates) throws InterruptedException {

		Actions act = new Actions(driver);
		act.moveToElement(getViewPort(whichViewbox)).moveByOffset(coordinates[0], coordinates[1]).clickAndHold().perform();    
		waitForTimePeriod(100);
		//		act.clickAndHold();
		for(int i =2 ;i<coordinates.length-1;) {
			act.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}
		waitForTimePeriod(100);
		act.release().perform();
		//		act.moveByOffset(1, 1).build().perform();
		waitForTimePeriod(2000);
	}

	public void drawFreehandPolyLineExitUsingESC(int whichViewbox, int[] coordinates) throws InterruptedException {

		Actions act = new Actions(driver);
		act.moveToElement(getViewPort(whichViewbox)).moveByOffset(coordinates[0], coordinates[1]).clickAndHold().perform();

		for(int i =2 ;i<coordinates.length-1;) {
			act.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}

		act.release().sendKeys(Keys.ESCAPE).perform();
		//		act.sendKeys(Keys.ESCAPE).moveByOffset(1, 1).build().perform();
		waitForTimePeriod(2000);


	}

	public void drawFreehandPolyLineExitUsingDoubleClick(int whichViewbox, int[] coordinates) throws InterruptedException {

		Actions act = new Actions(driver);
		act.moveToElement(getViewPort(whichViewbox)).moveByOffset(coordinates[0], coordinates[1]);    
		act.clickAndHold();
		for(int i =2 ;i<coordinates.length-1;) {
			act.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}
		act.doubleClick().moveByOffset(1, 1).build().perform();
		waitForTimePeriod(2000);


	}

	public void drawPolyLineExitUsingESCKey(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{

		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
			mouseHover(PRESENCE, getViewbox(whichViewbox));

			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).moveByOffset(coOrdinates[0], coOrdinates[1]).click().keyUp(Keys.CONTROL).build().perform();

			for(int i= 2; i < coOrdinates.length; i+= 2) {
				clickAt(coOrdinates[i], coOrdinates[i+1]);

				//represent last co-ordinate
				if(i + 2 == coOrdinates.length) {
					Actions action1 = new Actions(driver);
					action1.moveToElement(getViewPort(whichViewbox)).sendKeys(Keys.ESCAPE).perform();
					waitForTimePeriod(200);
				}
			}

			waitForTimePeriod(2000);
		}
	}

	public void drawPolyLineExitUsingDoubleClickKey(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{

		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
			mouseHover(PRESENCE, getViewbox(whichViewbox));

			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).moveByOffset(coOrdinates[0], coOrdinates[1]).click().keyUp(Keys.CONTROL).build().perform();
				for(int i= 2; i < coOrdinates.length; i+= 2) {
					clickAt(coOrdinates[i], coOrdinates[i+1]);
					waitForTimePeriod(200);
					//represent last co-ordinate
					if(i + 2 == coOrdinates.length) {
						//					action.moveByOffset(coOrdinates[i], coOrdinates[i+1]).build().perform();
						clickAt(coOrdinates[i], coOrdinates[i+1]);
						waitForTimePeriod(200);
						action.moveByOffset(coOrdinates[i], coOrdinates[i+1]).doubleClick().perform();
						waitForTimePeriod(200);
					}
				}

			
			waitForTimePeriod(200);
				
			}
	}

	public void drawPolyLineWithoutPressingCntrlKey(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{

		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
			mouseHover(PRESENCE, getViewbox(whichViewbox));

			for(int i= 0; i < coOrdinates.length; i+= 2) {
				clickAt(coOrdinates[i], coOrdinates[i+1]);
				waitForTimePeriod(200);
				//represent last co-ordinate
				if(i + 2 == coOrdinates.length) {
					Actions action = new Actions(driver);
					action.moveByOffset(coOrdinates[i], coOrdinates[i+1]).sendKeys(Keys.ESCAPE).build().perform();
					waitForTimePeriod(200);
				}
			}


		}
	}

	public List<WebElement> getTransparentPolylineHandles(int whichViewbox,int whichPolyLine){

		return getAllPolylines(whichViewbox).get(whichPolyLine-1).findElements(By.cssSelector("circle[fill='transparent']"));
	}

	public List<String> movePolyLine(int whichViewbox,int whichPolyline,int xOffset,int yOffset) throws InterruptedException{


		List<WebElement> handles = getTransparentPolylineHandles(whichViewbox, whichPolyline);

		List<String> cursor = new ArrayList<String>();

		int [] pixelArrayXoffset, pixelArrayYoffset;
		Actions action = new Actions(driver);
		action.moveToElement(handles.get(whichPolyline)).keyDown(Keys.CONTROL).clickAndHold().keyUp(Keys.CONTROL).perform();

		int hopValue= Integer.parseInt(TEST_PROPERTIES.get("hop"));
		pixelArrayXoffset= getSmallerPixelValuesForHopping(xOffset, hopValue);
		pixelArrayYoffset= getSmallerPixelValuesForHopping(yOffset, hopValue);

		LOGGER.info("drag and drop...clickAndHold");
		for(int i= 0; i < pixelArrayXoffset.length; i++) {
			action.moveByOffset(pixelArrayXoffset[i], pixelArrayYoffset[i]).perform();
			handles = getAllPolylines(whichViewbox).get(whichPolyline-1).findElements(By.cssSelector("circle[fill='transparent']"));
			//			cursor.add(handles.get(0).getCssValue("cursor"));
			waitForTimePeriod(100);

			LOGGER.info("drag and drop...moveByOffset");
		}
		action.release().build().perform();
		action.moveByOffset(1, 1).perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);
		return cursor;

	}

	public void deletePointFromPolyLine(int whichViewbox, int whichPolyline, int whichPoint) {

		Actions action = new Actions(driver);
		action.moveToElement(getTransparentPolylineHandles(whichViewbox, whichPolyline).get(whichPoint)).contextClick().build().perform();
		action.pause(1000).perform();

	}

	public void addNewPointToPolyline(int whichViewbox,int whichPolyline,int whichLine) throws InterruptedException{

		List<WebElement> lines = getLinesOfPolyLine(whichViewbox, whichPolyline);			
		Actions action = new Actions(driver);
		action.moveToElement(lines.get(whichLine)).click().build().perform();
		waitForTimePeriod(1000);
	}

	public boolean checkHaloRingIsDisplayed(int whichViewbox,int whichPolyline,int whichLine) {

		Actions action = new Actions(driver);
		action.moveToElement(getLinesOfPolyLine(whichViewbox, whichPolyline).get(whichLine)).perform();		
		action.pause(1000).perform();
		return (getHaloRing(whichViewbox, whichPolyline).size()>0) ? true : false; 


	}

	public List<WebElement> getHaloRing(int whichViewbox,int whichPolyLine) {

		return getAllPolylines(whichViewbox).get(whichPolyLine-1).findElements(By.cssSelector("g circle.highlightPt"));

	}

	public void addAndDragPoint(int whichViewbox,int whichPolyline,int whichLine,int xOffset, int yOffset) throws Exception {

		List<WebElement> lines = getLinesOfPolyLine(whichViewbox, whichPolyline);			
		Actions action = new Actions(driver);
		action.moveToElement(lines.get(whichLine)).clickAndHold().moveByOffset(xOffset, yOffset).release().build().perform();
		waitForTimePeriod(1000);
	}

	public void drawClosedPolyLine(int whichViewbox,int[] coOrdinates) throws TimeoutException, InterruptedException{

		if(coOrdinates.length >= 4 && coOrdinates.length % 2 == 0) {
			mouseHover(PRESENCE, getViewbox(whichViewbox));

			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).moveByOffset(coOrdinates[0], coOrdinates[1]).click().keyUp(Keys.CONTROL).build().perform();

			for(int i= 2; i < coOrdinates.length; i+= 2) {
				action.moveToElement(getViewPort(1)).moveByOffset(coOrdinates[i], coOrdinates[i+1]).click().perform();
				waitForTimePeriod(200);

			}

			waitForTimePeriod(200);
			action.moveByOffset(10,10).perform();


		}
	}
	
	
	public List<WebElement> getLinesOfPolyLine(int whichViewbox , int whichPolyline){

		return getAllPolylines(whichViewbox).get(whichPolyline-1).findElements(By.cssSelector("g > g> g > line"));
	}

	public List<WebElement> getAllPolylines(int whichViewbox){

		List<WebElement> elements = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" [ns-polyline]"));
		return elements;


	}





	public void selectPolylineFromQuickToolbar(int whichViewbox){

		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(polylineIcon);
		click(polylineIcon);
		waitForElementInVisibility(quickToolbar);

	}
	
	public WebElement getPolyLineSvg(int whichViewbox , int whichPolyline){

		return getElement(By.cssSelector("#svg-"+(whichViewbox-1)+" [ns-polyline] svg > path"));
	}

	public List<WebElement> getPolylineHandles(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+"> g > g[ns-polyline] circle[fill='"+ViewerPageConstants.CIRCLE_GREY_HANDLE+"'][style='display: block;']"));
	}

	public int getPolylineCount(int whichViewbox){

		return getAllPolylines(whichViewbox).size();

	}

	public void editPolyLine(int whichviewbox,WebElement line, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{
		Actions action = new Actions(driver);
		//		getPolylineHandles(1).get(5)
		action.moveToElement(getViewPort(whichviewbox)).clickAndHold().moveByOffset(to_xOffset, to_yOffset).release().perform();
		//		action.dragAndDropBy(getPolylineHandles(1).get(5), to_xOffset, to_yOffset).build().perform();
		waitForTimePeriod(100);

	}

	public void selectPolyline(int whichViewbox, int whichPolyline){	
		List<WebElement> elements = getLinesOfPolyLine(whichViewbox, whichPolyline);
		elements.get(1).click();
	}

	public void selectPolyline(int whichViewbox, int whichPolyline, int whichLine){	
		List<WebElement> elements = getLinesOfPolyLine(whichViewbox, whichPolyline);
		elements.get(whichLine-1).click();
	}

	public void selectClassicPolylineWithCtrlLeft(int whichViewbox, int whichPolyline){	
		Actions a = new Actions(driver);
		List<WebElement> elements = getLinesOfPolyLine(whichViewbox, whichPolyline);
		a.moveToElement(elements.get(1)).perform();
		a.pause(1000);
		a.keyDown(Keys.CONTROL).click().keyUp(Keys.CONTROL).perform();
		waitForElementVisibility(gspsPrevious);
	}

	public boolean isPolylineSelected(int whichViewbox){
		boolean status = false ;
		List<WebElement> allPolylines = getPolylineHandles(whichViewbox);
		try{
			if(allPolylines.size()>0){

				status = true;
			}
		}
		catch(NoSuchElementException e){
			printStackTrace(e.getMessage());
		}

		return status;
	}

	public void deleteSelectedPolyline() throws InterruptedException{
		pressKeys(Keys.DELETE); 
	}

	public boolean verifyPolyLineAnnotationIsRejectedGSPS(int whichViewbox, int whichPolyline){
		
		return verifyPolylineIsInActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}

	public boolean verifyPolyLineAnnotationIsAcceptedGSPS(int whichViewbox, int whichPolyline){

		return verifyPolylineIsInActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyPolyLineAnnotationIsPendingGSPS(int whichViewbox, int whichPolyline){

		return verifyPolylineIsInActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichPolyline){

		return verifyPolylineIsActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public boolean verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, WebElement polyLine){

		boolean status = false;

		try{
			//				boolean condition_color =isElementPresent(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.REJECTED_COLOR+"']")));
			boolean shadow_color = isElementPresent(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));

			if(shadow_color)// && condition_color)
				status=true;
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}

		return status;
	} 

	public boolean verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichPolyline){

		return verifyPolylineIsActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	} 

	public boolean verifyPolyLineAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichPolyline){

		return verifyPolylineIsActiveGSPS(whichViewbox, whichPolyline, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	} 

	public void editPolyLine(WebElement element, int[] coordinates ,WebElement lastElement) {

		Actions act = new Actions(driver);
		act.moveToElement(element).clickAndHold();    
		for(int i =0 ;i<coordinates.length-1;) {
			act.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}
		act.moveToElement(lastElement).click().release().build().perform();
		act.pause(2000).perform();			



	}

	public void moveFreePolyLine(int whichViewbox,int whichPolyline,int xOffset,int yOffset) throws InterruptedException{

		List<WebElement> handles = getLinesOfPolyLine(whichViewbox, whichPolyline);
		int [] pixelArrayXoffset, pixelArrayYoffset;
		Actions action = new Actions(driver);
		action.moveToElement(handles.get(whichPolyline)).keyDown(Keys.CONTROL).clickAndHold().keyUp(Keys.CONTROL).perform();

		int hopValue= Integer.parseInt(TEST_PROPERTIES.get("hop"));
		pixelArrayXoffset= getSmallerPixelValuesForHopping(xOffset, hopValue);
		pixelArrayYoffset= getSmallerPixelValuesForHopping(yOffset, hopValue);

		LOGGER.info("drag and drop...clickAndHold");
		for(int i= 0; i < pixelArrayXoffset.length; i++) {
			action.moveByOffset(pixelArrayXoffset[i], pixelArrayYoffset[i]).perform();
			waitForTimePeriod(100);

			LOGGER.info("drag and drop...moveByOffset");
		}
		action.release().build().perform();
		LOGGER.info("drag and drop...completed");

	}

	public void editPolyLine(WebElement element, int[] coordinates) throws InterruptedException {

		Actions act = new Actions(driver);
		act.moveToElement(element);    
		act.clickAndHold();
		for(int i =0 ;i<coordinates.length-1;) {
			act.moveByOffset(coordinates[i], coordinates[i+1]).perform();
			i=i+2;
		}
		act.release().build().perform();
		waitForTimePeriod(2000);

	}

	public void editPolyLine(WebElement element1, WebElement element2) throws InterruptedException {

		Actions act = new Actions(driver);
		act.moveToElement(element1);    
		act.clickAndHold();
		act.moveToElement(element2).release().build().perform();
		waitForTimePeriod(2000);


	}

	public WebElement getSelectedRTPolyLine(int whichViewbox) {

		List<WebElement> polylines = getAllPolylines(whichViewbox);
		WebElement selectedPolyLine = null;


		for(WebElement poly : polylines) {

			try {
				if(isElementPresent(poly.findElement(By.cssSelector("svg > path[stroke='"+ViewerPageConstants.SHADOW_COLOR+"']"))))
				{
					selectedPolyLine = poly;
					break;

				}}
			catch (NoSuchElementException | NullPointerException e) {
				continue;
			}
		}
		return selectedPolyLine;

	}

	public List<WebElement> getLinesOfSelectedRTPolyLine(WebElement polyLine) {

		return polyLine.findElements(By.cssSelector("g > g> g > line"));

	}

	public void openGSPSRadialMenuPolyLine(WebElement gspsObject) throws InterruptedException {

		Actions a = new Actions(driver);
		a.moveToElement(gspsObject).perform();
		a.keyDown(Keys.CONTROL).click().build().perform();
		waitForTimePeriod(1000);
		a.keyUp(Keys.CONTROL).perform();
		a.moveByOffset(2, 0).perform();
		waitForTimePeriod(300);
		waitForElementVisibility(gspsPrevious);
	}

	public void openGSPSRadialMenuPolyLine(WebElement gspsObject,boolean freePolyLine) throws InterruptedException {

		Actions a = new Actions(driver);
		a.moveToElement(gspsObject).perform();
		a.click().moveByOffset(1, 1).perform();
		waitForTimePeriod(300);
		waitForElementVisibility(gspsPrevious);
	}

	public void addResultComment(WebElement temp, String inpText,boolean freePolyLine) throws InterruptedException 
	{
		if(freePolyLine)
			openGSPSRadialMenuPolyLine(temp,freePolyLine);
		else
			openGSPSRadialMenuPolyLine(temp);
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(300);	
		enterText(PRESENCE, editBox, inpText);
		waitForTimePeriod(300);	
		pressKeys(Keys.ENTER);
		waitForTimePeriod(3000);	 
	}

	public List<WebElement> getTextCommentsOnAllPolyLines(int whichViewbox){

		List<WebElement> list = getAllPolylines(whichViewbox);

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

	public boolean verifyClosedPolylineIsSelected(int whichViewbox,int whichPolyline) {

		List<WebElement> polylines = getAllPolylines(whichViewbox);
		boolean status = false;

		try {
			if(isElementPresent(polylines.get(whichPolyline-1).findElement(By.cssSelector("svg > path[stroke='"+ViewerPageConstants.SHADOW_COLOR+"']"))))

				status = true;


		}
		catch (NoSuchElementException | NullPointerException e) {
		}
		return status;

	}

	public void selectRejectfromGSPSRadialMenu(WebElement gspsObject,boolean freePolyLine) throws InterruptedException{
		if(freePolyLine)
			openGSPSRadialMenuPolyLine(gspsObject,freePolyLine);
		else
			openGSPSRadialMenuPolyLine(gspsObject);
		click(gspsReject);
		//Added a static wait, as cannot handle this with fluent wait,since this command has multiple outcome
		waitForTimePeriod(2000);
	}

	// Select Previous from Accept/Reject radial Menu
	public void selectPreviousfromGSPSRadialMenu(WebElement gspsObject,boolean freePolyLine) throws InterruptedException{
		if(freePolyLine)
			openGSPSRadialMenuPolyLine(gspsObject,freePolyLine);
		else
			openGSPSRadialMenuPolyLine(gspsObject);
		click(gspsPrevious);
		//Added a static wait, as cannot handle this with fluent wait,since this command has multiple outcome
		waitForTimePeriod(2000);
	}

	// Select Next from Accept/Reject radial Menu
	public void selectNextfromGSPSRadialMenu(WebElement gspsObject,boolean freePolyLine) throws InterruptedException{
		if(freePolyLine)
			openGSPSRadialMenuPolyLine(gspsObject,freePolyLine);
		else
			openGSPSRadialMenuPolyLine(gspsObject);
		waitForTimePeriod(1000);
		click(gspsNext);
		//Added a static wait, as cannot handle this with fluent wait,since this command has multiple outcome
		waitForTimePeriod(2000);
	}

	public void selectAcceptfromGSPSRadialMenu(WebElement gspsObject,boolean freePolyLine) throws InterruptedException{

		if(freePolyLine)
			openGSPSRadialMenuPolyLine(gspsObject,freePolyLine);
		else
			openGSPSRadialMenuPolyLine(gspsObject);
		click(gspsAccept);
		//Added a static wait, as cannot handle this with fluent,since this command has multiple outcome
		waitForTimePeriod(2000);
	}

	public List<WebElement> getPolyline(String path) {

		return driver.findElements(By.cssSelector("g[ns-polyline] > svg > path[d='"+path+"']"));
	}

	public boolean verifyPolylineStatus(String path, String color, boolean focused){

		boolean status = false, condition_color= false, condition_width = false, shadow_color= false ;
		List<WebElement> polyLine =getPolyline(path);

		if(focused) {

			shadow_color = polyLine.get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.SHADOW_COLOR);	
			condition_width = polyLine.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.SHADOW_GSPS_WIDTH) && polyLine.get(1).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
			condition_color = polyLine.get(1).getAttribute(NSGenericConstants.STROKE).equals(color);	


		}else
		{
			shadow_color=true;
			condition_color = polyLine.get(0).getAttribute(NSGenericConstants.STROKE).equals(color);	
			condition_width = polyLine.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
		}

		if(condition_color && condition_width && shadow_color)
			status=true;

		return status;
	}

	public int getFocusedPolylineNumber(){

		List<WebElement> ele = getAllPolylines(1);
		int polylineNumber=0;
		for (int i = 1; i<= ele.size(); i++) {
			if(verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, i)){
				polylineNumber=i;
				break;
			}
		}
		return polylineNumber;	
	}

	public boolean verifyHaloRingIsDisplayed() {


		boolean elementPresent= isElementPresent(By.cssSelector(".highlightPt")); 
		boolean radius = (getElement(By.cssSelector(".highlightPt")).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.POINT_CIRCLE_RADIUS));
		boolean color = (getElement(By.cssSelector(".highlightPt")).getCssValue(NSGenericConstants.STROKE).equals("rgb(182, 255, 0)"));
		return elementPresent && radius && color;



	}

	public boolean verifyTextCommentWhileMovingPolyline(int whichViewbox,int whichPolyline,int xOffset,int yOffset,String expectedComment,int totalComment,boolean classicOrFree) throws InterruptedException{

		boolean status=false;
		List<WebElement> handles;
		int [] pixelArrayXoffset, pixelArrayYoffset;
		if(classicOrFree)
			handles = getTransparentPolylineHandles(whichViewbox, whichPolyline);
		else
			handles = getLinesOfPolyLine(whichViewbox, whichPolyline);  

		Actions action = new Actions(driver);
		action.moveToElement(handles.get(whichPolyline)).keyDown(Keys.CONTROL).clickAndHold().keyUp(Keys.CONTROL).build().perform();

		int hopValue= Integer.parseInt(TEST_PROPERTIES.get("hop"));
		pixelArrayXoffset= getSmallerPixelValuesForHopping(xOffset, hopValue);
		pixelArrayYoffset= getSmallerPixelValuesForHopping(yOffset, hopValue);

		LOGGER.info("drag and drop...clickAndHold");
		for(int i= 0; i <(pixelArrayXoffset.length-1); i++) {
			action.moveByOffset(pixelArrayXoffset[i], pixelArrayYoffset[i]).perform();

			if ((getElements(By.cssSelector("[ns-polyline]:nth-child("+whichViewbox+") g[ns-comment] > text:nth-child(2)")).size()!=totalComment))
			{
				status=!isElementPresent(By.cssSelector("[ns-polyline]:nth-child("+whichViewbox+") g[ns-comment] > text:nth-child(2)"));
				LOGGER.info("drag and drop...moveByOffset");
			}
		}
		action.release().build().perform();
		action.moveByOffset(1, 1).perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);

		return status;

	}

	public boolean isFreeHandPolylineSelected(int whichViewbox, int whichPolyline) {

		boolean status = false;
		WebElement polyLine =getAllPolylines(whichViewbox).get(whichPolyline-1);
		try{
			boolean shadow_color = isElementPresent(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));


			if(shadow_color)
				status=true;
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}

	public int getPolylineWhichIsFocused(int whichViewbox) {


		List<WebElement> allPolylines = getAllPolylines(whichViewbox);
		int val =-1;
		for(int i =0 ;i<allPolylines.size();i++) {
			if(isFreeHandPolylineSelected(whichViewbox, i+1)) {
				val=i+1;
				break;
			}

		}

		return val;
	}

	public boolean verifyPolylineIsActiveGSPS(int whichViewbox, int whichPolyline, String whichColor, String opacity) {

		boolean status = false;
		WebElement polyLine =getAllPolylines(whichViewbox).get(whichPolyline-1);
		try{
			status =isElementPresent(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+whichColor+"']")));
			status = status && isElementPresent(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")));
			status = status && getAttributeValue(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+whichColor+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);
			status = status && getAttributeValue(polyLine.findElement(By.cssSelector("path["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.SHADOW_COLOR+"']")),NSGenericConstants.STROKE_OPACITY).equals(opacity);

		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}

	public boolean verifyPolylineIsInActiveGSPS(int whichViewbox, int whichPolyline, String whichColor, String opacity) {

		boolean status = false;
		WebElement polyLine =getPolyLineSvg(whichViewbox, whichPolyline);
		try{
			status = getHexColorValue(getAttributeValue(polyLine, NSGenericConstants.STROKE)).equals(getHexColorValue(whichColor));
			status = status && getAttributeValue(polyLine, NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
			status = status && getAttributeValue(polyLine, NSGenericConstants.STROKE_OPACITY).equals(opacity);
		
		}
		catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;

		
		
	}
	
	public boolean verifyLabeling(int whichViewbox, int whichPoly, String label) {
		
		return getLabel(whichViewbox, whichPoly).equalsIgnoreCase(label);
		
	}
	
	public String getLabel(int whichViewbox, int whichPoly) {
		
		WebElement tag = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-polyline] + g[ns-roilabel] text:nth-child(2)")).get(whichPoly-1);
		
		return getText(tag);
		
	}
}
