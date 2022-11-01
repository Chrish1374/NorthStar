//package com.trn.ns.test.obsolete;
//
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.time.DateUtils;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.Select;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import com.trn.ns.page.constants.NSDBDatabaseConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.OrthancAndAPIConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class SearchOnPatientListTest extends TestBase 
//{
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//	
//	private ViewerPage viewerpage;
//
//	String filePath_AH4=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_AH4);
//
//	String filePath_RANDENT=Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
//	String patientNameRandoENT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_RANDENT);
//
//	String filePath_GSPSMultiSeries=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String patientNameGSPSMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_GSPSMultiSeries);
//
//	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
//
//	DatabaseMethods db = new DatabaseMethods(driver);
//	String AcquisitionDate_AH4= db.getAcquisitionDate(patientNameAh4);
//	String AcquisitionDate_GSPSPoint= db.getAcquisitionDate(patientNameGSPSMultiSeries);
//
//	private String patientName = "GSPS";
//
//	String GSPS11_Machine ="GSPS_11";
//	String GSPS12_Machine= "GSPS_12";
//	String GSPS5_Machine ="GSPS_5";
//	String GSPS3_Machine ="GSPS_3";
//	String GSPS2_Machine ="GSPS_2";
//	String GSPS7_Machine ="GSPS_7";
//	String OBJECT_IMPORTER_Machine ="object-importer";
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
//	@Test(groups ={"Chrome","IE11","Edge","US1403","Positive"})
//	public void test16_US1403_TC7563_verifyExpandOrCollapseButtonAndFieldsOnSearchPanel() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify search panel toggle button functionality and its tooltip");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//if condition search panel is expanded validate the fields are displayed or collapsed fields are displayed
//
//		if(patientPage.getAttributeValue(patientPage.toggleButtonTitle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_COLLAPSE)){
//
//			//In Expand validate the fields
//			patientPage.assertTrue(patientPage.verifySerachFieldsInExpandMode(),"Checkpoint[1/2]","Verifying the fields in the expanded search view");
//
//			//Click on the toggle button to collapse
//			patientPage.click(patientPage.toggleButton);
//
//			//In Collapse validate the fields
//			patientPage.assertTrue(patientPage.getAttributeValue(patientPage.toggleButtonTitle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_EXPAND),"Verifying the fields in the collapsed search view","Verified");
//			patientPage.assertTrue(patientPage.verifySerachFieldsInCollapseMode(),"Checkpoint[2/2]","Verifying the fields in the collapsed search view");
//			//Make the search as Expandable
//
//
//		}
//
//	}
//	@Test(groups ={"Chrome","IE11","Edge","US1403","Positive"})
//	public void test20_US1403_TC7609_verifyCollapsedSearch() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify recent search of collapsed and expanded search");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.toggleButtonTitle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_COLLAPSE), "Verify the search panel is in expanded view when user logged in", "Verified");
//
//		//searching the patient
//		List<String> patients = patientPage.convertWebElementToStringList(patientPage.searchPatient(patientNameGSPSMultiSeries, OrthancAndAPIConstants.ORTHANC_CT_VALUE, PatientPageConstants.MALE));
//		patientPage.assertTrue(patients.size()>=1, "Checkpoint[1/4]","Verifying the results"); 
//
//
//		//Verifying the searched patient is present in the table
//		for(int i =0;i<patients.size();i++) {
//			patientPage.assertTrue(patients.get(i).contains(patientNameGSPSMultiSeries),"Checkpoint[1."+(i+1)+"/4]","Verifying that search is correct as per search string");
//
//		}
//
//		patientPage.verifyEquals(patientPage.getText(patientPage.recentSearchElements.get(0)).compareToIgnoreCase(patientNameGSPSMultiSeries),1,"Verifying first element of first row of recent search","Verified");
//
//		//click on the toggle button to collapsed
//		patientPage.click(patientPage.toggleButton);
//
//		//		
//		patientPage.assertTrue(patientPage.searchButton.isDisplayed(), "Verify search button is present when user collapsed the search panel", "search button is present in collapsed panel");	
//		patientPage.assertTrue(patientPage.clearButton.isDisplayed(), "Verify reset button is present when user collapsed the search panel", "reset button is present in collapsed panel");	
//
//		//Patient name at expanded view is present in the collapsed view
//		patientPage.assertTrue(patientPage.searchTextbox.getAttribute(NSGenericConstants.VALUE).equalsIgnoreCase(patientNameGSPSMultiSeries), "Checkpoint[2/4]", " Patient name at expanded view is present in the collapsed view");	
//		patientPage.click(patientPage.searchButton);
//		patientPage.searchTextbox.clear();
//
//		//searching the patient and storing the list as string
//		List<String> newPatients = patientPage.convertWebElementToStringList(patientPage.searchPatient(patientNameRandoENT,"", ""));
//
//		patientPage.assertTrue(patientPage.patientNameList.size()>=1, "Checkpoint[3/4]","Verifying the results");
//
//		for(int i =0;i<newPatients.size();i++) {
//			patientPage.assertTrue(newPatients.get(i).contains(patientNameRandoENT),"Checkpoint[3."+(i+1)+"/5]","Verifying that search is correct as per search string");
//
//		}
//
//		patientPage.click(patientPage.toggleButton);
//		patientPage.verifyEquals(patientPage.getText(patientPage.recentSearchElements.get(0)).compareToIgnoreCase(patientNameRandoENT),1,"Checkpoint[4/4]","Verifying first element of first row of recent search");
//
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1403","Positive"})
//	public void test21_US1403_TC7613_verifySearchPanelResetFunctionality() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify search panel reset functionality");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		List<String> modalities = patientPage.convertWebElementToStringList(patientPage.allModalitiesButton);
//
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.toggleButtonTitle, NSGenericConstants.TITLE).equalsIgnoreCase(ViewerPageConstants.OUTPUT_PANEL_COLLAPSE), "Verify the search panel is in expanded view when user logged in", "Verified");
//
//		//searching the patient
//		List<String> patients = patientPage.convertWebElementToStringList(patientPage.searchPatient(patientNameGSPSMultiSeries, OrthancAndAPIConstants.ORTHANC_CT_VALUE, PatientPageConstants.MALE));
//		patientPage.assertEquals(patients.size(),1,"Verifying the results","Verified"); 
//
//		//Verifying the searched patient is present in the table
//		for(int i =0;i<patients.size();i++) {
//			patientPage.assertTrue(patients.get(i).contains(patientNameGSPSMultiSeries),"Checkpoint[1."+(i+1)+"/3]","Verifying that search is correct as per search string");
//
//		}
//
//		patientPage.verifyEquals(patientPage.getText(patientPage.recentSearchElements.get(0)).compareToIgnoreCase(patientNameGSPSMultiSeries),1,"Verifying first element of first row of recent search","Verified");
//
//		patientPage.click(patientPage.clearButton);
//		patientPage.waitForTimePeriod(5000);
//
//		for(int i =0;i<modalities.size();i++) {
//
//			patientPage.assertFalse(patientPage.allModalitiesButton.get(i).getAttribute(NSGenericConstants.STYLE).contains(NSGenericConstants.BACKGROUND_COLOR), "Checkpoint[2/3]"," Modality button is not highlighted after reseting the button");
//		}
//		patientPage.assertTrue(patientPage.patientNameList.size()>patients.size(),"Checkpoint[2.5/3]","Verifying the patient table is not filtered with search");
//
//
//		patientPage.click(patientPage.toggleButton);
//		patientPage.searchTextbox.clear();
//
//
//		List<String> newPatients = patientPage.convertWebElementToStringList(patientPage.searchPatient(patientNameRandoENT, "",""));
//		for(int i =0;i<newPatients.size();i++) {
//			patientPage.assertTrue(newPatients.get(i).contains(patientNameRandoENT),"Verified","Verifying that search is correct as per search string");
//
//		}
//
//		//reset and validate the search box and table whether table is reset or not
//		patientPage.click(patientPage.clearButton);
//		patientPage.waitForTimePeriod(5000);
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.searchTextbox, NSGenericConstants.VALUE).isEmpty(),"Checkpoint[3.1/3]","Verifying the search box is empty");
//		patientPage.assertTrue(patientPage.patientNameList.size()>newPatients.size(),"Checkpoint[3/3]","Verifying the patient table is not filtered with search");
//
//	}
//
////	@Test(groups ={"Chrome","IE11","Edge","US1403","Positive"})
//	public void test22_US1403_TC7614_verifyModalitiesAreNotOverlappedWhenResized() throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify modalities are not overlapped, when resizing the browser");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		ArrayList<Integer> xaxis = new ArrayList<Integer>();
//		ArrayList<Integer> yaxis = new ArrayList<Integer>();
//		ArrayList<Integer> xaxis1 = new ArrayList<Integer>();
//		ArrayList<Integer> yaxis1 = new ArrayList<Integer>();
//
//		List<WebElement> modalities = patientPage.allModalitiesButton;
//		for(int i =0;i<modalities.size();i++) {
//
//			int xAxis = patientPage.allModalitiesButton.get(i).getLocation().getX();
//			int yAxis = patientPage.allModalitiesButton.get(i).getSize().getWidth();
//
//			xaxis.add(i, xAxis);
//			yaxis.add(i, yAxis);
//
//		}
//
//
//		patientPage.resizeBrowserWindow(400, 400);
//
//		List<WebElement> newModalities = patientPage.allModalitiesButton;
//
//		for(int i =0;i<newModalities.size();i++) {
//
//			int xAxis1 = patientPage.allModalitiesButton.get(i).getLocation().getX();
//			int yAxis1 = patientPage.allModalitiesButton.get(i).getSize().getWidth();
//
//			xaxis1.add(i, xAxis1);
//			yaxis1.add(i, yAxis1);
//
//		}
//
//		for(int i =0;i<modalities.size();i++) 
//		{
//			for(int j =1;j<modalities.size();j++){
//
//				patientPage.assertTrue(xaxis.get(i) > xaxis1.get(j) && yaxis.get(i) > yaxis1.get(j), "Checkpoint[1/1]", "Verifying the modalities are not overlapped with each other");
//
//			}
//		}
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
//	public void test23_US1404_TC7574_TC7589_verifyAcquisitionDateOrderSelection() throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Acquisition Date - Verify that user is able to see the selected item text in the drop down."
//				+ "<br> Verify the acquisition date filter dropdown values order");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.acquisitiondateDropdown), "Checkpoint[1/5]", "Verifying the acquisition date drop down presence");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.acquisitiondateDropdown, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[2/5]", "Verifying upon loading no value is selected");
//
//		int options = patientPage.acquistionDateDropDownOptions.size();
//		patientPage.assertEquals(options,PatientPageConstants.ACQUISITIONDATE.size(),"Checkpoint[3/5]","verifying the drop down options and ordering");
//
//		for(int i =0;i<options;i++)		
//			patientPage.assertEquals(patientPage.getText(patientPage.acquistionDateDropDownOptions.get(i)), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[4/5]", "verifying the drop down options and ordering");
//
//		for(int i =0;i<options;i++) {	
//			patientPage.click(patientPage.acquisitiondateDropdown);
//			patientPage.click(patientPage.acquistionDateDropDownOptions.get(i));
////			patientPage.assertEquals(patientPage.getAttributeValue(patientPage.acquisitiondateDropdown, NSGenericConstants.VALUE), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[5/5]", "Verifying the selection of values");
//			Select dropdown = new Select(patientPage.acquisitiondateDropdown);
//			patientPage.assertEquals(patientPage.getText(dropdown.getFirstSelectedOption()), PatientPageConstants.ACQUISITIONDATE.get(i), "Checkpoint[5/5]", "Verifying the selection of values");
//
//
//		}
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1404","Positive"})
//	public void test24_US1404_TC7575_TC7576_TC7577_TC7578_TC7588_verifyMachineDropdownOrderSelection() throws InterruptedException, SQLException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to see the machine names inside machine drop down (Empty and data imported) on patient listing page."
//				+ "<br> Verify that machine filter values are alphabetically ascending in sort."
//				+ "<br> Verify that user is able to select multiple machine filter values."
//				+ "<br> Verify that the tool tip is displayed on the machine names"
//				+ "<br> Verify the machine filter, when user drawn the annotations");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		List<String> machinesFromDB = db.getAllMachinesName();
//
//		machinesFromDB.remove(NSDBDatabaseConstants.USER_DEFINED_MACHINE);
//
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineFilterDropdown), "Checkpoint[1/8]", "Verifying the machine dropdown is present");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[2/8]", "Verifying there is no value selected upon loading");
//
//		List<String> machinesFromUI = Arrays.asList(patientPage.getAllMachinefromDropdown());
//		Collections.sort(machinesFromDB);
//		Collections.sort(machinesFromUI);
//		
//		patientPage.assertEquals(machinesFromUI, machinesFromDB, "Checkpoint[3/8]", "Verifying the machines from DB and UI are same");
//
//		String selectedMachines ="";
//		int machinesToBeSelected =3;
//		for(int i =0 ;i<machinesToBeSelected;i++) {
//			patientPage.selectMachines(machinesFromUI.get(i));
//
//			if(i<machinesToBeSelected-1)
//				selectedMachines = selectedMachines+machinesFromUI.get(i)+"; ";
//			else
//				selectedMachines = selectedMachines+machinesFromUI.get(i);
//		}
//
//
//		patientPage.assertEquals(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.TITLE),selectedMachines,"Checkpoint[4/8]","verifying the tooltip when multiple machines are selected");
//		patientPage.assertEquals(patientPage.getText(patientPage.machineDropdownButton),selectedMachines,"Checkpoint[5/8]","Verifying the values when multiple machines are selected");
//
//		for(int i =0 ;i<machinesToBeSelected;i++) 
//			patientPage.selectMachines(machinesFromUI.get(i));
//
//		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineFilterDropdown), "Checkpoint[6/8]", "Verifying the dropdown presence when multiple options are deselected");
//		patientPage.assertTrue(patientPage.getAttributeValue(patientPage.machineDropdownButton, NSGenericConstants.VALUE).isEmpty(), "Checkpoint[7/8]", "verifying no value is displayed when all values are deselected");
//
//
//		patientPage.clickOnPatientRow(patientNameAh4);
//		SinglePatientStudyPage 
//		studyPage.clickOnStudy(1);
//
//		PointAnnotation point = new PointAnnotation(driver);
//		point.waitForViewerpageToLoad();
//
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -50, -50);
//
//		point.navigateToPatientPage();
//
//		patientPage.click(patientPage.machineDropdownButton);
//		machinesFromUI = patientPage.convertWebElementToTrimmedStringList(patientPage.machineDropdownOptions);
//		patientPage.click(patientPage.machineDropdownButton);
//
//		patientPage.assertFalse(machinesFromUI.contains(NSDBDatabaseConstants.USER_DEFINED_MACHINE), "Checkpoint[8/8]", "Verifying that user created machines are not displayed upon creating annotation");
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "DE1826", "Positive"})
//	public void test25_DE1826_TC7439_TC7440_TC7441_verifyDefaultAcquisitionDateSort() throws InterruptedException, ParseException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(
//				"Verify patientList search results are sorted by acquisition date by default in descending order while selecting modality. <br>"+
//						"Verify patient list search (on patient name/Sex/Modality individually) results are rendered by default in descending order of Acquisition date. <br>"+
//						"Verify patient list search (on patient name/Sex/Modality combined) results are rendered by default in descending order of Acquisition date. <br>"+
//						"Verify patient list search results are rendered by default in descending order of Acquisition date after clicking on recent search"
//				);
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		String patientName = "Star";
//
//		//default in descending order
//
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[1/8]", "Verifying the default order of acquisition date when patient page is loaded is in descending order");
//
//		//Search with Modality CT
//		patientPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE,"");
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[2/8]", "Verifying the default order of acquisition date when user search with Modality");
//		patientPage.click(patientPage.clearButton);
//
//		//Search with Sex
//		patientPage.searchPatient("","",PatientPageConstants.MALE);
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[3/8]", "Verifying the default order of acquisition date when user search with Sex");
//		patientPage.click(patientPage.clearButton);
//
//		//Search with patient Name
//		patientPage.searchPatient(patientName,"","");
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[4/8]", "Verifying the default order of acquisition date when user search with Patient name");
//		patientPage.click(patientPage.clearButton);
//
//		//Search with patient Name and Modality
//		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"");
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[5/8]", "Verifying the default order of acquisition date when user search with Patient name, Modality");
//		patientPage.click(patientPage.clearButton);
//
//		//Search with patient Name and Modality
//		patientPage.searchPatient(patientName, "", PatientPageConstants.MALE);
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[5/8]", "Verifying the default order of acquisition date when user search with Patient name, Sex");
//		patientPage.click(patientPage.clearButton);
//
//		//Search with sex and Modality
//		patientPage.searchPatient("",OrthancAndAPIConstants.ORTHANC_CT_VALUE,PatientPageConstants.MALE);
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[6/8]", "Verifying the default order of acquisition date when user search with Sex, Modality");
//		patientPage.click(patientPage.clearButton);
//
//		//search with all combination
//		patientPage.searchPatient(patientName,OrthancAndAPIConstants.ORTHANC_CT_VALUE,PatientPageConstants.MALE);
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[7/8]", "Verifying the default order of acquisition date when user search with Patient name, Sex, Modality");
//		patientPage.click(patientPage.clearButton);
//
//
//		//search with recentSearch
//		patientPage.click(patientPage.recentSearchElements.get(1));
//		patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT);
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[8/8]", "Verifying the default order of acquisition date when user selects the recent search");
//
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "DE1935", "Positive"})
//	public void test26_DE1935_TC7893_TC7895_VerifySortingOfSearchResultsAtPatientPage() throws InterruptedException, ParseException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription(
//				"Verify sorting of columns on patient page. <br>"+
//						"Verify sorting of search results on patient page"
//				);
//
//		loginPage = new LoginPage(driver);	
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//Search with patient Name and validate default date is descending
//		patientPage.searchPatient(patientName,"","");
//		patientPage.verifyEquals(patientPage.sortedDscColumnHeader.getText(),"Acquisition Date, descending order","Checkpoint[1/10]" , "Rendering of Down Arrow icon of column headers-PASS ");
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(patientPage.acquisitionDateList, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[2/10]", "Verifying the default order of acquisition date when user search with Patient name");
//
//		//Click on the Patient Name header
//		patientPage.click(patientPage.patientNameHeader);
//		patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(),"Patient Name, ascending order","Checkpoint[3/10]", "Rendering of up Arrow icon of column headers-PASS ");
//		//patient sort
//		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientPage.patientNameList),"Checkpoint[4/10]","Verifying the Patient Name column is in ascendeing order when user clicks on patient name header");
//		patientPage.searchPatient("","",PatientPageConstants.MALE);
//		patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(),"Patient Name, ascending order","Checkpoint[5/10]" , "Rendering of up Arrow icon of column headers-PASS ");
//		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientPage.patientNameList),"Checkpoint[6/10]","Verifying the Patient name column is in ascending order, when search is performed");
//
//		//Click on PatientID
//		patientPage.click(patientPage.patientIDHeader);
//		patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(),"Patient ID, ascending order","Checkpoint[7/10]" , "Rendering of up Arrow icon of column headers-PASS ");
//		//patient sort
//		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientPage.patientIDList),"Checkpoint[8/10]","Verifying the Patient ID column is in ascendeing order when user clicks on patient ID header");
//		patientPage.click(patientPage.clearButton);
//		patientPage.searchPatient(patientName,"",PatientPageConstants.FEMALE);
//		patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(),"Patient ID, ascending order","Checkpoint[9/10]" , "Rendering of up Arrow icon of column headers-PASS ");
//		patientPage.assertTrue(patientPage.VerifyAscSortingOrder(patientPage.patientIDList),"Checkpoint[10/10]","Verifying the Patient ID column is in ascending order, when search is performed");
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
//	public void test27_US1464_TC7858_TC7856_TC7855_DE1983_TC8029_verifyMachineAndAcquisitionFilterSearch() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that the user is able to search patients based on machine name filter. <br>"+
//				"Verify that machine names are displayed under recent searches.<br>"+
//				"Verify original patient list is getting displayed on deselected machines value when a search is performed.<br>"+"Verify that unmatched patients are not displayed on the selection of machine filter.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		db=new DatabaseMethods(driver);
//		
//		int patientCountBeforeSearch= patientPage.patientNamesList.size();
//
//		//  TC7858, TC7856 Selecting and searching patient based on machine filter and verifying that correct patient getting filtered.
//
//		patientPage.searchPatient("", "", "", "", GSPS11_Machine);
//
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[1/9]","Verifying that correct patient name is getting filtered based on Machine name");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[2/9]", "Verifying that recent search patient from machine filter, displayed in recent search");
//
//		patientPage.deselectMachines(GSPS11_Machine);
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForTimePeriod(6000);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.assertEquals(patientCountBeforeSearch, patientPage.patientNamesList.size(), "Checkpoint[3/9]", "patient list counts are same");
//
//		patientPage.searchPatient("", "", "", "", GSPS12_Machine);
//
//		patientPage.assertEquals(patientPage.getSelectedMachineCount(), patientPage.patientNamesList.size(), "Checkpoint[4/9]", "patient list counts are same");
//
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[5/9]","Verifying that correct patient name is getting filtered based on Machine name");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[6/9]", "Verifying that recent search patient from machine filter, displayed in recent search");
//
//		patientPage.searchPatient("", "", "", "", GSPS11_Machine);
//
//		patientPage.searchPatient("", "", "", "", GSPS5_Machine,GSPS3_Machine,GSPS2_Machine,GSPS7_Machine);
//		patientPage.waitForPatientPageToLoad();
//		
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[7/9]", "Verifying that recent search patient from machine filter, displayed in recent search");
//		List<String> machines = Arrays.asList(patientPage.getAllRecentSearchValues().get(0).get(PatientPageConstants.RECENT_SEARCH.get(2)).split(";"));
//		
//		patientPage.assertTrue(machines.contains(GSPS11_Machine) && machines.contains(GSPS12_Machine), "Checkpoint[8/9]", "Verifying the patient dislayed in second recent row after machine name is deselcted and searched");
//
//		// TC8029 Verifying Patient list count after deselecting all machine and clicking on search button.
//		patientPage.deselectMachines(patientPage.getAllMachinefromDropdown());
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForTimePeriod(6000);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.assertEquals(patientCountBeforeSearch, patientPage.patientNamesList.size(), "Checkpoint[9/9]", "patient list counts are same");
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
//	public void test28_US1464_TC7857_verifyAcquisitioFilterSearch() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to search patients based on the acquisition date filter");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		db=new DatabaseMethods(driver);
//
//		/*TC7857 first change the date in studyDate column in study level table against tha patient,
//		Selecting value from acquisition filter and searching patient based on acquisition filter and verifying that correct patient getting filtered.
//		as per today, yesterday, last week acquisition date.
//		 */
//
//		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.TODAY));
//		patientPage.selectAcquisiton(PatientPageConstants.TODAY);
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.waitForTimePeriod(6000);
//		
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[1/6]", "Verifying that patient belonging to 'Today' are getting filterd only.");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[2/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");
//
//		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.YESTERDAY));
//		patientPage.selectAcquisiton(PatientPageConstants.YESTERDAY);
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.waitForTimePeriod(6000);
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(1), "Checkpoint[3/6]", "Verifying that patient belonging to 'Yesterday' are getting filterd only.");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[4/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");
//
//		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.LASTWEEK));
//		patientPage.selectAcquisiton(PatientPageConstants.LASTWEEK);
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.waitForTimePeriod(6000);
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(7), "Checkpoint[5/6]", "Verifying that patient belonging to 'LastWeek' are getting filterd only.");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[6/6]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");
//
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
//	public void test29_US1464_TC7873_TC7892_TC7890_verifyAcquisitionMachineFilterSearch() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to search patients with combinations of machine name and acquisition date.<br>"+
//				"Verify that recent searches are applied in patient listings for machine and acquisition date filters.<br>"+
//				"Verify that filters are maintained for Machines and Acquisition Date when user navigates from the study page or viewer.");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		//  TC7873 Selecting value from acquisition filter and searching patient based on acquisition filter and verifying that correct patient getting filtered.
//		db=new DatabaseMethods(driver);
//		db.updateAcquisitionDate(patientNameAh4,fetchDate(PatientPageConstants.TODAY));
//		db.updateAcquisitionDate(patientNameGSPSMultiSeries,fetchDate(PatientPageConstants.TODAY));
//
//		patientPage.searchPatient("", "", "", PatientPageConstants.TODAY, GSPS11_Machine,OBJECT_IMPORTER_Machine);
//
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[1/10]","Verifying that correct patient name is getting filtered based on Machine name");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterMachineSearch(), "Checkpoint[2/10]", "Verifying that recent search patient from machine filter, displayed in recent search");
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[3/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");
//		patientPage.assertTrue(patientPage.verifyRecentSearchAfterAcquisitionDropDownSearch(), "Checkpoint[4/10]", "Verifying that recent search patient from acquisition filter, displayed in recent search section");
//
//		//TC7892 - Logout and login again and click on recent row of machine filter and acquisition date recent row
//		loginPage = new LoginPage(driver);
//		loginPage.logout();
//		loginPage.login(username, password);
//		patientPage.waitForPatientPageToLoad();
//
//		patientPage.triggerRecentSearch(1);
//
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[5/10]","Verifying that correct patient name is getting filtered based on Machine name");
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[6/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");
//
//
//		// TC7890 -Selecting patient and accessing viewer page and again navigating back to patient list page verifying that filers are still set.
//
//		patientPage.deselectMachines(GSPS11_Machine);
//
//		patientPage.click(patientPage.searchButton);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.waitForTimePeriod(6000);
//		patientPage.clickOnPatientRow(patientNameAh4);		
//		patientPage.clickOntheFirstStudy();
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//
//		viewerpage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//		
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromMachine(), "Checkpoint[7/10]","Verifying on navigating back to patient list page findings are getting filterd still as per applied machine filter.");
//		patientPage.assertTrue(patientPage.verifyCorrectPatientSearchFromAcquisition(0), "Checkpoint[8/10]", "Verifying that patient belonging to 'Today' are getting filterd only.");
//
//		patientPage.assertEquals(patientPage.getText(patientPage.selectedMachineValues), OBJECT_IMPORTER_Machine, "Checkpoint[9/10]", "Verifying that value is present in machine filter value after navigating back from viewer page");
//		patientPage.assertEquals(patientPage.getText(patientPage.selectedAcquisitionValues), PatientPageConstants.ACQUISITIONDATE.get(0), "Checkpoint[10/10]", "Verifying that value is present in acquisition filter value after navigating back from viewer page");
//
//
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "US1464", "Positive"})
//	public void test30_US1464_TC7872_verifyResetButtonSearch() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Machine and Acquisition date filters are set to default when user clicks the reset button.");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//
//		//  TC7873 Verifying on clicking on reset button all filters gets removed and machine filter dropdown and Acquisition filter are clean.
//
//		patientPage.searchPatient("", "", "", PatientPageConstants.TODAY, OBJECT_IMPORTER_Machine);
//		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/3]", "Verifying that machine filter dropdown has no value set there because of reset button pressed");;
//
//		patientPage.click(patientPage.clearButton);
//		patientPage.assertFalse(patientPage.verifyMachineValueSelected(OBJECT_IMPORTER_Machine), "Checkpoint[2/3]", "Verifying that machine filter dropdown has no value set there because of reset button pressed");
//		patientPage.assertFalse(patientPage.verifyAcquisitionValueSelected(PatientPageConstants.TODAY), "Checkpoint[3/3]", "Verifying that acquisition filter dropdown has no value set there because of reset button pressed");
//	}
//
//	@Test(groups ={"Chrome","Edge","IE11", "DR2253", "Negative"})
//	public void test30_DR2253_TC8963_verifyResetButtonAfterAcqSearch() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Acquisition date filter is getting reset after clicking on 'Reset' button.");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		int patients = patientPage.patientNamesList.size();
//
//		patientPage.searchPatient("", "", "", PatientPageConstants.TODAY);
//		patientPage.assertTrue(patientPage.patientNamesList.isEmpty(), "Checkpoint[1/7]", "Verifying there is no patients present");
//
//		patientPage.click(patientPage.clearButton);
//		patientPage.assertFalse(patientPage.verifyAcquisitionValueSelected(PatientPageConstants.TODAY), "Checkpoint[2/7]", "Verifying that acquisition filter dropdown has no value set there because of reset button pressed");
//		
//		List<Map<String, String>> recentSearchInfo = patientPage.getAllRecentSearchValues();
//		patientPage.verifyEquals(recentSearchInfo.size(),1,"Checkpoint[3/7]","Verifying there is only one recent search");
//		patientPage.verifyEquals(recentSearchInfo.get(0).get(PatientPageConstants.RECENT_SEARCH.get(3)),PatientPageConstants.TODAY,"Checkpoint[4/7]","Verifying Acquisition date in recent search");
//		patientPage.verifyEquals(patientPage.patientNamesList.size(),patients,"Checkpoint[5/7]","Verifying all the patients started showing up on reset");
//		
//		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Clicking on search button once again to verify no search is being triggered");
//		patientPage.click(patientPage.searchButton);
//		recentSearchInfo = patientPage.getAllRecentSearchValues();
//		patientPage.verifyEquals(recentSearchInfo.size(),1,"Checkpoint[6/7]","Verifying there is only one recent search");
//		patientPage.verifyEquals(recentSearchInfo.get(0).get(PatientPageConstants.RECENT_SEARCH.get(3)),PatientPageConstants.TODAY,"Checkpoint[7/7]","Verifying Acquisition date in recent search");
//		
//		
//		
//	}
//
//	public String fetchDate(String input) throws InterruptedException
//	{
//		String datevalue="";
//		Date date=new Date();
//
//		if(input.equalsIgnoreCase(PatientPageConstants.TODAY)){
//
//			Date Today =DateUtils.addDays(new Date(), -0);
//			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
//			String formattedDate = sdf.format(Today);
//			return formattedDate;
//		}
//
//		else if(input.equalsIgnoreCase(PatientPageConstants.YESTERDAY)){
//
//			Date Yesterday =DateUtils.addDays(new Date(), -1);
//			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
//			String formattedDate = sdf.format(Yesterday);
//			return formattedDate;
//		}
//
//		else if(input.equalsIgnoreCase(PatientPageConstants.LASTWEEK)){
//
//			Date Week =DateUtils.addDays(new Date(), -7);
//			SimpleDateFormat sdf = new SimpleDateFormat(PatientPageConstants.STUDYDATEFORMAT);
//			String formattedDate = sdf.format(Week);
//			return formattedDate;
//		}
//		return datevalue;
//
//
//	}
//
//
//
//	@AfterMethod(alwaysRun=true)
//	public void AfterMethod() {
//
//		DatabaseMethods db = new DatabaseMethods(driver);
//		try {
//			db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.RECENTSEARCH);
//			db.updateAcquisitionDate(patientNameAh4, AcquisitionDate_AH4);
//			db.updateAcquisitionDate(patientNameGSPSMultiSeries, AcquisitionDate_GSPSPoint);
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//
//	}
//
//}
