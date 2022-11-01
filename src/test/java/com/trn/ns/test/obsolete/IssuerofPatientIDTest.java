//package com.trn.ns.test.obsolete;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//
//public class IssuerofPatientIDTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//
//	private ExtentTest extentTest;
//
//	String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_US675_filepath");
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"),Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//	//@Test(groups ={"Chrome","US675"})
//	public void test01_US675_TC2418_verifyAssigningAuthorityIsAddedToPatientData() throws InterruptedException  
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Add Assigning Authority field to Patient data- Data with IssuerofPatientID");
//
//		String AH4_US675 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
//		String AH4_frhprod = DataReader.getPatientDetails(PatientXMLConstants.ISSUEROFPATIENTID1, filePath1);
//		String AH4_patientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);
//		String AH4_sqatest = DataReader.getPatientDetails(PatientXMLConstants.ISSUEROFPATIENTID2, filePath1);
//
//		patientPage = new PatientListPage(driver);		
//		patientPage.searchPatient(AH4_US675, "", "","");		
//		
//		for(int i=0;i<patientPage.patientNamesList.size();i++){
//			
//			patientPage.clickOnPatientRow(patientPage.getText(patientPage.patientNamesList.get(i)));
//			
//			if(patientPage.getCurrentPageURL().contains(AH4_sqatest)){
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify IssuerofPatientID and patient ID is present in single patient study list page URL");
//				patientPage.assertTrue(patientPage.getCurrentPageURL().contains(AH4_sqatest), "Verifing IssuerofPatientID from single patient study list page URL", "IssuerofPatientID -" + AH4_sqatest + "is present in patient study page URL -" + patientPage.getCurrentPageURL());
//				patientPage.assertTrue(patientPage.getCurrentPageURL().contains(AH4_patientID), "Verifing PatientID from single patient study list page URL", "PatientID -" + AH4_patientID + "is present in patient study page URL -" + patientPage.getCurrentPageURL());
//
//				patientPage.clickOntheFirstStudy();
//				viewerpage = new ViewerPage(driver);
//				viewerpage.waitForViewerpageToLoad();
//
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify image is getting rendered on viewer for " + AH4_patientID + " patient");
////				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying image is getting rendered on viewer for " + AH4_patientID + "patient","US675_TC2418_Checkpoint_2");
//
//				viewerpage.browserBackWebPage();
//				patientPage.waitForPatientPageToLoad();
//			}
//
//			if(patientPage.getCurrentPageURL().contains(AH4_frhprod)){
//				
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify IssuerofPatientID and patient ID is present in single patient study list page URL");
//				patientPage.assertTrue(patientPage.getCurrentPageURL().contains(AH4_frhprod), "Verifing IssuerofPatientID from single patient study list page URL", "IssuerofPatientID -" + AH4_frhprod + " is present in patient study page URL -" + patientPage.getCurrentPageURL());
//				patientPage.assertTrue(patientPage.getCurrentPageURL().contains(AH4_patientID), "Verifing PatientID from single patient study list page URL", "PatientID -" + AH4_patientID + " is present in patient study page URL -" + patientPage.getCurrentPageURL());
//
//				patientPage.clickOntheFirstStudy();
//
//				viewerpage = new ViewerPage(driver);
//				viewerpage.waitForViewerpageToLoad();
//				
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify image is getting rendered on viewer when first study of patient " + AH4_patientID + "is selected");
////				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying image is getting rendered on viewer when first study of patient " + AH4_patientID + " is selected","US675_TC2418_Checkpoint_4");
//
//				viewerpage.browserBackWebPage();
//				patientPage.waitForPatientPageToLoad();
//				patientPage.clickOnStudy(2);
//				viewerpage.waitForViewerpageToLoad();
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify image is getting rendered on viewer when second study of patient " + AH4_patientID + "is selected");
////				viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying image is getting rendered on viewer when second study of patient " + AH4_patientID + " is selected","US675_TC2418_Checkpoint_5");
//				viewerpage.browserBackWebPage();
//				patientPage.waitForPatientPageToLoad();
//			}
//		}
//	}
//
//
//}