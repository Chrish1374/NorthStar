package com.trn.ns.test.obsolete;
//package com.terarecon.northstar.test.obsolete;
//
//import java.awt.AWTException;
//import java.sql.SQLException;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.terarecon.northstar.page.factory.DatabaseMethods;
//import com.terarecon.northstar.page.factory.LoginPage;
//import com.terarecon.northstar.page.factory.NSConstants;
//import com.terarecon.northstar.page.factory.PatientListPage;
//import com.terarecon.northstar.page.factory.SinglePatientStudyPage;
//import com.terarecon.northstar.page.factory.ViewerPage;
//import com.terarecon.northstar.test.base.TestBase;
//import com.terarecon.northstar.test.configs.Configurations;
//import com.terarecon.northstar.utilities.DataReader;
//import com.terarecon.northstar.utilities.ExtentManager;
//
//@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
//public class AnnotationPersistsInDBTest extends TestBase{
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath); 
//
//	String filePath2=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath2);
//
//	String filePath3 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath3); 
//
//	String filePath4=Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneAge_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath4); 
//	
//	String filePath5=Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
//	String NorthStar_CT_Neck = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
//
//	String myText ="Automation_TextAnnotation_FirstViewbox";
//	private String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
//
//	
//	
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//
//	}
//
//
//	//TC1939 - 	Allow selection and action for user to trigger event to persist in NS DB - Persistence check for multi user system
//	//Limitation : Not working in edge because of mouseHover function
//	@Test(groups ={"firefox","Chrome","IE11","US501","Sanity"})
//	public void test04_US501_TC1939_verifyAnnotationPersistsOnReloadForMultiUser() throws AWTException, InterruptedException, SQLException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Allow selection and action for user to trigger event to persist in NS DB - Persistence check for multi user system");
//
//		loginPage = new LoginPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Login with user1" );
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );
//
//		//Getting series description for series in first viewbox
//		String firstGSPSSeriesDescription = viewerPage.getSeriesDescriptionOverlayText(1);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 2x2" );
//		viewerPage.selectLayout(viewerPage.twoByTwoLayoutIcon,true);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify the presence of GSPS data");
//		viewerPage.assertTrue(viewerPage.isPointPresent(1,1),"Verifying the point#1", "Point is present in first viewbox");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verifying presence of drawn GSPS objects");
////		drawPointRulerTextAnnotationMeasurement(1);
//
//		//Select same series in forth viewbox
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting first image in forth viewbox" );
//		viewerPage.selectResultFromContentSelectorForGivenSeries(4,"_scan_1",firstGSPSSeriesDescription);
//		viewerPage.inputImageNumber(9,4);
////		drawEllipseCircle(4);
//
//		loginPage.logout();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/10]", "Verifying that user log outs successfully");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verify that user logs out successfully", "User logs out");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
////		db.addUserInDB(NSConstants.NEW_USERNAME,NSConstants.NEW_USERNAME,NSConstants.NEW_PASSWORD,"","1900-01-01 00:00:00.000","","","","support@terarecon.com","1","","2017-05-01 16:22:53.380","dark","0");
//		db.addUserInDB(NSConstants.NEW_USERNAME,NSConstants.NEW_USERNAME, "uPqNaW7rurnkYzKBvLwKgYh99sn6O06W", "eLaUIr+A6NoNOXMEDZ+8ImpLnvY3kiKh", "1900-01-01 00:00:00.000","","","","support@terarecon.com","1","","2017-05-01 16:22:53.380","dark","0");
//
//		//Step 5
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Login with user2" );
//		loginPage.login(NSConstants.NEW_USERNAME, NSConstants.NEW_PASSWORD);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Verifying that other annotations should not present from new user login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/10]", "Verify that None of the additional elements drawn by user1 are seen by user 2 only Point should present");
//		viewerPage.assertTrue(viewerPage.isPointPresent(1,1),"verifying the point#1", "point is present in first viewbox");
//		viewerPage.assertFalse(viewerPage.isLinearMeasurementPresent(1),"verifying the linear measurenet is not present on Viewbox1", "Linear measurement is not present");
//		viewerPage.assertFalse(viewerPage.isTextAnnotationPresent(1),"verifying the text annotation is not present on Viewbox1", "Text annotation is not present");
//		viewerPage.assertFalse(viewerPage.isLinesPresent(1),"verifying the line is not present on Viewbox1", "Line is not present");
//
//		//Step 6
//		//Drawing new annotation on user2
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verify the presence of newly drawn GSPS objects on user2");
////		drawEllipseCircle(1);
//
//		//Clicking on reject button on GSPS radial menu
//		viewerPage.performMouseRightClick(viewerPage.getSelectedCircle(1));		
//		viewerPage.selectRejectfromGSPSRadialMenu(viewerPage.getCircles(1).get(0));
//		viewerPage.inputImageNumber(1, 1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify rejected status for circle annotation");
//		viewerPage.assertTrue(viewerPage.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Verifying that the circle annotation is rejected", "Circle annotation in displaying as rejected");		
//
//		//Step 7
//		//Again login with user1
//		loginPage.logout();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/10]", "Verifying that user log outs successfully");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verify that new user logs out successfully", "New user logs out");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Login with user1" );
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Verifying that other annotations should  present from user1 login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/10]", "Verify the presence of drawn GSPS objects on user1");
//		viewerPage.assertTrue(viewerPage.isLinearMeasurementPresent(1),"verifying the linear measurenet is present on Viewbox1", "Linear measurement is present");
//		viewerPage.assertTrue(viewerPage.isTextAnnotationPresent(1),"verifying the text annotation is present on Viewbox1", "Text annotation is present");
//
//		//viewerPage.assertTrue(viewerPage.isLinesPresent(1),"verifying the line is present on Viewbox1", "Line is present");
//		viewerPage.assertFalse(viewerPage.isEllipsePresent(1),"verifying the ellipse is not present on same slice", "Ellipse is not present");
//		viewerPage.assertFalse(viewerPage.isCirclePresent(1),"verifying the circle is not present on same slice", "Circle is not present");
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1,"Verify presence of drawn GSPS objects","TC1939_CheckPoint1");
//
//		viewerPage.inputImageNumber(9,1);
//		viewerPage.assertTrue(viewerPage.isEllipsePresent(1),"verifying the ellipse is present on slice : 9", "Ellipse is present");
//		viewerPage.assertTrue(viewerPage.isCirclePresent(1),"verifying the circle is present on slice : 9", "Circle is present");
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1,"Verify presence of drawn GSPS objects","TC1939_CheckPoint2");
//
//		//Step 8
//		//Again login with user2
//		loginPage.logout();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/10]", "Verifying that user log outs successfully");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains("login"), "Verify that new user logs out successfully", "New user logs out");
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Login with user2" );
//		loginPage.login(NSConstants.NEW_USERNAME, NSConstants.NEW_PASSWORD);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Verifying that other annotations should  present from user2 login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/10]", "Verify the presence of drawn GSPS objects on user2");
//		viewerPage.assertTrue(viewerPage.isEllipsePresent(1),"verifying the ellipse is present on slice", "Ellipse is present");
//		viewerPage.assertTrue(viewerPage.isCirclePresent(1),"verifying the circle is present on slice", "Circle is present");
//		viewerPage.assertTrue(viewerPage.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Verifying that the circle annotation is rejected", "Circle annotation in displaying as rejected");
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage1,"Verify presence of drawn GSPS objects","TC1939_CheckPoint3");
//
//
//	}
//	
//}
