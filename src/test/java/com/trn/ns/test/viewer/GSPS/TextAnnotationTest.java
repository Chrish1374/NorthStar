package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TextAnnotationTest extends TestBase {

	private TextAnnotation textAnno;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;
	private ViewerPage viewerPage;
	private TextAnnotation textAnn;

	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String gspsFilePath1 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^NoAnchor_filepath");
	String gspsPatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath1);

	String gspsFilePath2 =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
	String gspsPatientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath2);

	String gspsFilePath3 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String gspsPatientName3 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath3);

	String gspsFilePath4 =Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^ELLIPSE_filepath");
	String gspsPatientName4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsFilePath4);

	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String filePathBoneAge = Configurations.TEST_PROPERTIES.get("Boneage_filepath");
	String patientName_BoneAge = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathBoneAge);
	
	String filePathAH4PDF = Configurations.TEST_PROPERTIES.get("AH4_pdf_filepath");
	String patientName_AH4_PDF = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAH4PDF);
	private HelperClass helper;



	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"IE11","Chrome","US525","Positive","US2325","F1090","E2E"})
	public void test01_US525_TC1851_US2325_TC9777_verifyTextAnnotationIsPresentOnContextMenu() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Text annotation icon should be present in context menu"
				+ "<br> Verify GSPS annotations icons and its functionality from quick toolbar");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);
		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.TEXT_ARROW),"Checkpoint[1/2]", "Text Annotation icon is selected");

		ViewerToolbar toolbar = new ViewerToolbar(driver);
		toolbar.assertTrue(toolbar.checkCurrentSelectedIconOnViewer(ViewerPageConstants.TEXT),"Checkpoint[2/2]", "Verifying Text icon is selected in viewer bar");
	


	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test02_US525_TC1852_TC1857_US893_TC3649_DE1818_TC7365_verifyDrawTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user should be able to draw/place a Text annotation "
				+ "<br> Verify that annotaion tool keep selected even after annoation drawn in viewbox."+
				"<br>Verify that user should be able to draw text annotation on single click"
				+ "<br>Verify that after closing the conflict watermark, user is able to draw text annotation further on any viewbox.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that Text annotation is selected and enabled");

		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.TEXT_ARROW),"Verifying Text icon is selected", "Text icon is selected");

		//		textAnno.closeContextMenu();

		String myText ="TextAnnotation_FirstViewbox";

		textAnno.drawText(1, 10, 10, myText);
		textAnno.closingConflictMsg();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");


		myText ="TextAnnotation_SecViewbox";

		textAnno.drawText(2, 10, 10, myText);
		textAnno.closingConflictMsg();
		textAnno.assertEquals(textAnno.getTextAnnotations(2).size(),1,"verifying the TextAnnotation#2", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(2, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that Text annotation is selected and enabled");
		textAnno.openQuickToolbar(textAnno.getViewPort(2));
		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.TEXT_ARROW),"Verifying Text icon is selected", "Text icon is selected");
		//		textAnno.closeContextMenu();


	}
	
	@Test(groups ={"IE11","Chrome","US525","Positive","DE1250"})
	public void test02_US525_DE1250_TC5613_verifyTextAnnotationDoesNotDisappearAfterSelectDeselect() throws InterruptedException 
	{
		int[] coordinates = new int[] {25,44,20,-32,37,-23,37,-9,-7,-34,12,-25,43,-27,5,-36,-14,-37,-62,6,-46,23,-41,37,-42,42,-25,33};
		String myText ="TextAnnotation_FirstViewbox";
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the text annotation is not deleted after selecting/deselecting text annotation multiple times");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();
		textAnno.doubleClick(textAnno.getViewPort(1));
		textAnno.selectTextArrowFromQuickToolbar(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying that Text annotation is selected and enabled");

		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.TEXT_ARROW),"Verifying Text icon is selected", "Text icon is selected");		
		textAnno.drawText(1, 10, 10, myText);
		textAnno.closingConflictMsg();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		for(int i=0;i<coordinates.length;i=i+2)
		{

			textAnno.clickAt(coordinates[i], coordinates[i+1]);
			textAnno.doubleClick(textAnno.getTextElementFromTextAnnotation(1, 1));
			textAnno.click(textAnno.getViewPort(1));
		}
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that text annotation is not deleted after multple selects/deselects");
		textAnno.assertEquals(textAnno.getTextLinesFromTextAnnotation(1, 1).size(),1,"Verifying the existing text annotation not deleted", "One text annotation is present");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying the user entered text for the text annotation");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1), myText, "Verifying the entered text", "Entered text is TextAnnotation_FirstViewbox");
	}

	@Test(groups ={"IE11","Chrome","US525","Negative","BVT"})
	public void test03_US525_TC1853_verifyTextAnnotationNotDrawnOnPDFJPG() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is not able to draw text annotation on PDF/JPEG/PNG..");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(imbio_PatientName, 1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;

		textAnno.takeElementScreenShot(textAnno.getPDFViewbox(2), newImagePath+"/goldImages/"+imbio_PatientName+"_TextAnnot_pdf.png");

		// verifying on PDF
		textAnno.drawText(2,50,50,"ABC");
		textAnno.closingConflictMsg();
		textAnno.mouseHover(textAnno.getViewPort(1));
		textAnno.takeElementScreenShot(textAnno.getPDFViewbox(2), newImagePath+"/actualImages/"+imbio_PatientName+"_TextAnnot_pdf.png");


		String expectedImagePath = newImagePath+"/goldImages/"+imbio_PatientName+"_TextAnnot_pdf.png";
		String actualImagePath = newImagePath+"/actualImages/"+imbio_PatientName+"_TextAnnot_pdf.png";
		String diffImagePath = newImagePath+"/actualImages/diffImage_"+imbio_PatientName+"_TextAnnot_pdf.png";

		boolean cpStatus =  textAnno.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		textAnno.assertTrue(cpStatus, "The actual and Expected image should be same","Images are same hence no more text annotation present");

		helper.browserBackAndReloadViewer(patientName_BoneAge,  1, 1);
		
		textAnno.selectTextArrowFromQuickToolbar(4);
		textAnno.click(textAnno.getViewPort(1));
		// verifying on JPG
		textAnno.takeElementScreenShot(textAnno.getViewPort(1), newImagePath+"/goldImages/"+patientName_BoneAge+"_TextAnnot_jpg.png");

		textAnno.drawText(1,50,50,"ABC");
		textAnno.closingConflictMsg();
		textAnno.takeElementScreenShot(textAnno.getViewPort(1), newImagePath+"/actualImages/"+patientName_BoneAge+"_TextAnnot_jpg.png");


		expectedImagePath = newImagePath+"/goldImages/"+patientName_BoneAge+"_TextAnnot_jpg.png";
		actualImagePath = newImagePath+"/actualImages/"+patientName_BoneAge+"_TextAnnot_jpg.png";
		diffImagePath = newImagePath+"/actualImages/diffImage_"+patientName_BoneAge+"_TextAnnot_jpg.png";

		cpStatus =  textAnno.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		textAnno.assertTrue(cpStatus, "The actual and Expected image should be same","Images are same hence no more text annotation present");




	}

	@Test(groups ={"IE11","Chrome","US525","US570","US2329","BVT","F1090","E2E"})
	public void test04_US525_TC1854_US570_TC1762_US2329_TC10165_verifyTextAnnotationDrawInAnyDirection() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can move text annotation in any direction using line handlers "
				+ "<br> Verify that user can put multiple text annotation on same image.<br>"+
				"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		String myText="Automation_text";

		textAnno.selectTextArrowFromQuickToolbar(1);

		textAnno.drawText(1,250,200,myText+"_1");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/22] : verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText+"_1","Checkpoint[2/22] : Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.drawText(1,250,-200,myText+"_2");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),2,"Checkpoint[3/22] : verifying the TextAnnotation#2", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 2),myText+"_2","Checkpoint[4/22] : Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.drawText(1,-300,-200,myText+"_3");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),3,"Checkpoint[5/22] : verifying the TextAnnotation#3", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 3),myText+"_3","Checkpoint[6/22] : Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.drawText(1,-300, 100,myText+"_4");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),4,"Checkpoint[7/22] : verifying the TextAnnotation#4", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 4),myText+"_4","Checkpoint[8/22] : Verifying Text written on Text Annotation", "text is correctly displayed");
		textAnno.closingConflictMsg();
		
		textAnno.mouseHover(textAnno.getViewPort(1));

		textAnno.selectTextAnnotation(1, 1);

		float beforeX1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X1));
		float beforeY1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y1));
		float beforeX2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X2)); 
		float beforeY2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y2));

		textAnno.dragAndReleaseOnViewer(textAnno.getSelectedTextAnnotLineHandles(1, 1).get(0), 0, 0, -200, -200);

		float afterX1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X1));
		float afterY1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y1));
		float afterX2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X2)); 
		float afterY2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y2));

		textAnno.assertNotEquals(beforeX1,afterX1,"Checkpoint[9/22]","Verify X1 coordinate of line change on move");
		textAnno.assertNotEquals(beforeY1,afterY1,"Checkpoint[10/22]","Verify Y1 coordinate of line change on move");
		textAnno.assertEquals(beforeX2,afterX2,"Checkpoint[11/22]","Verify X2 coordinate of line change on move");
		textAnno.assertEquals(beforeY2,afterY2,"Checkpoint[12.1/22]","Verify Y2 coordinate of line change on move");
		textAnno.assertTrue((beforeX2 - beforeX1)<(afterX2 - afterX1), "Checkpoint[12.2/22]", "verifying the size is increased");	

		textAnno.dragAndReleaseOnViewer(textAnno.getSelectedTextAnnotLineHandles(1, 1).get(0), 0, 0, -250, -100);

		float after1X1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X1));
		float after1Y1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y1));
		float after1X2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X2)); 
		float after1Y2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y2));

		textAnno.assertNotEquals(after1X1,afterX1,"Checkpoint[13/22]","Verify X1 coordinate of line change on move");
		textAnno.assertNotEquals(after1Y1,afterY1,"Checkpoint[14/22]","Verify Y1 coordinate of line change on move");
		textAnno.assertEquals(after1X2,afterX2,"Checkpoint[15/22]","Verify X2 coordinate of line change on move");
		textAnno.assertEquals(after1Y2,afterY2,"Checkpoint[16/22]","Verify Y2 coordinate of line change on move");
		textAnno.assertTrue((after1X2 - after1X1)>(afterX2 - afterX1), "Checkpoint[17/22]", "Verifying the size is increased");	


		textAnno.dragAndReleaseOnViewer(textAnno.getSelectedTextAnnotLineHandles(1, 1).get(0), 0, 0, 50, 50);

		afterX1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X1));
		afterY1=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y1));
		afterX2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.X2)); 
		afterY2=Float.parseFloat(textAnno.getLineOfTextAnnotations(1, 1).getAttribute(ViewerPageConstants.Y2));

		textAnno.assertNotEquals(after1X1,afterX1,"Checkpoint[18/22]","Verify X1 coordinate of line change on move");
		textAnno.assertNotEquals(after1Y1,afterY1,"Checkpoint[19/22]","Verify Y1 coordinate of line change on move");
		textAnno.assertEquals(after1X2,afterX2,"Checkpoint[20/22]","Verify X2 coordinate of line change on move");
		textAnno.assertEquals(after1Y2,afterY2,"Checkpoint[21/22]","Verify Y2 coordinate of line change on move");
		textAnno.assertTrue((after1X2 - after1X1)>(afterX2 - afterX1), "Checkpoint[22/22]", "Verifying the size is changed");	


	}

	@Test(groups ={"IE11","Chrome","US525","US570","DE902","Positive"})
	public void test05_US525_TC1856_US570_TC1764_DE902_TC3504_verifyNoEditingAndNoColorChange() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that text annotation position should get save after moving"
				+ "<br> Verify annotation can't edit(resize) text annotation but can resize line"
				+ "<br> Verify that font of the text annotation should be displayed in green color and Font type and size should be as per Northstar guidlines."
				+ "<br> Verify that text annotation position should get save after moving");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText="TEXT_123";
		textAnno.drawText(1,-50,-50,myText);
		textAnno.closingConflictMsg();

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"Checkpoint[1/29] : verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"VCheckpoint[2/29] : erifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.updateTextOnTextAnnotation(1, 1, myText+"_updated");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText+"_updated","Checkpoint[3/29] : Verifying Text written on Text Annotation", "text is correctly updated");
		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"verifying color","verified");

		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.mouseHover(textAnno.getViewPort(1));

		int height= textAnno.getTextAnnotation(1,1).getSize().height;
		int width = textAnno.getTextAnnotation(1,1).getSize().width;
		int x = textAnno.getTextAnnotation(1,1).getLocation().x;
		int y = textAnno.getTextAnnotation(1,1).getLocation().y;

		textAnno.moveTextAnnotation(1,1,-70,-70);

		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[11.a/29] : verifying color","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.SHADOW_COLOR,"Checkpoint[11.b/29] : verifying color","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[11.c/29] : verifying color","verified");

		textAnno.dragAndReleaseOnViewer(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0),0,0, 100,100);

		int x1 = textAnno.getTextAnnotation(1,1).getLocation().x;
		int y1 = textAnno.getTextAnnotation(1,1).getLocation().y;

		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().height, height,"Checkpoint[12/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().width, width,"Checkpoint[13/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x,"Checkpoint[14/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().y,y,"Checkpoint[15/29] : verify that after drag the text box is not moved","verified");


		//		textAnno.assertEquals(textAnno.getTextAnnotation(1, 1).getCssValue(NSGenericConstants.FILL),"rgb(0, 128, 0)","Checkpoint[16/29] : verifying color","verified");
		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[17/29] : verifying color","verified");

		helper.browserBackAndReloadViewer(patientName,  1, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().height, height,"Checkpoint[18/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().width, width,"Checkpoint[19/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x1,"Checkpoint[20/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().y,y1,"Checkpoint[21/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[23/29] : verifying color","verified");

		Header header = new Header(driver);
		header.logout();

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
		patientPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(patientName,  1);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().height, height,"Checkpoint[24/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().width, width,"Checkpoint[25/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x1,"Checkpoint[26/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().y,y1,"Checkpoint[27/29] : verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"Checkpoint[29/29] : verifying color","verified");

	}

	@Test(groups ={"IE11","Chrome","US570","Positive"})
	public void test06_US570_TC1753_TC1752_verifyTextAnnotationPostZoom() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on zoom the size of point will be same "
				+ "<br> Verify that on zoom the size of text annotation should not get changed.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText = "TEXT_ANNOTATION";

		textAnno.drawText(1,-100,-100,myText);		
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");		
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");
		textAnno.closingConflictMsg();
		
		int height= textAnno.getTextAnnotation(1,1).getSize().height;
		int width = textAnno.getTextAnnotation(1,1).getSize().width;
		int x = textAnno.getTextAnnotation(1,1).getLocation().x;
		int y = textAnno.getTextAnnotation(1,1).getLocation().y;

		textAnno.selectZoomFromQuickToolbar(1);

		textAnno.dragAndReleaseOnViewer(textAnno.getViewPort(1), 0, 0, 0, -100);

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");		
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"verifying color","verified");
		textAnno.assertTrue(textAnno.getAnchorLinesOfTextAnnot(1,1).get(0).getSize().height>height,"verify that after drag the text box is not moved","verified");
		textAnno.assertTrue(textAnno.getAnchorLinesOfTextAnnot(1,1).get(0).getSize().width>width,"verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x,"verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().y, y,"verify that after drag the text box is not moved","verified");

		textAnno.dragAndReleaseOnViewer(textAnno.getViewPort(1), 0, 0, 0, 50);		
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");		
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"verifying color","verified");
		textAnno.assertTrue(textAnno.getAnchorLinesOfTextAnnot(1,1).get(0).getSize().height< height,"verify that after drag the text box is not moved","verified");
		textAnno.assertTrue(textAnno.getAnchorLinesOfTextAnnot(1,1).get(0).getSize().width<width,"verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x,"verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getLocation().y, y,"verify that after drag the text box is not moved","verified");

	}

	@Test(groups ={"IE11","Chrome","US525","US570","Positive"})
	public void test07_US525_TC1860_US570_TC1753_verifyTextAnnPostPAN() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that when user PAN the image size of Text annotation should not change and it should move with image "
				+ "<br> Verify that zooming / panning keeps the text and line at the same image coordinates");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText = "TEXT_ANNOTATION";

		textAnno.drawText(1,50,50,myText);		
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");		
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");
		textAnno.closingConflictMsg();
		int height= textAnno.getTextAnnotation(1,1).getSize().height;
		int width = textAnno.getTextAnnotation(1,1).getSize().width;
		int x = textAnno.getTextAnnotation(1,1).getLocation().x;
		int y = textAnno.getTextAnnotation(1,1).getLocation().y;

		textAnno.selectPanFromQuickToolbar(textAnno.getViewPort(1));

		textAnno.dragAndReleaseOnViewer(textAnno.getViewPort(1), 0, 0, 50, 50);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");		
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.assertEquals(textAnno.getTextElementFromTextAnnotation(1, 1).getAttribute(NSGenericConstants.FILL),ViewerPageConstants.ACCEPTED_COLOR,"verifying color","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().height, height,"verify that after drag the text box is not moved","verified");
		textAnno.assertEquals(textAnno.getTextAnnotation(1,1).getSize().width, width,"verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().x, x,"verify that after drag the text box is not moved","verified");
		textAnno.assertNotEquals(textAnno.getTextAnnotation(1,1).getLocation().y, y,"verify that after drag the text box is not moved","verified");

	}

	@Test(groups ={"IE11","Chrome","US525","US570","Positive"})
	public void test08_US525_TC1861_TC1862_US570_TC1756_verifyTextAnnotationWithGSPSData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS : Verify that text annotation should be displayed when viewers page loaded "
				+ "<br>  GSPS: Verify that accept/reject context menu should be displayed when GSPS text annotation displayed in viewers page"
				+ "<br> Verify that display a text annotation from GSPS without an anchor point");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName1+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName1,1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),"sample text without an anchor point","verfying the text on GSPS","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).size(),0,"verifying there is no anchor line","Data has no anchor line");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertTrue(textAnno.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		helper.browserBackAndReloadViewer(gspsPatientName2,  1, 1);

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),"sample text with anchor point at the top right (by burned in orientation marker)","verfying the text on GSPS","verified");

		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.SHADOW_COLOR,"verifying the line color is green","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"verifying the line color is transparent","verified");

		// GSPS: Verify that accept/reject context menu should be displayed when GSPS text annotation displayed in viewers page

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertTrue(textAnno.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

	}

	@Test(groups ={"IE11","Chrome","US525","Sanity","DE1057","Positive","BVT"})
	public void test09_US525_TC1863_TC1864_DE1057_TC4835_verifyAcceptAndRejectionOfTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS : Verify that text annotation should be rejected on clicking cross button of accept/reject radial context menu. "
				+ "<br> GSPS : Verify that text annotation should be accepted on clicking green accept button of accept/reject radial context menu."
				+ "<br> Verify that existing \"Text with Anchor\" functionality should not be broken");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName1+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName1, 1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),"sample text without an anchor point","verfying the text on GSPS","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).size(),0,"verifying there is no anchor line","Data has no anchor line");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertTrue(textAnno.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		textAnno.selectAcceptfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Accept button on radial menu");
		textAnno.assertTrue(textAnno.gspsAccept.isDisplayed(), "Verify Accept button on radial menu", "The Reject button is visible on radial menu");
		textAnno.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, false);

		textAnno.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Reject button on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is visible on radial menu");
		textAnno.verifyTextAnnotationIsCurrentRejectedInactive(1, 1, false);

		helper.browserBackAndReloadViewer(gspsPatientName2,  1, 1);


		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),"sample text with anchor point at the top right (by burned in orientation marker)","verfying the text on GSPS","verified");

		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.SHADOW_COLOR,"verifying the line color is green","verified");
		textAnno.assertEquals(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(1).getAttribute(NSGenericConstants.STROKE),ViewerPageConstants.PENDING_COLOR,"verifying the line color is transparent","verified");

		// GSPS: Verify that accept/reject context menu should be displayed when GSPS text annotation displayed in viewers page

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertTrue(textAnno.gspsPrevious.isDisplayed(), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertTrue(textAnno.gspsNext.isDisplayed(), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");

		textAnno.selectRejectfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Reject button on radial menu");
		textAnno.assertTrue(textAnno.gspsAccept.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is visible on radial menu");
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentActiveRejectedGSPS(1, 1, true),"Verify the text annotation is rejected","rejected");

		textAnno.selectAcceptfromGSPSRadialMenu();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Verify Accept button on radial menu");
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Accept button on radial menu", "The Reject button is visible on radial menu");
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true),"Verify the text annotation is accepted","accepted");


	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test10_US525_TC1865_verifyNavigationOnAcceptOrRejectTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("GSPS : Verify that on accepting or rejecting any finding, next GSPS finding should be highlighted and accept/reject radial context menu should display just above highlighted finding");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TextAnnotation_First";

		textAnno.drawText(1, 0, 0, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");


		myText ="TextAnnotation_Second";

		textAnno.drawText(1, 50, 50, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),2,"verifying the TextAnnotation#2", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 2),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.selectNextfromGSPSRadialMenu();
		textAnno.selectRejectfromGSPSRadialMenu();			
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentRejectedInactive(1, 2, true),"Verify the text annotation is rejected","rejected");

		textAnno.selectRejectfromGSPSRadialMenu();
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentRejectedInactive(1, 1, true),"Verify the text annotation is rejected","rejected");

		textAnno.selectAcceptfromGSPSRadialMenu();			
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentAcceptedInactive(1, 2, true),"Verify the text annotation is accepted","accepted");

		textAnno.selectRejectfromGSPSRadialMenu();
		textAnno.assertTrue(textAnno.verifyTextAnnotationIsCurrentPendingInactive(1, 1, true),"Verify the text annotation is pending","pending");

		helper.browserBackAndReloadViewer(gspsPatientName3,  1, 1);
		
		PointAnnotation point = new PointAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		//		myText ="TextAnnotation_First";

		textAnno.drawText(1, 0, 0, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.selectRejectfromGSPSRadialMenu(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0));			

		textAnno.assertEquals(textAnno.getCurrentScrollPositionOfViewbox(1), 6, "Verify next slice having GSPS object load in Viewbox1 as there is no GSPS object in current slice", "The current slice in ViewBox 1 is "+textAnno.getCurrentScrollPositionOfViewbox(1));
		textAnno.assertTrue(textAnno.gspsReject.isDisplayed(), "Verify Reject button on radial menu", "The Reject button is visible on radial menu");
		textAnno.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1,1), "Verify first point annotation is current Active GSPS object", "The first point is current active GSPS object");



	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test11_US525_TC1893_verifyCineIsNotPlayedForCineTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cine should not play on entering \"C\" text in text annotation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="c";

		int pos = textAnno.getCurrentScrollPositionOfViewbox(1);

		textAnno.drawText(1, 0, 0, myText);

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		textAnno.assertEquals(textAnno.getCurrentScrollPositionOfViewbox(1), pos, "Verify cine is not performed", "Cine is not performed");



	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test12_US525_TC1894_verifyWWWLForWinTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that window level should not apply on image when user press \"W\" from keyboard as text in text annotation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="w";

		textAnno.drawText(1, 0, 0, myText);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");
		textAnno.closingConflictMsg();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint", "Verifying that Text annotation is selected and enabled");
		textAnno.openQuickToolbar(textAnno.getViewPort(2));
		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.TEXT_ARROW),"Verifying Text icon is selected", "Text icon is selected");
		textAnno.drawText(1, 50, 50, "Test");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),2,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 2),"Test","Verifying Text written on Text Annotation", "text is correctly displayed");

	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test13_US525_TC1926_verifyImageOrientationTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that window level should not apply on image when user press \"W\" from keyboard as text in text annotation.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);
		
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TEXT_ANNOTATION";

		textAnno.drawText(1, 0, 0, myText);
		textAnno.closeNotification();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");
		ViewerOrientation orin = new ViewerOrientation(driver);
		orin.flipSeries(orin.getTopOrientationMarker(1));
		textAnno.compareElementImage(protocolName, textAnno.getViewbox(1), "Top Orientation Marker: Image flipped horizontally.", "US525_TC1296_checkpoint1");


	}

	@Test(groups ={"IE11","Chrome","US525","Positive"})
	public void test14_US525_verifyLayoutChangeOnTextAnnotation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Text Annotation on Layout Change");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();
		ViewerLayout layout = new ViewerLayout(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TEXT_ANNOTATION";

		textAnno.drawText(1, 0, 0, myText);

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertTrue(textAnno.isElementPresent(textAnno.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertTrue(textAnno.isElementPresent(textAnno.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertTrue(textAnno.isElementPresent(textAnno.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");		

		layout.selectLayout(layout.oneByTwoLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	


		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	


		layout.selectLayout(layout.twoByOneLayoutIcon);
		textAnno.waitForViewerpageToLoad();

		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1),myText,"Verifying Text written on Text Annotation", "text is correctly displayed");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "verify that GSPS radial menu is present", "Verify GSPS radial menu with Previous arrow, a Reject button, and a Next arrow appear on first GSPS object.");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsPrevious), "Verify Previous arrow on radial menu", "The Previous arrow is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsReject), "Verify Reject button on radial menu", "The Reject button is displayed on radial menu");
		textAnno.assertFalse(textAnno.isElementPresent(textAnno.gspsNext), "Verify Next arrow on radial menu", "The Next arrow is displayed on radial menu");	

	}

	@Test(groups ={"IE11","Chrome","Positive"})
	public void test15_DE645_TC2273_US893_TC3427_DE1275_TC5610_verifyDoubleClickOnTextAnnotation() throws InterruptedException{


		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Double click one up is getting performed after Text Annotation is drawn (without writing anything in text box)"+
				"<br>Verify that user should be able to perform oneup when text annotation icon is selected in radial menu");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TextAnnotation_FirstViewbox";

		//Drawing text annotation without text
		textAnno.drawTextAnnotationWithNoText(1, -200, -200);

		//Verifying the presence of blank edit box
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verifying that the blank edit box is displaying");
		textAnno.assertTrue(textAnno.getEditbox(1).isDisplayed(), "Verify that the edit box is displaying", "Edit box is present");

		//Mouse hover and double click on edit box of in progress text annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verifying the double click on editbox of text annotation when text annotation command is in progress");
		textAnno.DoubleClickWithMouseHover(textAnno.getEditbox(1));
		textAnno.assertNotEquals(textAnno.getNumberOfCanvasForLayout(), 1, "Verify that the one up is not possible", "Layout is not changing to 1x1");

		//Mouse hover and double click on line of in progress text annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verifying the double click on line of text annotation when text annotation command is in progress");
		textAnno.DoubleClickWithMouseHover(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0));
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 1, "Verify that the one up is possible", "Layout is changing to 1x1");

		//Performing double click and verifying that the layout get change
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verifying the double click on viewbox when text annotation command is in progress");
		textAnno.doubleClick(textAnno.getViewPort(1));
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 4, "Verify that the one up is possible", "Layout is not changing to 1x1");

		//Drawing text annotation with text
		textAnno.drawText(1, -200, -200, myText);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verifying the double click on viewbox when text annotation command is complete");
		textAnno.doubleClick(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0));
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 1, "Verify that the one up is  possible", "Layout is changing to 1x1");
		textAnno.doubleClick(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0));
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		//Mouse hover and double click on edit box of drawn text annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verifying the double click on editbox of text annotation when text annotation command is complete");
		textAnno.DoubleClickWithMouseHover(textAnno.getTextElementFromTextAnnotation(1, 1));
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 4, "Verify that the one up is not possible", "Layout is not changing to 1x1");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		//Mouse hover and double click on line of drawn text annotation
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verifying the double click on line of text annotation when text annotation command is complete");
		textAnno.DoubleClickWithMouseHover(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0));
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 1, "Verify that the one up is possible", "Layout is changing to 1x1");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");
		textAnno.doubleClick();
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 1, "Verify that the one up is possible", "Layout is not changing to 2x2");
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

	}

	// DE619_TC2241_TC2242
	@Test(groups = { "Chrome", "IE11", "Edge", "DE619"},enabled = false)
	public void test16_DE619_TC2241_TC2242_verifyingTextAnnWhenShortcutsArePressed() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Text annotation behaviour after pressing spacebar and then followed by G key to toggle off results");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+gspsPatientName4+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		// step 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Draw text annotation with no text" );

		// selecting text annotation icon from context menu 
		textAnno.selectTextArrowFromQuickToolbar(1);

		textAnno.drawTextAnnotationWithNoText(1, 50, 100);

		//SPACEBAR shouldn't close text annotation
		textAnno.pressKeys(Keys.SPACE);
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		//Toggle OFF
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");

		//Toggle ON
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");


		//TC2242 - Shortcut- W
		textAnno.drawTextAnnotationWithNoText(1, 50, 100);

		textAnno.pressKeys(ViewerPageConstants.KEYBOARD_SHORTCUTS.get("w"));
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		//Toggle OFF
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");

		//Toggle ON
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");

		textAnno.openQuickToolbar(textAnno.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying Windowing in Context menu is selected");

		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.KEYBOARD_SHORTCUTS.get("W")),"Verifying Windowing in Context menu is selected", "Windowing in Context menu is selected");

		//		textAnno.closeContextMenu();


		//TC2242 - Shortcut- D
		textAnno.drawTextAnnotationWithNoText(1, 50, 100);

		textAnno.pressKeys(ViewerPageConstants.KEYBOARD_SHORTCUTS.get("d"));
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),1,"verifying the TextAnnotation#1", "Annotation is present");

		//Toggle OFF
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");

		//Toggle ON
		textAnno.toggleOnOrOffResultUsingKeyboardGKey();
		textAnno.assertEquals(textAnno.getTextAnnotations(1).size(),0,"verifying the TextAnnotation#1", "Annotation is not present anymore");

		textAnno.openQuickToolbar(textAnno.getViewPort(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying Distance measurement in Context menu is selected");

		textAnno.assertTrue(textAnno.checkCurrentSelectedIcon(ViewerPageConstants.KEYBOARD_SHORTCUTS.get("D")),"Verifying Distance measurement in Context menu is selected", "Distance measurement in Context menu is selected");



	}

	@Test(groups ={"IE11","Chrome","US893","Positive"})
	public void test17_US893_TC3683_verifyTextAnnotation_Resize_Edited() throws InterruptedException 
	{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify text annotation can be drawn,resized and edited");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		textAnno.selectTextArrowFromQuickToolbar(1);

		// Perform single click and verifying  text annotation should be drawn after selecting text annotation
		String inpText="TestingIsGoingOn";
		textAnno.drawText(1,-60,-60, inpText);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verifying that text annotation is displaying");
		textAnno.assertTrue(textAnno.isTextAnnotationPresent(1), "Verify that text annotations is displaying", "Text Annotation is present on 1st View box");

		//Getting the Coordinate of Text annotations before movement
		int X1=textAnno.getTextAnnotations(1).get(0).getLocation().getX();
		int Y1=textAnno.getTextAnnotations(1).get(0).getLocation().getY();

		// Selecting Text annotations and Moving to some other  location inside the view box.
		textAnno.moveTextAnnotation(1, 1, 10, 10);		
		textAnno.dragAndReleaseOnViewer(textAnno.getLineOfTextAnnotations(1, 1), 0, 0, -70,-70);

		//Getting the Coordinate of Text annotations after movement and asserting

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying that annotations moved to some other place in Viewbox");
		textAnno.assertNotEquals(textAnno.getTextAnnotations(1).get(0).getLocation().getX(),X1,"Verifying text moved to another location", "Text annotations has moved hence x coordinates has changed");		
		textAnno.assertNotEquals(textAnno.getTextAnnotations(1).get(0).getLocation().getY(),Y1,"Verifying text moved to another location", "Text annotations has moved hence y coordinates has changed");		

		// One Up On view box and Verifying Annotations is on new positions.
		textAnno.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying that annotations is at new positions afte one up in Viewbox");
		textAnno.assertNotEquals(textAnno.getTextAnnotations(1).get(0).getLocation().getX(),X1,"Verifying text moved to another location", "Text annotations has moved hence x coordinates has changed");		
		textAnno.assertNotEquals(textAnno.getTextAnnotations(1).get(0).getLocation().getY(),Y1,"Verifying text moved to another location", "Text annotations has moved hence y coordinates has changed");	
		textAnno.assertEquals(textAnno.getNumberOfCanvasForLayout(), 1, "", "");

		// Editing text inside Text annotations

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying that Text inside the text annotations has been edited");
		String inpText2="Task is completed";
		textAnno.updateTextOnTextAnnotation(1, 1, inpText2);
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1), inpText2, "Verifying that text inside the Text annotations is updated","Text has been updated successfully");

		// One up and verifying updated text is present after one up

		textAnno.doubleClickOnViewbox(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying that updated Text inside the text annotations is present after one up");
		textAnno.assertEquals(textAnno.getTextFromTextAnnotation(1, 1), inpText2, "Verifying that text inside the Text annotations is present","Updated text is present after one up");

	}

	@Test(groups ={"firefox","Chrome","IE11","US986","Positive"})
	public void test26_US986_TC4259_TC4296_verifyTextAnnotation() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  Text annotation should not get highlighted when user perform right click on Text annotation"+ 
				"<br> Verify Text annotation get highlighted when user perform left click on handles or line between two handles (anchor)");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName,  1);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		textAnn=new TextAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify drawn annotaion is not active GSPS when perform right click" );
		textAnn.selectTextArrowFromQuickToolbar(1);
		String myText = "TEXT_ANNOTATION";
		textAnn.drawText(1,250,200,myText+"_1");
		viewerPage.performMouseRightClick(textAnn.getAnchorLinesOfTextAnnot(1, 1).get(0));
		viewerPage.assertFalse(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true), "Verify drawn annotation is not current active GSPS", "Verified");
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		textAnn.waitForTimePeriod(2000);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify drawn annotaion is active GSPS when click on line of text annotation" );
		textAnn.click(textAnn.getAnchorLinesOfTextAnnot(1, 1).get(0));
		textAnn.waitForTimePeriod(2000);
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, true), "Verify drawn annotation is active GSPS", "Verified");

	}

	@Test(groups ={"Chrome","IE11","DE1057","Positive"})
	public void test27_DE1057_TC4832_verifyAcceptRejectTextAnnotation() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can accept / reject no anchor text annotation");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(gspsPatientName1,  1);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		textAnn=new TextAnnotation(driver);
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Perform left click" );

		viewerPage.click(textAnn.getTextAnnotation(1, 1));		
		textAnn.waitForTimePeriod(1000);
		viewerPage.assertTrue(textAnn.verifyPendingGSPSToolbarMenu(), "Checkpoint[2/5] Verify drawn annotation is not current pending GSPS", "Verified");

		textAnn.selectRejectfromGSPSRadialMenu();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(1, 1, false), "Checkpoint[3/5] Verify drawn annotation is rejected GSPS", "Verified");

		textAnn.selectRejectfromGSPSRadialMenu();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 1, false), "Checkpoint[4/5] Verify drawn annotation is pending GSPS", "Verified");

		textAnn.selectAcceptfromGSPSRadialMenu();
		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[5/5] Verify drawn annotation is active GSPS", "Verified");


	}

	@Test(groups ={"Chrome","IE11","DE1057","Positive"})
	public void test28_DE1057_TC4833_verifyEditingOfTextAnnoWithoutAnchor() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can edit \"No anchor text annotation\"");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(gspsPatientName1,  1);

		textAnn=new TextAnnotation(driver);

		String text = "New Annotation";

		viewerPage.assertTrue(textAnn.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/4] Verify drawn annotation is not current pending GSPS", "Verified");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Edit text and press \"Enter\" button from keyboard" );

		textAnn.updateTextOnTextAnnotation(1, 1,text);		

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentAcceptedInactive(1, 1, false), "Checkpoint[3/4] Edited changes should reflect in \"No Anchor text annotation\"", "Verified");
		textAnn.assertEquals(textAnn.getTextFromTextAnnotation(1, 1), text,"Checkpoint[4/4] Edited changes should reflect in \"No Anchor text annotation\"", "Verified");	

	}

	@Test(groups ={"Chrome","IE11","DE1057","Positive"})
	public void test29_DE1057_TC4834_verifyMovementOfTextAnnoWithoutAnchor() throws Exception  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user can move \"No anchor text annotation\"");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(gspsPatientName1,  1);

		textAnn=new TextAnnotation(driver);


		viewerPage.assertTrue(textAnn.verifyPendingGSPSToolbarMenu(), "Checkpoint[1/4] Verify drawn annotation is not current active GSPS", "Verified");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Perform left mouse button press and drag" );

		String x = textAnn.getAttributeValue(textAnn.getTextElementFromTextAnnotation(1, 1),ViewerPageConstants.X);
		String y = textAnn.getAttributeValue(textAnn.getTextElementFromTextAnnotation(1, 1),ViewerPageConstants.Y);

		textAnn.moveTextAnnotation(1, 1,30,40);		

		textAnn.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 1, false), "Checkpoint[3/4] Perform left mouse button press and drag ", "Verified");

		textAnn.assertNotEquals(textAnn.getAttributeValue(textAnn.getTextElementFromTextAnnotation(1, 1),ViewerPageConstants.X),x,"User should be able to move text in any direction","Verified");
		textAnn.assertNotEquals(textAnn.getAttributeValue(textAnn.getTextElementFromTextAnnotation(1, 1),ViewerPageConstants.Y),y,"User should be able to move text in any direction","Verified");

	}

	@Test(groups ={"IE11","Chrome","US1092","Positive"})
	public void test30_US1092_TC5018_TC5019_verifyMultiLineText() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cursor should move to next line when user press (Shift + enter) while entering text in newly created text annotation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		//selecting text annotation from radial menu
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingTextToNextLine="Testing multi line";
		//		String commentText ="user can use mouse click to jump into the middle of line";

		//drawing multiline text annotation
		textAnno.drawMultiLineText(1, -150, -150, myText, addingTextToNextLine);

		//verifying that text annotation has multilines
		textAnno.assertTrue(textAnno.getTextLinesFromTextAnnotation(1, 1).size() > 1, "Checkpoint[1/4]", "Verified that text is multiline text");

		//editing text annotation by pressing "SHIFT + ENTER" and adding new text to existing one
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying that Text inside the text annotations has been edited");
		String inpText2="Task is completed and text is edited";
		textAnno.updateMultiLineTextOnTextAnnotation(1, 1, inpText2);

		//verifying multiline text after editing
		textAnno.assertTrue(textAnno.getTextFromTextAnnotation(1, 1).contains(inpText2), "Checkpoint[3/4]","Text has been updated successfully");
		textAnno.assertTrue(textAnno.getTextLinesFromTextAnnotation(1, 1).size() > 2, "Checkpoint[4/4]", "Verified that text is multiline text");	
	}

	@Test(groups ={"IE11","Chrome","US1092","Positive"})
	public void test31_US1092_TC5020_verifyMultiLineInNoAnchorText() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cursor should move to next line when user press (Shift + enter) for no anchor text annotation");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(gspsPatientName1,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		//editing text annotation by pressing "SHIFT + ENTER" and adding new text to existing one
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verifying that Text inside the text annotations has been edited");
		String inpText2="Task is completed and text is edited";
		textAnno.updateMultiLineTextOnTextAnnotation(1, 1, inpText2);

		//verifying multiline text after editing
		textAnno.assertTrue(textAnno.getTextFromTextAnnotation(1, 1).contains(inpText2), "Checkpoint[3/4]","Text has been updated successfully");
		textAnno.assertTrue(textAnno.getTextLinesFromTextAnnotation(1, 1).size() > 1, "Checkpoint[4/4]", "Verified that text is multiline text");	
	}

	@Test(groups ={"IE11","Chrome","US1092","Positive"})
	public void test32_US1092_TC5023_verifyMultiLineTextInComment() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that cursor should move to next line when user press (Shift + enter) while entering text in comment");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName,  1);


		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		//selecting text annotation from radial menu
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="Verify user is directed to new line when long comment is added in comment box Verify user is directed to new line when long comment is added in comment box";
		String addingTextToNextLine="Testing multi line";
		String commentText ="user can use mouse click to jump into the middle of line";

		//drawing multiline text annotation
		textAnno.drawMultiLineText(1, -150, -150, myText, addingTextToNextLine);

		//adding multiline comment
		textAnno.addMultiLineResultComment(textAnno.getTextElementFromTextAnnotation(1, 1),commentText,addingTextToNextLine);

		//verifying multiline comment
		textAnno.assertTrue(textAnno.getTextLinesFromCommentOfTextAnnotation(1, 1).size() > 1, "Checkpoint[1/1]", "Verified that text is multiline comment");
		/*
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify entered result comment text is visible when mouse hover on iIcon");
		textAnno.openFindingTableOnBinarySelector(1);
		textAnno.assertTrue(textAnno.isElementPresent(textAnno.iIconInfo.get(0)), "Verify iIcon after adding result comment", "Verified");
		textAnno.mouseHover(textAnno.iIconInfo.get(0));
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		textAnno.takeElementScreenShot(textAnno.viewer, newImagePath+"/goldImages/"+ah4PatientName+"_TextAnnot_MultilineComment_I_icon.png");

		 */ 

	}

	@Test(groups ={"Chrome","US1093","Positive"})
	public void test33_US1093_TC5004_verifyDashLineOnTextAnnoation() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify - Text annotation with Anchor point is drawn with dotted line");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName,  1);


		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		//selecting text annotation from radial menu
		textAnno.selectTextArrowFromQuickToolbar(1);

		String myText ="TextAnnotation";

		//drawing multiline text annotation
		textAnno.drawText(1, -100, -100, myText);
		textAnno.assertFalse(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0).getAttribute(NSGenericConstants.TEXT_ANN_DASH_LINE).isEmpty(),"Checkpoint[1/2]","Verifying the dashed anchor line is displayed");

		textAnno.mouseHover(textAnno.getViewPort(2));
		textAnno.assertFalse(textAnno.getAnchorLinesOfTextAnnot(1, 1).get(0).getAttribute(NSGenericConstants.TEXT_ANN_DASH_LINE).isEmpty(),"Checkpoint[2/2]","Verifying the dashed anchor line is displayed");


	}

	@Test(groups ={"IE11","Chrome","DE1831","Negative"})
	public void test35_DE1831_TC7404_verifyTextAnnotationCleanUp() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that annotations are cleaned up from viewer as well as finding list if they are empty.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(imbio_PatientName,  1);

		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();

		int findingCount = textAnno.getFindingsCountFromFindingTable();
		textAnno.selectTextArrowFromQuickToolbar(1);

		textAnno.drawTextAnnotationWithNoText(1, -150, -150);
		
		textAnno.assertTrue(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).isEmpty(), "Checkpoint[1/3]", "Verified that text is not present");

		textAnno.assertEquals(textAnno.getFindingsCountFromFindingTable(),findingCount, "Checkpoint[2/3]", "Verified that no findings are added");

		textAnno.assertFalse(textAnno.verifyDottedLinePresence(1), "Checkpoint[3/3]", "Verified that anchor line is not present");


	}

	@Test(groups ={"IE11","Chrome","Edge","DE1870", "Negative"})
	public void test36_DE1870_TC7400_verifyAfterClickingOnPDFDrawTextAnnotation() throws InterruptedException 
	{
	    String myText = "Text annotation is drawn";
//	    String newtext = "New Annotation";
	     
	     
		extentTest = ExtentManager.getTestInstance();	
		extentTest.setDescription("Verify user is able to draw a text annotation on a viewbox when user clicks on the pdf of another viewbox.");

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName_AH4_PDF,  1);

	
		textAnno = new TextAnnotation(driver);
		textAnno.waitForViewerpageToLoad();
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		textAnno.selectTextArrowFromQuickToolbar(1);
		
		//Draw the text annotation, close the watermark and click at the viewbox 2 - DICOM image
		textAnno.drawText(1, 0, 0, myText);
		textAnno.closingConflictMsg();
		textAnno.click(textAnno.getViewPort(2));
		
		//Draw the text annotation, close the watermark and click at the viewbox 3 - PDF
		textAnno.drawText(1, 25, 25, myText);
		textAnno.assertEquals(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 1).size() , 1, "Checkpoint[1/2]", "Verified that user is able to draw the text annotation after closing the watermark and clicking on DICOM image");

		textAnno.closingConflictMsg();
		textAnno.click(textAnno.getViewPort(3));
		textAnno.drawText(1, 50, 50, myText);
		textAnno.assertEquals(textAnno.getTextLinesWebElementsFromTextAnnotation(1, 2).size() , 1, "Checkpoint[2/2]", "Verified that user is able to draw the text annotation after closing the watermark and clicking on PDF image");
		textAnno.closingConflictMsg();
	
	
	}

}


