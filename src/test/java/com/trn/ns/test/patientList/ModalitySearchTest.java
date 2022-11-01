package com.trn.ns.test.patientList;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;


import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ModalitySearchTest extends TestBase 
{
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private LoginPage loginPage;

	String filePath_Ah4Pdf=Configurations.TEST_PROPERTIES.get("AH4_pdf_filepath");
	String patientNameAh4Pdf = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_Ah4Pdf);

	String filePath_RANDENT=Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
	String patientNameRandoENT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_RANDENT);

	String filePath_TCGA=Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_TCGA);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
	}
	
	@Test(groups ={"chrome","IE","edge","US1856","BVT","Positive","E2E","F956"})
	public void test01_US1856_TC8756_verifyLookAndFeelWhenNoModalityIsSelectedForEurekaTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that nothing is displayed in collapsed view of Modality drop down when no modalities is selected.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		
		patientPage = new PatientListPage(driver);
		
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.modalityLabel), "Checkpoint[1/4]", "Verified that Modality label is visible on search panel.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[2/4]", "Verified modalities field in collapse mode.");
		
		List<String> modalities = patientPage.getModalities();
		
		for(int i=0;i<modalities.size();i++){
		patientPage.assertFalse(modalities.get(i).isEmpty(), "Checkpoint[3."+i+"/4]", "verifying that there is no blank button displayed for "+modalities.get(i));
		patientPage.assertFalse(patientPage.VerifyCrossIconInExpandModeWhenModalityIsSelected(modalities.get(i)), "Checkpoint[4."+i+"/4]", "Verified that cross icon is not visible for "+modalities.get(i));
	
		}
	
	}
	
	@Test(groups ={"chrome","IE","edge","US1856","Positive","E2E","F956"})
	public void test02_US1856_TC8775_TC8776_verifySelectionOfModalityForEurekaTheme() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can select and deselect  modalities from dropdown. <br>"+
		"Verify that selected modalities are visible under Modality header in expanded and collapsed both mode.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		patientPage = new PatientListPage(driver);

		List<String> modalities = patientPage.getModalities();
		
		//verify selection of modality
		patientPage.clickOnGivenModality(modalities.get(0));
		patientPage.assertEquals(patientPage.modalitiesButtons.size(),1, "Checkpoint[1/7]", "Verified that CT modality is selected in modality field in collapse mode.");
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(modalities.get(0)), "Checkpoint[2/7]", "Verified that CT modality is selected in modality field in expand mode.");
		
		//verify deselction of modality
		patientPage.deSelectModality(modalities.get(0));
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[3/7]", "Verified that CT modality is deselected in modality field in collapse mode.");
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(modalities.get(0)), "Checkpoint[4/7]", "Verified that CT modality is deselected in modality field in expand mode.");
		
		modalities = patientPage.getModalities();
		
		for(int i=0;i<modalities.size();i++)
		{
			patientPage.clickOnGivenModality(modalities.get(i));
			patientPage.assertTrue(patientPage.verifyModalityIsSelected(modalities.get(i)), "Checkpoint[5."+i+"/7]", "Verified in collapse mode modality "+modalities.get(i)+" is selected");
			patientPage.assertTrue(patientPage.VerifyModalityIsSelectedInExpandAndCollapseMode(modalities.get(i)),"Checkpoint[6."+i+"/7]","Verified that "+modalities.get(i)+" is visible as selected in both expand and collapse mode.");
			  
			if(patientPage.modalitiesButtons.size()>4)
				patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[7/7]", "Verified that ellipses are visible when "+patientPage.modalitiesButtons.size()+" are selected.");
		}
	
	}

	@Test(groups ={"chrome","IE","edge","US1856","US1858","DR2394","Positive","E2E","F956"})
	public void test03_US1856_TC8780_US1858_TC8934_DR2394_TC9394_DR2222_TC9028_verifyEllipsesWhenMultipleModalitiesSelectedForEurekaTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that ellipses and and on hovering tool tip is appearing on Modality header when more than 4 modalities are selected.<br>"+
		"Verify tooltip is getting displayed on the text which is getting cut on patient search panel.<br>"+
		"Re-execute TC8780: Verify that ellipses and and on hovering tool tip is appearing on Modality header when more than 4 modalities are selected. <br>"+
		"Verify the tool tip of modality dropdown as per the dark theme.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		patientPage = new PatientListPage(driver);
		
		//select 4 modality to see ellipses
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_CT_VALUE);
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_OT_VALUE);
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_PR_VALUE);
		patientPage.clickOnGivenModality(PatientPageConstants.UNKNOWN_MODALITY);
		
		patientPage.assertEquals(patientPage.modalitiesButtons.size(),4, "Checkpoint[1/7]", "Verified that 4 modalities is selected.");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[2/7]", "Verified that ellipses are visible.");
		
		patientPage.mouseHover(patientPage.ellipsesForModality);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.ellipsesForModality), ThemeConstants.EUREKA_POPUP_BACKGROUND, "Checkpoint[3/5]", "Verified that Eureka theme is applied on  modality label.");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(OrthancAndAPIConstants.ORTHANC_CT_VALUE), "Checkpoint[3/7]", "Verified that CT modality visible on tooltip on mousehover.");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(OrthancAndAPIConstants.ORTHANC_OT_VALUE), "Checkpoint[4/7]", "Verified that OT modality visible on tooltip on mousehover");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(OrthancAndAPIConstants.ORTHANC_PR_VALUE), "Checkpoint[5/7]", "Verified that PR modality visible on tooltip on mousehover");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(PatientPageConstants.UNKNOWN_MODALITY), "Checkpoint[6/7]", "Verified that UNKNOWN modality visible on tooltip on mousehover");
		
		//deselect unknown modality so no ellipse will be seen
		patientPage.deSelectModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[7/7]", "Verified that ellipses are not visible after deselecting Unknown modality.");
	}
	
	@Test(groups ={"chrome","IE","edge","US1856","DR2393","DR2451","Positive","E2E","F956"})
	public void test04_US1856_TC8782_TC8783_DR2393_TC9391_TC9392_DR2451_TC9784_verifyUnknownAndRTModalityForEurekaTheme()  
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Unknown/Empty , RT Struct modalities are getting displayed as 'Unknown, RTSTRUCT' in collapsed and expanded both mode.<br>"+
		"Verify that Modality dropdown is getting closed on clicking on upward arrow or clicking anywhere outside the Modality expanded area.<br>"+
		"Verify that there are no dots displayed when there is only one modality selected in the filter. <br>"+
		"[Risk and Impact]: Verify that modalities are displayed correctly in the filter header after resizing the browser. <br>"+
		"[Risk and Impact]: Verify that modality drop down is working as expected.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		patientPage = new PatientListPage(driver);
		
		//select RT modality
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_RT_VALUE), "Checkpoint[1/12]", "Verified that RTSTRUCT modality is selected");
		
		//select unknown modality
		patientPage.clickOnGivenModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(PatientPageConstants.UNKNOWN_MODALITY), "Checkpoint[2/12]", "Verified that UNKNOWN modality is selected");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[3/12]", "Verified that ellipses are not visible when unknown and RT modality is selected.");
		
		//deselect RT modality
		patientPage.deSelectModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_RT_VALUE), "Checkpoint[4/12]", "Verified that RTSTRUCT modality is deselected");
		
		//deselect unknown modality
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[5/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		
		//TC9392: verify ellipses on browser resize
		patientPage.resizeBrowserWindow(800,800);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[6/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[7/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		patientPage.maximizeWindow();
		
		patientPage.deSelectModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.deSelectModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(PatientPageConstants.UNKNOWN_MODALITY), "Checkpoint[8/12]", "Verified that UNKNOWN modality is deselected");
		
		//TC8783:Verify that Modality dropdown is getting closed on clicking on upward arrow or clicking anywhere outside the Modality expanded area.
		patientPage.click(patientPage.modalityButton);
		patientPage.assertFalse(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[9/12]", "Verified that Modality dropdown expand on click on dropdown arrow.");
		
		patientPage.click(patientPage.modalityButton);
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[10/12]", "Verified that Modality dropdown collapse on click on dropdown arrow.");
		
		patientPage.click(patientPage.modalityButton);
		patientPage.assertFalse(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[11/12]", "Verified that Modality dropdown expand on click on dropdown arrow.");
		
		patientPage.click(patientPage.patientListTab);
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[12/12]", "Verified that Modality dropdown collpase on click on patient list tab.");
		

	}
	
	@Test(groups ={"chrome","IE","edge","US1856","Positive","E2E","F956"})
	public void test05_US1856_TC8784_TC8826_verifySearchUsingModalityForEurekaTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can select multiple modalities at a time and on clicking on search button, patient list is getting filtered as per selected modalities.<br>"+
		"Verify that Reset button is clearing all the selected modalities from Modality field and Dropdown.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		patientPage = new PatientListPage(driver);
		
		//take count of all available patient name
		int patientCount=patientPage.patientNamesList.size();
		
		List<String> modalities=patientPage.getModalities();
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5), PatientPageConstants.CHECK);
		
		for(int i=0;i<modalities.size();i++)
		{
		patientPage.searchPatient("", modalities.get(i), "","");
		
		List<String>modalityList=patientPage.convertWebElementToStringList(patientPage.modalityNamesList);
		patientPage.assertFalse(modalityList.isEmpty(),"Checkpoint[1/4]","Verified that search is works for modality "+modalities.get(i));
		
		for(String modality:modalityList){
			if(modalities.get(i).equalsIgnoreCase(PatientPageConstants.UNKNOWN_MODALITY))
				patientPage.assertFalse(modality.contains(modalities.get(i)), "Checkpoint[2."+i+"/4]", "Verified that unknown modality is not visible in patient list.");	
			else
				patientPage.assertTrue(modality.contains(modalities.get(i)), "Checkpoint[2."+i+"/4]", "Verified that selected modality is present for patient.");
		}
		
		
		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientNamesList.size(),patientCount, "Checkpoint[3/4]", "Verified that original patient list is visible after click on clear.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[4/4]", "Verified that modality filter is also reset.");

		}
	
	}
	
	//DR2222:Dark theme doesn't work for Modality Drop down , Search and Reset buttons.
	@Test(groups ={"chrome","IE","edge","DR2222","BVT","Positive","E2E","F956"})
	public void test06_DR2222_TC9026_verifyEurekaThemeForModalityDropdown() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Eureka theme is getting applied over Modality dropdown .");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Eureka theme.");
		patientPage = new PatientListPage(driver);
		
		PagesTheme theme=new PagesTheme(driver);
		
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.modalityLabel), "Checkpoint[1/5]", "Verified that Modality label is visible on search panel.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[2/5]", "Verified modalities field in collapse mode.");
		patientPage.assertTrue(theme.verifyThemeOnLabel(patientPage.modalityLabel, ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[3/5]", "Verified that Eureka theme is applied on  modality label.");
		
		patientPage.click(patientPage.modalityButton);
		List<WebElement> modalities = patientPage.getAllModalities();
		for(WebElement modality:modalities)
		patientPage.assertTrue(theme.verifyThemeOnLabel(modality, ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[4/5]", "Verified that Eureka theme is applied on each tiles within the modality dropdown");
		
		patientPage.assertFalse(patientPage.isConsoleErrorPresent(), "Checkpoint[5/5]","Verified that console error is not present.");
	
	}
	
	@Test(groups ={"chrome","edge","DR2222","BVT","Positive"})
	public void test07_DR2222_TC9026_verifyDarkThemeForModalityDropdown()  
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Dark theme is getting applied over Modality dropdown .");
	    
		patientPage=new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in Dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();		
		loginPage.login(username, password);
        patientPage.waitForPatientPageToLoad();
		
		PagesTheme theme=new PagesTheme(driver);
		
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.modalityLabel), "Checkpoint[1/5]", "Verified that Modality label is visible on search panel.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[2/5]", "Verified modalities field in collapse mode.");
		patientPage.assertTrue(theme.verifyThemeOnLabel(patientPage.modalityLabel, ThemeConstants.DARK_THEME_NAME), "Checkpoint[3/5]", "Verified that Dark theme is applied on  modality label.");
		
		patientPage.click(patientPage.modalityButton);
		List<WebElement> modalities = patientPage.getAllModalities();
		for(WebElement modality:modalities)
		patientPage.assertTrue(theme.verifyThemeOnLabel(modality, ThemeConstants.DARK_THEME_NAME), "Checkpoint[4/5]", "Verified that Dark theme is applied on each tiles within the modality dropdown");
		
		patientPage.assertFalse(patientPage.isConsoleErrorPresent(), "Checkpoint[5/5]","Verified that console error is not present.");
	
	}
	
	@Test(groups ={"chrome","edge","US1856","DR2222","Positive","E2E","F956"})
	public void test08_US1856_TC8756_DR2222_TC9027_verifyLookAndFeelWhenNoModalityIsSelectedForDarkTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that nothing is displayed in collapsed view of Modality drop down when no modalities is selected.");
		
		patientPage = new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();		
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.modalityLabel), "Checkpoint[1/4]", "Verified that Modality label is visible on search panel.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[2/4]", "Verified modalities field in collapse mode.");
		
		List<String> modalities = patientPage.getModalities();
		
		for(int i=0;i<modalities.size();i++){
		patientPage.assertFalse(modalities.get(i).isEmpty(), "Checkpoint[3."+i+"/4]", "verifying that there is no blank button displayed for "+modalities.get(i));
		patientPage.assertFalse(patientPage.VerifyCrossIconInExpandModeWhenModalityIsSelected(modalities.get(i)), "Checkpoint[4."+i+"/4]", "Verified that cross icon is not visible for "+modalities.get(i));
	
		}
	
	}
	
	@Test(groups ={"chrome","edge","US1856","DR2222","Positive","E2E","F956"})
	public void test09_US1856_TC8775_TC8776_DR2222_TC9027_verifySelectionOfModalityForDarkTheme() 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can select and deselect  modalities from dropdown. <br>"+
		"Verify that selected modalities are visible under Modality header in expanded and collapsed both mode.");
		
		patientPage = new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();	
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();

		List<String> modalities = patientPage.getModalities();
		
		//verify selection of modality
		patientPage.clickOnGivenModality(modalities.get(0));
		patientPage.assertEquals(patientPage.modalitiesButtons.size(),1, "Checkpoint[1/7]", "Verified that CT modality is selected in modality field in collapse mode.");
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(modalities.get(0)), "Checkpoint[2/7]", "Verified that CT modality is selected in modality field in expand mode.");
		
		//verify deselction of modality
		patientPage.deSelectModality(modalities.get(0));
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[3/7]", "Verified that CT modality is deselected in modality field in collapse mode.");
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(modalities.get(0)), "Checkpoint[4/7]", "Verified that CT modality is deselected in modality field in expand mode.");
		
		modalities = patientPage.getModalities();
		
		for(int i=0;i<modalities.size();i++)
		{
			patientPage.clickOnGivenModality(modalities.get(i));
			patientPage.assertTrue(patientPage.verifyModalityIsSelected(modalities.get(i)), "Checkpoint[5."+i+"/7]", "Verified in collapse mode modality "+modalities.get(i)+" is selected");
			patientPage.assertTrue(patientPage.VerifyModalityIsSelectedInExpandAndCollapseMode(modalities.get(i)),"Checkpoint[6."+i+"/7]","Verified that "+modalities.get(i)+" is visible as selected in both expand and collapse mode.");
			  
			if(patientPage.modalitiesButtons.size()>4)
				patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[7/7]", "Verified that ellipses are visible when "+patientPage.modalitiesButtons.size()+" are selected.");
		}
	
	}

	@Test(groups ={"chrome","edge","US1856","US1858","DR2394","DR2222","Positive","F956"})
	public void test10_US1856_TC8780_US1858_TC8934_DR2394_TC9394_DR2222_TC9027_verifyEllipsesWhenMultipleModalitiesSelectedForDarkTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that ellipses and and on hovering tool tip is appearing on Modality header when more than 4 modalities are selected.<br>"+
		"Verify tooltip is getting displayed on the text which is getting cut on patient search panel.<br>"+
				"Re-execute TC8780: Verify that ellipses and and on hovering tool tip is appearing on Modality header when more than 4 modalities are selected.");
		
		patientPage = new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();	
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		
		List<String>modalities=patientPage.getModalities();
		List<String>selectedModality=new ArrayList<String>();
		
		for(int i=0;i<=4;i++)
			selectedModality.add(modalities.get(i));
		
		//select 4 modality to see ellipses
		patientPage.clickOnGivenModality(selectedModality.get(0));
		patientPage.clickOnGivenModality(selectedModality.get(1));
		patientPage.clickOnGivenModality(selectedModality.get(2));
		patientPage.clickOnGivenModality(selectedModality.get(3));
		patientPage.clickOnGivenModality(selectedModality.get(4));
		
		patientPage.assertEquals(patientPage.modalitiesButtons.size(),5, "Checkpoint[1/9]", "Verified that 4 modalities is selected.");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[2/9]", "Verified that ellipses are visible.");
		
		patientPage.mouseHover(patientPage.ellipsesForModality);
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.ellipsesForModality), ThemeConstants.DARK_POPUP_BACKGROUND, "Checkpoint[3/9]", "Verified that Eureka theme is applied on  modality label.");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(selectedModality.get(0)), "Checkpoint[4/9]", "Verified that "+selectedModality.get(0)+" modality visible on tooltip on mousehover.");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(selectedModality.get(1)), "Checkpoint[5/9]", "Verified that "+selectedModality.get(1)+" modality visible on tooltip on mousehover");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(selectedModality.get(2)), "Checkpoint[6/9]", "Verified that "+selectedModality.get(2)+" modality visible on tooltip on mousehover");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(selectedModality.get(3)), "Checkpoint[7/9]", "Verified that "+selectedModality.get(3)+" modality visible on tooltip on mousehover");
		patientPage.assertTrue(patientPage.getText(patientPage.tooltip).contains(selectedModality.get(4)), "Checkpoint[8/9]", "Verified that "+selectedModality.get(4)+" modality visible on tooltip on mousehover");
		
		//deselect unknown modality so no ellipse will be seen
		patientPage.deSelectModality(selectedModality.get(4));
		patientPage.deSelectModality(selectedModality.get(3));
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[9/9]", "Verified that ellipses are not visible after deselecting Unknown modality.");
	}
	
	@Test(groups ={"chrome","edge","US1856","DR2393","DR2222","Positive","E2E","F956"})
	public void test11_US1856_TC8782_TC8783_DR2393_TC9391_TC9392_DR2222_TC9027_verifyUnknownAndRTModalityForDarkTheme()  
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Unknown/Empty , RT Struct modalities are getting displayed as 'Unknown, RTSTRUCT' in collapsed and expanded both mode.<br>"+
		"Verify that Modality dropdown is getting closed on clicking on upward arrow or clicking anywhere outside the Modality expanded area.<br>"+
		"Verify that there are no dots displayed when there is only one modality selected in the filter. <br>"+
		"[Risk and Impact]: Verify that modalities are displayed correctly in the filter header after resizing the browser.");
		
		patientPage = new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();	
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		
		//select RT modality
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_RT_VALUE), "Checkpoint[1/12]", "Verified that RTSTRUCT modality is selected");
		
		//select unknown modality
		patientPage.clickOnGivenModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertTrue(patientPage.verifyModalityIsSelected(PatientPageConstants.UNKNOWN_MODALITY), "Checkpoint[2/12]", "Verified that UNKNOWN modality is selected");
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[3/12]", "Verified that ellipses are not visible when unknown and RT modality is selected.");
		
		//deselect RT modality
		patientPage.deSelectModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(OrthancAndAPIConstants.ORTHANC_RT_VALUE), "Checkpoint[4/12]", "Verified that RTSTRUCT modality is deselected");
		
		//deselect unknown modality
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[5/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		
		//TC9392: verify ellipses on browser resize
		patientPage.resizeBrowserWindow(800,800);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[6/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		
		patientPage.clickOnGivenModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.ellipsesForModality), "Checkpoint[7/12]", "Verified that ellipses are not visible when RT modality is deselected.");
		patientPage.maximizeWindow();
		
		patientPage.deSelectModality(OrthancAndAPIConstants.ORTHANC_RT_VALUE);
		patientPage.deSelectModality(PatientPageConstants.UNKNOWN_MODALITY);
		patientPage.assertFalse(patientPage.verifyModalityIsSelected(PatientPageConstants.UNKNOWN_MODALITY), "Checkpoint[8/12]", "Verified that UNKNOWN modality is deselected");
		
		//TC8783:Verify that Modality dropdown is getting closed on clicking on upward arrow or clicking anywhere outside the Modality expanded area.
		patientPage.click(patientPage.modalityButton);
		patientPage.assertFalse(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[9/12]", "Verified that Modality dropdown expand on click on dropdown arrow.");
		
		patientPage.click(patientPage.modalityButton);
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[10/12]", "Verified that Modality dropdown collapse on click on dropdown arrow.");
		
		patientPage.click(patientPage.modalityButton);
		patientPage.assertFalse(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[11/12]", "Verified that Modality dropdown expand on click on dropdown arrow.");
		
		patientPage.click(patientPage.patientListTab);
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[12/12]", "Verified that Modality dropdown collpase on click on patient list tab.");
		

	}
	
	@Test(groups ={"chrome","edge","US1856","DR2222","Positive","E2E","F956"})
	public void test12_US1856_TC8784_TC8826_DR2222_TC9027_verifySearchUsingModalityForDarkTheme() throws InterruptedException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can select multiple modalities at a time and on clicking on search button, patient list is getting filtered as per selected modalities.<br>"+
		"Verify that Reset button is clearing all the selected modalities from Modality field and Dropdown.");
		
		patientPage = new PatientListPage(driver);
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Patient list page loaded in dark theme.");
		loginPage = new LoginPage(driver);		
		loginPage.logout();			
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		
		
		//take count of all available patient name
		int patientCount=patientPage.patientNamesList.size();
		
		List<String> modalities=patientPage.getModalities();
		patientPage.checkOrUncheckColumn(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0),PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5), PatientPageConstants.CHECK);
		
		for(int i=0;i<modalities.size();i++)
		{
		patientPage.searchPatient("", modalities.get(i), "","");
		
		List<String>modalityList=patientPage.convertWebElementToStringList(patientPage.modalityNamesList);
		patientPage.assertFalse(modalityList.isEmpty(),"Checkpoint[1/4]","Verified that search is works for modality "+modalities.get(i));
		
		for(String modality:modalityList){
			if(modalities.get(i).equalsIgnoreCase(PatientPageConstants.UNKNOWN_MODALITY))
				patientPage.assertFalse(modality.contains(modalities.get(i)), "Checkpoint[2."+i+"/4]", "Verified that unknown modality is not visible patient.");	
			else
		        patientPage.assertTrue(modality.contains(modalities.get(i)), "Checkpoint[2."+i+"/4]", "Verified that selected modality is present for patient.");
		}
		
		patientPage.click(patientPage.clearButton);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertEquals(patientPage.patientNamesList.size(),patientCount, "Checkpoint[3/4]", "Verified that original patient list is visible after click on clear.");
		patientPage.assertTrue(patientPage.modalitiesButtons.isEmpty(), "Checkpoint[4/4]", "Verified that modality filter is also reset.");

		}
	
	}
	
	@AfterMethod
	public void revertDefaultTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
		
		
	}

}
