package com.trn.ns.test.viewer.liasionE2E;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LaisionE2ETest extends TestBase{


	private LoginPage loginPage;
	private ExtentTest extentTest;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private Header header;

	String username =Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	String cstoreLocation = Configurations.TEST_PROPERTIES.get("cstoreFolder");
	String IP =  "10.1.10.70";
	String port = "104";
	String dataFolderLoc = "C:\\DR2455\\US1359\\tda\\Multi-Series";
	String ruleName  = "tda";
	String patientName = "WJ";
	String machineName = "prod/terarecon/tda:4.4.13.P7.804";
	private ContentSelector cs;
	

	@Test
	public void pushData() throws IOException
	{
		pushDataToLiasion(cstoreLocation, ruleName,IP, port, dataFolderLoc);
	}

	@Test(groups ={"DR2455", "Positive","Chrome","BVT","IE","Edge"})
	public void test01_DR2455_TC10028_verifyBatchException() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify multiple 'New Batch Results' notification received in NS for same batch ID NS in V3 - ( When results sent with each notification is unique)");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(),"Checkpoint[1/25]","verifying no patient is present");

		driver.manage().timeouts().implicitlyWait(3,TimeUnit.MINUTES) ;		
		
		patientPage.waitForElementVisibility(patientPage.notificationDiv);
		patientPage.assertEquals(patientPage.getText(patientPage.notificationTitle),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[2/25]","verifying the notification message is displayed upon sending of new study");
		patientPage.assertEquals(patientPage.getText(patientPage.notificationMessage),PatientPageConstants.A_NEW_STUDY+patientName+PatientPageConstants.HAS_BEEN_RECEIVED,"Checkpoint[3/25]","verifying the notification message is displayed upon sending of new study");
		
		patientPage.assertFalse(patientPage.studyRows.isEmpty(),"Checkpoint[4/25]","Verifying that studylist is not empty");
		patientPage.assertEquals(patientPage.studyRows.size(),1,"Checkpoint[5/25]","Verifying the New study is displayed");

		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
		
		patientPage.clickOnStudy(1);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/viewer.png";
		String actualImagePath = newImagePath+"/actualImages/viewer.png";
		viewerpage.takeElementScreenShot(viewerpage.viewer, expectedImagePath);
		
		viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Checkpoint[6/25]", "Source series is displayed");
		viewerpage.assertTrue(cs.getAllResults().isEmpty(), "Checkpoint[7/25]", "Verifying that there is no result is displayed");
		
		driver.manage().timeouts().implicitlyWait(5,TimeUnit.MINUTES) ;
		viewerpage.waitForElementVisibility(patientPage.notificationDiv);
		viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationTitle.get(0)),PatientPageConstants.MESSAGE_TYPE_INFO,"Checkpoint[8/25]","verifying the notification message is displayed upon sending of new study");
		viewerpage.assertEquals(viewerpage.getText(viewerpage.notificationMessage.get(0)),ViewerPageConstants.NEW_RESULT_NOTIFICATION_MESSAGE,"Checkpoint[9/25]","verifying the notification message is displayed upon sending of new study");

		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
		viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.reRunResultsDialog),"Checkpoint[10/25]","verifying the new results dialog is displayed");
		viewerpage.refreshButton.get(0).click();		
		viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.reRunResultsDialog),"Checkpoint[11/25]","verifying after clicking on refresh button new result dialog is closed");

		
		viewerpage.takeElementScreenShot(viewerpage.viewer, actualImagePath);
		viewerpage.assertEquals(expectedImagePath, actualImagePath, "Checkpoint[12/25]", "verifying the viewer is same before sending results and after sending results");

		db = new DatabaseMethods(driver);
		viewerpage.assertTrue(db.getLogsCount("Unexpected failure during new batch notification").isEmpty(),"Checkpoint[13/25]","verifying that there is no error message in for batch");
		
		HashMap<Integer, String> batchInfo = db.getAllBatchInfo();
		viewerpage.assertEquals(batchInfo.size(),1,"Checkpoint[14/25]","verifying that there is a batch being added");		
		batchInfo.forEach((k, v) -> {
			try {
				viewerpage.assertEquals(db.getBatchMachineID(k),k,"Checkpoint[15/25]","verifying the batchMachine table has entry too for added batch");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		List<String> machineNames = db.getAllMachinesName();
		viewerpage.assertEquals(machineNames.size(),2,"Checkpoint[16/25]","verifying that machine is created so machine count has increased");
		viewerpage.assertTrue(machineNames.contains(machineName),"Checkpoint[17/25]","verifying that machine is also added for added patient");
			
		cs = new ContentSelector(driver);
		cs.assertTrue(!cs.getAllResults().isEmpty(), "Checkpoint[18/25]", "verifying the result is displayed");

		header = new Header(driver);
		header.logout();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientNamesList.size(),1,"Checkpoint[19/25]","verifying patient is present");

		patientPage.clickOnStudy(1);
		viewerpage.waitForViewerpageToLoad();
		
		viewerpage.assertTrue(db.getLogsCount("Unexpected failure during new batch notification").isEmpty(),"Checkpoint[20/25]","Verifying that there is no error in log");
		
		 batchInfo = db.getAllBatchInfo();
			viewerpage.assertEquals(batchInfo.size(),1,"Checkpoint[21/25]","verifying the batch entry is still present after login");		
			batchInfo.forEach((k, v) -> {
				try {
					viewerpage.assertEquals(db.getBatchMachineID(k),k,"Checkpoint[22/25]","verifying the batch machine entry is also persisted after login");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
			machineNames = db.getAllMachinesName();
			viewerpage.assertEquals(machineNames.size(),2,"Checkpoint[23/25]","Verifying that there are two machines in DB");
			viewerpage.assertTrue(machineNames.contains(machineName),"Checkpoint[24/25]","verifying the machine is also persisted after login");
			
			viewerpage.assertEquals(layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), viewerpage.getNumberOfCanvasForLayout(), "Checkpoint[25/25]", "verifying the result series are also displayed and layout is changed to 3x3");
			

	}


	public void pushDataToLiasion(String cstoreLocation, String ruleName, String IP, String port, String dataFolderLoc) throws IOException {

		ProcessBuilder builder = new ProcessBuilder(
				"cmd.exe", "/c", "cd \""+cstoreLocation+"\" && dotnet CStoreScu.dll \""+ruleName+"\" "+IP+" "+port+" \""+dataFolderLoc+"\"");
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null || line.contains(ViewerPageConstants.COMPLETED_EXIT_TEXT)) { break; }
			LOGGER.info(line);
		}
		
		p.destroy();
	}



}
