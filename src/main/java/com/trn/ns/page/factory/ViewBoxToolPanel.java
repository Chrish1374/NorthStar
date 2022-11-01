package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.Utilities;


public class ViewBoxToolPanel extends ViewerToolbar {

	private WebDriver driver;
	public ViewBoxToolPanel(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}


	public By toolBox =By.cssSelector("div.toolpanel-container");
	public By wwwlTab =By.xpath("//div[@class='mat-tab-list']/div/div[contains(.,'"+ViewerPageConstants.WINDOW_LEVEL_TITLE+"')]");
	public By plane = By.xpath("//div[@class='mat-tab-list']/div/div [contains(.,'"+ViewerPageConstants.PLANE+"')]");

	@FindBy(css="ns-viewbox-toolpanel .mat-tab-header")
	public WebElement toolPanel;

	@FindBy(xpath="//div[@class='mat-tab-list']/div/div[contains(.,'"+ViewerPageConstants.WINDOW_LEVEL_TITLE+"')]")
	public WebElement windowLevelTab;

	@FindBy(xpath="//div[@class='mat-tab-list']/div/div [contains(.,'"+ViewerPageConstants.ZOOM+"')]")
	public WebElement zoomTab;

	@FindBy(xpath="//div[@class='mat-tab-list']/div/div [contains(.,'"+ViewerPageConstants.PLANE+"')]")
	public WebElement planeTab;

	@FindBy(css="ns-viewbox-toolpanel .drawer ")
	public WebElement viewBoxToolPanel;

	@FindBy(css="#zoomToFitId svg path")
	public WebElement zoomToFit;

	@FindBy(css="div.wrapper div.max.label.center")
	public WebElement zoomMaxLabel;

	@FindBy(css="div.wrapper div.min.label.center")
	public WebElement zoomMinLabel;

	@FindBy(css="#zoomInId > div > svg > g > text tspan")
	public WebElement zoomMaxIcon;

	@FindBy(css="#zoomOutId > div > svg > g > text tspan")
	public WebElement zoomMinIcon;

	@FindBy(css="#zoomOutId svg path")
	public WebElement zoomIn;

	@FindBy(css="#zoomInId svg path")
	public WebElement zoomOut;

	@FindBy(css="div.mat-slider-thumb-label")
	public WebElement sliderThumbLabel;

	@FindBy(css="div.mat-slider-thumb-label span")
	public WebElement sliderThumbLabelValue;

	@FindBy(css="div > ns-zoomtool > div > ns-sliderbar")
	public  WebElement slider;

	@FindBy(css="div.tooltip-inner")
	public WebElement ViewToolBoxButtonToolTip;

	@FindBy(css="ns-viewbox-toolpanel div> svg > path.drawer-fill-color")
	public WebElement ViewToolBoxButtonSvg;

	@FindBys(@FindBy(css="div.grid>div > div"))
	public List<WebElement> presetOptions;


	// Plane tab

	@FindBy(css="#planeContainer > div:nth-child(1) div #radio")
	public WebElement axialRadio;

	@FindBy(css="#planeContainer > div:nth-child(2) div #radio")
	public WebElement coronalRadio;

	@FindBy(css="#planeContainer > div:nth-child(3) div #radio")
	public WebElement sagittalRadio;

	@FindBy(xpath="//label[contains( text(),'Axial')]")
	public WebElement axialText;

	@FindBy(xpath="//label[contains( text(),'Coronal')]")
	public WebElement coronalText;

	@FindBy(xpath="//label[contains( text(),'Sagittal')]")
	public WebElement sagittalText;

	@FindBy(css="svg#icon-coronal")
	public WebElement coronalIcon;

	@FindBy(css="svg#icon-axial")
	public WebElement axialIcon;

	@FindBy(css="svg#icon-sagittal")
	public WebElement sagittalIcon;

	@FindBy(css="#planeContainer")
	public WebElement planeContainer;

	@FindBy(xpath="//*[@id='icon-axial']/../..")
	public WebElement axialBorder;

	@FindBy(xpath="//*[@id='icon-sagittal']/../..")
	public WebElement sagittalBorder;

	@FindBy(xpath="//*[@id='icon-coronal']/../..")
	public WebElement coronalBorder;


	public WebElement getViewBoxTool(int viewportNumber){

		return getElement(getViewBoxToolPanel(viewportNumber));
	}

	public boolean verifyPresetOptionIsSelected(int viewboxNum, String presetValue) throws  InterruptedException{

		Boolean status = false;

		openTabInViewbox(viewboxNum,windowLevelTab);

		WebElement presetOPt = getElement(By.xpath("//*[contains(text(),'"+presetValue+"')]"));
		String backColor = presetOPt.getCssValue(NSGenericConstants.BACKGROUND_COLOR);
		String color = presetOPt.getCssValue(NSGenericConstants.CSS_PROP_COLOR);

		if(backColor.equalsIgnoreCase(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR) && color.equalsIgnoreCase(ThemeConstants.EUREKA_OPTION_FONT_COLOR) )
			status=true;
		openOrCloseViewBoxToolPanel(viewboxNum ,NSGenericConstants.CLOSE);
		return status;
	} 	

	public boolean selectPresetValue(int viewboxNum , String presetValue) throws TimeoutException, InterruptedException{

		openTabInViewbox(viewboxNum,windowLevelTab);
		click(getElement(By.xpath("//div[text()='"+presetValue+"']")));
		return isElementPresent(toolBox);



	}

	public List<String> getWWWLPresetOptions(int whichViewbox) throws InterruptedException{

		openTabInViewbox(whichViewbox, windowLevelTab);
		List<String> options = new ArrayList<String>();
		for(WebElement ele : presetOptions)
			options .add(getText(ele));		
		openOrCloseViewBoxToolPanel(whichViewbox ,NSGenericConstants.CLOSE);

		return options;

	}

	public Map<Integer,String> getWWWLOverlayOptionsAndShortCuts(int whichViewbox) throws InterruptedException{

		openTabInViewbox(whichViewbox, windowLevelTab);


		Map<Integer,String> options = new HashMap<Integer,String>();

		for(WebElement preset : presetOptions) {

			mouseHover(preset);
			String tooltipText = getText(tooltip) ;

			int index = tooltipText.indexOf("[");				
			String shortcut ="";	
			Pattern p3 = Pattern.compile("\\[(.*?)\\]");	
			Matcher m3 = p3.matcher(tooltipText);
			while (m3.find()) {
				shortcut= m3.group(1);
			}

			options.put(Integer.parseInt(shortcut),tooltipText.substring(0, index-1));


		}

		openOrCloseViewBoxToolPanel(whichViewbox, NSGenericConstants.CLOSE);

		return options;

	}

	public List<String> getWAndCValuesFromPresetOption(int whichViewbox, String presetOption) throws InterruptedException{

		openTabInViewbox(whichViewbox,windowLevelTab);
		WebElement option = getElement(By.xpath("//div[text()='"+presetOption+"']"));
		mouseHover(option);
		String presetValue = getText(tooltip);			
		String temp = presetValue.substring(presetValue.indexOf("(")+1,presetValue.indexOf(")"));
		String value[]= temp.split(",");		

		return Arrays.asList(value);
	}

	public List<Integer> getWAndCValuesFromPresetOpt(int whichViewbox, String presetOption) throws InterruptedException{

		List<String> values = getWAndCValuesFromPresetOption(whichViewbox, presetOption);
		return values.stream().map(Integer::parseInt).collect(Collectors.toList());

	}

	public void openOrCloseViewBoxToolPanel(int viewNum , String openOrClose) throws InterruptedException
	{
		if(getActiveViewbox()!=viewNum)
			mouseHover(getViewPort(viewNum));
		
		WebElement element = getDrawerIcon(viewNum);
		switch(openOrClose) {

		case NSGenericConstants.OPEN:
			if(!isElementPresent(toolBox)){
				click(element);
				waitForElementVisibility(toolBox);}
			break;

		case NSGenericConstants.CLOSE:
			if(isElementPresent(toolBox)){
				click(element);
			}
			break;
		default : break;
		}

	}

	private WebElement getDrawerIcon(int whichViewbox) {

		return getElement(By.cssSelector("#viewbox-"+(whichViewbox-1)+" ns-viewbox-toolpanel .drawer"));


	}

	public By getViewBoxToolPanel(int viewportNumber){
		return By.cssSelector("#viewbox-"+(viewportNumber-1)+" ns-viewbox-toolpanel .drawer");

	}

	public void openTabInViewbox(int whichViewbox , WebElement whichTab) throws InterruptedException
	{

		openOrCloseViewBoxToolPanel(whichViewbox,NSGenericConstants.OPEN);
		if(!verifyTabIsActive(whichTab))
			click(whichTab);



	}

	public boolean verifyPresenceOfViewBoxToolPanelContainer(){

		Boolean status = false;
		if(isElementPresent(toolBox)){
			status =true;
		}
		return status;
	} 	

	public boolean verifyRoundedCorner(WebElement item, String cssParam, String value) {


		String rounderCorner = getCssValue(item, cssParam);
		boolean status= rounderCorner.equals(value);
		return status;
	}


	public boolean verifyTabIsActive(WebElement whichTab) {

		return whichTab.getAttribute(NSGenericConstants.CSS_PROP_ARIA_SELECTED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE);

	}

	public boolean compareWindowWidthAndCenter(int viewbox, String windowWidth, String windowCenter){

		if(windowWidth.equalsIgnoreCase(getWindowWidthValText(viewbox)) && 
				windowCenter.equalsIgnoreCase(getWindowCenterText(viewbox)))
			return true;

		else
			return false;
	}

	public boolean verifyWWWCValuesFromPresetContextMenuOption(int ViewboxNum ,String presetOption) throws InterruptedException {

		List<String> values = getWAndCValuesFromPresetOption(ViewboxNum, presetOption);
		selectPresetValue(ViewboxNum, presetOption);    
		boolean status = compareWindowWidthAndCenter(ViewboxNum, values.get(0).trim(),values.get(1).trim());
		return status;
	}

	public WebElement getZoomOverlay(int viewNum){

		return getElement(By.xpath("//*[@id='Zoom-"+(viewNum-1)+"']"));
	}

	public List<String> getZoomOverlayText(int viewNum) {

		WebElement zoomText =getZoomOverlay(viewNum);
		String value=replaceSpecialCharacterFromText(getText(zoomText), "%", "").trim();
		String zoom[]=value.split(":");
		return Arrays.asList(zoom);
	}

	public int getZoomValue(int ViewBoxNumber)
	{
		return convertIntoInt(getZoomOverlayText(ViewBoxNumber).get(1).trim());
	}

	public String getZoomLevelValue(int ViewBoxNumber)
	{
		return getZoomOverlayText(ViewBoxNumber).get(1).trim();
	}

	public String getSliderLevelValue()
	{
		String value= getText(sliderThumbLabelValue).replace("%", "") ;
		return value;   
	}

	public int getSliderValue()
	{
		String value= getText(sliderThumbLabelValue).replace("%", "") ;
		return (convertIntoInt(value));

	}

	public void changeZoomNumber(int viewNum,int zoomValue) throws InterruptedException
	{
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.OPEN);
		click(sliderThumbLabel);
		Actions action = new Actions(driver);
		String  value=getSliderLevelValue() ;
		String newZoomValue=Integer.toString(zoomValue);

		while((!newZoomValue.equalsIgnoreCase(value)))
		{
			if(zoomValue<getSliderValue())
				action.sendKeys(Keys.ARROW_LEFT).build().perform();
			else
				action.sendKeys(Keys.ARROW_RIGHT).build().perform();

			value=getSliderLevelValue() ;
		}
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.CLOSE);
	}

	public void chooseZoomToFit(int viewNum) throws InterruptedException
	{
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.OPEN);
		click(zoomToFit);
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.CLOSE);
	}

	public void changeZoomValue(int viewNum,int zoomNumber) throws InterruptedException 
	{              
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.OPEN);

		Actions dragger = new Actions(driver);        
		for (int j = 0; j <(getWidthOfWebElement(slider)*2); j = j + 1)        
		{ 
			dragger.moveToElement(sliderThumbLabel).clickAndHold().moveByOffset(10,0).release(sliderThumbLabel).build().perform(); 
			if(getSliderValue()>zoomNumber)     
				break;                     
		}             

	}

	public void applyZoomUsingIcon(int viewNum,int zoomValue) throws InterruptedException 
	{              
		openOrCloseViewBoxToolPanel(viewNum,NSGenericConstants.OPEN);

		for (int j = 0; j <=5; j = j++)        
		{ 
			if(zoomValue<getSliderValue())
			{
				click(zoomMinIcon);  
				if(getSliderValue()<zoomValue)    
					break;
			}
			else 
			{
				click(zoomMaxIcon);
				if(getSliderValue()>zoomValue)    
					break;
			}  
		}
	}

	public boolean verifyZooToFitIconDisable(int viewBox) throws InterruptedException
	{
		openOrCloseViewBoxToolPanel(viewBox, NSGenericConstants.OPEN);

		boolean status=getAttributeValue(zoomToFit, NSGenericConstants.CLASS_ATTR).equalsIgnoreCase("zoom-dark");
		status=status && getCssValue(zoomToFit, NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.EUREKA_BORDER_COLOR);

		return status;
	}

	public List<WebElement> getPlaneRadioIcon(int whichViewbox) {

		return getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #planeContainer .headerContainer ns-icon path"));

	}

	public List<WebElement> getPlaneLabels(int whichViewbox) {

		return getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #planeContainer .headerContainer label"));

	}

	public By getPlaneByText(int whichViewbox) {

		return By.cssSelector("#Plane-"+(whichViewbox-1));

	}

	public WebElement getPlaneInfo(int whichViewbox) {

		return getElement(getPlaneByText(whichViewbox));

	}
	
	public String getPlaneOverlayText(int whichViewbox) {

		return getText(getPlaneInfo(whichViewbox));

	}

	public void selectPlane(int whichViewbox,String planeToBeSelected) throws InterruptedException {

		openTabInViewbox(whichViewbox, planeTab);
		click(getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+planeToBeSelected+"')]")));
		
		if(getText(getPlaneInfo(whichViewbox)).equalsIgnoreCase(planeToBeSelected))
			waitForTimePeriod(5000);
		else
		{
			try{
				WebDriverWait wait = new WebDriverWait(driver,30);
				wait.until(new ExpectedCondition<Boolean>() 
				{
					@Override
					public Boolean apply(WebDriver driver) {
						return (!getText(getPlaneInfo(whichViewbox)).equalsIgnoreCase(planeToBeSelected));}});
			}
			catch(TimeoutException e)
			{LOGGER.info(Utilities.getCurrentThreadId() + "Encountered Error:"+e.getMessage());}
			
			waitForTimePeriod(5000);
		}


	}

	public boolean verifyPlaneRadioAndTileIsSelected(int whichViewbox,String plane, String theme) throws InterruptedException {
		boolean status = true;
		openTabInViewbox(whichViewbox, planeTab);

		WebElement planeLabel = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]"));
		WebElement planeRadio = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]/..//*/div")).findElement(By.cssSelector("path"));
		WebElement planeTile = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]/../../ns-plane-tile"));
		WebElement planeRect = planeTile.findElement(By.cssSelector("svg[id*='icon-'] rect"));
		WebElement planeTileImage = planeTile.findElement(By.cssSelector("svg[id*='icon-']> g> g"));

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)){
			status = (getCssValue(planeLabel, NSGenericConstants.CSS_PROP_COLOR)).equalsIgnoreCase(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
			status = status && (getCssValue(planeRadio, NSGenericConstants.FILL)).equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			status = status && (getCssValue(planeRect, NSGenericConstants.FILL)).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			status = status && (getCssValue(planeTileImage, NSGenericConstants.FILL)).contains(ThemeConstants.EUREKA_TABLE_BACKGROUND_RGB);
		}


		else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)){
			status = (getCssValue(planeLabel, NSGenericConstants.CSS_PROP_COLOR)).equalsIgnoreCase(ThemeConstants.DARK_LABEL_FONT_COLOR);
			status = status && (getCssValue(planeRadio, NSGenericConstants.FILL)).equalsIgnoreCase(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			status = status && (getCssValue(planeRect, NSGenericConstants.FILL)).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			status = status && (getCssValue(planeTileImage, NSGenericConstants.FILL)).contains(ThemeConstants.DARK_TABLE_BACKGROUND_RGB);
		}

		return status;

	}


	public boolean verifyPlaneTabPresence(int whichViewbox) throws InterruptedException {

		openOrCloseViewBoxToolPanel(whichViewbox, NSGenericConstants.OPEN);

		boolean status = isElementPresent(plane);

		openOrCloseViewBoxToolPanel(whichViewbox, NSGenericConstants.CLOSE);

		return status;

	}

	public boolean verifyIconsPresenceForNonNativePlaneInQuickToolbar(int Viewbox){
		boolean status = false ;
		openQuickToolbar(Viewbox);
		if(isElementPresent(quickToolbar))
		{
			status=isElementPresent(byPanIcon);
			status=status && isElementPresent(byCinePlayIcon);
			status=status && isElementPresent(byWindowLevelingIcon) ;
			status=status && isElementPresent(byZoomIcon) ;
			status=status && isElementPresent(byScrollIcon);
			status=status && isElementPresent(byInvertIcon) ;
			status=status && isElementPresent(byTriangulationIcon);
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

	public boolean verifyIconsDisableForNonNativePlaneInViewerToolbar(int viewbox){
		boolean status=false;
		ViewerToolbar tool=new ViewerToolbar(driver);
		click(getViewPort(viewbox));

		if(isElementPresent(viewerToolbar))
		{
			status=!(getAttributeValue(panIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && !(getAttributeValue(windowWidthIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status&& !(getAttributeValue(zoomIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && !(getAttributeValue(scrollIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(pointIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(circleIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(ellipseIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(textAnnotationIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(distanceIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE)) ;
			status=status && (getAttributeValue(lineIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
			status=status && (getAttributeValue(polylineIcon.findElement(By.cssSelector("div")),NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.ICON_DISABLE));
		}
		return status ;
	}


	public boolean verifyPlaneRadioAndTileIsInactive(int whichViewbox,String plane, String theme) throws InterruptedException {
		boolean status = true;
		openTabInViewbox(whichViewbox, planeTab);

		WebElement planeLabel = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]"));
		WebElement planeRadio = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]/..//*/div")).findElement(By.cssSelector("path"));
		WebElement planeTile = getElement(By.xpath("//*[@id='planeContainer']//*/div[@class='headerContainer']/*[contains(text(),'"+plane+"')]/../../ns-plane-tile"));
		WebElement planeRect = planeTile.findElement(By.cssSelector("svg[id*='icon-'] rect"));
		WebElement planeTileImage = planeTile.findElement(By.cssSelector("svg[id*='icon-']> g> g"));

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)){
			status = (getCssValue(planeLabel, NSGenericConstants.CSS_PROP_COLOR)).equalsIgnoreCase(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
			status = status && (getCssValue(planeRadio, NSGenericConstants.FILL)).equalsIgnoreCase(ThemeConstants.EUREKA_CHECKBOX_BORDER);
			status = status && (getCssValue(planeRect, NSGenericConstants.FILL)).contains(ThemeConstants.EUREKA_TABLE_BACKGROUND_RGB);
			status = status && (getCssValue(planeTileImage, NSGenericConstants.FILL)).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
		}

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)){
			status = (getCssValue(planeLabel, NSGenericConstants.CSS_PROP_COLOR)).equalsIgnoreCase(ThemeConstants.DARK_LABEL_FONT_COLOR);
			status = status && (getCssValue(planeRadio, NSGenericConstants.FILL)).equalsIgnoreCase(ThemeConstants.DARK_INACTIVE_RADIO_BORDER);
			status = status && (getCssValue(planeRect, NSGenericConstants.FILL)).contains(ThemeConstants.EUREKA_INACTIVE_TABLE_BACKGROUND_RGB);
			status = status && (getCssValue(planeTileImage, NSGenericConstants.FILL)).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
		}
		return status;

	}


	public boolean verifyPlaneTileOnMouserHover(WebElement WhichPlaneBorder) throws InterruptedException
	{	
		boolean status=false;
		mouseHover(WhichPlaneBorder);
		status=verifyRoundedCorner(WhichPlaneBorder,NSGenericConstants.CSS_BORDER_TOP_LEFT_RADIUS, ThemeConstants.PLANE_OPENED_ROUNDED_CORNER_POPUP);
		status=verifyRoundedCorner(WhichPlaneBorder,NSGenericConstants.CSS_BORDER_TOP_RIGHT_RADIUS, ThemeConstants.PLANE_OPENED_ROUNDED_CORNER_POPUP);
		return status;
	}
	
	
	
	public boolean verifyPlaneTabContentsPresence(int viewbox) throws InterruptedException
	{	
		boolean status=false;
		status=isElementPresent(planeContainer);

		status=isElementPresent(axialIcon); 

		status=isElementPresent(coronalIcon);

		status=isElementPresent(sagittalIcon);

		status=(getPlaneRadioIcon(viewbox).size()==3);
		return status;
	}

	



}