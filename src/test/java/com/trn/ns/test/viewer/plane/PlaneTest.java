package com.trn.ns.test.viewer.plane;
import java.awt.AWTException;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.page.factory.ViewerToolbar;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PlaneTest extends TestBase {

	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private HelperClass helper;
	private ContentSelector cs;
	private DatabaseMethodsADB userActionLog;
	private ViewBoxToolPanel viewBoxPanel;
	private ViewerSliderAndFindingMenu findingMenu;

	private ExtentTest extentTest;
	private String loadedTheme;
	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String chestFilePath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String chestPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, chestFilePath);

	String johnDoe = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String johnDoePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, johnDoe);

	String tcgaFilepath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String tcgaPatientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcgaFilepath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String IHEMammoTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);

	String filePath2 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath2);

	String filePath = Configurations.TEST_PROPERTIES.get("MR_CARDIAC_filepath");
	String mrCardiacPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String aidocFilePath = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String aidocPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aidocFilePath);
	
	String orientationRFilePath = Configurations.TEST_PROPERTIES.get("OrientationSample_R");
	String orRPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, orientationRFilePath);
	
	String covidFilePath =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, covidFilePath);
	
	String lidcFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
	String lidcPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, lidcFilePath);

	String ribFilepath = Configurations.TEST_PROPERTIES.get("Rib-AAA-2_Filepath");
	String ribPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ribFilepath);
	
	String anonyFilepath = Configurations.TEST_PROPERTIES.get("Anonymous1_Filepath");
	String annoyPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonyFilepath);
			
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	String nsDBName = Configurations.TEST_PROPERTIES.get("dbName");
	String userB ="user";
	private ViewerTextOverlays overlays;
	private ViewerOrientation orin;
		
	@Test(groups ={"IE11","Chrome","Edge","US2050","Positive","E2E","F777"})
	public void test01_US2050_TC9131_TC9132_TC9135_verifyPlaneIsVisible() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify plane text overlay display ."
				+ "<br> Verify changing plane  text overlay  using plane menu drop-down."
				+ "<br> Verify that Plane overlay is displayed for all Annotation Levels: Minimum, Default and Full.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName, username, password, 1);
		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		overlays = new ViewerTextOverlays(driver);

		for(int i =1 ;i<=totalViewboxes;i++) {
			String currentPlane = viewerPage.getText(overlays.getPlaneInfo(i));
			viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(i)), "Checkpoint[1."+i+"/5]", "Verifying plane is displayed");
			viewerPage.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[2."+i+"/5]", "Verifying plane text");
			
			overlays.openTabInViewbox(i, overlays.planeTab);
			
			List<WebElement> options = overlays.getPlaneLabels(i);
			for(int j=0;j<options.size();j++) {		
				
				if(viewerPage.getText(options.get(j)).equalsIgnoreCase(currentPlane))
					viewerPage.assertTrue(overlays.verifyPlaneRadioAndTileIsSelected(i, viewerPage.getText(options.get(j)),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/5]","Verifying the selected plane is displayed in italic");
				else
					viewerPage.assertFalse(overlays.verifyPlaneRadioAndTileIsSelected(i, viewerPage.getText(options.get(j)),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[3/5]","Verifying the selected plane is displayed normal");
			}
			
		}

		for(int j =0 ;j<ViewerPageConstants.ALL_OVERLAYS.size();j++) {
			overlays.selectTextOverlays(ViewerPageConstants.ALL_OVERLAYS.get(j));
			for(int i =1 ;i<=totalViewboxes;i++) {
				viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(i)), "Checkpoint[4."+i+"/5]", "Verifying plane is displayed after overlay changes");
				viewerPage.assertEquals(viewerPage.getText(overlays.getPlaneInfo(i)),ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[5."+i+"/5]", "Verifying plane text is remain same after overlay changes");

			}
		}
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2050","Negative","E2E","F777"})
	public void test02_US2050_TC9133_TC9199_verifyPlaneIsNotVisibleForNonDICOM() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that plane text overlay is not getting  displayed for Non- DICOM images."
				+ "<br> Verify that Plane overlay is not getting display for multi frame data,  inconsistent orientation in series or if number of instance is less than 3.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(johnDoePatientName, username, password , 1);
		overlays = new ViewerTextOverlays(driver);
		
		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		for(int i =1 ;i<=totalViewboxes;i++) {
			viewerPage.assertFalse(viewerPage.isElementPresent(overlays.getPlaneByText(i)), "Checkpoint[1."+i+"/3]", "Verifying plane is not displayed on non dicom as well on dicom which has < 3 slices");
			if(i!=3)
				viewerPage.assertFalse(overlays.verifyPlaneTabPresence(i), "Checkpoint[3/3]", "Verifying plane is displayed on DICOM data");
		}
	
		helper.loadViewerDirectly(chestPatient, 2);
		viewerPage.assertFalse(viewerPage.isElementPresent(overlays.getPlaneByText(1)), "Checkpoint[2/3]", "Verifying plane is not displayed for SR report data");
		// less than 3 slices
		viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(2)), "Checkpoint[3/3]", "Verifying plane is displayed on DICOM data");
		viewerPage.assertTrue(overlays.verifyPlaneTabPresence(2), "Checkpoint[3/3]", "Verifying plane is displayed on DICOM data");
				
	}

	@Test(groups ={"IE11","Chrome","Edge","US2050","Negative","F777"})
	public void test03_US2050_TC9136_verifyAllResults() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Plane text overlay is getting displayed for all results series.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(tcgaPatientName, username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		
		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();
		for(int i =1 ;i<=totalViewboxes;i++) {
			viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(i)), "Checkpoint[1."+i+"/3]", "Verifying plane is displayed on RT result");
			viewerPage.assertTrue(overlays.verifyPlaneTabPresence(i), "Checkpoint[1."+i+"/3]", "Verifying plane is displayed on RT result");
		}
		
		helper.loadViewerDirectly(imbioPatientName, 1);
		viewerPage.assertFalse(viewerPage.isElementPresent(overlays.getPlaneByText(1)), "Checkpoint[2/3]", "Verifying plane is displayed on SC data");
		viewerPage.assertFalse(overlays.verifyPlaneTabPresence(1), "Checkpoint[1/3]", "Verifying plane is displayed on SC result");
		
		helper.loadViewerDirectlyUsingID(pmapPatientID, 1);
		viewerPage.assertFalse(viewerPage.isElementPresent(overlays.getPlaneByText(1)), "Checkpoint[3/3]", "Verifying plane is displayed on PMAP result");
		viewerPage.assertFalse(overlays.verifyPlaneTabPresence(1), "Checkpoint[1/3]", "Verifying plane is displayed on pmap result");
		
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2050","Positive","US2326","E2E","F777"})
	public void test04_US2050_TC9146_US2326_TC9928_verifyOrientationMarker() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify  orientation marker & slice number after changing planes"
				+ "<br> Verify the orientation marks on the non-native planes.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName, username, password, 1);
		userActionLog = new DatabaseMethodsADB(driver);
		db = new DatabaseMethods(driver);
		overlays = new ViewerTextOverlays(driver);
		orin = new ViewerOrientation(driver);
		
		
		
		viewerPage.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/5]","verifying the orientation marker");		
		
		String sopId = db.getInstanceUID(patientName, viewerPage.getSeriesDescriptionOverlayText(1), NSDBDatabaseConstants.IMAGENUMBER, viewerPage.getCurrentScrollPosition(1));
		String value = db.getInstanceInfo(NSDBDatabaseConstants.ROWS,sopId);
		
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		String currentSlice = overlays.getMaxNumberofScrollForViewbox(1)/2+"";
		verifyPlanesAndOtherParms(ViewerPageConstants.SAGITTAL_KEY, currentSlice);
		userActionLog.assertTrue(userActionLog.verifyActionForPlaneChange(ViewerPageConstants.SAGITTAL_KEY,ViewerPageConstants.AXIAL_KEY, ViewerPageConstants.AXIAL_KEY, currentSlice),"Checkpoint[2/5]","verifying the user action log");
		
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		verifyPlanesAndOtherParms(ViewerPageConstants.CORONAL_KEY, currentSlice);
		
		viewerPage.assertEquals(viewerPage.getImageMaxScrollPositionOverlayText(1),value,"Checkpoint[3.1/5]","verifying the maximum slices");		
		
		//Wait For log to get recorded
		viewerPage.waitForTimePeriod(2000);
		userActionLog.assertTrue(userActionLog.verifyActionForPlaneChange(ViewerPageConstants.CORONAL_KEY,ViewerPageConstants.SAGITTAL_KEY, ViewerPageConstants.AXIAL_KEY, currentSlice),"Checkpoint[3.2/5]","verifying the user action log");
		
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		verifyPlanesAndOtherParms(ViewerPageConstants.SAGITTAL_KEY, currentSlice);
		viewerPage.waitForTimePeriod(2000);
		userActionLog.assertTrue(userActionLog.verifyActionForPlaneChange(ViewerPageConstants.SAGITTAL_KEY,ViewerPageConstants.CORONAL_KEY, ViewerPageConstants.AXIAL_KEY, currentSlice),"Checkpoint[4/5]","verifying the user action log");
		
		userActionLog.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		currentSlice = viewerPage.getCurrentScrollPosition(2);
		verifyPlanesAndOtherParms(ViewerPageConstants.AXIAL_KEY, currentSlice);
		viewerPage.waitForTimePeriod(2000);
		
		userActionLog.assertTrue(userActionLog.verifyActionForPlaneChange(ViewerPageConstants.AXIAL_KEY,ViewerPageConstants.SAGITTAL_KEY, ViewerPageConstants.AXIAL_KEY, currentSlice),"Checkpoint[5/5]","verifying the user action log");
		

	
	}

	@Test(groups ={"IE11","Chrome","Edge","US2050","Negative","E2E","F777"})
	public void test05_US2050_TC9199_verifyPlaneOnMultiphase() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Plane overlay is not getting display for multi frame data,  inconsistent orientation in series or if number of instance is less than 3.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(mrCardiacPatient,username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		orin = new ViewerOrientation(driver);
		
		int totalViewboxes = viewerPage.getNumberOfCanvasForLayout();

		for(int i =1 ;i<=totalViewboxes;i++)
			viewerPage.assertFalse(viewerPage.isElementPresent(overlays.getPlaneByText(i)), "Checkpoint[1."+i+"/3]", "Verifying plane is not displayed for multiphase data");
	
		helper.loadViewerDirectly(aidocPatient, 2);
		cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		cs.selectSeriesFromSeriesTab(1, series.get(2));		
		viewerPage.assertEquals(viewerPage.getText(overlays.getPlaneInfo(1)),ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY), "Checkpoint[2/3]", "Verifying plane text is displayed after selecting the series from content selector");
		
		cs.scrollToImage(1, 1);
		String left =orin.getLeftOrientationMarkerText(1);
		String right =orin.getRightOrientationMarkerText(1);
		String top = orin.getTopOrientationMarkerText(1);
		String bottom =orin.getBottomOrientationMarkerText(1);
		
		cs.scrollDownToSliceUsingKeyboard(1);
		
		viewerPage.assertNotEquals(orin.getLeftOrientationMarkerText(1),left,"Checkpoint[3.1/3]","Verifying the left marker is not same across series");
		viewerPage.assertNotEquals(orin.getRightOrientationMarkerText(1),right,"Checkpoint[3.2/3]","Verifying the right marker is not same across series");
		viewerPage.assertNotEquals(orin.getTopOrientationMarkerText(1),top,"Checkpoint[3.3/3]","Verifying the top marker is not same across series");
		viewerPage.assertNotEquals(orin.getBottomOrientationMarkerText(1),bottom,"Checkpoint[3.4/3]","Verifying the bottom marker is not same across series");
		
	
			
				
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2050","Positive","F777"})
	public void test06_US2032_TC9161_TC9416_verifyvolumeMessage() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the creation of  the volumes  when user requests/changes the plane from viewer."
				+ "Verify the reusing the existing volumes created - Single and Multi user.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, username);
		patientPage = new PatientListPage(driver);
		patientPage.waitForPatientPageToLoad();		
		
		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(orRPatient,1);
		overlays = new ViewerTextOverlays(driver);
		orin = new ViewerOrientation(driver);
		
		db = new DatabaseMethods(driver);
		db.truncateTable(nsDBName,NSDBDatabaseConstants.NS_LOG_TABLE);
		
		viewerPage.assertEquals(orin.getLeftOrientationMarkerText(1),ViewerPageConstants.HEAD_OR_OVERLAY,"Checkpoint[1.a/6]","Verifying the markers for default loaded plane");
		viewerPage.assertEquals(orin.getRightOrientationMarkerText(1),ViewerPageConstants.FOOT_OR_OVERLAY,"Checkpoint[1.b/6]","Verifying the markers for default loaded plane");
		viewerPage.assertEquals(orin.getTopOrientationMarkerText(1),ViewerPageConstants.ANTERIOR_OR_OVERLAY,"Checkpoint[1.c/6]","Verifying the markers for default loaded plane");
		viewerPage.assertEquals(orin.getBottomOrientationMarkerText(1),ViewerPageConstants.POSTERIOR_OR_OVERLAY,"Checkpoint[1.d/6]","Verifying the markers for default loaded plane");

		
		String seriesID = db.getSeriesInstanceUID(patientName,viewerPage.getSeriesDescriptionOverlayText(1));
		int count = db.getLogsCount("Created volume for series "+seriesID).size();
		viewerPage.assertTrue(db.getLogsCount("Created volume for series "+seriesID).isEmpty(),"Checkpoint[2/6]","verifying there is no volumne created in Log on load");	
		
		count = count+1;
		
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewerPage.assertEquals(db.getLogsCount("Created volume for series "+seriesID).size(),count,"Checkpoint[3/6]","verifying the volumne is created after changing the plane");	
				
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewerPage.assertEquals(db.getLogsCount("Created volume for series "+seriesID).size(),count,"Checkpoint[4/6]","verifying same volumne is used though user changes the plane again");	
		
		viewerPage.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		RegisterUserPage register = new RegisterUserPage(driver);
		register.createNewUser(userB, userB,LoginPageConstants.SUPPORT_EMAIL, userB, userB, userB);
		Header header = new Header(driver);		
		header.logout();
		
		loginPage.navigateToBaseURL();
		loginPage.login(userB, userB);
		patientPage.waitForPatientPageToLoad();		
		helper.loadViewerDirectly(orRPatient, 1);		
		
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewerPage.assertEquals(db.getLogsCount("Created volume for series "+seriesID).size(),count,"Checkpoint[5/6]","verifying that no volumne is created for other user on plane change for same series");	
				
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewerPage.assertEquals(db.getLogsCount("Created volume for series "+seriesID).size(),count,"Checkpoint[6/6]","verifying that no volumne is created for other user on plane change for same series");	
		
	
		
	
	}

	@Test(groups ={"IE11","Chrome","Edge","US2050","Positive","E2E","F777"})
	public void test07_US2032_TC9384_verifyViewerOptAfterPlaneChange() throws InterruptedException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the viewer options for the rendered plane on viewer.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		viewerPage =helper.loadViewerDirectly(patientName,username, password,1);
		overlays = new ViewerTextOverlays(driver);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		
		String ww = viewerPage.getWindowWidthValueOverlayText(1);
		String wc = viewerPage.getWindowCenterValueOverlayText(1);
		String zoom =viewBoxPanel.getZoomLevelValue(1);
		String currentSlice = viewerPage.getCurrentScrollPosition(1);
		
		viewerPage.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/9]","verifying the defeault plane on load");	
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewerPage.assertEquals(ww ,viewerPage.getWindowWidthValueOverlayText(1), "Checkpoint[2/9]", "verifying the WW remains same after plane change");
		viewerPage.assertEquals(wc ,viewerPage.getWindowCenterValueOverlayText(1), "Checkpoint[3/9]", "verifying the WC remains same after plane change");
		viewerPage.assertNotEquals(zoom ,viewBoxPanel.getZoomLevelValue(1), "Checkpoint[4/9]", "verifying the zoom remains same after plane change");
		viewerPage.assertNotEquals(currentSlice ,viewerPage.getCurrentScrollPosition(1), "Checkpoint[5/9]", "verifying the scroll remains same after plane change");
				
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0,0,0,30);
		viewerPage.assertNotEquals(currentSlice ,viewerPage.getCurrentScrollPosition(1), "Checkpoint[6//9]", "verifying the scroll happens after plane change");
		
		viewerPage.selectZoomFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0,0,10,30);
		viewerPage.assertNotEquals(zoom ,viewBoxPanel.getZoomLevelValue(1), "Checkpoint[7/9]", "verifying the zoom happens after plane change");
		
		viewerPage.selectWindowLevelFromQuickToolbar(1);
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0,0,10,30);
		viewerPage.assertNotEquals(ww ,viewerPage.getWindowWidthValueOverlayText(1), "Checkpoint[8/9]", "verifying the Windowing happens after plane change");
		viewerPage.assertNotEquals(wc ,viewerPage.getWindowCenterValueOverlayText(1), "Checkpoint[9/9]", "verifying the windowing happens after plane change");
		
	
	}
	
	//US2329:Disable creation of measurements in non-native plane
	@Test(groups ={"IE11","Chrome","Edge","US2329","Positive","F1090","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test08_US2329_TC10123_TC10124_verifyMeasurementIconDisableForNonNativePlane(String theme) throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that GSPS create/edit annotations are disabled in non native plane.<br>"+
				"Verify that tools like Zoom, PAN, Cine, WW/WL, Triangulation are enabled in the non native plane.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ViewerToolbar tool=new ViewerToolbar(driver);
	
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/6]","verifying the defeault plane on load");	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/6]","verifying the default plane on load changes to "+ViewerPageConstants.CORONAL_KEY);	
		PagesTheme pageTheme=new PagesTheme(driver);
		for(int i=0;i<tool.allToolIcons.size();i++)
		{
		tool.assertEquals(tool.getCssValue(tool.allToolIcons.get(i), NSGenericConstants.OPACITY), ViewerPageConstants.OPACITY_FOR_DISABLE_ICON, "Checkpoint[3/6]", "Verified that measurement icon is disable on viewer toolbar");
		viewBoxPanel.assertTrue(pageTheme.verifyThemeForIcon(tool.allToolIcons.get(i), loadedTheme),"Checkpoint[4/6]","Verified theme for disable measurement icon.");
		}
		
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(1),"Checkpoint[5/6]","Verified that measurement icons are disable on viewer toolbar");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(1),"Checkpoint[6/6]","Verified that measurement icons are not visible on quick toolbar.");
		
	}

	@Test(groups ={"IE11","Chrome","Edge","US2329","Positive","F1090","E2E"})
	public void test09_US2329_TC10143_verifyShortcutKeyDisableForNonNativePlane() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that shortcut keys for GSPS create/edit measurement are disabled on non native plane.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
	
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/10]","verifying the default plane on load");	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/10]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);	
		
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(1),"Checkpoint[3/10]","Verified that measurement icons are disable on viewer toolbar");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(1),"Checkpoint[4/10]","Verified that measurement icons are not visible on quick toolbar.");
		
		//select shortcut for distance annotation
		viewBoxPanel.enableOrDisableDistanceIconUsingKeyboardDKey();
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(1),"Checkpoint[5/10]","Verified that measurement icons are disable on viewer toolbar");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(1),"Checkpoint[6/10]","Verified that measurement icons are not visible on quick toolbar.");
		MeasurementWithUnit lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.drawLine(2, 50, 50, 50, 0);
		lineWithUnit.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(2, 1), "Checkpoint[7/10]","Verified that distance annotation on Native plane.");
		
		//select shortcut key for cine
		int currentSlice=viewBoxPanel.getCurrentScrollPositionOfViewbox(1);
		viewBoxPanel.click(viewBoxPanel.getViewPort(1));
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		viewBoxPanel.assertFalse(viewBoxPanel.isCineStopped(1),"Checkpoint[8/10]","Verify Cine is getting started using keyboard shortcut.");
		viewBoxPanel.waitForTimePeriod(2000);
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		viewBoxPanel.assertTrue(viewBoxPanel.isCineStopped(1),"Checkpoint[9/10]","Verify Cine is getting stopped using keyboard shortcut.");
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), currentSlice, "Checkpoint[10/10]","Verified that cine played successfully using shortcut key on non Native plane.");
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2329","Positive","F1090","E2E"})
	public void test10_US2329_TC10145_verifyMeasurementWhenUserSwitchesToActivePlane() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that GSPS create/edit measurement annotation are enabled when user switches back to the active plane.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(aidocPatient,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
	
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/11]","verifying the default plane on load");	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/11]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);	
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(1),"Checkpoint[3/11]","Verified that measurement icons are disable on viewer toolbar");
		viewBoxPanel.assertTrue(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(1),"Checkpoint[4/11]","Verified that measurement icons are not visible on quick toolbar.");
		viewBoxPanel.click(viewBoxPanel.getViewPort(1));
		
		viewBoxPanel.mouseHover(viewBoxPanel.getViewPort(2));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(2,ViewerPageConstants.CORONAL_KEY),"Checkpoint[5/11]","verified that Native plane.");	
		viewBoxPanel.assertFalse(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(2),"Checkpoint[6/11]","Verified that measurement icons are enable on viewer toolbar");
		viewBoxPanel.assertFalse(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(2),"Checkpoint[7/11]","Verified that measurement icons are visible on quick toolbar.");
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[8/11]","Verified plane when switch from Non Native to Native again.");	
		viewBoxPanel.assertFalse(viewBoxPanel.verifyIconsDisableForNonNativePlaneInViewerToolbar(1),"Checkpoint[9/11]","Verified that measurement icons are disable on viewer toolbar");
		viewBoxPanel.assertFalse(viewBoxPanel.verifyIconsPresenceForNonNativePlaneInQuickToolbar(1),"Checkpoint[10/11]","Verified that measurement icons are not visible on quick toolbar.");

		CircleAnnotation circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 50, 50, 50, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1), "Checkpoint[11/11]", "Verified that user able to draw annotation after switching back to native plane.");
	}

	//US2327: Non-native plane finding list
	@Test(groups ={"IE11","Chrome","Edge","US2327","Positive","F1153","E2E"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test11_PlaneChange"})
	public void test11_US2327_TC10293_DR2475_TC10153_verifyNavUsingFindingMenuForNonNativePlane(String filepath) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Non-Native Plane Jump to finding from Finding Menu.<br>"+
		"Verify that Plane is  getting changed and image is also  getting loaded without console error.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		patientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Configurations.TEST_PROPERTIES.get(filepath));
		
		helper.loadViewerDirectly(patientName,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		
		findingMenu.selectFindingFromTable(1);	
		int circle1Slice=findingMenu.getCurrentScrollPositionOfViewbox(1);
		findingMenu.selectFindingFromTable(2);	
		int circle2Slice=findingMenu.getCurrentScrollPositionOfViewbox(1);
		
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/11]","verifying the default plane on load is "+ViewerPageConstants.AXIAL_KEY);	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/11]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), circle1Slice, "Checkpoint[3/11]", "Verified that slice change when plane change to Non Native plane.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[4/11]", "Verified that no console errors seen after changing the plane from Axial to coronal.");
		findingMenu.selectFindingFromTable(1);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[5/11]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load when finding is selected from finding Menu.");		
		viewBoxPanel.assertEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), circle1Slice, "Checkpoint[6/11]","Verified that default slice on which findings is present.");
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[7/11]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);	
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), circle2Slice, "Checkpoint[8/11]","Verified that slice change when plane change to Non Native plane.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[9/11]", "Verified that no console errors seen after changing the plane from Axial to Sagittal.");
		findingMenu.selectFindingFromTable(2);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[10/11]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load when finding is selected from finding Menu.");		
		viewBoxPanel.assertEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), circle2Slice, "Checkpoint[11/11]","Verified that default slice on which findings is present.");

	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2327","Positive","F1153","E2E"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test11_PlaneChange"})
	public void test12_US2327_TC10307_verifyNavUsingARToolbarForNonNativePlane(String filepath) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Non-Native Plane Jump to finding from A/R Navigation Icons.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		patientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Configurations.TEST_PROPERTIES.get(filepath));
		
		helper.loadViewerDirectly(patientName,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		
		findingMenu.waitForTimePeriod(2000);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/5]","verifying the default plane on load");	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/5]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);	
		findingMenu.openGSPSRadialMenu(1);
		findingMenu.selectNextfromGSPSRadialMenu(1);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[3/5]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load after click on next from AR toolbar.");		
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[4/5]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);	
		findingMenu.openGSPSRadialMenu(1);
		findingMenu.selectPreviousfromGSPSRadialMenu(1);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[5/5]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load after click on previous from AR toolbar.");			

	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2327","Positive","F1153"},dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test11_PlaneChange"})
	public void test13_US2327_TC10308_verifyNavUsingArrowkeyForNonNativePlane(String filepath) throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Non-Native Plane Jump to finding from Keyboard.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		patientName= DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, Configurations.TEST_PROPERTIES.get(filepath));
		
		helper.loadViewerDirectly(patientName,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		
		findingMenu.waitForTimePeriod(2000);
		int defaultSlice=findingMenu.getCurrentScrollPositionOfViewbox(1);
		
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/8]","verifying the default plane on load is "+ViewerPageConstants.AXIAL_KEY);	
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/8]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);	
		int changeSlice=findingMenu.getCurrentScrollPositionOfViewbox(1);
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), defaultSlice, "Checkpoint[3/8]", "verified slice number after changing tha plane.");
		findingMenu.pressKeys(Keys.ARROW_RIGHT);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[4/8]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load after click on Arrow right from keyboard.");		
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), changeSlice, "Checkpoint[5/8]", "verified slice number after pressing arrow right key.");
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[6/8]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);
		int nextChangeSlice=findingMenu.getCurrentScrollPositionOfViewbox(1);
		findingMenu.pressKeys(Keys.ARROW_LEFT);
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[7/8]","verifying the default plane "+ViewerPageConstants.AXIAL_KEY+" load after click on Arrow left from keyboard.");		
		viewBoxPanel.assertNotEquals(viewBoxPanel.getCurrentScrollPositionOfViewbox(1), nextChangeSlice, "Checkpoint[8/8]", "verified slice number after pressing arrow left key");

	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2327","Positive","F1153"})
	public void test14_US2327_TC10329_verifyNavUsingArrowkeyForNonNativePlane() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Plane selection is enabled when source data is not tilted and disabled if source data is tilted.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+pmapPatientID+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectlyUsingID(pmapPatientID,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		findingMenu=new ViewerSliderAndFindingMenu(driver);
        cs=new ContentSelector(driver);
        cs.closeNotification();
        
        cs.selectSeriesFromSeriesTab(1, cs.getAllSeries().get(0));
        viewBoxPanel.assertFalse(viewBoxPanel.isElementPresent(viewBoxPanel.getPlaneByText(1)), "Checkpoint[1/5]", "Verifying plane is not visible when source data is tilted.");
        viewBoxPanel.assertFalse(viewBoxPanel.verifyPlaneTabPresence(1), "Checkpoint[2/5]", "Verifying plane tab is disable when source data is tilted.");
        
        //load covid patient
        helper.loadViewerDirectly(covidPatientName,username, password,1);
        cs.closeNotification();
        cs.selectSeriesFromSeriesTab(1, cs.getAllSeries().get(0));
        viewBoxPanel.assertTrue(viewBoxPanel.isElementPresent(viewBoxPanel.getPlaneByText(1)), "Checkpoint[3/5]", "Verifying plane is visible when source data is not tilted.");
        viewBoxPanel.assertTrue(viewBoxPanel.verifyPlaneTabPresence(1), "Checkpoint[4/5]", "Verifying plane tab is enable when source data is not tilted.");
        viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[5/5]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US2327","DR2790","Positive","US2326","F1153","E2E"})
	public void test15_US2327_TC10331_US2326_TC9991_DR2790_TC10744_verifyCachingWhenSwitchingPlanes() throws InterruptedException, AWTException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Impact of caching of pmap data (Covid-003) when switching planes."
				+ "<br> Verify Caching when changing planes."
				+ "<br> Verify that caching is not happening for already cached Axial plane series.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+covidPatientName+"in viewer" );

		helper = new HelperClass(driver);
        //load covid patient
        helper.loadViewerDirectly(covidPatientName,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
        cs=new ContentSelector(driver);
        cs.closeNotification();

        cs.waitForTimePeriod(3000);
        String result=cs.getAllResults().get(0);
        DatabaseMethods db=new DatabaseMethods(driver);
        String seriesUID=db.getSeriesInstanceUID(covidPatientName, result);
        cs.clearConsoleLogs();
        
        //load first result 
        cs.selectResultFromSeriesTab(1,result);
        List<String> logs = cs.getConsoleLogs();
        long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for "+seriesUID)).count();
        viewBoxPanel.assertEquals(count, 2, "Checkpoint[1/7]", "Verifying worker completed caching active viewbox");
     
    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/7]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
    	
    	//In order to get cache completed message we have to wait this much time.
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[3/7]", "Verifying worker completed caching active viewbox");

    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[4/7]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);
    	
    	//wait is required for Cache to be completed
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[5/7]", "Verifying worker completed caching active viewbox");
    	
    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[6/7]","verifying the default plane change to "+ViewerPageConstants.AXIAL_KEY);
    	//wait is required for Cache to be completed
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count,0, "Checkpoint[7/7]", "Verifying caching is not happening when we switch back to native plane .");
        
	}
	
	private void verifyPlanesAndOtherParms(String planeSelected, String currentSlice) {
		
		viewerPage.assertEquals(viewerPage.getText(overlays.getPlaneInfo(1)),ViewerPageConstants.PLANES.get(planeSelected), "Checkpoint[B.1/B.3]", "Verifying plane is changed and changed plane "+planeSelected+" is displayed");
		viewerPage.assertTrue(orin.verifyOrientationForPlane(1,planeSelected),"Checkpoint[B.2/B.3]","Verifying the markers are displayed as per plane"+planeSelected);		
		viewerPage.assertEquals(viewerPage.getCurrentScrollPosition(1),currentSlice,"Checkpoint[B.3/B.3]","verifying the current slice is not changed");		
	
	}
	
	// US2328 - Non-native plane jump to finding from output panel	
	
	@Test(groups ={"IE11","Chrome","Edge","F777","US2328","DR2630","Positive","F1153"})
	public void test16_01_US2328_TC10433_DR2630_TC10636_verifyFindingsOnNativePlanesSR() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings displayed and Jump to finding from Output Panel when non-native plane is loaded on viewer. <br>"+
		"Verify images are not corrupted in the non-native plane.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+chestPatient);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(chestPatient,username, password,1);
		
		overlays = new ViewerTextOverlays(driver);
		OutputPanel panel = new OutputPanel(driver);
		panel.closeNotification();
		
		panel.enableFiltersInOutputPanel(false, false, true);
		int numberOfThumb = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		String currentPlane = viewerPage.getText(overlays.getPlaneInfo(2));
		viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(2)), "Checkpoint[1/9]", "Verifying plane is displayed");
		viewerPage.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[2/9]", "Verifying plane text - Axial");
		
		overlays.selectPlane(2, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewerPage.assertEquals(viewerPage.getText(overlays.getPlaneInfo(2)),ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY), "Checkpoint[3.1/9]", "Verifying plane text- coronal after change");
		viewerPage.assertTrue(viewerPage.verifyDicomImageLoadedInViewer(2), "Checkpoint[3.2/9]", "Verified that image loaded on viewer in non native plane.");
		
		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.assertTrue(circle.getAllCircles(2).isEmpty(), "Checkpoint[4/9]", "verifying that findings are displayed in non-native plane");
		
		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(),numberOfThumb, "Checkpoint[5/9]", "verifying the thumbnails are displayed same");
		
		panel.clickOnJumpIcon(1);
		circle.assertFalse(circle.getAllCircles(2).isEmpty(), "Checkpoint[6/9]", "verifying that findings are displayed after click on thumbnail");
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActivePendingGSPS(2, 1), "Checkpoint[7/9]", "verifying the finding is focused");
		
		viewerPage.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[8/9]", "Verifying plane text - Axial");
		viewerPage.assertTrue(overlays.verifyPlaneRadioAndTileIsSelected(2, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[9/9]","Verifying the selected plane is displayed selected");
		
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","F777","US2328","DR2630","Positive","F1153"})
	public void test16_02_US2328_TC10433_TC10470_TC10509_DR2630_TC10636_verifyFindingsOnNativePlanesRT() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings displayed and Jump to finding from Output Panel when non-native plane is loaded on viewer."
				+ "<br> Verify the warning banner displayed when a finding is selected from Output Panel from a series that is not loaded on viewer."
				+ "<br> Verify RTLegend and PMAP LUT bar is not visible on non-native plane.<br>" +
				"Verify images are not corrupted in the non-native plane.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+tcgaPatientName);
		helper = new HelperClass(driver);		
		DICOMRT rt = helper.loadRTDirectly(tcgaPatientName,username, password,1);
		
		overlays = new ViewerTextOverlays(driver);
		OutputPanel panel = new OutputPanel(driver);
				
		panel.enableFiltersInOutputPanel(false, false, true);
		int numberOfThumb = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		String currentPlane = overlays.getText(overlays.getPlaneInfo(1));
		overlays.assertTrue(overlays.isElementPresent(overlays.getPlaneByText(1)), "Checkpoint[1/12]", "Verifying plane overlay is displayed - Axial");
		overlays.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[2/12]", "Verifying plane text - Axial is displayed");
		
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		overlays.assertEquals(overlays.getText(overlays.getPlaneInfo(1)),ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY), "Checkpoint[3.1/12]", "Verifying plane text- coronal is displayed");
		overlays.assertTrue(overlays.verifyDicomImageLoadedInViewer(1), "Checkpoint[3.2/12]", "Verified that image loaded on viewer in non native plane.");
		overlays.assertTrue(rt.legendOptionsList.isEmpty(), "Checkpoint[4/12]", "Verifying legend options are not displayed in non-native plane");
		overlays.assertFalse(rt.isElementPresent(rt.legendOptions), "Checkpoint[5/12]", "Verifying legend is not displayed");
		
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.assertTrue(poly.getAllPolylines(1).isEmpty(), "Checkpoint[6/12]", "verifying no contours are displayed");
		
		panel.enableFiltersInOutputPanel(false, false, true);
		overlays.assertEquals(panel.thumbnailList.size(),numberOfThumb, "Checkpoint[7/12]", "verifying the thumbnails are same for native plane");
		
		panel.clickOnJumpIcon(1);
		poly.assertFalse(poly.getAllPolylines(1).isEmpty(), "Checkpoint[8/12]", "verifying that contours are displayed after click on jump to icon");
	
		rt.assertEquals(rt.getHexColorValue(rt.getSelectedContourColor()),panel.getHexColorValue(panel.getContourColorThumbnail(1)), "Checkpoint[9/12]", "Contour should have solid circle to highlight which contour is selected. ");

		overlays.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[10/12]", "Verifying plane text - Axial");
		overlays.assertTrue(overlays.verifyPlaneRadioAndTileIsSelected(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[11/12]","Verifying the selected plane is displayed");
		
		panel.openAndCloseOutputPanel(false);
		
		PointAnnotation point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		
		point.drawPointAnnotationMarkerOnViewbox(1,-200, -200);
		helper.loadViewerDirectly(tcgaPatientName,1);
		
		panel.enableFiltersInOutputPanel(false, false, true);
		panel.clickOnJumpIcon(1);
		
		panel.assertTrue(panel.verifyNotificationPopUp(ViewerPageConstants.INFO,"\""+overlays.getSeriesDescriptionOverlayText(1)+"\" "+ViewerPageConstants.THUMBNAIL_WARNING_MSG), "Checkpoint[12/12]", "Verified that warning message contains series name for RT data if series not loaded for RT");
	}

	@Test(groups ={"IE11","Chrome","Edge","F777","US2328","DR2630","Positive","F1153"})
	public void test16_03_US2328_TC10433_DR2630_TC10636_verifyFindingsOnNativePlanesGroupData() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings displayed and Jump to finding from Output Panel when non-native plane is loaded on viewer. <br>"+
		"Verify images are not corrupted in the non-native plane.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient "+lidcPatientID);
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectlyUsingID(lidcPatientID,username, password,1);
		
		overlays = new ViewerTextOverlays(driver);
		OutputPanel panel = new OutputPanel(driver);
		panel.closeNotification();
		
		panel.enableFiltersInOutputPanel(false, false, true);
		int numberOfThumb = panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		String currentPlane = viewerPage.getText(overlays.getPlaneInfo(1));
		viewerPage.assertTrue(viewerPage.isElementPresent(overlays.getPlaneByText(1)), "Checkpoint[1/9]", "Verifying plane is displayed");
		viewerPage.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[2/9]", "Verifying plane text as Axial");
		
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewerPage.assertEquals(viewerPage.getText(overlays.getPlaneInfo(1)),ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY), "Checkpoint[3.1/9]", "Verifying plane text is displayed as Coronal after change");
		overlays.assertTrue(overlays.verifyDicomImageLoadedInViewer(1), "Checkpoint[3.2/9]", "Verified that image loaded on viewer in non native plane.");
		
		TextAnnotation text = new TextAnnotation(driver);
		text.assertTrue(text.getAllGSPSObjects(1).isEmpty(), "Checkpoint[4/9]", "Verifying no GSPS are displayed  in non native plane");
		
		panel.enableFiltersInOutputPanel(false, false, true);
		viewerPage.assertEquals(panel.thumbnailList.size(),numberOfThumb, "Checkpoint[5/9]", "verifying the thumbnail are same");
		
		panel.clickOnJumpIcon(1);

		text.assertFalse(text.getAllGSPSObjects(1).isEmpty(), "Checkpoint[6/9]", "verifying the GSPS are displayed on click of jump icon");
		text.assertTrue(text.verifyTextAnnotationIsCurrentActivePendingGSPS(1, 2, false), "Checkpoint[7/9]", "verifying the finding is focused");
		viewerPage.assertEquals(currentPlane,ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY), "Checkpoint[8/9]", "Verifying plane text changed to native plane");
		viewerPage.assertTrue(overlays.verifyPlaneRadioAndTileIsSelected(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY),ThemeConstants.EUREKA_THEME_NAME),"Checkpoint[9/9]","Verifying the selected plane is displayed selected");
	}
	
	@Test(groups ={"IE11","Chrome","Edge","DR2790","Positive"})
	public void test17_DR2790_TC10745_verifyCachingWhenRightPlaneIsOpened() throws InterruptedException, AWTException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that caching is not happening for already cached Axial plane series when right panel is opened.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+covidPatientName+"in viewer" );

		helper = new HelperClass(driver);
        //load covid patient
        helper.loadViewerDirectly(covidPatientName,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
        cs=new ContentSelector(driver);
        cs.closeNotification();

        cs.waitForTimePeriod(3000);
        String result=cs.getAllResults().get(0);
        DatabaseMethods db=new DatabaseMethods(driver);
        String seriesUID=db.getSeriesInstanceUID(covidPatientName, result);
        cs.clearConsoleLogs();
        
        //load first result 
        cs.selectResultFromSeriesTab(1,result);
        List<String> logs = cs.getConsoleLogs();
        long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for "+seriesUID)).count();
        viewBoxPanel.assertEquals(count, 2, "Checkpoint[1/8]", "Verifying worker completed caching active viewbox");
     
    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/8]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
    	
    	//In order to get cache completed message we have to wait this much time.
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[3/8]", "Verifying worker completed caching active viewbox");

    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[4/8]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[5/8]", "Verifying worker completed caching active viewbox");
    	
    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[6/8]","verifying the default plane change to "+ViewerPageConstants.AXIAL_KEY);
    	//In order to get cache completed message we have to wait this much time.
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count,0, "Checkpoint[7/8]", "Verifying caching is not happening when we switch back to native plane .");
        
    	cs.clearConsoleLogs();
    	cs.openAndCloseSeriesTab(false);
    	//In order to get cache completed message we have to wait this much time.
    	cs.waitForTimePeriod(12000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count,0, "Checkpoint[8/8]", "Verifying caching is not happening when right panel is closed .");
	}
	
	@Test(groups ={"IE11","Chrome","Edge","DR2790","Positive"})
	public void test18_DR2790_TC10746_verifyCachingWhenPlaneAndZoomValueChange() throws InterruptedException, SQLException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact]- Verify that caching is happening when plane is changed and zoom % is changed..");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+covidPatientName+"in viewer" );

		helper = new HelperClass(driver);
        //load covid patient
        helper.loadViewerDirectly(covidPatientName,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ViewerOrientation orin=new ViewerOrientation(driver);
        cs=new ContentSelector(driver);
        cs.closeNotification();

        //wait is added for all changes to load on viewer after closing the notification
        cs.waitForTimePeriod(3000);
        String result=cs.getAllResults().get(0);
        DatabaseMethods db=new DatabaseMethods(driver);
        String seriesUID=db.getSeriesInstanceUID(covidPatientName, result);
        cs.clearConsoleLogs();
        
        //load first result 
        cs.selectResultFromSeriesTab(1,result);
      //In order to get cache completed message we have to wait this much time.
        cs.waitForTimePeriod(14000);
        List<String> logs = cs.getConsoleLogs();
        long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for "+seriesUID)).count();
        viewBoxPanel.assertEquals(count, 2, "Checkpoint[1/7]", "Verifying worker completed caching active viewbox");
     
        //change a plane and verify cache completed msg
    	cs.clearConsoleLogs();
    	viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
    	viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/7]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
    	//In order to get cache completed message we have to wait this much time.
    	cs.waitForTimePeriod(14000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[3/7]", "Verifying worker completed caching after changing the plane.");

    	//change the zoom value and verify cache
    	cs.clearConsoleLogs();
    	viewBoxPanel.changeZoomNumber(1, 240);
    	viewBoxPanel.waitForAllChangesToLoad();
    	cs.waitForTimePeriod(20000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count, 1, "Checkpoint[4/7]", "Verifying worker completed caching after changing the zoom value.");
    	
    	//perform cine and verify cache is not restarted as its already done.
    	cs.clearConsoleLogs();
    	viewBoxPanel.click(viewBoxPanel.getViewPort(1));
    	int beforeImg = viewBoxPanel.getCurrentScrollPositionOfViewbox(1);
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		viewBoxPanel.waitForTimePeriod(2000);
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		int afterImg = viewBoxPanel.getCurrentScrollPositionOfViewbox(1);
		logs = cs.getConsoleLogs();
		// check that all  images are found in the cache
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND)).count();
		cs.assertEquals(count, afterImg-beforeImg, "Checkpoint[5/7]", "Verifying that all Images are found");
        
		//close already open series tab and verify cache is restared
    	cs.clearConsoleLogs();
    	cs.openAndCloseSeriesTab(false);
    	cs.waitForTimePeriod(14000);
    	logs = cs.getConsoleLogs();
        count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED)).count();
    	viewBoxPanel.assertEquals(count,1, "Checkpoint[6/7]", "Verifying caching is not happening when right panel is closed .");
    	
    	//perform cine and verify cache is not restarted as its already done.
    	cs.clearConsoleLogs();
    	viewBoxPanel.click(viewBoxPanel.getViewPort(1));
    	beforeImg = viewBoxPanel.getCurrentScrollPositionOfViewbox(1);
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		viewBoxPanel.waitForTimePeriod(2000);
		viewBoxPanel.playOrStopCineUsingKeyboardCKey();
		afterImg = viewBoxPanel.getCurrentScrollPositionOfViewbox(1);
		logs = cs.getConsoleLogs();
		// check that all images are found in the cache
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND)).count();
		cs.assertEquals(count, afterImg-beforeImg, "Checkpoint[7/7]", "Verifying that all phase images are found");
    	
	}

	//DR2851: Switching to non-native plane shows corrupted image
	@Test(groups ={"IE11","Chrome","Edge","DR2851","DR2699","Positive","F777"})
	public void test19_DR2851_TC10891_TC10895_DR2699_TC10638_TC10641_verifyDicomImagesForNonNativePlane() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify no corrupted images are displayed on non-native planes when Rows and columns are not equal. <br>"+
		"Verify no corrupted images are displayed on non-native planes when Rows and columns are equal. <br>"+
		"Verify non-native plane is computed and images are displayed on viewer with VPSoft. <br>"+
		"Verify non-native plane is computed and images are displayed on viewer with VPCUDA.");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
   
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify images on non native plane when Rows and columns are not equal." );
		int maxScroll =viewBoxPanel.getCurrentScrollPositionOfViewbox(2);
        viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[1/18]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[2/18]", "Verify that DICOM image is loaded properly after plane change.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[3/18]", "Verify no console error present while scroll on non native plane.");
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[4/18]", "TC19_01");
		
		for(int i=1;i<=maxScroll;i++)
		{
		viewBoxPanel.scrollDownToSliceUsingKeyboard(1);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[5/18]", "Verify Images are not currupted on Non native plane.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[6/18]", "Verify no console error present while scroll on non native plane.");
		}
		//TC10895: Verify no corrupted images are displayed on non-native planes when Rows and columns are equal.
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify images on non native plane when Rows and columns are equal." );
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel.doubleClick(viewBoxPanel.getViewPort(1));
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[7/18]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[8/18]", "Verify that DICOM image is loaded properly after plane change.");
			
		for(int i=1;i<=maxScroll;i++)
		{
		viewBoxPanel.scrollDownToSliceUsingKeyboard(1);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[9/18]", "Verify Images are not currupted on Non native plane.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[10/18]", "Verify Images are not currupted on Non native plane.");
		}
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[11/18]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[12/18]", "Verify that DICOM image is loaded properly after plane change.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[13/18]", "Verify no console error present after changing the plane from Coronal to Sagittal.");
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[14/18]", "TC019_02");
		
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[15/18]","verifying the default plane change to "+ViewerPageConstants.AXIAL_KEY);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[16/18]", "Verify that DICOM image is loaded properly after plane change.");
		viewBoxPanel.assertFalse(viewBoxPanel.isConsoleErrorPresent(), "Checkpoint[17/18]", "Verify no console error present after changing the plane from Sagittal to Axial.");
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[18/18]", "TC019_03");
	}
	
	
	@Test(groups ={"IE11","Chrome","Edge","DR2627","Positive","F777"})
	public void test20_DR2627_TC10393_verifyMarkersInNonNativePlane() throws InterruptedException, AWTException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Orientation markers and image are displayed correctly when user changes from native plane to non-native plane");
		
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+annoyPatient+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(annoyPatient,username, password,1);
		
		orin = new ViewerOrientation(driver);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		
		cs.selectSeriesFromSeriesTab(1, series.get(1));
		cs.doubleClickOnViewbox(1);
		cs.openAndCloseSeriesTab(false);
		
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[1/4]", "TC20_01");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify images on non native plane when Rows and columns are not equal." );
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/4]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);
		viewBoxPanel.assertTrue(viewBoxPanel.verifyDicomImageLoadedInViewer(1), "Checkpoint[3/4]", "Verify that DICOM image is loaded properly after plane change.");
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[4/4]", "TC20_02");
		
	}

	
	

}
