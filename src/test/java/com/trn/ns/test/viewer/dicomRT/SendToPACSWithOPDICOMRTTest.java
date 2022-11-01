package com.trn.ns.test.viewer.dicomRT;

import java.awt.AWTException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SendToPACSWithOPDICOMRTTest extends TestBase{

	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private HelperClass helper;


	//patient detail with multiple series data
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password =  Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	private ViewerSendToPACS sd;
	private OutputPanel panel;
	private DICOMRT rt;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
	}

	@Test(groups ={"Chrome","Edge","IE11","DE1949","Positive","BVT"})	
	public void test01_DE1949_TC7821_verifySendToPACSWhenFindingStateChangedARBar() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that finding is being sent to Pacs when user changes its state from Accepted to pending by AR tool bar."
				+ "<br> [Risk and Impact] :Verify that clone copy is created and loaded in viewer after the first accept or reject.");

		patientPage = new PatientListPage(driver);
		
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loaded the patient data in the viewer. "+patientNameTCGA);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the results from content selector");
		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Enabling send to pacs to send pending findings");
		sd = new  ViewerSendToPACS(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the findings sent to pacs");
		sd.openSendToPACSMenu(true,true, false, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		
		sd.waitForElementInVisibility(sd.notificationDiv);
		cs.assertEquals(cs.getAllResults().size(), result.size()+1, "Checkpoint[1/7]", "verifying the clone is created in content selector");
	
		int totalLegends = rt.legendOptionsList.size();
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyAcceptedRTSegment(i),"Checkpoint[2/7]","Verifying all the legend's state is changed to accepted");
			
		}
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Marking the one legend as pending using AR toolbar");
		rt.navigateToFirstContourOfSegmentation(1);
		rt.selectAcceptfromGSPSRadialMenu();
		
		panel = new OutputPanel(driver);
		
		sd.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), totalLegends-1, "Checkpoint[3/7]", "Verified finding count in accepted panel should be 1 less present in legend");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[4/7]", "Verified finding in pending panel should be 1 as marked one segment as pending");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the findings sent to pacs");
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
	
		totalLegends = rt.legendOptionsList.size();
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyAcceptedRTSegment(i),"Checkpoint[5/7]","Verifying all the legend's state is changed to accepted");
			
		}
		sd.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), totalLegends, "Checkpoint[6/7]", "Verified finding count in accepted panel should be same as legend");

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[7/7]", "No finding should be displayed as pending");
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1949","Positive"})	
	public void test02_DE1949_TC7822_verifySendToPACSWhenFindingStateChangedARBar() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that finding is being sent to Pacs when user changes its state from Accepted to pending by P keyboard shortuct button."
				+ "<br> [Risk and Impact] :Verify that clone copy is created and loaded in viewer after the first accept or reject.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loaded the patient data in the viewer. "+patientNameTCGA);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the results from content selector");
		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the findings sent to pacs");
		sd = new  ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, false, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		sd.waitForElementInVisibility(sd.notificationDiv);
		
		cs.assertEquals(cs.getAllResults().size(), result.size()+1, "Checkpoint[1/7]", "verifying the clone is created in content selector");
		
		int totalLegends = rt.legendOptionsList.size();
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyAcceptedRTSegment(i),"Checkpoint[2/7]","Verifying all the legend's state is changed to accepted");
			
		}
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Marking the one legend as pending using AR toolbar");
		rt.navigateToFirstContourOfSegmentation(1);
		rt.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		
		panel = new OutputPanel(driver);
		sd.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), totalLegends-1, "Checkpoint[3/7]", "Verified finding count in accepted panel should be 1 less present in legend");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[4/7]", "Verified finding in pending panel should be 1 as marked one segment as pending");
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the findings sent to pacs");
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		
		totalLegends = rt.legendOptionsList.size();
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyAcceptedRTSegment(i),"Checkpoint[5/7]","Verifying all the legend's state is changed to accepted");
			
		}
		
		sd.waitForElementInVisibility(sd.notificationDiv);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), totalLegends, "Checkpoint[6/7]", "Verified finding count in accepted panel should be same as legend");
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[7/7]", "No finding should be displayed as pending");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1949","Positive"})	
	public void test03_DE1949_TC7925_verifyClosingOfPendingBoxWhenClickOutside() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that user is able to close the pending result dialogue by clicking outside the pop up box as well.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Load the Patient data in the viewer. "+patientNameTCGA);
		sd = new  ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, false, true);
		sd.click(sd.sendToPacs);
		sd.waitForPendingFindingDialoglToLoad();
		
		rt.click(rt.getViewPort(1));
		rt.assertFalse(rt.isElementPresent(sd.pacsPendingResultAcceptAll), "Checkpoint[1/1]", "Verifying that on pending dialog is closed when user click outside");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","DE1973","Negative"})	
	public void test04_DE1973_TC8004_verifyOriginalResultLoadPostSendtoPACS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[RT specific]: Verify that user is able to load the machine result in the same session without navigating back.");

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT rt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loaded the patient data in the viewer. "+patientNameTCGA);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Retriving the results from content selector");
		cs = new ContentSelector(driver);
		List<String> result = cs.getAllResults();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Enabling send to pacs to send pending findings");
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Accepting all the findings sent to pacs");
		sd = new  ViewerSendToPACS(driver);
		sd.openSendToPACSMenu(true,true, false, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(true,false,false);
		sd.waitForElementInVisibility(sd.notificationDiv);
		
		cs.assertEquals(cs.getAllResults().size(), result.size()+1, "Checkpoint[1/3]", "verifying the clone is created in content selector");
		
		int totalLegends = rt.legendOptionsList.size();
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyAcceptedRTSegment(i),"Checkpoint[2/3]","Verifying all the legend's state is changed to accepted");
			
		}
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Open content selector and click on the machine result 'Microsoft Corp. Radiomics AIC RT Structure Set Created by tcga ENT'  to render original RT result into viewbox.");
		cs.selectResultFromSeriesTab(1, result.get(0));
		
		for(int i =1;i<=totalLegends;i++) {
			rt.assertTrue(rt.verifyPendingRTSegment(i),"Checkpoint[3/3]","'Microsoft Corp. Radiomics AIC RT Structure Set Created by tcga ENT' machine result should be loaded in the viewbox.");
			
		}
	
	}

	
} 
























