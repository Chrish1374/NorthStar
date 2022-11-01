package com.trn.ns.test.obsolete;
//
//import java.util.Set;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DateAndTimeVerificationTest extends TestBase{
//
//	private LoginPage loginPage;
//	private StudyListPage studyPage;
//	private PatientListPage patientPage;
//	private SinglePatientStudyPage patientStudyPage;
//	private ViewerPage viewerpage;
//	private ExtentTest extentTest;
//	
//	private String MRfilePath = TEST_PROPERTIES.get("Breast_MR_2_filepath");
//	private String CTfilePath = TEST_PROPERTIES.get("Liver_9_filepath");
//	private String Subject60filePath = TEST_PROPERTIES.get("Subject_60_filepath");
//	String MRPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, MRfilePath);
//	String CTPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, CTfilePath);
//	String Subject60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, Subject60filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	// TC893 : Verify patient DOB on patient page, single study list page and viewer page.
//	@Test(groups ={"firefox", "Chrome", "IE11", "Edge","multimonitor"})
//	public void test01_DE93_TC893_verifyDOBFormat() throws InterruptedException{
//
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify patient DOB on patient page, single study list page and viewer page");
//		DateFormat dateFormatDOB = DateFormat.getDateInstance(DateFormat.MEDIUM);
//
//		patientPage = new PatientListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying DOB format on Patient list screen" );
//		patientPage.assertTrue(patientPage.verifyDateFormatOnColumnData(dateFormatDOB), "Verify DOB column data format on Patient list screen should be in 'Month Day, Year'", "DOB is in 'Month Day, Year' format");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+MRPatientName+"in viewer" );
//		patientPage.clickOnPatientRow(MRPatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying DOB format on Single Patient list screen" );
//		patientStudyPage.assertTrue(patientStudyPage.verifyDateFormat(dateFormatDOB,patientStudyPage.dateOfBirthValue.getText()),"Verify DOB column data format on Single patient list screen should be in 'Month Day, Year'","DOB is in 'Month Day, Year' format");
//		patientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying DOB format on viewer screen" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(dateFormatDOB,viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim()),"Verify DOB column data format on viewer screen should be in 'Month Day, Year'","DOB is in 'Month Day, Year' format");
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify the layout changed to 1x1");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verifying DOB format on viewer screen on child window" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(dateFormatDOB,viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim()),"Verify DOB column data format on viewer screen should be in 'Month Day, Year'","DOB is in 'Month Day, Year' format");
//
//		//For closing child window
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//	// TC894 : Verify patient Study date and time on Single study list page and Viewer page.
//	//Not work in Chrome and FF because of DE299
//	@Test(groups ={"firefox", "Chrome", "IE11", "Edge","multimonitor"})
//	public void test02_DE93_TC894_verifyStudyDateAndTimeFormat() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify patient Study date and time on Single study list page and Viewer page");
//		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+MRPatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(MRPatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying Study Date and Time format on Single patient list screen" );
//		patientStudyPage.assertTrue(patientStudyPage.verifyDateFormatOnColumnData(formatter),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//		patientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying Study Date and Time format on viewer screen" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify the layout changed to 1x1");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying Study Date and Time format on viewer screen on child window" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		//For closing child window
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//	// TC895 : Verify patient Study date and time on Study list page.
//	//Not work in Chrome and FF because of DE299
//	@Test(groups ={"firefox", "Chrome", "IE11", "Edge","multimonitor"})
//	public void test03_DE93_TC895_verifyStudyDateAndTimeFormat() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify patient Study date and time on Study list page");
//		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
//
//		patientPage = new PatientListPage(driver);
//		patientPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL);
//
//		studyPage = new StudyListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying Study Date and Time format on Study list screen" );
//		studyPage.assertTrue(studyPage.verifyStudyDateFormatOnColumnData(formatter), "Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+MRPatientName+"in viewer" );
//		studyPage.clickOnPatientRow(MRPatientName);
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying Study Date and Time format on viewer screen" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		studyPage.waitForStudyListToLoad();
//		studyPage.clickOnPatientRow(MRPatientName);
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify the layout changed to 1x1");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying Study Date and Time format on viewer screen on child window" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		//For closing child window
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//
//	}
//
//	// TC896 : Verify Acquisition date and time on viewer page.
//	//Not work in Chrome and FF because of DE299
//	@Test(groups ={"firefox", "Chrome", "IE11", "Edge","multimonitor"})
//	public void test04_DE93_TC896_verifyAcquisitionDateAndTimeFormat() throws InterruptedException{
//		
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Acquisition date and time on viewer page");
//		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+CTPatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(CTPatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad(1);
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForViewerpageToLoad(1);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying Aquisition Date and Time format on viewer screen" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getImageAcquisitionDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOntheFirstStudy();
//		viewerpage.waitForViewerpageToLoad();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the layout changed to 1x1");
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon);
//		viewerpage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verifying Aquisition Date and Time format on viewer screen on child window" );
//		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getImageAcquisitionDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");
//
//		//For closing child window
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//	}
//
//	
//	//TC1549 : Incorrect DOB is getting displayed for Subject60 Patient data on Chrome browser (Browser and Study Specific issue
//	//Not working in Edge - Unable to find patient on patient list screen through automation 
//	@Test(groups ={"firefox", "Chrome", "IE11", "Edge", "multimonitor"})
//	public void test06_DE401_TC1549_verifyDOBForSubject60() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Incorrect DOB is getting displayed for Subject60 Patient data on Chrome browser (Browser and Study Specific issue");
//		patientPage = new PatientListPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying DOB on Patient list screen for "+Subject60PatientName);
//		patientPage.assertEquals(patientPage.getDOB(Subject60PatientName), DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath), 
//				"Verify that DOB on Patient list screen for "+Subject60PatientName, "DOB is "+patientPage.getDOB(Subject60PatientName));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+Subject60PatientName+"in viewer" );
//		patientPage.clickOnPatientRow(Subject60PatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying DOB on Single Patient list screen for "+Subject60PatientName);
//		patientStudyPage.assertEquals(patientStudyPage.dateOfBirthValue.getText(), DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath), 
//				"Verify that DOB on Single Patient list screen for "+Subject60PatientName, "DOB is "+patientStudyPage.dateOfBirthValue.getText());
//		
//		patientStudyPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying DOB format on viewer screen for "+Subject60PatientName );
//		viewerpage.assertEquals(viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim(),DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath),
//				"Verify that DOB on viewer screen for "+Subject60PatientName, "DOB is "+viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim());
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.viewerback();
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.clickOnStudy(2);
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.waitForAllImagesToLoad();
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) 
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//			}
//		viewerpage.waitForViewerpageToLoad();
//		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
//		viewerpage.waitForFullOverlayDisplay(1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying DOB format on viewer screen on child window for "+Subject60PatientName);
//		viewerpage.assertEquals(viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim(),DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath),
//				"Verify that DOB on viewer screen for "+Subject60PatientName+" on child window", "DOB is "+viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim());
//
//		//For closing child window
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.openOrCloseChildWindows(1);
//	}
//}

//OBSOLETE as study list is deprecated 
/*	// TC895 : Verify patient Study date and time on Study list page.
	//Not work in Chrome and FF because of DE299
	@Test(groups ={"Chrome", "IE11", "Edge"})
	public void test03_DE93_TC895_verifyStudyDateAndTimeFormat() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify patient Study date and time on Study list page");
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL);

		studyPage = new StudyListPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying Study Date and Time format on Study list screen" );
		studyPage.assertTrue(studyPage.verifyStudyDateFormatOnColumnData(formatter), "Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+MRPatientName+"in viewer" );
		studyPage.clickOnPatientRow(MRPatientName);

		viewerpage = new ViewerPage(driver);
		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerpage.waitForFullOverlayDisplay(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying Study Date and Time format on viewer screen" );
		viewerpage.assertTrue(viewerpage.verifyDateFormat(formatter,viewerpage.getStudyDateTimeOverlay(1).getText().trim()),"Verify the Study Date and Time format should be in 'Month Day, Year Hour:Minutes:Seconds ' format","Study Date and Time is in 'Month Day, Year Hour:Minutes:Seconds ' format");

		

	}

// TC897 : Verify sorting on DOB column.
	@Test(groups ={"firefox", "Chrome", "IE11", "Edge"})
	public void test05_DE93_TC897_verifyDOBSorting() throws ParseException{
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify sorting on DOB column");

		patientPage = new PatientListPage(driver);
		patientPage.columnHeaders.get(3).click();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifing sorting of DOB solumn in ascending order");
		patientPage.assertEquals(patientPage.fetchColumn(3),patientPage.ascSortingCol(3), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(3))+
				"column is sorted in ascending order",patientPage.getText("visibility",patientPage.columnHeaders.get(3)) + "is sorted in ascending order.");

		patientPage.columnHeaders.get(3).click();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifing sorting of DOB solumn in descending order");
		patientPage.assertEquals(patientPage.fetchColumn(3),patientPage.dscSortingCol(3), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(3))+
				"column is sorted in descending order",patientPage.getText("visibility",patientPage.columnHeaders.get(3))+ " is sorted in descending order.");

	}

//TC1549 : Incorrect DOB is getting displayed for Subject60 Patient data on Chrome browser (Browser and Study Specific issue
	//Not working in Edge - Unable to find patient on patient list screen through automation 
	@Test(groups ={"Chrome", "IE11", "Edge"})
	public void test06_DE401_TC1549_verifyDOBForSubject60() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Incorrect DOB is getting displayed for Subject60 Patient data on Chrome browser (Browser and Study Specific issue");
		patientPage = new PatientListPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying DOB on Patient list screen for "+Subject60PatientName);
		patientPage.assertEquals(patientPage.getDOB(Subject60PatientName), DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath), 
				"Verify that DOB on Patient list screen for "+Subject60PatientName, "DOB is "+patientPage.getDOB(Subject60PatientName));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Loading the Patient "+Subject60PatientName+"in viewer" );
		patientPage.clickOnPatientRow(Subject60PatientName);

		patientStudyPage = new SinglePatientStudyPage(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying DOB on Single Patient list screen for "+Subject60PatientName);
		patientStudyPage.assertEquals(patientStudyPage.dateOfBirthValue.getText(), DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath), 
				"Verify that DOB on Single Patient list screen for "+Subject60PatientName, "DOB is "+patientStudyPage.dateOfBirthValue.getText());
		
		patientStudyPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerpage.waitForFullOverlayDisplay(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying DOB format on viewer screen for "+Subject60PatientName );
		viewerpage.assertEquals(viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim(),DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, Subject60filePath),
				"Verify that DOB on viewer screen for "+Subject60PatientName, "DOB is "+viewerpage.getPatientBirthDateOverlay(1).getText().split("(^*[:])")[1].trim());

		
	}*/