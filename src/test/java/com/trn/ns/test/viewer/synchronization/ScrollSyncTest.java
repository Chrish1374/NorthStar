package com.trn.ns.test.viewer.synchronization;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import jxl.read.biff.BiffException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ScrollSyncTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	//Loading the patient on viewer 

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);	
	
	String filePath2 = Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
		
	String filePath3 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
	String boneagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	
	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	
	private String filePath5 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	private String anonymousPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	
	//private String secondResultDescription_anonymous = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath5);
	private String firstResultDescription_anonymous = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath5);

	private String firstSeriesDescriptionMR_LSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
	private String thirdSeriesDescriptionMR_LSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath);
	
	String filePath6 = Configurations.TEST_PROPERTIES.get("aidoc_CervicalSpine_DE686_filepath");
	String patientName6 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
	
	String filePath7 = Configurations.TEST_PROPERTIES.get("S10671CTSR_filepath");
	String patientName5 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);
	
	private ContentSelector contentSelector;

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"IE11","Chrome","Edge","US289","BVT"})
	public void test01_DE20_TC293_US289_TC2376_verifyScrollUsingMouse() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to scroll the images loaded in image viewer using mouse"
				+ "<br> Verify scroll synchronization with series having different Orientation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		layout=new ViewerLayout(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying forward and backword scroll is working fine in all viewbox" );
		int totalNumberOfViewbox = layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT);
		for (int i = 1; i <= totalNumberOfViewbox; i++) {
			int currentScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox"+i);
			
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");
			
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPosition<forwardScrollPosition, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox"+i);
		}

		//changing layout to 1X2
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying forward and backword scroll is working fine on layout change" );
		int totalNumberOfViewport = layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_TWO_LAYOUT);
		
		for (int i = 1; i <=totalNumberOfViewport-3; i++) {
			int currentScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
			
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");
			
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
		
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");
			
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
		
		}
		
		for (int i = 4; i <totalNumberOfViewport; i++) {
			int currentScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
			
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");
			
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(4),viewerpage.getCurrentScrollPositionOfViewbox(5), "verifying that first 3 viewboxes are in sync", "verified");
		
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");
			
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(2),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(3),viewerpage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(4),viewerpage.getCurrentScrollPositionOfViewbox(5), "verifying that first 3 viewboxes are in sync", "verified");
			
		}
	}

	@Test(groups ={"IE11","Chrome","Edge","US741","US61","US2329","US2325","DR2796","Positive","F1090","E2E"})
	public void test02_US61_TC792_US741_TC2624_US2329_TC10165_US2325_TC10410_DR2796_TC10786_verifyByDefaultScrollSyncOn() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify by default scroll synchronization is on"
				                  + "</br>Verify scroll synchronization with series having same Orientation. <br>"+
				"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane. <br>"+
				                  "[Risk and Impact]: Verify the TC10165 with quick toolbox options. <br>"+
				"[Risk and Impact]: Verify the TC10165.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	

		//variable to store the current slice position
		int originalScrollPositionOfViewbox1 = viewerpage.getCurrentScrollPositionOfViewbox(1);
		
		//Perform forward scroll scroll 
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 10, 10, 0, 100);
		int currentUpScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		
		//Verify view box 2 and 3 are synchronized with viewbox1 as they have same orientation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC13[1] & Checkpoint[1/3]", "Verifying viewbox 2 and 3 are synchronized with viewbox 1 as all of them have same orientation");
		viewerpage.assertNotEquals(currentUpScrollPosition, originalScrollPositionOfViewbox1, "Verifying scroll up is performed in viewbox 1 ", "Verified scroll up is performed in viewbox 1");
		viewerpage.assertEquals(currentUpScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll sync for viewbox 2", "Verfied scroll sync for viewbox 2");
		viewerpage.assertEquals(currentUpScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll sync for viewbox 3", "Verfied scroll sync for viewbox 3");
		
		//Verify view box 4 is not synchronized with viewbox1 as they have different orientation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC13[2] & Checkpoint[2/3]", "Verifying viewbox 4 is not synchronized with viewbox 1 as they have different orientation");
		viewerpage.assertNotEquals(currentUpScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll is not in sync for viewbox 4", "Verfied scroll is not in sync for viewbox 4");

		//Performing backward scroll 
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 0, -2);
		int currentDownScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC13[3] & Checkpoint[3/3]", "verifying scroll down and synchronization");
		viewerpage.assertEquals(currentDownScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll sync for viewbox 1", "Verfied scroll sync for viewbox 1");
		viewerpage.assertEquals(currentDownScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll sync for viewbox 2", "Verfied scroll sync for viewbox 2");
		viewerpage.assertEquals(currentDownScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll sync for viewbox 3", "Verfied scroll sync for viewbox 3");
		viewerpage.assertNotEquals(currentDownScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll is not in sync for viewbox 4", "Verfied scroll is not in sync for viewbox 4");
	}

	@Test(groups ={"IE11","Chrome","Edge","US289","US61","Sanity","BVT"})
	public void test03_US61_TC793_US289_TC2414_verifyScrollSyncOffAfterClickingSpaceBar() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Verify scroll synchronization is off after clicking on space bar"
				+ "<br> Verify Sync ON/OFF for Scroll.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	

		int originalScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.performSyncONorOFF();
		//performing scroll
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 20);
		int currentUpScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC14[2] & Checkpoint[1/2]", "verifying Images from all viewboxes having same frameReferenceUID should NOT be synchronously scroll up");

		viewerpage.assertNotEquals(currentUpScrollPosition, originalScrollPosition, "Verifying scroll up is performed in viewbox 1 ", "Verified scroll up is performed in viewbox 1");
		viewerpage.assertEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2", "Verfied scroll is not in sync for viewbox 2");
		viewerpage.assertEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll is not in sync for viewbox 3", "Verfied scroll is not in sync for viewbox 3");
		viewerpage.assertNotEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll is not in sync for viewbox 4", "Verfied scroll is not in sync for viewbox 4");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC14[4] & Checkpoint[2/2]", "verifying Images from all viewboxes having same frameReferenceUID should NOT be synchronously scroll down");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, -10);
		int currentDownScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentDownScrollPosition, currentUpScrollPosition, "Verifying scroll down is performed in viewbox 1 ", "Verified scroll down is performed in viewbox 1");
		viewerpage.assertEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is not in sync for viewbox 2", "Verfied scroll is not in sync for viewbox 2");
		viewerpage.assertEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll is not in sync for viewbox 3", "Verfied scroll is not in sync for viewbox 3");
		//		viewerpage.assertEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll is not in sync for viewbox 4", "Verfied scroll is not in sync for viewbox 4");
	}

	@Test(groups ={"IE11","Chrome","Edge","US61"})
	public void test04_US61_TC798_verifyScrollSyncWhenFrameRefUIDDifferent() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization behavior when FrameReferenceUID is different");

		//Loading the patient on viewer
		String filePath_AH4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
		String patientNameAH4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath_AH4);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameAH4+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	

		int originalScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		//performing scroll
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, 20);
		int currentUpScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying Images from all viewboxes having different frameReferenceUID should NOT be synchronously scroll up");
		viewerpage.assertNotEquals(currentUpScrollPosition, originalScrollPosition, "Verifying scroll up is performed in viewbox 1 ", "Verified scroll up is performed in viewbox 1");
		
		//modified by santwana for hardening as scroll sync logic is changed
		viewerpage.assertNotEquals(originalScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is in sync for viewbox 2  even when frame ref UID is different", "Verfied scroll is  in sync for viewbox 2 as frame ref UID is different");
		
		//Added by santwana for hardening as scroll sync logic is changed
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is  in sync for viewbox 2 even when frame ref UID is different", "Verfied scroll is in sync for viewbox 2 as frame ref UID is different");


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying Images from all viewboxes having different frameReferenceUID should NOT be synchronously scroll down");
		//performing scroll 
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 0, -10);
		int currentDownScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentDownScrollPosition, currentUpScrollPosition, "Verifying scroll down is performed in viewbox 1 ", "Verified scroll down is performed in viewbox 1");
		
		//modified by santwana for hardening as scroll sync logic is changed
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll is  in sync for viewbox 2 as frame ref UID is different", "Verfied scroll is  in sync for viewbox 2 as frame ref UID is different");
	}

	// Verify that all images should rendered from start to end position while scrolling
	// Verify the order of image rendering while scrolling and no skipping
	@Test(groups ={"IE11","Chrome","Edge"})
	public void test08_US578_TC1739_TC1740_verifyScrollUsingKeypress() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that all images should rendered from start to end position while scrolling </BR> Verify the order of image rendering while scrolling and no skipping");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying scroll is working fine in viewbox" );
		int totalNumberOfImages = viewerpage.getMaxNumberofScrollForViewbox(1);
		for (int i = 1; i <= totalNumberOfImages; i++) {
			int currentScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
			//performing forward scroll 
			viewerpage.scrollDownToSliceUsingKeyboard(1, 1);
			int forwardScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
			if(forwardScrollPosition!=totalNumberOfImages)
				viewerpage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox, While scrolling, each image should rendered from start to stop"+i, "Verified forward scroll is working fine in viewbox"+i);
			else
				viewerpage.assertTrue(totalNumberOfImages==forwardScrollPosition, "Verifying forward scroll in viewbox, While scrolling, each image should rendered from start to stop"+i, "Verified forward scroll is working fine in viewbox"+i);
		}

	}

	//Verify the scrolling of same sync series after selecting through content selector
	@Test(groups ={"IE11","Chrome","Edge"})
	public void test09_US578_TC1766_verifyScrollUsingKeypressForSameSyncSeries() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the scrolling of same sync series after selecting through content selector");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	
		
		contentSelector = new ContentSelector(driver);
		//issue: 2nd viewbox is not in sync 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying  scroll is working fine in viewbox" );

		contentSelector.selectSeriesFromSeriesTab(2,firstSeriesDescriptionMR_LSP);
		int totalNumberOfImages = 0;
		int totalNumberOfImagesInVB1 = viewerpage.getMaxNumberofScrollForViewbox(1);
		int totalNumberOfImagesInVB2 = viewerpage.getMaxNumberofScrollForViewbox(2);
		if(totalNumberOfImagesInVB1<totalNumberOfImagesInVB2) {
			totalNumberOfImages = totalNumberOfImagesInVB1;
		}else {
			totalNumberOfImages = totalNumberOfImagesInVB2;
		}
		for (int i = 1; i <= totalNumberOfImages; i++) {
			int currentScrollPositionVB1 = viewerpage.getCurrentScrollPositionOfViewbox(1);
			int currentScrollPositionVB2 = viewerpage.getCurrentScrollPositionOfViewbox(2);
			//performing forward scroll 
			viewerpage.scrollDownToSliceUsingKeyboard(1, 1);
			int forwardScrollPositionVB1 = viewerpage.getCurrentScrollPositionOfViewbox(1);
			int forwardScrollPositionVB2 = viewerpage.getCurrentScrollPositionOfViewbox(2);
			if(forwardScrollPositionVB1!=totalNumberOfImages)
				viewerpage.assertTrue(currentScrollPositionVB1<forwardScrollPositionVB1 && currentScrollPositionVB2<forwardScrollPositionVB2 && currentScrollPositionVB1==currentScrollPositionVB2, "Verifying forward scroll in viewbox, While scrolling, both images should scroll at same image number"+i, "Verified forward scroll is working fine in viewbox"+i);
			else
				viewerpage.assertTrue(totalNumberOfImages==forwardScrollPositionVB1 || totalNumberOfImages==forwardScrollPositionVB2  , "Verifying forward scroll in viewbox, While scrolling, both images should scroll at same image number"+i, "Verified forward scroll is working fine in viewbox"+i);
		}

	}

	//Verify the scrolling of sync series after selecting through content selector
	@Test(groups ={"IE11","Chrome","Edge"})
	public void test10_US578_TC1767_verifyScrollUsingKeypressForDifferentSyncSeries() throws InterruptedException, AWTException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to scroll the images loaded in image viewer using mouse");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	
		contentSelector = new ContentSelector(driver);
		
		//issue: 2nd viewbox is not in sync 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying  scroll is working fine in viewbox" );

		contentSelector.selectSeriesFromSeriesTab(2,thirdSeriesDescriptionMR_LSP);
		int totalNumberOfImages = 0;
		int totalNumberOfImagesInVB1 = viewerpage.getMaxNumberofScrollForViewbox(1);
		int totalNumberOfImagesInVB2 = viewerpage.getMaxNumberofScrollForViewbox(1);
		if(totalNumberOfImagesInVB1<totalNumberOfImagesInVB2) {
			totalNumberOfImages = totalNumberOfImagesInVB1;
		}else {
			totalNumberOfImages = totalNumberOfImagesInVB2;
		}
		for (int i = 1; i <= totalNumberOfImages; i++) {
			int currentScrollPositionVB1 = viewerpage.getCurrentScrollPositionOfViewbox(1);
			int currentScrollPositionVB2 = viewerpage.getCurrentScrollPositionOfViewbox(2);
			//performing forward scroll 
			viewerpage.scrollDownToSliceUsingKeyboard(1, 1);
			int forwardScrollPositionVB1 = viewerpage.getCurrentScrollPositionOfViewbox(1);
			int forwardScrollPositionVB2 = viewerpage.getCurrentScrollPositionOfViewbox(2);
			if(forwardScrollPositionVB1!=totalNumberOfImages)
				viewerpage.assertTrue(currentScrollPositionVB1<forwardScrollPositionVB1 && currentScrollPositionVB2<forwardScrollPositionVB2 && currentScrollPositionVB1==currentScrollPositionVB2, "Verifying forward scroll in viewbox, While scrolling, both images should scroll at synchronously"+i, "Verified forward scroll is working fine in viewbox"+i);
			else
				viewerpage.assertTrue(totalNumberOfImages==forwardScrollPositionVB1 || totalNumberOfImages==forwardScrollPositionVB2  , "Verifying forward scroll in viewbox, While scrolling, both images should scroll at synchronously"+i, "Verified forward scroll is working fine in viewbox"+i);
		}

	}

	@Test(groups ={"IE11","Chrome","Edge"})
	public void test01_DE356_TC1454_verifyScrollSyncOnStudtyWithRelatedSourceDisplayElement() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Scrolling order is correct for  studies that have source display elements defined as the related series");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName2+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName2, 1);
	
		viewerpage.waitForAllImagesToLoad();
		//Perform manual input of Image number
		viewerpage.scrollToImage(5, 60);

		//Verify Scrolling order is correct for related series by manual Input of image Number
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/3]","Verify scroll sync for related Series by manual Input on Overlay");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(5),60,"Verifying slice number is Viewbox5","The Slice number is Viewbox5 is 60");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(6),123,"Verifying slice number is Viewbox5","The Slice number is Viewbox5 is 123");

		//Verify Scrolling order is correct for related series  by scrolling using mouse wheel
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/3]","Verify scroll sync for related Series by scrolling using Mouse Wheel");		
		viewerpage.mouseWheelScrollInViewer(5, "down", 5);
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(5),65,"Verifying slice number is Viewbox5","The Slice number is Viewbox5 is 65");
		viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(6),128,"Verifying slice number is Viewbox5","The Slice number is Viewbox5 is 128");

		//Verify image in both Viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[3/3]","Verify images in both viewboxes");		
	//	viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(5),"Verify image after scroll on viewbox5","TC1454_Checkpoint3");
		//viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(6),"Verify image after scroll on viewbox6","TC1454_Checkpoint4");

	}	

	@Test(groups ={"IE11","Chrome","Edge"})
	public void test02_DE427_TC1792_verifyImageFor16bitSignedData() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Data display issue / corrupted - 16bit Signed Data Coverage");

		String filePath = Configurations.TEST_PROPERTIES.get("4Quan_LesionDetection_filepath");
		String patientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName4+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName4, 1);
	

		//Perform manual input of image number=44 on viewbox-2
		viewerpage.scrollToImage(2, 44);
		viewerpage.doubleClickOnViewbox(2);

		//Verify 44th image on viewbox-2
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/1]","Verify 44th image in second viewboxe");		
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verify 44th image after scroll on viewbox2","TC1792_Checkpoint1");
	}	

	//Verify scroll synchronization with series having same FrameReferenceUID and same Orientation
	
	@Test(groups ={"IE11","Chrome","Edge","US289","Sanity","BVT"})
	public void test03_US289_TC2374_verifyScrollScrollSameOrientationAndFUID() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9, 1);
		layout=new ViewerLayout(driver);
	
		
		//issue: 2nd viewbox is not in sync 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying forward and backword scroll is working fine in all viewbox" );
		
		for (int i = 1; i <= 2; i++) {
			int currentScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox"+i);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPosition<forwardScrollPosition, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox"+i);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			viewerpage.scrollDownToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse up", "verified");
			
			viewerpage.scrollUpToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse down", "verified");
			
			viewerpage.mouseWheelScrollInViewer(i, "down", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel down", "verified");
			
			viewerpage.mouseWheelScrollInViewer(i, "up", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel up", "verified");
		
			
		}

		//changing layout to 1X2
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "verifying forward and backword scroll is working fine on layout change" );
		int totalNumberOfViewport = layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT);
		for (int i = 1; i <= totalNumberOfViewport; i++) {
			int currentScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPositionAfterLayoutChange = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPositionAfterLayoutChange<forwardScrollPositionAfterLayoutChange, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox "+i+"when layout changed to"+ViewerPageConstants.ONE_BY_TWO_LAYOUT);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			viewerpage.scrollDownToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse up", "verified");
			
			viewerpage.scrollUpToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse down", "verified");
	
			viewerpage.mouseWheelScrollInViewer(i, "down", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel down", "verified");
			
			viewerpage.mouseWheelScrollInViewer(i, "up", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel up", "verified");
	
			
		}
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US289"})
	public void test04_US289_TC2375_verifyScrollScrollSameOrientationAndDiffererntFUID() throws  InterruptedException, SAXException, IOException, ParserConfigurationException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having different FrameReferenceUID and same Orientation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4_patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ah4_patientName, 1);
	
		
		//issue: 2nd viewbox is not in sync 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying forward and backword scroll is working fine in all viewbox" );
		
		for (int i = 1; i <= 2; i++) {
			int currentScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox"+i);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			//performing backword scroll 
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(i);
			viewerpage.assertTrue(backwordScrollPosition<forwardScrollPosition, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox"+i);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
			viewerpage.scrollDownToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse up", "verified");
			
			viewerpage.scrollUpToSliceUsingKeyboard(1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse down", "verified");
			
			viewerpage.mouseWheelScrollInViewer(i, "down", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel down", "verified");
			
			viewerpage.mouseWheelScrollInViewer(i, "up", 1);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced using mouse wheel up", "verified");
		
			
		}

	}

	@Test(groups ={"IE11","Chrome","Edge","US409"})
	public void test05_US409_TC2355_verifyImageSorting() throws InterruptedException,  SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Tech Debt: Remove image sorting from LayoutManager- Results of PDF,JPEG,PNG, files");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneagePatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(anonymousPatientName, 1);
	
		contentSelector = new ContentSelector(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify user is navigating to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user is navigating to viewer page", "User is navigated to viewer page");

		viewerpage.scrollUpToSliceUsingKeyboard(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reset the image to 1" );
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should scroll synchronously.","test17_checkpointa0");
		
		
		for(int i = 1 ;i<=11;i++) {
			viewerpage.scrollDownToSliceUsingKeyboard(1);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the scroll one by one on Non-DICOM" );
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should scroll synchronously.","test17_checkpoint_a"+i);
			
		}
		
		contentSelector.selectResultFromSeriesTab(1, firstResultDescription_anonymous);		
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should scroll synchronously.","test17_checkpointb0");
		
//		viewerpage.scrollUpToSliceUsingKeyboard(5);
				
		for(int i = 1 ;i<=11;i++) {
			viewerpage.scrollUpToSliceUsingKeyboard(1);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Performing the scroll one by one on Non-DICOM" );
			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should scroll synchronously.","test17_checkpoint_b"+i);
		
		}
	
	}
			
	@Test(groups ={"IE11","Chrome","Edge"})
	public void test14_DE686_TC2395_verifySliceOrder() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Sort order is incorrect for series with more than 100 images");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName5+" in viewer" );

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerPageUsingSearch(patientName5, 1, 2);
		
		// Verify images at slice position = 3,25,52,75,104 on second viewbox for patient = S10671CTSR_filepath
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/2]","Verify images at slice position [3,25,52,75,104] in second viewbox");		
		viewerpage.doubleClickOnViewbox(2);
		int[] imageNumber = new int[] {3,25,52,75,104};
		for (int i=0;i<imageNumber.length;i++){
			viewerpage.scrollToImage(2, imageNumber[i]);
		}
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName6+" in viewer" );
		helper.browserBackAndReloadViewer(patientName6, 1, 1);
		
		// Verify images at slice position = 3,25,52,75,104,125,155 on second viewbox for patient = aidoc_CervicalSpine_filepath
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/2]","Verify images at slice position [3,25,52,75,104,125,155] in second viewbox");		
		viewerpage.doubleClickOnViewbox(1);
		int[] imageNumber1 = new int[] {3,25,52,75,104,125,155};
		for (int i=0;i<imageNumber1.length;i++){
			viewerpage.scrollToImage(1, imageNumber1[i]);
			viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify " + imageNumber1[i] + "th image of viewbox1","TC2395_Checkpoint2_"+imageNumber1[i]);			
		}
	}	

	@Test(groups ={"IE11","Chrome","Edge"})
	public void test15_DE686_TC2398_verifySliceOrderOnImageNumberSwapped() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Sort order is incorrect for series with more than 100 images- Swap image numbers in DB");

		DatabaseMethods DB =new DatabaseMethods(driver);

		//Swap 4th and  10th image
		DB.updateImageNumber("10",DB.getAllImagesAndInstanceUIDOfPatient(patientName,2).get("4"));
		DB.updateImageNumber("4",DB.getAllImagesAndInstanceUIDOfPatient(patientName,2).get("10"));

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	
		viewerpage.scrollToImage(1, 4);
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[1/2]","Verify 4th image of viewbox1 getting swapped with 10th image");		
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify 4th image of viewbox1 getting swapped with 10th image","DE686_TC2398_Checkpoint1");			

		viewerpage.scrollToImage(1, 10);
		ExtentManager.customExtentReportLog(Configurations.INFO,extentTest, "Checkpoint[2/2]","Verify 10th image of viewbox1 getting swapped with 4th image");		
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify 10th image of viewbox1 getting swapped with 4th image","DE686_TC2398_Checkpoint2");		

		//Revert the changes - swap 10th and 4th image
		DB.updateImageNumber("4",DB.getAllImagesAndInstanceUIDOfPatient(patientName,2).get("10"));
		DB.updateImageNumber("10",DB.getAllImagesAndInstanceUIDOfPatient(patientName,2).get("4"));

		helper.browserBackAndReloadViewer(patientName, 1, 2);
		
		viewerpage.scrollToImage(1, 4);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify original fourth image is rendered in viewbox1 after reverting database changes","DE686_TC2398_Checkpoint3");		

		viewerpage.scrollToImage(1, 10);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify original tenth image is rendered in viewbox1 after reverting database changes","DE686_TC2398_Checkpoint4");		
	}
	
	
	@Test(groups ={"Chrome","Edge","IE11"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test16_DE658_verifyViewBoxSync"})
	public void test06_DE658_verifyViewBoxSync(String patientName) throws InterruptedException, BiffException, IOException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all sync viewboxes display same slice when scroll is stopped");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "verifying all four viewboxes are in sync" );
		// Get current scroll position of viewbox 1 - i.e index 0
		int currentScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
		//Verify slice number are in sync
		viewerpage.assertEquals(currentScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll sync for viewbox 1", "Verfied scroll sync for viewbox 1");
		viewerpage.assertEquals(currentScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll sync for viewbox 2", "Verfied scroll sync for viewbox 2");
		viewerpage.assertEquals(currentScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll sync for viewbox 3", "Verfied scroll sync for viewbox 3");
		viewerpage.assertEquals(currentScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll sync for viewbox 4", "Verfied scroll sync for viewbox 4");
 		//Performing forward scroll in loop and verifying all viewboxes are in sync
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "verifying all four viewboxes are in sync during forward scrolling" );
		int forwardScrollPosition = 0;
		for ( int i=1; i<=3; i++){
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 0, 10);
			forwardScrollPosition = viewerpage.getCurrentScrollPositionOfViewbox(1);
			//Verify sync after forward scroll
			viewerpage.assertEquals(forwardScrollPosition, viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying scroll sync for viewbox 1", "Verfied scroll sync for viewbox 1");
			viewerpage.assertEquals(forwardScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(2), "Verifying scroll sync for viewbox 2", "Verfied scroll sync for viewbox 2");
			viewerpage.assertEquals(forwardScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(3), "Verifying scroll sync for viewbox 3", "Verfied scroll sync for viewbox 3");
			viewerpage.assertEquals(forwardScrollPosition+1, viewerpage.getCurrentScrollPositionOfViewbox(4), "Verifying scroll sync for viewbox 4", "Verfied scroll sync for viewbox 4");
		}
	}
}

