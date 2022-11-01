package com.trn.ns.test.viewer.contentSelector;

import java.awt.AWTException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ContentSelectorTest01 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private HelperClass helper;
	private DICOMRT rt;
	private ViewerLayout layout;
	private ViewerSliderAndFindingMenu findingMenu;
	private CircleAnnotation circle;
	private PagesTheme pageTheme;
	private OutputPanel panel;
	private String loadedTheme= ThemeConstants.EUREKA_THEME_NAME;
	private Header header;
	private RegisterUserPage register;


	String filePathAidoc = Configurations.TEST_PROPERTIES.get("Aidoc_filepath");
	String GSPS_PatientName_Aidoc = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidoc);

	private String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);
	private String liver9SeriesDesc=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, liver9filePath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String pointfilePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINT_filepath");
	String GSPS_point = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, pointfilePath); 

	String AH4_Filepath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_Filepath);

	private String tcgafilePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	private String tcgaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcgafilePath);
	private String tcgaSeriesDesc=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, tcgafilePath);

	String IHEMammoTestFilePath = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, IHEMammoTestFilePath);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY,IHEMammoTestFilePath);
	String seriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY,IHEMammoTestFilePath);

	String anonymousFilepath = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymousPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymousFilepath);

	String imbioFilepath = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilepath);

	String chestCT1p25mmFilepath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String chestCT1p25mmPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, chestCT1p25mmFilepath);

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String username_1 = "user_1";
	String username_2 = "user_2";
	String username_3 = "user_3";


	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);	
	}


	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086","E2E"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test01_ContentSelector"})
	public void test01_US2208_TC9789_verifyContentSelectorForStudyWithMultipleSeries(String filepath,String viewbox,String seriesCount,String machineCount,String resultCount) throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify default load of the series tab on all datatypes");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get(filepath);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(PatientName, username, password, 1);

	    cs=new ContentSelector(driver);
	    cs.assertEquals(cs.getNumberOfCanvasForLayout(), cs.convertIntoInt(viewbox), "Checkpoint[1/5]", "Verified default layout.");

	    cs.openAndCloseSeriesTab(true);
	    cs.assertEquals(cs.getText(cs.source), ViewerPageConstants.SOURCE_TAG, "Checkpoint[2/5]", "Verified source tag in content selector.");
	    List<WebElement> seriesList=cs.allSeriesFromSeriesTab;
	    List<WebElement> resultList=cs.allResultFromSeriesTab;
	    List<WebElement> machineList=cs.allMachineName;

	    cs.assertEquals(seriesList.size(), cs.convertIntoInt(seriesCount), "Checkpoint[3/5]", "Verified count of series listed under source group.");
	    cs.assertEquals(machineList.size(), cs.convertIntoInt(machineCount), "Checkpoint[4/5]", "Verified count of machines under series tab.");
	    cs.assertEquals(resultList.size(), cs.convertIntoInt(resultCount), "Checkpoint[5/5]", "Verified count of total result under series tab.");

	}

	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086","E2E"})
	public void test02_US2208_TC9790_verifyDragAndDropOfTheSeriesForDicomData() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify drag and drop of the series work as expected");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+tcgaPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(liver9PatientName,1,1);

	    cs=new ContentSelector(driver);
	    String actualSeries=cs.getSeriesDescriptionOverlayText(1);

	    cs.selectSeriesFromSeriesTab(1, liver9SeriesDesc);
	    cs.assertEquals(cs.getSeriesDescriptionOverlayText(1), liver9SeriesDesc, "Checkpoint[1/6]", "Verified series is loaded properly in first viewbox.");
	    cs.assertNotEquals(liver9SeriesDesc, actualSeries, "Checkpoint[2/6]", "Verified that series is loaded properly.");

	    layout=new ViewerLayout(driver);
	    layout.selectLayout(layout.threeByThreeLayoutIcon);
        cs.assertEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[3/6]", "Verified that layout is change to 3*3.");

        panel=new OutputPanel(driver);
        panel.openAndCloseOutputPanel(true);
        cs.openAndCloseSeriesTab(true);
        cs.selectSeriesFromSeriesTab(9, liver9SeriesDesc);
        cs.assertEquals(cs.getSeriesDescriptionOverlayText(9), liver9SeriesDesc, "Checkpoint[4/6]", "Verified series is loaded properly in empty viewbox.");

        helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
        cs.assertNotEquals(layout.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[5/6]", "Verified that default layout is loaded on browser back.");
	    cs.assertEquals(cs.getSeriesDescriptionOverlayText(1), actualSeries, "Checkpoint[6/6]", "Verified that original series is loaded on browser back."); 

	}

	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086"})
	public void test03_US2208_TC9790_verifyDragAndDropOfTheSeriesForRTData() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify drag and drop of the series work as expected");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+tcgaPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageForRTUsingSearch(tcgaPatientName,1,1);

	    rt=new DICOMRT(driver);
	    cs=new ContentSelector(driver);
	    layout=new ViewerLayout(driver);

	    cs.assertTrue(cs.isElementPresent(rt.legendOptions), "Checkpoint[1/6]", "Verified that legened option is visible on result.");
	    cs.selectSeriesFromSeriesTab(1, tcgaSeriesDesc);
	    cs.assertFalse(cs.isElementPresent(rt.legendOptions), "Checkpoint[2/6]", "Verified that legened option is not visible on result after series is loaded successfully.");

        layout.selectLayout(layout.threeByThreeLayoutIcon);
        cs.assertEquals(cs.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[3/6]", "Verified that layout is change to 3*3.");

        cs.selectSeriesFromSeriesTab(9, tcgaSeriesDesc);
         cs.assertTrue(tcgaSeriesDesc.contains(cs.getSeriesDescriptionOverlayText(9).replace("...", "")) , "Checkpoint[4/6]", "Verified series is loaded properly in empty viewbox.");


        helper.browserBackAndReloadRTData(tcgaPatientName, 1, 1);
        cs.assertNotEquals(cs.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[5/6]", "Verified that default layout is loaded on browser back.");
        cs.assertTrue(cs.isElementPresent(rt.legendOptions), "Checkpoint[6/6]", "Verified that original result is loaded on browser back."); 


	}

	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086","E2E"})
	public void test04_US2208_TC9790_verifyDragAndDropOfTheSeriesForCAD() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify drag and drop of the series work as expected");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(IHEMammoTestPatientName,1,1);

	    cs=new ContentSelector(driver);
	    layout=new ViewerLayout(driver);
	    findingMenu=new ViewerSliderAndFindingMenu(driver);

	    cs.assertTrue(findingMenu.getAllGSPSObjects(2).isEmpty(),"Checkpoint[1/7]","Verified that series is loaded on viewbox.");
	    cs.selectResultFromSeriesTab(2, resultDescription, 4);
	    cs.assertFalse(findingMenu.getAllGSPSObjects(2).isEmpty(),"Checkpoint[2/7]","Verified that result is loaded on viewbox and findings are visible.");

        layout.selectLayout(layout.threeByThreeLayoutIcon);
        cs.assertEquals(cs.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[3/7]", "Verified that layout is change to 3*3.");

        cs.selectSeriesFromSeriesTab(7, seriesDescription, 4);
        cs.assertEquals(cs.getSeriesDescriptionOverlayText(7), seriesDescription, "Checkpoint[4/7]", "Verified that series is loaded properly on empty viewbox."); 
        cs.assertTrue(findingMenu.getAllGSPSObjects(7).isEmpty(),"Checkpoint[5/7]","Verified that GSPS object is not visible as series is loaded .");

        helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
        cs.assertNotEquals(cs.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[6/7]", "Verified that default viewbox is loaded on browser reload.");
        cs.assertTrue(findingMenu.getAllGSPSObjects(2).isEmpty(),"Checkpoint[7/7]","Verified that GSPS object is not visible as series is loaded.");

	}

	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086","E2E"})
	public void test05_US2208_TC9791_verifyDragAndDropOfTheSeriesForNonDICOMData() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify drag and drop of the NON-DICOM series work as expected.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+anonymousPatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(anonymousPatientName,1,1);

	    cs=new ContentSelector(driver);
	    layout=new ViewerLayout(driver);

	    List<String>seriesDesc=cs.getAllSeries();
	    List<String>resultDesc=cs.getAllResults();

	    for(int i=0;i<seriesDesc.size();i++)
	    {
	    	cs.selectSeriesFromSeriesTab(1,seriesDesc.get(i).trim());
	    	cs.assertEquals(cs.getSeriesDescriptionOverlayText(1), seriesDesc.get(i), "Checkpoint[1."+(i+1)+"/4]", "Verified that series is loaded in first viewbox.");
	    }

	    for(int j=0;j<resultDesc.size();j++)
	    {
	    	cs.selectResultFromSeriesTab(1, resultDesc.get(j).trim());
	    	cs.assertEquals(cs.getSeriesDescriptionOverlayText(1), resultDesc.get(j), "Checkpoint[2."+(j+1)+"/4]", "Verified that result is loaded in first viewbox.");

	    }

	    layout.selectLayout(layout.threeByThreeLayoutIcon);
	    for(int i=0;i<seriesDesc.size();i++)
	    {
	    	cs.selectSeriesFromSeriesTab(5,seriesDesc.get(i).trim());
	    	cs.assertEquals(cs.getSeriesDescriptionOverlayText(5), seriesDesc.get(i), "Checkpoint[3."+(i+1)+"/4]", "Verified that series is loaded in empty viewbox.");
	    }

	    for(int j=0;j<resultDesc.size();j++)
	    {
	    	cs.selectResultFromSeriesTab(5, resultDesc.get(j).trim());
	    	cs.assertEquals(cs.getSeriesDescriptionOverlayText(5), resultDesc.get(j), "Checkpoint[4."+(j+1)+"/4]", "Verified that result is loaded in empty viewbox.");

	    }

	}

	@Test(groups = { "Chrome","IE","Edge","US2208","Positive","F1086"})
	public void test06_US2208_TC9791_TC9792_verifyWhenSeriesIsDroppedOutOfViewbox() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify default load of the series tab on all datatypes. <br>"+
		"Verify viewbox content does not get change when series is dropped outside of the viewbox");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+tcgaPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(imbioPatientName,1,1);

	    cs=new ContentSelector(driver);
	    String series1=cs.getSeriesDescriptionOverlayText(1);
	    String pdf=cs.getSeriesDescriptionOverlayText(2);
	    String series2=cs.getSeriesDescriptionOverlayText(3);

	    cs.assertEquals(cs.getSeriesDescriptionOverlayText(2), pdf, "Checkpoint[1/6]", "Verified that PDF is loaded in 2nd viewbox."); 
	    cs.selectResultFromSeriesTab(2, pdf);
	    cs.assertEquals(cs.getSeriesDescriptionOverlayText(2), pdf, "Checkpoint[2/6]", "Verified that PDF can also load on already loaded PDF."); 

	    cs.selectResultFromSeriesTab(3, series1);
	    cs.assertEquals(cs.getSeriesDescriptionOverlayText(3), series1, "Checkpoint[3/6]", "Verified that result is loaded in 3rd viewbox."); 

	     //get X and Y co-ordinate for Series (drag)
	     cs.openAndCloseSeriesTab(true);
	     cs.dragSeries(cs.getSeriesElement(series2));
	     cs.waitForTimePeriod(1000);
	     cs.dropSeries(cs.circleIcon);

	     cs.assertEquals(cs.getSeriesDescriptionOverlayText(1), series1, "Checkpoint[4/6]", "Verified that new series not loaded in 1st viewbox when series is dropped outside viewbox."); 
	     cs.assertEquals(cs.getSeriesDescriptionOverlayText(2), pdf, "Checkpoint[5/6]", "Verified that new series not loaded in 2nd viewbox when series is dropped outside viewbox"); 
	     cs.assertEquals(cs.getSeriesDescriptionOverlayText(3), series1, "Checkpoint[6/6]", "Verified that new series not loaded in 3rd viewbox when series is dropped outside viewbox"); 


	}

	@Test(groups = { "Chrome","IE","Edge","US2208","US2380","Positive","F1086","E2E"})
	public void test07_US2208_TC9793_US2380_TC9953_verifyCloneFunctionalityForSeries() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify clones are getting listed under /'Series/' tab. <br>"+
		"Verify eye icon gets displayed for newly created clone under 'User created results'");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(imbioPatientName,1,3);

	    circle=new CircleAnnotation(driver);
	    circle.selectCircleFromQuickToolbar(3);
	    circle.drawCircle(3, 5, 5,-80,-80);

	    cs=new ContentSelector(driver);
	    cs.openAndCloseSeriesTab(true);
	    cs.assertTrue(cs.isElementPresent(cs.userCreatedResults), "Checkpoint[1/4]", "Verified that user created result at the end of result.");
	    cs.assertEquals(cs.getText(cs.userCreatedResults), ViewerPageConstants.USER_CREATED_RESULT, "Checkpoint[2/4]", "Verified the machine name for user created findings.");
	    List<String> clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[]", "Verified eye icon for the newly created clone.");
	    cs.assertEquals(clone.get(0), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[3/4]", "Verified result name for the clone as "+clone.get(0));

	    helper.browserBackAndReloadViewer(imbioPatientName, 1, 1);
	    cs.openAndCloseSeriesTab(true);
	    circle.click(circle.getViewPort(1));
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 50, 50,100,100);

	    clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT,1));
	    cs.assertEquals(clone.get(0), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2", "Checkpoint[4/4]", "Verified result name for the clone which is created from previous clone as "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2"), "Checkpoint[]", "Verified eye icon for the newly created clone.");
	}

	@Test(groups = { "Chrome","IE","Edge","US2208","US2218","US2380","Positive","F1086","E2E"})
	public void test08_US2208_TC9793_US2218_TC9559_US2380_TC9955_verifyCloneFunctionalityForResults() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify clones are getting listed under /'Series/' tab. <br>"+
		"Verify different icons present beside series name under new 'Series' tab.<br>"+
				"Verify eye icon gets displayed for newly created clone under 'Machine results'");

		//Loading the patient on viewer 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(imbioPatientName,1,3);

	    cs=new ContentSelector(driver);
	    cs.openAndCloseSeriesTab(true);
	    String machineName=cs.getMachineName().get(0);
	    circle=new CircleAnnotation(driver);
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 5, 5,-80,-80);

	    cs.openAndCloseSeriesTab(true);
	    cs.assertFalse(cs.isElementPresent(cs.userCreatedResults), "Checkpoint[1/4]", "Verified that User created result is not created as clone is created from Result.");

	    List<String> clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(machineName));
	    cs.assertEquals(clone.get(2), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[2/4]", "Verified result name for the clone as "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[]", "Verified eye icon for the newly created clone.");
	    cs.assertTrue(cs.validateIconPresenceInSeriesTab(clone.get(2), ViewerPageConstants.GSPS_ICON,ViewerPageConstants.GSPS), "Checkpoint[3/4]", "Verified icon and tooltip for GSPS.");
	    cs.openAndCloseSeriesTab(false);

        helper.browserBackAndReloadViewer(imbioPatientName, 1, 1);
        panel=new OutputPanel(driver);
        panel.openAndCloseOutputPanel(true);
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 50, 50,100,100);
	    cs.openAndCloseSeriesTab(true);

	    clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(machineName,3));
	    cs.assertEquals(clone.get(0), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2", "Checkpoint[4/4]","Verified result name for the clone which is created from previous result clone as "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2"), "Checkpoint[]", "Verified eye icon for the newly created clone.");

	}

	//US2218: Build a tree view structure to display hierarchical data in Series Tab

	@Test(groups = { "Chrome","Edge","US2218","US2466","Positive","F1086"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test09_US2218_TC9553_TC9558_TC9559_TC9557_US2466_TC10178_verifyUIForSeriesTab(String theme) throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify tooltip should be displayed when series name is too long under new 'Series' tab. <br>"+
		"Verify different icons present beside series name under new 'Series' tab. <br>"+
		"Verify expand / collapse buttons under new 'Series' tab. <br>"+
		"Verify look and feel of the new 'Series' tab on hovering over the rows. <br>"+
		"[Risk and Impact] - Verify tooltip displayed on SR icon in Series tab");

		//Loading the patient on viewer 

		patientPage = new PatientListPage(driver);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    cs=new ContentSelector(driver);
	    pageTheme=new PagesTheme(driver);
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
	    helper.loadViewerPageUsingSearch(imbioPatientName,1,3);

	    String seriesDesc=cs.getSeriesDescriptionOverlayText(1);
	    cs.openAndCloseSeriesTab(true);

	    List<WebElement>results=cs.allResultFromSeriesTab;

	    cs.mouseHover(results.get(1));
	    cs.assertTrue(cs.isElementPresent(cs.tooltip), "Checkpoint[1/13]", "Verified that ellipses are present and tooltip is displayed.");
	    cs.assertEquals(cs.getText(results.get(1)), seriesDesc, "checkpoint[2/13]", "Verified tooltip for result name.");
	    cs.assertTrue(pageTheme.verifyThemeOnTooltip(cs.tooltip, theme), "Checkpoint[3/13]", "Verified tooptip for result description.");

	    //TC9559:
	    cs.assertTrue(cs.validateIconPresenceInSeriesTab(cs.getText(results.get(0)), ViewerPageConstants.PDF_ICON,ViewerPageConstants.PDF_ICON), "Checkpoint[4/13]", "Verified PDF icon and tooltip on mousehover.");
	    cs.assertTrue(pageTheme.verifyThemeOnTooltip(cs.tooltip,theme), "Checkpoint[5/13]", "Verified theme for PDF tooltip.");
	    cs.assertTrue(cs.validateIconPresenceInSeriesTab(cs.getText(results.get(1)), ViewerPageConstants.DI_ICON,ViewerPageConstants.DICOM), "Checkpoint[6/13]", "Verified DICOM icon and tooltip on mousehover");
	    cs.assertTrue(pageTheme.verifyThemeOnTooltip(cs.tooltip,theme), "Checkpoint[7/13]", "Verified theme for DICOM tooltip.");

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+chestCT1p25mmPatientName+" in viewer" );
        helper.browserBackAndReloadViewer(chestCT1p25mmPatientName,1, 2);
        cs.closeNotification();
	    cs.openAndCloseSeriesTab(true);
	    results=cs.allResultFromSeriesTab;
	    cs.assertTrue(cs.validateIconPresenceInSeriesTab(cs.getText(results.get(0)), ViewerPageConstants.SR_ICON,ViewerPageConstants.SR_TOOLTIP), "Checkpoint[8/13]", "Verified SR icon and tooltip on mousehover.");
	    cs.assertTrue(pageTheme.verifyThemeOnTooltip(cs.tooltip,theme), "Checkpoint[9/13]", "Verified theme for SR tooltip");

	    //TC9553
	    cs.mouseHover(results.get(0));
	    cs.assertTrue(pageTheme.verifyThemeOnLabel(results.get(0),theme),"Checkpoint[10/13]","Verified theme when row is highlighted in Under series tab.");

	    //TC9557:
	    List<WebElement>expandOrCollapse=cs.toggleButton;
	    for(int i=0;i<expandOrCollapse.size();i++)
	    cs.assertEquals(cs.getAttributeValue(cs.toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[11/13]","Verified that source and machine are by default expanded.");

	    cs.click(cs.toggleButton.get(0));
	    cs.assertEquals(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.ARIA_EXPANDED),NSGenericConstants.BOOLEAN_FALSE,"Checkpoint[12/13]","Verified that machine node is in collapse mode.");

	    cs.click(cs.toggleButton.get(0));
	    cs.assertEquals(cs.getAttributeValue(cs.toggleButton.get(0), NSGenericConstants.ARIA_EXPANDED),NSGenericConstants.BOOLEAN_TRUE,"Checkpoint[13/13]","Verified that machine node is in expand mode.");


	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","E2E","F1082"})
	public void test10_US2165_TC10162_verifySeriesTabPersistenceOnPageRefresh()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab state is persisted for user when page is refreshed.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointMultiSeries, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		cs=new ContentSelector(driver);
		cs.openAndCloseSeriesTab(true);

		panel.refreshWebPage();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);

		panel.assertTrue(panel.isElementPresent(panel.seriesTabOpened), "Checkpoint[1/1]", "Verfiying that series tab is highlighted after page refresh");
	}

	@Test(groups ={"Chrome","IE11","Edge","US2165","DR2662","Positive","E2E","F1082"})
	public void test11_US2165_TC10163_DR2662_TC10407_verifySeriesTabPersistenceOnReLogin()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab state is persisted for user after re login. <br>"+
		"Verify when you open and close the output panel selection state still retained when user logs off and log backs in");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(pointMultiSeries, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		cs=new ContentSelector(driver);

		cs.openAndCloseSeriesTab(true);

		panel.click(panel.minimizeIcon);
		header.logout();

		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);

		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(pointMultiSeries, 1);

		panel.assertTrue(cs.getAttributeValue(cs.seriesTab, NSGenericConstants.CLASS_ATTR).contains(NSGenericConstants.BUTTON_SELECTED), "Checkpoint[1/1]", "Verifying that series tab is highlighted after re login");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","DR2662","Positive","E2E","F1082"})
	public void test12_US2165_TC10174_DR2662_TC10407_verifyMaximizedSeriesTabContentsOnReLogin()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the contents of persisted maximized tab is visible after the relogin. <br>"+
		"Verify when you open and close the output panel selection state still retained when user logs off and log backs in");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		helper.loadViewerDirectly(GSPS_point, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		cs.openAndCloseSeriesTab(true);

		List<String>seriesDes=cs.convertWebElementToStringList(cs.allSeriesFromSeriesTab);    
		List<String>resultDes=cs.convertWebElementToStringList(cs.allResultFromSeriesTab);    

		header.logout();

		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);

		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GSPS_point, 1);
		cs.assertEquals(cs.convertWebElementToStringList(cs.allSeriesFromSeriesTab),seriesDes, "Checkpoint[1/2]", "Verified that series tab content is same after the relogin");
		cs.assertEquals(cs.convertWebElementToStringList(cs.allResultFromSeriesTab),resultDes, "Checkpoint[2/2]", "Verified that result tab content is same after the relogin.");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","E2E","F1082"})
	public void test13_US2165_TC10164_verifySeriesTabPersistenceOnTwoUsers()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the last visited tab persistence of user A is not applicable to the user B.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		header= new Header(driver);
		panel= new OutputPanel(driver);

		panel.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("UserB", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);

		header.logout();
		loginPage.login(username_1, username_1);

		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(GSPS_point, 1);
		cs.openAndCloseSeriesTab(true);

		panel.assertTrue(panel.isElementPresent(panel.seriesTabOpened), "Checkpoint[1/2]", "Verifying that series tab is maximized for User A");

		panel.refreshWebPage();

		loginPage.login(username_2, username_2);
		panel.assertFalse(panel.isElementPresent(panel.seriesTabOpened), "Checkpoint[2/2]", "Verifying that series tab is minimized for User B");
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2165","Positive","F1082"})
	public void test14_US2165_TC10175_verifyMaximizedSeriesTabContentsOnViewerRefresh()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and impact]: Verify that the maximized tab contents are displayed after the viewer load.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		helper.loadViewerDirectly(GSPS_point, 1);

		panel = new OutputPanel(driver);
		header=new Header(driver);
		cs.openAndCloseSeriesTab(true);

		List<String>seriesDes=cs.convertWebElementToStringList(cs.allSeriesFromSeriesTab);    
		List<String>resultDes=cs.convertWebElementToStringList(cs.allResultFromSeriesTab);    

		panel.refreshWebPage();
		loginPage.login(username, password);
		cs.waitForSeriesTabToLoad();
		cs.assertEquals(cs.convertWebElementToStringList(cs.allSeriesFromSeriesTab),seriesDes, "Checkpoint[1/2]", "Verified that series tab content is same after the relogin");
		cs.assertEquals(cs.convertWebElementToStringList(cs.allResultFromSeriesTab),resultDes, "Checkpoint[2/2]", "Verified that result tab content is same after the relogin.");
	}

	//US2466: Remove showing content selector from series description annotation in desktop
	@Test(groups ={"Chrome","IE11","Edge","US2466","Positive","F1086"})
	public void test15_US2466_TC10125_verifyContentSelectorNotVisibleOnClickingOnSeriesDesc()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector should not be displayed on clicking on series description overlay.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		cs=new ContentSelector(driver);
		helper.loadViewerDirectly(pointMultiSeries, 1);

		String series1=cs.getSeriesDescriptionOverlayText(1);
		
		cs.click(cs.getSeriesDescriptionOverlay(1));
		cs.assertFalse(cs.isElementPresent(cs.source), "Checkpoint[1/3]", "Verified that Content selector is not displayed on clicking on series description overlay.");
		
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		cs.openAndCloseSeriesTab(true);
		cs.dragAndDropSeriesOrResult(3, cs.getSeriesElement(series1));
		cs.openAndCloseSeriesTab(false);
		
		cs.click(cs.getSeriesDescriptionOverlay(3));
		cs.assertFalse(cs.isElementPresent(cs.source), "Checkpoint[2/3]", "Verified that Content selector is not displayed on clicking on series description overlay after changing the layout.");
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		cs.openAndCloseSeriesTab(true);
		cs.dragAndDropSeriesOrResult(cs.getNumberOfCanvasForLayout(), cs.getSeriesElement(series1));
		cs.openAndCloseSeriesTab(false);
		
		cs.click(cs.getSeriesDescriptionOverlay(9));
		cs.assertFalse(cs.isElementPresent(cs.source), "Checkpoint[3/3]", "Verified that Content selector is not displayed on clicking on series description overlay in empty viewbox.");
		
	}


	@AfterMethod
	public void revertDefaultTheme() {

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);


	}
}



