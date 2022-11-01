//package com.trn.ns.test.obsolete;
//
//import java.util.List;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class AboutIconTest extends TestBase{
//
//	//String protocolName;
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//	String attribute = "src";	
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "US1165", "Sanity", "Positive"})
//	public void test03_US1165_TC5720_US1373_TC7342_verifyCEMarkingPageContent() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the CE mark of the Northstar application is updated as of PI7 - 2019");
//
//		loginPage = new LoginPage(driver);
//		String parentWindow = loginPage.getCurrentWindowID();
//		loginPage.click(loginPage.iIconButton);
//		loginPage.click(loginPage.ceMarkingLink);
//		List<String> childWinHandles = loginPage.getAllOpenedWindowsIDs();
//		childWinHandles.remove(parentWindow);
//		loginPage.switchToWindow(childWinHandles.get(0));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "CE marking page should open in another tab.");
//		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(NSConstants.CEMARKING_PAGE_URL), "CE marking page should open in another tab", "Verified");
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "CE marking page contents");
//		loginPage.compareElementImage(protocolName, loginPage.ceMarkingImg,"Verifying CE marking page contents","test03_01");
//
//	}
//
//}
