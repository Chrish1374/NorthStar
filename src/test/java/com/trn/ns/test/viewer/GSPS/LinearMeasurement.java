package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//F44_Integration.NS.I34_EnvoyAIIntegration-CF0304ARevD  - revision-0
//Functional.NS.I29_F90_ToolAvailableAndUX-CF0304ARevD - revision-0
// Safety.NS_F90_MeasurementOfDistanceOnImage-CF0304ARevD - revision-0 


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LinearMeasurement extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel preset;
	
	private MeasurementWithUnit lineWithUnit;
	private ContentSelector cs;
	private SimpleLine line; 
	String filePath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	String filePath1=Configurations.TEST_PROPERTIES.get("JobsSteve_filepath");
	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);

	String filePath2=Configurations.TEST_PROPERTIES.get("Phantom_Of_Grid_filepath");
	String PatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath2);

	String filePath3=Configurations.TEST_PROPERTIES.get("TEST^Non_Square_Pixels_filepath");
	String PatientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath3);

	String filePath4=Configurations.TEST_PROPERTIES.get("Test^Pixel_Spacing_filepath");
	String PatientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath4);
	String series1=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY,filePath4);
	String series2=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY,filePath4);

	String filePath5=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String PatientName5 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath5);
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//	TC1: Verify linear measurement icon on radial menu and context menu(Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601","BVT","US2325","F1090","E2E"})
	public void test01_US601_TC1963_TC2032_US2325_TC9777_verifyLinearMeasurementIcon() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement icon on radial menu and context menu."
				+ "<br>	Verify line annotation icon in radial menu"
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Perform a right click and validate ellipse icon on Radial Menu.
		lineWithUnit.openQuickToolbar(lineWithUnit.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that Linear Measurement Icon is visible on radial bar");
		lineWithUnit.assertTrue(lineWithUnit.isElementPresent(lineWithUnit.distanceIcon),"Verify that Linear Measurement Icon is present on radial bar","Linear Measurement Icon is present on radial bar");
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.assertTrue(lineWithUnit.checkCurrentSelectedIcon(ViewerPageConstants.DISTANCE),"Checkpoint[2/3]","Verifying Distance icon is selected in quick toolbar");
		
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.DISTANCE),"Checkpoint[3/3]", "Verifying Distance icon is selected in viewer bar");
	
		
		
	
	} 
	//	TC2: Verify linear measurement icon gets enabled on selection(Automated)
	//	TC3: Verify linear measurement can NOT be drawn on non-DICOM(pdf/jpeg) (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601","DE1924","US2329","BVT","F1090","E2E"})
	public void test02_US601_TC1964_TC1965_DE1924_TC7683_US2329_TC10165_verifyLinearMeasurementIconEnablesOnSelection() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement icon gets enabled on Selection.<br/> Verify Linear Measurement cannot be drawn on Non-DICOM images. <br>"
				+"Verify drawing 'Distance' by selecting it from Radial Menu. <br>"+
				"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);

		lineWithUnit = new MeasurementWithUnit(driver);
	
		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[1] & Checkpoint[1/5]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		//Perform Pan operation
		lineWithUnit.selectPanFromQuickToolbar(lineWithUnit.getViewPort(1));
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(1), 20, 20, 100, 100);
		lineWithUnit.closingConflictMsg();
		
		//Select Linear Measurement from Context Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify that Linear Measurement Icon is selected");
		lineWithUnit.assertTrue(lineWithUnit.checkCurrentSelectedIcon(ViewerPageConstants.DISTANCE),"Verify Linear Measurement Icon is selected", "Linear Measurement icon is selected");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC2[2] & Checkpoint[3/5]", "Verify that PAN icon is de-selected");
		lineWithUnit.assertFalse(lineWithUnit.checkCurrentSelectedIcon(ViewerPageConstants.PAN),"Verify PAN icon is de-selected", "PAN icon is de-selected");

		//Select Linear measurement from Radial Menu on JPEG and draw a linear measurement
		lineWithUnit.mouseHover(lineWithUnit.getViewPort(3));
		lineWithUnit.selectDistanceFromQuickToolbar(3);
		lineWithUnit.drawLine(4, 50, 50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC3[1] & Checkpoint[4/5]", "Verify that Linear Measurement cannot be drawn on JPEG");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(3).size(),0,"Verify there is no instance of Linear Measurement on Viewbox3 with JPEG Result","There is no instances of Linear Measurement on Viewbox3 with JPEG Result");
		//		lineWithUnit.compareElementImage(protocolName, lineWithUnit.getViewPort(3),"Verify that linear measurement should get panned along with DICOM image","TC1964_TC1965_CheckPoint4");

		//draw a linear measurement on PDF
		lineWithUnit.drawLine(4, 50, 50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC3[1] & Checkpoint[5/5]", "Verify that Linear Measurement cannot be drawn on PDF");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),0,"Verify there is no instance of Linear Measurement on Viewbox4 with PDF result","There is no instances of Linear Measurement on Viewbox4 with PDF Result");
		//		lineWithUnit.compareElementImage(protocolName, lineWithUnit.mainViewer,"Verify that linear measurement should get panned along with DICOM image","TC1964_TC1965_CheckPoint5");
	} 

	@Test(groups ={"Chrome","US601"}, dataProvider = "getDataFromExcelFile", dataProviderClass = ExcelDataProvider.class)
	@DataProviderArguments({ "filePath=src/test/resources/data.xls", "sheetName=test03_US601_TC1966" })
	public void test03_US601_TC1996_verifyLinearMeasurementOnAllModality(String PatientName, String Modality,String count) throws TimeoutException, InterruptedException {
		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Verify that linear measurement can be drawn on " + Modality + " data");
		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		lineWithUnit = new MeasurementWithUnit(driver);


		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that linear measurement can be drawn on " + Modality + " data");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),Integer.parseInt(count),"Verify an instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify linear measurement text for " + Modality + " data");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),Integer.parseInt(count),"Verify an instance of linear measurement text"," A Single instance of linear Measurement text is present on Viewbox1");

	}

	//	TC4: Verify linear measurement when pixel spacing values are incorrect (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test04_US601_TC1967_verifyLinearMeasurementWhenPixelValueIncorrect() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement when pixel spacing values are incorrect");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName1,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Checkpoint TC4[1]: Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//verify no pixel spacing appear on measurement text
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that no pixel spacing error appear on measurement text for data with Null Value of Pixel Space row in DB");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).get(0).getText(),ViewerPageConstants.NO_PIXEL_SPACE,"Verify no pixel spacing error appear on measurement text","No pixel spacing error appear on measurement text"); 
		//		lineWithUnit.compareElementImage(protocolName, lineWithUnit.getViewbox(1),"Checkpoint TC4[1]: Verify that no pixel spacing error appear on measurement text","TC1967_CheckPoint2");
	} 
	
	//	TC5: Verify linear measurement box can be moved (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601","Sanity","BVT"})
	public void test05_US601_TC1968_verifyLinearMeasurementCanBeMoved() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Verify Linear Measurement can be moved in Viewbox");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//draw line on the View box
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that linear measurement can be drawn on data");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		//Variable to Store current coordinate of linear measurement and measurement box 
		String beforeX1=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String beforeY1=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String beforeX2=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String beforeY2=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		String beforeTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String beforeTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		String beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//Move linear measurement text 
		lineWithUnit.moveLinearMeasurementText(1, 1, 10, 50);
		String afterTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String afterTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify user is able to move linear measurement text");
		lineWithUnit.assertNotEquals(beforeTextX,afterTextX,"Verify X coordinate of linear measurement text change along with line","The X coordinate of linear measurement text changes from "+beforeTextX+" to "+afterTextX);
		lineWithUnit.assertNotEquals(beforeTextY,afterTextY,"Verify Y coordinate of linear measurement text change along with line","The Y coordinate of linear measurement text changes from "+beforeTextY+" to "+afterTextY);

		//Move linear Measurement 
		lineWithUnit.moveLinearMeasurement(1, 1, 50, 100);

		String afterX1=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.X1);
		String afterY1=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.Y1);
		String afterX2=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.X2); 
		String afterY2=lineWithUnit.getLinearMeasurements(1,1).get(0).getAttribute(ViewerPageConstants.Y2);

		String afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify user is able to move linear measurement inside a Viewbox");
		lineWithUnit.assertNotEquals(beforeX1,afterX1," Checkpoint TC5[1]: Verify X1 coordinate of linear measurement change on move","The X1 coordinate of linear measurement changes from "+beforeX1+" to "+afterX1);
		lineWithUnit.assertNotEquals(beforeY1,afterY1," Checkpoint TC5[1]: Verify Y1 coordinate of linear measurement change on move","The Y1 coordinate of linear measurement changes from "+beforeY1+" to "+afterY1);
		lineWithUnit.assertNotEquals(beforeX2,afterX2," Checkpoint TC5[1]: Verify X2 coordinate of linear measurement change on move","The X2 coordinate of linear measurement changes from "+beforeX2+" to "+afterX2);
		lineWithUnit.assertNotEquals(beforeY2,afterY2," Checkpoint TC5[1]: Verify Y2 coordinate of linear measurement change on move","The Y2 coordinate of linear measurement changes from "+beforeY2+" to "+afterY2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC5[1]: Checkpoint[4/5]", "Verify linear measurement text move along with line");
		lineWithUnit.assertNotEquals(afterTextX,lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X), "Verify X coordinate of linear measurement text change along with line","The X coordinate of linear measurement text changes from "+afterTextX+" to "+lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X));
		lineWithUnit.assertNotEquals(afterTextY,lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y),"Verify Y coordinate of linear measurement text change along with line","The Y coordinate of linear measurement text changes from "+afterY2+" to "+lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC5[1]: Checkpoint[5/5]", "Verify linear measurement value remains same on linear measurement movement");
		lineWithUnit.assertEquals(beforeValue,afterValue,"Verify linear measurement value remain on linear measurement movement","The linear measurement value remain on linear measurement movement :" +beforeValue);

	} 
	
	//	TC6: Verify linear measurement box can NOT be re-sized/deleted (Automated)
	@Test(groups ={"Chrome","IE11","Edge","US601","Sanity","BVT"})
	public void test06_US601_TC1969_verifyLinearMeasurementTextCannotBeEdited() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement box cannot be deleted");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		//		lineWithUnit.drawLine(1, 50, 50, 150, 150);
		lineWithUnit.drawLine(1, 50, -50, 100, 100);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a single instance of Linear Measurement text is drawn on Viewbox1","A Single instance of Linear Measurement text is present on Viewbox1");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC6[1]: Checkpoint[2/2]", "Verify that Linear Measurement text is not editable");
		lineWithUnit.assertTrue(lineWithUnit.isElementEditable(lineWithUnit.getLinearMeasurementsText(1).get(0)),"Verify a single instance of Linear Measurement text is drawn on Viewbox1","A Single instance of Linear Measurement text is present on Viewbox1");

	} 
	
	//	TC7: Verify linear measurement along with all DICOM operations (scroll/PAN/WWWL/zoom) (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test07_US601_TC1970_verifyLinearMeasurementAlongWithAllDICOMOperation() throws InterruptedException{


		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Verify linear measurement along with all DICOM operation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);
		preset=new ViewBoxToolPanel(driver);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -120, -150, 0, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//variable to store current value of all the measurement text
		String beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		// get Zoom level for Canvas 0 before Zoom
		int intialZoomLevel1 = preset.getZoomValue(1);

		//Select a Zoom from radial bar and perform Zoom down
		lineWithUnit.selectZoomFromQuickToolbar(1);
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);
		int finalZoomLevel1 = preset.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"  Checkpoint TC7[1]: Checkpoint[2/9]","Verify Zoom Level after Mouse Up decrease in View Box 1.");
		lineWithUnit.assertTrue(finalZoomLevel1 < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up decreases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);
		String afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify value of linear measurement remains same on zoom");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after zoom down","The value of linear measurement text remains same : "+ afterValue);

		//Perform Pan on Viewbox1
		lineWithUnit.selectPanFromQuickToolbar(lineWithUnit.getViewPort(1));
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);

		//variable to store current value of all the measurement text
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC7[2]: Checkpoint[4/9]", "Verify that linear measurement should get panned along with DICOM image");
		//		lineWithUnit.compareElementImage(protocolName, lineWithUnit.getViewbox(1),"Verify that linear measurement should get panned along with DICOM image","TC1970_CheckPoint4");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify value of linear measurement remains same on pan");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after pan","The value of linear measurement text remains same : "+ afterValue);

		//variable to store current value W and C of viewbox1
		String beforeW = lineWithUnit.getWindowWidthValText(1);
		String beforeC = lineWithUnit.getWindowCenterText(1);

		//Select Window leveling from radial menu
		lineWithUnit.selectWindowLevelFromQuickToolbar(lineWithUnit.getViewPort(1));	
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);

		//variable to store current value W and C of viewbox1
		String afterW = lineWithUnit.getWindowWidthValText(1);
		String afterC = lineWithUnit.getWindowCenterText(1);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC7[3]: Checkpoint[6/9]", "Verify that Window leveling is applied on viewbox1");
		lineWithUnit.assertNotEquals(beforeW,afterW,"Verify Window Width level changes","The value of Window Width changes from "+ beforeW+" to "+afterW);
		lineWithUnit.assertNotEquals(beforeC,afterC,"Verify Window Center level changes","The value of Window Center changes from "+ beforeC+" to "+afterC);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify value of linear measurement remains same on Window Leveling");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after window leveling","The value of linear measurement text remains same : "+ afterValue);

		//Perform scroll to a slice number in a active View box
		lineWithUnit.scrollDownToSliceUsingKeyboard(5);
		lineWithUnit.scrollUpToSliceUsingKeyboard(5);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify that Linear Measurement is present on slice after scrolling");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is present on slice1","A linear measurement is present on slice1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify value of linear measurement remains same on scrolling");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after scrolling","The value of linear measurement text remains same : "+ afterValue);
	}

	//	TC8: Verify linear measurement resizing after applying DICOM operations (scroll/PAN/WWWL/zoom) (Automated) 
	//	TC1: Correctness of linear measurement for 'Pixel Spacing and Imager Pixel Spacing' (Automated)
	//	TC3: Correctness of linear measurement for 'square pixel spacing' (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test08_US601_TC2002_verifyAccuracyOfLinearMeasurementForSquarePixelSpacing() throws InterruptedException{


		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement accuracy for square pixel spacing");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName2,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//draw a horizontal line in 3 square grid
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-4,-20,134,0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify the accuracy of horizontal linear measurement");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 1,ViewerPageConstants.SQUARE_HORIZONTAl),"Checkpoint TC3[1]: Checkpoint TC1[1]: Verify accuracy of horizontal linear measurement line","The linear measurement value for horizontal line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(0).getText());

		//draw a vertical line across 4 square grid
		lineWithUnit.drawLine(1,66,-50,0,180);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the accuracy of vertical linear measurement");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 2,ViewerPageConstants.SQUARE_VERTICAL),"Checkpoint TC3[2]: Checkpoint TC1[2]: Verify accuracy of vertical linear measurement line","The linear measurement value for vertical line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(1).getText());

		//draw a diagonal line across 3 square grid
		lineWithUnit.drawLine(1,42,41,134,134);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify the accuracy of diagonal linear measurement");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 3,ViewerPageConstants.SQUARE_DAIGONAL),"Checkpoint TC3[3] : Checkpoint TC1[2]: Verify accuracy of diagonal linear measurement line","The linear measurement value for vertical line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(2).getText());

	}

	//	TC2: Correctness of linear measurement for 'oblique pixel spacing' (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test09_US601_TC2003_verifyAccuracyOfLinearMeasurementForObliquePixelSpacing() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement accuracy for oblique pixel spacing");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName3,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		preset=new ViewBoxToolPanel(driver);	        
		preset.changeZoomNumber(1,300);
		//draw a horizontal line from left to right of image in Viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-385,-20,768,0);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify the accuracy of horizontal linear measurement");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 1,ViewerPageConstants.OBLIQUE_HORIZONTAl),"Checkpoint TC2[1]: Verify accuracy of horizontal linear measurement line","The linear measurement value for horizontal line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(0).getText());

		//draw a vertical line from top to bottom of image
		lineWithUnit.drawLine(1,40,-193,0,384);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify the accuracy of vertical linear measurement");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 2,ViewerPageConstants.OBLIQUE_HORIZONTAl),"Checkpoint TC2[2]: Checkpoint TC2[3]: Verify accuracy of vertical linear measurement line","The linear measurement value for vertical line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(1).getText());

	}

	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test10_US601_TC2004_verifyAccuracyOfLinearMeasurementForPixelSpacingAndImagerPixelSpacing() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement accuracy for oblique pixel spacing");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName4,1);
		ViewerLayout layout = new ViewerLayout(driver);

		lineWithUnit = new MeasurementWithUnit(driver);
			
		cs = new ContentSelector(driver);

		//Change a layout to 1X1
		layout.selectLayout(layout.oneByOneLayoutIcon);

		//select Computed Radiograph – Pixel Spacing and Imager Pixel Spacing from Content selector
		cs.selectSeriesFromSeriesTab(1, series1);

		//draw a horizontal line from left to right of image in Viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1,-182,280,362,0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify the accuracy of linear measurement for Pixel spacing as well as Imager Pixel spacing data");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 1,ViewerPageConstants.PIXEL_SPACING),"Verify accuracy of linear measurement line","The linear measurement value for line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(0).getText());

		//select Computed Radiograph – Only Imager Pixel Spacing from Content selector
		cs.selectSeriesFromSeriesTab(1, series2);

		//draw a horizontal line from left to right of image in Viewbox1
		lineWithUnit.drawLine(1,-183,280,362,0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify the accuracy of linear measurement for Imager Pixel spacing data");
		lineWithUnit.assertTrue(lineWithUnit.verifyAccuracyOfLinearMeasurement(1, 1,ViewerPageConstants.IMAGER_SPACING),"Verify accuracy of linear measurement line","The linear measurement value for line is accurate :"+lineWithUnit.getLinearMeasurementsText(1).get(0).getText());
	}

	//	TC4: Correctness of linear measurement while resizing line (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601","DE1329"})
	public void test12_US601_TC2005_DE1329_TC5646_verifyLinearMeasurementWhileResizingRuler() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement while resizing line");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1]: Checkpoint[1/3]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//variable to store all measurement value
		String beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//resize line by moving left end of the line
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50, -100);

		//variable to store all measurement value after resize
		String afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2]:Checkpoint[2/3]", "Verify value of linear measurement text change on resizing ruler from left handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		//resize line by moving right end of the line
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30,30);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);
	} 

	//	TC8: Verify linear measurement resizing after applying DICOM operations (scroll/PAN/WWWL/zoom) (Automated) 
	@Test(groups ={"Chrome","IE11","Edge","US601"})
	public void test13_US601_TC2033_verifyLinearMeasurementAlongWithAllDICOMOperationAfterResizingOfRuler() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement resizing after applying all DICOM operation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -50, 120, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/13]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//variable to store current value of all the measurement text
		String beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		// get Zoom level for Canvas 0 before Zoom
		int intialZoomLevel1 = preset.getZoomValue(1);

		//Select a Zoom from radial bar and perform Zoom down
		lineWithUnit.selectZoomFromQuickToolbar(1);
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);
		int finalZoomLevel1 = preset.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"  Checkpoint TC8[1]: Checkpoint[2/13]","Verify Zoom Level after Mouse Up decrease in View Box 1.");
		lineWithUnit.assertTrue(finalZoomLevel1 < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up decreases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);
		String afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/13]", "Verify value of linear measurement remains same on zoom");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after zoom down","The value of linear measurement text remains same : "+ afterValue);

		//resize line by moving right end of the line
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, 20,20);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[4/13]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		//store the current value of measurement text
		beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//Perform Pan on Viewbox1
		lineWithUnit.selectPanFromQuickToolbar(lineWithUnit.getViewPort(1));
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);

		//variable to store current value of all the measurement text
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[5/13]", "Verify that linear measurement should get panned along with DICOM image");
		//		lineWithUnit.compareElementImage(protocolName, lineWithUnit.getViewbox(1),"Verify that linear measurement should get panned along with DICOM image","TC2033_CheckPoint4");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/13]", "Verify value of linear measurement remains same on pan");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after pan","The value of linear measurement text remains same : "+ afterValue);

		//resize line by moving right end of the line
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, 20,20);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[7/13]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		//store the current value of measurement text
		beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//variable to store current value W and C of viewbox1
		String beforeW = lineWithUnit.getWindowWidthValText(1);
		String beforeC = lineWithUnit.getWindowCenterText(1);

		//Select Window leveling from radial menu
		lineWithUnit.selectWindowLevelFromQuickToolbar(lineWithUnit.getViewPort(1));	
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewbox(1), 0, 0, 50,50);

		//variable to store current value W and C of viewbox1
		String afterW = lineWithUnit.getWindowWidthValText(1);
		String afterC = lineWithUnit.getWindowCenterText(1);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[8/13]", "Verify that Window leveling is applied on viewbox1");
		lineWithUnit.assertNotEquals(beforeW,afterW,"Verify Window Width level changes","The value of Window Width changes from "+ beforeW+" to "+afterW);
		lineWithUnit.assertNotEquals(beforeC,afterC,"Verify Window Center level changes","The value of Window Center changes from "+ beforeC+" to "+afterC);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[9/13]", "Verify value of linear measurement remains same on Window Leveling");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after window leveling","The value of linear measurement text remains same : "+ afterValue);

		//resize line by moving right end of the line
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30,20);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[10/13]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		//store the current value of measurement text
		beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//Perform scroll to a slice number in a active Viewbox
		lineWithUnit.scrollDownToSliceUsingKeyboard(5);
		lineWithUnit.scrollUpToSliceUsingKeyboard(5);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[11/13]", "Verify that Linear Measurement is present on slice after scrolling");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is present on slice1","A linear measurement is present on slice1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[12/13]", "Verify value of linear measurement remains same on scrolling");
		lineWithUnit.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after scrolling","The value of linear measurement text remains same : "+ afterValue);

		//resize line by moving right end of the line
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50,-50);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " Checkpoint TC8[2]: Checkpoint[13/13]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

	}

	@Test(groups ={"Chrome","IE11","Edge"})
	public void test14_DE446_TC1765_verifyScrollCommandAfterDrawingLinearMeasurement() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify Scroll is working after drawing linear measurement on viewbox");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//variable to store current slice position
		int currentImageViewBox1 = lineWithUnit.getCurrentScrollPositionOfViewbox(1);

		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.closingConflictMsg();	
		
		//variable to store current slice position after drawing linear measurement
		int imageAfterLinearViewBox1 = lineWithUnit.getCurrentScrollPositionOfViewbox(4);

		//verify scroll does not trigger when user select and draw a linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify Scrolling does not occur while drawing line");
		lineWithUnit.assertEquals(currentImageViewBox1,imageAfterLinearViewBox1,"Verify scroll does not get trigger while drawing linear measurement","Scroll does not get trigger while drawing linear measurement on Viewbox1");

		//variable to store current slice position
		int currentImageViewBox4 = lineWithUnit.getCurrentScrollPositionOfViewbox(1);

		//Draw a linear measurement from radial menu in viewbox4
		lineWithUnit.drawLine(4, -10, -30, 90, 80);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),1,"Verify a Linear Measurement is drawn on Viewbox4","A Single instance of Linear Measurement is present on Viewbox4");

		//Select scroll from radial menu
		lineWithUnit.selectScrollFromQuickToolbar(4);
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(4),0,-130, 0, 130);

		//variable to store current slice position after drawing linear measurement
		int imageAfterScrollViewBox4 = lineWithUnit.getCurrentScrollPositionOfViewbox(4);

		//Verify slice position changes after scrolling
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify scrolling occur on Viewbox4");
		lineWithUnit.assertNotEquals(currentImageViewBox4,imageAfterScrollViewBox4,"Verify scrolling occur in Viewbox4","Image slice changes from "+currentImageViewBox4+" "+imageAfterScrollViewBox4);

		//Verify linear measurement is not drawn on View box when scroll is selected from context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify that linear measurement is not drawn, while scrolling is performed");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),0,"Verify number of linear measurement on Viewbox4","There is no instance of Linear Measurement on current slice in Viewbox4");

		// scroll to next images in series  
		lineWithUnit.dragAndReleaseOnViewer(lineWithUnit.getViewPort(4),0,-110, 0, 110);

		//Verify slice position changes after scrolling
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify scrolling occur on Viewbox4");
		lineWithUnit.assertNotEquals(imageAfterScrollViewBox4,lineWithUnit.getCurrentScrollPositionOfViewbox(4),"Verify scrolling occur in Viewbox4","Image slice changes from "+imageAfterScrollViewBox4+" "+lineWithUnit.getCurrentScrollPositionOfViewbox(4));

		//Verify linear measurement is not drawn on View box when scroll is selected from context menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify that linear measurement is not drawn, while scrolling is performed");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),0,"Verify number of linear measurement on Viewbox4","There is no instance of Linear Measurement on current slice in Viewbox4");

	}

	//TC2097 - Save linear measurements to GSPS with text- Saving Text and location of Text
	@Test(groups ={"Chrome","IE11","Edge","US618","US950","DE1329"})
	public void test15_US618_TC2097_US950_TC4300_TC4303_DE1329_TC5646_verifyLinearMeasurementTextCanBeMoved() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Save linear measurements to GSPS with text- Saving Text and location of Text");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//draw line on the View box
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify that linear measurement can be drawn on data");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		String beforeTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String beforeTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		//Move linear measurement text 
		lineWithUnit.moveLinearMeasurementText(1, 1, 10, 50);
		String afterTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String afterTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify user is able to move linear measurement text");
		lineWithUnit.assertNotEquals(beforeTextX,afterTextX,"Verify X coordinate of linear measurement text change along with line","The X coordinate of linear measurement text changes from "+beforeTextX+" to "+afterTextX);
		lineWithUnit.assertNotEquals(beforeTextY,afterTextY,"Verify Y coordinate of linear measurement text change along with line","The Y coordinate of linear measurement text changes from "+beforeTextY+" to "+afterTextY);

		//Step - 2 : Rejecting the drawn linear measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify linear measurement annotation is rejected");
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		lineWithUnit.waitForTimePeriod(2000);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation in displaying as rejected");		

		//Moving browser back
		lineWithUnit.browserBackWebPage();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]","Verifying user is navigated to single patient list screen");

		patientPage.clickOntheFirstStudy();
		

		//Verifying that the linear measurement is present after reloading
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC5[1] & Checkpoint[5/5]","Verify that the location of measurement with text are persisted and loaded on viewer");
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation in displaying as rejected");		
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");



	}

	@Test(groups ={"Chrome","IE11","Edge"})
	public void test16_DE675_TC2421_verifyLinearMeasurementNotMoveOnMouseHover() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Verify that Linear measurement doesn't move on mouse hover event when user click on handle");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//draw line on the View box
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		
		lineWithUnit.closingConflictMsg();		
//		lineWithUnit.selectLinearMeasurement(1, 1);
//		//Taking screenshot 
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		lineWithUnit.takeElementScreenShot(lineWithUnit.getViewPort(1), newImagePath+"/goldImages/TC2421_checkpoint1.png");

		
		lineWithUnit.mouseHover(lineWithUnit.getViewPort(2));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that linear measurement can be drawn on data");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a single instance of Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");

		String beforeTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String beforeTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		//Click on measurement handle and move mouse
		lineWithUnit.getResizeHandleForSelectedLinearMeasurement(1, 2).click();
		lineWithUnit.mouseMovement(5, 4);

		String afterTextX=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X);
		String afterTextY=lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y);

		//Verifying that the measurement position is not changing
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify linear measurement doesn't move on mouse move event when user click on handle");
		lineWithUnit.assertEquals(beforeTextX,afterTextX,"Verify X coordinate of linear measurement","The X coordinate of linear measurement doesn't move");
		lineWithUnit.assertEquals(beforeTextY,afterTextY,"Verify Y coordinate of linear measurement","The Y coordinate of linear measurement doesn't move");

//		lineWithUnit.waitForTimePeriod(2000);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]","Verify that the location of measurement with text are persisted after mouse move");
//		lineWithUnit.takeElementScreenShot(lineWithUnit.getViewbox(1), newImagePath+"/actualImages/TC2421_checkpoint1.png");
//
//		String expectedImagePath = newImagePath+"/goldImages/TC2421_checkpoint1.png";
//		String actualImagePath = newImagePath+"/actualImages/TC2421_checkpoint1.png";
//		String diffImagePath = newImagePath+"/goldImages/diffImage_TC2421_checkpoint1.png";
//
//		boolean cpStatus =  lineWithUnit.compareimages(expectedImagePath, actualImagePath, diffImagePath);
//		lineWithUnit.assertTrue(cpStatus, "The actual and Expected image are same.","");

	}

	@Test(groups ={"Chrome","IE11","Edge","DE902","Positive"})
	public void test17_DE902_TC3499_verifyStateIsSavedOnLinearMeasurementMovement() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that linear measurement position should get save after moving");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(PatientName5,  1, 1);	
		ViewerLayout layout = new ViewerLayout(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		
		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify that Linear Measurement is drawn on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		lineWithUnit.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//variable to store all measurement value
		String beforeValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		//resize line by moving left end of the line
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50, -100);

		//variable to store all measurement value after resize
		String afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify value of linear measurement text change on resizing ruler from left handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		//resize line by moving right end of the line
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30,30);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify value of linear measurement text change on resizing ruler from right handle");
		lineWithUnit.assertNotEquals(afterValue,beforeValue,"Verify value of linear measurement text change on resizing ruler","The value of linear measurement text changes from "+beforeValue+" to "+afterValue);

		int beforeTextX=((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X))).intValue();
		int beforeTextY=((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y))).intValue();

		//Move linear measurement text 
		lineWithUnit.moveLinearMeasurementText(1, 1, 10, 50);
		
		int afterTextX=((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X))).intValue();
		int afterTextY=((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y))).intValue();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify user is able to move linear measurement text");
		lineWithUnit.assertNotEquals(beforeTextX,afterTextX,"Verify X coordinate of linear measurement text change along with line","The X coordinate of linear measurement text changes from "+beforeTextX+" to "+afterTextX);
		lineWithUnit.assertNotEquals(beforeTextY,afterTextY,"Verify Y coordinate of linear measurement text change along with line","The Y coordinate of linear measurement text changes from "+beforeTextY+" to "+afterTextY);

		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		helper.browserBackAndReloadViewer(PatientName5,  1, 1);
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify measurement state is saved on viewer reload");
		lineWithUnit.assertEquals(afterValue,lineWithUnit.getLinearMeasurementsText(1).get(0).getText(),"Verifying the text","The value of linear measurement text not changed from "+beforeValue+" to "+afterValue);
		lineWithUnit.assertEquals(((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X))).intValue(),afterTextX-1,"Verify X coordinate of linear measurement","verified");
		lineWithUnit.assertEquals(((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y))).intValue(),afterTextY,"Verify Y coordinate of linear measurement","verified");


		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper.loadViewerPageUsingSearch(PatientName5,  1, 1);
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify state of linear measurement is saved on logout and reload of viewer");
		lineWithUnit.assertEquals(afterValue,lineWithUnit.getLinearMeasurementsText(1).get(0).getText(),"Verifying the text","The value of linear measurement text not changed from "+beforeValue+" to "+afterValue);
		lineWithUnit.assertEquals(((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.X))).intValue(),afterTextX-1,"Verify X coordinate of linear measurement","verified");
		lineWithUnit.assertEquals(((Float)Float.parseFloat(lineWithUnit.getLinearMeasurementsText(1).get(0).getAttribute(ViewerPageConstants.Y))).intValue(),afterTextY,"Verify Y coordinate of linear measurement","verified");


	} 

	@Test(groups ={"Chrome","IE11","US986","Positive"})
	public void test09_US986_TC4198_TC4208_TC4287_verifySelectionForDistance() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that distance annotation should not get highlighted when user perform right click on distance annotation"+ 
				"<br> Verify that A/R should get display when user perform left click on linear measurement annotation");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName,1);

		lineWithUnit=new MeasurementWithUnit(driver);
		

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify drawn annotaion is current active GSPS" );
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 0, 150, 0);
		line.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verify distance annotation is current active GSPS object", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify drawn annotaion is not active GSPS after performing right click on annotation");
		line.performMouseRightClick(lineWithUnit.getAllLinearMeasurements(1).get(0));
		line.assertFalse(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verify distance annotation is not active GSPS object", "Verified");

		line.click(line.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify A/R when user perform left click on drawn annotation ");
		lineWithUnit.selectLinearMeasurementWithLeftClick(1, 1);
		line.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verify distance annotation is active GSPS object", "Verified");
		line.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1),"Verify A/R display when user perform left click on distance annotation", "Verified");

	}

	@Test(groups ={"Chrome","DE1062","negative"})
	public void test18_DE1062_TC4531_verifyLinearMeasurementNodeDisplacement() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement box cannot be deleted");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);

		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);

		//Draw linear measurement in viewbox 1
		lineWithUnit.drawLine(1, 50, 0, 100, 0);

		WebElement handle2 = lineWithUnit.getResizeHandleForSelectedLinearMeasurement(1,2);

		//Fetching values of X,Y coridinates of second node
		String x1_BeforeDrag = handle2.getAttribute(NSGenericConstants.CX);
		String y1_BeforeDrag = handle2.getAttribute(NSGenericConstants.CY);

		//Moving first node in such way that it cross node 2
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, 250, 0);

		//Fetching values of X,Y coridinates of second node
		String x1_AfterDrag = handle2.getAttribute(NSGenericConstants.CX);
		String y1_AfterDrag = handle2.getAttribute(NSGenericConstants.CY);

		//Verifying that while moving node 1 and crossing it from node 2 , Position (x,y) coordinates of node 2 should not get change
		lineWithUnit.assertEquals(x1_AfterDrag, x1_BeforeDrag,"Checkpoint[1/2]", "verified node 1 X co-ordinate");
		lineWithUnit.assertEquals(y1_AfterDrag, y1_BeforeDrag,"Checkpoint[1/2]", "verifying node 1 Y co-ordinate");

		WebElement handle1 = lineWithUnit.getResizeHandleForSelectedLinearMeasurement(1,1);

		//Fetching values of X,Y coordinates of first node
		String x2_BeforeDrag = handle1.getAttribute(NSGenericConstants.CX);
		String y2_BeforeDrag = handle1.getAttribute(NSGenericConstants.CY);

		//Moving second node in such way that it cross node 1	
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 250, 0);

		//Fetching values of X,Y cooridinates of first node
		String x2_AfterDrag = handle1.getAttribute(NSGenericConstants.CX);
		String y2_AfterDrag = handle1.getAttribute(NSGenericConstants.CY);

		//Verifying that while moving node 2 and crossing it from node 1 , Position (x,y) coordinates of node 1 should not get change
		lineWithUnit.assertEquals(x2_AfterDrag, x2_BeforeDrag,"Checkpoint[2/2]", "verified node 2 X co-ordinate");
		lineWithUnit.assertEquals(y2_AfterDrag, y2_BeforeDrag,"Checkpoint[2/2]", "verifying node 2 Y co-ordinate");

	}
	
	//DE1444:Linear measurement handle remains displayed on navigating on the finding menu from sliding bar
	@Test(groups ={"Chrome","IE11","DE1444","Positive"})
    public void test19_DE1444_TC5828_TC5829_verifyLinearMeasurementNavigationFromFindingMenu() throws InterruptedException{
    
        extentTest = ExtentManager.getTestInstance();    
        extentTest.setDescription("Verify Linear measurement handle is visible only when the annotation is selected or focused when navigating from the finding menu from sliding bar"+
        "<br> Verify Linear measurement handle is visible only when the annotation is selected or focused when navigating from the Accept/Reject toolbar");

        //Loading the patient on viewer
        patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);

        lineWithUnit = new MeasurementWithUnit(driver);
        line = new  SimpleLine(driver);
         
        //Draw a linear measurement from radial menu
        lineWithUnit.selectDistanceFromQuickToolbar(1);
        lineWithUnit.drawLine(1, -70, -80, -120, 90);
        lineWithUnit.drawLine(1, 50, 50, 100, 50);
        lineWithUnit.drawLine(1, 50, 0, 150, 0);
       
        //draw multiple lines from context menu
        line.selectLineFromQuickToolbar(1);
        line.drawLine(1,-10,10,250,10);
        line.drawLine(1,-30,-50,150,170);
        line.doubleClick(line.getViewPort(1));
        line.waitForAllChangesToLoad();

        int totalCountOfFindings = line.getAllGSPSObjects(1).size();
        List<String>findinglist=line.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
      
        //Scroll after hovering on finding marker on finding slider 
        line.getFindingMarkersOnSlider(1);
        line.assertTrue(verifyHandlesWhenFindingIsHighlighted(1, findinglist,totalCountOfFindings,2), "Checkpoint[1]:Verify if handles should be visible for only the annotation which is selected or focused", "Handles should be visible for only the annotation which is selected or focused");
  
        //Scroll after hovering mouse on A/R tool bar and click on finding menu
		line.mouseHover(line.acceptRejectToolbar);
		line.click(line.gspsFinding);
        line.assertTrue(verifyHandlesWhenFindingIsHighlighted(1, findinglist,5,2), "Checkpoint[2]:Verify if handles should be visible for only the annotation which is selected or focused", "Handles should be visible for only the annotation which is selected or focused");
              
        }        

	@Test(groups ={"Chrome","IE11","Edge","DE1924","Positive","US2325","F1090","E2E"})
	public void test20_DE1924_TC7682_US2325_TC9778_verifySelectionOfDistanceUsingShortcutKey() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify drawing 'Distance' selecting it using short cut key 'd'."
				+ "<br> Verify icons are getting highlighted in quick toolbar when keyboard shortcuts are selected for cine/WW/Distance");

		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(PatientName5, 1, 1);
		lineWithUnit = new MeasurementWithUnit(driver);
		
	
		//Select Linear measurement from Radial Menu
		lineWithUnit.enableOrDisableDistanceIconUsingKeyboardDKey();
		lineWithUnit.openQuickToolbar(1);
		lineWithUnit.assertTrue(lineWithUnit.checkCurrentSelectedIcon(ViewerPageConstants.DISTANCE),"Checkpoint[1/5]", "Linear Measurement icon is selected after pressing D from keyboard");
		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIcon(ViewerPageConstants.DISTANCE),"Checkpoint[3/3]", "Verifying Distance icon is selected in viewer bar");
		lineWithUnit.click(50,50);
		
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Checkpoint[2/5]","A Single instance of Linear Measurement is present on Viewbox1");

		//reload viewer page
		helper.browserBackAndReloadViewer(PatientName5, 1, 1);
		
		//verify scroll icon is by default selected and Distance icon is disabled
		lineWithUnit.openQuickToolbar(1);
		lineWithUnit.assertTrue(lineWithUnit.checkCurrentSelectedIcon(ViewerPageConstants.SCROLL),"Checkpoint[3/5]", "Scroll icon is by default selected");
		
		lineWithUnit.enableOrDisableDistanceIconUsingKeyboardDKey();
		lineWithUnit.drawLine(1, 50, 50, 50, 0);
		lineWithUnit.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),2,"Checkpoint[5/5]","A two instance of Linear Measurement is present on Viewbox1");	
		
	}
	
	//AR toolbar is getting displayed even when only left click is perfomed after selecting annotation from radial menu.
	@Test(groups ={"Chrome","IE11","Edge","DE1903","DE1928","Positive"})
	public void test21_DE1903_TC7794_TC7796_DE1928_TC7806_TC7808_verifyARToolbarAfterSelectingAnnotationFromRadialMenu() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verfify AR toobbar is not getting displayed when only left click is performed after selecting the annotation from radial menu (Selecting a linear measurement). <br>"
				+"Verify AR toobbar is getting displayed after drawing a a linear measurement");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(PatientName5,1);


		lineWithUnit = new MeasurementWithUnit(driver);
		

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.clickAt(0, 0);
		lineWithUnit.assertTrue(lineWithUnit.verifyResultAppliedTextPresence(1), "Checkpoint[1/5]", "Verified result applied text after perform mouse left click by selecting distance from radial menu.");
		
		lineWithUnit.mouseHover(lineWithUnit.getGSPSHoverContainer(1));
		lineWithUnit.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1), "Checkpoint[2/5]", "Verified that AR toolbar not visible on viewer");
	
		//draw linear measurement and verify AR toolbar
		lineWithUnit.drawLine(1, -50, -50, 150, 150);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1), "Checkpoint[3/5]", "Verified that AR toolbar visible on viewer after drawing distance measurement.");
		
		lineWithUnit.click(lineWithUnit.getViewPort(1));
		lineWithUnit.assertFalse(lineWithUnit.isAcceptRejectToolBarPresent(1), "Checkpoint[4/5]", "Verified that AR toolbar not visible on viewer because annotation is not highlighted.");
		
		lineWithUnit.mouseHover(lineWithUnit.getGSPSHoverContainer(1));
		lineWithUnit.assertTrue(lineWithUnit.isAcceptRejectToolBarPresent(1), "Checkpoint[5/5]", "Verified that AR toolbar visible on viewer when mouse hover on AR toolbar area.");
	}
	

    public boolean verifyHandlesWhenFindingIsHighlighted(int viewbox, List<String> findinglist,int totalCountOfFindings, int activeHandles) throws InterruptedException
    {
    
        boolean status=true;
        for(int i =1;i<totalCountOfFindings;i++)
        {
            ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling the finding using mouse wheel - finding"+i);
            line.scrollFindingsInSlider(ViewerPageConstants.SCROLL,1);   
           
        }       
        
        
   return status;
        
    }
}

