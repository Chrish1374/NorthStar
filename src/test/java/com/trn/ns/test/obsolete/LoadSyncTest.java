//package com.trn.ns.test.obsolete;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class LoadSyncTest extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//	
//	private ContentSelector contentSelector;
//
//	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4atientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath1);
//
//	String filePath2 = Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath");
//	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath2);
//
//	String aidoc_filepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
//	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, aidoc_filepath);
//
//	String sagittal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//	String sagittal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//
//	String axial_seriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//	String axial_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//
//	String coronal_SeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//	String coronal_ImageNum = DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Aidoc_filepath"));
//
//	String filePath9 = Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath");
//	String Modality_LUT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath9);
//
//	String filePath11 = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//	String NorthStar_MR_LSP = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath11);
//
//	String filePath10 = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
//	String TeraRecon_BrainTDA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath10);
//	
//	String filePath12 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
//	String Anonymous_Data = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath12);
//	
//	String filePath13 = Configurations.TEST_PROPERTIES.get("Breast_MR_2_filepath");
//	String Breast_MR_2_patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath13);
//	
//
//	private String fifthSeriesDescriptionModality_LUT = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES06_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath"));
//	private String sixthSeriesDescriptionModality_LUT = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_SERIES07_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TEST^Modality_LUT_filepath"));
//	private String fifthSeriesDescriptionMrLSP = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("NorthStar_MR_LSP_filepath"));
//	private String fourteenthSeriesDescriptionBrainTDA = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_RESULT14_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath"));
//	private String thirdSeriesDescriptionBrainTDA = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_RESULT03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath"));
//	//private String fifthSeriesDescriptionBrainTDA = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_RESULT05_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath"));
//	private String firstSeriesDescriptionAnonmyous = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath12);
//	private MeasurementWithUnit lineWithUnit;
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws IOException, InterruptedException, SQLException {
//
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	//@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
//	public void test01_US741_TC2646_VerifyGSPSLoading() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content when viewer loads with series containing GSPS");
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+mrLSPPatientName);
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(mrLSPPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
//		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the first and fourth viewbox");
//		viewerPage.scrollDownToSliceUsingKeyboard(1,3);
//		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
//
//		int currentImageNumberVB1 = viewerPage.getCurrentScrollPositionOfViewbox(1);
//		int currentImageNumberVB4 = viewerPage.getCurrentScrollPositionOfViewbox(4);
//
//		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
//		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing lines on viewbox first and fourth");
//		lineWithUnit.selectDistanceMeasurementFromRadial(3);
//		lineWithUnit.drawLine(1, 50, 50, 100, 50);
//		lineWithUnit.drawLine(4, 50, 50, 100, 50);
//
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.selectLayout(viewerPage.twoByTwoLayoutIcon);
//
//		// Verifying that GSPS is getting loaded
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), currentImageNumberVB1, "Verifying the current image number of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), currentImageNumberVB1, "Verifying the current image number of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), currentImageNumberVB1, "Verifying the current image number of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), currentImageNumberVB4, "Verifying the current image number of viewbox 4", "verified");
//
//		// verifying the WW/WL
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying the window width of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying the window width of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying the window width of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying the window width of viewbox 4", "verified");
//
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying the window center of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying the window center of viewbox 4", "verified");
//
//		//verifying the zoom is default to zoom to fit
//		viewerPage.getZoomLabelOverlay(1).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox2", "verified");
//
//		viewerPage.getZoomLabelOverlay(3).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox3", "verified");
//
//		viewerPage.getZoomLabelOverlay(4).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox4", "verified");
//
//	}
//
//	
//
//	//@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
//	public void test07_US741_TC2653_VerifyMachineDirective() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content after layout change when viewer loads with series containing machine directive and GSPS");
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+aidocPatientName);
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(aidocPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
//		
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aidocPatientName+" in viewer" );
//
//		//Verify default slice position after loading viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying slice position for images in each viewbox of Aidoc patient data");
//		viewerPage.assertEquals(viewerPage.getImageCurrentScrollPositionOverlayText(1), sagittal_ImageNum, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '5'");
//		viewerPage.assertEquals(viewerPage.getImageCurrentScrollPositionOverlayText(2), axial_ImageNum, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '101'");
//		viewerPage.assertEquals(viewerPage.getImageCurrentScrollPositionOverlayText(3), coronal_ImageNum, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");
//
//		//Step -2 Drawing annotations on new slice
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing ellipse on 10th slice of 'sagittal' and 'coronal' series" );
//		viewerPage.inputImageNumber(10, 1);
//		ellipse.selectEllipseAnnotationFromContextMenu(1);
//		ellipse.drawEllipse(1, -20, -10, 40,50);	
//
//		viewerPage.inputImageNumber(15, 3);
//		ellipse.drawEllipse(3, -20, -10, 30,50);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying presence of drawn annotation");
//		viewerPage.assertTrue(ellipse.isEllipsePresent(1),"verify the ellipse is present on slice 10 of first viewbox", "Ellipse is present");
//		viewerPage.assertTrue(ellipse.isEllipsePresent(3),"verify the ellipse is present on slice 10 of third viewbox", "Ellipse is present");
//
//		//Step -3  Verifying the presence of drawn annotations
//		viewerPage.browserBackWebPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying slice position for images in each viewbox after reloading to viewer");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), 10, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '5'");
//		viewerPage.assertEquals(viewerPage.getImageCurrentScrollPositionOverlayText(2), axial_ImageNum, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '101'");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3), 15, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");
//
//		//Step- 4 Changing the layout and verifying the presence of annotation
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing layout to 2x3" );
//		viewerPage.selectLayout(viewerPage.twoByThreeLayoutIcon);
//
//
//		//Verify the images are load as per machine directives
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying slice position for images selected through content selector ");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), 10, "Verify that 'sagittal' series should load at slice number '5'", "'sagittal' series is loading at slice number '10'");
//		viewerPage.assertEquals(viewerPage.getImageCurrentScrollPositionOverlayText(6), axial_ImageNum, "Verify that 'axial' series should load at slice number '101'", "'axial' series is loading at slice number '94'");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(5), 15, "Verify that 'coronal' series should load at slice number '15'", "'coronal' series is loading at slice number '15'");
//
//	}
//
//	
//	//@Test(groups ={"Chrome", "Edge","firefox", "IE11"})
//	public void test38_US741_TC2605_verifyWLSyncForSeriesWithDifferentBitandDifferentPIWithFlagSetAbsolute() throws  InterruptedException, IOException	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify WL Synchronization for Series having different bit but same Photometric interpretation with flag set as absolute");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+Modality_LUT+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(Modality_LUT);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//		
//		int beforeWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//		int	beforeWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.viewboxImg4);
//		viewerPage.dragAndReleaseOnViewerWithClick(4,0, 0, 100, -50);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Window Leveling takes place in 4th viewbox.");
//		viewerPage.assertNotEquals(beforeWW4, Integer.parseInt(viewerPage.getValueOfWindowWidth(4)) , "Verifying window width is  changed in viewbox 1", "Verified window width is  changed in viewbox 1");
//		viewerPage.assertNotEquals(beforeWC4, Integer.parseInt(viewerPage.getValueOfWindowCenter(4)) , "Verifying window center is  changed in viewbox 1", "Verified window center is  changed in viewbox 1");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Window Leveling takes place in 4th viewbox.");
//		contentSelector.selectSeriesFromContentSelector(1,fifthSeriesDescriptionModality_LUT);
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(1, fifthSeriesDescriptionModality_LUT), "Verifying newly displayed result on viewbox is higlighted on content selector", "Verified newly displayed result on viewbox is higlighted on content selector");
//
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Window Leveling takes place in 4th viewbox.");		
//		viewerPage.assertEquals(afterWW1,afterWW4 , "Verifying window width remains same in viewbox 2", "Verified window width remains same in viewbox 2");
//		viewerPage.assertEquals(afterWW1,afterWW2, "Verifying window center remains samein viewbox 2", "Verified window center remains samein viewbox 2");
//		viewerPage.assertEquals(afterWW1,afterWW3, "Verifying window width remains same in viewbox 3", "Verified window width remains same in viewbox 3");
//		viewerPage.assertEquals(afterWC1,afterWC4, "Verifying window center remains same in viewbox 3", "Verifying window center remains same in viewbox 3");
//		viewerPage.assertEquals(afterWC1,afterWC2, "Verifying window width remains same in viewbox 4", "Verified window width remains same in viewbox 4");
//		viewerPage.assertEquals(afterWC1,afterWC3, "Verifying window center remains same in viewbox 4", "Verifying window center remains same in viewbox 4");
//
//	}
//
//
//	//@Test(groups ={"IE11","Chrome","firefox", "Chrome","US741" })
//	//Not working on IE11, FireFox 
//	public void test39_US741_TC2608_verifyWWWLSyncSamePriorityBitAndPhotometricInterpretation() throws InterruptedException  
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify WW/WL sync when new series is added into first viewbox having SAME priority, bit and Photometric interpretation as existing series.");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Loading the Patient "+Modality_LUT+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(Modality_LUT);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//		
//		int beforeWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//		int	beforeWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		int beforeWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int	beforeWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//
//
//		//changing WWWL of 4th viewbox
//		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.viewboxImg4);
//		viewerPage.dragAndReleaseOnViewerWithClick(4,0, 0, 100, -50);
//
//		//Verifying that WWWL is performed in 4th viewbox
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify Window Leveling takes place in 4th viewbox.");
//		viewerPage.assertNotEquals(beforeWW4, Integer.parseInt(viewerPage.getValueOfWindowWidth(4)) , "Verifying window width is  changed in viewbox 4", "Verified window width is  changed in viewbox 4");
//		viewerPage.assertNotEquals(beforeWC4, Integer.parseInt(viewerPage.getValueOfWindowCenter(4)) , "Verifying window center is  changed in viewbox 4", "Verified window center is  changed in viewbox 4");
//
//		//selecting 6th series in 1st viewbox and verifying that it is selected
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify that the sixth series is getting selected in 1stviewbox.");
//		contentSelector.selectSeriesFromContentSelector(1,sixthSeriesDescriptionModality_LUT);
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(1, sixthSeriesDescriptionModality_LUT), "Verify that the sixth series is getting selected in 1stviewbox.", "Verified that the sixth series is getting selected in 1stviewbox.");
//
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		//verifying that the WWWL is unaffected in first viewbox wrt to other viewboxes and first
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify Window Leveling takes place in all viewboxes.");		
//		viewerPage.assertNotEquals(afterWW1,afterWW4 , "Verifying window width does not remains same in viewbox 4", "Verified window width does not remains same in viewbox 4");
//		viewerPage.assertNotEquals(afterWW1,afterWW2, "Verifying window width does not remains samein viewbox 2", "Verified window width does not remains samein viewbox 2");
//		viewerPage.assertNotEquals(afterWW1,afterWW3, "Verifying window width does not remains same in viewbox 3", "Verified window width does not remains same in viewbox 3");
//		viewerPage.assertNotEquals(afterWC1,afterWC4, "Verifying window center does not remains same in viewbox 4", "Verifying window center does not remains same in viewbox 4");
//		viewerPage.assertNotEquals(afterWC1,afterWC2, "Verifying window center does not remains same in viewbox 2", "Verified window center does not remains same in viewbox 3");
//		viewerPage.assertNotEquals(afterWC1,afterWC3, "Verifying window center does not remains same in viewbox 3", "Verifying window center does not remains same in viewbox 2");
//
//		viewerPage.assertNotEquals(beforeWW1, Integer.parseInt(viewerPage.getValueOfWindowWidth(1)) , "Verifying window width is not changed in viewbox 1 ", "Verified window width is not changed in viewbox 1");
//		viewerPage.assertNotEquals(beforeWC1, Integer.parseInt(viewerPage.getValueOfWindowCenter(1)) , "Verifying window center is not changed in viewbox 1", "Verified window center is not changed in viewbox 1");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify Window Leveling remains same in all viewboxes.");		
//
//		viewerPage.assertEquals(afterWW2,afterWW3 , "Verifying window width remains same in viewbox 2 as viewbox 3", "Verified window width remains same in viewbox 2 as viewbox 3");
//		viewerPage.assertEquals(afterWW2,afterWW4, "Verifying window width remains same in viewbox 2 as viewbox 4", "Verified window width remains same in viewbox 2 as viewbox 4");
//		viewerPage.assertEquals(afterWC2,afterWC3, "Verifying window center remains same in viewbox 3 as viewbox 2", "Verified window center remains same in viewbox 3 as viewbox 2");
//		viewerPage.assertEquals(afterWC2,afterWC4, "Verifying window center remains same in viewbox 4 as viewbox 2", "Verifying window center remains same in viewbox 4 as viewbox 2");
//
//
//	}
//
//
//	
//	//@Test(groups = { "Chrome","firefox","US741"})
//	public void test34_US741_TC2656_verifyViewboxContentFromContentSelectorInGSPSSeries() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content when new series is selected through content selector having GSPS before source");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+NorthStar_MR_LSP+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		contentSelector = new ContentSelector(driver);
//		CircleAnnotation circle = new CircleAnnotation(driver);
//		
//		//Select 3*3 layout
//		viewerPage.selectLayout(viewerPage.threeByThreeLayoutIcon);
//
//		//Input image number 10 in 5th viewbox
//		viewerPage.inputImageNumber(10, 5);
//
//		//Draw an annotation on the 10th slice in 5th viewbox
//		circle.selectCircleAnnotationIconFromRadialMenu(5);
//		circle.drawCircle(5, 0, 0, 10, 20);
//
//		//Go back to study list page and re-visit viewer page
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Select 5th series through content selector from second viewbox.
//		contentSelector.selectSeriesFromContentSelector(2,fifthSeriesDescriptionMrLSP);	
//
//		//The 2nd and 3rd viewbox has slice number as 6
//		viewerPage.assertEquals(6,viewerPage.getCurrentScrollPositionOfViewbox(2), "verifying that first 1 and 2 viewboxes are in sync", "verified");
//		viewerPage.assertEquals(6,viewerPage.getCurrentScrollPositionOfViewbox(3), "verifying that first 1 and 3 viewboxes are in sync", "verified");
//
//		//Fetching Window width and Window center's values
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		//Asserting that WWWL values are same for all viewboxes
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Window Leveling takes place in 4th viewbox.");		
//		viewerPage.assertEquals(afterWW1,afterWW4 , "Verifying window width remains same in viewbox 4 as viewbox 1", "Verified window width remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW2, "Verifying window width remains same in viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW3, "Verifying window width remains same in viewbox 3 as viewbox 1", "Verified window width remains same in viewbox 3 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC4, "Verifying window center remains same in viewbox 4 as viewbox 1", "Verifying window center remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC2, "Verifying window center remains same in viewbox 2 as viewbox 1", "Verified window center remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC3, "Verifying window center remains same in viewbox 3 as viewbox 1", "Verifying window center remains same in viewbox 3 as viewbox 1");
//
//		//verifying the zoom is default to zoom to fit in 2nd viewbox
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox2", "verified");
//
//	}
//
//
//	//@Test(groups = {"Chrome","firefox","US741"})
//	public void test35_US741_TC2658_verifyViewboxWhenNewSeriesAddedFromContentSelectorHavingGSPS() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content when new series is selected through content selector having GSPS after source");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(TeraRecon_BrainTDA);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(8);
//		contentSelector = new ContentSelector(driver);
//		CircleAnnotation circle = new CircleAnnotation(driver);
//		
//		//Select phase-14 series from Content selector on viewbox 8.
//		contentSelector.selectResultFromContentSelector(8,fourteenthSeriesDescriptionBrainTDA);	
//		viewerPage.waitForAllImagesToLoad();
//		viewerPage.waitForViewerpageToLoad(8);
//		//Input image number 10 in 8th viewbox
//		viewerPage.inputImageNumber(10, 8);
//
//		//Draw an annotation on 10th slide in 8th viewbox
//		circle.selectCircleAnnotationIconFromRadialMenu(8);
//		circle.drawCircle(8, -10, -10, 20, 20);
//
//		int beforeWW8 = Integer.parseInt(viewerPage.getValueOfWindowWidth(8));
//		int	beforeWC8 = Integer.parseInt(viewerPage.getValueOfWindowCenter(8));
//
//		//Go back to study list page and re-visit viewer page
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Select phase-14 series through content selector from 8th viewbox 
//		contentSelector.selectGSPSResultUsingContentSelector(8,fourteenthSeriesDescriptionBrainTDA);
//		viewerPage.waitForAllImagesToLoad();
//		viewerPage.waitForViewerpageToLoad(8);
//
//		//All the view-boxes have the Scroll position as 10
//		for(int i = 1; i<8; i++){
//
//			viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(i),10,"Verifying slice number is Viewbox2 is 10","The Slice number is Viewbox2 is 10");
//		}
//
//		//WWWL values should not change
//		viewerPage.assertEquals(beforeWW8,Integer.parseInt(viewerPage.getValueOfWindowWidth(8)), "Verifying window width remains same in viewbox 8", "Verified window width remains same in viewbox 8");
//		viewerPage.assertEquals(beforeWC8,Integer.parseInt(viewerPage.getValueOfWindowCenter(8)), "Verifying window center remains same in viewbox 8", "Verifying window center remains same in viewbox 8");
//
//		//verifying the zoom is default to zoom to fit
//		viewerPage.getZoomLabelOverlay(8).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox4", "verified");
//	}
//
//	//@Test(groups = { "Chrome","firefox","US741"})
//	public void test36_US741_TC2659_verifyViewboxWhenNewSeriesAddedFromContentSelector() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content when new series is selected through content selector having machine directive");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(TeraRecon_BrainTDA);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(8);
//		contentSelector = new ContentSelector(driver);
//		//Change Layout to 1*2 with 'Save by Default'
//		viewerPage.selectLayoutWithSaveByDefault(viewerPage.oneByTwoLayoutIcon);
//		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer,"Checkpoint1 Verify the layout changed to 2x1 and series loaded","TC2659_" + "_" + "Checkpoint1");
//
//		//Go back to study list page and re-visit viewer page
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		int beforeWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int	beforeWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//
//		//Select 3rd series through content selector from second view-box
////		contentSelector.selectSeriesFromContentSelector(2,thirdSeriesDescriptionBrainTDA);
//		contentSelector.selectResultFromContentSelector(2,thirdSeriesDescriptionBrainTDA);
//		
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),15,"Verifying slice number is Viewbox2 is 15","The Slice number is viewbox 2 is 15");
//		viewerPage.assertEquals(beforeWW1,Integer.parseInt(viewerPage.getValueOfWindowWidth(2)), "Verifying window width remains same in viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(beforeWC1,Integer.parseInt(viewerPage.getValueOfWindowCenter(2)), "Verifying window center remains same in viewbox 2 as viewbox 1", "Verifying window center remains same in viewbox 2 as viewbox 1");
//
//		//verifying the zoom is default to zoom to fit
//		viewerPage.getZoomLabelOverlay(1).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox2", "verified");
//
//	}
//
//
//	
//
//
//	//@Test(groups = { "Chrome","firefox","US741"})
//	public void test39_US741_TC2664_verifyImagePositionWhenNewSeriesSelectedFromContentSelector() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify image position when new series is selected through content selector");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		contentSelector = new ContentSelector(driver);
//		CircleAnnotation circle = new CircleAnnotation(driver);
//		//Flip image present in fourth viewbox
//		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(4));
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage4, "Checkpoint 1: Top Orientation Marker: Image flipped horizontally.", "US741_Checkpoint1_top_flip_" + NorthStar_MR_LSP );
//
//		//Select 5th (AX T1 LSP) from content selector into second viewbox
//		contentSelector.selectSeriesFromContentSelector(2,fifthSeriesDescriptionMrLSP);	
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(2, fifthSeriesDescriptionMrLSP), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//
//		//Scroll position for view-box is 17
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),17,"Verifying slice number is Viewbox 2 is 17","The Slice number is Viewbox 2 is 17");
//
//		//Verify WWWL for 1st and 2nd view-box
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getValueOfWindowWidth(2)),Integer.parseInt(viewerPage.getValueOfWindowWidth(1)), "Verifying window width remains same in viewbox 3", "Verified window width remains same in viewbox 3");
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getValueOfWindowCenter(2)),Integer.parseInt(viewerPage.getValueOfWindowCenter(1)), "Verifying window center remains same in viewbox 3", "Verifying window center remains same in viewbox 3");
//
//		//Verify 'Zoom to Fit'
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//		//Draw GSPS on 20th silce of fourth viewbox
//		viewerPage.inputImageNumber(20, 4);
//		circle.selectCircleAnnotationIconFromRadialMenu(4);
//		circle.drawCircle(4, 0, 0, 10, 20);
//
//		//Go back to study list page and revisit viewer page
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		////Flip image present in fourth viewbox
//		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(4));
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage4, "Checkpoint 2: Top Orientation Marker: Image flipped horizontally.", "US741_Checkpoint2_top_flip_" + NorthStar_MR_LSP );
//
//		//Select 5th (AX T1 LSP) from content selector into second viewbox
//		contentSelector.selectSeriesFromContentSelector(2,fifthSeriesDescriptionMrLSP);	
//		viewerPage.waitForViewerpageToLoad();
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(2, fifthSeriesDescriptionMrLSP), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//
//		//Verify the scroll position is 20 for 2nd viewbox
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),20,"Verifying slice number is Viewbox 2 is 20","The Slice number is Viewbox 2 is 20");
//
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getValueOfWindowWidth(2)),Integer.parseInt(viewerPage.getValueOfWindowWidth(4)), "Verifying window width remains same in viewbox 2 and 4", "Verified window width remains same in viewbox 2 and 4");
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getValueOfWindowCenter(2)),Integer.parseInt(viewerPage.getValueOfWindowCenter(4)), "Verifying window center remains same in viewbox 2 and 4", "Verifying window center remains same in viewbox 2 and 4");
//
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//	}
//
//
//	//@Test(groups = { "Chrome","firefox","US741" })
//	public void test40_US741_TC2630_verifySeriesInFirstVBHavingSamePriority() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify sync when new series is added into first viewbox having SAME priority as existing series.");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		contentSelector = new ContentSelector(driver);
//		//Change window level , zoom, PAN, image position of 1st viewbox
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -10,-10);
//
//		viewerPage.selectZoomFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectWindowLevelFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectPanFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-10);
//
//		//Change window level , zoom, PAN, image position of 4th viewbox
//		viewerPage.selectScrollFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//
//		viewerPage.selectZoomFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-100);
//
//		viewerPage.selectPanFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//
//		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(4), viewerPage.topClockwiseRotationMarker(4));
//
//		//Select 5th series from content selector into FIRST viewbox
//		contentSelector.selectSeriesFromContentSelector(1,fifthSeriesDescriptionMrLSP);
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(1,fifthSeriesDescriptionMrLSP), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Window Leveling takes place in 4th viewbox.");		
//		viewerPage.assertEquals(afterWW1,afterWW4 , "Verifying window width remains same in viewbox 4 as viewbox 1", "Verified window width remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW2, "Verifying window width remains same in viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW3, "Verifying window width remains same in viewbox 3 as viewbox 1", "Verified window width remains same in viewbox 3 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC4, "Verifying window center remains same in viewbox 4 as viewbox 1", "Verifying window center remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC2, "Verifying window center remains same in viewbox 2 as viewbox 1", "Verified window center remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC3, "Verifying window center remains same in viewbox 3 as viewbox 1", "Verifying window center remains same in viewbox 3 as viewbox 1");
//
//
//		viewerPage.assertEquals(viewerPage.getZoomLevel(3), viewerPage.getZoomLevel(2),"Check Zoom Level remain same in View Box 2","The Zoom level remains same");
//
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(3),viewerPage.getCurrentScrollPositionOfViewbox(2),"Verifying slice number in 2nd and 3rd viewbox is same","The Slice number in 2nd and 3rd viewbox is same");
//
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),17,"Verifying slice number is  17 in Viewbox1","The Slice number is Viewbox1 is 17");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4),17,"Verifying slice number is 17 in Viewbox4","The Slice number is Viewbox4 is 17");
//
//		//verifying the zoom is default to zoom to fit
//		viewerPage.getZoomLabelOverlay(1).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//		viewerPage.getZoomLabelOverlay(4).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox4", "verified");
//	}
//
//
//	//@Test(groups = { "Chrome","firefox","US741"})
//	public void test41_US741_TC2631_verifySeriesin2ndVBHavingSamePriority() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify sync when new series is added into second viewbox having SAME priority as existing series.");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		contentSelector = new ContentSelector(driver);
//		//Change window level , zoom, PAN, image position of 1st Viewbox.
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -10,-10);
//
//		viewerPage.selectZoomFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectWindowLevelFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectPanFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-10);
//
//		//Change window level , zoom, PAN, image position of 2nd Viewbox.
//		viewerPage.selectScrollFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//		int initialScrollPosition4 = viewerPage.getCurrentScrollPositionOfViewbox(4);
//
//		viewerPage.selectZoomFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-100);
//
//		viewerPage.selectPanFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//
//		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(4), viewerPage.topClockwiseRotationMarker(4));
//
//		//Select 5th series from content selector into SECOND viewbox
//		contentSelector.selectSeriesFromContentSelector(2,fifthSeriesDescriptionMrLSP);
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(2,fifthSeriesDescriptionMrLSP), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Window Leveling takes place in 4th viewbox.");		
//		viewerPage.assertEquals(afterWW1,afterWW4 , "Verifying window width remains same in viewbox 4 as viewbox 1", "Verified window width remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW2, "Verifying window width remains same in viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW3, "Verifying window width remains same in viewbox 3 as viewbox 1", "Verified window width remains same in viewbox 3 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC4, "Verifying window center remains same in viewbox 4 as viewbox 1", "Verifying window center remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC2, "Verifying window center remains same in viewbox 2 as viewbox 1", "Verified window center remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC3, "Verifying window center remains same in viewbox 3 as viewbox 1", "Verifying window center remains same in viewbox 3 as viewbox 1");
//
//
//		viewerPage.assertEquals(viewerPage.getZoomLevel(1), viewerPage.getZoomLevel(3),"Check Zoom Level remain same in view Box 1 as viewbox 3","The Zoom level remains same");
//
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),viewerPage.getCurrentScrollPositionOfViewbox(3),"Verifying slice number is not same for Viewbox1 and 3","The Slice number is not same for Viewbox 1 and 3");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),17,"Verifying slice number is Viewbox3 is 17","The Slice number is Viewbox5 is 65");
//
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(4), initialScrollPosition4 ,"Verifying slice number does not remain same for Viewbox4","The Slice number does not remain same for Viewbox4");
//
//		//verifying the zoom is default to zoom to fit
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox2", "verified");
//
//	}
//
//
//	//@Test(groups = { "Chrome","firefox","US741" })
//	public void test42_US741_TC2632_verifyNewSeriesAddedTo1stVBWithSamePriorities() throws InterruptedException  {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify sync when new series is added into FIRST viewbox having SAME priority as existing series for GSPS");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+TeraRecon_BrainTDA+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(4);
//		CircleAnnotation circle = new CircleAnnotation(driver);
//		contentSelector = new ContentSelector(driver);
//		//Draw GSPS on 2nd and 5th series
//		viewerPage.inputImageNumber(5, 2);
//		circle.selectCircleAnnotationIconFromRadialMenu(2);
//		circle.drawCircle(2, 0, 0, 10, 20);
//
//		//Go back to study list page and re-visit viewer page.
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Change the Window level, zoom, PAN, image position of 1st viewbox
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -10,-10);
//
//		viewerPage.selectZoomFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectWindowLevelFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-100);
//
//		viewerPage.selectPanFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage1, 0, 0, -100,-10);
//
//		//Change the Window level, zoom, PAN, image position of 2nd viewbox
//		viewerPage.selectScrollFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//
//		viewerPage.selectZoomFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-100);
//
//		viewerPage.selectPanFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewer(viewerPage.viewboxImage4, 0, 0, -10,-10);
//
//		//Rotate image from 4th viewbox
//		viewerPage.rotateSeries(viewerPage.getTopOrientationMarker(4), viewerPage.topClockwiseRotationMarker(4));
//
//		//Select 5th series from content selector into FIRST viewbox
//		contentSelector.selectSeriesFromContentSelector(1,fifthSeriesDescriptionMrLSP);
//		viewerPage.assertTrue(contentSelector.validateSeriesIsSelectedOnContentSelector(1,fifthSeriesDescriptionMrLSP), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//
//		int afterWW1 = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
//		int afterWW2 = Integer.parseInt(viewerPage.getValueOfWindowWidth(2));
//		int afterWW3 = Integer.parseInt(viewerPage.getValueOfWindowWidth(3));
//		int afterWW4 = Integer.parseInt(viewerPage.getValueOfWindowWidth(4));
//
//		int	afterWC1 = Integer.parseInt(viewerPage.getValueOfWindowCenter(1));
//		int afterWC2 = Integer.parseInt(viewerPage.getValueOfWindowCenter(2));
//		int afterWC3 = Integer.parseInt(viewerPage.getValueOfWindowCenter(3));
//		int afterWC4 = Integer.parseInt(viewerPage.getValueOfWindowCenter(4));
//
//		//Verifying WWWL, Zoom Level and Image position same for all viewboxes
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify Window Leveling takes place in 4th viewbox.");		
//		viewerPage.assertEquals(afterWW1,afterWW4 , "Verifying window width remains same in viewbox 4 as viewbox 1", "Verified window width remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW2, "Verifying window width remains same in viewbox 2 as viewbox 1", "Verified window width remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWW1,afterWW3, "Verifying window width remains same in viewbox 3 as viewbox 1", "Verified window width remains same in viewbox 3 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC4, "Verifying window center remains same in viewbox 4 as viewbox 1", "Verifying window center remains same in viewbox 4 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC2, "Verifying window center remains same in viewbox 2 as viewbox 1", "Verified window center remains same in viewbox 2 as viewbox 1");
//		viewerPage.assertEquals(afterWC1,afterWC3, "Verifying window center remains same in viewbox 3 as viewbox 1", "Verifying window center remains same in viewbox 3 as viewbox 1");
//
//		viewerPage.assertNotEquals(viewerPage.getZoomLevel(1), viewerPage.getZoomLevel(3),"Check Zoom Level does not remain same in View Box  1 as viewbox 3","The Zoom level does not remains same");
//		viewerPage.assertEquals(viewerPage.getZoomLevel(2), viewerPage.getZoomLevel(3),"Check Zoom Level remain same in View Box 2 as viewbox 3","The Zoom level remains same");
//
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),viewerPage.getCurrentScrollPositionOfViewbox(3),"Verifying slice number of viewbox 1 is not same as viewbox 3","The Slice number is viewbox 1 is not same as viewbox 3");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2),viewerPage.getCurrentScrollPositionOfViewbox(3),"Verifying slice number is viewbox 2 is same as viewbox 3","The 4lice number is viewbox 2 is same as viewbox 3");
//
//	}
//
//
//	
//	//@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
//	public void test02_US741_TC2671_VerifyGSPSLoadingUsingContentSelector() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify viewbox content when viewer loads with series containing GSPS");
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+mrLSPPatientName);
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//		lineWithUnit = new MeasurementWithUnit(driver);
//		viewerPage.selectLayout(viewerPage.threeByTwoLayoutIcon);
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing lines on viewbox fifth");
//		lineWithUnit.selectDistanceMeasurementFromRadial(5);
//		lineWithUnit.drawLine(5, 50, 50, 100, 50);
//
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the first and fourth viewbox");
//		viewerPage.scrollDownToSliceUsingKeyboard(2,3);
//		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
//
//		viewerPage.inputZoomNumber(200, 2);		
//		viewerPage.inputWWNumber(700, 2);
//		viewerPage.inputWCNumber(100, 2);
//
//		contentSelector.selectSeriesFromContentSelector(1, fifthSeriesDescriptionMrLSP);
//		viewerPage.waitForViewerpageToLoad();
//		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
//		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);
//
//		// Verifying that GSPS is getting loaded
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), viewerPage.getCurrentScrollPositionOfViewbox(4),"Verifying the current image number of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(3), "Verifying the current image number of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(4), "Verifying the current image number of viewbox 2", "verified");
//
//		// verifying the WW/WL
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying the window width of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying the window width of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying the window width of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying the window width of viewbox 4", "verified");
//
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying the window center of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying the window center of viewbox 4", "verified");
//
//		viewerPage.assertEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(4), "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getZoomLevelText(2), viewerPage.getZoomLevelText(3), "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(2), "Verifying the window center of viewbox 3", "verified");
//
//
//	}
//
//	//@Test(groups ={"firefox","Chrome","IE11","Edge","US741"})
//	public void test03_US741_TC2673_VerifyLoadingLowerPriorityItemUsingContentSelector() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[US738] Verify sync when new series is added into FIRSTviewbox having LOWER priority as existing series for GSPS");
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+mrLSPPatientName);
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		lineWithUnit = new MeasurementWithUnit(driver);
//		contentSelector = new ContentSelector(driver);
//		//		viewerPage.selectLayout(ViewerPageConstants.THREE_BY_TWO_LAYOUT);
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing lines on viewbox fifth");
//		lineWithUnit.selectDistanceMeasurementFromRadial(2);
//		lineWithUnit.drawLine(1, -10, -10, 30, 30);
//		lineWithUnit.drawLine(2, -10, -10, 30, 30);
//		lineWithUnit.drawLine(3, -10, -10, 30, 30);
//		lineWithUnit.drawLine(4, -10, -10, 30, 30);
//
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the first and fourth viewbox");
//		viewerPage.scrollDownToSliceUsingKeyboard(2,3);
//		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
//
//		viewerPage.inputZoomNumber(200, 2);
//		viewerPage.inputZoomNumber(200, 4);
//		viewerPage.inputWWNumber(700, 2);
//		viewerPage.inputWCNumber(100, 2);
//
//		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(1));   
//
//		contentSelector.selectSeriesFromContentSelector(1, fifthSeriesDescriptionMrLSP);
//		viewerPage.waitForViewerpageToLoad();
//		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
//		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);
//
//		// Verifying that GSPS is getting loaded
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), viewerPage.getCurrentScrollPositionOfViewbox(4),"Verifying the current image number of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(3), "Verifying the current image number of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(4), "Verifying the current image number of viewbox 2", "verified");
//
//		// verifying the WW/WL
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying the window width of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying the window width of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying the window width of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying the window width of viewbox 4", "verified");
//
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying the window center of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying the window center of viewbox 4", "verified");
//
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(4), "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getZoomLevelText(2), viewerPage.getZoomLevelText(3), "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(2), "Verifying the window center of viewbox 3", "verified");
//
//		viewerPage.getZoomLabelOverlay(1).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//
//	}
//
//	//@Test(groups ={"firefox","Chrome","IE11","Edge","US741"},enabled=false)
//	public void test03_US741_TC2674_VerifyLoadingLowerPriorityItemUsingContentSelector() throws InterruptedException, SQLException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[US738] Verify sync when new series is added into FIRSTviewbox having LOWER priority as existing series for GSPS");
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient"+mrLSPPatientName);
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		lineWithUnit = new MeasurementWithUnit(driver);
//		contentSelector = new ContentSelector(driver);
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Drawing lines on viewbox fifth");
//		lineWithUnit.selectDistanceMeasurementFromRadial(2);
//		lineWithUnit.drawLine(1, 50, 50, 100, 50);
//		lineWithUnit.drawLine(2, 50, 50, 100, 50);
//		lineWithUnit.drawLine(3, 50, 50, 100, 50);
//		lineWithUnit.drawLine(4, 50, 50, 100, 50);
//
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOnStudy(1);
//		viewerPage.waitForViewerpageToLoad();
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the first and fourth viewbox");
//		viewerPage.scrollDownToSliceUsingKeyboard(2,3);
//		viewerPage.scrollDownToSliceUsingKeyboard(4,3);
//
//		viewerPage.inputZoomNumber(200, 2);
//		viewerPage.inputZoomNumber(200, 4);
//		viewerPage.inputWWNumber(700, 2);
//		viewerPage.inputWCNumber(100, 2);
//
//		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(1));   
//
//		contentSelector.selectSeriesFromContentSelector(2, fifthSeriesDescriptionMrLSP);
//
//		String windowWidth = viewerPage.getWindowWidthValueOverlayText(1);
//		String windowCenter = viewerPage.getWindowCenterValueOverlayText(1);
//
//		// Verifying that GSPS is getting loaded
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), viewerPage.getCurrentScrollPositionOfViewbox(3),"Verifying the current image number of viewbox 1", "verified");
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(3), "Verifying the current image number of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(4), "Verifying the current image number of viewbox 2", "verified");
//
//		// verifying the WW/WL
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(1), windowWidth, "Verifying the window width of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(2), windowWidth, "Verifying the window width of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(3), windowWidth, "Verifying the window width of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowWidthValueOverlayText(4), windowWidth, "Verifying the window width of viewbox 4", "verified");
//
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(1), windowCenter, "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(2), windowCenter, "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(3), windowCenter, "Verifying the window center of viewbox 3", "verified");
//		viewerPage.assertEquals(viewerPage.getWindowCenterValueOverlayText(4), windowCenter, "Verifying the window center of viewbox 4", "verified");
//
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(2), viewerPage.getZoomLevelText(4), "Verifying the window center of viewbox 1", "verified");
//		viewerPage.assertEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(3), "Verifying the window center of viewbox 2", "verified");
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(4), "Verifying the window center of viewbox 3", "verified");
//		viewerPage.assertNotEquals(viewerPage.getZoomLevelText(1), viewerPage.getZoomLevelText(2), "Verifying the window center of viewbox 3", "verified");
//
//
//		viewerPage.getZoomLabelOverlay(2).click();
//		viewerPage.assertTrue(viewerPage.zoomToFitOverlay.getCssValue("font-style").equalsIgnoreCase(NSGenericConstants.FONT_ITALIC), "verifying the zoom if by default set to fit in viewbox1", "verified");
//
//
//	}
//
//	//@Test(groups ={"firefox","Chrome","IE11","US741"})
//	public void test05_US741_TC2665_verifySyncForSeriesHavingSamePriority() throws  InterruptedException, SQLException, IOException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[US738] - Verify sync when new series is added into SECOND viewbox having SAME priority as existing series for GSPS");
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(NorthStar_MR_LSP);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+NorthStar_MR_LSP+" in viewer" );
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Changing the layout" );
//		viewerPage.selectLayout(viewerPage.twoByThreeLayoutIcon);
//		viewerPage.waitForAllImagesToLoad();
//
//		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
//		
//		//Scroll to new image
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Chnaging the scroll position" );
//		viewerPage.inputImageNumber(20, 4);
//
//		//select ellipse from radial menu and draw a ellipse 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Drawing ellipse on 4th and 5th VBs" );
//		ellipse.selectEllipseAnnotationFromContextMenu(1);
//		ellipse.drawEllipse(4, 50, -50, 40, -50);
//		ellipse.drawEllipse(5, 50, -50, 40, -50);
//
//		//Reload the viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reloading the viewer" );
//		viewerPage.browserBackWebPage();
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad();
//
//		//Perform rendering operations
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Applying windowing,zoom and pan" );
//		viewerPage.selectWindowLevelFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewerWithClick(4, 10, 10, 50, 50);
//		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(4));
//		viewerPage.dragAndReleaseOnViewerWithClick(4,10, 10, 50, 60);
//		viewerPage.selectPanFromQuickToolbar(4);
//		viewerPage.dragAndReleaseOnViewerWithClick(4,0, 0, 100, -50);
//
//		//Rotate image in 4th VB
//		viewerPage.flipSeries(viewerPage.getTopOrientationMarker(4));	
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage4, "Verify that Image in VB4 is flipped successfully", "TC05_Checkpoint1");
//
//		//Select 5th image in 2nd VB
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Selecting 5th image in second VB" );
//		contentSelector.selectGSPSResultUsingContentSelector(2, fifthSeriesDescriptionMrLSP);
//		viewerPage.waitForViewerpageToLoad();
//		//Verify W and C values for all VBs
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verifying Windowing values for all viewboxes");
//
//		viewerPage.assertEquals(viewerPage.getValueOfWindowWidth(1), viewerPage.getValueOfWindowWidth(2), "Verify that the windowing width value for viewbox1 is same as viewbox2", "Verified");
//		viewerPage.assertEquals(viewerPage.getValueOfWindowCenter(1), viewerPage.getValueOfWindowCenter(2), "Verify that the windowing center value for viewbox1 is same as viewbox2", "Verified");
//		viewerPage.assertEquals(viewerPage.getValueOfWindowWidth(1), viewerPage.getValueOfWindowWidth(3), "Verify that the windowing width value for viewbox1 is same as viewbox3", "Verified");
//		viewerPage.assertEquals(viewerPage.getValueOfWindowCenter(1), viewerPage.getValueOfWindowCenter(3), "Verify that the windowing center value for viewbox1 is same as viewbox3", "Verified");
//		viewerPage.assertEquals(viewerPage.getValueOfWindowWidth(1), viewerPage.getValueOfWindowWidth(4), "Verify that the windowing width value for viewbox1 is same as viewbox4", "Verified");
//		viewerPage.assertEquals(viewerPage.getValueOfWindowCenter(1), viewerPage.getValueOfWindowCenter(4), "Verify that the windowing center value for viewbox1 is same as viewbox4", "Verified");
//
//		//Zoom value
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Zoom should be 'Zoom to Fit' as newly added series renders with zoom value as 'Zoom to Fit'");
//		viewerPage.assertEquals(viewerPage.getZoomLevel(2), viewerPage.getZoomLevel(4), "Verify zoom is set to its default zoom value after loading the series in a viewer", "Verified");
//
//		//Image positions
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verifying scroll positions for viewboxes");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1), viewerPage.getCurrentScrollPositionOfViewbox(3), "Image position of 1st  and 3rd  viewbox should be same ", "Verified");
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(2), viewerPage.getCurrentScrollPositionOfViewbox(4), "Image position of 2nd and 4th  viewbox should be = slice number on which GSPS is drawn on second viewbox (5th series)", "Verified");
//
//		//Verify the rotation
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying that the images are not rotated");
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage2, "Verify that Image in VB2 is not rotate and Returned to default", "TC05_Checkpoint2");
//		viewerPage.compareElementImage(protocolName, viewerPage.viewboxImage4, "Verify that Image in VB2 is not rotate and Returned to default", "TC5_Checkpoint3");
//
//	}
//
//	
//	
//
//}