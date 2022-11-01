/*package com.trn.ns.test.obsolete;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector_old;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F79_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  -revision-0
//Functional.NS.I15_ContentSelector-CF0304ARevD - revision-0

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ContentSelectorTest01 extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector_old cs;
	private HelperClass helper;

	private String firstSeriesDescriptionJobSteve = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("JobsSteve_filepath"));
	private String secondSeriesDescriptionJobSteve = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("JobsSteve_filepath"));
	private String thirdSeriesDescriptionJobSteve = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("JobsSteve_filepath"));		

	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	String secondSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));	
	private String thirdSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
	private String fourthSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String pointMuliSeries = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientNamePointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, pointMuliSeries);

	String phantomChectFilepath = Configurations.TEST_PROPERTIES.get("PHANTOM^CHEST_filepath");
	String patientNamePhantomChest = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, phantomChectFilepath);
	
	String doeLillyFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String doeLillyPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, doeLillyFilePath);

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
	}

	//TC1052-Content Selector from non DICOM Result Objects - Access to list of objects associated with the loaded patient from active overlay
	//	Test 10: Verify Content Selector with non DICOM Result Objects (Automated) 
	@Test(groups ={"firefox","Chrome","Edge","IE11","US323","DE402","BVT"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls", "sheetName=test01_US323_TC1052"})
	public void test01_US323_TC1052_DE402_TC1519_TC1520_TC1521_verifyContentSelector_AllModality(String fileName,String resultViewbox)throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector on different modalities");

		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get(fileName);
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);	

		patientPage.clickOntheFirstStudy();		
		viewerpage = new ViewerPage(driver);

		if(resultViewbox.isEmpty())	
			viewerpage.waitForViewerpageToLoad();
		else
			viewerpage.waitForPdfToRenderInViewbox(Integer.parseInt(resultViewbox));	

		cs = new ContentSelector_old(driver);
		cs.closingBannerAndWaterMark();

		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		cs.click(cs.getViewPort(Integer.parseInt(resultViewbox)));
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(Integer.parseInt(resultViewbox));
		int actualTotalSeries = seriesDesc.size();

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Checkpoint TC10[1]:Verifying total number of series rendered in content selector." ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDesc.size();i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Checkpoint TC10[3]:Checkpoint TC10[2]:Verifying series " + seriesDesc.get(i) + " is present in content selector" ,"Verified series " + seriesDesc.get(i) + " is present in content selector");
		}

		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
		cs.selectSeriesFromContentSelector(2,firstSeriesDescription);		
		cs.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(2, firstSeriesDescription), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
	}

	//TC1092-Content Selector to access other series and studies content - Selected Series is displayed in the view port
	// Patient data = Bone Age study 
	//	Test 11: Verify Content Selector with Selected Series is displayed in the view port (Automated) 
	@Test(groups = { "Chrome","firefox","US323"})
	public void test02_US323_TC1092_verifyContentSelectorNonDICOM_BoneAge() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector for Bone Age study");

		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		cs = new ContentSelector_old(driver);

		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(4);
		int actualTotalSeries = seriesDesc.size();

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Checkpoint TC11[1]:Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series of the study with description on source tab
		for (int i=0; i<seriesDesc.size();i++)
		{   
			int temp=i+1;
			viewerpage.assertEquals(seriesDesc.get(i),DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath),"Checkpoint TC11[2]:Verifying series description from content selector" ,"Verified series description from content selector");
		}

		int expectedTotalResult = DataReader.getNumberOfResult(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> resultDesc = cs.getAllResultDesciptionFromContentSelector(4);
		int actualTotalresult = resultDesc.size();

		//Verify total number of result rendered in content selector
		viewerpage.assertEquals(actualTotalresult,expectedTotalResult , "Checkpoint TC11[3]: Verifying total number of result rendered in content selector" ,"Verified total number of result rendered in content selector");

		//Verify content selector shows all the result of the study with description on result tab
		for (int i=0; i<resultDesc.size();i++)
		{
			int temp=i+1;
			viewerpage.assertEquals(resultDesc.get(i),DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_RESULT0"+temp, filePath),"Checkpoint TC11[4]:Verifying result description from content selector" ,"Verified result description from content selector");
		}

		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
		String firstResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath);
		String secondResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath);

		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
		cs.selectResultFromContentSelector(1,secondResultDescription);
		viewerpage.assertFalse(cs.validateResultIsSelectedOnContentSelector(1, firstResultDescription), "Checkpoint TC11[5]: Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series is higlighted on content selector");
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(1, secondResultDescription), "Checkpoint TC11[5]: Verifying newly displayed result on viewbox is higlighted on content selector", "Verified newly displayed result is higlighted on content selector");

		cs.selectResultFromContentSelector(2,firstResultDescription);
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(2, firstResultDescription), "Checkpoint TC11[5]: Verifying newly displayed result on viewbox is higlighted on content selector", "Verified newly displayed result is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(2, firstSeriesDescription), "Checkpoint TC11[5]: Verifying already visible series on viewbox is higlighted on content selector", "Verified already visible series on viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(2, secondResultDescription), "Checkpoint TC11[5]: Verifying result not visible on any viewbox is not highlited on content selector", "Verified result not visible on any viewbox is not highlited on content selector");	
	}


	//TC1093Content Selector to access other series and studies content - Edge Cases-Layout Change and TC1229-Content selector should show check marks only for the currently visible series
	// Patient data = Bone Age study 
	//	Test 12: Verify Content Selector with Layout Change (Automated) 
	@Test(groups = { "Chrome","firefox","US323","BVT"})
	public void test03_US323_TC1093_TC1229_verifyContentSelectorNonDICOM_LayoutChange() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector on layout change");
		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();			

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForPdfToRenderInViewbox(1);	

		ContentSelector_old cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		//Layout change
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Checkpoint TC12[2]:Verifying Layout change applied and result and image "
				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
		String secondResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath);

		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
		cs.selectSeriesFromContentSelector(1,firstSeriesDescription);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescription), "Checkpoint TC12[3]:Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateResultIsSelectedOnContentSelector(1, secondResultDescription), "Checkpoint TC12[3]:Verifying result which is not displayed on viewbox is not highlited on content selector", "Verifying result which is not displayed on viewbox is not highlited on content selector");
	}


	//TC984-Content Selector to access other series and studies content - Edge Cases- Study with lot of series
	// Patient data =  SQA Test Onsite
	//Verify content selector functionality for study having multiple series 
	//	Test 8:  Verify Content Selector with Study having lot of series (Automated) 
	@Test(groups = { "Chrome","firefox","US238","US740"})
	public void test04_US238_TC984_US740_TC2692_verifyContentSelectorForStudyWithMultipleSeries() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify content selector functionality for study having multiple series"
				+ "Verify vertical scroll bar appears in control selector</br>");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("SQA_Testing");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();			

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();	
		cs = new ContentSelector_old(driver);
		//click on content selector on first view box
		//viewerpage.click(viewerpage.getSeriesDescriptionOverlay(1));
		//viewerpage.waitForElementVisibility(cs.sourceTab);
		//		viewerpage.compareElementImage(protocolName,cs.contentcontainer,"Checkpoint TC8[1]:Verify Scroll bar appear on Content selector","test09_CheckPoint1");

		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);

		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(1);
		int actualTotalSeries = seriesDesc.size();

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Checkpoint TC8[2]:Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDesc.size();i++)
		{   
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Checkpoint TC8[2]:	Verifying series description from content selector" ,"Verified series description from content selector");
		}	

	}

	//TC1230-Content selector should show check marks only for the currently visible series-Edge case
	// Patient data =  MR_LSP_WithResult    
	@Test(groups = { "Chrome","firefox","DE272"})
	public void test05_DE272_TC1230_verifyContentSelectorForMultipleLayout() throws InterruptedException   {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify check marks are seen only for the currently visible series in content selector after changing layout");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("MR_LSP_WithResult_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();		

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(1);
		int actualTotalSeries = seriesDesc.size();	

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDesc.size();i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Verifying series description from content selector" ,"Verified series description from content selector");
		}
		layout.selectLayout(layout.oneByTwoLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_TWO_LAYOUT), "Verifying Layout change applied and result and image "
				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");		
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES06_TEXTOVERLAY, filePath)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");		

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_ONE_LAYOUT), "Verifying Layout change applied and result and image "
				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");		
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES06_TEXTOVERLAY, filePath)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");		

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Verifying Layout change applied and result and image "
				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");		
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES06_TEXTOVERLAY, filePath)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");		

		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT), "Verifying Layout change applied and result and image "
				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES04_TEXTOVERLAY, filePath)),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, filePath)),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");		
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES06_TEXTOVERLAY, filePath)),"Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");		
	}

	//TC1182-Cannot access content selector when series description is empty
	// Patient data =  CR-THORAX-CHEST1-WithResult    
	@Test(groups = { "Chrome","firefox","DE267","Sanity"})
	public void test06_DE267_TC1182_verifyContentSelectorForBlankSeriesDesc() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify series number is displayed when series description is blank on viewbox");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("CR-THORAX-CHEST1-WithResult_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();			

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(1);
		int actualTotalSeries = seriesDesc.size();		

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDesc.size();i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Verifying series description from content selector" ,"Verified series description from content selector");
		}

		cs.selectSeriesFromContentSelector(1,DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath));
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES03_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
	}

	@Test(groups = { "Chrome","firefox","US352"})
	public void test07_US352_TC1261_verifyContentSelectorForPICCLineData() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Content Selector for PICC line Data");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("Picline_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);
		cs = new ContentSelector_old(driver);

		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(2);
		int actualTotalSeries = seriesDesc.size();		

		//Verify total number of series rendered in content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[1]  & Checkpoint[1/6]", "Verify total number of series rendered on Content selector");
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector is :"+expectedTotalSeries+".");

		//Verify content selector shows all the series  of the study with description 	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[2] & Checkpoint[2/6]", "Verify series description for each series in a Content Selector");
		for (int i=0; i<seriesDesc.size();i++)
		{
			int temp = i+1;
			viewerpage.assertEquals(seriesDesc.get(i),DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath),"Verifying series description from content selector" ,"Verified series description from content selector");
		}

		int expectedTotalResult = DataReader.getNumberOfResult(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> resultDesc = cs.getAllResultDesciptionFromContentSelector(2);
		int actualTotalresult = resultDesc.size();		

		//Verify total number of series rendered in content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[3] & Checkpoint[3/6]", "Verify total number of series rendered on Content selector");
		viewerpage.assertEquals(actualTotalresult,expectedTotalResult , "Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector is :"+expectedTotalSeries+".");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC8[4] & Checkpoint[4/6]", "Verify result description for each series in a Content Selector");
		for (int i=0; i<resultDesc.size();i++)
		{
			int temp = i+1;
			viewerpage.assertEquals(resultDesc.get(i),DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_RESULT0"+temp, filePath),"Verifying result description from content selector" ,"Verified result description from content selector");
		}

		// Select Result series in ViewBox containing a original Patient series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify Result series appear on View box on selecting from Content Selector");
		cs.selectResultFromContentSelector(2,DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath));
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(2, DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(cs.validateSeriesIsSelectedOnContentSelector(2, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");

		// Select Original Patient series in original Patient Data series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify Result series appear on View box on selecting from Content Selector");
		cs.selectSeriesFromContentSelector(1,DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath));
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath)), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateResultIsSelectedOnContentSelector(1, DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath)), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");


	}
	//TC1125-Verify content selector is displayed on left as well right click on empty viewbox. Verify check icons displayed against all rendered series on viewer.
	// Patient data =  JobsSteveWithResult   
	//	Test 1: Verify Content Selector from empty view port (Automated) 
	@Test(groups = { "Chrome","firefox","US356","US764","Sanity"})
	public void test08_US356_TC1125_US764_2891_verifyContentSelectorOnEmptyViewbox() throws Exception  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Access Content Selector from empty view port -Create indicator on empty view port to indicate no content is displayed"+"<br>Refactor initial layout and change layout code in client/server-Verify with the content selector changess");
		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("JobsSteve_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		//Opening of content selector on left click on empty view box
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDescFromContentSelectorOnEmptyViewbox(4);
		int actualTotalSeries = seriesDesc.size();	

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Checkpoint TC1[2] : Checkpoint [2]: Verifying total number of series rendered in content selector." ,"Verified total number of series rendered in content selector");


		//Opening of content selector on right click on empty view box
		List <String> seriesDescOnRightClick = cs.getSeriesDescFromContentSelectorOnRightClick(4);
		int actualTotalSeriesOnRightClick = seriesDescOnRightClick.size();

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeriesOnRightClick,expectedTotalSeries , "Checkpoint TC1[3] : Verifying total number of series rendered in content selector." ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDescOnRightClick.size();i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(seriesDescOnRightClick.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Checkpoint TC1[4]:Verifying series " + seriesDescOnRightClick.get(i) + " is present in content selector" ,"Verified series " + seriesDescOnRightClick.get(i) + " is present in content selector");
		}

		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, firstSeriesDescriptionJobSteve), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, secondSeriesDescriptionJobSteve), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, thirdSeriesDescriptionJobSteve), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");	
	}

	//TC1126-Access Content Selector from empty view port - Selected object is displayed in the view port
	// Patient data =  JobsSteveWithResult   
	//	//	Test 1: Verify Content Selector from empty view port (Automated) 
	//	Test 2: Verify Content Selector from empty view port with display of selected series (Automated
	@Test(groups = { "Chrome","firefox","US356"})
	public void test09_US356_TC1126_DE1129_TC4705_verifyContentSelectorOnEmptyViewbox() throws Exception  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Access Content Selector from empty view port -Create indicator on empty view port to indicate no content is displayed");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("JobsSteve_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();		

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();	
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs.selectSeriesFromContentSelectorOnEmptyViewbox(4,firstSeriesDescriptionJobSteve);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(4, firstSeriesDescriptionJobSteve), "Checkpoint TC2[2]: Checkpoint TC2[1] : Checkpoint TC1[1]:Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "verifying the console error presence", "No console should be present");

		cs.selectSeriesFromContentSelector(4,secondSeriesDescriptionJobSteve);	
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(4, secondSeriesDescriptionJobSteve), "Checkpoint TC3[3] : Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "verifying the console error presence", "No console should be present");

	}

	//TC1683-Content selector always appear after opened once in an empty viewbox
	// Patient data =  JobsSteveWithResult   
	@Test(groups = { "Chrome","firefox","DE423","Sanity","BVT"})
	public void test10_DE423_TC1683_verifyContentSelectorOnNonEmptyViewbox() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Content selector always appear after opened once in an empty viewbox");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("JobsSteve_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();			

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		//Opening of content selector on left click on empty viewbox
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDescFromContentSelectorOnEmptyViewbox(4);
		int actualTotalSeries = seriesDesc.size();		

		//Verify total number of series rendered in content selector
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Verifying total number of series rendered in content selector." ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series  of the study with description 	
		for (int i=0; i<seriesDesc.size();i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Verifying series " + seriesDesc.get(i) + " is present in content selector" ,"Verified series " + seriesDesc.get(i) + " is present in content selector");
		}
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, firstSeriesDescriptionJobSteve), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, secondSeriesDescriptionJobSteve), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelectorFromEmptyViewbox(4, thirdSeriesDescriptionJobSteve), "Verifying result displayed on any viewbox is higlighted on content selector", "Verified result displayed on any viewbox is higlighted on content selector");	

		//Click anywhere on viewbox 1 (other than series description) and verify content selector does not appear.
		viewerpage.mouseHover("presence",viewerpage.getViewbox(1),0,0);
		viewerpage.getViewPort(1).click();		
		viewerpage.assertFalse(viewerpage.isElementPresent(cs.contentcontainer), "Verifying content selector should not appear when clicked anywhere on viewbox 1", "Verified content selector does not appear when clicked anywhere on viewbox 1");

		//Click anywhere on viewbox 2 (other than series description) and verify content selector does not appear.
		viewerpage.mouseHover("presence",viewerpage.getViewbox(2),0,0);
		viewerpage.getViewPort(1).click();
		viewerpage.assertFalse(viewerpage.isElementPresent(cs.contentcontainer), "Verifying content selector should not appear when clicked anywhere on viewbox 2", "Verified content selector does not appear when clicked anywhere on viewbox 2");	
	}

	@Test(groups = { "Chrome","firefox","Edge","IE11","DE352","DE482"})
	public void test11_DE482_TC1973_DE352_TC2105_TC2106_US289_TC2374_VerifySeriesAreSyncedWhenSelectedUsingCS() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription( "Unable to sync the series selected in the view port - Defect Coverage"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");

		//Loading the patient on viewer 
		String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
		String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);


		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ah4PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(ah4PatientName);

		patientPage.clickOntheFirstStudy();	
		viewerpage = new ViewerPage(driver);		
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		viewerpage.waitForPdfToRenderInViewbox(4);

		// Perform an action (such as pan, zoom, etc.) in the viewer to verify that synchronization is active on at least two of the loaded series.
		// The action performed is applied equivalently in all synchronized series.

		//		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		//		viewerpage.dragAndReleaseOnViewer(0, 0, 100, -50);
		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify images should PAN synchronously.");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verifying images should PAN synchronously in viewbox 1.","TC18_checkpoint1");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2),"Verifying images should PAN synchronously in viewbox 2.","TC18_checkpoint2");

		// performing zoom
		viewerpage.selectZoomFromQuickToolbar(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(2),0, 0, 100, -50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2]", "Verify images should zoom synchronously.");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verifying images should zoom synchronously in viewbox 1.","TC18_checkpoint3");
		viewerpage.assertEquals(viewerpage.getZoomLevel(1), viewerpage.getZoomLevel(2), "Checkpoint[3]","Verifying the zoom % are same in both the sync viewboxes");


		String firstSeries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
		String secondSeries= DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));		


		// selecting the first series in viewbox 3 
		cs.closingBannerAndWaterMark();
		cs.selectSeriesFromContentSelector(3, firstSeries);

		// selecting the second series in viewbox 4 
		cs.closingBannerAndWaterMark();
		cs.selectSeriesFromContentSelector(4, secondSeries);

		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(3),0, 0, 100, -50);

		//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3]", "Verify images should zoom synchronously.");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verifying images should zoom synchronously in viewbox 1.","TC18_checkpoint5");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2),"Verifying images should zoom synchronously in viewbox 2.","TC18_checkpoint6");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(3),"Verifying images should zoom synchronously in viewbox 3.","TC18_checkpoint7");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(4),"Verifying images should zoom synchronously in viewbox 4.","TC18_checkpoint8");

		int zoomValue= viewerpage.getZoomLevel(1);

		viewerpage.assertTrue(viewerpage.getZoomLevel(1)==zoomValue, "Checkpoint[3]","Verifying the zoom % are same in all the synced viewboxes");
		viewerpage.assertTrue(viewerpage.getZoomLevel(2)==zoomValue, "Checkpoint[3]","Verifying the zoom % are same in all the synced viewboxes");
		viewerpage.assertTrue(viewerpage.getZoomLevel(3)==zoomValue, "Checkpoint[3]","Verifying the zoom % are same in all the synced viewboxes");
		viewerpage.assertTrue(viewerpage.getZoomLevel(4)==zoomValue, "Checkpoint[3]","Verifying the zoom % are same in all the synced viewboxes");

		//		//performing tha pan in viewbox 4
		//		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		//		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(4),0, 0, 100, -50);
		//
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verifying images should PAN synchronously in viewbox 1.","TC18_checkpoint9");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2),"Verifying images should PAN synchronously in viewbox 2.","TC18_checkpoint10");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(3),"Verifying images should PAN synchronously in viewbox 3.","TC18_checkpoint11");
		//		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(4),"Verifying images should PAN synchronously in viewbox 4.","TC18_checkpoint12");


		//changing the layout
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying Series are getting retained on layout change","TC18_checkpoint14");

		// selecting the series in empty viewbox
		cs.closingBannerAndWaterMark();
		cs.selectSeriesFromContentSelector(6, firstSeries);
		cs.selectSeriesFromContentSelector(7, secondSeries);
		cs.selectSeriesFromContentSelector(8, firstSeries);
		cs.selectSeriesFromContentSelector(9, secondSeries);

		//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying series are selected","TC18_checkpoint15");

		//all the synchronized series should be synced based on viewbox7
		viewerpage.click(viewerpage.getViewPort(7));		
		//		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying all the viewboxes are synced with viewbox 7","TC18_checkpoint16");


		//applying window leveling on all the viewboxes		

		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(7));

		for(int i =1 ; i<=9 ;i++) {				


			if(i==5) {
				continue;
			}
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(i),0, 0, 100, -100);

			Integer vwWWa = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
			Integer	vwWCb = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));

			Integer vwWWc = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
			Integer	vwWCd = Integer.parseInt(viewerpage.getValueOfWindowCenter(2));

			//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying all the viewboxes are synced with viewbox 7","TC18_checkpoint17_"+i);

			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1)) ,vwWWa, "Verify the width of viewbox1", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2)) ,vwWWc, "Verify the width of viewbox2", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(3)) ,vwWWa, "Verify the width of viewbox3", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(4)) ,vwWWc, "Verify the width of viewbox4", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(6)) ,vwWWa, "Verify the width of viewbox6", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(7)) ,vwWWc, "Verify the width of viewbox7", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(8)) ,vwWWa, "Verify the width of viewbox8", "Window Width is for viewbox7 = "+vwWWa+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(9)) ,vwWWc, "Verify the width of viewbox9", "Window Width is for viewbox7 = "+vwWWa+" - verified");		

			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1)) ,vwWCb, "Verify the window center of viewbox1", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2)) ,vwWCd, "Verify the window center of viewbox2", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(3)) ,vwWCb, "Verify the window center of viewbox3", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(4)) ,vwWCd, "Verify the window center of viewbox4", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(6)) ,vwWCb, "Verify the window center of viewbox6", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(7)) ,vwWCd, "Verify the window center of viewbox7", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(8)) ,vwWCb, "Verify the window center of viewbox8", "Window window center is for viewbox7 = "+vwWCb+" - verified");		
			viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(9)) ,vwWCd, "Verify the window center of viewbox9", "Window window center is for viewbox7 = "+vwWCb+" - verified");		

		}

		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(2));
		for(int i =1 ; i<=9 ;i++) {				

			if(i==5) {
				continue;
			}
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(i),0, 0, 10, -10);

			Integer count = viewerpage.getCurrentScrollPositionOfViewbox(i);				
			//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying all the viewboxes are synced with viewbox "+i,"TC18_checkpoint18_"+i);

			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1) ,count, "Verify the scroll of viewbox1", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(2) ,count, "Verify the scroll of viewbox2", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(3) ,count, "Verify the scroll of viewbox3", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(4) ,count, "Verify the scroll of viewbox4", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(6) ,count, "Verify the scroll of viewbox6", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(7) ,count, "Verify the scroll of viewbox7", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(8) ,count, "Verify the scroll of viewbox8", "verified");		
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(9) ,count, "Verify the scroll of viewbox9", "verified");		


		}

		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(3));
		for(int i =1 ; i<=9 ;i++) {				

			if(i==5) {
				continue;
			}
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(i),0, 0, 10, -10);

			Integer count = viewerpage.getZoomLevel(i);				
			//			viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying all the viewboxes are synced with viewbox "+i,"TC18_checkpoint19_"+i);

			viewerpage.assertEquals(viewerpage.getZoomLevel(1) ,count, "Verify the scroll of viewbox1", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(2) ,count, "Verify the scroll of viewbox2", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(3) ,count, "Verify the scroll of viewbox3", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(4) ,count, "Verify the scroll of viewbox4", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(6) ,count, "Verify the scroll of viewbox6", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(7) ,count, "Verify the scroll of viewbox7", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(8) ,count, "Verify the scroll of viewbox8", "verified");		
			viewerpage.assertEquals(viewerpage.getZoomLevel(9) ,count, "Verify the scroll of viewbox9", "verified");		


		}


	}			
	
	@Test(groups = { "Chrome","firefox","DE580"})
	public void test12_DE580_TC2173_verifyTheSyncAfterSwappingSeriesFromContentSelector() throws Exception  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sync after swapping series from content selector.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		//taking values of WW and WC from different view-boxes
		Integer vwWW1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
		Integer vwWW2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
		Integer	vwWC1 = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
		Integer vwWC2 = Integer.parseInt(viewerpage.getValueOfWindowCenter(2));

		String beforeScrollValue = viewerpage.getValueOfImage(1);
		String beforeZoomValue = viewerpage.getValueOfZoom(1);

		cs.selectSeriesFromContentSelector(1,secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(2,firstSeriesDescriptionAH4);

		//performing scroll
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify scroll sync for viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");

		//performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify zoom sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1),0, 0, 0, 50);	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify WWWL sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(2)) , vwWW2 - vwWW1, "Verify the width of viewer's page", "Window Width is verified");		
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(2)) , vwWC2 - vwWC1, "Verify the window center of viewer's page", "Window window center is verified");		

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify images should PAN synchronously after swapping series from content selector.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should PAN synchronously.","DE580_TC2173_Checkpoint1");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should PAN synchronously.","DE580_TC2173_Checkpoint2");

	}

	@Test(groups = { "Chrome","firefox","DE580"})
	public void test13_DE580_TC2174_verifyContentSelectorOnNonEmptyViewbox() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sync after swapping series from content selector.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		cs.selectSeriesFromContentSelector(1,secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(2,firstSeriesDescriptionAH4);
		cs.closeWaterMarkIcon(3);
		cs.selectSeriesFromContentSelector(3,firstSeriesDescriptionAH4);

		Integer vwWW1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
		Integer vwWW2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));

		Integer	vwWC1 = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
		Integer vwWC2 = Integer.parseInt(viewerpage.getValueOfWindowCenter(2));

		String beforeScrollValue = viewerpage.getValueOfImage(1);
		String beforeZoomValue = viewerpage.getValueOfZoom(1);

		//performing scroll
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify scroll sync for viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		for(int i=2;i<4;i++){
			viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(i),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		}	

		//performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify zoom sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		for(int i=2;i<4;i++){
			viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(i),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		}	

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify WWWL sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2), viewerpage.getValueOfWindowCenter(3), "Verify the width of viewbox2", "");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2))- Integer.parseInt(viewerpage.getValueOfWindowWidth(1)) , vwWW2 - vwWW1, "Verify the width of viewer's page", "Window Width is verified");		
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2))- Integer.parseInt(viewerpage.getValueOfWindowCenter(1)) , vwWC2 - vwWC1, "Verify the window center of viewer's page", "Window window center is verified");		

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify images should PAN synchronously after swapping series from content selector.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should PAN synchronously.","DE580_TC2174_Checkpoint1");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should PAN synchronously.","DE580_TC2174_Checkpoint2");

		//performing scroll
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify scroll sync for viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		for(int i=2;i<4;i++){
			viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(i),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		}

		//performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify zoom sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		for(int i=2;i<4;i++){
			viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(i),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		}

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify WWWL sync is working on viewer's page after swapping series from content selector.");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(2), viewerpage.getValueOfWindowCenter(3), "Verify the width of viewer's page", "Verified width of viewer's page");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2))- Integer.parseInt(viewerpage.getValueOfWindowWidth(1)) , vwWW2 - vwWW1, "Verify the width of viewer's page", "Window Width is verified");		
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2))- Integer.parseInt(viewerpage.getValueOfWindowCenter(1)) , vwWC2 - vwWC1, "Verify the window center of viewer's page", "Window window center is verified");		

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(2));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify images should PAN synchronously after swapping series from content selector.");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Verifying images should PAN synchronously.","DE580_TC2174_Checkpoint3");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(2),"Verifying images should PAN synchronously.","DE580_TC2174_Checkpoint4");
		

	}

	@Test(groups = { "Chrome","firefox","DE580"})
	public void test14_DE580_TC2175_verifySyncAfterSelectingSeriesInViewboxesFromContentSelector() throws Exception  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sync after selecting series in all empty viewboxes on 3x2 layout from content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();	
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);

		cs.selectSeriesFromContentSelector(2,firstSeriesDescriptionAH4);
		cs.closeWaterMarkIcon(3);
		cs.selectSeriesFromContentSelector(3,secondSeriesDescriptionAH4);
		cs.closeWaterMarkIcon(4);
		cs.selectSeriesFromContentSelector(4,secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(5,secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelectorOnEmptyViewbox(6,secondSeriesDescriptionAH4);

		Integer vwWW1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
		Integer vwWW3 = Integer.parseInt(viewerpage.getValueOfWindowWidth(3));
		Integer vwWW4 = Integer.parseInt(viewerpage.getValueOfWindowWidth(4));
		Integer vwWW5 = Integer.parseInt(viewerpage.getValueOfWindowWidth(5));
		Integer vwWW6 = Integer.parseInt(viewerpage.getValueOfWindowWidth(6));

		Integer	vwWC1 = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
		Integer vwWC3 = Integer.parseInt(viewerpage.getValueOfWindowCenter(3));
		Integer vwWC4 = Integer.parseInt(viewerpage.getValueOfWindowCenter(4));
		Integer vwWC5 = Integer.parseInt(viewerpage.getValueOfWindowCenter(5));
		Integer vwWC6 = Integer.parseInt(viewerpage.getValueOfWindowCenter(6));

		String beforeScrollValue = viewerpage.getValueOfImage(1);
		String beforeZoomValue = viewerpage.getValueOfZoom(1);

		//performing scroll
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify scroll sync for viewer's page");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		for(int i=2;i<7;i++){
			viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(i),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		}			

		//performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify zoom sync is working on viewer's page");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		for(int i=2;i<7;i++){
			viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(i),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		}

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify WWWL sync is working on viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfWindowWidth(1), viewerpage.getValueOfWindowWidth(1), "", "");
		viewerpage.assertEquals(viewerpage.getValueOfWindowCenter(1), viewerpage.getValueOfWindowCenter(2), "Verify the width of viewbox2", "");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(3)) , vwWW1 - vwWW3, "Verify the width of viewer's page", "Window Width is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(4)) , vwWW1 - vwWW4, "Verify the width of viewer's page", "Window Width is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(5)) , vwWW1 - vwWW5, "Verify the width of viewer's page", "Window Width is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(6)) , vwWW1 - vwWW6, "Verify the width of viewer's page", "Window Width is verified");

		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(3)) , vwWC1 - vwWC3, "Verify the window center of viewer's page", "Window window center is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(4)) , vwWC1 - vwWC4, "Verify the window center of viewer's page", "Window window center is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(5)) , vwWC1 - vwWC5, "Verify the window center of viewer's page", "Window window center is verified");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(6)) , vwWC1 - vwWC6, "Verify the window center of viewer's page", "Window window center is verified");

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify images should PAN synchronously.");
		//viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","DE580_TC2175_Checkpoint1");


	}

	@Test(groups = { "Chrome","firefox","DE580"})
	public void test15_DE580_TC2176_verifySyncAfterSwappingSeriesFromContentSelectorAndLayoutChange() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the sync after swapping series from content selector and layout change.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();			
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		cs.selectSeriesFromContentSelector(1,secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(2,firstSeriesDescriptionAH4);

		Integer vwWW1 = Integer.parseInt(viewerpage.getValueOfWindowWidth(1));
		Integer vwWW2 = Integer.parseInt(viewerpage.getValueOfWindowWidth(2));
		Integer	vwWC1 = Integer.parseInt(viewerpage.getValueOfWindowCenter(1));
		Integer vwWC2 = Integer.parseInt(viewerpage.getValueOfWindowCenter(2));

		String beforeScrollValue = viewerpage.getValueOfImage(1);
		String beforeZoomValue = viewerpage.getValueOfZoom(1);

		//performing scroll
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify scroll sync for viewer's page after swapping series from content selector and layout change.");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");

		//Performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify zoom sync is working on viewer's page after swapping series from content selector and layout change");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify WWWL sync is working on viewer's page after swapping series from content selector and layout change");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(2))- Integer.parseInt(viewerpage.getValueOfWindowWidth(1)) , vwWW2 - vwWW1, "Verify the width of viewer's page", "Window Width is verified");		
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(2))- Integer.parseInt(viewerpage.getValueOfWindowCenter(1)) , vwWC2 - vwWC1, "Verify the window center of viewer's page", "Window window center is verified");		

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify images should PAN synchronously after swapping series from content selector and layout change.");
	//	viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","DE580_TC2176_Checkpoint1");

		layout.selectLayout(layout.twoByOneLayoutIcon);

		//performing scroll
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify scroll sync for viewer's page after swapping series from content selector and layout change");
		viewerpage.assertNotEquals(beforeScrollValue, viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfImage(1), viewerpage.getValueOfImage(2),"Verifying scroll sync for viewer's page", "Verified scroll sync for viewer's page");

		//Performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify zoom sync is working on viewer's pagepage after swapping series from content selector and layout change");
		viewerpage.assertNotEquals(beforeZoomValue, viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");
		viewerpage.assertEquals(viewerpage.getValueOfZoom(1), viewerpage.getValueOfZoom(2),"Verifying zoom sync for viewer's page", "Verified zoom sync for viewer's page");

		//performing WWWL
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify WWWL sync is working on viewer's page after swapping series from content selector and layout change");
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowWidth(1))- Integer.parseInt(viewerpage.getValueOfWindowWidth(2)) , vwWW1-vwWW2, "Verify the width of viewer's page", "Window Width is verified");		
		viewerpage.assertEquals(Integer.parseInt(viewerpage.getValueOfWindowCenter(1))- Integer.parseInt(viewerpage.getValueOfWindowCenter(2)) , vwWC1-vwWC2 , "Verify the window center of viewer's page", "Window window center is verified");		

		//performing pan
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(0, 10, 10, 50);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify images should PAN synchronously after swapping series from content selector and layout change.");
		//viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN synchronously.","DE580_TC2176_Checkpoint2");

	}

	@Test(groups = { "Chrome","US740"})
	public void test16_US740_TC2687_TC2688_TC2689_verifyDefaultAppearanceOfContentSelector() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Default appearance of Content Selector"
				+ "</br>Verify group by - Machine");


		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		cs = new ContentSelector_old(driver);

		cs.openContentSelector(4, true);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/12]", "Verify Source tab is present on content selector");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.sourceTab), "Verify Source tab is present on Content Selector", "The Source tab is present on content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/12]", "Verify Source tab is present on content selector");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.resultTab), "Verify result tab is present on Content Selector", "The result tab is present on content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/12]", "Verify series description coloumn is present on source tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.seriesDescOnContentSelector), "Verify series description coloumn is present on source tab", "The series description coloumn is present on source tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/12]", "Verify Date coloumn is present on source tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.dateCreatedForSeries.get(0)), "Verify Date coloumn is present on source tab", "The Date coloumn is present on source tab");

		cs.closeContentSelector(4);
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> seriesDesc = cs.getSeriesDesciptionFromContentSelector(4);
		int actualTotalSeries = seriesDesc.size();

		//Verify total number of series rendered in content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/12]", "Verify total no of series on source tab");
		viewerpage.assertEquals(actualTotalSeries,expectedTotalSeries , "Verifying total number of series rendered in content selector" ,"Verified total number of series rendered in content selector");

		//Verify content selector shows all the series of the study with description on source tab
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/12]", "Verify series which are rendered in viewer are highlited on content selector");
		for (int i=0; i<seriesDesc.size();i++)
		{   
			int temp=i+1;
			viewerpage.assertTrue(seriesDesc.contains(DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)),"Verifying series description from content selector" ,"Verified series description from content selector");
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(4, DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_SERIES0"+temp, filePath)), "Verifying already displayed series on viewbox is higlighted on content selector", "Verified already displayed series on viewbox is higlighted on content selector");

		}

		//Click on Series description on fourth View box and select Result Tab
		viewerpage.click(viewerpage.getSeriesDescriptionOverlay(4));
		viewerpage.click(cs.resultTab);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[7/12]", "Verify sort by label is present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.sortOnContentSelector), "Verify sort by label is present on result tab", "The sort by label is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[8/12]", "Verify machine radio button is present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.machineButtonOnContentSelector), "Verify machine radio button  is present on result tab", "The machine radio button  is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[9/12]", "Verify series radio button is present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.seriesButtonOnContentSelector), "Verify series radio button  is present on result tab", "The series radio button  is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[10/12]", "Verify Group by label is present on result tab");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.groupOnContentSelector), "Verify Group by label is present on result tab", "The Group by label is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[11/12]", "Verify option for sort by drop down menu");
		viewerpage.assertEquals(cs.selectionOptionOnContentSelector.get(0).getText(), ViewerPageConstants.RECENT_SELECTION, "Verify Recent option for selection in sort by combo box","Recent option for selection in sort by combo box");
		viewerpage.assertEquals(cs.selectionOptionOnContentSelector.get(1).getText(), ViewerPageConstants.ASCENDING_SELECTION, "Verify Ascending option for selection in sort by combo box","Ascending option for selection in sort by combo box");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[12/12]", "Verify machine description on source tab");
		viewerpage.assertEquals(viewerpage.getText(cs.contentSelectorMachine),ViewerPageConstants.BONEAGE_MACHINE_NAME1, "Verify machine description on source tab", "Machine description is present on source");

		int expectedTotalResult = DataReader.getNumberOfResult(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> resultDesc = cs.getAllResultDesciptionFromContentSelector(4);
		List <String> sourceDesc = cs.getMachineNameFromContentSelector(4);
		int actualTotalResult = resultDesc.size();

		// Verify total number of result rendered in content selector
		viewerpage.assertEquals(actualTotalResult,expectedTotalResult , "Verifying total number of result rendered in content selector" ,"Verified total number of result rendered in content selector");

		//Verify content selector shows all the result of the study with description on result tab
		for (int i=0; i<resultDesc.size()/2;i++)
		{
			int temp=i+1;
			viewerpage.assertEquals(resultDesc.get(i),DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_RESULT0"+temp, filePath),"Verifying result description from content selector" ,"Verified result description from content selector");
			viewerpage.assertEquals(sourceDesc.get(i),ViewerPageConstants.CREATED_BY+" "+ViewerPageConstants.BONEAGE_MACHINE_NAME1, "Verifying source description for result" ,"Verified source description from content selector");
		}
	}

	@Test(groups = { "Chrome","US740","BVT"})
	public void test17_US740_TC2690_verifyContentSelectorForSeriesGroupBy() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Default appearance of Content Selector on grouping by series");

		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		cs = new ContentSelector_old(driver);
		//Click on Series description on fourth view box
		cs.openContentSelector(4, false);
		viewerpage.click(cs.seriesButtonOnContentSelector);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]", "Verify empty description if source is not present for a result");
		viewerpage.assertEquals(viewerpage.getText(cs.contentSelectorMachine),ViewerPageConstants.UNKNOWN_REFERENCE, "Verify empty description if source is not present for a result" ,"Verified empty description if source is not present for a result");

		int expectedTotalResult = DataReader.getNumberOfResult(PatientXMLConstants.STUDY01_TEXTOVERLAY, filePath);
		List <String> resultDesc = cs.getAllResultDescriptionFromContentSelectWithGroupingBySeries(4);
		List <String> machineDesc = cs.getMachineNameFromContentSelector(4);
		int actualTotalResult = resultDesc.size();

		// Verify total number of result rendered in content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]", "Verify total number of result rendered on content selector");
		viewerpage.assertEquals(actualTotalResult,expectedTotalResult , "Verifying total number of result rendered on content selector" ,"Verified total number of result rendered on content selector");

		//Verify content selector shows all the result of the study with description on result tab
		for (int i=0; i<resultDesc.size()/2;i++)
		{
			int temp=i+1;
			viewerpage.assertTrue(resultDesc.contains(DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, "STUDY01_RESULT0"+temp, filePath)),"Verifying result description from content selector" ,"Verified result description from content selector");
			viewerpage.assertEquals(machineDesc.get(i),ViewerPageConstants.CREATED_BY+" "+ViewerPageConstants.BONEAGE_MACHINE_NAME, "Verifying machine description for result" ,"Verified machine description from content selector");
		}
	}
	
	@Test(groups = {"Chrome","US740"})
	public void test18_US740_TC2691_verifySortingOfAllResultTab() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify sorting of result tab");

		//Loading the patient on viewer
		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();		

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(4);
		cs = new ContentSelector_old(driver);
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);

		//select linear measurement from radial menu and draw an annotation on radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(4);
		lineWithUnit.drawLine(4, -70, -80, -120, 90);

		//click on series description and select Result tab
		cs.openContentSelector(4, false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify sort by drop down is present on Result tab of content selector");
		viewerpage.assertTrue(viewerpage.isElementPresent(cs.selectionComboBoxOnContentSelector), "Verify sort by dropdown is present on result tab", "Sort by dropdown is present on result tab");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify option for sort by drop down");
		viewerpage.assertEquals(cs.selectionOptionOnContentSelector.get(0).getText(),ViewerPageConstants.RECENT_SELECTION,"Verify Recent selection option on sort by drop down","Verified Recent selection option is present on sort by dropdown");
		viewerpage.assertEquals(cs.selectionOptionOnContentSelector.get(1).getText(),ViewerPageConstants.ASCENDING_SELECTION,"Verify Ascending selection option on sort by drop down","Verified Recent selection option is present on sort by dropdown");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify all result entries are grouped by machine ane and sorted by creatation date");
		viewerpage.assertEquals(cs.allContentSelectorMachine.get(0).getText(),ViewerPageConstants.USER_CREATED_RESULT,"Verify first column of machine description","The first column has machine description : "+ViewerPageConstants.EMPTY_DESCRIPTION );
		viewerpage.assertEquals(cs.allContentSelectorMachine.get(1).getText(),ViewerPageConstants.BONEAGE_MACHINE_NAME1,"Verify second column of machine description","The second column has machine description : "+ViewerPageConstants.TOOLTIP_FIRSTROW_BONEAGE );

		//select ascending as sort option in sort by drop down
		viewerpage.selectFromDropDown(cs.comboBoxOnContentSelector, ViewerPageConstants.ASCENDING_SELECTION);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify all result entries are grouped by machine ane and sorted by ascending order of machine name");
		viewerpage.assertEquals(cs.allContentSelectorMachine.get(0).getText(),ViewerPageConstants.BONEAGE_MACHINE_NAME,"Verify first column of machine description","The first column has machine description : "+ViewerPageConstants.TOOLTIP_FIRSTROW_BONEAGE );
		viewerpage.assertEquals(cs.allContentSelectorMachine.get(2).getText(),ViewerPageConstants.USER_CREATED_RESULT,"Verify second column of machine description","The second column has machine description : "+ViewerPageConstants.EMPTY_DESCRIPTION );
	}

	@Test(groups = {"Chrome","DE738"})
	public void test19_DE738_TC2795_verifyTheSyncAfterSwappingSeriesFromContentSelector() throws Exception  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Images selected through Content Selector gets reset after change in layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		//Select series from content selector
		cs.selectSeriesFromContentSelector(1, secondSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(2, firstSeriesDescriptionAH4);

		//Change layout to 3*3
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		//verifying that the changed series through content selector is retained after change in layout
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionAH4), "Verifying that second series selected from content selector is displayed in first viewbox after change in layout.", "Second series selected from content selector is displayed in first viewbox after change in layout.");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionAH4), "Verifying that first series selected from content selector is displayed in second viewbox after change in layout.", "First series selected from content selector is displayed in second viewbox after change in layout.");

		//Select series from content selector
		cs.selectSeriesFromContentSelector(1, firstSeriesDescriptionAH4);
		cs.selectSeriesFromContentSelector(2, secondSeriesDescriptionAH4);

		//Change layout to 2*2
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		//verifying that the changed series through content selector is retained after change in layout
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionAH4), "Verifying that first series selected from content selector is displayed in first viewbox after change in layout.", "First series selected from content selector is displayed in first viewbox after change in layout.");
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionAH4), "Verifying that second series selected from content selector is displayed in second viewbox after change in layout.", "Second series selected from content selector is displayed in second viewbox after change in layout.");

	}

	@Test(groups = {"Chrome","US687"})
	public void test20_US687_TC2587_verifyDataWrappingForContentSelector() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that if the series name or source name size is large then extra characters should trucated and display ellipses");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);

		patientPage.clickOntheFirstStudy();	

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		//Click on Series description on fourth View box and select Result Tab
		cs.click(cs.getViewPort(4));
		cs.openContentSelector(4, true);

		//verify ellipses are present if result description text is larger than specified size
		viewerpage.assertTrue(viewerpage.verifyTextOverFlowForDataWraping(cs.getSeriesToSelectFromContentSelector(thirdSeriesDescriptionAH4)), "Verify data wrapping for result, if size of result is greater than specified limit", "The Ellipsis is present for result");

	}

	@Test(groups = { "Chrome","DE715","BVT"})
	public void test21_DE715_TC2681_verifyRenderingOfNonDICOMSeriesInVBHavingDICOMSeries() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non-DICOM images are not getting rendered on viewbox containing DICOM images when selected through content selector-Happy Path T.C.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		//Select NON DICOM image in VB having DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify the selection of Non-Dicom to Dicom series from content selector");
		cs.closingBannerAndWaterMark();
		cs.selectSeriesFromContentSelector(1, thirdSeriesDescriptionAH4);
		cs.closingBannerAndWaterMark();
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, thirdSeriesDescriptionAH4), "Verifying that NON DICOM series selected from content selector is displayed in first viewbox having DICOM image", "Non-Dicom series properly rendered with image description.");

		//Verifying that no error message should display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify no console error should be received by user.");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(ViewerPageConstants.SETTRANSFORM_CONSOLE_ERROR),"Verifying that no console error should display related to 'setTransform' ","Verified");

		//Select DICOM image in VB having NON DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify the selection of Dicom to Non-Dicom series from content selector");
		cs.closingBannerAndWaterMark();
		cs.selectSeriesFromContentSelector(3, firstSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(3, firstSeriesDescriptionAH4), "Verifying that DICOM series selected from content selector is displayed in third viewbox having Non DICOM image", "Dicom series properly rendered with image description.");

		//Verifying that no error message should display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify no console error should be received by user.");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(ViewerPageConstants.SETTRANSFORM_CONSOLE_ERROR),"Verifying that no console error should display related to 'setTransform' ","Verified");
	}

	@Test(groups = { "Chrome","DE715"})
	public void test22_DE715_TC2682_verifyRenderingOfNonDICOMSeriesInVBHavingNonDICOMSeries() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Non-DICOM images are not getting rendered on viewbox containing DICOM images when selected through content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(PatientName);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		//Select NON DICOM image in VB having DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/4]", "Verify the selection of Non-Dicom to Non-Dicom series from content selector");
		cs.closeWaterMarkIcon(3);
		cs.click(cs.bannerCloseIcon);
		cs.selectSeriesFromContentSelector(3, fourthSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(3, fourthSeriesDescriptionAH4), "Verifying that NON DICOM series selected from content selector is displayed in third viewbox having non-DICOM image", "Non-Dicom series properly rendered with image description.");

		//Verifying that no error message should display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/4]", "Verify no console error should be received by user.");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(ViewerPageConstants.SETTRANSFORM_CONSOLE_ERROR),"Verifying that no console error should display related to 'setTransform' ","Verified");

		//Select DICOM image in VB having NON DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/4]", "Verify the selection of Dicom to Dicom series from content selector");
		cs.selectSeriesFromContentSelector(1, secondSeriesDescriptionAH4);
		viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionAH4), "Verifying that DICOM series selected from content selector is displayed in first viewbox having DICOM image", "Dicom series properly rendered with image description.");

		//Verifying that no error message should display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/4]", "Verify no console error should be received by user.");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(ViewerPageConstants.SETTRANSFORM_CONSOLE_ERROR),"Verifying that no console error should display related to 'setTransform' ","Verified");
	}

	//need to change patient as PR entry won`t be available for point multiseries
	//@Test(groups = { "Chrome","DE1021","DE1292","Sanity"})
	public void test23_DE1021_TC4147_TC4218_DE1292_TC5345_verifySeriesDescriptionAndPREntryUnderSourceForPointMultiSeries() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification series display in source tab when no json"
				+ "<br> Verification of GSPS PR series tree structure- New requirement <br>"+
				"Series description annotation is not displayed correctly");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNamePointMultiSeries);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);

		String seriesNumber=viewerpage.getSeriesDescriptionOverlayText(1);
		viewerpage.assertEquals(seriesNumber, "0", "Checkpoint[1/17]", "Verify series description display as series number when series description is null" );

		//Select NON DICOM image in VB having DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/17]", "Verify the selection of Non-Dicom to Non-Dicom series from content selector");
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, pointMuliSeries);

		List<String> series = cs.getSeriesDesciptionFromContentSelector(1);
		viewerpage.assertEquals(series.size(),expectedTotalSeries, "Checkpoint[3/17]", "Verifying the series count");
		String prEntriesFrom = DataReader.getPRDesc(ViewerPageConstants.PR_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_PR01_TEXTOVERLAY, pointMuliSeries);
		//	int numberOfPREntries = DataReader.getNumberOfPREntries(PatientXMLConstants.STUDY01_TEXTOVERLAY, pointMuliSeries);
		//	List<String> prentries = cs.getPREntiresForGivenSeries(1, series.get(0));

		for(int i=0 ;i<series.size();i++) {		
			//viewerpage.assertEquals(prentries.size(), numberOfPREntries, "Checkpoint[4/17]", "verifying PR entries count");
			//	viewerpage.assertEquals(prentries.get(0), prEntriesFrom, "Checkpoint[5/17]", "verifying PR entry name");
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, series.get(i)), "Checkpoint[6/17]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");
			//viewerpage.assertTrue(cs.validatePRIsSelectedOnContentSelector(1,series.get(i), prEntriesFrom), "Checkpoint[7/17]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");

		}

		viewerpage.assertEquals(cs.getAllResultDesciptionFromContentSelector(1).size(), 2, "Checkpoint[8/17]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");
		viewerpage.assertTrue(verifyGSPSObjectPresence(1), "Checkpoint[9/17]", "User should see the GSPS PR series loaded into the viewer where GSPS on it.");

		PointAnnotation p = new PointAnnotation(driver);
		p.movePoint(1, 1, 20, 20);
		p.waitForTimePeriod(3000);
		List<String> userGeneratedResults = cs.getAllResultDesciptionFromContentSelector(1);
		viewerpage.assertEquals(userGeneratedResults.size(),4, "Checkpoint[10/17]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");

		// checking the number of results generated for total series
		for(int i=0,j=1;i<2;i++) {
			viewerpage.assertEquals(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,userGeneratedResults.get(i+2),"Checkpoint[11/17]","verifying the clone");

		}

		// checking that current series is not highlighted as result is generated by editing gsps object
		for(int i=0, j=1 ;i<series.size();i++,j++) {		

			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(j, series.get(i)), "Checkpoint[12/17]", "After clicking on the source series only source will be getting displayed on viewer. .");
			//viewerpage.assertFalse(cs.validateResultIsSelectedOnContentSelector(j,series.get(i), prEntriesFrom), "Checkpoint[13/17]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertTrue(verifyGSPSObjectPresence(j), "Checkpoint[14/17]", "User should see the GSPS PR series loaded into the viewer where GSPS on it.");

		}


		for(int i=0, j=1 ;i<series.size();i++,j++) {		

			cs.selectSeriesFromContentSelector(j	, series.get(i));
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(j, series.get(i)), "Checkpoint[15/17]", "After clicking on the source series only source will be getting displayed on viewer. .");
			//viewerpage.assertFalse(cs.validateResultIsSelectedOnContentSelector(j,series.get(i), prEntriesFrom), "Checkpoint[16/17]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertFalse(verifyGSPSObjectPresence(j), "Checkpoint[17/17]", "User should see the GSPS PR series loaded into the viewer where GSPS on it.");

		}

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNamePointMultiSeries, 1, 1);

		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 2, "Checkpoint[17/17]", "Multiseries data doesn't have machine.json file so it will take user defined result layout (1*1) after editing GSPS.");



	}

	@Test(groups = { "Chrome","DE1021"})
	public void test24_DE1021_TC4218_TC4170_verifyPREntryUnderSourceForRTStruct() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of GSPS PR series tree structure- New requirement"
				+ "<br> Verification of Rtstruct series in source tab when no json");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNamePhantomChest+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientNamePhantomChest);
		patientPage.clickOntheFirstStudy();	
		
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		cs = new ContentSelector_old(driver);
		//Select NON DICOM image in VB having DICOM image from content selector
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1]", "Verify the selection of Non-Dicom to Non-Dicom series from content selector");
		int expectedTotalSeries = DataReader.getNumberOfSeries(PatientXMLConstants.STUDY01_TEXTOVERLAY, phantomChectFilepath);

		List<String> series = cs.getSeriesDesciptionFromContentSelector(1);
		viewerpage.assertEquals(series.size(),expectedTotalSeries, "Checkpoint[2]", "Verifying the total number of series present on UI vs in xml");
		String prEntriesFrom = DataReader.getPRDesc(ViewerPageConstants.PR_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_PR01_TEXTOVERLAY, pointMuliSeries);
		int numberOfPREntries = DataReader.getNumberOfPREntries(PatientXMLConstants.STUDY01_TEXTOVERLAY, phantomChectFilepath);
		List<String> prentries = cs.getPREntiresForGivenSeries(1, series.get(0));

		for(int i=0 ;i<series.size();i++) {		
			viewerpage.assertEquals(prentries.size(), numberOfPREntries, "Checkpoint[3]", "verifying the PR entries size");
			viewerpage.assertEquals(prentries.get(0), prEntriesFrom, "Checkpoint[4]", "verifying the PR entries name");
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(1, series.get(i)), "Checkpoint[5]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");
			viewerpage.assertTrue(cs.validatePRIsSelectedOnContentSelector(1,series.get(i), prEntriesFrom), "Checkpoint[6]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");

		}

		viewerpage.assertEquals(cs.getAllResultDesciptionFromContentSelector(1).size(), 0, "Checkpoint[7]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");

		PointAnnotation p = new PointAnnotation(driver);
		p.selectPointFromQuickToolbar(1);
		p.drawPointAnnotationMarkerOnViewbox(1, 20, 20);
		p.waitForTimePeriod(3000);
		List<String> userGeneratedResults = cs.getAllResultDesciptionFromContentSelector(1);
		viewerpage.assertEquals(userGeneratedResults.size(),1, "Checkpoint[8]", "User should see both series with the PR series in source tab and no series listed in result because there is no json file in this data");

		// checking the number of results generated for total series
		for(int i=0,j=1;i<userGeneratedResults.size();i++) {
			viewerpage.assertEquals(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,userGeneratedResults.get(i),"Checkpoint[9]","verifying the User generated clone");

		}

		// checking that current series is not highlighted as result is generated by editing gsps object
		for(int i=0, j=1 ;i<series.size();i++,j++) {		

			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(j, series.get(i)), "Checkpoint[10]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertTrue(cs.validatePRIsSelectedOnContentSelector(j,series.get(i), prEntriesFrom), "Checkpoint[11]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertTrue(verifyGSPSObjectPresence(j), "Checkpoint[12]", "User should see the GSPS PR series loaded into the viewer where GSPS on it.");

		}


		for(int i=0, j=1 ;i<series.size();i++,j++) {		

			cs.selectSeriesFromContentSelector(j	, series.get(i));
			viewerpage.assertTrue(cs.validateSeriesIsSelectedOnContentSelector(j, series.get(i)), "Checkpoint[13]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertFalse(cs.validatePRIsSelectedOnContentSelector(j,series.get(i), prEntriesFrom), "Checkpoint[14]", "After clicking on the source series only source will be getting displayed on viewer. .");
			viewerpage.assertFalse(verifyGSPSObjectPresence(j), "Checkpoint[15]", "User should see the GSPS PR series loaded into the viewer where GSPS on it.");

		}

		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientNamePhantomChest, 1, 1);

		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Checkpoint[16]", "Multiseries data doesn't have machine.json file so it will take user defined result layout (1*1) after editing GSPS.");



	}

	public boolean verifyGSPSObjectPresence(int whichViewbox){
		int count = 0;
		boolean status = false ;

		PointAnnotation point = new PointAnnotation(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		SimpleLine line = new  SimpleLine(driver); 

		count = line.getLinesOrPoints(whichViewbox, true, false).size() + circle.getAllCircles(whichViewbox).size() + ellipse.getEllipses(whichViewbox).size() 
				+ textAn.getTextAnnotations(whichViewbox).size() + point.getAllPoints(whichViewbox).size();

		if(count>0)
			status = true ;

		return status;
	}


}




*/