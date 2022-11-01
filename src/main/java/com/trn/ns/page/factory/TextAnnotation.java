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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.ExtentManager;
public class TextAnnotation extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public TextAnnotation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// functions related to text editor

	
	@FindBy(css="div.resizer.bottom-right")
	public WebElement resizeIconOnEditBox;
	
	public void selectTextArrowFromQuickToolbar(int whichViewbox) {

		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(textArrowIcon);
		click(textArrowIcon);
		waitForElementInVisibility(quickToolbar);

	}


	public List<WebElement> getTextAnnotations(int whichViewbox){

		List<WebElement> texts = new ArrayList<WebElement>();
		texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text]")));

		return texts;
	}

	public List<WebElement> getTextOfComment(int whichViewbox){
		List<WebElement> texts = new ArrayList<WebElement>();
		texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] g[ns-comment] text:not([fill='black'])")));
		//		if(texts.size()==0)
		//			texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" >  g > g > g > g > g > g:nth-child(3) > text:nth-child(2) ")));
		return texts;


	}

	public WebElement getTextElementFromTextAnnotation(int whichViewbox,int whichAnnotation){

		return getTextAnnotations(whichViewbox).get(whichAnnotation-1).findElement(By.cssSelector("text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.ACCEPTED_COLOR+"'] , text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.REJECTED_COLOR+"'], text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.PENDING_COLOR+"']"));		

	}

	public List<WebElement> getAllTextElementFromTextAnnotation(int whichViewbox,int whichAnnotation){

		return getTextAnnotations(whichViewbox).get(whichAnnotation-1).findElements(By.cssSelector("text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.ACCEPTED_COLOR+"'] , text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.REJECTED_COLOR+"'], text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.PENDING_COLOR+"']"));


	}

	public String getTextFromTextAnnotation(int whichViewbox , int whichAnnotation){

		return getTextAnnotations(whichViewbox).get(whichAnnotation-1).findElement(By.cssSelector("text:not([fill='black'])")).getText().trim();
	}

	public List<WebElement> getBorderTextAnnotations(int whichViewbox){

		List<WebElement> texts = new ArrayList<WebElement>();
		texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] > text:nth-child(1)")));
		//		texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] > text:nth-child(1)")));


		return texts;


	}

	public List<WebElement> getLineOfTextAnnotations(int whichViewbox){

		List<WebElement> elements = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text]  line[stroke='transparent']"));
		return elements;

	}

	public WebElement getLineOfTextAnnotations(int whichViewbox, int whichAnnotation){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] line[stroke='transparent']")).get(whichAnnotation-1);

	}

	public WebElement getTextAnnotation(int whichViewbox,int whichAnnot){

		return getTextAnnotations(whichViewbox).get(whichAnnot-1);

	}

	public void updateTextOnTextAnnotation(int whichViewbox,int whichAnnot,String text) throws InterruptedException{

		doubleClick(getTextElementFromTextAnnotation(whichViewbox,whichAnnot));
		pressKeys(Keys.BACK_SPACE);
		enterText(PRESENCE, getEditbox(whichViewbox), text);
		pressKeys(Keys.ENTER);
		waitForTimePeriod(1000);

	}

	public List<String> getTextLinesFromTextAnnotation(int whichViewbox , int whichAnnotation){

		List<String> textLines = new ArrayList<String>();
		List<WebElement> text = getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" g[ns-text]:nth-of-type("+whichAnnotation+") >text:not([fill='black']) > tspan"));
		for(WebElement txt : text) {
			textLines.add(txt.getText());
		}
		return textLines;
	}

	public void updateMultiLineTextOnTextAnnotation(int whichViewbox,int whichAnnot,String text) throws InterruptedException{
		doubleClick(getTextElementFromTextAnnotation(whichViewbox,whichAnnot));
		click(3,3);
		enterNewLineCharacter(text);
	}

	public List<String> getTextLinesFromCommentOfTextAnnotation(int whichViewbox , int whichAnnotation){

		List<String> textLines = new ArrayList<String>();
		//#svg-0 > g > g > g > g > g > text:nth-child(2) > tspan
		List<WebElement> text = getTextOfComment(whichViewbox).get(whichAnnotation-1).findElements(By.cssSelector("tspan"));
		for(WebElement txt : text) {
			textLines.add(txt.getText());
		}
		return textLines;
	}

	public List<WebElement> getTextLinesWebElementsFromTextAnnotation(int whichViewbox , int whichAnnotation){

		return getTextAnnotations(whichViewbox).get(whichAnnotation-1).findElements(By.cssSelector("text:not([fill='black']) > tspan "));

	}

	public List<WebElement> getAnchorLinesOfTextAnnot(int whichViewbox, int whichAnnot){			

		List<WebElement> anchors = new ArrayList<WebElement>();
		List<WebElement> elements = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text]"));

		try	{

			anchors.addAll(elements.get(whichAnnot-1).findElements(By.cssSelector("line:not([stroke='transparent'])")));
		}
		catch(NoSuchElementException exp){

			LOGGER.info(exp.getMessage());
		}

		return anchors;
	}

	public List<WebElement> getSelectedTextAnnotLineHandles(int whichViewbox, int whichAnnot){

		List<WebElement> allAnnot = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] circle[fill='#C0C0C0']"));
		return allAnnot;

	}

	public void selectTextAnnotation(int whichViewbox, int whichAnnot){

		click(getLineOfTextAnnotations(whichViewbox, whichAnnot));

	}

	public WebElement getEditbox(int whichViewbox){

		return driver.findElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" > ns-editbox > div > ns-textarea > div > div  div.textarea"));
	}

	public void drawText(int whichViewbox,int xoffset,int yoffset, String inpText) throws InterruptedException{
		try{
			mouseHoverWithClick(PRESENCE,getViewPort(whichViewbox), xoffset, yoffset);

			getEditbox(whichViewbox).click();
			WebElement box = getEditbox(whichViewbox);
			box.sendKeys(inpText);
			getEditbox(whichViewbox).sendKeys(Keys.ENTER);

		}catch(NoSuchElementException e){
			printStackTrace("Text Annotation is drawn either on PDF or JPG/PNG");
		}
	}	

	public void drawMultiLineText(int whichViewbox,int xoffset,int yoffset, String inpText, String addingTextToNextLine) throws InterruptedException{
		try{
			mouseHoverWithClick(PRESENCE, getViewPort(whichViewbox), xoffset, yoffset);
			mouseHoverWithClick(PRESENCE, getEditbox(whichViewbox), 0, 0);
			getEditbox(whichViewbox).sendKeys(inpText);			
			WebElement element = getEditbox(whichViewbox);
			String keysPressed = Keys.chord(Keys.SHIFT, Keys.ENTER);		
			element.sendKeys(keysPressed);
			waitForTimePeriod(2000);	
			enterText(addingTextToNextLine);
			pressKeys(Keys.ENTER);
			waitForTimePeriod(2000);
		}catch(NoSuchElementException e){
			printStackTrace("Text Annotation is drawn either on PDF or JPG/PNG");
		}
	}

	public boolean isTextAnnotationSelected(int whichViewbox, int whichAnn) {

		boolean status = false ;
		List<WebElement> allTextAnnot = getSelectedTextAnnotLineHandles(whichViewbox,whichAnn);
		try{
			if(allTextAnnot.size()>0){
				status = true;
			}
		}
		catch(NoSuchElementException e){
			printStackTrace(e.getMessage());
		}

		return status;
	}
	/**
	 * @author payal
	 * @param Viewbox number and x , y offsets
	 * @return: null
	 * Description: This method draw text annotation without text (In progress text annotation)
	 * 	 */
	public void drawTextAnnotationWithNoText(int whichViewbox,int xoffset,int yoffset) throws InterruptedException{
		mouseHoverWithClick(PRESENCE, getViewPort(whichViewbox), xoffset, yoffset);
		waitForTimePeriod(1000);		
	}

	public boolean verifyTextAnnotationIsCurrentActiveAcceptedGSPS(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){
		return verifyTextAnnotationIsCurrentActiveGSPS(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);		
	}

	public boolean verifyTextAnnotationIsCurrentActiveRejectedGSPS(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){
		return verifyTextAnnotationIsCurrentActiveGSPS(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}

	public boolean verifyTextAnnotationIsCurrentActivePendingGSPS(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){
		return verifyTextAnnotationIsCurrentActiveGSPS(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	
	/**
	 * @author payal
	 * @param Viewbox number
	 * @return: null
	 * Description: This method return true if text annotation is present
	 * 	 */
	public boolean isTextAnnotationPresent(int whichViewbox){
		boolean status = false;
		try{	
			if(getTextAnnotations(whichViewbox).size()>0)
				status = true;
		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}
		return status;
	}
	
	public boolean verifyTextAnnotationIsCurrentAcceptedInactive(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){

		return verifyTextAnnotationIsCurrentInactive(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}
	
	public boolean verifyTextAnnotationIsCurrentPendingInactive(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){

		
		return verifyTextAnnotationIsCurrentInactive(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	public boolean verifyTextAnnotationIsCurrentRejectedInactive(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine){

		
		return verifyTextAnnotationIsCurrentInactive(whichViewbox, whichTextAnnotation, withAnchorLine, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
		
	}

	public boolean verifyTextAnnotationIsCurrentInactive(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine, String whichColor,String opacity) {
		
		boolean status = false;

		WebElement textAnnotation = getTextElementFromTextAnnotation(whichViewbox,whichTextAnnotation);			
		List<WebElement> allText = getAllTextElementFromTextAnnotation(whichViewbox,whichTextAnnotation);
		
		
		status = textAnnotation.isDisplayed();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation is displayed "+status);
		
		status = status && textAnnotation.getAttribute(NSGenericConstants.FILL).equals(whichColor);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation color is "+ whichColor+" and is present "+status);
		

		status = status && textAnnotation.getAttribute(NSGenericConstants.FILL_OPACITY).equals(opacity);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text opacity "+status);
	
		
		status = status && (allText.size()==1);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that there is no shadow displayed "+status);
		
		// condition to verify the anchor line presence

		if(withAnchorLine){
			List<WebElement> anchorLines = getAnchorLinesOfTextAnnot(whichViewbox, whichTextAnnotation);
			status = status && anchorLines.get(0).isDisplayed();
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that achor line is displayed "+status);
			
			status = status && anchorLines.get(0).getAttribute(NSGenericConstants.STROKE).equals(whichColor);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that anchor line should be of color"+whichColor+" and present"+status);
			
			status = status && anchorLines.get(0).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text opacity "+status);
			
			
		}


		return status;
	}
		
	public void moveTextAnnotation(int whichViewbox, int whichText,int xOffset, int yOffset) throws InterruptedException{

		Actions action = new Actions(driver);
		if(getAnchorLinesOfTextAnnot(whichViewbox,whichText).size()>0) {
			action.moveToElement(getAnchorLinesOfTextAnnot(whichViewbox,whichText).get(0)).perform();
			LOGGER.info("drag and drop...clickAndHold");	
			action.clickAndHold(getAnchorLinesOfTextAnnot(whichViewbox,whichText).get(0)).perform();
		}
		else
			action.moveToElement(getTextAnnotation(whichViewbox,whichText)).clickAndHold(getTextAnnotation(whichViewbox,whichText)).perform();


		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().perform();
		action.moveByOffset(10, 10).perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);

	}

	public void deleteSelectedTextAnnotation() throws InterruptedException{
		pressKeys(Keys.DELETE); 
	}

	public void deleteTextAnnotation(int whichViewbox, int whichPoint) throws TimeoutException, InterruptedException{


		selectTextAnnotation(whichViewbox,whichPoint);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(2000);
	}
	
	public List<String> getCommentTextFromTextAnnotation(int whichViewbox, int whichAnnotation){


		List<WebElement> span = getTextAnnotations(whichViewbox).get(whichAnnotation-1).findElements(By.cssSelector("*[ns-comment] > text:nth-child(2) > tspan"));
		List<String> listOfText = new ArrayList<String>();
		span.stream().map(WebElement::getText).forEach(listOfText::add);

		return listOfText;
	}

	public String addTextToComments(WebElement element, String textTobeAdded, int where, boolean presskey) {

		doubleClick(element);
		for(int i =0;i<where;i++)
			pressKeys(Keys.ARROW_RIGHT);
		//		click(2,2);
		//		click(getEditbox(1));
		enterText(textTobeAdded);
		String editboxText = getText(getEditbox(1));
		if(presskey)
			pressKeys(Keys.ENTER);

		return editboxText;


	}

	public String addTextToResultComments(WebElement element, String textTobeAdded, int where, boolean presskey) {

		click(element);

		for(int i =0;i<where;i++)
			pressKeys(Keys.ARROW_RIGHT);
		enterText(textTobeAdded);
		String editboxText = getText(getEditbox(1));
		if(presskey)
			pressKeys(Keys.ENTER);

		return editboxText;


	}

	public void resizeEditbox(int whichViewbox, int x, int y, int xOffset, int yOffset) throws InterruptedException {

		Actions action = new Actions(driver);
		action.moveToElement(getViewPort(whichViewbox)).moveToElement(resizeIconOnEditBox).moveByOffset(x,y).build().perform();
		action.clickAndHold().moveByOffset(xOffset,yOffset).pause(100).release().build().perform();
		waitForTimePeriod(1000);


	}

	public boolean verifyScrollBarPresence() throws TimeoutException, InterruptedException
	{

		mouseHover(verticalScrollBarSlider.get(0));		
		return isElementPresent(verticalScrollBarComponent.get(0));
		//		return getCssValue(verticalScrollBarComponent, NSGenericConstants.BACKGROUND_COLOR).equals("rgba(153, 153, 153, 1)");

	}

	public boolean verifyDottedLinePresence(int whichViewbox) {

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-text] line[stroke-dasharray]")).size()>0;

	}
	
	public List<WebElement> getTextAnnotLineHandles(int whichViewbox, int whichAnnot){

		List<WebElement> handles = driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g> g[ns-text] circle[fill='transparent']"));
		return handles;

	}

	private List<WebElement> getTextReferenceOfTextAnnotation(int whichViewbox, int whichTextAnnotation){

		return getTextAnnotations(whichViewbox).get(whichTextAnnotation-1).findElements(By.cssSelector("text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.SHADOW_COLOR+"'], text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.ACCEPTED_COLOR+"'] , text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.REJECTED_COLOR+"'], text["+NSGenericConstants.FILL+"='"+ViewerPageConstants.PENDING_COLOR+"']"));


	}

	public boolean verifyTextAnnotationIsCurrentActiveGSPS(int whichViewbox, int whichTextAnnotation, boolean withAnchorLine,String whichColor,String opacity){

		boolean status = false;

		try {
			WebElement textAnnotation = getTextElementFromTextAnnotation(whichViewbox,whichTextAnnotation);	
			List<WebElement> allText = getTextReferenceOfTextAnnotation(whichViewbox,whichTextAnnotation);


			// Condition to verify the text is displayed
			status = textAnnotation.isDisplayed();
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation is displayed "+status);

			status = status && textAnnotation.getAttribute(NSGenericConstants.FILL).equals(whichColor);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation is displayed in "+whichColor+" color "+status);

			status = status && isAcceptRejectToolBarPresent(whichViewbox);			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that AR toolbar is displayed "+status);

			status= status && (allText.size()==2);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that two texts are presence one is yellow and another one in green "+status);

			status = status && getAttributeValue(allText.get(1),NSGenericConstants.FILL_OPACITY).equals(opacity);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text opacity is "+opacity+" and status "+status);

			// condition to verify the anchor line presence			
			if(withAnchorLine){ 
				List<WebElement> anchorLines = getAnchorLinesOfTextAnnot(whichViewbox, whichTextAnnotation);
				status = status &&  anchorLines.get(0).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.SHADOW_COLOR);
				ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation is highlighted  "+status);
				
				status = status &&  anchorLines.get(0).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);
				ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text opacity is "+opacity+" and status "+status);
				
				status = status && anchorLines.get(1).getAttribute(NSGenericConstants.STROKE).equals(whichColor) ;
				ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text annotation is displayed in "+whichColor+" color "+status);

				status = status && anchorLines.get(1).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity) ;
				ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying that text opacity is "+opacity+" and status "+status);
			}

		}
		catch (Exception e) {
			status= false;
		}

		return status;
	}
	
	
	public int  getTextAnnWhichIsPendingAndActive(int whichViewbox, boolean withAnchor){

		List<WebElement> myTxt = getTextAnnotations(whichViewbox);
		int index =0;

		for(int i =1;i<=myTxt.size();i++) {

			if(verifyTextAnnotationIsCurrentActivePendingGSPS(whichViewbox, i,withAnchor)) {
				index = i;
				break;
			}

		}

		return index;
	}


}
