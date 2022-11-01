package com.trn.ns.test.viewer.GSPS;

import java.util.List;

import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TextAnnotationMultiLineTest extends TestBase {

	private TextAnnotation textAnno;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;
	

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String gspsFilePath1 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^NoAnchor_filepath");
	String gspsPatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath1);

	String gspsFilePath2 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
	String gspsPatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath2);
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
		
	}

	//US945: Text annotation- Text area selection resizing enhancements

	@Test(groups ={"IE11","Chrome","US945","Positive","DE1247"})
	public void test19_US945_TC4107_TC4110_DE1247_TC5347_verifyLongTextTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user is directed to new line when long comment is added in comment box"
				+ "<br> Verify user is directed to new line when long comment in comment box is being moved."
				+ "<br> Verify the text annotation is accepting as many characters as the user wants to enter.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String text ="Annotation";
		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		//		String myText ="VerifyuserisdirectedtonewlinewhenlongcommentisaddedincommentboxVerifyuserisdirectedtonewlinewhenlongcommentisaddedincommentbox";

		textAnno.drawText(1, -50, -100, text);


		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/18] : verifying the TextAnnotation#1", "Annotation is present");

		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.mouseHover(textAnno.getViewPort(1));

		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),1,"Checkpoint[2/18] : Verifying Text written on Text Annotation", "text is correctly displayed");

		List<String> lines = textAnno.getTextLinesFromTextAnnotation(1, 1);

		textAnno.assertEquals(lines.get(0),text,"Checkpoint[3/18] : Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.addResultComment(textAnno.getLineOfTextAnnotations(1).get(0),myText);		

		textAnno.moveTextAnnotation(1, 1, 450, -100);
		textAnno.dragAndReleaseOnViewer(textAnno.getLineOfTextAnnotations(1, 1), 0, 0, -70,-70);

		textAnno.assertTrue(textAnno.getCommentTextFromTextAnnotation(1, 1).size()>1,"Checkpoint[12/18] : Verifying Text written on Text Annotation", "text is correctly displayed");





	}

	@Test(groups ={"IE11","Chrome","US945","Positive"})
	public void test20_US945_TC4111_TC4112_TC4176_TC4179_verifyTextAdditionInMiddle() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user can use mouse click to jump into the middle of line to insert text in the 'Add Text' comment box"
				+ "<br> Verify user can use mouse click to jump into the middle of line to insert text in the 'Add Text' annotation"
				+ "<br> Verify pressing enter after adding text in the text annotation should save the changes"
				+ "<br> 	Verify pressing enter after adding comment should save the changes");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingText="Testing";
		String restultText ="user can use mouse click to jump into the middle of line";

		textAnno.drawText(1, -150, -150, myText);

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/4] : verifying the TextAnnotation#1", "Annotation is present");

		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.mouseHover(textAnno.getViewPort(1));

		String text = textAnno.addTextToComments(textAnno.getTextElementFromTextAnnotation(1,1), addingText, 1, true);
		textAnno.assertEquals(text,"VTestingerify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box","Checkpoint[2/4]","verified");
		textAnno.assertTrue(textAnno.getTextFromTextAnnotation(1, 1).contains(addingText),"Checkpoint[3/4]","verifying the changes are getting saved when user hits the enter");

		textAnno.addResultComment(textAnno.getTextElementFromTextAnnotation(1,1),restultText);		
		textAnno.addTextToResultComments(textAnno.resultComment.get(0), addingText, 8,true);
		textAnno.assertTrue(textAnno.getText(textAnno.resultComment.get(0)).contains(addingText),"Checkpoint[4/4]","Verify pressing enter after adding comment should save the changes");


	}

	@Test(groups ={"IE11","Chrome","US945","Positive"})
	public void test21_US945_TC4175_TC4178_verifyChangesSavedOnClickingofAnywhere() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify clicking anywhere after adding text in the text annotation should save the changes"
				+ "<br> Verify clicking anywhere after adding comment should save the changes");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingText="Testing";	
		String restultText ="user can use mouse click to jump into the middle of line";

		textAnno.drawText(1, -150, -150, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/4] verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.addTextToComments(textAnno.getTextElementFromTextAnnotation(1, 1), addingText, 14,false),"Verify user isTesting directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box","Checkpoint[2/4] ","verified");
		textAnno.click(textAnno.getViewPort(1));
		textAnno.assertTrue(textAnno.getTextFromTextAnnotation(1, 1).contains(addingText),"Checkpoint[3/4] ","Verify clicking anywhere after adding text in the text annotation should save the changes");

		//Verify clicking anywhere after adding comment should save the changes
		textAnno.addResultComment(textAnno.getAnchorLinesOfTextAnnot(1,1).get(0),restultText);	
		textAnno.addTextToResultComments(textAnno.resultComment.get(0), addingText, 5,false);
		textAnno.click(textAnno.getViewPort(1));
		textAnno.assertTrue(textAnno.getText(textAnno.resultComment.get(0)).contains(addingText),"Checkpoint[4/4] ","Verify clicking anywhere after adding comment should save the changes");


	}

	@Test(groups ={"IE11","Chrome","US945","Sanity","Positive","US1090"})
	public void test22_US945_TC4113_TC4185_US1090_TC5010_verifyResizeTextbox() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user can re-size the textbox of text annotation"
				+ "<br> 	Verify textbox auto-expand functionality when machine drawn text annotation is edited");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		ViewerLayout layout = new ViewerLayout(driver);
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box";

		textAnno.drawText(1, -150, -150, myText);

		textAnno.doubleClick(textAnno.getTextElementFromTextAnnotation(1, 1));
		textAnno.dragAndReleaseOnViewer(textAnno.resizeIconOnEditBox,0, 2, -200,30);
		//		textAnno.resizeEditbox(1, 0, 2, -200,30);
		textAnno.mouseHover(textAnno.verticalScrollBarSlider.get(0));
		textAnno.assertTrue(textAnno.verifyScrollBarPresence(),"Checkpoint[1/6]","Verifying the vertical scroll bar presence on resizing the editbox diagonally");

		textAnno.resizeEditbox(1, 0, 2, 0,10);
		textAnno.mouseHover(textAnno.verticalScrollBarSlider.get(0));
		textAnno.assertTrue(textAnno.verifyScrollBarPresence(),"Checkpoint[2/6]","Verifying the vertical scroll bar absence on resizing the editbox vertically");

		int numberOfLineBefore = textAnno.getTextLinesFromTextAnnotation(1, 1).size();

		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.mouseHover(textAnno.getViewPort(1));
		textAnno.doubleClick(textAnno.getTextElementFromTextAnnotation(1, 1));

		int FW=textAnno.getWidthOfWebElement(textAnno.getEditbox(1));
		int FH=textAnno.getHeightOfWebElement(textAnno.getEditbox(1));

		helper.browserBackAndReloadViewer(patientName,  1, 1);

		textAnno.doubleClick(textAnno.getTextElementFromTextAnnotation(1, 1));
		textAnno.assertNotEquals(FW,textAnno.getWidthOfWebElement(textAnno.getEditbox(1)),"Checkpoint[3/6]","reloading the viewer width and validating the resized editbox");
		FH=textAnno.getHeightOfWebElement(textAnno.getEditbox(1));
		textAnno.assertEquals(FH,textAnno.getHeightOfWebElement(textAnno.getEditbox(1)),"Checkpoint[4/6]","reloading the viewer height and validating the resized editbox");

		FW=textAnno.getWidthOfWebElement(textAnno.getEditbox(1));
		FH=textAnno.getHeightOfWebElement(textAnno.getEditbox(1));

		layout.selectLayout(layout.twoByTwoLayoutIcon);
		textAnno.waitForViewerpageToLoad();
		textAnno.doubleClick(textAnno.getTextElementFromTextAnnotation(1, 1));

		textAnno.assertEquals(FW,textAnno.getWidthOfWebElement(textAnno.getEditbox(1)),"Checkpoint[5/6]","reloading the viewer width and validating the resized editbox after layout change");
		textAnno.assertEquals(FH,textAnno.getHeightOfWebElement(textAnno.getEditbox(1)),"Checkpoint[6.1/6]","reloading the viewer height and validating the resized editbox after layout change");		
		textAnno.assertEquals(numberOfLineBefore, textAnno.getTextLinesFromTextAnnotation(1, 1).size(), "Checkpoint[6.2/6]" , "Verifying that on reloading viewer line breaks preserved");
	}

	@Test(groups ={"IE11","Chrome","US945","Positive"})
	public void test23_US945_TC4177_TC4180_verifyChangesSavedOnmousehover() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify hovering mouse to another viewbox after adding text in the text annotation should save the changes"
				+ "<br>	Verify hovering mouse to another viewbox after adding comment should save the changes");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingText="Testing";
		String restultText ="user can use mouse click to jump into the middle of line";

		textAnno.drawText(1, -150, -150, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"checkpoint[1/4] verifying the TextAnnotation#1", "Annotation is present");


		textAnno.assertEquals(textAnno.addTextToComments(textAnno.getTextElementFromTextAnnotation(1,1), addingText, 14,false),"Verify user isTesting directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box","checkpoint[2/4] ","verified");
		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.mouseHover(textAnno.getViewPort(1));
		textAnno.assertFalse(textAnno.getTextFromTextAnnotation(1, 1).contains(addingText),"checkpoint[3/4] ","Verify hovering mouse to another viewbox after adding text in the text annotation should save the changes");

		//Verify hovering mouse to another viewbox after adding comment should save the changes
		textAnno.mouseHover(textAnno.getViewPort(1));
		textAnno.addResultComment(textAnno.getLineOfTextAnnotations(1).get(0),restultText);	
		textAnno.addTextToResultComments(textAnno.resultComment.get(0), addingText, 5,false);
		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.assertFalse(textAnno.getText(textAnno.resultComment.get(0)).contains(addingText),"checkpoint[4/4]","Verify hovering mouse to another viewbox after adding comment should save the changes");


	}

	@Test(groups ={"IE11","Chrome","US945","Positive"})
	public void test24_US945_TC4181_TC4182_verifyChangesAreSavedOnPressOfEscape() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify pressing escape after adding text in the text annotation should discard the changes"
				+ "<br> Verify pressing escape after adding comment should discard the changes");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingText="Testing";
		String restultText ="user can use mouse click to jump into the middle of line";

		textAnno.drawText(1, -150, -150, myText);

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/4] verifying the TextAnnotation#1", "Annotation is present");

		textAnno.closeNotification();
		for(int i=2;i<5;i++)
			textAnno.closeWaterMarkIcon(i);

		textAnno.assertEquals(textAnno.addTextToComments(textAnno.getTextElementFromTextAnnotation(1, 1), addingText, 14,false),"Verify user isTesting directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box","Checkpoint[2/4]","verified");
		textAnno.pressKeys(Keys.ESCAPE);
		textAnno.assertFalse(textAnno.getTextFromTextAnnotation(1, 1).contains(addingText),"Checkpoint[3/4] ","Verify pressing escape after adding text in the text annotation should discard the changes");

		textAnno.addResultComment(textAnno.getLineOfTextAnnotations(1).get(0),restultText);	
		textAnno.addTextToResultComments(textAnno.resultComment.get(0), addingText, 5,false);
		//		Verify pressing escape after adding comment should discard the changes
		textAnno.pressKeys(Keys.ESCAPE);
		textAnno.assertFalse(textAnno.getText(textAnno.resultComment.get(0)).contains(addingText),"Checkpoint[4/4] ","Verify pressing escape after adding comment should discard the changes");


	}

	@Test(groups ={"IE11","Chrome","US945","Positive"})
	public void test25_US945_TC4185_verifyGSPSDataEditing() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify textbox auto-expand functionality when machine drawn text annotation is edited");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName2,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		String addingText="Testing";
		String restultText ="user can use mouse click to jump into the middle of line";

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/5] verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertTrue(textAnno.addTextToComments(textAnno.getTextElementFromTextAnnotation(1, 1), addingText, 14,false).contains(addingText),"Checkpoint[2/5] ","Editing the GSPS annotation and verifying the editing");
		textAnno.pressKeys(Keys.ENTER);
		textAnno.assertTrue(textAnno.getTextFromTextAnnotation(1, 1).contains(addingText),"Checkpoint[3/5] ","Verifying the changes are saved after pressing enter key");
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, true), "Checkpoint[4/5] ", "Verifying the annotation is changed to accepted state");

		textAnno.closeNotification();
		for(int i=2;i<5;i++)
			textAnno.closeWaterMarkIcon(i);

		textAnno.addResultComment(textAnno.getLineOfTextAnnotations(1).get(0),restultText);	
		textAnno.addTextToResultComments(textAnno.resultComment.get(0), addingText, 5,false);

		//		Verify pressing escape after adding comment should discard the changes
		textAnno.pressKeys(Keys.ENTER);
		textAnno.assertTrue(textAnno.getText(textAnno.resultComment.get(0)).contains(addingText),"Checkpoint[5/5] ","Adding result comment and editing the comment and validating the changes are saved pressing the enter key");


	}

	@Test(groups ={"IE11","Chrome","US1090","Positive"})
	public void test34_US1090_TC5008_TC5009_verifyLongTextOverflow() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that If text does not fit in the specified bounding box then let the text overflow the bounding box.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);	
		ViewerLayout layout = new ViewerLayout(driver);
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		textAnno.drawText(1, -150, -150, myText);

		textAnno.assertTrue(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).size() > 1, "Checkpoint[1/1]", "Verified that text is multiline comment");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertTrue(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).size() > 1, "Checkpoint[1/1]", "Verified that text is multiline comment");

		helper.browserBackAndReloadViewer(patientName,  1, 1);	

		textAnno.assertEquals(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).size() , 2, "Checkpoint[1/1]", "Verified that text is multiline comment");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertEquals(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).size() , 1, "Checkpoint[1/1]", "Verified that text is multiline comment");

	}

}


