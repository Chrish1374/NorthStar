package com.trn.ns.test.viewer.basic;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import java.io.IOException;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewboxBGTest extends TestBase{


	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	private LoginPage loginPage;


	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
	
	String ibl =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ibl);
	
	String sr = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String srPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, sr);
	
	String pmap = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmap);


	private HelperClass helper;
	private PatientListPage patientPage;
	private ViewerLayout layout;
	private String loadedTheme;
	private Header header;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws IOException, InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups={"Chrome","IE11","Edge","US2443","Positive","E2E","F1092"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2443_TC10172_verifyingBGColorForUser(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the updated viewer background color is not visible to the user B.");

		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(liver9PatientName, 1);	
		
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage registerUserPage = new RegisterUserPage(driver);

		String newuser = "abc";
		registerUserPage.createNewUser(newuser, newuser,newuser, LoginPageConstants.SUPPORT_EMAIL, newuser, newuser, newuser);
	
				
		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, ViewerPageConstants.TEAL_COLOR);
		db.updateBackgroundColor(newuser, ViewerPageConstants.INDIANRED_COLOR);
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db.updateTheme(ThemeConstants.DARK_THEME_NAME, username);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME, newuser);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else {
			
			db.updateTheme(ThemeConstants.EUREKA_THEME_NAME, username);
			db.updateTheme(ThemeConstants.EUREKA_THEME_NAME, newuser);		
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
			
		}
		
		header = new Header(driver);
		header.logout();
		
		String[] user = {username, newuser};
		for(int i=0 ;i<2;i++) {
			
			loginPage.navigateToBaseURL();
			loginPage.login(user[i],user[i]);
			patientPage = new PatientListPage(driver);

			helper.loadViewerDirectly(johnDoePatientName, 1);	
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for theme "+loadedTheme);
			for(int j=1;j<=viewerPage.getNumberOfCanvasForLayout();j++)
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(j), "Checkpoint[1."+i+"/2]", loadedTheme+"_TC02_1"+j);
			
			layout = new ViewerLayout(driver);
			layout.selectLayout(layout.threeByTwoLayoutIcon);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for theme "+loadedTheme+" after layot change");
			
			for(int j=1;j<=viewerPage.getNumberOfCanvasForLayout();j++)
				viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(j), "Checkpoint[2."+i+"/2]", loadedTheme+"_TC02_2"+j);
		
			header.logout();
			
			
		}
		
		
	
		
		
	}
		
	@Test(groups={"Chrome","IE11","Edge","US2443","Positive","F1092"})
	public void test02_US2443_TC10171_VerifyingBGForThemeAndWWWL() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the updated background color is visible on the viewer for Eureka theme.");
		
		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(liver9PatientName, 1);	
		
		
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/viewer.png";
		String actualImagePath = newImagePath+"/actualImages/viewer.png";
		String diffImagePath = newImagePath+"/diffImages/viewer.png";
	
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), expectedImagePath);
				
		header = new Header(driver);
		header.logout();		
		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, ViewerPageConstants.OLIVE_COLOR);		
		
		helper.loadViewerDirectly(liver9PatientName,username, password, 1);		
		viewerPage.takeElementScreenShot(viewerPage.getViewPort(1), actualImagePath);
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[1/4]","verifying the background change is done");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for viewbox");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer, "Checkpoint[2/4]", "TC01_01");
		
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		layout.waitForPdfToRenderInViewbox(2);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for viewbox which has pdf too after layout change");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer, "Checkpoint[3/4]", "TC01_02");
		
		viewerPage.selectWindowLevelFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(50, 50, 80, 80);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is not changed for viewbox after applying the WWWL");
		viewerPage.compareElementImage(protocolName, viewerPage.viewer, "Checkpoint[4/4]", "TC01_03");
		
		
		
		
	}
	
	@Test(groups={"Chrome","IE11","Edge","US2443","Positive","F1092"})
	public void test03_US2443_TC10173_VerifyingBGForThumbnail() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the thumbnail background displays the updated color as of viewer background");
		
		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(liver9PatientName, 1);	
		
		SimpleLine line = new SimpleLine(driver);
		line.selectLineFromQuickToolbar(1);
		
		line.drawLine(1, -100, -100, -200, 0);
		
		OutputPanel panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);
						
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/thumbnail.png";
		String actualImagePath = newImagePath+"/actualImages/thumbnail.png";
		String diffImagePath = newImagePath+"/diffImages/thumbnail.png";	
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(0), expectedImagePath);
				
		header = new Header(driver);
		header.logout();		
		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, ViewerPageConstants.AQUA_COLOR);		
		
		helper.loadViewerDirectly(liver9PatientName,username, password, 1);		
		panel.waitForOutputPanelToLoad();
		viewerPage.takeElementScreenShot(panel.thumbnailList.get(0), actualImagePath);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for thumbnail");
		boolean cpStatus =  viewerPage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerPage.assertFalse(cpStatus, "Checkpoint[1/2]","Verifying that thumnail is not matching after background change");		
		viewerPage.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[2/2]", "TC03_01");
		
					
	}
	
	@Test(groups={"Chrome","IE11","Edge","US2443","Negative","F1092","E2E"})
	public void test04_US2443_TC10349_VerifyingBGColorValue() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to change the viewer background with adding texts Eureka/Black in the database.");
		
		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(liver9PatientName, 1);	
								
		db = new DatabaseMethods(driver);
		
		String[] background = {ThemeConstants.EUREKA_THEME_NAME, ThemeConstants.DARK_THEME_NAME,"abc","","#abcd1234",ThemeConstants.EUREKA_THEME_NAME.toUpperCase(), ThemeConstants.DARK_THEME_NAME.toUpperCase()};
		
		for(int i = 0 ;i<background.length;i++)
		{
			db.updateBackgroundColor(username,background[i]);
			header = new Header(driver);
			header.logout();	
			
			loginPage.navigateToBaseURL();
			loginPage.login(username,password);
			patientPage = new PatientListPage(driver);			
			helper.loadViewerDirectly(liver9PatientName, 1);		
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color when background color is set in db = "+background);
			
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint[1."+i+"/4]", "TC4"+i+"_01"+i);
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(2), "Checkpoint[2."+i+"/4]", "TC4"+i+"_02"+i);
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(3), "Checkpoint[3."+i+"/4]", "TC4"+i+"_03"+i);
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(4), "Checkpoint[4."+i+"/4]", "TC4"+i+"_04"+i);
		}
		
					
	}
	
	@Test(groups={"Chrome","IE11","Edge","US2443","Positive","F1092","E2E"})
	public void test05_US2443_VerifyingSRReport() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("No Test Case present");
		
		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectly(srPatientName, 1);
		viewerPage.closeNotification();
		viewerPage.assertEquals(viewerPage.getBackgroundColor(viewerPage.getViewPort(1)),ViewerPageConstants.BLACK_COLOR,"Checkpoint[1/2]","verifying the SR report has default black background");
		
		
		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, ViewerPageConstants.AQUA_COLOR);		
		header = new Header(driver);
		header.logout();			
		helper.loadViewerDirectly(srPatientName,username, password, 1);		
		viewerPage.assertEquals(viewerPage.getHexColorValue(viewerPage.getBackgroundColor(viewerPage.getViewPort(1))),ViewerPageConstants.AQUA_COLOR,"Checkpoint[2/2]","verifying the background color is also changed for SR report");
	
					
	}
	
	
	@Test(groups={"Chrome","IE11","Edge","US2443","Positive","E2E","F1092"})
	public void test06_US2443_TC10187_verifyingPMAP() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]: Verify that PMAP LUT bar is not impacted due to the viewer background color changes.");
		
		helper =new HelperClass(driver);
		viewerPage=helper.loadViewerDirectlyUsingID(pmapPatientID, 1);
		viewerPage.closeNotification();
		
		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, ViewerPageConstants.AQUA_COLOR);		
		header = new Header(driver);
		header.logout();			
		helper.loadViewerDirectlyUsingID(pmapPatientID,username, password, 1);		
		viewerPage.closeNotification();
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the background color is changed for PMAP data");
		viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1), "Checkpoint[1/1]", "TC06");
		
					
	}

	
	
	
	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.updateBackgroundColor(username, "");
	}


}

