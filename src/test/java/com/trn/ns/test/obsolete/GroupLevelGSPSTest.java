//package com.trn.ns.test.obsolete;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.PolyLineAnnotation;
//
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class GroupLevelGSPSTest extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//
//	private EllipseAnnotation ellipse ;
//	private TextAnnotation textAn;	
//	private PolyLineAnnotation polyLineAnn;
//
//
//	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);
//
//	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
//	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
//	public void test10_US1006_TC4561_verifyForwardNavigationBetweenGroupElementUsingPageDown()  throws InterruptedException    
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Navigation between group elements- Page Down");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientUsingID(LIDCPatientID);
//
//	    
//	    studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		
//		//Load patient on viewer
//		viewerPage.waitForViewerpageToLoad();
//			
//
//		textAn=new TextAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		polyLineAnn=new PolyLineAnnotation(driver);		
//		
//		viewerPage.openFindingTableForGroupElement(1);
//		//verify Groups
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/21]", "Verify count of group visible on A/R finding toolbar");
//		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
//		
//		viewerPage.selectFindingFromTable(1);
//		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[2/21]", "Verified that text annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[3/21]", "Verify finding is highlighted in finding menu");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[5/21]", "Verify first finding from first group is highlighted");
//	
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(polyLineAnn.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[6/21]","Verified that polyline annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,3), "Checkpoint[7/21]", "Verify third finding from first group is highlighted");
//	
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[8/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,5), "Checkpoint[9/21]", "Verify fifth finding from first group is highlighted");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Checkpoint[10/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[11/21]", "Verify first finding from second group is highlighted");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(polyLineAnn.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[12/21]", "Verified that polyline annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,3), "Checkpoint[13/21]", "Verify third finding from second group is highlighted");
//	
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[14/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,5), "Checkpoint[15/21]", "Verify fifth finding from second group is highlighted");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[16/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,5), "Checkpoint[17/21]", "Verify fifth finding from fourth group is highlighted");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[18/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[19/21]", "Verify fifth finding from fifth group is highlighted");
//		
//		viewerPage.scrollDownGSPSUsingKeyboard();
//		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[20/21]", "Verified that text annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[21/21]", "Verify finding is highlighted in finding menu");
//		
//	}
//	
//	@Test(groups ={"IE11","Chrome","Edge","US1006","positive"})
//	public void test11_US1006_TC4561_verifyBackwardNavigationBetweenGroupElementUsingPageUp()  throws InterruptedException    
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Navigation between group elements- Page Up");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientUsingID(LIDCPatientID);
//
//	    
//	    studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		
//		//Load patient on viewer
//		viewerPage.waitForViewerpageToLoad();
//			
//		textAn=new TextAnnotation(driver);
//		ellipse=new EllipseAnnotation(driver);
//		polyLineAnn=new PolyLineAnnotation(driver);
//		
//		
//		
//		viewerPage.openFindingTableForGroupElement(1);
//		//verify Groups
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/21]", "Verify count of group visible on A/R finding toolbar");
//		viewerPage.assertEquals(viewerPage.groupInfo.size(), 5, "Verify" +" "+ viewerPage.groupInfo.size()+" "+"groups seen in finding toolbar", "Verified");
//		
//		viewerPage.inputImageNumber(32, 1);
//		ellipse.selectEllipse(1, 1);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[2/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[3/21]", "Verify fifth finding from fifth group is highlighted");
//
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[4/21]","Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(4,5), "Checkpoint[5/21]","Verify fifth finding from fourth group is highlighted");
//	
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 3), "Checkpoint[6/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,5), "Checkpoint[7/21]", "Verify fifth finding from second group is highlighted");
//		
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(polyLineAnn.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 2), "Checkpoint[8/21]", "Verified that polyline annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,3), "Checkpoint[9/21]", "Verify third finding from second group is highlighted");
//		
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1,2), "Checkpoint[10/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(2,1), "Checkpoint[11/21]", "Verify first finding from second group is highlighted");
//		
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[12/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,5), "Checkpoint[13/21]", "Verify fifth finding from first group is highlighted");
//
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(polyLineAnn.verifyPolyLineAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[14/21]", "Verified that polyline annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1,3), "Checkpoint[15/21]", "Verify third finding from first group is highlighted");
//
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[16/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[17/21]", "Verify first finding from first group is highlighted");
//		
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[18/21]", "Verified that text annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlighted(1), "Checkpoint[19/21]", "Verify finding is highlighted in finding menu");
//	
//		viewerPage.scrollUpGSPSUsingKeyboard();
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActivePendingGSPS(1, 1), "Checkpoint[20/21]", "Verified that ellipse annotation is current active pending GSPS");
//		viewerPage.assertTrue(viewerPage.verifyFindingIsHighlightedWithinGroup(5,5), "Checkpoint[21/21]", "Verify fifth finding from fifth group is highlighted");
//	
//	
//	}
//	}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
