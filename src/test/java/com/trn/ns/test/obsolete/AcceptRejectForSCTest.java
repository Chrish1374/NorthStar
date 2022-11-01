//package com.trn.ns.test.obsolete;
//import java.sql.SQLException;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.MeasurementWithUnit;
//
//import com.trn.ns.page.factory.OutputPanel;
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class AcceptRejectForSCTest extends TestBase {
//	
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ViewerPage viewerPage;
//	
//	private ExtentTest extentTest;
//	private CircleAnnotation circle ;
//	private EllipseAnnotation ellipse ;
//	private MeasurementWithUnit lineWithUnit;
//	private ContentSelector contentSelector;
//	private TextAnnotation textAn;	
//	private OutputPanel panel;
//	
//
//    String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, iMBIO);
//	
//	public final String ANNOTATION_TXT_1="ABC";
//	public final String ANNOTATION_TXT_2="DEF";
//	String username = "test";
//	String EllipseComment="ellipseComment";
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws SQLException {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
//	public void test04_US1075_TC5255_TC5257_verifyCommentOnDICOMSCResult() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to add comment on DICOM SC result  view box.<BR>"+
//				"Verify the accept reject functionality of comment on DICOM SC data.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(imbio_PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		contentSelector=new ContentSelector(driver);
//		ellipse=new EllipseAnnotation(driver);
//		textAn=new TextAnnotation(driver);
//		panel=new OutputPanel(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
//		viewerPage.addtext(1,ANNOTATION_TXT_1);
//		//store location of first text comment before move
//		int beforeX1=viewerPage.getValueOfXCoordinate(textAn.selectTextAnnotLine(1, 1).get(0));
//		int beforeY1=viewerPage.getValueOfYCoordinate(textAn.selectTextAnnotLine(1, 1).get(0));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/15]", "Verify location of first text comment after move" );
//		textAn.selectTextAnnotLine(1, 1).get(0).click();
//		textAn.moveTextAnnotation(1,1, -20, -50);
//		int afterX1=viewerPage.getValueOfXCoordinate(textAn.selectTextAnnotLine(1, 1).get(0));
//		int afterY1=viewerPage.getValueOfYCoordinate(textAn.selectTextAnnotLine(1, 1).get(0));
//		viewerPage.assertNotEquals(afterX1, beforeX1, "Checkpoint[2/15]", "X coordinate for added text changed after move");
//		viewerPage.assertNotEquals(afterY1, beforeY1, "Checkpoint[3/15]", "Y coordinate for added text changed after move");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/15]", "Verify state of DICOM SC and state of added text after rejecting the text comment for annotation" );
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.selectRejectfromGSPSRadialMenu(textAn.getAnchorLinesOfTextAnnot(1, 1).get(0));
//		viewerPage.selectPreviousfromGSPSRadialMenu();
//		viewerPage.assertEquals(viewerPage.getBadgeCountForBinarySelector(1),1, "Checkpoint[5/15]", "State of DICOM SC result series remain as pending after rejecting drawn annotation on seondary capture");
//		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1), "Checkpoint[6/15]", "Verified state of  added text comment as rejected");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/15]", "Verify state of DICOM SC and state of added text after accepting the text comment for annotation" );
//		viewerPage.selectAcceptfromGSPSRadialMenu(textAn.getAnchorLinesOfTextAnnot(1, 1).get(0));
//		viewerPage.selectPreviousfromGSPSRadialMenu();
//		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(1), "Checkpoint[8/15]", "Verified state of  added text comment as accepted");
//		viewerPage.assertEquals(viewerPage.getBadgeCountForBinarySelector(1),1, "Checkpoint[9/15]", "State of DICOM SC result series remain as pending after accepting drawn annotation on seondary capture");
//
//		//after move of first comment ,add 2nd comment and verify location of x and y coordinate
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/15]", "Verify location of second text comment before move.It should be created at 'Center of Image' with anchor point" );
//		viewerPage.mouseHover(viewerPage.getViewPort(3));
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.addtext(1,ANNOTATION_TXT_2);
//		int beforeX2=viewerPage.getValueOfXCoordinate(textAn.selectTextAnnotLine(1, 2).get(0));
//		int beforeY2=viewerPage.getValueOfYCoordinate(textAn.selectTextAnnotLine(1, 2).get(0));
//		viewerPage.assertEquals(beforeX2, beforeX1, "Checkpoint[11/15]", "X coordinate for added text 2 before move");
//		viewerPage.assertEquals(beforeY2, beforeY1, "Checkpoint[12/15]", "Y coordinate for added text 2 before move");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/15]", "Verify location of second text comment after move" );
//		textAn.selectTextAnnotLine(1, 2).get(0).click();
//		textAn.moveTextAnnotation(1,2, -20, 50);
//		int afterX2=viewerPage.getValueOfXCoordinate(textAn.selectTextAnnotLine(1, 2).get(0));
//		int afterY2=viewerPage.getValueOfYCoordinate(textAn.selectTextAnnotLine(1, 2).get(0));
//		viewerPage.assertNotEquals(afterX2, beforeX2, "Checkpoint[14/15]", "X coordinate for added text 2 changed after move");
//		viewerPage.assertNotEquals(afterY2, beforeY2, "Checkpoint[15/15]", "Y coordinate for added text 2 changed after move");
//
//	}
//	
//	
//	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
//	public void test04_US1075_TC5258_TC5259_TC5260_verifyCommentOnDICOMSCResultWithAcceptResultFunctFORSC() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify  the State of DICOM SC result series on changing the state of Text comment annotation. <br>"+
//				"Verify the accept reject functionality of comment on DICOM SC data.<br>"
//				+ "Verify that user is able to add comment on GSPS annotation DICOM SC result  view box.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(imbio_PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(1);
//		contentSelector=new ContentSelector(driver);
//		ellipse=new EllipseAnnotation(driver);
//		textAn=new TextAnnotation(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
//		viewerPage.addtext(1,ANNOTATION_TXT_1);
//		viewerPage.assertTrue(viewerPage.verifyNumberOfFindingsOnHeaderOfFindingTable(2), "Checkpoint [1/13]", "The number of pending finding on header of table is "+viewerPage.getNumberOfFindingsOnBadge());
//		viewerPage.mouseHover(viewerPage.getViewPort(3));
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		viewerPage.click(viewerPage.getGSPSDeleteButton(1));	
//		viewerPage.assertEquals(viewerPage.getText(viewerPage.getOuterGSPSNotification()), ViewerPageConstants.NO_FINDING, "Checkpoint [2/13]", "verifying the tooltip text - Please select a finding when click on delete button from A/R toolbar");
//		viewerPage.assertTrue(viewerPage.isElementPresent(viewerPage.getOuterGSPSNotification()),"Checkpoint [3/13]", "verifying the tooltip presence when mousehover on Delete buttom");
//		viewerPage.assertTrue(viewerPage.verifyGSPSDeleteButtonIsDisabled(),"Checkpoint [4/13]", "verifying the icon is disabled - color for delete button");
//
//		viewerPage.selectPreviousfromGSPSRadialMenu();
//		viewerPage.acceptResult(1);
//		viewerPage.selectNextfromGSPSRadialMenu(1);
//		viewerPage.assertTrue(viewerPage.verifyResultsAreAccepted(1), "Checkpoint[5/13]", "Verified that state of Secondary capture result as Accepted");
//		viewerPage.assertEquals(viewerPage.getBadgeCountForBinarySelector(1),0, "Checkpoint[6/13]", "State of DICOM SC result series  as accepted after accepting seondary capture");
//
//		viewerPage.selectNextfromGSPSRadialMenu(1);
//		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1,false), "Checkpoint[7/13]", "Verify text comment is accepted GSPS");
//		viewerPage.selectPreviousfromGSPSRadialMenu();
//
//		viewerPage.mouseHover(viewerPage.getViewPort(3));
//		viewerPage.rejectResult(1);
//		viewerPage.selectPreviousfromGSPSRadialMenu();
//		viewerPage.assertTrue(viewerPage.verifyResultsAreRejected(1), "Checkpoint[8/13]", "Verified that state of Secondary capture result as rejected");
//		viewerPage.assertEquals(viewerPage.getBadgeCountForBinarySelector(1),0, "Checkpoint[9/13]", "State of DICOM SC result series  as rejected after rejecting seondary capture");
//		viewerPage.selectNextfromGSPSRadialMenu();
//		viewerPage.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1,false), "Checkpoint[10/13]", "Verify added text comment accepted");
//
//		viewerPage.mouseHover(viewerPage.getViewPort(1));
//		textAn.selectTextAnnotLine(1, 1).get(0).click();
//		viewerPage.selectDeletefromGSPSRadialMenu(1);
//		viewerPage.assertEquals(viewerPage.findings.size(),1, "Checkpoint[11/13]", "The number of finding on header table is "+viewerPage.findings.size());	
//
//		ellipse.selectEllipseAnnotationFromContextMenu(1);
//		ellipse.drawEllipse(1, 0, 0, -100,-150);
//		ellipse.addResultComment(ellipse.getAllEllipses(1).get(0), EllipseComment);
//		viewerPage.assertEquals(ellipse.getTextCommentsforAllEllipses(1).size(),1,"Checkpoint[12/13]" ,"Verify size of comment for ellipse");
//		viewerPage.assertEquals(ellipse.getText(ellipse.getTextCommentsforAllEllipses(1).get(0)),EllipseComment,"Checkpoint[13/13] : Verifying comment written on ellipse Annotation", "Verified text of comment for ellipse annotation");
//
//
//
//	}
//}
//
