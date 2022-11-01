package com.trn.ns.test.viewer.GSPS;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CommentAdditionGroupsTest extends TestBase  {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel preset;
	private TextAnnotation textAnn;
	private CircleAnnotation circle ;
	private EllipseAnnotation ellipse ;
	private final String LinearmeasurementComment="Linear Measurement Comment";

	String gaelKuhnFilePath =Configurations.TEST_PROPERTIES.get("GAEL^KUHN_filepath");
	String GaelPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gaelKuhnFilePath);
	String GaelPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, gaelKuhnFilePath);

	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String liver9FilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);


	String longComment = "This is a sentence which is long ";

	private final String editRulerComment="Edited Ruler comment";
	private final String ellipseComment="Ellipse comment";
	private final String editEllipseComment="Edit Ellipse comment";
	private final String circleComment="Circle comment";
	private final String ANNOTATION_TXT_1="ABC";
	private HelperClass helper;
	private ViewerSliderAndFindingMenu findingMenu;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//US1007 :Add comments to groups

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive","BVT"})
	public void test14_US1007_TC4565_verifyCommentAddedToMachineDrawnAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of adding comment to machine generated annotations findings/groups.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPage("",LIDCPatientID,  1, 1);
		findingMenu = new ViewerSliderAndFindingMenu(driver);

		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify user can add comment to annotation for patient"+" "+LIDCPatientID);
		findingMenu.openFindingTableOnBinarySelector(1);
		findingMenu.addResultComment(findingMenu.findings.get(1),LinearmeasurementComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verify entered result comment text is visible on viewbox with italic format for a unformatedText annotation");
		viewerPage.assertEquals(viewerPage.getText(findingMenu.resultComment.get(0)), LinearmeasurementComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+LinearmeasurementComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotation", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify entered result comment text is visible when mouse hover on iIcon");
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		viewerPage.mouseHover(findingMenu.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.iIconInfo.get(0), NSGenericConstants.TITLE),LinearmeasurementComment, "Verify result comment is visible when  mouse hover action perform", "Verified");

		//check add comment functionality for Group
		helper.browserBackAndReloadViewer(GaelPatient,  1, 1);

		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", " Verify user can add comment to annotation for patient"+" "+GaelPatient);
		viewerPage.assertTrue(findingMenu.addResultComment(1, 1, 2, LinearmeasurementComment)," Verify edit box closes after comment is added", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify entered result comment text is visible on viewbox with italic format for a unformatedText annotation");
		viewerPage.assertEquals(viewerPage.getText(findingMenu.resultComment.get(1)), LinearmeasurementComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+LinearmeasurementComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotationt", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify entered result comment text is visible when mouse hover on iIcon");
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		String groupName=findingMenu.getListOfGroupsInFindingMenu(1).get(0);
		String findingName=findingMenu.getListOfFindingsFromFindingMenu(1).get(1);
		viewerPage.assertTrue(findingMenu.verifyCommentForGroupFindingInFindingMenu(1, groupName, findingName, LinearmeasurementComment), "Verify result comment is visible when  mouse hover action perform", "Verified");


	}

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive","BVT"})
	public void test15_US1007_TC4566_verifyCommentAddedToUserDrawnAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of adding comment to user generated annotations finding.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);


		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify user can add comment to user drawn annotation ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 70,70);
		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify group is not created to contain the comment");
		viewerPage.assertEquals((circle.groupInfo.size()),0, "Verify group is not created", "Verified");

		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of circle annotation", "The result comment is in aceepted state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify entered result comment text is visible when mouse hover on iIcon");
		circle.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		viewerPage.mouseHover(circle.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.iIconInfo.get(0), NSGenericConstants.TITLE),circleComment, "Verify result comment is visible when  mouse hover action perform", "Verified");


	}

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive"})
	public void test16_US1007_TC4568_verifyCommentAddedToOnlyOneFindingPerGroup() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification for comment adding only in one finding per group.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectlyUsingID(LIDCPatientID,  1);

		ellipse=new EllipseAnnotation(driver);
		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", " Verify user can add comment to annotation for patient"+" "+LIDCPatientID);
		ellipse.addResultComment(1, 2,5,ellipseComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify entered result comment text is visible on viewbox with italic format for a ellipse annotation");
		viewerPage.assertEquals(viewerPage.getText(ellipse.resultComment.get(1)), ellipseComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+ellipseComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotationt", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify the background of the finding is in grey color when finding is selected on finding toolbar ");
		ellipse.openFindingTableOnBinarySelector(1);
		viewerPage.click(ellipse.toggleButton.get(1));
		viewerPage.assertEquals(viewerPage.getCssValue(ellipse.findings.get(6), NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR,"Verify Background color as Gray for the first finding when it is selected","Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify entered result comment text is visible when mouse hover on iIcon");
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");

		String resultComment=ellipseComment+" " +"from"+" "+ viewerPage.getAttributeValue(ellipse.listOfFindingsFromGroup.get(4), NSGenericConstants.TITLE);
		viewerPage.mouseHover(ellipse.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.iIconInfo.get(0), NSGenericConstants.TITLE),resultComment, "Verify result comment is visible when  mouse hover action perform", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify user can edit comment");
		ellipse.editResultComment(ellipse.resultComment.get(1), editEllipseComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify edited result comment text is visible on viewbox with italic format for a ellipse annotation");
		viewerPage.assertEquals(viewerPage.getText(ellipse.resultComment.get(1)), editEllipseComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+editEllipseComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(ellipse.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotationt", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify infobar appears on hovering add text button as only one text comment is allowed per group");
		ellipse.hoverOnAddTextButtonForFinding(1,2,1);
		viewerPage.assertTrue(viewerPage.isElementPresent(ellipse.commentInfo),"Verify Info bar appears on top of A/R bar","The info bar is present on viewbox");
		viewerPage.assertEquals(viewerPage.getText(ellipse.commentInfo),ViewerPageConstants.ALREADY_FINDING,"Verify text of info bar", "Verified the text of info bar");	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify add text comment icon is disabled when comment already exists for any finding");
		viewerPage.assertTrue(ellipse.isIconDisable(ellipse.gspsText), "Verify Add text icon is disabled", "The Add text icon is disabled");
	}

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive"})
	public void test17_US1007_TC4570_verifyCommentDeleteFromMachineFindings() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of deleting comment from machine generated findings/groups.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerPageUsingSearch(LIDCPatientID,  1, 1);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify user can add comment to annotation for patient"+" "+LIDCPatientID);
		findingMenu.openFindingTableOnBinarySelector(1);
		findingMenu.addResultComment(findingMenu.findings.get(1),LinearmeasurementComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/9]", "Verify entered result comment text is visible on viewbox with italic format for a unformatedText annotation");
		viewerPage.assertEquals(viewerPage.getText(findingMenu.resultComment.get(0)), LinearmeasurementComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+LinearmeasurementComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotation", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify entered result comment text is visible when mouse hover on iIcon");
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		viewerPage.mouseHover(findingMenu.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.iIconInfo.get(0), NSGenericConstants.TITLE),LinearmeasurementComment, "Verify result comment is visible when  mouse hover action perform", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify entered result comment deleted by clicking on x icon and iIcon should not seen");
		findingMenu.deleteResultComment(findingMenu.resultComment.get(0));
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(findingMenu.iIconInfo.size(),0, "Verify iIcon after deleting result comment", "Verified");
		viewerPage.assertEquals(findingMenu.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");

		//check add comment functionality for Group
		helper.browserBackAndReloadViewer(GaelPatient,  1, 1);

		//verify Groups for that patient
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", " Verify user can add comment to annotation for patient"+" "+GaelPatient);
		findingMenu.addResultComment(1, 1, 1, ellipseComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify entered result comment text is visible on viewbox with italic format for a unformatedText annotation");
		viewerPage.assertEquals(viewerPage.getText(findingMenu.resultComment.get(1)), ellipseComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+ellipseComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotationt", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify entered result comment text is visible when mouse hover on iIcon");
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(findingMenu.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		viewerPage.click(findingMenu.toggleButton.get(0));
		String resultComment=ellipseComment+" " +"from"+" "+ viewerPage.getAttributeValue(findingMenu.findingsText.get(0), NSGenericConstants.TITLE);
		viewerPage.mouseHover(findingMenu.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.iIconInfo.get(0), NSGenericConstants.TITLE),resultComment, "Verify result comment is visible when  mouse hover action perform", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify edited result comment text is visible on viewbox with italic format for a ellipse annotation");
		findingMenu.editResultComment(findingMenu.resultComment.get(1), editRulerComment);
		viewerPage.assertEquals(viewerPage.getText(findingMenu.resultComment.get(1)), editRulerComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+editRulerComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(findingMenu.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of unformatedText annotationt", "The result comment is in pending state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify entered result comment deleted by clicking on x icon and iIcon should not seen");
		findingMenu.deleteResultComment(findingMenu.resultComment.get(1));
		findingMenu.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(findingMenu.iIconInfo.size(),0, "Verify iIcon after adding result comment", "Verified");
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.assertEquals(findingMenu.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");

	}

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive"})
	public void test18_US1007_TC4583_verifyCommentDeletedForUserDrawnAnnotation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify functionality of deleting comment to user generated findings.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(patientName,  1);

		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify user can add comment to user drawn annotation ");
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 100, 100, 70,70);
		circle.addResultComment(circle.getAllCircles(1).get(0), circleComment);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify group is not created to contain the comment");
		viewerPage.assertEquals((circle.groupInfo.size()),0, "Verify group is not created", "Verified");

		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify entered result comment text is visible on viewbox with italic format for a circle annotation");
		viewerPage.assertEquals(viewerPage.getText(circle.resultComment.get(0)), circleComment, "Verify entered result comment text is visible on viewbox with italic format", "The entered result comment is "+circleComment);
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.resultComment.get(0), NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_FINDING_COLOR, "Verify result comment state is same as state of circle annotation", "The result comment is in aceepted state");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify entered result comment text is visible when mouse hover on iIcon");
		circle.openFindingTableOnBinarySelector(1);
		viewerPage.assertTrue(viewerPage.isElementPresent(circle.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		viewerPage.mouseHover(circle.iIconInfo.get(0));
		viewerPage.assertEquals(viewerPage.getAttributeValue(circle.iIconInfo.get(0), NSGenericConstants.TITLE),circleComment, "Verify result comment is visible when  mouse hover action perform", "Verified");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify entered result comment deleted by clicking on x icon and iIcon should not seen");
		circle.deleteResultComment(circle.resultComment.get(0));
		circle.openFindingTableOnBinarySelector(1);
		viewerPage.assertEquals(circle.iIconInfo.size(),0, "Verify iIcon after adding result comment", "Verified");
		viewerPage.assertEquals(circle.resultComment.size(),0, "Verify result comment is deleted", "The result comment is deleted");

	}

	@Test(groups ={"IE11","Chrome","Edge","US1007","positive"})
	public void test19_US1007_TC4565_verifyTextNotDeletedForMachineDrawnAnnotation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify text annotation(without anchor line) provided by machine cannot be deleted.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectlyUsingID(LIDCPatientID,  1);


		textAnn=new TextAnnotation(driver);
		ellipse = new EllipseAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/10]", "Verify the background of the finding is in grey color when finding is selected on finding toolbar ");
		textAnn.openFindingTableOnBinarySelector(1);
		viewerPage.click(textAnn.toggleButton.get(1));
		viewerPage.assertEquals(viewerPage.getCssValue(textAnn.findings.get(3), NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR,"Verify Background color as Gray for the first finding when it is selected","Verified");

		//verify add comment functionality for findings
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/10]", "Verify deletion of machine generated annotation when user click on delete from A/R toolbar");
		textAnn.selectFindingFromGroupOfTable(1, 2, 1);
		textAnn.selectDeletefromGSPSRadialMenu(1);
		textAnn.selectPreviousfromGSPSRadialMenu(1);
		//		viewerPage.selectPreviousfromGSPSRadialMenu(1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveRejectedGSPS(1, 1), "Checkpoint[3/10]: Verify text annotation is current active rejected GSPS", "Verified");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentRejectedInactive(1, 4, false), "Checkpoint[4/10]: Verify text annotation is current active rejected GSPS", "Verified");
		viewerPage.assertTrue(textAnn.verifyRejectGSPSRadialMenu(), "Checkpoint[5/10] Verify color of Reject icon", "The color of Reject icon changes to Red");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/10]", "Verify state of annotation when user click on Accept/reject");
		textAnn.selectNextfromGSPSRadialMenu(1);
		textAnn.selectAcceptfromGSPSRadialMenu(1);
		textAnn.selectPreviousfromGSPSRadialMenu(1);
		viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 2), "Checkpoint[7/10]: Verify ellipse is current active accepted GSPS", "Verified");
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 4 ,false), "Checkpoint[8/10]: Verify text annotation is active accepted GSPS", "Verified");
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentRejectedInactive(1, 2 ,false), "Checkpoint[9/10]: Verify text annotation is current active accepted GSPS", "The pointer color for accepted finding is green");
		viewerPage.assertTrue(textAnn.verifyAcceptGSPSRadialMenu(), "Checkpoint[10/10]:Verify color of Accept icon", "The color of Accept icon changes to Green");

	}

	//US1075: Show regular A/R toolbar for DICOM-SC result
	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
	public void test20_US1075_TC5312_verifyAddCommentWhenImageCenterNotInViewbox() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Add comment functionlaity of DICOM SC tool bar when image center is not in the view box, its panned and zoomed till the extent of the view box");

		patientPage = new PatientListPage(driver);
		preset= new ViewBoxToolPanel(driver);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(imbio_PatientName,  3);

		textAnn=new TextAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		String zoomValue=preset.getZoomLevelValue(1);

		viewerPage.click(viewerPage.getViewPort(1));
		textAnn.addtext(1,ANNOTATION_TXT_1);

		int beforeX1=viewerPage.getValueOfXCoordinate(textAnn.getTextAnnotation(1, 1));
		int beforeY1=viewerPage.getValueOfYCoordinate(textAnn.getTextAnnotation(1, 1));
		textAnn.selectDeletefromGSPSRadialMenu();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify value of zoom after applying PAN and ZOOM on SC result" );
		preset.changeZoomNumber(1,500);
		viewerPage.selectPanFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewerWithoutHop(0, 0, -470, 450);
		preset.changeZoomValue(2500, 1);
		viewerPage.assertNotEquals(preset.getZoomLevelValue(1), zoomValue, "Checkpoin[2/5]", "Verified zoom value after applying pan and zoom functionality on SC data");

		//after zoom and PAN add comment and verify centre of comment
		textAnn.addtext(1,ANNOTATION_TXT_1);
		viewerPage.assertEquals(preset.getZoomLevelValue(1), zoomValue, "Checkpoin[3/5]", "Image should get fitted in the view box and Zoom to fit on the image should apply ");
		int afterPanX1=viewerPage.getValueOfXCoordinate(textAnn.getTextAnnotation(1, 1));
		int afterPanY1=viewerPage.getValueOfYCoordinate(textAnn.getTextAnnotation(1, 1));

		viewerPage.assertEquals(afterPanX1, beforeX1, "Checkpoint[4/5]", "X coordinate for added text changed after move");
		viewerPage.assertEquals(afterPanY1, beforeY1, "Checkpoint[5/5]", "Y coordinate for added text changed after move");


	}


}
