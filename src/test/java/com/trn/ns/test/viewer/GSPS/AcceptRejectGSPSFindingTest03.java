package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectGSPSFindingTest03 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;	
	private SimpleLine line;

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String GSPS_PatientName_Aidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);
	String GSPS_Patient_Aidoc_Result1=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePathAidoc);
	String GSPS_Patient_Aidoc_Result2=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,filePathAidoc);

	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);

	String filePathBoneAge = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String patientName_BoneAge = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathBoneAge);
	String patient_BoneAge1_Result1 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,filePathBoneAge);
	String patient_BoneAge1_Result2 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY,filePathBoneAge);
	String patient_BoneAge1_Result3 =DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT03_TEXTOVERLAY,filePathBoneAge);

	String filePathPiccOne = Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
	String patientName_PiccOne = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathPiccOne);
	
	

	public final String ANNOTATION_TXT_1="ABC";
	public final String ANNOTATION_TXT_2="DEF";
	public final String ANNOTATION_TXT_3="JKL";
	public final String ANNOTATION_TXT_4="XYZ";
	public final String ANNOTATION_TXT_5="NewText";
	public final String ANNOTATION_TXT_6="Updated_fghi";
	private ContentSelector cs;
	private HelperClass helper;


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		
	}


	@Test(groups ={"Chrome","US680"})
	public void test21_US680_TC2765_TC2771_verifyNewGSPSRadialToolBar() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new Accept/Reject toolbar on data having machine generated GSPS annotation"
				+ "</br>Verify icon on new Accept/Reject radial bar");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
		
		point = new PointAnnotation(driver);

		//verify GSPS available on slice is already selected and it is in Pending state
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active pending result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current active pending GSPS object", "The first point annotation is current active pending GSPS object");
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");

		//navigate to slice having no GSPS object and verify Accept/Reject tool bar is not there
		viewerPage.scrollDownToSliceUsingKeyboard(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify Accept/Reject radial tool bar is not present on bottom of viewbox on slice with no GSPS object");
		viewerPage.assertFalse(point.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is not visible on bottom of screen", "The Accept/Reject tool bar is not visible on bottom of screen");

		//press left arrow key to move to next GSPS object in series
		point.navigateGSPSForwardUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify Accept/Reject radial tool bar is present on bottom of viewbox on GSPS forward navigation");
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");

		//press left arrow key to move to next GSPS object in series
		point.navigateGSPSBackUsingKeyboard();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Accept/Reject radial tool bar is present on bottom of viewbox on GSPS backward navigation");
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify enablity of icon on radial toolbar");
		viewerPage.assertFalse(point.isIconDisable(point.gspsFinding), "Verify Finding icon is disabled", "The Finding icon is enabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsNext), "Verify Next icon is enabled", "The Next icon is enabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsPrevious), "Verify Prevoius icon is enabled", "The Prevoius icon is enabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsAccept), "Verify Accept icon is enabled", "The Accept icon is enabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsReject), "Verify Reject icon is enabled", "The Reject icon is enabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsText), "Verify Add Text icon is disabled", "The Add Text icon is disabled");
		viewerPage.assertFalse(point.isIconDisable(point.gspsDelete), "Verify Delete icon is enabled", "The Next icon is enabled");
	}

	@Test(groups ={"Chrome","US800","Positive"})
	public void test22_US800_TC4231_verifyDeletionOfMachineAnnotationAcceptedOnEdition() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify deleting updated machine drawn annotation leads to changing its state to rejected");

		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
	

		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1a/2]", "Verify by default A/R binary selector is in pending state");

		//verifying that annotations are pending by default
		viewerPage.verifyFalse(point.verifyResultsAreAccepted(1) || point.verifyResultsAreRejected(1), "Verify by default A/R binary selector is in pending state","By default A/R binary selector is in pending state");
		point = new PointAnnotation(driver);

		//moving point which turn pending annotation to accepted
		point.movePoint(1, 1, 50, 50);

		//Verifying that deleted annotation state changed into accepted
		viewerPage.verifyTrue(point.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		//delete point annotation
		point.deletePoint(1, 1);

		//going back to studypage and reloading viewer
		viewerPage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GSPS_PatientName, 1);

		//verifying on deleting machine result , Results turned into rejected annotation after pressing delete key from keyboard
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		//verifying on deleting machine result , Results turned into rejected annotation after pressing delete button from A/R bar
		point.deleteSelectedPoint();

		point.selectPreviousfromGSPSRadialMenu(1);

		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

	}
	
	@Test(groups ={"Chrome","DE1022","Positive"})
	public void test23_DE1022_TC4267_verifyMachineGSPSRejectedOnCtrlDelWithNOJSONData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS is in rejected state when CTRL+DEL is pressed on machine annotations (Data with No JSON File)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName,  2);
		point = new PointAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying all machine drawn annotations are in pending state");
		viewerPage.assertEquals(point.getTotalPendingFindings(),5,"Verify that all machine results are in pending state","All machine results are in pending state");

		//CTRL + DEL
		point.deleteAllAnnotation(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying all machine drawn annotations are in rejected state after CTRL+DEL");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),5,"Verify that all machine results are in rejected state","All machine results are in rejected state");

		viewerPage.browserBackWebPage();
		helper.loadViewerDirectly(GSPS_PatientName,  2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying all machine drawn annotations are still in rejected state after reloading viewer");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),5,"Verify that all machine results are in rejected state after reloading viewer","All machine results are in rejected state after reloading viewer");

		//Selecting point annotation from radial menu
		point.selectPointFromQuickToolbar(1);

		//Drawing 3 point annotations on different slices
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_DOWN, 2);
		point.drawPointAnnotationMarkerOnViewbox(1, 150, 150);
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_DOWN, 2);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying recently drawn point annotations are in accepted state");
		viewerPage.assertEquals(point.getTotalAcceptedFindings(1),3,"Verify recently drawn point annotations are in accepted state","Recently drawn point annotations are in accepted state");

		viewerPage.browserBackWebPage();
		helper.loadViewerDirectly(GSPS_PatientName,  2);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying user drawn point annotations are in accepted state and machine drawn annotations are in rejected state");
		viewerPage.assertEquals(point.getTotalAcceptedFindings(1),3,"Verify user drawn point annotations are in accepted state","User drawn point annotations are in accepted state");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),5,"Verify all machine drawn annotations are in rejected state","All machine drawn annotations are in rejected state");

		//CTRL + DEL
		point.deleteAllAnnotation(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying user drawn point annotations are in accepted state and machine drawn annotations are in rejected state");
		viewerPage.assertEquals(point.getTotalAcceptedFindings(1),0,"Verify user drawn point annotations are deleted after pressing CTRL+DEL","User drawn point annotations are deleted");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),5,"Verify all machine drawn annotations are in rejected state","All machine drawn annotations are in rejected state");
	}

	@Test(groups ={"Chrome","US680"})
	public void test24_US680_TC2765_verifyNewGSPSRadialToolBar() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new Accept/Reject toolbar on data having GSPS object on one series.");

		patientPage = new PatientListPage(driver);
		helper= new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Aidoc_machine, 1);
	
		circle = new CircleAnnotation(driver);

		//		viewerPage.selectLayout(viewerPage.threeByTwoLayoutIcon);
		//verify Accept/Reject bar do not appear on hovering on bottom on view box for a series having a no GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Accept/Reject radial tool bar is present on bottom of viewbox for series with  GSPS object");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1),"Verify Circle annotation is current active Pending GSPS","The Circle annotationis current active pending GSPS");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is not visible on bottom of screen", "The Accept/Reject tool bar is not visible on bottom of screen");

		//hover on bottom of third view box and verify Accept/ Reject radial bar is present
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/2]", "Verify Accept/Reject radial tool bar is not present on bottom of viewbox for series with GSPS object on hovering");
		viewerPage.mouseHover(circle.getGSPSHoverContainer(3));
		viewerPage.assertFalse(circle.isAcceptRejectToolBarPresent(3), "Verify Accept/Reject tool bar is not visible on bottom of screen", "The Accept/Reject tool bar is not visible on bottom of screen");
	}	

	@Test(groups ={"IE11","US680","US950"})
	public void test25_US680_TC2775_US950_TC4386_verifyNewGSPSRadialToolBar() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Replace the radial GSPS menu with the new toolbar- Draw annotations & Navigation:Accept/Reject toolbar"
				+ "Replace the radial GSPS menu with the new toolbar- Draw annotations and validate animated GSPS navigation using new toolbar"
				+ "<br> Verify that on pressing left -right arrows from keyboard next annotation should highlighted with yellow shadow and annotation should not get bold /thick");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName,  1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);


		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);		
		viewerPage.scrollToImage(1, 18);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);
		viewerPage.scrollToImage(1, 25);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		viewerPage.scrollToImage(1, 4);

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -20, -20, -80, -80);

//		press right arrow to move to next GSPS object
//				viewerPage.click(viewerPage.getViewPort(1));	
//				circle.navigateGSPSForwardUsingKeyboard();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify all user drawn annotation are in accepted state");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),4,"Verifying the slice position","verified the slice position");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//navigate GSPS forward by clicking on next icon on tool bar and accept the current annotation
		circle.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify GSPS navigation for user created annotation on clicking Next button on toolbar");
		viewerPage.assertTrue(point.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify Point annotation is current active GSPS object","Point annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"Verifying the slice position","Verified the slice position");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//accept the active GSPS annotation
		point.selectAcceptfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify GSPS navigation for user created annotation by clicking on Accept button");
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Ellipse annotation is current active GSPS object","Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"Verifying the slice position","Verified the slice position");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//navigate GSPS forward by clicking on next icon on tool bar and accept the current annotation
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify GSPS navigation for user created annotation by clicking on previous button");
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Verify Point annotation is current active pending GSPS object","Point annotation is current active pending GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"Verifying the slice position","Verified the slice position");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to transparent");

		//accept the active GSPS annotation
		ellipse.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify GSPS navigation for user created annotation by clicking on Reject button");
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Ellipse annotation is current active GSPS object","Ellipse annotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),25,"Verifying the slice position","Verified the slice position");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//navigate GSPS forward by clicking on next icon on tool bar and accept the current annotation
		ellipse.selectPreviousfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify GSPS navigation for user created annotation by clicking on previous button");
		viewerPage.assertTrue(ellipse.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"Verify Point annotation is current active pending GSPS object","Pointannotation is current active GSPS object");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),18,"Verifying the slice position","Verified the slice position");
		viewerPage.assertTrue(ellipse.verifyRejectGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Red");

		//navigate GSPS forward by clicking on next icon on tool bar and delete current annotation
		ellipse.selectNextfromGSPSRadialMenu();
		ellipse.selectDeletefromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify GSPS annotation is deleted on clicking Delete button on toolbar");
		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),48,"Verifying the slice position","Verified the slice position");
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),0,"verifying ellipse annotation is deleted","Ellipse Annotation is deleted");

		//navigate to study page and reload same study
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");

		//navigate GSPS forward by clicking on next icon on tool bar and accept the current annotation
		circle.selectNextfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(circle.isAcceptRejectToolBarPresent(1), "Verify Accept/Reject tool bar is visible on bottom of screen", "The Accept/Reject tool bar is visible on bottom of screen");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1),"Verify Point annotation is current active rejected GSPS object","Point annotation is current active rejected GSPS object");





	}

	@Test(groups ={"Chrome","DE755","Sanity","BVT"})
	public void test26_DE755_TC2894_verifyGSPSFindingStateOnReloading() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that accept/reject state should be persists after moving to study page or reloading viewer page");

		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Aidoc_machine,  1);
				
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//click on reject on machine drawn annotation
		point.selectRejectfromGSPSRadialMenu();

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);		


		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(2);
		viewerPage.scrollToImage(1, 120);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//press right arrow to move to next GSPS object
		point.navigateGSPSBackUsingKeyboard();

		//select Accept from GSPS radial menu
		point.selectRejectfromGSPSRadialMenu();
		//		viewerPage.mouseHover(viewerPage.getViewPort(2));

		//navigate to study page and reload same study
		
		helper.browserBackAndReloadViewer(GSPS_PatientName_Aidoc_machine,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify GSPS finding state after study reload");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Verify Circle measurement retains it's rejected state","Circle Measurement is current active rejected GSPS object");
		viewerPage.click(viewerPage.getViewPort(1));
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1),"Verify linear measurement retains it's rejected state","Linear Measurement is current active rejected GSPS object");

		//navigate to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		point.navigateGSPSForwardUsingKeyboard();
		//		viewerPage.navigateGSPSBackUsingKeyboard();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify Point annotation retains it's rejected state","Point annotation is current active rejected GSPS object");
		point.navigateGSPSForwardUsingKeyboard();
		point.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Verify Circle measurement retains it's rejected state","Circle Measurement is current active rejected GSPS object");
	}

	@Test(groups ={"Chrome","DE755","DE902","Positive"})
	public void test27_DE755_TC2894_DE902_TC3507_verifyGSPSFindingStateOnRelogging() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that accept/reject state should be persists after logout and re-Login to application"
				+ "<br> Verify that linear measurement accept/reject state should get save");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName_Aidoc_machine,  1, 1);
		
		point = new PointAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Rejecting the first finding");
		//click on reject on machine drawn annotation
		point.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Drawing line");
		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Changing the slice 18");
		viewerPage.scrollToImage(1, 102);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Drawing the Point");
		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);


		//press right arrow to move to next GSPS object
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Performing back navigation");
		point.navigateGSPSBackUsingKeyboard();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Performing reject");
		//select Accept from GSPS radial menu
		point.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify GSPS finding state after study reload");
		//logout and login again
		viewerPage.navigateToBaseURL();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		
		helper.loadViewerPageUsingSearch(GSPS_PatientName_Aidoc_machine,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS finding state after study reload");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Verify Circle measurement retains it's rejected state","Circle Measurement is current active rejected GSPS object");
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1),"Verify linear measurement retains it's rejected state","Linear Measurement is current active rejected GSPS object");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verifying the state of point");
		//navigate to next GSPS object
		point.navigateGSPSForwardUsingKeyboard();
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify Point annotation retains it's rejected state","Point annotation is current active rejected GSPS object");
		point.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Verify Circle measurement retans it's rejected state","Circle Measurement is current active rejected GSPS object");
	}

	@Test(groups ={"Chrome","US777","US950","Sanity","BVT"})
	public void test28_US777_TC2987_US950_TC4300_TC4303_verifyMachineGeneratedAnnotationChangeStateAfterEditing() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the machine generated annotations automatically change state of measurements to Accepted after editing"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection");

		Header header = new Header(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName_Aidoc_machine,  1, 1);

		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);

		//Variable to store position on Center of Circle before moving
		String  beforeCX = circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX);   
		String  beforeCY = circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY);	

		//Move circle to the Left
		circle.selectCircle(1, 1);
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Verify Circle annotation changes from Pending to accepted", "The Current state of circle annotation is Accepted");

		circle.moveSelectedCircle(1,50,50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that user is able to move circle from one point to another with Viewbox");
		//		System.out.println(beforeCX+" "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		viewerPage.assertNotEquals(beforeCX,circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX), "Verify X Co-ordinate of center of circle change om moving circle", "The X Co-ordinate of Center of Circle changes from "+beforeCX+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CX));
		viewerPage.assertNotEquals(beforeCY,circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY), "Verify Y Co-ordinate of center of circle change om moving circle", "The Y Co-ordinate of Center of Circle changes from "+beforeCY+" to "+circle.getCircle(1,1).get(0).getAttribute(NSGenericConstants.CY));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify state of measurement changes from finding to Accepted on editing");
		//		viewerPage.openGSPSRadialMenu(circle.getAllCircles(2).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify Circle annotation changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		//		viewerPage.assertEquals(viewerPage.getCssValue(viewerPage.gspsAccept, NSGenericConstants.FILL),ViewerPageConstants.ACCEPT_GSPS_COLOR, "Verify color of Accept icon", "The color of Accept icon changes to Green");

		//logout and open GSPS Point series in Viewer
		header.logout();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify one GSPS result on the loaded slice is thicker than any other result, depicting that this is the first active result");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point annotation is current active Pending GSPS object");

		String xCoord =  point.getHandlesOfPoint(1,1).get(0).getAttribute(NSGenericConstants.CX);
		String yCoord =  point.getHandlesOfPoint(1,1).get(0).getAttribute(NSGenericConstants.CY);

		//select Point annotation and move it some other location
		point.movePoint(1, 1, 50, 50);
		// verifying that coordinates has changed
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify that user is able to move point annotation from one point to another with Viewbox");
		viewerPage.assertNotEquals(point.getHandlesOfPoint(1,1).get(0).getAttribute(NSGenericConstants.CX),xCoord,"Verifying Point is moved not edited", "point is moved hence x coordinates has changed");		
		viewerPage.assertNotEquals(point.getHandlesOfPoint(1,1).get(0).getAttribute(NSGenericConstants.CY),yCoord,"Verifying Point is moved not edited", "point is moved hence y coordinates has changed");		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify state of measurement changes from finding to Accepted on editing");
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Verify Point annotation state changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

	}

	@Test(groups ={"Chrome","US777","DE902","Positive","Sanity","BVT"})
	public void test29_US777_TC2990_DE902_TC3508_TC3509_verifyNewlyAddedAnnotationChangeStateAfterEditing() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the newly drawn annotations automatically change state of measurements to Accepted state after editing"
				+ "<br> Verify that circle accept/reject state should get save"
				+ "<br> Verify that Point accept/reject state should get save");


		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);	

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -30, -30, 80, 80);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -40, 20);

		//verify all annotation are in accepted state by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify all annotation are in accepted state");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "The linear measurement is in accepted state");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "The linear measurement is in accepted state");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1), "Verify Point annotation is in accepted state", "Point annotation is in accepted state");

		//Move circle to the Left
		//		circle.selectCircle(1,1);
		circle.moveSelectedCircle(1,-50,50);

		//Resize linear measurement
		lineWithUnit.selectLinearMeasurement(1,1);
		lineWithUnit.resizeSelectedLinearMeasurement(1,1,-20,30);

		//select Point annotation and move it some other location
		point.movePoint(1,1,100,100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify state of measurement changes from finding to Accepted on editing");
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Verify Point annotation state changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify state of measurement changes from finding to Accepted on editing");
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify Circle annotation state changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify state of measurement changes from finding to Accepted on editing");
		point.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify Linear measurement annotation state changes from Pending to accepted", "The Current state of linear measurement annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify all annotation are in rejected state");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "verified");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Verify linear measurement annotation is in rejected state", "verified");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1), "Verify Point annotation is in rejected state", "verified");

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify all annotation after reloading the viewer");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "verified");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Verify linear measurement annotation is in rejected state", "verified");
		viewerPage.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1), "Verify Point annotation is in rejected state", "verified");

		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify all annotation after reloading the application post logout");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "verified");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Verify linear measurement annotation is in rejected state", "verified");
		viewerPage.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1), "Verify Point annotation is in rejected state", "verified");


	}

	@Test(groups ={"Chrome","US777"})
	public void test30_US777_TC2991_verifyAlreadyAddedAnnotationChangeStateAfterEditing() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validate that the already drawn annotations automatically change state of measurements to Accepted state after editing");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);

		
		point = new PointAnnotation(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);


		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);	

		//select Circle Annotation from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -140, -140, 60, 60);

		//select Point annotation from radial menu and draw a point annotation
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, 20);

		//change the state of all the annotation
		point.selectRejectfromGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		circle.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		point.selectAcceptfromGSPSRadialMenu();

		//verify all annotation are in accepted state by default
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify all annotation are in accepted state");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Verify linear measurement annotation is in accepted state", "The linear measurement is in accepted state");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1), "Verify Point annotation is in accepted state", "Point annotation is in accepted state");

		//Move circle to the Left
		//		circle.selectCircle(1,1);
		circle.moveSelectedCircle(1,-30,30);

		//Resize linear measurement
		lineWithUnit.selectLinearMeasurement(1,1);
		lineWithUnit.resizeSelectedLinearMeasurement(1,1,-20,30);

		//select Point annotation and move it some other location
		point.movePoint(1,1,50,50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify state of measurement changes from finding to Accepted on editing");
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Verify Point annotation state changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify state of measurement changes from finding to Accepted on editing");
		ellipse.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify Circle annotation state changes from Pending to accepted", "The Current state of circle annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify state of measurement changes from finding to Accepted on editing");
		point.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify Linear measurement annotation state changes from Pending to accepted", "The Current state of linear measurement annotation is Accepted");
		viewerPage.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

	}

	@Test(groups ={"Chrome","US777","DE902","Positive"})
	public void test31_US777_TC2995_DE902_TC3507_US950_TC4300_TC4303_verifyNewlyAddedAnnotationChangeStateAfterEditing() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Edge case- TextArrow annotation and Linear measurement dotted string size"
				+ "<br> Verify that linear measurement accept/reject state should get save"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection");


		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn = new TextAnnotation(driver);

		//select Text Annotation from radial menu and draw a Text Annotation
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-50,-100,"ABC");
		textAn.drawText(1,-20,-30,"DEF");

		//change the state of all the annotation
		textAn.selectAcceptfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1).get(1));
		textAn.selectAcceptfromGSPSRadialMenu();

		//move to the next slice in series
		viewerPage.scrollToImage(1, 51);

		//select Text Annotation from radial menu and draw a Text Annotation
		//		textAn.selectTextArrowFromRadialMenu(1);
		textAn.drawText(1,-50,-100,"JKL");

		//change the state of all the annotation
//		textAn.selectRejectfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1).get(0));
		textAn.selectRejectfromGSPSRadialMenu();

		//move to the next slice in series
		viewerPage.scrollToImage(1, 54);

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-50,-20,-70,-90);

		//change the state of all the annotation
		textAn.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//move to the next slice in series
		viewerPage.scrollToImage(1, 55);

		//		lineWithUnit.selectDistanceMeasurementFromRadial(1);
		lineWithUnit.drawLine(1,-50,-20,-70,-90);

		//change the state of all the annotation
		textAn.selectAcceptfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));

		//change the text of pending text annotation
		textAn.updateTextOnTextAnnotation(1, 1, "Updated_ABC");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify state of text annotation remains unchange on editing text");
		//		viewerPage.openGSPSRadialMenu(textAn.getLineOfTextAnnotations(1).get(0));
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1,2, true), "Verify state of text annotation is unchanged", "The Current state of text annotation is Pending");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		//move the text annotation
		textAn.moveTextAnnotation(1, 2, -20, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify state of text annotation change to accepted on moving anchor line");

		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 2, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		//move to the next slice in series
		viewerPage.scrollToImage(1, 51);

		//move the text annotation
		textAn.moveTextAnnotation(1, 1, -20, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify state of text annotation change from rejected to accepted on moving anchor line");
		//viewerPage.openGSPSRadialMenu(textAn.getLineOfTextAnnotations(1).get(0));
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		//move to the next slice in series
		viewerPage.scrollToImage(1, 54);

		//move the text annotation
		lineWithUnit.moveLinearMeasurementText(1, 1, -20, 10);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify state of linear measurement does not change on moving text annotation");
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verify Linear measurement annotation state remains unchange", "The Current state of linear measurement annotation is Rejected");

		//move to the next slice in series
		viewerPage.scrollToImage(1, 55);

		//resize the linear measurement
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.moveLinearMeasurement(1, 1, -20, 20);
		viewerPage.mouseHover(viewerPage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify state of linear measurement changes on moving linear measurement anchor line");
		lineWithUnit.openGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Verify Linear measurement annotation state changes on resizing line", "The state of linear measurement changes on resizing line");

		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		viewerPage.scrollToImage(1, 55);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Verify Linear measurement annotation state changes on resizing line", "The state of linear measurement changes on resizing line");

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		viewerPage.scrollToImage(1, 55);
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Verify Linear measurement annotation state changes on resizing line", "The state of linear measurement changes on resizing line");



	}

	@Test(groups ={"Chrome","IE11","Edge","DE776","DE902","Positive"})
	public void test32_DE776_TC1772_DE902_verifyEditingTextFindingchangeState() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Editing the Unformatted Text finding change state to accepted.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		textAn = new TextAnnotation(driver);

		//select Text Annotation from radial menu and draw a Text Annotation
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-100,-150,ANNOTATION_TXT_1);
		textAn.drawText(1,100,-150,ANNOTATION_TXT_2);
		textAn.drawText(1,-150,100,ANNOTATION_TXT_3);
		textAn.drawText(1,200,50,ANNOTATION_TXT_4);

		//change the state of the annotation
		textAn.selectRejectfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1,3));
		textAn.selectAcceptfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1,1));

		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1,1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentRejectedInactive(1,3, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,4, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1,2, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		//Updated text
		textAn.updateTextOnTextAnnotation(1, 1,ANNOTATION_TXT_5);
		textAn.updateTextOnTextAnnotation(1, 3,ANNOTATION_TXT_6);

		//Verification of text annotation changed to accepted state
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify state of text annotation change to accepted after editing text");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,2, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,3, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1,4, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		//Verification of the color of accepted state on A/R toolbar
		textAn.navigateGSPSBackUsingKeyboard();
		textAn.assertTrue(textAn.verifyAcceptGSPSRadialMenu(), "Verify color of Accept icon", "The color of Accept icon changes to Green");

		textAn.selectRejectfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1,3));
		textAn.selectRejectfromGSPSRadialMenu(textAn.getLineOfTextAnnotations(1,1));

		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 3, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentRejectedInactive(1, 2, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(1, 4, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

		
		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 3, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentRejectedInactive(1, 2, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(1, 4, true), "Verify state of text annotation changes to accepted", "The Current state of text annotation is accepted");

	}

	@Test(groups ={"Chrome","DE902","Positive"})
	public void test33_DE902_TC3510_TC3511_verifyStateSavedForEllipseAndPoint() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that ellipse accept/reject state should get save"
				+ "<br> Verify that line accept/reject state should get save");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);
		ellipse = new EllipseAnnotation(driver);
		line = new  SimpleLine(driver);

		//select linear measurement from radial menu and draw a linear measurement
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, -70, -80, -120, 90);		

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);


		ellipse.selectRejectfromGSPSRadialMenu(line.getLine(1, 1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu();
		viewerPage.assertTrue(line.verifyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1),"Verify Point annotation is current active rejected GSPS object","Point annotation is current active rejected GSPS object");

		//navigate to study page and reload same study
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(line.verifyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1),"Verify Point annotation is current active rejected GSPS object","Point annotation is current active rejected GSPS object");

		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper.loadViewerPageUsingSearch(liver9PatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify GSPS finding state after studty reload");
		viewerPage.assertTrue(line.verifyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"verify Circle annotation is current active GSPS object","Circle annotation is current active GSPS object");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1),"Verify Point annotation is current active rejected GSPS object","Point annotation is current active rejected GSPS object");

	}

	@Test(groups ={"Chrome","US780","Positive"})
	public void test34_US780_TC3063_verifyARtoolbarOnHover() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of the hovering functionality over the accept or reject button when not enabled");

		patientPage=new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liver9PatientName, 1);
		
		ellipse = new EllipseAnnotation(driver);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50, 40, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify newly drawn annotation is present with accepting(green) state and not highlighted");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Verify ellipse is in green color and not highlighted","Ellipse is in green color and not highlighted");

		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));

		viewerPage.mouseHover(ellipse.getAcceptRejectToolBar(1));
		viewerPage.click(ellipse.gspsAccept);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Accept icon is disbled and Please select a finding - notification is displayed on top of the A/R UI");
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.gspsAccept, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [2a/3]", "Verifying Accept icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(ellipse.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [2b/3]", "Verifying the tooltip text - Please select a finding is displayed on hover over Accept icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [2c/3]", "Verifying the tooltip presence");

		viewerPage.click(ellipse.gspsReject);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Reject icon is disbled and Please select a finding - notification is displayed on top of the A/R UI");
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.gspsReject, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [3a/3]", "Verifying Reject icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(ellipse.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [3b/3]", "Verifying the tooltip text - Please select a finding is displayed on hover over Reject icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.getOuterGSPSNotification()),"Checkpoint [3c/3]", "verifying the tooltip presence");

	}

	@Test(groups ={"Chrome","US780","Positive"})
	public void test35_US780_TC3064_verifyARtoolbarOnHoverOnMachineResultAndBoneAge() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of the hovering functionality over the accept or reject button when not enabled for machine data and non-DICOM (Bone age)");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName, 1, 1);
		point = new PointAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying by default GSPS point annotation is selected and enabled");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Verifying point annotation is by default selected", "gsps point is selected");
		viewerPage.mouseHover(viewerPage.getViewPort(2));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying No GSPS is selected/highlighted");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Verifying point annotation is not selected", "gsps point is not selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying accept/reject buttons are disabled and 'Please select a finding' notification should be displayed");
		viewerPage.mouseHover(point.getAcceptRejectToolBar(1));
		viewerPage.click(point.getAcceptIcon());
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.gspsAccept, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [3a/5]", "Verifying Accept icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(point.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [3b/5]", "Verifying the tooltip text - Please select a finding is displayed on hover over Accept icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.getOuterGSPSNotification()),"Checkpoint [3c/5]", "Verifying the tooltip presence");

		viewerPage.click(point.getRejectIcon());
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.gspsReject, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [3d/5]", "Verifying Reject icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(point.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [3e/5]", "Verifying the tooltip text - Please select a finding is displayed on hover over Reject icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.getOuterGSPSNotification()),"Checkpoint [3f/5]", "verifying the tooltip presence");

		
		helper.browserBackAndReloadViewer(patientName_BoneAge,  1, 4);

		ellipse.selectEllipseFromQuickToolbar(4);
		ellipse.drawEllipse(4, 50, -50, 40, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify newly drawn annotation is present with accepting(green) state and not highlighted");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(4,1),"Verify ellipse is in green color and not highlighted","Ellipse is in green color and not highlighted");

		ellipse.openGSPSRadialMenu(ellipse.getEllipses(4).get(0));
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(4));

		viewerPage.mouseHover(point.getAcceptRejectToolBar(4));

		viewerPage.click(point.getAcceptIcon());
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Accept icon is disbled and Please select a finding - notification is displayed on top of the A/R UI");
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.gspsAccept, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [5a/5]", "Verifying Accept icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(point.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [5b/5]", "Verifying the tooltip text - Please select a finding is displayed on hover over Accept icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.getOuterGSPSNotification()),"Checkpoint [5c/5]", "Verifying the tooltip presence");

		viewerPage.click(point.getRejectIcon());
		viewerPage.assertEquals(viewerPage.getAttributeValue(point.gspsReject, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [5d/5]", "Verifying Reject icon is disabled - class on hover");		
		viewerPage.assertEquals(viewerPage.getText(point.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [5e/5]", "Verifying the tooltip text - Please select a finding is displayed on hover over Reject icon");
		viewerPage.assertTrue(viewerPage.isElementPresent(point.getOuterGSPSNotification()),"Checkpoint [5f/5]", "verifying the tooltip presence");

	}

	@Test(groups ={"Chrome","US780","Positive"})
	public void test36_US780_TC3072_verifyARtoolbarOnBinarySelector() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of A/R button enabled on binary selector");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(patientName_PiccOne, 1, 2);
		ViewerLayout layout = new  ViewerLayout(driver);
		ViewerARToolbox artool = new ViewerARToolbox(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1a/2]", "Verify by default A/R binary selector is in pending state");
		viewerPage.verifyFalse(artool.verifyResultsAreAccepted(1) || artool.verifyResultsAreRejected(1), "Verify by default A/R binary selector is in pending state","By default A/R binary selector is in pending state");

		artool.acceptResult(1);
		viewerPage.click(viewerPage.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1b/2]", "Verify selected checkmark is highlighted in green color");
		viewerPage.verifyTrue(artool.verifyResultsAreAccepted(1), "Verify selected checkmark is highlighted in green color","Selected checkmark is highlighted in green color");

		helper.browserBackAndReloadViewer(patientName_BoneAge, 1, 4);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		for(int i=5;i<8;i++)
			viewerPage.closeWaterMarkIcon(i);

		//Verify the Result Selector check boxes in all view boxes containing boneage result
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2a/2]", "Verify Result selector checkboxes in all viewbox containing BoneAge Result" );
		viewerPage.assertTrue(viewerPage.isElementPresent(artool.resultSelector(5)), "Verify Result selector is present on viewbox1", "Result selector is present on viewbox1");
		viewerPage.assertTrue(viewerPage.isElementPresent(artool.resultSelector(6)), "Verify Result selector is present on viewbox2", "Result selector is present on viewbox2");
		viewerPage.assertTrue(viewerPage.isElementPresent(artool.resultSelector(7)), "Verify Result selector is present on viewbox3", "Result selector is present on viewbox3");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2b(/2]", "Verify Result selector is checked on viewbox-1,viewbox-2 and viewbox-3");
		for(int i=5;i<8;i++){
			artool.checkBoneage(i);
			viewerPage.assertTrue(viewerPage.isChecked(artool.resultSelectorCheckBox(i)),"Verify Result selector is checked on viewbox-" + i , "Verified Result selector is checked on viewbox-" + i );
		}
	}

	@Test(groups ={"Chrome","US800","Positive"})
	public void test37_US800_TC4186_TC4188_verifySecuringMachineGSPSByDefault() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify deleting machine drawn annotation leads to changing its state to rejected" + "</br> Verify deleting machine drawn annotation which was rejected in earlier clone leads to changing its state to rejected");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(GSPS_PatientName, 1, 2);
	
		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1a/2]", "Verify by default A/R binary selector is in pending state");

		//verifying that annotations are pending by default
		viewerPage.verifyFalse(point.verifyResultsAreAccepted(1) || point.verifyResultsAreRejected(1), "Verify by default A/R binary selector is in pending state","By default A/R binary selector is in pending state");

		//Deleting Machine GSPS point annotation
		point.deleteSelectedPoint();

		//Clicking on PREV button from A/R bar
		point.selectPreviousfromGSPSRadialMenu(1);

		//Verifying that deleted annotation state changed into rejected
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		//Go back to study page and load the same study
		helper.browserBackAndReloadViewer(GSPS_PatientName, 1, 1);

		//Verifying that annotation which user deleted is in rejected state
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		//Again, deleting the same annotation and verifying that it is in rejected state
		point.deleteSelectedPoint();
		point.selectPreviousfromGSPSRadialMenu(1);
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

	}

	@Test(groups ={"Chrome","US800","Positive"})
	public void test38_US800_TC4187_verifyDeleteUserAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user can delete user drawn annotation from clone");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		point = new PointAnnotation(driver);

		//Select point from radial menu
		point.selectPointFromQuickToolbar(1);

		//Draw point annotations
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		point.drawPointAnnotationMarkerOnViewbox(1, 70, 70);
		point.drawPointAnnotationMarkerOnViewbox(1, 80, 80);
		point.drawPointAnnotationMarkerOnViewbox(1, -10, -10);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);

		//get count of all the annotations present in viewbox
		int countBeforeDeletion = point.getAllPoints(1).size();

		//Delete point by Pressing DEL key
		point.deletePoint(1, 1);

		//Delete point by pressing delete button from A/R bar
		point.deleteSelectedPoint();

		//Going back to study page and reloading viewer
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		//getting count of all point after deletion performed for two points
		int countAfterDeletion = point.getAllPoints(1).size();

		//verifying that count of point before deletion are greater than after deletion
		viewerPage.assertTrue(countBeforeDeletion > countAfterDeletion, "Verify annotation are deleted from viewbox1 ", "Deleted annotation from viewbox 1");

		//assigning count after deletion to countBeforeDeletion variable
		countBeforeDeletion = countAfterDeletion;

		//Again, delete more points 
		point.deleteSelectedPoint();
		point.deleteSelectedPoint();

		//getting count after deletion
		countAfterDeletion = point.getAllPoints(1).size();

		//verifying that count of point before deletion are greater than after deletion
		viewerPage.assertTrue(countBeforeDeletion > countAfterDeletion, "Verify annotation are deleted from viewbox1 ", "Deleted annotation from viewbox 1");
	}

	@Test(groups ={"Chrome","US800","Positive"})
	public void test39_US800_TC4189_verifyDeletionOfMachineGSPSandUserCreatedAnnotation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify CTRL+DEL functionality on machine + user drawn annotations");

		patientPage=new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 1);
	

		point = new PointAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1a/2]", "Verify by default A/R binary selector is in pending state");

		//verifying that annotations are pending by default
		viewerPage.verifyFalse(point.verifyResultsAreAccepted(2) || point.verifyResultsAreRejected(2), "Verify by default A/R binary selector is in pending state","By default A/R binary selector is in pending state");
		point = new PointAnnotation(driver);

		//selecting point annotation from radial menu
		point.selectPointFromQuickToolbar(2);

		//drawing new point annotation marker
		point.drawPointAnnotationMarkerOnViewbox(2, 50, 50);

		//getting count of all points before deletion
		int  countBeforeDeletion = point.getAllPoints(2).size();

		//deleting all annotations by pressing CTRL + DEL
		point.deleteAllAnnotation(2);

		//getting count of all points after deletion
		int countAfterDeletion = point.getAllPoints(2).size();

		//verifying that after deleting one point from viewbox total count of points are decreased by 1
		viewerPage.assertTrue((countBeforeDeletion -1) == countAfterDeletion, "Verify annotation are deleted from viewbox1 ", "Deleted annotation from viewbox 1");
	}

	@Test(groups ={"Chrome","DE1022","Positive"})
	public void test40_DE1022_TC4269_verifyMachineGSPSRejectedOnCtrlDelWithJSONData() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify GSPS is in rejected state when CTRL+DEL is pressed on machine annotations using data which has machine generated results (Data with JSON file)");

		patientPage=new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Aidoc_machine, 2);
		point = new PointAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying all machine drawn annotations are in pending state");
		viewerPage.assertEquals(point.getTotalPendingFindings(),2,"Verify that all machine results are in pending state","All machine results are in pending state");

		//CTRL + DEL
		point.deleteAllAnnotation(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying all machine drawn annotations are in rejected state after CTRL+DEL");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),2,"Verify that all machine results are in pending state","All machine results are in pending state");
		cs = new ContentSelector(driver);
		cs.selectResultFromSeriesTab(1,cs.getAllResults().get(0));
		cs.openAndCloseSeriesTab(false);
		
		//Selecting point annotation from radial menu
		point.selectPointFromQuickToolbar(1);

		//Drawing 3 point annotations on different slices
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_DOWN, 2);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		viewerPage.mouseWheelScrollInViewer(1,NSGenericConstants.SCROLL_DOWN, 2);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying user drawn point annotations are in accepted state and machine drawn annotations are in rejected state");
		viewerPage.assertEquals(point.getTotalAcceptedFindings(1),3,"Verify user drawn point annotations are in accepted state after pressing CTRL+DEL","User drawn point annotations are in accepted state");

		//CTRL + DEL
		point.deleteAllAnnotation(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying user drawn point annotations are deleted and machine drawn annotations are in rejected state");
		viewerPage.assertEquals(point.getTotalAcceptedFindings(1),0,"Verify user drawn point annotations are deleted after pressing CTRL+DEL","User drawn point annotations are deleted");
		viewerPage.assertEquals(point.getTotalRejectedFindings(),2,"Verify all machine drawn annotations are in rejected state","All machine drawn annotations are in rejected state");
	}

	//DE1072:Machine result is getting deleted after pressing 'Delete' button twice
	@Test(groups ={"Chrome","DE1072","Positive"})
	public void test41_DE1072_TC4428_verifyMachineResultsAfterPressingDeleteButtonTwice() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Machine result are not getting deleted after pressing 'Delete' button twice");

		patientPage=new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, 2);

		
		point = new PointAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify machine finding state remain in rejected state and never be deleted after pressing 'Delete' button twice from keyboard");
		//Deleting Machine GSPS point annotation (from Keyboard)
		point.deleteSelectedPoint();

		//Clicking on PREV button from A/R bar
		point.navigateGSPSBackUsingKeyboard();

		//Verifying that deleted annotation state changed into rejected
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		//again delete point
		point.deleteSelectedPoint();
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify machine finding state remain in rejected state and never be deleted after pressing 'Delete' button twice from A/R toolbar");
		//Deleting Machine GSPS point annotation (from A/R toolbar)
		point.selectNextfromGSPSRadialMenu(1);

		//delete point from A/R toolbar
		point.selectDeletefromGSPSRadialMenu();

		//Clicking on PREV button from A/R bar
		point.selectPreviousfromGSPSRadialMenu(1);

		//Verifying that deleted annotation state changed into rejected
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");

		//again delete point
		point.selectDeletefromGSPSRadialMenu();
		viewerPage.verifyTrue(point.verifyResultsAreRejected(1), "Verify selected checkmark is highlighted in red color","Selected checkmark is highlighted in red color");		
	}





}


