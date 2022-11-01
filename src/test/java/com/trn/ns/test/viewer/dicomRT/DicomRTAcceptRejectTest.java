package com.trn.ns.test.viewer.dicomRT;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DicomRTAcceptRejectTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private PolyLineAnnotation poly;
	private PointAnnotation pointAn;
	private EllipseAnnotation ellipse;
	private TextAnnotation textAnno;
	private CircleAnnotation circle;
	private ContentSelector cs;
	private HelperClass helper;
	private ViewerLayout layout;
	
	private String loadedTheme;
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	// Get Patient Name
	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username,password);
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "US773", "Positive","DE1746","US1411","US2511","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US773_TC3660_TC3662_TC3663_DE1746_TC7118_US1411_TC7760_US2511_TC10427_TC10429_verifySegmentAcceptInGroup(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to Accept / reject the findings in group"
				+ "<br> Verify that User is able to navigate and see the status (accept / reject) of segment in legend"
				+ "<br> Verify the state icon should be same as the legend in the drop down list"
				+ "<br> Verify if user is able to Accept/Reject machine findings from A/R toolbar and correct state displayed in Output panel, Finding list, Finding marker and on Legends. <br>"+
				"Verify that accept, reject working correctly from AR tool bar [Risk and Impact]. <br>"+
				"Verify new RT legend pop up in eureka theme when legends are in accepted/rejected/pending state. <br>"+
				"Verify new RT legend pop up in dark theme when legends are in accepted/rejected/pending state");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);
			
	    }else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		patientListPage.waitForPatientPageToLoad(); 
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify on clicking first contour of first segment should accept all contours");	

		//fetching total of pending finding from finding bubble/badge
		int totFinding = drt.getBadgeCountFromToolbar();

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(2);

		//clicking on accept button from A/R bar
		drt.selectAcceptfromGSPSRadialMenu();

		//fetching segment name
		String segmentName = drt.getNameOfAcceptedRejectedSegment(2);

		//opening finding table in viewbox (1)
	//	drt.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on clicking state icon color changed to green");

		//verifying segment accepted
		drt.assertTrue(drt.verifyAcceptedRTSegment(2), "Checkpoint[2/8]","Verifying state icon is accepted");	

		//fetching total of pending findings after accepting one
		int findingCountAfterAccept = drt.getBadgeCountFromToolbar();

		//verifying that count of total finding is decreased by 1
		drt.assertEquals(findingCountAfterAccept, totFinding-1,"Checkpoint[3/8]","Verifying the badge count is reduced");

		//verifying that color of icon in finding table should green after accepting segment
		drt.assertEquals(drt.getFindingState(segmentName),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[4/8]","Verifying the color of finding is changed in finding menu");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on clicking first contour of first segment should reject all contours");

		//fetching count of pending finding
		totFinding = findingCountAfterAccept;

		//clicking on segment and navigating to first contour of segment (3)
		drt.navigateToFirstContourOfSegmentation(3);

		//rejecting segment by clicking on reject button of A/R bar
		drt.selectRejectfromGSPSRadialMenu();

		//fetching name of rejected segment
		segmentName = drt.getNameOfAcceptedRejectedSegment(3);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify on clicking state icon color changed to red");

		//verifying rejected segment state icon in legend
		drt.assertTrue(drt.verifyRejectedRTSegment(3), "Checkpoint[6/8]","Verifying state icon is rejected");	

		//fetching count of pending finding
		int findingCountAfterReject = drt.getBadgeCountFromToolbar();

		//verifying that count of pending finding should decreased by 1
		drt.assertEquals(findingCountAfterReject, totFinding-1,"Checkpoint[7/8]","Verifying the badge count is reduced");

		//verifying that color of icon of rejected segment is red
		drt.assertEquals(drt.getFindingState(segmentName),ViewerPageConstants.REJECTED_FINDING_COLOR,"Checkpoint[8/8]","Verifying the color of finding is changed in finding menu");

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "US773","US1411","DE1928","US2511","Positive","BVT","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US773_TC3666_US1411_TC7760_DE1928_TC7792_US2511_TC10427_TC10429_verifyStateiconColorAfterAcceptReject(String theme) throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that same state icon works for accept, reject, pending finding/segment in legend.<br>"+
		"Verify that accept, reject working correctly from AR tool bar [Risk and Impact].<br>"+
		"Verify the AR tool bar is displayed for dicom series. <br>"+
		"Verify new RT legend pop up in eureka theme when legends are in accepted/rejected/pending state.<br>"+
		"Verify new RT legend pop up in dark theme when legends are in accepted/rejected/pending state");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);
			
	    }else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		patientListPage.waitForPatientPageToLoad();
		
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		//fetching total of pending finding from finding bubble/badge
		int totFinding = drt.getBadgeCountFromToolbar();

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(3);
		drt.assertTrue(drt.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/4]", "Verified that AR toolbar visible not the binary toolbar for RT data.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify on clicking first contour of first segment should accept all contours");	

		//clicking on accept button from A/R bar
		drt.selectAcceptfromGSPSRadialMenu();

		//fetching segment name
		String segmentName = drt.getNameOfAcceptedRejectedSegment(3);

		//opening finding table in viewbox (1)
		drt.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on clicking state icon color changed to green");

		//verifying segment accepted
		drt.assertTrue(drt.verifyAcceptedRTSegment(3), "Checkpoint[3.1/4]","Verifying state icon is accepted");	

		//fetching total of pending findings after accepting one
		int findingCountAfterAccept = drt.getBadgeCountFromToolbar();

		//verifying that count of total finding is decreased by 1
		drt.assertEquals(findingCountAfterAccept, totFinding-1,"Checkpoint[3.2/4]","verifying that count of total finding is decreased by 1");

		//verifying that color of icon in finding table should green after accepting segment
		drt.assertEquals(drt.getFindingState(segmentName),ViewerPageConstants.ACCEPTED_FINDING_COLOR,"Checkpoint[3.3/4]","Verifying the finding color in finding table");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify on clicking first contour of first segment should reject all contours");

		//fetching count of pending finding
		totFinding = findingCountAfterAccept;

		//clicking on segment and navigating to first contour of segment (3)
		drt.navigateToFirstContourOfSegmentation(3);

		//rejecting segment by clicking on reject button of A/R bar
		drt.selectRejectfromGSPSRadialMenu();

		//fetching name of rejected segment
		segmentName = drt.getNameOfAcceptedRejectedSegment(3);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4.1/4]", "Verify on clicking state icon color changed to red");

		//verifying rejected segment state icon in legend
		drt.assertTrue(drt.verifyRejectedRTSegment(3),"Checkpoint[4.2/4]", "Verifying state icon is rejected");	

		//fetching count of pending finding
		int findingCountAfterReject = drt.getBadgeCountFromToolbar();

		//verifying that count of pending finding should not decreased by 1
		drt.assertEquals(findingCountAfterReject, totFinding,"Checkpoint[4.3/4]","Verifying the count from badge");

		//verifying that color of icon of rejected segment is red
		drt.assertEquals(drt.getFindingState(segmentName),(ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[4.4/4]","Verifying the finding color in finding table");

		//Again navigate to same segment
		drt.navigateToFirstContourOfSegmentation(3);

		//clicking on reject button of A/R bar so that same segment will be pending
		drt.selectRejectfromGSPSRadialMenu();

		//verifying rejected segment is now pending
		drt.assertTrue(drt.verifyPendingRTSegment(3),"Checkpoint[4.5/4]", "Verifying state icon is pending");	

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "US773","DE1928","Positive"})
	public void test03_US773_TC3665_DE1928_TC7805_verifySegmentColor() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that color of segment should not get changed on accepting or rejecting segment / contour. <br>"+
		"Verify AR tool bar is displayed on the initial load for the series with findings like machine GSPS.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on accepting any segemeny color of segment doesnot change and should be same as color in legend");	

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(2);

		//verifying and comparing color of segment/contour in legend with slice
		drt.assertEquals(drt.getHexColorValue(drt.getColorOfSegment(2)), drt.getHexColorValue(drt.getSelectedContourColor()),"Checkpoint[1/2]","Verify on accepting any segemeny color of segment doesnot change and should be same as color in legend");
	
		drt.click(drt.getViewPort(1));
		drt.mouseHover(drt.getViewPort(1));
		drt.mouseHover(drt.getGSPSHoverContainer(1));
		drt.assertTrue(drt.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/2]", "Verified that AR toolbar visible not the binary toolbar for RT data when no contour is selected.");
	
		 
		 
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE917", "Positive","BVT"})
	public void test04_DE917_TC3773_verifyEditingOfContours() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit and move Contour");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
	
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		int linesBeforeEdit = poly.getLinesOfPolyLine(1, 1).size();

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,12),"Checkpoint[1/2]","Verifying the halo ring is displayed on mouse hover");

		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(10),poly.getLinesOfPolyLine(1, 1).get(200));

		poly.assertTrue(linesBeforeEdit > poly.getLinesOfPolyLine(1, 2).size(),"Checkpoint[2/2]" ,"Verifying that edit contour is done successful" );

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE917", "Positive","BVT"})
	public void test05_DE917_TC3773_verifyMovementOfContours() throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can move contour by [ctrl +drag] event");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		int polylineX=poly.getValueOfXCoordinate(poly.getLinesOfPolyLine(1, 1).get(1));
		int polylineY= poly.getValueOfYCoordinate(poly.getLinesOfPolyLine(1, 1).get(1));

		poly.moveFreePolyLine(1,1,50,50);


		poly.assertNotEquals(poly.getValueOfXCoordinate(poly.getLinesOfPolyLine(1, 1).get(1)), polylineX, "Checkpoint[1/2]", "verifying post movement the x co-ordinate is changed");
		poly.assertNotEquals(poly.getValueOfYCoordinate(poly.getLinesOfPolyLine(1, 1).get(1)), polylineY, "Checkpoint[2/2]", "verifying post movement the y co-ordinate is changed");

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE917", "Positive"})
	public void test06_DE917_TC3783_verifyInavlidEditingOfContours() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can't perform edit by dropping invalid point");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		drt.waitForViewerpageToLoad();
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		drt.navigateToFirstContourOfSegmentation(5);

		int linesBeforeEdit = poly.getLinesOfPolyLine(1, 1).size();		

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,12),"Checkpoint[1/2]","Verifying the halo ring presence on mouse hover");

		int []coordinates = new int[] {30,40,50,60};
		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(10),coordinates);

		poly.assertEquals(linesBeforeEdit , poly.getLinesOfPolyLine(1, 1).size(),"Checkpoint[2/2]" ,"Verify that user can't perform edit by dropping invalid point" );

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE917", "Positive"})
	public void test07_DE917_TC3784_verifyRightClickContours() throws  InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that contour should not get highlighted on right click");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,12),"Checkpoint[1/2]","Verifying the halo ring presence on mouse hover");
		poly.performMouseRightClick(poly.getLinesOfPolyLine(1, 1).get(10));

		poly.assertFalse(poly.verifyClosedPolylineIsSelected(1, 1), "Checkpoint[2/2]","Verify that contour should not get highlighted on right click");

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1017", "Sanity", "Positive"})
	public void test08_DE1017_TC4132_TC4148_VerifySegmentAcceptedAfterEdition() throws InterruptedException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of RT : Rejected / Pending Segment getting accepted after edition");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/12]", "Verify on clicking first contour of first segment should accept all contours");	

		//fetching total of pending finding from finding bubble/badge
		//int totFinding = viewerPage.getNumberOfFindingsOnBadge();

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(4);

		//clicking on accept button from A/R bar
		drt.selectRejectfromGSPSRadialMenu();

		//opening finding table in viewbox (1)
		drt.openFindingTableOnBinarySelector(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on clicking state icon color changed to red");

		//verifying segment rejected
		drt.assertTrue(drt.verifyRejectedRTSegment(4),"Checkpoint[2/12]", "Verifying state icon is rejected");	

		drt.click(drt.getViewPort(1));
		//clicking on PREV '<' from as accept reject toolbar
		drt.navigateToFirstContourOfSegmentation(4);

		//fetching total number of lines in polyline
		int linesBeforeEdit = poly.getLinesOfPolyLine(1, 4).size();

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,4,12),"Checkpoint[3/12]","Verifying the halo ring is displayed on mouse hover");

		//editing polyline
		poly.editPolyLine(poly.getLinesOfPolyLine(1, 4).get(1),poly.getLinesOfPolyLine(1, 4).get(30));

		poly.waitForTimePeriod(2000);

		//verifying that number of lines in polyline is less after editing
		poly.assertTrue(linesBeforeEdit > poly.getLinesOfPolyLine(1, 4).size(),"" ,"Verifying that edit contour is done successful" );

		//verifying segment accepted
		drt.assertTrue(drt.verifyAcceptedRTSegment(4), "Checkpoint[4/12]","Verifying state icon is accepted");	


		int linesBeforeEditOfPendingSegement = poly.getLinesOfPolyLine(1, 3).size();

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,3,12),"Checkpoint[5/12]","Verifying the halo ring is displayed on mouse hover");

		//verifying pending RT segement state change into accepted after editing
		poly.editPolyLine(poly.getLinesOfPolyLine(1, 3).get(1),poly.getLinesOfPolyLine(1, 3).get(10));

		poly.waitForTimePeriod(2000);

		poly.assertTrue(linesBeforeEditOfPendingSegement > poly.getLinesOfPolyLine(1, 3).size(),"Checkpoint[6/12]" ,"Verifying that edit contour is done successful" );

		//verifying segment accepted
		drt.assertTrue(drt.verifyAcceptedRTSegment(4),"Checkpoint[7/12]", "Verifying state icon is accepted");	

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,3,12),"Checkpoint[8/12]","Verifying the halo ring is displayed on mouse hover");

		poly.editPolyLine(poly.getLinesOfPolyLine(1, 3).get(1),poly.getLinesOfPolyLine(1, 3).get(15));

		poly.waitForTimePeriod(2000);

		poly.assertTrue(linesBeforeEditOfPendingSegement > poly.getLinesOfPolyLine(1, 3).size(),"Checkpoint[9/12]" ,"Verifying that edit contour is done successful" );

		//verifying segment accepted
		poly.assertTrue(drt.verifyAcceptedRTSegment(2),"Checkpoint[10/12]", "Verifying state icon is accepted");	

		poly.pressKeys(Keys.ENTER);

		poly.waitForTimePeriod(3000);

		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);

		//navigation to first contour of segment (here, segment 2)
		drt.navigateToFirstContourOfSegmentation(2);

		//verifying segment accepted
		poly.assertTrue(drt.verifyAcceptedRTSegment(2),"Checkpoint[11/12]", "Verifying state icon is accepted");	

		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[12/12]","Verifying the RT is accepted");			
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1017", "Sanity", "Positive"})
	public void test09_DE1017_TC4152_VerifyCloningCopyEdition() throws InterruptedException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Validating cloning copy edition");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		//		ContentSelector contentSelector = new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify on clicking first contour of first segment should reject all contours");	

		//navigation to first contour of segment (here, segment 5)
		drt.navigateToFirstContourOfSegmentation(5);

		//clicking on accept button from A/R bar
		drt.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify on clicking state icon color changed to red");

		//verifying segment rejected
		drt.assertTrue(drt.verifyRejectedRTSegment(5), "Checkpoint[3/5]","Verifying state icon is rejected");	

		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);

		//navigation to first contour of segment (here, segment 5)
		drt.navigateToFirstContourOfSegmentation(5);

		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(1),poly.getLinesOfPolyLine(1, 1).get(15));

		//verifying segment rejected
		drt.assertTrue(drt.verifyRejectedRTSegment(5), "Checkpoint[4/5]","Verifying state icon is accepted");	

		/*List<String> resultName =  contentSelector.getAllResultDesciptionFromContentSelector(1);
		System.out.println(resultName);
		viewerPage.assertTrue(contentSelector.validateResultIsSelectedOnContentSelector(1, resultName.get(0)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");*/
		drt.mouseHover(80, 80);
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[5/5]","Verifying the RT is accepted");


	}

	@Test(groups ={"firefox","Chrome","IE11","US986","Negative"})
	public void test10_US986_TC4211_verifyRTWithRightClick() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM RT :Verify that Segment should not get highlighted when user perform right click on any contour of any slice");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		poly = new PolyLineAnnotation(driver);

		drt.navigateToFirstContourOfSegmentation(5);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify thickness of contour (like selected) should not get change when user perform right click" );

		drt.performMouseRightClick(poly.getAllPolylines(1).get(0));
		drt.assertTrue(poly.getPolyLineSvg(1, 1).getAttribute(NSGenericConstants.STROKE_WIDTH).equalsIgnoreCase(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH),"Verify segment not highlighted when user perform right click","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify thickness of contour (like selected) should not get change when user perform right click after performing scroll" );
		int defaultScrollPosition=drt.getCurrentScrollPositionOfViewbox(1);
		drt.scrollDownToSliceUsingKeyboard(5);
		int ChangeScrollPosition=drt.getCurrentScrollPositionOfViewbox(1);
		drt.assertNotEquals(defaultScrollPosition, ChangeScrollPosition, "On performing scroll new slice displayed","Verified");
		drt.performMouseRightClick(poly.getAllPolylines(1).get(0));
		drt.assertTrue(poly.getPolyLineSvg(1, 1).getAttribute(NSGenericConstants.STROKE_WIDTH).equalsIgnoreCase(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH),"Verify on performing scroll ,segment not highlighted when user perform right click","Verified");
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1017", "Positive"})
	public void test11_DE1080_TC4496_VerifyGSPSAnnotationAcceptRejectBehaviorOnRTstruct() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS annotation is not getting rejected when user creates annotation on RTStruct and tries to reject it, rather first contour is getting rejected");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		int[] coordinates = new int[] {-90,-90,90,-90,90,90,-90,90,-90,-90};

		drt.scrollDownToSliceUsingKeyboard(1, 2);

		//selecting polyline from radial menu
		poly.selectPolylineFromQuickToolbar(1);

		//drawing closed polyline
		poly.drawClosedPolyLine(1, coordinates);

		poly.pressKeys(Keys.ARROW_RIGHT);

		poly.pressKeys(Keys.ARROW_LEFT);

		//selecting reject from A/R bar
		poly.selectRejectfromGSPSRadialMenu(1);

		//clicking on prev button of A/R bar
		poly.selectPreviousfromGSPSRadialMenu(1);

		//finding the polyline number which is highlighted / focused
		int polyLineNumber = poly.getFocusedPolylineNumber();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verifying polyline is selected and rejected");

		//verifying polyline is rejected
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1,polyLineNumber), "Verifying polyline is selected and rejected", "Verified polyline is selected and rejected");
	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1017", "Positive"})
	public void test12_DE1080_TC4579_TC4580_VerifyStateOfContourAfterAcceptOrReject() throws InterruptedException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification state of the annotation or contour on toolbar"  + 
				"<br> Verify accepting or rejecting RtStruct Contour accept/reject the selected contour");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify on clicking first contour of first segment should accept all contours");	

		//navigation to first contour of segment (here, segment 5)
		drt.navigateToFirstContourOfSegmentation(1);

		//perform scroll (so that it is not first contour of segment number 5) 
		drt.scrollDownToSliceUsingKeyboard(1, 2);

		//performing left click on contour
		poly.openGSPSRadialMenuPolyLine(poly.getLinesOfPolyLine(1, 1).get(0),true);

		//clicking on reject button from A/R bar
		drt.selectRejectfromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify on clicking state icon color changed to red");

		//verifying segment rejected
		drt.assertTrue(drt.verifyRejectedRTSegment(1), "Checkpoint[3/7]","Verifying state icon is rejected");	

		drt.mouseHover(drt.getViewPort(1));
		//selecting circle from context menu
		circle.selectCircleFromQuickToolbar(1);

		//draing circle
		circle.drawCircle(1, 50, 60, 100, 100);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify on drawing circle state is accepted");

		//verifying circle state is accepted on drawing
		drt.assertTrue(circle.verifyAcceptGSPSRadialMenu(), "Checkpoint[5/7]","Verifying state is accepted");

		//Again verifying contour state
		poly.openGSPSRadialMenuPolyLine(poly.getLinesOfPolyLine(1, 1).get(0),true);

		//verifying segment rejected
		//Failed because of DE1232
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify on state of segment is rejected");
		drt.assertTrue(drt.verifyRejectedRTSegment(1),"Checkpoint[6/7]", "Verifying state icon is rejected");	
		drt.assertTrue(drt.verifyRejectGSPSRadialMenu(),"Checkpoint[7/7]", "Verifying state is rejected");
	}

	//DE1065:Contours start showing on source series when user Accepts/rejects the contours post layout change
	@Test(groups = {"Chrome", "IE11", "Edge", "DE1065", "Positive"})
	public void test13_DE1065_TC4639_VerifyContoursNotSeenOnOriginalSeries() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Contours does not show in the original series when user Accept/reject the contours after changing layout");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		//change layout to 1*2
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		drt.waitForViewerpageToLoad();

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		//clicking on segment and navigating to first contour of segment (3)
		drt.navigateToFirstContourOfSegmentation(3);

		//store value of slice position and Finding count
		int firstLegend = drt.getCurrentScrollPositionOfViewbox(1);
		int badgeCount=drt.getBadgeCountFromToolbar(1);	

		//rejecting segment by clicking on reject button of A/R bar
		drt.closedFindingsMenu(1);
		drt.selectRejectfromGSPSRadialMenu();
		//verifying segment rejected
		drt.assertTrue(drt.verifyRejectedRTSegment(3),"Checkpoint[1/5]", "Verifying state icon is rejected for 3rd segment");	

		//store value of scroll position after click on reject 
		int scrollAfterReject = drt.getCurrentScrollPositionOfViewbox(1);
		drt.mouseHover(drt.getViewPort(1));

		drt.assertEquals(drt.getBadgeCountFromToolbar(1), badgeCount-1, "Checkpoint[2/5]", "Verifying the badge count is reduced");

		drt.assertNotEquals(firstLegend, scrollAfterReject, "Checkpoint[3/5]", "After clicking on 'Reject' icon, first contour of next segment should be rendered");

		drt.assertEquals(scrollAfterReject,drt.getCurrentScrollPositionOfViewbox(2), "Checkpoint[4/5]", "After clicking on 'Reject' icon, first contour of next segment should be rendered on source series");

		drt.assertEquals(poly.getPolylineCount(2),0, "Checkpoint[5/5]", "Verify no contours present on source series");

	}

	@Test(groups = {"Chrome", "IE11", "Edge", "DE1397", "Positive"})
	public void test14_DE1397_TC5723_VerifySliderWhenSegmentIsEdited() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Hardening] : Error found in TC5303: RT :  Pending (white / grey) marker is not getting turns into green on scroll slider when user edits pending RT segment");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		drt.waitForDICOMRTToLoad();

		// Getting the count of Segment which is present thourgh out
		//		int currentScroll=poly.getCurrentScrollPositionOfViewbox(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Navigating to first Contour of 5th segment");	
		drt.navigateToFirstContourOfSegmentation(1);
		//		int count = currentScroll-poly.getCurrentScrollPositionOfViewbox(1);
		List<WebElement> markersBefore = poly.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR);
		int linesBeforeEdit = poly.getLinesOfPolyLine(1, 1).size();

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,12),"Checkpoint[2/7]","Verifying the halo ring is displayed on mouse hover");
		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(1),poly.getLinesOfPolyLine(1, 1).get(30));

		poly.waitForTimePeriod(2000);
		poly.assertTrue(linesBeforeEdit > poly.getLinesOfPolyLine(1, 1).size(),"Checkpoint[3/7]" ,"Verifying that edit contour is done successful" );
		poly.assertTrue(drt.verifyAcceptedRTSegment(1), "Checkpoint[4/7]","Verifying state icon is accepted");	

		drt.assertEquals(drt.getFindingState(drt.getTextOfFindingFromTable(1)),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Checkpoint[5/7]", "Verifying that finding state is also changed in finding table");
		List<WebElement> markers = poly.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		drt.assertEquals(markers.size(),markersBefore.size(),"Checkpoint[6/7]","verifying the markers are same post editing as segment is present on all the slices");

		for(int i =0;i<10;i++)
			drt.assertTrue(drt.getFindingsNameFromSliderContainer(1, i+1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).contains(drt.getNameOfAcceptedRejectedSegment(1)),"Checkpoint[7/7]","verifying the segment is present in container");


	}

	@Test(groups = {"chrome", "IE11", "Edge", "DE1904","DE1920", "Sanity", "Positive"})
	public void test15_DE1904_TC7602_DE1920_TC7860_VerifyRTContourFocusShiftedOnStateChange() throws InterruptedException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of contour navigation on state change.<br>"+
		"Verify the view boxes are not duplicated/loaded with the latest result when a layout is changed after performing accept/reject for RT legend.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		cs=new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		
		int resultCount=cs.getAllResults().size();
        int legendOption=drt.getLegendOptionsList(1).size();
        
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify on accepting the contour the focus moves to the next contour");	
		drt.navigateToFirstContourOfSegmentation(1);
		drt.selectAcceptfromGSPSRadialMenu();
		drt.assertTrue(drt.verifyPendingGSPSToolbarMenu(),"Checkpoint[2/6]","Verifying the focus moved to next contour");

		drt.selectPreviousfromGSPSRadialMenu();
		drt.assertTrue(drt.verifyAcceptGSPSRadialMenu(),"Checkpoint[3/6]","Verifying that AR toolbar displays green icon after accepting the contour");
		drt.waitForTimePeriod(3000);
		drt.assertNotEquals(cs.getAllResults().size(), resultCount, "Checkpoint[4/6]", "Verified that new clone is created after accepting the contour.");
		
		drt.click(drt.getViewPort(1));
		layout.selectLayout(layout.twoByOneLayoutIcon);
		drt.openFindingTableOnBinarySelector(1);
		drt.assertEquals(drt.getStateSpecificFindings(1,ViewerPageConstants.PENDING_FINDING_COLOR).size(), legendOption-1, "Checkpoint[5/6]", "Verified that clone copy is loaded in first viewbox.");
		drt.click(drt.getViewPort(1));
		drt.click(drt.getViewPort(2));
		drt.openFindingTableOnBinarySelector(2);
		drt.assertEquals(drt.getStateSpecificFindings(2,ViewerPageConstants.PENDING_FINDING_COLOR).size(), legendOption, "Checkpoint[6/6]", "Verified that default result copy loaded for RT in second viewbox.");
		

	}

	@Test(groups = {"chrome", "IE11", "edge", "DE1904", "Sanity", "Positive"})
	public void test16_DE1904_TC7603_VerifyNavigationThroughGSPSAndRTContours() throws InterruptedException   {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the navigation through GSPS and RT findings when User drawn GSPS is available over RT Struct");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		pointAn = new PointAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);

		drt.scrollToImage(1,45);
		//Drawing the annotation on different slices
		pointAn.selectPointFromQuickToolbar(1);

		pointAn.drawPointAnnotationMarkerOnViewbox(1,-350,-350);
		ellipse = new EllipseAnnotation(driver);
		textAnno=new TextAnnotation(driver);
		circle=new CircleAnnotation(driver);
		poly=new PolyLineAnnotation(driver);
		//Draw a Ellipse on View Box1
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -100, -350, -100,-100);	
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TextAnnotation_FirstViewbox";

		textAnno.drawText(1, 100, -350, myText);

		drt.scrollToImage(1,60);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -300, -300,-100,-100);	

		drt.scrollToImage(1,130);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -200, -350, -100,-100);	

		drt.scrollToImage(1, 264);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, -350, -350, -100,-100);	

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -350,-30,-30);	

		pointAn.selectPointFromQuickToolbar(1);
		pointAn.drawPointAnnotationMarkerOnViewbox(1,10,-350);

		drt.scrollToImage(1, 290);
		pointAn.drawPointAnnotationMarkerOnViewbox(1,10,-350);
		
		drt.scrollToImage(1, 45);
		pointAn.selectPoint(1, 1);
		drt.waitForElementVisibility(drt.getAcceptRejectToolBar(1));
		
		//Verifying the navigations and state through AR toolbar next button
		drt.assertTrue(pointAn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[1/20]","Verified that point annotation is currently accepted GSPS with AR toolbar next event");

		drt.selectNextfromGSPSRadialMenu();
		drt.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[2/20]", "Verified that ellipse annotation is currently accepted GSPS with AR toolbar next event");
		
		drt.selectNextfromGSPSRadialMenu();
		drt.assertTrue(textAnno.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[3/20]", "Verified that text annotation is currently accepted GSPS with AR toolbar next event");
		
		drt.selectNextfromGSPSRadialMenu();
		drt.assertTrue(drt.verifyPendingRTSegment(2),"Checkpoint[4/20]", "Verified that the Aorta RT segment is in pending state with AR toolbar next event");	
		
		drt.selectNextfromGSPSRadialMenu();
		drt.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/20]","Verified that circle annotation is currently accepted GSPS with AR toolbar next event");
		
		drt.selectNextfromGSPSRadialMenu();
		drt.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[6/20]", "Verified that ellipse annotation is currently accepted GSPS with AR toolbar next event");
		
		drt.scrollToImage(1, 45);
		pointAn.selectPoint(1, 1);
		drt.waitForElementVisibility(drt.getAcceptRejectToolBar(1));
		
		//Verifying the navigations and state through keyboard forward button
		drt.assertTrue(pointAn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[7/20]","Verified that point annotation is currently accepted GSPS with AR toolbar next event");
		
		drt.navigateGSPSForwardUsingKeyboard();
		drt.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[8/20]", "Verified that ellipse annotation is currently accepted GSPS with keyboard next event");
		
		drt.navigateGSPSForwardUsingKeyboard();
		drt.assertTrue(textAnno.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[9/20]", "Verified that text annotation is currently accepted GSPS with keyboard next event");
		
		drt.navigateGSPSForwardUsingKeyboard();
		drt.assertTrue(drt.verifyPendingRTSegment(2),"Checkpoint[10/20]", "Verified that the Aorta RT segment is in pending state with keyboard next event");	
		
		drt.navigateGSPSForwardUsingKeyboard();
		drt.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[11/20]","Verified that circle annotation is currently accepted GSPS with keyboard next event");
		
		drt.navigateGSPSForwardUsingKeyboard();
		drt.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[12/20]", "Verified that ellipse annotation is currently accepted GSPS with keyboard next event");

		drt.scrollToImage(1, 45);
		pointAn.selectPoint(1, 1);
		drt.selectPreviousfromGSPSRadialMenu();
		
		//Verifying the navigations and state through AR toolbar previous button
		drt.assertTrue(drt.verifyPendingRTSegment(1),"Checkpoint[13/20]", "Verified that the External RT segment is in pending state with AR toolbar previous event");	
		
		drt.selectPreviousfromGSPSRadialMenu();
		drt.assertTrue(drt.verifyPendingRTSegment(9),"Checkpoint[14/20]", "Verified that the Prostate RT segment is in pending state AR toolbar previous event");	
	
		drt.scrollToImage(1, 45);
		pointAn.selectPoint(1, 1);
		
		drt.navigateGSPSBackUsingKeyboard();
		
		//Verifying the navigations and state through keyboard backward button
		drt.assertTrue(drt.verifyPendingRTSegment(1),"Checkpoint[15/20]", "Verified that the External RT segment is in pending state using keyboard previous event");	
		
		drt.navigateGSPSBackUsingKeyboard();
		drt.assertTrue(drt.verifyPendingRTSegment(9),"Checkpoint[16/20]", "Verified that the Prostate RT segment is in pending state using keyboard previous event");	
	
		drt.scrollToImage(1, 45);
		pointAn.selectPoint(1, 1);
		drt.scrollDownGSPSUsingKeyboard();
	
		//Verifying the navigations and state through PgDown button
		drt.assertTrue(drt.verifyPendingRTSegment(2),"Checkpoint[17/20]", "Verified that the Aorta RT segment is in pending state with AR toolbar next event");	
	
		drt.scrollDownGSPSUsingKeyboard();
		drt.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[18/20]","Verified that circle annotation is currently accepted GSPS with keyboard next event");
		
		drt.scrollUpGSPSUsingKeyboard();
		
		//Verifying the navigations and state through PgUp button
		drt.assertTrue(drt.verifyPendingRTSegment(2),"Checkpoint[19/20]", "Verified that the Aorta RT segment is in pending state with AR toolbar next event");	
		
		drt.scrollUpGSPSUsingKeyboard();
		drt.assertTrue(pointAn.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[20/20]","Verified that point annotation is current active pending GSPS");
		
	}

	//DE1970 is covered under US1413
	
	@Test(groups = {"Chrome", "IE11", "Edge", "DE1970", "US1413","Positive"})
	public void test17_DE1970_US1413_TC7943_verifyEditingOfContoursPersistOnReload() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DE1970: Verify that multiple edits made on same contour are persisted on reload.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		int []coordinates = new int[] {-100,0,120,0};
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		List<WebElement> linesBeforeEdit = poly.getLinesOfPolyLine(1, 1);
//		poly.editPolyLine(linesBeforeEdit.get(10),coordinates, linesBeforeEdit.get(50));
		poly.editPolyLine(linesBeforeEdit.get(10), linesBeforeEdit.get(200));
		poly.assertTrue(linesBeforeEdit.size() > poly.getLinesOfPolyLine(1, 1).size(),"Checkpoint[1/6]" ,"Verifying that edit contour is done successful" );
		
		poly.click(poly.getViewPort(1));
		
		coordinates = new int[] {-30,-40,-50,-60};
		linesBeforeEdit = poly.getLinesOfPolyLine(1, 2);
		poly.editPolyLine(linesBeforeEdit.get(10), coordinates,linesBeforeEdit.get(45));
		poly.assertTrue(linesBeforeEdit.size() > poly.getLinesOfPolyLine(1, 1).size(),"Checkpoint[2/6]" ,"Verifying that edit contour is done successful" );
		
		poly.click(poly.getViewPort(1));
		
		coordinates = new int[] {30,40,50,60};
		linesBeforeEdit = poly.getLinesOfPolyLine(1, 2);
		poly.editPolyLine(linesBeforeEdit.get(20), coordinates,linesBeforeEdit.get(30));
		poly.assertTrue(linesBeforeEdit.size() > poly.getLinesOfPolyLine(1, 1).size(),"Checkpoint[3/6]" ,"Verifying that edit contour is done successful" );
		
		drt.click(drt.getViewPort(1));		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Taking the screenshot post multiple editing");
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		poly.takeElementScreenShot(poly.getViewerCanvas(1), newImagePath+"/goldImages/rtBeforechange.png");
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);
		
		drt.click(drt.getViewPort(1));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Taking the screenshot post multiple editing and viewe reload");
		poly.takeElementScreenShot(poly.getViewerCanvas(1), newImagePath+"/actualImages/rtpostchange.png");
		
		String expectedImagePath = newImagePath+"/goldImages/rtBeforechange.png";
		String actualImagePath = newImagePath+"/actualImages/rtpostchange.png";
		String diffImagePath = newImagePath+"/diffImages/diff.png";
		
		boolean cpStatus =  poly.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		poly.assertTrue(cpStatus, "Checkpoint[4/6]","Verifying that post multiple changes and viewer reload the changes persist");
		
		cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();
		cs.selectResultFromSeriesTab(1, results.get(0));
		
		drt.click(drt.segmentstateicon.get(1));
		
		linesBeforeEdit = poly.getLinesOfPolyLine(1, 2);
		poly.editPolyLine(linesBeforeEdit.get(2),linesBeforeEdit.get(20));		
		poly.assertTrue(drt.verifyAcceptedRTSegment(2), "Checkpoint[5/6]", "Verifying after editing of segment,its state is changed to accepted");
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadRTData(patientNameDICOMRT, 1, 1);
		
		poly.assertTrue(drt.verifyAcceptedRTSegment(2), "Checkpoint[6/6]", "Verifying ssegment state is persisted on reload");
		
	}
	
	//DR1998:Unable to open the radial menu when user clicks on the AR toolbar holder space on source series
	@Test(groups = {"Chrome", "IE11", "Edge","DR1998","Negative"})
	public void test18_DR1998_TC8185_verifyRadialMenuOnARToolbarHolderPlaceForSeries() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to load  radial menu on AR tool bar holder space on source series.");

		//Loading patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameDICOMRT+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameDICOMRT,1, 1);
		
		cs=new ContentSelector(driver);
		
		drt.assertTrue(drt.isAcceptRejectToolBarPresent(1), "Checkpoint[1/4]", "Verified that A/R toolbar visible on Result series.");
		
		String seriesName=cs.getAllSeries().get(0);
		//load series from CS on the same viewbox.
		cs.selectSeriesFromSeriesTab(1, seriesName);
		
		drt.assertFalse(drt.isAcceptRejectToolBarPresent(1), "Checkpoint[2/4]", "Verified that A/R toolbar not visible on source series.");
	    drt.performMouseRightClick(drt.getViewPort(1),80, 400);
		drt.assertTrue(drt.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[3/4]", "Verified that radial menu open on AR toolbar space holder on source series.");
		
		drt.click(drt.getViewPort(1));
	    drt.performMouseRightClick(drt.getViewPort(1),-80, 400);
		drt.assertTrue(drt.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[4/4]", "Verified that radial menu open on AR toolbar space holder on source series again after closing.");

	}
	
	@AfterMethod(alwaysRun=true)
	public void afterTest() throws SQLException {
		db = new DatabaseMethods(driver);
		db.deleteRTafterPerformingAnyOperation(Configurations.TEST_PROPERTIES.get("nsUserName"));
	}

}
