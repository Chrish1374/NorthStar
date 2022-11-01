package com.trn.ns.page.factory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.web.page.WebActions;

public class PatientListPage extends WebActions{

	private WebDriver driver;	

	public PatientListPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForPatientPageToLoad();

	}

	public void waitForPatientPageToLoad() {

		waitForElementVisibility(patientListTab);
		waitForElementInVisibility(loadingIconInPatient);
		waitForElementInVisibility(loadingIconInStudy);
		waitForElementsVisibility(leftPanel);
		waitForElementsVisibility(rightPanel);
		waitForElementsVisibility(searchPanel);
		waitForElementsVisibility(patIDHeader);
		waitForElementsVisibility(patNameHeader);


	}

	public void waitForSearchSectionToBeDisplayed() {

		waitForElementVisibility(By.cssSelector(".ns-search-form.container-fluid"));
		waitForElementVisibility(By.cssSelector(".ns-search-form div.searchTextSection"));
		waitForElementVisibility(By.cssSelector(".ns-search-form .modalitySection"));
		waitForElementVisibility(By.cssSelector(".ns-search-form  .sexSection"));
	}

	public By leftPanel = By.cssSelector("div#patients-container #left-panel");
	public By rightPanel = By.cssSelector("div#patients-container #right-panel");
	public By searchPanel = By.cssSelector("div#top-container .ns-patient-search");
	public By loadingIconInPatient = By.cssSelector("#patient-list-scrollable-container > ns-tablelayout > fieldset > ns-spinner > div > div");
	public By loadingIconInStudy = By.cssSelector("#right-panel > ns-patient-studies > div > perfect-scrollbar > div > div.ps-content > div > ns-tablelayout > fieldset > ns-spinner > div > div");
	
	public By patientListHeader = By.cssSelector("#patient-list-scrollable-container div.columnHeaderOverFlow");
	public By patIDHeader = By.xpath(".//th[@id='1']");
	public By patNameHeader = By.xpath(".//th[@id='2']");
	public By studyData = By.cssSelector("#right-panel > ns-patient-studies tr.row.tableRow");


	@FindBy(css= "div#left-panel  .mat-tab-list div[id*='mat-tab-label']:nth-child(1)")
	public WebElement patientListTab;

	@FindBy(css= "div#left-panel  .mat-tab-list div[id*='mat-tab-label']:nth-child(2)")
	public WebElement searchAndViewedHistoryTab;

	@FindBy(css ="#patient-list-scrollable-container ns-tablelayout > fieldset > div")
	public WebElement patientListTable;

	@FindBy(xpath=".//th[@id='1']")
	public WebElement patientIDHeader;

	@FindBy(xpath=".//th[@id='2']")
	public WebElement patientNameHeader;

	@FindBy(xpath=".//th[@id='3']")
	public WebElement sexHeader;

	@FindBy(xpath=".//th[@id='4']")
	public WebElement dobHeader;

	@FindBy(xpath=".//th[@id='5']")
	public WebElement acqutisionHeader;

	@FindBy(xpath=".//th[@id='6']")
	public WebElement modalityHeader;

	@FindBy(xpath=".//th[@id='7']")
	public WebElement siteHeader;

	@FindBy(xpath=".//th[@id='28']")
	public WebElement machineHeader;

	@FindBys(@FindBy(css="#patient-list-scrollable-container  tr.row.tableRow"))
	public List<WebElement> patientRows;

	@FindBys(@FindBy(css="#patient-list-scrollable-container th.columnHeader"))
	public List<WebElement> patientTableHeaders;

	@FindBys(@FindBy(css=".patientList-div th:not([class*='nsColDisplay'])"))
	public List<WebElement> displayedPatientTableHeaders;


	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='2']"))
	public List<WebElement> patientNamesList;

	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='1']"))
	public List<WebElement> patientIdsList;	

	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='3']"))
	public List<WebElement> patientSexList;	

	@FindBys(@FindBy(xpath=".//td[@id='28']"))
	public List<WebElement> patientMachineList;	

	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='6']"))
	public List<WebElement> modalityNamesList;

	@FindBy(css="#sortIcon div svg")
	public WebElement sortIcon;

	@FindBy(css="div.tooltip-inner")
	public WebElement tooltip;

	@FindBy(xpath=".//th[@id='8']")
	public WebElement accessionNoHeader ;

	@FindBy(xpath=".//th[@id='9']")
	public WebElement studyDescriptionHeader;

	@FindBy(xpath=".//th[@id='10']")
	public WebElement eurekaHeader;

	@FindBy(xpath=".//th[@id='11']")
	public WebElement imagesHeader;

	@FindBy(xpath=".//th[@id='12']")
	public WebElement modalityHeaderOnStudyPage;

	@FindBy(xpath=".//th[@id='13']/div/div")
	public WebElement studyDateTimeHeader;

	@FindBy(xpath=".//th[@id='14']")
	public WebElement referringPhysicianHeader;

	@FindBy(xpath=".//th[@id='15']")
	public WebElement institutionHeader;

	@FindBys(@FindBy(css="#right-panel > ns-patient-studies tr.row.tableRow"))
	public List<WebElement> studyRows;

	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='9']"))
	public List<WebElement> studyDescriptionList;

	@FindBys(@FindBy(css=".patient-information div.property div"))
	public List<WebElement> patientInformationOnStudyPage;

	@FindBys(@FindBy(css=".patient-information div.property label"))
	public List<WebElement> patientInformationHeaderOnStudyPage;

	@FindBys(@FindBy(css="td.columnWord.displayToolTip[id='12']"))
	public List<WebElement> modalityListOnStudyPage;


	//Scrollbars 

	@FindBy(css="#patient-list-scrollable-container .perfectScroll .ps__rail-x")
	public WebElement horizontalScrollbarOnPatientTable;

	@FindBy(css="#patient-list-scrollable-container .perfectScroll .ps__rail-x div")
	public WebElement horizontalScrollbarThumbOnPatientTable;

	// New Results Notifications
	public By notificationDiv= By.cssSelector("ns-notification-status div.message");

	@FindBy(css="ns-notification-status div.message")
	public WebElement notificationUI;

	@FindBy(css="ns-notification-status div.message .notification-title")
	public WebElement notificationTitle;

	@FindBy(css="ns-notification-status div.message .notification-message")
	public WebElement notificationMessage;


	@FindBys(@FindBy(css="div.dialog-container"))
	public List<WebElement> notificationTiles;	

	@FindBys(@FindBy(css="a.closeIcon"))
	public List<WebElement> closeIconOnNotification;	


	@FindBy(css="[id*='mat-tab-content'] ns-patient-list > div > perfect-scrollbar > div > div.ps__rail-x")
	public WebElement patientListHoriScrollbar;

	@FindBy(css="[id*='mat-tab-content'] ns-patient-list > div > perfect-scrollbar > div > div.ps__rail-x > .ps__thumb-x")
	public WebElement patientListHoriScrollbarThumb;

	@FindBy(css="#patient-list-scrollable-container > ns-tablelayout > fieldset > div > table > tbody > perfect-scrollbar > div > div.ps__rail-y")
	public WebElement patientListVerticalScrollbar;

	@FindBy(css=" #patient-list-scrollable-container > ns-tablelayout > fieldset > div > table > tbody > cdk-virtual-scroll-viewport > div.ps__rail-y > div")
	public WebElement patientListVerticalScrollbarThumb;


	@FindBy(css="#right-panel > ns-patient-studies > div > perfect-scrollbar > div > div.ps__rail-x > div")
	public WebElement studyListHoriScrollbarThumb;

	public By studyListThumb = By.cssSelector("#right-panel > ns-patient-studies > div > perfect-scrollbar > div > div.ps__rail-x > div");

	//Search Section 

	//All Labels 

	@FindBy(css="div section.sexSection")
	public WebElement sexLabel;

	@FindBy(css="section.machineDropDownSection > div.machineDropDownContainer")
	public WebElement machinesLabel;

	@FindBy(css="section.acquisitionDropDownSection > div")
	public WebElement acquisitionLabel;

	@FindBy(css="ns-modalities>div")
	public WebElement modalityLabel;

	// dropdown And text boxes

	@FindBy(css=".searchTextSection div")
	public WebElement searchLabel;

	@FindBy(css="input#txtSearch")
	public WebElement searchTextbox;

	@FindBy(css=".checkbox-inline.gender-label:nth-of-type(2)")
	public WebElement femaleLabel;

	@FindBy(css=".checkbox-inline.gender-label:nth-of-type(1)")
	public WebElement maleLabel;

	@FindBy(css=".checkbox-inline.gender-label:nth-of-type(2) svg")
	public WebElement femaleCheckbox;

	@FindBy(css=".checkbox-inline.gender-label:nth-of-type(1) svg")
	public WebElement maleCheckbox;


	@FindBy(css="section.acquisitionDropDownSection button")
	public WebElement acquisitiondateButton;

	@FindBy(css="section.acquisitionDropDownSection .themable-singleselect .drop-toggle-label")
	public WebElement selectedAcqOption;

	@FindBy(css="div.machineDropDownContainer span.caret")
	public WebElement machineDropdownButton;

	@FindBy(css="div.machineDropDownContainer .themable-multiselect > div")
	public WebElement machineDropdown;

	@FindBys({@FindBy(css=".drop-show .ps-content label")})
	public List<WebElement> machineDropdownOptions;

	@FindBy(css="button.submitBtn")
	public WebElement searchButton;

	@FindBy(css="ns-modalities span.dropdown div.defaultIcon")
	public WebElement modalityButton;

	@FindBys(@FindBy(css="ns-tile button"))
	public List<WebElement> modalitiesButtons;

	@FindBys(@FindBy(css=".selected-tiles"))
	public List<WebElement> selectedModalitiesOptions;

	@FindBy(css ="button.resetBtn")
	public WebElement clearButton;

	@FindBy(css = ".ns-toggle-icon svg")
	public WebElement toggleButton;

	@FindBy(css = ".ns-toggle-icon div")
	public WebElement toggleButtonTitle;

	@FindBy(css=".mat-dialog-container.ng-trigger")
	public WebElement selectColumnDialogue;

	@FindBy(css="#mat-dialog-title-4")
	public WebElement selectColumnDialogueTitle;

	@FindBys({@FindBy(css=".row.element-div label")})
	public List<WebElement> columnNamesInSelectColDialogue;

	@FindBys({@FindBy(css=".row.element-div input")})
	public List<WebElement> checkboxInSelectColDialogue;

	@FindBy(css=".themable-multiselect  div.drop-toggle-label")
	public WebElement selectedMachineValues;

	@FindBy(css="div.themable-singleselect button")
	public WebElement selectedAcquisitionValues;

	@FindBy(css="div.drop-show .ps__thumb-y")
	public WebElement machineVerticalScrollbarThumb;

	@FindBy(css="div.drop-show .ps__rail-y")
	public WebElement machineVerticalScrollbar;

	@FindBys(@FindBy(css="label svg rect.checked-background"))
	public List<WebElement> machineDropdownCheckbox;

	@FindBy(css="div .ellipsis-tile")
	public WebElement ellipsesForModality;


	//Study Panel -	

	public By patientHeader = By.className("patient-information ng-star-inserted");
	public By studydescription = By.xpath(".//th[@id='9']");
	public By patientNameOnStudyPanel = By.cssSelector(".patient-information .patient-name > div");
	public By studyTabHeaders = By.cssSelector(".study-inner-container th.columnHeader");
	public By dialogbox = By.cssSelector(".mat-dialog-container.ng-trigger");
	public By studyListRows = By.cssSelector("#right-panel > ns-patient-studies tr.row.tableRow");


	@FindBys(@FindBy(css=".study-inner-container th.columnHeader"))
	public List<WebElement> studyTableHeaders;

	//patient Information On Study Page

	@FindBy(css=".patient-information div.property:nth-of-type(1) label")
	public WebElement patientNameHeaderInPatientInfo;

	@FindBy(css="div.machineDropDownContainer .themable-multiselect > div:nth-child(2)")
	public WebElement expandedMachineDropDown;

	@FindBy(css="section.modalitySection perfect-scrollbar")
	public WebElement expandedModalityDropDown;

	@FindBy(css="div.acquisitionDropDownContainer .drop-show.ng-star-inserted")
	public WebElement expandedAcquisitionDateDropDown;

	
	@FindBy(css=".patient-information div.property:nth-of-type(2) label")
	public WebElement sexHeaderInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(3) label")
	public WebElement dateOfBirthHeaderInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(4) label")
	public WebElement ageHeaderInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(5) label")
	public WebElement acquisitionDateHeaderInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(6) label")
	public WebElement patientIDHeaderInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(7) label")
	public WebElement modalityHeaderInPatientInfo;


	@FindBy(css=".patient-information div.property:nth-of-type(1) div")
	public WebElement patientNameHeaderValueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(2) div")
	public WebElement sexHeadervalueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(3) div")
	public WebElement dateOfBirthHeaderValueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(4) div")
	public WebElement ageHeaderValueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(5) div")
	public WebElement acquisitionDateHeaderValueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(6) div")
	public WebElement patientIDHeaderValueInPatientInfo;

	@FindBy(css=".patient-information div.property:nth-of-type(7) div")
	public WebElement modalityHeaderValueInPatientInfo;

	@FindBy(css=".btn.btn-secondary.mat-raised-button")
	public WebElement selectColumnDialogueCloseButton;	

	@FindBy(css="div.result_1")
	public WebElement pendingIcon;

	@FindBy(css="div.result_2")
	public WebElement warningIcon;

	@FindBy(css="div.result_3")
	public WebElement eurekaIcon;

	@FindBys({@FindBy(css="div.result_1")})
	public List<WebElement> allpendingIcon;

	@FindBys({@FindBy(css="div.result_3")})
	public List<WebElement> allEurekaIcon;	

	@FindBy(css="div.result_4")
	public WebElement disableEurekaIcon;

	@FindBy(css=" div.wiaMachineCount")
	public WebElement numberOfResults;

	@FindBys({@FindBy(css= "div.resultList")})
	public List<WebElement> tooltipRows; 

	@FindBy(css ="td.ns-resulttableheader-td div")
	public WebElement machineNameOnEurekaAl;

	public By toolTip = By.xpath("//div[@class='ns-resultcontainer']");

	//Methods 

	public WebElement getColumnHeader(String columnName) {

		return getElement(By.xpath("//div[normalize-space(text())='"+columnName+"']/../../div"));

	}

	public boolean verifySortIcon(WebElement ele, String ascDsc) {

		WebElement columnHeader=ele.findElement(By.xpath("//*[@id='sortIcon']"));
		boolean status =  isElementPresent(columnHeader);

		if(ascDsc.equals(PatientPageConstants.ASC_CONSTANT))
			status = status && isElementPresent(By.cssSelector("#Selected_Sort_A"));
		else
			status = status && isElementPresent(By.cssSelector("#Selected_Sort"));

		return status;
	}


	public boolean verifySortIconPresence(WebElement ele) {

		WebElement columnHeader=ele.findElement(By.xpath("//*[@id='sortIcon']"));
		boolean status =  isElementPresent(columnHeader);
		return status;
	}
	public String getTooltipOnTableHeader(WebElement ele) throws InterruptedException {

		mouseHover(patientListTab);
		mouseHover(ele);
		return getText(tooltip);		

	}

	public List<String> getColumnValue(String columnName){

		int index = PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.indexOf(columnName);
		List<WebElement> values = getElements(By.cssSelector("td.columnWord.displayToolTip[id='"+(index+1)+"']"));

		List<String> allValues = new ArrayList<String>();
		for(WebElement value : values) {
		if(!isElementPresent(value)) 
			scrollDownUsingPerfectScrollbar(value, patientListVerticalScrollbarThumb);
			allValues.add(getText(value));
			values = getElements(By.cssSelector("td.columnWord.displayToolTip[id='"+(index+1)+"']"));
		}
		
		scrollUpUsingPageUp(values.get(0), patientListVerticalScrollbarThumb);
		return allValues;

	}

	public class PatientDetails{

		List<String> patientID;
		List<String> patientName;
		List<String> patientGender;
		List<Date> patientDOB;
		List<Date> patientAcq;
		List<String> modality;
		List<String> site;
		List<String> machine;

		public List<String> getPatientID() {
			return patientID;
		}
		public void setPatientID(List<String> patientID) {
			this.patientID = patientID;
		}
		public List<String> getPatientName() {
			return patientName;
		}
		public void setPatientName(List<String> patientName) {
			this.patientName = patientName;
		}
		public List<String> getPatientGender() {
			return patientGender;
		}
		public void setPatientGender(List<String> patientGender) {
			this.patientGender = patientGender;
		}
		public List<Date> getPatientDOB() {
			return patientDOB;
		}
		public void setPatientDOB(List<String> patientDOB) {
			this.patientDOB = convertStringToDate(patientDOB);
		}
		public List<Date> getPatientAcq() {
			return patientAcq;
		}
		public void setPatientAcq(List<String> patientAcq) {
			this.patientAcq = convertStringToDate(patientAcq);
		}
		public List<String> getModality() {
			return modality;
		}
		public void setModality(List<String> modality) {
			this.modality = modality;
		}
		public List<String> getSite() {
			return site;
		}
		public void setSite(List<String> site) {
			this.site = site;
		}
		public List<String> getMachine() {
			return machine;
		}
		public void setMachine(List<String> machine) {
			this.machine = machine;
		}

		private List<Date> convertStringToDate(List<String> values){

			DateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
			List<Date> convertedList = new ArrayList<Date>();

			for(String value : values) {
				try {
					Date parsedVal = sdf.parse(value);
					convertedList.add(parsedVal);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return convertedList;
		}

	}


	// Hide column and unhide methods 


	public void openHideUnhideDialogbox(String columnName) {

		WebElement header = getColumnHeader(columnName);
		performMouseRightClick(header);
		waitForElementVisibility(selectColumnDialogue);


	}

	public void openHideUnhideDialogbox(WebElement whichCol) {

		performMouseRightClick(whichCol);
		waitForElementVisibility(selectColumnDialogue);


	}

	public void closeSelectColumnDialogBox() {

		click(selectColumnDialogueCloseButton);
		waitForElementInVisibility(dialogbox);
	}

	public boolean verifyCheckboxIsEnabled(String whichCol) {


		openHideUnhideDialogbox(whichCol);
		WebElement element = getElement(By.xpath("//div[contains(@class,'row element-div')]/label[contains(text(),'"+whichCol+"')]/input"));
		boolean	inputBox = element.isEnabled();
		closeSelectColumnDialogBox();		
		return inputBox;
	}

	public boolean verifyCheckboxIsEnabled(WebElement header, String whichCol) {


		openHideUnhideDialogbox(header);
		WebElement element = getElement(By.xpath("//div[contains(@class,'row element-div')]/label[contains(text(),'"+whichCol+"')]/input"));
		boolean	inputBox = element.isEnabled();
		closeSelectColumnDialogBox();		
		return inputBox;
	}

	public List<String> getOptionsFromSelectColDialog(){

		return convertWebElementToStringList(columnNamesInSelectColDialogue);

	}

	public void checkOrUncheckColumn(String whichCol, String checkOrUncheck) {


		openHideUnhideDialogbox(whichCol);
		WebElement inputBox = getElement(By.xpath("//div[contains(@class,'row element-div')]/label[contains(text(),'"+whichCol+"')]/input"));
		switch(checkOrUncheck) {

		case PatientPageConstants.CHECK: 

			if(!inputBox.isSelected())
				inputBox.click();
			break;

		case PatientPageConstants.UNCHECK:
			if(inputBox.isSelected())
				inputBox.click();
			break;
		}
		closeSelectColumnDialogBox();
	}

	public void checkOrUncheckColumn(String whichColToClick,String whichColToSelect, String checkOrUncheck) {


		openHideUnhideDialogbox(whichColToClick);
		WebElement inputBox = getElement(By.xpath("//div[contains(@class,'row element-div')]/label[contains(text(),'"+whichColToSelect+"')]/input"));
		switch(checkOrUncheck) {

		case PatientPageConstants.CHECK: 

			if(!inputBox.isSelected())
				inputBox.click();
			break;

		case PatientPageConstants.UNCHECK:
			if(inputBox.isSelected())
				inputBox.click();
			break;
		}
		closeSelectColumnDialogBox();
	}

	public List<String> getColumnsWhichAreNotDisplayed(List<WebElement> columns){

		List<String> values = new ArrayList<String>();
		for(WebElement column : columns) {

			if(getCssValue(column, NSGenericConstants.DISPLAY_PROPERTY).equals("table-cell"))
				values.add(getText(column));

		}

		return values;

	}

	// Search Methods

	public void clickOnGivenModality(String... modalityName){
		click(modalityButton);
		for(String modality : modalityName)
			click(driver.findElement(By.xpath("//button[starts-with(normalize-space(text()),'"+ modality +"')]")));	
		click(modalityButton);

	}

	public List<WebElement> getAllModalities(){

		click(modalityButton);

		List<WebElement> modalities = getElements(By.cssSelector("div.ps-content button"));
		click(modalityButton);
		return modalities;

	}

	public List<String> getModalities(){

		click(modalityButton);

		List<String> modalities = convertWebElementToStringList(getElements(By.cssSelector("div.ps-content button")));
		click(modalityButton);
		return modalities;

	}
	public boolean verifyModalityIsSelected(String modalityName) {


		boolean status = getText(selectedModalitiesOptions.get(0)).equalsIgnoreCase(modalityName);
		status = isElementPresent(By.xpath("//span[@class='selected-tiles']//*//button[starts-with(normalize-space(text()),'"+ modalityName +"')]"));
		status = status && isElementPresent(By.xpath("//span[@class='selected-tiles']//*//button[starts-with(normalize-space(text()),'"+ modalityName +"')]/ns-icon"));
		click(modalityButton);
		status = status && isElementPresent(By.xpath("//div[@class='ps-content']//*//button[starts-with(normalize-space(text()),'"+ modalityName +"')]"));
		status = status && isElementPresent(By.xpath("//div[@class='ps-content']//*//button[starts-with(normalize-space(text()),'"+ modalityName +"')]/ns-icon"));
		click(modalityButton);
		return status;		

	}

	public void deSelectModality(String modalityName) {

		click(modalityButton);
		if(isElementPresent(By.xpath("//button[starts-with(normalize-space(text()),'"+ modalityName +"')]/ns-icon"))) {
			WebElement modality = driver.findElement(By.xpath("//button[starts-with(normalize-space(text()),'"+ modalityName +"')]/ns-icon"));
			click(modality);
		}
		click(modalityButton);

	}


	public boolean VerifyModalityIsSelectedInExpandAndCollapseMode(String... modalityName){
		click(modalityButton);
		boolean status=false;
		for(int i=0;i<modalityName.length;i++){

			status = isElementPresent(By.xpath("//span[@class='selected-tiles']//*//button[starts-with(normalize-space(text()),'"+ modalityName[i] +"')]"));
			status = status && isElementPresent(By.xpath("//span[@class='selected-tiles']//*//button[starts-with(normalize-space(text()),'"+ modalityName[i] +"')]/ns-icon"));
			status = status && isElementPresent(By.xpath("//div[@class='ps-content']//*//button[starts-with(normalize-space(text()),'"+ modalityName[i] +"')]"));
			status = status && isElementPresent(By.xpath("//div[@class='ps-content']//*//button[starts-with(normalize-space(text()),'"+ modalityName[i] +"')]/ns-icon"));


			if(!status==false)
				break;

		}
		click(modalityButton);
		return status;

	}

	public boolean VerifyCrossIconInExpandModeWhenModalityIsSelected(String modalityName){

		click(modalityButton);
		boolean status = isElementPresent(By.xpath("//div[@class='ps-content']//*//button[starts-with(normalize-space(text()),'"+ modalityName +"')]/ns-icon"));

		return status;

	}
	public List <WebElement> searchPatient(String patientName, String modalityName, String female,String male) throws InterruptedException{

		return searchPatient(patientName, modalityName, female,male,"",new String[0]);		


	}

	public List <WebElement> searchPatient(String patientName, String modalityName, String female,String male, String value, String... machines) throws InterruptedException{


		if(machines.length > 0) selectMachines(machines);
		if(!value.isEmpty()) selectAcquisiton(value);
		if(!patientName.isEmpty()) enterText(searchTextbox, patientName);
		if(!modalityName.isEmpty()) clickOnGivenModality(modalityName);
		if(!male.isEmpty()) click(maleCheckbox);
		if(!female.isEmpty()) click(femaleCheckbox);
		click(searchButton);	
		waitForPatientPageToLoad();
		return patientNamesList;

	}

	public void selectMachines(String... machines) {

		click(machineDropdownButton);
		for(int i =0;i< machines.length;i++) {
			WebElement ele = getElement(By.xpath("//label[normalize-space(text())='"+machines[i]+"']"));
			scrollDownUsingPerfectScrollbar(ele, machineVerticalScrollbarThumb);
			click(ele);
		}
		click(machineDropdownButton);
	}

	public void deselectMachines(String... machines) {

		click(machineDropdownButton);
		for(int i =0;i< machines.length;i++) {
			WebElement ele = getElement(By.xpath("//label[normalize-space(text())='"+machines[i]+"']/span/ns-icon/div"));

			if(!ele.isDisplayed())
				scrollDownUsingPerfectScrollbar(ele, machineVerticalScrollbarThumb);

			if(ele.findElements(By.cssSelector("svg rect.checked-background")).size()==1){
				click(ele);
			}
		}
		click(machineDropdownButton);
	}

	public String[] getAllMachinefromDropdown(){
		List<String> list = new LinkedList<String>(); 
		click(machineDropdownButton);
		for(int i=0; i<machineDropdownOptions.size(); i++){	
			scrollDownUsingPerfectScrollbar(machineDropdownOptions.get(i), machineVerticalScrollbarThumb);
			list.add(getText(machineDropdownOptions.get(i))); 
		}
		String[] arr = new String[list.size()]; 

		// ArrayList to Array Conversion 
		for (int i =0; i < list.size(); i++) 
			arr[i] = list.get(i);
		click(machineDropdownButton);
		return arr; 
	}

	public void searchAndReset(String patientName, String modality, String female, String male) throws InterruptedException {
		searchPatient(patientName, modality, female, male);
		click(clearButton);

	}

	public void selectAcquisiton(String value) {

		click(acquisitiondateButton);
		click(getElement(By.xpath("//label[contains(text(),'"+value+"')]")));
	}

	public List<String> getAcquisitionOptions() {

		click(acquisitiondateButton);
		List<String> options = convertWebElementToStringList(getElements(By.cssSelector(".drop-show label")));
		click(acquisitiondateButton);

		return options;

	}

	public void triggerRecentSearch(int whichSearch) throws InterruptedException {

		click(searchTextbox);
		waitForElementsVisibility(By.cssSelector("div.panel-container"));
		click(getElements(By.cssSelector("div.panel-container .row")).get(whichSearch-1));

	}

	public List<Map<String, String>> getAllRecentSearchValues() throws InterruptedException {

		click(searchTextbox);
		waitForElementsVisibility(By.cssSelector("div.panel-container"));	

		List<Map<String,String>> allRecentSearch = new ArrayList<Map<String,String>>();

		List<WebElement> rows = getElements(By.cssSelector("div.panel-container .row"));
		for(WebElement row : rows) {
			Map<String,String> rowValues = new HashMap<String,String>();
			rowValues.put(PatientPageConstants.RECENT_SEARCH.get(0), getText(row.findElement(By.cssSelector(".search-text"))));
			rowValues.put(PatientPageConstants.RECENT_SEARCH.get(1), getText(row.findElement(By.cssSelector(".search-sex"))));
			rowValues.put(PatientPageConstants.RECENT_SEARCH.get(2), getText(row.findElement(By.cssSelector(".search-machine"))));
			rowValues.put(PatientPageConstants.RECENT_SEARCH.get(3), getText(row.findElement(By.cssSelector(".search-aquisition"))));
			rowValues.put(PatientPageConstants.RECENT_SEARCH.get(4), getText(row.findElement(By.cssSelector(".search-modality"))));

			allRecentSearch.add(rowValues);

		}

		mouseHover(searchButton);
		return allRecentSearch;

	}

	public boolean verifyCheckboxIsChecked(WebElement checkbox) {

		WebElement rectangle = checkbox.findElement(By.cssSelector("rect"));
		boolean status = getAttributeValue(rectangle, NSGenericConstants.CLASS_ATTR).equals(NSGenericConstants.CHECKED_BACKGROUND);
		status = status && getCssValue(rectangle, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);		
		try {
			WebElement path = checkbox.findElement(By.cssSelector("path"));
			status = status && getAttributeValue(path, NSGenericConstants.CLASS_ATTR).equals("checked-tick");
		}catch (Exception e) {
			status = false;
		}

		return status;




	}

	public boolean verifyAcquisitionValueSelected(String value) {

		return getText(getElement(By.cssSelector("div.themable-singleselect button"))).equalsIgnoreCase(value);


	}

	public boolean verifyMachineValueSelected(String value) {


		return getText(selectedMachineValues).equalsIgnoreCase(value);


	}

	public boolean verifyCorrectPatientSearchFromMachine() throws InterruptedException{

		boolean status=false;
		String selectedMachines = getText(selectedMachineValues).replace(";", "");
		List<String> machines = Arrays.asList(selectedMachines.split("\\s"));

		if(!isElementPresent(machineHeader))
		scrollRightUsingPerfectScrollbar(machineHeader, patientListHoriScrollbarThumb, getWidthOfWebElement(patientIDHeader), getWidthOfWebElement(patientListTable));
		
		List<String> resultMachine =convertWebElementToStringList(patientMachineList);
		for(int i=0; i<resultMachine.size(); i++){

			for(int j = 0; j<machines.size();j++){
				if(resultMachine.get(i).trim().contains(machines.get(j).trim()))
				{
					status = true;
					break;
				}
			}
		}
		if(!isElementPresent(patientIDHeader))
		scrollRightUsingPerfectScrollbar(patientIDHeader, patientListHoriScrollbarThumb, -getWidthOfWebElement(patientIDHeader), getWidthOfWebElement(patientListTable));
		return status;
	}

	public boolean verifyCorrectPatientSearchFromAcquisition(int value) throws InterruptedException{

		boolean status=false;
		if(!isElementPresent(acqutisionHeader))
		scrollRightUsingPerfectScrollbar(acqutisionHeader, patientListHoriScrollbarThumb, getWidthOfWebElement(patientIDHeader), getWidthOfWebElement(patientListTable));
		List<String> resultAcquisition = getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));		
		for(int i=0; i<resultAcquisition.size(); i++){

			Date date =DateUtils.addDays(new Date(), -+value);
			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.ACQUISITIONCOLUMNFORMAT);
			String formattedDate = sdf.format(date);

			if(resultAcquisition.get(i).trim().contains(formattedDate.trim()))
			{
				status = true;
				//break;
			}

		}
		if(!isElementPresent(patientIDHeader))
		scrollRightUsingPerfectScrollbar(patientIDHeader, patientListHoriScrollbarThumb, -getWidthOfWebElement(patientIDHeader), getWidthOfWebElement(patientListTable));

		return status;
	}

	public boolean verifyRecentSearchAfterMachineSearch() throws InterruptedException{

		boolean status=false;
		List<String> machineName =Arrays.asList(getText(selectedMachineValues).split(";"));
		List<String> machineNameFromRecentSearch =Arrays.asList(getAllRecentSearchValues().get(0).get(PatientPageConstants.RECENT_SEARCH.get(2)).split(","));
		if(machineName.equals(machineNameFromRecentSearch))
			status = true;
		return status;
	}

	public boolean verifyRecentSearchAfterAcquisitionDropDownSearch() throws InterruptedException{

		boolean status=false;
		ArrayList<String> AcquisitionName =PatientPageConstants.ACQUISITIONDATE;
		int dateSize= AcquisitionName.size();

		String recentFirstRow =getAllRecentSearchValues().get(0).get(PatientPageConstants.RECENT_SEARCH.get(3));

		for(int i=0;i<dateSize;i++){
			if(recentFirstRow.equalsIgnoreCase(AcquisitionName.get(i)))
			{
				status = true;
				break;
			}
		}
		return status;

	}

	public int getSelectedMachineCount(){

		click(machineDropdownButton);
		int count = machineDropdownCheckbox.size();
		click(machineDropdownButton);
		return count;

	}

	// Patient and study Methods 
	public void clickOnPatientRow(String patientName) throws InterruptedException{

		mouseHover(patientListTab);
		
		if(!patientNamesList.get(0).isDisplayed())
			scrollUpUsingPageUp(patientNamesList.get(0), patientListVerticalScrollbarThumb);
		
		
		for(WebElement patient : patientNamesList) {

			if(!patient.isDisplayed()) 
				scrollDownUsingPerfectScrollbar(patient, patientListVerticalScrollbarThumb);
			
			if(patient.getText().trim().equalsIgnoreCase(patientName)) {
				clickUsingAction(patient);
				break;
			}

		}
		waitForStudyToLoad();

	}

	public void clickOnPatientRow(int number) throws InterruptedException{

		mouseHover(patientListTab);
		
		if(!patientNamesList.get(0).isDisplayed())
			scrollUpUsingPageUp(patientNamesList.get(0), patientListVerticalScrollbarThumb);
				
		if(!patientNamesList.get(number-1).isDisplayed()) 
				scrollDownUsingPerfectScrollbar(patientNamesList.get(number-1), patientListVerticalScrollbarThumb);
		
		click(patientNamesList.get(number-1));

		waitForStudyToLoad();

	}

	public void clickOnPatientUsingID(String patientID) throws InterruptedException{

		mouseHover(patientListTab);
		click(patientNameHeader);
		
		if(!patientIdsList.get(0).isDisplayed())
			scrollUpUsingPageUp(patientIdsList.get(0), patientListVerticalScrollbarThumb);
		
		
		for(WebElement patient : patientIdsList) {

			if(!patient.isDisplayed()) 
				scrollDownUsingPerfectScrollbar(patient, patientListVerticalScrollbarThumb);
	
			if(patient.getText().trim().equalsIgnoreCase(patientID)) {
				click(patient);
				break;
			}
		}

		waitForStudyToLoad();
	}

	public boolean verifyPatientIsPresent(String patientName) throws InterruptedException{

		List<WebElement> values = searchPatient(patientName, "", "", "");
		click(clearButton);
		return values.size()>0;
	}

	public WebElement getStudyColumnHeader(String columnName) {

		return getElement(By.xpath("//*[@class='study-outer-container']//div[normalize-space(text())='"+columnName+"']/../../div"));

	}

	public void clickOnStudy(int i){

		waitForElementVisibility(studydescription);
		try {
			mouseHover(studyRows.get(i-1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		click(studyRows.get(i-1));

	}

	public void clickOnStudy(String value){

		click(getElement(By.xpath("//*[contains(@class,'row tableRow')]/td[contains(text(),'"+value+"')]")));

	}

	public List<String> getStudyColumnValue(String columnName){

		int index = PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.indexOf(columnName);
		List<WebElement> values = getElements(By.cssSelector("td.columnWord.displayToolTip[id='"+(index+7)+"']"));

		List<String> allValues = new ArrayList<String>();

		for(WebElement value : values) {

			if(!isElementPresent(value))
				scrollIntoView(value);
			allValues.add(getText(value));

		}
		return allValues;

	}

	public void clickOntheFirstStudy(){
		clickOnStudy(1);

	}

	public void waitForStudyToLoad() {

		waitForElementInVisibility(loadingIconInStudy);
		waitForElementVisibility(patientNameHeaderInPatientInfo);
		waitForElementsVisibility(studydescription);
		waitForElementsVisibility(studyTabHeaders);
		waitForElementsVisibility(studyData);
		
	}

	public String getStudyDescription(int row){
		return getText(studyDescriptionList.get(row));
	}



	//get tooltip info on mousehover on Eureka icon on study page

	public WebElement getEurekaAIIcon(int whichRow){	

		return getElement(By.cssSelector("#right-panel > ns-patient-studies tr.row.tableRow td:nth-child(3) div.result")); 

	}

	public String getTooltipHeader(int whichRow) throws TimeoutException, InterruptedException {

		mouseHover(getEurekaAIIcon(whichRow));
		waitForTimePeriod(200);
		return getText(tooltipRows.get(0));
	}

	public List<WebElement> getAllMachineNameFromTooltip(int whichRow) throws InterruptedException{

		mouseHover(getEurekaAIIcon(whichRow));
		waitForElementVisibility(toolTip);
		return getElements(By.cssSelector("td.ns-resulttableheader-td"));

	}

	public List<WebElement> getAllResultStatus(int whichRow) throws InterruptedException{

		mouseHover(getEurekaAIIcon(whichRow));
		waitForElementVisibility(toolTip);
		return getElements(By.xpath("//td[@class='ns-resulttableheader-td']/../following-sibling::tr[2]"));

	}

	public List<String> getAllResultStatusFromTooltip(int whichRow) throws InterruptedException{

		return convertWebElementToStringList(getAllResultStatus(whichRow));

	}

	public List<WebElement> getAllLastUpdatedStatus(int whichRow) throws InterruptedException{

		mouseHover(getEurekaAIIcon(whichRow));
		waitForElementVisibility(toolTip);
		return getElements(By.xpath("//td[@class='ns-resulttableheader-td']/../following-sibling::tr[1]"));

	}

	public List<String> getAllLastUpdatedFromTooltip(int whichRow) throws InterruptedException{

		return convertWebElementToStringList(getAllLastUpdatedStatus(whichRow));

	}

	public List<String> getAllMachinesFromTooltip(int whichRow) throws InterruptedException{

		return convertWebElementToStringList(getAllMachineNameFromTooltip(whichRow));

	}

	public String getResultStatus() throws InterruptedException {

		String value = getText(getAllResultStatus(1).get(0));
		mouseHover(getAllResultStatus(1).get(0));
		mouseHover(patientNameHeaderInPatientInfo);
		return value;

	}

	public String getEurekaAITooltip() throws InterruptedException{	
		waitForElementVisibility(toolTip);
		return getText(getElement(By.xpath("//div[@class='ns-resultcontainer']")));  	
	}	

	public String getDateTimeFromToolTip() throws InterruptedException{

		String toolTip = getAllLastUpdatedFromTooltip(1).get(0);
		String dateTime = "" ;
		Matcher m = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4}\\,.*\\d{1,2}:\\d{1,2}\\s([A/P])(M))", Pattern.CASE_INSENSITIVE).matcher(toolTip);
		while (m.find()) { 
			dateTime = dateTime + m.group(1); 
		}
		mouseHover(getAllLastUpdatedStatus(1).get(0));
		mouseHover(patientNameHeaderInPatientInfo);
		return dateTime;
	}


	
	public  boolean dateDescendingOrderValidation(List<String> element, String STANDARDDATEFORMAT) throws ParseException{	

		ArrayList <Date> dateList  = new ArrayList<Date>();
		ArrayList <Date> dateListSort  = new ArrayList<Date>();
		boolean status = false;

		//Formatter as per acquisition date
		DateFormat  dateFormat = new SimpleDateFormat(STANDARDDATEFORMAT);


		//if acquisition date is null then removing the null date 
		while(element.remove("")){}

		// converting the string to dates and storing in dateList
		for (int j = 0; j < element.size(); j++) 
			dateList.add(dateFormat.parse(element.get(j)));


		dateListSort = new ArrayList<Date>(dateList);

		//dateListSort into Descending order
		Collections.sort(dateListSort, Collections.reverseOrder());

		// Verifying dateList and dateListSort are in same order
		if(dateList.equals(dateListSort))
			status=true; 

		return status;		
	}	

	public boolean VerifyAscSortingOrder(List<String> ele){
		ArrayList <String> arr = new ArrayList<String>();
		ArrayList <String> arr1 = new ArrayList<String>();

		boolean status = false;

		//if patientName is null then removing the null date 
		while(arr.remove("")){}

		arr1 = new ArrayList<String>(arr);
		Collections.sort(arr,String.CASE_INSENSITIVE_ORDER);

		// Verifying dateList and dateListSort are in same order
		if(arr1.equals(arr))
			status=true; 

		return status;		

	}

	public boolean verifyPatientInSearchViewedHistoryTab(String PatientName) throws InterruptedException{
		boolean status=false;
		
		openSearchAndViewedHistoryTab();
		for(WebElement patient : patientNamesList) {
			if(!patient.isDisplayed())
				scrollIntoView(patient);

				if(getText(patient).equalsIgnoreCase(PatientName)) {
					status = true;

					break;
				}
		}


		return status;
	}

	public void openSearchAndViewedHistoryTab() throws InterruptedException {
		
		if((getAttributeValue(searchAndViewedHistoryTab, PatientPageConstants.ARIA_SELECTED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE)))
		{
			mouseHover(searchAndViewedHistoryTab);
			click(searchAndViewedHistoryTab);
			waitForPatientPageToLoad();

		}
		
	}
	
	public void openPatientListTab() throws InterruptedException {
		
		if((getAttributeValue(patientListTab, PatientPageConstants.ARIA_SELECTED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_FALSE)))
		{
			mouseHover(patientListTab);
			click(patientListTab);
			waitForPatientPageToLoad();
		}
		
	}

	public boolean verifyTableRowBorder() {
		
		boolean status = true;
		for(WebElement row : patientRows) {
			status = status && getCssValue(row, NSGenericConstants.TABLE_BORDER_BOTTOM).equalsIgnoreCase(PatientPageConstants.EUREKA_ROW_BOTTOM_BORDER_COLOR);
			if(!status)
				break;
		}
		return status;
		
	}
	
	public boolean verifyFirstRowIsSelectedAndInfoDisplayedOnStudy() {
		
		boolean status = true;
		for(WebElement row : patientRows) {
			status = status && getCssValue(row, NSGenericConstants.TABLE_BORDER_BOTTOM).equalsIgnoreCase(PatientPageConstants.EUREKA_ROW_BOTTOM_BORDER_COLOR);
			if(!status)
				break;
		}		
		status = status &&  getCssValue(patientRows.get(0),NSGenericConstants.CSS_PRO_BACKGROUND).equals(PatientPageConstants.EUREKA_ROW_SELECTED_COLOR);
		status = status && getText(patientNamesList.get(0)).equalsIgnoreCase(getText(patientNameHeaderValueInPatientInfo));
		status = status && getText(patientIdsList.get(0)).equalsIgnoreCase(getText(patientIDHeaderValueInPatientInfo));
		
		return status;
		
	}
	
	
	public boolean verifyNotifactionDimensions() {

		int x = getXCoordinate(notificationTiles.get(0));
		int y = getYCoordinate(notificationTiles.get(0));
		int height = getHeightOfWebElement(notificationTiles.get(0));
		int width = getWidthOfWebElement(notificationTiles.get(0));

		int searchPanelx =getXCoordinate(getElement(By.cssSelector("div#top-container")));
		int searchPanely =getYCoordinate(getElement(By.cssSelector("div#top-container")));
		int searchPanelHeight =getHeightOfWebElement(getElement(By.cssSelector("div#top-container")));
		int searchPanelWidth =getWidthOfWebElement(getElement(By.cssSelector("div#top-container")));
		int patientInfoPanelY = getYCoordinate(getElement(By.cssSelector("section.patient-information")));

		
		System.out.println("x:= "+x+" y:="+y+" height="+height+" width="+width);
		System.out.println("x:= "+searchPanelx+" y:="+searchPanely+" height="+searchPanelHeight+" width="+searchPanelWidth+" panely="+patientInfoPanelY);
		
		boolean status = (x) > (searchPanelx+searchPanelWidth)/2;
//		status = status && (searchPanely < (y+height))&& ((searchPanely+searchPanelHeight)>(y+height));
		status = status && (searchPanely < (y+height));
		status = status && (x+width)< (searchPanelx+searchPanelWidth);
		status = status &&  (y+height) < patientInfoPanelY;
		
		


		return status;
	}

	public void refreshTabPatientOrSearchAndViewedHistoryTab() throws InterruptedException
	{
		if((getAttributeValue(searchAndViewedHistoryTab, PatientPageConstants.ARIA_SELECTED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE)))
		{
			openPatientListTab();
			openSearchAndViewedHistoryTab();
		}
		else if((getAttributeValue(patientListTab, PatientPageConstants.ARIA_SELECTED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE)))
		{
			openSearchAndViewedHistoryTab();
			openPatientListTab();
			
		}

	}

	public int getRowNumber(String patientNameOrID)
	{
		int value=0;
		for(int i=0;i<patientRows.size();i++)
		{
			if(getText(patientRows.get(i)).contains(patientNameOrID))
			{
			 value=i;
			break;
			}
		}
		return value+1; 
	}

    public boolean verifyRowIsSelectedAndInfoDisplayedOnStudy(String patientName,String studyDesc) {
		
	
		boolean status = verifyRowIsSelectedAndInfoDisplayedOnStudy(patientName);
		status = status && getText(studyDescriptionList.get(0)).equalsIgnoreCase(studyDesc);
		
		return status;
		
	}
    
    public boolean verifyRowIsSelectedAndInfoDisplayedOnStudy(String patientName) {
		
    	
		int rowNumber=getRowNumber(patientName);
		String value  = getCssValue(patientRows.get(rowNumber-1),NSGenericConstants.CSS_PRO_BACKGROUND);
		boolean status = value.contains(ThemeConstants.EUREKA_VIEWBOX_NO_BACKGROUND) || value.contains(ThemeConstants.DARK_VIEWBOX_NO_BACKGROUND);
		status = status && getText(patientInformationOnStudyPage.get(0)).equalsIgnoreCase(patientName);
		return status;
		
	}
    
    public String getPatientName(int whichPatient){
    	
    	
    	return getText(patientNamesList.get(whichPatient-1));
    	
    }
    
   
}
