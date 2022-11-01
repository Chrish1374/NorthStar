package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ImageQualityTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel preset ;
	
	// Get Patient Name
	String filePath=Configurations.TEST_PROPERTIES.get("Axial_GrayScale_filepath");
	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() 
	{

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();
	}

	@Test(groups = {"firefox", "Chrome", "IE11", "Edge","US681","US1044"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/data.xls","sheetName=test01_ImageQuality"})
	public void test01_US681_TC2371_US1044_TC6151_verifyImageQuality(String patientFilepath) throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify picture quality using phantom data");

		String filePath=Configurations.TEST_PROPERTIES.get(patientFilepath);
		String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);

		patientPage = new PatientListPage(driver);

		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();
		
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();		

		// verifying the image on load
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"checkpoint[1]" , "Verifying the image quality on load");
		viewerPage.compareElementImage(protocolName,viewerPage.mainViewer,"Verifying the image quality when viewer is loaded","TC01_checkpoint1_"+patientName);
		int[] imageNumber = new int[] {3,10,15,23,27,30,47,59,64};

		//scrolling and checking the image quality
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"checkpoint[2]" , "Verifying the image quality on scroll");
		for (int i=0;i<imageNumber.length;i++){

			viewerPage.scrollToImage(1, imageNumber[i]);
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"checkpoint[3]" , "Verifying the image quality on slice="+imageNumber[i]);
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify " + imageNumber[i] + "th image of viewbox2","TC01_Checkpoint2_"+imageNumber[i]+"_"+patientName);			
		}

		//zooming and checking the image quality
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"checkpoint[4]" , "Verifying the image quality on zoom");
		preset =new ViewBoxToolPanel(driver);
		preset.changeZoomNumber(1,250);
		
		for (int i=0;i<imageNumber.length;i++){

			viewerPage.scrollToImage(1, imageNumber[i]);			
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"checkpoint[5]" , "Verifying the image quality on slice="+imageNumber[i]+" with zoom");
			viewerPage.compareElementImage(protocolName, viewerPage.getViewPort(1),"Verify " + imageNumber[i] + "th image of viewbox2","TC01_Checkpoint3_"+imageNumber[i]+"_"+patientName);			
		}

	}


}





