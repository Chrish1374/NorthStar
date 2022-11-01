package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import java.awt.AWTException;
import java.io.IOException;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;



import jxl.read.biff.BiffException;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SupportedModalitiesTest extends TestBase {

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private CircleAnnotation circle;
	private ContentSelector contentSelector;
	private MeasurementWithUnit lineWithUnit;
	private HelperClass helper;
	private ViewerTextOverlays textOverlay;
	private ViewerLayout layout;
	private ViewerSliderAndFindingMenu findingMenu;
	
	
	String filePath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("Icometrix");
	String IcometrixPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String icoMetrixStudy = DataReader.getStudyDetails(PatientXMLConstants.NUMBER_OF_IMAGES,PatientXMLConstants.STUDY01_TEXTOVERLAY,filePath1);
	
	String LUNIT_MCA_2_filepath = Configurations.TEST_PROPERTIES.get("LUNIT_MCA_2_filepath");
	String LUNIT_MCA_2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, LUNIT_MCA_2_filepath);

	String LUNIT_PTX_1_filepath = Configurations.TEST_PROPERTIES.get("LUNIT_PTX_1_filepath");
	String LUNIT_PTX_1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, LUNIT_PTX_1_filepath);
	
	String LUNIT_PTX_2_filepath = Configurations.TEST_PROPERTIES.get("LUNIT_PTX_2_filepath");
	String LUNIT_PTX_2= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, LUNIT_PTX_2_filepath);
	
	String LUNIT_PTX_3_filepath = Configurations.TEST_PROPERTIES.get("LUNIT_PTX_3_filepath");
	String LUNIT_PTX_3= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, LUNIT_PTX_3_filepath);
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();

	}


	@Test(groups ={"firefox","Chrome","Edge","IE11","US66","US671","Sanity","BVT"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_verifyDefaultLayout"})
	public void test01_US66_US671_verifyDefaultLayout(String Testcase_id,String PatientName , String Modality,String study, String Rows,String Columns) throws InterruptedException, BiffException, IOException 
	{

		Reporter.getCurrentTestResult().setAttribute("TEST_CASE_ID", Testcase_id);
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("US66/US671"+ Testcase_id+"_Verify the "+Modality+" study is loaded in the viewer in "+Rows+"x"+Columns +" layout");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName, 1);
		

		int rows = Integer.parseInt(Rows);
		int columns = Integer.parseInt(Columns);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 1", "Verifying for patient \"<b>"+PatientName+"</b>\" study is loaded in the viewer in "+Rows+" x "+ Columns +" layout"+" for "+Modality+" modality");
		//viewerpage.verifyEquals(viewerpage.tableRows.size(), Integer.parseInt(Rows), "Verify the "+Modality+" modality is loaded in the viewer in "+Rows+"x"+Columns +" layout", "Verified the modality is rendered with correct number of rows");
		//viewerpage.verifyEquals(viewerpage.tableColumns.size(), Integer.parseInt(Columns),  "Verify the "+Modality+" study is loaded in the viewer in "+Rows+"x"+Columns +" layout", "Verified the modality is rendered with correct number of columns");

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);


		//Verify image is rendered 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint 2", "Verifying for patient \"<b>"+PatientName+"</b>\" image is rendered in the viewer in "+Rows+" x "+ Columns +" layout"+" for "+Modality+" modality");

		if(rows==2 && columns==2)
		{

			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verify image in first viewbox rendered", "For" + Modality + "viewbox1");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2), "Verify image in second viewbox rendered", "For"+Modality+"viewbox2");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(3), "Verify image in third viewbox rendered", "For"+Modality+"viewbox3");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(4), "Verify image in fourth viewbox rendered", "For"+Modality+"viewbox4");

		}
		else if(rows==1 && columns==2)
		{
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Verify image in first viewbox rendered", "For"+Modality+"viewbox1");
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(2), "Verify image in second viewbox rendered", "For"+Modality+"viewbox2");
		}
		else if(rows==1 && columns==1)
			viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1), "Verify image in first viewbox rendered", "For"+Modality+"viewbox1");


	}

//	@Test
	/*public void test02_loadingMultipleStudies() throws ParseException
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Loading multiple studies");

		List<WebElement> patients = patientPage.getColumn(2);		
//		List<String> names = new ArrayList<String>();
//		
//		for(int i=0 ;i < patients.size();i++) {
//			names.add(patients.get(i).getText());
//			
//		}
		
		System.out.println(patients.size());
		
		for(int i=0 ;i < 50 ;i++) {
			
			System.out.println(i);
			patientPage.clickOnPatientRow("Qure.ai_XRay_Patient3");

			patientPage.clickOntheFirstStudy();
			
			viewerpage = new ViewerPage(driver);
//			try {
			viewerpage.waitForViewerpageToLoad(3);
//			catch(TimeoutException e) {
			viewerpage.waitForPdfToRenderInViewbox(2);
//			}
			
//			viewerpage.waitForAllImagesToLoad();
			
			viewerpage.navigateToPatientPage();
			patientPage.waitForPatientPageToLoad();
			}
		}*/
		
	@Test(groups ={"Chrome","Edge","IE","DE893"})
	public void test03_DE893_TC3656_VerifyFindingsDropDown() throws Throwable 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Veriying the Findings menu presence in view box");

		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, 1);
		circle= new CircleAnnotation(driver);
		contentSelector=new ContentSelector(driver);

		//Drawing annotations on all of the series present for Multiseries data.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 5, 5, 10, 10);
		circle.drawCircle(1, 25,25,10, 10);
		circle.drawCircle(1, 40, 40, 10, 10);
		circle.drawCircle(1, 60,60, 10, 10);
		circle.drawCircle(1, 80, 80, 10, 10);	
		circle.drawCircle(1, 100,100, 10, 10);
		circle.drawCircle(1, 120, 120, 10, 10);
		circle.drawCircle(1, 150, 150, 10, 10);
		circle.drawCircle(1, 180, 180, 10, 10);
		circle.drawCircle(1, -25, 25, 10, 10);
		circle.drawCircle(1, -50, 50, 10, 10);
		circle.drawCircle(1, -70, 70, 10, 10);
		circle.drawCircle(1, -90, 90, 10, 10);
		circle.drawCircle(1, -110, 110, 10, 10);

		// Changing the layout to 3*3 and opening the AR tool bar and verifying Scroll bar is present.

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/3]","Verifying that Scroll bar is present in 3*3 Layout in AR tool bar");
        layout=new ViewerLayout(driver);
        layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.click(viewerpage.getViewPort(1));
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerpage.mouseHover(findingMenu.findings.get(3));
		viewerpage.scrollDownUsingPerfectScrollbar(findingMenu.findingsText.get(5),findingMenu.findingMenuThumb,5,findingMenu.getHeightOfWebElement(findingMenu.findingMenuScroll));
		viewerpage.scrollDownUsingPerfectScrollbar(findingMenu.findingsText.get(7),findingMenu.findingMenuThumb,5,findingMenu.getHeightOfWebElement(findingMenu.findingMenuScroll));
		viewerpage.assertTrue(findingMenu.findingMenuScroll.isDisplayed(), "Verifying scroll bar is present","Scroll bar is present");

		// Finding the X and Y coordinates of finding table and View box

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/3]","Verifying that findings menu is inside the view box in 3*3 Layout");
		int FX=viewerpage.getValueOfXCoordinate(findingMenu.findingTable);
		int FY= viewerpage.getValueOfYCoordinate(findingMenu.findingTable);

		int VX= viewerpage.getValueOfXCoordinate(viewerpage.getViewPort(1));
		int VY= viewerpage.getValueOfYCoordinate(viewerpage.getViewPort(1));

		// Findings the Width and Height of Findings table and Viewbox.

		int  FW=viewerpage.getWidthOfWebElement(findingMenu.findingTable);
		int  FH=viewerpage.getHeightOfWebElement(findingMenu.findingTable);

		int VW=	viewerpage.getWidthOfWebElement(viewerpage.getViewPort(1));
		int VH=	viewerpage.getHeightOfWebElement(viewerpage.getViewPort(1));

		viewerpage.assertTrue(FX>VX && VW>FW && FY>VY&&FH<VH , "Verifying that the findings menu is inside the view box", "Findings menu is inside the view box");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/3]","Verifying that findings menu is inside the view box in last row of 3*3 Layout");

		String resultname = "_"+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1";
		contentSelector.selectResultFromSeriesTab(9,resultname);
		findingMenu.openFindingTableOnBinarySelector(9);
		viewerpage.assertTrue(FX>VX && VW>FW && FY>VY&&FH<VH, "Verifying that the findings menu is inside the view box", "Findings menu is inside the view box");
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","DE890" })
	public void test04_DE890_TC3681_verifyViewboxForIcomentirx() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verifying 6th view box is empty for 'icomentrix' data");

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(IcometrixPatient);

		patientPage.clickOnStudy(icoMetrixStudy);

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		layout=new ViewerLayout(driver);

		// Comparing image for icometrix data to verify that 6th view box is empty.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify the layout count for Icometrix data and the empty 6th view box");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT),"Validating the total number of layout count for icometrix data", "Total number of layout is 6");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(6),"Verifying the layout for icometrix data is 2*3 and 6th viewbox is empty for icometrix data","TC39_CheckPoint1");


	}

	@Test(groups ={"Chrome","Edge","IE11","DE1070","sanity","positive"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test05_DE1070_PatientList"})
	public void test05_DE1070_TC4504_verifyNoConsoleErrorWhenNavigateBackToStudyPage(String PatientName) throws AWTException, InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  Linear measurement not causes black screen and console error when going back to study list for patient"+" "+PatientName);

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(PatientName,1);;
		lineWithUnit = new MeasurementWithUnit(driver);
		
		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		
		//navigate back to study page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify Linear measurement not causes black screen when user navigate back to study page");
		helper = new HelperClass(driver);
		helper.browserBackAndReloadViewer(patientName,1,1);
		
		
		// Comparing image for icometrix data to verify that 6th view box is empty.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify console error when user navigate back to study page");
		viewerpage.assertFalse(viewerpage.isConsoleErrorPresent(), "Verfiy console error not present for study page", "Verified that console error not seen for"+" "+PatientName);


	}

	

}
