package com.trn.ns.test.viewer.GSPS;

import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class FreeHandPolylineAnnotationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewBoxToolPanel presetMenu;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private PolyLineAnnotation poly;
	
	String LIDC_IDRI_0012_path =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDC_IDRI_0012 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDC_IDRI_0012_path);

	String filePath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);
	
	String IBL_JohnDoe =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IBL_JohnDoe);

	
	private PointAnnotation point;
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"IE11","Chrome","US260","Positive","BVT"})
	public void test01_US260_TC3402_verifyFreeHandPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("draw free hand");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the free hand polyline on viewbox 1");
		poly.drawFreehandPolyLine(1, abc);
		
		poly.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Drawing the free hand polyline on viewbox 2");
		poly.drawFreehandPolyLine(2, abc);
		
		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[3/4]","Verifying the free hand polyline presence on viewbox1");

		poly.assertEquals(poly.getPolylineCount(2),1,"Checkpoint[4/4]","Verifying the free hand polyline presence on viewbox2");
	}

	@Test(groups ={"IE11","Chrome","US260","Positive"})
	public void test02_US260_TC3402_verifyFreeHandPolylineExitUsingESCKey() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("draw free hand");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the free hand polyline on viewbox 1 existing using ESC key");
		poly.drawFreehandPolyLineExitUsingESC(1, abc);

		poly.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Drawing the free hand polyline on viewbox 2 existing using ESC key");
		poly.drawFreehandPolyLineExitUsingESC(2, abc);

		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[3/4]","Verifying the free hand polyline presence on viewbox1");

		poly.assertEquals(poly.getPolylineCount(2),1,"Checkpoint[4/4]","Verifying the free hand polyline presence on viewbox2");
	}

	@Test(groups ={"IE11","Chrome","US260","Positive","US820","US2325","DR2796","F1090","E2E"})
	public void test03_US260_TC3403_US820_TC3439_US2325_TC9777_DE2796_TC10763_TC10775_verifyDrawPolyLineUsingCntrlExitUsingESC() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("draw polyline"
				+ "<br> Verify that user should be able to draw polyline and Finish (by double click or By ESC keypress) Polyline"
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar. <br>"+
				"Verify that a point is not dropped itself at view box top left corner upon creating classic polyline. <br>"+
				"Verify that the created classic polyline is displayed at the same location before and after on up.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		poly.assertTrue(poly.checkCurrentSelectedIcon(ViewerPageConstants.POLYLINE),"Checkpoint[1.1/8]","Verifying Polyline icon is selected in quick toolbar");
		
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIcon(ViewerPageConstants.POLYLINE),"Checkpoint[1.2/8]", "Verifying Polyline icon is selected in viewer bar");
	
		int[] abc = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1.3/8]", "Drawing the polyline with cntrl key on viewbox 1 existing using ESC key");
		poly.drawPolyLineExitUsingESCKey(1, abc);
		
		poly.closingConflictMsg();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/8]", "Drawing the polyline with cntrl key on viewbox 2 existing using ESC key");
		poly.drawPolyLineExitUsingESCKey(2, abc);

		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[3/8]","Verifying the polyline presence on viewbox1");

		poly.assertEquals(poly.getPolylineCount(2),1,"Checkpoint[4/8]","Verifying the polyline presence on viewbox2");
		
		//TC10775: Verify that the created classic polyline is displayed at the same location before and after on up.
		poly.closingConflictMsg();
		poly.doubleClick(poly.getViewPort(1));
		poly.waitForViewerpageToLoad();
		int handlePoint=poly.getPolylineHandles(1).size();
		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[5/8]","Verifying the polyline presence on viewbox1 on one up.");
		poly.assertEquals(poly.getPolylineHandles(1).size(), handlePoint, "Checkpoint[6/8]", "Verified polyline handle remain same.");
		poly.compareElementImage(protocolName,poly.getViewbox(1),"Verified that Polyline is present on after one up.","TC03_PolylineAfterOneUp");
		
		poly.doubleClick(poly.getViewPort(1));
		poly.assertEquals(poly.getPolylineHandles(1).size(), handlePoint, "Checkpoint[7/8]", "Verifying the polyline presence on viewbox1 on one up.");
		poly.assertEquals(poly.getPolylineHandles(2).size(), handlePoint, "Checkpoint[8/8]", "Verifying the polyline presence on viewbox2 on one up.");
		
	}

	@Test(groups ={"IE11","Chrome","US260","Positive","US820","DE1285"})
	public void test04_US260_TC3403_US820_TC3439_DE1285_TC5331_verifyDrawPolyLineUsingCntrlExitUsingDoubleClick() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("draw polyline"
				+ "<br> Verify that user should be able to draw polyline and Finish (by double click or By ESC keypress) Polyline. <br>"
				+ "Verify that all handles of classic polyline should get displayed after drawing");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);

		int[] abc = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		int handles=poly.getPolylineHandles(1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Drawing the polyline with cntrl key on viewbox 2 existing using double click");
		poly.closingConflictMsg();
		poly.mouseHover(poly.getViewPort(2));
		poly.click(poly.getViewPort(2));
		
		poly.drawPolyLineExitUsingDoubleClickKey(2, abc);
		poly.mouseHover(poly.getViewPort(1));
		poly.click(poly.getViewPort(1));
		
		poly.closingConflictMsg();
		poly.selectZoomFromQuickToolbar(1);
		
		

		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[3/5]","Verifying the polyline presence on viewbox1");
		poly.assertEquals(poly.getPolylineCount(2),1,"Checkpoint[4/5]","Verifying the polyline presence on viewbox2");

		poly.mouseHover(poly.getViewPort(3));
		poly.selectClassicPolylineWithCtrlLeft(1, 1);
		poly.assertEquals(poly.getPolylineHandles(1).size(), handles, "Checkpoint[5/5]", "Verified all handles of polyline is highlighted on viewbox1.");

	}

	@Test(groups ={"IE11","Chrome","US260","Negative","US2329","F1090","E2E"})
	public void test05_US260_TC3405_US2329_TC10165_drawPolyLinewithoutPressingCntrlKey() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Cannot draw Polyline. <br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		poly = new PolyLineAnnotation(driver);
		point = new PointAnnotation(driver);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 20);

		poly.closingConflictMsg();
		poly.mouseHover(poly.getViewPort(1));
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Getting Findings");
		poly.openFindingTableOnBinarySelector(1);
		String myTest = poly.findingTable.getText();

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25};		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the polyline without pressing cntrl key on viewbox 1");
		poly.drawPolyLineWithoutPressingCntrlKey(1, abc);
		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));
		
//		poly.assertEquals(poly.getPolylineCount(1),0,"Checkpoint[2/4]","Verifying the polyline absence on viewbox1");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Opening the findings present in toolbar");
		poly.openFindingTableOnBinarySelector(1);
		poly.assertEquals(poly.findingTable.getText(),myTest,"Checkpoint[4/4]","Verifying that findings are same before drawing of polyline without cntrl key pressed");


	}

	@Test(groups ={"IE11","Chrome","US260","Negative"})
	public void test06_US260_TC3405_verifyDeletionOfFreeHandPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Delete Freehand drawing and Polyline");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPage(liver9PatientName, "", 1, 1);
				
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc1 = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		int[] abc2 = new int[] {-10,-44,20,-32,37,-23,37,-9,-7,-34};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/6]", "Drawing polyline on viewbox 1 ");
		poly.drawFreehandPolyLineExitUsingESC(1, abc1);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/6]", "Drawing another polyline on viewbox 1");
		poly.drawFreehandPolyLineExitUsingESC(1, abc2);

		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[3/6]","Verifying the two polylines presence on viewbox1");	


		poly.deleteSelectedPolyline();

		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[4/6]","Verifying the one polyline presence on viewbox1 post deletion");

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		poly.assertEquals(poly.getPolylineCount(1),1,"Checkpoint[5/6]","Verifying the one polyline presence on viewbox1 post reload viewer");

		poly.deleteAllAnnotation(1);

		poly.assertEquals(poly.getPolylineCount(1),0,"Checkpoint[6/6]","Verifying the one polyline absence on viewbox1 post delete all annotation");

	}

	@Test(groups ={"IE11","Chrome","US260","Positive","US950"})
	public void test07_US260_TC3406_DE912_TC3512_US950_TC4300_TC4303_verifyChangeStateOfDrawnPolyline() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("change state of Freehand and Polyline " + "<br> Verify that polyline accept/reject state should get save"
				+ "<br> Verfiy that yellow shadow should get display when user finishes drawing, editing and on selection"
				+ "<br> Verfiy that annotation should not be bold / thick when user finishes drawing, editing and on selection");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc1 = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};
//		int[] abc2 = new int[] {20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		int[] abc2 = new int[] {20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/20]", "Drawing polyline on viewbox 1 ");
		poly.drawFreehandPolyLineExitUsingESC(1, abc1);		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/20]", "Drawing another polyline on viewbox 1");
		poly.drawFreehandPolyLineExitUsingESC(1, abc2);		
		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[3/14]","Verifying the two polylines presence on viewbox1");	

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/20]", "Rejecting the first polyline");
		poly.selectRejectfromGSPSRadialMenu(1);			
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[4/20]","Verifying the polyline is rejected");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[4/20]","Verifying the polyline is rejected");
	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/20]", "Rejecting another polyline");
		poly.selectRejectfromGSPSRadialMenu(1);		
		
		int totalPoly = poly.getAllPolylines(1).size();
		int whichPoly = poly.getPolylineWhichIsFocused(1);
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, whichPoly),"Checkpoint[6/20]","Verifying another polyline is rejected");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, (totalPoly-whichPoly+1)),"Checkpoint[4/20]","Verifying the polyline is rejected");
		
		//Logout and relogin to check persistance
		Header header = new Header(driver);
		header.logout();
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
		
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2),"Checkpoint[7/20]","Verifying another polyline is rejected after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[7/20]","Verifying another polyline is rejected after logout");
		
		helper.browserBackAndReloadViewer(liver9PatientName,  1, 1);
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[8/20]","Verifying another polyline is rejected after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2),"Checkpoint[8/20]","Verifying another polyline is rejected after logout");
				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[9/20]", "Rejecting the first polyline");
		poly.selectAcceptfromGSPSRadialMenu(1);		
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[10/20]","Verifying the polyline is accepted");		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 2),"Checkpoint[10/20]","Verifying the polyline is accepted");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[11/20]", "Accepting another polyline");
		poly.selectAcceptfromGSPSRadialMenu(1);
		 totalPoly = poly.getAllPolylines(1).size();
		 whichPoly = poly.getPolylineWhichIsFocused(1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, (totalPoly-whichPoly+1)),"Checkpoint[12/20]","Verifying another polyline is accepted");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, whichPoly),"Checkpoint[12/20]","Verifying another polyline is accepted");
		
		//Logout and relogin to check persistance
		header.logout();
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
	
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[13/20]","Verifying another polyline is accepted after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[13/20]","Verifying another polyline is accepted after logout");
		
		helper.browserBackAndReloadViewer(liver9PatientName,1, 1);
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[14/20]","Verifying another polyline is accepted after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[14/20]","Verifying another polyline is accepted after logout");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[15/20]", "Accepting first polyline again");
		
		poly.selectAcceptfromGSPSRadialMenu(1);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[16/20]","Verifying polyline is in pending state");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 2),"Checkpoint[16/20]","Verifying polyline is in pending state");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[17/20]", "Accepting second polyline again");
		poly.selectAcceptfromGSPSRadialMenu(1);		
		 totalPoly = poly.getAllPolylines(1).size();
		 whichPoly = poly.getPolylineWhichIsFocused(1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, (totalPoly-whichPoly+1)),"Checkpoint[18/20]","Verifying polyline is in pending state");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, whichPoly),"Checkpoint[18/20]","Verifying polyline is in pending state");
		
		//Logout and relogin to check persistance
		header.logout();
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerPageUsingSearch(liver9PatientName, 1, 1);
	
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[19/20]","Verifying polyline is in pending state after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[19/20]","Verifying polyline is in pending state after logout");
		
		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsPendingGSPS(1, 1),"Checkpoint[20/20]","Verifying polyline is in pending state after logout");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2),"Checkpoint[20/20]","Verifying polyline is in pending state after logout");

	}

	@Test(groups ={"IE11","Chrome","US260","Negative","Sanity","BVT"})
	public void test08_US260_TC3407_verifySwitchingROIAndPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("switching between ROI and polyline (vice versa)");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPage(liver9PatientName, "", 1, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);

		poly.doubleClickOnViewbox(1);
		
		int[] abc1 = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};
		int[] abc2 = new int[] {-36,23,-41,37,-42,42,-25,33};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/8]", "Drawing roi and polyline on viewbox 1");
		poly.drawPolyLineExitUsingESCKey(1, abc2);		
		poly.waitForTimePeriod(10000);
		poly.drawFreehandPolyLineExitUsingESC(1, abc1);				
		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[2/8]","Verifying the two polylines presence on viewbox1");	

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);		
		
		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[3/8]","Verifying the two polylines presence on viewbox1 on viewer reload");	

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		poly.selectPolylineFromQuickToolbar(3);		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/8]", "Drawing roi and polyline on viewbox 2");		
		poly.drawFreehandPolyLineExitUsingESC(3, abc1);		
		poly.drawPolyLineExitUsingESCKey(3, abc2);

		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[5/8]","Verifying the two polylines presence on viewbox1");	
		poly.assertEquals(poly.getPolylineCount(3),2,"Checkpoint[6/8]","Verifying the two polylines presence on viewbox2");	

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);	
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		poly.waitForViewerpageToLoad(1);
		
		poly.assertEquals(poly.getPolylineCount(1),2,"Checkpoint[7/8]","Verifying the two polylines presence on viewbox1 on viewer reload");	
		poly.assertEquals(poly.getPolylineCount(2),2,"Checkpoint[8/8]","Verifying the two polylines presence on viewbox2 on viewer reload");	

	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test09_US820_TC3440_TC3445_TC3552_DE912_TC3506_verifyMovementOfPolyLine() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to move Polyline after selecting it."
				+ "<br> Verify that user is able to move the polyline by ctrl + drag (left mouse down) event"
				+ "<br> Verify that new entry should get generated in result tab of content selector when user draw polyline"
				+ "<br> Verify that polyline annotation position should get save after moving");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(liver9PatientName,1,1);
		ViewerLayout layout = new ViewerLayout(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/6]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		
		poly.mouseHover(poly.getViewPort(2));
		poly.click(poly.getViewPort(2));
		poly.closingConflictMsg();
		
		cs = new ContentSelector(driver);
		List<String> latestResults = cs.getAllResults();
		poly.assertEquals(latestResults.size(),1,"Checkpoint[2/6]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			poly.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[3/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		
		poly.mouseHover(poly.getViewPort(2));
		
		String beforeX1=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);


		List<String> cursor = poly.movePolyLine(1, 1,-60,-60);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/6]", "Checkpoint TC2[1] : Verify new cross icon should display");
		for(String val : cursor)
			poly.assertEquals(val, NSGenericConstants.MOVE_CURSOR, "verifying that mouse cursor changes to cross icon", "verified");

		String afterX1=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify user is able to move polyline inside a Viewbox");
		poly.assertNotEquals(beforeX1,afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes ");
		poly.assertNotEquals(beforeY1,afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes  ");
		poly.assertNotEquals(beforeX2,afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes  ");
		poly.assertNotEquals(beforeY2,afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges ");


		// Testcase TC3506
		poly.browserBackWebPage();
		patientPage.clickOntheFirstStudy();
		poly.waitForViewerpageToLoad();		

		poly.assertNotEquals(poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X1),afterX1,"Verify X1 coordinate of line change on move","The X1 coordinate of line changes from "+beforeX1+" to "+afterX1);
		poly.assertNotEquals(poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y1),afterY1,"Verify Y1 coordinate of line change on move","The Y1 coordinate of line changes from "+beforeY1+" to "+afterY1);
		poly.assertNotEquals(poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.X2),afterX2,"Verify X2 coordinate of line change on move","The X2 coordinate of line changes from "+beforeX2+" to "+afterX2);
		poly.assertNotEquals(poly.getLinesOfPolyLine(1,1).get(0).getAttribute(ViewerPageConstants.Y2),afterY2,"Verify Y2 coordinate of line change on move","The Y2 coordinate of linechanges from "+beforeY2+" to "+afterY2);

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/6]", "Verify that user can see the polyline after changing layout");
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		poly.waitForViewerpageToLoad(1);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence post layout change", "verified");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1), "verifying polyline is highlighted post layout change", "verified");
		

	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test10_US820_TC3441_TC3513_TC3515_verifyPointDeletionFromPolyline() throws   InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify that user should be able to delete point from Polyline"
				+ "<br> Verify that radial menu should not get open when user performs right click for deleting points from polyline"
				+ "<br> Verify that polyline should not get highlighted when user performs right click for deleting points");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);

		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));

		int totalPoints = poly.getTransparentPolylineHandles(1,1).size();

		poly.deletePointFromPolyLine(1, 1, 2);

		poly.assertEquals((totalPoints-1), poly.getPolylineHandles(1).size(), "Checkpoint[2/5]: Verifying the deletion of point", "verified");
		poly.assertFalse(poly.isElementPresent(poly.quickToolbarMenu), "Checkpoint[3/5]: Verifying that radial menu is not opened when user tries to delete the point", "verified");
	//	poly.assertTrue(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[4/5]:verifying polyline is highlighted when user performs right click for deleting points ", "verified");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/5]:verifying polyline is ighlighted when user performs right click for deleting points ", "verified");

	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test11_US820_TC3442_verifyAdditionOfNewPointInPolyLine() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user wants to add new point between existing point new icon should get display in place of mouse pointer");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));

		int pointsBeforeAddition = poly.getTransparentPolylineHandles(1, 1).size();

		poly.addNewPointToPolyline(1, 1, 2);

		poly.assertEquals((pointsBeforeAddition), poly.getTransparentPolylineHandles(1, 1).size(), "Checkpoint[2/3]: Verifying the addition of new point", "verified");
        
		poly.assertTrue(poly.checkHaloRingIsDisplayed(1, 1, 4), "Checkpoint[3/3]", "Verifying the Halo ring is displayed");


	}

	@Test(groups ={"IE11","Chrome","US820","Positive","Sanity"})
	public void test12_US820_TC3444_TC3546_verifyAddPointsAndResizePolyLine() throws Exception 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can also add new points between two points at the time of resizing the lines between the two points"
				+ "<br> Verify that user can resize any point of polyline");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		
		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));

		int pointsBeforeAddition = poly.getTransparentPolylineHandles(1, 1).size();

		poly.addAndDragPoint(1, 1, 2,-10,-20);

		//poly.mouseHover(poly.getViewPort(2));

		poly.assertEquals((pointsBeforeAddition+1), poly.getTransparentPolylineHandles(1, 1).size(), "Checkpoint[2/4]: Verifying the addition of new point", "verified");

		poly.assertTrue(poly.checkHaloRingIsDisplayed(1, 1, 2), "Checkpoint[3/4]", "Verifying the Halo ring is displayed");

		poly.mouseHover(poly.getViewPort(2));

		poly.getTransparentPolylineHandles(1, 1).get(1).click();
		
		poly.waitForTimePeriod(1000);

		poly.dragAndReleaseOnViewer(poly.getPolylineHandles(1).get(4), 0, 0, 30, 30);

		poly.mouseHover(poly.getViewPort(2));

		poly.getTransparentPolylineHandles(1, 1).get(1).click();
	
		poly.waitForTimePeriod(1000);


		
		poly.assertEquals((pointsBeforeAddition)+1, poly.getPolylineHandles(1).size(), "Checkpoint[4/4]:Verify that user can resize any point of polyline", "verified");

		poly.assertEquals((pointsBeforeAddition+1), poly.getPolylineHandles(1).size(), "Checkpoint[4/4]:Verify that user can resize any point of polyline", "verified");




	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test13_US820_TC3447_verifyPolyLinePostZoom() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of Polyline will be change proportionally with image");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		presetMenu=new ViewBoxToolPanel(driver);

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/2]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying that polyline is drawn", "verified");
		poly.closingConflictMsg();	
		
		int zoomValue=presetMenu.getZoomValue(1);
		poly.selectZoomFromQuickToolbar(1);

		poly.dragAndReleaseOnViewer(poly.getViewbox(1), 0, 0, 50, 50);

		poly.assertTrue(presetMenu.getZoomValue(1)< zoomValue,"Verifying the zoom is done","verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/2]", "Validating the polyline is also zoomed");
		poly.compareElementImage(protocolName, poly.getViewPort(1), "verifying the polyline is zoomed in/out as well", "test13_Checkpoint2");




	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test14_US820_TC3448_verifyPolyLinePostPan() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user PAN the image size of annotation Polyline should not change and it should move with image");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/2]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying that polyline is drawn", "verified");

		poly.closingConflictMsg();	
		poly.selectPanFromQuickToolbar(1);

		poly.dragAndReleaseOnViewer(poly.getViewPort(1), 0, 0, 20, 30);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/2]", "Validating the polyline is not panned");
		poly.closeNotification();
		poly.compareElementImage(protocolName, poly.getViewPort(1), "verifying the polyline is not panned", "test14_Checkpoint2");




	}

	@Test(groups ={"IE11","Chrome","US820","Positive"})
	public void test15_US820_TC3449_verifyPolyLineOnJpegOrPdfs() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is not able to draw Polyline on PDF/JPEG/PNG.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(IBL_JohnDoe_PatientName,4);
		

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};				
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click on pdf");
		poly.assertEquals(poly.getAllPolylines(1).size(), 0, "Verifying that polyline is absent", "verified");
		poly.drawPolyLineExitUsingESCKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 0, "Verifying that polyline is not drawn", "verified");

	
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Validating the polyline is not drawn on PDF");
		poly.assertEquals(poly.getAllPolylines(3).size(), 0, "Verifying that polyline is absent", "verified");
		poly.drawPolyLineExitUsingESCKey(3, abc);
		poly.assertEquals(poly.getAllPolylines(3).size(), 0, "Verifying that polyline is not drawn", "verified");




	}

	@Test(groups ={"IE11","Chrome","US820","Negative"})
	public void test16_US820_TC3450_verifyMovementOfPolyLineAcrossViewboxes() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify that user should not able to drag or move Polyline annotation from one viewbox to other");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		//poly.mouseHover(poly.getViewPort(2));

		List<String> cursor = poly.movePolyLine(1, 1,50,50);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Checkpoint TC2[1] : Verify new cross icon should display");

		for(String val : cursor)
			poly.assertEquals(val, NSGenericConstants.MOVE_CURSOR, "verifying that mouse cursor changes to cross icon", "verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Checkpoint TC2[2] : Verify that user should not able to drag or move Polyline annotation from one viewbox to other");
		poly.closeNotification();
		poly.compareElementImage(protocolName, poly.getViewbox(1), "Verifying the polyline is not moved to another viewbox", "test16_checkpoint3");


	}

	@Test(groups ={"IE11","Chrome","US820","Negative"})
	public void test17_US820_TC3468_verifyAddPointsAndResizePolyLine() throws Exception 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can also add new points in overlapping area of two polylines");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};
		int[] coordinates = new int[] {20,-90,-90,20,-30,30,30,30,60,40};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Drawing the two polylines with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.drawPolyLineExitUsingDoubleClickKey(1, coordinates);
		poly.assertEquals(poly.getAllPolylines(1).size(), 2, "Verifying polylines presence", "verified");

		//poly.mouseHover(poly.getViewPort(2));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Moving polyline 2 so that it is overlapped with first polyline");
		poly.movePolyLine(1, 2,30,30);

		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));

		int pointsBeforeAddition = poly.getTransparentPolylineHandles(1, 1).size();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Adding new point to overlapping polyline");
		poly.addAndDragPoint(1, 1, 3,-50,50);

		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));

		poly.assertEquals((pointsBeforeAddition+1), poly.getTransparentPolylineHandles(1,2).size(), "Checkpoint[4/5]: Verifying the addition of new point", "verified");
		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,2, 2), "Checkpoint[5/5]", "Verifying the Halo ring is displayed");


	}

	@Test(groups ={"IE11","Chrome","US820","Negative","Sanity"})
	public void test18_US820_TC3469_verifyPolylineIsDeletedOnDeletionOfPoints() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Invalid Polyline scenario : Verify that when user continuously delete points from existing polyline then last point should get deleted with second last point");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] coordinates = new int[] {10,-100,-100,10,-50,50,50,50,100,10};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Drawing the two polylines with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, coordinates);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");

		int totalPoints =0;
		for(int i =poly.getPolylineHandles(1).size()-1;i>0;i--) {
			totalPoints = poly.getPolylineHandles(1).size();			
			poly.deletePointFromPolyLine(1, 1, i);
		  if(i>2)
				poly.assertEquals((totalPoints-1), poly.getPolylineHandles(1).size(), "Checkpoint[2."+i+"/3]: Verifying the deletion of point", "verified");
		}

		poly.assertEquals(poly.getAllPolylines(1).size(), 0, "Checkpoint[3/3] : Verifying polyline presence", "verified");

	}

	@Test(groups ={"IE11","Chrome","US820","Negative"})
	public void test19_US820_TC3470_DE1814_TC7362_verifyPolylineIsOpenOnDeletionOfPoints() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that closed polyline becomes open polyline when user continuously delete points from existing polyline and in last only two points left. <BR>"+
		"Verify that the user is able delete handle/point for closed classic polyline.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
//		int[] coordinates = new int[] {-90,-90,90,-90,90,90,-90,90,-90,-90};
		int[] coordinates = new int[] {-30,-30,30,-30,30,30,-30,30,-30,-30};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Drawing the two polylines with cntrl key on viewbox 1 existing using double click");
		poly.drawClosedPolyLine(1, coordinates);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");

//		poly.closingBannerAndWaterMark();	

		
		int totalPoints =0;
		for(int i =poly.getPolylineHandles(1).size()-1;i>=1;i--) {
			totalPoints = poly.getPolylineHandles(1).size();			
			poly.deletePointFromPolyLine(1, 1, i);
			if(totalPoints==2)
				poly.assertEquals((totalPoints-2), poly.getPolylineHandles(1).size(), "Checkpoint[2."+i+"/3]: Verifying the deletion of point", "verified");
			else
				poly.assertEquals((totalPoints-1), poly.getPolylineHandles(1).size(), "Checkpoint[2."+i+"/3]: Verifying the deletion of point", "verified");
			
		}
		


		poly.assertEquals(poly.getAllPolylines(1).size(), 0, "Checkpoint[3/3] : Verifying polyline presence", "verified");

	}

	@Test(groups ={"IE11","Chrome","US820","Posi"})
	public void test20_US820_TC3545_verifyPolylineIsHighlightedOnRightClick() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify that polyline should get highlighted when user performs right click on segment for accept / reject");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] coordinates = new int[] {10,-100,-100,10,-50,50,50,50,100,10};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Drawing the two polylines with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, coordinates);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Checkpoint[2/5]Verifying polylines presence", "verified");

		poly.performMouseRightClick(poly.getLinesOfPolyLine(1, 1).get(1));

		poly.assertFalse(poly.isElementPresent(poly.quickToolbarMenu), "Checkpoint[3/5]: Verifying that radial menu is not opened when user tries to delete the point", "verified");
		poly.assertTrue(poly.isAcceptRejectToolBarPresent(1), "Checkpoint[4/5]:verifying polyline should not get highlighted when user performs right click for deleting points ", "verified");
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/5]:verifying polyline should not get highlighted when user performs right click for deleting points ", "verified");



	}

	@Test(groups ={"IE11","Chrome","US837","Positive"})
	public void test21_US837_TC3564_verifyIndicatorIsDisplayedOnFreeHandRoi() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("visual indicator near/on free hand ROI");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


//		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};	
		int[] abc = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2,-21,9,-21,20,-13,19,-10,25};
		

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/2]", "Drawing the free hand polyline on viewbox 1 existing using ESC key");
		poly.drawFreehandPolyLineExitUsingESC(1, abc);
		
		poly.mouseHover(poly.getLinesOfPolyLine(1,1).get(1));
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/2]", "Mouse hover close to or along the polyline(cover entire ROI region). Check for a visual indicator");

		int i =0;
		for(WebElement line :poly.getLinesOfPolyLine(1,1)) {
			poly.mouseHover(line);
			poly.assertTrue(poly.verifyHaloRingIsDisplayed(),"Checkpoint[2/2"+i+"]","validating the halo ring");
		}
	}
	
	@Test(groups ={"IE11","Chrome","US837","Positive","Sanity"})
	public void test22_US837_TC3567_verifyEditingOfFreeHandRoi() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Edit polyline using freehand mode");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);


		int[] abc = new int[] {-49,-47,27,-3,24,-2,26,1,27,-6,27,-20,5,-25,-5,-34,-28,-13,-34,-2};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/22]", "Drawing the free hand polyline on viewbox 1 existing using ESC key");
		poly.drawFreehandPolyLineExitUsingESC(1, abc);
		poly.closingConflictMsg();
		poly.drawFreehandPolyLineExitUsingESC(2, abc);
		poly.closingConflictMsg();
		
//		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(3));
		poly.openGSPSRadialMenu(poly.getLinesOfPolyLine(2, 1).get(3));
		
		poly.selectRejectfromGSPSRadialMenu(2);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(2, 1),"Checkpoint[5/22]","Verifying the polyline is rejected");
		
		poly.selectRejectfromGSPSRadialMenu(2);		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(2, 1),"Checkpoint[6/22]","Verifying the polyline is pending");
	
		poly.selectAcceptfromGSPSRadialMenu(2);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"Checkpoint[7/22]","Verifying the polyline is accepted");

				
		int size = poly.getLinesOfPolyLine(2, 1).size();
		
		int []coordinates = new int[] {30,40,50,60};
		poly.editPolyLine(poly.getLinesOfPolyLine(2, 1).get(3), coordinates);
		
		poly.assertEquals(poly.getLinesOfPolyLine(2, 1).size(), size, "Checkpoint[8/22] : Verify that poly line editing is unsuccessful", "verified");
		
		
		List<WebElement> handles = poly.getLinesOfPolyLine(2, 1);
			
		poly.editPolyLine(handles.get(2), coordinates,handles.get(3));

		poly.assertTrue(poly.getLinesOfPolyLine(2, 1).size()>size, "Checkpoint[12/22] Verify that poly line editing is successful", "verified");

		String beforeX1=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.Y2);

		poly.moveFreePolyLine(2, 1, 20,20);

		
		String afterX1=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=poly.getLinesOfPolyLine(2, 1).get(0).getAttribute(ViewerPageConstants.Y2);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[17/22]", "Verify that user should not able to drag or move Polyline annotation from one viewbox to other");
		poly.assertNotEquals(beforeX1,afterX1,"Checkpoint[18/22]","Verify X1 coordinate of line change on move");
		poly.assertNotEquals(beforeY1,afterY1,"Checkpoint[18/22]","Verify Y1 coordinate of line change on move");
		poly.assertNotEquals(beforeX2,afterX2,"Checkpoint[18/22]","Verify X2 coordinate of line change on move");
		poly.assertNotEquals(beforeY2,afterY2,"Checkpoint[18/22]","Verify Y2 coordinate of line change on move");

		

	}

	@Test(groups ={"IE11","Chrome","DE1048"})
     public void test23_DE1048_TC4274_verifyNoConsoleErrorWithFreeHandPolyline() throws  InterruptedException 
{
	    extentTest = ExtentManager.getTestInstance();
	    extentTest.setDescription("Verify no error in console when user loads RT struct data or Polyline data");
	    
	  //Loading the patient on viewer
	  	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
	  	helper = new HelperClass(driver);
	  	helper.loadViewerPage(liver9PatientName, "", 1, 1);	
	  	
	  	
	  	PolyLineAnnotation poly = new PolyLineAnnotation(driver);
	  	poly.closingConflictMsg();
	 // verifying the selection of polyline on radial and verifying it on context menu 
	  	poly.selectPolylineFromQuickToolbar(1);
	  	int[] abc = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
	  	
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Drawing the free hand polyline on viewbox 1");
	  	poly.drawFreehandPolyLine(1, abc);
	  	poly.closingConflictMsg();
	  	poly.drawFreehandPolyLine(2, abc);
	  	
	  	poly.browserBackWebPage();	  	
	  	poly.clearConsoleLogs();
	  	
	  	helper.loadViewerPage(liver9PatientName, "", 1, 1);	
	  	
	  	poly.assertEquals(poly.getPolylineCount(1),1, "Checkpoint[2/3]","Verifying the free hand polyline presence on viewbox1");
	  	
	  	
	  //verify there is no console error on loading study on viewer
	  	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify there is no console error");
	  	poly.assertFalse(poly.isConsoleErrorPresent(),"Verify there is no console error on opening AH.4 study", "There is no console error logged at this point");
	  	
	  	
    }

 	@Test(groups = {"Chrome", "IE11", "Edge", "DE1017", "Positive"})
 	public void test24_DE1017_TC4363_verifyInavlidEditingOfPolyline() throws   InterruptedException  {

 		extentTest = ExtentManager.getTestInstance();
 		extentTest.setDescription("Verify that user state change to accepted when invalid edit done to polyline");

 		//Loading patient on viewer
 		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
 		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
 		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
 		
 		//selecting polyline icon from radial menu
	  	poly.selectPolylineFromQuickToolbar(1);
	  	int[] abc = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,10,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
	  	
	    ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Drawing the free hand polyline on viewbox 1");
	    
	    //drawing freehand polyline
	  	poly.drawFreehandPolyLine(1, abc);
	  	poly.closingConflictMsg();

 		int []coordinates = new int[] {30,40,50,60};
 		
 		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Rejecting the first polyline");
 		
 		//clicking on reject button of polyline
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(0),true);
		
		//verifying that polyline is rejected
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Checkpoint[3/5]","Verifying the polyline is rejected");
		poly.mouseHover(poly.getViewPort(2));
	
 		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,12),"Checkpoint[4/5]","Verifying the halo ring presence on mouse hover");

 		//editing polyline (invalid edit)
 		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(10),coordinates);

 		//verifying polyline is accepted after editing
 		poly.assertTrue(poly.verifyPolyLineAnnotationIsRejectedGSPS(1, 1),"Checkpoint[5/5]","Verifying the polyline is accepted");
 	}
 	
 	@Test(groups ={"IE11","Chrome","DE1017","Negative","BVT"})
	public void test25_DE1017_TC4195_VerifyRejectedPolylineAcceptedAfterEdition() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of DE1028 : Verify Rejected polyline accepted after edition");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);
		int[] coordinates = new int[] {-90,-90,90,-90,90,90,-90,90,-90,-90};
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/3]", "Drawing the two polylines with cntrl key on viewbox 1 existing using double click");
		
		//drawing close polyline
		poly.drawClosedPolyLine(1, coordinates);
		poly.closingConflictMsg();
		
		//verifying close polyline
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Rejecting the first polyline");
		
		poly.mouseHover(poly.getViewPort(2));
		//clicking on reject button
		poly.selectRejectfromGSPSRadialMenu(poly.getLinesOfPolyLine(1, 1).get(1),false);
		
		//verifying thar polyline is rejected
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveRejectedGSPS(1, 1),"Checkpoint[3/5]","Verifying the polyline is rejected");
		
 		poly.assertTrue(poly.checkHaloRingIsDisplayed(1,1,3),"Checkpoint[4/5]","Verifying the halo ring presence on mouse hover");

 		//editing polyline
 		poly.editPolyLine(poly.getLinesOfPolyLine(1, 1).get(2),coordinates);

 		//verifying that on editing rejected polyline is turned into accepted
 		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[5/5]","Verifying the polyline is accepted");

	}

 	@Test(groups ={"firefox","Chrome","IE11","Edge","US986","positive"})
	public void test26_US986_TC4201_TC4204_verifySelectionForFreehandPolyline() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that freehand polyline annotation should not get highlighted when user perform right click on distance annotation"+
		"<br> Verify that A/R should get display when user perform left click on Freehand polyline annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
	    poly = new PolyLineAnnotation(driver);
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify drawn annotaion is current active GSPS" );
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		poly.selectPolyline(1, 1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify freehand polyline annotation is current active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		
		poly.performMouseRightClick(poly.getAllPolylines(1).get(0));
		poly.assertFalse(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify freehand polyline annotation is not active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify A/R when user perform left click on drawn annotation");
		poly.click(poly.getViewPort(1));
		poly.selectPolyline(1, 1);
		poly.assertTrue(poly.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on ellipse annotation", "Verified");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US986","positive"})
	public void test27_US986_TC4202_TC4203_TC4209_TC4260_verifySelectionForClassicPolyline() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that classic polyline annotation should not get highlighted when user perform right click on distance annotation"+
		"<br> Verify that A/R should get display when user perform ctrl+ left click on Classic polyline annotation+"
		+ "<br> Verify that user can delete points from classic polyline by performing right click"+
		  "<br> Verify that A/R should get display when user perform left click on handle of Classic polyline annotation");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
	    poly = new PolyLineAnnotation(driver);
				
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify drawn annotaion is current active GSPS" );
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};	
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify classic polyline annotation is current active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		poly.mouseHover(poly.getViewPort(2));
		poly.performMouseRightClickOnGSPS(poly.getLinesOfPolyLine(1,1).get(0));
	    poly.assertFalse(poly.verifyPolyLineAnnotationIsAcceptedGSPS(1, 1),"Verify classic polyline annotation is not active GSPS object", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify A/R when user perform left click on drawn annotation ");
		poly.selectClassicPolylineWithCtrlLeft(1,1);
		poly.assertTrue(poly.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on ellipse annotation", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify performing right click on handles/points, Points gets deleted ");
		poly.mouseHover(poly.getViewPort(2));
		poly.mouseHover(poly.getViewPort(1));
		int totalPoints = poly.getTransparentPolylineHandles(1,1).size();
		poly.deletePointFromPolyLine(1, 1, 2);
		poly.assertEquals((totalPoints-1),poly.getPolylineHandles(1).size(),"Verifying the deletion of point for Classic polyline", "verified");
		
	}

	@Test(groups ={"Chrome","DE1103","positive","BVT"})
	public void test56_DE1103_TC4671_verifyStateOfClosedPolylineOnClick() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Closed polyline state changes to 'Accepted' when left clicked for selecting annotation-Happy path");
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);	
	  	
		 PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		
		poly.selectPolylineFromQuickToolbar(1);
		int[] poly1 = new int[] {-90,-90,90,-90,90,90,-90,90,-90,-90};
		poly.drawFreehandPolyLineExitUsingESC(1, poly1);
		
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Checkpoint[1/2]","Verifying another polyline is accepted");
		
	//	poly.click(poly.getViewPort(2));
		poly.selectClassicPolylineWithCtrlLeft(1, 1);
		poly.selectAcceptfromGSPSRadialMenu(1);
		poly.selectClassicPolylineWithCtrlLeft(1, 1);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1),"Checkpoint[2/2]","Verifying another polyline is pending");
		
	}
	
	@Test(groups ={"IE11","Chrome","DE1273","Positive"})
	public void test57_DE1273_TC5102_ClassicPolylineHandleNotDeletedAfterDoubleClickingOnIt() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify that on performing left click and double click polyline handle should not get delete");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);

		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);

		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/2]", "Drawing the polyline with cntrl key on viewbox 1 existing using double click");
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertEquals(poly.getAllPolylines(1).size(), 1, "Verifying polylines presence", "verified");
		poly.mouseHover(poly.getViewPort(1));

		int totalPoints = poly.getTransparentPolylineHandles(1,1).size();

		poly.DoubleClickWithMouseHover(poly.getTransparentPolylineHandles(1,1).get(2));
		
		poly.assertEquals((totalPoints), poly.getTransparentPolylineHandles(1,1).size(), "Checkpoint[2/2]: Verify that on performing left click and double click polyline handle should not get delete", "verified");
		poly.deleteAllAnnotation(1);
	}
	
	@Test(groups ={"IE11","Chrome","DE1128","Positive"})
	public void test58_DE1128_TC4737_TC4738_VerifyPolylineHandles() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify all handles of polyline are NOT getting displayed when polyline is selected with mouse click" 
		+ "<br> Verify all handles of polyline are NOT getting displayed while navigating through findings");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+LIDC_IDRI_0012+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDC_IDRI_0012,1);
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		//poly.inputImageNumber(18, 1);
		poly.selectNextfromGSPSRadialMenu(1);
		poly.waitForTimePeriod(2000);
		poly.selectNextfromGSPSRadialMenu(1);
		poly.waitForTimePeriod(2000);
		poly.selectNextfromGSPSRadialMenu(1);
		
		int count = poly.getPolylineHandles(1).size();
		poly.assertEquals(count, 0, "Checkpoint [1/2]", "Verifying that handles should not get displayed on riverrain data");
		poly.waitForTimePeriod(3000);
		poly.selectNextfromGSPSRadialMenu(1);
	    count = poly.getPolylineHandles(1).size();
		poly.assertEquals(count, 0, "Checkpoint [2/2]", "Verifying that handles should not get displayed on riverrain data on navigation");
		
	}

	@Test(groups ={"IE11","Chrome","DE1475","Negative","Sanity"})
	public void test28_DE1475_TC6185_verifyPolylineCreationPostClosedPolyline() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user can draw polyline after drawing closed classic polyline");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		
		// verifying the selection of polyline on radial and verifying it on context menu 
		poly.selectPolylineFromQuickToolbar(1);

		int[] abc1 = new int[] {-10,-5,-20,-10,-30,-15,10,-20,20,-40,30,50};
		int[] abc2 = new int[] {-46,23,-41,37,-42,42,-25,33};
		int[] coordinates = new int[] {-100,-100,100,-100,100,100,-100,100,-100,-100};

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing closed polyline");
		poly.drawClosedPolyLine(1, coordinates);		
		
		poly.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing classic polyline");
		poly.drawPolyLineExitUsingESCKey(1, abc2);		
		
		poly.closingConflictMsg();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing freehand polyline");
		poly.drawFreehandPolyLineExitUsingESC(1, abc1);				
		
		poly.closingConflictMsg();
		poly.assertEquals(poly.getPolylineCount(1),3,"Checkpoint[1/3]","Verifying the user is able to create polylines after closed polyline");	

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		poly.assertEquals(poly.getPolylineCount(1),3,"Checkpoint[2/3]","verifying polylines are retained on viewer reload");	

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		poly.assertEquals(poly.getPolylineCount(1),3,"Checkpoint[3/3]","verifying polylines are retained on change layout");	

		
	}
	
	//AR toolbar is getting displayed even when only left click is perfomed after selecting annotation from radial menu.
	@Test(groups ={"Chrome","IE11","Edge","DE1903","DE1928","Negative"})
	public void test21_DE1903_TC7795_DE1928_TC7807_verifyARToolbarAfterSelectingPolylineAnnotationFromRadialMenu() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verfify AR toobbar is not getting displayed when only left click is performed after selecting the annotation from radial menu (Selecting a polyline).");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);
		

		poly = new PolyLineAnnotation(driver);
		//Select Linear measurement from Radial Menu
		poly.selectPolylineFromQuickToolbar(1);
		poly.clickAt(0, 0);
		poly.assertTrue(poly.verifyResultAppliedTextPresence(1), "Checkpoint[1/5]", "Verified result applied text after perform mouse left click by selecting polyline from radial menu.");
		
		poly.mouseHover(poly.getGSPSHoverContainer(1));
		poly.assertFalse(poly.isAcceptRejectToolBarPresent(1), "Checkpoint[2/5]", "Verified that AR toolbar not visible on viewer");
	
		//draw linear measurement and verify AR toolbar
	
		int[] abc = new int[] {10,-100,-100,10,-50,50,50,50,100,10};	
		poly.drawPolyLineExitUsingDoubleClickKey(1, abc);
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/5]", "Verified that AR toolbar visible on viewer after drawing polyline measurement.");
		
		poly.click(poly.getViewPort(1));
		poly.assertFalse(poly.isAcceptRejectToolBarPresent(1), "Checkpoint[4/5]", "Verified that AR toolbar not visible on viewer because annotation is not highlighted.");
		
		poly.mouseHover(poly.getViewPort(1));
		poly.mouseHover(poly.getGSPSHoverContainer(1));
		poly.assertTrue(poly.isAcceptRejectToolBarPresent(1), "Checkpoint[5/5]", "Verified that AR toolbar visible on viewer when mouse hover on AR toolbar area.");	
		}
}
