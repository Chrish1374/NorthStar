package com.trn.ns.test.viewer.synchronization;

import static com.trn.ns.test.configs.Configurations.PASS;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MultiphaseDisplayForSingleSeriesTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;	
	private ViewerPage viewerPage;
	DatabaseMethods db= new DatabaseMethods(driver);
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector contentSelector;
	private PointAnnotation point ;
	private ViewerSliderAndFindingMenu findingMenu;
	private ViewerLayout layout;
    private ViewerOrientation orientation;

	// Get Patient Name
	String AH4_FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_FilePath);
	String Series1= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, AH4_FilePath);

	String ADCphilips_FilePath = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ADCphilips_FilePath);
	String SeriesToSelect_ADC= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, ADCphilips_FilePath);

	String CTPETMultiphase_FilePath = Configurations.TEST_PROPERTIES.get("CT_PET_Multiphase_filepath");
	String CT_PET_Multiphase_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, CTPETMultiphase_FilePath);

	String CT_Neck_FilePath = Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
	String CT_Neck_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, CT_Neck_FilePath);


	String MR_LSP_filePath = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
	String MR_LSP_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, MR_LSP_filePath);

	String AIChange_MS_filePath = Configurations.TEST_PROPERTIES.get("AI_Change_MS");
	String AIChange_MS_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AIChange_MS_filePath);
	String AIChange_MS_Result1= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, AIChange_MS_filePath);
	String AIChange_MS_Result2= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, AIChange_MS_filePath);
	private HelperClass helper;



	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));

	}


	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive","Sanity" ,"BVT"})
	public void test01_US952_TC4213_VerifyLoadingOfMultiphaseDataInViewer()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify loading of Multiphase Data in viewer");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
		

		//verify console error 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify viewer page loaded successfully for Multiphase data");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(),"Verify console error present while loading multiphase data","Console error not present and multiphase data loaded successfully on viewer");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952","DE1874","Positive","Sanity","BVT"})
	public void test02_US952_TC4215_DE1874_TC7503_VerifyForwardNavigationOverPhases()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user able to navigate forward over phases. <br>"
				+"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		int DefaultPhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//Click on dot(.) from keyboard to change phase number
		viewerPage.pressKey(NSGenericConstants.DOT_KEY);	
		viewerPage.waitForAllImagesToLoad();
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify user can navigate forward over phases");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify user navigate forward on phases", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952","DE1874", "Positive","Sanity" ,"BVT"})
	public void test03_US952_TC4216_DE1874_TC7503_VerifyBackwardNavigationOverPhases()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user able to navigate backward over phases <br>"
				+"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");
		//input phase number
		viewerPage.scrollToPhase(1, 3);
		int DefaultPhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//Click on comma(,) to navigate backward
		viewerPage.pressKey(NSGenericConstants.COMMA_KEY);
		viewerPage.waitForAllImagesToLoad();
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify user can navigate backward for  phases");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify user navigate backward on phases", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test04_US952_TC4225_VerifyLocationAndFormatOfPhaseOverlay()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Location and Format of Phase overlay for Multiphase data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify viewer page loaded successfully for Multiphase data");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify format for Phase overlay");

		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1), 1, "Verify Current phase position for Multiphase Data", "Verified current position of" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+viewerPage.getValueOfCurrentPhase(1));
		viewerPage.assertEquals(viewerPage.getValueOfMaxPhase(1), 10, "Verify Max phase position for Multiphase Data", "Verified Max position of" +" "+ CT_PET_Multiphase_Patient +" "+ "is:" +viewerPage.getValueOfMaxPhase(1));


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test05_US952_TC4226_VerifyPhaseChangeFunctionality()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to change the phases by entering number from the keyboard.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify viewer page loaded successfully for Multiphase data");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//Enter phase number
		int DefaultPhasePosition = viewerPage.getValueOfCurrentPhase(1);

		//change phase number on first viewbox
		viewerPage.scrollToPhase(1, 4);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify user can enter phase data");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify Default phase position not display on viewer", "Updated phase position of"+" "+CT_PET_Multiphase_Patient+" "+"is:"+" "+ChangePhasePosition);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive","BVT" })
	public void test06_US952_TC4227_VerifySliceChangeFunctionalityForPhaseNumber()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is able to change the phases by entering number from the keyboard.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//Enter phase number
		int DefaultPhasePosition =viewerPage.getValueOfCurrentPhase(1);

		//change phase number on first viewbox
		viewerPage.scrollToPhase(1, 4);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify user can change phase data");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify Default phase position not display on viewer", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);

		//change slice or image number
		String DefaultSlicePosition=viewerPage.getCurrentScrollPosition(1);
		viewerPage.scrollToImage(1, 35);
		String ChangeSlicePosition=viewerPage.getCurrentScrollPosition(1);

		//verify change image number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify user can change the slices by scrolling for Phase number");
		viewerPage.assertNotEquals(ChangeSlicePosition,DefaultSlicePosition, "Verify slice position for changed phase number", "Verified updated slice position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangeSlicePosition);
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Negative" })
	public void test07_US952_TC4228_VerifyLocationAndFormatOfPhaseForSinglePhaseData()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Location and Format of Phase for Single Phase Data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
	

		//verify Phase overlay not display for single phase data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify location and fromat for single phase data");
		viewerPage.assertFalse(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is not present on viewbox-1 for single series of"+" "+ ADC_philips_Patient +" "+"patient");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Negative","BVT" })
	public void test08_US952_TC4229_VerifyForwardNavigationForPhasesAfterChangingSliceNumber()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user able to navigate forward on phases on clicking on the period(.) and that slice position should remain fixed.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//Default phase number
		int DefaultPhasePosition = viewerPage.getValueOfCurrentPhase(1);

		//change slice or image number
		String DefaultSlicePosition=viewerPage.getCurrentScrollPosition(1);
		viewerPage.scrollToImage(1, 35);
		String ChangeSlicePosition=viewerPage.getCurrentScrollPosition(1);

		//verify change Slice/Image number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify user can change the slices postion");
		viewerPage.assertNotEquals(ChangeSlicePosition,DefaultSlicePosition, "Verify change slice position for patient ", "Verified updated slice position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangeSlicePosition);

		//Click on dot(.) from keyboard to change phase number
		viewerPage.pressKey(NSGenericConstants.DOT_KEY);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);
		String CurrentSlicePosition=viewerPage.getCurrentScrollPosition(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify user can navigate forward for  phases and slice position remain as it is");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify user navigate forward on phases", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);
		viewerPage.assertEquals(CurrentSlicePosition,ChangeSlicePosition, "Verify user navigate forward on phases", "Verified slice postion for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+CurrentSlicePosition);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test09_US952_TC4230_VerifyBackwardNavigationForPhasesAfterChangingSliceNumber()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user able to navigate backward on phases on clicking on the period(.) and that slice position should remain fixed.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//change slice or image number
		String DefaultSlicePosition=viewerPage.getCurrentScrollPosition(1);
		viewerPage.scrollToImage(1, 35);
		String ChangeSlicePosition=viewerPage.getCurrentScrollPosition(1);

		//verify change Slice/Image number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify user can change the slices postion");
		viewerPage.assertNotEquals(ChangeSlicePosition,DefaultSlicePosition, "Verify change slice position for patient ", "Verified updated slice position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangeSlicePosition);

		//Default Phase number
		viewerPage.scrollToPhase(1, 3);
		int DefaultPhasePosition = viewerPage.getValueOfCurrentPhase(1);
		//navigate backward using comma
		viewerPage.pressKey(NSGenericConstants.COMMA_KEY);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify user can navigate backward for Phases");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify user navigate backward on phases", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);


	}

	//DE1056: Unable to Reject findings for NorthStar^CT^Neck patient.
	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Negative" })
	public void test10_US952_DE1056_TC4232_VerifyGSPSNavigationFunctionality()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS navigation functionality for"+" "+ CT_Neck_Patient);

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_Neck_Patient, 1);
	
		point=new PointAnnotation(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/5]","Verify GSPS toolbar display on viewer");
		viewerPage.assertTrue(findingMenu.verifyPendingGSPSToolbarMenu(), "Verify GSPS toolbar for viewbox1.", "Verified");

		//click on gsps next 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/5]","Verify next annotaion is highlighted after click on GSPS next arrow");
		findingMenu.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 3),"Verifying Point 2 is highlighted when user click on next", "GSPS point is highlighted");

		//click on gsps previous
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/5]","Verify previous annotaion is highlighted after click on GSPS previous arrow");
		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 3),"Verifying Point 1 is highlighted when user click on next", "GSPS point is highlighted");

		//click on gsps accept
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/5]","Verify selected annotaion is seen in green color after click on GSPS accept");
		findingMenu.selectAcceptfromGSPSRadialMenu();
		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 3),"Verifying Point 1 is active accepted GSPS", "GSPS point is accepted");

		//click on gsps reject
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/5]","Verify selected annotaion is seen in red color after click on GSPS reject");
		findingMenu.selectRejectfromGSPSRadialMenu();
		findingMenu.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyRejectGSPSRadialMenu(), "Verify color of Reject icon", "The color of Reject icon changes to Red");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 3),"Verifying Point 1 is active rejected GSPS", "GSPS point is rejected");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test11_US952_TC4238_VerifySlicePositionWhileChangingPhaseNumber() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify slice number reamin at its position when changing the phase number only.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//change slice or image number
		viewerPage.scrollToImage(1, 35);
		String DefaultSlicePosition=viewerPage.getCurrentScrollPosition(1);

		//Default Phase number
		int DefaultPhasePosition = viewerPage.getValueOfCurrentPhase(1);
		//navigate backward using comma
		viewerPage.pressKey(NSGenericConstants.DOT_KEY);
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(1);

		//verify change phase number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify user can navigate backward for Phases");
		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Verify user navigate backward on phases", "Verified updated phase position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangePhasePosition);

		//verify change Slice/Image number for first viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify slice number remain as it is after change in phase number");
		String ChangeSlicePosition=viewerPage.getCurrentScrollPosition(1);
		viewerPage.assertEquals(ChangeSlicePosition,DefaultSlicePosition, "Verify slice position for patient ", "Verified that slice position for" +" "+ CT_PET_Multiphase_Patient +" "+ "is:"+" "+ChangeSlicePosition);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Negative" })
	public void test12_US952_TC4246_VerifyScrollSynchronizationFor2D()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify default synchronization for 2D patient"+" "+ CT_Neck_Patient);

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(MR_LSP_Patient, 1);	
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "verifying forward and backword scroll is working fine in all viewbox" );
		int totalNumberOfViewbox = layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT);
		for (int i = 1; i <= totalNumberOfViewbox; i++) {
			int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(i);
			//performing forward scroll 
			viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(i), 0, 0, 0, 20);
			int forwardScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(i);
			viewerPage.assertTrue(currentScrollPosition<forwardScrollPosition, "Verifying forward scroll in viewbox"+i, "Verified forward scroll is working fine in viewbox"+i);

			viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),viewerPage.getCurrentScrollPositionOfViewbox(2), "verifying that first 3 viewboxes are in sync", "verified");
			viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),viewerPage.getCurrentScrollPositionOfViewbox(3), "verifying that first 3 viewboxes are in sync", "verified");

			viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),viewerPage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),viewerPage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");
			viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(3),viewerPage.getCurrentScrollPositionOfViewbox(4) , "verifying that first 3 viewboxes are in sync", "verified");

			//performing backword scroll 
			viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(i), 0, 0, 0, -10);
			int backwordScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(i);
			viewerPage.assertTrue(backwordScrollPosition<forwardScrollPosition, "Verifying backword scroll in viewbox"+i, "Verified backword scroll is working fine in viewbox"+i);
		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test13_US952_TC4247_VerifyOrientationOverlayFunctionalityOnMultiphaseData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify loading of Multiphase Data in viewer");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
		orientation=new ViewerOrientation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		//Hover the mouse over Left orientation marker
		viewerPage.mouseHover(orientation.getRightOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[2/4]:Verify that the rotation arrows display symmetrically when hovering over the left orientation marker.", "test22_Checkpoint2_LeftOrientationMarker_" + CT_PET_Multiphase_Patient + "_1x2");			

		//Hover the mouse over Right orientation marker
		viewerPage.mouseHover(orientation.getLeftOrientationMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[2/4]: Verify that the rotation arrows display symmetrically when hovering over the right orientation marker.", "test22_Checkpoint2_RightOrientationMarker_" + CT_PET_Multiphase_Patient + "_1x2");

		//Click Left orientation marker
		orientation.rotateSeries(orientation.getRightOrientationMarker(1), orientation.rightClockwiseRotMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[3/4]: Verify Left Orientation Marker: Image rotated 90 degrees clockwise.", "test22_Checkpoint3_left_90_degrees_rotation_" + CT_PET_Multiphase_Patient + "_1x2");

		orientation.rotateSeries(orientation.getRightOrientationMarker(1), orientation.rightCounterClockwiseRotMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[3/4]: Verify Left Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "test22_Checkpoint3_left_rotation_canceled_" + CT_PET_Multiphase_Patient + "_1x2");

		//Click Right orientation marker
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftClockwiseRotMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[4/4]: Verify Right Orientation Marker: Image rotated 90 degrees clockwise.", "test22_Checkpoint4_right_90_degrees_rotation_" + CT_PET_Multiphase_Patient + "_2x3");
		orientation.rotateSeries(orientation.getLeftOrientationMarker(1), orientation.leftCounterClockwiseRotMarker(1));
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Checkpoint[4/4]: Verify Right Orientation Marker: Image rotated 90 degrees counterclockwise. Returned to default", "test22_Checkpoint4_right_rotation_canceled_" + CT_PET_Multiphase_Patient + "_2x3");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test14_US952_TC4248_VerifyOneUpAndOneDownFunctionality()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify one up and one down functionality for Multiphase Data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		int DefaultLayout=viewerPage.getNumberOfCanvasForLayout();

		//verifying layout is 1x1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify layout is 1x1");
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),1, "Checkpoint[2/3]:Verify layout is 1x1", "verifying layout is 1x1");

		//verifying layout is 1x2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify layout is 1x2");
		viewerPage.doubleClickOnViewbox(1);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),DefaultLayout, "Checkpoint[3/3]:Verify layout is 1x2", "verifying layout is 1x2");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test15_US952_TC4248_VerifyLayoutChangeFunctionality()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify layout change functionality on multiphase 4D data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
		layout=new ViewerLayout(driver);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		int DefaultLayout=viewerPage.getNumberOfCanvasForLayout();

		//verifying layout is 1x2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify Default layout display on viewer");
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),DefaultLayout, "Checkpoint[2/3]:Verify layout is 1x2", "Verifying layout 1x2 is display");

		//verifying layout is 2x3
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify layout change as per the selected layout");
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(),6, "Checkpoint[3/3]:Verify layout is 2x3", "verifying layout 2x3 is display");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test16_US952_TC4261_VerifyTotalNumberOfImagesCount()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify total number of images count in view box for 4D data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify Phase is present on viewbox");
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1),"Verify phase is present on viewbox-1","Phase is present on viewbox-1 for multiseries Data");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify Total number of slices count and format on viewbox");
		int NumberofSlices=viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.assertEquals(NumberofSlices, 29, "Verify total number of slice count as well as format.", "Verified total number of slice count along with format display on viewbox as "+" "+NumberofSlices);


		//Increase number of slice count using keyboard shortcut
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify total number of images count in () braces after using keyboard shortcut ");
		viewerPage.scrollUpToSliceUsingKeyboard(1, 3);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify slice count after keyboard shortcut", "test25_Checkpoint3_SliceCountAfterKeyboardShortcut_"+ CT_PET_Multiphase_Patient);

		//Increase number of slice count after scrolling mouse wheel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/3]","Verify total number of images count in () braces after scrolling mouse wheel ");
		viewerPage.mouseWheelScrollInViewer(1, "up", 2);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify slice count after scrolling mouse wheel", "test25_Checkpoint4_SliceCountAfterScrollingMouseWheel_"+ CT_PET_Multiphase_Patient);

		//Increase number of slice count by pressing left mouse button and dragging the mouse 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/3]","Verify total number of images count in () braces after drag and release on viewer");
		viewerPage.dragAndReleaseOnViewerWithClick(10, 50, 20, 100);
		viewerPage.compareElementImage(protocolName, viewerPage.getViewbox(1), "Verify slice count after drag and release on viewer", "test25_Checkpoint5_SliceCountByDragAndReleaseOnViewer_"+ CT_PET_Multiphase_Patient);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test17_US963_TC4293_VerifySliceAndPhaseScrolling()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the system is detecting the multiphase data and scroll works accrodingly.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
	

		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(3);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(3);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(3);

//		viewerPage.assertTrue(viewerPage.isConsoleErrorPresent(), "verifying that there is no console error", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","mouse left key pressed and perform the Mouse drag upward and downward Verify that system is detecting that data is 4D data and scrolling works for slices only.");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(3), 0, 0, 0, 100);

		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentScrollPosition, "slice scrolls", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(3), currentImageNumber, "total slices are getting scrolled", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(3), currentPhaseNumber, "no phase scrolling", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","in 3rd view box keep the mouse left key pressed and perform the Mouse drag right and left Verify that system is detecting that data is 4D data and scrolling works for phases only");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(3), 0, 0, 150, 0);

		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentScrollPosition, "slice scrolls", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(3), currentImageNumber, "total slices are getting scrolled", "verified");
		viewerPage.assertNotEquals(viewerPage.getValueOfCurrentPhase(3), currentPhaseNumber, "verifying phase scrolling", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","In 1st view box keep the mouse left key pressed and perform the Mouse drag upward and downward and right and left and verify that system is detecting that data is 2D and scrolling works for slices for all upward ,downward right and left direction");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 0, 100);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "scrolling upward - 2D", "verified");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 30, 50);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "scrolling diagonal - 2D", "verified");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 0, -100);
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "scrolling downward - 2D", "verified");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test18_US963_TC4297_VerifyPhaseScrolling()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Phase number change using mouse scrolling");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Keep mouse  Left key pressed and scroll right side");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 150, 0);

		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "No slice change when phase is scrolled", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices number is getting changed", "verified");
		viewerPage.assertTrue(viewerPage.getValueOfCurrentPhase(1) > currentPhaseNumber, "phase is getting scrolled - right side", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Keep mouse  Left key pressed and scroll left side.");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -150, 0);

		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "No slice change when phase is scrolled", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices number is getting changed", "verified");
		viewerPage.assertTrue(viewerPage.getValueOfCurrentPhase(1) < currentPhaseNumber, "phase is getting scrolled - left side", "verified");




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test19_US963_TC4298_VerifySliceScrolling()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the slice number change using mouse scrolling");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Keep mouse  Left key pressed and scroll upward side.");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 0,150);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1)>currentScrollPosition, "slices are getting scrolled - upward and slices count is getting increased", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices are getting scrolled", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "No phase scrolling", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Keep mouse  Left key pressed and scroll downward side.");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 0,-150);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1) < currentScrollPosition, "slices are getting scrolled - downward and slices count is getting decreased", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices are getting scrolled", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "No phase scrolling", "verified");




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test20_US963_TC4299_VerifySliceScrollingUsingMouseWheel()throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the mouse wheel default scrolling");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Scroll the mouse wheel upward.");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.performMouseWheelUp(10);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1)<currentScrollPosition, "mouse wheel upaward - scrolls slice", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices scrolls", "verified");
    	viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "No phase scroll", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Scroll the mouse wheel downward.");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		viewerPage.performMouseWheelDown(3);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1) > currentScrollPosition, "mouse wheel downward - scrolls slice", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slices scrolls", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "No phase scroll", "verified");




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test21_US963_TC4301_VerifyScrollingWhenYGreaterThanX()throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the scrolling functionlaity when direction Y> X");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Keep mouse left button pressed and drag the mouse upwards in Y direction in such a way that Y direction is > X direction.");

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 10,100);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1)>currentScrollPosition, "when y is greater than x  slice scrolls - upward", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slice changes", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "no phase scroll", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Keep mouse left button pressed and drag the mouse downwards in Y direction in such a way that Y direction is > X direction.");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -10,-100);

		viewerPage.assertTrue(viewerPage.getCurrentScrollPositionOfViewbox(1) < currentScrollPosition, "when y is greater than x  slice scrolls - upward", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slice changes", "verified");
		viewerPage.assertEquals(viewerPage.getValueOfCurrentPhase(1) , currentPhaseNumber, "no phase scroll", "verified");




	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US963", "Positive" })
	public void test22_US963_TC4302_VerifyScrollingWhenYSmallerThanX()throws InterruptedException, IOException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the scrolling functionlaity when direction X> Y");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		int currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		String currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Keep mouse left button pressed and drag the mouse upwards in X direction in such a way that Xdirection is > Ydirection.");

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, 100,10);

		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "when y is lesser than x  slice doesn't scroll", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slice scrolls", "verified");
		viewerPage.assertTrue(viewerPage.getValueOfCurrentPhase(1) >currentPhaseNumber, "phase scrolls when x > y - upward", "verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Keep mouse left button pressed and drag the mouse downwards in X direction in such a way that X direction is > Y direction.");

		currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
		currentPhaseNumber = viewerPage.getValueOfCurrentPhase(1);
		currentImageNumber=viewerPage.getImageNumberLabelValueText(1);

		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 0, 0, -100,-10);

		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentScrollPosition, "when y is lesser than x  slice doesn't scroll", "verified");
		viewerPage.assertNotEquals(viewerPage.getImageNumberLabelValueText(1), currentImageNumber, "total slice scrolls", "verified");
		viewerPage.assertTrue(viewerPage.getValueOfCurrentPhase(1)<currentPhaseNumber, "phase scrolls when x > y - downward", "verified");




	}

	//US972 Multiphase Cine support
	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Positive" ,"US2325","F1090","E2E"})
	public void test23_US972_TC4345_US2325_TC9776_Verify4DCineIconOnRadialMenu()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 4D cine icon on radial arc menu"
				+ "<br> Verify 2D and 4D cine  icons and its functionality from quick toolbar");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);
	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify 4D cine icon on radial arc menu");
		viewerPage.openQuickToolbar(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.cine4DPlayIcon), "Verify 4D icon present" , "4D Cine icon present on radial arc menu");
	}	

	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Positive" ,"US2325","F1090","E2E"})
	public void test24_US972_TC4346_US2335_TC9776_Verify4DCineEnableFor4DData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 4D cine enable for 4D data only."
				+ "<br> Verify 2D and 4D cine  icons and its functionality from quick toolbar");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(AH4_Patient, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify 4D cine icon on radial arc menu for 2D data");
		viewerPage.openQuickToolbar(1);
		viewerPage.click(viewerPage.cine4DPlayIcon);
		viewerPage.assertFalse(viewerPage.checkCurrentSelectedIcon(ViewerPageConstants.CINE4D), "Verify 4D icon" , "4D Cine icon disable on radial arc menu for 2D data");

		//load 4d data on viewer
		helper.browserBackAndReloadViewer(CT_PET_Multiphase_Patient, 1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify 4D cine icon on radial arc menu for 4D data");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isCine4DStopped(1),"Verify 4D icon","4D Cine icon enable on radial arc menu for 4D data");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Positive" })
	public void test25_US972_TC4347_Verify4DCinePhaseSync()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 4D phase perform without skipping any image /phase");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		int currentPhaseImage = viewerPage.getValueOfCurrentPhase(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(1), newImagePath+"/goldImages/radial4DCinePlayImage.png");
		//open a Radial Menu for Viewbox1 and play cine4D
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));

		//to stop cine4D
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		int phaseimageAfterCine4D = viewerPage.getValueOfCurrentPhase(1);
		viewerPage.takeElementScreenShot(viewerPage.getViewbox(1), newImagePath+"/actualImages/radial4DCinePlayImage.png");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify Cine 4D Play is stopped on Viewbox");
		viewerPage.assertNotEquals(currentPhaseImage, phaseimageAfterCine4D, "Verifying the Cine 4D play is stopped", "Cine 4D is stopped and working fine");
		String expectedImagePath = newImagePath+"/goldImages/radial4DCinePlayImage.png";
		String actualImagePath = newImagePath+"/actualImages/radial4DCinePlayImage.png";

		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, actualImagePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify post Cine 4D operation images on Viewbox are different");
		viewerPage.assertFalse(cpStatus, "The Pre and Post Cine 4D Images are different","");
		ExtentManager.customExtentReportLog(PASS, extentTest, "Verifying Cine operation from Radial Menu", "Successfully verified checkpoint with image comparison.<br>Image name is radial4DCinePlayImage.png");	



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Negative" ,"BVT"})
	public void test26_US972_TC4348_Verify4DCineSynchronizationInActiveViewbox()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cine 4D functionality in active viewbox");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify 4D cine icon on functionality on viewbox-1");
		//play Cine4D 
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isCine4DStopped(1),"Verify Cine4D playing on viewbox-1","Verified");

		//perform cine operation on viewbox2
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify 4D cine functionality on active viewbox-2 ");
		//play Cine4D
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D stop when user select 4D cine icon from radial menu on viewbox-2","Verified");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertFalse(viewerPage.isCine4DStopped(2),"Verify Cine4D playing on viewbox-2","Verified");
	}	

	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Positive","BVT" })
	public void test27_US972_TC4351_Verify2DCineSynchronization()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cine 2D functionality on multiphase data");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		lineWithUnit = new MeasurementWithUnit(driver);

		//play Cine4D 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify cine 2D functionality on viewbox-1");
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify cine 2D stopped on clicking cine icon");
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped after click on cine icon","Cine is stopped on Viewbox1");

		//draw annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify 2D cine stopped when drawn annotation is in progress");
		viewerPage.selectCineFromQuickToolbar(viewerPage.getViewPort(1));
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewerWithClick(1,-70, 0, 50,0);
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on drawing a linear measurement","Cine is stopped on Viewbox1");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US972", "Positive" })
	public void test28_US972_TC4349_Verify4DCineStopsIfNewCommandIsInprogress()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 4D cine stops if any new command is in progress");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(CT_PET_Multiphase_Patient, 1);
	
		lineWithUnit = new MeasurementWithUnit(driver);

		//verify cine 4D functionality on viewbox-1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/8]","Verify cine 4D functionality on viewbox-1");
		//int beforeCinePhasenumber1=viewerPage.getValueOfCurrentPhase(1);
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine is stopped after click on cine icon","Cine is stopped on Viewbox1");

		//verify cine 4D functionality on viewbox-2	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/8]","Verify cine 4D functionality on viewbox-2 and cine stops in viewbox-1");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(2));
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine stop on viewbox-1","Cine is stopped on Viewbox1");
		viewerPage.assertFalse(viewerPage.isCine4DStopped(2),"Verify Cine start on viewbox-2","Cine is playing on Viewbox2");

		//verify cine stopped when any command is in progress
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/8]","Verify cine 4D functionality on viewbox-2 stops while drawing annotation");
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		viewerPage.dragAndReleaseOnViewerWithClick(2,-70, 0, 50,0);
		viewerPage.assertTrue(viewerPage.isCine4DStopped(2),"Verify Cine4D is stopped when user starts drawing linear measurement on viewbox-2","Verified");

		//4D cine start on viewbox1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/8]","Verify that 4D cine stops in viewbox 1 when user starts performing scroll");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.mouseWheelScrollInViewer(1, NSGenericConstants.SCROLL_UP, 1);
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D is stopped when user starts performing scroll on viewbox-1w","Verified");

		//again click on cine 4D ,cine 4D should stop
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/8]","Verify that 4D cine stops in viewbox 1 when user again click on cine4D icon");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D is stopped when user again click on cine 4D icon","Verified");

		//press C from keyboard and check 2D cine start
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[6/8]","Verify 4D cine stop and 2D cine start after pressing C from keyboard");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D stop when user press C from keyboard","Verified");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine2D start when user press C from keyboard","Verified");

		//click on 4D and check 2D cine stop
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[7/8]","Verify 4D cine start and 2D cine stop after selecting 4D cine icon from radial menu");
		viewerPage.selectCine4DFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isCine4DStopped(1),"Verify Cine4D start when user select 4D cine icon from radial menu","Verified");
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine2D stop when user select 4D cine icon from radial menu","Verified");

		//press C and check 4D stop and 2D start
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[8/8]","Verify 4D cine stop and 2D cine start afte pressing C from keyboard");
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D stop when user press C from keyboard","Verified");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine2D  start when user press C from keyboard","Verified");

		//while running 2D press C and check 2D cine stop
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine2D stop when user again press C from keyboard","Verified");
		viewerPage.assertTrue(viewerPage.isCine4DStopped(1),"Verify Cine4D stop when user again press C from keyboard","Verified");
	}


	//DE1071 Slices shown as phases for secondary capture series
	@Test(groups = { "Chrome", "IE11", "Edge", "Positive" })
	public void test29_DE1071_TC4421_VerifySlicesNotDisplayAsPhases()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that slices are not showing as phases for secondary capture series");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AIChange_MS_Patient, 1);
	
		contentSelector=new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify top 2 series 'CD:FLAIR-SC-Overlay' and 'CD:MTS-SC-Overlay' are present in default loaded layout");
		viewerPage.closeNotification();
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(AIChange_MS_Result1), "Verify series" +" "+AIChange_MS_Result1+" "+"is present in default loaded layout" , "Verified");
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(AIChange_MS_Result2), "Verify series" +" "+AIChange_MS_Result2+" "+"is present in default loaded layout" , "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify max scroll position seen as 48 for both the series.");
		viewerPage.assertEquals(viewerPage.getMaxNumberofScrollForViewbox(1), 48, "Verify phase value for"+" "+AIChange_MS_Result1+ ""+ "display as 48", "Verified");
		viewerPage.assertEquals(viewerPage.getMaxNumberofScrollForViewbox(2), 48, "Verify phase value for"+" "+AIChange_MS_Result2+ ""+ "display as 48", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify phase number seen as 1 for both the series.");
		viewerPage.assertFalse(viewerPage.verifyPhaseTextPresence(1), "Verify only 1 phase seen for "+" "+AIChange_MS_Result1, "Verified");
		viewerPage.assertFalse(viewerPage.verifyPhaseTextPresence(2), "Verify only 1 phase seen for "+" "+AIChange_MS_Result2, "Verified");

	}	
}






