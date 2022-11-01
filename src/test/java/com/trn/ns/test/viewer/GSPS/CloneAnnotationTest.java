package com.trn.ns.test.viewer.GSPS;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CloneAnnotationTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private ExtentTest extentTest;
	
	private RegisterUserPage register;
	private ContentSelector cs;

	String liver9FilePath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liverPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);

	String multiSeriesPointFilePath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String multiSeriesPointPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, multiSeriesPointFilePath);

	String mrLSPFilePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String mrLSpPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, mrLSPFilePath);

	String SQATestingPatientPath =Configurations.TEST_PROPERTIES.get("SQA_Testing");
	String SQATestingPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, SQATestingPatientPath);

	String username_1 = "user_1";
	String username_2 = "user_2";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	final String  csResultPrefix =ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_";
	String csResultPrefixForAnotherUser =ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX;
	private MeasurementWithUnit lineWithUnit;
	private TextAnnotation textAn;
	private PointAnnotation point;
	private EllipseAnnotation ellipse;
	private CircleAnnotation circle;
	private SimpleLine line;
    private PolyLineAnnotation poly;
	private HelperClass helper;
	private ViewerLayout layout;
    
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientPage = new PatientListPage(driver);

	}

	@Test(groups ={"Chrome","US770"})
	public void test01_US770_TC2910_TC2911_TC3034_verifyResultEntryinCSForLineMeasurement() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user edits Line measurement annotation "
				+ "<br> Verify cloned entry is generated when user deletes Line measurement annotation"
				+ "<br> Verify cloned entry is generated when user draws new annotation on second viewbox after editing annotation from first viewbox in same session");

		//Loading the patient on viewer
		helper = new HelperClass(driver);		
		viewerPage =helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		
		lineWithUnit = new MeasurementWithUnit(driver);
		cs = new ContentSelector(driver);
		layout = new ViewerLayout(driver);
		
		//Draw measurement on viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 0, 150, 0);

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/14]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/14]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		//resize the line in viewbox 1
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50, -100);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[3/14]","Validating that no new entry is created on annotation resize");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/14]","No New entry should be displayed into content selector like <GSPS(username)>");
		}
		//Draw measurement on viewbox 2
		lineWithUnit.closingConflictMsg();
		lineWithUnit.drawLine(2, 50, 0, 150, 0);		
		lineWithUnit.closingConflictMsg();

		//Verify cloned entry is generated when user draws new annotation on second viewbox after editing annotation from first viewbox in same session
		latestResults = cs.getAllResults();
		
		lineWithUnit.closingConflictMsg();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/14]","Validating new entry is generated for new annotation created on another viewbox");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/14]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		//resize the line in viewbox 2
		lineWithUnit.selectLinearMeasurement(2, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(2, 1, -50, -100);

		lineWithUnit.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[7/14]","Validating no new entry is generated on resizing the annotation in another viewbox");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[8/14]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		//go back to study page and reload view and confirm the content selector
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[9/14]","Validating No new entry is generated upon reloading the viewer");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[10/14]","No New entry should be displayed into content selector like <GSPS(username)>");
		}
		//		Series with annotation drawn in step-2 should be rendered
		cs.selectResultFromSeriesTab(3, csResultPrefix+1);
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(3).size(),1,"Checkpoint[11/14]","A Single instance of Linear Measurement is present upon selecting the first result using content selector");
		lineWithUnit.closingConflictMsg();
		//		Series with annotation drawn in step-6 should be rendered
		cs.selectResultFromSeriesTab(4, csResultPrefix+2);
		cs.click(cs.getViewPort(4));
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),1,"Checkpoint[12/14]","A Single instance of Linear Measurement is present upon selecting the second result using content selector");
		cs.openAndCloseSeriesTab(false);
		
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		//deleting the annotation
		lineWithUnit.click(lineWithUnit.getViewPort(2));
		lineWithUnit.selectLinearMeasurement(2, 1);
		lineWithUnit.deleteAllAnnotation(2);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[13/14]","Validating existing entry is deleted on removal of respective annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[14/14]","Entry should be deleted");
		}
	} 

	@Test(groups ={"Chrome","US770"})
	public void test02_US770_TC2912_TC2913_TC3034_verifyAnnotationEntryinCSForTextAnnot() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user edits Text annotation "
				+ "<br> Verify cloned entry is generated when user deletes Text annotation"
				+ "<br> Verify cloned entry is generated when user draws new annotation on second viewbox after editing annotation from first viewbox in same session");

		String textAnnot1="ABC1",textAnnot2="ABC2";

		//Loading the patient on viewer
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liverPatientName,  1);
		
		textAn = new TextAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw text annotation on viewbox1
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,50,-50,textAnnot1);
		textAn.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/16]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/16]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		//resize the line in viewbox 1
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),50, -50, 10, 10);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[3/16]","Validating no new entry is generated for annotation resizing");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/16]","No New entry should be displayed into content selector like <GSPS(username)>");
		}
		
		textAn.closingConflictMsg();
		//Draw Text on viewbox 2
		textAn.drawText(2,50,-50,textAnnot2);

		textAn.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/16]","Validating new entry is generated for new annotation created in another viewbox");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/16]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		//resize the line in viewbox 2
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),50, -50, 10, 10);
		textAn.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[7/16]","Validating no new entry is generated for new annotation resizing");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[8/16]","No new entry should be displayed into content selector like <GSPS(username)>");
		}

		//deleting the annotation
		textAn.getAnchorLinesOfTextAnnot(2, 1);
		textAn.deleteAllAnnotation(2);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[9/16]","Validating entries are same upon reloading the viewer");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[10/16]","No new entry should be displayed into content selector like <GSPS(username)>");
		}


		//go back to study page and reload view and confirm the content selector
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[15/16]","Corresponding entry should be deleted upon deleting the respective annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[16/16]","Content selector entry should be deleted");
		}

	} 

	@Test(groups ={"Chrome","US770"})
	public void test03_US770_TC2914_TC2916_TC3034_verifyAnnotationEntryinCSForPointAnnot() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user edits any annotation(Simple line, circle, ellipse, point) "
				+ "<br> Verify cloned entry is generated when user rejects any annotation"
				+ "<br> Verify cloned entry is generated when user draws new annotation on second viewbox after editing annotation from first viewbox in same session");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.closingConflictMsg();

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/17]","Validating the Content selector creation of new annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/17]","Entry should be created named as GSPS_<Username>_number");
		}
		//move point in viewbox 1
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),50, 50, 10, 10);
		point.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[3/17]","No new entry should be created on resizing the annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/17]","Content selector entry should same");
		}

		//Draw point on viewbox 2
		point.drawPointAnnotationMarkerOnViewbox(2,50,50);
		point.closingConflictMsg();
		//Verify cloned entry is generated when user draws new annotation on second viewbox after editing annotation from first viewbox in same session
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/17]","New entry should be created in content selector on creation of annotation in another viewbox");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/17]","Entry should be created named as GSPS_<Username>_number");
		}
		//resize the line in viewbox 2
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(2),50, 50, 10, 10);
		point.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[7/17]","No new entry should be created in content selector upon resizing");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[8/17]","Content selector entry should still be same");
		}

		//go back to study page and reload view and confirm the content selector
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[9/17]","No new entry should be created upon reloading viewer");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[10/17]","Content selector entry should remain same");
		}

		//Select entry created  in step-3 from  content selector -> Result tab

		cs.selectResultFromSeriesTab(3, csResultPrefix+1);
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(point.getLinesOfPoint(3,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[11/17]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(3,1),"Checkpoint[12/17] : verifying the point#1", "point is present");

		cs.selectResultFromSeriesTab(4, csResultPrefix+2);
		cs.click(cs.getViewPort(4));
		viewerPage.assertEquals(point.getLinesOfPoint(4,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[13/17]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(4,1),"Checkpoint[14/17]: verifying the point#1", "point is present");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		viewerPage.assertEquals(point.getLinesOfPoint(2,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[17/17] : Verifying point is not present", "point is present");

		//deleting the annotation
		point.deleteAllAnnotation(2);
		point.closingConflictMsg();

		// only one entry should be present GSPS_Scan_1 however becoz of de906 and DE900 it is not getting deleted
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[15/17]","Corresponding entry should be deleted upon deleting the respective annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[16/17]","Content selector entry should be deleted");
		}

		viewerPage.assertFalse(point.isPointPresent(2,1),"Checkpoint[17/17] : Verifying point is not present", "point is present");


	} 

	@Test(groups ={"Chrome","US770","Sanity","BVT"})
	public void test04_US770_TC2916_verifyClonedEntryOnFindingRejected() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user rejects any annotation");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.closingConflictMsg();
		
		// Have to reload the viewer then have to try

		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/18]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int i=0,j=1;i<allResults.size();i++,j++) {
			viewerPage.assertEquals(allResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/18]","New entry should be displayed into content selector like <GSPS(username)>");
		}

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();

		point.closingConflictMsg();
		// It should show two entries
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[3/18]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[4."+i+"/18]","New entry should be displayed into content selector like <GSPS(username)>_1");


		//Draw point on viewbox 2
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,50,50);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[5/18]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[6."+i+"/18]","New entry should be displayed into content selector like <GSPS(username)>_1");


		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[7/18]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[8."+i+"/18]","New entry should be displayed into content selector like <GSPS(username)>_1");


		//go back to study page and reload view and confirm the content selector
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[9/18]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[10."+i+"18]","No New entry should be displayed into content selector like <GSPS(username)>_1");


		layout.selectLayout(layout.threeByTwoLayoutIcon);
		point.closingConflictMsg();
		//Select entry created  in step-3 from  content selector -> Result tab
		cs.selectResultFromSeriesTab(3, csResultPrefix+1);
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(point.getLinesOfPoint(3,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[11.0/18]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(3,1),"Checkpoint[11.1/18]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(3,1),"Verifying that point is currently accepted and focused", "verified");
		point.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(4, csResultPrefix+2);
		cs.click(cs.getViewPort(4));
		viewerPage.assertEquals(point.getLinesOfPoint(4,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[12.0/18]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(4,1),"Checkpoint[12.1/18]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(4,1),"Verifying that point is currently rejected and focused", "verified");
		point.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(5, csResultPrefix+3);
		cs.click(cs.getViewPort(5));
		viewerPage.assertEquals(point.getLinesOfPoint(5,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[13/18]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(5,1),"Checkpoint[14/18]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(5,1),"Verifying that point is currently accepted and focused", "verified");
		point.closingConflictMsg();
		
		cs.selectResultFromSeriesTab(6, csResultPrefix+4);
		cs.click(cs.getViewPort(6));
		viewerPage.assertEquals(point.getLinesOfPoint(6,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[15/18]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(6,1),"Checkpoint[16/18]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(6,1),"Verifying that point is currently rejected and focused", "verified");
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[17/18]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[18."+i+"/18]","No New entry should be displayed into content selector like <GSPS(username)>_1");

	} 

	@Test(groups ={"Chrome","US770","Sanity","BVT"})
	public void test05_US770_TC2917_verifyClonedEntryOnFindingAccepted() throws InterruptedException, AWTException{


		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user accepts any annotation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(liverPatientName, 1);
		layout = new ViewerLayout(driver);
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		// Have to reload the viewer then have to try
		point.closingConflictMsg();
		
		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/19]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[2/19]","New entry should be displayed into content selector like <GSPS(username)>");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		// It should show three entries
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[3/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[4."+i+"/19]","New entry should be displayed into content selector like <GSPS(username)>_1");

		//Draw point on viewbox 2
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,100,100);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[5/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[6."+i+"/19]","New entry should be displayed into content selector like <GSPS(username)>_1");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),6,"Checkpoint[7/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[8."+i+"/19]","New entry should be displayed into content selector like <GSPS(username)>_1");


		//go back to study page and reload view and confirm the content selector
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),6,"Checkpoint[9/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[9."+i+"/19]","New entry should be displayed into content selector like <GSPS(username)>_1");


		layout.selectLayout(layout.threeByTwoLayoutIcon);
		point.closingConflictMsg();
		//Select entry created  in step-3 from  content selector -> Result tab
		int a=1;
		cs.selectResultFromSeriesTab(3, csResultPrefix+a);
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(point.getLinesOfPoint(3,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[10/19]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(3,1),"Checkpoint[11/19]", "point is present");
		point.closingConflictMsg();
		
		a++;
		cs.selectResultFromSeriesTab(4, csResultPrefix+a);
		cs.click(cs.getViewPort(4));
		viewerPage.assertEquals(point.getLinesOfPoint(4,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[12/19]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(4,1),"Checkpoint[13/19]", "point is present");
		point.closingConflictMsg();
		a++;
		cs.selectResultFromSeriesTab(5, csResultPrefix+a);
		cs.click(cs.getViewPort(5));
		viewerPage.assertEquals(point.getLinesOfPoint(5,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[14/19]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(5,1),"Checkpoint[15/19]", "point is present");
		point.closingConflictMsg();
		a++;
		cs.selectResultFromSeriesTab(6, csResultPrefix+a);
		cs.click(cs.getViewPort(6));
		viewerPage.assertEquals(point.getLinesOfPoint(6,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[16/19]", "point is present");
		viewerPage.assertTrue(point.isPointPresent(6,1),"Checkpoint[17/19]", "point is present");
		point.closingConflictMsg();

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),6,"Checkpoint[18/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefix+i,"Checkpoint[19."+i+"/19]","New entry should be displayed into content selector like <GSPS(username)>_1");


	} 

	@Test(groups ={"Chrome","US770"})
	public void test06_US770_TC2918_verifyEntiresWhenMultipleUsersEditAnnotation() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when multiple users edit any annotation");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		cs = new ContentSelector(driver);
		//Draw measurement on viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 150, 150);
		lineWithUnit.closingConflictMsg();
		
		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/13]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[2/13]","New entry should be displayed into content selector like <GSPS(username)>");

		//resize the line in viewbox 1
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50, -100);
		lineWithUnit.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[3/13]","New entry should NOT be displayed into content selector like <GSPS(username)>	");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[4/13]","New entry should NOT be displayed into content selector like <GSPS(username)>	");

		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);		
		register.createNewUser("User", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		Header header = new Header(driver);		
		header.logout();

		loginPage.login(username_1, username_1);
		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[5/13]","New entry should NOT be displayed into content selector like <GSPS(username)>	");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[6/13]","New entry should NOT be displayed into content selector like <GSPS(username)>	");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Checkpoint[7/13]","A Single instance of Linear Measurement is present on Viewbox1");

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, 50, 50, 150, 150);

		lineWithUnit.closingConflictMsg();
		lineWithUnit.selectLinearMeasurement(2, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(2, 1, -50, -100);
		lineWithUnit.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[8/13]","No New entry should be created into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[9/13]","No New entry should be created into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[10/13]","No New entry should be created into content selector like <GSPS(username)>");

		header.logout();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));

		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[11/13]","Entries should be same into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[12/13]","Entries should be same into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[13/13]","Entries should be same into content selector like <GSPS(username)>");


	} 

	@Test(groups ={"Chrome","US770"})
	public void test07_US770_TC2920_verifyEntriesWhenMultipleUserAcceptsAnnotation() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when multiple users accepts any annotation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);
		register.createNewUser("automation", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);		
		register.createNewUser("auto", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);		
		Header header = new Header(driver);		
		header.logout();

		loginPage.login(username_1, username_1);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		point = new PointAnnotation(driver);
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.closingConflictMsg();
		
		// Have to reload the viewer then have to try
		cs = new ContentSelector(driver);
		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/17]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[2/17]","New entry should be displayed into content selector like <GSPS(username)>");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);

		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		// It should show three entries
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[3/17]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[4."+j+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		header = new Header(driver);		
		header.logout();

		loginPage.login(username_2, username_2);
		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);

		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint[5/17]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Verifying that point is currently accepted and focused", "verified");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[6/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[7."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");

		//Draw point on viewbox 2
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,-100,-100);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[8/19]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<3;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[9."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");
		viewerPage.assertEquals(allResults.get(allResults.size()-1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[10.1/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectAcceptfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),6,"Checkpoint[11/17]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<3;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[12."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		for(int j=3,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_2+"_"+i,"Checkpoint[13."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		header.logout();

		loginPage.login(username_1, username_1);
		 helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint[14/17]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1),"Verifying that point is currently accepted and focused", "verified");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),6,"Checkpoint[15/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<3;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[16."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");

		for(int j=3,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_2+"_"+i,"Checkpoint[17."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");


	} 

	@AfterMethod(alwaysRun=true)
	public void deleteData() throws SQLException {

		db = new DatabaseMethods(driver);
		db.deleteDrawnAnnotation(username_1);
		db.deleteDrawnAnnotation(username_2);

	}

	@Test(groups ={"Chrome","US770"})
	public void test08_US770_TC2921_verifyEntriesWhenMultipleUserRejectsAnnotation() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify active GSPS");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);
		register.createNewUser("automation", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("auto", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);		
		Header header = new Header(driver);		
		header.logout();

		loginPage.login(username_1, username_1);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.closingConflictMsg();
		
		// Have to reload the viewer then have to try

		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/17]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[2/17]","New entry should be displayed into content selector like <GSPS(username)>");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);

		point.openGSPSRadialMenu(point.getHandlesOfPoint(1, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.closingConflictMsg();
		
		// It should show three entries
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[3/17]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[4."+j+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		header = new Header(driver);		
		header.logout();

		loginPage.login(username_2, username_2);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);

		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint[5/17]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(1,1),"Verifying that point is currently accepted and focused", "verified");

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[6/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[7."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		//Draw point on viewbox 2
		point.selectPointFromQuickToolbar(2);
		point.drawPointAnnotationMarkerOnViewbox(2,-50,-50);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[8/17]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<2;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[9."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");
		viewerPage.assertEquals(allResults.get(allResults.size()-1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[10.1/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();

		point.openGSPSRadialMenu(point.getHandlesOfPoint(2, 1).get(0));
		point.selectRejectfromGSPSRadialMenu();
		point.closingConflictMsg();

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[11/17]","New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<2;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[12."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		for(int j=2,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_2+"_"+i,"Checkpoint[13."+i+"/17]","New entry should be displayed into content selector like <GSPS(username)>_1");

		header.logout();

		loginPage.login(username_1, username_1);
		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		viewerPage.assertTrue(point.isPointPresent(2,1),"Checkpoint[14/17]", "point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsRejectedInActiveGSPS(1,1),"Verifying that point is currently accepted and focused", "verified");

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),4,"Checkpoint[15/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		for(int j=0,i =1;j<2;i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_1+"_"+i,"Checkpoint[16."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");

		for(int j=2,i =1;j<allResults.size();i++,j++)
			viewerPage.assertEquals(allResults.get(j),csResultPrefixForAnotherUser+username_2+"_"+i,"Checkpoint[17."+i+"/17]","No New entry should be displayed into content selector like <GSPS(username)>_1");



	} 

	@Test(groups ={"Chrome","US770","Sanity"})
	public void test09_US770_TC2963_verifyEntriesForGSPSData() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when new annotation is drawn on series having machine drawn annotation");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		List<String> allResults = cs.getAllResults();

		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.waitForTimePeriod(1000);
		// Have to reload the viewer then have to try

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),allResults.size()+2,"Checkpoint[1/2]","New entry should be displayed into content selector like _username_number");

		for(int i=allResults.size(),j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/2]","New entry should be displayed into content selector like <GSPS(username)>");
		}

	} 

	@Test(groups ={"Chrome","US770","BVT"})
	public void test10_US770_TC2964_verifyResultsWhenSameResultSelectedUsingCS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry when new annotation is drawn/edited into series containing no GSPS and same series is selected through content selector");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		String secondSource = viewerPage.getSeriesDescriptionOverlayText(2);
		cs = new ContentSelector(driver);
		//Draw measurement on viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 150, 150);
		lineWithUnit.closingConflictMsg();
		
		// Series with annotation drawn in step-2 should be rendered
		cs.selectResultFromSeriesTab(3, csResultPrefix+1);
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(3).size(),1,"Checkpoint[1/10]","Validating the linear measurement presence on viewbox 3");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Checkpoint[2/10]","Validating the linear measurement presence on viewbox 1");
				
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[3/10]","Validating there is no more cloned result generated when selected through content selector");
		for(int i=latestResults.size(),j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/10]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		//Series with annotation drawn in step-6 should be rendere4
		cs.selectSeriesFromSeriesTab(4,secondSource);
		viewerPage.closingConflictMsg();
		cs.click(cs.getViewPort(4));
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),0,"Checkpoint[5/10]","Selecting the second source in fourth viewbox and validating no new annotation present");
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[6/10]","Validating there is no more cloned result generated when selected through content selector");
		//lineWithUnit.closingBannerAndWaterMark();
		
		lineWithUnit.drawLine(2, 50, 50, 150, 150);
		lineWithUnit.closingConflictMsg();
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Checkpoint[7/10]","A Single instance of Linear Measurement is present on Viewbox2");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(4).size(),1,"Checkpoint[8/10]","A Single instance of Linear Measurement is present on Viewbox4");
				
		latestResults = cs.getAllResults();
		lineWithUnit.closingConflictMsg();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[9/10]","New entry should be displayed into content selector like GSPS_username_number");
		for(int i=latestResults.size(),j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[10/10]","New entry should be displayed into content selector like <GSPS(username)>");
		}
	} 

	@Test(groups ={"Chrome","US770"})
	public void test11_US770_TC2965_verifyActiveGSPS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when multiple users rejects any annotation");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);

		patientPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);
		register.createNewUser("automation", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		register.createNewUser("auto", "test", LoginPageConstants.SUPPORT_EMAIL,username_2, username_2, username_2);		
	
		Header header = new Header(driver);		
		header.logout();

		loginPage.login(username_1, username_1);
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(mrLSpPatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw point annotation on viewbox4
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(4,50,50);
		point.closingConflictMsg();
		
		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/23]","New entry should be displayed - validating the size of cloned results");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[2/23]","New entry should be displayed into content selector like <GSPS(username)>");

		header = new Header(driver);		
		header.logout();

		loginPage.login(username_2, username_2);
		helper.loadViewerPageUsingSearch(mrLSpPatientName,  1, 1);

		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint[3/23]", "Verifying point created by user1 is present and sisplayed");
		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint[4/23]","Verifying that point is currently accepted and focused");
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		point.closingConflictMsg();
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-50,-50);
		point.closingConflictMsg();
		
		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint[5/23]", "created new point in viewbox 1 and verifying point is present");
		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint[6/23]","Verifying that point is currently accepted and focused");


		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[7/23]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[8/23]","Existing entry from user 1");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[9/23]","New entry from user 2");
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, 60, 60, 150, 150);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[10/23]","New entry should be displayed into content selector like <GSPS(username)> - created new annotation in viewbox 2");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[11/23]","Existing entry from user 1");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[12/23]","Existing entry from user 2");
		viewerPage.assertEquals(allResults.get(2),csResultPrefixForAnotherUser+username_2+"_2","Checkpoint[13/23]","New entry from user 2");

		//resize the line in viewbox 2
		lineWithUnit.selectLinearMeasurement(2, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(2, 1, -50, -100);
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[14/23]","No new entry in content selector as resized the line drawn in viewbox2");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[15/23]","Existing entry 1 from user 1");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[16/23]","Existing entry 1 from user 2");
		viewerPage.assertEquals(allResults.get(2),csResultPrefixForAnotherUser+username_2+"_2","Checkpoint[17/23]","Existing entry 2 from user 2");

		cs.selectResultFromSeriesTab(3,csResultPrefixForAnotherUser+username_2+"_2");
		cs.click(cs.getViewPort(3));
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(3).size(),1,"Checkpoint[18/23]","Validating the linear measurement presence on viewbox 3");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Checkpoint[19/23]","Validating the linear measurement presence on viewbox 2");
		point.closingConflictMsg();
		
		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),3,"Checkpoint[20/23]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefixForAnotherUser+username_1+"_1","Checkpoint[21/23]","Existing entry 1 from user 1");
		viewerPage.assertEquals(allResults.get(1),csResultPrefixForAnotherUser+username_2+"_1","Checkpoint[22/23]","Existing entry 1 from user 2");
		viewerPage.assertEquals(allResults.get(2),csResultPrefixForAnotherUser+username_2+"_2","Checkpoint[23/23]","Existing entry 2 from user 2");




	} 

	@Test(groups ={"Chrome","US770"})
	public void test12_US770_TC2966_TC2968_TC3016_TC3019_TC3022_verifyClonedEntryOnSameViewboxForLine() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates line measurement annotation on same slice of same series on which GSPS already exists"
				+ "<br> Verify spinning wheel is displayed after clicking on content selector for the time clone is getting saved to database"
				+ "<br> Verify cloned entry is generated when user creates line measurement annotation on different slice of same series on which GSPS already exists"
				+ "<br> Verify cloned entry is generated when user creates line measurement annotation on different slice of different series on which GSPS already exists"
				+ "<br> Verify cloned entry is generated when user creates line measurement annotation on same slice of different series on which GSPS already exists");

		//Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		lineWithUnit = new MeasurementWithUnit(driver);
		cs = new ContentSelector(driver);
		//Draw measurement on viewbox1
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 50, 100, 50);
		lineWithUnit.closingConflictMsg();
		
		//Verify spinning wheel is displayed after clicking on content selector for the time clone is getting saved to database
//		viewerPage.assertTrue(contentSelector.verifySpinnerOnSeriesSelector(1),"Checkpoint[1/16]","Verify the spinner during load of series selector");

		viewerPage.assertEquals(cs.getAllResults().size(),1,"Checkpoint[2/16]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(cs.getAllResults().get(0),csResultPrefix+1,"Checkpoint[3/16]","New entry should be displayed into content selector like <GSPS(username)>");

		//Verify cloned entry is generated when user creates line measurement annotation on same slice of same series on which GSPS already exist

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, 50, -50, 50, -100);
		lineWithUnit.closingConflictMsg();
		
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),2,"Checkpoint[4/16]","Two instances of Linear Measurement are present on Viewbox1");

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/16]","Validating new entry is generated for new annotation created on same slice");
		for(int i=latestResults.size(),j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/16]","New entry should be displayed into content selector like <GSPS(username)>");
		}

		//Verify cloned entry is generated when user creates line measurement annotation on different slice of same series on which GSPS already exists

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		viewerPage.scrollToImage(1,10);

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, -50, -50, -150, -150);
		lineWithUnit.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),3,"Checkpoint[7/16]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[8/16]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Checkpoint[9/16]","Single instance of Linear Measurement is present on Viewbox1 as of now since slice is changed");


		//Verify cloned entry is generated when user creates line measurement annotation on same slice of different series on which GSPS already exists		
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -50, -50, -150, -150);
		lineWithUnit.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),4,"Checkpoint[10/16]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[11/16]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Checkpoint[12/16]","Single instance of Linear Measurement is present on Viewbox1 as of now since slice is changed");
		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Checkpoint[13/16]","Single instance of Linear Measurement is present on Viewbox1 as of now since same slice on another series ");

		//Verify cloned entry is generated when user creates line measurement annotation on different slice of different series on which GSPS already exists

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(1);
		viewerPage.waitForViewerpageToLoad(2);
		lineWithUnit.closingConflictMsg();
		
		viewerPage.scrollToImage(38,2);

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -50, -50, -150, -150);
		lineWithUnit.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),5,"Checkpoint[14/16]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[15/16]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(lineWithUnit.getAllLinearMeasurements(2).size(),1,"Checkpoint[16/16]","Single instance of Linear Measurement is present on Viewbox2 as of now since slice is changed");
	}

	@Test(groups ={"Chrome","US770"})
	public void test13_US770_TC3014_verifyClonedEntryOnSameViewboxForPoint() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates point annotation on same slice of same series on which GSPS already exists");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw measurement on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/6]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(latestResults.get(0),csResultPrefix+1,"Checkpoint[2/6]","New entry should be displayed into content selector like <GSPS(username)>");

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,-50,-50);

		viewerPage.assertEquals(point.getLinesOfPoint(1,1).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[3/6]", "point 1 is present");
		viewerPage.assertEquals(point.getFousedLinesOfPoint(1,2).size(),ViewerPageConstants.POINT_LINES,"Checkpoint[4/6]", "point 2 is present");

		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/6]","New entry should be generated when point is created on same slice");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}


	}

	@Test(groups ={"Chrome","US770"})
	public void test14_US770_TC3015_TC3018_TC3021_TC3024_verifyAnnotationEntryinCSForTextAnnot() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates text annotation on same slice of same series on which GSPS already exists"
				+ "<br> Verify cloned entry is generated when user creates Text annotation on different slice of same series on which GSPS already exists"
				+ "<br> Verify cloned entry is generated when user creates Text annotation on different slice of different series on which GSPS already exists"
				+ "<br> Verify cloned entry is generated when user creates Text annotation on same slice of different series on which GSPS already exists");

		String textAnnot1="ABC1",textAnnot2="ABC2";

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		textAn = new TextAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw text annotation on viewbox1
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,50,50,textAnnot1);
		textAn.closingConflictMsg();
		
		List<String> allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),1,"Checkpoint[1/17]","New entry should be displayed into content selector like <GSPS(username)>");
		viewerPage.assertEquals(allResults.get(0),csResultPrefix+1,"Checkpoint[2/17]","New entry should be displayed into content selector like <GSPS(username)>");


		//Verify cloned entry is generated when user creates text annotation on same slice of same series on which GSPS already exists
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);

		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-50,-50,textAnnot2);
		textAn.closingConflictMsg();
		
		viewerPage.assertEquals(textAn.getTextAnnotations(1).size(),2,"Checkpoint[3/17]", "Annotation is present");
		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(1, 1),textAnnot1,"Checkpoint[4/17] : Verifying Text written on Text Annotation 1 ", "text is correctly displayed");		
		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(1, 2),textAnnot2,"Checkpoint[5/17] : Verifying Text written on Text Annotation 2 ", "text is correctly displayed");

		allResults = cs.getAllResults();
		viewerPage.assertEquals(allResults.size(),2,"Checkpoint[6/17]","Same Entires should presisted into content selector like <GSPS(username)>");
		for(int i =0 ,j=1;i<allResults.size();i++,j++){
			viewerPage.assertEquals(allResults.get(i),csResultPrefix+j,"Checkpoint[7/17]","Entries created in checkpoint-2 and checkpoint-7 should persists in content selector");
		}
		//Verify cloned entry is generated when user creates Text annotation on different slice of same series on which GSPS already exists
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		viewerPage.scrollToImage(1, 10);
		textAn.closingConflictMsg();
		
		textAn.selectTextArrowFromQuickToolbar(1);
		textAn.drawText(1,-60,-60,textAnnot2);
		textAn.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),3,"Checkpoint[8/17]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[9/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(1, 1),textAnnot2,"Checkpoint[10/17] : Verifying Text written on Text Annotation 1 from 10th slice ", "text is correctly displayed");		

		//Verify cloned entry is generated when user creates Text annotation on different slice of different series on which GSPS already exists
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		textAn.closingConflictMsg();
		
		viewerPage.scrollToImage(20,2);

		textAn.selectTextArrowFromQuickToolbar(2);
		textAn.drawText(2,-50,-50,textAnnot2);
		textAn.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),4,"Checkpoint[11/17]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=latestResults.size(),j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[12/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(2, 1),textAnnot2,"Checkpoint[13/17]: Verifying Text written on Text Annotation 1 from 20th slice ", "text is correctly displayed");		

		//Verify cloned entry is generated when user creates Text annotation on same slice of different series on which GSPS already exists

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		textAn.closingConflictMsg();
		
		textAn.selectTextArrowFromQuickToolbar(2);
		textAn.drawText(2,-100,-100,textAnnot2);
		textAn.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),5,"Checkpoint[14/17]","Validating there is cloned result generated when annotation is created on different slice");
		for(int i=latestResults.size(),j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[15/17]","No New entry should be displayed into content selector like <GSPS(username)>");
		}

		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(1, 1),textAnnot1,"Checkpoint[16/17] : Verifying Text written on Text Annotation 1 from 10th slice in vb1 ", "text is correctly displayed");		
		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(2, 1),textAnnot1,"Checkpoint[17/17] : Verifying Text written on Text Annotation 2 from 10th slice in vb2 ", "text is correctly displayed");		
		viewerPage.assertEquals(textAn.getTextFromTextAnnotation(2, 2),textAnnot2,"Checkpoint[17/17] : Verifying Text written on Text Annotation 2 from 10th slice in vb2 ", "text is correctly displayed");		


	} 

	@Test(groups ={"Chrome","US770"})
	public void test15_US770_TC3035_verifyResultEntryinCSNotSyncWithFirstResult() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is not in sync with its previous master entry ot any other cloned entry");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		lineWithUnit = new MeasurementWithUnit(driver);
		cs = new ContentSelector(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 50, 0, 100, 0);
		lineWithUnit.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/5]","Validating Results in content selector for creating new annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) 
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/5]","New entry should be displayed into content selector like <GSPS(username)>");


		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);

		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(1, 0, -50, 0, -100);
		lineWithUnit.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[3/5]","Validating there is cloned result generated when annotation is created on different viewbox");
		for(int i=0,j=1;i<latestResults.size();i++,j++) 
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/5]","New entry should be displayed into content selector like <GSPS(username)>");

		cs.selectResultFromSeriesTab(3, csResultPrefix+1);		
		lineWithUnit.closingConflictMsg();
		lineWithUnit.selectLinearMeasurement(1, 2);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, -50, -100);
		lineWithUnit.closingConflictMsg();
		
		viewerPage.compareElementImage(protocolName, viewerPage.mainViewer, "Checkpoint[5/5] : verifying the vb1 and vb3 are not in sync", "test15_1");

	} 

	@Test(groups ={"Chrome","US770"})
	public void test16_US770_TC3023_verifyClonedEntryforEllipse() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates Ellipse annotation on same slice of different series on which GSPS already exists");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		ellipse = new EllipseAnnotation(driver);
		cs = new ContentSelector(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 50, 50, 100, 100);
		ellipse.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/6]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		ellipse.closingConflictMsg();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 50, 50, 100, 100);
				
		viewerPage.assertEquals(ellipse.getEllipses(1).size(),1,"Checkpoint[3/6]","Validating ellipse presence on viewbox1");
		viewerPage.assertEquals(ellipse.getEllipses(2).size(),1,"Checkpoint[4/6]","Validating ellipse presence on viewbox2");
		ellipse.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/6]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}

	}

	@Test(groups ={"Chrome","US770"})
	public void test17_US770_TC3020_verifyClonedEntryforCircle() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates Circle annotation on different slice of different series on which GSPS already exists");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		circle = new CircleAnnotation(driver);
		cs = new ContentSelector(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 100, 100);
		circle.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/7]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/7]","New entry should be displayed into content selector like <GSPS(username)>");
		}
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		circle.closingConflictMsg();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 50, 50, 100, 100);
				
		viewerPage.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint[4/7]","Validating the circle presence on Viewbox1");
		viewerPage.assertEquals(circle.getAllCircles(2).size(),1,"Checkpoint[5/7]","Validating the circle presence on Viewbox2");
		circle.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[6/7]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[7/7]","New entry should be displayed into content selector like <GSPS(username)>");
		}

	}

	@Test(groups ={"Chrome","US770"})
	public void test18_US770_TC3017_verifyClonedEntryforSimpleLine() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify cloned entry is generated when user creates Simple line annotation on different slice of same series on which GSPS already exists");

		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		layout = new ViewerLayout(driver);
		line = new  SimpleLine(driver);
		cs = new ContentSelector(driver);
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1, 50, 50, 100, 100);
		line.closingConflictMsg();
		
		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/6]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}

		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerPage.waitForViewerpageToLoad(2);
		line.closingConflictMsg();
		line.selectLineFromQuickToolbar(2);
		line.drawLine(2, 50, 50, 100, 100);
		
		viewerPage.assertEquals(line.getAllLines(1).size(),1,"Checkpoint[3/6]","Instance of Line presence on Viewbox1");
		viewerPage.assertEquals(line.getAllLines(2).size(),1,"Checkpoint[4/6]","Instance of Line presence on Viewbox2");
		line.closingConflictMsg();
		
		latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),2,"Checkpoint[5/6]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[6/6]","New entry should be displayed into content selector like <GSPS(username)>");
		}

	}

	@Test(groups ={"Chrome","US760","Positive"})
	public void test19_US760_TC3033_verifyRetrievingOfLatestResult() throws InterruptedException, AWTException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify retrieving of latest result");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);
		lineWithUnit = new MeasurementWithUnit(driver);

		//Select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -100, -70, 30);

		//Select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//Select linear measurement from radial menu and draw a linear measurement. Reject it.
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		lineWithUnit.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify rejected status of linear measurement");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation in displaying as rejected");		

		//Mark ellipse in pending state by rejecting it twice
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/14]", "Verify pending status for ellipse annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Verifying that the ellipse annotation is in pending state", "Ellipse annotation is displaying in pending state");		

		String aorticRootMaskSeries = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES05_TEXTOVERLAY, liver9FilePath);

		//Select series from content selector
		cs.selectSeriesFromSeriesTab(1, aorticRootMaskSeries);

		//select circle from radial menu and draw a circle
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -150, -100, -70, 30);

		//select ellipse from radial menu and draw a ellipse 
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select linear measurement from radial menu and draw a linear measurement. Reject it.
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -70, -80, -120, 90);

		
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/14]", "Verify rejected status for linear measurement annotation");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation is displaying as rejected");		

		//Mark ellipse in pending state by rejecting it twice
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/14]", "Verify pending status for ellipse annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Verifying that the ellipse annotation is in pending state", "Ellipse annotation is displaying in pending state");		

		//Login using another user
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		register = new RegisterUserPage(driver);
		register.createNewUser("User", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);	
		Header header = new Header(driver);		
		header.logout();
		loginPage.login(username_1, username_1);
		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/14]", "Verify rejected status for linear measurement annotation drawn by previous user");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation is displaying as rejected");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/14]", "Verify pending status of ellipse annotation drawn by previous user");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Verifying that the ellipse annotation is in pending state", "Ellipse annotation is displaying in pending state");		

		//Draw new line and reject it
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -10, -30, -40, 20);
		lineWithUnit.selectRejectfromGSPSRadialMenu(lineWithUnit.getLinearMeasurements(1,2).get(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/14]", "Verify rejected status for linear measurement annotation");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation is displaying as rejected");	
//		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsRejectedGSPS(1, 2), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation is displaying as rejected");	

		//Reject existing ellipse
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/14]", "Verify rejected status for ellipse annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1), "Verifying that the ellipse annotation is rejected", "Ellipse annotation in displaying as rejected");		

		String csResultPrefix =ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_";
		//Select series from content selector
		cs.selectResultFromSeriesTab(1, csResultPrefix+1);
		cs.click(cs.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/14]", "Verify rejected status for linear measurement annotation drawn by previous user");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Circle annotation in displaying as rejected");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/14]", "Verify pending status of ellipse annotation drawn by previous user");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Verifying that the ellipse annotation is in pending state", "Ellipse annotation is displaying in pending state");		

		//Reject existing ellipse
		ellipse.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
		ellipse.selectRejectfromGSPSRadialMenu(ellipse.getEllipses(1).get(0));		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/14]", "Verify rejected status for ellipse annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1), "Verifying that the ellipse annotation is rejected", "Ellipse annotation in displaying as rejected");		

		header.logout();

		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);

		//rejected new line
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/14]", "Verify rejected status for linear measurement annotation");
		viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Verifying that the linear measurement annotation is rejected", "Linear measurement annotation is displaying as rejected");	

		//rejected ellipse
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/14]", "Verify pending status for ellipse annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsRejectedGSPS(1, 1), "Verifying that the ellipse annotation is in pending state", "Ellipse annotation is displaying in pending state");	

		cs.selectResultFromSeriesTab(1, csResultPrefix+1);
		cs.click(cs.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/14]", "Verify rejected status for circle annotation");
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsPendingGSPS(1, 1), "Verifying that the ellipse annotation is rejected", "Ellipse annotation is displaying as rejected");

	}

	@Test(groups ={"Chrome","DE1029","Positive","BVT"})
	public void test20_DE1029_TC4264_verifyClonedCopyDeletedOnPatientListPage() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Cloned Copy Deleted On PatientListPage after delete all annotations on viewer");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		point = new PointAnnotation(driver);
		cs = new ContentSelector(driver);
		//Draw point annotation on viewbox1
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1,50,50);
		point.drawPointAnnotationMarkerOnViewbox(1,80,80);
		point.drawPointAnnotationMarkerOnViewbox(1,120,120);
		point.drawPointAnnotationMarkerOnViewbox(1,-80,-80);

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/6]","Validating the Content selector creation of new annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(cs.getAllResults().get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/6]","Entry should be created named as GSPS_<Username>_number");
		}
		//CTRL + delete on viewbox -1
		point.deleteAllAnnotation(1);
		viewerPage.browserBackWebPage();

		//User created result tooltip is not present
		// Mouse hover over Result Icon

		patientPage = new PatientListPage(driver);
	
		//Verifing tooltip information
		patientPage.assertTrue(patientPage.getAllMachinesFromTooltip(1).get(0).equalsIgnoreCase(ViewerPageConstants.OBJECT_IMPORTER_TOOLTIP), "Checkpoint[3/6] Verifying "+ViewerPageConstants.OBJECT_IMPORTER_TOOLTIP +" is present in tooltip ", ViewerPageConstants.OBJECT_IMPORTER_TOOLTIP+ " is present in tooltip");
		//Verifying date format
		patientPage.assertTrue(patientPage.verifyDateFormat(ViewerPageConstants.TOOLTIP_DATE_FORMAT, patientPage.getDateTimeFromToolTip()), "Checkpoint[6/6] Verifying date format present in tooltip","Date format is in MM/dd/yyyy, hh:mm ");


	}

	@Test(groups ={"Chrome","DE1029","Positive"})
	public void test21_DE1029_TC4265_verifyUserCreatedCopyNotDeletedAfterReload() throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user created copy not deleted after study reload");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		//Draw circle annotations on viewer
		circle = new CircleAnnotation(driver);
		cs = new ContentSelector(driver);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -100, -80, -50, 30);
		circle.drawCircle(1, 30, 80, -50, 100);
		//Navigate back and reload the study again
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);

		List<String> latestResults = cs.getAllResults();
		viewerPage.assertEquals(latestResults.size(),1,"Checkpoint[1/4]","Validating the Content selector creation of new annotation");
		for(int i=0,j=1;i<latestResults.size();i++,j++) {
			viewerPage.assertEquals(latestResults.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[2/4]","Entry should be created named as GSPS_<Username>_number");
		}
		circle.deleteAllAnnotation(1);
		List<String> latestResults1 = cs.getAllResults();
		viewerPage.assertEquals(latestResults1.size(),1,"Checkpoint[3/4]","Validating new entry is generated for new annotation created");
		for(int i=0,j=1;i<latestResults1.size();i++,j++) {
			viewerPage.assertEquals(latestResults1.get(i),ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+Configurations.TEST_PROPERTIES.get("nsUserName")+"_"+j,"Checkpoint[4/4]","Entry should be created named as GSPS_<Username>_number");
		}

	}

	@Test(groups ={"Chrome","IE11","Edge","DE1334","Positive"})
	public void test22_DE1334_TC5516_verifyCloneWhenUserMovesFreehandPolyline() throws InterruptedException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify cloned entry is generated when user moves freehand  and classic polyline");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(liverPatientName,  1, 1);
		
		cs=new ContentSelector(driver);
	    poly = new PolyLineAnnotation(driver);
	    poly.waitForViewerpageToLoad();
			
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify clone after drawing free hand polyline." );
		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
	    poly.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + username+"_1"), "Checkpoint[1/4]", "Verified clone in CS after drawing freehand polyline.");
		
	    //reload viewer page and move freehand polyline after that draw classic polyline
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewer page and move the freehand polyline." );
	    helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
		
		poly.moveFreePolyLine(1, 1, 70, -30);	
		poly.waitForTimePeriod(2000);
		poly.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + username+"_2"), "Checkpoint[2/4]", "Verified that new clone created after moving freehand polyline when viewer page reloaded.");
	    
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw classic polyline after move action perform for freehand polyline and verify clone");
		poly.selectPolylineFromQuickToolbar(1);
		int[] xyz = new int[] {10,-100,-100,10,-50,50,50,50,100,10};	
		poly.drawPolyLineExitUsingDoubleClickKey(1, xyz);
		poly.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + username+"_2"), "Checkpoint[3/4]", "Verified that no new clone created when classic polyline is drawn.");
		
		//reload viewer page and move classic polyline
		helper.browserBackAndReloadViewer(liverPatientName,  1, 1);
			
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewer page and move classic polyline then verify clone in content selector");
		poly.moveFreePolyLine(1,1,-60,-60);
		poly.waitForTimePeriod(3000);
		poly.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX + username+"_3"), "Checkpoint[4/4]", "Verified that new clone created after moving classic polyline when viewer page reloaded");
	
	}
		
	@AfterMethod(alwaysRun=true)
	public void deleteUsers() {

		DatabaseMethods db = new DatabaseMethods(driver);
		try {
			if(db.checkUserPresence(username_1))
				db.deleteUser(username_1);
			if(db.checkUserPresence(username_2))
				db.deleteUser(username_2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
