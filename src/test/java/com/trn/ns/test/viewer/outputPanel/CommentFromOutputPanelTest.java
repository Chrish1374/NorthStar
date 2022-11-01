package com.trn.ns.test.viewer.outputPanel;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class CommentFromOutputPanelTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private OutputPanel panel;
	private SimpleLine line;
	private CircleAnnotation circle;
	private PointAnnotation point;
	private HelperClass helper;
	private ViewerLayout layout;

	String comment="Sample Comment";


	// Get Patient Name
	String liver9FilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);

	String aidoc_FilePath = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidoc_FilePath);

	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^MultiText^MultiSeries_filepath");
	String multiTextPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String gspsFilePath =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath);
	String secondSeries=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, gspsFilePath);
	String firstSeries=DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, gspsFilePath);

	String Imbio_Texture_FilePath = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String Imbio_Texture_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Imbio_Texture_FilePath);

	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	String resultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, filePath1);
	
	private ContentSelector cs;
	private MeasurementWithUnit lineWithUnit;
	private EllipseAnnotation ellipse;

	private final String pointComment="Point comment";
	private final String editPointComment="Edited Point comment";
	private final String rulerComment="Ruler comment";
	private final String editRulerComment="Edited Ruler comment";
	private final String ellipseComment="Ellipse comment";
	private final String editEllipseComment="Edited Ellipse comment";
	private final String circleComment="Circle comment";
	private final String editCicleComment="Edited Circle comment";


	String text = "Sample comments";
	String editedComment = "Sample edited comments";
	String val = "ABC";
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1280", "Positive","DE1672","BVT","US2284","F1125","E2E"})
	public void test01_US1280_TC6629_TC6630_TC6698_TC6711_TC6644_US2284_TC9757_TC9924_verifyCommentAdditionDeletionCloneInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel for the edit comment box in Output panel."
				+ "<br> Verify deletion of comment added from Output panel"
				+ "<br> Verify state of finding change to accepted after editing the comment from Output panel.."
				+ "<br> Verify cloned entry is generated after adding comment from Output panel when result is in non-editable mode"
				+ "<br> Verify comment in finding menu when it is added or edited from Output panel. "
				+ "<br> Verify adding, viewing and editing comment for a finding from Output Panel."
				+ "<br> Verify adding comments from Output Panel to Pending and Rejected findings.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);
	
		panel=new OutputPanel(driver);
		circle = new CircleAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);

		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(), "checkpoint[1/23]", "Verifying the comments box is displayed in output panel");
		List<String> cloneEntries = cs.getAllResults();


		panel.enableFiltersInOutputPanel(true,true,true);
		int commentsArea = 1;

		for(int i =1;i<=commentsArea;i++) {
			panel.addCommentFromOutputPanel(i, text+"_"+i);
			panel.assertEquals(panel.getCommentText(i).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[4."+(i)+"/23]", "Verifying the comment is displayed along with series description");

		}
		panel.openAndCloseOutputPanel(false);
		cs.assertEquals(cs.getAllResults().size(), cloneEntries.size()+1, "Checkpoint[6/23]", "Verifying that after adding the comment - clone is generated");
		panel.click(panel.getViewPort(1));		
		int findings = panel.getTotalAcceptedFindings(1);
		for(int i =1;i<=findings;i++) {			
			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i),"Checkpoint[7."+(i)+"/23]","Verifying the i Icon is displayed in finding menu");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i,text+"_"+i),"Checkpoint[8."+(i)+"/23]","Verifying the tooltip is same as comment for i Icon");
			panel.selectFindingFromTable(1,i);
			panel.assertEquals(circle.getText(circle.getTextCommentsforAllCircles(1).get(i-1)), text+"_"+i,"Checkpoint[9."+(i)+"/23]", "Verifying the comment for finding in viewer is displayed");
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, i), "Checkpoint[10."+(i)+"/23]", "verifying the finding is turned in green");
			

		}


		panel.enableFiltersInOutputPanel(true,false,false);
		for(int i =1;i<=commentsArea;i++) {

			panel.deleteCommentFromOutputPanel(i,true);
			panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(),"Checkpoint[12."+(i)+"/23]", "Comment is not displayed when finding is in collapsed mode");
			panel.assertEquals(panel.getCommentText(i),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[13/23]", "Verifying the comment when finding is expanded");

		}	
		panel.openAndCloseOutputPanel(false);
		panel.click(panel.getViewPort(1));
		for(int i =1;i<=findings;i++) {

			panel.assertFalse(panel.verifyCommentPresenceInFindingMenu(1,i),"Checkpoint[14."+(i)+"/23]","Verifying there is no more iIcon is displayed in finding menu");
			panel.selectFindingFromTable(1,i);
			panel.assertTrue(circle.getTextCommentsforAllCircles(1).isEmpty(),"Checkpoint[15."+(i)+"/23]", "Verifying there is no comment displayed on viewer");
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, i), "Checkpoint[16."+(i)+"/23]", "verifying that no state changed after deleting the comment");
			

		}

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		panel.enableFiltersInOutputPanel(false,true,false);		

		for(int i =1;i<=commentsArea;i++) {
			panel.addCommentFromOutputPanel(i, text+"_"+i);
			panel.assertEquals(panel.getCommentText(i).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[18."+(i)+"/23]", "Verifying the text comment displayed when finding is in collapse mode");

		}
		panel.openAndCloseOutputPanel(false);
		panel.click(panel.getViewPort(1));
		for(int i =1;i<=findings;i++) {

			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i),"Checkpoint[20."+(i)+"/23]","Verifying the finding menu");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i,text+"_"+i),"Checkpoint[21."+(i)+"/23]","Verifying the tooltip for iIcon");
			panel.selectFindingFromTable(1,i);
			panel.assertEquals(circle.getText(circle.getTextCommentsforAllCircles(1).get(i-1)), text+"_"+i,"Checkpoint[22."+(i)+"/23]", "Verifying the comment is displayed for finding");
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, i), "Checkpoint[23."+(i+1)+"/23]", "Verifying the finding state is same when finding is rejected");	

		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1280", "Positive","BVT"})
	public void test02_US1280_TC6629_TC6630_verifyCommentAdditionFromViewerThenVerificationInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify look and feel for the edit comment box in Output panel."
				+ "<br> Verify deletion of comment added from Output panel.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);

		panel=new OutputPanel(driver); ;

		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(), "checkpoint[1/23]", "Verifying the comments box is displayed in output panel");

		panel.openAndCloseOutputPanel(false);
		panel.click(panel.getViewPort(1));

		circle = new CircleAnnotation(driver);

		int findings = panel.getFindingsCountFromFindingTable(1);		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Adding comments to findings from viewer");
		for(int i=findings;i>0;i--) {

			panel.selectFindingFromTable(1,i);
			panel.addResultComment(circle.getAllCircles(1).get(0), text+"_"+(i-1));

		}

		panel.enableFiltersInOutputPanel(true,false,false);
		int commentsArea = panel.getCommentAddedIcon().size();
		for(int i =0;i<commentsArea;i++) 
			panel.assertEquals(panel.getCommentText(i+1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[2."+(i+1)+"/7]", "Verifying the comments text added using viewer is displayed in output panel");
		panel.openAndCloseOutputPanel(false);		
	
		panel.enableFiltersInOutputPanel(true,false,false);
		for(int i =1;i<=commentsArea;i++) {
			panel.deleteCommentFromOutputPanel(i, true);
			panel.assertEquals(panel.getCommentText(i),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[13/23]", "Verifying the comment when finding is expanded");

		}
		panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(),"Checkpoint[12/23]", "Comment is not displayed when finding is in collapsed mode");
		panel.openAndCloseOutputPanel(false);
	
		panel.click(panel.getViewPort(1));
		for(int i =0;i<findings;i++) {			
			panel.assertFalse(panel.verifyCommentPresenceInFindingMenu(1,i+1),"Checkpoint[6."+(i+1)+"/7]","Verifying the comments icon is not displayed in finding menu");
			panel.selectFindingFromTable(1,i+1);
			panel.assertTrue(panel.resultComment.isEmpty(),"Checkpoint[7."+(i+1)+"/7]", "verifying the comment is also not displayed in viewer");

		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1280", "Positive","US2284","F1125"})
	public void test04_US1280_TC6632_TC6675_TC6671_US2284_TC9757_verifyEditingInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Editbox functionality when click is perform outside the editbox in Output panel while entering any text."
				+ "<br> Verify no cloned entry is generated after editing comment from Output panel when result is in editable mode."
				+ "<br> Verify comment in finding menu when it is added or edited from Output panel."
				+ "<br> Verify adding, viewing and editing comment for a finding from Output Panel.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);
		panel=new OutputPanel(driver); ;
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);


		int findings = panel.getFindingsCountFromFindingTable(1);	
		for(int i=findings;i>0;i--) {			
			panel.selectFindingFromTable(1,i);
			panel.addResultComment(circle.getAllCircles(1).get(0), text+"_"+(i-1));

		}

		panel.enableFiltersInOutputPanel(true,false,false);
		int commentsArea = panel.getCommentAddedIcon().size();
		for(int i =0;i<commentsArea;i++) 
			panel.assertEquals(panel.getCommentText(i+1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[2."+(i)+"/7]", "Verifying the comments text added using viewer is displayed in output panel");
		panel.openAndCloseOutputPanel(false);	
		List<String> cloneEntries = cs.getAllResults();
		panel.enableFiltersInOutputPanel(true,false,false);
		for(int i=commentsArea;i>0;i--)
			panel.editCommentFromOutputPanel(2, 7, "edited ", true);
	
		for(int i =0;i<commentsArea;i++) 
			panel.assertEquals(panel.getCommentText(i+1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editedComment+"_"+i,"Checkpoint[2."+(i)+"/7]", "Verifying the comments text added using viewer is displayed in output panel");
		
		panel.openAndCloseOutputPanel(false);		
		cs.assertEquals(cs.getAllResults().size(), cloneEntries.size(), "Checkpoint[4]", "");
		panel.click(panel.getViewPort(1));
		
		for(int i =0;i<findings;i++) {
			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i+1),"Checkpoint[4/6]","Verifying the finding menu for added comment");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i+1,editedComment+"_"+i),"Checkpoint[5/6]","Verifying the tooltip in finding menu");
			panel.selectFindingFromTable(1,i+1);
			panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment+"_"+i,"Checkpoint[6."+(i+1)+"/6]", "Verifying the edited comment in viewer");

		}


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1280", "Positive"})
	public void test05_US1280_TC6633_TC6635_verifyMultilineCommentScrollbarInOutputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Shift+Enter fucntionality on comment text in Output panel."
				+ "<br> Verify text scroll in editbox when it overflows.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);

		panel=new OutputPanel(driver); ;
		circle = new CircleAnnotation(driver);

		String[] multilineComments =new String[4];
		for(int i = 0;i<4 ;i++)
			multilineComments[i] = text+i;

		int findings = panel.getFindingsCountFromFindingTable(1);		
		for(int i=findings;i>0;i--) {			
			panel.selectFindingFromTable(1,i);
			panel.addResultComment(circle.getAllCircles(1).get(0), text+"_"+(i-1));

		}

		panel.enableFiltersInOutputPanel(true,false,false);
		int commentsArea = panel.getCommentAddedIcon().size();
		for(int i =0;i<commentsArea;i++) 
			panel.assertEquals(panel.getCommentText(i+1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[2."+(i+1)+"/7]", "Verifying the comments text added using viewer is displayed in output panel");
		panel.openAndCloseOutputPanel(false);		
	
		panel.enableFiltersInOutputPanel(true,false,false);

		for(int i=commentsArea;i>0;i--)
			panel.editCommentFromOutputPanel(2, true,multilineComments);

		
		for(int i =0;i<commentsArea;i++) {
			List<String> comment = panel.getMultiLineCommenEntriesInOP(i+1);
			List<String> expectedComments = new ArrayList<String>();
			
			expectedComments.add(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT);
			expectedComments.add(text+"_"+(i));
			expectedComments.addAll(Arrays.asList(multilineComments));
			
			panel.assertEquals(comment.size(),expectedComments.size(),"Checkpoint[4."+(i+1)+"/8]", "Verifying the multiline comments size");
		
		}

		panel.openAndCloseOutputPanel(false);		

		panel.click(panel.getViewPort(1));
		for(int i =0;i<findings;i++) {
			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i+1),"Checkpoint[7."+(i+1)+"/8]","Verifying the iIcon presence in finding menu");
			List<WebElement> commentsFromCircle = circle.getTextCommentsforAllCircles(1);
			panel.selectFindingFromTable(1,i+1);
			for(int j = 1 ; j<commentsFromCircle.size();j++) {
				panel.assertEquals(panel.getText(commentsFromCircle.get(0)),multilineComments[j-1],"Checkpoint[8."+(i+1)+"/8]", "Verifying the multiline in comments in viewer");

			}


		}


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1280", "Positive"})
	public void test06_US1280_TC6637_verifyEditingCommentInOutputPanelWhenBrowserResized() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify editing comment from Output panel while resizing the browser window.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);

		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		circle = new CircleAnnotation(driver);

		int findings = panel.getFindingsCountFromFindingTable(1);		
		for(int i=findings;i>0;i--) {

			panel.selectFindingFromTable(1,i);
			panel.addResultComment(circle.getAllCircles(1).get(0), text+"_"+(i-1));		
		}

		

		Dimension dimension = new Dimension(900, 900);
		String parentWindow = panel.getCurrentWindowID();

		panel.enableFiltersInOutputPanel(true,false,false);
		for(int i =0;i<findings;i++) {
			panel.editCommentFromOutputPanel((i+1), 7, "edited ", false);

			if(i%2==0)
				panel.setWindowSize(parentWindow, dimension);
			else
				panel.maximizeWindow();
		}
		panel.openAndCloseOutputPanel(false);		
		panel.maximizeWindow();
		
		panel.click(panel.getViewPort(1));
		for(int i =0;i<findings;i++) {
			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i+1),"Checkpoint[3."+(i+1)+"/5]","Verifying the iIcon in menu");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i+1,text+"_"+i),"Checkpoint[4."+(i+1)+"/5]","Verifying the tooltip in finding menu");
			panel.selectFindingFromTable(1,i+1);
			panel.assertEquals(panel.getText(panel.resultComment.get(0)), text+"_"+i,"Checkpoint[5."+(i+1)+"/5]", "Verifying the comments in output panel");

		}


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1304", "Positive"})
	public void test07_US1304_TC7137_verifyTextIsDisplayedInSameRow() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the 'Text:' label and its texts are displayed in the same row at output panel for user as well as machine drawn annotations.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(multiTextPatientName,  1);
		
		panel=new OutputPanel(driver); ;

		TextAnnotation textAnn = new TextAnnotation(driver);
		text = textAnn.getTextFromTextAnnotation(1, 1);

		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertEquals(panel.getTextLabelValFromThumbnail(1), ViewerPageConstants.OUTPUT_PANEL_TEXT_LABEL+" "+text, "Checkpoint[1/3]", "Verifying the Text label is displayed for machine data");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName,1);

		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 50, 50, val);
		text = textAnn.getTextFromTextAnnotation(1, 1);
		textAnn.closeNotification();
		
		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertEquals(panel.getTextLabelValFromThumbnail(1), ViewerPageConstants.OUTPUT_PANEL_TEXT_LABEL+" "+text, "Checkpoint[2/3]", "Verifying the Text label is displayed for User created data");
		panel.assertEquals(text, val, "Checkpoint[3/3]", "verify the text displayed");

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1304", "Positive"})
	public void test08_US1304_TC7143_TC7145_TC7146_TC7183_TC7147_verifyCommentsInSameRow() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the 'Comment:' label and its comments are displayed in the same row at output panel for user as well as machine drawn annotations."
				+ "<br> Verify that the dashed area covers the comment label and the respective comment text box on the output panel."
				+ "<br> Verify that the comment label is not editable on the output panel."
				+ "<br> Verify that 'Comment:' label gets hidden when cursor is moved to comment box on output panel."
				+ "<br> Verify that the comment label and comment texts are displayed in the same row when user collapses/expands the finding on output panel.");

		// Loading the patient on viewer

		// verifying the comments for machine data
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(multiTextPatientName,  1);
	
		panel=new OutputPanel(driver); ;

		TextAnnotation textAnn = new TextAnnotation(driver);

		panel.enableFiltersInOutputPanel(true,true,true);		
		panel.assertTrue(panel.addCommentFromOutputPanel(1, val),"Checkpoint[2/14]","Adding comments and validating that comment label doesn't get displayed during adding of comments");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+val, "Checkpoint[3/14]", "Verifying the comments and comment box in expand mode");
		panel.assertFalse(panel.verifyLineBreakTagPresence(1), "Checkpoint[4/14]", "Verifying there is no line break present");
		panel.openAndCloseOutputPanel(false);	

		// verifying the comments for user created annotation
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 50, 50, val);
		textAnn.closingConflictMsg();
		
		panel.enableFiltersInOutputPanel(true,true,true);		
		//panel.assertTrue(panel.verifyCommentHasDotterLineAndLabelUnEditable(1),"Checkpoint[8/14]","erifying the comment box has dotted line and label is uneditable");		
		panel.assertTrue(panel.addCommentFromOutputPanel(1, val),"Checkpoint[9/14]","Adding comments and validating that comment label doesn't get displayed during adding of comments");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+val, "Checkpoint[10/14]", "Verifying the comments and comment box in expand mode");
		panel.assertFalse(panel.verifyLineBreakTagPresence(1), "Checkpoint[11/14]", "Verifying there is no line break present");
		panel.openAndCloseOutputPanel(false);	

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1304", "Positive"})
	public void test09_US1304_TC7156_verifyLongComment() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the comments are displayed in new line with same existing alignment at output panel when comment is long.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(multiTextPatientName, 1);

		panel=new OutputPanel(driver); ;

		TextAnnotation textAnn = new TextAnnotation(driver);
		String val = "Commmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmment";

		panel.enableFiltersInOutputPanel(true,true,true);		
		panel.assertTrue(panel.addCommentFromOutputPanel(1, val),"Checkpoint[1/12]","Verifying the comment box");
		panel.assertEquals(panel.getCommentText(1), ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT+val, "Checkpoint[2/12]", "verifying the long comment");
		panel.assertFalse(panel.verifyLineBreakTagPresence(1), "Checkpoint[3/12]", "Verifying that there is no line break");
		panel.openAndCloseOutputPanel(false);	

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		textAnn.selectTextArrowFromQuickToolbar(1);
		textAnn.drawText(1, 50, 50, val);
		textAnn.closingConflictMsg();

		String[] values = {"comment1","comment2","comment3"};
		panel.enableFiltersInOutputPanel(true,true,true);		
		panel.assertTrue(panel.addCommentFromOutputPanel(1, values),"Checkpoint[7/12]","Verifying that comment box");

		List<String> value = panel.getMultiLineCommenEntriesInOP(1);

		List<String> expectedValues = new ArrayList<String>();
		expectedValues.add(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim());
		expectedValues.addAll(Arrays.asList(values));
		

		panel.assertEquals(value,expectedValues, "Checkpoint[8/12]", "Verifying the multiline comments");
		panel.assertTrue(panel.verifyLineBreakTagPresence(1), "Checkpoint[9/12]", "Verifying there are line break present");
		panel.openAndCloseOutputPanel(false);	

	}

	@Test(groups = { "Chrome", "IE11", "Edge","DE1672","DE2028","Positive"})
	public void test10_DE1672_TC7157_DE2028_TC8324_verifyCommentAdditionWhenAnnotationNotLoadedOnViewer() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if user adds comment from output panel to the annotation not loaded in the viewer");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		panel=new OutputPanel(driver);; 
		circle = new CircleAnnotation(driver);
		point=new PointAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);

		String seriesName=cs.getSeriesDescriptionOverlayText(1);
		int cloneEntries = cs.getAllResults().size();

		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(), "checkpoint[1/10]", "Verifying the empty comments box is displayed in output panel");
		panel.openAndCloseOutputPanel(false);

		cs.selectSeriesFromSeriesTab(1, seriesName);

		panel.enableFiltersInOutputPanel(true,true,true);	
		panel.addCommentFromOutputPanel(1, text);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text,"Checkpoint[2/10]", "Verifying the comment is displayed along with series description");
		panel.assertEquals(panel.getCssValue(panel.getFindingStateIndicator(1),NSGenericConstants.FILL), ThemeConstants.SUCCESS_ICON_COLOR, "Checkpoint[3/10]", "Verifying that findings is turned into accepted after adding comment");	
		panel.openAndCloseOutputPanel(false);

		//wait for 2 seconds 
		panel.waitForTimePeriod(2000);		
		panel.assertEquals(cs.getAllResults().size(), cloneEntries+2, "Checkpoint[4/10]", "Verified that new clone created after adding comment from OP when annotation is not loaded on viewer");


		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);

		panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,1),"Checkpoint[5/10]","Verifying the i Icon is displayed in finding menu");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,1,text),"Checkpoint[6/10]","Verifying the tooltip is same as comment for i Icon");
		panel.selectFindingFromTable(1,1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[7/10]", "Verifying the comment for finding in viewer is displayed");
		circle.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[8/10]", "verifying the finding is current active accepted GSPS");	

		panel.enableFiltersInOutputPanel(true,false,false);	
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text,"Checkpoint[9/10]", "Verifying the comment is displayed after reload of viewer page");
		panel.assertEquals(panel.getCssValue(panel.getFindingStateIndicator(1),NSGenericConstants.FILL), ThemeConstants.SUCCESS_ICON_COLOR, "Checkpoint[10/10]", "Verifying that findings state is remain as accepted after reload of viewerpage");	



	}


	// DE1712: Not all the comments are getting added to machine findings when user adds the comments from output panel

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1712", "Positive"})
	public void test11_DE1712_TC7072_verifyAdditionOfCommentToMultipleMachineFindings() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that comment is getting added to GSPS multiple findings or ALL findings.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
		panel=new OutputPanel(driver); ;


		point=new PointAnnotation(driver);

		int findingCount=panel.getFindingsCountFromFindingTable(1);

		panel.enableFiltersInOutputPanel(true, true, true);
		for(int i=0;i<findingCount;i++)
		{
			panel.scrollIntoView(panel.thumbnailList.get(i));
			panel.addCommentFromOutputPanel(i+1, text+"_"+(i+1));
		}
		panel.openAndCloseOutputPanel(false);

		//verify comment on viewer
		panel.click(panel.getViewPort(1));
		panel.selectFindingFromTable(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(1)), text+"_"+"1", "Checkpoint[1/6]", "Verified that first added comment visible on viewer");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[2/6]", "Verified that first point annotation is active accepted GSPS.");
		panel.selectFindingFromTable(2);
		panel.assertEquals(panel.getText(panel.resultComment.get(1)), text+"_"+"2", "Checkpoint[3/6]", "Verified that  added comment visible on viewer second point annotation.");
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 2), "Checkpoint[4/6]", "Verified that second point annotation is active accepted GSPS.");

		for(int i=2;i<findingCount;i++)
		{
			panel.selectFindingFromTable(i+1);
			panel.assertEquals(panel.getText(panel.resultComment.get(0)), text+"_"+(i+1), "Checkpoint[5/6]", "Verified that added comment visible on viewer for point "+(i+1));
			panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[6/6]", "Verified that point annotation is active accepted GSPS "+(i+1));
		}

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1712", "Positive"})
	public void test12_DE1712_TC7072_verifyAdditionOfCommentToMultipleUserCreatedFindings() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that comment is getting added to user's drawn multiple findings.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		panel=new OutputPanel(driver); 
		panel.doubleClickOnViewbox(1);

		point=new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 50, 50);

		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -100, 50, 30, 20);

		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.drawEllipse(1, 200, -20, 20, -20);
		panel.doubleClickOnViewbox(1);

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.addCommentFromOutputPanel(1, ViewerPageConstants.ELLIPSE_FINDING_NAME+1);
		panel.addCommentFromOutputPanel(2, ViewerPageConstants.ELLIPSE_FINDING_NAME);
		panel.addCommentFromOutputPanel(3, ViewerPageConstants.LINEAR_FINDING_NAME);
		panel.addCommentFromOutputPanel(4, ViewerPageConstants.POINT_FINDING_NAME);
		panel.openAndCloseOutputPanel(false);

		panel.assertEquals(panel.getText(panel.resultComment.get(0)), ViewerPageConstants.LINEAR_FINDING_NAME, "Checkpoint[1/8]", "Verified that linear measurement comment visible on viewer");
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[2/8]", "Verified that first point annotation is active accepted GSPS.");

		panel.assertEquals(panel.getText(panel.resultComment.get(1)), ViewerPageConstants.ELLIPSE_FINDING_NAME, "Checkpoint[3/8]", "Verified that linear measurement comment visible on viewer");
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[4/8]", "Verified that first point annotation is active accepted GSPS.");

		panel.assertEquals(panel.getText(panel.resultComment.get(2)), ViewerPageConstants.ELLIPSE_FINDING_NAME+1, "Checkpoint[5/8]", "Verified that linear measurement comment visible on viewer");
		panel.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1), "Checkpoint[6/8]", "Verified that first point annotation is active accepted GSPS.");

		panel.assertEquals(panel.getText(panel.resultComment.get(3)), ViewerPageConstants.POINT_FINDING_NAME, "Checkpoint[7/8]", "Verified that linear measurement comment visible on viewer");
		panel.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1), "Checkpoint[8/8]", "Verified that first point annotation is active accepted GSPS.");



	}
	
	//DE2028: [Hardening-Northstar_1.0.2.0] Unable to update comments added under SC data from output panel
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2028", "Positive"})
	public void test15_DE2028_TC8289_verifyCommentWhenLayoutChangeUsingLayoutSelector() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify added/updated comment when layout is 1*1 and result is not loaded on viewer.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 3);

		String machineName=cs.getMachineName().get(0);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
	
		//add comment from Output panel
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.addCommentFromOutputPanel(1, text);
		panel.clickOnJumpIcon(1);
		panel.openAndCloseOutputPanel(false);
	
		//verify added comment on scroll slider
		
		List<String> findingName = panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[1/9]", "Verified added comment on scroll slider");
		panel.click(panel.getViewPort(1));
		
		//reopen OP and verify added comment in OP
		panel.waitForTimePeriod(2000);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[2/9]", "Verified that state of finding is in Accepted state after adding comment to Pending annotation.");
		panel.openAndCloseOutputPanel(false);

		//change layout to 1*1 using Layout selector and select another series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout and select second series from Content selector " );
		layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		cs=new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(1, secondSeries);
				
		//verify added comment in OP and then update added comment
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[3/9]", "Verified that added comment still visible in output panel when finding is not loaded on viewer.");
		panel.editCommentFromOutputPanel(1, 7, "edited ", true);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[4/9]", "Verified that finding is visible under Accepted tab only.");
		panel.openAndCloseOutputPanel(false);
		
		//change layout to default using Layout container
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout and select second series from Content selector " );
		layout.selectLayout(layout.twoByOneLayoutIcon);
		panel.click(panel.getViewPort(2));
		
		//load clone in 2nd viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load clone copy in second viewbox after adding or updating the comment from Output panel." );
		cs.selectResultCloneFromSeriesTabForGivenResult(2, 2, machineName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
	
		//verify edited comment in OP after loading clone in one of the viewbox.
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editedComment, "Checkpoint[5/9]", "Verified that updated comment visible in Output panel.");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/9]", "Verified that finding is visible under accepted tab only after updating comment.");
		
		//click on jump and verify comment on viewer,Finding menu and scroll slider
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment, "Checkpoint[7/9]", "Verified that updated comment visible in second viewbox for the point annotation.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, editedComment),"Checkpoint[8/9]","Verified that updated comment visible in finding menu against that finding.");
//	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.MIXED_RESULT_COLOR).get(0);
//		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker,findingName, editedComment), "Checkpoint[9/9]", "Verified that updated comment visible on scroll slider against that finding.");
		
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2028", "Positive"})
	public void test16_DE2028_TC8289_verifyCommentWhenLayoutChangeUsingOneUp() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify added/updated comment when layout is 1*1 and result is not loaded on viewer.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;

		String machineName=cs.getMachineName().get(0);
		//add comment from Output panel
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.addCommentFromOutputPanel(1, text);
		panel.openAndCloseOutputPanel(false);
		
		//verify added comment on scroll slider
		panel.click(panel.getViewPort(1));
		String findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName, text), "Checkpoint[1/9]", "Verified added comment on scroll slider");
		
		//reopen OP and verify added comment in OP
		panel.click(panel.getViewPort(2));
		panel.waitForTimePeriod(2000);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[2/9]", "Verified that state of finding is in Accepted state after adding comment to Pending annotation.");
		panel.openAndCloseOutputPanel(false);

		//change layout using one up and select another series
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout and select second series from Content selector " );
		panel.doubleClick(panel.getViewPort(1));
		panel.waitForAllChangesToLoad();
		cs=new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(1, secondSeries);
				
		//verify added comment in OP and then update added comment
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[3/9]", "Verified that added comment still visible in output panel when finding is not loaded on viewer.");
		panel.editCommentFromOutputPanel(1, 7, "edited ", true);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[4/9]", "Verified that finding is visible under Accepted tab only.");
		panel.openAndCloseOutputPanel(false);
		
		//change layout using one down
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout and select second series from Content selector " );
		panel.doubleClick(panel.getViewPort(1));
		panel.waitForAllChangesToLoad();
		
		//load clone in 2nd viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load clone copy in second viewbox after adding or updating the comment from Output panel." );
		cs.selectResultCloneFromSeriesTabForGivenResult(2, 2, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
		
		//verify edited comment in OP after loading clone in one of the viewbox.
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editedComment,  "Checkpoint[5/9]", "Verified that updated comment visible in Output panel.");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/9]", "Verified that finding is visible under accepted tab only after updating comment.");
		
		//click on jump and verify comment on viewer,Finding menu.
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment, "Checkpoint[7/9]", "Verified that updated comment visible in second viewbox for the point annotation.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, editedComment),"Checkpoint[8/9]","Verified that updated comment visible in finding menu against that finding.");
		marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName, editedComment), "Checkpoint[9/9]", "Verified that updated comment visible on scroll slider against that finding.");
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2028", "Positive"})
	public void test17_DE2028_TC8290_verifyCommentWhenSameResultLoadedOnBothViewboxes() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify added and updated comment in Output panel when same result copy is loaded on both the viewboxes.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw 4 different annotation and add comment from A/R toolbar for few annotation." );
		ellipse=new EllipseAnnotation(driver);
		//draw multiple annotation and add comment for the few findings
		lineWithUnit=new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, 0, 0, 50, 50);
		lineWithUnit.addResultComment(1, rulerComment);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		ellipse.addResultComment(1, ellipseComment);

        point=new PointAnnotation(driver);
        point.selectPointFromQuickToolbar(1);
        point.drawPointAnnotationMarkerOnViewbox(1, 150, 150);
        
        circle=new CircleAnnotation(driver);
        circle.selectCircleFromQuickToolbar(1);
        circle.drawCircle(1, -30, -50, -50, -80);
        
        int findingCount=ellipse.getFindingsCountFromFindingTable(1);
		
		//add comment from Output panel
        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment from Output panel for remaining annotation." );
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.addCommentFromOutputPanel(1, circleComment);
		panel.addCommentFromOutputPanel(2, pointComment);
		panel.openAndCloseOutputPanel(false);
		
		//change layout 2*1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change layout in 2*1 and load clone in both the viewbox." );
		layout.selectLayout(layout.twoByOneLayoutIcon);
		cs=new ContentSelector(driver);
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1");
		
		//verify comment in OP when clone is loaded in both the viewbox
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), findingCount, "", "");
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), pointComment,"Checkpoint[1/16]", "Verifying the comment for point annotation when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(2).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), circleComment,"Checkpoint[2/16]", "Verifying the comment for circle annotation when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(3).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), ellipseComment,"Checkpoint[3/16]", "Verifying the comment for ellipse annotation when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(4).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), rulerComment,"Checkpoint[4/16]", "Verifying the comment for distance annotation when same copy is loaded on both viewbox.");
		
		//update added comment from viewer page
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update added comment from A/R toolbar for few annotation." );
		for(int i=3;i<=findingCount;i++)
		panel.editCommentFromOutputPanel(i , 0, "Edited ", true);
		panel.openAndCloseOutputPanel(false);
		
		//update added comment from Output panel 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update added comment from Output panel for remaining annotation." );
		panel.click(panel.getViewPort(1));
		panel.editResultComment(circle.resultComment.get(2), editCicleComment);
		panel.editResultComment(point.resultComment.get(3), editPointComment);
	
		//verify updated  comment in Output panel
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify updated comment in Output panel when same clone is loaded in both the viewbox." );
		panel.click(panel.getViewPort(2));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), findingCount, "", "");
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editPointComment,"Checkpoint[5/16]", "Verifying the updated comment for point annotation in OP when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(2).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editCicleComment,"Checkpoint[6/16]", "Verifying the updated comment for circle annotation in OP when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(3).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editRulerComment,"Checkpoint[7/16]",  "Verifying the updated comment for ruler annotation in OP when same copy is loaded on both viewbox.");
		panel.assertEquals(panel.getCommentText(4).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""),editEllipseComment ,"Checkpoint[8/16]", "Verifying the updated comment for ellipse annotation in OP when same copy is loaded on both viewbox.");
		panel.openAndCloseOutputPanel(false);
		
		//verify comment in finding menu
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify updated comment in finding menu when same clone is loaded in both the viewbox." );
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, editRulerComment),"Checkpoint[9/16]","Verifying the updated comment for ruler annotation in finding menu when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 2, editEllipseComment),"Checkpoint[10/16]","Verifying the updated comment for ellipse annotation in finding menu when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 3, editPointComment),"Checkpoint[11/16]","Verifying the updated comment for point annotation in finding menu when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 4, editCicleComment),"Checkpoint[12/16]","Verifying the updated comment for circle annotation in finding menu when same copy is loaded on both viewbox.");
	
		//verify comment from scrollslider
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify updated comment in scroll slider when same clone is loaded in both the viewbox." );
		panel.click(panel.getViewPort(1));
		List<String> findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		WebElement marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), editRulerComment), "Checkpoint[13/16]", "Verifying the updated comment for ruler annotation in scroll slider when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(1), editEllipseComment), "Checkpoint[14/16]", "Verifying the updated comment for ellipse annotation in scroll slider when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(2), editPointComment), "Checkpoint[15/16]", "Verifying the updated comment for point annotation in scroll slider when same copy is loaded on both viewbox.");
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(3), editCicleComment), "Checkpoint[16/16]", "Verifying the updated comment for circle annotation in scroll slider when same copy is loaded on both viewbox.");
	
		
		
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2028","DE2127","DE2126","Positive"})
	public void test18_DE2028_TC8321_DE2127_TC8492_DE2126_TC8675_verifyAddedCommentWhenAnnotationNotLoadedOnViewer() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment added from output panel under annotation which is not loaded in viewer is getting retained.<br>"+
		"Re-execute TC8321: Verify comment added from output panel under annotation which is not loaded in viewer is getting retained. <br>"+
		"Re-Execute TC8321: Verify comment added from output panel under annotation which is not loaded in viewer is getting retained.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		String machineName=cs.getMachineName().get(0);

		cs=new ContentSelector(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the same result in both the viewbox." );
		cs.selectResultFromSeriesTab(2,secondSeries,2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment from OP for the finding which result is not loaded in any of the viewbox." );
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.scrollIntoView(panel.thumbnailList.get(5));
		panel.addCommentFromOutputPanel(6, text);
		panel.openAndCloseOutputPanel(false);
		
		//load clone in 2nd viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load clone copy in second viewbox." );
		cs.selectResultCloneFromSeriesTabForGivenResult(2, 1, machineName, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
        panel.assertEquals(panel.getText(panel.resultComment.get(0)).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[1/5]", "Verified added comment on viewer page.");
        panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, text), "Checkpoint[2/5]", "Verified added comment in finding menu.");
        
        List<String> findingName=panel.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
        WebElement marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[3/5]", "Verified added comment in Scroll slider.");
		
		//open OP and verify added comment
		panel.click(panel.getViewPort(1));
		panel.waitForTimePeriod(2000);
        panel.enableFiltersInOutputPanel(true, false, false);
        panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[4/5]", "Verified that added comment visible in OP for the finding.");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[5/5]", "Verified state of finding is accepted in Output panel.");
 
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","DE2028","DE2127","DE2126","Positive"})
	public void test19_DE2028_TC8334_DE2127_TC8493_DE2126_TC8677_verifyCommentAdditionWhenAnnotationNotLoadedOnViewer() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify if user adds comment from output panel to the annotation not loaded in the viewer. <br>"+
		"Re-execute TC8334: Verify comment added from output panel under annotation which is not loaded in viewer as well as for the annotation which is being loaded  in viewer is getting retained.<br>"+
		"Re-execute TC8334: Verify comment added from output panel under annotation which is not loaded in viewer as well as for the annotation which is being loaded  in viewer is getting retained.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(gspsPatientName);

		patientListPage.clickOntheFirstStudy();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(2);
		cs = new ContentSelector(driver);
		
		String machineName=cs.getMachineName().get(0);
		cs.selectResultFromSeriesTab(1, secondSeries, 1);
		
		//add comment for the annotation which is not loaded on viewer as well as for the annotation which is loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment for the annotation which is not loaded as well as for the annotation which is loaded on viewer." );
		panel.enableFiltersInOutputPanel(true,true,true);	
		panel.addCommentFromOutputPanel(1, text);
		panel.scrollIntoView(panel.thumbnailList.get(6));
		panel.addCommentFromOutputPanel(7, pointComment);

       //click on jump icon for the finding which is loaded on viewer.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider after click on jump icon for which result is loaded on viewer." );
		panel.clickOnJumpIcon(7);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), pointComment,"Checkpoint[1/15]", "Verifying the comment on viewer for the result which is loaded on viewbox-1.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 2, pointComment), "Checkpoint[2/15]", "Verifying the comment in finding menu for the result which is loaded on viewbox-1.");
		
        List<String> findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
        WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), pointComment), "Checkpoint[3/15]", "Verifying the comment in scroll slider for the result which is loaded on viewbox-1.");
		
		//verify comment in second viewbox
		panel.click(panel.getViewPort(2));
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, pointComment), "Checkpoint[4/15]", "Verifying the comment in finding menu for the result which is loaded on viewbox-2.");
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), pointComment,"Checkpoint[5/15]", "Verifying the comment on viewer for the result which is loaded on viewbox-2.");
		panel.scrollDownToSliceUsingKeyboard(1);
		
		findingName=panel.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), pointComment), "Checkpoint[6/15]","Verifying the comment in scroll slider for the result which is loaded on viewbox-2.");
		
	    //load clone of series 1 in second viewbox.
	    panel.click(panel.getViewPort(1));
	    cs.selectResultCloneFromSeriesTabForGivenResult(2, 2, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, text), "Checkpoint[7/15]", "Verifying the comment in finding menu for the result which is loaded from CS on viewbox-2.");
	    panel.selectFindingFromTable(2, 1);
	    panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[8/15]", "Verifying the comment on viewer for the result which is loaded from CS on viewbox-2.");
	    findingName=panel.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[9/15]", "Verifying the comment in scroll slider for the result which IS loaded from CS on viewbox-2.");
		
	    //reload viewer page and perform scroll in first viewbox
	    helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
	    panel.scrollDownToSliceUsingKeyboard(2,1);
	   
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider for the first result after reload." );
	    panel.selectFindingFromTable(1, 1);
	    panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[10/15]", "Verifying the comment for finding after reload of viewer in viewbox-1");
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text), "Checkpoint[11/15]","Verifying the comment for finding in finding menu  after reload of viewer in viewbox-1");
	    findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
	    marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[12/15]","Verifying the comment for finding on scroll slider after reload of viewer in viewbox-1");
	    
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider for the second result after reload." );
	    panel.click(panel.getViewPort(1));
	    cs.selectSeriesFromSeriesTab(1, firstSeries);
	    panel.selectFindingFromTable(2, 2);
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, pointComment), "Checkpoint[13/15]", "Verifying the comment for finding after reload of viewer in viewbox-2");
	    panel.assertEquals(panel.getText(panel.resultComment.get(0)), pointComment,"Checkpoint[14/15]", "Verifying the comment for finding in finding menu after reload of viewer in viewbox-2");
	    findingName=panel.getFindingsName(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), pointComment), "Checkpoint[15/15]", "Verifying the comment for finding on scroll slider after reload of viewer in viewbox-2");

	}

	@Test(groups = { "Chrome", "IE11", "Edge","DE2028","DE2127","DE2126","Positive"})
	public void test20_DE2028_TC8288_DE2127_TC8491_DE2126_TC8674_verifyCommentAdditionUpdationAfterReloadForGSPS() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of annotation change after adding comment to pending findings for GSPS data. <br>"+
		"Re-execute TC8288: Verify state of annotation change after adding comment to pending findings for Mammo CAD and GSPS data. <br>"+
				"Re-execute TC8288: Verify state of annotation change after adding comment to pending findings for Mammo CAD and GSPS data.");
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(2);

		//add comment from OP
		panel.enableFiltersInOutputPanel(true,true,true);	
		panel.addCommentFromOutputPanel(1, text);
	
       //click on jump icon for the finding which is loaded on viewer.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and scroll slider after click on jump icon." );
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[1/14]", "Verifying the comment for finding in viewer is displayed");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text), "Checkpoint[2/14]", "Verifying the comment for finding in finding menu  is displayed");
		List<String>findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[3/14]", "Verifying the comment for finding in scroll slider is displayed");
		
		//reload viewer page and verify comment
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu,Output panel and scroll slider after reload of viewer" );
		panel.selectFindingFromTable(1, 1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), text, "Checkpoint[4/14]", "Verifying the comment for finding in viewer is displayed after reload");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text),  "Checkpoint[5/14]", "Verifying the comment for finding in finding menu  is displayed after reload");
		findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), text), "Checkpoint[6/14]", "Verifying the comment for finding in scroll slider is displayed after reload");
		
		//verify comment in OP and again update it.
		panel.click(panel.getViewPort(1));
		panel.click(panel.getViewPort(2));
		panel.waitForTimePeriod(3000);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[7/14]", "Verified that comment is visible in Output panel after reload.");
		panel.editCommentFromOutputPanel(1, 7, "edited ", true);
		
	    //click on jump icon for the finding which is loaded on viewer.
		panel.clickOnJumpIcon(1);
		panel.click(panel.getViewPort(2));
		panel.click(panel.getViewPort(1));
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment,"Checkpoint[8/14]", "Verifying the updated comment for finding in viewer is displayed");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, editedComment),"Checkpoint[9/14]", "Verifying the updated comment for finding in finding menu  is displayed.");
		findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), editedComment), "Checkpoint[10/14]", "Verifying the updated comment for finding in scroll slider is displayed.");
		
		//reload viewer and verify updated comment
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify updated comment on viewer,finding menu,Output panel and scroll slider after reload of viewer" );
		panel.selectFindingFromTable(1, 1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment,"Checkpoint[11/14]", "Verifying the updated comment for finding in viewer is displayed after reload");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, editedComment), "Checkpoint[12/14]", "Verifying the updated comment for finding in finding menu is displayed after reload");
		findingName=panel.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName.get(0), editedComment), "Checkpoint[13/14]", "Verifying the updated comment for finding in scroll slider is displayed after reload");
		
		panel.click(panel.getViewPort(1));
		panel.click(panel.getViewPort(2));
		panel.waitForTimePeriod(3000);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editedComment, "Checkpoint[14/14]", "Verified that updated comment is visible in Output panel after reload.");
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","DE2028","DE2127","DE2126","Positive"})
	public void test21_DE2028_TC8288_DE2127_TC8491_DE2126_TC8674_verifyCommentAdditionUpdationAfterReloadForCAD() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of annotation change after adding comment to pending findings for CAD data.<br>"+
		"Re-execute TC8288: Verify state of annotation change after adding comment to pending findings for Mammo CAD and GSPS data.<br>"+
				"Re-execute TC8288: Verify state of annotation change after adding comment to pending findings for Mammo CAD and GSPS data.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(IHEMammoTestPatientName);

		patientListPage.clickOntheFirstStudy();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(2);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load 4th copy of CAD in first viewbox and add comment from Output panel for one of the finding.");
		cs=new ContentSelector(driver);
		cs.selectResultFromSeriesTab(1, resultDescription, 4);
		
		//add comment for the annotation which is not loaded on viewer as well as for the annotation which is not loaded
		panel.enableFiltersInOutputPanel(true,true,true);	
		panel.addCommentFromOutputPanel(2, text);
	
       //click on jump icon for the finding which is loaded on viewer.
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[1/10]", "Verifying the comment for finding in viewer is displayed");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text), "Checkpoint[2/10]", "Verifying the comment for finding in finding menu is displayed");
		
		//reload viewer and load clone in first viewbox
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
		cs.selectResultFromSeriesTab(1, resultDescription+"_"+username+"_1", 4);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), text,"Checkpoint[3/10]", "Verifying the comment for finding in viewer is displayed after reload of viewer.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text), "Checkpoint[4/10]", "Verifying the comment for finding in finding menu is displayed after reload of viewer.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[5/10]", "Verifying the comment for finding in Output panel is displayed after reload of viewer.");
		
		//update comment from OP after reload of viewer.
		panel.editCommentFromOutputPanel(1, 7, "edited ", true);
	    //click on jump icon for the finding which is loaded on viewer.
		panel.clickOnJumpIcon(1);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment,"Checkpoint[6/10]", "Verifying the updated comment for finding in viewer is displayed.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, editedComment), "Checkpoint[7/10]",  "Verifying the updated comment for finding in finding menu is displayed.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewer page and verify updated comment in finding menu,viewer and Output panel.");
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
		cs.selectResultFromSeriesTab(1, resultDescription+"_"+username+"_2", 4);
		panel.assertEquals(panel.getText(panel.resultComment.get(0)), editedComment,"Checkpoint[8/10]","Verifying the comment for finding in viewer is displayed after reload of viewer.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, editedComment), "Checkpoint[9/10]", "Verifying the updated comment for finding in viewer is displayed after reload of viewer.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), editedComment, "Checkpoint[10/10]", "Verifying the updated comment for finding in Output panel is displayed after reload of viewer.");

	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE1947", "Positive"})
	public void test22_DE1947_TC7891_TC7953_VerifyCommentsAddedFromOutputPanelUnderPendingAnnotationAreRetained() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comments added at viewer and from the output panel under pending annotation should be retained . <br>"+
								  "Verify comments added from output panel under pending annotation should be retained");
		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
       
		panel=new OutputPanel(driver); ;

		cs = new ContentSelector(driver);
		line = new  SimpleLine(driver);

		
		// fetching the series description from XML
		String seriesNumberThree = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, liver9FilePath);
		String seriesNumberTwo = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, liver9FilePath);

		//Load the source series into first view box
		// draw the line annotation and change it as pending status
	
		cs.selectSeriesFromSeriesTab(1,seriesNumberThree);		
		line.selectLineFromQuickToolbar(panel.getViewPort(1)); 
		line.drawLine(1,-10,-10,50,50);                                   
		panel.selectAcceptfromGSPSRadialMenu();                         
		panel.closingConflictMsg();
		
		//Load the source series into second view box
		// draw the line annotation and change it as pending status and double click on that viewbox to make it 1X1
		
		cs.selectSeriesFromSeriesTab(2,seriesNumberTwo);		
		line.selectLineFromQuickToolbar(panel.getViewPort(2));
		line.drawLine(2,-10,-10,70,70);
		panel.selectAcceptfromGSPSRadialMenu();
		panel.closingConflictMsg();
		panel.doubleClickOnViewbox(2);
			
		//Select the finding to add the comment from AR toolbar
		panel.selectFindingFromTable(2,1);
		panel.addResultComment(2, username);
		panel.selectAcceptfromGSPSRadialMenu();
		
		//Adding comment at output panel
		panel.enableFiltersInOutputPanel(true,true,true);
		panel.addCommentFromOutputPanel(1, val);
		panel.addCommentFromOutputPanel(2, val);

		panel.openAndCloseOutputPanel(false);
	        
		//load the series and open the output panel
		cs.selectSeriesFromSeriesTab(2,seriesNumberThree);
		panel.enableFiltersInOutputPanel(true,false,true);
		
		//checkpoint to verify the comment at the output panel (TC7953)
		panel.assertTrue(panel.getCommentText(2).contains(username+val), "Checkpoint[1/4]", "Verifying the comment for the loaded series is present on the output panel");
		//Step which is failing currently
		panel.assertTrue(panel.getCommentText(1).contains(val), "Checkpoint[2/4]", "Verifying the comment for the non loaded series is present on the output panel");
	
		
		//checkpoint to verify the comment at the output panel (TC7891)
		panel.openAndCloseOutputPanel(false);
		cs.selectResultFromSeriesTab(2, "GSPS_scan_1");
		panel.enableFiltersInOutputPanel(true,false,true);
		panel.assertTrue(panel.getCommentText(2).contains(username+val), "Checkpoint[3/4]", "Verifying the comment for the loaded series is present on the output panel");
    	panel.assertTrue(panel.getCommentText(1).contains(val), "Checkpoint[4/4]", "Verifying the comment for the non loaded series is present on the output panel");
		
						
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2048", "Positive"})
	public void test23_DE2048_TC8279_verifyCommentsMappedToCorrectFindingAtBackground() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that comment in output panel map to correct measurement in the view box");
		
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		panel=new OutputPanel(driver); 
		
		List<String> findingsName = panel.getFindingsName(2, ViewerPageConstants.PENDING_FINDING_COLOR);
		String selectedFindingName = findingsName.get(findingsName.size()-1);
		
		panel.enableFiltersInOutputPanel(false, false, true);
		int thumbnailNo = panel.getThumbnailForGivenResult(selectedFindingName);
		panel.addCommentFromOutputPanel(thumbnailNo+1, selectedFindingName);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+selectedFindingName,"Checkpoint[1/18]","Verifying the comment is displayed for added finding");
		
		panel.clickOnJumpIcon(1);
		point = new PointAnnotation(driver);
		int pointNumber = point.getPointWhichIsAcceptedAndActive(2);
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[2/18]","Verifying the text is displayed for finding after click on jump to icon");
		
		panel.assertTrue(panel.verifyCommentIconIsDisplayedInFindingMenu(2, selectedFindingName),"Checkpoint[3/18]","verifying the comment icon is displayed to same finding");
		panel.assertEquals(panel.getCommentTextFromFindingMenu(2, selectedFindingName),selectedFindingName,"Checkpoint[4/18]","Verifying the text is same displayed on i Icon");
		
		WebElement marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, selectedFindingName, selectedFindingName), "Checkpoint[5/18]", "Verified added comment on scroll slider");
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		
		marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[6/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[7/18]","Verifying that same comment is displayed on click of finding from slider");
		
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		panel.selectFindingFromTable(2, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[8/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[9/18]","Verifying that same comment is displayed on click of finding from finding menu");
		
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		
		panel.click(panel.getViewPort(2));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+selectedFindingName,"Checkpoint[10/18]","Verifying the comment is displayed for added finding");
		panel.clickOnJumpIcon(1);
		point = new PointAnnotation(driver);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[11.1/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[11.2/18]","Verifying the text is displayed for finding after click on jump to icon");
		
		panel.mouseHover(panel.getViewPort(2));
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		panel.assertTrue(panel.verifyCommentIconIsDisplayedInFindingMenu(2, selectedFindingName),"Checkpoint[12/18]","verifying the comment icon is displayed to same finding");
		panel.assertEquals(panel.getCommentTextFromFindingMenu(2, selectedFindingName),selectedFindingName,"Checkpoint[13/18]","Verifying the text is same displayed on i Icon");
		
		marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, selectedFindingName, selectedFindingName), "Checkpoint[14/18]", "Verified added comment on scroll slider");
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		
		marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[15/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[16/18]","Verifying that same comment is displayed on click of finding from slider");
		
		panel.scrollUpToSliceUsingKeyboard(2, 1);
		panel.selectFindingFromTable(2, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(2, 1), "Checkpoint[17/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(2, pointNumber),selectedFindingName,"Checkpoint[18/18]","Verifying that same comment is displayed on click of finding from finding menu");

		
		
						
	}
		
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2016", "Positive"})
	public void test24_DE2016_TC8253_verifyCommentsRetainedOnReload() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the comments added from output panel under an annotation which is in pending state, are retained and visible in output panel.");
		
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad();		
		panel.doubleClickOnViewbox(1);
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
		point.selectAcceptfromGSPSRadialMenu();
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.addCommentFromOutputPanel(1,comment);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+comment,"Checkpoint[1/4]","Verifying the comment is displayed for added finding");
		panel.openAndCloseOutputPanel(false);

		helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+comment,"Checkpoint[2/4]","Verifying the comment is displayed for added finding");
		panel.clickOnJumpIcon(1);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[3/4]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, 1),comment,"Checkpoint[4/4]","Verifying the text is displayed for finding after click on jump to icon");
		
						
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "Positive","DE2099"})
	public void test25_DE2099_TC8353_verifyCommentsMappedToCorrectFindingAtForeground() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the comment is mapped to the right finding in Finding drop down and Finding Preview box slider when added from Output Panel.");
		
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);
		
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad();
		
		List<String> findingsName = panel.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR);
		String selectedFindingName = findingsName.get(findingsName.size()-2);
		
		panel.enableFiltersInOutputPanel(false, false, true);
		int thumbnailNo = panel.getThumbnailForGivenResult(selectedFindingName);
		panel.addCommentFromOutputPanel(thumbnailNo, selectedFindingName);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+selectedFindingName,"Checkpoint[1/18]","Verifying the comment is displayed for added finding");
		
		panel.clickOnJumpIcon(1);
		point = new PointAnnotation(driver);
		int pointNumber = point.getPointWhichIsAcceptedAndActive(1);
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[2/18]","Verifying the text is displayed for finding after click on jump to icon");
		
		panel.assertTrue(panel.verifyCommentIconIsDisplayedInFindingMenu(1, selectedFindingName),"Checkpoint[3/18]","verifying the comment icon is displayed to same finding");
		panel.assertEquals(panel.getCommentTextFromFindingMenu(1, selectedFindingName),selectedFindingName,"Checkpoint[4/18]","Verifying the text is same displayed on i Icon");
		
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, selectedFindingName, selectedFindingName), "Checkpoint[5/18]", "Verified added comment on scroll slider");
		panel.scrollUpToSliceUsingKeyboard(1, 1);
		
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[6/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[7/18]","Verifying that same comment is displayed on click of finding from slider");
		
		panel.scrollUpToSliceUsingKeyboard(1, 1);
		panel.selectFindingFromTable(1, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[8/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[9/18]","Verifying that same comment is displayed on click of finding from finding menu");

		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1),ViewerPageConstants.COMMENT_TAG+" "+selectedFindingName,"Checkpoint[10/18]","Verifying the comment is displayed for added finding");
		panel.clickOnJumpIcon(1);
		point = new PointAnnotation(driver);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[11.1/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[11.2/18]","Verifying the text is displayed for finding after click on jump to icon");
		
		panel.mouseHover(panel.getViewPort(1));
		panel.scrollDownToSliceUsingKeyboard(1, 1);
		panel.assertTrue(panel.verifyCommentIconIsDisplayedInFindingMenu(1, selectedFindingName),"Checkpoint[12/18]","verifying the comment icon is displayed to same finding");
		panel.assertEquals(panel.getCommentTextFromFindingMenu(1, selectedFindingName),selectedFindingName,"Checkpoint[13/18]","Verifying the text is same displayed on i Icon");
		
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, selectedFindingName, selectedFindingName), "Checkpoint[14/18]", "Verified added comment on scroll slider");
		panel.scrollDownToSliceUsingKeyboard(1, 1);
		
		marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.selectFindingFromSlider(marker, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[15/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[16/18]","Verifying that same comment is displayed on click of finding from slider");
		
		panel.scrollDownToSliceUsingKeyboard(1, 1);
		panel.selectFindingFromTable(1, selectedFindingName);
		panel.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[17/18]", "Verifying on selection point is highlighted");
		panel.assertEquals(point.getTextCommentForPoint(1, pointNumber),selectedFindingName,"Checkpoint[18/18]","Verifying that same comment is displayed on click of finding from finding menu");

		
		
						
	}
		
	@Test(groups = { "Chrome", "IE11", "Edge", "DE2126", "Positive"})
	public void test26_DE2126_TC8639_verifyCloneWhenAnnotationNotLoadedOnViewerForGSPS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new clone is created after adding comment from output panel for the annotation which is not loaded on viewer.");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(1);
		
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);
		
		String machineName=cs.getMachineName().get(0);
		String findingName1=panel.getFindingsName(2, ViewerPageConstants.PENDING_FINDING_COLOR).get(1);
		int resultCount=cs.getAllResults().size();
				
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load the same result in both the viewbox." );
		cs.selectResultFromSeriesTab(2,secondSeries,2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment from OP for the finding which result is not loaded in any of the viewbox." );
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.addCommentFromOutputPanel(7, text);
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify clone in content selector after adding comment." );
		panel.assertEquals(cs.getAllResults().size(), resultCount+2, "Checkpoint[1/11]", "Verified that new clone is created after adding comment from Output panel.");
		
		//load clone in 2nd viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load clone copy in viewer" );
		cs.selectResultCloneFromSeriesTabForGivenResult(2, 1, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
        panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(2).get(0)), text, "Checkpoint[2/11]", "Verified added comment on viewer page.");
        panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, text), "Checkpoint[3/11]", "Verified added comment in finding menu.");
        
        WebElement marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName1, text), "Checkpoint[4/11]", "Verified added comment in Scroll slider.");
		
		//open OP and verify added comment
		panel.mouseHover(panel.getViewPort(1));
        panel.enableFiltersInOutputPanel(true, false, false);
        panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[5/11]", "Verified that added comment visible in OP for the finding.");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/5]", "Verified state of finding is accepted in Output panel.");

		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
		panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(2).get(0)), text, "Checkpoint[7/11]", "Verified added comment on viewer page after reload");
        panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, text), "Checkpoint[8/11]", "Verified added comment in finding menu after reload");
        marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
        panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName1, text), "Checkpoint[9/11]", "Verified added comment in Scroll slider after reload.");
		
		//open OP and verify added comment
        panel.mouseHover(panel.getViewPort(1));
        panel.enableFiltersInOutputPanel(true, false, false);
        panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[10/11]", "Verified that added comment visible in OP for the finding after reload.");
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[11/11]", "Verified state of finding is accepted in Output panel.");
		
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","DE2126","Positive"})
	public void test27_DE2126_TC8639_verifyCloneWhenAnnotationNotLoadedOnViewerForCAD() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new clone is created after adding comment from output panel for the annotation which is not loaded on viewer.");

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(IHEMammoTestPatientName, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+IHEMammoTestPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(2);

        cs=new ContentSelector(driver);
		int resultCount=cs.getAllResults().size();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load 4th copy of CAD in first viewbox and add comment from Output panel for one of the finding.");
		cs=new ContentSelector(driver);
		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		cs.selectResultFromSeriesTab(1, resultDescription, 1);
		cs.selectResultFromSeriesTab(2, resultDescription, 1);
		
		//add comment for the annotation which is not loaded on viewer
		panel.enableFiltersInOutputPanel(false,false,true);	
		panel.addCommentFromOutputPanel(2, text);
		panel.openAndCloseOutputPanel(false);
	
		//load clone in 2nd viewbox 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Load clone copy in second viewbox." );
		panel.assertEquals(cs.getAllResults().size(), resultCount+resultCount, "Checkpoint[1/6]", "Verified that new clone is created after adding commnet ");
				
		cs.selectResultFromSeriesTab(2, resultDescription+"_"+username+"_1", 4);
		panel.assertEquals(poly.getText(poly.getTextCommentsOnAllPolyLines(2).get(0)), text, "Checkpoint[2/6]", "Verified added comment on viewer page.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, text), "Checkpoint[3/6]", "Verified added comment in finding menu.");
		        
		//reload viewer and load clone in first viewbox
		helper.browserBackAndReloadViewer(IHEMammoTestPatientName, 1, 1);
		cs.selectResultFromSeriesTab(2, resultDescription+"_"+username+"_1", 4);
		panel.assertEquals(poly.getText(poly.getTextCommentsOnAllPolyLines(2).get(0)), text,"Checkpoint[4/6]", "Verifying the comment for finding in viewer is displayed after reload of viewer.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, text), "Checkpoint[5/6]", "Verifying the comment for finding in finding menu is displayed after reload of viewer.");
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text, "Checkpoint[6/6]", "Verifying the comment for finding in Output panel is displayed after reload of viewer.");
	
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge","DE2126","Negative"})
	public void test28_DE2126_TC8678_verifyCommentAdditionWhenAcceptedButtonIsDisableForGSPS() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment added from output panel under annotation which is not loaded in viewer as well as for the annotation which is being loaded  in viewer is getting retained when Accepted button is disable.");
		
		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName, 1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad(2);
		cs = new ContentSelector(driver);
		
		String machineName=cs.getMachineName().get(0);
		String findingName1=panel.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR).get(0);
		String findingName2=panel.getFindingsName(2, ViewerPageConstants.PENDING_FINDING_COLOR).get(1);
		
		int resultCount=cs.getAllResults().size();
		cs.selectResultFromSeriesTab(1, secondSeries, 1);
		point=new PointAnnotation(driver);
		
		//add comment for the annotation which is not loaded on viewer as well as for the annotation which is loaded
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment for the annotation which is not loaded as well as for the annotation which is loaded on viewer." );
		panel.enableFiltersInOutputPanel(false,true,true);	
		panel.addCommentFromOutputPanel(1, text);
		panel.addCommentFromOutputPanel(6, pointComment);

       //click on jump icon for the finding which is loaded on viewer.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider after click on jump icon for which result is loaded on viewer." );
		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.clickOnJumpIcon(1);
		panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(1).get(0)), pointComment,"Checkpoint[1/18]", "Verifying the comment on viewer for the result which is loaded on viewbox-1.");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 2, pointComment), "Checkpoint[2/18]", "Verifying the comment in finding menu for the result which is loaded on viewbox-1.");
		panel.assertEquals(cs.getAllResults().size(),resultCount+resultCount ,"Checkpoint[3/18]", "Verified tha new clone is created in Content selector.");
        WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName2, pointComment), "Checkpoint[4/18]", "Verifying the comment in scroll slider for the result which is loaded on viewbox-1.");
	
		//verify comment in second viewbox
		panel.click(panel.getViewPort(2));
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, pointComment), "Checkpoint[5/18]", "Verifying the comment in finding menu for the result which is loaded on viewbox-2.");
		panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(2).get(0)), pointComment,"Checkpoint[6/18]", "Verifying the comment on viewer for the result which is loaded on viewbox-2.");
		
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName2, pointComment), "Checkpoint[7/18]","Verifying the comment in scroll slider for the result which is loaded on viewbox-2.");
	    panel.scrollDownToSliceUsingKeyboard(1,3);
	    
	    //load clone of series 1 in second viewbox.
	    panel.click(panel.getViewPort(1));
	    cs.selectResultCloneFromSeriesTabForGivenResult(2, 2, machineName,ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+Configurations.TEST_PROPERTIES.get("nsUserName")+"_1");
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 1, text), "Checkpoint[8/18]", "Verifying the comment in finding menu for the result which is loaded from CS on viewbox-2.");
	    panel.selectFindingFromTable(2, 1);
	    panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(2).get(0)), text,"Checkpoint[9/18]", "Verifying the comment on viewer for the result which is loaded from CS on viewbox-2.");
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName1, text), "Checkpoint[10/18]", "Verifying the comment in scroll slider for the result which IS loaded from CS on viewbox-2.");
		
	    //reload viewer page and perform scroll in first viewbox
		helper.browserBackAndReloadViewer(gspsPatientName, 1, 1);
	
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider for the second result after reload." );
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(2, 2, pointComment), "Checkpoint[11/18]", "Verifying the comment for finding after reload of viewer in viewbox-2");
	    panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(2).get(0)), pointComment,"Checkpoint[12/18]", "Verifying the comment for finding in finding menu after reload of viewer in viewbox-2");
	    marker=panel.getStateSpecificMarkerOnSlider(2, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName2, pointComment), "Checkpoint[13/18]", "Verifying the comment for finding on scroll slider after reload of viewer in viewbox-2");
	    
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify comment on viewer,finding menu and slider for the first result after reload." );
	    panel.selectFindingFromTable(1, 1);
	    panel.assertEquals(point.getText(point.getTextCommentsForAllPoints(1).get(0)), text,"Checkpoint[14/18]", "Verifying the comment for finding after reload of viewer in viewbox-1");
	    marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
	    panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName1, text), "Checkpoint[15/18]","Verifying the comment for finding on scroll slider after reload of viewer in viewbox-1");
	    panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, text), "Checkpoint[16/18]","Verifying the comment for finding in finding menu  after reload of viewer in viewbox-1");
	   
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE2126", "Negative"})
	public void test29_DE2126_TC8679_VerifyCloneForUserDrawnAnnotationWhenAcceptedButtonDisable() throws InterruptedException, AWTException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new clone is created after adding comment from output panel for the user drawn annotation which is not loaded on viewer.");
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		panel=new OutputPanel(driver); ;
		panel.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		line = new  SimpleLine(driver);
		
		// draw the line annotation and change it as pending status
		circle=new CircleAnnotation(driver);
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, -30, -50, -50, -80);
	    circle.selectAcceptfromGSPSRadialMenu();   
	    
	    String findingName=panel.getFindingsName(1, ViewerPageConstants.PENDING_FINDING_COLOR).get(0);
		panel.closeWaterMarkIcon(2);
		
		// draw the circle annotation and change it as pending status 	 
	    line.selectLineFromQuickToolbar(panel.getViewPort(1)); 
		line.drawLine(2,-10,-10,50,50);                                   
		line.selectAcceptfromGSPSRadialMenu();                         

	    helper.browserBackAndReloadViewer(liver9PatientName, 1, 1);
		
		int resultCount=cs.getAllResults().size();
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);
		panel.closeWaterMarkIcon(2);
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_2");
		//Adding comment at output panel
		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(false,false,true);
		int thumbnail=panel.getThumbnailForGivenResult(findingName);
		panel.addCommentFromOutputPanel(thumbnail+1, val);
		panel.enableFiltersInOutputPanel(true,false,true);
		panel.assertTrue(panel.getCommentText(1).contains(val), "Checkpoint[1/6]", "Verifying the comment for the un loaded series is present on the output panel under accepted tab.");
		panel.openAndCloseOutputPanel(false);   
		
		//load the series and open the output panel
		panel.assertEquals(cs.getAllResults().size(), resultCount+1, "Checkpoint[2/6]", "Verified that new clone is created after adding comment when Accepted button is disable.");
		panel.click(panel.getViewPort(2));
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_3");
		
		panel.selectFindingFromTable(1, 1);
	    panel.assertEquals(circle.getText(circle.getTextCommentsforAllCircles(1).get(0)), val,"Checkpoint[3/6]", "Verifying the comment for finding in viewbox-1");
		WebElement marker=panel.getStateSpecificMarkerOnSlider(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR).get(0);
		panel.assertTrue(panel.verifyCommentOnScrollSlider(marker, findingName, val), "Checkpoint[4/6]","Verifying the comment for finding on scroll slider in viewbox-1");
		panel.assertTrue(panel.verifyCommentTextInFindingMenu(1, 1, val), "Checkpoint[5/6]","Verifying the comment for finding in finding menu in viewbox-1");
		    
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.getCommentText(1).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), val, "Checkpoint[6/6]", "Verifying the comment for finding in Output panel is displayed after reload of viewer.");
					
	}
	
	@Test(groups = { "Chrome", "IE11", "Edge", "Positive","BVT","US2284","F1125","E2E"})
	public void test30_US2284_TC9924_verifyStateCommentAddition() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify adding comments from Output Panel to Pending and Rejected findings.");

		// Loading the patient on viewer
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient, 1);
	
		panel=new OutputPanel(driver);
		circle = new CircleAnnotation(driver);
		ContentSelector cs = new ContentSelector(driver);

		panel.enableFiltersInOutputPanel(true,true,true);
		panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(), "Checkpoint[1/19]", "Verifying the comments box is displayed in output panel");
		List<String> cloneEntries = cs.getAllResults();


		panel.enableFiltersInOutputPanel(false,false,true);
		int commentsArea = 1;

		for(int i =1;i<=commentsArea;i++)
			panel.addCommentFromOutputPanel(i, text+"_"+i);
	
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[3.1/19]","verifying the thumbnail left under pending filter");
		
		panel.enableFiltersInOutputPanel(true,false,false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[3.2/19]","verifying the thumbnail displayed under accepted filter");
		
		for(int i =1;i<commentsArea;i++) {
			
			panel.assertEquals(panel.getCommentText(i).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[4."+(i)+"/19]", "Verifying the comment is displayed along with series description");
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[5/19]", "verifying the accepting icon color");
		}
		panel.openAndCloseOutputPanel(false);
		
		
		cs.assertEquals(cs.getAllResults().size(), cloneEntries.size()+1, "Checkpoint[6.1/19]", "Verifying that after adding the comment - clone is generated");
		panel.click(panel.getViewPort(1));		
		int findings = panel.getTotalAcceptedFindings(1);
		panel.assertEquals(findings,1, "Checkpoint[6.2/19]","Verifying that total accepted findings in finding menu");
		
		for(int i =1;i<=findings;i++) {			
			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i),"Checkpoint[7."+(i)+"/19]","Verifying the i Icon is displayed in finding menu");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i,text+"_"+i),"Checkpoint[8."+(i)+"/19]","Verifying the tooltip is same as comment for i Icon");
			panel.selectFindingFromTable(1,i);
			panel.assertEquals(circle.getText(circle.getTextCommentsforAllCircles(1).get(i-1)), text+"_"+i,"Checkpoint[9."+(i)+"/19]", "Verifying the comment for finding in viewer is displayed");
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, i), "Checkpoint[10."+(i)+"/19]", "verifying the finding is turned in green");
			

		}


		panel.enableFiltersInOutputPanel(true,false,false);
		for(int i =1;i<=commentsArea;i++) {

			panel.deleteCommentFromOutputPanel(i,true);
			panel.assertTrue(panel.getNotAddedCommentsIcon().isEmpty(),"Checkpoint[11."+(i)+"/19]", "Comment is not displayed when finding is in collapsed mode");
			panel.assertEquals(panel.getCommentText(i),ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT.trim(), "Checkpoint[12/19]", "Verifying the comment when finding is expanded");

		}	
		panel.openAndCloseOutputPanel(false);
		panel.click(panel.getViewPort(1));
		
		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		panel.enableFiltersInOutputPanel(false,true,false);		

		for(int i =1;i<2;i++) {
			panel.addCommentFromOutputPanel(i, text+"_"+i);
			panel.assertEquals(panel.getCommentText(i).replaceAll(ViewerPageConstants.OUTPUT_PANEL_COMMENT_TEXT, ""), text+"_"+i,"Checkpoint[13."+(i)+"/19]", "Verifying the text comment displayed when finding is in collapse mode");
			panel.assertTrue(panel.verifyRejectedStateInThumbnail(i),"Checkpoint[14/19]", "verifying the rejected thumbnail icon color");
			
		}
		
		panel.openAndCloseOutputPanel(false);
		panel.click(panel.getViewPort(1));
		findings = panel.getTotalRejectedFindings();
		panel.assertEquals(findings,1, "Checkpoint[15/19]","verifying the total rejected findings in finding menu");
		
		
		for(int i=1;i<=findings;i++) {

			panel.assertTrue(panel.verifyCommentPresenceInFindingMenu(1,i),"Checkpoint[16."+(i)+"/19]","Verifying the finding menu");
			panel.assertTrue(panel.verifyCommentTextInFindingMenu(1,i,text+"_"+i),"Checkpoint[17."+(i)+"/19]","Verifying the tooltip for iIcon");
			panel.selectFindingFromTable(1,i);
			panel.assertEquals(circle.getText(circle.getTextCommentsforAllCircles(1).get(i-1)), text+"_"+i,"Checkpoint[18."+(i)+"/19]", "Verifying the comment is displayed for finding");
			circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveRejectedGSPS(1, i), "Checkpoint[19."+(i+1)+"/19]", "Verifying the finding state is same when finding is rejected");	

		}

	}
	
	
	@AfterMethod(alwaysRun=true)
	public void afterMethod() throws SQLException {

		DatabaseMethods db=new DatabaseMethods(driver);
		db.deleteUserFeedbackPreference();
		db.deleteCloneFromSeriesLevelForCAD(resultDescription+"_"+username );
	}

  

}
