package com.trn.ns.test.viewer.SC;

import java.awt.AWTException;
import java.io.IOException;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CommentAdditionOPSCTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	private OutputPanel panel;
	private CircleAnnotation circle;
	private ContentSelector contentSelector;
	private TextAnnotation textAnn;
	private HelperClass helper;

	String comment="Sample Comment";
	String editedComment="Sample Comment edited";
	String seriesComment="Series Level comment";
	String seriesEditedComment="Series Level Edited comment";
	String username1 = "abc";


	String imbio_filePath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbio_filePath);
	String pdfResult=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, imbio_filePath);
	String result=DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, imbio_filePath);

	String doeLillyFilePath =Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String doeLillyPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, doeLillyFilePath);

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {

		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username ,password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1323","US1331"})
	public void test01_US1323_TC6856_TC6857_TC6864_US1331_TC6865_TC6958_TC6968_verifyOPAndCSAfterAddingComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if only Dicom SC result is displayed in output panel with the comments added, Text annotation should not be created on adding comment. <br>"+
				"Verify if user is able to add only one comment per slice, message is displayed if user try to add more than one comment on the same slice. <br>"
				+ "Verify if comment is added when Dicom SC result is in pending state, it changes the Dicom SC result and comment state to Accepted. <br>"+
				"Verify if comment is added when Dicom SC result is in Accepted state, comment will also be shown in Accepted state. <br>"
				+"Desktop: Verify if there are no slice level comment added then comment text box will not show '/Slice number/' label. <br>"
				+"Desktop: Verify content selector shows Dicom SC result series and clones generated after adding a slice level comments");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		contentSelector=new ContentSelector(driver);

		panel.assertFalse(panel.verifyResultsAreAccepted(1)&& panel.verifyResultsAreRejected(1), "Checkpoint[1/10]", "Verified that SC result is in Pending state.");
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[2/10]", "Verified that SC result is in pending state in OP.");
		panel.assertTrue(panel.getSliceComments(1).isEmpty(), "Checkpoint[3/10]", "Verified that slice lable is not visible when no slice level comment added for SC result.");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user can add only one comment per slice for SC data" );
		panel.addResultComment(1, comment);
		panel.click(panel.gspsText);
		panel.assertEquals(panel.getText(panel.commentInfo),ViewerPageConstants.ALREADY_FINDING, "Checkpoint[4/10]", "Verified tooltip when comment is already added for SC data");
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[5/10]", "Verified that SC result state change to accepted after adding comment ");

		//verify clone in content selector after adding slice level comment for SC 
		panel.assertTrue(contentSelector.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[6/10]", "Verified clone in Content selector after adding slice level comment on SC data" );

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify SC result in OP after adding slice level comment from viewer." );
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[7/10]", "Verified that SC result in Output panel after adding comment from viewer under accepted tab.");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[8/10]", "Verified added comment in Output panel for SC result");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+panel.getCurrentScrollPositionOfViewbox(1), comment), "checkpoint[9/10]", "Verified added comment text in Output panel for SC result");

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[10/10]", "Verified that no new thumbnail created after adding comment to SC data");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1323","US1331","US1304"})
	public void test02_US1323_TC6858_US1331_TC6924_TC6930_TC6932_US1304_TC7134_verifyLatestCommentCopyAfterAddingComment() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if Dicom SC is selected from content selector loads the latest comment copy always. <br>"+
				"Desktop: Verify if user is able to add slice level comment through viewer only using A/R tool bar. <br>"+
				"Desktop: Verify look and feel of slice level comment added for Dicom SC on output panel. <br>"+
				"Desktop: Verify free text area is visible at the top of the Comment text box  area if slice level comments are added");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		contentSelector=new ContentSelector(driver);
		String resultDescSC = panel.getSeriesDescriptionOverlayText(1);
		int resultCount=contentSelector.getAllResults().size();

		//Verify if Dicom SC is selected from content selector loads the latest comment copy always
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify DICOM SC loading from content selector after adding comment.");
		panel.click(panel.getViewPort(1));
		panel.addResultComment(1, comment+"_1");

		//adding comment on different slices for SC data 
		for(int i=0;i<5;i++)
		{
			panel.scrollDownToSliceUsingKeyboard(i+3);
			panel.addResultComment(1, comment +"_"+(i+2));
		}

		contentSelector.assertTrue(contentSelector.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[1/6]", "Verified new clone copy in Content selector after aading comment");
		contentSelector.assertEquals(contentSelector.getAllResults().size(), resultCount+1, "Checkpoint[2/6]", "Verified entry for SC and 1(new) GSPS result in Content selector");
		contentSelector.selectResultFromSeriesTab(1, resultDescSC);
		contentSelector.assertTrue(panel.resultComment.isEmpty(), "Checkpoint[3/6]", "Verified that SC result loaded without any comment and GSPS");
		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		contentSelector.assertFalse(panel.resultComment.isEmpty(), "Checkpoint[4/6]", "Verified newly created clone loaded successfuly along with the added comment.");

		contentSelector.openAndCloseSeriesTab(false);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify look and feel when multiple Slice level comment along with series level comment is present in OP.");
    	panel.enableFiltersInOutputPanel(true, false, false);
    	panel.mouseHover(panel.findingTileContainers.get(0));
    	panel.click(panel.commentIconOnTiles.get(0));
		panel.compareElementImage(protocolName, panel.commentSection,"Verifying alignment of slice comment and free text area at the bottom of comment text box","test14_Slice_Level_Comment");

	
	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1323"})
	public void test03_US1323_TC6860_TC6861_TC6862_TC6874_verifyTextAnnotationOnSCResultAndNavigationThroughSliceLevelComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if text annotation is created from radial menu options then it shows under finding menu. <br>"+
				"Verify if user is not able to navigate through slice level comments from A/R tool bar. <br>"+
				"Verify if user is able to navigate to text annotation findings created on Dicom SC from A/R tool bar. <br>"+
				"Verify if text annotation is created from radial menu options then it shows under Output panel as finding");

		// Loading the patient on viewer
		panel = new OutputPanel(driver);
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		panel.enableFiltersInOutputPanel(true, true, true);
		int thumbnailCountBefore=panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);

		String textAnnot1="ABC1";
		textAnn=new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1,50,-50,textAnnot1);

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertTrue(panel.thumbnailList.size() >thumbnailCountBefore, "Checkpoint[1/9]", "Verified new thumbnail in OP after adding text annotation on SC result");
		panel.openAndCloseOutputPanel(false);

		panel.click(panel.getViewPort(1));
		panel.openGSPSRadialMenu(1);
		panel.assertEquals(panel.getTextOfFindingFromTable(2), ViewerPageConstants.TEXT_FINDING_NAME, "Checkpoint[3/9]", "Verified finding name for the text annotation in Finding menu");		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user not able to navigate across the slice level comment using A/R toolbar" );
		panel.mouseHover(panel.getViewPort(1));
		panel.selectNextfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[4/9]", "Verify after click on next ,user navigate to next finding i.e. SC result ");
		panel.selectNextfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[5/9]", "Verify after click on next ,user navigate to text annotation.");
		panel.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[6/9]", "Verified that text annotation is current active accepted GSPS on SC result");
		panel.selectPreviousfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.OFF), "Checkpoint[7/9]", "Verify after click on previous user navigate to previous finding i.e. SC result ");
		panel.selectPreviousfromGSPSRadialMenu(1);
		panel.assertTrue(panel.verifyResultAppliedToggle(1,NSGenericConstants.ON), "Checkpoint[8/9]", "Verify after click on previous user navigate to text annotation.");
		panel.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[9/9]", "Verified that text annotation is current active accepted GSPS on SC result");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1323","BVT"})
	public void test04_US1323_TC6863_TC6866_verifyCommentWhenDICOMSCInRejected() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if comment is selected then Delete button shows enabled on A/R tool bar and user is able to able to delete slice level comments. <br>"+
				"Verify if comment is added when Dicom SC is in Rejected state, comment added will also be shown in rejected state");

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		panel.assertFalse(panel.verifyResultsAreAccepted(1)&& panel.verifyResultsAreRejected(1), "Checkpoint[1/6]", "Verified that SC result is in Pending state.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify deletion of comment added by user." );
		panel.addResultComment(1, comment);
		panel.selectDeletefromGSPSRadialMenu(1);
		panel.assertTrue(panel.resultComment.isEmpty(), "Checkpoint[2/6]", "Verified deletion of comment added by user on SC data");

		panel.click(panel.gspsReject);
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[3/6]", "Verified that SC result is in rejected state.");
		panel.addResultComment(1, comment);
		panel.assertTrue(panel.verifyResultsAreRejected(1), "Checkpoint[4/6]", "Verified that SC result is in rejected state only after adding comment");

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[5/6]", "Verified comment in OP after adding on rejected SC data");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+panel.getCurrentScrollPositionOfViewbox(1), comment), "checkpoint[6/6]", "Verified added comment text in Output panel for SC result");

	}

	//US1331: Display and edit DICOM SC comments in output panel

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1331","US2284","F1125","E2E"})
	public void test05_US1331_TC6925_TC6926_TC6960_TC6963_TC6947_US2284_TC9950_verifySeriesLevelAddEditCommentForSCData() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Desktop: Verify if user is not able to add comment from output panel for SC result. <br>"
				+"Desktop: Verify if user edited the slice level or series level comment from output panel after user hit enter, it refreshes output panel window to show updated comment."
				+"Desktop: Verify if free text added from output panel is not displayed on viewer. <br>"
				+"Desktop: Verify if user is able to add free text in free text area described in output panel as series level comment. <br>"
				+"Desktop: Verify if free text area is always visible and if there are no slice level comments then it shows at the top in Comment text box"
				+ "<br> Verify adding, editing slice and series level comments.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user can add series level comment on SC data from Output panel" );
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.addCommentFromOutputPanel(1, comment);
		panel.openAndCloseOutputPanel(false);

		panel.click(panel.getViewPort(1));
		panel.openGSPSRadialMenu(1);
	
		//panel.assertTrue(panel.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/6]", "Verified that SC result is in pending state after adding series level comment from OP.");
		panel.assertTrue(panel.resultComment.isEmpty(), "Checkpoint[2/6]", "Verified that no slice level comment for SC result  on viewer when series level comment is added from OP.");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 0, "Checkpoint[3/6]", "Verified thumbnail in OP under Accepted tab is Zero.");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[4/6]", "Verified thumbnail in OP under Pending tab.");
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+comment, "checkpoint[5/6]", "Verified that series level comment in OP after reload of OP.");	

		//edit series level comment and verify in OP
		panel.editCommentFromOutputPanel(1, 5, comment+"_3", true);
		panel.assertNotEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+comment, "checkpoint[6/6]", "Verified that user can edit series level comment from OP for SC data");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"US1331","US2284","F1125"})
	public void test06_US1331_TC6949_TC6953_TC6955_US2284_TC9950_verifyEditingAndDeletionOfSliceLevelComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Desktop: Verify if user is able to edit slice level comment added from viewer or from Output panel. <br>"
				+"Desktop: Verify slice number label is non-editable and area next to label is editable for user to add comment. <br>"+
				"Desktop: Verify if user deletes or use backspace to cleanup the comment in the edit comment area and hit enter, comment and label both should get deleted"
				+ "<br> Verify adding, editing slice and series level comments.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
	
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);
		textAnn=new TextAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment in OP for SC result" );
		panel.addResultComment(1, comment);
		panel.assertEquals(panel.getText(textAnn.getTextOfSCComment(1).get(0)),comment, "Checkpoint[1/7]", "Verified slice level comment in OP for SC result.");

		panel.click(panel.getViewPort(1));
		panel.editSCResultComment(textAnn.getTextOfSCComment(1).get(0), editedComment);
		panel.assertEquals(panel.getText(textAnn.getTextOfSCComment(1).get(0)),editedComment, "Checkpoint[2/7]", "Verified edited Slice level comment on viewer for SC result.");

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/7]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "checkpoint[4/7]", "Verified edited slice level comment in OP for SC result");	
		panel.assertTrue(panel.verifySliceLabelAndComment(1, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+panel.getCurrentScrollPositionOfViewbox(1), editedComment), "checkpoint[4/7]", "Verified added comment text in Output panel for SC result");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify deletion of Slice level comment when finding panel list is in Expanded mode.");
		panel.deleteSliceComment(1,1, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[5/7]", "Verified that comment deleted from Output panel");
		panel.assertTrue(panel.getSliceComments(1).isEmpty(), "Checkpoint[6/7]", "Verified that slice lable is non-editable and not get deleted after deleting the comment");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify if user deletes or use backspace to cleanup the comment in the edit comment area and hit enter, comment and label both should get deleted");
		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.getSliceComments(1).isEmpty(), "Checkpoint[7/7]", "Verified that slice lable is not visible after reload of viewer page.");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Negative" ,"US1331"})
	public void test07_US1331_TC6966_TC6976_verifySeriesAndSliceCommentForLatestCloneOfSC() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Desktop: Verify in output panel slice level comment and free text are shown from latest clone of SC result. <br>"
				+ "Desktop: Verify if only Dicom SC result is displayed in finding summary irrespective of the virtual clones generated when user add/edit slice/series level comments");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
	
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);
		textAnn=new TextAnnotation(driver);
		panel.waitForViewerpageToLoad(3);

		String scResultName=panel.getSeriesDescriptionOverlayText(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user can add only one comment per slice for SC data" );
		panel.click(panel.getViewPort(1));
		//add slice level comment
		panel.addResultComment(1, comment);
		panel.enableFiltersInOutputPanel(true, false, false);
		//add series level comment from OP
		panel.addCommentFromOutputPanel(1,seriesComment);
		panel.openAndCloseOutputPanel(false);

		//reload viewer page again
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);

		////add slice level comment 
		panel.scrollToImage(1, 116);
		panel.addResultComment(1, comment+"_3");
		panel.enableFiltersInOutputPanel(true, false, false);
		//edit series level comment from OP
		panel.editCommentFromOutputPanel(1,21, seriesEditedComment, true);
		panel.openAndCloseOutputPanel(false);

		//reload viewer page
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);

		//verify checkpoints
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/5]", "Verified SC result under Accepted tab in OP.");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+seriesComment+seriesEditedComment, "checkpoint[2/5]", "Verified first series level comment in OP.");	
		panel.assertEquals(panel.getAllSliceComments(1).get(0), comment, "checkpoint[3/5]", "Verified second series level comment in OP.");	
		panel.assertEquals(panel.getAllSliceComments(1).get(1),comment+"_3" , "checkpoint[4/5]", "Verified that edited series level comment is visible in OP .");	
		panel.openAndCloseOutputPanel(false);

		//verify SC result entry in finding menu
		panel.openFindingTableOnBinarySelector(1);
		panel.assertEquals(panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0), scResultName, "Checkpoint[5/5]", "Verified that only one entry in finding menu for the SC data.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1862"})
	public void test08_DE1862_TC7513_verifyDeletedCommentOnViewer() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that deleted comment from the viewer is not visible in the output panel for the SC data.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);

		int imageNo=panel.getCurrentScrollPositionOfViewbox(1);

		//add comment for SC and verify in Output panel
		panel.addResultComment(1, seriesComment);
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[1/13]", "Verified that SC result state change to accepted after adding series level comment ");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[2/13]", "Verified that SC result in Output panel after adding comment from viewer under accepted tab.");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[3/13]", "Verified added comment in Output panel for SC result");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+imageNo, seriesComment), "checkpoint[4/13]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		//verify comment for SC on viewer page
		panel.assertEquals(panel.getText(panel.scResultComment.get(0)),seriesComment, "Checkpoint[6/13]", "Verified series level comment on viewer page.");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[7/13]", "Verified that SC result state is in accepted only.");

		//delete comment from viewer and verify on viewer after deleting
		panel.click(panel.scResultComment.get(0));
		panel.click(panel.gspsDelete);
		panel.click(panel.getViewPort(1));
		panel.assertTrue(panel.scResultComment.isEmpty(), "Checkpoint[8/13]", "Verified that series level comment deleted from viewer.");

		//verify in deleted comment in Output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.getCommentAddedIcon().isEmpty(), "Checkpoint[9/13]", "Verified deleted series level comment in Output panel.");
		panel.openAndCloseOutputPanel(false);

		//select text annotation and verify in Output panel
		panel.click(panel.getViewPort(1));
		textAnn=new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, -100, 50, comment);

		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertTrue(panel.getSliceComments(1).isEmpty(),"checkpoint[10/13]", "Verified in Output panel that no comment visible after creating text annotation.");
		panel.openAndCloseOutputPanel(false);

		//delete text annotation
		panel.click(panel.getWindowCenterValueOverlay(1));
		textAnn.selectFindingFromTable(2);
		textAnn.deleteSelectedTextAnnotation();
		panel.assertFalse(textAnn.isTextAnnotationPresent(1), "Checkpoint[12/13]", "Verified that no text annotation is present on viewer.");

		//verify text annotation in Output panel after delete
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[13/13]", "Verified that text annotation thumbnail not visible in Output panel after deleting from Viewer.");
		panel.openAndCloseOutputPanel(false);

	}

	//DE1779: [Automation]: Comment added to SC result is not visible in OP after adding comment to user drawn annotation.
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1779","DE1889"})
	public void test09_DE1779_TC7366_DE1889_TC7532_verifySeriesCommentAfterAddingCommentToAnnotation() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the comment added to SC result is visible in the Output Panel after adding comment to user drawn annotation.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);

		int imageNo=panel.getCurrentScrollPositionOfViewbox(1);

		//add comment for SC and verify in Output panel
		panel.addResultComment(1, seriesComment);
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[1/10]", "Verified that SC result state change to accepted after adding series level comment ");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[2/10]", "Verified that SC result in Output panel after adding comment from viewer under accepted tab.");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[3/10]", "Verified added comment in Output panel for SC result");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+imageNo, seriesComment), "checkpoint[4/10]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		//verify comment for SC on viewer page
		panel.assertEquals(panel.getText(panel.scResultComment.get(0)),seriesComment, "Checkpoint[6/10]", "Verified series level comment on viewer page.");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[7/10]", "Verified that SC result state is in accepted only.");

		//draw text annotation
		textAnn=new TextAnnotation(driver);
		textAnn.selectTextArrowFromQuickToolbar(3);
		textAnn.drawText(3, -100, 50, comment);
		textAnn.closeNotification();
		textAnn.addResultComment(3, editedComment);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+editedComment, "checkpoint[8/10]", "Verified in Output panel that  comment visible after adding comment to text annotation.");
		panel.assertTrue(panel.verifySliceLabelAndComment(2, ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+" "+imageNo, seriesComment), "checkpoint[9/10]", "Verified in Output panel that series level comment visible after creating text annotation as well.");
//		panel.assertEquals(panel.getText(panel.findingsNameList.get(0)),ViewerPageConstants.FINDING_NAME+": "+ ViewerPageConstants.TEXT_FINDING_NAME, "Checkpoint[10/10]", "Verified finding name for text annotation in Output panel.");


	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1779","DE1889"})
	public void test10_DE1779_TC7367_TC7387_DE1889_TC7532_verifyDICOMSCCommentAfterViewerReload() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the DICOM SC comments in output panel should be same as what is loaded into viewer.<br>"+
				"Verify that if DICOM SC is not loaded into viewer, output panel should show latest comment.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		panel = new OutputPanel(driver);

		textAnn=new TextAnnotation(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment in OP for SC result" );
		panel.addResultComment(1, comment);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/7]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), comment, "checkpoint[2/7]", "Verified added comment text in Output panel for SC result");

		//reload viewer page and edit comment
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		panel.waitForTimePeriod(2000);

		panel.editSCResultComment(textAnn.getTextOfSCComment(1).get(0), editedComment);
		panel.assertEquals(panel.getText(textAnn.getTextOfSCComment(1).get(0)),editedComment, "Checkpoint[3/7]", "Verified edited Slice level comment on viewer for SC result.");

		//verify comment in OP after editing
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[4/7]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), editedComment, "checkpoint[5/7]", "Verified edited comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		contentSelector=new ContentSelector(driver);
		contentSelector.selectResultFromSeriesTab(1, pdfResult);
		contentSelector.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/7]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), editedComment, "checkpoint[7/7]", "Verified edited comment text in Output panel for SC result");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE1779","DE1889"})
	public void test11_DE1779_TC7388_DE1889_TC7532_verifyCommentWhenMultipleVersionOfDICOMSCLoadedOnViewer() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that if multiple versions of DICOM SC are loaded into the viewer grid, output panel should always show the comment on the latest version. ");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(imbio_PatientName);
		patientPage.clickOntheFirstStudy();
		panel = new OutputPanel(driver);
		panel.waitForViewerpageToLoad(3);

		textAnn=new TextAnnotation(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment in OP for SC result" );
		panel.addResultComment(1, comment);

		contentSelector=new ContentSelector(driver);
		contentSelector.selectResultFromSeriesTab(1, result);
		panel.addResultComment(1, editedComment);

		contentSelector.selectResultFromSeriesTab(1, result);
		panel.addResultComment(1, seriesComment);

		contentSelector.selectResultFromSeriesTab(1, result);
		contentSelector.openAndCloseSeriesTab(false);
		panel.addResultComment(1, seriesEditedComment);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/8]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), seriesEditedComment, "checkpoint[2/8]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		contentSelector.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/8]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), comment, "checkpoint[4/8]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");
		contentSelector.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[5/8]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), editedComment, "checkpoint[6/8]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

		contentSelector.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3");
		contentSelector.openAndCloseSeriesTab(false);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[7/8]", "Verified thumbnail for SC result under Accepted tab.");
		panel.assertEquals(panel.getAllSliceComments(1).get(0), seriesComment, "checkpoint[8/8]", "Verified added comment text in Output panel for SC result");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1911", "Positive"})
	public void test12_DE1911_TC7626_verifyUserAbleToAddCommentForAnnotationAfterSliceLevelComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify adding comment to annotation after adding slice level comment for SC data.");

		String mytext = "This is a slice level comment";

		// Loading the patient on viewer
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;

		circle = new CircleAnnotation(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );

		//Adding Slice Level comment
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify that user is able to add comment to annotation after adding Slice level comment.");
		panel.click(panel.getViewPort(1));
		panel.addResultComment(1, mytext+"_1");

		//Draw a Circle on Viewer page.
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);	

		circle.closingConflictMsg();

		circle.selectCircle(1, 1);
		panel.addResultComment(1, comment+"_1");


		//adding slice level comment on different slices for SC data

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify that user is able to add slice level comment to different slice as well.");

		panel.scrollDownToSliceUsingKeyboard(3);
		panel.addResultComment(1, mytext +"_"+(2));

		panel.assertTrue(panel.isElementPresent(panel.getTextOfSCComment(1).get(0)),"Verifying that slice level comment added by user is present or not", "Slice level comment is present.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2006","US2284","F1125","E2E"})
	public void test13_DE2006_TC8215_DR2107_TC8602_TC8603_US2284_TC9950_verifySeriesCommentRetained() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Updated series level comment is displayed in Output panel after closing and re-opening output panel"
				+ "<br> Verify that deleted series level and Slice level comments are not getting retained in Output panel after reloading the viewer[happy Path]"
				+ "<br> Verify that edited series level and Slice level comments are getting retained in Output panel after reloading the viewer"
				+ "<br> Verify adding, editing slice and series level comments.");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		textAnn=new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open output panel and add series level comment from comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.addCommentFromOutputPanel(1, seriesComment);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+seriesComment,"Checkpoint[1/5]","Added Comment \"Comment1\" should be shown  in output panel");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update existing series level comment from output panel and hit enter EX: \"Comment2\" ");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.editCommentFromOutputPanel(1,seriesComment.length()+1, seriesEditedComment,true);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+seriesComment+seriesEditedComment,"Checkpoint[2/5]","Updated Comment should be shown  in output panel on reopen");
		panel.openAndCloseOutputPanel(false);

//		Right now on viewer reload the delete series comment is not working hence commented it for now
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+seriesComment+seriesEditedComment,"Checkpoint[3/5]","Updated series Comment should be shown  in output panel on reload");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete existing series level comment from output panel and hit enter ");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.deleteCommentFromOutputPanel(1, true);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(),"Checkpoint[4/5]","Deleted comment should not be shown  in output panel on reopen");
		panel.openAndCloseOutputPanel(false);

		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(),"Checkpoint[5/5]","Deleted comment should not be shown  in output panel on reload");
		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2006"})
	public void test14_DE2006_TC8241_DR2107_TC8602_TC8603_verifySliceCommentRetained() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Updated slice level comment is displayed in Output panel after closing and re-opening output panel"
				+ "<br> Verify that deleted series level and Slice level comments are not getting retained in Output panel after reloading the viewer[happy Path]"
				+ "<br> Verify that edited series level and Slice level comments are getting retained in Output panel after reloading the viewer");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		textAnn=new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;

		panel.scrollDownToSliceUsingKeyboard(3);
		panel.addResultComment(1, comment);

		int currentImage = panel.getCurrentScrollPositionOfViewbox(1);
		String sliceLabel = ViewerPageConstants.VIEWER_SLICE_TEXT.replace(":", "")+" "+currentImage;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment), "Checkpoint[1/5]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update existing slice level comment from output panel and hit enter EX: \"Comment2\" ");
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.editSliceComment(1, sliceLabel,true, editedComment);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment+editedComment), "Checkpoint[2/5]", "Verifying the Slice Label and comment for SC result is persisted on reopen of output panel");
		panel.openAndCloseOutputPanel(false);
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment+editedComment), "Checkpoint[3/5]", "Verifying the Slice Label and comment for SC result is persisted on reload");
		panel.openAndCloseOutputPanel(false);
		

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete existing slice level comment from output panel and hit enter ");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.deleteSliceComment(1,1, true);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.getAllSliceLabelsAndComments(1).isEmpty(),"Checkpoint[4/5]","Comment should not be shown  in output panel after reload");
		panel.openAndCloseOutputPanel(false);
		
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.getAllSliceLabelsAndComments(1).isEmpty(),"Checkpoint[5/5]","Comment should not be shown  in output panel after viewer reload");
		panel.openAndCloseOutputPanel(false);
	

	}

	//DE2127: State of SC result not change after updating the pending slice level comment .
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2127"})
	public void test15_DE2127_TC8488_verifySCStateAfterUpdatingSliceLevelCommentFromOP() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of SC is updated when a slice level comment is updated from Output Panel.");

		// Loading the patient on viewer
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
	    patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		textAnn=new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;

		panel.scrollDownToSliceUsingKeyboard(3);
		panel.addResultComment(1, comment);

		int currentImage = panel.getCurrentScrollPositionOfViewbox(1);
		String sliceLabel = ViewerPageConstants.VIEWER_SLICE_TEXT.replace(":", "")+" "+currentImage;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open Output Panel  and verify slice comment section");
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/5]", "Verified state of SC result as Accepted under Output panel.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment), "Checkpoint[2/5]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change state of SC to Pending and then update existing slice level comment from output panel and hit enter EX: \"Comment2\" ");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectAcceptfromGSPSRadialMenu(1);
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.editSliceComment(1, sliceLabel,true, editedComment);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify updated slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/5]", "Verified that SC result is under Accepted tab in Output panel.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment+editedComment), "Checkpoint[4/5]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		panel.mouseHover(panel.getGSPSHoverContainer(1));	
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[5/5]", "Verified Accepted GSPS radial menu on viewer.");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2127"})
	public void test16_DE2127_TC8489_verifyUpdatedSliceCommentAfterReload() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of SC is updated when a slice level comment is updated from Output Panel for a series not loaded in Viewer.");

		// Loading the patient on viewer
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
	    patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		textAnn=new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;

		contentSelector=new ContentSelector(driver);
		String seriesToSelect=contentSelector.getSeriesDescriptionOverlayText(3);
		panel.scrollDownToSliceUsingKeyboard(3);
		panel.addResultComment(1, comment);

		int currentImage = panel.getCurrentScrollPositionOfViewbox(1);
		String sliceLabel = ViewerPageConstants.VIEWER_SLICE_TEXT.replace(":", "")+" "+currentImage;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Output Panel  and verify slice comment section");
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/6]", "Verified that SC is accepted In Output panel.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment), "Checkpoint[2/6]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update existing slice level comment from output panel for SC when SC result not loaded on viewer and hit enter EX: \"Comment2\" ");
		panel.click(panel.getViewPort(1));
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.selectAcceptfromGSPSRadialMenu(1);
		
		contentSelector.selectSeriesFromSeriesTab(1, seriesToSelect);
		contentSelector.openAndCloseSeriesTab(false);
		
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.editSliceComment(1, sliceLabel,true, editedComment);
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify slice comment section");
		panel.mouseHover(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/6]", "Verified that SC is accepted In Output panel.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment+editedComment), "Checkpoint[4/6]", "Verifying the Slice Label and comment for SC result");

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[5/6]", "Verified state of SC as Accepted on viewer after reload");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/6]", "Verified state of SC as Accepted in Output panel after reload");
		panel.openAndCloseOutputPanel(false);
		

	}

	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2127"})
	public void test17_DE2127_TC8490_verifySCStateAfterUpdatingSliceLevelCommentFromViewer() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the state of SC is updated when a slice level comment is updated from Viewer.");

		// Loading the patient on viewer
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
	    patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);
		textAnn=new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;

		contentSelector=new ContentSelector(driver);
		panel.scrollDownToSliceUsingKeyboard(3);
		panel.addResultComment(1, comment);

		int currentImage = panel.getCurrentScrollPositionOfViewbox(1);
		String sliceLabel = ViewerPageConstants.VIEWER_SLICE_TEXT.replace(":", "")+" "+currentImage;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open Output Panel  and verify slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/8]", "Verified state of SC as Accepted in Output panel after adding Slice level comment.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, comment), "Checkpoint[2/8]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change state of SC to pending and Verify count in Output panel. ");
		panel.click(panel.getViewPort(1));
		panel.selectAcceptfromGSPSRadialMenu(1);
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), 2, "Checkpoint[3/8]", "Verified that SC result is under Accepted tab in Output panel.");
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update existing pending slice level comment from viewer and hit enter EX: \"Comment2\" ");
		panel.editSCResultComment(textAnn.getTextOfSCComment(1).get(0), editedComment);
		panel.assertEquals(panel.getText(textAnn.getTextOfSCComment(1).get(0)),editedComment, "Checkpoint[4/8]", "Verified edited Slice level comment on viewer for SC result.");
		panel.openAndCloseOutputPanel(false);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open Output Panel  and verify updated slice comment section");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[5/8]", "Verified state of SC as Accepted in Output panel.");
		panel.assertTrue(panel.verifySliceLabelAndComment(1, sliceLabel, editedComment), "Checkpoint[6/8]", "Verifying the Slice Label and comment for SC result");
		panel.openAndCloseOutputPanel(false);

		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(imbio_PatientName, 1, 3);
		panel.waitForAllChangesToLoad();
		panel.mouseHover(panel.getGSPSHoverContainer(1));
		panel.assertTrue(panel.verifyAcceptGSPSRadialMenu(), "Checkpoint[7/8]", "Verified state of SC as Accepted On viewer after reloads.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[8/8]", "Verified state of SC as Accepted in Output panel after reload.");
		panel.openAndCloseOutputPanel(false);
		
}
	
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2111"})
	public void test18_DE2111_TC8613_verifySCStateForSeriesComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the state of DICOM SC is not changed to Accepted after reload when series level comment added .");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open output panel and add series level comment from comment section");
		panel.enableFiltersInOutputPanel(false, false, true);

		int thumbnailCount=panel.thumbnailList.size();
		panel.addCommentFromOutputPanel(1,seriesComment);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint-[1/8]", "Verified that SC result is in Pending state after adding the comment.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint-[2/8]", "Verified that SC result is in Pending state after reloading the output panel.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update existing series level comment from output panel and hit enter EX: \"Comment2\" ");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.editCommentFromOutputPanel(1,seriesComment.length()+1, seriesEditedComment,true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint-[3/8]", "Verified that SC result is in Pending state after editing the comment at output panel.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel( false, false,true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint[4/8]", "Verified that SC result is in Pending state after editing the series level comment and reloading.");
	
		panel.enableFiltersInOutputPanel(true, false,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[5/8]", "Verified that there are no findings in accepted state after editing a series level comment");
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete existing series level comment from output panel and hit enter ");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.deleteCommentFromOutputPanel(1, true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint[6/8]", "Verified that SC result is in Pending state after deleting the series level comment.");
		
		panel.enableFiltersInOutputPanel(true, false,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[7/8]", "Verified that there are no findings in accepted state after deleting a series level comment");
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint[8/8]", "Verified that SC result is in Pending state after deleting the series level comment and reloading.");

	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","Positive" ,"DE2181"})
	public void test19_DE2181_TC8719_verifySCStateForSeriesCommentWhenResultIsNotLoaded() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that series level comment is visible in output panel after reload .");

		// Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 3);

		panel=new OutputPanel(driver) ;
		String seriesName=panel.getSeriesDescriptionOverlayText(3);

		contentSelector=new ContentSelector(driver);
		contentSelector.selectSeriesFromSeriesTab(1, seriesName);
		contentSelector.openAndCloseSeriesTab(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Open output panel and add series level comment from comment section");
		panel.enableFiltersInOutputPanel(false, false, true);
		int thumbnailCount=panel.thumbnailList.size();
		panel.addCommentFromOutputPanel(1,seriesComment);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint[1/2]", "Verified that SC result is in Pending state after adding the comment.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Re-Open Output Panel  and verify comment section");
		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(), thumbnailCount, "Checkpoint[2/2]", "Verified that SC result is in Pending state after reloading the output panel.");
		

	}


	@AfterMethod(alwaysRun=true)
	public void updateDB() throws SQLException, IOException, InterruptedException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"),NSDBDatabaseConstants.USERFEEDBACKTABLE); 
		db.truncateTable(Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName"),ActionLogConstant.USER_ACTION_TABLE);
		db.deleteUser(username1);

		if(db.getDefaultSlicePosition().equals(".3")) {
			db.updateDefaultSlicePosition(".5");
			db.resetIISPostDBChanges();
		}
	}

}