//package com.trn.ns.test.obsolete;
//
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.Set;
//
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.dataProviders.DataProviderArguments;
//import com.trn.ns.dataProviders.ExcelDataProvider;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DefaultSlicePositionRenderingTest extends TestBase {
//	private ViewerPage viewerPage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//	
//	// Get Patient Name
//	String aidoc_filepath = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
//	String aidocPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, aidoc_filepath);
//
//	String TeraRecon_BrainTDA_filepath = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
//	String tdaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, TeraRecon_BrainTDA_filepath);
//
//	String SQA_Testing = Configurations.TEST_PROPERTIES.get("SQA_Testing");
//	String sqaTestingPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, SQA_Testing);
//
//	String DigitalReferenceObject_filepath = Configurations.TEST_PROPERTIES.get("DigitalReferenceObject_filepath");
//	String dRPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, DigitalReferenceObject_filepath);
//
//	String MRLSP_filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
//	String LSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,MRLSP_filePath);
//
//	String Quibim_BreastPerfusion_filePath=Configurations.TEST_PROPERTIES.get("Quibim_BreastPerfusion_filepath");
//	String Quibim_BreastPerfusionPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,Quibim_BreastPerfusion_filePath);
//
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String aH4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//	
//	//TC2101 - Set default slice position based on configuration
//	//Limitation : Not working in edge because of mouseHover function
//	@Test(groups ={"firefox","Chrome","IE11","multimonitor","US614","Sanity"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
//	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test03_US614_SlicePosition"})
//	public void test03_US614_TC2101_verifySlicePositionBasedOnConfiguration(String percValue) throws SQLException, InterruptedException, IOException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Set default slice position based on configuration");
//
//		//Changing the default slice position in configSetting table
//		DatabaseMethods db = new DatabaseMethods(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Updating the default slice position value to :"+percValue, "");
//		db.updateDefaultSlicePosition(percValue);
//		db.assertEquals(db.getDefaultSlicePosition(),percValue,"verifying the default slice position percentage is :"+percValue,"verified");
//
//		db.resetIISPostDBChanges();
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(sqaTestingPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+sqaTestingPatientName+" in viewer" );
//
//		//Verify default slice position 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that all viewboxes load with "+percValue+" of total slices available in them by default");
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),percValue), 
//				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(1));
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),percValue), 
//				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(2));
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),percValue), 
//				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(3));
//
//		//Opening multiple child windows and verifying the default slice position in all windows
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying the slice position in each viewbox across parent and child windows ");
//		viewerPage.openOrCloseChildWindows(3);
//		viewerPage.selectLayout(viewerPage.oneByOneLayoutIcon);
//		String parentWindow = viewerPage.getCurrentWindowID();
//
//		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {				
//			if(!childWindow.equals(parentWindow)){
//				viewerPage.switchToWindow(childWindow);
//				viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),percValue), 
//						"Verify the default slice position for series in first viewbox", "First series of child window :"+viewerPage.getCurrentPageURL()+" is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(1));
//			}
//		}
//
//		viewerPage.switchToWindow(parentWindow);
//		viewerPage.selectLayout(viewerPage.twoByTwoLayoutIcon);
//
//		//After layout change verify the default slice position
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that all viewboxes load with the 50th percentile slice of total slices available in them by default after layout change");
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(1)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(1),percValue), 
//				"Verify the default slice position for series in first viewbox", "First series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(1));
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(2)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(2),percValue), 
//				"Verify the default slice position for series in second viewbox", "Second series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(2));
//		viewerPage.assertEquals(Integer.parseInt(viewerPage.getImageCurrentScrollPositionOverlayText(3)), viewerPage.getSlicePositionAsPerConfiguration(viewerPage.getMaxNumberofScrollForViewbox(3),percValue), 
//				"Verify the default slice position for series in third viewbox", "Third series is loading at slice number :"+viewerPage.getImageCurrentScrollPositionOverlayText(3));
//
//		//Closing all open child window
//		viewerPage.openOrCloseChildWindows(1);
//	}
//
//
//	
//}
