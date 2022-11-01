package com.trn.ns.page.factory;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.Utilities;


public class OutputPanel extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public OutputPanel(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	//outputPanel Component declarartion
	public By byOutputPanel = By.className("outputpanel");
	public By opActiveTab  = By.cssSelector("div[id^='mat-tab-label-'][id$='0']");
	public By spinningWheel = By.cssSelector("#container  ns-spinner");
	public By opSpinner = By.cssSelector("[id*='mat-tab-content'] ns-spinner div.loader");
	public By findingToolbar = By.cssSelector("div.finding-tile-toolbar-container div");

	@FindBy(css="div.outputpanel")
	public WebElement outputPanelSection;

	@FindBy(css="#msgDiv-default-alert")
	public WebElement warningPopUp;
	
	@FindBy(css="#editabletext")
	public WebElement editableText;
	
	@FindBy(css="#studySummary div.studySummary-description")
	public WebElement studySummary;

	@FindBy(css="#studySummary div.studySummary-count")
	public WebElement findingSummaryCount;

	@FindBy(css="div[id^='mat-tab-label-'][id$='0']")
	public WebElement opTabOpened;

	@FindBy(css="div[id^='mat-tab-label-'][id$='1']")
	public WebElement seriesTabOpened;

	@FindBy(css="#container > div.toolbar > div.optionIcons > ns-icon:nth-child(1) > div > svg")
	public WebElement filterFindingsIcon;

	@FindBy(css=".option:nth-child(1) svg rect")
	public WebElement acceptedButton;	

	@FindBy(css=".option:nth-child(2) svg rect")
	private  WebElement rejectedButton;

	@FindBy(css=".option:nth-child(3) svg rect")
	private WebElement pendingButton;


	@FindBy(css=".option:nth-child(1) svg")
	private WebElement accepted;	

	@FindBy(css=".option:nth-child(2) svg")
	private  WebElement rejected;

	@FindBy(css=".option:nth-child(3) svg ")
	private WebElement pending;

	@FindBy(css="div.dockedTabContainer div:nth-child(2)")
	public WebElement outputPanelTab;

	@FindBy(css="ns-icon#iconMinimizeToolWindows svg#icn-eureka-minimize")
	public WebElement outputPanelMinimizeIcon;

	@FindBy(css="#container > div.toolbar > div.optionIcons > ns-icon:nth-child(2) > div > svg")
	public WebElement syncAllFindingsIcon;
	
	@FindBy(css="#container > div.toolbar > div.optionIcons > ns-icon:nth-child(2) > div")
	public WebElement copyToClipIcon;

	@FindBys({@FindBy(css="div#thumb-nail")})
	public List<WebElement> thumbnailList;

	@FindBys({@FindBy(css="div.finding-tile-toolbar-container")})
	public List<WebElement> findingTileContainers;
	
	@FindBys({@FindBy(css="div.finding-tile-toolbar-container div")})
	public List<WebElement> toolbar;

	@FindBys({@FindBy(css="ns-icon#comment svg")})
	public List<WebElement> commentIconOnTiles;

	@FindBys({@FindBy(css=".toolbar-left-content ns-icon svg path")})
	public List<WebElement> stateIndictorTiles;
		
	@FindBys({@FindBy(css="ns-icon#pacs svg g")})
	public List<WebElement> sendToPacsIcons;

	@FindBys({@FindBy(css="div.ellipses.list-style div")})
	public List<WebElement> textAnnotationTextList;

	@FindBys({@FindBy(css="#thumb-nail > div.viewbox-lite-annotation > ns-textoverlay > svg > g > g > text:nth-child(4)")})
	public List<WebElement> thumbailMeasurementTextList;

	@FindBy(css=".viewbox-lite-jump-icon svg#JumpTo")
	public List <WebElement> jumpToFindingIcon;

	@FindBy(css="div.icon-thumbnail-container")
	public WebElement srThumbnail;

	@FindBy(css="div.selectAll ns-icon rect.unchecked-outline")
	public WebElement deSelectedAllIcon;

	@FindBy(css="div.selectAll ns-icon rect.checked-background")
	public WebElement selectAllIcon;
	
	@FindBy(css="div.selectAll  label > span > span")
	public WebElement selectAllText;

	@FindBys({@FindBy(css="g[ns-text] > text:nth-child(2)")})
	public List<WebElement> scResultComment;

	@FindBy(css="#container > div:nth-child(4) > perfect-scrollbar > div > div.ps__rail-y")
	public WebElement verticalScrollBarInOP;
	
	@FindBy(css="#container > div:nth-child(4) > perfect-scrollbar > div > div.ps__rail-y > div")
	public WebElement verticalScrollThumbInOP;

	@FindBy(css="ns-finding-tile > div > ns-finding-comment > div > perfect-scrollbar > div")     
	public WebElement commentSection;
	
	@FindBy(css="#container ns-finding-comment > div > perfect-scrollbar > div > div.ps__rail-y")
	public WebElement commentScroll;
	
	@FindBy(css="#container ns-finding-comment > div > perfect-scrollbar > div > div.ps__rail-y > div")
	public WebElement commentThumb;
	
	@FindBy(css="#container > div:nth-child(4) > perfect-scrollbar > div > div.ps__rail-y")
	public WebElement scrollbar;
	
	@FindBy(css="#container > div:nth-child(4) > perfect-scrollbar > div > div.ps__rail-y > div")
	public WebElement scrollThumb;
	
	// Elements

	public WebElement getThumbnailImage(int whichFinding){

		return driver.findElement(By.id("thumbnailImage-"+(whichFinding-1)));
	}

	public  WebElement getCommentLabelForFindings(int whichThumbnail) throws InterruptedException{

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));		
		WebElement commentBox = driver.findElement(By.cssSelector("div#editabletext #label"));

		return commentBox;

	}

	public  WebElement getCommentForFindings(int whichThumbnail) throws InterruptedException{

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));		
		WebElement commentBox = driver.findElement(By.cssSelector("div#editabletext"));

		return commentBox;

	}

	public  WebElement getFindingStateIndicator(int whichThumbnail) throws InterruptedException{

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		return stateIndictorTiles.get(whichThumbnail-1);		

	}

	public  List<WebElement> getSliceComments(int whichThumbnail) throws InterruptedException{

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));		
		List<WebElement> commentBox = driver.findElements(By.xpath("//*[contains(text(),'Slice ')]/following-sibling::div//*//*[@id='editabletext']"));

		return commentBox;

	}

	public  WebElement getLineOfTextAnnotationsInOutPutPanel(int whichThumbnail){

		WebElement elements = driver.findElement(By.cssSelector("#finding-"+(whichThumbnail-1)+" line[stroke-dasharray]"));
		return elements;

	}

	public WebElement getThumbnailMeasurement(int thumbnail){

		return driver.findElement(By.cssSelector("#finding-"+thumbnail+" div.viewbox-lite-annotation > ns-textoverlay > svg > g"));
	}

	public List<WebElement> getAnnotationsFromThumnailCineSlow(int whichThumbnail) throws InterruptedException {

		Actions action = new Actions(driver);
		action.moveToElement(thumbnailList.get(whichThumbnail-1)).moveByOffset(30, 30).build().perform();	
		action.pause(30).moveToElement(findingTileContainers.get(whichThumbnail-1)).perform();

		List<WebElement> annotations = new ArrayList<WebElement>();
		WebElement thumbnail = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]"));
		annotations=thumbnail.findElements(By.cssSelector("g[ns-measurements]>g"));
		return annotations;


	}

	public int getCountOfLinesInPolylineInThumbnail(int whichThumbnail) {

		WebElement polyline = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElement(By.cssSelector("g[ns-measurements] g[ns-polyline] path"));
		String coordinates = getAttributeValue(polyline, ViewerPageConstants.ATTRIBUTE_D);		
		return coordinates.split(",").length-1;		
	}

	public  List<WebElement> getNotAddedCommentsIcon() throws InterruptedException{

		mouseHover(findingTileContainers.get(0));
		List<WebElement> commentBox = driver.findElements(By.cssSelector(".icon-highlight.no-comment#comment"));

		return commentBox;

	}

	public  List<WebElement> getCommentAddedIcon() throws InterruptedException{

		mouseHover(findingTileContainers.get(0));
		List<WebElement> commentBox = driver.findElements(By.cssSelector(".icon-highlight#comment"));

		return commentBox;

	}

	public WebElement getThumbnailCheckbox(int whichThumbnail) {

		return driver.findElement(By.cssSelector("#finding-"+(whichThumbnail-1)+"> div.checkbox > label > span > ns-icon > div > svg rect"));
	}

	// void

	public void enableFiltersInOutputPanel(boolean acceptOption, boolean rejectOption,boolean pendingOption) throws InterruptedException {

		openAndCloseOutputPanel(true);
		waitForElementInVisibility(spinningWheel);
		click(filterFindingsIcon);
		waitForElementVisibility(By.cssSelector("ns-findings-filter .container"));

		if(rejectOption) {
			if(!rejectedButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				clickUsingAction(rejected);

		}else
			if(rejectedButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				clickUsingAction(rejected);

		if(pendingOption) {
			if(!pendingButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				clickUsingAction(pending);

		}else
			if(pendingButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				clickUsingAction(pending);

		if(acceptOption) {
			if(!acceptedButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				click(accepted);
		}
		else {

			if(acceptedButton.getAttribute(NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(NSGenericConstants.CHECKED_BACKGROUND))
				click(accepted);

		}
		click(studySummary);
		waitForElementInVisibility(By.cssSelector("ns-findings-filter .container"));
		waitForElementVisibility(syncAllFindingsIcon);
	}

	public void waitForOutputPanelToLoad() throws InterruptedException {

		waitForElementInVisibility(opSpinner);
		waitForElementVisibility(filterFindingsIcon);
		waitForElementVisibility(opActiveTab);
		waitForElementVisibility(minimizeIcon);
		
	}

	public void waitForThumbnailToGetDisplayed() throws InterruptedException {

		waitForOutputPanelToLoad();
		waitForElementsVisibility(By.cssSelector("div#thumb-nail"));
		
	}
	
	public void clickOnJumpIcon(int findingNumber) throws InterruptedException
	{
		if(!thumbnailList.get(findingNumber-1).isDisplayed())
			scrollDownUsingPerfectScrollbar(thumbnailList.get(findingNumber-1),verticalScrollThumbInOP, 10, getHeightOfWebElement(verticalScrollBarInOP));
		clickUsingAction(thumbnailList.get(findingNumber-1));
		waitForTimePeriod(4000);
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on Jump Icon "+findingNumber+" successfully.");

	}

	public void mouseHoverOnThumbnail(int findingNumber) throws InterruptedException
	{
		mouseHover(VISIBILITY,thumbnailList.get(findingNumber-1));
		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hover on Thumbnail "+findingNumber+" successfully.");

	}

	public void mouseHoverOnJumpIcon(int findingNumber) throws InterruptedException
	{
		Actions action = new Actions(driver);
		action.moveToElement(thumbnailList.get(findingNumber-1)).moveByOffset(30, 30).build().perform();		
		action.moveByOffset(-30, -30).perform();

		LOGGER.info(Utilities.getCurrentThreadId() + "Mouse hover on Thumbnail "+findingNumber+" successfully.");

	}

	public void clickOnThumbnail(int findingNumber) throws InterruptedException
	{

		if(!thumbnailList.get(findingNumber-1).isDisplayed())
			scrollDownUsingPerfectScrollbar(thumbnailList.get(findingNumber-1),verticalScrollThumbInOP, 10, getHeightOfWebElement(verticalScrollBarInOP));
		
		Actions action = new Actions(driver);
		action.moveToElement(thumbnailList.get(findingNumber-1)).moveByOffset(30, 30).build().perform();	
		action.moveByOffset(-30, -30).click().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on Thumbnail "+findingNumber+" successfully.");

	}

	public void clickOnThumbnailNotAtCenter(int findingNumber) 
	{

		Actions action = new Actions(driver);
		action.moveToElement(thumbnailList.get(findingNumber-1)).moveByOffset(30, 30).click().build().perform();
		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on Thumbnail "+findingNumber+" successfully.");

	}

	public void clickOnNonDICOMThumbnail(int findingNumber) throws InterruptedException
	{


		Actions action = new Actions(driver);

		try {

			WebElement ele = thumbnailList.get(findingNumber-1).findElement(By.cssSelector(".nonDicom-container > img"));
			action.moveToElement(ele).moveByOffset(1, 1).build().perform();

		}
		catch(Exception e){
			WebElement ele = thumbnailList.get(findingNumber-1).findElement(By.cssSelector("#pdf-viewer"));
			action.moveToElement(ele).moveByOffset(1, 1).build().perform();

			try {
				Robot r = new Robot();
				for(int i=5;i<100;i++) {
					r.mouseMove(ele.getLocation().getX()+140, ele.getLocation().getY()+100+i);
					if(isElementPresent(By.cssSelector("#Arrow")))
						break;
				}
			}catch(AWTException ex) {}
		}


		WebElement element = getElement(By.cssSelector("#Arrow"));
		action.click(element).perform();	

		LOGGER.info(Utilities.getCurrentThreadId() + "Clicked on Thumbnail "+findingNumber+" successfully.");

	}

	public void stopCineOnThumbnail(int whichThumbnail) throws InterruptedException {

		mouseHover(thumbnailList.get(whichThumbnail-1));	
	}

	public void editSliceComment(int whichThumbnail, int whichSlice, boolean pressEnter,String... comment) throws InterruptedException {


		WebElement commentBox = getSliceComments(whichThumbnail).get(whichSlice-1);;

		Actions action = new Actions(driver);
		action.click(commentBox).sendKeys(Keys.END).build().perform();

		for(int i=0;i<comment.length;i++) {
			waitForTimePeriod(100);
			action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER).keyUp(Keys.SHIFT).build().perform();
			waitForTimePeriod(1000);
			commentBox.sendKeys(comment[i]);
		}

		if(pressEnter)
			pressKeys(Keys.ENTER);

	}

	public void editSliceComment(int whichThumbnail, String whichSlice, boolean pressEnter,String... comment) throws InterruptedException {


		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));
		WebElement commentBox = getElement(By.xpath("//*[text()='"+whichSlice+"']/..//*[@id='editabletext']"));

		Actions action = new Actions(driver);
		action.click(commentBox).sendKeys(Keys.END).build().perform();

		for(int i=0;i<comment.length;i++) {
			waitForTimePeriod(100);
			commentBox.sendKeys(comment[i]);
			action.click(commentBox).sendKeys(Keys.END).build().perform();
		}

		if(pressEnter)
			pressKeys(Keys.ENTER);
		waitForTimePeriod(100);

	}

	public boolean addCommentFromOutputPanel(int whichThumbnail,String comment) throws InterruptedException {

		WebElement commentBox = getCommentForFindings(whichThumbnail);
		click(commentBox);
		boolean noLabelPresence = isElementPresent(By.cssSelector("#editabletext #label"));		
		WebElement editbox = getElement(By.cssSelector("#editabletext"));

		String noTextPresent = getText(editbox);        
		editbox.sendKeys(comment);
		pressKeys(Keys.ENTER);
		waitForTimePeriod(3000);
		return !noLabelPresence && noTextPresent.isEmpty();
	}

	public boolean addCommentFromOutputPanel(int whichThumbnail, String... comment) throws InterruptedException 
	{

		WebElement commentBox = getCommentForFindings(whichThumbnail);
		click(commentBox);
		boolean noLabelPresence = isElementPresent(By.cssSelector("#editabletext #label"));
		WebElement editbox = getElement(By.cssSelector("#editabletext"));
		String noTextPresent = getText(commentBox);    
		editbox.sendKeys(comment[0]);
		for(int i=1; i<comment.length;i++) {            
			String keysPressed = Keys.chord(Keys.LEFT_SHIFT, Keys.ENTER);
			editbox.sendKeys(keysPressed);
			enterText(comment[i]);
		}
		pressKeys(Keys.ENTER);
		return !noLabelPresence && noTextPresent.isEmpty();
	}

	public void deleteCommentFromOutputPanel(int whichThumbnail, boolean pressEnter) throws InterruptedException {

		WebElement commentBox = getCommentForFindings(whichThumbnail);
		click(commentBox);

		WebElement editbox = getElement(By.cssSelector("#editabletext"));

		editbox.clear();
		if(pressEnter)
			pressKeys(Keys.ENTER);

		waitForTimePeriod(2000);
	}

	public void editCommentFromOutputPanel(int whichThumbnail,boolean pressEnter, String... comment) throws InterruptedException {

		WebElement commentBox = getCommentForFindings(whichThumbnail);

		Actions action = new Actions(driver);
		action.click(commentBox).sendKeys(Keys.END).build().perform();

		for(int i=0;i<comment.length;i++) {
			waitForTimePeriod(100);
			action.keyDown(Keys.SHIFT).sendKeys(Keys.ENTER).keyUp(Keys.SHIFT).build().perform();
			waitForTimePeriod(1000);
			commentBox.sendKeys(comment[i]);
		}

		if(pressEnter)
			pressKeys(Keys.ENTER);

	}

	public void editCommentFromOutputPanel(int whichThumbnail,int position, String comment, boolean pressEnter) throws InterruptedException {

		WebElement commentBox = getCommentForFindings(whichThumbnail);
		Actions action = new Actions(driver);
		action.click(commentBox).sendKeys(Keys.HOME).build().perform();

		for(int i=0;i<position;i++) {
			action.sendKeys(Keys.ARROW_RIGHT).perform();

		}
		action.sendKeys(comment).perform();

		if(pressEnter)
			pressKeys(Keys.ENTER);
	}

	public boolean openAndCloseOutputPanel(boolean openOrClose) throws InterruptedException{

		boolean flag = false;
		if(openOrClose){ //opening the panel if parameter is passed as true

			if(!isElementPresent(By.cssSelector("div#container"))) 
			{
				if(isElementPresent(By.cssSelector("ns-series-panel > ns-nested-view > perfect-scrollbar")))
					click(getElement(By.cssSelector("div.mat-tab-list > div>div:nth-child(1)")));
				else
				{
					if(!isElementPresent(outputPanelSection))
					click(opTab);
				}
				
			}
			waitForOutputPanelToLoad();

			flag = false;
			LOGGER.info(Utilities.getCurrentThreadId() + "viewer output panel opened");
		}
		else{
			LOGGER.info(Utilities.getCurrentThreadId() + "Checking for viewer output panel open");
			if(isElementPresent(outputPanelSection)){
				clickUsingAction(minimizeIcon);
				LOGGER.info(Utilities.getCurrentThreadId() + "viewer output panel close");
				flag = true;
			}
		}
		return flag;

	}

	public boolean verifyOutputPanelIsOpened() {

		return isElementPresent(By.cssSelector("div#container")) && isElementPresent(opActiveTab) ;
	}

	public boolean verifyRoundedCorner(WebElement item, String cssParam, String value) {

		boolean status = true;

		String rounderCorner = getCssValue(item, cssParam);
		status = status && rounderCorner.equals(value);

		return status;

	}

	public boolean playCineOnThumbnail(int findingNumber) throws InterruptedException
	{

		Actions action = new Actions(driver);
		action.moveToElement(thumbnailList.get(findingNumber-1),35, 35).build().perform();	
		LOGGER.info(Utilities.getCurrentThreadId() + "playing cine on Thumbnail "+findingNumber+" successfully.");
		return getElements(By.cssSelector("div.viewbox-lite-jump-icon ns-icon")).get(findingNumber-1).getAttribute("style").contains("fill-opacity:0.0");

	}

	public boolean verifyAnnotationsPresenceInThumbnail(int whichThumbnail) throws InterruptedException {

		playCineOnThumbnail(whichThumbnail);
		boolean status = true;
		List<WebElement> annotations = new ArrayList<WebElement>();
		for(int a=0;a<20;a++) {
			annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g"));
			if(annotations.size()==0) {			
				status = false;
				break;
			}

		}

		return status;


	}

	public boolean verifyThumbnailInOutputPanel(int thumbnailCount) throws InterruptedException {

		boolean status = false;

		try{

			if(Integer.parseInt(getThumbnailImage(thumbnailCount).getAttribute(NSGenericConstants.HEIGHT)) > 0 && Integer.parseInt(getThumbnailImage(thumbnailCount).getAttribute(NSGenericConstants.WIDTH))>0 )
				status = true;
			else
				status= Integer.parseInt(driver.findElement(By.cssSelector("#finding-"+(thumbnailCount-1)+" .nonDicom-container > img")).getAttribute(NSGenericConstants.HEIGHT))>0 ;
		}
		catch (Exception e) 
		{
			WebElement pdf=driver.findElement(By.cssSelector("#pdf-viewer div.page"));
			status=Integer.parseInt(pdf.getCssValue(NSGenericConstants.WIDTH).replace("px", "")) > 0 && Integer.parseInt(pdf.getCssValue(NSGenericConstants.HEIGHT).replace("px", ""))>0 ;

		}
		return status;
	}

	public boolean verifyPolyLineIsPresentInThumbnail(int whichThumbnail) {

		boolean status = true;
		List<WebElement> annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g[ns-polyline]"));
		if(annotations.isEmpty()) {			
			status = false;				
		}
		return status;

	}

	public boolean verifySliceLabelAndComment(int whichThumbnail,String sliceLable,String comment) throws InterruptedException
	{
		boolean status=false;
		Set set = getAllSliceLabelsAndComments(whichThumbnail).entrySet();

		// Get an iterator
		Iterator i = set.iterator();
		while(i.hasNext()) 
		{
			Map.Entry sliceLabelAndComment = (Map.Entry)i.next();

			status=(sliceLabelAndComment.getKey().toString()).equalsIgnoreCase(sliceLable);
			status=status && (sliceLabelAndComment.getValue().toString()).equalsIgnoreCase(comment);

		}
		return status;


	}

	public boolean verifyLineBreakTagPresence(int whichThumbnail) throws InterruptedException {

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));
		return isElementPresent(By.cssSelector("#editabletext br"));


	}

	public String getCommentText(int whichThumbnail) throws InterruptedException {

		WebElement commentBox = getCommentForFindings(whichThumbnail);
		String comment =getText(commentBox);
		click(studySummary);
		return comment;

	}

	public String getSliceLabelText(int whichThumbnail) throws InterruptedException {
		getCommentForFindings(whichThumbnail);
		WebElement commentBox = getElement(By.cssSelector("ns-finding-level-comment div.row>div:nth-child(1)"));
		return getText(commentBox);
	}

	public void deleteSliceComment(int whichThumbnail, int whichSlice, boolean pressEnter) throws InterruptedException {

		WebElement commentBox = getSliceComments(whichThumbnail).get(whichSlice-1);
		commentBox.click();
		commentBox.clear();
		if(pressEnter)
			pressKeys(Keys.ENTER);
	//	return getText(commentBox);
	}

	public List<String> getMultiLineTextEntriesInOP(int whichThumbnail) throws InterruptedException{		

		List<String> lst2 = new ArrayList<String>();

		getCommentForFindings(whichThumbnail);
		for(WebElement item : textAnnotationTextList) {

			if(!item.isDisplayed())
				scrollDownUsingPerfectScrollbar(item, commentThumb, 10, getHeightOfWebElement(commentScroll));
			lst2.add(getText(item));			

		}
		return lst2;

	}

	public List<String> getMultiLineCommenEntriesInOP(int whichThumbnail) throws InterruptedException{		

		List<String> lst2 = new ArrayList<String>();

		WebElement item = getCommentForFindings(whichThumbnail);

		List<WebElement> elements =item.findElements(By.xpath("span"));

		for(int i =0 ;i<elements.size();i++) {

			scrollIntoView(elements.get(i));
			lst2.add(getText(elements.get(i)));	
		}
		return lst2;

	}

	public String getMeasurementTextFromThumnail(int whichThumbnail) throws InterruptedException {

		String text = new String();
		WebElement thumbnail = thumbailMeasurementTextList.get(whichThumbnail-1);
		text=thumbnail.getText();
		return text;
	}

	public String getTextLabelValFromThumbnail(int whichThumbnail) throws InterruptedException {


		getCommentForFindings(whichThumbnail);		
		return getText(driver.findElement(By.xpath("//*[contains(text(),'"+ViewerPageConstants.OUTPUT_PANEL_TEXT_LABEL+"')]")));


	}

	public Map<String,String> getAllSliceLabelsAndComments(int whichThumbnail) throws InterruptedException {

		mouseHover(findingTileContainers.get(whichThumbnail-1));
		click(commentIconOnTiles.get(whichThumbnail-1));		
		
		List<WebElement> labels = getElements(By.cssSelector("ns-finding-level-comment div.row>div:nth-child(1)"));
		List<WebElement> comments = getElements(By.cssSelector("ns-finding-level-comment div.row>div:nth-child(2)"));

		Map<String,String> sliceLabelAndComments = new LinkedHashMap<String,String>();

		for(int i =0 ;i<labels.size();i++)
			sliceLabelAndComments.put(getText(labels.get(i)),getText(comments.get(i)));

		return sliceLabelAndComments;
	}
	
	public List<String> getAllSliceComments(int whichThumbnail) throws InterruptedException {

		return convertWebElementToStringList(getSliceComments(whichThumbnail));
	}

	public int getThumbnailForGivenResult(String selectedFindingName) throws InterruptedException {

		int thumbnail =-1;
		for(int i =1;i<=thumbnailList.size();i++) {

			if(!thumbnailList.get(i-1).isDisplayed())
				scrollDownUsingPerfectScrollbar(thumbnailList.get(i-1),verticalScrollThumbInOP, 10, getHeightOfWebElement(verticalScrollBarInOP));
			
			clickOnJumpIcon(i);
			click(getViewPort(getActiveViewbox()));
			clickOnJumpIcon(i);
			if(getSelectedFindingName().equalsIgnoreCase(selectedFindingName)) {
				thumbnail=i;
				break;
			}
		}

		return thumbnail;
	}

	public int getThumbnailForNonDICOM(String selectedFindingName) throws InterruptedException {

		int thumbnail =-1;
		for(int i =1;i<=thumbnailList.size();i++) {

			if(!thumbnailList.get(i-1).isDisplayed())
				scrollDownUsingPerfectScrollbar(thumbnailList.get(i-1),verticalScrollThumbInOP, 10, getHeightOfWebElement(verticalScrollBarInOP));
			
			clickOnJumpIcon(i);
			int vieweport = getActiveViewbox();
			
			if(getSeriesDescriptionOverlayText(vieweport).equalsIgnoreCase(selectedFindingName)) {
				thumbnail=i;
				break;
			}
		}

		return thumbnail;
	}

	public Map<String, Integer> getThumbnailForNonDICOM() throws InterruptedException {

		
		Map<String, Integer> thumbnail = new HashMap<String, Integer>();
		
		for(int i =1;i<=thumbnailList.size();i++) {

			if(!thumbnailList.get(i-1).isDisplayed())
				scrollDownUsingPerfectScrollbar(thumbnailList.get(i-1),verticalScrollThumbInOP, 10, getHeightOfWebElement(verticalScrollBarInOP));
			
			clickOnJumpIcon(i);
			int viewport = getActiveViewbox();			
			thumbnail.put(getSeriesDescriptionOverlayText(viewport), i);
			
			
		}

		return thumbnail;
	}
	
	public void checkSelectAllOption() {

		List<WebElement> selectAllIcons = getElements(By.cssSelector("div.selectAll ns-icon svg"));
		for(WebElement icon : selectAllIcons) {
			if(getAttributeValue(icon.findElement(By.cssSelector("rect")),NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(ViewerPageConstants.SELECT_ALL_ICON_DESELECTED)) {
				click(icon);
				break;
			}
		}
	}

	public void uncheckSelectAllOption() {

		List<WebElement> selectAllIcons = getElements(By.cssSelector("div.selectAll ns-icon svg"));
		for(WebElement icon : selectAllIcons) {
			if(getAttributeValue(icon.findElement(By.cssSelector("rect")),NSGenericConstants.CLASS_ATTR).equalsIgnoreCase(ViewerPageConstants.SELECT_ALL_ICON_SELECTED)) {
				click(icon);
				break;
			}
		}
	}

	public boolean verifyStudyDescAndFindingCount(String studyDesc, int count) {

		boolean status = false;
		if(!studyDesc.isEmpty())
			status =getText(studySummary).equalsIgnoreCase(studyDesc);
		else
			status =getText(studySummary).equalsIgnoreCase("Study name not available");
		status = status && getText(findingSummaryCount).contains(count+"");

		return status;
	}

	public WebElement getAnnotationFromThumbnail(int whichThumbnail) throws InterruptedException {

		WebElement annotation = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElement(By.cssSelector("g[ns-measurements] g"));
			
		return annotation;


	}
	
	public String getContourColorThumbnail(int whichThumbnail) throws InterruptedException {

		WebElement annotation = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElement(By.cssSelector("g[ns-measurements] g[ns-polyline] svg path"));
			
		 String value  =getCssValue(annotation, NSGenericConstants.STROKE);
		return value;


	}
	
	public boolean verifyCommentIconPresence(int whichThumnail) throws InterruptedException {
		
		mouseHover(findingTileContainers.get(whichThumnail-1));
    	boolean status = isElementPresent(By.cssSelector("#finding-"+(whichThumnail-1)+" ns-icon#comment"));
    	mouseHover(studySummary);
    	return status;
 
		
	}
	
	public boolean verifyAcceptedStateInThumbnail(int whichThumbnail) throws InterruptedException {
		
		mouseHoverOnStateIcon(whichThumbnail);
		boolean status = getText(tooltip).equalsIgnoreCase(ViewerPageConstants.ACCEPTED_TEXT);
		List<WebElement> stateIcon = getElements(By.cssSelector(".toolbar-left-content ns-icon svg"));
		status = status && getCssValue(stateIcon.get(whichThumbnail-1).findElement(By.cssSelector("path")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.SUCCESS_ICON_COLOR);

		return status ;
	}
	
	public boolean verifyRejectedStateInThumbnail(int whichThumbnail) throws InterruptedException {
		
		mouseHoverOnStateIcon(whichThumbnail);
		boolean status = getText(tooltip).equalsIgnoreCase(ViewerPageConstants.REJECTED_TEXT);
		List<WebElement> stateIcon = getElements(By.cssSelector(".toolbar-left-content ns-icon svg"));
		status = status && getCssValue(stateIcon.get(whichThumbnail-1).findElement(By.cssSelector("g")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.ERROR_ICON_COLOR);

		return status ;
	}
	
	public boolean verifyPendingStateInThumbnail(int whichThumbnail) throws InterruptedException {
		
		mouseHoverOnStateIcon(whichThumbnail);
		boolean status = getText(tooltip).equalsIgnoreCase(ViewerPageConstants.PENDING_TEXT);
		List<WebElement> stateIcon = getElements(By.cssSelector(".toolbar-left-content ns-icon svg"));
		status = status && getCssValue(stateIcon.get(whichThumbnail-1).findElement(By.cssSelector("g use")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.PENDING_ICON_COLOR);
		
		return status ;
	}
	
	private void mouseHoverOnStateIcon(int whichThumbnail) throws InterruptedException {
		
		mouseHover(findingTileContainers.get(whichThumbnail-1));
		mouseHover(getElements(By.cssSelector(".toolbar-left-content ns-icon svg")).get(whichThumbnail-1));
	
	}
	
	public boolean verifyRTFindingsWithGSPSInOuputPanel(boolean accept, boolean reject, boolean pending , List<String> findings) throws InterruptedException {

        PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		enableFiltersInOutputPanel(accept, reject, pending);
		boolean status= (findings.size()==thumbnailList.size());  
		waitForTimePeriod(4000);

		for(int i =1;i<thumbnailList.size();i++) {
			
			WebElement ele=driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(i-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]"));
			if((ele.findElement(By.cssSelector("svg:not([ns-polyline]) path"))).isDisplayed())
			{
				status=status && isElementPresent(thumbnailList.get(i-1));
			}
			else if (ele.findElement(By.cssSelector("svg:not([ns-polyline]) path")).isDisplayed())
			{
				clickOnJumpIcon(i);
				status=status && (poly.getLinesOfSelectedRTPolyLine(poly.getSelectedRTPolyLine(1)).size()==getCountOfLinesInPolylineInThumbnail(i));
				status=status && isElementPresent(thumbnailList.get(i-1));	
			}
		}
	    return status;

	}

	public boolean verifyCircleIsPresentInThumbnail(int whichThumbnail) {

		boolean status = true;
		List<WebElement> annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g[ns-circle]"));
		if(annotations.isEmpty()) {			
			status = false;				
		}
		return status;

	}
	
	public boolean verifyEllipseIsPresentInThumbnail(int whichThumbnail) {

		boolean status = true;
		List<WebElement> annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g[ns-ellipse]"));
		if(annotations.isEmpty()) {			
			status = false;				
		}
		return status;

	}
	
	public boolean verifyPointIsPresentInThumbnail(int whichThumbnail) {

		boolean status = true;
		List<WebElement> annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g[ns-point]"));
		if(annotations.isEmpty()) {			
			status = false;				
		}
		return status;

	}
	
	public boolean verifyLineIsPresentInThumbnail(int whichThumbnail) {

		boolean status = true;
		List<WebElement> annotations = driver.findElement(By.xpath("//*[@id='thumbnailCanvas-"+(whichThumbnail-1)+"']/../div[contains(@class,'viewbox-lite-annotation')]")).findElements(By.cssSelector("g[ns-measurements] g[ns-line]"));
		if(annotations.isEmpty()) {			
			status = false;				
		}
		return status;

	}
	
	public String getROILabelInThumbnail(int whichThumbnail) {

		WebElement label = getElement(By.cssSelector("[id*='finding-"+(whichThumbnail-1)+"'] g +g[ns-roilabel] text:nth-child(2)"));
		return getText(label);

	}
	
	public boolean verifyROILabelsInThumbnail(int whichThumbnail,String label) {

		return getROILabelInThumbnail(whichThumbnail).equalsIgnoreCase(label);

	}
}

