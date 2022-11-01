package com.trn.ns.test.basic;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.*;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class EurekaHeaderTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private PasswordPolicyPage passwordPolicyPage;
	private Header header;
	private ViewerPage viewerpage;
	private ExtentTest extentTest ;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password= Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
	}

	@Test(groups ={ "Chrome", "IE11", "Edge","US1814","Positive","F931","E2E"})
	public void test01_US1814_TC8558_TC8561_verifyEurekaLogo() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new name \"Eureka\" and logo on 'Register/Add User' and 'Users' page."
				+ "<br> Verify the new name \"Eureka\" and logo on 'PatientList' and 'PatientStudyList' page.");	
		
		//Navigated to Patient list screen
		patientPage = new PatientListPage(driver);
		header = new Header(driver);
		header.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[1/10]","Verify Eureka logo image on Patient page");
		
		header.resizeBrowserWindow(300, 500);
		patientPage.click(patientPage.patientNamesList.get(0));
		patientPage.click(patientPage.studyRows.get(0));	
		
		//Navigate to viewer screen
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		header.assertTrue(header.isEurekaLogoPresent(1), "Checkpoint[3/10]","Verify Eureka logo image on viewer page when window is small");
		
		header.maximizeWindow();		
		header.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[4/10]","Verify Eureka logo image on viewerpage when browser is maximized");

		//Navigate to password policy screen
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.PASSWORD_POLICY_URL);
		passwordPolicyPage = new PasswordPolicyPage(driver);
		header.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[7/10]","Verify Eureka logo image on password policy page");
		
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		header.assertTrue(header.isEurekaProductNamePresent(),"Checkpoint[8/10]","Verify Eureka logo image on register page");
		
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.USER_PAGE_URL);
		header.assertTrue(header.isEurekaProductNamePresent(),"Checkpoint[9/10]", "Verify Eureka logo image on users page");
		
	
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.ACTIVE_DIRECTORY_PAGE_URL);
		header.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[10/10]","Verify Eureka logo image on active directory page");
		
	}	


	//DR2224: Hovering radial menu buttons changes Eureka logo highlight
	@Test(groups ={ "Chrome", "IE11", "Edge","DR2224","Positive","F931","E2E"})
	public void test04_DR2224_TC8961_verifyEurekaLogoWhenMouseHoverOnRadialMenuIcons() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify hovering on radial menu buttons should not change Eureka logo highlight");
		
		patientPage = new PatientListPage(driver);
		//patientPage.clickOnPatientRow(patientPage.getPatientName(0));
		patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(0)));

		//patientstudypage = new SinglePatientStudyPage(driver);
		patientPage.clickOntheFirstStudy();		
	
		PointAnnotation pointAn = new PointAnnotation(driver);
		pointAn.waitForViewerpageToLoad();

		header = new Header(driver);
		header.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[1/25]","Verified Eureka product name on viewerpage when browser is maximized");
		
		pointAn.openQuickToolbar(pointAn.getViewPort(1));
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.distanceIcon), "Checkpoint[2/25]","Verified Eureka product name not highlighted when when mousehover on distance icon.");
		
		Triangulation tool=new Triangulation(driver);
		tool.assertTrue(header.verifyEurekaProductNameOnMouseHover(tool.traingulationIcon), "Checkpoint[3/25]","Verified Eureka product name is not highlighted when mousehover on traingulation icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.windowLevelingIcon), "Checkpoint[4/25]","Verified Eureka product name is not highlighted when mousehover on WWWC icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.zoomIcon), "Checkpoint[5/25]","Verified Eureka product name is not highlighted when mousehover on zoom icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.scrollIcon), "Checkpoint[6/25]","Verified Eureka product name is not highlighted when mousehover on scroll icon.");
		
		
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.pointIcon), "Checkpoint[7/25]","Verified Eureka product name is not highlighted when mousehover on point annotation icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.cinePlayIcon), "Checkpoint[8/25]","Verified Eureka product name is not highlighted when mousehover on cine play icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.textArrowIcon), "Checkpoint[9/25]","Verified Eureka product name is not highlighted when mousehover on text annotation icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.polylineIcon), "Checkpoint[10/25]","Verified Eureka product name is not highlighted when mousehover on polyline icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.cine4DPlayIcon), "Checkpoint[11/25]","Verified Eureka product name is not highlighted when mousehover on cine 4D icon.");
		pointAn.assertTrue(header.verifyEurekaProductNameOnMouseHover(pointAn.circleIcon), "Checkpoint[12/25]","Verified Eureka product name is not highlighted when mousehover on circle.");
		pointAn.assertTrue(header.isEurekaProductNamePresent(), "Checkpoint[13/25]","Verified Eureka product name on viewerpage is not highlighted when basic tab is open.");
		
	}	
}

