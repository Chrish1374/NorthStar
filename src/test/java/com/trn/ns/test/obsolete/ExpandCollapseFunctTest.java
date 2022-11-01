package com.trn.ns.test.obsolete;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;

import java.sql.SQLException;
import java.text.DateFormat;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

//import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ExpandCollapseFunctTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private HelperClass helper; 

	DatabaseMethods db= new DatabaseMethods(driver);

	// Get Patient Name
	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_Filepath);

	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);
	
	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeriesPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	private CircleAnnotation circle;
	private EllipseAnnotation ellipse;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private ViewerSendToPACS sd;
	private final String circleComment="Circle comment";
	private final String ellipseComment="Ellipse comment";
	private String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	private String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private RegisterUserPage register;
	private String username_1="scan1";
	private PointAnnotation point;
	String userA="UserA";

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

	}

//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1163", "Positive","BVT" })
//	public void test01_US1163_TC5855_TC5856_TC5859_TC5885_TC5886_verifyExpandCollapseToggleButton() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify if individual expand/collapse toggle buttons are displayed for output panel list items. <br>"+
//				"Verify Expand All/Collapse All button is displayed at the summary level <br>"+
//				"Verify on mouse hover div is highlighted and Expand/Collapse/Expand All/Collapse All buttons shows correct tool tip <br>"+
//				"Verify if output panel rows are in collapsed state and user re-size the browser window, after re-size all output panel rows should display in expanded state <br>"+
//				"Verify look and feel, styling and labeling of output panel in full screen and when browser window is re-sized");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName1);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		circle=new CircleAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		lineWithUnit=new MeasurementWithUnit(driver);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 70, 70, 80, 80);
//
//		panel.inputImageNumber(17, 1);
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1,100, -50, 40, -50);
//
//		panel.inputImageNumber(19, 1);
//		lineWithUnit.selectDistanceFromQuickToolbar(1);
//		lineWithUnit.drawLine(1, -70, -80, -120, 90);
//
//		lineWithUnit.selectScrollFromQuickToolbar(lineWithUnit.getViewPort(1));
//		lineWithUnit.click(lineWithUnit.getViewPort(1));		
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		panel.assertEquals(panel.thumbnailList.size(), 3, "Checkpoint[1/13]", "Verified that all rows are in expanded state i.e thumbnail are visible in output panel");
//
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[2."+(i+1)+"/13]", "Verify collapse button display for finding "+(i+1));
//			panel.assertEquals(panel.getCssValue(panel.expandCollapseToggleDiv.get(i), ViewerPageConstants.FLOAT), ViewerPageConstants.RIGHT, "Checkpoint[3."+(i+1)+"/13]", "Verified alignment of collapse icon on output Panel for finding "+(i+1));
//		}
//
//		panel.click(panel.expandCollapseToggleDiv.get(0));
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[4/13]", "Verified  Expand icon when user collapse individual row");
//
//		panel.click(panel.expandCollapseToggleDiv.get(0));
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[5/13]", "Verified Collpase icon when user Expand individual row");
//
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[6/13]", "Verified Expand All icon when user collapse all row");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[7/13]", "Verified that all rows are in expanded state");
//
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[8/13]", "Verified Collpase All icon  when user Expand all row");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[9/13]", "Verified that all rows are in expanded state");
//
//		panel.mouseHover(panel.createdOnDateList.get(0));
//		panel.assertEquals(panel.getCssValue(panel.findingsDivOP, NSGenericConstants.BACKGROUND_COLOR),ViewerPageConstants.BACKGROUND_COLOR_OP_DIV, "Checkpoint[10/13]", "Verified that div is highlighted in OP when mouse hover on panel list item row");
//
//		//for resizing verify expand collapse
//		panel.click(panel.expandCollapseToggleDiv.get(0));
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[11/13]", "Verified  Expand icon when user collapse individual row");
//
//		panel.resizeBrowserWindow(1200,900);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[12.1/13]", "Verified collapse icon when user expand row for findind 1");
//		panel.assertEquals(panel.getCssValue(panel.expandCollapseToggleDiv.get(0), ViewerPageConstants.FLOAT), ViewerPageConstants.RIGHT, "Checkpoint[13.1/13]", "Verified alignment of collapse icon on output Panel for finding 1");
//	
//		for(int i=1;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[12."+(i+1)+"/13]", "Verified collapse icon when user expand row for findind "+(i+1));
//			panel.assertEquals(panel.getCssValue(panel.expandCollapseToggleDiv.get(i), ViewerPageConstants.FLOAT), ViewerPageConstants.RIGHT, "Checkpoint[13."+(i+1)+"/13]", "Verified alignment of collapse icon on output Panel for finding "+(i+1));
//		}
//
//
//	}
//
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1163", "Positive" })
//	public void test02_US1163_TC5857_verifyThumbnailTextDetailOnExpand() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on expand of a panel list item, it should display the thumbnail with the text details on right hand side");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName1);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		OutputPanel panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		lineWithUnit=new MeasurementWithUnit(driver);
//		DateFormat dateFormat=new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		Date date = new Date();
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 70, 70, 80, 80);
//
//		viewerPage.inputImageNumber(17, 1);
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1,100, -50, 40, -50);
//
//		panel.enableFiltersInOutputPanel(true, false, false);
//		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[1/10]", "Verified that all rows are in expanded state");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify creator name, Creation date and collapse icon on right hand side of thumbnail ");
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			viewerPage.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[2."+(i+1)+"/10]", "Verified collapse button display for finding  "+(i+1));
//			viewerPage.assertEquals(panel.createdByUserList.get(i).getText(), ViewerPageConstants.CREATED_BY_TEXT+" "+Configurations.TEST_PROPERTIES.get("nsUserName"), "Checkpoint[3."+(i+1)+"/10]", "verifying created by user for finding "+(i+1));
//			viewerPage.assertEquals(panel.createdOnDateList.get(i).getText(), ViewerPageConstants.CREATED_ON_TEXT+" "+dateFormat.format(date), "Checkpoint[4."+(i+1)+"/10]", "Verifying the created on for finding "+(i+1));
//			viewerPage.assertEquals(panel.findingsEditorName.get(i).getText(), username, "Checkpoint[5."+(i+1)+"/10]", "verifying editor name for finding "+(i+1));
//			
//			Date d1 = new Date(panel.findingEditionDateList.get(i).getText());						
//			viewerPage.assertEquals(dateFormat.format(d1), dateFormat.format(date), "Checkpoint[6."+(i+1)+"/10]", "Verifying the edition date on finding "+(i+1));
//
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name for both user drawn annotation on left hand side");
//		viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(1)).contains(ViewerPageConstants.CIRCLE_FINDING_NAME), "Checkpoint[7/10]", "Verified finding name for the Circle annotation");
//		viewerPage.assertTrue(panel.getText(panel.findingsNameList.get(0)).contains(ViewerPageConstants.ELLIPSE_FINDING_NAME), "Checkpoint[8/10]", "Verified finding name for the Ellipse annotation");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name title for both user drawn annotation on top of the thumbnail");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)),ViewerPageConstants.CIRCLE_FINDING_NAME, "Checkpoint[9/10]", "Verified finding name title for the Circle annotation");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[10/10]", "Verified finding name title for the Ellipse annotation");
//	}
//
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1163", "Positive" })
//	public void test03_US1163_TC5858_verifyThumbnailTextDetailOnCollapse() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on collapse of a panel list item, summary information is displayed for DICOM & NON-DICOM data");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(patientName1);
//
//		patientListPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		OutputPanel panel = new OutputPanel(driver);
//		circle=new CircleAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		lineWithUnit=new MeasurementWithUnit(driver);
//		DateFormat dateFormat=new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		Date date = new Date();
//		cs=new ContentSelector(driver);
//		String seriesname=panel.getSeriesDescriptionOverlayText(1);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 70, 70, 80, 80);
//		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);
//
//		viewerPage.inputImageNumber(17, 1);
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1,100, -50, 40, -50);
//		ellipse.addResultComment(ellipse.getAllEllipses(1).get(0), ellipseComment);
//
//		lineWithUnit.selectScrollFromQuickToolbar(lineWithUnit.getViewPort(1));
//		lineWithUnit.click(lineWithUnit.getViewPort(1));	
//		
//		panel.enableFiltersInOutputPanel(true, false, false);
//		
//		if(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL))
//			viewerPage.click(panel.expandCollapseAllToggle);
//		
//		int thumbnailCount=panel.thumbnailList.size();
//		viewerPage.click(panel.expandCollapseAllToggle);
//		viewerPage.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/16]", "Verified that all rows are in collapse state i.e. thumbnail not visible in Output panel");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name title and comment for both user drawn annotation on top of the thumbnail");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)),ViewerPageConstants.CIRCLE_FINDING_NAME, "Checkpoint[2/16]", "Verified finding name title for the Circle annotation");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[3/16]", "Verified finding name title for the Ellipse annotation");
//		viewerPage.assertEquals(panel.getText(panel.commentForFindings.get(1)),ViewerPageConstants.COMMENT_TAG+" "+circleComment, "Checkpoint[4/16]", "Verified finding comment for the circle annotation");
//		viewerPage.assertEquals(panel.getText(panel.commentForFindings.get(0)),ViewerPageConstants.COMMENT_TAG+" "+ellipseComment, "Checkpoint[5/16]", "Verified finding comment for the Ellipse annotation");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Editor name, edition date and collapse icon on right hand side of thumbnail ");
//		for(int i=0;i<thumbnailCount;i++)
//			panel.assertTrue(panel.verifyFindingDetailsInOutputPanel(i, ViewerPageConstants.OUTPUT_PANEL_EXPAND, username, dateFormat.format(date),seriesname),"Checkpoint[6."+(i+1)+"/16]","Verified ");
//
//		//navigate back to patient list and select another patient
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(anonymous_Patient, 1, 2);
//
//		String resultName1=cs.getAllResults().get(0);
//		String resultName2=cs.getAllResults().get(1);
//		String resultName3=cs.getAllResults().get(2);
//
//		panel.enableFiltersInOutputPanel(false, false, true);
//		if(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL))
//			viewerPage.click(panel.expandCollapseAllToggle);
//		
//		viewerPage.click(panel.expandCollapseAllToggle);
//		viewerPage.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[7/16]", "Verified that all rows are in collapse state");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name title for non dicom data");
//		//verify title
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),resultName1+ViewerPageConstants.PNG_EXTENSION, "Checkpoint[8/16]", "Verified finding name title for the png data");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)),resultName2+ViewerPageConstants.JPEG_EXTENSION, "Checkpoint[9/16]", "Verified finding name title for jpeg data");
//		viewerPage.assertEquals(panel.getText(panel.findingsNameTitleList.get(2)),resultName3+ViewerPageConstants.BMP_EXTENSION, "Checkpoint[10/16]", "Verified finding name title for the bmp data");
//
//		//verify series name
//		viewerPage.assertEquals(panel.getText(panel.seriesNameForFindings.get(0)),resultName1, "Checkpoint[11/16]", "Verified series name for the png data");
//		viewerPage.assertEquals(panel.getText(panel.seriesNameForFindings.get(1)),resultName2, "Checkpoint[12/16]", "Verified series name for the bmp data");
//		viewerPage.assertEquals(panel.getText(panel.seriesNameForFindings.get(2)),resultName3, "Checkpoint[13/16]", "Verified series name for for jpeg data");
//
//		//verify format
//		viewerPage.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(0)),ViewerPageConstants.PNG_EXTENSION, "Checkpoint[14/16]", "Verified format for the png data");
//		viewerPage.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(1)),ViewerPageConstants.JPEG_EXTENSION, "Checkpoint[15/16]","Verified format for jpeg data");
//		viewerPage.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(2)),ViewerPageConstants.BMP_EXTENSION, "Checkpoint[16/16]", "Verified format for the bmp data");
//		
//
//	}
//
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Negative","BVT" })
//	public void test03_US1186_TC6296_verifyExpandCollapseForNextSession() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify \"Collapse All\" state should be persisted in next session for the user");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[1/12]", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[2/12]", "Verified that all rows are in expanded state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[3."+(i+1)+"/12]", "Verify collapse button display for finding "+(i+1));
//		}
//		
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[4/12]", "Verified Expand All icon when user collapse all row");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[5/12]", "Verified that all rows are in collapse state");
//		
//		DatabaseMethods db = new DatabaseMethods(driver);
//		panel.assertTrue(db.getCollapseFindingsInOutputPanel(username),"Checkpoint[6/12]","Verifying the user Preference table");
//		Header header = new Header(driver);
//		header.logout();
//		
//		loginPage.login(username,password);
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForViewerpageToLoad();
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[7/12]", "Verifying the state is persisted in next session for the user");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[8/12]", "Verified that all rows are in collapse state");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[9/12]", "Verified  Expand icon when user collapse individual row");
//
//		panel.resizeBrowserWindow(1200,900);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[10/12]", "Verifying the state is persisted on resize of browser as well");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[11/12]", "Verified that all rows are in expanded state");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[12/12]", "Verified  Expand icon when user collapse individual row");
//
//
//
//	}
//		
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Negative" })
//	public void test04_US1186_TC6297_verifyExpandCollapseForDifferentUserSession() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify \"Expand All/Collapse All\" state should NOT be persisted in next session for the different user");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[1/13]", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[2/13]", "Verified that all rows are in expanded state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[3."+(i+1)+"/13]", "Verify collapse button display for finding "+(i+1));
//		}
//		
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[4/13]", "Verified Expand All icon when user collapse all row");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[5/13]", "Verified that all rows are in collapse state");
//		
//		DatabaseMethods db = new DatabaseMethods(driver);
//		panel.assertTrue(db.getCollapseFindingsInOutputPanel(username),"Checkpoint[6/13]","Verifying the user Preference table");
//				
//		panel.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register = new RegisterUserPage(driver);	
//		register.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
//				
//		Header header = new Header(driver);
//		header.logout();
//		
//		loginPage.login(username_1,username_1);
//		
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verifying Expand All/Collapse All state should NOT be persisted in next session for the different user");
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[7/13]", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[8/13]", "Verified that all rows are in expanded state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[9."+(i+1)+"/13]", "Verify collapse button display for finding "+(i+1));
//		}
//		panel.resizeBrowserWindow(1200,900);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[10/13]", "Verified collapse all icon when user collapse all row");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[11/13]", "Verified that all rows are in expanded state");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[12/13]", "Verified  collapse icon when user collapse individual row");
//
//		// blank
//		panel.assertFalse(db.getCollapseFindingsInOutputPanel(username_1),"Checkpoint[13/13]","Verifying the user Preference table");
//		
//
//	}
//	
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Positive" })
//	public void test05_US1186_TC6298_verifyExpandCollapseForSameSession() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify \"Expand/Collapse\" state should be persisted in same session");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Selecting 4 findings as accepted in viewbox 1");
//		for(int i=0;i<4;i++)
//			panel.selectAcceptfromGSPSRadialMenu();
//		
//		point = new PointAnnotation(driver);
//		point.inputImageNumber(3, 2);
//		point.selectPoint(2, 1);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Selecting 4 findings as rejected in viewbox 2");
//		for(int i=0;i<4;i++) 
//			panel.selectRejectfromGSPSRadialMenu();
//				
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing the accepted findings");
//		panel.enableFiltersInOutputPanel(true, false, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing the rejected findings");
//		panel.enableFiltersInOutputPanel(false,true, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		
//		panel.enableFiltersInOutputPanel(true,true, true);
//		for(int i=0;i<8;i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[1."+(i+1)+"/6]", "Verify expand button display for finding "+(i+1));
//		
//		for(int i=8;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[2."+(i+1)+"/6]", "Verify collapse button display for finding "+(i+1));
//		
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verifying that on re-opening the Expand/Collapse state should be persisted in same session");
//		panel.enableFiltersInOutputPanel(true,true, true);
//		for(int i=0;i<7;i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[3."+(i+1)+"/6]", "Verify expand button display for finding "+(i+1));
//		
//		for(int i=8;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[4."+(i+1)+"/6]", "Verify collapse button display for finding "+(i+1));
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verifying that on re-opening the Expand/Collapse state should be persisted on resize of browser");
//		panel.resizeBrowserWindow(1200,900);
//		
//		for(int i=0;i<7;i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[5."+(i+1)+"/6]", "Verify expand button display for finding "+(i+1));
//		
//		for(int i=8;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[6."+(i+1)+"/6]", "Verify collapse button display for finding "+(i+1));
//		
//
//	}
//		
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Negative" })
//	public void test06_US1186_TC6299_verifyExpandCollapseDiffSessionForAUser() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify \"Expand/Collapse\" state should NOT be persisted when user re-loads viewer for same study");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Accepting few findings");
//		for(int i=0;i<4;i++)
//			panel.selectAcceptfromGSPSRadialMenu();
//		
//		point = new PointAnnotation(driver);
//		point.inputImageNumber(3, 2);
//		point.selectPoint(2, 1);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Rejecting few findings");
//		for(int i=0;i<4;i++) 
//			panel.selectRejectfromGSPSRadialMenu();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing the accepted findings");	
//		panel.enableFiltersInOutputPanel(true, false, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing the rejected findings");
//		
//		panel.enableFiltersInOutputPanel( false,true, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		
//		panel.enableFiltersInOutputPanel(true,true, true);
//		for(int i=0;i<7;i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[1."+(i+1)+"/8]", "Verify expand button is displayed for collapsed finding "+(i+1));
//	
//		
//		for(int i=8;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[2."+(i+1)+"/8]", "Verify collapse button display for finding "+(i+1));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Navigating back to study page");
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(pointMultiSeriesPatient, 1, 1);
//
//		panel.enableFiltersInOutputPanel(true,true, true);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[3/8]", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[4/8]", "Verified that all rows are in expanded state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[5."+(i+1)+"/8]", "Verify collapse button display for finding "+(i+1));
//
//		
//		panel.resizeBrowserWindow(1200,900);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPS_ALL, "Checkpoint[6/8]", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed. on browser resize");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[7/8]", "Verified that all rows are in expanded state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[8."+(i+1)+"/8]", "Verify collapse button display for finding "+(i+1));
//
//
//	}
//	
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Positive" })
//	public void test07_US1186_TC6300_verifyCloneGetsAsExpanded() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify clone entry gets added as expanded in output panel");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Selecting the findings as accepted");
//		for(int i=0;i<4;i++)
//			panel.selectAcceptfromGSPSRadialMenu();
//		
//		point = new PointAnnotation(driver);
//		point.inputImageNumber(3, 2);
//		point.selectPoint(2, 1);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Selecting the findings as rejected");
//		for(int i=0;i<4;i++) 
//			panel.selectRejectfromGSPSRadialMenu();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "collapsing the accepted findings");
//		panel.enableFiltersInOutputPanel(true, false, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "collapsing the rejected findings");
//		panel.enableFiltersInOutputPanel( false,true, false);
//		for(int i=0;i<4;i++)
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		
//		panel.openAndCloseOutputPanel(false);
//		panel.mouseHover(panel.getViewPort(1));
//		panel.enableFiltersInOutputPanel(true,true, true);
//		for(int i=0;i<7;i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[1."+(i+1)+"/5]", "Verify expand button display for finding "+(i+1));
//		
//		for(int i=8;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[2."+(i+1)+"/5]", "Verify collapse button display for finding "+(i+1));
//		
//		
//		panel.openAndCloseOutputPanel(false);
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Creating new point");
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1,150,50);
//		
//		panel.enableFiltersInOutputPanel(true,true, true);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[3/5]", "Verify clone entry gets added as expanded in output panel");
//		for(int i=1;i<7;i++)
//				panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[4."+(i+1)+"/5]", "Verify expand button display for finding "+(i+1));
//	
//		for(int i=9;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Checkpoint[5."+(i+1)+"/5]", "Verify collapse button display for finding "+(i+1));
//	
//	}
//
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Positive" })
//	public void test08_US1186_TC6301_TC6302_verifyExpandAllConvertsToExpandAll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify 'Expand All' button gets enabled after user collaspes the last finding which was expanded"
//				+ "<br> Verify 'Collapse All' button gets enabled after user expands the last finding which was collapse");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing all the findings");
//		panel.enableFiltersInOutputPanel(false, false, true);
//		int findings = panel.thumbnailList.size();
//		for(int i=0;i<findings;i++) {
//			panel.scrollIntoView(panel.expandCollapseToggle.get(i));
//			panel.click(panel.expandCollapseToggleDiv.get(i));
//		}
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[1/4]", "Verifying after collapsing all the findings toggel button has changed from collapse all to expand all");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[2/4]", "Verified that all rows are in collapsed state");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "expanding last findings");
//		panel.click(panel.expandCollapseToggleDiv.get(findings-1));
//		panel.scrollIntoView(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[3/4]", "Verifying if any finding is expanded toggle button should change ot collapse all");
//		panel.assertFalse(panel.thumbnailList.isEmpty(), "Checkpoint[4/4]", "Verified that thumbnail is displayed for expanded finding");
//
//		
//	}
//	
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Positive" })
//	public void test09_US1186_TC6321_TC6326_verifySendToPACSPostExpandOrCollapseAll() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the setting of SendToPacs not getting changed on performing \"Collapsed All or Expand All\"."
//				+ "<br> Verify the setting of \"Collapsed All or Expand All\" is not getting changed on changing the preference for SendToPacs.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Opening the Send to PACS menu and getting all the state");
//		sd=new ViewerSendToPACS(driver);
//		sd.openSendToPACSMenu();
//		List<String> options = sd.getStateOfSendToPACSMenu();
//		panel.mouseHover(panel.getViewPort(1));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Performing the collapse all");
//		panel.enableFiltersInOutputPanel(true, true, true);
//		panel.click(panel.expandCollapseAllToggle);
//		panel.openAndCloseOutputPanel(false);
//		
//		sd.openSendToPACSMenu();		
//		panel.assertEquals(sd.getStateOfSendToPACSMenu(), options, "Checkpoint[1/4]", "Verifying send to pacs options don't change when user perform collapse all");
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Changing the Send to PACS menu options");
//		sd.enableSendToPACSFindingOptions(true, true, true);
//		
//		panel.mouseHover(panel.getViewPort(1));
//		
//		panel.enableFiltersInOutputPanel(true, true, true);		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify all findings are expanded by default and Verify 'Collapse All' button is displayed.");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[2/4]", "Verifying that after changing the send to pacs settings toggle button state is not changed");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[3/4]", "Verified that all rows are in collapsed state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//		{
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[4."+(i+1)+"/4]", "Verify collapse button display for finding "+(i+1));
//		}
//		
//		
//		
//	}
//
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1186", "Positive" })
//	public void test10_US1186_TC6322_TC6325_verifyExpandCollapseAllForNextSessionForDifferentSession() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify \"Expand All/Collapse All\" state should be persisted in next session for the user"
//				+ "<br> Verify that User created annotations are in Collasped state Collapsed All prefernece is already set.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + pointMultiSeriesPatient + "in viewer");
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(pointMultiSeriesPatient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel = new OutputPanel(driver);
//		panel.waitForViewerpageToLoad();
//		
//		panel.enableFiltersInOutputPanel(false, false, true);		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Collapsing all the findings");
//		panel.click(panel.expandCollapseAllToggle);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify all findings are collapsed");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[1/13]", "Verify all findings are collapsed and Verify 'Expand All' button is displayed.");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[2/13]", "Verified that all rows are in collapsed state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[3."+(i+1)+"/13]", "Verify expand button display for finding "+(i+1));
//		
//		panel.openAndCloseOutputPanel(false);
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Creating new findings");
//		point = new PointAnnotation(driver);
//		
//		point.selectPointFromQuickToolbar(1);
//		point.drawPointAnnotationMarkerOnViewbox(1,100,50);
//		point.drawPointAnnotationMarkerOnViewbox(1,150,50);
//		point.drawPointAnnotationMarkerOnViewbox(1,200,50);
//		
//		panel.enableFiltersInOutputPanel(true, true, true);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[4/13]", "Verifying if the settings are saved new findings are displayed in collapsed state hence toggle button as collapse all is displayed.");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[5/13]", "Verified that all rows are in collapsed state");
//		
//		for(int i=0;i<panel.thumbnailList.size();i++)		
//			panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(i), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[6."+(i+1)+"/13]", "Verify expand button display for finding "+(i+1));
//		
//		
//		Header header = new Header(driver);
//		header.logout();
//		
//		loginPage.login(username,password);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + IHEMammoTestPatientName + "in viewer");
//		patientListPage.waitForPatientPageToLoad();
//		patientListPage.clickOnPatientRow(IHEMammoTestPatientName);
//
//		patientListPage.clickOntheFirstStudy();
//		panel.waitForViewerpageToLoad();
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[7/13]", "Verified Expand All icon when user collapse all row");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[8/13]", "Verified that all rows are in expanded state");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[9/13]", "Verified Expand icon when user collapse individual row");
//
//		panel.resizeBrowserWindow(1200,900);
//		
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[10/13]", "Verified Expand All icon when user collapse all row  - browser resize");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[11/13]", "Verified that all rows are in expanded state  - browser resize");
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseToggle.get(0), NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Checkpoint[12/13]", "Verified  Expand icon when user collapse individual row  - browser resize");
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		panel.assertTrue(db.getCollapseFindingsInOutputPanel(username),"Checkpoint[13/13]", "Verifying the DB");
//
//	}
//	
//	@Test(groups ={ "Chrome", "IE11", "Edge", "US1163", "Positive" })
//	public void test11_US1163_TC5858_verifyThumbnailTextDetailOnCollapse() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on collapse of a panel list item, summary information is displayed for DICOM & NON-DICOM data");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientName1 + "in viewer");
//
//		patientListPage = new PatientListPage(driver);
//		patientListPage.clickOnPatientRow(anonymous_Patient);
//
//		patientListPage.clickOntheFirstStudy();
//
//		OutputPanel panel=new OutputPanel(driver);
//		panel.waitForViewerpageToLoad(2);
//		cs=new ContentSelector(driver);
//
//		String resultName1=cs.getAllResults().get(0);
//		String resultName2=cs.getAllResults().get(1);
//		String resultName3=cs.getAllResults().get(2);
//
//		panel.enableFiltersInOutputPanel(false, false, true);
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[7/16]", "Verified that all rows are in collapse state");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name title for non dicom data");
//		panel.unselectMachineButton(2);
//		panel.unselectMachineButton(2);
//		//verify title
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),resultName1+ViewerPageConstants.PNG_EXTENSION, "Checkpoint[8/16]", "Verified finding name title for the png data");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)),resultName2+ViewerPageConstants.JPEG_EXTENSION, "Checkpoint[9/16]", "Verified finding name title for jpeg data");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(2)),resultName3+ViewerPageConstants.BMP_EXTENSION, "Checkpoint[10/16]", "Verified finding name title for the bmp data");
//
//		//verify series name
//		panel.assertEquals(panel.getText(panel.seriesNameForFindings.get(0)),resultName1, "Checkpoint[11/16]", "Verified series name for the png data");
//		panel.assertEquals(panel.getText(panel.seriesNameForFindings.get(1)),resultName2, "Checkpoint[12/16]", "Verified series name for the bmp data");
//		panel.assertEquals(panel.getText(panel.seriesNameForFindings.get(2)),resultName3, "Checkpoint[13/16]", "Verified series name for for jpeg data");
//
//		//verify format
//		panel.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(0)),ViewerPageConstants.PNG_EXTENSION, "Checkpoint[14/16]", "Verified format for the png data");
//		panel.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(1)),ViewerPageConstants.JPEG_EXTENSION, "Checkpoint[15/16]","Verified format for jpeg data");
//		panel.assertEquals("."+panel.getText(panel.FormatForNonDicom.get(2)),ViewerPageConstants.BMP_EXTENSION, "Checkpoint[16/16]", "Verified format for the bmp data");
//		
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(patientName1, 1, 1);
//	
//		circle=new CircleAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		lineWithUnit=new MeasurementWithUnit(driver);
//		DateFormat dateFormat=new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
//		Date date = new Date();
//		
//		String seriesname=panel.getSeriesDescriptionOverlayText(1);
//
//		circle.selectCircleFromQuickToolbar(1);
//		circle.drawCircle(1, 70, 70, 80, 80);
//		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);
//
//		panel.inputImageNumber(17, 1);
//		ellipse.selectEllipseFromQuickToolbar(1);
//		ellipse.drawEllipse(1,100, -50, 40, -50);
//		ellipse.addResultComment(ellipse.getAllEllipses(1).get(0), ellipseComment);
//
//		lineWithUnit.selectScrollFromQuickToolbar(lineWithUnit.getViewPort(1));
//		lineWithUnit.click(lineWithUnit.getViewPort(1));	
//		
//		panel.enableFiltersInOutputPanel(true, false, false);
//		
//		if(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL))
//			panel.click(panel.expandCollapseAllToggle);
//		
//		int thumbnailCount=panel.thumbnailList.size();
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/16]", "Verified that all rows are in collapse state i.e. thumbnail not visible in Output panel");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify finding name title and comment for both user drawn annotation on top of the thumbnail");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(1)),ViewerPageConstants.CIRCLE_FINDING_NAME, "Checkpoint[2/16]", "Verified finding name title for the Circle annotation");
//		panel.assertEquals(panel.getText(panel.findingsNameTitleList.get(0)),ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[3/16]", "Verified finding name title for the Ellipse annotation");
//		panel.assertEquals(panel.getText(panel.commentForFindings.get(1)),ViewerPageConstants.COMMENT_TAG+" "+circleComment, "Checkpoint[4/16]", "Verified finding comment for the circle annotation");
//		panel.assertEquals(panel.getText(panel.commentForFindings.get(0)),ViewerPageConstants.COMMENT_TAG+" "+ellipseComment, "Checkpoint[5/16]", "Verified finding comment for the Ellipse annotation");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Editor name, edition date and collapse icon on right hand side of thumbnail ");
//		for(int i=0;i<thumbnailCount;i++)
//			panel.assertTrue(panel.verifyFindingDetailsInOutputPanel(i, ViewerPageConstants.OUTPUT_PANEL_EXPAND, username, dateFormat.format(date),seriesname),"Checkpoint[6."+(i+1)+"/16]","Verified ");
//
//		//navigate back to patient list and select another patient
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + anonymous_Patient + "in viewer");
//		
//		
//
//	}
//
	@Test(groups ={"Chrome","IE11","Edge","DR2390","Positive"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
    @DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test12_DR2390_TC9382"})
//	public void test12_DR2390_TC9382_VerifyCollapseAllToggleFunctionalityForPMAP(String patientFilepath, String noOfFindings) throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Collapse All / Expand All button from output panel is working correctly on PMAP.");
//		
//		patientListPage = new PatientListPage(driver);
//		patientListPage.waitForPatientPageToLoad();
//		
//		String filePath = Configurations.TEST_PROPERTIES.get(patientFilepath);
//		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//		
//		helper=new HelperClass(driver);
//		helper.loadViewerDirectly(patientName, 1);
//
//		OutputPanel panel = new OutputPanel(driver);
//		
//		panel.enableFiltersInOutputPanel(false, false, true);
//	
//		panel.assertEquals(panel.thumbnailList.size(), helper.convertIntoInt(noOfFindings), "Checkpoint[1/3]", "Verified that all rows are in expanded state i.e thumbnail are visible in output panel");
//		panel.click(panel.expandCollapseAllToggle);
//		panel.assertEquals(panel.getAttributeValue(panel.expandCollapseAllToggle, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND_ALL, "Checkpoint[2/3]", "Verified Collpase All icon  when user Expand all row");
//		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[3/3]", "Verified that all rows are in expanded state");
//		
//	}
	@AfterMethod(alwaysRun=true)
	public void updateUserFeedbackPref() throws SQLException  {
		
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKPREFERENCESTABLE);
        db.deleteUser(username_1);
		db.deleteUser(userA);
	
	}

}
