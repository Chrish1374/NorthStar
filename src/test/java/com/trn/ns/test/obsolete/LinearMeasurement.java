package com.trn.ns.test.obsolete;
//package com.terarecon.northstar.test.obsolete;
//
//import java.awt.AWTException;
//import static com.terarecon.northstar.test.configs.Configurations.TEST_PROPERTIES;
//import org.openqa.selenium.TimeoutException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.terarecon.northstar.dataProviders.DataProviderArguments;
//import com.terarecon.northstar.dataProviders.ExcelDataProvider;
//import com.terarecon.northstar.page.factory.Header;
//import com.terarecon.northstar.page.factory.LoginPage;
//import com.terarecon.northstar.page.factory.NSConstants;
//import com.terarecon.northstar.page.factory.PatientListPage;
//import com.terarecon.northstar.page.factory.SinglePatientStudyPage;
//import com.terarecon.northstar.page.factory.ViewerPage;
//import com.terarecon.northstar.test.base.TestBase;
//import com.terarecon.northstar.test.configs.Configurations;
//import com.terarecon.northstar.utilities.DataReader;
//import com.terarecon.northstar.utilities.ExtentManager;
//@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
//public class LinearMeasurement extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	private ExtentTest extentTest;
//	
//
//	String filePath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
//
//	String filePath1=Configurations.TEST_PROPERTIES.get("JobsSteve_filepath");
//	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath1);
//
//	String filePath2=Configurations.TEST_PROPERTIES.get("Phantom_Of_Grid_filepath");
//	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath2);
//
//	String filePath3=Configurations.TEST_PROPERTIES.get("TEST^Non_Square_Pixels_filepath");
//	String PatientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath3);
//
//	String filePath4=Configurations.TEST_PROPERTIES.get("Test^Pixel_Spacing_filepath");
//	String PatientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath4);
//	String series1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY,filePath4);
//	String series2=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY,filePath4);
//
//	String filePath5=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String PatientName5 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath5);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"firefox","Chrome","IE11","Edge","multimonitor","US601","Sanity"})
//	public void test11_US601_TC1971_verifyLinearMeasurementOnMultiMointor() throws InterruptedException, AWTException{
//
//		extentTest = ExtentManager.getTestInstance();	
//		extentTest.setDescription("Verify Linear measurement on multi-monitor and on layout change");
//
//		//Loading the patient on viewer
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//
//		//Select Linear measurement from Radial Menu
//		viewerPage.selectDistanceMeasurementFromRadial(1);
//		viewerPage.drawLine(2, 50, 50, 150, 150);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify that Linear Measurement is drawn on Viewbox2");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(2).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox2","A Single instance of Linear Measurement is present on Viewbox2");
//
//		//open a child window 
//		viewerPage.openOrCloseChildWindows(2);
//		viewerPage.switchToNewWindow(2);
//		viewerPage.maximizeWindow();
//
//		//change layout to 1X1 and verify linear measurement drawn above appear on Child window
//		viewerPage.switchToNewWindow(1);
//		viewerPage.selectLayout(viewerPage.oneByOneLayoutIcon,true);
//		viewerPage.switchToNewWindow(2);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify that linear Measurement drawn on Viewbox2 appear on child window after layout change");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(1).size(),1,"Verify that linear Measurement appear on Child window","A Single instance of linear Measurement is present on Child window");
//
//		//draw a line on a Child Window
//		viewerPage.drawLine(1, -50, -200, 100, 180);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify that there are 2 linear measurement on child window");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(1).size(),2,"Verify number of linear measurement on child window","The number of linear measurement on child window is : "+viewerPage.getLinearMeasurements(1).size());
//
//		// Switch to parent window and verify linear measurement drawn above appear on Viewbox2
//		viewerPage.switchToNewWindow(1);
//		viewerPage.selectLayout(viewerPage.twoByTwoLayoutIcon,true);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify linear measurement drawn on child window appear on Parent window after layout change");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(2).size(),2,"Verify number of linear measurement on Viewbox2","The number of linear measurement on Viewbox2: "+viewerPage.getLinearMeasurements(2).size());
//
//		//draw a line on Viewbox1 
//		viewerPage.drawLine(1, -50, 20, 120, 150);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify that linear Measurement drawn on Viewbox1");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(1).size(),1,"Verify that linear Measurement appear on Viewbox1","A Single instance of linear Measurement appear on Viewbox1");
//		viewerPage.selectLayout(viewerPage.oneByOneLayoutIcon,true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify that linear Measurement drawn on Viewbox1");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(1).size(),1,"Verify that linear Measurement appear on Viewbox1","A Single instance of linear Measurement appear on Viewbox1");
//
//		//switch to child window and verify no of linear measurement
//		viewerPage.switchToNewWindow(2);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify linear measurement drawn on child window appear on Parent window after layout change");
//		viewerPage.assertEquals(viewerPage.getLinearMeasurements(1).size(),2,"Verify number of linear measurement on Viewbox2","The number of linear measurement on Viewbox2: "+viewerPage.getLinearMeasurements(2).size());
//	}
//
//}
