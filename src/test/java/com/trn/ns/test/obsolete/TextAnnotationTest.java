//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.AWTException;
//import java.io.IOException;
//import java.util.List;
//
//import org.openqa.selenium.Keys;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.PointAnnotation;
//
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class TextAnnotationTest extends TestBase {
//
//	private TextAnnotation textAnno;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//	private ViewerPage viewerPage;
//	private TextAnnotation textAnn;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//
//	String ah4FilePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, ah4FilePath);
//
//	String gspsFilePath1 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^NoAnchor_filepath");
//	String gspsPatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath1);
//
//	String gspsFilePath2 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
//	String gspsPatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath2);
//
//	String gspsFilePath3 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String gspsPatientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath3);
//
//	String gspsFilePath4 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
//	String gspsPatientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, gspsFilePath4);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	//Obsolete
//	//@Test(groups ={"IE11","Chrome","US945"})
//	public void test18_US945_TC4106_verifyLongTextandLayoutChangeOnTextAnnotation() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify user is directed to new line when long text is added in text annotation");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		textAnno = new TextAnnotation(driver);
//		textAnno.waitForViewerpageToLoad();
//
//		textAnno.selectTextArrowFromRadialMenu(1);
//
//		String myText ="Verify user is directed to new line when long text is added in text annotation";
//
//		textAnno.drawText(1, 0, 0, myText);
//
//		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/11] : verifying the TextAnnotation#1", "Annotation is present");
//
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),3,"Checkpoint[2/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(0),"Verify user is directed to new l","Checkpoint[3/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(1),"ine when long text is added in","Checkpoint[4/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(2),"text annotation","Checkpoint[5/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//
//		textAnno.selectLayout(textAnno.threeByThreeLayoutIcon);
//		textAnno.waitForViewerpageToLoad();
//		textAnno.mouseHover(textAnno.getViewPort(2));
//		textAnno.mouseHover(textAnno.getViewPort(1));
//
//		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[6/11] :verifying the TextAnnotation#1", "Annotation is present");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),3,"Checkpoint[7/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(0),"Verify user is directed to new l","Checkpoint[8/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(1),"ine when long text is added in","Checkpoint[9/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).get(2),"text annotation","Checkpoint[10/11] :Verifying Text written on Text Annotation", "text is correctly displayed");
//
//
//		textAnno.compareElementImage(protocolName, textAnno.viewboxImage1, "Checkpoint[11/11] :On Layout 3 x3 change : text annotation should remain", "test18_checkpoint1");
//
//
//
//	}
//
//	
//	@Test(groups ={"IE11","Chrome","US945"})
//	public void test19_US945_TC4107_TC4108_TC4109_TC4110_verifyLongTextTextAnnotation() throws IOException, AWTException, InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify user is directed to new line when long comment is added in comment box"
//				+ "<br> Verify user is directed to new line when text annotation is being moved"
//				+ "<br>Verify text annotation containing long text after changing layout"
//				+ "<br> Verify user is directed to new line when long comment in comment box is being moved.");
//
//		//Loading the patient on viewer
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(patientName);
//
//		
//		studyPage.waitForSingleStudyToLoad();
//		studyPage.clickOntheFirstStudy();
//
//		textAnno = new TextAnnotation(driver);
//		textAnno.waitForViewerpageToLoad();
//
//		textAnno.selectTextArrowFromRadialMenu(1);
//
//		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
//		//		String myText ="VerifyuserisdirectedtonewlinewhenlongcommentisaddedincommentboxVerifyuserisdirectedtonewlinewhenlongcommentisaddedincommentbox";
//
//		textAnno.drawText(1, 50, 50, myText);
//
//		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/18] : verifying the TextAnnotation#1", "Annotation is present");
//
//		textAnno.mouseHover(textAnno.getViewPort(2));
//		textAnno.mouseHover(textAnno.getViewPort(1));
//
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),6,"Checkpoint[2/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//
//		List<String> lines = textAnno.getTextLinesFromTextAnnotation(1, 1);
//
//		textAnno.assertEquals(lines.get(0),"Verify user is directed to new l","Checkpoint[3/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(lines.get(1),"ine when long comment is ad","Checkpoint[4/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(lines.get(2),"ded in comment box Verify us","Checkpoint[5/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(lines.get(3),"er is directed to new line whe","Checkpoint[6/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(lines.get(4),"n long comment is added in c","Checkpoint[7/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.assertEquals(lines.get(5),"omment box","Checkpoint[8/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//
//		textAnno.compareElementImage(protocolName, textAnno.viewboxImage1, "Checkpoint[9/18] : Creating annotation near to viewbox should not distort the text annotation", "test19_checkpoint1");
//
//		textAnno.click(textAnno.getTextAnnotation(1, 1));
//		textAnno.assertTrue(textAnno.verifyPresenceOfVerticalScrollbar(textAnno.getEditbox(1)),"Checkpoint[10/18] :","verifying the vertical scroll bar presence");
//		textAnno.assertEquals(textAnno.getEditbox(1).getText(),myText,"Checkpoint[11/18] :","verifying the text presence in edit box");
//
//
//		//		Verify user is directed to new line when text annotation is being moved
//		textAnno.moveTextAnnotation(1, 1, 390, -100);
//		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),10,"Checkpoint[12/18] : Verifying Text written on Text Annotation", "text is correctly displayed");
//		textAnno.compareElementImage(protocolName, textAnno.getViewPort(1), "Checkpoint[13/18] : Creating annotation near to viewbox should not distort the text annotation", "test19_checkpoint2");
//
//		// Annotation text X and Y coordinates and View box
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[14/18] : ","Verifying that findings menu is inside the view box in 3*3 Layout");
//
//		int FX=textAnno.getValueOfXCoordinate(textAnno.getTextAnnotation(1, 1));
//		int FY= textAnno.getValueOfYCoordinate(textAnno.getTextAnnotation(1, 1));
//
//		int VX= textAnno.getValueOfXCoordinate(textAnno.viewboxImg1);
//		int VY= textAnno.getValueOfYCoordinate(textAnno.viewboxImg1);
//
//		// Annotation text Width and Height  and Viewbox.
//
//		int  FW=textAnno.getWidthOfWebElement(textAnno.getTextAnnotation(1, 1));
//		int  FH=textAnno.getHeightOfWebElement(textAnno.getTextAnnotation(1, 1));
//
//		int VW=	textAnno.getWidthOfWebElement(textAnno.viewboxImg1);
//		int VH=	textAnno.getHeightOfWebElement(textAnno.viewboxImg1);
//
//		textAnno.assertTrue(FX>VX && VW>FW && FY>VY&&FH<VH, "Checkpoint[15/18] : Verifying that the editbox is inside the view box", "editbox is inside the view box");
//
//		//		TC4109	Verify text annotation containing long text after changing layout
//
//		textAnno.selectLayout(textAnno.threeByThreeLayoutIcon);
//		textAnno.waitForViewerpageToLoad();
//		textAnno.mouseHover(textAnno.getViewPort(2));
//		textAnno.mouseHover(textAnno.getViewPort(1));
//
//		FX=textAnno.getValueOfXCoordinate(textAnno.getTextAnnotation(1, 1));
//		FY= textAnno.getValueOfYCoordinate(textAnno.getTextAnnotation(1, 1));
//
//		VX= textAnno.getValueOfXCoordinate(textAnno.viewboxImg1);
//		VY= textAnno.getValueOfYCoordinate(textAnno.viewboxImg1);
//
//		// Annotation text Width and Height  and Viewbox.
//
//		FW=textAnno.getWidthOfWebElement(textAnno.getTextAnnotation(1, 1));
//		FH=textAnno.getHeightOfWebElement(textAnno.getTextAnnotation(1, 1));
//
//		VW=	textAnno.getWidthOfWebElement(textAnno.viewboxImg1);
//		VH=	textAnno.getHeightOfWebElement(textAnno.viewboxImg1);
//
//		textAnno.assertTrue(FX>VX && VW>FW && FY>VY&&FH<VH, "Checkpoint[16/18] : Verifying that the editbox is inside the view box", "editbox is inside the view box");
//		textAnno.click(textAnno.getTextAnnotation(1, 1));
//		textAnno.assertTrue(textAnno.verifyPresenceOfVerticalScrollbar(textAnno.getEditbox(1)),"Checkpoint[17/18] : ","verifying the vertical scrollbar");
//		textAnno.assertTrue(textAnno.verifyPresenceOfHorizontalScrollbar(textAnno.getEditbox(1)),"Checkpoint[18/18] : ","verifying the horizontal scrollbar");
//
//	}
//
//
//
//}
//
