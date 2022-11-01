package com.trn.ns.test.basic;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MappingKeysAndMouseEvents extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerPage viewerpage;
	private ViewBoxToolPanel viewBoxPanel;
	private HelperClass helper;
	private ViewerToolbar tool;
	//private SinglePatientStudyPage studyPage;

	String liver9FilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);

	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "dbConfig", "Positive" })
	public void test01_US880_TC3411_TC3475_TC3474_verifyDBForKeysMappingAndNonUICheck() throws SQLException, InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of mapping keys and mouse events tables in DB"
				+ "<br> Verify mapping keys and mouse events functionality for page access through Non UI login"
				+ "<br> Verification mapping on first user and viewer has existing user");

		DatabaseMethods db = new DatabaseMethods(driver);		
		db.assertTrue(db.getDataFromMouseEventType().size()>0,"Checkpoint[1/11]","Verifying the EventMap Table Exists");		
		db.assertEquals(db.getTableColumnName("MouseEventTypeConfiguration").size(),2,"Checkpoint[2/11]","Verifying the DB Columns");
		db.assertEquals(db.getTableColumnName("MouseEventTypeConfiguration").get(0),"ButtonId","Checkpoint[3/11]","Verifying the DB Columns");
		db.assertEquals(db.getTableColumnName("MouseEventTypeConfiguration").get(1),"MouseButton","Checkpoint[4/11]","Verifying the DB Columns");

		db.assertTrue(db.getDataFromEventMap().size()>0,"Checkpoint[TC3411_1][5/11]","Verifying the EventMap Table Exists");		
		db.assertEquals(db.getTableColumnName("EventMap").size(),2,"Checkpoint[6/11]","Verifying the DB Columns");
		db.assertEquals(db.getTableColumnName("EventMap").get(0),"Action","Checkpoint[7/11]","Verifying the DB Columns");
		db.assertEquals(db.getTableColumnName("EventMap").get(1),"ButtonId","Checkpoint[8/11]","Verifying the DB Columns");

		db.assertTrue(db.deleteActionInEventMap("2"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("4"),"Verifying user can delete Action in EventMap","Verified");
		db.addActionInEventMap("2", ViewerPageConstants.ZOOM);
		db.addActionInEventMap("4", ViewerPageConstants.SCROLL);

		loginPage = new LoginPage(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName, Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"), 1);
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel = viewBoxPanel.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, -100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/11]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel,"verify zooming should happen with 'LeftMousedown'","The Zoom level after Mouse Up increases from "+ intialZoomLevel + " to "+ viewBoxPanel.getZoomLevelValue(1));

		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage registerUserPage = new RegisterUserPage(driver);
		
		//creating new user and verifying it
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Creating new user and verifying the mapping for keys");
		String username = "abc";
		registerUserPage.createNewUser("abc", "","test" , LoginPageConstants.SUPPORT_EMAIL, username, username, username);
		
		loginPage.navigateToBaseURL();
		loginPage.login(username, username);
		patientPage=new PatientListPage(driver);
		
		helper.loadViewerDirectly(liver9PatientName,1);
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
		viewBoxPanel=new ViewBoxToolPanel(driver);
		intialZoomLevel =viewBoxPanel.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, -100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[10/11]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel,"verify zooming should happen with 'LeftMousedown'","The Zoom level after Mouse Up increases from "+ intialZoomLevel + " to "+ viewBoxPanel.getZoomLevelValue(1));

		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,TEST_PROPERTIES.get("nsPassword")); 
		hm.put(LoginPageConstants.USERNAME,TEST_PROPERTIES.get("nsUserName"));
		String viewerAH4Url = URLConstants.VIEWER_PAGE_URL+"/2.16.840.1.113669.632.21.486081.486212.16416582522216879.1424103";

		String nonUIURL =viewerpage.getNonUILaunchURL(viewerAH4Url, hm);
		viewerpage.openNewWindow(nonUIURL);
		viewerpage.waitForViewerpageToLoad();
		intialZoomLevel = viewBoxPanel.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, -100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[11/11]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel,"verify zooming should happen with 'LeftMousedown'","The Zoom level after Mouse Up increases from "+ intialZoomLevel + " to "+ viewBoxPanel.getZoomLevelValue(1));

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "dbConfig", "Negative" })
	public void test02_US880_TC3412_verifyBehaviorForAddingDuplicateKeys() {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification on prevent duplicate and invalid mappings");


		DatabaseMethods db= new DatabaseMethods(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3389_1][1/4]","verify ButtonID and Mouse Event defined.");

		//Verification on prevent duplicate and invalid mappings
		try{

			db.addActionInEventMap("5", ViewerPageConstants.CINE);
		}catch(SQLException e){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3412_1][2/4]","verify The UPDATE statement conflicted with the FOREIGN KEY.");
			db.assertTrue(e.getMessage().contains(NSDBDatabaseConstants.FOREIGNS_KEY_CONSTRAINT), "verifying Forgeing key constraint", "verified");
		}
		try{
			db.addActionInEventMap("1", ViewerPageConstants.CINE);
		}catch(SQLException e){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3412_2][3/4]","verify Violation of uniquekey constraint.");
			db.assertTrue(e.getMessage().contains(NSDBDatabaseConstants.UNIQUE_KEY_CONSTRAINT), "verifying Unique key constraint", "verified");

		}
		try{
			db.addActionInEventMap("2", ViewerPageConstants.SCROLL);
		}catch(SQLException e){

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[TC3412_3][4/4]","verify Violation of Primary key constraint 'PK_EventTyepe_.");
			db.assertTrue(e.getMessage().contains(NSDBDatabaseConstants.PRIMARY_KEY_CONSTRAINT), "verifying primary key constraint", "verified");
		}



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "dbConfig", "Negative" })
	public void test03_US880_TC3413_verifyDifferentKeysCombo() throws SQLException, InterruptedException, AWTException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification when different command combinations");

		DatabaseMethods db= new DatabaseMethods(driver);
		//Verification when different command combinations
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verification when different command combinations");
		db.assertTrue(db.deleteActionInEventMap("1"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("2"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("3"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("4"),"Verifying user can delete Action in EventMap","Verified");

		db.addActionInEventMap("1", ViewerPageConstants.WINDOWING);
		db.addActionInEventMap("2", ViewerPageConstants.PAN);
		db.addActionInEventMap("3", ViewerPageConstants.ZOOM);
		db.addActionInEventMap("4", ViewerPageConstants.SCROLL);

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);

		//studyPage = new SinglePatientStudyPage(driver);
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();	
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, -100,-100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1), "Verifying now its panning", "test03_checkpoint1");


	}

	//Required IIS reset for event map table.
	//In same session event is not highlighted even after IIS reset.Need to use new session after DB changes in even map table.
	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "Positive" })
	public void test04_US880_TC3415_verifyScrollHighlightedInRadialMenuWhenMappedLeftMouseButton() throws InterruptedException, AWTException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of radial menu(Scroll) highlighted button with mapped leftMouseButton");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9PatientName, 1);
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
		
		viewerpage.openQuickToolbar(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verify Scroll icon should be highlighted.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL), "Verify Scroll icon should be highlighted", "verified");
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		int currentScrollPos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Validate scroll functionality" );
		viewerpage.assertNotEquals(currentScrollPos,viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying the scroll functionality", "scroll is working fine");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "Positive" })
	public void test05_US880_TC3415_verifyPanHighlightedInRadialMenuWhenMappedLeftMouseButton() throws InterruptedException, AWTException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of radial menu(PAN) highlighted button with mapped leftMouseButton");

		DatabaseMethods db= new DatabaseMethods(driver);
		db.assertTrue(db.deleteActionInEventMap("1"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("2"),"Verifying user can delete Action in EventMap","Verified");
		db.addActionInEventMap("2", ViewerPageConstants.PAN);
		db.addActionInEventMap("1", ViewerPageConstants.SCROLL);
		db.resetIISPostDBChanges();
		
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9PatientName, 1);
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
	
		viewerpage.openQuickToolbar(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verify Pan icon should be highlighted.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verify Pan icon should be highlighted", "verified");
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify Pan icon is selected.");
		viewerpage.openQuickToolbar(1);
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.PAN), "Verifying Pan icon is selected", "Pan icon is selected");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify performing Panning using leftmousedown +drag and verify radial menu icon highlighted.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verify performing Panning using leftmousedown +drag and verify radial menu icon highlighted ", "US880_TC3415_Checkpoint3");
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "Positive" })
	public void test06_US880_TC3415_verifyZoomHighlightedInRadialMenuWhenMappedLeftMouseButton() throws InterruptedException, AWTException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of radial menu highlighted button with mapped leftMouseButton");

		DatabaseMethods db= new DatabaseMethods(driver);
		db.assertTrue(db.deleteActionInEventMap("4"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("2"),"Verifying user can delete Action in EventMap","Verified");
		db.addActionInEventMap("2", ViewerPageConstants.ZOOM);
		db.addActionInEventMap("4", ViewerPageConstants.SCROLL);
		db.resetIISPostDBChanges();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9PatientName, 1);
		
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));

		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel = viewBoxPanel.getZoomValue(1);
		
		viewerpage.openQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verify Zoom icon should be highlighted.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.ZOOM), "Verify Zoom icon should be highlighted", "verified");
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		tool=new ViewerToolbar(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]", "Verify Zoom icon is selected.");
		viewerpage.assertTrue(tool.checkCurrentSelectedIconOnViewer(ViewerPageConstants.ZOOM),"Verifying Zoom icon is selected", "Zoom icon is selected");
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0,100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verify Zoom Level after Mouse Up Increase in View Box 1.");
		viewerpage.assertTrue(viewBoxPanel.getZoomValue(1) > intialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel + " to "+ viewBoxPanel.getZoomLevelValue(1));

	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "Positive" })
	public void test07_US880_TC3415_verifyWindowingHighlightedInRadialMenuWhenMappedLeftMouseButton() throws InterruptedException, AWTException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of radial menu highlighted button with mapped leftMouseButton");

		loginPage = new LoginPage(driver);
		DatabaseMethods db= new DatabaseMethods(driver);
		db.assertTrue(db.deleteActionInEventMap("3"),"Verifying user can delete Action in EventMap","Verified");
		db.assertTrue(db.deleteActionInEventMap("2"),"Verifying user can delete Action in EventMap","Verified");
		db.addActionInEventMap("2", ViewerPageConstants.WINDOWING);
		db.addActionInEventMap("3", ViewerPageConstants.SCROLL);

		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9PatientName, 1);
		
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
	
		viewerpage.openQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verify Windowing icon should be highlighted.");
		viewerpage.assertTrue(viewerpage.checkCurrentSelectedIcon(ViewerPageConstants.WINDOW_LEVEL), "Verify Windowing icon should be highlighted", "verified");
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Validate WW/WL functionality" );
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verify the WW/WL from Context Menu", liver9PatientName+"_Viewbox1_WWWL");

	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "US880", "Negative" })
	public void test08_US880_TC3518_VerifyDefaultActionsPerformWhenActionNameWrong() throws InterruptedException, AWTException, SQLException, IOException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying default actions perform when action name wrong");

		//delete existing and add new ButtonID's with spelling mistakes
		DatabaseMethods db= new DatabaseMethods(driver);
		db.truncateActionInEventMap();
		db.addActionInEventMap("1", "Pann");
		db.addActionInEventMap("2", "Windowing");
		db.addActionInEventMap("3", "Scrolli");
		db.addActionInEventMap("4", "Zoom");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(liver9PatientName, 1);
		
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));

		//User should be performed scrolling, zooming,Panning, windowing as per default value , not as per assigned actions in Input but as Default actions

		int currentScrollPos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify performing scrolling, zooming,Panning, windowing as per default value" );
		viewerpage.assertNotEquals(currentScrollPos,viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying the scroll functionality", "scroll is working fine");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]","Verify DB-> dbo.Log All viewer actions are not configured in dbo");
		viewerpage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.INVALID_CONFIGURATION_ERROR_MSG+"%").contains(NSDBDatabaseConstants.INVALID_ACTION_NAME_ERROR), "Verify DB-> dbo.Log All viewer actions are not configured in dbo", "verified");
		//delete existing and add new ButtonID's with missing action
		db.truncateActionInEventMap();
		db.addActionInEventMap("1", ViewerPageConstants.PAN);
		db.addActionInEventMap("2", ViewerPageConstants.ZOOM);
		db.addActionInEventMap("3", ViewerPageConstants.SCROLL);

		loginPage.refreshWebPage();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));;

		viewerpage.waitForViewerpageToLoad();
		viewerpage.doubleClickOnViewport(viewerpage.getViewPort(1));
		//User should be performed scrolling, zooming,Panning, windowing as per default value , not as per assigned actions in Input but as Default actions
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel = viewBoxPanel.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0, 0, 100,-50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]","Verify performing scrolling, zooming,Panning, windowing as per default value");
		viewerpage.assertFalse(viewBoxPanel.getZoomValue(1) != intialZoomLevel,"Verifying zoom level percentange","The Zoom level after Mouse Up increases from "+ intialZoomLevel + " to "+ viewBoxPanel.getZoomLevelValue(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]","Verify DB-> dbo.Log All viewer actions are not configured in dbo");
		viewerpage.assertTrue(db.getFullMessageFromLogContainsString(NSDBDatabaseConstants.INVALID_CONFIGURATION_ERROR_MSG+"%").contains(NSDBDatabaseConstants.INVALID_ACTION_NAME_ERROR), "Verify DB-> dbo.Log All viewer actions are not configured in dbo", "verified");

	}

	

	

	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {
		//reverting all configSetting
		DatabaseMethods db= new DatabaseMethods(driver);
		db.truncateActionInEventMap();
		for(String key: NSDBDatabaseConstants.ACTION_BUTTONID.keySet())
			db.addActionInEventMap(key, NSDBDatabaseConstants.ACTION_BUTTONID.get(key));
		
		db.deleteAllUsers(Configurations.TEST_PROPERTIES.get("nsUserName"));

	}
	
	
}