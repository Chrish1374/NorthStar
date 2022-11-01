package com.trn.ns.test.patientList;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SearchOnPatientListTest extends TestBase 
{
	private PatientListPage patientPage;
	private LoginPage loginPage;
	private ExtentTest extentTest;
	
	private ViewerPage viewerpage;

	String filePath_AH4=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_AH4);

	String filePath_RANDENT=Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
	String patientNameRandoENT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_RANDENT);

	String filePath_GSPSMultiSeries=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNameGSPSMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_GSPSMultiSeries);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	DatabaseMethods db = new DatabaseMethods(driver);
	String AcquisitionDate_AH4= db.getAcquisitionDate(patientNameAh4);
	String AcquisitionDate_GSPSPoint= db.getAcquisitionDate(patientNameGSPSMultiSeries);

	private String patientName = "GSPS";

	String GSPS11_Machine ="GSPS_11";
	String GSPS12_Machine= "GSPS_12";
	String GSPS5_Machine ="GSPS_5";
	String GSPS3_Machine ="GSPS_3";
	String GSPS2_Machine ="GSPS_2";
	String GSPS7_Machine ="GSPS_7";
	String OBJECT_IMPORTER_Machine ="object-importer";
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);

	}	

	@Test(groups ={"chrome","US1195"})
	public void test11_US1195_TC6167_verifyNewPatientListSearchComponent() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the UI of the 'Patient List Search' component and the columns displayed for the patient list on the Patient list page.");
		DatabaseMethods db=new DatabaseMethods(driver);
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		Collections.sort(db.getAllModalities());

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify patient search components");

		patientPage.verifyEquals(patientPage.getText(patientPage.searchLabel),"Search", "Verifying 'Search' label is displayed", "Patient Name: label is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.searchTextbox), "Verifying search textbox is displayed", "Patient name textbox is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.searchTextbox), "Verifying search textbox is displayed", "Patient name textbox is displayed");

		patientPage.assertTrue(patientPage.getText(patientPage.sexLabel).contains("Sex"), "Verifying 'Sex:' label is displayed", "Sex: label is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.maleCheckbox), "Verifying 'Male' button is displayed", "Male button is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.femaleCheckbox), "Verifying 'Female' button is displayed", "Female button is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.searchButton), "Verifying 'Search' button is displayed", "Search button is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.clearButton), "Verifying 'Reset' button is displayed", "Reset button is displayed");

		patientPage.verifyEquals(patientPage.getText(patientPage.modalityLabel),"Modality", "Verifying Modality label is displayed", "Modality label is displayed");

		patientPage.verifyEquals(patientPage.getText(patientPage.acquisitionLabel),"Acquisition Date", "Verifying  Acquisition Date label is displayed", " Acquisition Date  label is displayed");

		patientPage.verifyEquals(patientPage.getText(patientPage.machinesLabel),"Machine Name", "Verifying  Machine Name  label is displayed", "  Machine Name  label is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineDropdownButton), "Verifying machine dropdown is displayed", "Machine dropdown is displayed");

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.acquisitiondateButton), "Verifying acquisition dropdown is displayed", "Acquisition dropdown is displayed");

	
	}

	@Test(groups ={"chrome","US1195"})
	public void test12_US1195_TC6168_verifyNewPatientListSearchComponents() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the fields on the 'Patient List Search' component");
		String patientNameRando = "Rando^ENT";
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify Patient Name' text box should accept the user entered text and a 'x' should be displayed in the text box as soon as the user starts entering text in the text box.");

		patientPage.enterText(patientPage.searchTextbox,"test");
		patientPage.click(patientPage.clearButton);
		patientPage.verifyEquals(patientPage.getText(patientPage.searchTextbox),"", "Verifying after clicking on 'X' button entered text gets cleared", "Text gets cleared");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify after clicking on CT modality button, it should get selected and highlighted");
		patientPage.clickOnGivenModality("CT");
		patientPage.assertTrue(patientPage.verifyModalityIsSelected("CT"),"CT modality button should get highlighted after clicking on it","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify after clicking on CT modality button again, it should get unselectedd");
		patientPage.deSelectModality("CT");
		patientPage.assertFalse(patientPage.verifyModalityIsSelected("CT"),"CT modality button should get unselected after clicking on it again","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify after clicking on Male button, it should get selected and highlighted");
		patientPage.click(patientPage.maleCheckbox);
		patientPage.assertTrue(patientPage.verifyCheckboxIsChecked(patientPage.maleCheckbox),"Male button should get highlighetd after clicking on it","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify after clicking on Male button again, it should get unselected");
		patientPage.click(patientPage.maleCheckbox);
		patientPage.assertFalse(patientPage.verifyCheckboxIsChecked(patientPage.maleCheckbox),"Male button should get unselected after clicking on it again","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify after clicking on Female button, it should get selected and highlighted");
		patientPage.click(patientPage.femaleCheckbox);
		patientPage.assertTrue(patientPage.verifyCheckboxIsChecked(patientPage.femaleCheckbox),"Female button should get highlighetd after clicking on it","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify after clicking on Female button again, it should get unselected");
		patientPage.click(patientPage.femaleCheckbox);
		patientPage.assertFalse(patientPage.verifyCheckboxIsChecked(patientPage.femaleCheckbox),"Female button should get unselected after clicking on it again","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify after clicking on Reset button, all the selected search criteria should be cleared.");
		patientPage.enterText(patientPage.searchTextbox,patientNameRando);
		patientPage.clickOnGivenModality("RTSTRUCT");
		patientPage.click(patientPage.maleCheckbox);
		patientPage.click(patientPage.clearButton);
		patientPage.verifyTrue(patientPage.getText(patientPage.searchTextbox).isEmpty(), "Verifying after clicking on 'Reset' button entered text gets cleared", "Text gets cleared");
		patientPage.assertFalse(patientPage.verifyModalityIsSelected("RTSTRUCT"),"CT modality button should get unselected after clicking on Reset button","Verified");
		patientPage.assertFalse(patientPage.verifyCheckboxIsChecked(patientPage.maleCheckbox),"Male button should get unselected after clicking on 'Reset' button","Verified");
	}

	@Test(groups ={"firefox","chrome","edge","IE11"})
	public void test13_US1195_TC6170_verifyRecentSearches() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the functionality of the 'Patient List Search' and 'Recent Searches'");
		String patientName3C = "3C";
		String patientNameRando = "Rando";
		String patientNameImbio = "Imb";

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		List <WebElement> searchResults = new ArrayList<WebElement>();

		searchResults = patientPage.searchPatient(patientNameAh4,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"",PatientPageConstants.MALE);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify Patient List should be displayed matching the search criteria");
		patientPage.waitForPatientPageToLoad();	
	//	patientPage.verifyEquals(patientPage.getText(searchResults.get(0)).compareToIgnoreCase(patientNameAh4),1,"Verifying search results for AH.4 in patient list","Verified");
		patientPage.assertTrue(patientPage.getText(searchResults.get(1)).contains(patientNameAh4),"Verifying search results for AH.4 in patient list","Verified");
		patientPage.assertTrue(patientPage.getText(searchResults.get(2)).contains(patientNameAh4),"Verifying search results for AH.4 in patient list","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify 'Recent search:' section should display recently searched entry");
		List<Map<String, String>> recentSearch = patientPage.getAllRecentSearchValues();
		//patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameAh4),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.MALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"Verifying third element of first row of recent search","Verified");

		patientPage.click(patientPage.clearButton);
		//patientPage.verifyTrue(patientPage.getText(patientPage.searchTextbox).isEmpty(), "Verifying after clicking on 'Reset' button entered text gets cleared", "Text gets cleared");
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"CT modality button should get unselected after clicking on Reset button","Verified");
		patientPage.assertFalse(patientPage.verifyCheckboxIsChecked(patientPage.maleCheckbox),"Male button should get unselected after clicking on 'Reset' button","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify 'Recent search:' section should display recently searched entry");
		recentSearch = patientPage.getAllRecentSearchValues();
		//patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameAh4),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.MALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"Verifying third element of first row of recent search","Verified");

		searchResults = patientPage.searchPatient(patientName3C,OrthancAndAPIConstants.ORTHANC_PR_VALUE,PatientPageConstants.FEMALE,"");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify Patient List should be displayed matching the search criteria");
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getText(searchResults.get(0)).contains(patientName3C),"Verifying search results for patientName-3ChestCT1p25mm in patient list","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify 'Recent searches' should hold 2 records, 1 for AH.4 and 1 for 3ChestCT1p25mm displaying the latest search criteria on the top.");
		recentSearch = patientPage.getAllRecentSearchValues();
		//patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientName3C),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.FEMALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE),"Verifying third element of first row of recent search","Verified");

		//patientPage.verifyEquals(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameAh4),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.MALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"Verifying third element of first row of recent search","Verified");

		patientPage.click(patientPage.clearButton);

		searchResults = patientPage.searchPatient(patientNameRando,OrthancAndAPIConstants.ORTHANC_RT_VALUE,"","");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify Patient List should be displayed matching the search criteria");
		patientPage.waitForPatientPageToLoad();	
		//patientPage.verifyEquals(patientPage.getText(searchResults.get(0)).compareToIgnoreCase("Rando"),1,"Verifying search results for patientName-3ChestCT1p25mm in patient list","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify 'Recent searches' should hold 3 records, 1 for AH.4 second for 3chest and third for randoENT displaying the latest search criteria on the top");
		recentSearch = patientPage.getAllRecentSearchValues();		
		//patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameRando),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).isEmpty(),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_RT_VALUE),"Verifying third element of first row of recent search","Verified");

		//patientPage.verifyEquals(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientName3C),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.FEMALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE),"Verifying third element of first row of recent search","Verified");

		//patientPage.verifyEquals(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameAh4),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.MALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"Verifying third element of first row of recent search","Verified");

		patientPage.click(patientPage.clearButton);
		searchResults = patientPage.searchPatient(patientNameImbio,"","","");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify Patient List should be displayed matching the search criteria");
		patientPage.waitForPatientPageToLoad();	
		//patientPage.verifyEquals(searchResults.get(0).getText().compareToIgnoreCase(patientNameImbio),1,"Verifying search results for patientName-Imbio in patient list","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify 'Recent searches' should hold 3 records, 1 for AH.4 second for 3chest and third for randoENT displaying the latest search criteria on the top");
		recentSearch = patientPage.getAllRecentSearchValues();
		//patientPage.verifyEquals(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameImbio),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).isEmpty(),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).isEmpty(),"Verifying third element of first row of recent search","Verified");

		//patientPage.verifyEquals(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientNameRando),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(1)).isEmpty(),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(1).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_RT_VALUE),"Verifying third element of first row of recent search","Verified");

		//patientPage.verifyEquals(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(0)).compareToIgnoreCase(patientName3C),1,"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.FEMALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(2).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE),"Verifying third element of first row of recent search","Verified");
	}
	
	@Test(groups ={"chrome","IE","edge","US1195","US1858","Positive","E2E","F956"})
	public void test14_US1195_TC6182_US1858_TC8937_verifySelectionOfRecentSearch() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the selection of the Recent Search. <br>"+
		"[Risk and Impact] Verify patient search result renders when search button is clicked and Clear functionality");
		String patientName = "3C";
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_PR_VALUE,PatientPageConstants.FEMALE,"");
		patientPage.click(patientPage.clearButton);
		patientPage.searchPatient(patientName,"","","");
		patientPage.click(patientPage.clearButton);
		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_PR_VALUE,"",PatientPageConstants.MALE);
		patientPage.click(patientPage.clearButton);
			
		
		patientPage.triggerRecentSearch(3);
		patientPage.waitForPatientPageToLoad();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify Patient List should be updated/display the list of patients matching the search criteria ");
		patientPage.assertTrue(patientPage.patientNamesList.get(0).getText().contains(patientName),"Verifying search results for patientName-3ChestCT1p25mm in patient list","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify values from the selected search should be selected/displayed in the search component");
		//patientPage.verifyEquals(patientPage.getText(patientPage.searchTextbox),patientName, "Verifying after clicking on 'X' button entered text gets cleared", "Text gets cleared");
		patientPage.assertTrue(patientPage.verifyCheckboxIsChecked(patientPage.femaleCheckbox),"Female button should get highlighetd after clicking on it","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify selected search should be displayed/moved to the top row in Recent searches.");
		List<Map<String, String>> recentSearch = patientPage.getAllRecentSearchValues();
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(0)).contains(patientName),"Verifying first element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(1)).contains(PatientPageConstants.FEMALE),"Verifying second element of first row of recent search","Verified");
		patientPage.assertTrue(recentSearch.get(0).get(PatientPageConstants.RECENT_SEARCH.get(4)).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE),"Verifying third element of first row of recent search","Verified");
	}

	@Test(groups ={"Chrome","IE11","Edge","DE1737", "DE1922","Negative"})
	public void test15_DE1737_DE1922_TC7126_TC7763_verifyModalityButton() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify that \"Unknown\" is shown in the modality filter button if the series modality is blank. <br>"+
				"Verify that Modality Text \"Unknown\" is visible clearly when user click on 'Unknown' modality button on desktop.");

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		String searchCriteria = "AH";

		List<String> modalities = patientPage.getModalities();

		for(int i =0;i<modalities.size();i++)
			patientPage.assertFalse(modalities.get(i).isEmpty(),"Checkpoint[1."+i+"/5]","verifying that there is no blank button displayed for modalities");

		patientPage.clickOnGivenModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(PatientPageConstants.UNKNOWN_MODALITY),"Unknown modality button should get highlighted after clicking on it","Verified");
		patientPage.deSelectModality(PatientPageConstants.UNKNOWN_MODALITY);
		
		patientPage.searchPatient(searchCriteria, PatientPageConstants.UNKNOWN_MODALITY, "","");
		int n = patientPage.patientNamesList.size();
		patientPage.assertTrue(n>=1, "Checkpoint[3/5]","Verifying the results"); 

		for(int i =0;i<patientPage.patientNamesList.size();i++) {
			patientPage.assertTrue(patientPage.patientNamesList.get(i).getText().contains(searchCriteria),"Checkpoint[4."+(i+1)+"/5]","Verifying that search is correct as per search string");

		}
		patientPage.click(patientPage.clearButton);
		int n1 = patientPage.patientNamesList.size();
		patientPage.waitForTimePeriod(1000);
		patientPage.assertEquals(n1 , n,"Checkpoint[5/5]","Verifying reset funcationality");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
	public void test23_US1404_TC7574_TC7589_verifyAcquisitionDateOrderSelection() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Acquisition Date - Verify that user is able to see the selected item text in the drop down."
				+ "<br> Verify the acquisition date filter dropdown values order");

		patientPage = new PatientListPage(driver);
	
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.acquisitiondateButton), "Checkpoint[1/5]", "Verifying the acquisition date drop down presence");
		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.acquisitiondateButton, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[2/5]", "Verifying upon loading no value is selected");

		List<String> options = patientPage.getAcquisitionOptions();
		patientPage.assertEquals(options.size(),PatientPageConstants.ACQUISITIONDATE.size(),"Checkpoint[3/5]","verifying the drop down options and ordering");

		for(int i =0;i<options.size();i++) {	
			patientPage.selectAcquisiton(options.get(i));
			patientPage.assertEquals(patientPage.getText(patientPage.selectedAcqOption), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[4/5]", "verifying the drop down options and ordering");


		}
	}

	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
	public void test24_US1404_TC7575_TC7576_TC7577_TC7578_TC7588_verifyMachineDropdownOrderSelection() throws InterruptedException, SQLException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to see the machine names inside machine drop down (Empty and data imported) on patient listing page."
				+ "<br> Verify that machine filter values are alphabetically ascending in sort."
				+ "<br> Verify that user is able to select multiple machine filter values."
				+ "<br> Verify that the tool tip is displayed on the machine names"
				+ "<br> Verify the machine filter, when user drawn the annotations");

		patientPage = new PatientListPage(driver);		
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7),PatientPageConstants.CHECK);
	

		DatabaseMethods db = new DatabaseMethods(driver);
		List<String> machinesFromDB = db.getAllMachinesName();

		machinesFromDB.remove(NSDBDatabaseConstants.USER_DEFINED_MACHINE);

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineDropdown), "Checkpoint[1/8]", "Verifying the machine dropdown is present");
		patientPage.assertTrue(patientPage.getText(patientPage.machineDropdown).isEmpty(), "Checkpoint[2/8]", "Verifying there is no value selected upon loading");

		List<String> machinesFromUI = Arrays.asList(patientPage.getAllMachinefromDropdown());
		Collections.sort(machinesFromDB);
		Collections.sort(machinesFromUI);
		
		patientPage.assertEquals(machinesFromUI, machinesFromDB, "Checkpoint[3/8]", "Verifying the machines from DB and UI are same");

		String selectedMachines ="";
		int machinesToBeSelected =3;
		for(int i =0 ;i<machinesToBeSelected;i++) {
			patientPage.selectMachines(machinesFromUI.get(i));

			if(i<machinesToBeSelected-1)
				selectedMachines = selectedMachines+machinesFromUI.get(i)+"; ";
			else
				selectedMachines = selectedMachines+machinesFromUI.get(i);
		}


		patientPage.assertTrue(patientPage.verifyMachineValueSelected(selectedMachines),"Checkpoint[5/8]","Verifying the values when multiple machines are selected");

		for(int i =0 ;i<machinesToBeSelected;i++) 
			patientPage.selectMachines(machinesFromUI.get(i));

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineDropdown), "Checkpoint[6/8]", "Verifying the dropdown presence when multiple options are deselected");
		patientPage.assertTrue(patientPage.getText(patientPage.machineDropdown).isEmpty(), "Checkpoint[7/8]", "verifying no value is displayed when all values are deselected");

		helper=new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientNameAh4, 1, 1);
		
		PointAnnotation point = new PointAnnotation(driver);

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);

		point.browserBackWebPage();

		patientPage.click(patientPage.machineDropdownButton);
		machinesFromUI = patientPage.convertWebElementToTrimmedStringList(patientPage.machineDropdownOptions);
		patientPage.click(patientPage.machineDropdownButton);

		patientPage.assertFalse(machinesFromUI.contains(NSDBDatabaseConstants.USER_DEFINED_MACHINE), "Checkpoint[8/8]", "Verifying that user created machines are not displayed upon creating annotation");

		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7),PatientPageConstants.UNCHECK);
		
	}

	@Test(groups ={"Chrome","Edge","IE11", "DE1826", "Positive"})
	public void test25_DE1826_TC7439_TC7440_TC7441_verifyDefaultAcquisitionDateSort() throws InterruptedException, ParseException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(
				"Verify patientList search results are sorted by acquisition date by default in descending order while selecting modality. <br>"+
						"Verify patient list search (on patient name/Sex/Modality individually) results are rendered by default in descending order of Acquisition date. <br>"+
						"Verify patient list search (on patient name/Sex/Modality combined) results are rendered by default in descending order of Acquisition date. <br>"+
						"Verify patient list search results are rendered by default in descending order of Acquisition date after clicking on recent search"
				);

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		String patientName = "Star";

		//default in descending order
		List<String> acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));		
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[1/8]", "Verifying the default order of acquisition date when patient page is loaded is in descending order");

		//Search with Modality CT
		patientPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE,"","");
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[2/8]", "Verifying the default order of acquisition date when user search with Modality");
		patientPage.click(patientPage.clearButton);

		//Search with Sex
		patientPage.searchPatient("","","",PatientPageConstants.MALE);
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[3/8]", "Verifying the default order of acquisition date when user search with Sex");
		patientPage.click(patientPage.clearButton);

		//Search with patient Name
		patientPage.searchPatient(patientName,"","","");
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[4/8]", "Verifying the default order of acquisition date when user search with Patient name");
		patientPage.click(patientPage.clearButton);

		//Search with patient Name and Modality
		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"","");
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[5/8]", "Verifying the default order of acquisition date when user search with Patient name, Modality");
		patientPage.click(patientPage.clearButton);

		//Search with patient Name and Modality
		patientPage.searchPatient(patientName, "","", PatientPageConstants.MALE);
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[5/8]", "Verifying the default order of acquisition date when user search with Patient name, Sex");
		patientPage.click(patientPage.clearButton);

		//Search with sex and Modality
		patientPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE,"",PatientPageConstants.MALE);
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[6/8]", "Verifying the default order of acquisition date when user search with Sex, Modality");
		patientPage.click(patientPage.clearButton);

		//search with all combination
		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"",PatientPageConstants.MALE);
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[7/8]", "Verifying the default order of acquisition date when user search with Patient name, Sex, Modality");
		patientPage.click(patientPage.clearButton);


		//search with recentSearch
		patientPage.triggerRecentSearch(2);
		acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[8/8]", "Verifying the default order of acquisition date when user selects the recent search");


	}

	@Test(groups ={"Chrome","Edge","IE11", "DE1935", "Positive"})
	public void test26_DE1935_TC7893_TC7895_VerifySortingOfSearchResultsAtPatientPage() throws InterruptedException, ParseException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(
				"Verify sorting of columns on patient page. <br>"+
						"Verify sorting of search results on patient page"
				);

		loginPage = new LoginPage(driver);	
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		//Search with patient Name and validate default date is descending
		patientPage.searchPatient(patientName,"","","");
		List<String> acquisitionDateList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4));	
		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[2/10]", "Verifying the default order of acquisition date when user search with Patient name");

		//Click on the Patient Name header
		patientPage.click(patientPage.patientNameHeader);
		//patient sort
		List<String> patientNamesList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1));	
		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientNamesList),"Checkpoint[4/10]","Verifying the Patient Name column is in ascendeing order when user clicks on patient name header");
		patientPage.searchPatient("","","",PatientPageConstants.MALE);
		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientNamesList),"Checkpoint[6/10]","Verifying the Patient name column is in ascending order, when search is performed");

		//Click on PatientID
		patientPage.click(patientPage.patientIDHeader);
		//patient sort
		List<String> patientIDList = patientPage.getColumnValue(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0));
		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientIDList),"Checkpoint[8/10]","Verifying the Patient ID column is in ascendeing order when user clicks on patient ID header");
		patientPage.click(patientPage.clearButton);
		patientPage.searchPatient(patientName,"",PatientPageConstants.FEMALE,"");
		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientIDList),"Checkpoint[10/10]","Verifying the Patient ID column is in ascending order, when search is performed");
	}

	@Test(groups ={"Chrome","Edge","IE11", "US1464","US1858", "Positive","E2E","F956"})
	public void test27_US1464_TC7858_TC7856_TC7855_DE1983_TC8029_US1858_TC8927_verifyMachineAndAcquisitionFilterSearch() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to search patients based on machine name filter. <br>"+
				"Verify that machine names are displayed under recent searches.<br>"+
				"Verify original patient list is getting displayed on deselected machines value when a search is performed.<br>"+"Verify that unmatched patients are not displayed on the selection of machine filter. <br>"+
				"[Risk and Impact] Verify patient search result renders when search button is clicked and Clear functionality");

		patientPage = new PatientListPage(driver);
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7),PatientPageConstants.CHECK);
		
		db=new DatabaseMethods(driver);
		
		int patientCountBeforeSearch= patientPage.patientNamesList.size();

		//  TC7858, TC7856 Selecting and searching patient based on machine filter and verifying that correct patient getting filtered.

		patientPage.searchPatient("", "", "", "", "",GSPS11_Machine);

		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[1/9]","Verifying that correct patient name is getting filtered based on Machine name");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[2/9]", "Verifying that recent search patient from machine filter, displayed in recent search");

		patientPage.deselectMachines(GSPS11_Machine);
		patientPage.click(patientPage.searchButton);
		patientPage.waitForTimePeriod(6000);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.patientNamesList.size()>20, "Checkpoint[3/9]", "patient list counts are same");

		patientPage.searchPatient("", "", "","", "", GSPS12_Machine);

		patientPage.assertEquals(patientPage.getSelectedMachineCount(), patientPage.patientNamesList.size(), "Checkpoint[4/9]", "patient list counts are same");

		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[5/9]","Verifying that correct patient name is getting filtered based on Machine name");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[6/9]", "Verifying that recent search patient from machine filter, displayed in recent search");

		patientPage.searchPatient("", "", "", "","", GSPS11_Machine);

		patientPage.searchPatient("", "", "", "","", GSPS5_Machine,GSPS3_Machine,GSPS2_Machine,GSPS7_Machine);
		patientPage.waitForPatientPageToLoad();
		
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[7/9]", "Verifying that recent search patient from machine filter, displayed in recent search");
		List<String> machines = Arrays.asList(patientPage.getAllRecentSearchValues().get(0).get(PatientPageConstants.RECENT_SEARCH.get(2)).split(";"));
		patientPage.assertTrue(machines.toString().contains(GSPS11_Machine) && machines.toString().contains(GSPS12_Machine), "Checkpoint[8/9]", "Verifying the patient dislayed in second recent row after machine name is deselcted and searched");

		// TC8029 Verifying Patient list count after deselecting all machine and clicking on search button.
		patientPage.deselectMachines(patientPage.getAllMachinefromDropdown());
		patientPage.click(patientPage.searchButton);
		patientPage.waitForTimePeriod(6000);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientCountBeforeSearch, patientPage.patientNamesList.size(), "Checkpoint[9/9]", "patient list counts are same");
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7),PatientPageConstants.UNCHECK);
		

	}

	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
	public void test28_US1464_TC7857_verifyAcquisitioFilterSearch() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to search patients based on the acquisition date filter");
		patientPage = new PatientListPage(driver);
		db=new DatabaseMethods(driver);

		/*TC7857 first change the date in studyDate column in study level table against tha patient,
		Selecting value from acquisition filter and searching patient based on acquisition filter and verifying that correct patient getting filtered.
		as per today, yesterday, last week acquisition date.
		 */

		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.TODAY));
		patientPage.selectAcquisiton(PatientPageConstants.TODAY);
		patientPage.click(patientPage.searchButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(6000);
		
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[1/6]", "Verifying that patient belonging to 'Today' are getting filterd only.");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[2/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");

		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.YESTERDAY));
		patientPage.selectAcquisiton(PatientPageConstants.YESTERDAY);
		patientPage.click(patientPage.searchButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(6000);
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(1), "Checkpoint[3/6]", "Verifying that patient belonging to 'Yesterday' are getting filterd only.");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[4/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");

		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.LASTWEEK));
		patientPage.selectAcquisiton(PatientPageConstants.LASTWEEK);
		patientPage.click(patientPage.searchButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(6000);
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(7), "Checkpoint[5/6]", "Verifying that patient belonging to 'LastWeek' are getting filterd only.");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[6/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");


	}

	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
	public void test29_US1464_TC7873_TC7892_TC7890_verifyAcquisitionMachineFilterSearch() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to search patients with combinations of machine name and acquisition date.<br>"+
				"Verify that recent searches are applied in patient listings for machine and acquisition date filters.<br>"+
				"Verify that filters are maintained for Machines and Acquisition Date when user navigates from the study page or viewer.");
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		//  TC7873 Selecting value from acquisition filter and searching patient based on acquisition filter and verifying that correct patient getting filtered.
		db=new DatabaseMethods(driver);
		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.TODAY));
		db.updateAcquisitionDate(patientNameGSPSMultiSeries,fetchDate(PatientPageConstants.TODAY));

		patientPage.searchPatient("", "","","", PatientPageConstants.TODAY, GSPS11_Machine,OBJECT_IMPORTER_Machine);
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7), PatientPageConstants.CHECK);
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[1/10]","Verifying that correct patient name is getting filtered based on Machine name");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[2/10]", "Verifying that recent search patient from machine filter, displayed in recent search");
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[3/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");
		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[4/10]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");

		//TC7892 - Logout and login again and click on recent row of machine filter and acquisition date recent row
		loginPage = new LoginPage(driver);
		loginPage.logout();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();

		patientPage.triggerRecentSearch(1);
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7), PatientPageConstants.CHECK);
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[5/10]","Verifying that correct patient name is getting filtered based on Machine name");
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[6/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");


		// TC7890 -Selecting patient and accessing viewer page and again navigating back to patient list page verifying that filers are still set.

		patientPage.deselectMachines(GSPS11_Machine);

		patientPage.click(patientPage.searchButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(6000);
		patientPage.clickOnPatientRow(patientNameAh4);		
		patientPage.clickOntheFirstStudy();

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		viewerpage.browserBackWebPage();
		patientPage.waitForPatientPageToLoad();
		
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(7), PatientPageConstants.CHECK);
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[7/10]","Verifying on navigating back to patient list page findings are getting filterd still as per applied machine filter.");
		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[8/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");

		patientPage.assertEquals(patientPage.getText(patientPage.selectedMachineValues), OBJECT_IMPORTER_Machine, "Checkpoint[9/10]", "Verifying that value is present in machine filter value after navigating back from viewer page");
		patientPage.assertEquals(patientPage.getText(patientPage.selectedAcquisitionValues), PatientPageConstants.ACQUISITIONDATE.get(0), "Checkpoint[10/10]", "Verifying that value is present in acquisition filter value after navigating back from viewer page");


	}

	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
	public void test30_US1464_TC7872_verifyResetButtonSearch() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Machine and Acquisition date filters are set to default when user clicks the reset button.");
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		//  TC7873 Verifying on clicking on reset button all filters gets removed and machine filter dropdown and Acquisition filter are clean.
		patientPage.searchPatient("", "", "","", PatientPageConstants.TODAY, OBJECT_IMPORTER_Machine);
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/3]", "Verifying that machine filter dropdown has no value set there because of reset button pressed");;

		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertFalse(patientPage.patientNamesList.isEmpty(), "Checkpoint[2/3]", "Verifying that machine filter dropdown has no value set there because of reset button pressed");
		patientPage.assertFalse(patientPage.verifyAcquisitionValueSelected(PatientPageConstants.TODAY), "Checkpoint[3/3]", "Verifying that acquisition filter dropdown has no value set there because of reset button pressed");
	}

	@Test(groups ={"Chrome","Edge","IE11", "DR2253", "Negative"})
	public void test31_DR2253_TC8963_verifyResetButtonAfterAcqSearch() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Acquisition date filter is getting reset after clicking on 'Reset' button.");
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		int patients = patientPage.patientNamesList.size();

		patientPage.searchPatient("", "", "","", PatientPageConstants.TODAY);
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/7]", "Verifying there is no patients present");

		patientPage.click(patientPage.clearButton);
		patientPage.assertFalse(patientPage.verifyAcquisitionValueSelected(PatientPageConstants.TODAY), "Checkpoint[2/7]", "Verifying that acquisition filter dropdown has no value set there because of reset button pressed");
		
		List<Map<String, String>> recentSearchInfo = patientPage.getAllRecentSearchValues();
		patientPage.verifyEquals(recentSearchInfo.size(),1,"Checkpoint[3/7]","Verifying there is only one recent search");
		patientPage.verifyEquals(recentSearchInfo.get(0).get(PatientPageConstants.RECENT_SEARCH.get(3)),PatientPageConstants.TODAY,"Checkpoint[4/7]","Verifying Acquisition date in recent search");
		patientPage.verifyEquals(patientPage.patientNamesList.size(),patients,"Checkpoint[5/7]","Verifying all the patients started showing up on reset");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on search button once again to verify no search is being triggered");
		patientPage.click(patientPage.searchButton);
		recentSearchInfo = patientPage.getAllRecentSearchValues();
		patientPage.verifyEquals(recentSearchInfo.size(),1,"Checkpoint[6/7]","Verifying there is only one recent search");
		patientPage.verifyEquals(recentSearchInfo.get(0).get(PatientPageConstants.RECENT_SEARCH.get(3)),PatientPageConstants.TODAY,"Checkpoint[7/7]","Verifying Acquisition date in recent search");
				
	}
	
	@Test(groups ={"Chrome","Edge","IE11", "DR2365", "Negative","E2E","F956"})
	public void test32_DR2365_TC9316_verifySearchCriteriaNotCleared() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Search panel is cleared partially when navigated back from viewer");
		
		String searchPatient = "abc";
		patientPage = new PatientListPage(driver);
		String firstPatient = patientPage.getText(patientPage.patientNamesList.get(0));
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(firstPatient, 1);
		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();		
		patientPage.searchPatient(searchPatient,OrthancAndAPIConstants.ORTHANC_CT_VALUE, PatientPageConstants.FEMALE, PatientPageConstants.MALE, PatientPageConstants.TODAY, GSPS11_Machine);
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/7]", "Verifying there is no patients present for chose search");

		viewerpage.navigateToForward();
		viewerpage.waitForViewerpageToLoad();
		viewerpage.navigateToBack();
		patientPage.waitForPatientPageToLoad();	

		patientPage.assertEquals(patientPage.getAttributeValue(patientPage.searchTextbox,NSGenericConstants.VALUE), 
				searchPatient, "Checkpoint[2/7]", "Verifying that searched patient name is retained");
			
		patientPage.assertTrue(patientPage.verifyCheckboxIsChecked(patientPage.maleCheckbox),"Checkpoint[3/7]","Male button is remained selected on browser back");
		patientPage.assertTrue(patientPage.verifyCheckboxIsChecked(patientPage.femaleCheckbox),"Checkpoint[4/7]","Female button is remained selected on browser back");
		
		patientPage.assertTrue(patientPage.verifyMachineValueSelected(GSPS11_Machine),"Checkpoint[5/7]","verifying that machine name is retained on browser back");
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_CT_VALUE),"Checkpoint[6/7]","Verifying the Modality value is retained on browser back");
		patientPage.assertTrue(patientPage.verifyAcquisitionValueSelected(PatientPageConstants.TODAY),"Checkpoint[7/7]","Verify the acquistion date is retained on browser back");

	}

	//US1858:Search - Enhance search panel as new UX design
	@Test(groups ={"Chrome","Edge","IE11", "US1858", "Positive","E2E","F956"})
	public void test33_US1858_TC8922_TC8932_verifyRecentSearchPanelFunctionality() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify pressing enter, renders the search results. <br>"+
		"Verify patient search result renders when clicked on recent search row");
		
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
		
		//search patient using Patient name
		patientPage.searchPatient(patientNameGSPSMultiSeries, "", "","");
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientNameGSPSMultiSeries, "Checkpoint[1/5]", "Verified that "+patientNameGSPSMultiSeries+" is search successfully.");
		
		patientPage.click(patientPage.clearButton);
		patientPage.assertEquals(patientPage.getAttributeValue(patientPage.searchTextbox, LoginPageConstants.PLACEHOLDER), "Enter text to search", "Checkpoint[2/5]", "Verified that placeholder is visible after clearing the search.");
		
		//search patient usinh patient name,modality and Gender
		patientPage.searchPatient(patientNameGSPSMultiSeries,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"",PatientPageConstants.MALE );
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientNameGSPSMultiSeries, "Checkpoint[3/5]", "Verified that "+patientNameGSPSMultiSeries+" is search successfully.");
		
		//TC8932: Verify patient search result renders when clicked on recent search row
		patientPage.triggerRecentSearch(1);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.getText(patientPage.patientNamesList.get(0)), patientNameGSPSMultiSeries, "Checkpoint[4/5]", "Verified that "+patientNameGSPSMultiSeries+" is search successfully when Patient name,Gender and Modality is entered as search criteria.");
		patientPage.click(patientPage.clearButton);
		
		//search using patient name as abc
		patientPage.searchPatient("abc", "", "","");
		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[5/5]", "Verified that no records founds and Patient list is empty.");
		
	}

	@Test(groups ={"chrome","IE","edge","US1858","Positive","E2E","F956"})
	public void test34_US1858_TC8934_verifyEllipsesWhenMultipleMachinesAreSelected() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltip is getting displayed on the text which is getting cut on patient search panel.");
		
		patientPage = new PatientListPage(driver);
	
		patientPage.selectMachines(GSPS2_Machine,GSPS12_Machine,OBJECT_IMPORTER_Machine);
		patientPage.assertTrue(patientPage.verifyTextOverFlowForDataWraping(patientPage.selectedMachineValues), "Checkpoint[1/4]", "Verified that tooltip is displayed on the text for machine name.");
		
		patientPage.assertTrue(patientPage.getText(patientPage.selectedMachineValues).contains(GSPS2_Machine), "Checkpoint[2/4]", "Verified that "+GSPS2_Machine +" are visible on tooltip.");
		patientPage.assertTrue(patientPage.getText(patientPage.selectedMachineValues).contains(GSPS12_Machine), "Checkpoint[3/4]", "Verified that "+GSPS12_Machine +" are visible on tooltip.");
		patientPage.assertTrue(patientPage.getText(patientPage.selectedMachineValues).contains(OBJECT_IMPORTER_Machine), "Checkpoint[4/4]", "Verified that "+OBJECT_IMPORTER_Machine +" are visible on tooltip.");
		
	}
	
	@Test(groups ={"chrome","IE","edge","US1858","Positive","E2E","F956"})
	public void test35_US1858_TC8916_verifySearchFunctionalityUsingGender() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify search functionality for gender - Male and Female.");
		
		String patientSex="O";
		patientPage = new PatientListPage(driver);
		
		//take count of all available patient name
		int patientCount=patientPage.patientNamesList.size();
		
		//check sex column from column selector dialog.
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), PatientPageConstants.CHECK);
		
		//search using Male 
		patientPage.searchPatient("", "","", PatientPageConstants.MALE);
		List<String>patientsexList=patientPage.convertWebElementToStringList(patientPage.patientSexList);
		patientPage.assertFalse(patientsexList.isEmpty(),"Checkpoint[1/9]","Verified that search works fine Gender Male.");
		
		for(int i=0;i<patientsexList.size();i++)
		patientPage.assertTrue(patientsexList.get(i).contains(PatientPageConstants.MALE), "Checkpoint[2."+i+"/9]", "Verified that gender is present for patient as M ");
		
		//clear search filter and verify patient list count.
		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientNamesList.size(),patientCount, "Checkpoint[3/9]", "Verified that original patient list is visible after click on clear.");
		
		//search using Female
		patientPage.searchPatient("", "", PatientPageConstants.FEMALE,"");
		patientsexList=patientPage.convertWebElementToStringList(patientPage.patientSexList);
		patientPage.assertFalse(patientsexList.isEmpty(),"Checkpoint[4/9]","Verified that search works fine Gender Female.");
		
		for(int i=0;i<patientsexList.size();i++)
		patientPage.assertTrue(patientsexList.get(i).contains(PatientPageConstants.FEMALE), "Checkpoint[5."+i+"/9]", "Verified that gender is present for patient as F");
	
		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientNamesList.size(),patientCount, "Checkpoint[6/9]", "Verified that original patient list is visible after click on clear.");
		
		//search using both Male and Female
		db=new DatabaseMethods(driver);
		db.updatePatientSex(patientNameGSPSMultiSeries, patientSex);
		patientPage.searchPatient("", "", PatientPageConstants.FEMALE,PatientPageConstants.MALE);
		patientPage.assertEquals(patientPage.patientNamesList.size(),patientCount, "Checkpoint[7/9]", "Verified that original patient list is visible when both male and Female is selected.");
		    
		//search for Patient sex O when both Male and Female is selected
	    patientPage.searchPatient(patientNameGSPSMultiSeries, "", PatientPageConstants.FEMALE,PatientPageConstants.MALE);
	    patientPage.assertEquals(patientPage.patientNamesList.size(),1, "Checkpoint[8/9]", "Verified that original patient list is visible when both male and Female is selected.");
	    patientPage.assertEquals(patientPage.getText(patientPage.patientSexList.get(0)),patientSex, "Checkpoint[9/9]", "Verified that original patient list is visible when both male and Female is selected.");
	    
	    
	}
	
	@Test(groups ={"Chrome","IE11","Edge","DE2328","Positive"})
	public void test36_DE2328_TC9187_TC9214_verifyGenderLabelAndAlignmentOnBrowserResize() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify 'Sex' word is displayed instead of 'Gender' on patient search panel. <br>"+
		"Verify gender options do not get misaligned on browser resize.");

		patientPage = new PatientListPage(driver);
		
		patientPage.assertTrue(patientPage.getText(patientPage.sexLabel).contains(PatientPageConstants.SEX), "Checkpoint[1/3]", "Gender label is mentioned as Sex");
		
		patientPage.resizeBrowserWindow(500, 500);
		patientPage.compareElementImage(protocolName,patientPage.sexLabel ,"Checkpoint[2/3]: Verifying the gender aligment image after resizing to 500 px","test36_01");
		
		patientPage.resizeBrowserWindow(300, 300);
		patientPage.compareElementImage(protocolName,patientPage.sexLabel ,"Checkpoint[3/3]: Verifying the gender aligment image after resizing to 300 px","test36_01");
		
	}

	
	@Test(groups ={"Chrome","IE11","Edge","DE2595","Positive","F966"})
	public void test37_DR2595_TC10287_verifyPatientSelectionAfterSearch() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify first patient is selected and studies from that patient is displayed on search results when the results donot contain the previously selected patient.");

		patientPage = new PatientListPage(driver);
		
		String patientName =patientPage.getPatientName(1);		
		patientPage.assertTrue(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[1/12]","Verifying on loading first patient is selected");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientName),"Checkpoint[2/12]","Verifying on loading first patient is selected");
		
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Searching for modality");
		patientPage.searchPatient("", OrthancAndAPIConstants.ORTHANC_OT_VALUE, "", "","");
		
		String patientNameNew =patientPage.getPatientName(1);	
		
		patientPage.assertTrue(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[3/12]","Verifying on first patient is selected after search based on modality");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientNameNew),"Checkpoint[4/12]","Verifying patient is selected");		
		patientPage.assertFalse((patientName).equals(patientNameNew),"Checkpoint[5/12]","Verifying Patient is changed after search");
		patientPage.click(patientPage.clearButton);
		
		patientPage.assertFalse(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[6/12]","Verifying First patient is not selected after search is cleared");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientNameNew),"Checkpoint[7/12]","Verifying patient is selected which was first during search");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the patient which is not first ");
		patientPage.clickOnPatientRow(5);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Searching for modality which doesn't belong to above selected one");
		patientPage.searchPatient("", OrthancAndAPIConstants.ORTHANC_MR_VALUE, "", "", "");
		
		String patientNameUpdated =patientPage.getPatientName(1);
		patientPage.assertTrue(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[8/12]","Verifying on first patient is selected");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientNameUpdated),"Checkpoint[9/12]","Verifying first patient is selected");	
		patientPage.assertFalse((patientNameUpdated).equals(patientNameNew),"Checkpoint[10/12]","Verifying this is different patient not earlier ones");
			
		patientPage.click(patientPage.clearButton);		
		patientPage.assertFalse(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[11/12]","Verifying on loading first patient is not selected");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientNameUpdated),"Checkpoint[12/12]","Verifying on clear the patient remain selected which was first in search");
	
		
		
	}
	
	
	@Test(groups ={"Chrome","IE11","Edge","DE2595","Positive","F966"})
	public void test38_DR2595_TC10306_verifyPatientSelectionWhenPatientIsPartofSearch() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the patient selected and studies displayed on search results when the results contain the previously selected patient.");

		patientPage = new PatientListPage(driver);
		
		String patientName =patientPage.getPatientName(1);		
		patientPage.assertTrue(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[1/4]","Verifying on loading first patient is selected");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientName),"Checkpoint[2/4]","Verifying on loading first patient is selected");
		
		String searchCritirea = "NorthStar";
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the patient");
		patientPage.clickOnPatientRow(patientNameGSPSMultiSeries);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Searching for searchCritirea which has above selected patient");
		patientPage.searchPatient(searchCritirea, "", "", "", "");
		
		patientPage.assertFalse(patientPage.verifyFirstRowIsSelectedAndInfoDisplayedOnStudy(),"Checkpoint[3/4]","Verifying on first patient is not selected as already selected patient which is part of search");
		patientPage.assertFalse(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(patientNameGSPSMultiSeries),"Checkpoint[4/4]","Verifying patient is selected");		
	
		
		
	}
	
	

	public String fetchDate(String input) throws InterruptedException
	{
		String datevalue="";
		Date date=new Date();

		if(input.equalsIgnoreCase(PatientPageConstants.TODAY)){

			Date Today =DateUtils.addDays(new Date(), -0);
			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
			String formattedDate = sdf.format(Today);
			return formattedDate;
		}

		else if(input.equalsIgnoreCase(PatientPageConstants.YESTERDAY)){

			Date Yesterday =DateUtils.addDays(new Date(), -1);
			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
			String formattedDate = sdf.format(Yesterday);
			return formattedDate;
		}

		else if(input.equalsIgnoreCase(PatientPageConstants.LASTWEEK)){

			Date Week =DateUtils.addDays(new Date(), -7);
			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
			String formattedDate = sdf.format(Week);
			return formattedDate;
		}
		return datevalue;


	}



	@AfterMethod(alwaysRun=true)
	public void AfterMethod() {

		DatabaseMethods db = new DatabaseMethods(driver);
		try {
			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.RECENTSEARCH);
			db.updateAcquisitionDate(patientNameAh4, AcquisitionDate_AH4);
			db.updateAcquisitionDate(patientNameGSPSMultiSeries, AcquisitionDate_GSPSPoint);
			db.updatePatientSex(patientNameGSPSMultiSeries, PatientPageConstants.MALE);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
