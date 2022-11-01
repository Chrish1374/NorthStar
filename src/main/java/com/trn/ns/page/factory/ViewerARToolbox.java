package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.Utilities;


public class ViewerARToolbox extends ViewerPage{

	private WebDriver driver;

	public ViewerARToolbox(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}


	/******************************************
	 * VIEWER AR TOOLBOX
	 * ***************************************
	 */

	public By gspsPreviousIcn= By.xpath("//*[contains(@id,'icon-backward')]");
	public By gspsNextIcn= By.xpath("//*[contains(@id,'icon-forward')]");
	public By gspsRejectIcn= By.xpath("//*[contains(@id,'icon-reject-active')]");
	public By gspsFindingIcn= By.xpath("//*[contains(@id,'icon-eureka-logo')]");
	public By gspsTextIcn= By.xpath("//*[contains(@id,'icon-comment-active')]");
	public By gspsDeleteIcn= By.xpath("//*[contains(@id,'icon-delete-active')]");	
	public By acceptImage = By.id("acceptRect");
	public By rejectImage = By.id("rejectRect");

	@FindBy(css = "ns-icon[id*='icon-backward']")
	public WebElement gspsPrevious;

	@FindBy(css = "ns-icon[id*='icon-forward']")
	public WebElement gspsNext;

	@FindBy(css = "ns-icon[id*='icon-reject-active']")
	public WebElement gspsReject;

	@FindBy(css = "ns-icon[id*='icon-accept-active']")
	public WebElement gspsAccept;

	@FindBy(css = "ns-icon[id*='icon-eureka-logo']")
	public WebElement gspsFinding;
	
	@FindBy(css = "ns-icon[id*='icon-eureka-logo'] svg")
	public WebElement gspsFindingSVG;

	@FindBy(css = "ns-icon[id*='icon-comment-active']")
	public WebElement gspsText;

	@FindBy(css = "ns-icon[id*='icon-delete-active']")
	public WebElement gspsDelete;

	@FindBy(css="[id*='acceptRejectToolbarDiv']")
	public WebElement acceptRejectToolbar;

	@FindBy(id = "acceptRect")
	public WebElement acceptButtonOnPiccline;

	@FindBy(xpath="//*[@id='id-0-0']")
	public WebElement chkboxBoneage ;

	@FindBy(xpath="//div[@class='ns-wiaResultSelectorContainer']//label")
	public WebElement checkboxBoneage;

	@FindBy(css="div.textarea")
	public WebElement editBox;

	@FindBy(css="ns-rerunmenu div.rerunmeucontainer")
	public WebElement reRunIconContainer;

	@FindBy(css="ns-rerunmenu div.rerunbtncontainer")
	public WebElement reRunIcon;

	/*
	 * *************************
	 *  Cut - Copy Paste Options
	 *  ************************
	 */
	

	public By cutOption = By.cssSelector(".ns-contextmenu.unselectable  .menu .menu-item");
	protected By pasteOption = By.cssSelector(".ns-contextmenu.unselectable  .menu>div:nth-child(1)>div");
	protected By cancelOption = By.cssSelector(".ns-contextmenu.unselectable  .menu>div:nth-child(2)>div");

	public WebElement getCutOption() {

		return getElement(cutOption);
	}

	public WebElement getPasteOption() {

		return getElement(pasteOption);
	}
	public WebElement getCancelOption() {

		return getElement(cancelOption);
	}
	public WebElement getOuterGSPSNotification() {

		return getElement(By.cssSelector("div.contextInfo.eur-outerbox"));
	}

	public WebElement selectBoneageImage(int viewNum){
		return getElement(By.xpath("//*[@id='id-"+(viewNum-1)+"-0']"));
	}

	public WebElement resultSelector(int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-wiaresultselector/div/label"));
	}

	public WebElement resultSelectorCheckBox(int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-wiaresultselector/div/input"));
	}

	public WebElement getGSPSHoverContainer(int viewNum) { 
		return getElement(By.cssSelector("#acceptRejectToolbarDiv"+(viewNum-1)));	
	}


	public WebElement getGSPSAccept(int whichViewbox) {

		return getElement(By.cssSelector("ns-icon[id='icon-accept-active"+(whichViewbox-1)+"'] svg"));

	}

	public WebElement getGSPSPrevious(int whichViewbox) {

		return getElement(By.cssSelector("ns-icon[id='icon-backward"+(whichViewbox-1)+"'] svg"));

	}

	public WebElement getGSPSNext(int whichViewbox) {

		return getElement(By.cssSelector("ns-icon[id='icon-forward"+(whichViewbox-1)+"'] svg"));

	}

	public WebElement getGSPSReject(int whichViewbox) {

		return getElement(By.cssSelector("ns-icon[id='icon-reject-active"+(whichViewbox-1)+"'] svg"));

	}

	public WebElement getAcceptRejectToolBar(int whichViewbox) {

		return getElement(By.id("acceptRejectToolbarDiv"+(whichViewbox-1)));
	}

	public WebElement getAcceptIcon() {

		return getElement(By.cssSelector("#innerBox-Accept"));

	}

	public WebElement getRejectIcon() {

		return getElement(By.cssSelector("#innerBox-Reject"));
	}

	public WebElement getGSPSDeleteButton(int whichViewbox) throws InterruptedException {
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		return getElement(By.id("icon-delete-active"+(whichViewbox-1)));
	}

	public List<WebElement> getTextOfSCComment(int whichViewbox){
		List<WebElement> texts = new ArrayList<WebElement>();
		texts.addAll(driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-measurements]> g:nth-child(1)> text:nth-child(2)")));
		return texts;


	}

	public By getGSPSToolbar() {

		return By.xpath("//*[contains(@id,'acceptRejectToolbarDiv')]");

	}



	public boolean verifyAnnotationState(WebElement element, String stateColor,boolean activeOrInactive) {

		boolean color = element.getAttribute(NSGenericConstants.STROKE).equals(stateColor);
		boolean width= false;
		if(activeOrInactive)
			width=element.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.SHADOW_GSPS_WIDTH);

		else
			width=element.getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);

		return width && color;

	}

	public boolean verifyAcceptedIsChecked(int whichViewbox) {


		return getElement(By.cssSelector("#icon-accept-active"+(whichViewbox-1)+" > div > svg path")).getCssValue(NSGenericConstants.FILL).equals(ThemeConstants.SUCCESS_ICON_COLOR);
	}

	public boolean verifyRejectedIsChecked(int whichViewbox) {

		return getElement(By.cssSelector("#icon-reject-active"+(whichViewbox-1)+" > div > svg path")).getCssValue(NSGenericConstants.FILL).equals(ThemeConstants.ERROR_ICON_COLOR);
	}

	public boolean verifyGSPSDeleteButtonIsDisabled() {

		return getCssValue(gspsDelete.findElement(By.cssSelector("path")),NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
	}

	public boolean verifyResultSelectorIsChecked(WebElement element) {
		boolean val = false ;
		val = isChecked(element);
		return val;	
	}

	public boolean verifyRejectGSPSRadialMenu(){

		boolean status = false;
		try {
			if (gspsPrevious.isDisplayed() && gspsAccept.isDisplayed() && 
					gspsNext.isDisplayed() && gspsDelete.isDisplayed() && gspsFinding.isDisplayed() && gspsText.isDisplayed() &&
					getRejectIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("eur-fill-active Reject"));

				status= true ;
		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}

		return status;
	}

	public boolean verifyAcceptGSPSRadialMenu(){
		boolean status = false;
		try {
			if (gspsPrevious.isDisplayed() && gspsAccept.isDisplayed() && 
					gspsNext.isDisplayed() && gspsDelete.isDisplayed() && gspsFinding.isDisplayed() && gspsText.isDisplayed() &&
					getAcceptIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("eur-fill-active Accept"));			
			status= true ;
		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public boolean verifyPendingGSPSToolbarMenu(){

		boolean status = false;
		try {
			if (gspsPrevious.isDisplayed() && gspsAccept.isDisplayed() && 
					gspsNext.isDisplayed() && gspsDelete.isDisplayed() && gspsFinding.isDisplayed() && gspsText.isDisplayed()&&
					getRejectIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("eur-fill-inactive Reject") &&
					getAcceptIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("eur-fill-inactive Accept"))
				status= true ;


		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}

		return status;
	}

	public boolean isAcceptRejectToolBarPresent(int ViewBoxNumber) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ "Check if Accept/Reject radial tool bar is present");
		status=isElementPresent(By.cssSelector("#acceptRejectToolbarDiv"+(ViewBoxNumber-1)+" div div"));
		return status;
	}

	public boolean isIconDisable(WebElement icn) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ getAttributeValue(icn,"class"));
		status= getAttributeValue(icn,"class").equalsIgnoreCase(ViewerPageConstants.ICON_DISABLE);
		return status;
	}

	public boolean verifyResultsAreAccepted(int whichViewbox) throws InterruptedException {

		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).build().perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		return getAcceptIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("fill-active Accept");

	}

	public boolean verifyResultsAreRejected(int whichViewbox) throws InterruptedException {

		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		return getRejectIcon().getAttribute(NSGenericConstants.CLASS_ATTR).contains("eur-fill-active Reject");

	}

	public boolean verifyBinarySelectorToobar(int whichViewbox) throws InterruptedException {

		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).build().perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		return isElementPresent(getAcceptIcon()) && isElementPresent(getRejectIcon()) && 
				isElementPresent(getAcceptRejectToolBar(whichViewbox))&& 
				(!(isElementPresent(gspsDeleteIcn) && isElementPresent(gspsTextIcn) && isElementPresent(gspsFindingIcn)));

	}

	public boolean verifyResultAccepted(int viewNum) {

		return (getElement(By.cssSelector("#viewbox-"+ (viewNum -1) +"> ns-wiaresultselector > div > svg > g > g > g:nth-child(1)> path:nth-of-type(2)")).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.ACCEPT_CHECKBOX_COLOR));

	}

	public boolean verifyResultRejected(int viewNum) {
		return (getElement(By.cssSelector("#viewbox-"+ (viewNum -1) +"> ns-wiaresultselector > div > svg > g > g > g:nth-child(2)> path:nth-of-type(2)")).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECT_CHECKBOX_COLOR));	
	}




	public void navigateGSPSForwardUsingKeyboard() throws InterruptedException{

		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_RIGHT).build().perform();

		waitForElementVisibility(getGSPSToolbar());

	}

	public void navigateGSPSBackUsingKeyboard() throws InterruptedException{

		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_LEFT).build().perform();

		waitForElementVisibility(getGSPSToolbar());
	} 

	public void scrollDownGSPSUsingKeyboard() throws InterruptedException{

		Actions action = new Actions(driver);
		action.sendKeys(Keys.PAGE_DOWN).build().perform();

		waitForElementVisibility(getGSPSToolbar());

	}

	public void scrollUpGSPSUsingKeyboard() throws InterruptedException{

		Actions action = new Actions(driver);
		action.sendKeys(Keys.PAGE_UP).build().perform();		
		waitForElementVisibility(getGSPSToolbar());

	}

	public void waitForCoordinatesToGetChanged(WebElement element) throws InterruptedException {

		int x = element.getLocation().getX();
		int y = element.getLocation().getY();

		for(int i = 0 ;i< 10; ) {

			waitForTimePeriod(2000);
			int x1 = element.getLocation().getX();
			int y1 = element.getLocation().getY();

			if(!(x1==x) && !(y1==y))
				i++;

			else
				i=10;
		}


	}

	public void checkBoneage(int viewNum) {


		WebElement ele = resultSelector(viewNum);
		if(!isChecked(ele))
			click(ele);
	}

	public void pressKeyboardShortcutsForARP(int viewbox,String ARPShortcut) throws InterruptedException{


		mouseHover(getViewPort(viewbox));
		switch(ARPShortcut){

		case ViewerPageConstants.GSPS_ACCEPT :	
			pressKey(ViewerPageConstants.ACCEPT_KEY);
			break;


		case ViewerPageConstants.GSPS_REJECT :
			pressKey(ViewerPageConstants.REJECT_KEY);
			break;


		case ViewerPageConstants.PENDING_TEXT :
			pressKey(ViewerPageConstants.PENDING_KEY);
			break;

		}

	}

	public void deleteAllAnnotation(int whichViewbox) throws InterruptedException{

		mouseHover(getViewPort(whichViewbox));
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys(Keys.DELETE).perform();
		waitForTimePeriod(1000);

	}

	public void openGSPSRadialMenu(WebElement gspsObject) throws InterruptedException {
		//		performMouseRightClick(gspsObject);

		Actions a = new Actions(driver);
		a.moveToElement(gspsObject).perform();
		a.click().perform();
		a.moveByOffset(1, 1).perform();
		waitForElementVisibility(gspsPrevious);

	}

	public void openGSPSRadialMenu(int whichViewbox) throws InterruptedException {

		mouseHover(getViewPort(whichViewbox));
		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		waitForElementVisibility(By.xpath("//*[contains(@id,'acceptRejectToolbarDiv"+(whichViewbox-1)+"')]"));

	}

	public void selectRejectfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{
		openGSPSRadialMenu(gspsObject);
		click(gspsReject);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectPreviousfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{
		openGSPSRadialMenu(gspsObject);

		click(gspsPrevious);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectNextfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{
		openGSPSRadialMenu(gspsObject);
		click(gspsNext);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectAcceptfromGSPSRadialMenu(WebElement gspsObject) throws InterruptedException{

		openGSPSRadialMenu(gspsObject);
		mouseHover(gspsAccept);
		click(gspsAccept);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectAcceptfromGSPSRadialMenu() throws InterruptedException{
		mouseHover(gspsAccept);
		click(gspsAccept);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectAcceptfromGSPSRadialMenu(int whichViewbox) throws InterruptedException{
		mouseHover(getGSPSHoverContainer(whichViewbox));
		click(getGSPSAccept(whichViewbox));
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectRejectfromGSPSRadialMenu() throws  InterruptedException{

		mouseHover(gspsReject);
		click(gspsReject);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectRejectfromGSPSRadialMenu(int whichViewbox) throws  InterruptedException{

		click(getGSPSReject(whichViewbox));
		waitForElementVisibility(getGSPSToolbar());
	}

	// Select Previous from Accept/Reject radial Menu if GSPS radial is already opened
	public void selectPreviousfromGSPSRadialMenu() throws InterruptedException{

		click(gspsPrevious);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectPreviousfromGSPSRadialMenu(int whichViewbox) throws InterruptedException{

		click(getGSPSPrevious(whichViewbox));
		waitForElementVisibility(getGSPSToolbar());
	}

	// Select Next from Accept/Reject radial Menu if GSPS radial is already opened
	public void selectNextfromGSPSRadialMenu() throws InterruptedException{

		click(gspsNext);
		waitForElementVisibility(getGSPSToolbar());
	}

	public void selectNextfromGSPSRadialMenu(int whichViewbox) throws InterruptedException{

		click(getGSPSNext(whichViewbox));
		waitForElementVisibility(getGSPSToolbar());
	}


	public void acceptResult(int whichViewbox) throws InterruptedException{

		mouseHover(getViewPort(whichViewbox));
		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		ac.click(getAcceptIcon()).perform();
		ac.moveToElement(getViewPort(whichViewbox));
		waitForElementVisibility(getGSPSToolbar());

	}

	public void acceptResultOnBinaryToolbar(int whichViewbox) throws InterruptedException{

		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		ac.click(getAcceptIcon()).perform();
		ac.moveToElement(getViewPort(whichViewbox));
		waitForTimePeriod(1000);

	}

	// Select Next from Accept/Reject radial Menu if GSPS radial is already opened
	public void selectDeletefromGSPSRadialMenu() throws InterruptedException{

		click(gspsDelete);
		//Added a static wait, as cannot handle this with fluent,since this command has multiple outcome
		waitForTimePeriod(2000);
	}

	public void selectDeletefromGSPSRadialMenu(int whichViewbox) throws InterruptedException{

		click(gspsDelete);
		//Added a static wait, as cannot handle this with fluent,since this command has multiple outcome
		waitForTimePeriod(200);
	}

	public void rejectResult(int whichViewbox) throws InterruptedException{

		mouseHover(getViewPort(whichViewbox));
		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		ac.click(getRejectIcon()).perform();
		waitForElementVisibility(getGSPSToolbar());
	}

	public void rejectNonDICOMImage(int viewNum) {

		Actions ac = new Actions(driver);		
		ac.moveToElement(getElement(By.cssSelector("#viewbox-"+ (viewNum -1) + "> ns-wiaresultselector > div > svg > g > g > g:nth-child(2) >rect:nth-child(1)")), 1, 1).click().build().perform();
		waitForElementVisibility(getGSPSToolbar());
	}

	public void acceptNonDICOMImage(){
		//		click(acceptButtonOnPiccline);	
		Actions ac = new Actions(driver);		
		ac.moveToElement(acceptButtonOnPiccline, 1, 1).click().build().perform();
		waitForElementVisibility(getGSPSToolbar());
	}

	public void acceptNonDICOMImage(int viewNum){
		Actions ac = new Actions(driver);		
		ac.moveToElement(getElement(By.cssSelector("#viewbox-"+ (viewNum -1) + "> ns-wiaresultselector > div > svg > g > g > g:nth-child(1) >rect:nth-child(1)")), 1, 1).click().build().perform();
		waitForElementVisibility(getGSPSToolbar());
	}

	/*
	 * ********************************************************************************
	 *  Cut - Copy Paste Options
	 *  ************************
	 */

	public boolean selectCutUsingContextMenu(WebElement element) throws InterruptedException {

		mouseHover(element);
		performMouseRightClick(element);		
		boolean status = isElementPresent(cutOption);		
		WebElement cut = getElement(cutOption);
		status = status && getText(cut).equalsIgnoreCase(ViewerPageConstants.CUT);
		try {
			click(cut);
			status = status && (isElementPresent(cutOption)==false);
		}catch(Exception e) {
			status = false;
		}


		return status;

	}

	// paste as true means paste , if as false means cancel
	public boolean selectPasteOrCancelUsingContextMenu(int whichviewbox,String pasteOrCancel) throws InterruptedException {

		performMouseRightClick(getViewPort(whichviewbox));	

		boolean status = isElementPresent(pasteOption) && isElementPresent(cancelOption);			
		WebElement pasteElement = getElement(pasteOption);
		WebElement cancelElement = getElement(cancelOption);

		status = status && getText(pasteElement).equalsIgnoreCase(ViewerPageConstants.PASTE) && getText(cancelElement).equalsIgnoreCase(ViewerPageConstants.CANCEL);

		switch(pasteOrCancel) {
		case ViewerPageConstants.PASTE: click(pasteElement);break;
		case ViewerPageConstants.CANCEL: click(cancelElement);break;

		}

		status = status && (isElementPresent(pasteOption)==false) && (isElementPresent(cancelOption)==false);
		return status;		

	}



}