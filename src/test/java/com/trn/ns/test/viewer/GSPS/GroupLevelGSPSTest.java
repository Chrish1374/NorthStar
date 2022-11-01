package com.trn.ns.test.viewer.GSPS;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GroupLevelGSPSTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerSliderAndFindingMenu viewerPage;
	
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String myText="TEXT_1";

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);
	private HelperClass helper;
	private String loadedTheme;

	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
	}

	//Accept/Reject at the group level for GSPS
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test01_US1006_TC4558_verifyAcceptRejectAtGroupLevel() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Accept/Reject at the group level for GSPS");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		viewerPage.openFindingTableForGroupElement(1);
		
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify count of group visible on A/R finding toolbar");
		int groupSize=viewerPage.groupInfo.size();
		viewerPage.assertEquals(groupSize, 5, "Verify" +" "+ groupSize +" "+"groups seen in finding toolbar", "Verified");
		
		//accept all finding for Group1
		viewerPage.selectAcceptOrRejectAtGroupLevel(1,1, ViewerPageConstants.GSPS_ACCEPT);
		
		//verify state of finding within a group after accept
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify state of each finding within the group after accepting the Group");
		viewerPage.assertTrue(viewerPage.verifyFindingsStateUnderGroup(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[3/7]"," Verifying the state of findings under group in finding menu after accepting the finding of group");
		
		//reject finding for remaining Group 
		for (int j=2;j<groupSize;j++)
		{ 
			viewerPage.selectAcceptOrRejectAtGroupLevel(1,j, ViewerPageConstants.GSPS_REJECT);
		}
	
		//reload viewer page and verify state of finding for Group 1(Accepted) and 2 (Rejected
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		helper.browserBackAndReloadViewer("",LIDCPatientID, 1, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify state of each finding within the  first group after reload of viewer page");
		viewerPage.assertTrue(viewerPage.verifyFindingsStateUnderGroup(1, 1, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[5/7]"," Verifying the state of findings under group in finding menu after accepting the finding of group");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify state of each finding within the second  group after reload of viewer page");
		viewerPage.assertTrue(viewerPage.verifyFindingsStateUnderGroup(1, 2, ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[7/7]"," Verifying the state of findings under group in finding menu after accepting the finding of group");
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","DE1659","positive"})
	public void test02_US1006_TC4615_DE1659_TC7044_verifyForwardNavigationBetweenGroupNonGroupElementUsingARToolbar() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Forward Navigation between group and non-group elements. <br>"+
		"Verify the navigation through GSPS and GSPS group using AR tool bar and previous and next keys in keyboard.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		//Load patient on viewer
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);

				
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/19]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.selectFindingFromTable(1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/19]", "Verified that text annotation is current active pending GSPS");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[3/19]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/19]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[5/45]", "Verify first finding from first group is highlighted");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[6/19]","Verified that point annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(3), "Checkpoint[7/19]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[8/19]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "Checkpoint[9/19]", "Verified finding is highlighted in finding menu");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[8/19]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[9/19]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[10/19]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[11/19]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[12/19]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/19]", "Verify first finding from second group is highlighted");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[14/19]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(6), "Checkpoint[15/19]","Verified finding is highlighted in finding menu");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[14/19]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(7), "Checkpoint[15/19]","Verified finding is highlighted in finding menu");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[16/19]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[17/19]", "Verify first finding from third group is highlighted");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[18/19]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[19/19]", "Verify first finding from fifth group is highlighted");

	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","DE1659","positive"})
	public void test03_US1006_TC4615_DE1659_TC7044_verifyBackwardNavigationBetweenGroupNonGroupElementUsingARToolbar() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Navigation between group and non-group elements. <br> "+
		"Verify the navigation through GSPS and GSPS group using AR tool bar and previous and next keys in keyboard.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
	
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/25]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 32);
		
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/25]", "Verify fifth finding from fifth group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[4/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[5/25]", "Verify first finding from third group is highlighted");

		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[6/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[7/25]", "Verify first finding from third group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[8/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(7), "Checkpoint[9/25]","Verified finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[10/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(6), "Checkpoint[11/25]","Verified finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Checkpoint[12/25]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/25]", "Verify first finding from second group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[14/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[15/25]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[16/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[17/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[18/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "Checkpoint[19/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[20/25]","Verified that point annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(3), "Checkpoint[21/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[22/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[23/25]", "Verify first finding from first group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[24/25]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[25/25]", "Verified finding is highlighted in finding menu");
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test04_US1006_TC4655_verifyForwardNavigationBetweenGroupNonGroupElementUsingPageDown() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Navigation between group and non-group elements- Page Down");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
	
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);

		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/23]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.selectFindingFromTable(1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[3/23]", "Verify finding is highlighted in finding menu");
		
		viewerPage.pressKeys(Keys.PAGE_DOWN);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[5/23]", "Verify first finding from first group is highlighted");
	
		viewerPage.pressKeys(Keys.PAGE_DOWN);		
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[10/23]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[11/23]", "Verify finding is highlighted in finding menu");
	
		viewerPage.pressKeys(Keys.PAGE_DOWN);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[8/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[9/23]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.pressKeys(Keys.PAGE_DOWN);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[16/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[17/23]", "Verify fifth finding from second group is highlighted");
	
	
		viewerPage.pressKeys(Keys.PAGE_DOWN);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,3), "Checkpoint[12/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[13/23]", "Verify first finding from second group is highlighted");
		
	
		viewerPage.pressKeys(Keys.PAGE_DOWN);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[3/23]", "Verify finding is highlighted in finding menu");
	
				
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test05_US1006_TC4655_verifyBackwardNavigationBetweenGroupNonGroupElementUsingPageUp()throws InterruptedException      
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Navigation between group and non-group elements- Page Up");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
	
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
				
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/23]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 32);
		
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/23]",  "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/23]", "Verify fifth finding from fifth group is highlighted");

		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,3), "Checkpoint[4/23]",  "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[5/23]","Verify fifth finding from fourth group is highlighted");
	
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[6/23]",  "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[7/23]", "Verify fifth finding from second group is highlighted");
		
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[8/23]",  "Verified that polyline annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[9/23]", "Verify third finding from second group is highlighted");
		
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[12/23]",  "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[13/23]", "Verify finding is highlighted in finding menu");
		
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[10/23]",  "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,1), "Checkpoint[11/23]","Verify first finding from second group is highlighted");
		
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[20/23]",  "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[21/23]", "Verify finding is highlighted in finding menu");
	
		viewerPage.pressKeys(Keys.PAGE_UP);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[22/23]",  "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[23/23]", "Verify fifth finding from fifth group is highlighted");
	
	
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test06_US1006_TC4559_verifyForwardNavigationBetweenGroupElementUsingARToolbar() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Forward Navigation at the group level for GSPS- Using A/R toolbar");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
	
		ellipse=new EllipseAnnotation(driver);
		textAn = new TextAnnotation(driver);

		viewerPage = new ViewerSliderAndFindingMenu(driver);
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/35]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
		
		viewerPage.selectFindingFromTable(1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 1, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[3/23]", "Verify finding is highlighted in finding menu");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 1, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[3/23]", "Verify finding is highlighted in finding menu");
	
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[5/23]", "Verify first finding from first group is highlighted");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[8/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[9/23]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[8/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[9/23]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[16/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[17/23]", "Verify fifth finding from second group is highlighted");
	
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,3), "Checkpoint[12/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[13/23]", "Verify first finding from second group is highlighted");
		
		viewerPage.selectNextfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[3/23]", "Verify finding is highlighted in finding menu");
	
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test07_US1006_TC4559_verifyBackwardNavigationBetweenGroupElementUsingARToolbar()throws InterruptedException      
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Backward Navigation at the group level for GSPS- Using A/R toolbar");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
	
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/37]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
		
		viewerPage.scrollToImage(1, 32);
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/37]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/37]",  "Verify fifth finding from fifth group is highlighted");
				
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[6/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[7/37]",  "Verify fifth finding from fourth group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[8/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[9/37]",  "Verify first finding from fifth group is highlighted");
		
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[12/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/37]", "Verify fifth finding from third group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[14/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[15/37]",  "Verify fifth finding from second group is highlighted");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[16/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,1), "Checkpoint[17/37]",  "Verify first finding from fourth group is highlighted");
				
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[32/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[33/37]",  "Verify finding is highlighted in finding menu");
		
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 1, false), "Checkpoint[34.1/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 2, false), "Checkpoint[34.2/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[35/37]", "Verify finding is highlighted in finding menu");
	
		viewerPage.selectPreviousfromGSPSRadialMenu();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[36/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[37/37]",  "Verify fifth finding from fifth group is highlighted");
	
	}
    
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test08_US1006_TC4560_verifyForwardNavigationBetweenGroupElementUsingArrowNextButton() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Forward Navigation at the group level for GSPS- Using arrow right button");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
				
		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/19]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
		
		viewerPage.selectFindingFromTable(1);
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 1, false), "Checkpoint[3/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[4/19]", "Verify finding is highlighted in finding menu");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[5/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 1, false), "Checkpoint[6/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[7/19]", "Verify finding is highlighted in finding menu");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[8/19]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[9/19]", "Verify first finding from first group is highlighted");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[10/19]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[11/19]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[12/19]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/19]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[14/19]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[15/19]", "Verify fifth finding from second group is highlighted");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,3), "Checkpoint[16/19]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[17/19]", "Verify first finding from second group is highlighted");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[18/19]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[19/19]", "Verify finding is highlighted in finding menu");
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test09_US1006_TC4560_verifyBackwardNavigationBetweenGroupElementUsingArrowBackButton()  throws InterruptedException    
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Backward Navigation at the group level for GSPS- Using Arrow left button ");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/37]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
		
		viewerPage.scrollToImage(1, 32);
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/37]", "Verify fifth finding from fifth group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[6/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[7/37]",  "Verify fifth finding from fourth group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[8/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[9/37]",  "Verify first finding from fifth group is highlighted");
				
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[12/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/37]", "Verify fifth finding from third group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[14/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[15/37]",  "Verify fifth finding from second group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[16/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,1), "Checkpoint[17/37]",  "Verify first finding from fourth group is highlighted");
				
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[32/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 1, false), "Checkpoint[34/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[33/37]",  "Verify finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 1, false), "Checkpoint[34/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 2, false), "Checkpoint[34/37]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[35/37]", "Verify finding is highlighted in finding menu");
	
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[36/37]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[37/37]",  "Verify fifth finding from fifth group is highlighted");

	}

	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test12_US1006_TC4563_verifyPageUpDownFunctionalityForGSPS()  throws InterruptedException    
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Page UP/Down functionality for non-group element");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,1);
		
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		circle=new CircleAnnotation(driver);
		
		String myText ="Test1";
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 10, 10, 50, 50);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
		
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-250, -50, myText);
		
		viewerPage.scrollToImage(1, 16);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, -50, 40, -50);
		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 150);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		
		viewerPage.scrollToImage(1, 17);
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-250, -50, myText);
		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 60, 60, 150, 150);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		 
		viewerPage.closingConflictMsg();
		
		//verify page up and page down for GSPS
		viewerPage.scrollToImage(15, 1);
		viewerPage.scrollDownGSPSUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[1/8]", "Verified that ellipse annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "checkpoint[2/8]", "Verify fourth finding is highlighted in finding menu on page down");
		
		viewerPage.scrollDownGSPSUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[3/8]", "Verified that text annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(7), "checkpoint[4/8]", "Verify seventh finding is highlighted in finding menu on page down");
	
		viewerPage.scrollUpGSPSUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[5/8]", "Verified that ellipse annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "checkpoint[6/8]", "Verify fourth finding is highlighted in finding menu on page up");
		
		viewerPage.scrollUpGSPSUsingKeyboard();
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[7/8]","Verified that circle annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "checkpoint[8/8]", "Verify first finding is highlighted in finding menu on page up");
		
		
		viewerPage.scrollUpGSPSUsingKeyboard();
	}

	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
	public void test13_DE1659_TC7044_verifyForwardNavigationBetweenGroupNonGroupElementUsingArrowNext() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Forward Navigation between group and non-group elements. <br>"+
		"Verify the navigation through GSPS and GSPS group using AR tool bar and previous and next keys in keyboard.");

			helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
				
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/23]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.selectFindingFromTable(1);
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 1, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(1, 2, false), "Checkpoint[2/23]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[3/23]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[5/23]", "Verify first finding from first group is highlighted");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[6/23]","Verified that point annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(3), "Checkpoint[7/23]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[8/23]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "Checkpoint[9/23]", "Verified finding is highlighted in finding menu");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[10/23]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[11/23]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[12/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[13/23]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,1), "Checkpoint[14/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[15/23]", "Verify first finding from second group is highlighted");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[16/23]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(6), "Checkpoint[17/23]","Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[18/23]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(7), "Checkpoint[19/23]","Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[20/23]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[21/23]", "Verify first finding from third group is highlighted");
		
		viewerPage.navigateGSPSForwardUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[22/23]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[23/23]", "Verify first finding from fifth group is highlighted");

	}
	
	@Test(groups ={"IE11","Chrome","Edge","DE1659","positive"})
	public void test14_DE1659_TC7044_verifyBackwardNavigationBetweenGroupNonGroupElementUsingBackArrow() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("");

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID, 1);
		viewerPage = new ViewerSliderAndFindingMenu(driver);
			
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		ellipse=new EllipseAnnotation(driver);
		
		viewerPage.openFindingTableForGroupElement(1);
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/25]", "Verify count of group visible on A/R finding toolbar");
		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");

		viewerPage.scrollToImage(1, 17);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 24);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 28);
		point.drawPointAnnotationMarkerOnViewbox(1, 10, 10);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);
		
		viewerPage.scrollToImage(1, 32);
		
		ellipse.selectEllipse(1, 1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/25]", "Verify fifth finding from fifth group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[4/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,1), "Checkpoint[5/25]", "Verify first finding from third group is highlighted");

		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[6/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,1), "Checkpoint[7/25]", "Verify first finding from third group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[8/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(7), "Checkpoint[9/25]","Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[10/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(6), "Checkpoint[11/25]","Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Checkpoint[12/25]","Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(3,1), "Checkpoint[13/25]", "Verify first finding from second group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[14/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[15/25]", "Verify fifth finding from first group is highlighted");
	
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[16/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(5), "Checkpoint[17/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[18/25]", "Verified that point annotation is current active accepted GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(4), "Checkpoint[19/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[20/25]","Verified that point annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(3), "Checkpoint[21/25]", "Verified finding is highlighted in finding menu");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[22/25]", "Verified that ellipse annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[23/25]", "Verify first finding from first group is highlighted");
		
		viewerPage.navigateGSPSBackUsingKeyboard();
		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[24/25]", "Verified that text annotation is current active pending GSPS");
		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(2), "Checkpoint[25/25]", "Verified finding is highlighted in finding menu");
		
	}
	
	//DR2626: Cross-slice warning message not visible at viewer level for group findings.
	@Test(groups ={"IE11","Chrome","Edge","DR2626","Positive","F332"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test15_DR2626_TC10664_TC10665_TC10674_TC10676_verifyInfoPopupForGroupFinding(String theme) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cross-slice warning message are visible at viewer level for group findings - Eureka theme. <br>"+
		"Verify that cross-slice warning message are visible at viewer level for group findings - Dark theme. <br>"+
		"Verify the viewer message upon the group finding selection from A/R toolbar. <br>"+
		"Verify that the finding pop up is displayed on the viewer, when user selects a group finding from finding slider.");

		helper = new HelperClass(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		patientPage = new PatientListPage(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,username,password, 1);
		
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		
		List<String> groupName=viewerPage.getListOfGroupsInFindingMenu(1);
		viewerPage.selectFindingFromGroupOfTable(1, 2, 3);
		
		//verify Groups
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of "+LIDCPatientID+" patient on viewer." );
		PagesTheme pageTheme=new PagesTheme(driver);
		viewerPage.assertTrue(pageTheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[2/7]", "Verified theme for Info pop up when finding is present on different slice.");

		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO, groupName.get(1)+" is present on multiple slices"),"Checkpoint[3/7]", "Verified text for Notification pop up.");
		//waiting for invisibility of popup to check Auto closed
		viewerPage.waitForElementInVisibility(viewerPage.notificationDiv);
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.notificationDiv), "Checkpoint[4/7]", "Verified that Notification pop up is auto closed.");
		
		//TC10676: when finding selected from Slider
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "]", "Verified notification pop up when finding is selected from Slider." );
		int currentImg=viewerPage.getCurrentScrollPositionOfViewbox(1);
		ViewerSliderAndFindingMenu findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.selectGroupFromSlider(findingMenu.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.PENDING_FINDING_COLOR).get(currentImg-1), groupName.get(2));
		viewerPage.assertTrue(pageTheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[5/7]","Verified theme for Info pop up when finding is present on different slice.");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO, groupName.get(2)+" is present on multiple slices"),"Checkpoint[6/7]","Verified text for Notification pop up.");
		viewerPage.waitForTimePeriod(10000);
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.notificationDiv), "Checkpoint[7/7]","Verified that Notification pop up is auto closed.");
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","DR2626","Positive","F3332"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test16_DR2626_TC10678_TC10679_verifyPopUPWhenScrollThroughGroups(String theme) throws InterruptedException, IOException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that finding pop ups are displayed when user scrolls through group finding in A/R toolbar. <br>"+
		"Verify that group finding pop up is closed when user selects a non group finding from the finding list or finding slider.");

		helper = new HelperClass(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading of "+LIDCPatientID+ " on Viewer.");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(LIDCPatientID,username,password, 1);
		
		viewerPage = new ViewerSliderAndFindingMenu(driver);
		
		List<String> groupName=viewerPage.getListOfGroupsInFindingMenu(1);
		viewerPage.selectGroupFromTable(1, 1);
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTableEle), "Checkpoint[1/6]", "Verified that finding table is closed after selecting group from finding menu.");
		
		//verify Groups
		
		PagesTheme pageTheme=new PagesTheme(driver);
		viewerPage.assertTrue(pageTheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP, loadedTheme), "Checkpoint[2/6]", "Verified theme for Notification pop up.");
		viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO, groupName.get(0)+" is present on multiple slices"),"Checkpoint[3/6]", "Verified message and Info from Notification pop up.");

		for(int i=1;i<groupName.size();i++)
		{
			viewerPage.openFindingTableOnBinarySelector(1);
			viewerPage.mouseHover(viewerPage.groupInfo.get(0));
			viewerPage.scrollFindingsInSlider(NSGenericConstants.SCROLL_DOWN,1);
			viewerPage.assertTrue(pageTheme.verifyThemeForNotification(viewerPage.notificationTiles.get(0),ThemeConstants.TAB_OPENED_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[4/6]", "Verified theme for pop up when mouse hover on group findings from Finding menu..");
			viewerPage.assertTrue(viewerPage.verifyNotificationPopUp(ViewerPageConstants.INFO, groupName.get(i)+" is present on multiple slices"),"Checkpoint[5/6]", "Verified group name and Info icon from notification pop up.");
		}
		
		viewerPage.selectFindingFromTable(1);
		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.findingTableEle), "Checkpoint[6/6]", "Verified that finding table is closed after selecting non-group from finding menu.");

	}

	
}















