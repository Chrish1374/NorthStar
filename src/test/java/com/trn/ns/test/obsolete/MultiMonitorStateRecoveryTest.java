package com.trn.ns.test.obsolete;
//package com.terarecon.northstar.test.obsolete;
//
//import static com.terarecon.northstar.test.configs.Configurations.TEST_PROPERTIES;
//
//import com.terarecon.northstar.test.base.TestBase;
//import com.terarecon.northstar.test.configs.Configurations;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import com.terarecon.northstar.page.factory.LoginPage;
//import com.terarecon.northstar.page.factory.MultimonsetPage;
//import com.terarecon.northstar.page.factory.StudyListPage;
//import com.terarecon.northstar.utilities.ExtentManager;
//
//
//public class MultiMonitorStateRecoveryTest extends TestBase  {
//
//	private LoginPage loginPage;
//	private MultimonsetPage MultimonsetPage;
//	private StudyListPage studyPage;
//
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//		
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		MultimonsetPage = new MultimonsetPage(driver);
//		studyPage = new StudyListPage(driver) ;
//	}
//	
//	// TC153: Verify 'Login' and 'Advanced' button on Login page.
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_US158_TC_153_verifyAdvancedAndloginButton() throws InterruptedException {
//		
//		extentTest = ExtentManager.setTestName("US158_TC_153", "Verify 'Login' and 'Advanced' button on Login page");
//		loginPage = new LoginPage(driver);
//		
//		// Verifying the Login and advanced button presence
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Validate Login and Advanced Button");
//		
//		loginPage.assertTrue(loginPage.loginButton.isDisplayed(), "Verifying the login button presence", "login button is present");
////		loginPage.assertTrue(loginPage.advancedButton.isDisplayed(), "Verifying the advanced button presence", "advanced button is present");
//		
//		}
//	
//	
//	// TC154: Verify valid login through 'Advanced' button on Login page.
//		@Test(groups ={"firefox","Chrome","Edge","IE11"})
//		public void test02_US158_TC_154_verifyValidLogin() throws InterruptedException {
//		
//			extentTest = ExtentManager.setTestName("US158_TC_154", "Verify valid login through 'Advanced' button on Login page");
//			loginPage = new LoginPage(driver);
//			
//			//Enter a valid user name and  valid password and click on Advanced Button. Verify the user is able to login to the system by clicking Advanced button.
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Enter a valid user name and password and click on Advanced Button");
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//			
//			MultimonsetPage.waitForElementVisibility(MultimonsetPage.proceedButton);
//		}	
//		
//		
//	//TC155: Verify application behavior when 'Do not show it again' checkbox is selected and 'Login' button is selected.
//		@Test(groups ={"firefox","Chrome","Edge","IE11"})
//		public void test03_US158_TC_155_verifyUserNavigationByLoginButton() throws InterruptedException {
//		
//			extentTest = ExtentManager.setTestName("US158_TC_155", "Verify application behavior when 'Do not show it again' checkbox is selected and 'Login' button is selected");
//			loginPage = new LoginPage(driver);
//						
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//						
//			//Navigate to Multimonset page
//			MultimonsetPage = new MultimonsetPage(driver);
//			MultimonsetPage.waitForElementVisibility(MultimonsetPage.proceedButton);
//			//Verify checkbox 'Do not show this page again' is not selected 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate checkbox 'Do not show this page again' is not selected on Multi-Monitor screen");
//			MultimonsetPage.assertFalse(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is not selected", "Checkbox 'Do not show this page again' is not selected");
//			
//			//On selecting checkbox, verify checkbox is selected 
//			MultimonsetPage.checkboxDoNotShowPage.click();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate checkbox 'Do not show this page again' is selected on Multi-Monitor screen");
//			MultimonsetPage.assertTrue(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is selected", "Checkbox 'Do not show this page again' is selected");
//			
//			//On clicking Proceed Button user should navigate to Patient page
//			MultimonsetPage.proceedButton.click();
//			MultimonsetPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate user is navigated to Patient list page");
//			MultimonsetPage.assertTrue(MultimonsetPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ MultimonsetPage.getCurrentPageURL());
//			
//			//Relaunch the application and click on login button
//			loginPage.navigateToBaseURL();
//			loginPage.login("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			
//			//Verify that user is navigate to Patient List page
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "On relaunching application with Login button, Validate that the user is navigate to Patient List screen instead of MultiMonitor screen");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("patient"), "Verifying user is successfully navigate to Patient list screen", "User is on page "+ loginPage.getCurrentPageURL());
//				
//		}	
//		
//	//TC156: Verify presence of ' Setup multi-monitor settings' page when 'Do not show it again' check box is selected and 'Advanced' button is clicked.
//		@Test(groups ={"firefox","Chrome","Edge","IE11"})
//		public void test04_US158_TC_156_verifyUserNavigationByAdvancedButton() throws InterruptedException {
//				
//			extentTest = ExtentManager.setTestName("US158_TC_156", "Verify presence of ' Setup multi-monitor settings' page when 'Do not show it again' check box is selected and 'Advanced' button is clicked");
//			loginPage = new LoginPage(driver);
//							
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//					
//			//Navigate to Multimonset page
//			MultimonsetPage = new MultimonsetPage(driver);
//			MultimonsetPage.waitForElementVisibility(MultimonsetPage.proceedButton);
//			//Verify checkbox 'Do not show this page again' is not selected 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Validate checkbox 'Do not show this page again' is not selected on Multi-Monitor screen");
//			MultimonsetPage.assertFalse(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is not selected", "Checkbox 'Do not show this page again' is not selected");
//			
//			//On selecting checkbox, verify checkbox is selected 
//			MultimonsetPage.checkboxDoNotShowPage.click();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Validate checkbox 'Do not show this page again' is selected on Multi-Monitor screen");
//			MultimonsetPage.assertTrue(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is selected", "Checkbox 'Do not show this page again' is selected");
//			
//			//On clicking Proceed Button user should navigate to Patient page
//			MultimonsetPage.proceedButton.click();
//			MultimonsetPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Validate user is navigated to Patient list page");
//			MultimonsetPage.assertTrue(MultimonsetPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ MultimonsetPage.getCurrentPageURL());
//			
//			//Re launch the application and click on Advanced button
//			loginPage.navigateToBaseURL();
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			
//			//Verify that user is navigate to Multi-Monitor page
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "On relaunching application with Advanced button, Validate that the user is navigate to Multi-Monitor screen instead of Patient list screen");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying user is successfully navigate to Multi-Monitor screen", "User is on page "+ loginPage.getCurrentPageURL());
//					
//		}
//		
//	//TC156: Verify presence of 'Setup multi-monitor settings' page when 'Do not show it again' check box is selected and navigating from Studylist page with 'Login' and 'Advanced' button.
//		@Test(groups ={"firefox","Chrome","Edge","IE11"})
//		public void test05_US158_TC_156_verifyUserNavigationFromStudyListpage() throws InterruptedException {
//						
//			extentTest = ExtentManager.setTestName("US158_TC_156", "Verify presence of ' Setup multi-monitor settings' page when 'Do not show it again' check box is selected and navigating from Studylist screen with 'Login' and 'Advanced' button.");
//			loginPage = new LoginPage(driver);
//									
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//							
//							
//			//Navigate to Multimonset page
//			MultimonsetPage = new MultimonsetPage(driver);
//			MultimonsetPage.waitForElementVisibility(MultimonsetPage.proceedButton);
//			//Verify checkbox 'Do not show this page again' is not selected 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Validate checkbox 'Do not show this page again' is not selected on Multi-Monitor screen");
//			MultimonsetPage.assertFalse(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is not selected", "Checkbox 'Do not show this page again' is not selected");
//			
//			//On selecting checkbox, verify checkbox is selected 
//			MultimonsetPage.checkboxDoNotShowPage.click();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Validate checkbox 'Do not show this page again' is selected on Multi-Monitor screen");
//			MultimonsetPage.assertTrue(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is selected", "Checkbox 'Do not show this page again' is selected");
//			
//			//On clicking Proceed Button user should navigate to Patient page
//			MultimonsetPage.proceedButton.click();
//			MultimonsetPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Validate user is navigated to Patient list page");
//			MultimonsetPage.assertTrue(MultimonsetPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ MultimonsetPage.getCurrentPageURL());
//			
//			//Navigate to Studylist page
//			studyPage = new StudyListPage(driver) ;
//			studyPage.navigateToURL("http://"+TEST_PROPERTIES.get("hostName")+":"+TEST_PROPERTIES.get("port")+"/"+"#/studylist"); 
//			studyPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Validate user is navigated to Studylist page");
//			studyPage.assertTrue(studyPage.getCurrentPageURL().contains("studylist"), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());
//						
//			//Re launch the application and click on Advanced button
//			loginPage.navigateToBaseURL();
//			loginPage.advancedLogin("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			
//			//Verify that user is navigate to Multi-Monitor page
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "On relaunching application with Advanced button, Validate that the user is navigate to Multi-Monitor screen instead of Patient list screen");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying user is successfully navigate to Multi-Monitor screen", "User is on page "+ loginPage.getCurrentPageURL());
//			
//			//On selecting checkbox, verify checkbox is selected 
//			MultimonsetPage.checkboxDoNotShowPage.click();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Validate checkbox 'Do not show this page again' is selected on Multi-Monitor screen");
//			MultimonsetPage.assertTrue(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is selected", "Checkbox 'Do not show this page again' is selected");
//			
//			//On clicking Proceed Button user should navigate to Patient page
//			MultimonsetPage.proceedButton.click();
//			MultimonsetPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Validate user is navigated to Patient list page");
//			MultimonsetPage.assertTrue(MultimonsetPage.getCurrentPageURL().contains("patient"), "Verifying that user is navigated to Patient list page", "User is on page "+ MultimonsetPage.getCurrentPageURL());
//			
//			//Navigate to Studylist page
//			studyPage = new StudyListPage(driver) ;
//			studyPage.navigateToURL("http://"+TEST_PROPERTIES.get("hostName")+":"+TEST_PROPERTIES.get("port")+"/"+"#/studylist"); 
//			studyPage.waitForTimePeriod("Medium");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Validate user is navigated to Studylist page");
//			studyPage.assertTrue(studyPage.getCurrentPageURL().contains("studylist"), "Verifying that user is navigated to study list page", "User is on page "+ studyPage.getCurrentPageURL());
//					
//			
//			//Relaunch the application and click on login button
//			loginPage.navigateToBaseURL();
//			loginPage.login("scan", "scan");
//			loginPage.waitForTimePeriod("Medium");
//			
//			//Verify that user is navigate to Patient List page
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "On relaunching application with Login button, Validate that the user is navigate to Patient List screen instead of MultiMonitor screen");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("patient"), "Verifying user is successfully navigate to Patient list screen", "User is on page "+ loginPage.getCurrentPageURL());
//								
//		}
//		
//		// TC157: Verify presence of ' Setup multi-monitor settings' page when ' Login' button is clicked by the user very first time.
//		@Test(groups ={"firefox","Chrome","Edge","IE11"})
//		public void test06_US158_TC_157_verifyMultiMonitorScreen() throws InterruptedException {
//			
//			extentTest = ExtentManager.setTestName("US158_TC_157", "Verify presence of ' Setup multi-monitor settings' page when ' Login' button is clicked by the user very first time");
//			loginPage = new LoginPage(driver);
//			
//			// Verifying the Login and advanced button presence
//			
//			loginPage.assertTrue(loginPage.loginButton.isDisplayed(), "Verifying the login button presence", "login button is present");
//			loginPage.assertTrue(loginPage.advancedButton.isDisplayed(), "Verifying the advanced button presence", "advanced button is present");
//			loginPage.login("scan", "scan");
//			
//			//Verifying presence of Multi-monitor set up screen when ' Login' button is clicked by the user very first time
//			
//			loginPage.waitForTimePeriod("Medium");
//			loginPage.assertTrue(loginPage.getCurrentPageURL().contains("multimonset"), "Verifying the Successful Login", "User is on page "+ loginPage.getCurrentPageURL());
//			
//			MultimonsetPage = new MultimonsetPage(driver);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Validate presence of text, buttons and checkbox on MultiMonitor screen");
//			
//			MultimonsetPage.assertEquals(MultimonsetPage.setUpMultimonitorsettingslabel.getText(),"Setup multi-monitor settings" , "Verifying the 'Setup multi-monitor settings' label text", "'Setup multi-monitor settings' label text is correct");
//			MultimonsetPage.assertFalse(MultimonsetPage.checkboxDoNotShowPage.isSelected(), "Verifying checkbox 'Do not show this page again' is not selected", "Checkbox 'Do not show this page again' is not selected");
//			MultimonsetPage.assertTrue(MultimonsetPage.proceedButton.isDisplayed(), "Verifying the proceed button presence", "proceed button is present");
//			
//		}
//	
//}
