package com.trn.ns.test.viewer.SC;

import java.sql.SQLException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
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
import com.trn.ns.page.factory.PolyLineAnnotation;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AcceptRejectForSCTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector contentSelector;
	private OutputPanel panel;
	private PolyLineAnnotation poly;
	private HelperClass helper;
	private ViewerSliderAndFindingMenu findingMenu;

	String username = "test";
	String EllipseComment="ellipseComment";

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String filePath_3ChestCT1p25mm = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath_3ChestCT1p25mm);

	String filePathBoneAge = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String patientName_BoneAge = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathBoneAge);

	String vidaCaseFile = Configurations.TEST_PROPERTIES.get("VidaCase02_Filepath");
	String vidaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, vidaCaseFile);

	String MammoCad = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String CadPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, MammoCad);


	public final String ANNOTATION_TXT_1="ABC";
	public final String ANNOTATION_TXT_2="DEF";

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//US1075: Show regular A/R toolbar for DICOM-SC result
	@Test(groups ={"Chrome","IE11","Edge","US1075","DE1383","DE1928","US2523","Positive","BVT","F1090","E2E"})
	public void test01_US1075_TC5244_TC5246_DE1383_TC5716_DE1928_TC7792_US2523_TC10401_verifyARToolBarOnDICOMSCData() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance(); 
		extentTest.setDescription("Verify the new UI of AR tool bar on Dicom SC data when neither finding is selected nor focused."
				+ "<br> Verify that Dicom SC result series name is getting displayed in findings menu in new AR tool bar when no findings selected or focused"
				+ "<br> Verify there is no console error when SC series is selected from finding menu drop down. <br>"
				+ "Verify the AR tool bar is displayed for dicom series. <br>"+
				"[Risk and Impact]: Verify that no quick toolbox is displayed for PDF's.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);
		contentSelector=new ContentSelector(driver);
		//Loading the patient on viewer

		String resultName=viewerPage.getSeriesDescriptionOverlayText(1);
		contentSelector.openAndCloseSeriesTab(true);

		String machineName=contentSelector.allMachineName.get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/21]", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on accept reject toolbar");
		viewerPage.mouseHover(findingMenu.acceptRejectToolbar);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsAccept),"Checkpoint[2/21]" , "Verify Accept button on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsReject), "Checkpoint[3/21]", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsNext), "Checkpoint[4/21]", "The Next arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsPrevious), "Checkpoint[5/21]", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsDelete), "Checkpoint[6/21]", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsText), "Checkpoint[7/21]", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsFinding), "Checkpoint[8/21]", "The Previous arrow is displayed on radial menu");

		viewerPage.assertEquals(findingMenu.getBadgeCountFromToolbar(1),1, "Checkpoint[9/21]", "The number of pending finding is "+findingMenu.getBadgeCountFromToolbar(1));
		viewerPage.assertEquals(findingMenu.getFindingsCountFromFindingTable(1),findingMenu.getBadgeCountFromToolbar(), "Checkpoint[10/20]", "The number of pending finding on header of table is "+findingMenu.getBadgeCountFromToolbar());
		viewerPage.assertTrue(findingMenu.verifySeriesDescriptionOnFindingTable(resultName), "Checkpoint[11/21]", "The series description on finding table is "+resultName);
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(1),resultName , "Checkpoint[12/21]", "The name of first finding is: "+findingMenu.getTextOfFindingFromTable(1));
		viewerPage.assertTrue(viewerPage.getText(findingMenu.findingsCreatedByUser.get(0)).contains(machineName), "Checkpoint[13/21]", "The name of first finding is: "+findingMenu.getTextOfFindingFromTable(1));

		findingMenu.selectFindingFromTable(1);
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[14/21]", "Verifying there is no console error present");


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/21]", "Verify state of Secondary capture result after loading in viewer page is Pending");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[16/21]", "On hover over Accept icon from A/R bar green color should not be seen");
		viewerPage.assertFalse(findingMenu.verifyResultsAreRejected(1), "Checkpoint[17/21]", "On hover over Reject icon from A/R bar red color should not be seen");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[18/21]", "Verify Result Applied tag on view box at bottom left of the view box.");
		viewerPage.assertFalse(viewerPage.verifyResultAppliedTextPresence(1), "Checkpoint[19/21]", "Verified that Result applied text not present on secondary capture");

		viewerPage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(resultName), "Checkpoint[20/21]", "Verified SC result highlighted in content selector");

		//TC10401 
		viewerPage.performMouseRightClick(viewerPage.getViewPort(2));
		viewerPage.assertFalse(viewerPage.verifyAllIconsPresenceInQuickToolbar(), "Checkpoint[21/21]", "Verified that quick toolbar not visible on PDF.");


	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","DR2717","Negative","F301"})
	public void test02_US1075_TC5245_DR2717_TC10602_verifyARToolBarWhenGSPSFindingIsSelected() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the AR tool bar UI when GSPS findings is in focused or Selected. <br>"+
		"Verify that there is no console error when mouse is hovered on PDF view box.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);
		viewerPage.waitForPdfToRenderInViewbox(2);
		
		contentSelector=new ContentSelector(driver);
		ellipse=new EllipseAnnotation(driver);
		//Loading the patient on viewer and draw annotation on secondary capture data

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify drawn annotation is highlighted");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify drawn annotation is current active accepted GSPS", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify A/R toolbar when mouse hover on GSPS hover container ");
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(findingMenu.isAcceptRejectToolBarPresent(1), "Verify A/R toolbar display when mouse hover", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify A/R toolbar when finding is selected or highlighted ");
		findingMenu.selectFindingFromTable(2);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsAccept),"Verify Accept button on radial menu" , "The Accept button is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsDelete), "Verify Previous arrow on radial menu", "The Delete button is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsText), "Verify Previous arrow on radial menu", "The Add text is displayed on radial menu");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.gspsFinding), "Verify Previous arrow on radial menu", "The GSPS finding is displayed on radial menu");

		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[4/4]", "Verified that no console error seen when mousehover on PDF.");
	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test03_US1075_TC5247_TC5254_verifyARToolBarWhenGSPSFindingIsSelected() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Dicom SC result series name is getting displayed in findings menu in AR tool bar when GSPS finding is selected or focused <BR>"
				+"Verify the functionality when user selects the finding annotation from the finding menu when DICOM SC result source is loaded.");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbioPatientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		contentSelector=new ContentSelector(driver);
		ellipse=new EllipseAnnotation(driver);
		//Loading the patient on viewer and draw annotation
		String resultName=viewerPage.getSeriesDescriptionOverlayText(1);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 0, 0, -100,-150);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/12]", "Verify drawn annotation is highlighted");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Verify drawn annotation is current active accepted GSPS", "Verified");

		viewerPage.assertTrue(findingMenu.isAcceptRejectToolBarPresent(1), "Checkpoint[2/12]", "Verified A/R toolbar display on mouse hover");

		viewerPage.assertTrue(findingMenu.verifySeriesDescriptionOnFindingTable(resultName), "Checkpoint[3/12]", "The series description on finding table is "+resultName +" display");

		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(1),resultName , "Checkpoint[4/12]", "The name of first finding is: "+findingMenu.getTextOfFindingFromTable(1));
		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(2),ViewerPageConstants.ELLIPSE_FINDING_NAME , "Checkpoint[5/12]", "The name of second finding is: "+findingMenu.getTextOfFindingFromTable(1));
		viewerPage.assertEquals(viewerPage.getCssValue(findingMenu.findingRows.get(1), NSGenericConstants.BACKGROUND_COLOR), ThemeConstants.EUREKA_POPUP_BACKGROUND,"Checkpoint[6/12]", "Verified background color of finding in finding menu as Grey");

		findingMenu.selectFindingFromTable(1);
		viewerPage.assertFalse(ellipse.isEllipsePresent(1), "Checkpoint[7/12]", "Annotation not present when DICOM SC result open from finding menu toolbar");

		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Checkpoint[8/12]", "Verified presence of Result applied text on DICOM SC result after drawing annotation");
		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.resultApplied(1), NSGenericConstants.FILL), ViewerPageConstants.COLOUR_GREY, "Checkpoint[9/12]", "Verified fill color of Result applied text after drawing annotation on DICOM SC result as" +ViewerPageConstants.COLOUR_GREY );

		//open finding menu and select second finding and verify result applied text
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		findingMenu.openFindingTableOnBinarySelector(1);
		findingMenu.selectFindingFromTable(2);
		viewerPage.assertTrue(ellipse.isEllipsePresent(1), "Checkpoint[10/12]", "Ellipse present when ellipse finding open from finding menu");

		viewerPage.assertTrue(viewerPage.verifyResultAppliedTextPresence(1), "Checkpoint[11/12]", "Result applied text not present on DICOM SC result after drawing annotation");	
		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.resultApplied(1), NSGenericConstants.FILL), ViewerPageConstants.COLOUR_WHITE, "Checkpoint[12/12]", "Verified fill color of Result applied text after drawing annotation on DICOM SC result as" +ViewerPageConstants.COLOUR_WHITE );
	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test04_US1075_TC5271_TC5276_verifyGSPSNotificationWhenNoGSPSSelected() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Comment button ,Accept and reject button not showing any tool tip message on mouse hovering when no GSPS finding is selected. <br>"
				+ "Verify the AR tool bar when  measurement is deselected.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);
		
		contentSelector=new ContentSelector(driver);
		ellipse=new EllipseAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		circle=new CircleAnnotation(driver);
		panel=new OutputPanel(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 100, 50);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/11]", "Verify last  drawn annotation is selected and accept reject toolbar visible on viewer");
		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[2/11]", "Verified last drawn circle annotation is active accepted GSPS");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[3/11]", "Verify Accept Reject toolbar display when annotation is selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoin[4/11]", "Verify Comment button ,Accept and reject button showing tool tip message on mouse hovering when annotation is not selected" );
		viewerPage.click(viewerPage.getViewPort(3));
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.mouseHover(findingMenu.gspsAccept);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [5/11]", "verifying the GSPS notification when mousehover on GSPS accept button ");

		viewerPage.mouseHover(findingMenu.gspsReject);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [6/11]", "verifying the GSPS notification when mousehover on GSPS reject button ");

		viewerPage.mouseHover(findingMenu.gspsText);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [7/11]", "verifying the GSPS notification when mousehover on GSPS comment button");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoin[8/11]", "Verify Comment button ,Accept and reject button not showing any tool tip message on mouse hovering for SC result" );
		viewerPage.toggleOnOrOffResultUsingKeyboardGKey();
		viewerPage.waitForTimePeriod(1000);
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.mouseHover(findingMenu.gspsAccept);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [9/11]", "verifying the no GSPS notification when mousehover on GSPS accept button ");

		viewerPage.mouseHover(findingMenu.gspsReject);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [10/11]", "verifying the no GSPS notification when mousehover on GSPS reject button ");

		viewerPage.mouseHover(findingMenu.gspsText);
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.getOuterGSPSNotification()),"Checkpoint [11/11]", "verifying the no GSPS notification when mousehover on GSPS comment button");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test05_US1075_TC5275_verifyAcceptRejectOnDicomSRAndNonDicom() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the AR tool bar on DICOM SR and NON DICOM data.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(ChestCT1p25mmPatient, 1);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Checkpoint[1/2","AR tool bar is present on DICOM-SR patient" +" "+ChestCT1p25mmPatient);

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName_BoneAge, 1, 4);

		
		viewerPage.mouseHover(findingMenu.acceptRejectToolbar);
		viewerPage.assertTrue(findingMenu.verifyBinarySelectorToobar(1),"Checkpoint[2/2]","AR tool bar is present for NON-DICOM patient"+" "+patientName_BoneAge);	
	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test06_US1075_TC5280_verifyNotificationCountInFindingMenu() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verfify the notification count in finding menu tool bar.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);

		circle = new CircleAnnotation(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.acceptRejectToolbar);
		viewerPage.assertEquals(findingMenu.getBadgeCountFromToolbar(1),1, "Checkpoint[1/7]", "The number of pending finding is "+findingMenu.getBadgeCountFromToolbar(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify drawn annotation is in accepted state and finding count still 1 in finding toolbar" );
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[3/7]", "Verified that circle annotation is accepted GSPS");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[4/7]", "Verified that linear measurement is acccepted GSPS");
		viewerPage.assertEquals(findingMenu.getBadgeCountFromToolbar(1),1, "Checkpoint[5/7]", "The number of pending finding is "+findingMenu.getBadgeCountFromToolbar(1));

		findingMenu.selectAcceptfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		viewerPage.assertEquals(findingMenu.getBadgeCountFromToolbar(1),2, "Checkpoint[6/7]", "Verify Findings count get increased from 1 to 2 in AR tool bar after change state of GSPS annotation to pending");
		
		findingMenu.selectFindingFromTable(1);

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.acceptRejectToolbar);
		findingMenu.selectAcceptfromGSPSRadialMenu();
		

		viewerPage.click(viewerPage.getResultAppliedAnnotation(1));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		findingMenu.openFindingTableOnBinarySelector(1);

		viewerPage.assertEquals(findingMenu.getBadgeCountFromToolbar(1),1, "Checkpoint[7/7]", "Verify Findings count get decreased from 2 to 1 in AR tool bar after change state of GSPS annotation to accepted");


	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity","BVT"})
	public void test07_US1075_TC5314_verifyNavigationForSCWhenAnnotationOnSameSlice() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the naivagtion over the GSPS findings and on DICOM SC result series when annotation are on same slices.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);

		panel=new OutputPanel(driver);
		panel.waitForViewerpageToLoad(3);
		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/11]", "Verify drawn annotation is in accepted state from output panel" );
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[2/11]", "Verified finding count from output panel ");
		panel.openAndCloseOutputPanel(false);

		panel.mouseHover(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.isElementPresent(panel.acceptRejectToolbar), "Checkpoint[3/11]","Verify accept reject tool when mousehover after drawing annotation");

		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(panel.isElementPresent(panel.resultApplied(1)), "Checkpoint[4/11]","Verified presence of Result applied text on secondary capture");
		panel.assertEquals(panel.getAllGSPSObjects(1).size(), 0, "Checkpoint[5/11]", "Verified that there is not GSPS object present on Secondary capture data");

		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[6/11]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(2), "Checkpoint[7/11]", "Verify finding is highlighted in finding table");
		panel.mouseHover(panel.getViewPort(1));
		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[8/11]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(3), "Checkpoint[9/11]", "Verify finding is highlighted in finding table");
		panel.mouseHover(panel.getViewPort(1));
		panel.selectPreviousfromGSPSRadialMenu();
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[10/11]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(2), "Checkpoint[11/11]", "Verify finding is highlighted in finding table");	
	}

	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test08_US1075_TC5324_verifyNavigationForSCWhenAnnotationOnDifferentSlice() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the naivagtion over the GSPS findings and on DICOM SC result series when annotation are on same slices.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);


		panel=new OutputPanel(driver);		
		panel.waitForViewerpageToLoad(3);
		circle = new CircleAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/11]", "Verify drawn annotation is in accepted state from output panel" );
		//draw annotation and note down finidng count from output panel
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		circle.scrollToImage(1, 120);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[2/11]", "Verified finidng size display in output panel for Accepted findings");

		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.isElementPresent(panel.acceptRejectToolbar), "Checkpoint[3/11]","Verified accept reject toolbar when mousehover after drawing annotation");

		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(panel.isElementPresent(panel.resultApplied(1)), "Checkpoint[4/11]","Verified Result applied text presence after selecting next from A/R toolbar");
		panel.assertTrue(panel.getAllGSPSObjects(1).isEmpty(), "Checkpoint[5/11]", "Verified that no GSPS is present on Secondary capture data");

		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[6/11]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(2), "Checkpoint[7/11]", "Verify finding is highlighted in finding table");
		panel.mouseHover(panel.getViewPort(1));
		panel.selectNextfromGSPSRadialMenu();
		panel.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[8/1]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(3), "Checkpoint[9/11]", "Verify finding is highlighted in finding table");
		panel.mouseHover(panel.getViewPort(1));
		panel.selectPreviousfromGSPSRadialMenu();
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[10/11]", "Verify annotation is selected");
		panel.assertTrue(panel.verifyFindingIsHighlighted(2), "Checkpoint[11/11]", "Verify finding is highlighted in finding table");

	}

	@Test(groups ={"Chrome","IE11","Edge","DE1392","Negative"})
	public void test09_DE1392_TC5271_TC5276_verifyAcceptRejectToolBarOnSC() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Hardening]: Accept/Reject binary toolbar disable when left click perform on secondary capture data to deselect annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);

		contentSelector=new ContentSelector(driver);
		ellipse=new EllipseAnnotation(driver);
		lineWithUnit=new MeasurementWithUnit(driver);
		circle=new CircleAnnotation(driver);
		panel=new OutputPanel(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/28]", "Verify accept reject functionality for circle annotation on SC data");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);

		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[2/28]", "Verified drawn circle annotation is active accepted GSPS");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[3/28]", "Verify Accept Reject toolbar display when annotation is selected");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[4/28]", "Verify that no console error seen while drawing circle annotation on SC data");

		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[5/28]", "Verify Accept Reject toolbar not display when annotation is not selected");

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[6/28]", "Verify Accept Reject toolbar display when mousehover on Accept reject toolbar area");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[7/28]", "Verify that no console error seen when mousehover on Accept reject toolbar area");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/28]", "Verify accept reject functionality for ellipse annotation on SC data");
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[9/28]", "Verified  drawn ellipse annotation is active accepted GSPS");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[10/28]", "Verify Accept Reject toolbar display when annotation is selected");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[11/28]", "Verify that no console error seen while drawing ellipse annotation on SC data");

		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[12/28]", "Verify that no console error seen while drawing ellipse annotation on SC data");

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[13/28]", "Verify Accept Reject toolbar display when mousehover on Accept reject toolbar area");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[14/28]", "Verify that no console error seen when mousehover on Accept reject toolbar area");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[15/28]", "Verify accept reject functionality for linear measurement annotation on SC data");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 100, 50);

		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[16/28]", "Verified  drawn linear measurement annotation is active accepted GSPS");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[17/28]", "Verify Accept Reject toolbar display when annotation is selected");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[18/28]", "Verify that no console error seen while drawing linear measurement annotation on SC data");

		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[19/28]", "Verify Accept Reject toolbar not display when annotation is not selected");

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[20/28]", "Verify Accept Reject toolbar display when mousehover on Accept reject toolbar area");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[21/28]",  "Verify that no console error seen when mousehover on Accept reject toolbar area");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[22/28]", "Verify accept reject functionality for polyline annotation on SC data");
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);

		viewerPage.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[23/28]", "Verified  drawn polyline annotation is active accepted GSPS");
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[24/128]", "Verify Accept Reject toolbar display when annotation is selected");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[25/28]", "Verify that no console error seen while drawing polyline annotation on SC data");

		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertFalse(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[26/28]", "Verify Accept Reject toolbar not display when annotation is not selected");

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.acceptRejectToolbar), "Checkpoint[27/28]", "Verify Accept Reject toolbar display when mousehover on Accept reject toolbar area");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[28/28]", "Verify that no console error seen when mousehover on Accept reject toolbar area");

	}	

	@Test(groups ={"Chrome","IE11","Edge","DE1320","Positive"})
	public void test10_DE1320_TC5376_verifySCForVida() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that data should be loaded into viewer");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(vidaPatientName, 2);
		viewerPage.assertFalse(viewerPage.getSeriesDescriptionOverlayText(1).isEmpty(), "Checkpoint[1/5]", "Verifying that series 1 is displayed");
		viewerPage.assertFalse(viewerPage.getSeriesDescriptionOverlayText(2).isEmpty(), "Checkpoint[2/5]", "Verifying that series 2 is displayed");
		viewerPage.assertFalse(viewerPage.getSeriesDescriptionOverlayText(3).isEmpty(), "Checkpoint[3/5]", "Verifying that series 3 is displayed");

		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint[4/4]", "test12_01");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[5/5]", "Verifying the console error is absent");


	}

	@Test(groups ={"Chrome","IE11","Edge","DE1820","Positive"})
	public void test11_DE1820_TC7363_verifyNavigationHappeningInCorrectOrderOnGSPS() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Navigation is happening over findings in correct order on multiple slice GSPS patient.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);

		circle=new CircleAnnotation(driver);
		circle.waitForViewerpageToLoad();

		lineWithUnit=new MeasurementWithUnit(driver);
		circle=new CircleAnnotation(driver);
		poly = new PolyLineAnnotation(driver);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying navigation forward and backward is working in correct order after drawing annotation on multiple slices");

		//Select Circle annotation from Radial Menu
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 80, 80);

		//navigate to slice next slice
		circle.scrollDownToSliceUsingKeyboard(4);

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		//navigate to slice next slice
		circle.scrollDownToSliceUsingKeyboard(3);

		//Select Polyline annotation from Radial Menu
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		
		//navigate to slice previous slice
		circle.scrollUpToSliceUsingKeyboard(2);
		
		circle.click(circle.getViewPort(1));
		
		circle.mouseHover(circle.getAcceptRejectToolBar(1));
	
		//Jumping to Next slice on which annotation is present and Verifying that annotation is selected
		poly.selectNextfromGSPSRadialMenu();
	
		poly.assertTrue(poly.verifyPolyLineAnnotationIsCurrentActiveAcceptedGSPS(1,1), "Checkpoint[2/3]", "Verify Polyline is selected and highlighted on view box");
		
 		//Jumping to Previous slice on which annotation is present and Verifying that annotation is selected
 		lineWithUnit.selectPreviousfromGSPSRadialMenu();
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[3/3]", "Verify Linear measurement is selected and highlighted on view box");

	}

	@Test(groups ={"Chrome","Edge","IE11","US1046","Positive"})
	public void test12_US1046_TC6323_TC6328_verifyStateOfSCAfterRevisit() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of the SC result after re-visiting viewer. <br>"
				+ "Verify SC can be selected from finding menu when no GSPS is present on it.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbioPatientName, 1);

		findingMenu=new ViewerSliderAndFindingMenu(driver);

		String resultName=viewerPage.getSeriesDescriptionOverlayText(1);

		//Loading the patient on viewer and draw annotation on secondary capture data
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertFalse(findingMenu.verifyResultsAreAccepted(1)&& findingMenu.verifyResultsAreRejected(1), "Checkpoint[1/7]", "Verified that SC data is in pending state when default loaded");

		//accept SC and verify after reload
		findingMenu.selectAcceptfromGSPSRadialMenu();
		viewerPage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[2/7]", "Verified that state of SC data as Accepted");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbioPatientName, 1, 1);

		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(findingMenu.verifyResultsAreAccepted(1), "Checkpoint[3/7]", "Verified that state of SC data as Accepted after reload of viewer page");

		//reject SC and verify after reload
		findingMenu.selectRejectfromGSPSRadialMenu();
		viewerPage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[4/7]","Verified that state of SC data as Rejected");

		helper.browserBackAndReloadViewer(imbioPatientName, 1, 1);
		viewerPage.mouseHover(findingMenu.getGSPSHoverContainer(1));
		viewerPage.assertTrue(findingMenu.verifyResultsAreRejected(1), "Checkpoint[5/7]","Verified that state of SC data as Rejected after reload of viewer page");

		viewerPage.assertEquals(findingMenu.getTextOfFindingFromTable(1),resultName , "Checkpoint[6/7]", "Verified name of SC in finding menu as : "+findingMenu.getTextOfFindingFromTable(1));
		findingMenu.selectFindingFromTable(1);
		viewerPage.assertFalse(findingMenu.isAcceptRejectToolBarPresent(1), "Checkpoint[7/7]", "Verified that A/R toolbar not visible after selecting SC result from finding menu.");

	}


	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERFEEDBACKTABLE);
	}

}
