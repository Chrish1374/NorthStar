package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PdfDisplayTest extends TestBase{

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ViewerLayout layout;
	private ViewerTextOverlays textOverlay;
	private ViewBoxToolPanel preset;

	//Loading the patient on viewer
	String filePath = TEST_PROPERTIES.get("AH4_pdf_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	private HelperClass helper;
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException{

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, username, password, 1);
		
		
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US227","DE881","Positive","Sanity"})
	public void test01_US227_TC680_DE881_TC3347_verifyPdfDisplay() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify pdf display in viewport"
				+ "<br> Verify Blank space should not get displayed on top of viewbox having pdf");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );

		layout=new ViewerLayout(driver);
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.waitForPdfToRenderInViewbox(3);
		viewerpage.closingConflictMsg();
		viewerpage.compareElementImage(protocolName, viewerpage.getPDFViewbox(3), "Verify the PDF report is displayed in a view port, Also make sure the PDF  contents are visible and readable.", "US227_TC680_Pdf_Display");
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Verifying layout is changed to "+ViewerPageConstants.TWO_BY_THREE_LAYOUT, "Layout is changed to "+ViewerPageConstants.TWO_BY_THREE_LAYOUT);
		viewerpage.compareElementImage(protocolName, viewerpage.getPDFViewbox(3), "Verify the PDF report is still displayed in a view port on layout change.", "US227_TC680_Pdf_Display_Layout_Change");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE629","Sanity"})
	public void test02_DE269_TC1327_verifyPdfOpenIn1X1Lyout() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify pdf gets open in 1*1 layout when double clicked");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.waitForPdfToRenderInViewbox(3);
		viewerpage.closingConflictMsg();
		layout=new ViewerLayout(driver);
		
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying Viewer page should be launched successfully.", "Viewer page launched successfully.");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verify the PDF file is displayed in a view port", "DE269_TC1327_Pdf_Display");
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.waitForPdfToRenderInViewbox(3);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying layout is changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT, "Layout is changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT);
		viewerpage.compareElementImage(protocolName, viewerpage.getPDFViewbox(3), "Verify pdf gets open in 1*1 layout on double click", "DE269_TC1327_Pdf_Display_Double_Click");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE269","Sanity"})
	public void test03_DE269_TC1332_verifyPdfOpenWhenSelectedFromContentSelector() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify application behavior on double Click to 1-Up on pdf when pdf is selected through content selector");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();
		viewerpage.waitForPdfToRenderInViewbox(3);
		viewerpage.closingConflictMsg();
		ContentSelector contentSelector = new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying Viewer page should be launched successfully.", "Viewer page launched successfully");
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verify the PDF file is displayed in a view port", "DE269_TC1332_Pdf_Display");

		String pdfSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, "STUDY01", "STUDY01_SERIES03", filePath);
		contentSelector.selectSeriesFromSeriesTab(2, pdfSeriesDescription);

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer, "Verify pdf should be displayed instead of DICOM image inside the viewbox.", "DE269_TC1332_Pdf_Display_Content_Selector");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.waitForPdfToRenderInViewbox(2);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying layout is changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT, "Layout is changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT);
		//viewerpage.compareElementImage(protocolName, viewerpage.getPDFViewbox(2), "Verify pdf gets open in 1*1 layout on double click", "DE269_TC1332_Pdf_Display_Double_Click");
		viewerpage.doubleClickOnViewbox(2);
		viewerpage.waitForPdfToRenderInViewbox(2);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT), "Verifying layout is changed to "+ViewerPageConstants.TWO_BY_TWO_LAYOUT, "Layout is changed to "+ViewerPageConstants.ONE_BY_ONE_LAYOUT);
	// compareElementImage(protocolName, viewerpage.mainViewer, "Verify pdf should be open in previous state layout format For AH4^pdf it should open in 2*2 from 1*1", "DE269_TC1332_Pdf_Display_Previous_State");
	}

	@Test(groups ={"IE11","Chrome","firefox","Edge","DE248"})
	public void test04_DE248_TC1037_verifyViewboxCommandsPdfLoadedViewer() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify zoom, cine, pan, window leveling, scroll, ruler command functionality on selected viewport when pdf is loaded in viewer in case of layout change");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer" );

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForPdfToRenderInViewbox(3);
		viewerpage.closingConflictMsg();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify user navigated to viewer page.");
		viewerpage.assertTrue(viewerpage.getViewPort(1).isDisplayed(), "Verifying user should be on viewer page", "User is on viewer page");

		layout=new ViewerLayout(driver);
		textOverlay=new ViewerTextOverlays(driver);
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.closingConflictMsg();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify layout change.");
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_THREE_LAYOUT), "Verifying Layout should be changed successfully to "+ViewerPageConstants.TWO_BY_THREE_LAYOUT, "Layout is changed to "+ViewerPageConstants.TWO_BY_THREE_LAYOUT);

		//selecting and performing zoom
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		preset=new ViewBoxToolPanel(driver);
		int beforeZoom = preset.getZoomValue(1);
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 0, -10);
		int afterZoom = preset.getZoomValue(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify images should Zoom.");
		viewerpage.assertTrue(beforeZoom < afterZoom, "verifying zoom level percentange", "Image zoom percentage was "+beforeZoom+". After zoom, percentage is ="+afterZoom);

		// right clicking on viewbox 1 and Clicking on the 3 dots icon on radial bar -> allIcon -> pan option
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify images should PAN .");
		viewerpage.dragAndReleaseOnViewer(0, 0, 100, 0);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Verifying images should PAN.","DE248_TC1037_Checkpoint_Pan");

		//selecting and performing window leveling
		String viewbox1_width = textOverlay.getWindowWidthValueOverlayText(1);
		String viewbox1_windowCenter = textOverlay.getWindowCenterValueOverlayText(1);
		// right clicking on viewbox 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify the WW/WL is performed.");
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),0, 0, 100, -50);
		viewerpage.assertNotEquals(textOverlay.getWindowWidthValueOverlayText(1) , viewbox1_width, "verifying the WW/WL in viewbox1", "verified");		
		viewerpage.assertNotEquals(textOverlay.getWindowCenterValueOverlayText(1)  , viewbox1_windowCenter, "verifying the WW/WL(center) in viewbox1", "verified");

		//selecting and performing scroll
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify scroll.");
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		int currentScrollPos = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.dragAndReleaseOnViewer(0, 0, 0, 10);
		viewerpage.assertNotEquals(currentScrollPos,viewerpage.getCurrentScrollPositionOfViewbox(1), "Verifying the scroll functionality", "scroll is working fine");

		//select and perform distance measurement
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify measurement is drawn.");
		MeasurementWithUnit line = new MeasurementWithUnit(driver);
//		viewerpage.selectLinearMeasurementFromContextMenu(viewerpage.getViewPort(1));
		line.selectDistanceFromQuickToolbar(1);
		viewerpage.dragAndReleaseOnViewer(10,20, 100, 50);
		viewerpage.waitForAllChangesToLoad();
		viewerpage.compareElementImage(protocolName, viewerpage.getViewbox(1),"Verify the measurement drawn", "DE248_TC1037_Msrt");

		//selecting and playing cine
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify cine play.");
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));

		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/beforeCineplayImage.png");

		//stopping cine
		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));


		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterCineplayImage.png");
		int imageAfterCine = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentImage, imageAfterCine, "verifying the cine play is stopped", "cine is stopped and working fine");
		String expectedImagePath = newImagePath+"/actualImages/beforeCineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/afterCineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/cineplayImage.png";
		boolean cpStatus =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerpage.assertFalse(cpStatus, "Verifying cine is played","Successfully verified checkpoint with image comparison.<br>Image name is cineplayImage.png");
	}

	//TC994 - Verify that the selected viewport should be highlighted when pdf is loaded in viewer
	@Test(groups ={"Chrome", "Edge","firefox","IE11","DE248"})
	public void test05_DE248_TC994_verifySelectedViewboxIsHighlighted(){
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the selected viewport should be highlighted when pdf is loaded in viewer");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer");

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForPdfToRenderInViewbox(3);

		for(int i = 1 ; i<=2 ;i++){
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+"/2]", "Verifying that the selected viewport"+i+" is highlighted when pdf is loaded in viewer");
			viewerpage.assertTrue(viewerpage.selectAndVerifyActiveViewbox(i), "Verify that the selected viewport"+i+" is highlighted", "viewport"+i+" is highlighted");
		}
	}

	//TC995 - Verify that the selected viewport should be highlighted when pdf is loaded in viewer in case of layout change
	@Test(groups ={"Chrome", "Edge","firefox","IE11","DE248"})
	public void test06_DE248_TC995_verifySelectedViewboxIsHighlightedFromLayoutChange(){
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the selected viewport should be highlighted when pdf is loaded in viewer in case of layout change");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+" in viewer");

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForPdfToRenderInViewbox(3);
		//Verify the active overlay by changing the layout
		layout=new ViewerLayout(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.THREE_BY_TWO_LAYOUT+" ");
		verifySelectedLayout(layout.threeByTwoLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.TWO_BY_THREE_LAYOUT+" ");
		verifySelectedLayout(layout.twoByThreeLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.ONE_BY_ONE_L_AND_THREE_BY_ONE_R_LAYOUT+" ");
		verifySelectedLayout(layout.oneByOneLAndThreeByOneRLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT+" ");
		verifySelectedLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.ONE_BY_ONE_T_AND_ONE_BY_TWO_B_LAYOUT+" ");
		verifySelectedLayout(layout.oneByOneTAndOneByTwoBLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.TWO_BY_ONE_L_AND_ONE_BY_ONE_R_LAYOUT+" ");
		verifySelectedLayout(layout.twoByOneLAndOneByOneRLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.THREE_BY_ONE_L_AND_ONE_BY_ONE_R_LAYOUT+" ");
		verifySelectedLayout(layout.threeByOneLAndOneByOneRLayoutIcon);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verifying that selected viewport is highlighted in case of layout "+ViewerPageConstants.THREE_BY_THREE_LAYOUT+" ");
		verifySelectedLayout(layout.threeByThreeLayoutIcon);
	}


	@Test(groups = { "Chrome", "IE11", "Edge", "US804","US1063","Positive" })
	public void test07_US804_US1063_TC4451_TC5027_VerifyScrollbarComponentInPDF()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new UI of scrollbar component for PDF data <br>"+
		"Verify pdf functionlaity on view box.");

		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[1/6]","Verify one up functionality on PDF");
		viewerpage.doubleClickOnViewbox(3);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Verify layout change to 1*1", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint:[2/6]","Verify one down functionality on PDF");
		viewerpage.doubleClick();
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(),3, "Verify layout change to 1*1", "Verified");
		
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verify scroll bar component for PDF");
	    viewerpage.mouseHover(viewerpage.getViewPort(3));
		viewerpage.assertTrue(viewerpage.verticalScrollBarComponent.get(0).isDisplayed(), "Verify new UI of Scrollbar component present on PDF", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verify UI of regular scrollbar component for SR report");
		viewerpage.assertTrue(viewerpage.verifyPropertyOfRegularVerticalScrollBar(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of regular vertical scrollbar","Verified");
	 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verify UI of scrollbar component when mouse pointer on the scrollbar for PDF");
		viewerpage.mouseHover(viewerpage.verticalScrollBarComponent.get(0));
	    viewerpage.assertTrue(viewerpage.verifyPropertyOfVerticalScrollBarWhenMousePointer(viewerpage.verticalScrollBarComponent.get(0), viewerpage.verticalScrollBarSlider.get(0)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");
	
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/6]", "Verify scroll functionality in PDF");
	    viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.lastItemOnPDF(3)),"Verify last item of PDF not visible before scroll", "Verified");
	    viewerpage.scrollDownUsingPerfectScrollbar(viewerpage.lastItemOnPDF(3),viewerpage.verticalScrollBarComponent.get(0),10,viewerpage.getHeightOfWebElement(viewerpage.verticalScrollBarSlider.get(0)));
	    viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.lastItemOnPDF(3)),"Verify last item of PDF visible after scroll", "Verified");
	    
	}

	
	
	
	//Verifying active viewbox's from layout change
	private void verifySelectedLayout(WebElement layoutName){
		layout=new ViewerLayout(driver);
		try {
			layout.selectLayout(layoutName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewerpage.waitForPdfToRenderInViewbox(3);

		for(int i = 1 ; i<=2 ;i++){
			viewerpage.assertTrue(viewerpage.selectAndVerifyActiveViewbox(i), "Verify that the selected viewport"+i+" is highlighted", "viewport"+i+" is highlighted");
		}
	}


}
