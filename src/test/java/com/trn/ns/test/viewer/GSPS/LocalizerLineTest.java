package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class LocalizerLineTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	
	private ContentSelector cs;

	String pmap = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmap);

	String tcga = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcga);

	String aidoc = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocMachine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidoc);

	String imbio =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbio);

	String ibl =Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String iblJohnDeoPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ibl);
	
	String mrLSP = Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String mrLSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, mrLSP);
	
	String cardiacMR = Configurations.TEST_PROPERTIES.get("Cardiac_MR_T1T2_02_filepath");
	String cardiacMRPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cardiacMR);
	private HelperClass helper;
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateFrameRefIDOfPatient(aidocMachine, "1.22.333.444.555.666.7777.8888.9999");

	}

	@Test(groups ={"Chrome","IE11","Edge","US1255","Negative","BVT"})
	public void test01_US1255_TC7161_VerifyLocalizerLinePresenceForOtherDataset() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that localizer line not present in 1X1 layout on default loading.");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(patientNameTCGA,  1, 1);
		

		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		rt.assertFalse(rt.verifyLocalizerLinePresence(1), "Checkpoint[1/2]", "Localizer line should not be present.");

		helper.browserBackAndReloadViewer("",pmapPatientName,  1, 1);
		
		rt.assertFalse(rt.verifyLocalizerLinePresence(1), "Checkpoint[2/2]", "Localizer line should not be present.");


	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Positive","BVT"})
	public void test02_US1255_TC7164_TC7178_VerifyLocalizerLinePresenceAndOnLayoutChange() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that Localizer is present default in inactive view box on loading viewer."
				+ "<br> Verify that localizer line is present in non active view boxes on layout change.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocMachine,1);
		ViewerLayout layout = new ViewerLayout(driver);

		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[1/15]", "Localizer line should not be present.");		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that inactive box has no localizer line");

		for(int i =2; i<=3;i++) 
			verifyLocalizerHorizontalLine(i,2,15);
	
		viewerPage.mouseHover(viewerPage.getViewPort(3));		
		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[3/15]", "Localizer line should not be present.");

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the vertical line if phase is changed");
		for(int i =1; i<=2;i++) 
			verifyLocalizerVerticalLine(i,4,15);

		viewerPage.mouseHover(viewerPage.getViewPort(2));		
		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[5/15]", "Localizer line should not be present.");

		for(int i =1; i<=3;i++) {
			if(i==2)
				continue;			
			verifyLocalizerLine(i, 6, 15);
			
			if(i==1)
				verifyLocalizerHorizontalLine(i,7,15);
			else
				verifyLocalizerVerticalLine(i,7,15);
		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the localizer line post layout change");
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.click(viewerPage.getViewPort(4));
		viewerPage.click(viewerPage.getViewPort(1));

		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[8/15]", "Localizer line should not be present.");
		for(int i =2; i<=3;i++) {
			
			verifyLocalizerHorizontalLine(i,9,15);
		}
		for(int i =4; i<=6;i++)
			viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(i), "Checkpoint[10/15]", "Localizer line should not be present.");


		viewerPage.mouseHover(viewerPage.getViewPort(3));		
		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[11/15]", "Localizer line should not be present.");

		for(int i =1; i<=4;i++) {
			if(i==3)
				continue;
			verifyLocalizerVerticalLine(i,12,15);
		}
		for(int i =5; i<=6;i++)
			viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(i), "Checkpoint[13/15]", "Localizer line should not be present.");


		viewerPage.mouseHover(viewerPage.getViewPort(2));		
		viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[14/15]", "Localizer line should not be present.");

		for(int i =1; i<=3;i++) {
			if(i==2)
				continue;

			if(i==1||i==4)
				verifyLocalizerHorizontalLine(i,15,15);
			else
				verifyLocalizerVerticalLine(i,15,15);
		}

	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Negative"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test03_localizerLine"})
	public void test03_US1255_TC7170_VerifyLocalizerLineForPdfSRAndSC(String patientFilePath) throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that  localizer line is not getting displayed for NonDicom, SR, SC, Pdf data set.");

		//Loading the patient on viewer

		String file =Configurations.TEST_PROPERTIES.get(patientFilePath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, file);

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,2);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying that if it is other type of data no localizer line should be displayed");
		for(int i =1; i<=viewerPage.getNumberOfCanvasForLayout();i++) 
		{
			viewerPage.mouseHover(viewerPage.getViewPort(i));
			for(int j =1; j<viewerPage.getNumberOfCanvasForLayout();j++)			
				viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(j), "Checkpoint[1/1]", "Localizer line should not be present.");

		}



	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Negative","BVT"})
	public void test04_US1255_TC7171_VerifyLocalizerLineForSamePlanes() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that localizer line is not present for same type of planes loaded on viewer.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(aidocMachine,1);


		cs = new ContentSelector(driver);
		
		List<String> series = cs.getAllSeries();
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying if same plane is loaded using content selector then no localizer line should be displayed");
		for(int j =0; j<series.size();j++)
		{
			for(int i =1; i<=cs.getNumberOfCanvasForLayout();i++) 
				cs.selectSeriesFromSeriesTab(i, series.get(j));

			for(int i =1; i<=cs.getNumberOfCanvasForLayout();i++) 
			{
				cs.mouseHover(cs.getViewPort(i));
				for(int k =1; k<cs.getNumberOfCanvasForLayout();k++)
				{	cs.assertFalse(cs.verifyLocalizerLinePresence(k), "Checkpoint[1]", "Localizer line should not be present.");
				}
			}


		}


	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Positive"})
	public void test05_US1255_TC7172_VerifyLocalizerLineForAllTypeOfOverlays() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that localizer line is present for all types of Text overlays.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocMachine,1);
		ViewerTextOverlays textOverlay = new ViewerTextOverlays(driver);

		List<String> overlays = textOverlay.getTextOverlaysOptions();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the localizer line for all types of overlays");

		for(int j =0; j<overlays.size();j++)
		{

			textOverlay.selectTextOverlays(overlays.get(j).toLowerCase());

			for(int i =1; i<=viewerPage.getNumberOfCanvasForLayout();i++) 
			{
				viewerPage.mouseHover(viewerPage.getViewPort(i));
				for(int k =1; k<viewerPage.getNumberOfCanvasForLayout();k++)
				{
					if(i==k)
						continue;
					verifyLocalizerLine(k,j,overlays.size());
				}
			}
		}
	}

	@Test(groups ={"Chrome","IE11","Edge","US1255","Positive","BVT"})
	public void test06_US1255_TC7174_TC7176_TC7177_VerifyLocalizerLineChangesOnScroll() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify the localizer line is shifting across the image in inactive view box as per the scrolling performed on the active view box."
				+ "<br> Verify that localizer line is present in Axial series while slice scroll is performed in Saggital view box."
				+ "<br> Verify that localizer line is present in Axial series while slice scroll is performed in Coronal view box.");


		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocMachine,1);


		viewerPage.scrollToImage(1, 1);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the localizer line changes it's location when scroll happens");
		WebElement line1 = viewerPage.getLocalizerLine(2);
		String vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
		String vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);

		String vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
		String vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


		WebElement line2 = viewerPage.getLocalizerLine(3);
		String vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
		String vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);

		String vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
		String vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);


		for(int j =1; j<30;j++)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the  first viewbox using keyboard");
			viewerPage.scrollDownToSliceUsingKeyboard(2);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[2/5]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[3/5]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[1/30]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[2/30]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[3/30]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[4/30]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb2");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1))>Double.parseDouble(vb2y1), "Checkpoint[5/30]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2))>Double.parseDouble(vb2y2), "Checkpoint[6/30]", "Verifying that new y2 is greater than old Y2");

			

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[7/30]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[8/30]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[9/30]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[10/30]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[11/30]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[12/30]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb3");
			
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1))>Double.parseDouble(vb3y1), "Checkpoint[13/30]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2))>Double.parseDouble(vb3y2), "Checkpoint[14/30]", "Verifying that new y2 is greater than old Y2");

			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}

		for(int j =10; j>=1;j--)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling up the  first viewbox using keyboard");
			viewerPage.scrollUpToSliceUsingKeyboard(3);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[15/30]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[16/30]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[17/30]", "Verifying the y1 coordinate is not same");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[20/30]", "Verifying the y2 coordinate is not same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[21/30]", "Verifying the x1 coordinate is same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[22/30]", "Verifying the x2 coordinate is same");
			
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1))<Double.parseDouble(vb2y1), "Checkpoint[18/30]", "Verifying that x1 attribute has not changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2))<Double.parseDouble(vb2y2), "Checkpoint[19/30]", "Verifying that x2 attribute has not changed");

			


			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[23/30]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[24/30]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[25/30]", "Verifying the y1 coordinate is not same");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[26/30]", "Verifying the y2 coordinate is not same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[29/30]", "Verifying the x1 coordinate is same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[30/30]", "Verifying the x2 coordinate is same");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1))<Double.parseDouble(vb3y1), "Checkpoint[27/30]", "Verifying that x1 attribute has not changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2))<Double.parseDouble(vb3y2), "Checkpoint[28/30]", "Verifying that x2 attribute has not changed");


			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}



	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Negative","BVT"})
	public void test07_US1255_TC7186_VerifyLocalizerLineAbsenceInDiffOrientation() throws InterruptedException, SQLException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that images are spatially related AND of different orientations");

		//Loading the patient on viewer
		db = new DatabaseMethods(driver);
		db.updateFrameRefIDOfPatient(aidocMachine, "");

		
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aidocMachine,1);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the localizer is not displayed for data which has different planes but not same frame id");

		for(int i=1; i<=viewerPage.getNumberOfCanvasForLayout();i++) {

			viewerPage.mouseHover(viewerPage.getViewPort(i));	
			for(int j=1; j<=viewerPage.getNumberOfCanvasForLayout();j++) {

				if(i==j)
					continue;
				viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(j), "Checkpoint[1/2]", "Localizer line should not be present.");
			}

		}


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the localizer is not displayed on layout change though data has different planes but not same frame id");
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerPage.click(viewerPage.getViewPort(4));
		viewerPage.click(viewerPage.getViewPort(1));

		for(int i=1; i<=viewerPage.getNumberOfCanvasForLayout();i++) {

			viewerPage.mouseHover(viewerPage.getViewPort(i));	
			for(int j=1; j<=viewerPage.getNumberOfCanvasForLayout();j++) {

				if(i==j)
					continue;
				viewerPage.assertFalse(viewerPage.verifyLocalizerLinePresence(j), "Checkpoint[2/2]", "Localizer line should not be present.");
			}

		}

	}	

	@Test(groups ={"Chrome","IE11","Edge","US1255","Negative"})
	public void test08_US1255_TC7189_VerifyLocalizerLineAbsenceInSamePhase() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that localizer line is visible in the inactive view boxes of different planes compared to that series's plane which is loaded from content selector.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(mrLSPPatientName,1);

		cs = new ContentSelector(driver);
		
		String fourthSeries= cs.getSeriesDescriptionOverlayText(4);
		cs.selectSeriesFromSeriesTab(4, cs.getSeriesDescriptionOverlayText(1));

		for(int i=1; i<=cs.getNumberOfCanvasForLayout();i++) {

			cs.mouseHover(cs.getViewPort(i));	
			for(int j=1; j<=cs.getNumberOfCanvasForLayout();j++) {

				if(i==j)
					continue;
				cs.assertFalse(cs.verifyLocalizerLinePresence(j), "Checkpoint[1/9]", "Localizer line should not be present.");
			}

		}

		cs.selectSeriesFromSeriesTab(1, fourthSeries);
		cs.selectSeriesFromSeriesTab(4, fourthSeries);
				
		
		cs.mouseHover(cs.getViewPort(1));	
		
		cs.assertFalse(cs.verifyLocalizerLinePresence(1), "Checkpoint[2/9]", "Localizer line should not be present.");
		cs.assertFalse(cs.verifyLocalizerLinePresence(4), "Checkpoint[3/9]", "Localizer line should not be present.");
		verifyLocalizerLine(2,4,9);
		verifyLocalizerLine(3,5,9);
		
		cs.mouseHover(cs.getViewPort(2));	
		
		cs.assertFalse(cs.verifyLocalizerLinePresence(2), "Checkpoint[6/9]", "Localizer line should not be present.");
		cs.assertFalse(cs.verifyLocalizerLinePresence(3), "Checkpoint[7/9]", "Localizer line should not be present.");
		verifyLocalizerLine(1,8,9);
		verifyLocalizerLine(4,9,9);
			

	}	

	@Test(groups ={"Chrome","IE11","Edge","DE1789","Negative"})
	public void test09_DE1789_TC7210_verifyLocalizerLineIsNotStuckOnScroll() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify that localizer line not getting freezed on inactive view box on performing scroll fast.");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(cardiacMRPatientName,1);
		ViewerLayout layout = new ViewerLayout(driver);
		ViewerSliderAndFindingMenu slider = new ViewerSliderAndFindingMenu(driver);
		
		layout.selectLayout(layout.threeByTwoLayoutIcon);
	
		
		for(int i =0;i<10;i++) {
			slider.scrollTheSlicesUsingSliderQuickVersion(1, 0, 0, 0, -100);
			slider.scrollTheSlicesUsingSliderQuickVersion(1, 0, 0, 0, 150);
			
			if(viewerPage.verifyLocalizerLinePresence(2))
				verifyLocalizerLine(2, 1, 5);
			if(viewerPage.verifyLocalizerLinePresence(3))
				verifyLocalizerLine(3, 2, 5);
			if(viewerPage.verifyLocalizerLinePresence(4))
				verifyLocalizerLine(4, 3, 5);
			if(viewerPage.verifyLocalizerLinePresence(5))
				verifyLocalizerLine(5, 4, 5);
			if(viewerPage.verifyLocalizerLinePresence(6))
				verifyLocalizerLine(6, 5, 5);
			
			
		}
		
			

	}	
	
	private void verifyLocalizerLine(int whichViewebox , int checkpoint, int finalCheckpoint) {
		
		viewerPage = new ViewerPage(driver);		
		viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(whichViewebox), "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Localizer line should not be present.");
		viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(whichViewebox),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying the line is of blue color");
		viewerPage.assertTrue(viewerPage.getLocalizerLineText(whichViewebox).isEmpty(),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying no slice number is displayed");
		viewerPage.assertEquals(viewerPage.getLocalizerLines(whichViewebox).size(),1, "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Only one localizer line should be displayed");
		
		
	}
	
	private void verifyLocalizerHorizontalLine(int whichViewebox , int checkpoint, int finalCheckpoint) {
		
		viewerPage = new ViewerPage(driver);		
		viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(whichViewebox), "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Localizer line should not be present.");
		viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(whichViewebox),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying the line is of blue color");
		viewerPage.assertTrue(viewerPage.getLocalizerLineText(whichViewebox).isEmpty(),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying no slice number is displayed");
		viewerPage.assertTrue(viewerPage.verifyLocalizerLineIsHorizontal(whichViewebox), "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Verifying the localizer line is vertical");
	
		
	}
	
	private void verifyLocalizerVerticalLine(int whichViewebox , int checkpoint, int finalCheckpoint) {
		
		viewerPage = new ViewerPage(driver);		
		viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(whichViewebox), "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Localizer line should not be present.");
		viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(whichViewebox),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying the line is of blue color");
		viewerPage.assertTrue(viewerPage.getLocalizerLineText(whichViewebox).isEmpty(),"Checkpoint["+checkpoint+"/"+finalCheckpoint+"]","Verifying no slice number is displayed");
		viewerPage.assertTrue(viewerPage.verifyLocalizerLineIsVertical(whichViewebox), "Checkpoint["+checkpoint+"/"+finalCheckpoint+"]", "Verifying the localizer line is vertical");
	
		
	}

	@AfterMethod
	public void cleanFrameRefID() throws SQLException {

		DatabaseMethods db = new DatabaseMethods(driver);
		db.updateFrameRefIDOfPatient(aidocMachine, "");

	}

}
