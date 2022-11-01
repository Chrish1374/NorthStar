package com.trn.ns.page.factory;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.Utilities;
import com.trn.ns.web.page.WebActions;


public class ViewerPage extends WebActions {


	public ViewerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}


	public By eurekaLogo = By.id("product-name-logo-toolbar");	
	public By patientIDOnViewer= By.id("PatientID-0");
	public By mainViewer = By.id("viewer");

	@FindBy(css="div#product-name-logo-toolbar")
	public WebElement EurekaLogo;

	@FindBy(css="#header div.displayLabelMain")
	public WebElement patientIDHeader;

	@FindBy(xpath="//div[@class='displayLabelMain']")
	public WebElement patientlableName; 

	@FindBy(xpath="//div[@class='menu-item' and contains(text(),'"+ViewerPageConstants.PASTE+"')]")
	public WebElement pasteText; 

	@FindBy(xpath="//div[@class='menu-item' and contains(text(),'"+ViewerPageConstants.COPY+"')]")
	public WebElement copyText; 

	@FindBy(xpath="//div[@class='menu-item' and contains(text(),'"+ViewerPageConstants.CANCEL+"')]")
	public WebElement cancelText; 

	@FindBy(css = ".viewbox-pdf-container#viewbox-2")
	public WebElement pdfViewbox;

	@FindBy(css = "#viewbox-3 > ns-pdf-view > div > pdf-viewer > div > div > div > div.textLayer > div:nth-child(37)")
	public WebElement pdfFirstText;	

	@FindBy(css = "div#viewboxes-container")
	public WebElement viewer;

	@FindBy(css="ns-tool-bar div.tool-bar.ng-star-inserted > div > div:nth-child(1) > div > span:nth-child(1)> ns-tool-bar-tile > div")
	public WebElement patientsIcon;

	@FindBy(css ="#viewer > ns-tool-bar > div.tool-bar.ng-star-inserted > div > div > div > span > ns-spinner > div")
	public WebElement viewerToolbarSpinner;

	@FindBys(@FindBy(xpath = "//div[@id='viewer']//canvas[starts-with(@id, 'canvas')]"))
	public List<WebElement> totalNumberOfCanvasForLayout;

	@FindBys(@FindBy(css = "ns-textoverlay>svg"))
	public List<WebElement> totalNumberOfSVGForLayout;

	@FindBys({@FindBy(css="div.overlay-container")})
	public List<WebElement> waterMarkOverlay;

	@FindBy(css="div.ps__thumb-y")
	public List<WebElement>verticalScrollBarComponent;

	@FindBy(css="div.ps__rail-y")
	public List<WebElement> verticalScrollBarSlider;

	@FindBy(css=".ps__thumb-x")
	public List<WebElement>horizontalScrollBarComponent;

	@FindBy(css=".ps__rail-x")
	public List<WebElement> horizontalScrollBarSlider;



	/*****************************
	 * BANNER AND NOTIFICATION
	 *****************************/

	public By notificationDiv= By.cssSelector("ns-notification-status div.message");
	public By newResultDialog = By.cssSelector(".rerunResultsDialog");
	public By viewerAlert = By.cssSelector("#default-alert #msgDiv-default-alert");

	@FindBy(css="ns-notification-status div.message")
	public WebElement notificationUI;

	@FindBys(@FindBy(css="ns-notification-status div.message .notification-title"))
	public List<WebElement> notificationTitle;

	@FindBys(@FindBy(css="ns-notification-status div.message .notification-message"))
	public List<WebElement> notificationMessage;	

	@FindBys(@FindBy(css="div.dialog-container"))
	public List<WebElement> notificationTiles;	

	@FindBys(@FindBy(css="#closeIcn-default-alert"))
	public List<WebElement> closeIconOnNotification;
	
	@FindBy(css="#banner.ok")
	public WebElement bannerWaitIcon;

	@FindBy(css="#banner.Discard")
	public WebElement bannerDiscardIcon;

	@FindBy(css= ".rerunResultsDialog")
	public WebElement reRunResultsDialog;

	@FindBy(css= "div.rerunResultsDialog label")
	public WebElement newResultMessage;

	@FindBys(@FindBy(css= "button.btn.resetBtn"))
	public List<WebElement> cancelButton;

	@FindBys(@FindBy(xpath= "//button[contains(@class,'btn resetBtn')]/parent::div[contains(@class,'btnDiv')]"))
	public List<WebElement> cancelButtonParent;
	
	
	public By refreshIconBy = By.cssSelector("button.btn.submitBtn");
	
	@FindBys(@FindBy(css= "button.btn.submitBtn"))
	public List<WebElement> refreshButton;



	/* **********************
	 * Radial Menu components
	 ***********************
	 */

	public By quickToolbar = By.cssSelector("#quickToolContainer");	

	public By quickToolbox = By.cssSelector("#quickToolBox");	
	
	public By byScrollIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(1) ns-quick-toolbox-tile svg ");
	public By byPanIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(2) ns-quick-toolbox-tile svg");
	public By byZoomIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(3) ns-quick-toolbox-tile svg");
	public By byWindowLevelingIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(4) ns-quick-toolbox-tile svg");
	public By byInvertIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(5) ns-quick-toolbox-tile svg");
	public By byCinePlayIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(6) ns-quick-toolbox-tile svg");
	public By byCine4DPlayIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(7) ns-quick-toolbox-tile svg");
	public By byTriangulationIcon=By.cssSelector("#quickToolContainer div:nth-child(1) > span:nth-child(8) ns-quick-toolbox-tile svg");
	
	public By byPointIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(5) ns-quick-toolbox-tile svg");
	public By byCircleIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(2) ns-quick-toolbox-tile svg");
	public By byEllipseIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(6) ns-quick-toolbox-tile svg");
	public By bytextArrowIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(3) ns-quick-toolbox-tile svg");
	public By byDistanceIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(4) ns-quick-toolbox-tile svg");
	public By byLineIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(1) ns-quick-toolbox-tile svg");
	public By byPolylineIcon=By.cssSelector("#quickToolContainer div:nth-child(2) > span:nth-child(7) ns-quick-toolbox-tile svg");

	@FindBy(css="#quickToolBox")
	public WebElement quickToolboxMenu;

	@FindBy(css="#quickToolContainer")
	public WebElement quickToolbarMenu;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(1) ns-quick-toolbox-tile svg")
	public WebElement scrollIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(2) ns-quick-toolbox-tile svg")
	public WebElement panIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(3) ns-quick-toolbox-tile svg")
	public WebElement zoomIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(4) ns-quick-toolbox-tile svg")
	public WebElement windowLevelingIcon;

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(5) ns-quick-toolbox-tile svg")
	public WebElement invertIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(6) ns-quick-toolbox-tile svg")
	public WebElement cinePlayIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(7) ns-quick-toolbox-tile svg")
	public WebElement cine4DPlayIcon;

	@FindBy(css ="#quickToolContainer div:nth-child(1) > span:nth-child(8) ns-quick-toolbox-tile svg")
	public WebElement triangulationIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(5) ns-quick-toolbox-tile svg")
	public WebElement pointIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(2) ns-quick-toolbox-tile svg")
	public WebElement circleIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(6) ns-quick-toolbox-tile svg")
	public WebElement ellipseIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(3) ns-quick-toolbox-tile svg")
	public WebElement textArrowIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(4) ns-quick-toolbox-tile svg")
	public WebElement distanceIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(1) ns-quick-toolbox-tile svg")
	public WebElement lineIcon;

	@FindBy(css="#quickToolContainer div:nth-child(2) > span:nth-child(7) ns-quick-toolbox-tile svg ")
	public WebElement polylineIcon;

	@FindBys(@FindBy(css = "#quickToolContainer div:nth-child(1) > span section"))
	public List<WebElement> allViewerIcons;

	@FindBys(@FindBy(css = "#quickToolContainer div:nth-child(2) > span section"))
	public List<WebElement> allAnnotationIcons;	

	@FindBys(@FindBy(css = "#quickToolContainer div:nth-child(1) > span section svg > g >rect"))
	public List<WebElement> allViewerRectIcons;

	@FindBys(@FindBy(css = "#quickToolContainer div:nth-child(2) > span section rect"))
	public List<WebElement> allAnnotationRectIcons;	

	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;

	@FindBy(xpath="//div[text()='Copied to clipboard.']")
	public WebElement copiedToClipboard; 

	/******************************************
	 * Tabs (OP and Series)
	 * 
	 ******************************************/
	
	@FindBy(css="div.dockedTabContainer div:nth-child(2)")
	public WebElement opTab;

	@FindBy(css="#dockedTabOuterDiv > ns-docked-tab > div > div > div:nth-child(1)")
	public WebElement seriesTab;
	
	@FindBy(css="div.menu-item")
	public WebElement cutText;

	@FindBys(@FindBy(css = "#dockedTabOuterDiv > ns-docked-tab > div > div > div"))
	public List<WebElement> tabs;	

	@FindBy(css="ns-icon#iconMinimizeToolWindows svg#icn-eureka-minimize")
	public WebElement minimizeIcon;

	public int getNumberOfCanvasForLayout(){
		return totalNumberOfCanvasForLayout.size();
	}

	public int getNuberOfSVGForLayout() {
		return totalNumberOfSVGForLayout.size();

	}



	/***************************************
	/* PATIENT INFO
	/***************************************/

	
	@FindBys(@FindBy(css = ".viewoverlay text["+NSGenericConstants.FILL+"='White']"))
	public List<WebElement> allOverlays;	
			
	public WebElement getImageMaxPhasePosition(int viewNum){

		return getElement(By.xpath("//*[@id='PhaseNumber-"+(viewNum-1)+"']"));
	}

	public WebElement getResultIDForViewbox(int viewNum){
		return getElement(By.xpath("//*[@id='ResultText-"+(viewNum-1)+"']"));
	}

	public By getPatientName(int viewNum) {

		return By.xpath("//*[@id='PatientName-"+(viewNum-1)+"']");
	}

	public WebElement getPatientNameOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='PatientName-"+(viewNum-1)+"']"));
	}

	public WebElement getPatientIDOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='PatientID-"+(viewNum-1)+"']"));
	}

	public By getSeriesDescription(int viewNum){

		return By.xpath("//*[@id='SeriesDescription-"+(viewNum-1)+"']");
	}

	public WebElement getSeriesDescriptionOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='SeriesDescription-"+(viewNum-1)+"']"));
	}

	
	public WebElement getStudyDescriptionOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='StudyDescription-"+(viewNum-1)+"']"));
	}

	public WebElement getPatientExternalIDOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='PatientExternalID-"+(viewNum-1)+"']"));
	}

	public WebElement getPatientBirthDateOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='PatientBirthDate-"+(viewNum-1)+"']"));
	}	

	public WebElement getReferringPhysiciansNameOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='ReferringPhysiciansName-"+(viewNum-1)+"']"));
	}	

	public WebElement getAcquisitionDeviceOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='AcquisitionDevice-"+(viewNum-1)+"']"));
	}	

	public WebElement getPatientSexOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='PatientSex-"+(viewNum-1)+"']"));
	}	

	public WebElement getStudyDateTimeOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='StudyDateTime-"+(viewNum-1)+"']"));
	}	

	public WebElement getPatientClassOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='PatientClass-"+(viewNum-1)+"']"));
	}	

	public WebElement getImageMatrixOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='ImageMatrix-"+(viewNum-1)+"']"));
	}

	public WebElement getImagePositionOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='ImagePosition-"+(viewNum-1)+"']"));
	}

	public WebElement getSliceThicknessOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='SliceThickness-"+(viewNum-1)+"']"));
	}

	public WebElement getSliceLocationOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='SliceLocation-"+(viewNum-1)+"']"));
	}

	public WebElement getKvpOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='Kvp-"+(viewNum-1)+"']"));
	}

	public WebElement getFOVOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='FOV-"+(viewNum-1)+"']"));
	}


	public WebElement getmAsOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='mAs-"+(viewNum-1)+"']"));
	}

	public WebElement getTypeOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='Type-"+(viewNum-1)+"']"));
	}

	public WebElement getImageAcquisitionDateTimeOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='ImageAcquisitionDateTime-"+(viewNum-1)+"']"));
	}

	public WebElement getWindowWidthValueOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='WindowWidth-"+(viewNum-1)+"']"));
	}

	public WebElement getWindowCenterValueOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='WindowCenter-"+(viewNum-1)+"']"));
	}

	public WebElement getAccessionNumberOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='AccessionNumber-"+(viewNum-1)+"']"));
	}

	public WebElement getStudyAcquisitionSiteOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='StudyAcquisitionSite-"+(viewNum-1)+"']"));
	}

	public WebElement getDetectorOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='Detector-"+(viewNum-1)+"']"));
	}

	public WebElement getTargetOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='Target-"+(viewNum-1)+"']"));
	}	

	public WebElement getLossyOverlay(int viewNum){
		return getElement(By.xpath("//*[@id='Lossy-"+(viewNum-1)+"']"));
	}	

	public WebElement resultApplied(int viewNum) {
		return getElement(By.id("GspsToggle-"+(viewNum-1)));

	}

	public WebElement svgImage(int viewNum) {
		return getElement(By.id("svg-"+(viewNum-1)+""));
	}


	public WebElement getImageCurrentPhasePosition(int viewNum){

		return getElement(By.cssSelector("#PhaseNumber-"+(viewNum-1)));
	}

	public WebElement getViewboxNumber(int whichViewbox) {
		
		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #ViewboxNumber-"+(whichViewbox-1)));
		
	}
	
	public WebElement getViewboxNumberBackground(int whichViewbox) {
		
		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" g path"));
		
	}
	
	public String getTextOfResultIDForViewbox(int viewNum){
		return getResultIDForViewbox(viewNum).getText();
	}

	public String getWindowWidthValText(int whichViewbox) {
		return getText(getWindowWidthValueOverlay(whichViewbox)).replace(ViewerPageConstants.WINDOW_WIDTH, "").trim();
	}

	public String getWindowCenterText(int whichViewbox) {
		return getText(getWindowCenterValueOverlay(whichViewbox)).replace(ViewerPageConstants.WINDOW_CENTER, "").trim();
	}
	
	
	public int getValueOfWindowWidth(int whichViewbox) {
		return convertIntoInt(getWindowWidthValText(whichViewbox));
	}

	public int getValueOfWindowCenter(int whichViewbox) {
		return convertIntoInt(getWindowCenterText(whichViewbox));
	}

	public String getPatientNameOverlayText(int viewNum) {
		String patientNameOverlayText = "";	
		patientNameOverlayText = getText( getPatientNameOverlay(viewNum));		
		return patientNameOverlayText;
	}

	public String getPatientIDOverlayText(int viewNum) {
		String patientIDOverlayText = "";	
		patientIDOverlayText = getText( getPatientIDOverlay(viewNum));		
		return patientIDOverlayText;
	}

	public String getSeriesDescriptionOverlayText(int viewNum) {
		String seriesDescriptionOverlayText = "";	
		seriesDescriptionOverlayText = getText( getSeriesDescriptionOverlay(viewNum));		
		return seriesDescriptionOverlayText;
	}

	public String getStudyDateTimeOverlayText(int viewNum) {
		String studyDateTimeOverlayText = "";	
		studyDateTimeOverlayText = getText( getStudyDateTimeOverlay(viewNum));		
		return studyDateTimeOverlayText;
	}

	public String getStudyDescriptionOverlayText(int viewNum) {
		String studyDescriptionOverlayText = "";	
		studyDescriptionOverlayText = getText( getStudyDescriptionOverlay(viewNum));		
		return studyDescriptionOverlayText;
	}

	public String getPatientSexOverlayText(int viewNum) {
		String patientSexOverlayText = "";	
		patientSexOverlayText = getText( getPatientSexOverlay(viewNum));		
		return patientSexOverlayText;
	}

	public String getPatientClassOverlayText(int viewNum) {
		String patientClassOverlayText = "";	
		patientClassOverlayText = getText( getPatientClassOverlay(viewNum));		
		return patientClassOverlayText;
	}

	public String getImageMatrixOverlayText(int viewNum) {
		String imageMatrixOverlayText = "";	
		imageMatrixOverlayText = getText( getImageMatrixOverlay(viewNum));		
		return imageMatrixOverlayText;
	}

	public String getImagePositionOverlayText(int viewNum) {
		String imagePositionOverlayText = "";	
		imagePositionOverlayText = getText( getImagePositionOverlay(viewNum));		
		return imagePositionOverlayText;
	}

	public String getSliceThicknessOverlayText(int viewNum) {
		String sliceThicknessOverlayText = "";	
		sliceThicknessOverlayText = getText( getSliceThicknessOverlay(viewNum));		
		return sliceThicknessOverlayText;
	}

	public String getSliceLocationOverlayText(int viewNum) {
		String sliceLocationOverlayText = "";	
		sliceLocationOverlayText = getText( getSliceLocationOverlay(viewNum));		
		return sliceLocationOverlayText;
	}

	public String getKvpOverlayText(int viewNum) {
		String kvpOverlayText = "";	
		kvpOverlayText = getText( getKvpOverlay(viewNum));		
		return kvpOverlayText;
	}

	public String getFOVOverlayText(int viewNum) {
		String sFOVOverlayText = "";	
		sFOVOverlayText = getText( getFOVOverlay(viewNum));		
		return sFOVOverlayText;
	}

	public WebElement getSliceInfo(int viewNum) {
		
		return getElement(By.cssSelector("#ImageNumber-"+(viewNum-1)));
		
	}

	public String getImageMaxScrollPositionOverlayText(int viewNum) {
		String[] values = getText(getSliceInfo(viewNum)).split("/");	
		return values[1].trim();
	}

	public String getmAsOverlayText(int viewNum) {
		String mAsOverlayText = "";	
		mAsOverlayText = getText( getmAsOverlay(viewNum));		
		return mAsOverlayText;
	}

	public String getWindowWidthValueOverlayText(int viewNum) {
		String sWindowWidthValueOverlayText = "";	
		sWindowWidthValueOverlayText = getText(getWindowWidthValueOverlay(viewNum));		

		return sWindowWidthValueOverlayText;
	}

	public String getWindowCenterValueOverlayText(int viewNum) {
		String sWindowCenterValueOverlayText = "";	
		sWindowCenterValueOverlayText = getText(getWindowCenterValueOverlay(viewNum));		

		return sWindowCenterValueOverlayText;
	}

	public String getCurrentScrollPosition(int whichViewbox){
		
		String sliceText = getText(getSliceInfo(whichViewbox));
		String subString = "";
		if(sliceText.contains("("))
		{
			int index = sliceText.indexOf(")");
			subString = sliceText.substring(index+1, sliceText.length());
		}
		else {
			subString = getText(getSliceInfo(whichViewbox)).replace(ViewerPageConstants.VIEWER_SLICE_TEXT, "").replace(":", "").trim();
		}
		String[] values = subString.split("/");
		return values[0].trim();
		
		
	}

	public String getViewboxNumberText(int whichViewbox) {
		
		return getText(getViewboxNumber(whichViewbox));
		
	}
	
	

	public int getSlicePositionAsPerConfiguration(int totalNum, String percValue){
		double value = Double.parseDouble(percValue);
		int actualNum =0;

		if(totalNum > 2)
			actualNum = (int) Math.floor(totalNum*value);
		else 
			actualNum = 1;
		return actualNum;	
	}

	public int getCurrentScrollPositionOfViewbox(int ViewBoxNumber)
	{
		return convertIntoInt(getCurrentScrollPosition(ViewBoxNumber));
	}

	public int getMaxNumberofScrollForViewbox(int viewNum) {
		
		
		return convertIntoInt(getImageMaxScrollPositionOverlayText(viewNum));

	}

	public double getIndexValue(int viewbox,String indexPos)
	{
		double indexVal = 0;
		List<String> values = Arrays.asList(getImagePositionOverlayText(viewbox).split(","));

		switch(indexPos)
		{
		case ViewerPageConstants.X_INDEX:indexVal =Double.parseDouble(values.get(0)); break;
		case ViewerPageConstants.Y_INDEX:indexVal =Double.parseDouble(values.get(1)); break;
		case ViewerPageConstants.Z_INDEX:indexVal =Double.parseDouble(values.get(2)); break;        }
		return indexVal;


	}


	/************************************
	 *For Multiphase Data 
	 ************************************
	 */

	public boolean verifyPhaseTextPresence(int whichViewbox){
		boolean status = false ;
		WebElement temp;

		try
		{
			temp= getImageCurrentPhasePosition(whichViewbox);
			status =temp.isDisplayed();
		}
		catch(Exception e)
		{
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public int getValueOfCurrentPhase(int whichViewbox) {
		
		String phase = getText(getImageCurrentPhasePosition(whichViewbox));		
		String phasePos= phase.replace(ViewerPageConstants.OUTPUTPANEL_PHASE_LABEL+":", "");
		String[] arr = phasePos.split("/");  
				
		return Integer.parseInt(arr[0].trim());
	}

	public int getValueOfMaxPhase(int whichViewbox) {

		String phase = getText(getImageCurrentPhasePosition(whichViewbox));		
		String phasePos= phase.replace(ViewerPageConstants.OUTPUTPANEL_PHASE_LABEL+":", "");
		String[] arr = phasePos.split("/");  
				
		return Integer.parseInt(arr[1].trim());	

	}

	public String getImageNumberLabelValueText(int viewNum){

		String value = getText(getSliceInfo(viewNum));
		
		int firstIndex = value.indexOf("(");
		int lastIndex = value.indexOf(")");
		
		return value.substring(firstIndex+1,lastIndex);
		
	}	
	
	public int getImageNumberLabelValue(int viewNum){

		return convertIntoInt(getImageNumberLabelValueText(viewNum));
		
	}	
	

	/***************************************
	 * LOCALIZER LINE 
	 ***************************************
	 */


	public WebElement getLocalizerLine(int viewnum)
	{
		return getElement(By.cssSelector("#svg-"+(viewnum-1)+" [ns-scoutline] line"));
	}

	public List<WebElement> getLocalizerLines(int viewnum)
	{
		return getElements(By.cssSelector("#svg-"+(viewnum-1)+" [ns-scoutline] line"));
	}

	public boolean verifyLocalizerLinePresence(int viewnum)
	{
		return isElementPresent(By.cssSelector("#svg-"+(viewnum-1)+" [ns-scoutline] line"));
	}

	public boolean verifyLocalizerLineColor(int viewnum)
	{
		return getCssValue(getLocalizerLine(viewnum),NSGenericConstants.STROKE).equals(ViewerPageConstants.LOCALIZER_BLUE_COLOR);
	}

	public String getLocalizerLineText(int viewnum)
	{
		return getText(getLocalizerLine(viewnum));
	}

	public boolean verifyLocalizerLineIsHorizontal(int viewnum)
	{

		WebElement line = getLocalizerLine(viewnum);
		String y1= getAttributeValue(line, ViewerPageConstants.Y1);
		String y2= getAttributeValue(line, ViewerPageConstants.Y2);

		String x1= getAttributeValue(line, ViewerPageConstants.X1);
		String x2= getAttributeValue(line, ViewerPageConstants.X2);
		return y1.equals(y2) && !(x1.equals(x2));
	}

	public boolean verifyLocalizerLineIsVertical(int viewnum)
	{

		WebElement line = getLocalizerLine(viewnum);
		String y1= getAttributeValue(line, ViewerPageConstants.Y1);
		String y2= getAttributeValue(line, ViewerPageConstants.Y2);

		String x1= getAttributeValue(line, ViewerPageConstants.X1);
		String x2= getAttributeValue(line, ViewerPageConstants.X2);
		return x1.equals(x2) && !(y1.equals(y2));
	}


	/**********************************
	/******** BANNER AND WATER MARK
	/**********************************/

	public By getContentQualification(int whichViewbox) {

		return By.id("ContentQualification-"+(whichViewbox -1));


	}

	public By getContentQualificationForViewer() {

		return By.xpath("//*[@id='viewer']//*//*[@id='banner']/div[@class='message']");
	}

	public By getWaterMark(int whichViewbox) {

		return By.cssSelector("#viewbox-"+(whichViewbox-1)+" #closeIcn-viewboxBanner");
	}

	
	public WebElement getContentQualificationTextOverlay(int i) {
		return getElement(getContentQualification(i));
	}
	public WebElement getBannerForContentQualification() {
	
		return getElement(By.xpath("//div[contains(text(),'"+ViewerPageConstants.BANNER_TEXT+"')]/../..//div[@class='notification-message']"));
	}

	

	public WebElement getBannerMessageForSendToPACS() {

		return getElement(By.xpath("//*[@id='viewer']//*//*[@id='banner']/div[contains( text(),'"+ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS+"')]"));
	}

	public WebElement getContentQualificationTextOverlayViewer() {

		return getElement(getContentQualificationForViewer());
	}

	public WebElement getViewboxNotification (int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #viewboxBanner"));
	}

	public WebElement getViewboxNotificationCloseIcon (int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #closeIcn-viewboxBanner"));
	}

	public WebElement getViewboxNotificationMessage(int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" .notification-message"));
	}
	
	public WebElement getViewboxNotificationMessageBody(int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" .row"));

	}
	
	public WebElement getViewboxTitle(int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" ns-notification-status div.message .notification-title"));
	}

	public WebElement getViewboxTitleIcon(int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" g["+NSGenericConstants.FILL+"='#FF9C00'] path"));
	}


	
	public WebElement getbatchConflictResult(int viewNum){
		return getElement(By.cssSelector("#BatchConflictText-"+(viewNum-1)+"["+NSGenericConstants.FILL+"='orange']"));
	}

	public void closeWaterMarkIcon(int whichViewbox) {

		if(isElementPresent(getWaterMark(whichViewbox))) {
			click(getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #closeIcn-viewboxBanner")));
			waitForElementInVisibility(getWaterMark(whichViewbox));
		}	
	}

	public void closingConflictMsg() throws InterruptedException {


		int totalViewboxes = getNumberOfCanvasForLayout();
		for (int i = 1; i <= totalViewboxes; i++) {
			closeWaterMarkIcon(i);
		}

	}



	public boolean verifyViewboxNotificationForConflicts(int viewnum)
	{
		boolean status=false;
		
		boolean warningMessage= getText(getViewboxNotificationMessage(viewnum)).equalsIgnoreCase(ViewerPageConstants.BATCH_CONFLICT_MESSAGE);
		boolean waterMark=isElementPresent(getWaterMark(viewnum));
		boolean conflictMessage=isElementPresent(getbatchConflictResult(viewnum));	
		boolean backgroundColor=getCssValue(getViewboxNotificationMessageBody(viewnum),NSGenericConstants.DISPLAY_PROPERTY).equalsIgnoreCase(NSGenericConstants.flex);
		boolean title = getText(getViewboxTitle(viewnum)).equalsIgnoreCase(ViewerPageConstants.WARNING_TITLE);
		boolean titleIcon = getAttributeValue(getViewboxTitleIcon(viewnum),NSGenericConstants.PATH_ATTR_D).equalsIgnoreCase("M2 16c.62 0 1.135-.59 1.187-1.357l.806-11.95c.047-.694-.142-1.379-.521-1.89C3.094.29 2.56 0 2 0 1.44 0 .906.291.528.802.149 1.314-.04 2 .007 2.692l.806 11.951C.865 15.41 1.38 16 2 16zM2 17c-1.105 0-2 .895-2 2s.895 2 2 2 2-.895 2-2-.895-2-2-2z");
		
		status=warningMessage && waterMark && conflictMessage && backgroundColor && title && titleIcon;

		return status;
	}
	
	public boolean verifyViewboxBGForConflicts(int viewnum)
	{
		boolean status=false;
	
		status=getCssValue(driver.findElement(By.cssSelector("#viewbox-"+(viewnum-1)+" > ns-batch-conflict-overlay > div > div")), NSGenericConstants.OPACITY).equalsIgnoreCase(ViewerPageConstants.OPACITY_FOR_BATCH_CONFLICT);
		return status;
	}

	public boolean verifyBorderWhenViewboxIsActive(int viewboxNo)
	{

		boolean activeborder=getCssValue(getViewPort(viewboxNo),NSGenericConstants.CSS_PROP_BORDER_COLOR).equalsIgnoreCase(ViewerPageConstants.MACHINE_FILTER_BACKGROUND_W);

		return activeborder;
	}


	public String getBannerMessageForViewbox (int whichViewbox) {
		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" div.message")).getText().trim();
	}

	public boolean isViewboxActive(int whichViewbox){

		return (convertIntoInt(driver.findElement(By.cssSelector("div.viewbox.active")).getAttribute("id").replace("viewbox-", ""))+1) == whichViewbox;
	}

	public boolean selectAndVerifyActiveViewbox(int viewNum){
		boolean status = false;
		WebElement viewbox =driver.findElement(By.id("viewbox-"+(viewNum-1)+""));
		try {
			mouseHover(viewbox);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		status = getAttributeValue(viewbox,NSGenericConstants.CLASS_ATTR).contains(ViewerPageConstants.ACTIVE_VIEWBOX);
		return status;
	}


	public void closeWarningNotification() {

		if(isElementPresent(notificationDiv))
			if(getText(notificationTitle.get(0)).equalsIgnoreCase(ViewerPageConstants.WARNING_TITLE))
				click(closeIconOnNotification.get(0));
	}
	
	public void closeNotification() {

		if(isElementPresent(notificationDiv))
			for(int i=0;i<closeIconOnNotification.size();i++)
				clickUsingAction(closeIconOnNotification.get(i));
		
		waitForElementInVisibility(notificationDiv);
	}


	/************************************
	 * Viewer Basic Methods
	 * **********************************
	 */
	public By getViewbox(int viewportNumber){
		return By.id("viewbox-"+(viewportNumber-1));
	}

	public By getPDFViewbox(int viewportNumber){

		return By.cssSelector(".viewbox-pdf-container#viewbox-"+(viewportNumber-1));
	}

	public WebElement getViewPort(int viewportNumber){
		return driver.findElement(By.id("viewbox-"+(viewportNumber-1)));
	}

	public WebElement getViewerCanvas(int viewportNumber){
		return driver.findElement(By.id("canvas-"+(viewportNumber-1)));
	}

	public WebElement getPDFViewPort(int viewportNumber){

		return getElement(By.cssSelector(".viewbox-pdf-container#viewbox-"+(viewportNumber-1)));
	}

	public WebElement getSRViewPort(int viewportNumber){

		return getElement(By.cssSelector("ns-viewbox-sr #viewbox-"+(viewportNumber-1)));
	}

	public WebElement verticalScrollBarComponent(int viewNum)
	{
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+" "+".ps__thumb-y"));
	}

	public WebElement verticalScrollBarSlider(int viewNum)
	{
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+" "+".ps__rail-y"));
	}

	public WebElement lastItemOnPDF(int viewNum)
	{
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+" "+"pdf-viewer div.textLayer span:last-of-type"));
	}

	public void doubleClickOnViewport(WebElement Viewbox){
		doubleClick(Viewbox);
	}

	public boolean verifyPDFLoadedInViewer(int whichViewbox) throws InterruptedException {

		boolean status = false;
		WebElement element = getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" pdf-viewer .page"));

		if(Integer.parseInt(element.getCssValue(NSGenericConstants.HEIGHT).replace("px", "")) > 0 && Integer.parseInt(element.getCssValue(NSGenericConstants.WIDTH).replace("px", ""))>0)
			status = true;

		return status;
	}



	public boolean verifyNonDicomImageLoadedInViewer(int whichViewbox) throws InterruptedException {

		boolean status = false;

		if(Integer.parseInt(driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1))).getAttribute(NSGenericConstants.HEIGHT)) > 0 && Integer.parseInt(driver.findElement(By.cssSelector("#svg-"+(whichViewbox-1))).getAttribute(NSGenericConstants.WIDTH))>0)
			status = true;

		return status;
	}

	public boolean verifyDicomImageLoadedInViewer(int whichViewbox) throws InterruptedException {

		boolean status = false;

		WebElement element = getElement(By.id("canvas-"+(whichViewbox-1)+""));
		if(Integer.parseInt(element.getAttribute(NSGenericConstants.HEIGHT)) > 0 && Integer.parseInt(element.getAttribute(NSGenericConstants.WIDTH))>0)
			status = true;

		return status;
	}





	// Double click on specific to viewbox as number
	public void doubleClickOnViewbox(int whichViewbox){

		try {
			mouseHover(getViewPort(whichViewbox));
		} catch (TimeoutException  | InterruptedException e) {
			// TODO Auto-generated catch block
			printStackTrace(e.getMessage());
		}
		doubleClickJs(getViewPort(whichViewbox));
		if(waitForPatientToLoad(whichViewbox))			
			waitForElementVisibility(By.id("PatientID-"+(whichViewbox-1))); 	
		
	}

	public void changeSlice(int sliceCount, String move) throws TimeoutException, InterruptedException {


		mouseHover(PRESENCE, getViewPort(1));
		if (move.equalsIgnoreCase("Up")){
			for(int i= 0; i < sliceCount; i++) {
				pressKeys(Keys.ARROW_UP);

			}
		}else if (move.equalsIgnoreCase("Down")) {
			for(int i= 0; i < sliceCount; i++) {
				pressKeys(Keys.ARROW_DOWN);
			}
		}

	}

	public int scrollThroughAllSlices(int viewboxNumber, int delay) {
		int maxScrollPosition = 0;
		try {
			maxScrollPosition = getMaxNumberofScrollForViewbox(viewboxNumber);
//			if(getCurrentScrollPositionOfViewbox(viewboxNumber)!=1)
//				scrollUpToSliceUsingKeyboard(viewboxNumber, getCurrentScrollPositionOfViewbox(viewboxNumber));
			// scroll down till the last scroll position
			scrollDown(maxScrollPosition - 1, delay);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return maxScrollPosition;
	}


	/***********************
	 * WAIT METHODS
	 * *********************
	 */


	public void waitForPdfToRenderInViewbox(int viewbox){
		waitForElementVisibility(By.id("SeriesDescription-"+(viewbox-1)));
	}

	public void waitForViewerpageToLoad(){
		waitForElementVisibility(getViewbox(1));
		waitForElementVisibility(patientIDOnViewer);
		waitForElementAttributeGreaterThanZero(getElement(By.id("canvas-0")), NSGenericConstants.WIDTH, Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
//		waitForElementInVisibility(viewerToolbarSpinner);

	}

	public void waitForRespectedViewboxToLoad(int viewbox) {

		if(waitForPatientToLoad(viewbox))			
			waitForElementVisibility(By.id("PatientID-"+(viewbox-1))); 	
		if(isElementPresent(By.cssSelector("#canvas-"+(viewbox-1))))
			waitForElementAttributeGreaterThanZero(getElement(By.id("canvas-"+(viewbox-1))), NSGenericConstants.WIDTH, Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
	}

	private void waitForElementAttributeGreaterThanZero(WebElement element, String attribute,int specifiedTimeout) {

		WebDriverWait wait = new WebDriverWait(driver,specifiedTimeout);
		ExpectedCondition<Boolean> elementAttributeEqualsString = arg0 -> Integer.parseInt(element.getAttribute(attribute)) > 0;
		wait.until(elementAttributeEqualsString);
	}

	public boolean waitForPatientToLoad(int viewbox) {

		try {
			waitForElementVisibility(By.id("PatientID-"+(viewbox-1))); 
			return true;
		}catch(TimeoutException e) {
			return false;
		}


	}

	public void waitForAllChangesToLoad(){
		waitForEndOfAllAjaxes();
	}

	public void waitForAllImagesToLoad(){

		int totalCanvas = getNuberOfSVGForLayout();

		for(int i =0;i<totalCanvas;i++){

			try{
				WebElement e = getElement(By.id("PatientID-"+(i)));
				if(e!=null && isElementPresent(totalNumberOfSVGForLayout.get(i).findElement(By.id("PatientID-"+(i))))){
					waitForElementVisibility(totalNumberOfSVGForLayout.get(i).findElement(By.id("PatientID-"+(i))));
					waitForTimePeriod(3000);
				}
				else
					continue;

				waitForElementAttributeGreaterThanZero(getElement(By.id("canvas-"+i)), NSGenericConstants.WIDTH, Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));

			}catch(TimeoutException e){
				continue ;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}

	}

	// wait for specific viewbox to load
	public void waitForViewerpageToLoad(int whichViewbox){

		waitForElementVisibility(By.id("canvas-"+(whichViewbox-1)));
		try {
			waitForElementVisibility(By.id("PatientID-"+(whichViewbox-1)));
		}catch(TimeoutException e) {
			waitForPdfToRenderInViewbox(whichViewbox);

		}

	}

	public void waitForSilicesToChange(int ViewBoxNumber) 
	{
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		int currentImage = getCurrentScrollPositionOfViewbox(ViewBoxNumber);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {
					return (currentImage!=getCurrentScrollPositionOfViewbox(ViewBoxNumber));}});
		}
		catch(TimeoutException e)
		{}
	}

	/************************************
	 * 
	 * DRAG AND RELEASE METHODS
	 ************************************
	 */

	public void dragAndReleaseOnViewer(int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHover(PRESENCE, getViewbox(1), from_xOffset, from_yOffset);
		clickAndHold_Release(to_xOffset, to_yOffset);

	}

	public void dragAndReleaseOnViewerWithoutHop(int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException {

		mouseHover(PRESENCE, getViewbox(1), from_xOffset, from_yOffset);
		waitForTimePeriod(5);
		clickAndHoldRelease(to_xOffset, from_yOffset);


	}

	public void dragAndReleaseOnViewerWithClick(int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, getViewbox(1), from_xOffset, from_yOffset);
		waitForTimePeriod(5);
		clickAndHold_Release(to_xOffset, to_yOffset);
		waitForTimePeriod(2000);

	}

	public void dragAndReleaseOnViewerWithClick(By viewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, viewbox, from_xOffset, from_yOffset);
		waitForTimePeriod(5);
		clickAndHold_Release(to_xOffset, to_yOffset);
		waitForTimePeriod(2000);

	}

	public void dragAndReleaseOnViewerWithClick(WebElement viewbox,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, viewbox, from_xOffset, from_yOffset);
		waitForTimePeriod(5);
		clickAndHold_Release(to_xOffset, to_yOffset);
		waitForTimePeriod(2000);

	}

	//Added hardcoded wait for images to get load
	public void dragAndReleaseOnViewerWithClick(int viewnum,int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{
		WebElement viewbox = getViewPort(viewnum);
		mouseHoverWithClick(PRESENCE, viewbox, from_xOffset, from_yOffset);
		waitForTimePeriod(5);
		clickAndHold_Release(to_xOffset, to_yOffset);
		waitForTimePeriod(1000);

	}

	public void dragAndReleaseOnViewer(WebElement viewbox, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHover(VISIBILITY, viewbox, from_xOffset, from_yOffset);
		waitForTimePeriod("Low");
		clickAndHoldRelease(to_xOffset, to_yOffset);
		waitForTimePeriod("Low");
	}

	public void dragAndReleaseOnViewerQuickVersion(WebElement viewbox, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHover(VISIBILITY, viewbox, from_xOffset, from_yOffset);
		clickAndHoldReleaseQuick(to_xOffset, to_yOffset);

	}

	// This method is created without hopping, it is gonna drag in single attempt. which will work correctly specially in case of WW/WL
	public void dragAndReleaseOnViewerWithoutHop(By viewbox, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, viewbox, from_xOffset, from_yOffset);
		clickAndHoldRelease(to_xOffset, to_yOffset);
		waitForTimePeriod("High");

	}

	// This method is created without hopping, it is gonna drag in single attempt. which will work correctly specially in case of WW/WL
	public void dragAndReleaseOnViewerWithoutHop(WebElement viewbox, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, viewbox, from_xOffset, from_yOffset);
		clickAndHoldRelease(to_xOffset, to_yOffset);
		waitForTimePeriod("High");

	}

	public void dragAndReleaseOnViewer(By viewbox, int from_xOffset, int from_yOffset, int to_xOffset, int to_yOffset) throws InterruptedException{

		mouseHover(PRESENCE, viewbox, from_xOffset, from_yOffset);
		clickAndHold_Release(to_xOffset, to_yOffset);
		waitForTimePeriod("Medium");

	}


	/*************************************
	 * ACTION USING KEYBOARD METHODS
	 * ***********************************
	 */

	public void mouseWheelScrollInViewer(int viewbox, String direction, int scrollAmount) throws InterruptedException{
		if((!direction.equalsIgnoreCase("up")) && (!direction.equalsIgnoreCase("down"))) {
			LOGGER.error("Invalid direction: " + direction);
		}
		String scrollVal="120";
		if(direction.equalsIgnoreCase("up")) {
			scrollVal = "-"+"120" ;
		} 


		for(int i=0 ;i<scrollAmount;i++){
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String script ="view = document.getElementById('viewbox-"+(viewbox-1)+"');"+					
					"var evt = document.createEvent(\"MouseEvents\");"+
					"evt.initMouseEvent('wheel', true, true);"+
					"evt.deltaY = "+scrollVal+";"+
					"view.dispatchEvent(evt);";


			executor.executeScript(script);	
			waitForTimePeriod(2000);
		}

		LOGGER.info("scrollAmount: " + scrollAmount);

	}

	public void playOrStopCineUsingKeyboardCKey() throws InterruptedException{

		String cKey="\u0063";		
		pressKeys(cKey);
		//		waitForTimePeriod(3000);
	}

	public void enableOrDisableWWWLUsingKeyboardWKey() throws InterruptedException{

		String wKey="\u0077";		
		pressKeys(wKey);
		//		waitForTimePeriod(2000);
	}

	public void toggleOnOrOffResultUsingKeyboardGKey() throws InterruptedException{

		String gKey="\u0067";		
		pressKeys(gKey);
		//		waitForTimePeriod(2000);
	}

	public void performSyncONorOFF(){
		pressKeys(Keys.SPACE);
		try {
			waitForTimePeriod(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void scrollDown(int scrollCount, int delay) throws InterruptedException {

		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN).build().perform();
		waitForTimePeriod(delay);
		scrollCount--;
		if(scrollCount > 0) {
			scrollDown(scrollCount, delay);
		}
	}

	public void scrollDownToSliceUsingKeyboard(int scrollAmount) throws InterruptedException{


		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN,Keys.RETURN).build().perform();
		waitForTimePeriod(500);
		if (scrollAmount==1)
			return;
		else
			scrollDownToSliceUsingKeyboard(scrollAmount-1);

	}

	public void scrollDownToSliceUsingKeyboard(int viewNum, int scrollAmount) throws InterruptedException{

		mouseHover(getViewPort(viewNum));
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN,Keys.RETURN).build().perform();
		waitForTimePeriod(500);
		if (scrollAmount==1)
			return;
		else
			scrollDownToSliceUsingKeyboard(scrollAmount-1);

	}

	public void scrollUpToSliceUsingKeyboard(int scrollAmount) throws InterruptedException{


		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_UP).build().perform();
		waitForTimePeriod(500);
		if (scrollAmount==1)
			return;
		else
			scrollUpToSliceUsingKeyboard(scrollAmount-1);

	} 

	
	public void scrollUpSlice(int scrollAmount, int delay) throws InterruptedException{


		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_UP).build().perform();
		waitForTimePeriod(delay);
		if (scrollAmount==1)
			return;
		else
			scrollUpToSliceUsingKeyboard(scrollAmount-1);

	} 
	
	public void scrollUpToSliceUsingKeyboard(int viewNum, int scrollAmount) throws InterruptedException{

		mouseHoverWithClick(PRESENCE, getViewPort(viewNum), 0, 0);
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_UP).build().perform();
		waitForTimePeriod(500);

		if (scrollAmount==1)
			return;
		else
			scrollDownToSliceUsingKeyboard(scrollAmount-1);

	}

	public void enableOrDisableDistanceIconUsingKeyboardDKey() throws InterruptedException{

		String dKey="\u0064";		
		pressKeys(dKey);
		waitForTimePeriod(2000);
	}

	public void toggleOffandOnKeyboardGWithGSPSscroll() throws InterruptedException, AWTException

	{
		Robot robot = new Robot();   //use robot class, since we can only simulate Modfier key Down in selenium
		robot.keyPress(KeyEvent.VK_LEFT);
		robot.delay(200);
		toggleOnOrOffResultUsingKeyboardGKey();
		toggleOnOrOffResultUsingKeyboardGKey();
		robot.keyRelease(KeyEvent.VK_LEFT);
	}



	/******************************
	/* Lossy Overlay
	/******************************/


	public By getLossyAnnotation(int viewbox){
		return By.id("Lossy-"+(viewbox-1));
	}

	public By getResultAppliedElement(int whichViewbox){

		return By.id("GspsToggle-"+(whichViewbox-1));

	}

	public WebElement getResultAppliedAnnotation(int whichViewbox){

		return driver.findElement(By.id("GspsToggle-"+(whichViewbox-1)));

	}

	public void pressResultApplied(int whichViewbox) throws InterruptedException{

		mouseHover(getViewPort(whichViewbox));
		click(getResultAppliedAnnotation(whichViewbox));

	}

	public boolean verifyResultAppliedTextPresence(int whichViewbox){
		boolean status = false ;
		WebElement temp;

		try
		{
			temp= driver.findElement(By.id("GspsToggle-"+(whichViewbox-1))); 
			status =temp.isDisplayed();
		}
		catch(NoSuchElementException e)
		{
			status = false; 
			printStackTrace(e.getMessage());
		}


		return status;
	}

	// specific to lossy since lossy goes so fast hence captured all the statements here and skipped tool layer
	public boolean verifyLossyAnnotationOnInputOfImageNum(int viewbox, int x, int y , int xOffset, int yOffset) throws TimeoutException, InterruptedException{

		Actions action = new Actions(driver);
		action.moveToElement(getViewPort(viewbox)).moveByOffset(x, y).build().perform();
		action.clickAndHold().perform();
		action.moveByOffset(xOffset, yOffset).perform();
		boolean status = isElementPresent(getLossyAnnotation(viewbox));
		action.release().perform();
		return status;

	}

	public boolean verifyResultAppliedToggle(int whichViewbox, String toggle){
		boolean status = false ;
		if(getResultAppliedAnnotation(whichViewbox).isDisplayed() && toggle.equalsIgnoreCase(NSGenericConstants.OFF)){
			if(getResultAppliedAnnotation(whichViewbox).getAttribute(NSGenericConstants.FILL).equalsIgnoreCase(ViewerPageConstants.COLOUR_GREY) && 
					getResultAppliedAnnotation(whichViewbox).getAttribute(NSGenericConstants.FONT_STYLE).equalsIgnoreCase(NSGenericConstants.FONT_ITALIC)){
				status = true ;
			}
		}
		if(getResultAppliedAnnotation(whichViewbox).isDisplayed() && toggle.equalsIgnoreCase(NSGenericConstants.ON)){
			if(getResultAppliedAnnotation(whichViewbox).getAttribute(NSGenericConstants.FILL).equalsIgnoreCase(ViewerPageConstants.COLOUR_WHITE) && 
					getResultAppliedAnnotation(whichViewbox).getAttribute(NSGenericConstants.FONT_STYLE).equalsIgnoreCase(NSGenericConstants.FONT_STYLE_NORMAL)){
				status = true ;
			}
		}
		return status;
	}


	/* **********************
	 * Radial Menu components
	 ***********************
	 */


	public boolean verifyAllIconsPresenceInQuickToolbar(){

		boolean status = panIcon.isDisplayed() ;
		status = status && zoomIcon.isDisplayed();
		status = status && cinePlayIcon.isDisplayed();
		status = status && windowLevelingIcon.isDisplayed();
		status = status && scrollIcon.isDisplayed();
		status = status && invertIcon.isDisplayed();
		status = status && cinePlayIcon.isDisplayed();
		status = status && cine4DPlayIcon.isDisplayed();
		status = status && triangulationIcon.isDisplayed();

		status = status && lineIcon.isDisplayed();
		status = status && circleIcon.isDisplayed();
		status = status && textArrowIcon.isDisplayed();
		status = status && distanceIcon.isDisplayed();
		status = status && pointIcon.isDisplayed();
		status = status && polylineIcon.isDisplayed();

		return status ;
	}
	
	
	public boolean verifyCompactQuickToolbar(){

		boolean status = panIcon.isDisplayed() ;
		status = status && zoomIcon.isDisplayed();
		status = status && windowLevelingIcon.isDisplayed();
		status = status && scrollIcon.isDisplayed();
		return status ;
	}

	public void openQuickToolbar(WebElement viewbox) {
		performMouseRightClick(viewbox);
		waitForElementVisibility(quickToolbar);
	}

	public void openQuickToolbar(int whichViewbox) {
		openQuickToolbar(getViewPort(whichViewbox));		
	}

	public void selectScrollFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(scrollIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public void selectScrollFromQuickToolbar(int viewbox) throws InterruptedException {
		selectScrollFromQuickToolbar(getViewPort(viewbox));

	}

	public void selectPanFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(panIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public void selectPanFromQuickToolbar(int viewbox) throws InterruptedException {
		selectPanFromQuickToolbar(getViewPort(viewbox));

	}

	public void selectZoomFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(zoomIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public void selectZoomFromQuickToolbar(int viewbox) throws InterruptedException {
		selectZoomFromQuickToolbar(getViewPort(viewbox));

	}

	public void selectWindowLevelFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(windowLevelingIcon);
		waitForElementInVisibility(quickToolbar);

	}	

	public void selectWindowLevelFromQuickToolbar(int viewbox) throws InterruptedException {
		selectWindowLevelFromQuickToolbar(getViewPort(viewbox));
	}	

	public void selectInvertFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(invertIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public void selectCineFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(cinePlayIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public void selectCine4DFromQuickToolbar(WebElement viewbox) throws InterruptedException {
		openQuickToolbar(viewbox);
		click(cine4DPlayIcon);
		waitForElementInVisibility(quickToolbar);

	}

	public WebElement getTooltipWebElement(WebElement ele) throws InterruptedException {

		mouseHover(ele);
		return tooltip ;		

	}

	public boolean checkCurrentSelectedIcon(String iconName) throws InterruptedException
	{   
		boolean status = false;
		
		int viewport = getActiveViewbox();

		if(isAttribtuePresent(getElement(By.tagName("ns-quicktoolbox")),"hidden"))
			performMouseRightClick(getViewPort(viewport));
		
		List<WebElement> element = getElements(By.cssSelector("ns-quick-toolbox-tile div.tool-tile.active-icon"));

		for(WebElement ele : element) {
			mouseHover(ele);
			if(iconName.equalsIgnoreCase(getText(getTooltipWebElement(ele)))) {
				status = true;
				break;
			}
		}
		
		click(EurekaLogo);

		return status;
	}

	public Boolean isCineStopped(int ViewBoxNumber) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		int currentImage = getCurrentScrollPositionOfViewbox(ViewBoxNumber);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {
					return (currentImage!=getCurrentScrollPositionOfViewbox(ViewBoxNumber));}});
		}
		catch(TimeoutException e)
		{
			status=true;
		}
		return status;
	}

	public Boolean isCine4DStopped(int ViewBoxNumber) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ " Wait for the image silices to change...");
		int currentImage = getValueOfCurrentPhase(ViewBoxNumber);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(Configurations.TEST_PROPERTIES.get(ELEMENTSEARCHTIMEOUT)));
			wait.until(new ExpectedCondition<Boolean>() 
			{
				@Override
				public Boolean apply(WebDriver driver) {
					return (currentImage!=getValueOfCurrentPhase(ViewBoxNumber));}});
		}
		catch(TimeoutException e)
		{
			status=true;
		}
		return status;
	}

	public boolean verifyQuickToolboxPresence() {

		return !isAttribtuePresent(getElement(By.cssSelector("ns-quicktoolbox")), "hidden");
	}

	public boolean verifyCine4DIsInactive() throws InterruptedException {


		WebElement cine4d = getElement(By.cssSelector("#quickToolContainer > div > div:nth-child(1) > span.inline-block.not-allowed.ng-star-inserted > ns-quick-toolbox-tile > div"));
		boolean status = getCssValue(cine4d, "cursor").equalsIgnoreCase("not-allowed");
		mouseHover(cine4d);

		status = status && getText(getTooltipWebElement(cine4d)).equalsIgnoreCase(ViewerPageConstants.CINE4D);

		return status;		

	}

	public boolean verifyBackGroundGColorForRadialMenuOption(WebElement element)
	{
		Boolean status = false;
		String temp = element.getCssValue(NSGenericConstants.FILL);
		temp = getHexColorValue(temp);
		status=temp.equals(ViewerPageConstants.RADIAL_BACKGROUND_COLOR);
		return status;
	}

	public boolean verifyPropertyOfRegularVerticalScrollBar(WebElement ScrollComponent,WebElement ScrollSlider) {

		boolean status=false;
		boolean width = ScrollComponent.getCssValue(NSGenericConstants.WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);
		boolean background_color_of_ScrollComponent= getHexColorValue(ScrollComponent.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_COMPONENT_BACKGROUND_COLOR);
		boolean background_color_of_ScrollSlider=getHexColorValue(ScrollSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_SLIDER_BACKGROUND_COLOR);


		if (width && background_color_of_ScrollComponent && background_color_of_ScrollSlider)
			status=true;

		return status;

	}

	public boolean verifyPropertyOfRegularHorizontalScrollBar(WebElement ScrollComponent,WebElement ScrollSlider) {

		boolean status=false;

		boolean width = ScrollComponent.getCssValue(NSGenericConstants.WIDTH).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);
		boolean background_color_of_ScrollComponent= getHexColorValue(ScrollComponent.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_COMPONENT_BACKGROUND_COLOR);
		boolean background_color_of_ScrollSlider=getHexColorValue(ScrollSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_SLIDER_BACKGROUND_COLOR);


		if (width && background_color_of_ScrollComponent && background_color_of_ScrollSlider)
			status=true;

		return status;

	}

	public boolean verifyPropertyOfVerticalScrollBarWhenMousePointer(WebElement ScrollComponent,WebElement ScrollSlider) {

		boolean status=false;
		boolean width = ScrollComponent.getCssValue(NSGenericConstants.WIDTH).equals(ViewerPageConstants.SCROLLBAR_WIDTH);
		boolean background_color_of_ScrollComponent=getHexColorValue(ScrollComponent.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_COMPONENT_BACKGROUND_COLOR_WHEN_FOCUSED);
		boolean background_color_of_ScrollSlider=getHexColorValue(ScrollSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR)).equals(ViewerPageConstants.SCROLLBAR_SLIDER_BACKGROUND_COLOR_WHEN_FOCUSED);
		if (width && background_color_of_ScrollComponent && background_color_of_ScrollSlider)
			status=true;

		return status;
	}

	public boolean verifyNoContentSpecifiedIsDisplayed(int whichViewbox) throws InterruptedException {



		boolean status = false;



		WebElement element = getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" ns-textoverlay svg"));
		if(Integer.parseInt(element.getAttribute(NSGenericConstants.HEIGHT)) > 0 && Integer.parseInt(element.getAttribute(NSGenericConstants.WIDTH))>0)
			status = true;



		return status;
	}


	public void pushDataToLiasion(String cstoreLocation, String ruleName, String IP, String port, String dataFolderLoc) throws IOException {

		ProcessBuilder builder = new ProcessBuilder(
				"cmd.exe", "/c", "cd \""+cstoreLocation+"\" && dotnet CStoreScu.dll \""+ruleName+"\" "+IP+" "+port+" \""+dataFolderLoc+"\"");
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null || line.contains(ViewerPageConstants.COMPLETED_EXIT_TEXT)) { break; }
			LOGGER.info(line);
		}
	}

	public boolean verifyWhiteBorderForViewbox(int viewboxNo,String theme)
	{
		boolean status=getCssValue(getViewPort(viewboxNo),NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(NSGenericConstants.VIEWBOX_BORDER_RADIUS);

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status=status && getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_WHITE_OUTLINE_COLOR);

		else  if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status=status && getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_WHITE_OUTLINE_COLOR);

		return status;
	}


	public int getActiveViewbox()
	{
		int currentViewbox =-1;

		for(int i =1;i<=getNumberOfCanvasForLayout();i++) {
			String border = getCssValue(getViewPort(i),NSGenericConstants.BOX_SHADOW);
			if(border.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_BLUE_OUTLINE_COLOR)||border.equalsIgnoreCase(ThemeConstants.DARK_THEME_GRAY_OUTLINE_COLOR)) {
				currentViewbox =i;
				break;
			}

		}
		return currentViewbox;
	}

	public boolean verifyBorderWhenViewboxIsInActive(int viewboxNo,String theme)
	{
		boolean status = false;
		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)){

			status= !getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_BLUE_OUTLINE_COLOR) ;
			status=status && !getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_WHITE_OUTLINE_COLOR);
		}

		else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)){

			status=!getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_GRAY_OUTLINE_COLOR) ;
			status=status && !getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_WHITE_OUTLINE_COLOR);

		}
		return status;
	}

	public boolean verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(int activeViewboxNo,int seriesViewboNo,String theme)
	{

		boolean status =getCssValue(getViewPort(activeViewboxNo),NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(NSGenericConstants.VIEWBOX_BORDER_RADIUS);
		status =status && getCssValue(getViewPort(seriesViewboNo), NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(NSGenericConstants.VIEWBOX_BORDER_RADIUS);

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)){
			status=status && getCssValue(getViewPort(activeViewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_BLUE_OUTLINE_COLOR) ;
			status =status && getCssValue(getViewPort(seriesViewboNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_WHITE_OUTLINE_COLOR);
		}

		else  if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)){
			status=status && getCssValue(getViewPort(activeViewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_GRAY_OUTLINE_COLOR) ;
			status =status && getCssValue(getViewPort(seriesViewboNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_WHITE_OUTLINE_COLOR);
		}


		return status;
	}

	public boolean verifyBorderForActiveViewbox(int viewboxNo,String theme) 
	{
		boolean status =getCssValue(getViewPort(viewboxNo),NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(NSGenericConstants.VIEWBOX_BORDER_RADIUS);

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status=getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_THEME_BLUE_OUTLINE_COLOR) ;

		else if((theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)))
			status=getCssValue(getViewPort(viewboxNo),NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_THEME_GRAY_OUTLINE_COLOR) ;

		return status;
	}

	public boolean verifyViewboxSpaceAndBackgroundColor(String theme)
	{
		boolean status=false;

		status=getCssValue(driver.findElement(mainViewer),NSGenericConstants.VIEWBOX_BORDER_SIZE ).equalsIgnoreCase(NSGenericConstants.BORDER_BOX);

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status=status && getBackgroundColor(driver.findElement(mainViewer)).equalsIgnoreCase(ThemeConstants.EUREKA_TABLE_BACKGROUND);

		else if((theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)))

			status=status && getBackgroundColor(driver.findElement(mainViewer)).equalsIgnoreCase(ThemeConstants.DARK_TABLE_BACKGROUND);

		return status;

	}

	public boolean verifyNotificationPopUp(String successOrFail,String message)
	{
		boolean status = false;
        waitForElementsVisibility(notificationDiv);
		status=getText(notificationTitle.get(0)).equalsIgnoreCase(successOrFail);

		if(!message.isEmpty())
			status=status && getText(notificationMessage.get(0)).equalsIgnoreCase(message);

				switch(successOrFail)
		{
		case ViewerPageConstants.SUCCESS:
		{
			status=status && getCssValue(driver.findElement(By.cssSelector("div.message div.defaultIcon path")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.SUCCESS_ICON_COLOR);
			break;
		}
		case ViewerPageConstants.WARNING_TITLE:
		{
			status=status && getCssValue(driver.findElement(By.cssSelector("div.message div.defaultIcon path")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.WARNING_ICON_COLOR);
			break;
		}
		case PatientPageConstants.MESSAGE_TYPE_INFO:
		{
			status=status && getCssValue(driver.findElement(By.cssSelector("div.message div.defaultIcon path")), NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			break;
		}

		}

		return status;
	}

	public boolean verifyPresenceOfViewerNotification()
	{
		return isElementPresent(viewerAlert);
		
	}
	
	public boolean verifyPresenceOfNotification()
	{
		return isElementPresent(notificationDiv);
		
	}
	
	public String getNotificationMessage(int notificationNo)
	{
		return getText(notificationMessage.get(notificationNo-1));
		
	}
	
	
	public void scrollToImage(int viewbox, int whichImage) throws InterruptedException {
		
		int currentPos = getCurrentScrollPositionOfViewbox(viewbox);
		mouseHover(getViewPort(viewbox));
		if(currentPos<whichImage)
			scrollDown((whichImage-currentPos),1);
		
		else if (currentPos>whichImage)
			scrollUpSlice((currentPos - whichImage),1);
	}
	
	public void scrollToPhase(int viewbox, int whichPhase) throws InterruptedException {
		
		int currentPos = getValueOfCurrentPhase(viewbox);
		if(currentPos<whichPhase)
			for(int i=0;i<(whichPhase-currentPos);i++)
				pressKey(NSGenericConstants.DOT_KEY);
		
		else if (currentPos>whichPhase)
			for(int i=0;i<(currentPos - whichPhase);i++)
				pressKey(NSGenericConstants.COMMA_KEY);
	}

	
	public boolean verifyIconsPresenceForNonDicomInQuickToolbar(int Viewbox){
		boolean status = false ;
		openQuickToolbar(Viewbox);
		if(isElementPresent(quickToolbar))
		{
		status=isElementPresent(byPanIcon);

		status=status && isElementPresent(byZoomIcon) ;
		status=status && isElementPresent(byScrollIcon);
		status=status && isElementPresent(byWindowLevelingIcon);
		
		status=status && !(isElementPresent(byInvertIcon));
		status=status && !(isElementPresent(byCinePlayIcon));
		status=status && !(isElementPresent(byTriangulationIcon));
		status=status && !(isElementPresent(byPointIcon)) ;
		status=status && !(isElementPresent(byCircleIcon)) ;
		status=status && !(isElementPresent(byEllipseIcon)) ;
		status=status && !(isElementPresent(bytextArrowIcon)) ;
		status=status && !(isElementPresent(byDistanceIcon)) ;
		status=status && !(isElementPresent(byLineIcon)) ;
		status=status && !(isElementPresent(byPolylineIcon));	
		}
		return status ;
	}
 
}

