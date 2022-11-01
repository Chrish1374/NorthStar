package com.trn.ns.test.viewer.dicomRT;

import java.awt.AWTException;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Safety.NS_F158_SupportForStorageAndDisplayOfDICOMRTSeries-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class DicomRTTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private DICOMRT drt;
	private HelperClass helper;
    private ViewerLayout layout;
    private ContentSelector cs;
    private ViewBoxToolPanel viewBoxPanel;
    private String loadedTheme;
	// Get Patient Name

    String username=Configurations.TEST_PROPERTIES.get("nsUserName");
    String password= Configurations.TEST_PROPERTIES.get("nsPassword");
    
	String TCGA_filepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameTCGA = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, TCGA_filepath);
	String firstResultDescriptionrtStruct = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, TCGA_filepath);

	
	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);		
		loginPage.navigateToBaseURL();		
		loginPage.login(username,password);

	}

//	TC1: Correctness of DICOM RT contours on appropriate slices (Automated) 
	@Test(groups = { "firefox", "Chrome", "IE11", "Edge" , "US720"})
	public void test01_US720_TC2838_TC2840_TC2841_TC2842_verifyContourColorDisplayAndSlicePosition() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify DICOM RT should be displayed on viewer page with correct contour color and slice position");

		//Loading Bone Age patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		
		//verifying DICOM RT data display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verify DICOM RT Display");

		drt.compareElementImage(protocolName, drt.mainViewer, "verifying the viewer for patient "+patientNameTCGA, "TC01_checkpoint_test01_");

		int inputValues[]={70, 71, 83, 100, 101, 111, 139};
		int inputValuesForLatestRT[]={80, 81, 93, 90, 115, 111, 139};

		//verifying DICOM RT color and slice position
		verifyDicomRTDisplay(patientNameTCGA, inputValues, "TC02_checkpoint_test01_");

		helper=new HelperClass(driver);
	    helper.browserBackAndReloadRTData(patientNameTCGA,1,1);

		verifyDicomRTDisplay(patientNameTCGA, inputValuesForLatestRT, "TC03_checkpoint_test01_");

	}

	@Test(groups ={"firefox","Chrome","IE11","Edge" , "US720"})
	public void test02_US720_TC2843_TC2892_verifyLinearMeasurementWithAllRadialMenuOperations() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();		
		extentTest.setDescription("Verify linear measurement along with all DICOM operation");

		//Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);

		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		
		//Draw a linear measurement from radial menu
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -50, 120, 150);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/9]", "Verify that Linear Measurement is drawn on Viewbox1");
		drt.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is drawn on Viewbox1","A Single instance of Linear Measurement is present on Viewbox1");
		drt.assertEquals(lineWithUnit.getLinearMeasurementsText(1).size(),1,"Verify a Linear Measurement text is drawn along with line","A Single instance of Linear Measurement text is present on Viewbox1");

		//variable to store current value of all the measurement text
		String beforeValue = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		// get Zoom level for Canvas 0 before Zoom
		viewBoxPanel=new ViewBoxToolPanel(driver);
		int intialZoomLevel1 = viewBoxPanel.getZoomValue(1);

		//Select a Zoom from radial bar and perform Zoom down
		drt.selectZoomFromQuickToolbar(drt.getViewPort(1));
		drt.dragAndReleaseOnViewer(drt.getViewbox(1), 0, 0, 50,50);
		int finalZoomLevel1 = viewBoxPanel.getZoomValue(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/9]","Verify Zoom Level after Mouse Up decrease in View Box 1.");
		drt.assertTrue(finalZoomLevel1 < intialZoomLevel1,"Verifying zoom level percentange","The Zoom level after Mouse Up decreases from "+ intialZoomLevel1 + " to "+ finalZoomLevel1);
		String afterValue = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/9]", "Verify value of linear measurement remains same on zoom");
		drt.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after zoom down","The value of linear measurement text remains same : "+ afterValue);

		//Perform Pan on Viewbox1
		drt.selectPanFromQuickToolbar(drt.getViewPort(1));
		drt.dragAndReleaseOnViewer(drt.getViewbox(1), 0, 0, 50,50);

		//variable to store current value of all the measurement text
		afterValue = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/9]", "Verify that linear measurement should get panned along with DICOM image");
		drt.compareElementImage(protocolName, drt.getViewPort(1),"Verify that linear measurement should get panned along with DICOM image","TC1970_CheckPoint4");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/9]", "Verify value of linear measurement remains same on pan");
		drt.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after pan","The value of linear measurement text remains same : "+ afterValue);

		//variable to store current value W and C of viewbox1
		String beforeW = drt.getWindowWidthValText(1);
		String beforeC = drt.getWindowCenterText(1);

		//Select Window leveling from radial menu
		drt.selectWindowLevelFromQuickToolbar(drt.getViewPort(1));	
		drt.dragAndReleaseOnViewer(drt.getViewbox(1), 0, 0, 50,50);

		//variable to store current value W and C of viewbox1
		String afterW = drt.getWindowWidthValText(1);
		String afterC = drt.getWindowCenterText(1);
		afterValue=lineWithUnit.getLinearMeasurementsText(1).get(0).getText();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/9]", "Verify that Window leveling is applied on viewbox1");
		drt.assertNotEquals(beforeW,afterW,"Verify Window Width level changes","The value of Window Width changes from "+ beforeW+" to "+afterW);
		drt.assertNotEquals(beforeC,afterC,"Verify Window Center level changes","The value of Window Center changes from "+ beforeC+" to "+afterC);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/9]", "Verify value of linear measurement remains same on Window Leveling");
		drt.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after window leveling","The value of linear measurement text remains same : "+ afterValue);

		//Perform scroll to a slice number in a active View box
		drt.scrollDownToSliceUsingKeyboard(5);
		drt.scrollUpToSliceUsingKeyboard(5);
		afterValue = lineWithUnit.getLinearMeasurementsText(1).get(0).getText();
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/9]", "Verify that Linear Measurement is present on slice after scrolling");
		drt.assertEquals(lineWithUnit.getAllLinearMeasurements(1).size(),1,"Verify a Linear Measurement is present on slice1","A linear measurement is present on slice1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/9]", "Verify value of linear measurement remains same on scrolling");
		drt.assertEquals(afterValue,beforeValue,"Verify value of linear measurement text remains same after scrolling","The value of linear measurement text remains same : "+ afterValue);

	} 

//	TC1: Correctness of DICOM RT contours on appropriate slices (Automated) 
	@Test(groups = {"Chrome","IE","Edge","US762","US2511","Positive","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US762_TC2942_TC2943_TC2951_US2511_TC10421_TC10426_verifyTheLegend(String theme) throws InterruptedException  {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the display of DICOM RT Legend"
				+ "<br> Verify that the legend should be display on the left side of viewbox on viewer"
				+ "<br> Verify that the same legend should display on all slices of the viewbox. <br>"+
				"Verify new RT legend pop up in eureka theme. <br>"+
				"Verify new RT legend pop up in dark theme");

		//Loading Bone Age patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+"in viewer" );
		patientListPage = new PatientListPage(driver);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);
			
	    }else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		patientListPage.waitForPatientPageToLoad();
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
	
		//verifying DICOM RT data display
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify DICOM RT Display");

		for(int i=0;i<drt.legendOptionsList.size();i++)
		{
		drt.click(drt.legendOptionsList.get(i));
		drt.assertEquals(drt.getHexColorValue(drt.getColorOfSegment(i+1)), drt.getHexColorValue(drt.getSelectedContourColor()), "Checkpoint[1.("+(i+1)+"/11]", "Verified color of segment "+(i+1));
		drt.assertTrue(drt.isAcceptRejectToolBarPresent(1),"Checkpoint[2.("+(i+1)+"/11]", "Verified that Accept reject toolbar is present.");
		}
		
		int inputValuesForLatestRT[]={80, 81, 93, 90, 115, 111, 139};

		//verifying DICOM RT color and slice position		
		int countourLength = DataReader.getNumberOfContours(TCGA_filepath);
		for(int i=0;i<inputValuesForLatestRT.length;i++) {

			drt.scrollToImage(1 ,inputValuesForLatestRT[i]);
			drt.assertTrue(drt.isElementPresent(drt.legendOptions),"Checkpoint[3.("+(i+1)+"/11]", "Verified that Legend  is present on slice "+inputValuesForLatestRT[i]);
			drt.assertEquals(drt.legendOptionsList.size(),countourLength,"Checkpoint[4.("+(i+1)+"/11]", "Verified that Legend option size is correct.");
			PagesTheme pageTheme=new PagesTheme(driver);
			
			for(int j=0;j<drt.legendOptionsList.size();j++) {
				pageTheme.assertTrue(pageTheme.verifyThemeForEyeIcon(drt.segmentEyeIcon.get(j), loadedTheme), "Checkpoint[5.("+(j+1)+"/11]", "Verified eye icon for segment " +(j+1));
				drt.assertEquals(drt.getText(drt.legendOptionsList.get(j)),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+(j+1),ViewerPageConstants.CONTOUR_DESCRIPTION, TCGA_filepath),"Checkpoint[6.("+(j+1)+"/11]", "Verified name for segment " +(j+1));
				drt.assertEquals(drt.getBackgroundColor(drt.segmentColorIcon.get(j)),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+(j+1),NSGenericConstants.CSS_PROP_COLOR, TCGA_filepath),"Checkpoint[7.("+(j+1)+"/11]", "Verified color icon for segment " +(j+1));
			    drt.assertTrue(pageTheme.verifyThemeOnLabel(drt.legendOptionsList.get(j),loadedTheme),"Checkpoint[8.("+(j+1)+"/11]", "Verified theme for label of segment " +(j+1));
			    drt.assertEquals(drt.getCssValue(drt.pendingStateicon.get(j),NSGenericConstants.FILL), ViewerPageConstants.PENDING_RT_FINDING_COLOR,"Checkpoint[9.("+(j+1)+"/11]", "Verified pending state icon for segment " +(j+1));
			    drt.assertEquals(drt.getCssValue(drt.legendOptionsList.get(j),NSGenericConstants.FONT_SIZE_PROP), "10px", "Checkpoint[10.("+(j+1)+"/11]", "Verified font size for segement " +(j+1));
			    drt.mouseHover(drt.legendOptionsList.get(j));
			    drt.assertTrue(pageTheme.verifyThemeForBorder(drt.legendOptionsList.get(j),NSGenericConstants.BORDER,loadedTheme),"Checkpoint[11.("+(j+1)+"/11]", "Verified boreder on mousehover for segment " +(j+1));
			}
		}


	}

	@Test(groups = {"Chrome","IE","Edge","US2511","Positive","F1085","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US2511_TC10432_verifySelectionAndDeselectionOfItems(String theme) throws InterruptedException  {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify eye icon functionality in new RT legend pop up");

		//Loading Bone Age patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+"in viewer" );
		patientListPage = new PatientListPage(driver);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
			loginPage = new LoginPage(driver);
			loginPage.navigateToBaseURL();
			loginPage.login(username, password);
			
	    }else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		PolyLineAnnotation poly=new PolyLineAnnotation(driver);
		
		//verifying DICOM RT color and slice position		
		int countourLength = DataReader.getNumberOfContours(TCGA_filepath);
		
		drt.assertTrue(drt.isElementPresent(drt.legendOptions),"Checkpoint[1/9]","Verifying the options are getting displayed");
		drt.assertEquals(drt.legendOptionsList.size(),countourLength,"Checkpoint[2/9]","verifying that options size is same");

		drt.mouseHover(drt.segmentEyeIcon.get(0));
		drt.assertTrue(pageTheme.verifyThemeOnTooltip(drt.tooltip, loadedTheme), "Checkpoint[3/9]", "Verified theme on tooltip.");
		drt.assertEquals(drt.getText(drt.tooltip),NSGenericConstants.HIDE_SEG,"Checkpoint[4/9]","Verified tooltip text when Legend is enable.");
		drt.mouseHover(drt.getSliceInfo(1));
		drt.click(drt.segmentEyeIconSvg.get(0));
		drt.mouseHover(drt.segmentEyeIconSvg.get(0));
		drt.assertTrue(pageTheme.verifyThemeOnTooltip(drt.tooltip, loadedTheme),"Checkpoint[5/9]", "Verified theme on tooltip.");
		drt.assertEquals(drt.getText(drt.tooltip),NSGenericConstants.DISPLAY_SEG,"Checkpoint[6/9]" ,"Verified tooltip text when Legend is disable.");
		drt.assertEquals(drt.getAttributeValue(drt.segmentEyeIcon.get(0), NSGenericConstants.OPACITY), ".5","Checkpoint[7/9]", "Verified that eye icon is disable.");
		drt.mouseHover(drt.getSliceInfo(1));
					
		drt.navigateToFirstContourOfSegmentation(1);
		drt.assertFalse(poly.getLinesOfPolyLine(1, 1).isEmpty(),"Checkpoint[8/9]", "Verified that contours are visible after click on Legend text.");
		drt.click(drt.segmentEyeIconSvg.get(0));
		drt.assertTrue(poly.getAllGSPSObjects(1).isEmpty(),"Checkpoint[9/9]", "Verified that contours are not visible after click on eye icon to disable the segment.");
			

	}
	
	@Test(groups = {"Chrome","IE","Edge","US762","US2511","F1085","E2E"})
	public void test05_US762_TC2954_US2511_TC10436_verifytoggleOnOffForLegend() throws InterruptedException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on toggling off the results, legends should also get invisible for only active viewbox. <br>"+
		"Verify new RT legend pop up gets disabled on pressing G or on clicking 'result applied'");

		//Loading Bone Age patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientNameTCGA+"in viewer" );
		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);

		//verifying DICOM RT color and slice position		

		int countourLength = DataReader.getNumberOfContours(TCGA_filepath);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify toggle on and off using Keyboard shortcut key G.");
		drt.assertTrue(drt.isElementPresent(drt.legendOptions),"Checkpoint[1/10]","Verifying the options are getting displayed");
		drt.assertEquals(drt.legendOptionsList.size(),countourLength,"Checkpoint[2/10]","verifying that options size is same");

		//using G key from keyboard
		drt.toggleOnOrOffResultUsingKeyboardGKey();
		drt.assertFalse(drt.isElementPresent(drt.legendOptions),"Checkpoint[3/10]","Verifying the options are not getting displayed after pressing G from Keyboard");
		drt.assertTrue(drt.legendOptionsList.isEmpty(),"Checkpoint[4/10]","Verified that Legend not visible.");

		drt.toggleOnOrOffResultUsingKeyboardGKey();

		int j=1;
		for(WebElement element : drt.legendOptionsList) {
			drt.assertEquals(drt.getText(element),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+j,ViewerPageConstants.CONTOUR_DESCRIPTION, TCGA_filepath),"Checkpoint[5/10]","verifying the options displayed are same as configured in data xml after toggle on");
			drt.assertEquals(drt.getBackgroundColor(drt.segmentColorIcon.get(j-1)),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+(j),NSGenericConstants.CSS_PROP_COLOR, TCGA_filepath),"Checkpoint[6/10]","verifying the options colors");
			j++;
		}
		
		//Using result applied
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify toggle on and off using Result applied.");
		drt.pressResultApplied(1);
		drt.assertFalse(drt.isElementPresent(drt.legendOptions),"Checkpoint[7/10]","Verifying the options are not getting displayed after click on Result applied.");
		drt.assertEquals(drt.legendOptionsList.size(),0,"Checkpoint[8/10]","Verified that Legend not visible.");

		drt.pressResultApplied(1);
		j=1;
		for(WebElement element : drt.legendOptionsList) {

			drt.assertEquals(drt.getText(element),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+j,ViewerPageConstants.CONTOUR_DESCRIPTION, TCGA_filepath),"Checkpoint[9/10]","verifying the options displayed are same as configured in data xml after toggle on");
			drt.assertEquals(drt.getBackgroundColor(drt.segmentColorIcon.get(j-1)),DataReader.getContourProperty(ViewerPageConstants.CONTOUR_OPTION+(j),NSGenericConstants.CSS_PROP_COLOR, TCGA_filepath),"Checkpoint[10/10]","verifying the options colors");
			j++;
		}
	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US804","US2511", "Positive","F1085" })
	public void test06_US804_TC4447_US2511_TC10435_VerifyScrollbarComponentInRTStruct()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new UI of scrollbar component on RT Struct. <br>"+
		"Verify new RT legend pop up on layout change and on browser resize");

		patientListPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		DICOMRT drt =helper.loadViewerPageForRTUsingSearch(patientNameTCGA,1, 1);

		cs=new ContentSelector(driver);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		drt.waitForAllImagesToLoad();
	
		//load result on all viewboxes after layout change.
	    for(int i=1;i<=drt.getNumberOfCanvasForLayout();i++)
		{
		cs.selectResultFromSeriesTab(i, firstResultDescriptionrtStruct);
		}

		drt.resizeBrowserWindow(1200, 900);
		drt.waitForViewerpageToLoad();
		
		for(int i=1;i<=drt.getNumberOfCanvasForLayout();i++)
		{
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify scroll bar component for Finding Menu in viewbox-"+i);
	    drt.mouseHover(drt.getLegendOptionsList(i).get(2));
	    drt.assertTrue(drt.verticalScrollBarComponent(i).isDisplayed(), "Verify new UI of Scrollbar component present on viewbox1", "Verified");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify UI of regular scrollbar component for Finding Menu in viewbox-"+i);
		drt.assertTrue(drt.verifyPropertyOfRegularVerticalScrollBar(drt.verticalScrollBarComponent(i), drt.verticalScrollBarSlider(i)),"Verify width and background color of regular vertical scrollbar","Verified");
	 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify UI of scrollbar component when mouse pointer on the scrollbar in viewbox-"+i);
		drt.click(drt.patientIDHeader);
		drt.mouseHover(drt.verticalScrollBarComponent(i));
		drt.assertTrue(drt.verifyPropertyOfVerticalScrollBarWhenMousePointer(drt.verticalScrollBarComponent(i), drt.verticalScrollBarSlider(i)),"Verify width and background color of vertical scrollbar when mouse pointer on the scrollbar","Verified");
		} 
		
		
	}

	

	public void verifyDicomRTDisplay(String patientName, int inputValuesForLatestRT[], String testcase) throws InterruptedException {

		drt=new DICOMRT(driver);
		for(int j = 0; j < inputValuesForLatestRT.length ; j++ ) {

			drt.scrollToImage( 1, inputValuesForLatestRT[j]);
			drt.waitForViewerpageToLoad();

			drt.compareElementImage(protocolName, drt.mainViewer, "Checkpoint TC1[1] : verifying the viewer for patient "+patientName, testcase+j);

		}
	}

}
