package com.trn.ns.test.viewer.outputPanel;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ThumbnailSelectionOnOutputPanelTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	String flag = "false", open = "open", close = "close";
	DatabaseMethods db= new DatabaseMethods(driver);
	private PointAnnotation point;
	private CircleAnnotation circle;
	private MeasurementWithUnit lineWithUnit;
	private OutputPanel panel;
	private SimpleLine line;
	private HelperClass helper;
	private ViewerSliderAndFindingMenu findingMenu;
	private ViewerLayout layout;

	private TextAnnotation textAnno;
	private EllipseAnnotation ellipse;
	private ContentSelector contentSelector;
	private PolyLineAnnotation poly;

	private final String circleComment="Verify tooltip is displayed on text in output panel when browser window is resized and text becomes very small";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String groupName="Group_";

	// Get Patient Name
	String AH4_FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_FilePath);
	String AH4_PatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, AH4_FilePath);
	String Series1= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, AH4_FilePath);

	String GSPS_FilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, GSPS_FilePath);

	String Liver9_FilePath= Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String Liver9_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Liver9_FilePath);
	String FirstSeriesDescription= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Liver9_FilePath);
	String FifthSeriesDescription= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, Liver9_FilePath);

	String multiPointSeriesFilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String multiPointPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, multiPointSeriesFilePath);

	String ADCphilips_FilePath = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ADCphilips_FilePath);
	String SeriesToSelect_ADC1= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, ADCphilips_FilePath);
	String SeriesToSelect_ADC2= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, ADCphilips_FilePath);

	String anonymous_FilePath = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous_FilePath);

	String boneAge_FilePath = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String boneAge_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, boneAge_FilePath);
	String patient_BoneAge1_Result1 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,boneAge_FilePath);

	String IBLJohnDoe_FilePath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBLJohnDoe_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IBLJohnDoe_FilePath);

	String ah4_pdf_FilePath = Configurations.TEST_PROPERTIES.get("AH4_pdf_filepath");
	String ah4_pdf_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ah4_pdf_FilePath);

	String Imbio_Texture_FilePath = TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String Imbio_Texture_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Imbio_Texture_FilePath);

	String gaelKuhnFilePath =Configurations.TEST_PROPERTIES.get("GAEL^KUHN_filepath");
	String GaelPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gaelKuhnFilePath);
	String GaelPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, gaelKuhnFilePath);

	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);

	String imbioFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilePath);
	String resultDescriptionForImbio= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, imbioFilePath);


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive" ,"US950","US1159","BVT"})
	public void test01_US935_TC4122_US950_TC4387_US1159_TC5793_VerifyFocusAfterClickingOnThumbnailForUserDrawnAnnotaion()
			throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail for User drawn annotaion"
				+ "<br> Verify that when user click on thumbnail from output panel yellow shadow should be displayed around annotation in viewer	");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(Liver9_Patient);

		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		//Open output panel
		panel.enableFiltersInOutputPanel(true, false, false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify on clicking the 'Thumbnail' ,focus jump to that slice corresponding to that thumbnail in active viewbox for user drawn annotaion");
//		viewerPage.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.LINEAR_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));
		panel.clickOnJumpIcon(1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive","US950","US1159","BVT"})
	public void test02_US935_TC4123_US950_TC4387_US1159_TC5793_VerifyFocusAfterClickingOnThumbnailForMachineDrawnAnnotaion()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail for machine drawn annotation"
				+ "<<br> Verify that when user click on thumbnail from output panel yellow shadow should be displayed around annotation in viewer");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(GSPS_Patient);

		patientListPage.clickOntheFirstStudy();

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();

		List<String> firstViewbox = point.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR);
		List<String> secondViewbox = point.getFindingsName(2, ViewerPageConstants.PENDING_FINDING_COLOR);

		panel=new OutputPanel(driver) ;
		//open output panel and click on pending button
		panel.enableFiltersInOutputPanel(false, false, true);

//		String findingName = panel.getTextOfFindingFromOutputPanel(1);
//		// verify focus for machine drawn annotation
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify on clicking the 'Thumbnail' ,focus jump to that slice corresponding to that thumbnail in active viewbox for machine drawn annotation");
//		point.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.POINT_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));

		panel.clickOnJumpIcon(1);

		if(panel.getActiveViewbox()==1)
			point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 2), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for Machine drawn annotation");
		else 
			point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(2, 3), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for Machine drawn annotation");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive","US1159","BVT","US2284","F1125","E2E"})
	public void test03_US935_TC4124_US1159_TC5793_US2284_TC9965_VerifyWarningMessageForThumbnail()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning message when thumbnail corresponding slice is not open in active view"
				+ "<br> Verify the info message displayed when a finding is selected from a series that is not loaded in Output Panel.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Liver9_Patient, 1);
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);

		panel=new OutputPanel(driver) ;
		circle = new CircleAnnotation(driver);

		//Drwan circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -70,-70);	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[1/3]", "Verify that drawn annotaion is selected");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the circle is selected", "Circle is selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[2/3]", "Verify new series loaded on viewbox-1 ");
		contentSelector.selectSeriesFromSeriesTab(1, FifthSeriesDescription);
		viewerPage.waitForAllChangesToLoad();
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(FifthSeriesDescription), "Verify 5th series selected from content selector ", "Verified 5th series is displyed on viewbox-1");

		// click on output panel
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();

		//click on first thumbnail image from output panel
		panel.clickOnJumpIcon(1);

		// verify warning message for thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+FirstSeriesDescription+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");

	}

	
	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive" })
	public void test05_US935_TC4138_VerifyFocusWhenThumbnailOpenedInMultipleViewBox()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail in multiple viewbox ");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(Liver9_Patient);

		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;

		//Draw circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -200, -50, -100,-100);	
		circle.closeWaterMarkIcon(2);

		//Select First series on viewbox-2
		viewerPage.click(viewerPage.getViewPort(2));
		contentSelector.selectSeriesFromSeriesTab(2, FirstSeriesDescription);
		viewerPage.waitForAllChangesToLoad();
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(FirstSeriesDescription), "Verify First series selected from content selector ", "Verified First series is displyed on viewbox-2");

		//Select First series on viewbox-3 and viewbox-4
		viewerPage.click(viewerPage.getViewPort(3));
		contentSelector.selectSeriesFromSeriesTab(3, FirstSeriesDescription);
		viewerPage.waitForAllChangesToLoad();
		viewerPage.click(viewerPage.getViewPort(4));
		contentSelector.selectSeriesFromSeriesTab(4, FirstSeriesDescription);
		viewerPage.waitForAllChangesToLoad();

		//Open Output panel
		panel.enableFiltersInOutputPanel(true, false, false);

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify focus when thumbnail open in multiple viewbox");
//		viewerPage.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));

		panel.clickOnJumpIcon(1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify focus jump to finding in First viewbox when thumbnail open in multiple viewbox", "Focus jump to finding in First viewbox when thumbnail open in multiple viewbox");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive","Sanity","BVT" })
	public void test06_US935_TC4139_VerifyFocusWhenSliceNotLoadedOnViewbox()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail when slice is not loaded on any viewbox but series is loaded");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(Liver9_Patient);

		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;

		//Draw circle annotation on viewbox-1
		//Slice number on which annotaion is drawn
		String defaultImageNumber=viewerPage.getCurrentScrollPosition(1);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -200, -50, -100,-100);	

		//change slice using scroll
		viewerPage.scrollUpToSliceUsingKeyboard(1, 4);

		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		String changeImageNumber=viewerPage.getCurrentScrollPosition(1);
		viewerPage.assertNotEquals(changeImageNumber, defaultImageNumber, "Verify slice position change or not", "Slice position changed successfully to :"+ changeImageNumber );

		//Open output panel and click on thumbnail 
		panel.enableFiltersInOutputPanel(true, false, false);

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verified text of annotation on output panel");
//		viewerPage.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));	
		panel.clickOnJumpIcon(1);

		//Verify viewbox-1 is active and drawn annotation is selected
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify drawn annotaion is currentActiveGSPS on viewbox-1", "Drawn annoation is currentActiveGSPS on viewbox-1 is verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify on clicking the 'Thumbnail',focus jump on the slice belonging to that annotation");
		String CaptureImageNumber=viewerPage.getCurrentScrollPosition(1);
		viewerPage.assertEquals(CaptureImageNumber, defaultImageNumber, "Verify focus jump to default slice position", "On clicking thumbnail,Focus jump to slice belonging to that annotation : "+ CaptureImageNumber );
		viewerPage.assertNotEquals(CaptureImageNumber, changeImageNumber, "Verify focus jump to default slice position", "Verified the focus jump to slice belonging to that annotation");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive" })
	public void test08_US935_TC4141_VerifyWarningMessageClosed()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning message closed once click on message and close Icon");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Liver9_Patient, 1);
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;
		contentSelector = new ContentSelector(driver);

		//Drwan circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -70,-70);	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[1/4]", "Verify that drawn annotation is selected");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the circle is selected", "Circle is selected on viewbox-1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[2/4]", "Verify new series loaded on viewbox-1 ");
		contentSelector.selectSeriesFromSeriesTab(1, FifthSeriesDescription);
		viewerPage.waitForAllChangesToLoad();
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(FifthSeriesDescription), "Verify 5th series selected from content selector ", "Verified 5th series is displyed on viewbox-1");

		//Open output panel and click on first thumbnail
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();
		panel.clickOnJumpIcon(1);

		//Verify warning message closed after click on message
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify warning messsage closed after click on message");
		viewerPage.click(panel.notificationTiles.get(0));
		viewerPage.waitForTimePeriod(1000); // waiting for notification to get closed
		viewerPage.assertFalse(viewerPage.isElementPresent(panel.notificationDiv), "Verify warning message closed once click on message", "Warning message closed after click on that message");

		//Click on thumbnail
		panel.clickOnJumpIcon(1);

		//Verify warning message closed after click on closed icon
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify warning messsage closed after click on close icon");
		viewerPage.closeNotification();
		viewerPage.assertFalse(viewerPage.isElementPresent(panel.notificationDiv), "Verify warning message closed once click on closed icon", "Warning message closed after click on close icon");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US935", "Positive"})
	public void test09_US935_TC4166_VerifyWarningMessageIncludeSeriesName()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning message contains series name if thumbnail corresponding slice is not open in active view");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Liver9_Patient, 1);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;
		contentSelector = new ContentSelector(driver);
		//Drwan circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -50, -50, -70,-70);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[1/3]", "Verify that drawn Circle is selected");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the circle is selected", "Circle is selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[2/3]", "Verify new series loaded on viewbox-1 ");
		contentSelector.selectSeriesFromSeriesTab(1, FifthSeriesDescription);
		viewerPage.waitForAllChangesToLoad();
		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(FifthSeriesDescription), "Verify 5th series selected from content selector ", "Verified 5th series is displyed on viewbox-1");

		//Open output panel and click on first thumbnail
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();
		panel.clickOnJumpIcon(1);

		//Verify series name in warning message
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verify warning messsage contains series name if thumbnail corresponding slice is not open in active view ");
		viewerPage.assertTrue(viewerPage.getNotificationMessage(1).contains(FirstSeriesDescription), "Verify warning message contains series name", "Verified that warning message contains series name");
	}


	//US829 Display thumbnails in the output panel​

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive" })
	public void test10_US829_TC3650_verifyThumbnailForMachineDrawnAnno() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail for machine drawn annotations");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + multiPointPatient + "in viewer");
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(multiPointPatient, 1);

		OutputPanel panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		point = new PointAnnotation(driver);
		
		HashMap<String, String> findingsListVb1 = panel.getGSPSFindingList(1);
		point.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		HashMap<String, String> findingsListVb2 = panel.getGSPSFindingList(2);

		panel.enableFiltersInOutputPanel(false, false, true);

		//			{Checkpoint:1} 
		panel.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()), "Checkpoint[1/3]", "verifying the total findings");
      
		// All machine drawn GSPS annotations should be listed in output panel under 'Pending' status.
		//		All the machine drawn annotations should display thumbnails. 
		//		Thumbnail image should contain snapshot of slice on which that annotation is drawn.
		
		for(int i =0;i<panel.thumbnailList.size();i++) {
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/3]"," verifying the thumbnail for machine finding "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/3]"," verifying annotation within thumbnail for machine finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive" })
	public void test11_US829_TC3651_TC3778_verifyThumbnailForUserDrawnAnno() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail for user drawn annotations"
				+ "Verify thumbnaill for newly added annotation");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		//		viewerPage.inputImageNumber(1, 1);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);
		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[1/7]", "verifying the total findings");

		//		All the user drawn annotations should display thumbnails. Thumbnail image should contain snapshot of slice on which that annotation is drawn.
		for(int i =0;i<panel.thumbnailList.size();i++) {
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/7]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/7]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);

		panel.enableFiltersInOutputPanel(true, true, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[4/7]", "verifying the total findings");

		//		Newly added annotation should be displayed in output panel
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5."+i+"/7]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[6."+i+"/7]"," verifying annotation within thumbnail for user finding  "+(i+1));

		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive","Sanity"})
	public void test12_US829_TC3652_verifyThumbnailForMachineDrawnAnnoAndUserDrawnAnn() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail for machine drawn annotations + user drawn annotations");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + multiPointPatient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(multiPointPatient, 1);
	
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.waitForViewerpageToLoad();
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		HashMap<String, String> findingsListVb1 = findingMenu.getGSPSFindingList(1);
		point.openGSPSRadialMenu(point.getAllPoints(2).get(0));
		HashMap<String, String> findingsListVb2 = findingMenu.getGSPSFindingList(2);

		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLineExitUsingESC(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);

		findingMenu.assertEquals(panel.thumbnailList.size(), (findingsListVb1.size()+findingsListVb2.size()+5), "Checkpoint[1/3]", "verifying the total findings");

		//		Output panel should list all the findings (machine drawn plus user drawn). All should have thumbnail.
		//		Thumbnail image should contain snapshot of slice on which that annotation is drawn.		
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/3]"," verifying the thumbnail for machine finding"+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/3]"," verifying annotation within thumbnail for machine finding  "+(i+1));

		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive" })
	public void test13_US829_TC3651_TC3779_verifyOutputPanelWhenAnnoIsDeleted() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail when any annotation is deleted");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		//		viewerPage.inputImageNumber(1, 1);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);

		//					{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[1/6]", "verifying the total findings");

		//		<Checkpoint 1>
		//		All the user drawn annotations should display thumbnails. Thumbnail image should contain snapshot of slice on which that annotation is drawn.
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);

		lineWithUnit.deleteSelectedMeasurement();
		circle.selectCircle(1, 1);
		circle.deleteSelectedCircle();
		circle.waitForTimePeriod(1000);

		panel.enableFiltersInOutputPanel(true, true, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[4/6]", "verifying the total findings");

		//		<Checkpoint 2>
		//		Deleted  annotation should NOT be displayed in output panel
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[6."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive" })
	public void test14_US829_TC3780_verifyOutputPanelWhenAnnoIsEdited() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail when any annotation is edited");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		//		viewerPage.inputImageNumber(1, 1);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[1/4]", "verifying the total findings");

		////		<Checkpoint 1>
		////		All the user drawn annotations should display thumbnails. Thumbnail image should contain snapshot of slice on which that annotation is drawn.
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);

		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, -10, 20);

		ellipse.selectEllipse(1, 1);
		ellipse.moveSelectedEllipse(1, -30, -40);

		point.movePoint(1, 1, 40, 50);

		panel.enableFiltersInOutputPanel(true, true, true);

		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[4/6]", "verifying the total findings");

		//		<Checkpoint 2>
		//		Edited/updated annotation should be displayed in output panel
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[6."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829", "Positive","Sanity" })
	public void test15_US829_TC3781_verifyOutputPanelWhenAnnoStateChanged() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail when state of any annotation is changed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		OutputPanel panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		point = new PointAnnotation(driver);
		

		//		viewerPage.inputImageNumber(1, 1);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);

		//			{Checkpoint:1} 
		panel.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[1/6]", "verifying the total findings");

		//		<Checkpoint 1>
		//		All the user drawn annotations should display thumbnails. Thumbnail image should contain snapshot of slice on which that annotation is drawn.
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));

		}

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);
		
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));		
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(1).get(0));

		panel.enableFiltersInOutputPanel(true, true, true);

		panel.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[4/6]", "verifying the total findings");

		//		<Checkpoint 2>
		//		Updated annotation should be displayed in output panel
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[6."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US829","US828", "Positive" })
	public void test16_US829_TC3782_US828_TC3495_verifyOutputPanelWhenAnnoPostLayoutChange() throws  InterruptedException, Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail after layout change"
				+ "<br> Verify the scroll on outpanel.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

		//		viewerPage.inputImageNumber(1, 1);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		lineWithUnit.selectAcceptfromGSPSRadialMenu();

		//		viewerPage.inputImageNumber(2, 1);
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);
		circle.selectRejectfromGSPSRadialMenu();

		//		viewerPage.inputImageNumber(3, 1);
		ellipse = new  EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, 0, -30,-40);

		//		viewerPage.inputImageNumber(4, 1);
		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);

		//		viewerPage.inputImageNumber(5,1);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		panel.enableFiltersInOutputPanel(true, true, true);

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[1/6]", "verifying the total findings");

		//		<Checkpoint 1>
		//		All the user drawn annotations should display thumbnails. Thumbnail image should contain snapshot of slice on which that annotation is drawn.

		for(int i =0;i<panel.thumbnailList.size();i++) {

			//			Verify the scroll on outpanel.
			viewerPage.assertTrue(panel.isElementPresent(panel.verticalScrollBarInOP), "Verify new UI of Scrollbar component present on viewbox1", "Verified");
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[2."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[3."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);
	
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		layout.click(layout.getViewPort(1));

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));		
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(1).get(0));

		panel.enableFiltersInOutputPanel(true, true, true);

		layout.assertEquals(panel.thumbnailList.size(), 5, "Checkpoint[4/6]", "verifying the total findings");

		//		<Checkpoint 2>
		//		Updated annotation should be displayed in output panel
		for(int i =0;i<panel.thumbnailList.size();i++) {

			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[5."+i+"/6]"," verifying the thumbnail for user drawn annotation "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)),"Checkpoint[6."+i+"/6]"," verifying annotation within thumbnail for user finding  "+(i+1));
		}

		panel.openAndCloseOutputPanel(false);

	}


	//US828: Display findings in the output panel
	@Test(groups = { "Chrome", "IE11", "Edge", "US828", "Positive" })
	public void test17_US828_TC3782_verifyEmptyOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify thumbnail after layout change");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		OutputPanel panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		
		panel.enableFiltersInOutputPanel(true, true, true);

		panel.assertEquals(panel.thumbnailList.size(),0,"Checkpoint[1/6]", "Verifying no thumnails present");

		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);
	
		panel.enableFiltersInOutputPanel(true, true, true);

		panel.assertEquals(panel.thumbnailList.size(),0,"Checkpoint[4/6]", "Verifying no thumnails present");
		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US828", "Positive" })
	public void test18_US828_TC3481_TC3482_verifyAcceptedButtonAndFinding() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the default 'Accepted' button on outpanel."
				+ "<br> Verify the Accepted findings when default Accepted button is selected.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		circle = new CircleAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);

//		panel.enableFiltersInOutputPanel(true, false, false);
//
//		panel.assertEquals(panel.getCssValue(panel.acceptedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[1]", "Verifying no thumnails present");
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BLACK_COLOR,"Checkpoint[2]", "Verifying no finding present");
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BLACK_COLOR,"Checkpoint[3]", "Verifying no finding name present");
//
//		panel.openAndCloseOutputPanel(false);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Liver9_Patient, 1, 1);
		
//		panel.enableFiltersInOutputPanel(true, false, false);
//
//		panel.assertEquals(panel.getCssValue(panel.acceptedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[4]", "Verifying no thumnails present");
//		panel.assertEquals(panel.getCssValue(panel.rejectedButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BLACK_COLOR,"Checkpoint[5]", "Verifying no finding present");
//		panel.assertEquals(panel.getCssValue(panel.pendingButton, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BLACK_COLOR,"Checkpoint[6]", "Verifying no finding name present");
//
//		panel.openAndCloseOutputPanel(false);

		circle.selectCircleFromQuickToolbar(1);
		circle = new CircleAnnotation(driver);
		circle.drawCircle(1, 100, -30, -30,-40);

		panel.enableFiltersInOutputPanel(true, true, true);

		DateFormat dateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		Date date = new Date();

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[7]", "verifying the total findings");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US828", "Positive" })
	public void test19_US828_TC3483_TC3490_TC3491_TC3489_TC3492_TC3493_TC3494_TC3496_verifyMultipleSelection() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Multiselection filter button."
				+ "<br> Grouper when \"Accepted\", \"Pending\", \"Rejected\" filters are selected."
				+ "<br> 	Grouper when \"Accepted\", \"Pending\" filters are selected."
				+ "<br> 	Grouper when \"Accepted\", \"Rejected\" filters are selected."
				+ "<br> 	Grouper when \"Pending\", \"Rejected\" filters are selected."
				+ "<br> Grouper when \"Rejected\" filters is selected."
				+ "<br> 	Grouper when \"Pending\" filters is selected."
				+ "<br> Verify the fall back functionality for 'Accepted' button.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		point = new PointAnnotation(driver);

		OutputPanel panel = new OutputPanel(driver);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, -30, -30,-40);

		circle.closingConflictMsg();
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -30, -60,-70);
		ellipse.selectRejectfromGSPSRadialMenu();

		circle.closingConflictMsg();
		point.selectPointFromQuickToolbar(4);
		point.drawPointAnnotationMarkerOnViewbox(4, 100, 100);
		point.selectAcceptfromGSPSRadialMenu(point.getAllPoints(4).get(0));

		//		Grouper when "Accepted", "Pending", "Rejected" filters are selected.
		//		Verify the Multiselection filter button.
		panel.enableFiltersInOutputPanel(true, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[1]", "verifying the total findings");

		panel.enableFiltersInOutputPanel(false, true, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[1]", "verifying the total findings");
		//		Grouper when "Accepted", "Pending" filters are selected.
		
		panel.enableFiltersInOutputPanel(true, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[1]", "verifying the total findings");

	
		//		Grouper when "Accepted", "Rejected" filters are selected.
		panel.enableFiltersInOutputPanel(true, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[1]", "verifying the total findings");

		//		Grouper when "Rejected" filters is selected.
		panel.enableFiltersInOutputPanel(false, true, false);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1]", "verifying the total findings");

		//		Grouper when "Pending" filters is selected.

		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1]", "verifying the total findings");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US828", "Positive" })
	public void test20_US828_TC3497_verifyAllAnnotations() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings on study level for Text, Graphics, Distance.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		point = new PointAnnotation(driver);
		OutputPanel panel = new OutputPanel(driver);	
		ellipse = new  EllipseAnnotation(driver);

		String test = "abc";
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);
		lineWithUnit.closingConflictMsg();

		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(2, -70, -30, -30,-40);

		ellipse.closingConflictMsg();
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(4, -70, 0, -80,-90);

		point = new  PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 40, 50);
		ellipse.closingConflictMsg();
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(4);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(4, abc);
		ellipse.closingConflictMsg();
		
		TextAnnotation text = new TextAnnotation(driver);
		text.selectTextArrowFromQuickToolbar(2);
		text.drawText(2, 30, 30, test);

		panel.enableFiltersInOutputPanel(true, true, true);

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 6, "Checkpoint[1]", "verifying the total findings");
			panel.openAndCloseOutputPanel(false);

		lineWithUnit.click(viewerPage.getViewPort(1));
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(2));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(2).get(0));
		lineWithUnit.click(viewerPage.getViewPort(4));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(4).get(0));
		lineWithUnit.click(viewerPage.getViewPort(1));
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(4));
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(4, 1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(2));
		text.selectRejectfromGSPSRadialMenu(text.getLineOfTextAnnotations(2).get(0));


		panel.enableFiltersInOutputPanel(true, true, true);

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 6, "Checkpoint[8]", "verifying the total findings");

		panel.openAndCloseOutputPanel(false);

		lineWithUnit.click(viewerPage.getViewPort(1));
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(2));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(2).get(0));
		lineWithUnit.click(viewerPage.getViewPort(4));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(4).get(0));
		lineWithUnit.click(viewerPage.getViewPort(1));
		point.selectRejectfromGSPSRadialMenu(point.getAllPoints(1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(4));
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(4, 1).get(0));
		lineWithUnit.click(viewerPage.getViewPort(2));
		text.selectRejectfromGSPSRadialMenu(text.getLineOfTextAnnotations(2).get(0));


		panel.enableFiltersInOutputPanel(false, false, true);

		//			{Checkpoint:1} 
		viewerPage.assertEquals(panel.thumbnailList.size(), 6, "Checkpoint[15]", "verifying the total findings");
	panel.openAndCloseOutputPanel(false);


	}

	//US985: Jump to finding using jump-to icon
	@Test(groups = { "Chrome", "IE11", "Edge", "US985","US1159","Positive","US2284","F1125"})
	public void test21_US985_TC4171_US1159_TC5793_US2284_TC9756_verifyJumpingIconForAcceptedFindings()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify jumping icon on thumbnail for accepted findings in output panel."
				+ "<br> Verify Jump to finding from Output Panel thumbnail.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		//Open output panel
		panel.enableFiltersInOutputPanel(true, true, true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify jump to icon visibility on mouse hovering on thumbnail");
		panel.mouseHoverOnThumbnail(1);
		viewerPage.assertEquals(viewerPage.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon", "Verified jump to icon visibility on mouse hovering on thumbnail ");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/1]","Verify jump to icon visibility on mouse hovering on icon");
		panel.mouseHoverOnJumpIcon(1);
		viewerPage.assertEquals(viewerPage.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon","Verified jump to icon visibility on mouse hovering on jump to icon");

		panel.clickOnJumpIcon(1);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}	

	@Test(groups = { "Chrome", "IE11", "Edge", "US985", "Positive","US2284","F1125"})
	public void test22_US985_TC4172_US2284_TC9756_VerifyJumpingIconForRejectedFindings()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify jumping icon on thumbnail for rejected findings in output panel."
				+ "<br> Verify Jump to finding from Output Panel thumbnail.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verify linear measurement annotation is displaying as rejected on viewer page");
		findingMenu.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation in displaying as rejected on viewer page");		

		//Open output panel
		panel.enableFiltersInOutputPanel(false, true, false);

		//click on rejected tab 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/4]","Verify rejected findings on output panel");
		//mouse hover on thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify jump to icon visibility on mouse hovering on thumbnail");
		panel.mouseHoverOnThumbnail(1);
		findingMenu.assertEquals(findingMenu.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon", "Verified jump to icon visibility on mouse hovering on thumbnail ");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify jump to icon visibility on mouse hovering on jump to icon");
		panel.mouseHoverOnJumpIcon(1);
		findingMenu.assertEquals(findingMenu.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon","Verified jump to icon visibility on mouse hovering on jump to icon");

		panel.clickOnJumpIcon(1);
		findingMenu.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US985", "Positive","US2284","F1125"})
	public void test23_US985_TC4173_US2284_TC9756_VerifyJumpingIconForPendingFindings()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify jumping icon on thumbnail for pending findings in output panel."
				+ "<br> Verify Jump to finding from Output Panel thumbnail.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verify linear measurement annotation is displaying as pending on viewer page");
		findingMenu.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Verifying that the linear measurement annotation is pending", "Linear measurement annotation in displaying as pending on viewer page");		

		//Open output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		//mouse hover on thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify jump to icon visibility on mouse hovering on thumbnail");
		panel.mouseHoverOnThumbnail(1);
		findingMenu.assertEquals(findingMenu.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon", "Verified jump to icon visibility on mouse hovering on thumbnail ");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify jump to icon visibility on mouse hovering on jump to icon");
		panel.mouseHoverOnJumpIcon(1);
		findingMenu.assertEquals(findingMenu.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon","Verified jump to icon visibility on mouse hovering on jump to icon");

		panel.clickOnJumpIcon(1);
		findingMenu.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US985", "Positive","DE1374"})
	public void test24_US985_TC4244_DE1374_TC5742_VerifyVisibilityOfJumpIconOnMouseHover()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify visibility of jump icon ,on hovering the mouse on thumbnail."
				+ "<br> Verify that the 'Jump-To'  is aligned to center of the thumbnail in the output panel. - Happy path.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verify linear measurement annotation is displaying on viewer page");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying that the linear measurement annotation is accepted", "Linear measurement annotation in displaying as accepted on viewer page");		

		//Open output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/4]","Verified that output panel open successfully.");
		panel.enableFiltersInOutputPanel(true, true, true);

		//mouse hover on thumbnail 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify jump to icon visible on mouse hover on thumbnail");
		
		panel.mouseHoverOnThumbnail(1);		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[4/4]", "Verified that after mouse hover on thumbnail,jump to icon is visible");
		

	}	

	@Test(groups = { "Chrome", "IE11", "Edge", "US985", "Positive" })
	public void test25_US985_TC4245_VerifyVisibilityOfJumpIconWhenOutputPanelOpen()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify visibility of jump icon ,when output panel open");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify linear measurement annotation is displaying on viewer page");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying that the linear measurement annotation is accepted", "Linear measurement annotation in displaying as accepted on viewer page");		

		//Open output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verified that output panel open successfully.");
		panel.enableFiltersInOutputPanel(true, false, false);

		//verify jump icon highlighted or not
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify jump to icon visible after opening the output panel");
		panel.assertFalse(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Verify jump to icon is not visible on thumbnail", "Verified that jump icon not highlighted when user open output panel");

	}	

	@Test(groups = { "Chrome", "IE11", "Edge", "US985", "Positive" })
	public void test26_US985_TC4254_VerifyJumpIconWhenMoveMouseAwayFromThumbnail()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Jump icon when user moves the mouse away from the thumbnail");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		
		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/5]","Verify linear measurement annotation is displaying on viewer page");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying that the linear measurement annotation is accepted", "Linear measurement annotation in displaying as accepted on viewer page");		

		//Open output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/5]","Verified that output panel open successfully.");
		panel.enableFiltersInOutputPanel(true,false,false);

		//mouse hover on thumbnail 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/5]","Verify jump to icon visibility on mouse hovering on thumbnail");
		panel.mouseHoverOnThumbnail(1);
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Verify jump to icon is visible on thumbnail", "Verified that after mouse hover on thumbnail,jump icon is visible");
		panel.assertEquals(panel.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump icon", "Verified jump to icon visibility on mouse hovering on thumbnail ");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/5]","Verify jump icon visibility on mouse hovering on jump icon");
		panel.mouseHoverOnJumpIcon(1);
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Verify jump to icon is visible on thumbnail", "Verified that jump icon visible on mouse hovering on icon");
		panel.assertEquals(panel.getCssValue(panel.jumpToFindingIcon.get(0), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Verify visibility of jump to icon","Verified jump to icon visibility on mouse hovering on jump to icon");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/5]","Verify jump to icon not highlighted on thumbnail");
		panel.enableFiltersInOutputPanel(true, true, false);
		panel.assertFalse(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Verify jump to icon is not visible on thumbnail", "Verified that jump icon not highlighted when user moves the mouse away from thumbnail");

	}

	//US952
	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive" })
	public void test27_US952_TC4221_VerifyThumbnailForUserDrawnAnnotation()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail on 2D data for annotations drawn by user and annotations slice is opened.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		
		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -20, 80, 90);

		//Open output panel
		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify on clicking the 'Thumbnail' ,focus jump to that slice corresponding to that thumbnail in active viewbox for user drawn annotaion");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}		

	//DE1084 :DE1084	Banner does not appear again after closing in output panel
	@Test(groups = { "Chrome", "IE11", "Edge", "US952","DE0184","Negative" })
	public void test28_US952_DE1084_TC4222_TC4224_VerifyWarningMessageForThumbnail()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify warning message when thumbnail corresponding slice is not open in active view for 2D data <br>"+
				"Verify the banner appears everytime when a thumbnail image which is not displayed in the viewer is clicked"
				+ "<br> Verify warning message closed once click on message and close Icon");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);

		circle = new CircleAnnotation(driver);

		//Drwan circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -70, -30, -30,-40);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[1/4]", "Verify that drawn annotaion is selected");
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Verifying the circle is selected", "Circle is selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint:[2/4]", "Verify new series loaded on viewbox-1 ");
		contentSelector.selectSeriesFromSeriesTab(1, FifthSeriesDescription);
		panel.waitForAllChangesToLoad();
		panel.assertTrue(contentSelector.verifyPresenceOfEyeIcon(FifthSeriesDescription), "Verify 5th series selected from content selector ", "Verified 5th series is displyed on viewbox-1");

		// click on output panel
		panel.enableFiltersInOutputPanel(true, false, false);

		//click on first thumbnail image from output panel
		panel.clickOnJumpIcon(1);

		// verify warning message for thumbnail
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+ FirstSeriesDescription +"\" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");

		panel.click(panel.notificationUI);
		panel.waitForTimePeriod(1000);
		panel.assertFalse(panel.isElementPresent(panel.notificationDiv), "Verify warning message closed once click on message", "Warning message closed after click on that message");

		//again click on first thumbnail image from output panel
		panel.clickOnJumpIcon(1);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify warning messsage appears everytime if thumbnail corresponding slice is not open in active view");
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+ FirstSeriesDescription +"\" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message appears again if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Negative" })
	public void test29_US952_TC4223_VerifyFocusWhenSliceNotOpenOnViewboxButSeriesIsLoadedFor2DData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail when slice is not loaded on any viewbox but series is loaded for 2D data");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		circle = new CircleAnnotation(driver);
		
		//Draw circle annotation on viewbox-1
		//Slice number on which annotaion is drawn
		String defaultImageNumber=panel.getCurrentScrollPosition(1);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -200, -50, -100,-100);	

		//change slice using scroll
		panel.scrollToImage(1, 20);
		//viewerPage.scrollUpToSliceUsingKeyboard(1, 4);
		String changeImageNumber=panel.getCurrentScrollPosition(1);
		panel.assertNotEquals(changeImageNumber, defaultImageNumber, "Verify slice position change or not", "Slice position changed successfully to :"+ changeImageNumber );

		//Open output panel and click on thumbnail 
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		//Verify viewbox-1 is active and drawn annotation is selected
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify drawn annotaion is currentActiveGSPS on viewbox-1", "Drawn annoation is currentActiveGSPS on viewbox-1 is verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify on clicking the 'Thumbnail',focus jump on the slice belonging to that annotation");
		String CaptureImageNumber=panel.getCurrentScrollPosition(1);
		panel.assertEquals(CaptureImageNumber, defaultImageNumber, "Verify focus jump to default slice position", "On clicking thumbnail,Focus jump to slice belonging to that annotation : "+ CaptureImageNumber );
		panel.assertNotEquals(CaptureImageNumber, changeImageNumber, "Verify focus jump to default slice position", "Verified the focus jump to slice belonging to that annotation");
	}

	
	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive","Sanity" })
	public void test31_US952_TC4240_VerifyThumbnailForUserDrawnAnnotationOn4DData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail on 4D data for annotations drawn by user and annotations slice is opened.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		
		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(3);
		lineWithUnit.drawLine(3, -50, -20, 80, 90);

		//Open output panel
		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify on clicking the 'Thumbnail' ,focus jump to that slice corresponding to that thumbnail in active viewbox for user drawn annotaion");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(3, 1), "Verifying focus jump to finding when thumbnail is selected in the output panel", "Focus jump to finding when thumbnail is selected in the output panel for user drawn annotation");

	}

	//DE1084 :DE1084	Banner does not appear again after closing in output panel
	@Test(groups = { "Chrome", "IE11", "Edge", "US952","DE0184","DE1874","Positive" })
	public void test32_US952_DE1084_TC4241_DE1874_TC7504_VerifyFocusWhenSliceNotOpenInActiveViewFor4DData()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail when slice is not loaded on any viewbox for 4D data <br>"+
				"Verify the banner appears everytime when a thumbnail image which is not displayed in the viewer is clicked. <br>"
				+"[Risk and Impact]: Verify the output panel thumbnail images are loaded, for series not loaded in viewer page");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();
		contentSelector = new ContentSelector(driver);

		circle = new CircleAnnotation(driver);
		
		//Draw circle annotation on viewbox-1
		circle.selectCircleFromQuickToolbar(3);
		circle.drawCircle(3, -20, -50, -70,-70);	

		//change series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Select new series from Content selector");
		contentSelector.selectSeriesFromSeriesTab(3, SeriesToSelect_ADC2);
		panel.assertTrue(contentSelector.verifyPresenceOfEyeIcon(SeriesToSelect_ADC2),"Verify new series is selected","Verified new series is selected on viewbox-3");

		//Open output panel and click on thumbnail 
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/4]","Verified text of annotation on output panel");
//		panel.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));	
		panel.clickOnJumpIcon(1);

		//Verify viewbox-3 is active and drawn annotation is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+SeriesToSelect_ADC1+"\" " + ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");

		panel.click(panel.notificationUI);
		panel.waitForTimePeriod(1000);
		panel.assertFalse(panel.isElementPresent(panel.notificationDiv), "Verify warning message closed once click on message", "Warning message closed after click on that message");

		//again click on first thumbnail image from output panel
		panel.clickOnJumpIcon(1);		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify warning messsage appears everytime if thumbnail corresponding slice is not open in active view");
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+ SeriesToSelect_ADC1 +"\" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message appears again if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US952", "Positive","BVT"})
	public void test33_US952_TC4242_VerifyFocusWhenSliceNotOpenOnViewboxButSeriesIsLoadedFor4DData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of thumbnail when slice is not loaded on any viewbox but series is loaded for 4D data");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ADC_philips_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();

		circle = new CircleAnnotation(driver);
		
		//Draw circle annotation on viewbox-1
		//Slice number on which annotaion is drawn
		String defaultImageNumber=panel.getCurrentScrollPosition(3);
		circle.selectCircleFromQuickToolbar(3);
		circle.drawCircle(3, -200, -50, -100,-100);	

		panel.selectScrollFromQuickToolbar(panel.getViewPort(3));
		//change slice using scroll
		panel.scrollUpToSliceUsingKeyboard(3, 4);

		//Open output panel and click on thumbnail 
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verified text of annotation on output panel");
//		panel.assertTrue(panel.getTextOfFindingFromOutputPanel(1).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Verify name of first finding on Output Panel", "The name of first finding is: "+panel.getTextOfFindingFromOutputPanel(1));	
		panel.clickOnJumpIcon(1);

		//Verify viewbox-3 is active and drawn annotation is selected
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verified drawn annotation is currentActiveGSPS on viewbox-3 ");
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(3, 1), "Verify drawn annotaion is currentActiveGSPS on viewbox-3", "Drawn annoation is currentActiveGSPS on viewbox-3 is verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify on clicking the 'Thumbnail',focus jump on the slice belonging to that annotation");
		String CaptureImageNumber=panel.getCurrentScrollPosition(3);
		panel.assertEquals(CaptureImageNumber, defaultImageNumber, "Verify focus jump to default slice position", "On clicking thumbnail,Focus jump to slice belonging to that annotation : "+ CaptureImageNumber );

	}

	//US934 Make output panel content responsive
	@Test(groups = { "Chrome", "IE11", "Edge", "US934", "Positive" })
	public void test34_US934_TC4279_TC4283_TC4280_VerifyResponsivenessOfOutputPanel()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify responsiveness of output panel"+"<br> Verify tooltip is displayed on text in output panel when browser window is resized and text becomes very small"
				+"<br> Verify auto-zooming feature of thumbnail");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		ellipse = new EllipseAnnotation(driver);
		line = new  SimpleLine(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 150, 150);

		panel.closeWaterMarkIcon(1);
		panel.scrollToImage(1, 17);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);
		panel.selectRejectfromGSPSRadialMenu(ellipse.getAllEllipses(1).get(0));

		panel.closeWaterMarkIcon(1);
		panel.scrollToImage(1, 18);
		line.selectLineFromQuickToolbar(panel.getViewPort(1));
		line.drawLine(1,50,0,150,0);

		panel.closeWaterMarkIcon(1);
		panel.scrollToImage(1, 19);
		poly.selectPolylineFromQuickToolbar(1);
		int[] coordinates = new int[] {0,0,-90,-90,90,-90,90,90,-90,90};
		poly.drawClosedPolyLine(1, coordinates);

		panel.closeWaterMarkIcon(1);
		panel.scrollToImage(1, 20);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};	
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		panel.selectRejectfromGSPSRadialMenu();

		panel.closeWaterMarkIcon(2);
		panel.mouseHover(panel.getViewPort(2));
		panel.scrollToImage(2, 16);
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 0, 0, -100,-100);	
		panel.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(2).get(0));
		circle.addResultComment(circle.getAllCircles(2).get(0),circleComment);

		//open output panel and click on pending button
		panel.enableFiltersInOutputPanel(true, true, true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify thumbnail display annotation in center so that complete annotation is always visible");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Verify complete annotation is always visible on thumbnail", "test34_TC4283_checkpoint1");

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Resize should not squish the thumbnail and it should be resized in proportion to original aspect ratio."+
//				"Thumbnail boarders should always be visible and should not get cut while resizing the window.");
//		panel.resizeBrowserWindow(800, 800);
//
//	//	viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Verify Resize should not squish the thumbnail", "test34_TC4279_checkpoint2");
//
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify ellipses display due to space constraints");
//		textOverlay=new ViewerTextOverlays(driver);
//		panel.assertFalse(textOverlay.isToolTipPresentForTextOveraly(panel.findingsNameList.get(3)),"Verify ellipses after resize of window", "Verify ellipses display due to space constraints when browser window resize");


	}

	//DE1044 Tooltip is displayed as "Layer 1" when user mouse hover on thumbnails present on output panel
	@Test(groups = { "Chrome", "IE11", "Edge", "US934", "Positive" })
	public void test35_US934_DE1044_TC4481_TC4480_TC4281_VerifyTooltipAndJumpToImageIconEnable()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltip is not displayed on hovering over jump/return to image icon in thumbnail"+
		"<br> Verify return/jump to image icon is always displayed above DICOM image and annotations in the thumbnail."+
				"<br> Verify background of output panel");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	
		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);	

		//open output panel
		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify tooltip on mouse hover on jump icon.");
		panel.mouseHoverOnJumpIcon(1);
		panel.assertTrue((panel.getText(panel.jumpToFindingIcon.get(0)).contains("")), "Verify no tooltip display when user mouse hover on jump o finding icon","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify jump icon displayed above DICOM image and annotations in the thumbnail.");
		panel.assertTrue(viewerPage.verifyButtonEnabled(panel.jumpToFindingIcon.get(0)), "Verify jump to image icon is enable","Verified");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US934", "Positive","Sanity" })
	public void test37_US934_TC4484_VerifyPolylineLocationWhenRejectButtonPressedTwise()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify polyline location does not change when Reject button is pressed twice");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Liver9_Patient, 1);
	

		panel=new OutputPanel(driver);
		panel.waitForViewerpageToLoad();
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		circle = new CircleAnnotation(driver);

		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);

		panel.scrollToImage(1, 20);
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);

		//Draw a Circle on View Box1
		panel.closingConflictMsg();
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 0, 0, -100,-100);	
		circle.selectCircleWithClick(2, 1);
		panel.selectRejectfromGSPSRadialMenu();

		//open output panel and click on pending button
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint:[1/3]", "Verify output panel shows only 2 polyline in accepted state");

		panel.enableFiltersInOutputPanel(true, true, false);
		panel.assertEquals(panel.thumbnailList.size(),3,"Checkpoint:[2/3]", "Verify output panel shows circle in rejected state along with 2 polyline in accepted state");

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint:[3/3]", "Verify output panel shows only 2 polyline in accepted state when again click on reject button");

	}

	//US1062: Add support for displaying Jpeg and PNG in Output panel

	@Test(groups = { "Chrome", "IE11", "Edge", "US1062","US1159","DE1280", "Positive" })
	public void test38_US1062_TC5034_TC5035_US1159_TC5793_DE1280_TC5236_VerifyDetailInOutputPanelForJpegAndPng()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to see JPG / PNG images in Output panel thumbnail   <br>"+
				"Verify images count , created by, created on details in text part of output panel");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(anonymous_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad(2);

		contentSelector=new ContentSelector(driver);
		panel=new OutputPanel(driver) ;

		String Result1=contentSelector.getAllResults().get(0);
		String Result2=contentSelector.getAllResults().get(1);

		contentSelector.openAndCloseSeriesTab(true);
		String Result1UserName=contentSelector.getText(contentSelector.allMachineName.get(0));
		String Result2UserName=contentSelector.getText(contentSelector.allMachineName.get(1));

		
		String Result1Date=contentSelector.getText(contentSelector.allSeriesCreationDateFromSeriesTab.get(0));
	
		DateFormat dateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		Date date = new Date(Result1Date);

		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify thumbnail for jpeg and png image format");
		
		for(int i =0 ;i<panel.thumbnailList.size();i++) {
				panel.scrollIntoView(panel.thumbnailList.get(i));
				panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint:[1."+(i+1)+"/3]", "Verifying thumbnail for png and jpeg finding "+(i+1));
		}
		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify images count , created by, created on details in text part of output panel");
//		for(int i =0 ;i<panel.thumbnailList.size();i++) 
//		{
//			panel.assertEquals(panel.createdOnDateList.get(i).getText(),ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[2.a]", "Verifying the created on for finding"+i);
//			panel.assertEquals(panel.numberOfImages.get(i).getText(), ViewerPageConstants.COUNT_OF_IMAGES+" "+ViewerPageConstants.SCROLLBAR_WIDTH.replaceAll("px",""), "Checkpoint[2.b]", "Verifying the number of images for finding"+(i+1));
//		}
//		panel.assertEquals(panel.createdByUserList.get(0).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Result1UserName, "Checkpoint[2.c]", "Verifying the created by for finding png format");
//		panel.assertEquals(panel.createdByUserList.get(1).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Result2UserName, "Checkpoint[2.d]", "Verifying the created by for finding jpeg format");
//		panel.assertEquals(panel.resultName.get(0).getText(), ViewerPageConstants.RESULT_NAME+" "+Result1+ ViewerPageConstants.PNG_EXTENSION,"Checkpoint[2.e]", "Verifying result name for png format");
//		panel.assertEquals(panel.resultName.get(1).getText(), ViewerPageConstants.RESULT_NAME+" "+Result2+ ViewerPageConstants.JPEG_EXTENSION ,"Checkpoint[2.f]", "Verifying result name for jpeg format");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1062","DE1280", "Positive" })
	public void test39_US1062_TC5077_DE1280_TC5236_VerifyDisplayInOutputPanelWhenNoBinarySelectorForJpegAndPng()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if  PNG / JPEG images doesnot contain binary selector than it should not get display in output panel");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);
	
		panel=new OutputPanel(driver);
		panel.waitForViewerpageToLoad(2);
		
		panel.enableFiltersInOutputPanel(true,true,true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify output panel when jpeg/png images doesnot contain binary selector");
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Verify thumbnail count in output panel is zero", "Verified");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1062","DE1280", "Positive" })
	public void test40_US1062_TC5092_DE1280_TC5236_VerifyDicomNonDicomImagesInOutputPanel()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify dicom / non dicom images should get display in output panel when loaded or not loaded into viewer");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(boneAge_Patient, 1);
	
		contentSelector=new ContentSelector(driver);
		panel=new OutputPanel(driver);

		ellipse = new EllipseAnnotation(driver);
		int ResultCount=contentSelector.getAllResults().size();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/7]","Verify thumbnail for non dicom bonage images in output panel");
		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertEquals(panel.thumbnailList.size(),ResultCount, "Verify thumbnail count in output panel when no annotation is drawn", "Verified count of thumbnail as 6");
		for(int j=0;j<panel.thumbnailList.size();j++){
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(j+1), "Checkpoint[1."+(j+1)+"]", "Verified thumbnail images for non-dicom findings "+(j+1));
		}
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/7]","Verify dicom image displayed in thumbnail of output panel under Accepted filter");
		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 0, 0, -100,-100);
		panel.enableFiltersInOutputPanel(true,false,false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Verify thumbnail count in output panel is two", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/7]","Verify 1x1 layout and Dicom image with annotation after reload of viewer page");
		helper.browserBackAndReloadViewer(boneAge_Patient, 1, 1);
		panel.assertEquals(panel.getNumberOfCanvasForLayout(),1, "Verify number of canvas after reload of viewer page is 1*1", "Verified");

		int ResultCountafterDrawingAnno=contentSelector.getAllResults().size();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/7]","Verify thumbnails count and thumbnail images for both dicom/non-dicom in output panel when layout is 1*1");
		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertEquals(panel.thumbnailList.size(),ResultCountafterDrawingAnno, "Verify thumbnail count in output panel is seven", "Verified");
		for(int i=0;i<panel.thumbnailList.size();i++){
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[4a."+(i+1)+"]", "Verified thumbnail images for non dicom findings "+(i+1));
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/7]","Verify layout change to 3*3 and non-dicom image loaded in all viewboxes");
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
//		for (int k=1;k<=panel.getNumberOfCanvasForLayout();k++)
//		{
//			contentSelector.selectResultFromSeriesTabWithMachineName(k, patient_BoneAge1_Result1, ViewerPageConstants.BONEAGE_MACHINE_NAME);
//		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[6/7]","Verify thumbnails count and thumbnail images for both dicom/non-dicom in output panel when layout is 3*3");
		panel.enableFiltersInOutputPanel(true,true,true);
	
		panel.assertEquals(panel.thumbnailList.size(),ResultCountafterDrawingAnno, "Verify thumbnail count in output panel is seven", "Verified");
		for(int l=0;l<panel.thumbnailList.size();l++){
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(l+1), "Checkpoint[4a."+(l+1)+"]", "Verified thumbnail images for non-dicom findings "+(l+1));
		}
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[7/7]","Verify thumbnails count and thumbnail images for both dicom/non-dicom in output panel when layout is 1*1");
		panel.doubleClick(panel.getViewPort(1));
		panel.assertEquals(panel.getNumberOfCanvasForLayout() ,1, "Verify number of canvas when double click on viewbox", "Verified");
		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertEquals(panel.thumbnailList.size() ,ResultCountafterDrawingAnno, "Verify thumbnail count in output panel is seven when one up done", "Verified");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1062","DE1280","US1411", "Negative" })
	public void test41_US1062_TC5036_DE1280_TC5236_US1411_TC7760_VerifyFiltersInOutputPanelForNonDicomImages()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify accepted / rejected / pending filters of output panel for non dicom images.<br>"+
		"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(boneAge_Patient, 1);
	    findingMenu=new ViewerSliderAndFindingMenu(driver);
	    findingMenu.waitForViewerpageToLoad(4);

		panel=new OutputPanel(driver);

		panel.enableFiltersInOutputPanel(true, false, false);
		int AcceptedThumbailBefore=0;
		int RejectedThumbailBefore=3;
		int PendingThumbailBefore=3;

		//count of thumbnail for Accepted/Rejected/Pending filters in output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/6]","Verify tumbnail count seen for Accepted/Rejected/Pending filters ");
		panel.assertEquals(panel.thumbnailList.size(),AcceptedThumbailBefore , "Verify thumbnail count in Accepted filter" , "Verified");

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(),RejectedThumbailBefore , "Verify thumbnail count in Rejected filter" , "Verified");

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(),PendingThumbailBefore , "Verify thumbnail count in Pending filter" , "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/6]","Verify images count , created by, created on details in text part of output panel");
		panel.enableFiltersInOutputPanel(true, true, true);
//		for(int i =0 ;i<panel.thumbnailList.size();i++) 
//		{
//			findingMenu.scrollIntoView(panel.findingsList.get(i));
//			//viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Configurations.TEST_PROPERTIES.get("nsUserName"), "Checkpoint[2.a]", "Verifying the created by for finding"+(i+1));
//			findingMenu.assertEquals(panel.createdOnDateList.get(i).getText(), ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[2.b]", "Verifying the created on for finding"+i);
//			findingMenu.assertEquals(panel.numberOfImages.get(i).getText(), ViewerPageConstants.COUNT_OF_IMAGES+" "+sliceNumber, "Checkpoint[2.c]", "Verifying the number of images for finding"+(i+1));
//			findingMenu.assertTrue(panel.resultName.get(i).getText().contains(ViewerPageConstants.JPEG_EXTENSION),"Checkpoint[2.e]", "Verifying extension for result name is in jpeg");
//		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Accept atlas-1 series from boneage-1 (binary A/R toolbar) in viewbox1 and validate");
		findingMenu.mouseHover(viewerPage.getViewPort(1));
		findingMenu.acceptResult(1);
		findingMenu.assertTrue(findingMenu.verifyResultsAreAccepted(1),"Verify that result is accepted","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/6]","Verify thumbnail count seen for Accepted after accepting non-dicom image for atlas-1 series from boneage-1");
		panel.assertNotEquals(panel.thumbnailList.size(),AcceptedThumbailBefore, "Verify thumbnail count in Accepted filter after accepting non-dicom image" , "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Reject atlas-1 series from boneage-1 (binary A/R toolbar) in viewbox1 and validate");
		findingMenu.mouseHover(findingMenu.getViewPort(1));
		panel.rejectResult(1);
		panel.assertTrue(panel.verifyResultsAreRejected(1),"Verify that result is rejected","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[6/6]","Verify thumbnail count seen after rejecting non-dicom image for atlas-1 series from boneage-1");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),AcceptedThumbailBefore, "Verify thumbnail count in Accepted filter after rejecting non-dicom image" , "Verified");

		panel.enableFiltersInOutputPanel(false, true,  false);
		panel.assertEquals(panel.thumbnailList.size(),RejectedThumbailBefore+1, "Verify thumbnail count in Rejected filter after rejecting non-dicom image" , "Verified");

		panel.enableFiltersInOutputPanel(false,  false, true);
		panel.assertEquals(panel.thumbnailList.size(),PendingThumbailBefore-1 , "Verify thumbnail count in Pending filter" , "Verified");

		//state change to pending for for atlas-1 series from boneage-1
		findingMenu.mouseHover(findingMenu.getViewPort(1));
		panel.rejectResult(1);
	}

	//US1063: Add support for displaying PDF in Output panel

	@Test(groups = { "Chrome", "IE11", "Edge", "US1063", "DE1280","Positive" })
	public void test42_US1063_TC5021_TC5022_TC5025_DE1280_TC5237_VerifyDetailInOutputPanelForPDFData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that PDF is visible in Outpanel.<br>"
				+ "Verify that  scroll bar is not visible in thumbnail for pdf <br>"+"Verify that page number is appearing on output panel");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(IBLJohnDoe_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(3);
		contentSelector=new ContentSelector(driver);
	
		String Result3=panel.getSeriesDescriptionOverlayText(3);
		panel.getViewPort(4);
		int imageNumber=panel.getCurrentScrollPositionOfViewbox(4);

		contentSelector.openAndCloseSeriesTab(true);
		String MachineName=contentSelector.getText(contentSelector.allMachineName.get(0));

		DateFormat dateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		Date date = new Date();

		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);

//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/5]","Verify images count , created by, created on details in text part of output panel for PDF");
//		panel.assertEquals(panel.createdByUserList.get(2).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+MachineName, "Checkpoint[1.a]", "Verifying the created by for PDF finding");
//		panel.assertEquals(panel.createdOnDateList.get(2).getText(), ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[1.b]", "Verifying the created on for  PDF finding");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/5]","Verify thumbnail for jpeg and pdf format");
	
		for(int i=0;i<panel.thumbnailList.size();i++){
			panel.scrollIntoView(panel.thumbnailList.get(0));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint:[2."+(i+1)+"/5]", "Verifying thumbnail for png and jpeg finding "+(i+1));
		
		}

//		panel.assertEquals(panel.numberOfPages.get(0).getText(), ViewerPageConstants.COUNT_OF_PAGES+" "+imageNumber, "Checkpoint:[3/5]", "Verifying the number of images for finding");
//		panel.assertEquals(panel.resultName.get(2).getText(), ViewerPageConstants.RESULT_NAME+" "+Result3+"."+ LoginPageConstants.USER_MANUAL_TYPE,"Checkpoint:[4/5]", "Verifying result name for pdf format");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[5/5]","Verify scrollbar in thumbnail for PDF ");
		panel.mouseHover(panel.thumbnailList.get(2));
		panel.assertFalse(panel.isElementPresent(panel.verticalScrollBarComponent.get(1)), "Verify scrollbar not present in thumbnail for PDF data", "Verified");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1063","DE1280","US1411", "Positive","BVT"})
	public void test43_US1063_TC5026_DE1280_TC5237_US1411_TC7760_VerifyAcceptRejectPendingFilterFunctionalityForPDF() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Accept/Rejected/Pending filter functionality for pdf.<br>"+
		"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(IBLJohnDoe_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForPdfToRenderInViewbox(3);

		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);
		int thumbnailCount=panel.thumbnailList.size();
		int sliceNumber=panel.getCurrentScrollPositionOfViewbox(4);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify state of PDF in output panel");
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Verify state of PDF is pending in Output panel" , "Verified");

		//change state from pending to accepted and verify in Output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify state of PDF in output panel after accepting the PDF ");
		panel.openAndCloseOutputPanel(false);
		panel.acceptResult(3);
		panel.click(panel.getViewPort(3));
		panel.verifyTrue(panel.verifyResultsAreAccepted(3), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");
		panel.enableFiltersInOutputPanel(false,false,false);
		panel.assertEquals(panel.thumbnailList.size(), sliceNumber, "Verify state of PDF is Accepted in Output panel" , "Verified");
		panel.enableFiltersInOutputPanel(false,false,true);
		panel.assertNotEquals(panel.thumbnailList.size(), thumbnailCount, "Verify state of PDF is not pending in Output panel" , "Verified");

		//change state from accepted to Rejected and verify in Output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify state of PDF in output panel after changing the state from Accpeted to Rejected");
		panel.openAndCloseOutputPanel(false);
		panel.rejectResult(3);
		panel.click(panel.getViewPort(3));
		panel.verifyTrue(panel.verifyResultsAreRejected(3), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");
		panel.enableFiltersInOutputPanel(false,true,false);
		panel.assertEquals(panel.thumbnailList.size(), sliceNumber, "Verify state of PDF is Rejected in Output panel" , "Verified");
		panel.enableFiltersInOutputPanel(false,false,true);
		panel.assertNotEquals(panel.thumbnailList.size(), thumbnailCount, "Verify state of PDF is not accepted in Output panel" , "Verified");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1063","DE1280", "Positive" })
	public void test44_US1063_TC5033_DE1280_TC5237_VerifyPDFWhenBinarySelectorNotPresent()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of pdf in output panel which is in result finding but not having binary selector tool bar");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ah4_pdf_patientName, 1);
	
		viewerPage.waitForPdfToRenderInViewbox(3);

		panel=new OutputPanel(driver) ;
		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/1]","Verify state of PDF in output panel");
		viewerPage.assertTrue(panel.thumbnailList.isEmpty(), "Verify thumbnail list is empty in output panel" , "Verified");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1063","DE1280","US1159","Positive" })
	public void test45_US1063_TC5066_DE1280_TC5237_US1159_TC5793_VerifyPDFOnResizingTheBrowser()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("verify that on resizing the browser pdf is getting adjuseted in thumbnail.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(IBLJohnDoe_Patient, 1);
	
		viewerPage.waitForPdfToRenderInViewbox(3);

		panel=new OutputPanel(driver) ;
		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify state of PDF in output panel");
		viewerPage.assertEquals(panel.thumbnailList.size(),3,"Verify thumbnail list in output panel" , "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify PDF on resizing the browser");
		viewerPage.resizeBrowserWindow(800, 500);
		//panel.enableFiltersInOutputPanel(false,false,true);
		for (int i = 0; i < panel.thumbnailList.size(); i++) {
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[3."+i+"/3]"," verifying the thumbnail for machine finding"+(i+1));

		}
		
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1077","US1411", "Positive" })
	public void test46_US1077_TC5425_TC5426_US1411_TC7760_VerifyOutPutPanelForDicomSCData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that DICOM SC is visible in Outpanel.<br>"
				+ "Verify that  Dicom SC is visible in OutPut panal as per the state in respective filter.<br>"
				+"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Imbio_Texture_patientName, 1);
	
		panel=new OutputPanel(driver) ;
		contentSelector=new ContentSelector(driver);
		String Result1=panel.getSeriesDescriptionOverlayText(1);

		contentSelector.openAndCloseSeriesTab(true);
		String MachineName=contentSelector.getText(contentSelector.allMachineName.get(0));

		DateFormat dateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		Date date = new Date();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/6]","Verify thumbnail for DICOM SC format");

		panel.enableFiltersInOutputPanel(false,false,true);
		for(int i=0;i<panel.thumbnailList.size();i++){
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i), "Checkpoint[3."+(i+1)+"/6]", "Verified thumbnail image for non-dicom data finding "+(i+1));
		}

		//TC5426, verifying the DICOM SC in output panel filter as per the DICOM SC state.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest," ","Verifying that DICOM SC is visible in the respective filter as per it's state.");
		panel.openAndCloseOutputPanel(false);
		panel.acceptResult(1);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),1,"","");		
		panel.openAndCloseOutputPanel(false);

		panel.rejectResult(1);
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),1,"","");		
		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1077", "Positive" })
	public void test47_US1077_TC5428_TC5429_VerifyOutPutPanelForDicomSCDataWithGsps() throws Exception {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that DICOM SC is visible in Outpanel along with the GSPS changed annotation.<br>"
				+ "Verify that changed state Dicom SC is visible in OutPut panal with GSPS fixed state");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Imbio_Texture_patientName, 1);
	
		panel=new OutputPanel(driver) ;
		contentSelector=new ContentSelector(driver);
		panel.waitForViewerpageToLoad();
		circle = new CircleAnnotation(driver);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -100,-100);	

		//open output panel 
		panel.enableFiltersInOutputPanel(false,false,true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/14]","Verifying DICOM SC is present in Pending filter");
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),2,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.clickOnJumpIcon(2);
		panel.assertEquals(panel.getActiveViewbox(),1,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		
		
		// Verifying that GSPS annotation is present in Accepted Filter
		panel.enableFiltersInOutputPanel(true,false,false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/14]","Verifying that GSPS findings is visible in the respective filter as per it's state.");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.openAndCloseOutputPanel(false);

		// Verifying that GSPS annotation is present in Rejected Filter
		panel.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		panel.enableFiltersInOutputPanel(false,true,false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/14]","Verifying that GSPS findings is visible in the respective filter as per it's state.");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.openAndCloseOutputPanel(false);

		panel.enableFiltersInOutputPanel(false,false,true);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),2,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.clickOnJumpIcon(2);
		panel.assertEquals(panel.getActiveViewbox(),1,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.openAndCloseOutputPanel(false);

		// Verifying that GSPS annotation is present in Pending Filter
		panel.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		panel.enableFiltersInOutputPanel(false,false,true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/14]","Verifying that GSPS findings is visible in the respective filter as per it's state.");
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),1,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.clickOnJumpIcon(2);
		panel.assertEquals(panel.getActiveViewbox(),2,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.clickOnJumpIcon(3);
		panel.assertEquals(panel.getActiveViewbox(),1,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		
		panel.openAndCloseOutputPanel(false);

		//Implementing TC5429

		//open output panel and Verifying that GSPS annotation is present in Accepted Filter+DICOM SC is in Pending filter
		panel.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		
		panel.enableFiltersInOutputPanel(false,false,true);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/14]","Verifying DICOM SC is present in Pending filter");
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),2,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		panel.clickOnJumpIcon(2);
		panel.assertEquals(panel.getActiveViewbox(),1,"Checkpoint[2/14]", "Verifying result name for DICOM SC format");
		
		panel.enableFiltersInOutputPanel(true,false,false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/14]","Verifying that GSPS findings is visible in the respective filter as per it's state.");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.openAndCloseOutputPanel(false);
		
		
		// Verifying that GSPS annotation+DICOM SC is present in Accepted Filter
		panel.click(panel.getViewPort(1));
		panel.selectFindingFromTable(1);
		panel.acceptResult(1);

		panel.enableFiltersInOutputPanel(true,false,false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[11/8]","Verifying that DICOM SC in accepted+GSPS Annotation is visible in the Accepted filter");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.clickOnJumpIcon(2);
		panel.assertEquals(panel.getActiveViewbox(),1, "", "Verifying that circle is also displayed");
		panel.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		
		panel.openAndCloseOutputPanel(false);

		// Verifying that GSPS annotation is present in Accepted Filter and DICOM SC is in Rejected filter
		panel.click(panel.getViewPort(1));
		panel.selectFindingFromTable(1);
		panel.rejectResult(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[13/14]","Verifying that DICOM SC is in Pending+GSPS Annotation is visible in the Accepted filter.");
		panel.enableFiltersInOutputPanel(true,false,false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/14]","Verifying that GSPS findings is visible in the respective filter as per it's state.");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		panel.enableFiltersInOutputPanel(false,true,false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getActiveViewbox(),1, "", "Verifying that circle is also displayed");
		panel.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "", "Verifying that circle is also displayed");
		
		panel.openAndCloseOutputPanel(false);

		
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1077", "Positive" })
	public void test48_US1077_TC5430_TC5431_TC5451_VerifyOutPutPanelForDicomSCDataWithGsps()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the DICOM SC in outpanel when Same copy loaded from content selector in another view box and one is Accepted and One is Rejected.<br>"
				+ "Verify Auto zooming on thumbnail.<br>" + "Verify the browser responsivness");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(Imbio_Texture_patientName, 1);
	

		panel=new OutputPanel(driver) ;
		contentSelector=new ContentSelector(driver);
		panel.waitForViewerpageToLoad();
		circle = new CircleAnnotation(driver);

		//Implementing TC5430

		panel.acceptResult(1);
		panel.enableFiltersInOutputPanel(true,false,false);
		panel.openAndCloseOutputPanel(false);

		String Imbio_Texture_DicomSCResult= DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, Imbio_Texture_FilePath);
		contentSelector.selectResultFromSeriesTab(3, Imbio_Texture_DicomSCResult);

		panel.mouseHover(panel.getAcceptRejectToolBar(3));
		panel.click(panel.gspsReject);

		//open output panel and Verifying
		panel.enableFiltersInOutputPanel(false,true,false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verifying the latest state of DICOM SC in output panel, when same series selected in 2nd view box and state is changed");
		viewerPage.assertEquals(panel.thumbnailList.size(), 1,"Verifying that DICOM is in which filter", "Verified that DICOM SC is in Rejected Filter");

		//Implementing TC5430
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 0, 0, -150,-100);	

		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/4]","Verify that annotation should be in center of thumbnail.");

		for(int i=0;i<panel.thumbnailList.size();i++){
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint:[2."+(i+1)+"/4]", "Verifying thumbnail for png and jpeg finding "+(i+1));
		}

		// Implementing TC5431

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/4]","Verify DICOM on resizing the browser");
		panel.resizeBrowserWindow(800, 500);
		panel.scrollIntoView(panel.thumbnailList.get(0));
		panel.assertTrue(panel.verifyThumbnailInOutputPanel(1), "Checkpoint[4/4]"," verifying the thumbnail for machine finding 1.");

	}

	@Test(groups ={"IE11","Chrome","Edge","DE1280","positive"})
	public void test49_DE1280_TC5144_DE1374_TC5743_verifyThumbnailForFindingsInGroups() throws Exception     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Ouptut Panel  shows single thumbnail for findings in the group."
				+ "<br> Verify that the 'Jump-To' icon  is aligned to center of the thumbnail in the output panel for GSPS grouped objects.");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		panel=new OutputPanel(driver) ;
		circle= new CircleAnnotation(driver);
		//Load patient on viewer
		panel.waitForViewerpageToLoad();
		contentSelector=new ContentSelector(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnno = new TextAnnotation(driver);

		panel.openFindingTableOnBinarySelector(1);
		int groupCount=panel.groupInfo.size();
		int findings=panel.findings.size();

		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify count of group visible on A/R finding toolbar");
		panel.assertEquals(groupCount, 5, "Verify" +" "+ panel.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify count of findings visible on A/R finding toolbar");
		panel.assertEquals(findings, 2, "Verify" +" "+ panel.findings.size()+" "+"findings seen in finding toolbar", "Verified");

		//Verify warning Icon for the Groups

		panel.enableFiltersInOutputPanel(true, true, true);
		int thumbnailList=panel.thumbnailList.size();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify count of findings visible on A/R finding toolbar with thumbnail images seen in Output panel");
		panel.assertEquals(thumbnailList, groupCount+findings, "Verify count output panel" +" "+ thumbnailList+" "+"thumbnail seen in output panel", "Verified");

		panel.mouseHoverOnThumbnail(1);		
		panel.assertTrue(panel.isElementPresent(panel.jumpToFindingIcon.get(0)), "Checkpoint[4/8]", "Verified that after mouse hover on thumbnail,jump to icon is visible");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify count of findings visible on A/R finding toolbar with thumbnail images seen in Output panel");
		for(int i =0;i<thumbnailList;i++) {
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[6."+(i+1)+"/8]", "Verified thumbnail images for group findings "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)), "Checkpoint[7."+(i+1)+"/8]", "Verified annotation within thumbnail "+(i+1));
		}

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify jump icon functionality for the group");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(textAnno.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "On clicking the thumbnail slice where the finding is present should be loaded and the finding should be highlighted", "Verified");

	}

	@Test(groups ={"IE11","Chrome","Edge","DE1280","positive"})
	public void test50_DE1280_TC5285_verifyClickingOnThumbnailForGSPS() throws Exception     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify clicking of Thumbnail for GSPS in the outputpanel");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatient, 1);
	

		panel=new OutputPanel(driver) ;
		circle= new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		//Load patient on viewer
		panel.waitForViewerpageToLoad();
		contentSelector=new ContentSelector(driver);		
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnno = new TextAnnotation(driver);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify jump icon functionality for the group");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "On clicking the thumbnail slice where the finding is present should be loaded and the finding should be highlighted", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify layout after one up on any of series loaded in viewer other than the one which the annotation was drawn");
		panel.doubleClick(panel.getViewPort(3));
		panel.waitForTimePeriod(1000);
		panel.assertEquals(panel.getNumberOfCanvasForLayout(), 1, "Verify the series is loaded in 1X1 layout.", "Verified");

		panel.enableFiltersInOutputPanel(true, false, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify list of thumbnail seen in output panel");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Verify thumbnail seen in output panel after one up is"+panel.thumbnailList.size(), "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/4]","Verify warning messsage if thumbnail corresponding slice is not open in active view");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+resultDescriptionForImbio+"\""+" "+ ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Verify warning message if thumbnail corresponding slice is not open", "Verified warning message when thumbnail corresponding slice is not open in active view ");		
	}

	@Test(groups ={"IE11","Chrome","Edge","DE1280","positive"})
	public void test51_DE1280_TC5241_verifyUnformattedTextInOutputPanel() throws Exception     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify display of Unformatted text in the GSPS findings in Output Panel");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);
	

		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad(1);
		circle= new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		//Load patient on viewer
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnno = new TextAnnotation(driver);
		poly = new PolyLineAnnotation(driver);
		DatabaseMethods db=new DatabaseMethods(driver);

		String value="00000000-0000-0000-0000-000000000000";
		//Load patient on viewer
		panel.waitForViewerpageToLoad();

		String myText1 ="TextAnnotation_First";
		String myText2 ="TextAnnotation_Second";
		String myText3 ="TextAnnotation_Third";
		String myText4 ="TextAnnotation_Fourth";
		String myText5 ="TextAnnotation_Fifth";
		String myText6 ="TextAnnotation_Sixth";

		//select ellipse from radial menu and draw a ellipse and to Group1
		panel.click(panel.getViewPort(1));
		panel.scrollToImage(1, 10);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//add 4 groups for patient and update derived ID for the same
		for(int i=1;i<=4;i++)
		{
			db.addGroupsInDB(AH4_PatientID, i, "Group_"+i);
			db.updateDerivedIDInDB(groupName+i, value);
		}
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,1);

		//On slice 12 draw second ellipse and add to Group 2
		panel.scrollToImage(1, 12);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,2);

		//On slice 15 draw ellipse,circle and point and add to Group3 
		panel.scrollToImage(1, 15);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,3);

		//draw unformatted text and add to group1
		panel.scrollToImage(1, 10);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText2);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,1);

		//draw unformatted text and add to group2
		panel.scrollToImage(1, 11);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,2);

		//draw 4 unformatted text and add to group4
		panel.scrollToImage(1, 17);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText3);
		textAnno.drawText(1, 50, 50, myText4);
		textAnno.drawText(1,25,25, myText5);
		textAnno.drawText(1,75,75, myText6);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,4);

		//after grouping logout from application and Login
		loginPage = new LoginPage(driver);
		loginPage.logout();
		loginPage.login(username,password);

		//navigate to viewer page and select layout
		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(AH4_Patient, 1);
		panel.mouseHover(panel.getViewPort(1));

		//verify group count from Finding menu toolbar
		panel.openFindingTableOnBinarySelector(1);
		int groupCount=panel.getGroupCount();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify count of group visible on A/R finding toolbar");
		panel.assertEquals(groupCount, 4, "Verify" +" "+ panel.getGroupCount()+" "+"groups seen in finding toolbar", "Verified");

		//open output panel and check count of thumbnail list
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();
		int thumbnailList=panel.thumbnailList.size();

		//verify count of findings in output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify count of findings visible on A/R finding toolbar with thumbnail images seen in Output panel");
		panel.assertEquals(thumbnailList, groupCount,"Checkpoint[3/9]", "Verify count output panel" +" "+ thumbnailList+" "+"thumbnail seen in output panel");

		//verify CreatedBy,Created date and number of groups and text annotation count
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify content of findings visible with thumbnail images in Output panel");
		for(int i =0;i<thumbnailList;i++) 
		{   
			panel.scrollIntoView(panel.thumbnailList.get(i));
//			panel.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Configurations.TEST_PROPERTIES.get("nsUserName"), "Checkpoint[5."+i+"/9]", "Verifying the created by");
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[6."+(i+1)+"/9]", "Verified thumbnail images for group findings "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)), "Checkpoint[7."+(i+1)+"/9]", "Verified annotation within thumbnail "+(i+1));
		
			
//			panel.assertEquals(panel.createdOnDateList.get(i).getText(), ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[8."+i+"/9]", "Verifying the created on");
			//viewerPage.assertEquals(panel.getText(panel.findingsNameList.get(i)),ViewerPageConstants.FINDING_NAME+": "+groupName+(i+1), "Checkpoint[7."+i+"]", "verifying the finding name");
			panel.assertEquals(panel.textAnnotationTextList.size(),6, "Checkpoint[9/9]", "verifying the finding name");
		}
	}

	@Test(groups ={"IE11","Chrome","Edge","DE1280","positive"})
	public void test52_DE1280_TC5242_verifyUnfomrattedTextWithDifferentAnnotationInOutputPanel() throws Exception{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify display of Unformatted text in the GSPS findings in Output Panel (when Unformatted text and another annotation are in the same slice in a group)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);
	
		panel=new OutputPanel(driver) ;
		circle= new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		point=new PointAnnotation(driver);

		//Load patient on viewer
		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad();
		lineWithUnit = new MeasurementWithUnit(driver);
		textAnno = new TextAnnotation(driver);
		poly = new PolyLineAnnotation(driver);
		DatabaseMethods db=new DatabaseMethods(driver);
		line = new  SimpleLine(driver);

		DateFormat dateFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		Date date = new Date();

		String value="00000000-0000-0000-0000-000000000000";
		//Load patient on viewer
		panel.waitForViewerpageToLoad();

		String myText1 ="TextAnnotation_First";
		String myText2 ="TextAnnotation_Second";
		String myText3 ="TextAnnotation_Third";
		String myText4 ="TextAnnotation_Fourth";

		//select ellipse from radial menu and draw a ellipse and to Group1
		panel.mouseHover(panel.getViewPort(1));
		panel.scrollToImage(1, 10);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//add 4 groups for patient and update derived ID for the same
		for(int i=1;i<=5;i++)
		{
			db.addGroupsInDB(AH4_PatientID, i, "Group_"+i);
			db.updateDerivedIDInDB(groupName+i, value);
		}
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,1);
		panel.scrollToImage(1, 10);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		textAnno.drawText(1, 50, 50, myText2);
		textAnno.drawText(1,25,25, myText3);
		textAnno.drawText(1,75,75, myText4);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,1);

		//draw line and multiple text annotation on slice 12 and create Group2
		panel.scrollToImage(1, 12);
		line.selectLineFromQuickToolbar(panel.getViewPort(1));
		line.drawLine(1,-10,10,250,10);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		textAnno.drawText(1, 50, 50, myText2);
		textAnno.drawText(1,25,25, myText3);
		textAnno.drawText(1,75,75, myText4);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,2);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,2);

		//On slice 14 draw second circle and multiple text annotation on slice 14 and create Group3
		panel.scrollToImage(1, 14);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		textAnno.drawText(1, 50, 50, myText2);
		textAnno.drawText(1,25,25, myText3);
		textAnno.drawText(1,75,75, myText4);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,3);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,3);

		//On slice 16 draw second circle and multiple text annotation on slice 16 and create Group4
		panel.scrollToImage(1, 16);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		textAnno.drawText(1, 50, 50, myText2);
		textAnno.drawText(1,25,25, myText3);
		textAnno.drawText(1,75,75, myText4);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,4);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,4);


		//On slice 20 draw ellipse,circle and point and add to Group6
		panel.scrollToImage(1, 20);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		db.updateGroupIDInGSPSGraphicObject(AH4_PatientID,5);
		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.drawText(1, 0, 0, myText1);
		textAnno.drawText(1, 50, 50, myText2);
		textAnno.drawText(1,25,25, myText3);
		textAnno.drawText(1,75,75, myText4);
		db.updateGroupIDInGSPSTextObject(AH4_PatientID,5);

		//after grouping logout from application and Login
		loginPage = new LoginPage(driver);
		loginPage.logout();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));

		//navigate to viewer page and select layout
		patientListPage.waitForPatientPageToLoad();
		patientListPage.clickOnPatientRow(AH4_Patient);
		patientListPage.clickOntheFirstStudy();
		viewerPage.waitForViewerpageToLoad();

		panel.mouseHover(panel.getViewPort(1));

		//verify group count from Finding menu toolbar
		panel.openFindingTableOnBinarySelector(1);
		int groupCount=panel.getGroupCount();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(groupCount, 5, "Verify" +" "+ panel.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		//open output panel and check count of thumbnail list
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();
		int thumbnailList=panel.thumbnailList.size();

		//verify count of findings in output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify count of findings visible on A/R finding toolbar with thumbnail images seen in Output panel");
		panel.assertEquals(thumbnailList, groupCount, "Verify count output panel" +" "+ thumbnailList+" "+"thumbnail seen in output panel", "Verified");

		//verify CreatedBy,Created date and number of groups and text annotation count
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify content of findings visible with thumbnail images in Output panel");
		for(int i =0;i<thumbnailList;i++) 
		{   
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.assertTrue(panel.verifyThumbnailInOutputPanel(i+1), "Checkpoint[4."+(i+1)+"/8]", "Verified thumbnail images for group findings "+(i+1));
			panel.assertTrue(panel.isElementPresent(panel.getThumbnailMeasurement(i)), "Checkpoint[5."+(i+1)+"/8]", "Verified annotation within thumbnail "+(i+1));
		
//			panel.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Configurations.TEST_PROPERTIES.get("nsUserName"), "Checkpoint[6."+i+"]", "Verifying the created by");
//		//	viewerPage.assertEquals(panel.createdOnDateList.get(i).getText(), ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[7."+i+"]", "Verifying the created on");
//			panel.assertTrue(panel.getText(panel.findingsNameList.get(i)).replaceAll(":","").trim().contains(groupName+(thumbnailList-i)), "Checkpoint[8."+i+"]", "verifying the finding name");

		}
		panel.assertEquals(panel.textAnnotationTextList.size(),18, "Checkpoint[9/]", "verifying the finding name");	
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1374", "Negative" })
	public void test53_DE1374_TC5742_VerifyJumpToIconIsInCenterForCornerGSPS()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the GSPS objects(grouped and non-grouped) are visible on the thumbnail which are drawn to the corners of the viewer.");

		// Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(AH4_Patient, 1);		

		lineWithUnit=new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Distance anotation drawn on slice 15
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 250, 220, -80, -90);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/4]","Verify linear measurement annotation is displaying on viewer page");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),2, "Checkpoint:[2/4]", "Linear measurement annotation in displaying as accepted on viewer page");		

		//Open output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verified that output panel open successfully.");
		panel.enableFiltersInOutputPanel(true, true, true);

		//mouse hover on thumbnail 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verify jump to icon visible on mouse hover on thumbnail");
		
		for(int i =1;i<=panel.thumbnailList.size();i++) {
			panel.mouseHoverOnThumbnail(i);		
			viewerPage.assertTrue(viewerPage.isElementPresent(panel.jumpToFindingIcon.get(i-1)), "Checkpoint[3/4]", "Verified that after mouse hover on thumbnail,jump to icon is visible");
	
		}
	}	

//	OBSOLETE
//	//DE1927: Output panel displays "Distance: no pixel spacing" for no measurement done
//	@Test(groups = { "Chrome","IE11","Edge","DE1927","Positive"})
//	public void test54_DE1927_TC7693_verifyForLineAnnotationPixelSpacingNotVisible() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify there is pixel spacing not displayed for Line GSPS in Output Panel.");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + Liver9_Patient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(Liver9_Patient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);	
//		panel.waitForViewerpageToLoad();
//		
//		lineWithUnit=new MeasurementWithUnit(driver);
//		line=new SimpleLine(driver);
//		line.selectLineFromQuickToolbar(panel.getViewPort(1));
//	    line.drawLine(1,-10,10,250,10);
//	    
//	    panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.getText(panel.findingsNameList.get(0)),ViewerPageConstants.FINDING_NAME+": "+ViewerPageConstants.DISTANCE_FINDING_NAME, "Checkpoint[1/5]", "Verified finding for the line annotation in Output panel.");
//		panel.assertTrue(panel.distanceInThumbnailDetails.isEmpty(), "Checkpoint[2/5]", "Verified distance label not visible in OP for line annotation.");
//		panel.openAndCloseOutputPanel(false);
//		
//
//		lineWithUnit.selectDistanceFromQuickToolbar(1);
//		lineWithUnit.drawLine(1, -50, -20, 80, 90);
//		lineWithUnit.selectRejectfromGSPSRadialMenu();
//        String distance=panel.getText(lineWithUnit.getLinearMeasurementsText(1).get(0));
//        
//        panel.enableFiltersInOutputPanel(false, true, false);
//		panel.assertEquals(panel.getText(panel.findingsNameList.get(0)),ViewerPageConstants.FINDING_NAME+": "+ViewerPageConstants.LINEAR_FINDING_NAME, "Checkpoint[3/5]", "Verified finding for the linear measurement annotation in Output panel.");
//		panel.assertTrue(panel.isElementPresent(panel.distanceInThumbnailDetails.get(0)),"Checkpoint[3/5]","Verified presence of distance label in Output panel for linear measurement.");
//		panel.assertTrue(panel.getText(panel.distanceInThumbnailDetails.get(0)).contains(distance),"Checkpoint[5/5]","Verified presence of distance value in Output panel for linear measurement.");
//	
//		panel.openAndCloseOutputPanel(false);
//		
//
//	}
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
	}


}



