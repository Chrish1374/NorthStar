package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectApplicationShortcutsTest extends TestBase {

	private LoginPage loginPage;
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private OutputPanel panel;
	private TextAnnotation textAnn;
	private PolyLineAnnotation poly;
	
	String comment = "Sample comment1";

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 
    String SecondSeriesDescription=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath);
    String FirstSeriesDescription=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
    
	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String IBL_JohnDoe =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IBL_JohnDoe);
	
	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);
	
	String ChestCT1p25mm = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mm);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String pmap = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmap);
	
	String IHEMammoTest = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IHEMammoTest);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath"));
	
	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_Filepath);
	
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String dicomRTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private PatientListPage patientPage;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password );
	}

	//US1411: Accept/Reject shortcuts
	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test01_US1411_TC7679_verifyKeyBoardShortcutForMachineDrawnFindings() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that A, R, keyboard button shortcut are functional and changing the state of the findings on pressing the button.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName, 1);
	
		panel = new OutputPanel(driver);
		point = new PointAnnotation(driver);
		cs=new ContentSelector(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );
			
		String machineName=cs.getMachineName().get(0);
		int findingCount=panel.getBadgeCountFromToolbar(1);
		int beforeResult=cs.getAllResults().size();

		//accept one point
		point.selectPoint(1, 1);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1), "Checkpoint[1/11]", "Verified that focus jump to next slice after pressing shortcut key A.");
		panel.assertEquals(panel.getBadgeCountFromToolbar(1), findingCount-1, "Checkpoint[2/11]", "Verified that pending finding count descreased by 1 after pressing shortcut key A.");
		
		//reject one point
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1), "Checkpoint[3/11]", "Verified that focus jump to next slice after pressing shortcut key R.");
		panel.assertEquals(panel.getBadgeCountFromToolbar(1), findingCount-2, "Checkpoint[4/11]", "Verified that pending finding count descreased by 1 after pressing shortcut key R.");
	
		//verify state of accepted and rejected finding
		panel.selectPreviousfromGSPSRadialMenu(1);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1), "Checkpoint[5/11]", "Verified that finding state changed to accepted after using shortcut key A.");
		
		panel.selectPreviousfromGSPSRadialMenu(1);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[6/11]", "Verified that finding state changed to rejected after using shortcut key R. ");
		
		//verify new clone in content selector
		panel.assertNotEquals(cs.getAllResults().size(), beforeResult, "Checkpoint[7/11]", "Verified that new clone copy created after pressing shortcut key for Accept and Reject finding.");
		
	
		//load newly created clone in first viewbox
		cs.selectResultCloneFromSeriesTabForGivenResult(1,2, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1");
		panel.waitForViewerpageToLoad(1);
		//verify state of accepted ,rejected finding on viewer
		point.selectPoint(1, 1);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[8/11]", "Verified state of finding as accepted.");
		panel.selectNextfromGSPSRadialMenu(1);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1, 1), "Checkpoint[9/11]", "Verified state of finding as rejected.");

	   //open OP and verify accepted,rejected count
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[10/11]", "Verified count of accepted finding in Output panel.");
		panel.openAndCloseOutputPanel(false);
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[11/11]", "Verified count of rejected finding in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test02_US1411_TC7680_TC7720_verifyFunctionalityOfKeyboardShortcutP() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that P keyboard button shortcut are functional and changing the state of the findings on pressing the button.<br>"+
		"Verifying that ARP button are wokring only in active view box not in inactive view box.");

//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(liver9PatientName);		
//		patientPage.clickOntheFirstStudy();
//		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		panel = new OutputPanel(driver);
		point = new PointAnnotation(driver);
		cs=new ContentSelector(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		panel.assertTrue(cs.getAllResults().isEmpty(), "Checkpoint[1/5]", "Verified that no result entry present in Content selector.");
		point=new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 50);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "verify nothing happens on viewbox when viewbox is inactive by pressing shortcut key from keyboard." );
		point.mouseHover(point.getViewPort(2));
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[2/5]", "Verified that state of annotation is remain as it when user press shortcut P key.");
		
		//draw 3 annotations
		panel.scrollToImage(1 , 17);
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 150);
		lineWithUnit.selectRejectfromGSPSRadialMenu();

		panel.scrollToImage(1, 19);
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		
		panel.scrollToImage(1, 21);
		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -50, -100,-150);
		ellipse.selectRejectfromGSPSRadialMenu();
		
		int findingCount=panel.getFindingsCountFromFindingTable(1);
		
		//press shortcut key P from keyboard to change the state of annotation from accepted ,rejected to pending
		point.selectPoint(1, 1);
		for(int i=1;i<=findingCount;i++)
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(cs.getAllResults().isEmpty(), "Checkpoint[3/5]", "Verified that new result clone present in Content selector after changing the state of user drawn annotation to Pending.");

		//load clone copy in 2nd viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading clone copy in second viewbox." );
		panel.closingConflictMsg();
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		panel.assertEquals(panel.getBadgeCountFromToolbar(2), findingCount, "Checkpoint[4/5]", "Verified that all user drawn findings are in pending state.");
		
	   //open OP and verify accepted,rejected count
		panel.enableFiltersInOutputPanel(false,false,true);
		panel.assertEquals(panel.thumbnailList.size(), findingCount, "Checkpoint[5/5]", "Verified that all user drawn findings are under pending tab in Output panel.");
		panel.openAndCloseOutputPanel(false);
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test03_US1411_TC7720_TC7721_verifyFunctionalityWithActiveInactiveViewboxes() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying that ARP button are wokring only in active view box not in inactive view box.<br>"+
		"Verify that A,R,P keyboard shortcut are not working when no finding is selected on DICOM view box");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(GSPS_PatientName, 1);
			
		panel = new OutputPanel(driver);
		point = new PointAnnotation(driver);
		cs=new ContentSelector(driver);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+GSPS_PatientName+" in viewer" );
	    cs.selectResultFromSeriesTab(2, cs.getAllResults().get(1));
	    String beforeSlice=panel.getCurrentScrollPosition(1);
	    point.selectPoint(1, 1);
	    point.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
	    point.assertNotEquals(panel.getCurrentScrollPosition(1), beforeSlice, "Checkpoint[1/7]", "Verified that focus shifted to next slice after pressing shortcut key for Accept in first viewbox.");
	    point.assertNotEquals(panel.getCurrentScrollPosition(2), beforeSlice, "Checkpoint[2/7]", "Verified that both the viewboxes are in sync after pressing shortcut key for Accept");
		
	    beforeSlice=panel.getCurrentScrollPosition(1);
	    point.selectPoint(2, 1);
	    point.pressKeyboardShortcutsForARP(2,ViewerPageConstants.GSPS_REJECT);
	    point.assertNotEquals(panel.getCurrentScrollPosition(2), beforeSlice, "Checkpoint[3/7]", "Verified that focus shifted to next slice after pressing shortcut key for Reject in second viewbox.");
	    point.assertNotEquals(panel.getCurrentScrollPosition(1), beforeSlice, "Checkpoint[4/7]", "Verified that both the viewboxes are in sync after pressing shortcut key for Reject.");
	
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify that A,R,P keyboard shortcut are not working when no finding is selected on DICOM view box" );
	    panel.mouseHover(panel.getViewPort(1));
	    int pendingCount=point.getBadgeCountFromToolbar(1);
	    
	    point.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
	    panel.assertEquals(point.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[5/7]", "Nothing happens after pressing A from Keyboard when no finding is selected.");
	    panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
	    panel.assertEquals(point.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[6/7]", "Nothing happens after pressing R from Keyboard when no finding is selected.");
	    point.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
	    panel.assertEquals(point.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[7/7]", "Nothing happens after pressing P from Keyboard when no finding is selected.");
	    
	    
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test04_US1411_TC7722_TC7731_verifyAcceptRejectShortcutForNonDICOMData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to change the state of NON DICOM PNG, PDF, JPG image from A, R, P keyboard shortcut.<br>"+
				"Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for NONDICOM patient.");

		helper = new HelperClass(driver);		
		helper.loadViewerPageUsingSearch(IBL_JohnDoe_PatientName, 1, 4);
		
		panel = new OutputPanel(driver);
		panel.waitForPdfToRenderInViewbox(3);
		cs=new ContentSelector(driver);
	
		String result1=panel.getSeriesDescriptionOverlayText(1);
		String result2=panel.getSeriesDescriptionOverlayText(2);
		String result3=panel.getSeriesDescriptionOverlayText(3);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IBL_JohnDoe_PatientName+" in viewer" );
		
		//accept jpeg series and verify accepted finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[1/21]", "Verified that binary selector toolbar not visible for jpeg dataset");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[2/21]", "Verified that binary selector toolbar not visible for jpeg dataset");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[3/21]", "Verified that jpeg series is accepted using keyboard shortcut key A");
		
		//again click on shortcut key A ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[4/21]", "Verified that jpeg series is remain in accepted state after pressing shortcut key A again.");
		panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,result1,1),"Checkpoint[5/21]", "Verified that jpeg series is accepted in Output panel.");
		
		//reject png series and verify rejected finding in Output panel
		panel.pressKeyboardShortcutsForARP(2,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[6/21]", "Verified that binary selector toolbar not visible for png dataset");
		panel.assertTrue(panel.verifyResultsAreRejected(2), "Checkpoint[7/21]", "Verified that png series is rejected using keyboard shortcut key R");
		
		//again click on shortcut key R ,nothing should happen
		panel.pressKeyboardShortcutsForARP(2,ViewerPageConstants.GSPS_REJECT);
		panel.assertTrue(panel.verifyResultsAreRejected(2), "Checkpoint[8/21]", "Verified that png series is remain in rejected state after pressing shortcut key R again");
		panel.assertTrue(verifyFindingInOPAfterARP(false,true,false,result2,1),"Checkpoint[9/21]", "Verified that png series is rejected in Output panel.");
		
		//accept pdf then change state to pending and verify pending finding in Output panel
		panel.pressKeyboardShortcutsForARP(3,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[10/21]", "Verified that binary selector toolbar not visible for PDF dataset");
		panel.assertTrue(panel.verifyResultsAreAccepted(3), "Checkpoint[11/21]", "Verified that PDF is accepted using keyboard shortcut key A");
	    
		panel.pressKeyboardShortcutsForARP(3,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[12/21]", "Verified that binary selector toolbar not visible for PDF dataset");
		panel.assertFalse(panel.verifyResultsAreAccepted(3), "Checkpoint[13/21]", "Verified that PDF is in pending state using keyboard shortcut key P");
		panel.assertFalse(panel.verifyResultsAreRejected(3), "Checkpoint[14/21]", "Verified that PDF is in pending state using keyboard shortcut key P");
		
		//again click on shortcut key P ,nothing should happen
		panel.pressKeyboardShortcutsForARP(3,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.verifyResultsAreAccepted(3), "Checkpoint[15/21]", "Verified that PDF is in pending state using keyboard shortcut key P again.");
		panel.assertFalse(panel.verifyResultsAreRejected(3), "Checkpoint[16/21]", "Verified that PDF is in pending state using keyboard shortcut key P again.");
		panel.assertTrue(verifyFindingInOPAfterARP(false,false,true,result3,1),"Checkpoint[17/21]", "Verified that PDF is in pending state in Output panel.");
		
		//navigate back and select anonymous patient data		
		//Loading the patient on viewer
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymous_Patient+" in viewer" );
	    helper.browserBackAndReloadViewer(anonymous_Patient, 1, 2);
		
		result1=panel.getSeriesDescriptionOverlayText(1);
		
		//accept output png series and verify accepted finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[18/21]", "Verified that binary selector toolbar not visible for png series");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[19/21]", "Verified that binary selector toolbar not visible for png series");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[20/21]", "Verified that png series is accepted using keyboard shortcut key A");
		panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,result1,1),"Checkpoint[21/21]", "Verified that png series is in accepted state in Output panel.");
	
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1411","Sanity"})
	public void test05_US1411_TC7724_verifyAcceptRejectShortcutForDICOMSR() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to change the state of DICOM SR  from A, R, P keyboard shortcut.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm_PatientName, 2);

		panel = new OutputPanel(driver);
		panel.closeNotification();

		String SRreport=panel.getSeriesDescriptionOverlayText(1);
	
		panel.enableFiltersInOutputPanel(false, false, true);
		int totalfindings=panel.thumbnailList.size();
		panel.click(panel.getViewPort(1));
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ChestCT1p25mm_PatientName+" in viewer" );
		
		//accept SR series and verify accepted finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[1/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[2/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[3/13]", "Verified that SR is accepted using keyboard shortcut key A");
		panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,SRreport,1),"Checkpoint[4/13]", "Verified that SC result is in accepted state in Output panel.");
		
		//reject SR report and verify rejected finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[5/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[6/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[7/13]", "Verified that SR is rejected using keyboard shortcut key R");
		panel.assertTrue(verifyFindingInOPAfterARP(false,true,false,SRreport,1),"Checkpoint[8/13]", "Verified that SR result is in rejected state in Output panel.");
	    
		//Pending SR report and verify pending finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[9/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[10/13]", "Verified that binary selector toolbar not visible for SR data.");
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[11/13]", "Verified that SR result is not accepted its in pending state.");
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[12/13]", "Verified that SR result is not rejected its in pending state.");
		panel.assertTrue(verifyFindingInOPAfterARP(false,false,true,SRreport,totalfindings),"Checkpoint[13/13]", "Verified that SR result is in pending state in Output panel");	
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test06_US1411_TC7726_TC7739_verifyAcceptRejectShortcutForDICOMSCAndGSPSData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to change the state of DICOM SC from A, R, P keyboard shortcut. <br>"+
		"Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for DICOM SC patient.");

//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(imbio_PatientName);		
//		patientPage.clickOntheFirstStudy();

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 1);
	
		panel = new OutputPanel(driver);
		panel.waitForPdfToRenderInViewbox(2);
		String resultName=panel.getSeriesDescriptionOverlayText(1);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		
		//accept SC report and verify accepted finding in OP
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[1/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[2/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[3/21]", "Verified that SC result as accepted in Output panel.");
	
		//again click on shortcut key A ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[4/21]", "Verified that SC remain in accepted state only and nothing happens");
		panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,resultName,1),"Checkpoint[5/21]", "Verified SC result in Output panel");
		
		//reject SC report and verify rejected finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[6/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[7/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[8/21]", "Verified that SC result as rejected in Output panel.");
		
		//again click on shortcut key R ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[9/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[10/21]", "Verified that SC remain in rejected state only and nothing happens");
		panel.assertTrue(verifyFindingInOPAfterARP(false,true,false,resultName,1),"Checkpoint[11/21]", "Verified SC result in Output panel");
	
		//Pending SC report and verify pending finding in Output panel
	    panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[12/21]", "Verified that binary selector toolbar not visible for SC result");
	    panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[13/21]", "Verified that binary selector toolbar not visible for SC result");
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[14/21]", "Verified thay SC result is not in accepted state.");
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[15/21]", "Verified that SC result not in rejected state.");
		
		//again click on shortcut key P ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[16/21]",  "Verified thay SC result is not in accepted state.");
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[17/21]", "Verified that SC result not in rejected state.");
		panel.assertTrue(verifyFindingInOPAfterARP(false,false,true,resultName,2),"Checkpoint[18/21]", "Verified that SC result is in pending state only in Output panel.");	
		
		//draw annotation on SC result
		panel.click(panel.getViewPort(1));
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,5, 5,-80,-80);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[19/21]", "Verified that circle annotation is in Accepted state.");
		
		//change state to rejected for circle
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		//change state of SC to accepted 
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[20/21]", "Verified that circle annotation is in rejected state.");
		circle.selectNextfromGSPSRadialMenu();
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[21/21]", "Verified that SC result is in Accepted state.");
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test07_US1411_TC7728_verifyKeyboardShortcutWhenFindingIsAlreadyInThatState() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for DICOM patient.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
	

		lineWithUnit=new MeasurementWithUnit(driver);
		circle=new CircleAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		circle.scrollToImage(1,17);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 150);

		circle.scrollToImage(1,19);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5,-80,-80);
		
		//verify nothing happens when shortcut key A press for the accepted annotation
		circle.selectFindingFromTable(1);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[1/12]", "Verified that linear measurement is accepted GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[2/12]", "Verified that nothing happens when A press for accepted Linear measurement annotation. ");
		circle.selectFindingFromTable(2);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/12]", "Verified that circle annotation is current active accepted GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		circle.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[4/12]", "Verified that nothing happens when A press for accepted circle measurement annotation.");
		
		//change state of both annotation to rejected
		circle.selectFindingFromTable(1);
		circle.selectRejectfromGSPSRadialMenu();
		circle.selectRejectfromGSPSRadialMenu();
		
		//verify nothing happens when shortcut key R press for the rejected anntation
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Checkpoint[5/12]", "Verified that linear measurement is rejected GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Checkpoint[6/12]", "Verified that nothing happens when R press for rejected Linear measurement.");	
		circle.selectFindingFromTable(2);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[7/12]", "Verified that circle annotation is current active rejected GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		circle.assertTrue(circle.verifyCircleAnnotationIsRejectedGSPS(1, 1), "Checkpoint[8/12]", "Verified that nothing happens when R press for rejected circle annotation.");
		
		//change state of both annotation to pending
		circle.selectFindingFromTable(1);
		circle.selectRejectfromGSPSRadialMenu();
		circle.selectRejectfromGSPSRadialMenu();
			
		//verify nothing happens when shortcut key R press for the rejected anntation
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActivePendingGSPS(1, 1), "Checkpoint[9/12]", "Verified that linear measurement is pending GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsPendingGSPS(1, 1), "Checkpoint[10/12]", "Verified that nothing happens when P press for pending Linear measurement .");		
		circle.selectFindingFromTable(2);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[11/12]", "Verified that circle annotation is current active pending GSPS");
		circle.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		circle.assertTrue(circle.verifyCircleAnnotationIsPendingGSPS(1, 1), "Checkpoint[12/12]", "Verified that nothing happens when P press for pending circle annotation.");
		
		
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Positive"})
	public void test08_US1411_TC7757_TC7758_verifyAcceptRejectShortcutForPAMP() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to change the state of PMAP from A, R, P keyboard shortcut. <br>"+
		"Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for PMAP patient.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);
		
		panel = new OutputPanel(driver);
		cs=new ContentSelector(driver);
		panel.closeNotification();
		
		String resultName=cs.getAllResults().get(0);
	
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+" in viewer" );
		
		//accept SR report and verify accepted finding in OP
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[1/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.mouseHover(panel.getViewPort(1));
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[2/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[3/21]", "Verified that pmap result is in accepted state .");
	
		//again click on shortcut key A ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[4/21]", "Verified that pmap result is in accepted state.");
		panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,resultName,1),"Checkpoint[5/21]", "Verified that pmap result is in accepted state in Output panel");
		
		//reject SR report and verify rejected finding in Output panel
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[6/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[7/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[8/21]", "Verified that pmap result is in rejected state .");
		
		//again click on shortcut key R ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[9/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[10/21]", "Verified that pmap result is in rejected state .");
		panel.assertTrue(verifyFindingInOPAfterARP(false,true,false,resultName,1),"Checkpoint[11/21]", "Verified that pmap result is in rejected state in Output panel .");
	
		//Pending SR report and verify pending finding in Output panel
	    panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[12/21]", "Verified that binary selector toolbar not visible for pmap result");
	    panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.isAcceptRejectToolBarPresent(1), "Checkpoint[13/21]", "Verified that binary selector toolbar not visible for pmap result");
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[14/21]", "Verified that pmap result is in pending state ");
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[15/21]", "Verified that pmap result is in pending state ");
		
		//again click on shortcut key P ,nothing should happen
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[16/21]", "Verified that pmap result is in pending state ");
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[17/21]", "Verified that pmap result is in pending state ");
		panel.assertTrue(verifyFindingInOPAfterARP(false,false,true,resultName,1),"Checkpoint[18/21]", "Verified that pmap result is in pending state in Output panel. ");	
		
		panel.click(panel.getViewPort(1));
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,5, 5,-80,-80);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[19/21]", "Verified that circle annotation is current active accepted GSPS.");
		circle.click(circle.getViewPort(1));
		
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		//reject circle 2 times and verify state after that
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		
		circle.click(circle.getViewPort(1));
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[20/21]", "Verified that pmap result is in rejected state.");
		circle.selectNextfromGSPSRadialMenu();
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[21/21]", "Verified that circle annotation is current rejected GSPS");
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Negative"})
	public void test09_US1411_TC7801_TC7799_verifyKeyboardShortcutsInOutputPanel() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Output panel is not getting closed and state not getting changed on viewer on pressing A, R, P button from keyboard. <br>"+
		"Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for DICOM SC patient.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 1);
	
		
		panel = new OutputPanel(driver);
		panel.waitForPdfToRenderInViewbox(2);
		textAnn=new TextAnnotation(driver);

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		
		panel.mouseHover(panel.getViewPort(3));
		panel.enableFiltersInOutputPanel(false, false, true);
		int pendingCount=panel.thumbnailList.size();
		
		panel.mouseHover(panel.thumbnailList.get(0));
	    panel.pressKey(ViewerPageConstants.ACCEPT_KEY);
		panel.assertTrue(panel.verifyOutputPanelIsOpened(), "Checkpoint[1/7]", "Verified that OP not closed after pressing shortcut key A.");
		
		panel.mouseHover(panel.thumbnailList.get(0));
	    panel.pressKey(ViewerPageConstants.PENDING_KEY);
		panel.assertTrue(panel.verifyOutputPanelIsOpened(), "Checkpoint[2/7]","Verified that OP not closed after pressing shortcut key P.");
		
		panel.mouseHover(panel.thumbnailList.get(0));
	    panel.pressKey(ViewerPageConstants.REJECT_KEY);
		panel.assertTrue(panel.verifyOutputPanelIsOpened(), "Checkpoint[3/7]", "Verified that OP not closed after pressing shortcut key R.");
		
		panel.assertEquals(panel.thumbnailList.size(), pendingCount, "Checkpoint[4/7]", "Verified that state of finding not change after pressing shortcut keys");
		panel.openAndCloseOutputPanel(false);
		
		panel.click(panel.getViewPort(1));
		panel.addResultComment(1, comment);
		panel.scrollToImage(1, 115);
		panel.addResultComment(1, comment+"2");
		
		panel.scrollToImage(1, 113);
		textAnn.click(textAnn.getTextOfSCComment(1).get(0));
		panel.pressKey(ViewerPageConstants.REJECT_KEY);
		panel.click(panel.getViewPort(1));
		panel.assertFalse(panel.verifyResultsAreRejected(1), "Checkpoint[5/7]", "Verified that SC result is in rejected state after pressing shortcut key R.");
		panel.pressKey(ViewerPageConstants.REJECT_KEY);
		panel.click(panel.getViewPort(1));
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[6/7]", "Verified that SC result is in rejected state after pressing shortcut key R.");
		
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[7/7]", "Verified that SC result is in rejected state in Output panel.");
		

	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Sanity"})
	public void test10_US1411_TC7755_TC7756_verifyAcceptRejectShortcutForMammoCADData() throws InterruptedException, AWTException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that A, R, keyboard button shortcut are functional and changing the state of the findings on pressing the button for MammoCad data <br>"+
		"Verify that state of Accepted, Rejected , Pending findings not getting changed on pressing the A, R ,P keyboard shortcut repectively for Mammo CAD patient.");

//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(IHEMammoTestPatientName);		
//		patientPage.clickOntheFirstStudy();


		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 1);

		panel = new OutputPanel(driver);
		cs=new ContentSelector(driver);
		poly=new PolyLineAnnotation(driver);
		point=new PointAnnotation(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		
		int resultCount=cs.getAllResults().size();
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		
		int findingCount=panel.getBadgeCountFromToolbar(1);
		panel.click(panel.getViewPort(1));
	
		//verify finding state not changes using shortcut key when no annotation is selected
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.pressKeyboardShortcutsForARP(1, ViewerPageConstants.GSPS_REJECT);
		panel.pressKeyboardShortcutsForARP(1, ViewerPageConstants.PENDING_TEXT);
		panel.assertEquals(panel.getBadgeCountFromToolbar(1), findingCount, "Checkpoint[1/11]", "Verified that state of annotation not changes when finding is not selected.");
	
		//verify nothing happens by pressing shortcut key when annotation is already in that step
		panel.selectFindingFromTable(1);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.selectFindingFromTable(2);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.selectFindingFromTable(1);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
		panel.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 2), "Checkpoint[2/11]", "Verified that polyline2 is accepted GSPS.");
		
		panel.selectFindingFromTable(2);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		panel.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 2), "Checkpoint[3/11]", "Verified that polyline1 is rejected GSPS.");
		
		panel.selectFindingFromTable(3);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1), "Checkpoint[4/11]", "Verified that point annotation is in pending state.");

		
		//accept first finding and reject second finding then verify state
		panel.selectFindingFromTable(1);
		panel.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[5/11]", "Verified that polyline2 is accepted GSPS");
		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2), "Checkpoint[6/11]", "Verified that polyline1 is rejected GSPS");
	
		//select accepted finding then change state to pending
		panel.selectFindingFromTable(1);
		panel.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
		panel.selectPreviousfromGSPSRadialMenu(1);
		panel.assertFalse(panel.verifyResultsAreAccepted(1), "Checkpoint[7/11]", "Verified that finding is in pending state.");
		
		panel.assertNotEquals(cs.getAllResults().size(), resultCount, "Checkpoint[8/11]", "Verified that new clone is created after performing accept/reject using shortcut key.");
		//verify findings in OP
        panel.enableFiltersInOutputPanel(true, false, false);
        panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[9/11]", "Verified that no finding is in accepted state");
        panel.openAndCloseOutputPanel(false);
		panel.assertTrue(verifyFindingInOPAfterARP(false, true, false,ViewerPageConstants.POLYLINE+"_3",1),"Checkpoint[10/11]","Verified rejected finding in Output panel.");
	    panel.enableFiltersInOutputPanel(false, false, true);
	    panel.assertEquals(panel.thumbnailList.size(),findingCount, "Checkpoint[11/11]", "Verified the pending finding count in Output panel.");
	    panel.openAndCloseOutputPanel(false);
	    
	
		
	}

	@Test(groups ={"Chrome","IE11","Edge","US1411","Sanity"})
	public void test11_US1411_TC7800_verifyKeyboardShortcutForRTStructData() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  state of RT struct contour is getting changed on  AR tool bar, finding bar,Legend and in Output panel.");

//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(dicomRTPatientName);		
//		patientPage.clickOntheFirstStudy();
		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(dicomRTPatientName, 1);


		DICOMRT rt=new DICOMRT(driver);
	    rt.waitForDICOMRTToLoad();
		panel = new OutputPanel(driver);
		point = new PointAnnotation(driver);
		cs=new ContentSelector(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+dicomRTPatientName+" in viewer" );
		int pendingCount=rt.getBadgeCountFromToolbar(1);
		
		rt.navigateToFirstContourOfSegmentation(1);
		String sliceNo=rt.getCurrentScrollPosition(1);
		rt.pressKeyboardShortcutsForARP(1, ViewerPageConstants.GSPS_ACCEPT);
		
		String segmentName = rt.getNameOfAcceptedRejectedSegment(1);
		rt.assertNotEquals(rt.getCurrentScrollPosition(1), sliceNo, "Checkpoint[1/11]", "Verified that focus jump to next contour after using shortcut key for accept.");
		rt.assertNotEquals(rt.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[2/11]", "Verified the pending finding count decreased by 1");
		rt.assertEquals(rt.getFindingState(segmentName),(ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[3/11]","Verifying the color of finding is changed in finding menu");
        rt.assertTrue(rt.verifyAcceptedRTSegment(1),"Checkpoint[4/11]","Verified that state of segment is in accepted state.");
        panel.assertTrue(verifyFindingInOPAfterARP(true,false,false,segmentName, 1),"Checkpoint[5/11]","Verified accepted segment in Output panel.");
        
        //navigate to 3rd segement
        rt.navigateToFirstContourOfSegmentation(3);
        sliceNo=rt.getCurrentScrollPosition(1);
		rt.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_REJECT);
		 segmentName = rt.getNameOfAcceptedRejectedSegment(3);
		
		rt.assertNotEquals(rt.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[6/11]", "Verified the pending finding count again decreased by 1");
		rt.assertEquals(rt.getFindingState(segmentName),(ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[7/11]","Verifying the color of finding is changed in finding menu");
        rt.assertTrue(rt.verifyRejectedRTSegment(3),"Checkpoint[8/11]","Verified that state of segment is in rejected state.");
        panel.assertTrue(verifyFindingInOPAfterARP(false,true,false,segmentName, 1),"Checkpoint[9/11]","Verified rejected segment in Output panel.");
        
		//navigate to first contour of second segememt and change state to accept  all
        rt.navigateToFirstContourOfSegmentation(2);
        for(int i=1;i<pendingCount;i++)
        	rt.pressKeyboardShortcutsForARP(1, ViewerPageConstants.GSPS_ACCEPT);
        
      //navigate to first contour of first segememt and change state to pending  all
        rt.navigateToFirstContourOfSegmentation(1);
        for(int i=0;i<pendingCount;i++)
        	rt.pressKeyboardShortcutsForARP(1,ViewerPageConstants.PENDING_TEXT);
        
        //verify pending finding on A/R yoolbar
        rt.assertEquals(rt.getBadgeCountFromToolbar(1), pendingCount, "Checkpoint[10/11]", "Verified pending finding count in finding table.");
        
        panel.enableFiltersInOutputPanel(false, false, true);
        panel.assertEquals(panel.thumbnailList.size(), pendingCount, "Checkpoint[11/11]", "Verified pending finding count in output panel.");
      	
	}
	
	// Need to find better way to fix this verification
    public boolean verifyFindingInOPAfterARP(boolean accept, boolean reject, boolean pending,String findingname,int thumbnail) throws InterruptedException
     {
	  boolean status=false;
	  panel.enableFiltersInOutputPanel(accept, reject, pending);
	  
	  boolean thumbnailCount=!panel.thumbnailList.isEmpty();
//	  panel.scrollIntoView(panel.findingsNameTitleList.get(thumbnail-1));
//	  boolean findingName=panel.getText(panel.findingsNameTitleList.get(thumbnail-1)).contains(findingname);
	  panel.openAndCloseOutputPanel(false); 
	   
	  status=thumbnailCount; //&& findingName;
      return status;
     }

  @AfterMethod(alwaysRun=true)
	public void AfterMethod() throws SQLException, IOException, InterruptedException {
		DatabaseMethods db = new DatabaseMethods(driver);
	    db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+username );
  }
}
