//package com.trn.ns.test.obsolete;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DICOMRT;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.RegisterUserPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//
//public class SaveByDefaultTest extends TestBase{
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ExtentTest extentTest;
//	private ContentSelector cs;
//	private HelperClass helper;
//	
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String boneAgePatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);	
//	
//	String filePath2 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
//	
//	String filePath3 = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
//	String AiDocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
//	
//	String filePath4 = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String MultiSeriesPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
//	String firstSeries= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath4);
//	
//	String filepath5= Configurations.TEST_PROPERTIES.get("Picline_filepath");
//	String piccliineData= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath5);
//	
//	String filepath6= Configurations.TEST_PROPERTIES.get("Head_CT_filepath");
//	String infervisonData= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath6);
//
//	String firstSeriesDescritpionsInfervision= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filepath6);
//	String secondSeriesDescritpionsInfervision= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filepath6);
//	
//	String filepath7 = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
//	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath7);
//	
//	String filepath8 = Configurations.TEST_PROPERTIES.get("VIDA_LCS_COPD_Filepath");
//	String VIDA_LCS_COPD_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath8);
//	
//	String filepath9 = Configurations.TEST_PROPERTIES.get("VIDA_Emphysema_ILD_Filepath");
//	String VIDA_Emphysema_ILD_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath9);
//	
//	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
//	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
//	
//	String pmapFilePath = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
//	String pmapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmapFilePath);
//	
//	String cadFilePath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
//	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cadFilePath);
//	
//
//	private ContentSelector contentSelector;
//
//	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
//	
//	String username_1 = "user_1";
//	private DICOMRT drt;
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username,password );	
//	}
//
////	@Test(groups ={"Chrome","Edge","IE11","US764"})//obsolete
//	public void test01_US764_TC2981_BoneAge_VerfiySaveByDefaultCheckBox() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify 'Save By Default CheckBox on BoneAgeDATA");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+boneAgePatient+" in viewer" );
//		//Step2 verifying the layout and saveBy default on bonaAge
//
//		patientPage = new PatientListPage(driver);
//
//		patientPage.clickOnPatientRow(boneAgePatient);
//		patientPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(4);
//		
//		//Step2.1
//		viewerpage.assertEquals(viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), 4, "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "The 'Save as Default' checkbox is enabled as the study is not associated with a machine.json/algorithm" );
//
//		//step2.2 Select save by default and //step2.3 Change layout to  2*1 layout
//		viewerpage.selectLayoutWithSaveByDefault(viewerpage.twoByOneLayoutIcon);
////		viewerpage.assertTrue(viewerpage.validateSaveByDefault(), "Verifying Save By Default is Enabled and checked", "Save By Default is checked now");
//
//		//step 2.4 Reload the viewerPage
//		helper=new HelperClass(driver);
//		helper.browserBackAndReloadViewer(boneAgePatient, 1, 1);
//		
//		int c=viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT);
//		viewerpage.assertEquals(viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), c, "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");
//
//		//Step3 verifying the layout and saveBy default on Aidoc patient
//
//		//Step3.1 Loading The Aidoc patient and verifying the default Layout
//		helper.browserBackAndReloadViewer(AiDocPatientName, 1, 1);
//		
//		viewerpage.selectLayoutWithSaveByDefault(viewerpage.oneByOneLayoutIcon);
////		viewerpage.assertTrue(viewerpage.validateSaveByDefault(), "Verifying Save By Default is Enabled and checked", "Save By Default is checked now");
//		//step 3.4 Reload the viewerPage
//		helper.browserBackAndReloadViewer(AiDocPatientName, 1, 1);
//		
//		c=viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying the layout type of the viewer", "Viewer is loaded in 2*2");
//	}
//
//}
//
//
//
//
//
//
