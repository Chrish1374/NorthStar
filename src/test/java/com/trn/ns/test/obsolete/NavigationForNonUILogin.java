//package com.trn.ns.test.basic;
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.sql.SQLException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.OrthancAndAPIConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.ErrorOrLogoutPage;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.RegisterUserPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.basic.StudyListPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class NavigationForNonUILogin extends TestBase{
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//	private PatientListPage patientPage;
//
//	private ViewerPage viewerPage;
//	private RegisterUserPage registerUserPage;
//	private DatabaseMethods db;
//	private MeasurementWithUnit lineWithUnit;
//	private ContentSelector cs;
//	private PointAnnotation point;
//	
//
//	private String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//	String patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath);
//	String studyDescription=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
//	
//	String filePath1 = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//	String bonagePatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);
//	String bonagePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
//	String studyDesc=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath1);
//	
//	String filePath2 =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
//	String IBL_JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
//	
//	String filePath3 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
//	String ChestCT1p25mm = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);
//
//	//AH4 patient Url to be accessed
//	private String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103";
//	
//	String filePath5 =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
//	
//	String filePath6 = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
//	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath6);
//	String SeriesToSelect_ADC1= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath6);
//	String SeriesToSelect_ADC2= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath6);
//
//	private String filePath7 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	private String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath7);
//	
//	String TCGA_VP_A878_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
//	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_VP_A878_filepath);
//	
//	String accNo="ACN01";
//	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
//
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","US677"})
//	public void test01_b_US677_TC2699_verifyRefreshForNonUILogin() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify refresh functionality for non UI login");
//
//		loginPage = new LoginPage(driver);
//
//		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
//		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
//		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
//		
//		//Accessing study list page url from non ui login
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/10]", "Verifying the launching of study list page from non-ui login");
//		String myURL = loginPage.getNonUILaunchURL(URLConstants.STUDY_LIST_URL, hm);
//		loginPage.navigateToURL(myURL);
//		studyPage = new StudyListPage(driver);
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Verify that study list page should display", studyPage.getCurrentPageURL()+" is displaying");
//
//		//Perform refresh
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verifying the refresh on study list page");
//		studyPage.refreshWebPage();
//		studyPage.waitForStudyListToLoad();
//		studyPage.assertTrue(studyPage.getCurrentPageURL().contains(URLConstants.STUDY_LIST_URL) , "Verify that study list page should display", studyPage.getCurrentPageURL()+" is displaying");
//
//		loginPage.logout();
//		
//		
//	}}