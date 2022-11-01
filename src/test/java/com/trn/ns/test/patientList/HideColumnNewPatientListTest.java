package com.trn.ns.test.patientList;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.UsersPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class HideColumnNewPatientListTest extends TestBase 
{
	private PatientListPage patientPage;
	private LoginPage loginPage;
	private ExtentTest extentTest;

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private UsersPage userPage;
	
	
	String gspsFilepath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNameGSPS = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,gspsFilepath);

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
	}	

	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Positive","F957","E2E","F966"})
	public void test01_US1865_TC8685_US2011_TC9002_verifyUISelectColumnPatientPage() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the add or hide column dialogue box at the patient page. <br>"+
		"Re-execute US1865 test cases.");
		
		patientPage = new PatientListPage(driver);		
		
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)),"Checkpoint[1/11]","Verifying that Patient ID column checkbox is uneditable");
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),"Checkpoint[2/11]","Verifying that Patient ID column checkbox is uneditable");
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),"Checkpoint[3/11]","Verifying that Patient ID column checkbox is uneditable");

		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)),"Checkpoint[4/11]","Verifying that Sex column checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(3)),"Checkpoint[5/11]","Verifying that Sex column checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5)),"Checkpoint[6/11]","Verifying that Sex column checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(6)),"Checkpoint[7/11]","Verifying that Sex column checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7)),"Checkpoint[8/11]","Verifying that Sex column checkbox is editable");

		
		for (Entry<String, Boolean> entry : PatientPageConstants.Patient_Header_Checkbox.entrySet()) {
			if(entry.getValue())
				patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),entry.getKey(),PatientPageConstants.CHECK);
		}
		
		List<String> columnNames  = PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS;

		for(int i=0;i<columnNames.size();i++) {

			patientPage.openHideUnhideDialogbox(columnNames.get(i));
			patientPage.assertTrue(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[9."+i+"/11]","Verifying that select column dialogue is present");
			patientPage.assertEquals(patientPage.columnNamesInSelectColDialogue.size(),columnNames.size(),"Checkpoint[9."+i+"/11]","Verifying that size of column names present on select dialogue box is same as table on patient list page");
			patientPage.assertEquals(patientPage.checkboxInSelectColDialogue.size(),columnNames.size(),"Checkpoint[9."+i+"/11]","Verifying that size of column checkboxes present on select dialogue box is same as columns present in table on patient list page");
			patientPage.assertEquals(patientPage.convertWebElementToStringList(patientPage.columnNamesInSelectColDialogue),columnNames,"Checkpoint[9."+i+"/11]","Verifying that column names present on select dialogue box are same as patient page list");

			patientPage.closeSelectColumnDialogBox();
		}

		for (Entry<String, Boolean> entry : PatientPageConstants.Patient_Header_Checkbox.entrySet()) {
			if(entry.getValue())
				patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.patientIDHeader,entry.getKey()),"Checkpoint[10/11]","Verifying that "+entry.getKey()+" column checkbox is editable");
			else
				patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(entry.getKey()),"Checkpoint[11/11]","Verifying that "+entry.getKey()+" column checkbox is uneditable");
				
		}
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Positive","F957","E2E","F966"})
	public void test02_US1865_TC8685_US2011_TC9002_verifyUISelectColumnPatientStudyPage() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the add or hide column dialogue box at the study page.<br>"+
		"Re-execute US1865 test cases.");

		patientPage = new PatientListPage(driver);

		List<String> columnNames  = PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS;
		for(int i=0;i<columnNames.size();i++) {

			
			patientPage.openHideUnhideDialogbox(patientPage.studyTableHeaders.get(i));
			patientPage.assertTrue(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/12]","Verifying that select column dialogue is present");
			patientPage.assertEquals(patientPage.columnNamesInSelectColDialogue.size(),columnNames.size(),"Checkpoint[2."+i+"/12]","Verifying that size of column names present on select dialogue box is same study page");
			patientPage.assertEquals(patientPage.checkboxInSelectColDialogue.size(),columnNames.size(),"Checkpoint[3."+i+"/12]","Verifying that size of column checkboxes present on select dialogue box is same as study page list");
			patientPage.assertEquals(patientPage.getOptionsFromSelectColDialog(),columnNames,"Checkpoint[4."+i+"/12]","Verifying that column names present on select dialogue box are same as patient page list");

			patientPage.closeSelectColumnDialogBox();
		
		}

		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(0),PatientPageConstants.ACESSION_NUMBER),"Checkpoint[5/12]","Verifying the Column Accession # checkbox is uneditable");
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(1),PatientPageConstants.STUDY_DESCRPTION),"Checkpoint[6/12]","Verifying the Column Stusy Description checkbox is uneditable");
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(2),PatientPageConstants.AI_PLATFORM),"Checkpoint[7/12]","Verifying the Column Envoy AI checkbox is uneditable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(3),PatientPageConstants.NUMBER_OF_IMAGES),"Checkpoint[8/12]","Verifying the Column # images checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(4),PatientPageConstants.MODALITY),"Checkpoint[9/12]","Verifying the Column modality checkbox is editable");
		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(5),PatientPageConstants.STUDY_DATE_TIME),"Checkpoint[10/12]","Verifying the Column Study Date & Time checkbox is uneditable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(6),PatientPageConstants.REFERING_PHYSICIAN),"Checkpoint[11/12]","Verifying the Column Referring physician checkbox is editable");
		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(patientPage.studyTableHeaders.get(7),PatientPageConstants.INSTITUTION_NAME),"Checkpoint[12/12]","Verifying the Column checkbox Institution Name is editable");
	
	}

	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Negative","F957","F966"})
	public void test03_US1865_TC8685_US2011_TC9002_verifyUISelectColumnUserPage() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact] Verify the add or hide column dialogue box at the user page. <br>"+
		"Re-execute US1865 test cases.");

		patientPage = new PatientListPage(driver);
		
		String newURL="http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.USER_PAGE_URL;
		patientPage.navigateToURL(newURL); 
		userPage = new UsersPage(driver);
		int totalColumns = userPage.columnHeadersTextOverFlow.size();		

		for(int i=0;i<totalColumns;i++) {

			patientPage.performMouseRightClick(userPage.columnHeadersTextOverFlow.get(i));
			patientPage.assertFalse(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/1]","Verifying that select column dialogue is absent on users page");
		}

	}

	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Positive","F957","E2E","F966"})
	public void test04_US1865_TC8685_US2011_TC9002_verifyColumnHideUnHidePatientPage() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the display column selector, allows the user to show or hide the available list of columns at patient page. <br>"+
		"Re-execute US1865 test cases.");

		PatientListPage patientPage = new PatientListPage(driver);

		int totalColumns = patientPage.displayedPatientTableHeaders.size();	
		
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
		patientPage.assertEquals(patientPage.displayedPatientTableHeaders.size(),totalColumns+1,"Checkpoint[1/4]","Verifying that size of column names present on select dialogue box is same after checking that column");
		List<String> list = patientPage.convertWebElementToStringList(patientPage.displayedPatientTableHeaders);
		patientPage.assertTrue(list.contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[2/4]", "Verifying the Column name is same post checking the column from dailog box");
	
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);
		patientPage.assertEquals(patientPage.displayedPatientTableHeaders.size(),totalColumns,"Checkpoint[3/4]","Verifying that size of column names present on select dialogue box is not same as patient page");
		list = patientPage.convertWebElementToStringList(patientPage.displayedPatientTableHeaders);
		patientPage.assertFalse(list.contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[4/4]", "Verifying the Column name is not same post uncheck of column from dailog box");

		

	}

	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Positive","F957","F966"})
	public void test05_US1865_TC8685_US2011_TC8876_TC9002_verifyColumnHideUnHideStudyPage() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the display column selector, allows the user to show or hide the available list of columns at study page. <br>"+
		"Verify the  Column Selector functionality on StudyList pane.<br>"+
		"Re-execute US1865 test cases.");

		PatientListPage patientPage = new PatientListPage(driver);
		
		int totalColumns = patientPage.studyTableHeaders.size();		

		
		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), PatientPageConstants.UNCHECK);
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).size(),totalColumns-1,"Checkpoint[1/12]","Verifying that column is hide post uncheck of that column from dialog");
		patientPage.assertFalse(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)), "Checkpoint[2/12]", "Verifying unchecked column is not present in the table");

		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), PatientPageConstants.CHECK);
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).size(),totalColumns,"Checkpoint[3/12]","Verifying that column is started showing after checking that column from dialog");
		patientPage.assertTrue(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)), "Checkpoint[4/12]", "Verifying the column name started showing in the table post check of that column in table");
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).get(totalColumns-1),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), "Checkpoint[5/12]", "verifying that checked column is added at the last of table");

		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), PatientPageConstants.UNCHECK);
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).size(),totalColumns-1,"Checkpoint[6/12]","Verifying that column is hide post uncheck of that column from dialog");
		patientPage.assertFalse(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)), "Checkpoint[7/12]", "Verifying unchecked column is not present in the table");

		loginPage = new LoginPage(driver);		
		loginPage.logout();
	
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).size(),totalColumns-1,"Checkpoint[8/12]","Verifying that column is hide post uncheck of that column from dialog after relogin as well.");
		patientPage.assertFalse(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)), "Checkpoint[9/12]", "Verifying unchecked column is not present in the table after relogin");
		
		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), PatientPageConstants.CHECK);
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).size(),totalColumns,"Checkpoint[10/12]","Verifying that column is started showing after checking that column from dialog");
		patientPage.assertTrue(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)), "Checkpoint[11/12]", "Verifying the column name started showing in the table post check of that column in table");
		patientPage.assertEquals(patientPage.getColumnsWhichAreNotDisplayed(patientPage.studyTableHeaders).get(totalColumns-1),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), "Checkpoint[12/12]", "verifying that checked column is added at the last of table");

		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1865","US2011","Positive","F957","E2E","F966"})
	public void test07_US1865_TC8685_US2011_TC9002_verifySearchWhenColumnHiddenPatientPage() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact] Verify the search functionality when user selects column from column selector.<br>"+
		"Re-execute US1865 test cases.");

		PatientListPage patientPage = new PatientListPage(driver);
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);

		List<WebElement> results = patientPage.searchPatient("", "", PatientPageConstants.FEMALE,"");
		patientPage.assertTrue(results.size()>1, "Checkpoint[1/7]", "Verifying that search is happening");
		
		List<Map<String, String>> recentSearch = patientPage.getAllRecentSearchValues();
		
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).isEmpty(),"Checkpoint[2/7]","Verifying first element of first row of recent search");
		patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)),PatientPageConstants.FEMALE,"Checkpoint[3/7]","Verifying the search critirea");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(2)).isEmpty(),"Checkpoint[4/7]","Verifying first element of first row of recent search");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(3)).isEmpty(),"Checkpoint[5/7]","Verifying the search critirea");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).isEmpty(),"Checkpoint[6/7]","Verifying the search critirea");
		
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);

		for(int i=1;i<patientPage.patientSexList.size();i++) {
			patientPage.scrollIntoView(patientPage.patientSexList.get(i));
			patientPage.assertEquals(patientPage.getText(patientPage.patientSexList.get(i)), PatientPageConstants.FEMALE, "Checkpoint[7/7]", "Verifying that sex column started showing and search is applied");
		}


	}
		
	@AfterMethod
	public void cleanDB() throws SQLException {
		
		db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.DISPLAY_TABLE_USER_PREF);
	}


}


