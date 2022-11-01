package com.trn.ns.page.factory;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.GSONParser;
import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

import io.restassured.response.Response;

public class ViewerSendToPACS extends OutputPanel {

	private WebDriver driver;

	protected final Logger LOGGER = Logg.createLogger();

	public ViewerSendToPACS(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public By bySendToPacs = By.cssSelector("feedbackbutondiv.slide.sendToPacsDiv button");


	@FindBy(css=".btn.btn-feedback.ng-star-inserted#feedbackbuton")
	public WebElement feedBackbutton; 

	@FindBy(css="#btnSendToPacs #sendToPacs svg")
	public WebElement sendToPacs;

	@FindBys({@FindBy(css="div.slide.sendToPacsDiv button > span")})
	public List<WebElement> sendToPacsButtonTitle;

	@FindBy(css="div.preferencesDiv")
	public WebElement preferenceMenu;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']")
	public WebElement sendToPACSUI;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send accepted')]")
	public WebElement pacsAcceptedFinding;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send rejected')]")
	public WebElement pacsRejectedFinding;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send pending')]")
	public WebElement pacsPendingFinding;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send accepted')]/following-sibling ::*//*//*[@class='slider round']")
	public WebElement pacsAcceptedFindingSlider;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send rejected')]/following-sibling ::*//*//*[@class='slider round']")
	public WebElement pacsRejectedFindingSlider;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Send pending')]/following-sibling ::*//*//*[@class='slider round']")
	public WebElement pacsPendingFindingSlider;

	@FindBy(xpath="//div[@class='preferencesDiv']//*//*[@class='header']/*[contains(text(),'Enable user action')]/following-sibling ::*//*//*[@class='slider round']")
	public WebElement pacsUserActionTracking;

	@FindBy(css="#ContinueId")
	public WebElement continueButton;

	@FindBy(css="#CancelId")
	public WebElement cancelButton;

	@FindBy(css="#warningDivDesktop div.defaultIcon svg g g")
	public WebElement warningIconOnPopUP;

	//Pending Finding Result dialog
	//#doNotShowAgainId > span:nth-child(1) > ns-icon > div > svg
	@FindBy(css="#doNotShowAgainId")
	public WebElement pacsPendingResultDialogCheckbox;

	@FindBy(css="#warningDivTitle")
	public WebElement pacsPendingResultDialogHeader;

	@FindBy(css="div.header div.actionContainer label:nth-child(1)")
	public WebElement pacsPendingResultAcceptAll;

	@FindBy(css="div.header div.actionContainer label:nth-child(2)")
	public WebElement pacsPendingResultRejectAll;

	@FindBy(css="div.header div.actionContainer label:nth-child(3)")
	public WebElement pacsPendingResultNoChanges;

	@FindBy(css="div.header div.actionContainer label:nth-child(1) div.defaultIcon svg path")
	public WebElement pacsAcceptAllRadioButton;

	@FindBy(css="div.header div.actionContainer label:nth-child(2) div.defaultIcon svg path")
	public WebElement pacsRejectAllRadioButton;

	@FindBy(css="div.header div.actionContainer label:nth-child(3) div.defaultIcon svg path")
	public WebElement pacsNoChangesRadioButton;

	@FindBy(xpath="//div[contains(text(),'"+ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS+"')]")
	public WebElement errorBanner;

	@FindBy(css="#warningDivDesktop")
	public WebElement pacsPendingFindingDialog;

	// Send To Pacs Methods

	public void openSendToPACSMenu() throws InterruptedException {

		openAndCloseOutputPanel(true);
		performMouseRightClick(sendToPacs);
		waitForElementVisibility(preferenceMenu);
	}

	public void openSendToPACSMenu(boolean openOrClose,boolean sendAccepted, boolean sendRejected,boolean sendPending) throws InterruptedException {

		if(openOrClose){ //opening the panel if parameter is passed as true

		openAndCloseOutputPanel(true);
		{
				mouseHover(sendToPacs);
				performMouseRightClick(sendToPacs);
				waitForElementVisibility(preferenceMenu);
				enableSendToPACSFindingOptions(sendAccepted,sendRejected,sendPending);
				waitForTimePeriod(5000);

			}

			LOGGER.info(Utilities.getCurrentThreadId() + "viewer output panel opened");
		}
		else{
			LOGGER.info(Utilities.getCurrentThreadId() + "Checking for viewer output panel open");
			if(isElementPresent(outputPanelSection)){
				clickUsingAction(outputPanelMinimizeIcon);
				LOGGER.info(Utilities.getCurrentThreadId() + "viewer output panel close");
			}
		}

	}

	public void enableSendToPACSFindingOptions(boolean sendAccepted, boolean sendRejected,boolean sendPending) throws InterruptedException {


		if(sendAccepted) {
			if(pacsAcceptedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_DISABLE_COLOR))
				click(pacsAcceptedFindingSlider);
		}
		else
			if(pacsAcceptedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_ENABLE_COLOR))
				click(pacsAcceptedFindingSlider);

		if(sendRejected) {
			if(pacsRejectedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_DISABLE_COLOR))
				click(pacsRejectedFindingSlider);

		}else
			if(pacsRejectedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_ENABLE_COLOR))
				click(pacsRejectedFindingSlider);

		if(sendPending) {
			if(pacsPendingFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_DISABLE_COLOR))
				click(pacsPendingFindingSlider);

		}else
			if(pacsPendingFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_ENABLE_COLOR))
				click(pacsPendingFindingSlider);

		click(getElement(By.cssSelector("#studySummary")));
	}

	public void enableSendToPACSUserActionTracking(boolean userAction) throws InterruptedException {

		openSendToPACSMenu();
		if(userAction) {
			if(pacsUserActionTracking.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_DISABLE_COLOR))
				click(pacsUserActionTracking);
		}
		else
			if(pacsUserActionTracking.getCssValue(NSGenericConstants.BACKGROUND_COLOR).equals(ViewerPageConstants.SLIDER_ENABLE_COLOR))
				click(pacsUserActionTracking);

	}

	public void waitForPendingFindingDialoglToLoad() {

		waitForElementVisibility(pacsPendingResultAcceptAll);
		waitForElementVisibility(pacsPendingResultRejectAll);
		waitForElementVisibility(pacsPendingResultNoChanges);
	}

	public List<String> getStateOfSendToPACSMenu(){


		List<String> state = new ArrayList<String>();
		state.add(pacsAcceptedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR));
		state.add(pacsRejectedFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR));
		state.add(pacsPendingFindingSlider.getCssValue(NSGenericConstants.BACKGROUND_COLOR));

		return state;
	}

	public void sendToPacsAndSelectOptionsFromPendingBox(boolean acceptAll, boolean rejectAll,boolean leaveAsIs) throws InterruptedException {


		clickUsingAction(sendToPacs);

		if((acceptAll ||rejectAll ||leaveAsIs)==true)
		{
			waitForPendingFindingDialoglToLoad();
			if(leaveAsIs)
				click(pacsPendingResultNoChanges);
			else if(acceptAll)
				click(pacsPendingResultAcceptAll);
			else if(rejectAll)
				click(pacsPendingResultRejectAll);

			clickUsingAction(continueButton);
		}

		waitForElementsVisibility(notificationDiv);

	}


	//Orthanc response verification/ retrieval methods


	public String verifyOrthancFindings(String patientName, String username, String resultName,int numberOfEntries,
			List<String> acceptedFindingsName,List<String> rejectedFindingsName,List<String> pendingFindingsName , String...textAnnotation) {


		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there is only one patient
		assertEquals(RESTUtil.getJsonPath(response).getList("").size(),1,"","");

		String patient_id = RESTUtil.getJsonPath(response).getList("").get(0).toString();

		//Getting patient details
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id);
		assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG),patientName,"Checkpoint[o.1]","Verifying the patient name");


		//Getting PR Entires
		List<String> seriesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL)).getList("");
		assertEquals(seriesID.size(),numberOfEntries,"Checkpoint[o.2]","Verifying the total PR entries [Pending/accepted/rejected]");

		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));

			assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG),OrthancAndAPIConstants.ORTHANC_PR_VALUE,"Checkpoint[o.3]","Verifying the PR entries name");
			assertThat(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG), Matchers.anyOf(Matchers.is(resultName+"_"+ViewerPageConstants.PENDING_TEXT),
					Matchers.is(resultName+"_"+ViewerPageConstants.ACCEPTED_TEXT),Matchers.is(resultName+"_"+ViewerPageConstants.REJECTED_TEXT)));


		}
		List<String> instancesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL)).getList("");

		verifyFindingName(instancesID,acceptedFindingsName, resultName+"_"+ViewerPageConstants.ACCEPTED_TEXT,username);
		verifyFindingName(instancesID,rejectedFindingsName, resultName+"_"+ViewerPageConstants.REJECTED_TEXT,username);
		verifyFindingName(instancesID,pendingFindingsName, resultName+"_"+ViewerPageConstants.PENDING_TEXT,username,textAnnotation);

		return patient_id;

	}

	public boolean verifyOrthancEntry(String patientName,String patientId, String studyDesc, int numberOfEntries ,String resultName,String modality, boolean acceptedEntry, boolean rejectedEntry, boolean pendingEntry) 
	{
		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		// verifying there is only one patient
		boolean status = RESTUtil.getJsonPath(response).getList("").size()==1;		


		//Getting patient details
		String patientID = RESTUtil.getJsonPath(response).getList("").get(0).toString();
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patientID);		
		status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG).equalsIgnoreCase(patientName);
		LOGGER.info("Verifying the patient name "+status);		
		status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_ID_TAG).equalsIgnoreCase(patientId);
		LOGGER.info("Verifying the patient ID "+status);

		//Getting Study details
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.STUDY_ORTHANC_URL);
		response = RESTUtil.getResponse();		
		String studyID = RESTUtil.getJsonPath(response).getList("").get(0).toString();
		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.STUDY_ORTHANC_URL+"/"+studyID);		
		status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_STUDY_DESCRIPTION_TAG).equalsIgnoreCase(studyDesc.trim());
		LOGGER.info("Verifying the Study Description "+status);

		//Getting Series Entires
		List<String> seriesID = RESTUtil.getJsonPath(RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL)).getList("");
		status = status && seriesID.size()==numberOfEntries;
		LOGGER.info("Verifying the Number of results "+status);

		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));

			status = status && modality.contains(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_MODALITY_TAG));
			LOGGER.info("Verifying the modality "+status);

			if(acceptedEntry) {
				status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).equalsIgnoreCase(resultName+"_"+ViewerPageConstants.ACCEPTED_TEXT);
				LOGGER.info("Verifying the Accepted result  "+status);
			}

			if(pendingEntry) {
				status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).equalsIgnoreCase(resultName+"_"+ViewerPageConstants.PENDING_TEXT);
				LOGGER.info("Verifying the Rejected result "+status);
			}
			if(rejectedEntry) {
				status = status && RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG).equalsIgnoreCase(resultName+"_"+ViewerPageConstants.REJECTED_TEXT);
				LOGGER.info("Verifying the pending result "+status);
			}


		}

		return status;

	}

	public boolean verifyOrthancEntries(String... findings) 
	{

		//Getting Series Entires

		Response response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL);
		List<String> seriesID = RESTUtil.getJsonPath(response).getList("");
		boolean status = seriesID.size()==findings.length;
		LOGGER.info("Verifying the Number of results "+status);


		List<String> entries = new ArrayList<String>();

		for(int i=0;i<seriesID.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesID.get(i));
			entries.add(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG));
		}

		List<String> values  = Arrays.asList(findings);
		Collections.sort(values);
		Collections.sort(entries);

		status = status && values.equals(entries);
		LOGGER.info("Verifying the findings "+status);

		return status;

	}

	public void verifyNoOfPatientsInOrthanc(int expectedCount) {


		//Getting patient id
		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		
		// verifying there is no entry
		assertEquals(RESTUtil.getJsonPath(response).getList("").size(),expectedCount,"Checkpoint[a]","Verifying the orthanc has patient entry "+expectedCount);



	}

	private void verifyFindingName(List<String> instancesID,List<String> findings, String ft,String username,String... textAnnoText) {

		// verifying the findings name 

		Response response = null;
		String findingName="";
		if(findings.size()>0) {

			for(int i=0;i<instancesID.size();i++) {

				List<Object> items = new ArrayList<Object>();
				String findingType = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_INSTANCES_SERIES_DESCRP_TAG).asString();
				if(findingType.trim().equalsIgnoreCase(ft.trim()))
				{
					if(textAnnoText.length>0)
						response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_TEXTANNOTATION_TAG);
					else
						response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_ITEMS_TAG);

					if(!response.toString().isEmpty())
						items = RESTUtil.getJsonPath(response).getList("");

					for(int j=0,k=0,l=0;j<items.size();j++,k++,l++) {

						if(findings.get(k).equalsIgnoreCase(OrthancAndAPIConstants.ORTHANC_TEXT_ANN_VAL)) { 
							findingName = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_TEXTANNOTATION_TAG+"/"+items.get(j)+"/"+OrthancAndAPIConstants.ORTHANC_TEXT_VALUE_TAG).asString();
							assertEquals(findingName.trim(),textAnnoText[l].trim(),"Checkpoint[o.5]","Verifying the finding name");
						}
						else {
							findingName = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_ITEMS_TAG+"/"+items.get(j)+"/"+OrthancAndAPIConstants.ORTHANC_FINDING_NAME_TAG).asString();
							assertEquals(findingName.trim(),findings.get(k).trim(),"Checkpoint[o.5]","Verifying the finding name");
						}
						if(findingName.equalsIgnoreCase(ViewerPageConstants.POLYLINE)) {
							j=j+1;
						}

					}

					response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesID.get(i)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+OrthancAndAPIConstants.ORTHANC_USERNAME_TAG);
					assertEquals(response.asString().trim(),username,"Checkpoint[o.6]","Verifying the user who created the findings");
					break;
				}
			}

		}



	}

	public class ReplaceVal implements UnaryOperator<String>
	{
		@Override
		public String apply(String t) {

			t = t.substring(0 , t.indexOf("_"));
			return t.replaceAll("LinearMeasurement", "POLYLINE").replaceAll("CrossPoint", "POINT").toUpperCase();

		}
	}

	public HashMap<String, Object> getAllInformationAboutInstance(String patientName, String seriesDescription, int whichInstance) {


		List<String> instancesList = getAllInstances(patientName, seriesDescription);
		Response response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesList.get(whichInstance-1)+OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_TAG_URL);
		return GSONParser.createHashMapFromJsonString(response.asString());

	}

	public HashMap<String, Object> getAllInformationAboutInstance(String instanceID) {
		Response response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instanceID+OrthancAndAPIConstants.ORTHANC_SIMPLIFIED_TAG_URL);
		return GSONParser.createHashMapFromJsonString(response.asString());


	}

	public  String getTagInformation(String patientName, String seriesDescription, int whichInstance, String tag) {
		List<String> instancesList = getAllInstances(patientName, seriesDescription);
		Response response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instancesList.get(whichInstance-1)+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+"/"+tag);
		return response.asString();

	}

	public  String getTagInformation(String instanceID, String tag) {
		Response response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.INSTANCES_ORTHANC_URL,instanceID+"/"+OrthancAndAPIConstants.ORTHANC_CONTENT_TAG+tag);
		return response.asString().trim();

	}

	public List<String> getAllInstances(String patientName, String seriesDescription) {


		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		
		List<Object> patientIDs = RESTUtil.getJsonPath(response).getList("");

		String studiesForPatient="";
		for(int i =0 ;i<patientIDs.size();i++) {			
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patientIDs.get(i).toString());				
			String patName = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_PATIENT_NAME_TAG);
			if(patName.equalsIgnoreCase(patientName)) {
				studiesForPatient = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.STUDY_ORTHANC_TAG);
				studiesForPatient = studiesForPatient.replace("[", "").replace("]", "");				
				break;
			}
		}

		response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.STUDY_ORTHANC_URL,studiesForPatient);				
		String series = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.SERIES_ORTHANC_TAG);
		List<String> seriesList = Arrays.asList(series.replace("[", "").replace("]", "").split(","));

		String instances="";
		List<String> instancesList = new ArrayList<String>();

		for(int i =0 ;i<seriesList.size();i++) {
			response = RESTUtil.getResponse(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.SERIES_ORTHANC_URL,seriesList.get(i));
			String seriesDesc = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.ORTHANC_SERIES_DESCRIPTION_TAG);
			if(seriesDesc.equalsIgnoreCase(seriesDescription)) {
				instances = RESTUtil.getResponseValue(response,OrthancAndAPIConstants.INSTANCES_ORTHANC_TAG);
				instancesList  = Arrays.asList(instances.replace("[", "").replace("]", "").split(","));
				break;
			}
		}

		return instancesList;

	}

	public List<String> convertTheResponseInList(String response){

		return Arrays.asList(response.replace("[", "").replace("]", "").split(","));
	}

	public void deleteAllPatients() {

		//Getting patient id

		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		for(int i =0;i<RESTUtil.getJsonPath(response).getList("").size();i++) {
			String patient_id = RESTUtil.getJsonPath(response).getList("").get(i).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}

	}

	public boolean verifyBannerAfterSendToPacs(String message) {

		waitForElementVisibility(errorBanner);
		boolean status=getCssValue((driver.findElement(By.xpath("//div[contains(text(),'"+ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS+"')]/../../preceding-sibling::div[@class='iconDiv']//*[local-name()='g']//*[local-name()='g']"))),NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.ERROR_ICON_COLOR);
		status=status && getText(driver.findElement(By.xpath("//div[contains(text(),'"+ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS+"')]/../..//div[@class='notification-title']"))).equalsIgnoreCase(ViewerPageConstants.ERROR);
		status=status && getText(driver.findElement(By.xpath("//div[contains(text(),'"+ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS+"')]/../..//div[@class='notification-message']"))).equalsIgnoreCase(message);

		return true;	
	}

	public void closeNotificationPopUp(int totalNotifications)
	{
		for(int i=totalNotifications-1;i>=0;i--) 
			click(closeIconOnNotification.get(i));
	}

	public boolean verifyWhenFindingsSentToPACS(int thumbnail,boolean sentOrNotSent) throws InterruptedException
	{
		mouseHover(findingTileContainers.get(thumbnail));
		
		boolean status=getCssValue(sendToPacsIcons.get(thumbnail),NSGenericConstants.FILL).equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
		
		if(sentOrNotSent)
		    status=status && getText(getTooltipWebElement(sendToPacsIcons.get(thumbnail))).equalsIgnoreCase(ViewerPageConstants.SENT_TO_PACS_TEXT);
		else
			status=status && getText(getTooltipWebElement(sendToPacsIcons.get(thumbnail))).equalsIgnoreCase(ViewerPageConstants.NOT_SENT_TO_PACS_TEXT);
        return status;
	}
	

}
