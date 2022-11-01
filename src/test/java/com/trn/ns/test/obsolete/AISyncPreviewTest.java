//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.LogStatus;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSDBDatabaseConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.AISyncPreviewWindow;
//import com.trn.ns.page.factory.DICOMRT;
//import com.trn.ns.page.factory.DatabaseMethodsADB;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.HelperClass;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.RegisterUserPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
////package com.trn.ns.test.obsolete;
////
////
////import java.sql.SQLException;
////import java.util.ArrayList;
////import java.util.List;
////import org.openqa.selenium.WebElement;
////import org.testng.annotations.AfterMethod;
////import org.testng.annotations.BeforeMethod;
////import org.testng.annotations.Listeners;
////import org.testng.annotations.Test;
////import com.relevantcodes.extentreports.ExtentTest;
////import com.relevantcodes.extentreports.LogStatus;
////import com.trn.ns.page.constants.LoginPageConstants;
////import com.trn.ns.page.constants.NSDBDatabaseConstants;
////import com.trn.ns.page.constants.NSGenericConstants;
////import com.trn.ns.page.constants.PatientXMLConstants;
////import com.trn.ns.page.constants.URLConstants;
////import com.trn.ns.page.constants.ViewerPageConstants;
////import com.trn.ns.page.factory.AISyncPreviewWindow;
////import com.trn.ns.page.factory.DICOMRT;
////import com.trn.ns.page.factory.DatabaseMethods;
////import com.trn.ns.page.factory.DatabaseMethodsADB;
////import com.trn.ns.page.factory.Header;
////import com.trn.ns.page.factory.LoginPage;
////import com.trn.ns.page.factory.OutputPanel;
////import com.trn.ns.page.factory.PatientListPage;
////import com.trn.ns.page.factory.PointAnnotation;
////import com.trn.ns.page.factory.RegisterUserPage;
////
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.base.TestBase;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.DataReader;
////import com.trn.ns.utilities.ExtentManager;
////
////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
////public class AISyncPreviewTest extends TestBase {
////
////	private ExtentTest extentTest;
////	private LoginPage loginPage;
////	private PatientListPage patientListPage;
//////	private SinglePatientStudyPage spStudyListPage;
////	private ViewerPage viewerPage;
////	private DatabaseMethodsADB db;
////	private OutputPanel panel;
////	private AISyncPreviewWindow aiSync;
////
////	private DICOMRT drt;
////
////	private RegisterUserPage register;
////	String username_1 = "user_1";
////
////
////	String flag = "false";
////
////	// Get Patient Name
////
////	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
////	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 
////
////	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
////	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
////
////	String JohnDoe = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
////	String JohnDoe_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, JohnDoe);
////
////
////	String ChestSR= Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
////	String ChestSR_PatientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestSR);
////
////	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
////	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
////	String machineName = DataReader.getPatientDetails(NSDBDatabaseConstants.MACHINE_NAME, TCGA_filepath);
////
////	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
////	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
////
////
////
////	// Before method, handles the steps before loading the data for set up.
////	@BeforeMethod(alwaysRun=true)
////	public void beforeMethod() throws InterruptedException {
////		// Begin on the Login Page, and log in.
////		loginPage = new LoginPage(driver);
////		loginPage.navigateToBaseURL();
////		loginPage.login(username,password);
////
////	}
////// DR2186 - obsolete
////	//@Test(groups = { "Chrome", "IE11", "Edge", "US1282", "Positive" })
////	public void test04_US1362_TC7086_UIUpdateSyncPreviewTest()
////			throws Throwable {
////
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that check box for sync to pacs on AI sync preview.");
////
////		// Loading the patient on viewer
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + ChestSR_PatientName + "in viewer");
////		patientListPage = new PatientListPage(driver);
////		patientListPage.clickOnPatientRow(ChestSR_PatientName);
////
//////		spStudyListPage = new SinglePatientStudyPage(driver);
//////		spStudyListPage.clickOntheFirstStudy();
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad(2);
////		panel = new OutputPanel(driver);
////		aiSync = new AISyncPreviewWindow(driver);
////
////		panel.enableFiltersInOutputPanel(true, true, true);
////		aiSync.openAndCloseAISyncPreviewWindow(true);
////
////
////		// TC7086 Verify that check box for sync to pacs on AI sync preview.
////
////		viewerPage.assertTrue(panel.isElementPresent(aiSync.syncToPacsCheckBox), "Verifying that checkbox is present or not", "Check Box is present.");
////		viewerPage.assertTrue(panel.isElementPresent(aiSync.syncIconOnAISyncPreview), "Verifying that Sync icon is present or not", "Sync icon is present.");
////		viewerPage.assertTrue(panel.isElementPresent(aiSync.toPacsText), "Verifying that 'ToPacs' text is present or not", "ToPacs text is present.");
////
////		panel.clickUsingAction(aiSync.syncToPacsCheckBox);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verify that buttons is center position and text is 'Synced To PACS'.");
////		viewerPage.assertTrue((panel.getCssValue(aiSync.syncedButtonOrSyncedToPacsText, "justify-content")).contains("center"), "Verifying Synced To PACS button position", "Button is in center");
////		viewerPage.assertTrue(panel.getText(aiSync.syncedButtonOrSyncedToPacsText).contains("Synced to PACS"), "Verifying text of button is Synced To PACS", "Text of button is Synced To PACS");
////		aiSync.enableOrDisableFilterButton(true,aiSync.sourceSeriesButton);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verifying the color of synced button after disabling the filter button'.");
////		viewerPage.assertTrue(panel.getCssValue(aiSync.syncedButtonOrSyncedToPacsButton,NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR), "Verifying color of synced button before disabling the filter button", "The color is:-" +ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR);
////	}
////
////}
//
//
//
//// Before method, handles the steps before loading the data for set up.
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1282", "Positive" })
//	public void test01_US1282_TC6725_TC6729_SyncButtonAISyncPreviewTest() throws InterruptedException
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Copy button functionality in the Output aiSync."+"<br> Verify the Preview page UI.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"[1/8]", "Loading the Patient " + ChestSR_PatientName + "in viewer");
//		
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(ChestSR_PatientName,username, password,2);
//		
//		aiSync = new AISyncPreviewWindow(driver);
//	
//		// Click on the Output panel button
//
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//
//		//TC6725 Verify the copy button functionality.
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/8]","Verify that 'Sync ALL findings' button is present in Ouput aiSync.");
//
//		//Verifying the title and sync all findings button present or not
//		aiSync.assertTrue(aiSync.isElementPresent(aiSync.syncAllfindingsIcon),"Verifying that Sync All findings button is present", "Verified its present");
//		aiSync.assertEquals(aiSync.getAttributeValue(aiSync.syncAllfindingstext, NSGenericConstants.TITLE), ViewerPageConstants.SYNC_ALL_FINDINGS, "Verifying the tool tip of the sync all findings button", "Tool tip is Sync all findings");
//
//		int thumbnailCount=aiSync.thumbnailList.size();
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//
//		// Verifying that Output panel is getting closed.
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.outputPanelSection), "verifying that Output panel is not opened after opening AISyncPreview window", "Output panel is closed");
//
//		// Verifying that all the findings are getting copied from Output panel on AI Sync preview window.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/8]","Verify that findings count is same in AIsyncpreview window count is same Ouput aiSync.");
//		aiSync.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),thumbnailCount, "Verifying the findings count is same in O/P and on AI sync preview", "Count is same");
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//
//		//Again opening O/P panel and selecting findings from keyboard by CTRl+Left click and opening AI sync preview window.
//
//		aiSync.mouseHover(aiSync.getViewPort(2));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//
//		List<String> seriesDescription=aiSync.convertWebElementToStringList(aiSync.findingSummarySeriesDesc);
//		List<String> findingCount=aiSync.convertWebElementToStringList(aiSync.findingSummaryCount);
//		int thumbnailCount2=aiSync.findingsList.size();
//
//		for(int i=1; i<=3;i++){
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//		}
//		int SelectedThumbnail =aiSync.getCountOfSelectedThumbnail(thumbnailCount2);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/8]","Verify that findings count is same in AIsyncpreview window count is same Ouput aiSync.");
//		aiSync.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),SelectedThumbnail,"Verifying the findings count is same in O/P and on AI sync preview", "Count is same");
//
//		//TC6729 Verify the Preview page
//
//
//		aiSync.assertTrue(aiSync.getText(aiSync.aiSyncPreviewWindowTitle).contains(ViewerPageConstants.AI_SYNC_PREVIEW_TITLE),"Verifying the title of the AI sync preview window", "Title is AI SYNC PREVIEW");
//		aiSync.assertEquals(aiSync.getAttributeValue(aiSync.expandOrCollapseArrow, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_COLLAPSE, "Verifying the state of filter icon", "Arrow is in expanded state");
//
//
//		// Verifying that filters buttons are selected and highlighted default on opening AISyncPreview window
//		for(int i =0 ;i<aiSync.filtersButtonOnAISyncPreview.size();i++){
//
//			verifyButtonsSelection(aiSync.filtersButtonOnAISyncPreview.get(i),"Checkpoint[5/8]","Verifying all the buttons are selected");
//		}
//
//		// Verifying the SeriesDescriptions and findings count on AI Sync Preview window
//		aiSync.assertTrue(aiSync.verifySeriesDescAndFindingCountOnAISyncPreview(seriesDescription, findingCount), "Verifying that SeriesDescriptions and count below the filters buttons are present or not", "SeriesDescriptions and count is present");
//
//		// Disabling the filters button one by one and making sure that details are getting hided for that button.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/8]","Verify that buttons are deselected and details are hidden.");
//     
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.sourceSeriesButton, aiSync.sereisDetailsOnAISyncPreview),"Verifying that Series details is present", "Series details are not present after disabling the button");
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.findingNoteButton, aiSync.findingNoteCommentDetails),"Verifying that Finding note is present or not", "Findings notes are not present after disabling the button");
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.creationUpdateButton, aiSync.findingsEditorNameAndDateOnAISyncPreview),"Verifying that Editor name and Date is present or not", "Editor name is not present after disabling the button");
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.thumbnailButton, aiSync.getthumbanilListOnAiPreview),"Verifying that thumbnail present or not", "thumbnail is not present after disabling the button");
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.findingDescriptionButton, aiSync.getCompleteFindingsDescriptionsOnAiPreview),"Verifying that Findings Descriptions is present or not", "Findings Descriptions is not present after disabling the button");
//		aiSync.assertTrue(aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(true,aiSync.findingCountButton, aiSync.findingSummaryCountOnAISyncPreview),"Verifying that Findings Summary & count is present or not", "Findings Summary & count is not present after disabling the button");
//
//		// Verifying that all filters buttons are disabled now
//
//		for(int i =0 ;i<aiSync.filtersButtonOnAISyncPreview.size();i++){
//
//			verifyButtonsDeSelection(aiSync.filtersButtonOnAISyncPreview.get(i),"Checkpoint[7/8]","Verifying all the buttons are Deselected");
//		}
//
//		// Clicking on filter Arrow and verifying that all filter button are hidden.
//		aiSync.click(aiSync.expandOrCollapseArrowIcon);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/8]","Verify that all buttons are hidden.");
//		aiSync.assertEquals(aiSync.getAttributeValue(aiSync.expandOrCollapseArrow, NSGenericConstants.TITLE), ViewerPageConstants.OUTPUT_PANEL_EXPAND, "Verifying the state of filter icon", "Arrow is in collapsed state");
//		aiSync.assertTrue(aiSync.filtersButtonOnAISyncPreview.isEmpty(),"Verifying filters button are visible or not", "Button are not visible");
//		
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//	}
//
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1282","US1284", "Positive" })
//	public void test02_US1282_TC6756_US1284_TC6754_AISyncPreviewSettingDBTest() throws InterruptedException, SQLException
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the User selected settings on Preview page are saved in DB.."+
//		"<br> Verify 'CopyFilter' table after DB upgrade and downgrade..");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + patientNameTCGA + "in viewer");
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(username, password);
//		patientPage = new PatientListPage(driver);
//	
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(patientNameTCGA,1);
//		
//		drt= new DICOMRT(driver);
//		drt.waitForDICOMRTToLoad();
//		aiSync = new AISyncPreviewWindow(driver);
//		db= new DatabaseMethodsADB(driver);
//
//
//		//TC6754 Verifying the Copy Filter table is present in NSDB and CopyFilterI,UserAccountId,CopyFilters columns are present.
//
//		db.assertEquals(db.getTableColumnName(NSDBDatabaseConstants.COPYFILTER_TABLE).size(),3,"Checkpoint[1a/14]","Verifying the DB Columns");
//		db.assertEquals(db.getTableColumnName(NSDBDatabaseConstants.COPYFILTER_TABLE).get(0),NSDBDatabaseConstants.COPYFILTER_ID_COLUMN,"Checkpoint[1b/14]","Verifying the DB CopyFilterId Columns");
//		db.assertEquals(db.getTableColumnName(NSDBDatabaseConstants.COPYFILTER_TABLE).get(1),NSDBDatabaseConstants.USERACCOUNT_ID_COLUMN,"Checkpoint[1c/14]","Verifying the DB UserAccountId Columns");
//		db.assertEquals(db.getTableColumnName(NSDBDatabaseConstants.COPYFILTER_TABLE).get(2),NSDBDatabaseConstants.COPYFILTERS_COLUMN,"Checkpoint[1d/14]","Verifying the DB  CopyFilters Columns");
//
//		//TC6756 Step1, Step2, Step3 ,Checking the user preferences in copy filter table in Database.
//
//		//Opening O/P panel and selecting findings from keyboard by CTRl+Left click and opening AI sync preview window.
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/14]","Verify that 'Sync ALL findings' button is present in Ouput aiSync.");
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		int totalThumbnailCount=aiSync.findingsList.size();
//
//		for(int i=1; i<=3;i++){
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//		}
//		int SelectedThumbnail =aiSync.getCountOfSelectedThumbnail(totalThumbnailCount);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/14]","Verify that findings count is same in AIsyncpreview window count is same Ouput aiSync.");
//		aiSync.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),SelectedThumbnail,"Verifying the findings count is same in O/P and on AI sync preview", "Count is same");
//
//		// Verifying that filters buttons are selected and highlighted default on opening AISyncPreview window
//		for(int i =0 ;i<aiSync.filtersButtonOnAISyncPreview.size();i++){
//
//			verifyButtonsSelection(aiSync.filtersButtonOnAISyncPreview.get(i),"Checkpoint[4/14]","Verifying all the buttons are selected");
//		}
//
//		//Clicking on Synced/ copy button and verifying the AI sync preview window is closed and nothing is updated in DB in Copy filter table.
//		aiSync.click(aiSync.syncedButtonOrSyncedToPacsText);
//
//	//	aiSync.waitForElementInVisibility(AISync.aiSyncPreviewWindow, 3);
//
//		aiSync.waitForTimePeriod(Configurations.TEST_PROPERTIES.get("waitForElementLowTime"));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/14]","Verifyin that AI sync preview is getting closed and nothing updated in database in copy filter table.");
//		aiSync.click(aiSync.getViewPort(1));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.assertFalse((aiSync.isElementPresent(aiSync.aiSyncPreviewWindow)), "Verifying that AI sync preview is closed", "AI Sync preview is closed");
//
//		int accountID=db.getUserIDFromUserAccount(username);
//		String query="select CopyFilters from dbo.CopyFilter where UserAccountId="+accountID+"";
//		String CopyFiltersValue=db.getValue(query);
//		db.assertEquals(CopyFiltersValue, "[]", "Verifying that value is nothing", "No Value is present");
//		// Step 4 and Step 5 .Again Opening O/P panel and selecting findings from keyboard by CTRl+Left click and opening AI sync preview window.
//
//		//aiSync.click(aiSync.getViewPort(1));
//		//aiSync.enableFiltersInOutputPanel(true, true, true);
//
//		for(int i=1; i<=3;i++){
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//		}
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/14]","Verify that findings count is same in AIsyncpreview window count is same Ouput aiSync.");
//		aiSync.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),SelectedThumbnail,"Verifying the findings count is same in O/P and on AI sync preview", "Count is same");
//
//		// Verifying that filters buttons are selected and highlighted default on opening AISyncPreview window
//		for(int i =0 ;i<aiSync.filtersButtonOnAISyncPreview.size();i++){
//
//			verifyButtonsSelection(aiSync.filtersButtonOnAISyncPreview.get(i),"Checkpoint[6/14]","Verifying all the buttons are selected");
//		}
//
//
//		VerifyFilterInCopyFilterTable( true,ViewerPageConstants.CONTENT_SERIES_BUTTON.toLowerCase(), aiSync.sourceSeriesButton, aiSync.sereisDetailsOnAISyncPreview, username);
//
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		aiSync.click(aiSync.filterButton);
//		verifyButtonsDeSelection(aiSync.sourceSeriesButton,"Checkpoint[7/14]","Verifying Source Series button is deselected");
//		deleteDBValue();
//
//
//		//Step6-Register User and Login with new user and selecting the patient
//
//		aiSync.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
//		register = new RegisterUserPage(driver);		
//		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Logging using user A");
//		Header header = new Header(driver);		
//		header.logout();
//		
//		helper.loadViewerDirectly(patientNameTCGA,username_1, username_1,1);
//		drt.waitForDICOMRTToLoad();
//
//		//Opening O/P panel selecting the findings and opening the AI sync preview window with new user
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/14]","Opening the AI sync preview filter window and verifying that all filters are selected and enabled for new user "+username_1+"");
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		int thumbnailCountNewUser=aiSync.findingsList.size();
//
//		for(int i=1; i<=3;i++){
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//		}
//		int SelectedThumbnailNewUser =aiSync.getCountOfSelectedThumbnail(thumbnailCountNewUser);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/14]","Verify that findings count is same in AIsyncpreview window count is same Ouput aiSync.");
//		aiSync.assertEquals(aiSync.getthumbanilListOnAiPreview.size(),SelectedThumbnailNewUser,"Verifying the findings count is same in O/P and on AI sync preview", "Count is same");
//
//		// Step 7-Verifying that filters buttons are selected and highlighted default on opening AISyncPreview window
//		for(int i =0 ;i<aiSync.filtersButtonOnAISyncPreview.size();i++){
//
//			verifyButtonsSelection(aiSync.filtersButtonOnAISyncPreview.get(i),"Checkpoint[10/14]","Verifying all the buttons are selected");
//		}
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[11/14]","Disabling the machine filter and checking the value in 'Copy Filter' table after clicking on click to sync button.");
//
//		VerifyFilterInCopyFilterTable( true,ViewerPageConstants.FINDINGNOTE_BUTTONTAG, aiSync.findingNoteButton, aiSync.findingNoteCommentDetails, username_1);
//
//		String UserAccountIDNewUser=Integer.toString(db.getUserIDFromUserAccount(username_1));
//		ArrayList<String> CopyFilterValueNewUser = db.getCopyFiltersValue(UserAccountIDNewUser);
//		String filterValue=CopyFilterValueNewUser.get(0).replaceAll("[^a-zA-Z0-9]", "");
//		db.assertTrue(filterValue.contains(ViewerPageConstants.FINDINGNOTE_BUTTONTAG)&&CopyFilterValueNewUser.get(0).contains(NSGenericConstants.BOOLEAN_FALSE), "", "");
//
//		//Step 9
//
//		aiSync.click(aiSync.getViewPort(1));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		aiSync.click(aiSync.expandOrCollapseArrowIcon);
//		verifyButtonsDeSelection(aiSync.findingNoteButton,"Checkpoint[12/13]","Verifying Finding note button is deselected");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[13/14]","Disabling the machine filter and checking the value in 'Copy Filter' table without clicking on that Click To Sync button.");
//		//Step 10
//		aiSync.click(aiSync.findingNoteButton);
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//		//step 11
//		db.assertTrue(filterValue.contains(ViewerPageConstants.FINDINGNOTE_BUTTONTAG)&&CopyFilterValueNewUser.get(0).contains("false"), "", "");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[14/14]","Enabling the machine filter and checking the value in 'Copy Filter' table after clicking on that Click To Sync button.");
//		//Step 12&13
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		aiSync.enableOrDisableFilterButton(false, aiSync.findingNoteButton);
//		aiSync.click(aiSync.syncedButtonOrSyncedToPacsText);
//
//		int UserAccountIDUpdated=db.getUserIDFromUserAccount(username_1);
//		String NewQuery="select CopyFilters from dbo.CopyFilter where UserAccountId="+UserAccountIDUpdated+"";
//		String Value=db.getValue(NewQuery);
//		db.assertEquals(Value, "[]", "Verifying that value is nothing", "No Value is present");
//		
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//
//	}
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "US1282","DR2186","Positive" })
//	public void test03_US1362_TC7060_TC7061_TC7071_DR2186_TC8785_UIUpdateSyncPreviewTest()
//			throws Throwable {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Copy button functionality in the Output aiSync."+"<br> Verify the toggle functionality on Synced button on AI Sync Preview popup."+"<br> Verify that name of 'PreviewPage' changed to AI Sync Preview. <br>"+
//		"Verify 'sync to PACS' checkbox is removed on AI Sync Preview window.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + ChestSR_PatientName + "in viewer");
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(ChestSR_PatientName,username, password,2);
//			
//		aiSync = new AISyncPreviewWindow(driver);
//
//
//		// TC7060 Opening Output panel and and opening AI sync preview and Verifying that Synced button is present and in center 
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verify that buttons is center position and text is 'Synced'.");
//		aiSync.assertTrue((aiSync.getCssValue(aiSync.syncedButtonOrSyncedToPacsText, ViewerPageConstants.JUSTIFY_CONTENT)).contains(ViewerPageConstants.CENTER), "Verifying Synced button position", "Button is in ccenter");
//		aiSync.assertTrue(aiSync.getText(aiSync.syncedButtonOrSyncedToPacsText).contains(ViewerPageConstants.SYNCED), "Verifying text of button is Synced", "Text of button is Synced");
//
//		// TC7061 Verify the toggle functionality on Synced button on AI Sync Preview popup.
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verify that buttons are getting toggled off and background is getting changed.");
//
//		aiSync.assertTrue(aiSync.getCssValue(aiSync.syncedButtonOrSyncedToPacsButton,NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.SYNCED_BUTTON_BACKGROUNDCOLOR), "Verifying color of synced button before disabling the filter button", "The color is:-" +ViewerPageConstants.SYNCED_BUTTON_BACKGROUNDCOLOR);;
//		aiSync.enableOrDisableFilterButton(true,aiSync.sourceSeriesButton);
//		aiSync.assertTrue(aiSync.getCssValue(aiSync.syncedButtonOrSyncedToPacsButton,NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR), "Verifying color of Click to Sync button after disabling the filter button", "The color is:-" +ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR);;
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verify sync to PACS checkbox not visible on AI Sync preview.");
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.syncToPacsCheckBox), "Verifying that checkbox is present or not", "Check Box is present.");
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.syncIconOnAISyncPreview), "Verifying that Sync icon is present or not", "Sync icon is present.");
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.toPacsText), "Verifying that 'ToPacs' text is present or not", "ToPacs text is present.");
//	
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//	}
//
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1765", "Positive" })
//	public void test06_DE1765_TC7123_VerifyStateIndicatorForAISyncPreviewWindow() throws InterruptedException
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that finding's state indicator is displayed in AI Sync Preview window for DICOM SC finding.");
//
//		// Loading the patient data on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(imbio_PatientName, username, password, 3);
//	
//		aiSync = new AISyncPreviewWindow(driver);
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//
//		for(int findingcount=0;findingcount<aiSync.PreviewfindingSummaryCount.size();findingcount++)
//		{
//			aiSync.assertTrue(aiSync.getAttributeValue(aiSync.findingPreviewWindowStateIndicator.get(findingcount), NSGenericConstants.STYLE).contains(ViewerPageConstants.PENDING_COLOR), "Checkpoint[1/1]", "Verified that the state indicator color for pending findings are Lighblue at sync preview window");
//		}
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//
//	}
//	
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1765", "Positive" })
//	public void test07_DE1783_TC7256_VerifyAISycnPreviewWindowWhenBrowserResize() throws InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that the AI Sync Preview window is closed when user clicks anywhere on the screen when browser screen is re-sized");
//
//		// Loading the patient data on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(imbio_PatientName, username, password, 3);
//	
//		aiSync = new AISyncPreviewWindow(driver);
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		aiSync.assertTrue(aiSync.isElementPresent(aiSync.aiSyncPreviewWindow), "Checkpoint[1/4]", "Verified that AI sync preview window is opened.");
//		
//		aiSync.click(aiSync.getXCoordinate(aiSync.EurekaLogo),aiSync.getYCoordinate(aiSync.EurekaLogo));
//		aiSync.waitForTimePeriod(2000);
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.aiSyncPreviewWindow), "Checkpoint[2/4]", "Verified that AI sync preview window is closed when click anywhere on the screen..");
//		
//		aiSync.resizeBrowserWindow(900, 900);
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		aiSync.assertTrue(aiSync.isElementPresent(aiSync.aiSyncPreviewWindow), "Checkpoint[3/4]", "Verified that AI sync preview window is opened after browser resize.");
//		
//		aiSync.click(aiSync.getXCoordinate(aiSync.EurekaLogo),aiSync.getYCoordinate(aiSync.EurekaLogo));
//		aiSync.waitForTimePeriod(2000);
//		aiSync.assertFalse(aiSync.isElementPresent(aiSync.aiSyncPreviewWindow), "Checkpoint[4/4]", "Verified that AI sync preview window is closed when click anywhere on the screen.");
//		
//
//	}
//    
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1764", "Positive" })
//	public void test08_DE1764_TC7120_verifyFilterIcon() throws InterruptedException
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the 'Filters' arrow is showing the right direction for collapse and expand on AI sync preview window.");
//
//		// Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + imbio_PatientName + "in viewer");
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(imbio_PatientName, username, password, 3);
//	
//		aiSync = new AISyncPreviewWindow(driver);
//		
//		// Click on the Output panel button
//	
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.openAndCloseAISyncPreviewWindow(true);
//		
//		aiSync.assertEquals(aiSync.getAttributeValue(aiSync.expandOrCollapseArrow, NSGenericConstants.TITLE),ViewerPageConstants.OUTPUT_PANEL_COLLAPSE,"Checkpoint[1/4]","Verifying the title for expand icon");
//		aiSync.compareElementImage(protocolName,aiSync.expandOrCollapseArrowIcon , "Checkpoint[2/4]", "TC_08_01");
//		
//		aiSync.click(aiSync.expandOrCollapseArrowIcon);		
//				
//		aiSync.compareElementImage(protocolName,aiSync.expandOrCollapseArrowIcon , "Checkpoint[3/4]", "TC_08_02");
//		aiSync.assertEquals(aiSync.getAttributeValue(aiSync.expandOrCollapseArrow, NSGenericConstants.TITLE),ViewerPageConstants.OUTPUT_PANEL_EXPAND,"Checkpoint[4/4]","Verifying the title for collapse icon");
//		
//		aiSync.openAndCloseAISyncPreviewWindow(false);
//
//	}
//	
//	//DE1846: [Hardening] - Findings selected on mouse button up
//	@Test(groups = { "Chrome", "IE11", "Edge", "DE1846", "Positive"})
//	public void test09_DE1846_TC7455_verifyFindingNotSelectedOnMouseButtonUp() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify findings are not selected on mouse button up in output aiSync.");
//
//		// Loading the patient on viewer
//		helper = new HelperClass(driver);
//		helper.loadViewerDirectly(GSPS_PatientName, username, password, 1);
//	
//		aiSync = new AISyncPreviewWindow(driver);
//
//		PointAnnotation point = new PointAnnotation(driver);
//		point.selectPointFromQuickToolbar(1);
//		
//		int findingCount=aiSync.getFindingsCountFromFindingTable(2);
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Rejeccting some of points");
//		for(int i =0;i<4;i++)
//			point.selectRejectfromGSPSRadialMenu();
//
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Marking pending some of points");
//		for(int i =0;i<4;i++)
//			point.selectAcceptfromGSPSRadialMenu();
//
//		//Releasing left mouse button (mouse button up event) on 3rd finding should not lead to selecting it.
//		//Only first finding which is selected in step-1 should be selected/highlighted.
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.pressAndholdShiftKey(aiSync.getThumbnailRow(1));
//		int selectedFinding=aiSync.getCountOfSelectedThumbnail(findingCount);
//		aiSync.scrollIntoView(aiSync.thumbnailList.get(4));
//		aiSync.releaseShiftKeyPressed();
//		aiSync.assertEquals(aiSync.getCountOfSelectedThumbnail(findingCount), selectedFinding, "Checkpoint[1/4]", "Verified that Releasing left mouse button (mouse button up event) and shift keys on 3rd finding should not lead to selecting the third finding.");	
//		aiSync.openAndCloseOutputPanel(false);
//		
//		//Releasing left mouse button (mouse button up event) on 3rd finding should not lead to selecting it.
//		//Only first finding which is selected in step-1 should be selected/highlighted.
//		aiSync.click(aiSync.getViewPort(1));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		aiSync.pressAndHoldControlKey(aiSync.getThumbnailRow(2));	
//		selectedFinding=aiSync.getCountOfSelectedThumbnail(findingCount);
//		aiSync.scrollIntoView(aiSync.thumbnailList.get(4));
//		aiSync.releaseControlKey();
//		aiSync.assertEquals(aiSync.getCountOfSelectedThumbnail(findingCount), selectedFinding, "Checkpoint[2/4]", "Verified that Releasing left mouse button (mouse button up event) and control key on 3rd finding should not lead to selecting it");	
//
//		//After releasing the mouse left button (mouse button up event) on the blank space present near A/R/P filters, selected findings should not get un-selected and should remain selected.
//		aiSync.click(aiSync.getViewPort(1));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		for(int i=1; i<=3;i++)
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//	    selectedFinding=aiSync.getCountOfSelectedThumbnail(findingCount);
//		aiSync.scrollIntoView(aiSync.acceptedButton);
//		aiSync.assertEquals(aiSync.getCountOfSelectedThumbnail(findingCount), selectedFinding, "Checkpoint[3/4]", "Verified that releasing left mouse button (mouse button up event) on the blank space should not lead to selecting it");	
//
//		//While scrolling using scrollbar, findings should not get de-selected.
//		aiSync.click(aiSync.getViewPort(1));
//		aiSync.enableFiltersInOutputPanel(true, true, true);
//		for(int i=1; i<=3;i++)
//			aiSync.pressControlLeft(aiSync.getThumbnailRow(i));
//	    selectedFinding=aiSync.getCountOfSelectedThumbnail(findingCount);
//		aiSync.scrollIntoView(aiSync.acceptedButton);
//		aiSync.scrollIntoView(aiSync.thumbnailList.get(findingCount-1));
//		aiSync.assertEquals(aiSync.getCountOfSelectedThumbnail(findingCount), selectedFinding, "Checkpoint[4/4]", "Verified that finding is not deselected after performing mouse scroll up and down.");	
//		
//		}
//		
//	private void verifyButtonsSelection(WebElement filter,String checkpoint,String comment) {
//
//		aiSync = new AISyncPreviewWindow(driver);
//		aiSync.assertTrue(aiSync.getCssValue(filter, NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.AISYNC_FILTERBUTTON_BACKGROUND),checkpoint,comment);
//		aiSync.assertTrue(aiSync.getCssValue(filter, NSGenericConstants.CSS_PROP_COLOR).contains(ViewerPageConstants.AISYNC_FILTERBUTTON_COLOR),checkpoint,comment);
//	}
//
//	private void verifyButtonsDeSelection(WebElement filter, String checkpoint,String comment) {
//
//		aiSync = new AISyncPreviewWindow(driver);
//		aiSync.assertTrue(aiSync.getCssValue(filter, NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.BLACK_COLOR_1),checkpoint,comment);
//		aiSync.assertTrue(aiSync.getCssValue(filter, NSGenericConstants.CSS_PROP_COLOR).contains(ViewerPageConstants.FILTERS_BUTTON_DISABLED_COLOR),checkpoint,comment);
//	}
//
//	private void VerifyFilterInCopyFilterTable(boolean EnableORDisbale,String ButtonTag, WebElement filterButton,List<WebElement> WhichDetails,String username ) throws InterruptedException, SQLException {
//
//		aiSync = new AISyncPreviewWindow(driver);
//		db= new DatabaseMethodsADB(driver);
//		aiSync.verifyDetailsAISyncPreviewAfterDisablingButton(EnableORDisbale, filterButton ,WhichDetails);
//		aiSync.assertTrue(aiSync.getCssValue(aiSync.syncedButtonOrSyncedToPacsButton,NSGenericConstants.BACKGROUND_COLOR).contains(ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR), "Verifying color of Click to sync button after disabling the filter button", "The color is:-" +ViewerPageConstants.CLICK_TO_SYNC_BUTTON_BACKGROUNDCOLOR);;
//		aiSync.click(aiSync.syncedButtonOrSyncedToPacsText);
//		aiSync.waitForTimePeriod(Configurations.TEST_PROPERTIES.get("waitForElementLowTime"));
//		aiSync.assertFalse((aiSync.isElementPresent(aiSync.aiSyncPreviewWindow)), "Verifying that AI sync preview is closed", "AI Sync preview is closed");
//		String UserAccountID=Integer.toString(db.getUserIDFromUserAccount(username));
//		ArrayList<String> CopyFilterValue = db.getCopyFiltersValue(UserAccountID);
//		String filteredValue=CopyFilterValue.get(0).replaceAll("[^a-zA-Z0-9]", "");
//		db.assertTrue(filteredValue.contains(ButtonTag)&&CopyFilterValue.get(0).contains("false"), "", "");
//	}