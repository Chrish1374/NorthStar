//package com.trn.ns.test.obsolete;
//
//import java.text.ParseException;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class E2E_TC1381_Workflow extends TestBase{
//
//	private SinglePatientStudyPage patientstudypage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){	
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//		patientstudypage = new SinglePatientStudyPage(driver);
//	}
//
//	//Test Data - patientName_MR=Subject_60 
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_E2E_TC1381_workflow() throws InterruptedException, ParseException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Isolated Feature Testing – Non-Image Related : Worklist Features – Sorting & Hover over Info ");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		//2. Sort List by clicking Patient Name header – Once for Ascending and second time for descending patient list page
//		//2. Sorts Alphabetical A-Z arrow Icon appears UP, Second click reverses to Z-A Sort
//		//Verifying up and down arrow appears.
//		int j =1;
//		for (WebElement header : patientPage.columnHeaders) {		 
//			String colHeader=patientPage.getText("visibility",header);		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+j+".a /"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : ascending sort");
//			patientPage.click(header);
//			patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(), colHeader,"Verify Up arrow icon of column header- "+colHeader+"" , "Rendering of Up Arrow icon of column header "+colHeader+" successful. ");		
//			//patientPage.compareElementImage(protocolName, patientPage.upArrow, "Verify up arrow image of column header - "+colHeader+"", patientPage.getText("visibility",header)+"upArrow");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+j+".b/"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : descending sort");	
//			patientPage.click(header);	
//			patientPage.verifyEquals(patientPage.sortedDscColumnHeader.getText(),colHeader,"Verify Down arrow icon of column headers - "+colHeader+"" , "Rendering of Down Arrow icon of column headers-PASS ");
//			//patientPage.compareElementImage(protocolName, patientPage.downArrow, "Verify down arrow image of of column headers - "+colHeader+"", patientPage.getText("visibility",header)+"dwnArrow");		
//			j++;
//		}
//		//3. Sort Patient list by clicking each column
//		//3. Confirm sorting is now applied to selected column on   list
//		//Verifying sorting on patient page
//		for (int i =0 ;i< patientPage.columnHeaders.size();i++){
//
//			patientPage.assertEquals(patientPage.getTooltipColHeader(i), patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order..","Verified tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".a /" + patientPage.columnHeaders.size() +"]", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//
//			patientPage.assertEquals(patientPage.fetchColumn(i),patientPage.ascSortingCol(i), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(i))+  "column is sorted in ascending order",patientPage.getText("visibility",patientPage.columnHeaders.get(i)) + "is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".b /" + patientPage.columnHeaders.size() +"]", "Verify "+patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column is sorted in ascending order.");		
//
//			patientPage.assertEquals(patientPage.getTooltipColHeader(i), patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "," + " descending order", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order.","Verified tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order..");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".c /" + patientPage.columnHeaders.size() +"]", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order..");
//
//			patientPage.assertEquals(patientPage.fetchColumn(i),patientPage.dscSortingCol(i), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "column is sorted in ascending order",patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ " is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".d /" + patientPage.columnHeaders.size() +"]", "Verify "+patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column is sorted in descending order.");
//		}
//
//		//Navigating to single study list page
//		patientPage.clickOnPatientRow(subject60PatientName);
//
//		StudyListPage studyListPage = new StudyListPage(driver);
//
//		// Verifying the correct image is getting displayed for ascending/descending sort icon. 
//		int k =1;
//		String colHeader ="";
//		for (WebElement header : patientstudypage.columnHeaders) {
//
//			colHeader=patientstudypage.getText("visibility",header);	
//			if(!colHeader.equals(patientstudypage.getText("visibility",patientstudypage.accession)))
//				patientPage.click(header);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[ 5."+k+" / 5."+patientstudypage.columnHeaders.size()+"]", "Clicking on the column "+patientstudypage.getText("visibility",header)+" : ascending sort");
//			patientstudypage.verifyEquals(patientstudypage.getText("visibility",patientstudypage.sortedAscColumnHeader), colHeader,"Verify Up arrow icon of column headers " , "Rendering of Up Arrow icon of column headers ");
//			//patientstudypage.compareElementImage(protocolName, patientstudypage.upArrow, "Verify up arrow image of column headers", patientstudypage.getText("visibility",header)+"-upArrow");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5."+k+" / 5."+patientstudypage.columnHeaders.size()+"]", "Clicking on the column "+patientstudypage.getText("visibility",header)+" : descending sort");
//			patientPage.click(header);
//			patientstudypage.verifyEquals(patientstudypage.getText("visibility",patientstudypage.sortedDscColumnHeader),colHeader,"Verify Down arrow icon of column headers " , "Rendering of Down Arrow icon of column headers-PASS ");
//			//patientstudypage.compareElementImage(protocolName, patientstudypage.downArrow, "Verify down arrow image of of column headers", patientstudypage.getText("visibility",header)+"-dwnArrow");
//			k++;
//		}
//		//Sorting	
//		for (int i =0 ;i< patientstudypage.columnHeaders.size();i++){
//
//			//Verifying tooltips on sorted columns
//			patientstudypage.click(patientstudypage.columnHeaders.get(i));
//			patientstudypage.waitForTimePeriod(100);
//			patientstudypage.assertEquals(patientstudypage.getTooltipColHeader(i), patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+" column when it is sorted in ascedning order..","Verified tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i)) +" column when it is sorted in ascedning order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".a /" + patientstudypage.columnHeaders.size() +"]", "Verify tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//			// After sorting in ascending order by clicking on column header
//			patientstudypage.assertEquals(patientstudypage.fetchColumn(i),patientstudypage.ascSortingCol(i), "Verify column is sorted in ascending order","Verified column is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".b /" + patientstudypage.columnHeaders.size() +"]", "Verify column is sorted in ascending order.");				
//			//Verifying tooltips on sorted columns
//			patientstudypage.click(patientstudypage.columnHeaders.get(i));
//			patientstudypage.waitForTimePeriod(100);
//			patientstudypage.assertEquals(patientstudypage.getTooltipColHeader(i), patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+ "," + " descending order", "Verify tooltip on column when it is sorted in descedning order.","Verified tooltip on column when it is sorted in descedning order..");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".c /" + patientstudypage.columnHeaders.size() +"]", "Verify tooltip on column when it is sorted in descedning order..");	
//			//Descending sort
//			patientstudypage.assertEquals(patientstudypage.fetchColumn(i),patientstudypage.dscSortingCol(i), "Verify column is sorted in descending order", "Verfied column is sorted in descending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".d /" + patientstudypage.columnHeaders.size() +"]", "Verify column is sorted in descending order.");
//		}	
//
//	}
//}