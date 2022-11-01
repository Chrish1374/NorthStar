//package com.trn.ns.test.obsolete;
//
//import java.util.List;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.SimpleLine;
//
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class DeleteAnnotationTest extends TestBase {
//
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//	private ContentSelector contentSelector;
//
//	String filePath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath);
//
//	String filePath1=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath1);
//	
//	String filePath2=Configurations.TEST_PROPERTIES.get("PICCONE_filepath");
//	String picclinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY,filePath2);
//	
//	private PointAnnotation point;
//	private CircleAnnotation circle;
//	private EllipseAnnotation ellipse;
//	private MeasurementWithUnit lineWithUnit;
//	private TextAnnotation textAn;
//	private SimpleLine line;
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}
//
//
//	@Test(groups ={"Chrome","IE11","Edge","US793"})  
//	public void test10_US793_TC3061_verifyDeletionOnMachineAnnotation() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify delete button functionality on machine generated GSPS");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(gspsPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		
//		point = new PointAnnotation(driver);
////		circle = new CircleAnnotation(driver);
////		ellipse = new EllipseAnnotation(driver);
////		lineWithUnit = new MeasurementWithUnit(driver);
////		textAn = new TextAnnotation(driver);
////		line = new  SimpleLine(driver);
//		
//		int currentImageNumber= viewerPage.getCurrentScrollPositionOfViewbox(1);
//		
//		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint [1/12]","Verifying that point is present and in pending state");
//		viewerPage.assertTrue(viewerPage.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/12]", "Pending tool bar is dispplayed upon load");
//		
//				
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/12]", "Disabled icon should be present on delete button when mouse hover to delete machine generated GSPS and user should not be able to click on delete button and notification text will be pop up that \"Machine results cannot be deleted\"\r\n" + 
//				"Disabled icon should be present on delete button when mouse hover to delete machine generated GSPS and user should not be able to click on delete button and notification text will be pop up that \"Please select a finding\"");
//		
//		
//		viewerPage.mouseHover(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [4/12]", "verifying the icon color");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [5/12]", "verifying the tooltip presence");
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [6/12]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), NSConstants.MACHINE_RESULTS_DELETION_MSG, "Checkpoint [7/12]", "verifying the tooltip text - Machine results cannot be deleted");
//		
//		viewerPage.mouseHover(viewerPage.getViewPort(2));
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),currentImageNumber,"Checkpoint [8/12]", "Verifying on deletion it will not jump to the next annotation since nothing is selected and machine data can't be deleted");
//		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint [9/12]","Verifying point is still present");
//		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(1, 1),"Checkpoint [10/12]","No state change for point");
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [11/12]", "verifying the tooltip text - Please select a finding");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [12/12]", "verifying the tooltip presence");
//		
//	}
//		
//	@Test(groups ={"Chrome","IE11","Edge","US793"})  
//	public void test11_US793_TC3062_verifyDeletionForAllAnnotation() throws InterruptedException  {
//
//		int inputImageNumber=5;
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Delete button functionality on all annotations");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(gspsPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		
//		point = new PointAnnotation(driver);
//		circle = new CircleAnnotation(driver);
//		ellipse = new EllipseAnnotation(driver);
//		lineWithUnit = new MeasurementWithUnit(driver);
//		textAn = new TextAnnotation(driver);
//		line = new  SimpleLine(driver);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/16]", "Drawn annotations are visible on the series and all annotations are in accepting state and selected but not highlighted. Delete button should be enabled after drawing an annotation because last annotation is selected");
//		
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,2),"Checkpoint [2/16]","Drawn Point is in accepted stated");
//		
//		circle.selectCircleAnnotationIconFromRadialMenu(1);
//		circle.drawCircle(1, 50, 50, 100, 100);
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint [3/16]","Drawn Circle is in accepted stated");
//		
//		viewerPage.inputImageNumber(inputImageNumber, 1);
//		
//		ellipse.selectEllipseAnnotationFromContextMenu(1);		
//		ellipse.drawEllipse(1, -50, -50, 70, 90);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint [4/16]","Drawn Ellipse is in accepted stated");
//		
//		
//		int currentImageNumber= viewerPage.getCurrentScrollPositionOfViewbox(1);
//		
//			
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/16]", "Delete button should only delete the selected or highlighted annotation and it will jump to the next annotation if selected annotation is user drawn.");
//		
//		viewerPage.mouseHover(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [6/16]", "verifying the icon color");
//		viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [7/16]", "verifying the tooltip absence");
//		viewerPage.assertTrue(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class").isEmpty(), "Checkpoint [8/16]", "verifying the icon is not disabled - class");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),currentImageNumber,"Checkpoint [9/16]", "Verifying on deletion it will jump to the next annotation ");
//		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint [10/16]","Verifying ellipse is deleted and Next annotation is highlighted");
//		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint [11/16]","Verifying that machine data is highlighted");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [12/16]", "verifying the icon color - when machine annotation is highlighted");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [13/16]", "verifying the tooltip presence");
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [14/16]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), NSConstants.MACHINE_RESULTS_DELETION_MSG, "Checkpoint [15/16]", "verifying the tooltip text - Machine results cannot be deleted");
//		
//		viewerPage.inputImageNumber(inputImageNumber, 1);		
//		viewerPage.assertFalse(ellipse.isEllipsePresent(1),"Checkpoint [16/16]","Verifying the ellipse is deleted");
//		
//	}
//		
//	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
//	public void test12_US793_TC3110_verifyDeletionOfAnnotationOnSameSlice() throws InterruptedException  {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify delete button functionality while (animated)annotations on same slice");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		
//		point = new PointAnnotation(driver);
//		circle = new CircleAnnotation(driver);
//		ellipse = new EllipseAnnotation(driver);
//		lineWithUnit = new MeasurementWithUnit(driver);
//		textAn = new TextAnnotation(driver);
//		line = new  SimpleLine(driver);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/18]", "Draw annotations on same slice and different place on viewbox .");
//		
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 200, -200);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint [2/18]","Drawn Point is in accepted stated");
//		
//		point.drawPointAnnotationMarkerOnViewbox(1, -200, 200);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,2),"Checkpoint [3/18]","Drawn Point is in accepted stated");
//		
//		circle.selectCircleAnnotationIconFromRadialMenu(1);
//		circle.drawCircle(1, 50, 50, 100, 100);
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint [4/18]","Drawn Circle is in accepted stated");
//			
//		ellipse.selectEllipseAnnotationFromContextMenu(1);		
//		ellipse.drawEllipse(1, -300, -200, 100, 90);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint [5/18]","Drawn Ellipse is in accepted stated");
//		
//		
//		viewerPage.inputZoomNumber(200, 1);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/18]", "Verify GSPS is not selected/highlighted and also verify delete button on A/R toolbar."
//				+ "No GSPS is selected when user do panning and delete button is disabled and user should see the text \"please select a finding' as a notification on top of the A/R UI and tool tip should appear on bottom of the button.");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [7/18]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [8/18]", "verifying the tooltip text - Please select a finding");
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [9/18]", "verifying the icon is disabled - color");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [10/18]", "verifying the tooltip presence");
//		
//		viewerPage.navigateGSPSForwardUsingKeyboard();
//		viewerPage.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,2),"Checkpoint [11/18]","Second point is highlighted");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint [12/18]","Deleted and verified the point");
//		
//		viewerPage.waitForCoordinatesToGetChanged(circle.getAllCircles(1).get(0));
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [13/18]","Verifying Circle is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(circle.getAllCircles(1).size(),0,"Checkpoint [14/18]","Post Deletion checking circle is absent");
//		
//		viewerPage.waitForCoordinatesToGetChanged(ellipse.getEllipses(1).get(0));
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [15/18]","Verifying ellipse is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(ellipse.getEllipses(1).size(),0,"Checkpoint [16/18]","Post Deletion checking ellipse is absent");
//		
//		viewerPage.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [17/18]","Verifying point is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(point.getAllPoints(1).size(),0,"Checkpoint [18/18]","Post Deletion checking point is absent");
//		
//		
//	}
//	
//	@Test(groups ={"Chrome","IE11","Edge","US793","Sanity"})  
//	public void test13_US793_TC3112_verifyDeletionOfAnnotationOnDifferentSlice() throws InterruptedException  {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("verify delete button functionality while (animated)annotations on different slice");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(ah4PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		point = new PointAnnotation(driver);
//		circle = new CircleAnnotation(driver);
//		ellipse = new EllipseAnnotation(driver);
//		lineWithUnit = new MeasurementWithUnit(driver);
//		textAn = new TextAnnotation(driver);
//		line = new  SimpleLine(driver);
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/22]", "Draw annotations on same slice and different place on viewbox .");
//		
//		viewerPage.inputImageNumber(10, 1);
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, 200, -200);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint [2/22]","Drawn Point is in accepted stated");
//		
//		viewerPage.inputImageNumber(11, 1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -200, 200);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,1),"Checkpoint [3/22]","Drawn Point is in accepted stated");
//		
//		viewerPage.inputImageNumber(12, 1);
//		circle.selectCircleAnnotationIconFromRadialMenu(1);
//		circle.drawCircle(1, 50, 50, 100, 100);
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint [4/22]","Drawn Circle is in accepted stated");
//			
//		viewerPage.inputImageNumber(13, 1);
//		ellipse.selectEllipseAnnotationFromContextMenu(1);		
//		ellipse.drawEllipse(1, -300, -200, 100, 90);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint [5/22]","Drawn Ellipse is in accepted stated");
//		
//		
//		viewerPage.inputZoomNumber(200, 1);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/22]", "Verify GSPS is not selected/highlighted and also verify delete button on A/R toolbar."
//				+ "No GSPS is selected when user do panning and delete button is disabled and user should see the text \"please select a finding' as a notification on top of the A/R UI and tool tip should appear on bottom of the button.");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [7/22]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [8/22]", "verifying the tooltip text - Please select a finding");
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [9/22]", "verifying the icon is disabled - color");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [10/22]", "verifying the tooltip presence");
//		
//		viewerPage.navigateGSPSBackUsingKeyboard();
//		
//		viewerPage.waitForCoordinatesToGetChanged(circle.getAllCircles(1).get(0));
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [11/22]","Verifying Circle is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),13,"Checkpoint [12/22]","Verifying after deletion it is mapped to another finding");
//		
//		viewerPage.waitForCoordinatesToGetChanged(ellipse.getEllipses(1).get(0));
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1,1),"Checkpoint [13/22]","Verifying ellipse is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),10,"Checkpoint [14/22]","Verifying after deletion it is mapped to another finding");
//		
//		viewerPage.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [15/22]","Second point is highlighted");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),11,"Checkpoint [16/22]","Verifying after deletion it is mapped to another finding");
//		
//		viewerPage.waitForCoordinatesToGetChanged(point.getPoint(1, 1).get(0));
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1,1),"Checkpoint [17/22]","Verifying point is in focus and selected");
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),11,"Checkpoint [18/22]","Verifying after deletion it is mapped to another finding");
//		
//		viewerPage.inputImageNumber(10, 1);
//		viewerPage.assertEquals(point.getAllPoints(1).size(),0,"Checkpoint [19/22]","Post Deletion checking point is absent");
//		viewerPage.inputImageNumber(11, 1);	
//		viewerPage.assertEquals(point.getAllPoints(1).size(),0,"Checkpoint [20/22]","Deleted and verified the point");
//		viewerPage.inputImageNumber(12, 1);
//		viewerPage.assertEquals(ellipse.getEllipses(1).size(),0,"Checkpoint [21/22]","Post Deletion checking ellipse is absent");
//		viewerPage.inputImageNumber(13, 1);
//		viewerPage.assertEquals(circle.getAllCircles(1).size(),0,"Checkpoint [22/22]","Post Deletion checking circle is absent");
//		
//	}
//	
//	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
//	public void test14_US793_TC3113_verifyDeletionForAllAnnotationAfterResize() throws InterruptedException  {
//
//		int inputImageNumber=5;
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify delete functionality after Resize annotations");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(gspsPatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		
//		point = new PointAnnotation(driver);
//		circle = new CircleAnnotation(driver);
//		ellipse = new EllipseAnnotation(driver);
//		lineWithUnit = new MeasurementWithUnit(driver);
//		textAn = new TextAnnotation(driver);
//		line = new  SimpleLine(driver);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/16]", "Drawn annotations are visible on the series and all annotations are in accepting state and selected but not highlighted. Delete button should be enabled after drawing an annotation because last annotation is selected");
//		
//		point.selectPointAnnotationIconFromRadialMenu(1);
//		point.drawPointAnnotationMarkerOnViewbox(1, -100, -100);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1,2),"Checkpoint [2/16]","Drawn Point is in accepted stated");
//		
//		circle.selectCircleAnnotationIconFromRadialMenu(1);
//		circle.drawCircle(1, 50, 50, 100, 100);
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1,1),"Checkpoint [3/16]","Drawn Circle is in accepted stated");
//		
//		viewerPage.inputImageNumber(inputImageNumber, 1);
//		
//		ellipse.selectEllipseAnnotationFromContextMenu(1);		
//		ellipse.drawEllipse(1, -50, -50, 70, 90);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1,1),"Checkpoint [4/16]","Drawn Ellipse is in accepted stated");
//		
//		//resize the ellipse		
//		ellipse.moveSelectedEllipse(1,100,120);
//		
//		int currentImageNumber= viewerPage.getCurrentScrollPositionOfViewbox(1);
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/16]", "Delete button should only delete the selected or highlighted annotation and it will jump to the next annotation if selected annotation is user drawn.");
//		
//		viewerPage.mouseHover(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [6/16]", "verifying the icon color");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [7/16]", "verifying the tooltip presence");
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"),ViewerPageConstants.ICON_DISABLE, "Checkpoint [8/16]", "verifying the icon is not disabled - class");
//		
//		viewerPage.navigateGSPSForwardUsingKeyboard();
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		
//		viewerPage.assertNotEquals(viewerPage.getCurrentScrollPositionOfViewbox(1),currentImageNumber,"Checkpoint [9/16]", "Verifying on deletion it will jump to the next annotation ");
//		viewerPage.assertTrue(point.isPointPresent(1,1),"Checkpoint [10/16]","Verifying ellipse is deleted and Next annotation is highlighted");
//		viewerPage.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1),"Checkpoint [11/16]","Verifying that machine data is highlighted");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [12/16]", "verifying the icon color - when machine annotation is highlighted");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [13/16]", "verifying the tooltip presence");
//		viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.gspsDelete, "class"), ViewerPageConstants.ICON_DISABLE, "Checkpoint [14/16]", "verifying the icon is disabled - class");		
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), NSConstants.MACHINE_RESULTS_DELETION_MSG, "Checkpoint [15/16]", "verifying the tooltip text - Machine results cannot be deleted");
//		
//		viewerPage.inputImageNumber(inputImageNumber, 1);		
//		viewerPage.assertFalse(ellipse.isEllipsePresent(1),"Checkpoint [16/16]","Verifying the ellipse is deleted");
//		
//		
//		
//	}
//	
//	@Test(groups ={"Chrome","IE11","Edge","US793"}) 
//	public void test15_US793_TC3116_TC3117_verifyReloadAnnotationPostDeletion() throws InterruptedException  {
//
//				
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Reload the study after delete few annotations"
//				+ "<br> Verify deleted annotations on series selected through content selector");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(picclinePatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(2);
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/18]", "Drawn annotations are visible on the series and all annotations are in accepting state and selected but not highlighted. Delete button should be enabled after drawing an annotation because last annotation is selected");
//		
//		point.selectPointAnnotationIconFromRadialMenu(2);
//		point.drawPointAnnotationMarkerOnViewbox(2, -100, -100);
//		viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(2,1),"Checkpoint [2/18]","Drawn Point is in accepted stated");
//		
//		circle.selectCircleAnnotationIconFromRadialMenu(2);
//		circle.drawCircle(2, 50, 50, 100, 100);
//		viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(2,1),"Checkpoint [3/18]","Drawn Circle is in accepted stated");
//		
//		ellipse.selectEllipseAnnotationFromContextMenu(2);		
//		ellipse.drawEllipse(2, -50, -50, 70, 90);
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(2,1),"Checkpoint [4/18]","Drawn Ellipse is in accepted stated");
//		
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/18]", "Delete button should only delete the selected or highlighted annotation and it will jump to the next annotation if selected annotation is user drawn.");
//				
////		viewerPage.navigateGSPSForwardUsingKeyboard();
//		viewerPage.navigateGSPSBackUsingKeyboard();
//		
//		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(2, 1),"Checkpoint [6/18]","Verifying that machine data is highlighted");
//		
//		viewerPage.click(viewerPage.getGSPSDeleteButton(2));		
//			
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1),"Checkpoint [7/14]","Verifying that machine data is highlighted");
//		viewerPage.assertEquals(ellipse.getEllipses(2).size(),0,"Checkpoint [8/18]","Post Deletion checking ellipse is absent");
//		viewerPage.assertEquals(point.getAllPoints(2).size(),1,"Checkpoint [9/18]","Post Deletion checking point is present");
//		viewerPage.assertEquals(circle.getAllCircles(2).size(),1,"Checkpoint [10/18]","Post Deletion checking circle is present");
//		
//		viewerPage.navigateBackToStudyListPage();
//		studyPage.clickOntheFirstStudy();
//		viewerPage.waitForViewerpageToLoad(2);
//		
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1),"Checkpoint [11/16]","Verifying that machine data is highlighted");
//		viewerPage.assertEquals(ellipse.getEllipses(2).size(),0,"Checkpoint [12/18]","Post Deletion checking ellipse is absent");
//		viewerPage.assertEquals(point.getAllPoints(2).size(),1,"Checkpoint [13/18]","Post Deletion checking point is present");
//		viewerPage.assertEquals(circle.getAllCircles(2).size(),1,"Checkpoint [14/18]","Post Deletion checking circle is present");
//		
//		contentSelector = new ContentSelector(driver);
//		
//		List<String> results = contentSelector.getAllResultDesciptionFromContentSelector(2);
//		contentSelector.selectResultFromContentSelector(1, results.get(results.size()-1));
//		
//		viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Checkpoint [15/18]","Verifying that machine data is highlighted");
//		viewerPage.assertEquals(ellipse.getEllipses(1).size(),0,"Checkpoint [16/18]","Post Deletion checking ellipse is absent");
//		viewerPage.assertEquals(point.getAllPoints(1).size(),1,"Checkpoint [17/18]","Post Deletion checking point is present");
//		viewerPage.assertEquals(circle.getAllCircles(1).size(),1,"Checkpoint [18/18]","Post Deletion checking circle is present");
//		
//	
//	}
//
//
//	public void dropPointOnNewSlice(int scrollNum, int pointX, int pointY) throws InterruptedException{
//		point = new PointAnnotation(driver);
//		point.scrollDownToSliceUsingKeyboard(1, scrollNum);
//		point.drawPointAnnotationMarkerOnViewbox(1, pointX, pointY);
//		point.acceptResult(1);
//
//	}
//
//	
//}
