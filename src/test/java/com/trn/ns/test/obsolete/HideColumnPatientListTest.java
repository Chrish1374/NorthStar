//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.util.List;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.UsersPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class HideColumnPatientListTest extends TestBase 
//{
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//
//	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
//	private StudyListPage studylistPage;
//	private UsersPage userPage;
//	
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(username, password);
//
//	}	
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test01_US1405_TC7777_verifyUISelectColumnPatientPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the add or hide column dialogue box at the patient page");
//
//		patientPage = new PatientListPage(driver);
//
//		int totalColumns = patientPage.columnHeadersTextOverFlow.size();		
//		List<String> columnNames  = patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow);
//
//		for(int i=0;i<totalColumns;i++) {
//
//			patientPage.performMouseRightClick(patientPage.columnHeadersTextOverFlow.get(i));
//			patientPage.assertTrue(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/12]","Verifying that select column dialogue is present");
//			patientPage.assertEquals(patientPage.columnNamesInSelectColDialogue.size(),totalColumns,"Checkpoint[2."+i+"/12]","Verifying that size of column names present on select dialogue box is same as table on patient list page");
//			patientPage.assertEquals(patientPage.checkboxInSelectColDialogue.size(),totalColumns,"Checkpoint[3."+i+"/12]","Verifying that size of column checkboxes present on select dialogue box is same as columns present in table on patient list page");
//			patientPage.assertEquals(patientPage.convertWebElementToStringList(patientPage.columnNamesInSelectColDialogue),columnNames,"Checkpoint[4."+i+"/12]","Verifying that column names present on select dialogue box are same as patient page list");
//
//			patientPage.click(patientPage.selectColumnDialogueCloseButton);
//		}
//
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)),"Checkpoint[5/12]","Verifying that Patient ID column checkbox is uneditable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)),"Checkpoint[6/12]","Verifying that Patient Name column checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)),"Checkpoint[7/12]","Verifying that Sex column checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(3)),"Checkpoint[8/12]","Verifying that DOB column checkbox is editable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),"Checkpoint[9/12]","Verifying that Acquistion Date column checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5)),"Checkpoint[10/12]","Verifying that Modality column checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(6)),"Checkpoint[11/12]","Verifying that Site column checkbox is editable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7)),"Checkpoint[12/12]","Verifying that Machine column checkbox is uneditable");
//
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test02_US1405_TC7778_verifyUISelectColumnSinglePatientStudyPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the add or hide column dialogue box at the study page");
//
//		patientPage = new PatientListPage(driver);		
//		patientPage.clickOnPatientRow(1);
//
//		
//
//		int totalColumns = studyPage.columnHeadersTextOverFlow.size();		
//		List<String> columnNames  = studyPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow);
//
//		for(int i=0;i<totalColumns;i++) {
//
//			patientPage.performMouseRightClick(studyPage.columnHeadersTextOverFlow.get(i));
//			patientPage.assertTrue(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/13]","Verifying that select column dialogue is present");
//			patientPage.assertEquals(patientPage.columnNamesInSelectColDialogue.size(),totalColumns,"Checkpoint[2."+i+"/13]","Verifying that size of column names present on select dialogue box is same study page");
//			patientPage.assertEquals(patientPage.checkboxInSelectColDialogue.size(),totalColumns,"Checkpoint[3."+i+"/13]","Verifying that size of column checkboxes present on select dialogue box is same as study page list");
//			patientPage.assertEquals(patientPage.getOptionsFromSelectColDialog(),columnNames,"Checkpoint[4."+i+"/13]","Verifying that column names present on select dialogue box are same as patient page list");
//
//			patientPage.click(patientPage.selectColumnDialogueCloseButton);
//		}
//
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.ACESSION_NUMBER),"Checkpoint[5/13]","Verifying the Column Accession # checkbox is uneditable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_DESCRPTION),"Checkpoint[6/13]","Verifying the Column Stusy Description checkbox is uneditable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.AI_PLATFORM),"Checkpoint[7/13]","Verifying the Column Envoy AI checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.NUMBER_OF_IMAGES),"Checkpoint[8/13]","Verifying the Column # images checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.MODALITY),"Checkpoint[9/13]","Verifying the Column modality checkbox is editable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_DATE_TIME),"Checkpoint[10/13]","Verifying the Column Study Date & Time checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.REFERING_PHYSICIAN),"Checkpoint[11/13]","Verifying the Column Referring physician checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.INSTITUTION_NAME),"Checkpoint[12/13]","Verifying the Column checkbox Institution Name is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PRIORITY),"Checkpoint[13/13]","Verifying the Column checkbox Priority is editable");
//
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test03_US1405_TC7778_verifyUISelectColumnStudyPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the add or hide column dialogue box at the study list page");
//
//		patientPage = new PatientListPage(driver);		
//		String newURL="http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL;
//		patientPage.navigateToURL(newURL); 
//
//		studylistPage = new StudyListPage(driver) ;
//		studylistPage.waitForStudyListToLoad();
//
//		int totalColumns = studylistPage.columnHeadersTextOverFlow.size();		
//		List<String> columnNames  = studylistPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow);
//
//		for(int i=0;i<totalColumns;i++) {
//
//			patientPage.performMouseRightClick(studylistPage.columnHeadersTextOverFlow.get(i));
//			patientPage.assertTrue(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/15]","Verifying that select column dialogue is present");
//			patientPage.assertEquals(patientPage.columnNamesInSelectColDialogue.size(),totalColumns,"Checkpoint[2."+i+"/15]","Verifying that size of column names present on select dialogue box");
//			patientPage.assertEquals(patientPage.checkboxInSelectColDialogue.size(),totalColumns,"Checkpoint[3."+i+"/15]","Verifying that size of column checkboxes present on select dialogue box");
//			patientPage.assertEquals(patientPage.getOptionsFromSelectColDialog(),columnNames,"Checkpoint[4."+i+"/15]","Verifying that column names present on select dialogue box are same as patient page list");
//
//			patientPage.click(patientPage.selectColumnDialogueCloseButton);
//		}
//
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(0)),"Checkpoint[5/15]","Verifying the Column Patient ID checkbox is uneditable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(1)),"Checkpoint[6/15]","Verifying the Column Patient Name checkbox is uneditable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.ACESSION_NUMBER),"Checkpoint[7/15]","Verifying the Column Accession # checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_DESCRPTION),"Checkpoint[8/15]","Verifying the Column Study Description checkbox is editable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.AI_PLATFORM),"Checkpoint[9/15]","Verifying the Column Envoy AI checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.NUMBER_OF_IMAGES),"Checkpoint[10/15]","Verifying the Column # images checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.MODALITY),"Checkpoint[11/15]","Verifying the Column Modality checkbox is editable");
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_DATE_TIME),"Checkpoint[12/15]","Verifying the Column Study Date & Time checkbox is uneditable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.REFERING_PHYSICIAN),"Checkpoint[13/15]","Verifying the Column Referring physician checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.INSTITUTION_NAME),"Checkpoint[14/15]","Verifying the Column Institution Name checkbox is editable");
//		patientPage.assertTrue(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.PRIORITY),"Checkpoint[15/15]","Verifying the Column Accession Priority is editable");
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Negative"})
//	public void test04_US1405_TC7782_verifyUISelectColumnUserPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[Risk and Impact] Verify the add or hide column dialogue box at the user page");
//
//		patientPage = new PatientListPage(driver);		
//		String newURL="http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.USER_PAGE_URL;
//		patientPage.navigateToURL(newURL); 
//
//		userPage = new UsersPage(driver);
//
//		int totalColumns = userPage.columnHeadersTextOverFlow.size();		
//
//		for(int i=0;i<totalColumns;i++) {
//
//			patientPage.performMouseRightClick(userPage.columnHeadersTextOverFlow.get(i));
//			patientPage.assertFalse(patientPage.isElementPresent(patientPage.selectColumnDialogue),"Checkpoin[1."+i+"/1]","Verifying that select column dialogue is absent on users page");
//		}
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test05_US1405_TC7786_verifyColumnHideUnHidePatientPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the display column selector, allows the user to show or hide the available list of columns at patient page");
//
//		patientPage = new PatientListPage(driver);
//
//		int totalColumns = patientPage.columnHeadersTextOverFlow.size();		
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);
//		patientPage.assertEquals(patientPage.columnHeadersTextOverFlow.size(),totalColumns-1,"Checkpoint[1/5]","Verifying that size of column names present on select dialogue box is not same as patient page");
//		patientPage.assertFalse(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[2/5]", "Verifying the Column name is not same post uncheck of column from dailog box");
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
//		patientPage.assertEquals(patientPage.columnHeadersTextOverFlow.size(),totalColumns,"Checkpoint[3/5]","Verifying that size of column names present on select dialogue box is same after checking that column");
//		patientPage.assertTrue(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[4/5]", "Verifying the Column name is same post checking the column from dailog box");
//		patientPage.assertEquals(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).get(totalColumns-1),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[5/5]", "Verifying after adding that column the column is added at last of table");
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test06_US1405_TC7787_verifyColumnHideUnHideStudyPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the display column selector, allows the user to show or hide the available list of columns at study page");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(1);
//
//		
//
//		int totalColumns = studyPage.columnHeadersTextOverFlow.size();		
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3), PatientPageConstants.UNCHECK);
//		studyPage.assertEquals(studyPage.columnHeadersTextOverFlow.size(),totalColumns-1,"Checkpoint[1/5]","Verifying that column is hide post uncheck of that column from dialog");
//		studyPage.assertFalse(studyPage.convertWebElementToStringList(studyPage.columnHeadersTextOverFlow).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3)), "Checkpoint[2/5]", "Verifying unchecked column is not present in the table");
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3), PatientPageConstants.CHECK);
//		studyPage.assertEquals(studyPage.columnHeadersTextOverFlow.size(),totalColumns,"Checkpoint[3/5]","Verifying that column is started showing after checking that column from dialog");
//		studyPage.assertTrue(studyPage.convertWebElementToStringList(studyPage.columnHeadersTextOverFlow).contains(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3)), "Checkpoint[4/5]", "Verifying the column name started showing in the table post check of that column in table");
//		studyPage.assertEquals(studyPage.convertWebElementToStringList(studyPage.columnHeadersTextOverFlow).get(totalColumns-1),PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3), "Checkpoint[5/5]", "verifying that checked column is added at the last of table");
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test07_US1405_TC7788_verifyColumnHideUnHideStudyListPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the display column selector, allows the user to show or hide the available list of columns at study list page");
//
//		patientPage = new PatientListPage(driver);
//		patientPage = new PatientListPage(driver);		
//		String newURL="http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL;
//		patientPage.navigateToURL(newURL); 
//
//		studylistPage = new StudyListPage(driver) ;
//		studylistPage.waitForStudyListToLoad();
//
//		int totalColumns = studylistPage.columnHeadersTextOverFlow.size();		
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(3), PatientPageConstants.UNCHECK);
//		studylistPage.assertEquals(studylistPage.columnHeadersTextOverFlow.size(),totalColumns-1,"Checkpoint[1/5]","Verifying column is not present after unchecking");
//		studylistPage.assertFalse(studylistPage.convertWebElementToStringList(studylistPage.columnHeadersTextOverFlow).contains(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(3)), "Checkpoint[2/5]", "Verifying the column name is not present in the list");
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(3), PatientPageConstants.CHECK);
//		studylistPage.assertEquals(studylistPage.columnHeadersTextOverFlow.size(),totalColumns,"Checkpoint[3/5]","Verifying that column started showing when checked");
//		studylistPage.assertTrue(studylistPage.convertWebElementToStringList(studylistPage.columnHeadersTextOverFlow).contains(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(3)), "Checkpoint[4/5]", "verifying that Column started showing post check that column");
//		studylistPage.assertEquals(studylistPage.convertWebElementToStringList(studylistPage.columnHeadersTextOverFlow).get(totalColumns-1),PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(3), "Checkpoint[5/5]", "Verifying checked column started showing at last in table");
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test08_US1405_TC7786_verifyColumnHideUnHidePatientPage() 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the transparent overlay, when user add/hides the columns at the patient, study and studylist page");
//
//		patientPage = new PatientListPage(driver);
//
//		int totalColumns = patientPage.columnHeadersTextOverFlow.size();		
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);
//		patientPage.assertEquals(patientPage.columnHeadersTextOverFlow.size(),totalColumns-1,"Checkpoint[1/5]","Verifying that column is not displayed post uncheck");
//		patientPage.assertFalse(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[2/5]", "Verifying that column name is not displayed");
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
//		patientPage.assertEquals(patientPage.columnHeadersTextOverFlow.size(),totalColumns,"Checkpoint[3/5]","Verifying that size of column names present on select dialogue box");
//		patientPage.assertTrue(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).contains(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)), "Checkpoint[4/5]", "Verifying that column name is displayed post check");
//		patientPage.assertEquals(patientPage.convertWebElementToStringList(patientPage.columnHeadersTextOverFlow).get(totalColumns-1),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[5/5]", "Verifying that column is added at last in table");
//
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1405","Positive"})
//	public void test09_US1405_TC7804_verifySearchWhenColumnHideatientPage() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("[Risk and Impact] Verify the search functionality when user selects column from column selector");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.UNCHECK);
//
//		List<WebElement> results = patientPage.searchPatient("", "", PatientPageConstants.FEMALE);
//
//		patientPage.assertTrue(results.size()>1, "Checkpoint[1/4]", "Verifying that search is happening");
//		patientPage.assertTrue(patientPage.getText(patientPage.recentSearchElements.get(0)).isEmpty(),"Checkpoint[2/4]","Verifying first element of first row of recent search");
//		patientPage.verifyEquals(patientPage.getText(patientPage.recentSearchElements.get(1)),PatientPageConstants.FEMALE,"Checkpoint[3/4]","Verifying the search critirea");
//
//		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
//
//		for(int i=1;i<patientPage.sexColumn.size();i++) {
//			patientPage.scrollIntoView(patientPage.sexColumn.get(i));
//			patientPage.assertEquals(patientPage.getText(patientPage.sexColumn.get(i)), PatientPageConstants.FEMALE, "Checkpoint[4/4]", "Verifying that sex column started showing and search is applied");
//		}
//
//
//	}
//
//
//}
//
//
