package com.trn.ns.test.viewer.outputPanel;

import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TextAnnotationInOutputPanelTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerSliderAndFindingMenu findingMenu;

	private TextAnnotation textAnno;	
	private MeasurementWithUnit lineWithUnit;
	private HelperClass helper;

	// Get Patient Name
	String AH4_FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String AH4_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, AH4_FilePath);
	
	String Imbio_Texture_FilePath = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String Imbio_Texture_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Imbio_Texture_FilePath);

	private OutputPanel panel;
	private CircleAnnotation circle;
	String mytext1 ="Testing_Testing";

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1091", "Positive","US2284","F1125"})
	public void test01_US1091_TC5000_TC5001_TC5002_TC5003_TC5071_US2284_TC9967_verifyMultiLineTextAndCommentsInOuputPanel() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that multi line Text should be supported in output panel."
				+ "<br>Verify that when content of text does not fit in the space available for multiline text then scroll should be displayed"
				+ "<br>Reg- Verify multiline text supported in ouput panel, Comment, Finding Dropdown list comment icon tool tip."
				+ "<br>Verify that text measurement should not get displayed in thumbnail"
				+ "<brVerify that text measurement should get displayed in panel at right side"
				+ "<br> Verify Text annotation text is not editable from Output Panel.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(AH4_Patient, 1);
		findingMenu = new ViewerSliderAndFindingMenu(driver);
		textAnno = new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;
		panel.doubleClickOnViewbox(1);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingTextToNextLine="Testing multi line";
		String commentText ="user can use mouse click to jump into the middle of line";

		//drawing multiline text annotation
		textAnno.drawMultiLineText(1, -50, -50, myText, addingTextToNextLine);
		
		//Open output panel
		panel.enableFiltersInOutputPanel(true, false, false);	
		
		List<String> textAnnoText = textAnno.getTextLinesFromTextAnnotation(1, 1);
		int lines = textAnnoText.size();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/6]","Verifying the text is displayed multiline in output panel");
		List<String> textFromOP = panel.getMultiLineTextEntriesInOP(1);
		textAnno.assertTrue(textAnnoText.size()>1, "Verified that text is multiline text is same as viewbox","verified");
		textAnno.assertEquals(myText,textFromOP.get(0), "Verified that multiline text values as same as viewbox in ouptput panel","verified");
		textAnno.assertEquals(addingTextToNextLine,textFromOP.get(1), "Verified that multiline text values as same as viewbox in ouptput panel","verified");
		
		findingMenu.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[2/6]: verifying the thumbnail - as no text should be displayed", "test01_01");
		
			
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[4/6]","Verifying the comment is displayed multiline in output panel");
		textAnno.addMultiLineResultComment(textAnno.getTextElementFromTextAnnotation(1, 1),commentText,addingTextToNextLine);
		textAnno.assertTrue(textAnno.getTextLinesFromCommentOfTextAnnotation(1, 1).size() > 1, "Verified that text is multiline comment","verified");		
		
		panel.openAndCloseOutputPanel(true);
		panel.waitForOutputPanelToLoad();
		textAnnoText = textAnno.getTextLinesFromCommentOfTextAnnotation(1, 1);
		textFromOP = panel.getMultiLineCommenEntriesInOP(1);		
		textAnno.assertEquals(textAnnoText.size()+1,textFromOP.size(), "Verified that text is multiline comment is same as viewbox","verified");		
		for(int i =0; i<textAnnoText.size();i++)
			textAnno.assertEquals(textAnnoText.get(i),textFromOP.get(i+1),"Verified that multiline comment values as same as viewbox in ouptput panel","verified");
	
		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),lines, "Verified that text is multiline text is same as viewbox","verified");
		textAnno.assertEquals(commentText,textFromOP.get(1), "Verified that multiline text values as same as viewbox in ouptput panel","verified");
		textAnno.assertEquals(addingTextToNextLine,textFromOP.get(2), "Verified that multiline text values as same as viewbox in ouptput panel","verified");
		
		findingMenu.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[6/6]: verifying the thumbnail - as no text should be displayed post adding comments", "test01_02");
		String[] title = findingMenu.getCommentTextFromFindingMenu(1, ViewerPageConstants.TEXT_FINDING_NAME).split("\n");
		 textAnno.assertEquals(title.length,textFromOP.size()-1,"Verifying the info icon tooltip also has line preseved","verified");

		for(int j =0; j<textFromOP.size();j++) {
			
			textAnno.assertEquals(title[j],textFromOP.get(j+1),"Verified that multiline comment values as same as displayed in info icon tooltip","verified");
		}
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1033", "Positive"})
	public void test02_US1033_TC4700_verifyCommentIsnotDisplayedInThumbnail() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify comment is NOT displayed in thumbnail");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(AH4_Patient, 1);
		
		circle = new CircleAnnotation(driver);
		panel=new OutputPanel(driver) ;

		String comment = "Circle Comment";
		circle.selectCircleFromQuickToolbar(1);
		
		circle.drawCircle(1, 50, 50, -100, -100);
		circle.addResultComment(circle.getAllCircles(1).get(0), comment);
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[1/2]: Verifying that now no text is displayed", "test02_01");
		
		panel.assertEquals(panel.getMultiLineCommenEntriesInOP(1).get(1),comment,"Checkpoint[2/2]","Verifying the comment is displayed at right hand side");
		

		
	}
	
	@Test(groups ={"firefox","Chrome","IE11","US1028","Positive"})
	public void test03_US1028_TC4666_TC4668_VerifyLinearMeasurementTextInOutPutPanel() throws Exception{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify linear measurement text is visible in Output panel thumbnail"+
				"<br> Verify linear measurement text is visible in Output panel thumbnail when browser resized");

		//Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(AH4_Patient, 1);

		lineWithUnit = new MeasurementWithUnit(driver);
		panel=new OutputPanel(driver) ;

		//Select Linear measurement from Radial Menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);

		//Draw linear measurement in viewbox 1
		lineWithUnit.drawLine(1, 50, 0, 100, 0);

		panel.enableFiltersInOutputPanel(true, false, false);
		
		panel.assertEquals(lineWithUnit.getLinearMeasurementsText(1).get(0).getText(), panel.getMeasurementTextFromThumnail(1), "Checkpoint[1/3]", "Verifying the value in thumbnail of linear measurement with the view box linear measurement.");
		
		//Implementing TC4668 Browser resizing and Verifying the linear measurement text.

		panel.resizeBrowserWindow(800, 500);
		panel.assertEquals(lineWithUnit.getLinearMeasurementsText(1).get(0).getText(), panel.getMeasurementTextFromThumnail(1), "Checkpoint[2/3]", "After browser to minimum, verifying the value in thumbnail of linear measurement with the view box linear measurement.");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[3/3]: verifying in thumbnail the dotted line of linear measurement", "test03_01");
		panel.openAndCloseOutputPanel(false);


	}
	
	@Test(groups ={"firefox","Chrome","IE11","US1028","Positive"})
	public void test04_DE1389_TC5713_VerifyDottedLineInThumbnail() throws Exception{

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify the dotted line of text annotation handle is displayed in the thumbnail in the output panel");

		//Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(AH4_Patient, 1);
		
		textAnno = new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;

		textAnno.selectTextArrowFromQuickToolbar(1);

		//Drawing text annotation
		textAnno.drawText(1, 50, 50, mytext1);

		panel.enableFiltersInOutputPanel(true, false, false);
	
		// Verifying that dotted line of Text annotation is present in thumbnail
		panel.assertTrue(panel.isElementPresent(panel.getLineOfTextAnnotationsInOutPutPanel(1)), "Checkpoint[1/1]:Verifying that dotted line of Text annotation present in thumbnail", "Dotted line is present");
	
	}

	@Test(groups ={"firefox","Chrome","IE11","DE1812","Positive"})
	public void test05_DE1812_TC7349_VerifyTextAnnotationOnViewerAsWellAsInOutputPanel() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify dotted line for text annotation is visible while creating it and also visible in the thumbnails on  finding panel and in output panel after it is created.");

		//Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(Imbio_Texture_patientName, 1);
	
		textAnno = new TextAnnotation(driver);
		panel=new OutputPanel(driver) ;
		panel.waitForViewerpageToLoad(3);

		textAnno.selectTextArrowFromQuickToolbar(3);
		
		//Drawing text annotation
		textAnno.drawTextAnnotationWithNoText(3, 50, 50);
		
		textAnno.drawText(3,-30 , -120, mytext1);
		panel.assertTrue(textAnno.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(3, 1, false), "Checkpoint[1/10]", "Verified that text annotation is current active accepted GSPS.");
		panel.assertEquals(textAnno.getAnchorLinesOfTextAnnot(3, 1).size(), 2, "Checkpoint[2/10]", "Verified dotted line of text annotation on viewer page.");
		panel.assertEquals(textAnno.getTextFromTextAnnotation(3, 1), mytext1, "Checkpoint[3/10]", "Verified text of text annotation on viewer page.");
		
		panel.openFindingTableOnBinarySelector(3);
		panel.assertEquals(panel.findingsText.size(), 1, "Checkpoint[4/10]", "Verified finding count from finding table.");
		panel.assertEquals(panel.getText(panel.findingsText.get(0)), ViewerPageConstants.TEXT_FINDING_NAME, "Checkpoint[5/10]", "Verified text of text annotation finding in Finding menu.");

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.isElementPresent(panel.getLineOfTextAnnotationsInOutPutPanel(1)), "Checkpoint[6/10]", "Verified that dotted line present in thumbnail Output panel.");
		panel.assertTrue(panel.getMultiLineTextEntriesInOP(1).contains(mytext1) , "Checkpoint[8/10]", "Verified text of text annotation in Output panel.");
	    panel.openAndCloseOutputPanel(false);
	    
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(Imbio_Texture_patientName, 1, 1);
	
        panel.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).size(), 2, "Checkpoint[9/10]", "Verified that dotted line for the text annotation on viewer reload.");
        panel.assertEquals(textAnno.getTextFromTextAnnotation(1, 1), mytext1, "Checkpoint[10/10]", "Verified text of text annotation after viewer page reload.");
        
		
	}
	
	
	

}



