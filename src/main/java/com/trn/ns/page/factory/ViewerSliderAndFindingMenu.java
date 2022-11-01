package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
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


public class ViewerSliderAndFindingMenu extends ViewerARToolbox {


	private WebDriver driver;

	public ViewerSliderAndFindingMenu(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	public By findingTableEle = By.cssSelector(".eur-findingsDiv .eur-findingsTableHeader");
	public By findingsBadge = By.cssSelector("#findingDiv span");

	@FindBys({@FindBy(css = ".btnCollapse ns-icon .defaultIcon")})
	public List<WebElement> toggleButton;

	@FindBy(css ="li.group.ng-star-inserted ul.subgroup.collapse.show .dataRow .dataTable > tr > td:nth-child(2)>div")
	public List<WebElement> listOfFindingsFromGroup;

	@FindBy(css ="li.group.ng-star-inserted ul.subgroup.collapse.show .dataRow .dataTable > tr > td:nth-child(1)>div")
	public List<WebElement> stateIconOfFindingsFromGroup;

	@FindBy(xpath="//*[@class='btnCollapse']/../../td[contains(@class,'layoutColumn')]/table/tr/td/div[@data-container='body']")
	public List<WebElement> groupInfo;

	// This the locator when there is warning along with group
//	@FindBy(xpath="//tr[contains(@class,'trHover')]/td[contains(@class,'layoutColumn ng-star-inserted')]/preceding-sibling::td[contains(@class,'layoutColumn clickableNode')]//*//td[1]/div")
//	public List<WebElement> groupInfoWithoutWarning;

	@FindBy(css="#tree-findingDetails td.layoutColumn.clickableNode ns-icon > div>svg>path:nth-child(1)")
	public List<WebElement> warningIcon;

	@FindBy(css="div.ps--active-y div.ps__rail-y")
	public WebElement verticalScrollBar;

	@FindBy(css = "#findingDiv span")
	public WebElement findingBadge;

	@FindBy(css = ".eur-findingsDiv .eur-findingsTableHeader")
	public WebElement findingTable;

	@FindBys(@FindBy(css = "li.group .dataRow .dataTable > tr > td:nth-child(2)>div"))
	public List<WebElement> findings;

	@FindBy(css = "div.eur-Reject-FindingColor")
	public WebElement totalRejectedFindings;

	@FindBy(css = "div.eur-Accept-FindingColor")
	public WebElement totalAcceptedFindings;

	@FindBy(css = "div.eur-Pending-FindingColor")
	public WebElement totalPendingFindings;

	@FindBy(css = ".eur-findingsDiv .row.eur-findingsTableHeader div.header-column1")
	public WebElement findingHeader;

	@FindBy(css =".eur-findingsDiv .row.eur-findingsTableHeader div.header-column1 div.slice-info")
	public WebElement sliceHeader;
	
	@FindBys(@FindBy(css ="div.treeview ul li .layoutColumn "))
	public List<WebElement> findingRows;
	
	@FindBys(@FindBy(css ="div.treeview ul li tr.trHover"))
	public List<WebElement> findingRowsOnMouseHover;

	@FindBys(@FindBy(css =".dataTable tr>td:nth-child(2)> div"))
	public List<WebElement> findingsText;

	@FindBys(@FindBy(css =".dataTable tr>td:nth-child(1)> div"))
	public List<WebElement> findingsState;
	
	@FindBys(@FindBy(css = ".dataTable tr>td:nth-child(4)> div"))
	public List<WebElement> findingsCreatedByUser;

	@FindBy(css="div.textarea")
	public WebElement editBox;

	@FindBy(css = "#deleteDiv #deleteComment> div")
	public WebElement deleteCommentButton;

	@FindBys(@FindBy(css = "g[ns-comment] > text:nth-child(2)"))
	public List<WebElement> resultComment;

	@FindBys(@FindBy(css = "g[ns-comment] > text:nth-child(2) > tspan"))
	public List<WebElement> resultMultiLineComment;

	@FindBys(@FindBy(css = "g[ns-comment] > text:nth-child(1)"))
	public List<WebElement> textComment;

	@FindBy(css = "div.contextInfo.eur-outerbox")
	public WebElement commentInfo;

	@FindBy(css =" div > ul ns-icon > div > svg  g[class*='"+NSGenericConstants.FILL+"']")
	public List<WebElement> iIconInfo;

	@FindBys(@FindBy(xpath = "//*[contains(@class,'eur-stateIcon eur-Accept-FindingColor')]/../../td[2]/div"))
	public List<WebElement> acceptedFindings;

	@FindBys(@FindBy(xpath = "//*[contains(@class,'eur-stateIcon eur-Reject-FindingColor')]/../../td[2]/div"))
	public List<WebElement> rejectedFindings;

	@FindBys(@FindBy(xpath = "//*[contains(@class,'eur-stateIcon eur-Pending-FindingColor')]/../../td[2]/div"))
	public List<WebElement> pendingFindings;


	@FindBy(css ="#tree-findingDetails > perfect-scrollbar > div > div.ps__rail-y > div")
	public WebElement findingMenuThumb;

	@FindBy(css ="#tree-findingDetails > perfect-scrollbar > div > div.ps__rail-y")
	public WebElement findingMenuScroll;

	public By getCommentIconForFinding(int whichViewbox, String findingName) throws InterruptedException {

		openFindingTableOnBinarySelector(whichViewbox);
		return By.xpath("//*[normalize-space(text())='"+findingName+"']/../../td[3]/ns-icon/div");

	}

	public boolean verifyCommentIconIsDisplayedInFindingMenu(int whichViewbox, String findingName) throws InterruptedException {

		return isElementPresent(getCommentIconForFinding(whichViewbox, findingName));

	}

	public String getCommentTextFromFindingMenu(int whichViewbox, String findingName) throws InterruptedException {

		mouseHover(getElement(getCommentIconForFinding(whichViewbox, findingName)));
		return getText(tooltip);

	}

	public boolean verifyCommentForGroupFindingInFindingMenu(int whichViewbox,String groupName,String findingName,String comment) throws InterruptedException
	{
		boolean status=false;
		String resultComment=comment+" " +"from"+" "+ findingName;
		mouseHover(getElement(getCommentIconForFinding(whichViewbox, findingName)));
		status= getText(tooltip).equalsIgnoreCase(resultComment);

		return status;

	}

	public boolean verifyLocationOfPointAndTextAnnWhenPointIsMoved(int whichViewbox, int whichPoint,int whichAnnot,String cx,String cy)
	{
		//x and y co-ordinate for handle 1 of text annotation
		TextAnnotation  textAn=new TextAnnotation(driver);
		PointAnnotation point=new PointAnnotation(driver);
		boolean xOffset1=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(0).getAttribute(NSGenericConstants.CX).equalsIgnoreCase(cx);
		boolean yOffset1=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(0).getAttribute(NSGenericConstants.CY).equalsIgnoreCase(cy);

		//co-ordinate to point circle
		String point_xCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CX);
		String point_yCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CY);

		//co-ordinate of handle 2 for text annotation
		String TextAnnotation_xCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CX);
		String TextAnnotation_yCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CY);

		boolean xOffset2=point_xCoordinate.equals(TextAnnotation_xCoordinate);
		boolean yOffset2=point_yCoordinate.equals(TextAnnotation_yCoordinate);

		return xOffset1 && yOffset1 && xOffset2 && yOffset2;

	}

	public boolean verifyLocationOfPointAndTextAnnWhenTextIsMoved(int whichViewbox, int whichPoint,int whichAnnot,String cx,String cy)
	{
		TextAnnotation  textAn=new TextAnnotation(driver);
		PointAnnotation point=new PointAnnotation(driver);
		//x and y co-ordinate for handle 1 of text annotation
		boolean xOffset1=!textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(0).getAttribute(NSGenericConstants.CX).equalsIgnoreCase(cx);
		boolean yOffset1=!textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(0).getAttribute(NSGenericConstants.CY).equalsIgnoreCase(cy);

		//co-ordinate to point circle
		String point_xCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CX);
		String point_yCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CY);

		//co-ordinate of handle 2 for text annotation
		String TextAnnotation_xCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CX);
		String TextAnnotation_yCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CY);

		boolean xOffset2=point_xCoordinate.equals(TextAnnotation_xCoordinate);
		boolean yOffset2=point_yCoordinate.equals(TextAnnotation_yCoordinate);

		return xOffset1 && yOffset1 && xOffset2 && yOffset2;

	}

	public boolean verifyLocationOfPointAndTextAnnWhenDefaultLoaded(int whichViewbox, int whichPoint,int whichAnnot)
	{
		TextAnnotation  textAn=new TextAnnotation(driver);
		PointAnnotation point=new PointAnnotation(driver);
		//co-ordinate to point circle
		String point_xCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CX);
		String point_yCoordinate=point.getCentreForPoint(whichViewbox, whichPoint).getAttribute(NSGenericConstants.CY);

		//co-ordinate of handle 2 for text annotation
		String TextAnnotation_xCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CX);
		String TextAnnotation_yCoordinate=textAn.getTextAnnotLineHandles(whichViewbox, whichAnnot).get(1).getAttribute(NSGenericConstants.CY);

		boolean xOffset2=point_xCoordinate.equals(TextAnnotation_xCoordinate);
		boolean yOffset2=point_yCoordinate.equals(TextAnnotation_yCoordinate);

		return xOffset2 && yOffset2;

	}

	public boolean verifyGroupOnSliderContainerWithoutWarningIcon(int whichViewbox, int whichMark,int whichGroup,String groupName,List<String> findings, String state) throws TimeoutException ,InterruptedException{

		List<WebElement> findingMarkersOnslider = getFindingMarkersOnSlider(whichViewbox);
		WebElement mark = findingMarkersOnslider.get(whichMark-1);
		mouseHover(mark);
		waitForTimePeriod(100);
		boolean status=false;

		status= isElementPresent(toggleButton.get(whichGroup-1)) && getText(groupInfo.get(whichGroup-1)).equalsIgnoreCase(groupName);

		click(toggleButton.get(whichGroup-1));

		List<WebElement> findingsL = stateIconOfFindingsFromGroup;

		boolean colorStatus = false;
		for(WebElement element : findingsL) {

			if(getCssValue(element, NSGenericConstants.BACKGROUND_COLOR).equals(state))
				colorStatus= true;
			else {
				colorStatus = false;
				break;
			}
		}

		boolean listStatus = convertWebElementToStringList(listOfFindingsFromGroup).equals(findings);

		return colorStatus && status && listStatus;

	}

	public boolean verifyCommentPresenceInFindingMenu(int whichViewbox, int findingNumber) throws InterruptedException {

		openFindingTableOnBinarySelector(whichViewbox);
		waitForTimePeriod(1000);
		return isElementPresent(By.cssSelector("li.group:nth-child("+findingNumber+") tr> td svg"));

	}	

	public int getGroupCount(){
		return groupInfo.size();
	}

	public String getHeaderFromFindingTable()
	{
		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		return getText(getElement(By.cssSelector(".eur-findingsTableHeader>div.header-column1.ellipses")));


	}

	public boolean verifyNumberOfFindingsOnHeaderOfFindingTable(int findingNumber) throws InterruptedException
	{

		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}

		boolean status = isElementPresent(By.cssSelector(".eur-Accept-FindingColor.eur-statusBox"));
		status = status && isElementPresent(By.cssSelector(".eur-Reject-FindingColor.eur-statusBox"));
		status = status && isElementPresent(By.cssSelector(".eur-Pending-FindingColor.eur-statusBox"));
		status=status && (getFindingsCountFromFindingTable()==findingNumber);
		return status;
	}

	public boolean verifySeriesDescriptionOnFindingTable(String seriesDesc)
	{
		boolean status=false;
		if(!isElementPresent(findingTable))
		{
			click(CLICKABILITY,gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}

		waitForElementVisibility(findingTable);
		String temp =driver.findElement(By.cssSelector("div.row.eur-findingsTableHeader div.header-column1")).getText().trim();
		LOGGER.info(Utilities.getCurrentThreadId() + "Series description is:"+temp);
		status=temp.equalsIgnoreCase(seriesDesc);

		return status;
	}

	public boolean verifyFindingIsHighlighted(int findingNumber) throws TimeoutException, InterruptedException
	{
		boolean status=false;
		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		String temp =getBackgroundColor(findingRows.get(findingNumber-1));
		status=temp.contains(ThemeConstants.EUREKA_POPUP_BACKGROUND);
		LOGGER.info(Utilities.getCurrentThreadId() + "Observed class for finding:"+temp);
		return status;

	}

	public boolean verifyFindingIsHighlighted(String findingName) throws TimeoutException, InterruptedException
	{
		boolean status=false;
		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}

		for(int i=0;i<findings.size();i++) {			
			if(findings.get(i).getText().equalsIgnoreCase(findingName)) {			
				status =getBackgroundColor(findingRows.get(i)).equalsIgnoreCase(ThemeConstants.EUREKA_POPUP_BACKGROUND);
				break;
			}
		}

		LOGGER.info(Utilities.getCurrentThreadId() + "Observed class for finding:"+findingName);
		return status;

	}

	public HashMap<String, String> getGSPSFindingList(int whichViewbox) throws TimeoutException, InterruptedException
	{
		LinkedHashMap<String,String> findingTextAndUsername = new LinkedHashMap<String,String>();	
		try{
			openFindingTableOnBinarySelector(whichViewbox);
			for(int i =0;i<findingsText.size();i++) 
				findingTextAndUsername.put(getText(findingsText.get(i)), getText(findingsCreatedByUser.get(i)));
		}
		catch(Exception e)
		{
			printStackTrace(e.getMessage());
		}
		return findingTextAndUsername;

	}

	public int getTotalRejectedFindings() throws InterruptedException 
	{
		openFindingTableOnBinarySelector(1);
		waitForElementVisibility(findingTable);
		return convertIntoInt(getText(totalRejectedFindings));
	}

	public int getTotalAcceptedFindings(int whichViewbox ) throws InterruptedException 
	{
		openFindingTableOnBinarySelector(whichViewbox);
		waitForElementVisibility(findingTable);
		return convertIntoInt(getText(totalAcceptedFindings));
	}

	public int getTotalPendingFindings() throws InterruptedException 
	{
		openFindingTableOnBinarySelector(1);
		waitForElementVisibility(findingTable);
		return convertIntoInt(getText(totalPendingFindings));
	}

	public void selectFindingFromTable(int findingNumber) throws InterruptedException
	{
		openFindingTableOnBinarySelector(1);
		click(findings.get(findingNumber-1));

	}

	public void selectFindingFromTable(int whichViewbox , int findingNumber) throws InterruptedException
	{
		openFindingsMenu(whichViewbox);
		click(findings.get(findingNumber-1));

	}

	public void selectFindingFromTable(String findingName) throws InterruptedException
	{
		openFindingTableOnBinarySelector(1);

		for(WebElement finding : findings) {
			if(getText(finding).equalsIgnoreCase(findingName)) {
				click(finding);
				break;
			}
		}

	}

	public void selectFindingFromTable(int whichViewbox, String findingName) throws InterruptedException
	{
		openFindingsMenu(whichViewbox);
		for(WebElement finding : findings) {
			if(getText(finding).equalsIgnoreCase(findingName)) {
				click(finding);
				break;
			}
		}


	}

	public String getTextOfFindingFromTable(int findingNumber) throws InterruptedException
	{
		if(!isElementPresent(findingTable))
		{
			click(CLICKABILITY,gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		waitForElementVisibility(findingTable);
		String temp =getText(findingsText.get(findingNumber-1));
		LOGGER.info(Utilities.getCurrentThreadId() + " Text for finding :"+temp);
		return temp;
	}
	
	public String getFindingState(String findingName) throws TimeoutException, InterruptedException {

		List<String> findingsL = new ArrayList<String>();
		if(!isElementPresent(findingTableEle)) {
			click(CLICKABILITY,gspsFindingSVG);
			waitForElementVisibility(findingTable); }

		findingsText.stream().map(WebElement::getText).forEach(findingsL::add);
		int temp =findingsL.indexOf(findingName);
		String backgroundColor = findingRows.get(temp).findElement(By.cssSelector("td:nth-child(1) div")).getCssValue(NSGenericConstants.BACKGROUND_COLOR);


	//	mouseHover(getSliceInfo(1));
		return backgroundColor;
	}

	protected void openFindingsMenu(int whichViewbox) throws InterruptedException {		

		if(isElementPresent(gspsFinding(whichViewbox)) && !isElementPresent(findingTableEle)) {
			click(CLICKABILITY,gspsFindingSVG);
		}
		else
			openFindingTableOnBinarySelector(whichViewbox);



	}

	public List<WebElement> getStateSpecificFindings(int whichViewbox,String state) throws InterruptedException{

		List<WebElement> findingsL = new ArrayList<WebElement>();
		openFindingsMenu(whichViewbox);	
		waitForTimePeriod(1000);
		waitForElementVisibility(findingTable);
		if(state.equals(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Accept-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.REJECTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Reject-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.PENDING_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Pending-FindingColor')]/../following-sibling::td[1]/div"));

		return findingsL;
	}

	public List<String> getFindingsName(int whichViewbox, String state) throws InterruptedException {

		List<String> findingsL = new ArrayList<String>();
		List<WebElement> items= new ArrayList<WebElement>();

		openFindingsMenu(whichViewbox);	
		//		waitForTimePeriod(1000);
		if(state.equals(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			items = getStateSpecificFindings(whichViewbox, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		else if(state.equals(ViewerPageConstants.REJECTED_FINDING_COLOR))
			items = getStateSpecificFindings(whichViewbox, ViewerPageConstants.REJECTED_FINDING_COLOR);
		else if(state.equals(ViewerPageConstants.PENDING_FINDING_COLOR))
			items = getStateSpecificFindings(whichViewbox, ViewerPageConstants.PENDING_FINDING_COLOR);

		for (int i = 0; i < items.size(); i++) {

			if(items.get(i).getAttribute(NSGenericConstants.TITLE).isEmpty())
				findingsL.add(items.get(i).getText());
			else
				findingsL.add(items.get(i).getAttribute(NSGenericConstants.TITLE));

		}
		return findingsL;
	}

	public String getSelectedFindingName() throws InterruptedException {

		int viewbox = getActiveViewbox();		
		WebElement eurekaIcon = getElement(By.cssSelector("#icon-eureka-logo"+(viewbox-1)+" svg"));
		mouseHover(eurekaIcon);
		click(eurekaIcon);
		String text  = getText(getElement(By.cssSelector("td.selectedNode td:nth-child(2) div")));
		click(eurekaIcon);
		return text;


	}

	public WebElement getFindingIcon(int whichViewbox) {
		return getElement(By.cssSelector("#icon-eureka-logo"+(whichViewbox-1)+" svg"));

	}

	public int getBadgeCountFromToolbar(int whichViewbox) throws InterruptedException{

		openARToolbar(whichViewbox);
		int value =convertIntoInt(getText(findingBadge));
		closeFindingMenu(whichViewbox);
		return value;
	}

	public int getBadgeCountFromToolbar() throws InterruptedException{

		return getBadgeCountFromToolbar(1);
	}

	public void closeFindingMenu(int whichViewbox) {

		if(isElementPresent(gspsFinding(whichViewbox)) && isElementPresent(findingTableEle)) {
			click(CLICKABILITY,gspsFindingSVG);
		}
	}

	public boolean verifyFindingIconOnBinarySelector(int whichViewbox) throws InterruptedException{
		boolean temp=false;
		Actions ac = new Actions(driver);
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		temp=isElementPresent(By.cssSelector("ns-icon[id='icon-eureka-logo"+(whichViewbox-1)+"'] svg"));
		return temp;
	}


	public boolean verifySeriesDescFindingTableHeader(int whichViewbox, String seriesDesc) throws TimeoutException, InterruptedException
	{
		boolean status=false;
		Actions ac = new Actions(driver);		
		openFindingTableOnBinarySelector(whichViewbox);
		String temp =getText(findingHeader);
		LOGGER.info(Utilities.getCurrentThreadId() + "Series description is:"+temp);
		if(temp.contains("..."))
			status=seriesDesc.contains(temp.replace("...", ""));
		else
			status=temp.equalsIgnoreCase(seriesDesc);
		ac.moveToElement(getViewPort(whichViewbox));
		return status;

	}

	public void openFindingTableOnBinarySelector(int whichViewbox) throws InterruptedException 
	{

		openARToolbar(whichViewbox);
		if(!isElementPresent(findingTableEle)) {
			click(getFindingIcon(whichViewbox));
			waitForElementVisibility(findingTable);
		}

	}
	
	

	public void openARToolbar(int whichViewbox) throws InterruptedException 
	{

		if(!isElementPresent(gspsFinding(whichViewbox))){

			mouseHover(getViewPort(whichViewbox));
			Actions ac = new Actions(driver);		
			ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
			mouseHover(getAcceptRejectToolBar(whichViewbox));
			mouseHover(getFindingIcon(whichViewbox));
		}


	}
	
	private By gspsFinding(int whichViewbox) {
		
		return By.cssSelector("#icon-eureka-logo"+(whichViewbox-1)+" svg");
	}

	public void selectFindingFromGroupOfTable(int whichViewbox, int groupNo,int findingNumber) throws InterruptedException
	{
		openFindingTableOnBinarySelector(whichViewbox);
		waitForElementVisibility(findingTable);
		click(getElements(By.cssSelector(".btnCollapse ns-icon .defaultIcon")).get(groupNo-1));
		click(listOfFindingsFromGroup.get(findingNumber-1));

	}

	public void selectGroupFromTable(int whichViewbox , int groupNumber) throws InterruptedException
	{
		openFindingsMenu(whichViewbox);
		click(groupInfo.get(groupNumber-1));
	}

	public void selectGroupFromTable(int whichViewbox , String groupName) throws InterruptedException
	{
		openFindingsMenu(whichViewbox);
//		if(groupInfo.size()>0)
			for(WebElement finding : groupInfo) {
				if(getText(finding).equalsIgnoreCase(groupName)) {
					click(finding);
					break;
				}
			}
//		else
//			for(WebElement finding : groupInfoWithoutWarning) {
//				if(getText(finding).equalsIgnoreCase(groupName)) {
//					click(finding);
//					break;
//				}
//			}

	}

	public boolean verifyFindingsStateUnderGroup(int whichViewbox, int groupNumber, String state) throws InterruptedException {

		openFindingTableOnBinarySelector(whichViewbox);
		waitForElementVisibility(findingTable);
		click(toggleButton.get(groupNumber-1));		
		waitForElementVisibility(findingTable);
		List<WebElement> findingsL = stateIconOfFindingsFromGroup;

		boolean status = false;
		for(WebElement element : findingsL) {

			if(getCssValue(element, NSGenericConstants.BACKGROUND_COLOR).equals(state))
				status= true;
			else {
				status = false;
				break;
			}
		}
		
		click(toggleButton.get(groupNumber-1));	
		return status;


	}

	public boolean verifyCommentOnScrollSlider(WebElement mark, String findingName, String comment) throws InterruptedException {

		boolean status = false;

		mouseHover(mark);
		By element = By.xpath("//*[normalize-space(text())='"+findingName+"']/../../td[3]/ns-icon/div");
		if (isElementPresent(element))
		{
			mouseHover(getElement(element));
			status=getText(tooltip).equalsIgnoreCase(comment);

		}
		return status;

	}


	public List<WebElement> getAllGSPSObjects(int whichViewbox) throws InterruptedException  {

		return getAllGSPSObjects(whichViewbox, true);

	}

	public List<WebElement> getAllGSPSObjects(int whichViewbox, boolean withMouseHover) throws InterruptedException  {

		if(withMouseHover)
			mouseHover(getViewPort(whichViewbox));
		List<WebElement> gspsElements = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-measurements]>g"));

		return gspsElements;
	}

	public void addtext(int viewnum,String inputText) throws TimeoutException, InterruptedException
	{
		mouseHover(getGSPSHoverContainer(viewnum));
		click(gspsText);
		editBox.sendKeys(inputText);
		waitForTimePeriod(100);	
		editBox.sendKeys(Keys.ENTER);
	}

	public String getFindingStateForGroupElement(int groupNo,int findingNumber) throws TimeoutException, InterruptedException {

		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		click(toggleButton.get(groupNo-1));		
		waitForElementVisibility(findingTable);
		String stateIconColor = driver.findElements(By.xpath("//div[contains(@class,'list-group')]/following-sibling::div//*//*[@class='list-group']/li/div/div/div[contains(@class,'ns-stateIcon')]")).get(findingNumber-1).getAttribute(NSGenericConstants.CLASS_ATTR);
		mouseHover(getSliceInfo(1));
		return stateIconColor;
	}

	public boolean verifyFindingIsHighlightedWithinGroup(int groupNo,int findingNumber ) throws TimeoutException, InterruptedException
	{

		boolean status=false;
		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		click(toggleButton.get(groupNo-1));
		List<WebElement> findingsL=driver.findElements(By.cssSelector("td.selectedNode"));
		waitForAllChangesToLoad();
		String temp =findingsL.get(findingNumber-1).getCssValue(NSGenericConstants.BACKGROUND_COLOR);
		status=temp.equals(ThemeConstants.EUREKA_POPUP_BACKGROUND);
		LOGGER.info(Utilities.getCurrentThreadId() + "Observed class for finding:"+temp);
		click(toggleButton.get(groupNo-1));
		return status;
	}

	
	public boolean verifyFindingMenuIsDisplyed(int whichViewbox) {
		
		
		try {
			openFindingTableOnBinarySelector(whichViewbox);
			boolean status =isElementPresent(findingTableEle);
			closedFindingsMenu(whichViewbox);
			return status;
		}catch (Exception e) {
			return false;
		}
		
		
		
		
	}
	public boolean verifyMarkerColorOnSlider(int whichViewbox, String state) throws TimeoutException, InterruptedException {

		List<WebElement> findingMarkersOnslider = getStateSpecificMarkerOnSlider(whichViewbox, state);
		boolean status = false;
		String color="";
		for(int i =0;i<findingMarkersOnslider.size();i++) {
			color = getCssValue(findingMarkersOnslider.get(i),NSGenericConstants.BACKGROUND_COLOR);			

			if(color.equals(state))
				status = true;
			else {
				status = false;
				break;
			}
		}
		return status;

	}

	public boolean verifyMarkerColorOnSlider(int whichViewbox, String state, int expectedMarkers) throws InterruptedException {

		List<WebElement> findingMarkersOnslider = getStateSpecificMarkerOnSlider(whichViewbox, state);
		String color="";

		boolean status = findingMarkersOnslider.size()==expectedMarkers;
		for(int i =0;i<findingMarkersOnslider.size();i++) {
			color = getCssValue(findingMarkersOnslider.get(i),NSGenericConstants.BACKGROUND_COLOR);	
			if(color.equals(state))
				status = status && true;
			else {
				status = false;
				break;
			}
		}
		return status;

	}

	public void openFindingTableForGroupElement(int viewnum) throws InterruptedException
	{
		mouseHover(getViewPort(viewnum));
		mouseHover(getGSPSHoverContainer(viewnum));
		if(!isElementPresent(findingTableEle)) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
	}

	public void selectAcceptOrRejectAtGroupLevel(int viewnum,int GroupNo,String actionType) throws InterruptedException
	{
		openFindingTableForGroupElement(viewnum);
		click(groupInfo.get(GroupNo-1));

		if(actionType.equalsIgnoreCase(ViewerPageConstants.GSPS_ACCEPT))
			selectAcceptfromGSPSRadialMenu();
		else
			if (actionType.equalsIgnoreCase(ViewerPageConstants.GSPS_REJECT))
				selectRejectfromGSPSRadialMenu();
	}


	public boolean verifyGroupOnSliderContainer(int whichViewbox, int whichMark,int whichGroup,String groupName,List<String> findings, String state) throws TimeoutException ,InterruptedException{

		List<WebElement> findingMarkersOnslider = getFindingMarkersOnSlider(whichViewbox);
		WebElement mark = findingMarkersOnslider.get(whichMark-1);
		mouseHover(mark);
		waitForTimePeriod(100);
		boolean status=false;

		status= isElementPresent(warningIcon.get(whichGroup-1)) &&
				getAttributeValue(warningIcon.get(whichGroup-1), NSGenericConstants.FILL).equals(ViewerPageConstants.SHADOW_COLOR)&&
				isElementPresent(toggleButton.get(whichGroup-1)) && getText(groupInfo.get(whichGroup-1)).equalsIgnoreCase(groupName);

		click(toggleButton.get(whichGroup-1));

		List<WebElement> findingsL = stateIconOfFindingsFromGroup;

		boolean colorStatus = false;
		for(WebElement element : findingsL) {

			if(getCssValue(element, NSGenericConstants.BACKGROUND_COLOR).equals(state))
				colorStatus= true;
			else {
				colorStatus = false;
				break;
			}
		}

		boolean listStatus = convertWebElementToStringList(listOfFindingsFromGroup).equals(findings);

		return colorStatus && status && listStatus;

	}


	public void selectFindingFromSlider(int whichViewbox,int whichMarker, String findingName) throws InterruptedException
	{
		mouseHover(getViewPort(whichViewbox));
		List<WebElement> findings = getAllFindingsFromSliderContainer(whichViewbox, whichMarker);
		for(WebElement finding : findings) {
			if(getText(finding).equalsIgnoreCase(findingName)) {
				click(finding);
				break;
			}
		}
	}

	public void selectFindingFromSlider(WebElement whichMarker, String findingName) throws InterruptedException
	{
		mouseHover(whichMarker);
		WebElement finding = getElement(By.xpath("//*[normalize-space(text())='"+findingName+"']/../../../tr"));
		click(finding);
	}

	public void selectFindingFromSlider(int whichViewbox,WebElement whichMarker, int findingNumber) throws InterruptedException
	{
		mouseHover(getViewPort(whichViewbox));
		mouseHover(whichMarker);
		click(findings.get(findingNumber-1));

	}

	public void selectGroupFromSlider(WebElement whichMarker, String groupName) throws InterruptedException
	{
		mouseHover(whichMarker);

//		if(groupInfo.size()>0)
			for(WebElement finding : groupInfo) {
				if(getText(finding).equalsIgnoreCase(groupName)) {
					click(finding);
					break;
				}
			}
//		else
//			for(WebElement finding : groupInfoWithoutWarning) {
//				if(getText(finding).equalsIgnoreCase(groupName)) {
//					click(finding);
//					break;
//				}
//			}
	}

	//Warning - this method will change the loaded slice location
	public List<Integer> getSlicesWhichHasGSPS(int whichViewbox) throws InterruptedException {

		int totalSlices = getMaxNumberofScrollForViewbox(whichViewbox);		
		if(getCurrentScrollPositionOfViewbox(whichViewbox)!=1)
			scrollTheSlicesUsingSlider(whichViewbox, 0, 0, 0, 1);	
		List<Integer> slicesHavingGSPS = new ArrayList<Integer>();

		for(int i =1;i<=totalSlices;i++) {
			scrollDownToSliceUsingKeyboard(1);
			if(getAllGSPSObjects(whichViewbox).size()>0) {
				slicesHavingGSPS.add(getCurrentScrollPositionOfViewbox(whichViewbox));
			}

		}		
		return slicesHavingGSPS;
	}

	public int getFindingsCountFromFindingTable() throws InterruptedException
	{

		return getFindingsCountFromFindingTable(1);

	}

	public int getFindingsCountFromFindingTable(int whichViewbox) throws InterruptedException
	{
		int value=0;
		mouseHover(getViewPort(whichViewbox));
		if(isElementPresent(gspsFinding))
			click(gspsFindingSVG);
		else
			openFindingTableOnBinarySelector(whichViewbox);

		waitForElementVisibility(findingTable);
		value = value +convertIntoInt(getText(totalAcceptedFindings));
		value = value +convertIntoInt(getText(totalRejectedFindings));
		value = value +convertIntoInt(getText(totalPendingFindings));

		return value;

	}

	public List<String> getListOfFindingsFromFindingMenu(int groupNumber){


		if(!findingTable.isDisplayed()) {
			click(gspsFindingSVG);
			waitForElementVisibility(findingTable);
		}
		click(toggleButton.get(groupNumber-1));		
		waitForElementVisibility(findingTable);
		List<String> value = convertWebElementToStringList(listOfFindingsFromGroup);
		click(toggleButton.get(groupNumber-1));	
		return value;		

	}

	public List<String> getListOfGroupsInFindingMenu(int whichViewbox) throws InterruptedException{
		openFindingTableOnBinarySelector(whichViewbox);
//		if(groupInfo.size()>0)
			return convertWebElementToStringList(groupInfo);
//		else
//			return convertWebElementToStringList(groupInfoWithoutWarning);

	}


	public WebElement getScrollSlider(int whichViewbox) {

		WebElement element = getElement(By.cssSelector("#rangeSlider"+(whichViewbox-1)+" #trackWrap"));
		return element;

	}

	public WebElement getSlider(int whichViewbox) throws InterruptedException {

		return getSlider(whichViewbox, true);

	}

	public WebElement getSlider(int whichViewbox, boolean openSlider) throws InterruptedException {

		if(openSlider)
			openSlider(whichViewbox);
		return getElement(By.cssSelector("#rangeSlider"+(whichViewbox-1)+" #thumb"));

	}

	public WebElement getSliderLine(int whichViewbox) throws InterruptedException {

		openSlider(whichViewbox);
		return getElement(By.cssSelector("#rangeSlider"+(whichViewbox-1)+" input"));

	}

	public void scrollTheSlicesUsingSlider(int whichViewbox, int x,int y, int xOffset, int yOffset) throws TimeoutException, InterruptedException {

		mouseHover(getScrollSlider(whichViewbox));
		dragAndReleaseOnViewer(getSlider(whichViewbox), x, y, xOffset, yOffset);		

	}

	public void scrollTheSlicesUsingSliderQuickVersion(int whichViewbox, int x,int y, int xOffset, int yOffset) throws TimeoutException, InterruptedException {

		mouseHover(VISIBILITY, getScrollSlider(whichViewbox), x, y);
		dragAndReleaseOnViewerQuickVersion(getSlider(whichViewbox), x, y, xOffset, yOffset);		

	}

	public void openSlider(int whichViewbox) throws TimeoutException, InterruptedException {

		mouseHover(getViewPort(whichViewbox));
		if(isElementPresent(getScrollSlider(whichViewbox))) {		
			mouseHover(getScrollSlider(whichViewbox));

		}
	}

	public boolean verifySliderPresence(int whichViewbox) throws TimeoutException, InterruptedException  {


		mouseHover(getViewPort(whichViewbox));
		if(isElementPresent(getScrollSlider(whichViewbox))) {

			Actions action = new Actions(driver);
			action.moveToElement(getScrollSlider(whichViewbox)).moveByOffset(1, 1).build().perform();
			return isElementPresent(getSlider(whichViewbox));		
		}
		else
			return false;
	}

	public  List<WebElement> getFindingMarkersOnSlider(int whichViewbox) throws TimeoutException, InterruptedException {

		openSlider(whichViewbox);
		List<WebElement> markers = getElements(By.cssSelector("div.trackMarker> div div.track-range-slider-mark"));
		return markers;
	}



	public  List<WebElement> getStateSpecificMarkerOnSlider(int whichViewbox , String state) throws InterruptedException {

		openSlider(whichViewbox);
		List<WebElement> markers = new ArrayList<WebElement>();
		if(state.equalsIgnoreCase(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			markers = driver.findElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #acceptedTrack div"));
		else if(state.equalsIgnoreCase(ViewerPageConstants.REJECTED_FINDING_COLOR))
			markers = driver.findElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #rejectedTrack div"));
		else if(state.equalsIgnoreCase(ViewerPageConstants.PENDING_FINDING_COLOR))
			markers = driver.findElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #pendingTrack div"));

		return markers;

	}

	public int getFindingsFromSliderContainer(int whichViewbox, String state) throws TimeoutException, InterruptedException {

		List<WebElement>markerSize=getStateSpecificMarkerOnSlider(whichViewbox, state);

		int findingsL=0;

		for(int i=0;i<markerSize.size();i++)
		{
		mouseHover(markerSize.get(i));
		waitForTimePeriod(100);

		waitForElementVisibility(findingTable);
		if(state.equals(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			findingsL =findingsL+convertIntoInt(getText(driver.findElement(By.cssSelector("div.row.eur-findingsTableHeader >div div.eur-Accept-FindingColor"))));
		else if(state.equals(ViewerPageConstants.REJECTED_FINDING_COLOR))
			findingsL = findingsL+convertIntoInt(getText(driver.findElement(By.cssSelector("div.row.eur-findingsTableHeader >div div.eur-Reject-FindingColor"))));
		else if(state.equals(ViewerPageConstants.PENDING_FINDING_COLOR))
			findingsL =findingsL+ convertIntoInt(getText(driver.findElement(By.cssSelector("div.row.eur-findingsTableHeader >div div.eur-Pending-FindingColor"))));
		}
		return findingsL;
	}


	public List<WebElement> getAllFindingsFromSliderContainer(int whichViewbox,int whichMarker) throws TimeoutException, InterruptedException {


		List<WebElement> findingsL = new ArrayList<WebElement>();

		mouseHover(getFindingMarkersOnSlider(whichViewbox).get(whichMarker-1));
		waitForTimePeriod(100);

		waitForElementVisibility(findingTable);
		findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon')]/../following-sibling::td[1]/div"));

		return findingsL;
	}

	public List<String> getAllFindingsFromSliderContainer(int whichViewbox,WebElement whichMarker) throws TimeoutException, InterruptedException {


		mouseHover(whichMarker);
		waitForTimePeriod(100);

		waitForElementVisibility(findingTable);
		return convertWebElementToStringList(driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon')]/../following-sibling::td[1]/div")));


	}

	public List<String> getFindingsNameFromSliderContainer(int whichViewbox,int whichMarker, String state) throws TimeoutException, InterruptedException {


		List<WebElement> findingsL = new ArrayList<WebElement>();
		mouseHover(getStateSpecificMarkerOnSlider(whichViewbox,state).get(whichMarker-1));
		waitForElementVisibility(findingTable);
		waitForTimePeriod(1000);		
		if(state.equals(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Accept-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.REJECTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Reject-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.PENDING_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Pending-FindingColor')]/../following-sibling::td[1]/div"));


		return convertWebElementToStringList(findingsL);

	}

	public String getHeaderFromSliderTable()
	{
		return getText(getHeaderFromSlider());


	}

	public WebElement getHeaderFromSlider()
	{
		waitForElementVisibility(findingTable);
		return getElement(By.cssSelector(".eur-findingsTableHeader>div.header-column1.ellipses"));


	}

	public List<String> getFindingsNameFromSliderContainer(WebElement whichMarker, String state) throws TimeoutException, InterruptedException {


		List<WebElement> findingsL = new ArrayList<WebElement>();

		mouseHover(whichMarker);
		waitForElementVisibility(findingTable);
		waitForTimePeriod(1000);		

		if(state.equals(ViewerPageConstants.ACCEPTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Accept-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.REJECTED_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Reject-FindingColor')]/../following-sibling::td[1]/div"));
		else if(state.equals(ViewerPageConstants.PENDING_FINDING_COLOR))
			findingsL = driver.findElements(By.xpath("//*[contains(@class,'eur-stateIcon eur-Pending-FindingColor')]/../following-sibling::td[1]/div"));


		return convertWebElementToStringList(findingsL);

	}

	public void scrollFindingsInSlider(String direction, int scrollAmount) throws InterruptedException{

		if((!direction.equalsIgnoreCase("up")) || (!direction.equalsIgnoreCase("down"))) {
			LOGGER.error("Invalid direction: " + direction);
		}
		String scrollVal="120";
		if(direction.equalsIgnoreCase("up")) {
			scrollVal = "-"+"120" ;
		} 


		for(int i=0 ;i<scrollAmount;i++){
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			String script ="view = view = document.evaluate(\"//*[contains(@class,'eur-stateIcon')]/../following-sibling::td[1]/div\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;"+					
					"var evt = document.createEvent(\"MouseEvents\");"+
					"evt.initMouseEvent('wheel', true, true);"+
					"evt.deltaY = "+scrollVal+";"+
					"view.dispatchEvent(evt);";


			executor.executeScript(script);	
			waitForTimePeriod(1000);
		}

		LOGGER.info("scrollAmount: " + scrollAmount);


	}

	public boolean verifyCommentTextInFindingMenu(int whichViewbox, int findingNumber, String commentText) throws InterruptedException {

		openFindingTableOnBinarySelector(whichViewbox);
		waitForTimePeriod(100);

		WebElement element = getElement(By.cssSelector("li.group:nth-child("+findingNumber+") tr> td svg"));
		waitForTimePeriod(100);
		mouseHover(element);
		return getText(tooltip).equals(commentText);

	}

	public void addMultiLineResultComment(WebElement temp, String inpText, String text2 ) throws InterruptedException 
	{
		openGSPSRadialMenu(temp);
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(100);	
		enterText(inpText);
		enterNewLineCharacter(text2);
	}

	public void addMultiLineResultComment(WebElement temp, String... text2) throws InterruptedException 
	{

		openGSPSRadialMenu(temp);
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(100);
		enterText(text2[0]);

		for(int i=1; i<text2.length;i++) {		

			WebElement element = driver.findElement(By.cssSelector("div.textarea"));
			String keysPressed = Keys.chord(Keys.LEFT_SHIFT, Keys.ENTER);
			element.sendKeys(keysPressed);
			enterText(text2[i]);

		}
		pressKeys(Keys.ENTER);

	}

	public void enterNewLineCharacter(String text2) throws InterruptedException{

		WebElement element = driver.findElement(By.cssSelector("div.textarea"));
		String keysPressed = Keys.chord(Keys.SHIFT, Keys.ENTER);		
		element.sendKeys(keysPressed);
		waitForTimePeriod(100);	
		enterText(text2);
		pressKeys(Keys.ENTER);
		waitForTimePeriod(100);	 
	}  


	public boolean addResultComment(int whichviewbox , int groupID,int findingNumber,String inpText) throws InterruptedException 
	{
		selectFindingFromGroupOfTable(whichviewbox, groupID,findingNumber);	
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(100);	
		enterText(PRESENCE, editBox, inpText);
		waitForTimePeriod(300);	
		pressKeys(Keys.ENTER);
		waitForTimePeriod(100);
		//Return true if method completes without any error and closes the edit box also after edition is complete
		return !isElementPresent(editBox);
	}

	public boolean editResultComment(WebElement temp, String inpText ) throws InterruptedException 
	{
		click(CLICKABILITY,temp);
		enterText(PRESENCE, editBox, inpText);
		pressKeys(Keys.ENTER);
		waitForTimePeriod(2000);
		//Return true if method completes without any error and closes the edit box also after edition is complete
		return !isElementPresent(editBox);
	}

	public boolean addResultComment(int whichViewbox, String inpText ) throws InterruptedException 
	{
		openGSPSRadialMenu(whichViewbox);
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(100);	
		enterText(PRESENCE, editBox, inpText);
		waitForTimePeriod(100);	
		pressKeys(Keys.ENTER);
		waitForTimePeriod(100);	 
		//Return true if method completes without any error and closes the edit box also after edition is complete
		return !isElementPresent(editBox);
	}

	public boolean addResultComment(WebElement temp, String inpText ) throws InterruptedException 
	{
		openGSPSRadialMenu(temp);
		waitForElementVisibility(gspsText);
		click(CLICKABILITY,gspsText);
		waitForTimePeriod(100);	
		WebElement textArea = getElement(By.cssSelector("div.textarea"));
		textArea.sendKeys(inpText+Keys.ENTER);
		waitForElementVisibility(getGSPSToolbar());
		//Return true if method completes without any error and closes the edit box also after edition is complete
		return !isElementPresent(editBox);
	}

	public void deleteResultComment(WebElement temp) throws InterruptedException 
	{
		click(CLICKABILITY,temp);
		click(CLICKABILITY,deleteCommentButton);
		//		waitForTimePeriod(8000);
	}

	public void hoverOnAddTextButton(int whichViewbox) throws InterruptedException{

		Actions ac = new Actions(driver);		
		ac.moveToElement(getAcceptRejectToolBar(whichViewbox)).perform();
		mouseHover(getAcceptRejectToolBar(whichViewbox));
		ac.moveToElement(gspsText).perform();
		//		waitForTimePeriod(1000);
	}

	public void hoverOnAddTextButtonForFinding(WebElement temp) throws InterruptedException{

		openGSPSRadialMenu(temp);
		Actions ac = new Actions(driver);	
		ac.moveToElement(acceptRejectToolbar).build().perform();
		mouseHover(acceptRejectToolbar);
		ac.moveToElement(gspsText).perform();
		//		waitForTimePeriod(1000);
	}

	public void hoverOnAddTextButtonForFinding(int whichviewbox,int GroupID,int findingNumber) throws InterruptedException{

		selectFindingFromGroupOfTable(whichviewbox,GroupID,findingNumber);	
		Actions ac = new Actions(driver);	
		ac.moveToElement(acceptRejectToolbar).build().perform();
		mouseHover(acceptRejectToolbar);
		ac.moveToElement(gspsText).perform();
		//		waitForTimePeriod(1000);

	}

	public void editSCResultComment(WebElement temp, String inpText ) throws InterruptedException 
	{
		doubleClick(temp);
		enterText(PRESENCE, editBox, inpText);
		pressKeys(Keys.ENTER);
		waitForTimePeriod(2000);
	}


	public List<String> getFindingNames(int whichViewbox) throws InterruptedException{

		mouseHover(getViewPort(whichViewbox));
		openFindingTableOnBinarySelector(whichViewbox);

		List<WebElement> findings = getElements(By.cssSelector("li.group .dataRow .dataTable > tr > td:nth-child(2)>div"));
		List<String> names = new ArrayList<String>();
		for(int i=0;i<findings.size();i++) {

			if(!findings.get(i).isDisplayed()) {
				scrollDownUsingPerfectScrollbar(findings.get(i), findingMenuThumb);
				wait(300);
			}
			names.add(getText(findings.get(i)));

		}


		return names;
	}
	public void closedFindingsMenu(int whichViewbox) throws InterruptedException {		

		mouseHover(getViewPort(whichViewbox));
		if(isElementPresent(findingTable))
			click(gspsFindingSVG);
	}
	
	public WebElement getFindingTable(int whichViewbox) throws InterruptedException {

		openFindingTableOnBinarySelector(whichViewbox);
		return driver.findElement(By.cssSelector("#findingsDiv"+(whichViewbox-1)+" div.eur-findingsDiv"));

	}
}

