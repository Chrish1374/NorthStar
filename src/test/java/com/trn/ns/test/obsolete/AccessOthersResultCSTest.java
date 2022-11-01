/*package com.trn.ns.test.obsolete;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector_old;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class AccessOthersResultCSTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector_old contentSelector;
	private HelperClass helper;

	
	private String dxfilePath = Configurations.TEST_PROPERTIES.get("DX_D55R573B101_filepath");
	private String dxPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, dxfilePath);

	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String GSPS_PatientName_Aidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);
	
	private String Liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Liver9filePath);

	String userA = "userA";
	String userB = "userB";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);	
	}
	
	
	@Test(groups = { "Chrome","US784","Positive","BVT"})
	public void test01_US784_TC2880_TC2884_verifyOtherUserAccessOriginalSeriesAndResults() throws  InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("	Verify that user A should be able to see the original results, his own results and other users results"
				+ "<br> Verify that when user logs into application then annotation created by same user should be loaded in viewer");
		
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);
		
		patientPage.clickOntheFirstStudy();
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		contentSelector = new ContentSelector_old(driver); 
		
		List<HashMap<String, String>> beforeResults = contentSelector.getAllResultDetails();
		
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
	    
	    loginPage.logout();
		loginPage.navigateToBaseURL();
			    
	    loginPage.login(userA,userA);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);
		
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
	
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		
        lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 100, -50, 40, -50);
		
		circle.closingWarningAndWaterMark();
		List<HashMap<String, String>> afterResults = contentSelector.getAllResultDetails();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/4]", "Checkpoint 4: Verify that user A should be able to see the original results and other users results");
		
		contentSelector.assertEquals(afterResults.size(), (beforeResults.size()+1), "Verifying the results have one more additional entry","Verified" );
		
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.NAME), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.NAME), "Verifying the non-edited copy of result's name", "Verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.STATEICON), ViewerPageConstants.NG_STAR_INSERTED, "Verifying the non-edited copy of result's state is blank as not loaded", "Verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.OWNER), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.OWNER), "Verifying the non-edited copy of result's created by owner", "Verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/4]", "Checkpoint 5: Verify that user A should be able to see his own results");

		verifyClonesStateInContentSelector(afterResults.size()-2,userA,false);
		
		// Test Case  TC2884
		loginPage.logout();
		loginPage.navigateToBaseURL();
			    
	    loginPage.login(userA,userA);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(GSPS_PatientName_Aidoc);
		
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/4]", "Verify that when user logs into application then annotation created by same user should be loaded in viewer");
		circle.assertEquals(circle.getAllCircles(2).size(),1,  "Verifying the circle annotation presence", "Verified");
		ellipse.assertEquals(ellipse.getAllEllipses(2).size(),1,  "Verifying the ellipse presence", "Verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,  "Verifying the measurement presence", "Verified");
		
		afterResults = contentSelector.getAllResultDetails();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/4]", "Verify that when user logs into application then annotation created by same user should be loaded in viewer");
		
		verifyClonesStateInContentSelector(afterResults.size()-2,userA,false);

		
		
		
	}
	
	@Test(groups = { "Chrome","US784","Positive"})
	public void test02_US784_TC2903_TC2904_TC2905_TC2906_verifySameUserAccessOriginalSeriesAndResults() throws  InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when second user logs into application first time then annotation created by first user should be loaded in viewer [in second user's copy]"
				+ "<br> Verify that when second user logs into application and create new annotation then new annotation should be added in its own copy"
				+ "<br> Verify that when first user (user A) logs into application then copy of same user should be loaded in viewer which is just duplicate of original (as there no new annotation created by any user)"
				+ "<br> Verify that when first user (user a) logs into application after second or other user then latest annotation drawn by last user should be displayed");
		
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(liver9PatientName);
		
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		contentSelector = new ContentSelector_old(driver); 
		
		// Creating annotations using scan user
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		
		ellipse.closingWarningAndWaterMark();
        lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(2, -70, -80, -120, 90);
		
		ellipse.closingWarningAndWaterMark();
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(2, -100, -50, 40, -50);
		
		List<HashMap<String, String>> beforeResults = contentSelector.getAllResultDetails();
		
		viewerpage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
	    registerUserPage.createNewUser(userB, userB, LoginPageConstants.SUPPORT_EMAIL, userB, userB, userB);
	    
	    loginPage.logout();
		loginPage.navigateToBaseURL();
			    
		// login using userA
	    loginPage.login(userA,userA);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(liver9PatientName);
		
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/5]", "Verify that when second user logs into application first time then annotation created by same user should be loaded in viewer");
		
		List<HashMap<String, String>> afterResults = contentSelector.getAllResultDetails();
		
		contentSelector.assertEquals(afterResults.size(), (beforeResults.size()), "Verifying results copies are same as UserA just loaded user scan copy", "Verified");
		
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.NAME), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.NAME), "Verifying the clone copy name is same as scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.OWNER), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.OWNER), "verifying the create by field is same as scan copy", "verified");
		
		verifyClonesStateInContentSelector(afterResults.size()-1,username,false);
		
		circle.assertEquals(circle.getAllCircles(1).size(),1,  "verifying circle annotation presence", "verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1,  "verifying ellipse presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,  "Verifying measurement presence", "verified");
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "verifying the number of layout", "verified");
		

//		Verify that when second user logs into application and create new annotation then new annotation should be added in its own copy		
	    
	    loginPage.logout();
		loginPage.navigateToBaseURL();
			    
	    loginPage.login(userB,userB);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(liver9PatientName);
		
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/5]", "Verify that when second user logs into application and doesn't create new annotation then latest copy should be loaded");
		afterResults = contentSelector.getAllResultDetails();		
		contentSelector.assertEquals(afterResults.size(), (beforeResults.size()), "Verifying results copies are same as UserA just loaded user scan copy", "Verified");
		
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.NAME), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.NAME), "Verifying the clone copy name is same as scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-1).get(ViewerPageConstants.OWNER), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.OWNER), "verifying the create by field is same as scan copy", "verified");
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "verifying the number of layout", "verified");
		verifyClonesStateInContentSelector(afterResults.size()-1,username,false);
		
		circle.assertEquals(circle.getAllCircles(1).size(),1,  "verifying circle annotation presence", "verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1,  "verifying ellipse presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,  "Verifying measurement presence", "verified");
		
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "verifying the number of layout", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/5]", "Verify that when second user logs into application and create new annotation then new annotation should be added in its own copy");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 200, 100, 70, 70);
		
		circle.assertEquals(circle.getAllCircles(1).size(),2,  "verifying circle annotation presence", "verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1,  "verifying ellipse presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,  "Verifying measurement presence", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[4/5]", "GSPS result should be displayed which is created by user B");
		
		afterResults = contentSelector.getAllResultDetails();		
		contentSelector.assertEquals(afterResults.size(), (beforeResults.size()+1), "Verifying results copies are 1 greater than UserA ", "Verified");	
		
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.NAME), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.NAME), "Verifying the clone copy name is same for scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.OWNER), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.OWNER), "verifying the create by field is same for scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.STATEICON), ViewerPageConstants.NG_STAR_INSERTED, "Verifying that Scan copy is not currently loaded", "Verified");
		
		verifyClonesStateInContentSelector(afterResults.size()-1,userB,false);
		

		//Verify that when first user (user A) logs into application then copy of same user should be loaded in viewer which is just duplicate of original (as there no new annotation created by any user)
		//Verify that when first user (user a) logs into application after second or other user then latest annotation drawn by last user should be displayed
		loginPage.logout();
		loginPage.navigateToBaseURL();
			    
	    loginPage.login(userA,userA);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(liver9PatientName);
		
		patientPage.clickOntheFirstStudy();	
		viewerpage.waitForViewerpageToLoad();	
		
		circle.assertEquals(circle.getAllCircles(1).size(),2,  "verifying circle annotation presence", "verified");
		ellipse.assertEquals(ellipse.getAllEllipses(1).size(),1,  "verifying ellipse presence", "verified");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,  "Verifying measurement presence", "verified");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[5/5]", "Post reloging by user A latest copy is loaded in viewer i.e. UserB's copy");
		afterResults = contentSelector.getAllResultDetails();		
		contentSelector.assertEquals(afterResults.size(), (beforeResults.size()+1), "Verifying results copies are 1 greater than UserA ", "Verified");	
		
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.NAME), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.NAME), "Verifying the clone copy name is same for scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.OWNER), beforeResults.get(beforeResults.size()-1).get(ViewerPageConstants.OWNER), "verifying the create by field is same for scan copy", "verified");
		contentSelector.assertEquals(afterResults.get(afterResults.size()-2).get(ViewerPageConstants.STATEICON),ViewerPageConstants.NG_STAR_INSERTED, "Verifying that Scan copy is not currently loaded", "Verified");
		
		verifyClonesStateInContentSelector(afterResults.size()-1,userB,false);

		
	}
	
	@Test(groups = { "Chrome","US784","Positive"})
	public void test03_US784_TC2909_verifyLatestCopyIsLoaded() throws  InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Data Specific : DX_2results_Updated: Verify that when user logs into application and create new annotation then in next login latest should be display");
		
		patientPage = new PatientListPage(driver);
		
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
	    RegisterUserPage registerUserPage = new RegisterUserPage(driver);
	    registerUserPage.waitForRegisterPageToLoad();
	    registerUserPage.createNewUser(userA, userA, LoginPageConstants.SUPPORT_EMAIL, userA, userA, userA);
	    
	    loginPage.logout();
		loginPage.navigateToBaseURL();
			    
	    loginPage.login(userA,userA);
	    patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(dxPatientName);
		
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		
		contentSelector = new ContentSelector_old(driver); 
		
		List<String> results = contentSelector.getAllResultDesciptionFromContentSelector(1);
		
		contentSelector.selectResultFromContentSelector(1, results.get(0));
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		
        lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -100, -50, 40, -50);
				
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		contentSelector.selectResultFromContentSelector(2, results.get(1));
		
		int[] coordinates = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolylineFromQuickToolbar(2);
		poly.drawFreehandPolyLine(2, coordinates);
		
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(dxPatientName, 1, 1);
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/1]", "The latest drawn annotation displayed in viewer ( here, Polyline and two circles)");
		
		circle.assertEquals(circle.getAllCircles(1).size(),2,  "Verifying the latest copy has two circles", "Verified");
		poly.assertEquals(poly.getAllPolylines(1).size(),1,  "Verifying the latest copy has one polyline", "Verified");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Verifying the layout is one x one", "Verified");
		
		viewerpage.assertTrue(contentSelector.validateResultIsSelectedOnContentSelector(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+userA+"_2"), "Verifying the user created result is loaded in viewer", "Verified");
		viewerpage.assertFalse(contentSelector.validateResultIsSelectedOnContentSelector(1,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+userA+"_1"), "Verifying the user created result is not loaded in viewer", "Verified");
		viewerpage.assertFalse(contentSelector.validateResultIsSelectedOnContentSelector(1,results.get(0)), "Verifying the machine result is not loaded in viewer", "Verified");
		viewerpage.assertFalse(contentSelector.validateResultIsSelectedOnContentSelector(1,results.get(1)), "Verifying the machine result is not loaded in viewer", "Verified");
		
	}

	public boolean verifyGSPSObjectPresence(int whichViewbox){
		int count = 0;
		boolean status = false ;

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		SimpleLine line = new  SimpleLine(driver); 

		count = line.getLinesOrPoints(whichViewbox, true, false).size() + circle.getAllCircles(whichViewbox).size() + ellipse.getEllipses(whichViewbox).size() 
				+ textAn.getTextAnnotations(whichViewbox).size() + point.getAllPoints(whichViewbox).size();

		if(count>0)
			status = true ;

		return status;
	}
	
	@AfterMethod(alwaysRun=true)
	public void deleteData() throws SQLException {
		
		db = new DatabaseMethods(driver);
		db.deleteDrawnAnnotation(userA);
		db.deleteDrawnAnnotation(userB);
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void deleteUsers() {

		DatabaseMethods db = new DatabaseMethods(driver);
		try {
			if(db.checkUserPresence(userA))
				db.deleteUser(userA);
			if(db.checkUserPresence(userB))
				db.deleteUser(userB);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void verifyClonesStateInContentSelector (int resultNumber, String userName,boolean machine) {
		
		contentSelector = new ContentSelector_old(driver);
		
		List<HashMap<String, String>> afterResults = contentSelector.getAllResultDetails();
		
		if(machine)
			contentSelector.assertEquals(afterResults.get(resultNumber).get(ViewerPageConstants.NAME), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+userName+"_1", "user "+userName+" created copy is loaded", "Verified");
		else
			contentSelector.assertEquals(afterResults.get(resultNumber).get(ViewerPageConstants.NAME), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+userName+"_1", "User "+userName+" created copy is loaded", "Verified");
		
		contentSelector.assertEquals(afterResults.get(resultNumber).get(ViewerPageConstants.OWNER), ViewerPageConstants.CREATED_BY+" "+userName, "Verifying the created by field", "Verified");
		contentSelector.assertTrue(afterResults.get(resultNumber).get(ViewerPageConstants.STATEICON).contains(ViewerPageConstants.PR_HIGHLIGHT), "verifying the user "+userName+" copy is loaded", "Verified");
	
		
		
	}

}



*/