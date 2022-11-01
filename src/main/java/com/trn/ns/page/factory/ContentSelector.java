package com.trn.ns.page.factory;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.Utilities;

public class ContentSelector extends ViewerPage {

	private WebDriver driver;

	public ContentSelector(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}

	public By seriesPanel = By.cssSelector("ns-series-panel div.ps-content");
	public By csSpinner = By.cssSelector("[id*='mat-tab-content'] ns-spinner div.loader");
	public By spinningWheel = By.cssSelector("#container  ns-spinner");


	//new added
	@FindBy(css="div.dockedTabContainer div:nth-child(1)")
	public WebElement seriesTab;
	
	@FindBy(css="div[id^='mat-tab-label-'][id$='1']")
	public WebElement seriesTabOpened;

	@FindBys({@FindBy(css="div > ul > li> div table.dataTable td> div")})
	public List<WebElement> headerName;
	
	@FindBy(xpath="//div[normalize-space(text())='"+ViewerPageConstants.SOURCE_TAG+"']")
	public WebElement source;
	
	@FindBys({@FindBy(css="#SourceGroup_subgroup li td.layoutColumn td:nth-child(3) div")})
	public List<WebElement>allSeriesFromSeriesTab;
	
	@FindBys({@FindBy(css="#SourceGroup_subgroup li td.layoutColumn td:nth-child(4) div")})
	public List<WebElement>allSeriesCreationDateFromSeriesTab;
	
	@FindBys({@FindBy(xpath="//ul [not(contains(@id,'SourceGroup'))] [(contains(@id,'subgroup'))]/..//td[(@class='layoutColumn')]")})
	public List<WebElement> allMachineName;

	@FindBys(@FindBy(xpath = "//ul [not(contains(@id,'SourceGroup'))] [(contains(@id,'subgroup'))]/li/div/table/tr/td/table/tr/td[3]/div"))
	public List<WebElement> allResultFromSeriesTab;
	
	@FindBys({@FindBy(css=".defaultIcon #icn-eureka-minimize")})
	public WebElement closeToolWindow;
	
	@FindBy(xpath="//div[normalize-space(text())='"+ViewerPageConstants.USER_CREATED_RESULT+"']")
	public WebElement userCreatedResults;
	
	@FindBy(css="div.btnCollapse ns-icon")
	public List<WebElement> toggleButton;
	
	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;
	
	@FindBys({@FindBy(xpath="//ul [not(contains(@id,'SourceGroup'))] [(contains(@id,'subgroup'))] //*[(contains(@class,'dataRow'))]")})
    public List<WebElement>csRows;

	public void toggleOnAndOff(boolean onOrOff)
	{
	      for(int i=1;i<toggleButton.size();i++) 
	      {
    if(onOrOff)
	    		{
		if(getAttributeValue(toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE))
			click(CLICKABILITY,toggleButton.get(i));
	    		}
    else
               {
	if(getAttributeValue(toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE))
		click(CLICKABILITY,toggleButton.get(i));
               }
	      }
	}
	
	public void waitForSeriesTabToLoad() throws InterruptedException {

		waitForElementInVisibility(spinningWheel);
		waitForElementInVisibility(csSpinner);
		waitForElementVisibility(seriesPanel);
		waitForElementVisibility(minimizeIcon);
		
	}
	
	public void dragSeries(WebElement seriesOrResult) throws InterruptedException, AWTException
	{
		//get X and Y co-ordinate for Series (drag)
	     int x = getXCoordinate(seriesOrResult);
	     int y = getYCoordinate(seriesOrResult);

	     Robot robot = new Robot();
	     robot.mouseMove(x+50, y+80);
	     waitForTimePeriod(1000);
	     robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	     waitForTimePeriod(2000);
	     robot.mouseMove(x+200, y+200);
	     
	    
	}
	
	public void dropSeries(WebElement ele) throws InterruptedException, AWTException
	{
		 Robot robot = new Robot();
		 //get X and Y co-ordinate for drop location
	     int x = getXCoordinate(ele);
	     int y = getYCoordinate(ele);
	     robot.mouseMove(x+100, y+100);
	     waitForTimePeriod(1000);
	     robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	     waitForTimePeriod(2000);
	}
	
	public void dragAndDropSeriesOrResult(int viewbox,WebElement seriesOrResult) throws InterruptedException, AWTException
	{
		openAndCloseSeriesTab(true);
		dragSeries(seriesOrResult);
	    waitForTimePeriod(1000);
	    dropSeries(getViewPort(viewbox));
	    
	}
	
    public boolean openAndCloseSeriesTab(boolean openOrClose) throws InterruptedException{

    	boolean flag = false;
    	if(openOrClose){ //opening the panel if parameter is passed as true


			if(isElementPresent(By.cssSelector("div#container"))) {
				if(isElementPresent(By.cssSelector(".outputpanel perfect-scrollbar")))
				click(getElement(By.cssSelector("div.mat-tab-list > div>div:nth-child(2)")));
			}
			else
			{
				if(!isElementPresent(seriesPanel))
				click(seriesTab);
			}
    	        waitForSeriesTabToLoad();
				
			LOGGER.info(Utilities.getCurrentThreadId() + "viewer series tab opened");
	}
		else{
			LOGGER.info(Utilities.getCurrentThreadId() + "Checking for viewer series tab open");
			if(isElementPresent(seriesPanel)){
				clickUsingAction(closeToolWindow);
				waitForElementInVisibility(seriesPanel);
				LOGGER.info(Utilities.getCurrentThreadId() + "viewer series tab close");
				flag = true;
			}
		}
		
		return flag;
	}
   
    
    public  List<String> getMachineName() throws InterruptedException
	{
		openAndCloseSeriesTab(true);
		List<String> listOfMachineName = new ArrayList<String>();

		for(int i=0 ;i<allMachineName.size();i++){
			if(isElementPresent(allMachineName.get(i)))
				listOfMachineName.add(getText(allMachineName.get(i)));
		}
	   openAndCloseSeriesTab(false);
		return listOfMachineName;

	}
	
	public List<String> getAllResults() throws InterruptedException{

		openAndCloseSeriesTab(true);
		List<String> results = new ArrayList<String>();
		for(int i=0;i<allResultFromSeriesTab.size();i++){

			if(!isElementPresent(allResultFromSeriesTab.get(i)))
				scrollIntoView(allResultFromSeriesTab.get(i));
			results.add(getText(allResultFromSeriesTab.get(i)));
		}
		openAndCloseSeriesTab(false);
		return results;
		
		
	}
	
	public List<String> getAllSeries() throws InterruptedException
	{
		openAndCloseSeriesTab(true);
		List<String> listOfSeries = new ArrayList<String>();
		for(int i=0 ;i<allSeriesFromSeriesTab.size();i++){
			if(!isElementPresent(allSeriesFromSeriesTab.get(i)))
				scrollIntoView(allSeriesFromSeriesTab.get(i));

			listOfSeries.add(getText(allSeriesFromSeriesTab.get(i)));
		}
		openAndCloseSeriesTab(false);
		return listOfSeries;
	}
	
	public List<WebElement> getAllResultsForSpecificMachine(String machineName) throws InterruptedException 
	{

		openAndCloseSeriesTab(true);
		toggleOnAndOff(true);
		List<WebElement>resultCount=new ArrayList<WebElement>();
		List<WebElement> result=driver.findElements(By.xpath("//*[normalize-space(text())='"+machineName+"']/ancestor::li/ul/li/div/table/tr/td/table/tr/td[3]/div"));
		

		for(int i=0;i<result.size();i++){
			   resultCount.add(result.get(i));
		}
		return resultCount;
	
	}

	//list down all the clone for the particular result
    public List<WebElement> getAllResultsForSpecificMachine(String machineName,int resultNo) 
   {

    	toggleOnAndOff(true);
	  List<WebElement>resultCount=new ArrayList<WebElement>();
	  List<WebElement> result=driver.findElements(By.xpath("//*[normalize-space(text())='"+machineName+"']/ancestor::li/ul/li["+resultNo+"]/ul[(contains(@class,'subgroup'))]/li/div/table/tr/td/table/tr/td[3]/div"));
	
	   for(int i=0;i<result.size();i++){
		   resultCount.add(result.get(i));
	  }
	return resultCount;

   }
	
    public List<String> getResultsForSpecificMachine(String machineName) throws InterruptedException 
    {
 	  List<WebElement>resultCount=getAllResultsForSpecificMachine(machineName);
 	 List<String> listOfResult=convertWebElementToStringList(resultCount);
 	  
 
 	return listOfResult;

    }
    
	public WebElement getSeriesElement(String seriesToSelect){

		WebElement seriesDes = null ;
		
		if(getAttributeValue(toggleButton.get(0), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE))
			click(CLICKABILITY,toggleButton.get(0));
		
		for(int i=0;i<allSeriesFromSeriesTab.size();i++){
			if(!isElementPresent(allSeriesFromSeriesTab.get(i)))
				scrollIntoView(allSeriesFromSeriesTab.get(i));
			
			if(getText(allSeriesFromSeriesTab.get(i)).equalsIgnoreCase(seriesToSelect.trim())){
				
				
				seriesDes = allSeriesFromSeriesTab.get(i);
				break;
			}
		}

		return seriesDes;
	}
	
	public WebElement getSeriesElement(String seriesToSelect, int whichSeries){

		WebElement resultDes = null ;

		for(int i=1;i<toggleButton.size();i++) {
			if(getAttributeValue(toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE))
				click(CLICKABILITY,toggleButton.get(i));	
			
		for(int j=0,count=0;j<allSeriesFromSeriesTab.size();j++){
			if(!isElementPresent(allSeriesFromSeriesTab.get(i)))
			scrollIntoView(allSeriesFromSeriesTab.get(j));
			if(getText(allSeriesFromSeriesTab.get(j)).equalsIgnoreCase(seriesToSelect))
			{
				resultDes = allSeriesFromSeriesTab.get(j);
				if(count==(whichSeries-1))
					break;
				count++;
			}
		}
	}
		return resultDes;
	}
	
    public WebElement getResultElement(String resultToSelect){

		WebElement resultDes = null ;
		
		toggleOnAndOff(true);
		for(int j=0;j<allResultFromSeriesTab.size();j++){
			if(!isElementPresent(allResultFromSeriesTab.get(j)))
				scrollIntoView(allResultFromSeriesTab.get(j));
			if(getText(allResultFromSeriesTab.get(j)).equalsIgnoreCase(resultToSelect))
			{
				resultDes = allResultFromSeriesTab.get(j);
				break;
			}}
		
	
		return resultDes;
		
		}
		
	public WebElement getResultElement(String resultToSelect, int whichResult){

		WebElement resultDes = null ;

		for(int i=1;i<toggleButton.size();i++) {
			if(getAttributeValue(toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE))
				click(CLICKABILITY,toggleButton.get(i));	
		}
			
		List<WebElement>results=new ArrayList<>();
		for(int j=0;j<allResultFromSeriesTab.size();j++){
			if(!isElementPresent(allResultFromSeriesTab.get(j)))
			scrollIntoView(allResultFromSeriesTab.get(j));
			if(getText(allResultFromSeriesTab.get(j)).equalsIgnoreCase(resultToSelect))
			{
				results.add(allResultFromSeriesTab.get(j));
			}
		}
		for(int j=0,count=0;j<results.size();j++){

			if(getText(results.get(j)).equalsIgnoreCase(resultToSelect))
			{
				if(count==(whichResult-1))
				{
					resultDes = results.get(j);
					break;
				}
			}
			count++;
		}
		return resultDes;
	}


	public WebElement getResultElementForMachine(String machineName,String resultToSelect) throws InterruptedException{

		WebElement resultDes = null ;
		
		List<WebElement>machineResults=getAllResultsForSpecificMachine(machineName);

		for(int i=0;i<machineResults.size();i++){
			if(getText(machineResults.get(i)).equalsIgnoreCase(resultToSelect))
			{
				resultDes = allResultFromSeriesTab.get(i);
				break;
			}
		}
		return resultDes;
		
		}
	
	public WebElement getWebElementForEyeIcon(String seriesOrResult){
		
		WebElement ele=driver.findElement(By.xpath("//*[normalize-space(text())='"+seriesOrResult+"']/../../td[1]/ns-icon/div//*[local-name()='g']"));
		return ele;

		
	}
	
	//get webelement for the clone
	public WebElement getResultCloneElementForGivenResult(String machineName,int resultNo,String cloneResult){

		WebElement resultDes = null ;
			
		List<WebElement>machineResults=getAllResultsForSpecificMachine(machineName, resultNo);

		for(int i=0;i<machineResults.size();i++){
			if(getText(machineResults.get(i)).equalsIgnoreCase(cloneResult))
			{
					resultDes = machineResults.get(i);
					break;
			}
		}
		return resultDes;
			
		}
	
	public List<String> getSelectedResults() throws InterruptedException 
	  	{
	  	openAndCloseSeriesTab(true);

	  	List<String> results=getAllResults();
	  	List<String> listOfResultFromContentSelector = new ArrayList<String>();

	  		
	  	openAndCloseSeriesTab(true);
	  	for(int i=0 ;i<csRows.size();i++){
	  	By ele=By.xpath("//*[normalize-space(text())='"+results.get(i)+"']/../../td[1]/ns-icon/div//*[local-name()='g']");
	  		
	  	if(isElementPresent(ele))
	  			listOfResultFromContentSelector.add(results.get(i));

	  	}

	  	openAndCloseSeriesTab(false);
	  	return listOfResultFromContentSelector;
	  	}
	
	public void selectSeriesFromSeriesTab(int viewbox, String seriesToSelect) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement series=getSeriesElement(seriesToSelect);
		dragAndDropSeriesOrResult(viewbox, series);
		waitForRespectedViewboxToLoad(viewbox);
	}
	
	public void selectSRReportFromSeriesTab(int viewbox, String seriesToSelect) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement series=getSeriesElement(seriesToSelect);
		dragAndDropSeriesOrResult(viewbox, series);
		waitForRespectedViewboxToLoad(viewbox);
	}
	
	public void selectSeriesFromSeriesTab(int viewbox, String seriesToSelect , int whichSeries) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement series=getSeriesElement(seriesToSelect, whichSeries);
		dragAndDropSeriesOrResult(viewbox, series);
		waitForRespectedViewboxToLoad(viewbox);
		openAndCloseSeriesTab(false);
	}
	
	public void selectResultFromSeriesTab(int viewbox, String resultToSelect) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement result=getResultElement(resultToSelect);
		dragAndDropSeriesOrResult(viewbox, result);
		waitForAllChangesToLoad();

    }
	
	public void selectResultFromSeriesTab(int viewbox, String resultToSelect , int whichResult) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement result=getResultElement(resultToSelect, whichResult);
		dragAndDropSeriesOrResult(viewbox, result);
		waitForRespectedViewboxToLoad(viewbox);
	
	}
	
	public void selectResultFromSeriesTabWithMachineName(int viewbox, String resultToSelect, String machineName) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement result=getResultElementForMachine(machineName,resultToSelect);
		dragAndDropSeriesOrResult(viewbox, result);
		waitForRespectedViewboxToLoad(viewbox);
		openAndCloseSeriesTab(false);
	}

	public void selectResultCloneFromSeriesTabForGivenResult(int viewbox, int resultNo, String machineName,String cloneResult) throws InterruptedException, AWTException 
	{
		openAndCloseSeriesTab(true);
		WebElement result=getResultCloneElementForGivenResult(machineName,resultNo,cloneResult);
		dragAndDropSeriesOrResult(viewbox, result);
		waitForRespectedViewboxToLoad(viewbox);
		openAndCloseSeriesTab(false);
	}
	
    //verify result is selected or not in Series tab
    public boolean verifyPresenceOfEyeIcon(String seriesName) throws InterruptedException 
	{
		Boolean status = false;
		
		openAndCloseSeriesTab(true);
		toggleOnAndOff(true);

		By ele=By.xpath("//*[normalize-space(text())='"+seriesName+"']/../../td[1]/ns-icon/div//*[local-name()='g']");
		status=isElementPresent(ele);
		
		WebElement eyeIcon = getElement(ele);
		status =status && (getCssValue(eyeIcon,NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR) || getCssValue(eyeIcon,NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.DARK_BUTTON_BORDER_COLOR));
		openAndCloseSeriesTab(false);
		return status;
	}
    
    public boolean verifyPresenceOfEyeIcon(String seriesOrResult,int whichResult) throws InterruptedException 
	{
		Boolean status = false;
		
		openAndCloseSeriesTab(true);
		toggleOnAndOff(true);

		List<WebElement> resultWithSameName=driver.findElements(By.xpath("//*[normalize-space(text())='"+seriesOrResult+"']/../../td[1]/ns-icon/div//*[local-name()='g']"));
		for(int i=0,count=0;i<resultWithSameName.size();i++)
		{
		if(count==(whichResult-1))
		{
			status = (getCssValue(resultWithSameName.get(i),NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR) || getCssValue(resultWithSameName.get(i),NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.DARK_BUTTON_BORDER_COLOR));
			break;
		  
		}
		  count++;
	    }
		openAndCloseSeriesTab(false);
		return status;
   
	}

    //verify warning icon and message for unsupported series
    public Boolean verifyWarningIconForUnsupportedSeriesInSeriesTab(String seriesDesc) throws InterruptedException
	{
		Boolean status=false;
		openAndCloseSeriesTab(true);
		By ele=By.xpath("//div[normalize-space(text())='"+seriesDesc+"']/ancestor::td/table/tr/td[1]/ns-icon/div//*");

		status = isElementPresent(ele);
		status=status && getAttributeValue(driver.findElements(ele).get(1), NSGenericConstants.FILL).equalsIgnoreCase(ViewerPageConstants.SHADOW_COLOR);
		openAndCloseSeriesTab(false);
		return status;
	}

    public String getWarningMessageForUnsupportedSeriesInSeriesTab(String seriesDesc) throws InterruptedException 
	{
    	openAndCloseSeriesTab(true);
		WebElement ele = driver.findElement(By.xpath("//div[normalize-space(text())='"+seriesDesc+"']/ancestor::td/table/tr/td[1]/ns-icon/div//*/../.."));
		mouseHover(ele);
		waitForTimePeriod(3000);
		return getText(tooltip);
	}

    //verify icon presence for series and Result
    public boolean validateIconPresenceInSeriesTab(String seriesorResult,String whichIcon,String toolTip) throws InterruptedException
	{
		boolean status =false;
		WebElement ele;
		 openAndCloseSeriesTab(true);
			ele=driver.findElement(By.xpath("//div[normalize-space(text())='"+seriesorResult+"']/ancestor::td/table//*[local-name()='tspan']"));
			status = isElementPresent(ele);
			status=status && getText(ele).equalsIgnoreCase(whichIcon);
			mouseHover(ele);
			status=status && getText(tooltip).equalsIgnoreCase(toolTip);
		return status;
	}
    
    
    public void waitUntilClonesAreCreated(int count) throws InterruptedException {
    	
    	waitUntilCountChanges(10, By.xpath("//ul [not(contains(@id,'SourceGroup'))] [(contains(@id,'subgroup'))]/li/div/table/tr/td/table/tr/td[3]/div"), count);
    	
    }
}
