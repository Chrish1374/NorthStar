package com.trn.ns.test.viewer.basic;

import java.util.HashMap;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class KeyboardEventsTest extends TestBase {	
	
	
	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;

	//Loading the patient on viewer
	private String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String AH4 =Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4);
	
	String TDAMaps =Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String TDAMaps_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TDAMaps);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	private ViewerPage viewerPage;

	
	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientListPage = new PatientListPage(driver);
		
	}	

	@Test(groups ={"Chrome","Edge","IE11","US178","US1411","Positive"})
	public void test00_US178_TC365_US1411_TC7678_verifyKeyboardShortcutsFromDB() throws InterruptedException{
		
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Keyboard Shortcuts. <br>"+
		"Verify that configurations are set in ApplicationShortcuts table in NSDB databasefor A, R, P button shortcut[NSDB upgrade scenario]");
		
		// Begin on the Patient List Page, select the patient Data.
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(PatientName, 1);
	
		db = new DatabaseMethods(driver);
		
		HashMap<String, String> keyboardShortcuts = db.getKeyboardShortcutsFromDB();		
		
		viewerPage.assertEquals(keyboardShortcuts.size(), ViewerPageConstants.KEYBOARD_SHORTCUTS.size(), "Verifying the keyboard shortcut size", "Verified");
		
		Set<String> keys = ViewerPageConstants.KEYBOARD_SHORTCUTS.keySet();
		
		for(String key : keys){
			
			viewerPage.assertTrue(keyboardShortcuts.containsKey(key),"verifying the keyboard shortcut <b>" +key + "</b> presence", "verified");
			viewerPage.assertEquals(keyboardShortcuts.get(key),ViewerPageConstants.KEYBOARD_SHORTCUTS.get(key),"verifying the keyboard  shortcut <b>" +key+"</b> value <b>"+keyboardShortcuts.get(key)+"</b>", "verified");
		}
		
			
		
		
		
		
	}
	
	@Test(groups ={"firefox","Chrome","Edge","IE11","US178","DE1874","Sanity"})
	public void test01_US178_TC366_DE1874_TC7503_verifyKeyboardShortcutsCine() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Apply Keyboard shortcuts-Cine. <br>"+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");
		// Select the specific Patient Name to go to the Single Patient Study List for the Patient

		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(PatientName, 1);
	

		viewerPage.mouseHover(viewerPage.getViewPort(1));

		viewerPage.playOrStopCineUsingKeyboardCKey();

		String position = viewerPage.getCurrentScrollPosition(1);

		viewerPage.waitForTimePeriod(300);
		// purposely waiting for some time
		viewerPage.playOrStopCineUsingKeyboardCKey();
	
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(1),position ,"Verifying the Cine performed using Keyboard C key","Cine is working fine");


	}

	@Test(groups ={"firefox","Chrome","Edge","US178","IE11","DE1874","Sanity"})
	public void test02_US178_TC422_DE1874_TC7503_verifyKeyboardShortcutsWindowLeveling() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Apply Keyboard shortcuts-Window Level. <br> "+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(PatientName, 1);
	

		String currentWidth = viewerPage.getWindowWidthValueOverlayText(1);
		String currentCenter = viewerPage.getWindowCenterValueOverlayText(1);

		viewerPage.enableOrDisableWWWLUsingKeyboardWKey();		
		viewerPage.dragAndReleaseOnViewerWithoutHop(viewerPage.getViewPort(1),0, 0, 100, 50);		

		viewerPage.assertNotEquals(viewerPage.getWindowWidthValueOverlayText(1),currentWidth ,"Verifying the WW/WL(width) performed using Keyboard W key","WW/WL is working fine");
		viewerPage.assertNotEquals(viewerPage.getWindowCenterValueOverlayText(1),currentCenter ,"Verifying the WW/WL(center) performed using Keyboard W key","WW/WL is working fine");


	}

	@Test(groups ={"firefox","Chrome","Edge","IE11","Sanity"})
	public void test03_US178_TC425_TC426_verifyScrollUsingUpAndDownArrow() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Apply Keyboard shortcuts-Scroll Forward \n Apply Keyboard shortcuts-Scroll Reverse");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(PatientName, 1);
	

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		int position = Integer.parseInt(viewerPage.getCurrentScrollPosition(1));

		viewerPage.scrollDownToSliceUsingKeyboard(1);

		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),position+1,"Verifying the forward scroll using up key","Scroll is working fine");

		viewerPage.scrollUpToSliceUsingKeyboard(1);

		viewerPage.assertEquals(Integer.parseInt(viewerPage.getCurrentScrollPosition(1)),position,"Verifying the backward scroll using up key","Scroll is working fine");

	}

	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test04_DE159_TC677_verifyKeyboardShortcutsNotBlocksOtherFlows() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Keyboard shortcuts block keys on other pages");
		// Select the specific Patient Name to go to the Single Patient Study List for the Patient

		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(PatientName, 1);
	

		viewerPage.mouseHover(viewerPage.getViewPort(1));

		viewerPage.playOrStopCineUsingKeyboardCKey();
		String position = viewerPage.getCurrentScrollPosition(1);
		viewerPage.waitForTimePeriod(1000);
		viewerPage.playOrStopCineUsingKeyboardCKey();
	
		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPosition(1),position ,"Verifying the Cine performed using Keyboard C key","Cine is working fine");
		viewerPage.browserBackWebPage();
		patientListPage = new PatientListPage(driver);
		loginPage.refreshWebPage();
		loginPage.waitForEndOfAllAjaxes();
		loginPage.login(username,password);
		patientListPage.waitForPatientPageToLoad();
		patientListPage.assertTrue(patientListPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),"Verifying the no other flow is blocked","Verified");

		
	}
	

	
	

}
