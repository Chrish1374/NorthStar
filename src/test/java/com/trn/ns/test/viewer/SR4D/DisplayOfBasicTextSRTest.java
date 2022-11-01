package com.trn.ns.test.viewer.SR4D;


import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SRTextDisplay;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DisplayOfBasicTextSRTest  extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private SRTextDisplay srText;
	private HelperClass helper;
	private ViewerLayout layout;
	private PointAnnotation pointAnn ;

	private ViewerARToolbox arToolbar;
	private ViewerSliderAndFindingMenu findingMenu;

	String filePath = Configurations.TEST_PROPERTIES.get("TEST^SR_Tanaka_Hanako_filepath");
	String hanako = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String SR_filepath = Configurations.TEST_PROPERTIES.get("S10671CTSR_filepath");
	String S10671 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SR_filepath);

	String SRDICOM=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
	String SRSeries=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);	

	String filePath2 = Configurations.TEST_PROPERTIES.get("TEST^SR_Wilkins_Charles_filepath");
	String wilkins = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	private EllipseAnnotation ellipse;
	private ContentSelector contentSelector;

	String filePath3 = Configurations.TEST_PROPERTIES.get("AnonymizedByInferVISION2017-10-17_filepath");
	String infervision = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
	String infervision_Series=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath3);

	String filePath4 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	String filePath5 =Configurations.TEST_PROPERTIES.get("QureCXR1_Filepath");
	String qurecxrPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);

	private static int  seriesLevelID;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		
		DatabaseMethods db = new DatabaseMethods(driver);
		seriesLevelID = db.getLastRowNum(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID);

	}

	@Test (groups = {"Chrome", "IE11", "Edge","US789","Positive","Sanity","BVT"})
	public void test01_US842_TC3642_VerifySRSeriesRenderedOnViewer() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify that study open in 1/2 layout."+"<br>Verify One of the viewbox contain DICOM file and another viewbox contain SR text.");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);
		srText = new SRTextDisplay(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Validating that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text");
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy study open in 1*2 layout", "Study open in 1*2 layout when both DICOM and SR text report available for the patient ");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Validate only on DICOM file is present and another one is SR Text report", "Both DICOM file and  SR Text report available for the patient");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Validating Content inside viewbox containing SR text report");
		viewerpage.assertTrue((srText.getText(srText.getName(1)).trim()).contains(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath)), "Verift that Patient name should display", "Patient name display on SR Text Report");
		viewerpage.assertEquals(srText.getDOB(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.DOB, filePath),"Verift that Patient DOB should display", "Patient DOB display on SR Text Report");
		viewerpage.assertEquals(srText.getID(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.PATIENTID, filePath),"Verift that Patient ID should display", "Patient ID display on SR Text Report");
		viewerpage.assertEquals(srText.getSEX(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.SEX, filePath),"Verift that Patient sex should display", "Patient sex display on SR Text Report");
		viewerpage.assertEquals(srText.getSeriesDescriptionOverlayText(1).trim(),DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath),"Verift that Patient result should display", "Patient result display on SR Text Report");

		viewerpage.assertEquals(srText.getAccessionNumber(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.ACCESSION_NUMBER_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath), "Verift that Patient Accession Number should display", "Patient Accession Number display on SR Text Report");
		viewerpage.assertEquals(srText.getCompletionFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.COMPLETION_FLAG, filePath), "Verift that Completion Flag should display", "Completion Flag display on SR Text Report");
		viewerpage.assertEquals(srText.getVerificationFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.VERIFICATION_FLAG, filePath), "Verift that Verification Flag should display", "Verification Flag display on SR Text Report");
		viewerpage.assertEquals(srText.getRequest(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.REQUEST,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath), "Verift that Request should display", "Request display on SR Text Report");
		viewerpage.assertEquals(srText.getHistory(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.HISTORY,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath),"Verift that History should display", "History display on SR Text Report");
		viewerpage.assertEquals(srText.getProcedure(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.PROCEDURE,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath),"Verift that Procedure should display", "Procedure display on SR Text Report");
		viewerpage.assertEquals(srText.getFindings1(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.FINDINGS1,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath),"Verift that Findings should display", "Findings display on SR Text Report");
		viewerpage.assertEquals(srText.getFindings2(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.FINDINGS2,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath),"Verift that Findings should display", "Findings display on SR Text Report");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Validating Content inside viewbox containing SR text report");
		//	viewerpage.assertTrue(contentSelector.validateIconPresenceInSeriesTab(SRSeries,ViewerPageConstants.SR_ICON,ViewerPageConstants.SR_TOOLTIP), "Image Icon not present for the SR text", "Image Icon not present for the SR text");	
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US842","US2523","Positive","F1090"})
	public void test02_US842_TC3643_US2523_TC10412_VerifySRSeriesRenderedOnViewerAfterLayoutChangeAndScrollbarSeenWhenTextIsMore() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify SR series are getting rendered into viewer after layout change and scrollbar is present whenever text is more than the layout size. <br>"+
				"[Risk and Impact]: Verify that copy option is visible instead of quick toolbox for SR patient report");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);

		srText = new SRTextDisplay(driver);
		layout=new ViewerLayout(driver);

		//TC10412
		viewerpage.assertEquals(srText.getText(srText.getCopyTextForSRReport(1)),NSGenericConstants.COPY_TEXT,"Checkpoint[1/4]", "Verified that copy text is visible on SR report after perform right click");
		viewerpage.assertFalse(viewerpage.verifyQuickToolboxPresence(), "Checkpoint[2/4]", "Verified that quick toolbar is not display after perform right click on SR report.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Validate SR DICOM series and SR text report rendered  as it was before changing the layout");
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy on double clicking on any viewbox(containing SR DICOM series), it  open in 1LX2R layout.", "Double clicking on any viewbox,it open in 1*1 layout.");
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 9, "Verfiy on double clicking on any viewbox(containing SR DICOM series), it  open in 3X3 layout.", "Double clicking on any viewbox,it open in 1*1 layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Validate Scrollbar present on SR text report for smaller layout and User able to scroll through all the content inside viewbox containing SR Text report");
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.verifyTrue(viewerpage.verifyPresenceOfVerticalScrollbar(srText.getSRTextReport(2)),"Validate Scrollbar present on SR text report viewbox or not","On smaller layouts, scrollbar is displayed for SR text report");
		viewerpage.scrollIntoView(srText.getFindings1(1));
		layout.selectLayout(layout.twoByOneLayoutIcon);


	}

	@Test (groups = {"Chrome", "IE11", "Edge","US842","Positive"})
	public void test03_US842_TC3644_VerifySRSeriesRenderedOnViewerAfterDoubleClick() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify SR series are getting rendered into viewer successfully on double click");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);

		srText = new SRTextDisplay(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Validating layout as 1*1 ,on Double clicking on SR DICOM series");
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 1, "Verfiy on double clicking on any viewbox(containing SR DICOM series), it  open in 1X1 layout.", "Double clicking on any viewbox,it open in 1*1 layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Validating layout as 1*2 ,again Double clicking on SR DICOM series");
		viewerpage.doubleClickOnViewbox(1);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy on double clicking on any viewbox(containing SR DICOM series), it  open in 1X2 layout.", "Double clicking on any viewbox,it open in 1*2 layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Validating layout as 1*1 ,on Double clicking on SR text report");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 1, "Verfiy on double clicking on any viewbox(containing SR text report), it  open in 1X1 layout.", "Double clicking on any viewbox,it open in 1*1 layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Validating layout as 1*2 ,again Double clicking on SR text report");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy on double clicking on any viewbox(containing SR text report), it  open in 1X2 layout.", "Double clicking on any viewbox,it open in 1*2 layout.");
		viewerpage.doubleClickOnViewbox(2);
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US842","Positive"})
	public void test04_US842_TC3646_VerifySRSeriesRenderedOnViewerWhenSelectedThroughContentSelector() throws InterruptedException, AWTException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify SR series are getting rendered into viewer successfully on double click");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);
		arToolbar=new ViewerARToolbox(driver);

		srText = new SRTextDisplay(driver);
		contentSelector = new ContentSelector(driver);
		ellipse = new EllipseAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Verify SR text report replaced by SR DICOM series after selecting it from content selector");
		contentSelector.selectSeriesFromSeriesTab(2, SRDICOM);
		ellipse.closingConflictMsg();
		viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(SRDICOM), "Verifying result displayed on SR Text Report replaced by SR DICOM", "Verified result displayed on 2nd viewbox is SR DICOM file");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify SR DICOM  replaced by SR Text Report series after selecting it from content selector");
		contentSelector.selectSRReportFromSeriesTab(1, SRSeries);
		ellipse.closingConflictMsg();
		//viewerpage.assertTrue(contentSelector.verifyPresenceOfEyeIcon(SRSeries), "Verifying result displayed on SR DICOM replaced by SR Text Report", "Verified result displayed on 1st viewbox is SR Text report file");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify All DICOM operations performed successfully.");
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		ellipse.closingConflictMsg();
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(2));
		arToolbar.deleteAllAnnotation(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verify Radial menu not open on SR Text Report");
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.closingConflictMsg();
		viewerpage.assertTrue(ellipse.getEllipses(1).size()==0, "Verify Radial menu not open on SR Text Report", "Verify Radial menu not open on SR Text Report");	
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US842","Positive"})
	public void test05_US842_TC3647_VerifySRSeriesRenderedOnSuccesssiveload() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify SR series are getting rendered into viewer successfully on on successive loads");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);

		srText = new SRTextDisplay(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Validating that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text for hanako patient");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Validate only one DICOM file is present and another one is SR Text report", "Both DICOM file and  SR Text report available for the patient");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Validating that study open in 2*2 layout and all viewbox contain DICOM file for Liver9 patient ");
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(liver9, 1, 1);

		viewerpage.waitForViewerpageToLoad(2);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 4, "Validate only one DICOM files are present", "Validate all files are DICOM files");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Validating that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text for wilkins patient");
		helper.browserBackAndReloadViewer(wilkins, 1, 2);
		viewerpage.waitForViewerpageToLoad(2);
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Validate only one DICOM file is present and another one is SR Text report", "Both DICOM file and  SR Text report available for the patient");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Validate only one DICOM files are present", "Validate only one DICOM file is present");
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US842","Positive"})
	public void test06_US842_TC3648_VerifySRSeriesRenderedOnViewerWhenSelectedThroughNonUILogin() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify SR series are getting rendered into viewer successfully through NonUI Login");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(hanako, 2);

		//note down studyInstanceID 
		String actualUrl = viewerpage.getCurrentPageURL();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Verify Viewer page render successfully with DICOM series and SR text reports through Non UI Login.");

		String[] Array=actualUrl.split("/");
		String studyInstaceUID =Array[5];

		loginPage.logout();

		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/"+studyInstaceUID;
		String myURL =viewerpage.getNonUILaunchURL(viewerAH4Url, hm);

		//String DirectURL ="http://"+ Configurations.TEST_PROPERTIES.get("nsHostName")+"/#/"+ URLConstants.VIEWER_PAGE_URL +"/"+ studyInstaceUID+ "?"+"password="+Configurations.TEST_PROPERTIES.get("nsPassword")+"&username="+Configurations.TEST_PROPERTIES.get("nsUserName");
		loginPage.openNewWindow(myURL);
		viewerpage.assertTrue(viewerpage.getCurrentPageURL().contains(studyInstaceUID),"Verify Viewer page render successfully with DICOM series and SR text reports.","Viewer page render successfully with DICOM series and SR text reports through non-UI login."); 
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US789","Positive"})
	public void test07_DE938_TC3703_VerifySRSeriesRenderedOnViewerForInferVision() throws InterruptedException   
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify InferVision SR Report");
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(infervision, 2);

		srText = new SRTextDisplay(driver);
		viewerpage.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/1]","Validating Content inside viewbox containing SR text report");		
		viewerpage.assertTrue((srText.getName(1).getText().trim()).contains(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3)), "Verify that Patient name should display", "Patient name display on SR Text Report");
		viewerpage.assertEquals(srText.getDOB(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.DOB, filePath3),"Verify that Patient DOB should display", "Patient DOB display on SR Text Report");
		viewerpage.assertEquals(srText.getID(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.PATIENTID, filePath3),"Verify that Patient ID should display", "Patient ID display on SR Text Report");
		viewerpage.assertEquals(srText.getSEX(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.SEX, filePath3),"Verify that Patient sex should display", "Patient sex display on SR Text Report");
		viewerpage.assertEquals(srText.getSeriesDescriptionOverlayText(1),DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath3),"Verift that Patient result should display", "Patient result display on SR Text Report");
		viewerpage.assertEquals(srText.getAccessionNumber(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.ACCESSION_NUMBER_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath3), "Verify that Patient Accession Number should display", "Patient Accession Number display on SR Text Report");
		viewerpage.assertEquals(srText.getCompletionFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.COMPLETION_FLAG, filePath3), "Verify that Completion Flag should display", "Completion Flag display on SR Text Report");
		viewerpage.assertEquals(srText.getVerificationFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.VERIFICATION_FLAG, filePath3), "Verify that Verification Flag should display", "Verification Flag display on SR Text Report");
		viewerpage.assertEquals(srText.getFinding1Infervision(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.FINDINGS1,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath3),"Verify that Findings should display", "Findings display on SR Text Report");
		viewerpage.scrollIntoView(srText.getFinding1Infervision(1));	

		viewerpage.assertEquals(srText.getFinding2Infervision(1).getText(),DataReader.getStudyDetails(PatientXMLConstants.FINDINGS2,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath3),"Verify that Findings should display", "Findings display on SR Text Report");			
	}

	// US925 Basic Text SR user interaction
	@Test (groups = {"Chrome", "IE11", "Edge","US925","Positive"})
	public void test08_US925_TC3849_VerifyNavigationOfJumpToImage() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify after click on jump to image link ,desired image get rendered in viewbox");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(infervision, 2);

		srText = new SRTextDisplay(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Validating that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text");
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy study open in 1*2 layout", "Study open in 1*2 layout when both DICOM and SR text report available for the patient ");

		//verify desired image rendered on viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify desired image get rendered in viewbox after click on jump to image link");
		viewerpage.click(srText.JumpToImageLink.get(0));
		viewerpage.waitForAllImagesToLoad();
		String SliceNumber1=viewerpage.getCurrentScrollPosition(2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(0).getText().contains(SliceNumber1), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumber1);

		viewerpage.scrollIntoView(srText.getFinding1Infervision(1));	
		viewerpage.click(srText.JumpToImageLink.get(1));
		viewerpage.waitForAllImagesToLoad();
		String SliceNumber2=viewerpage.getCurrentScrollPosition(2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(1).getText().contains(SliceNumber2), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumber2);

		viewerpage.click(srText.JumpToImageLink.get(2));
		viewerpage.waitForAllImagesToLoad();
		String SliceNumber3=viewerpage.getCurrentScrollPosition(2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(2).getText().contains(SliceNumber3), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumber3);

	}

	//  DE1058 SR Text : Wrong slice displayed when user clicks on Jump to image.
	@Test (groups = {"Chrome", "IE11", "Edge","US925","Sanity","Positive"})
	public void test09_US925_TC3854__DE1058_VerifySynchronizationForJumpToImage() throws InterruptedException, AWTException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify synchronization for SR Jump to Image link functionality");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(infervision, 2);

		srText = new SRTextDisplay(driver);
		contentSelector= new ContentSelector(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/2]","Verify that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text");
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy study open in 1*2 layout", "Study open in 1*2 layout when both DICOM and SR text report available for the patient ");

		//verify desired image rendered on viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/2]","Verify Synchronization for SR jump to image link for empty viewbox");
		viewerpage.click(srText.JumpToImageLink.get(0));
		viewerpage.waitForAllImagesToLoad();
		String SliceNumber1=viewerpage.getCurrentScrollPosition(2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(0).getText().contains(SliceNumber1), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumber1);

		//change layout to 2*2
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.waitForAllImagesToLoad();
		viewerpage.closingConflictMsg();

		//select same series in empty viewbox
		contentSelector.selectSeriesFromSeriesTab(4, infervision_Series);
		viewerpage.closingConflictMsg();
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.scrollIntoView(srText.JumpToImageLink.get(1));
		viewerpage.click(srText.JumpToImageLink.get(2));
		viewerpage.waitForAllImagesToLoad();

		String SliceNumberForViewbox2=viewerpage.getCurrentScrollPosition(2);
		String SliceNumberForViewbox3=viewerpage.getCurrentScrollPosition(3);
		String SliceNumberForViewbox4=viewerpage.getCurrentScrollPosition(4);

		viewerpage.assertTrue(srText.JumpToImageNumber.get(2).getText().contains(SliceNumberForViewbox2), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumberForViewbox2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(2).getText().contains(SliceNumberForViewbox3), "Verify slice image rendered on viewbox-3", "Image rendered on viewbox is:"+" "+SliceNumberForViewbox3);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(2).getText().contains(SliceNumberForViewbox4), "Verify slice image rendered on empty viewbox-4", "Image rendered on viewbox is:"+" "+SliceNumberForViewbox4);
	}

	@Test (groups = {"Chrome", "IE11", "Edge","US925","Positive"})
	public void test10_US925_TC3855_VerifyFunctionalityWhenSRTextRendredInOneByOneLayout() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Verify if SR Text is rendered in 1x1 layout then on clicking Jump to Image,nothing should happen");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(infervision, 2);

		srText = new SRTextDisplay(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/3]","Verify that study open in 1*2 layout and one of the viewbox contain DICOM file while another is SR report text");
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 2, "Verfiy study open in 1*2 layout", "Study open in 1*2 layout when both DICOM and SR text report available for the patient ");

		//verify desired image rendered on viewbox
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/3]","Verify desired image get rendered in viewbox after click on jump to image link");
		viewerpage.click(srText.JumpToImageLink.get(0));
		viewerpage.waitForTimePeriod(2000);
		String SliceNumber=viewerpage.getCurrentScrollPosition(2);
		viewerpage.assertTrue(srText.JumpToImageNumber.get(0).getText().contains(SliceNumber), "Verify slice image rendered on viewbox-2", "Image rendered on viewbox is:"+" "+SliceNumber);

		//open SR report in 1*1 layout
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[3/3]","Verify SR Text report open in 1x1 layout");
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 1, "Verfiy study open in 1*1 layout", "SR Text Report open in 1x1 layout when double click on viewbox-1");

		//click on jump to link 
		viewerpage.click(srText.JumpToImageLink.get(1));
		viewerpage.waitForAllImagesToLoad();
		viewerpage.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(), 1, "Verfiy layout after click on jump to link", "SR Text is rendered in 1x1 layout then on clicking Jump to Image nothing happen");


	}

	//US804:Scrollbar component

	@Test(groups = { "Chrome", "IE11", "Edge", "US804", "Positive" })
	public void test11_US804_TC4452_VerifyScrollbarComponentInSRData()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new UI of scrollbar component on SR data");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(infervision, 2);

		srText = new SRTextDisplay(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify scroll bar component for SR report in first viewbox");
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.verticalScrollBarComponent.get(0)), "Verify new UI of Scrollbar component present on SR report", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify UI of regular scrollbar component for SR report");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify UI of scrollbar component when mouse pointer on the scrollbar for SR report");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
		viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify scroll functionality in SR report ");
		viewerpage.assertFalse(srText.isElementPresent(srText.LastItemOnSRReport(1)),"Verify last item of SR report not visible before scroll", "Verified");
		viewerpage.scrollIntoView(srText.LastItemOnSRReport(1));
		viewerpage.assertTrue(srText.isElementPresent(srText.LastItemOnSRReport(1)),"Verify last item of SR report visible after scroll", "Verified");
	}

	//DE1301: Northstar not displaying the SR result appropriately-infervision (Reported by Viswaath)
	@Test (groups = {"Chrome", "IE11", "Edge","DE1301","Positive","DE1793","DE1928"})
	public void test12_DE1301_TC5281_DE1793_TC7291_DE1928_TC7805_VerifySRForCTChestPatient() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify SR result is displayed appropriately for infervision data"
				+ "<br> Verify comment box gets closed when user clicks in comment box specially for SR data. <br>"+
				"Verify AR tool bar is displayed on the initial load for the series with findings like machine GSPS.");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mm, 2);

		patientPage.clearConsoleLogs();

		srText = new SRTextDisplay(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);

		//verify AR tool for machine drawn annotation
		findingMenu.selectFindingFromTable(2, 1);
		viewerpage.assertTrue(findingMenu.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/6]", "Verified AR toolbar when machine finding is selected.");

		viewerpage.click(viewerpage.getViewPort(2));
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.mouseHover(findingMenu.getGSPSHoverContainer(2));
		viewerpage.assertTrue(findingMenu.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/6]", "Verified AR toolbar when mouse hover on AR toolbar but finding is not selected.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Validating Content inside viewbox containing SR text report for"+" "+ChestCT1p25mm);		
		viewerpage.assertTrue((srText.getName(1).getText().trim()).contains(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4)), "Verify that Patient name should display", "Patient name display on SR Text Report");
		viewerpage.assertTrue((srText.getID(1).getText().trim()).contains(DataReader.getPatientDetails(PatientPageConstants.PATIENTID, filePath4)),"Verift that Patient ID should display", "Patient ID display on SR Text Report");
		viewerpage.assertEquals(srText.getDOB(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.DOB, filePath4),"Verify that Patient DOB should display", "Patient DOB display on SR Text Report");
		viewerpage.assertEquals(srText.getSEX(1).getText().trim(),DataReader.getPatientDetails(PatientPageConstants.SEX, filePath4),"Verify that Patient sex should display", "Patient sex display on SR Text Report");
		viewerpage.assertEquals(srText.getSeriesDescriptionOverlayText(1),DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath4),"Verify that Patient result should display", "Patient result display on SR Text Report");
		viewerpage.assertEquals(srText.getAccessionNumber(1).getText().trim(),DataReader.getStudyDetails(PatientXMLConstants.ACCESSION_NUMBER_TEXTOVERLAY,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath4), "Verify that Patient Accession Number should display", "Patient Accession Number display on SR Text Report");
		viewerpage.assertEquals(srText.getCompletionFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.COMPLETION_FLAG, filePath4), "Verify that Completion Flag should display", "Completion Flag display on SR Text Report");
		viewerpage.assertEquals(srText.getVerificationFlag(1).getText().trim(),DataReader.getPatientDetails(PatientXMLConstants.VERIFICATION_FLAG, filePath4), "Verify that Verification Flag should display", "Verification Flag display on SR Text Report");
		viewerpage.assertFalse(srText.listOfNoduleOnSRData.isEmpty(),"Verify SR data loaded successfully on viewer page.","SR finding content seen properly on viewer page");

		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Checkpoint[4/6]", "'3ChestCT1p25mm' data should get loaded successfully on viewer. Both series - DICOM and SR series should be rendered in viewer  without any error in console.");

		String circleComment = "CircleComment";
		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.addResultComment(circle.getAllCircles(2).get(0), circleComment);

		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(2, 1), "Checkpoint[5/6]", "Verify circle state is changed");

		circle.assertEquals(circle.getText(circle.resultComment.get(0)), circleComment, "Checkpoint[6/6]", "Verify circle comments added - meaning after clicking on comment box, comment box is not getting closed");



	}

	@Test (groups = {"Chrome", "IE11", "Edge","DE1869","Positive"})
	public void test13_DE1869_TC7438_verifyLineDelimeterInSrReport() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that NorthStar is able to handle new line delimiter for SR data(QureCXR1)");

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(qurecxrPatientName, 1);

		srText = new SRTextDisplay(driver);
		srText.closeNotification();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]","Validating Content inside viewbox containing SR text report for"+" "+qurecxrPatientName);		
		String content = srText.getText(srText.getRequest(1));
		String contentFromXML = DataReader.getStudyDetails(PatientXMLConstants.FINDINGS1,PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath5);

		String[] contentValue = content.split("\n");
		String[] contentValurFromXML=contentFromXML.split("\n");

		srText.assertEquals(contentValue.length,contentValurFromXML.length,"Checkpoint[1/3]","Verifying that content is displayed multi line");

		for(int i =1 ;i<contentValue.length;i++) {
			srText.assertEquals(contentValue[i].split("\\s+\\|").length,contentValurFromXML[i].split("\\s+\\|").length,"Checkpoint[2/3]","verifying that content is in tabular form");
			srText.assertEquals(contentValue[i].trim(),contentValurFromXML[i].trim(),"Checkpoint[3/3]","verifying that content is in tabular form");

		}

	}

	@Test (groups = {"Chrome","IE11","Edge","DE2686","Postive"})
	public void test14_DE2686_TC10735_verifyDicomImagesAfterLayoutChange() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that DICOM Images are loaded in view box after changing the layout for SR data");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(S10671, 2);

		layout=new ViewerLayout(driver);
		pointAnn = new PointAnnotation(driver);
		srText = new SRTextDisplay(driver);
		pointAnn.selectPointFromQuickToolbar(2);

		pointAnn.drawPointAnnotationMarkerOnViewbox(2, 30, 30);
		helper.browserBackAndReloadViewer(S10671, 1, 1);

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		layout.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT) , "Checkpoint[1/7]", "Verified that study is loaded in 2*2 layout");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(1), "Checkpoint[2/7]"," Verifying series loaded on viewbox-1");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(3), "Checkpoint[3/7]"," Verifying series loaded on viewbox-3");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.assertEquals(srText.getNumberOfLayoutDisplayOnViewer(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT) , "Checkpoint[1/3]", "Verified that study is loaded in 3*3 layout");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(1), "Checkpoint[4/7]"," Verifying series loaded on viewbox-1");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(3), "Checkpoint[5/7]"," Verifying series loaded on viewbox-3");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(5), "Checkpoint[6/7]"," Verifying series loaded on viewbox-5");
		layout.assertTrue(layout.verifyDicomImageLoadedInViewer(7), "Checkpoint[7/7]"," Verifying series loaded on viewbox-7");

	}


	@AfterMethod
	public void deleteSRClone() throws SQLException {

		db.deleteDBEntry(NSDBDatabaseConstants.SERIES_LEVEL, NSDBDatabaseConstants.SERIES_LEVEL_ID, seriesLevelID);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,Configurations.TEST_PROPERTIES.get("nsUserName"));
	}

}

