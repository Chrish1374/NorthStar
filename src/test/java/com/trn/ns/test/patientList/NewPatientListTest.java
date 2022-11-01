package com.trn.ns.test.patientList;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.Tooltip;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class NewPatientListTest extends TestBase 
{
	private PatientListPage patientPage;
	private LoginPage loginPage;
	private ExtentTest extentTest;

	String filePath_AH4=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath_AH4);
	String studyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath_AH4);

	String filePathPointMultiseries=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNameGSPS = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePathPointMultiseries);
	String patientIDGSPS = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY,filePathPointMultiseries);

	String filepathGSPSText =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
	String PatientNameGSPSText = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepathGSPSText);

	String gspsMultiSeriesFilepath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNameGSPSMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,gspsMultiSeriesFilepath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
	}	

	@Test(groups ={"Chrome","Edge","IE11","Positive","US2011","US1865","US1866","F957","E2E","F966"})
	public void test01_US1865_TC8683_US2011_TC9002_US1866_TC8714_verifyPatientPage() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new Patient list page display. <br>"+
				"Re-execute US1865 test cases.<br>"+
				"Verify the tab strip component added to patient list.");	

		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/12]", "Verifying the patient's url");

		PagesTheme theme = new PagesTheme(driver);
		

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.patientListTab), "Checkpoint[2/6]", "Verifying the patient list tab");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.searchAndViewedHistoryTab), "Checkpoint[3/6]", "verifying the recently viewed tab");
		patientPage.assertEquals(patientPage.patientIdsList.size(), patientPage.patientNamesList.size(), "Checkpoint[4/6]","verifyinh the data is displayed");
		

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.patientListTab), "Checkpoint[2/12]", "Verifying the patient list tab");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.searchAndViewedHistoryTab), "Checkpoint[3/12]", "verifying the recently viewed tab");
		patientPage.assertEquals(patientPage.patientIdsList.size(), patientPage.patientNamesList.size(), "Checkpoint[4/12]","verifyinh the data is displayed");


		for(WebElement row : patientPage.patientRows)
			patientPage.assertEquals(patientPage.getCssValue(row, NSGenericConstants.TABLE_BORDER_BOTTOM),PatientPageConstants.EUREKA_ROW_BOTTOM_BORDER_COLOR,"Checkpoint[5/12]","verifying the row strips");

		patientPage.assertEquals(patientPage.getCssValue(patientPage.patientRows.get(0), NSGenericConstants.CSS_PRO_BACKGROUND),PatientPageConstants.EUREKA_ROW_SELECTED_COLOR,"Checkpoint[6/12]","verifying the default first row is highlighted");

		patientPage.assertTrue(theme.verifyThemeOnTableHeader(patientPage.patientListTab, ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[7/12]", "Verified font color for active patient list tab.");
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.patientListTab),ThemeConstants.EUREKA_BACKGROUND_COLOR, "Checkpoint[8/12]", "Verified background color for active patient list tab.");
		
		patientPage.assertEquals(patientPage.getColorOfRows(patientPage.searchAndViewedHistoryTab),ViewerPageConstants.SLIDER_DISABLE_COLOR, "Checkpoint[9/12]", "Verified font color for inactive recently viewed patient tab.");
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchAndViewedHistoryTab),ThemeConstants.EUREKA_TABLE_BACKGROUND, "Checkpoint[10/12]", "Verified background color for inactive recently viewed patient tab.");
		
		patientPage.mouseHover(patientPage.searchAndViewedHistoryTab);
		patientPage.assertTrue(theme.verifyThemeOnLabel(patientPage.searchAndViewedHistoryTab,ThemeConstants.EUREKA_THEME_NAME), "Checkpoint[11/12]", "Verified font color for recently viewed patient tab on mousehover.");
		patientPage.assertEquals(patientPage.getBackgroundColor(patientPage.searchAndViewedHistoryTab),ThemeConstants.EUREKA_BACKGROUND_COLOR, "Checkpoint[12/12]", "Verified background color for recently viewed patient tab on mousehover.");


	}

	@Test(groups ={"Chrome","Edge","IE11","Positive","US2011","US1865","F957","F966"})
	public void test02_US1865_TC8684_US2011_TC9002_verifySorting() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Patient List table sorting functionalities. <br>"+
				"Re-execute US1865 test cases.");	

		patientPage = new PatientListPage(driver);
		patientPage.scrollIntoView(patientPage.acqutisionHeader);

		patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[1/6]","Verifying default patient list is sorted based on descending order");
		patientPage.click(patientPage.acqutisionHeader);
		patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[2/6]","Verifying default patient list is sorted based on ascending order after click on header");


		for(String header : PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS) {

			WebElement tableEle = patientPage.getColumnHeader(header);
			if(!tableEle.isDisplayed()) {
				patientPage.scrollIntoView(tableEle);
				patientPage.waitForElementVisibility(tableEle);
			}

			patientPage.click(tableEle);
			patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getColumnHeader(patientPage.getText(tableEle)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[3/6]","verifying the column is sorted in ascending order");

			patientPage.waitForEndOfAllAjaxes();
			List<String> columnValues = patientPage.getColumnValue(header);
			Collections.reverse(columnValues);

			patientPage.click(tableEle);
			patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getColumnHeader(patientPage.getText(tableEle)),PatientPageConstants.DSC_CONSTANT),"Checkpoint[5/6]","verifyiing the column is sorted descending");
			patientPage.assertEquals(patientPage.getColumnValue(header),columnValues,"Checkpoint[6.2/6]","verifying the list is reversed in descending order");

			patientPage.mouseHover(patientPage.studyDescriptionHeader);

		}

	}

	@Test(groups ={"Chrome","Edge","IE11","Positive","US2011","F966","E2E"})
	public void test03_US2011_TC8844_DR2277_TC9077_verifyStudyPageUI() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the new StudyList pane UI on PatientList(patients2) page.");	

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameGSPS);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/23]", "Verifying the patient's url");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.patientNameHeaderInPatientInfo), "Checkpoint[2/23]", "Verified that Patient Information section is visible on Study Pane");

		//verify patient information section on study page
		patientPage.assertEquals(patientPage.getText(patientPage.patientNameHeaderInPatientInfo), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1), "Checkpoint[3/23]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(1)+" is visible on study page.");
		//DR2277 - Gender/Sex mismatch on patient list
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify 'Gender' is updated to 'Sex' on StudyList pane.");
		patientPage.assertEquals(patientPage.getText(patientPage.sexHeaderInPatientInfo), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[4/23]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(2)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.dateOfBirthHeaderInPatientInfo), PatientPageConstants.DATEOFBIRTH, "Checkpoint[5/23]", "Verified that "+ PatientPageConstants.DATEOFBIRTH.replace(":", "")+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.ageHeaderInPatientInfo), PatientPageConstants.AGE, "Checkpoint[6/23]", "Verified that "+PatientPageConstants.AGE.replace(":", "")+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.acquisitionDateHeaderInPatientInfo), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4), "Checkpoint[7/23]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.patientIDHeaderInPatientInfo), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0), "Checkpoint[8/23]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(0)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.modalityHeaderInPatientInfo), PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5), "Checkpoint[9/23]", "Verified that "+PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(5)+" is visible on study page.");

		//verify study list pane
		patientPage.assertEquals(patientPage.getText(patientPage.accessionNoHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(0), "Checkpoint[10/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(0)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDescriptionHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(1), "Checkpoint[11/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(1)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.eurekaHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[12/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(2)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.imagesHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3), "Checkpoint[13/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(3)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.modalityHeaderOnStudyPage), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(4), "Checkpoint[14/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(4)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.studyDateTimeHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(5), "Checkpoint[15/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(5)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.referringPhysicianHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6), "Checkpoint[16/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)+" is visible on study page.");
		patientPage.assertEquals(patientPage.getText(patientPage.institutionHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(7), "Checkpoint[17/23]", "Verified that "+PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(7)+" is visible on study page.");

		patientPage.assertTrue(patientPage.verifyTextOverFlowForDataWraping(patientPage.patientInformationOnStudyPage.get(5)), "Checkpoint[18/23]", "Verified that ellipses visible when text is long for Patient information column.");
		patientPage.mouseHover(patientPage.patientInformationOnStudyPage.get(5));
		patientPage.assertEquals(patientPage.getText(patientPage.patientInformationOnStudyPage.get(5)), patientIDGSPS, "Checkpoint[19/23]", "Verified tooltip of Patient ID on mouse hover when ellipes are present.");

		patientPage.assertTrue(patientPage.verifyTextOverFlowForDataWraping(patientPage.studyDateTimeHeader), "Checkpoint[20/23]", "Verified that ellipses are visible for header when header text is long.");
		patientPage.mouseHover(patientPage.studyDateTimeHeader);
		patientPage.assertEquals(patientPage.getText(patientPage.studyDateTimeHeader), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(5), "Checkpoint[21/23]", "Verified value of header when ellipses are present.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify study UI page on browser resize.");
		patientPage.resizeBrowserWindow(900, 700);

		for(String header : PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS) {

			WebElement tableEle = patientPage.getStudyColumnHeader(header);
			if(!tableEle.isDisplayed()) {
				patientPage.scrollIntoView(tableEle);
				patientPage.waitForElementVisibility(tableEle);
			}
			patientPage.assertTrue(patientPage.isElementPresent(tableEle), "Checkpoint[22/23]", "Verified that "+tableEle+" is present on browser resize as well.");	
		}	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify patient Information on study page on browser resize.");
		for(WebElement header : patientPage.patientInformationHeaderOnStudyPage) {

			WebElement tableEle =header;
			if(!tableEle.isDisplayed()) {
				patientPage.scrollIntoView(tableEle);
				patientPage.waitForElementVisibility(tableEle);
			}
			patientPage.assertTrue(patientPage.isElementPresent(tableEle), "Checkpoint[23/23]", "Verified that "+tableEle+" is present on browser resize as well.");	
		}	
	}

	@Test(groups ={"Chrome","Edge","IE11","Positive","US2011","F966"})
	public void test04_US2011_TC8875_verifySortingAndOrderingOnStudyListPage() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sorting and ordering of columns on StudyList pane.");	

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameGSPS);
		patientPage.waitForStudyToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/5]", "Verified the patient page URL.");

		for(String header : PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS) {

			WebElement tableEle = patientPage.getStudyColumnHeader(header);

			patientPage.click(tableEle);

			patientPage.assertEquals(patientPage.getTooltipOnTableHeader(patientPage.getStudyColumnHeader(patientPage.getText(tableEle))),header+", "+PatientPageConstants.ASCENDING_ORDER_TEXT,"Checkpoint[2/5]","verifying the tooltip displayed for ascending order");
			List<String> columnValues = patientPage.getStudyColumnValue(header);
			Collections.reverse(columnValues);
			patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getStudyColumnHeader(patientPage.getText(tableEle)),PatientPageConstants.ASC_CONSTANT),"Checkpoint[3/5]","verifying the column is sorted in ascending order");

			patientPage.click(tableEle);
			patientPage.assertTrue(patientPage.verifySortIcon(patientPage.getStudyColumnHeader(tableEle.getText()),PatientPageConstants.DSC_CONSTANT),"Checkpoint[4/5]","verifyiing the column is sorted descending");
			patientPage.assertEquals(patientPage.getTooltipOnTableHeader(patientPage.getStudyColumnHeader(patientPage.getText(tableEle))),header+", "+PatientPageConstants.DESCENDING_ORDER_TEXT,"Checkpoint[5.1/6]","verifying the tooltip is showing order is descending");
			patientPage.assertEquals(patientPage.getStudyColumnValue(header),columnValues,"Checkpoint[5.2/5]","verifying the list is reversed in descending order");

		}
	}

	//DR2303: Envoy AI tool tip remains opened even user not hovering mouse on (E) icon and user has moved focus to some other component.
	@Test(groups ={"US2011","DR2303","Chrome","Edge","IE11","Positive","F966","E2E"})
	public void test05_US2011_TC9004_DR2303_TC9181_verifyEurekaIconOnStudyListPane() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Eureka icon on StudyList pane and the list of algorithms displayed on mouse hover. <br>"+
				"[V1.0.4] Verify the tool tip displayed when mouse hovered on Eureka AI is hidden when mouse cursor is moved away from the icon.");	

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientNameGSPSText);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[1/11]", "Verified patient page URL.");
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[2/11]","Verified Eureka icon on study list page.");
		patientPage.assertEquals(patientPage.convertIntoInt(patientPage.getText(patientPage.numberOfResults)),2,"Checkpoint[3/11]","Verified number of results displayed on Eureka icon");

		patientPage.searchPatient(patientNameGSPS, "", "","");
		patientPage.clickOnPatientRow(patientNameGSPS);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.eurekaIcon),"Checkpoint[4/11]","Verified Eureka icon on study list page.");

		patientPage.mouseHover(patientPage.eurekaIcon);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineNameOnEurekaAl), "Checkpoint[5/11]", "Verified that Machine name visible on mousehover on Eureka AI Icon.");
		patientPage.mouseHover(patientPage.studyDescriptionHeader);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.machineNameOnEurekaAl), "Checkpoint[6/11]", "Verified that tooltip not visible when mouse cursor move away from Eureka AI Icon.");

		patientPage.mouseHover(patientPage.eurekaIcon);
		patientPage.mouseHover(patientPage.machineNameOnEurekaAl);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.machineNameOnEurekaAl), "Checkpoint[7/11]", "Verified that Machine name visible on mousehover on Eureka AI Icon.");
		patientPage.mouseHover(patientPage.studyDescriptionHeader);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.machineNameOnEurekaAl), "Checkpoint[8/11]", "Verified that tooltip not visible when mouse cursor move away from Eureka AI Icon.");

		//pending icon
		patientPage.searchPatient(patientNameAh4, "", "","");
		patientPage.clickOnPatientRow(patientNameAh4);

		patientPage.assertTrue(patientPage.isElementPresent(patientPage.pendingIcon),"Checkpoint[9/11]","Verified Pending icon on study list page.");

		//warning icon
		DatabaseMethods	db=new DatabaseMethods(driver);
		db.updateWiaCloudResultInStudyTable(studyDescription, 2);
		patientPage.refreshWebPage();
		loginPage.login(username,password);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientNameAh4);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.warningIcon),"Checkpoint[10/11]","Verified warning icon on study list page.");

		//disable warning icon
		db.updateWiaCloudResultInStudyTable(studyDescription, 4);
		patientPage.refreshWebPage();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		patientPage.clickOnPatientRow(patientNameAh4);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.disableEurekaIcon),"Checkpoint[11/11]","Verified disable Eureka icon on study list page.");


	}

	@Test(groups ={"Chrome","Edge","IE11","Positive","US2011","F966"})
	public void test06_US2011_TC8881_verifyNavigationFromStudyPageToViewer() throws InterruptedException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify studies are loaded smoothly on StudyList pane by quickly selecting the different patients and verify loading the study on viewer.");	

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNameGSPS);

		patientPage.clickOnStudy(1);
		ViewerPage viewerPage=new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);
		ViewerLayout layout = new ViewerLayout(driver);
		viewerPage.assertEquals(viewerPage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Checkpoint[1/3]", "Verified number of canvas on viewer load.");
		viewerPage.assertFalse(viewerPage.isConsoleErrorPresent(), "Checkpoint[2/3]", "Verified that no console error observed while loading viewer page.");

		viewerPage.browserBackWebPage();		
		patientPage.waitForStudyToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[3/3]", "Verified that Patient list page loaded properly on navigate back");
	}

	@Test(groups ={"Chrome","Edge","IE11", "DE1915","DE2321","Negative"})
	public void test07_DE1915_TC7753_TC7754_DE2321_TC9213_verifyVisibilityOfLastPatientOnPatientPage() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription(
				"Verify the last patient is getting displayed after scrolling at the end of the patient list page, when there are more number of patients. <br>" +
						"Verify no empty space is displayed at the bottom of the the patient list page when the search pane in the collapsed and when there are more number of patients. <br>"+
						"Verify that last row is visible in the patient listings when there are many patients imported in the application."

				);

		patientPage = new PatientListPage(driver);

		//get all patient name WebElements 
		List <WebElement> list = patientPage.patientNamesList;
		int n = list.size();

		//get the first element 
		//scroll to the end of the page and validate the last patient

		patientPage.scrollIntoView(list.get(n-1));
		patientPage.assertTrue(list.get(n-1).isDisplayed(),"Checkpoint[1/4]","Verifying the last patient is displayed on the patient page");

		//Now search 3 times in the expanded panel to make the entry in recent search

		patientPage.searchAndReset(patientNameGSPSMultiSeries,"","","");
		patientPage.searchAndReset("","",PatientPageConstants.FEMALE,"");
		patientPage.searchAndReset(patientNameGSPSMultiSeries,OrthancAndAPIConstants.ORTHANC_CT_VALUE,"",PatientPageConstants.MALE);

		//Collapse the search panel and validate the last patient
		patientPage.click(patientPage.toggleButton);
		patientPage.scrollIntoView(list.get(n-1));
		patientPage.assertTrue(list.get(n-1).isDisplayed(),"Checkpoint[2/4]","Verifying the last patient is displayed on the patient page when search panel is collapsed with recent search");

		//Expand the search panel and validate the last patient 
		patientPage.click(patientPage.toggleButton);
		patientPage.scrollIntoView(list.get(n-1));
		patientPage.assertTrue(list.get(n-1).isDisplayed(),"Checkpoint[3/4]","Verifying the last patient is displayed on the patient page when search panel is expanded with recent search");

		// Click on the patient and redirect to study page and validate the last patient
		patientPage.clickOnPatientRow(patientNameGSPSMultiSeries);	
		patientPage.browserBackWebPage();
		patientPage.scrollIntoView(list.get(n-1));
		patientPage.assertTrue(list.get(n-1).isDisplayed(),"Checkpoint[4/4]","Verifying the last patient is displayed on the patient page when user navigates from study to patinet");


	}

	@Test(groups ={"firefox","Chrome","Edge","IE11"})
	public void test08_US586_TC1887_verifyAboutLinkFromPatientList() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify licensing Information using About link from Patient list page");

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

		Header header = new Header(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Scan drop down menu is present on top left on page");
		patientPage.assertTrue(header.userInfo.isDisplayed(), "Verifying that Scan drop down menu is present on top left of Patient List Page", "Scan drop down menu is present on left corner of page");
		header.viewAboutPage();
		header.switchToNewWindow(2);
		header.maximizeWindow();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify About page opens in a new tab");
		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verifying the About Page is open", "User is on About page "+ loginPage.getCurrentPageURL());
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Hammer JS licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.HAMMERJS), "Verifying Hammer JS licensing information is present", "The Hammer JS licensing information is present on page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify ng-bootstrap licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.BOOTSTARP), "Verifying ng-bootstrap licensing information is present", "The ng-bootstrap licensing information is present on page");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Newtonsoft.Json licensing Information is present on About page");
		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.NEWTONSOFT), "Verifying Newtonsoft.Json licensing information is present", "The Newtonsoft.Json licensing information is present on page");

	}

	@Test(groups ={"Chrome","Edge","IE11","DR2383","Negative"})
	public void test09_DR2383_TC9331_verifyTooltipIsNotGettingCut() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that full tooltip displays  at the far right column header of study list and patient list pans.");

		int x1=1100;
		int y1=500;
		patientPage = new PatientListPage(driver);
		Tooltip tooltipOnPage = new Tooltip(driver);
		patientPage.assertTrue(tooltipOnPage.verifyTooltipOnResize(patientPage.getColumnHeader(PatientPageConstants.PATIENT_PAGE_COLUMN_HEADERS.get(4)),x1,y1),"Checkpoint[1/3]","Verifying the Acquisition header tooltip is not cut");
		patientPage.assertTrue(tooltipOnPage.verifyTooltipOnResize(patientPage.getColumnHeader(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(5)),x1,y1),"Checkpoint[2/3]","Verifying the study date time header tooltip is not cut");
		patientPage.assertTrue(tooltipOnPage.verifyTooltipOnResize(patientPage.getColumnHeader(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(6)),x1,y1),"Checkpoint[3/3]","Verifying the Referring physician header tooltip is not cut");
	}

	@Test(groups ={"Chrome","Edge","IE11","DR2364","Negative"})
	public void test10_DR2364_TC9402_verifyStudyListPage() throws InterruptedException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that StudyList URL is removed/deprecated from  the URL bar of the browser.");

		patientPage = new PatientListPage(driver);
		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.STUDY_LIST_URL);

		loginPage = new LoginPage(driver);
		loginPage.waitForLoginPageToLoad();
		loginPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.LOGIN_PAGE_URL), "Checkpoint[1/2]", "verify the Studylist URL is no more accessible");

		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/2]", "Verify after login user is navigated to patient study URL");


	}
	
	@Test(groups ={"Chrome","Edge","IE11","DR2372","Positive","F966","E2E"})
	public void test11_DR2372_TC9428_verifyStudyListPage() throws InterruptedException, AWTException
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that scroll bar is transiting properly in study List page on click over the empty space of perfect scrollbar");

		patientPage = new PatientListPage(driver);
		
		int x1=1366;
		int y1=768;
		
		Dimension dimension = new Dimension(x1, y1);
		String parentWindow = patientPage.getCurrentWindowID();
		patientPage.setWindowSize(parentWindow, dimension);
		
		int totalHeader = patientPage.studyTableHeaders.size();
		
		int x =patientPage.getXCoordinate(patientPage.studyListHoriScrollbarThumb);
		int y =patientPage.getYCoordinate(patientPage.studyListHoriScrollbarThumb);
		int width =patientPage.getWidthOfWebElement(patientPage.studyListHoriScrollbarThumb);
		int newx = x+width+30;
		
		patientPage.mouseMove(newx, y+90);
		patientPage.leftClick();
		
        patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyTableHeaders.get(totalHeader-1)), "Checkpoint[1/7]", "Verifying that on click on right empty space scroll happens and last column displayed");
        patientPage.assertFalse(patientPage.isElementPresent(patientPage.studyTableHeaders.get(0)), "Checkpoint[2/7]", "Verifying that on click on right empty space scroll happens and first column is not displayed");
		patientPage.waitForTimePeriod(1000);
		
		patientPage.mouseMove(x+30, y+90);
		patientPage.leftClick();
        patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyTableHeaders.get(0)), "Checkpoint[3/7]", "Verifying that on click of left empty scroll space the first column is displayed");
		patientPage.waitForTimePeriod(1000);
		
		
		x1=1100;
		y1=700;
		
		dimension = new Dimension(x1, y1);
		patientPage.setWindowSize(parentWindow, dimension);
		patientPage.waitForTimePeriod(1000);
		
		x =patientPage.getXCoordinate(patientPage.studyListHoriScrollbarThumb);
		y =patientPage.getYCoordinate(patientPage.studyListHoriScrollbarThumb);
		width =patientPage.getWidthOfWebElement(patientPage.studyListHoriScrollbarThumb);
		
		newx = x+width+20;
        patientPage.click(newx, y);
        patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyTableHeaders.get(totalHeader-1)), "Checkpoint[4/7]", "Verifying that on click on right empty space, scroll happens and last column displayed");
        patientPage.assertFalse(patientPage.isElementPresent(patientPage.studyTableHeaders.get(0)), "Checkpoint[5/7]", "Verifying that on click on right empty space, scroll happens and first column is not displayed");
		patientPage.waitForTimePeriod(1000);
				
		patientPage.mouseMove(x+100, y+90);
		patientPage.leftClick();
        patientPage.assertFalse(patientPage.isElementPresent(patientPage.studyTableHeaders.get(totalHeader-1)), "Checkpoint[6/7]", "Verifying that on click on left empty space, scroll happens and last column is not displayed");
        patientPage.assertTrue(patientPage.isElementPresent(patientPage.studyTableHeaders.get(0)), "Checkpoint[7/7]", "Verifying that on click on left empty space, scroll happens and first column is displayed");
        patientPage.waitForTimePeriod(1000);
		

	}
	@Test(groups ={"Chrome","IE11","Edge","DR2359","Positive"})
	public void test12_DR2359_TC9278_verifyDefaultColumnsOnPatientsPage() throws SQLException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that patient list shows only three columns by default");

		PatientListPage patientPage = new PatientListPage(driver);

		patientPage.assertEquals(patientPage.displayedPatientTableHeaders.size(),3,"Checkpoint[1/2]","Verified that dafault three columns are visible on the patients page, in terms of count.");
		for(int i=0;i<patientPage.displayedPatientTableHeaders.size();i++)
		{
			String columnName=patientPage.getText(patientPage.displayedPatientTableHeaders.get(i));
			
			patientPage.assertEquals(db.getHiddenColumnFlagValue(columnName), NSDBDatabaseConstants.NONADMINUSER, "Checkpoint[2/2]", "Verified that default columns are three on the patients section of patient study page, in terms of visibility.");
					
		}

		}

	@Test(groups ={"Chrome","IE11","Edge","DR2451","Positive"})
	public void test13_DR2451_TC9783_verifyActiveDropDownIsClosedAfterAnotherDropdownClick() throws SQLException 
	{                      
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that machine drop down is closed when user clicks on another drop down");

		PatientListPage patientPage = new PatientListPage(driver);

		patientPage.click(patientPage.machineDropdownButton);
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.expandedMachineDropDown), "Checkpoint[1/5]", "Verified that machine dropdown is expanded.");

		patientPage.click(patientPage.modalityButton);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.expandedMachineDropDown), "Checkpoint[2/5]", "Verified that machine dropdown is closed after the click on modality dropdown.");
		
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.expandedModalityDropDown), "Checkpoint[3/5]", "Verified that modality dropdown is displayed");
		
		patientPage.click(patientPage.acquisitiondateButton);
		patientPage.assertFalse(patientPage.isElementPresent(patientPage.expandedModalityDropDown), "Checkpoint[4/5]", "Verified that modality dropdown is closed after the click on aquisition date dropdown.");
	
		patientPage.assertTrue(patientPage.isElementPresent(patientPage.expandedAcquisitionDateDropDown), "Checkpoint[5/5]", "Verified that acquisition dropdown is expanded");
		
	}

	//US2389:Return to same patient when going back to patient list from viewer
	@Test(groups ={"Chrome","Edge","IE11","Positive","US2389","DR2570","E2E","F1081"})
	public void test14_US2389_TC10036_DR2570_TC10310_verifyPatientRowWhenNavBackUsingPatientIcon() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User returns to the same patient when returning to PatientList page from Viewer - Back button on tool bar.<br>"+
		"Verify 'Patients' on viewer tool bar with UI login.");	

		patientPage = new PatientListPage(driver);
		db=new DatabaseMethods(driver);
		ViewerPage viewerpage=new ViewerPage(driver);
		String firstPatient=patientPage.getText(patientPage.patientNamesList.get(0));
		
		//select patient
		patientPage.clickOnPatientRow(firstPatient);
		String studyDesc=patientPage.getStudyDescription(0);
		String studyInstanceUID=db.getStudyInstanceUID(firstPatient);
		//load viewer page
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID), "Checkpoint[1/10]", "Verified that viewer page is loaded for first selected patient.");
		
		//navigate back to patient using patient back icon and verify selected patient.
		patientPage.click(viewerpage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/10]", "Verified that user is navigate back to patient list page using patient icon.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(firstPatient, studyDesc), "Checkpoint[3/10]", "Verified patient row and study info for the selected patient.");
					
		//select patient which is not visible on default patient list
		patientPage.clickOnPatientRow(imbioPatientName);
		studyDesc=patientPage.getStudyDescription(0);
		studyInstanceUID=db.getStudyInstanceUID(imbioPatientName);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID), "Checkpoint[4/10]","Verified that viewer page is loaded for selected patient.");
		
		//navigate back using patient icon and verify selected patient.
		viewerpage.assertTrue(viewerpage.isEnabled(viewerpage.patientsIcon), "Checkpoint[5/10]", "Verified that patient icon is enable on viewer toolbar.");
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON, "Checkpoint[6/10]", "Verified that patient icon is enable on viewer toolbar.");
		patientPage.click(viewerpage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.waitForTimePeriod(1000);
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[7/10]",  "Verified that user is navigate back to patient list page using patient icon.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(imbioPatientName, studyDesc), "Checkpoint[8/10]", "Verified patient row and study info for the selected patient.");

		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID), "Checkpoint[9/10]","Verified that viewer page is loaded for selected patient.");
		patientPage.refreshWebPage();
		LoginPage login=new LoginPage(driver);
		login.login(username, password);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.assertEquals(viewerpage.getCssValue(viewerpage.patientsIcon, NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[10/10]", "Verified that patient icon is disable on viewer toolbar after browser refresh.");
		
	}
	
	@Test(groups ={"Chrome","Edge","IE11","Positive","US2389","F1081"})
	public void test15_US2389_TC10036_verifyPatientRowWhenNavBackUsingBrowser() throws InterruptedException, SQLException
	{	 
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User returns to the same patient when returning to PatientList page from Viewer - Browser Back button.");	

		patientPage = new PatientListPage(driver);
		db=new DatabaseMethods(driver);
		ViewerPage viewerpage=new ViewerPage(driver);
		String firstPatient=patientPage.getText(patientPage.patientNamesList.get(0));
		
		//select patient
		patientPage.clickOnPatientRow(firstPatient);
		String studyDesc=patientPage.getStudyDescription(0);
		String studyInstanceUID=db.getStudyInstanceUID(firstPatient);
		//load viewer page
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID),"Checkpoint[1/6]","Verified that viewer page is loaded for first selected patient.");
		
		//navigate back to patient using browser back and verify selected patient.
		patientPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL),"Checkpoint[2/6]","Verified that user is navigate back to patient list page using browser back.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(firstPatient, studyDesc),"Checkpoint[3/6]","Verified patient row and study info for the selected patient.");
					
		//select patient which is not visible on default patient list
		patientPage.clickOnPatientRow(imbioPatientName);
		studyDesc=patientPage.getStudyDescription(0);
		studyInstanceUID=db.getStudyInstanceUID(imbioPatientName);
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID),"Checkpoint[4/6]","Verified that viewer page is loaded for selected patient.");
		
		//navigate back using browser back and verify selected patient.
		patientPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[5/6]", "Verified that user is navigate back to patient list page using browser back.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(imbioPatientName, studyDesc),"Checkpoint[6/6]", "Verified patient row and study info for the selected patient.");

	}
	
	@Test(groups ={"Chrome","Edge","IE11","US2389","Positive","F1081","E2E"})
	public void test16_US2389_TC10038_verifyPatientNavForNonUILogin() throws InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User returns to the same patient when returning to PatientList page from Viewer - Non-UI login.");
		
		db= new DatabaseMethods(driver);
		ViewerPage viewerpage=new ViewerPage(driver);
		String id = db.getPatientID(imbioPatientName);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,id);
		loginPage = new LoginPage(driver);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
	
		patientPage = new PatientListPage(driver);
		String studyDesc=patientPage.getStudyDescription(0);
		String studyInstanceUID=db.getStudyInstanceUID(imbioPatientName);
		
		//load viewer page 
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID), "Checkpoint[1/6]", "Verified that viewer page is loaded for selected patient.");
		
		//navigate back using Browser back button and verify row is selected or not
		patientPage.navigateToBack();
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[2/6]", "Verified that user is navigate back to patient list page using browser back.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(imbioPatientName, studyDesc), "Checkpoint[3/6]", "Verified patient row and study info for the selected patient.");
					
	    //againg load viewerpage
		patientPage.clickOntheFirstStudy();
		viewerpage.waitForViewerpageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID), "Checkpoint[4/6]", "Verified that viewer page is loaded for selected patient.");
		
		//navigate back using patient back icon and verify row is selected or not
		patientPage.click(viewerpage.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		patientPage.assertTrue(patientPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL), "Checkpoint[5/6]","Verified that user is navigate back to patient list page using patient icon.");
		patientPage.assertTrue(patientPage.verifyRowIsSelectedAndInfoDisplayedOnStudy(imbioPatientName, studyDesc), "Checkpoint[6/6]", "Verified patient row and study info for the selected patient.");
		
	}
	
	@Test(groups = { "Chrome", "Edge", "IE11", "DR2798", "Positive","E2E","F1081"})
	public void test06_DR2798_TC10706_verifyToggleOnSearchAccessedNonUI()throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Clicking search button does not load all patients when application is launched with a patient filter");
	
		Header header = new Header(driver);
		header.logout();
		
		db= new DatabaseMethods(driver);
		String id = db.getPatientID(imbioPatientName);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify the launching of patient page from non-ui login using accession number");
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(OrthancAndAPIConstants.PATIENT_ID_FOR_API,id);
		loginPage = new LoginPage(driver);
		String myURL = loginPage.getNonUILaunchURL(URLConstants.URL_LAUNCH, hm);
		loginPage.navigateToURL(myURL);
	
		patientPage = new PatientListPage(driver);
		patientPage.assertTrue(loginPage.getCurrentPageURL().contains(URLConstants.PATIENT_LIST_URL) , "Checkpoint[1/5]","Verified that current page URL is displaying as " +patientPage.getCurrentPageURL());
		patientPage.assertEquals(patientPage.patientRows.size(), 1, "Checkpoint[2/5]", "Verified that only patient is displayed");
		patientPage.assertEquals(patientPage.getRowNumber(imbioPatientName), 1, "Checkpoint[3/5]", "Verified the row");
		
		patientPage.click(patientPage.searchButton);
		
		patientPage.assertEquals(patientPage.patientRows.size(), 1, "Checkpoint[4.1/5]", "Verified that only patient is displayed after toggle of search button");
		patientPage.assertEquals(patientPage.getRowNumber(imbioPatientName), 1, "Checkpoint[4.2/5]", "Verified the row for patient is same");
		
		patientPage.click(patientPage.clearButton);
		
		patientPage.assertEquals(patientPage.patientRows.size(), 1, "Checkpoint[5.1/5]", "Verified that only patient is displayed after toggle of clear button");
		patientPage.assertEquals(patientPage.getRowNumber(imbioPatientName), 1, "Checkpoint[5.2/5]", "Verified the row for patient is same");
		
	}
	
	
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException{

		db=new DatabaseMethods(driver);	
		db.updateWiaCloudResultInStudyTable(studyDescription, 1);
	}	
}
