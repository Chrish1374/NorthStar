package com.trn.ns.test.viewer.overlays;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.LoginPageConstants;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.RegisterUserPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TextOverlayTest extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private ViewerTextOverlays textOverlay;
	private HelperClass helper;	
	private ViewBoxToolPanel preset;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	String liver9Filepath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9Filepath);
	
	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 
	
	String ChestCT1p25mmFilepath = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String ChestCT1p25mmPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ChestCT1p25mmFilepath);
	
	private String loadedTheme="";
	String username_1 = "user_1";

	@Test(groups ={"Chrome", "Edge","US2252","Positive","US2337","US2381","F1081","E2E","F1083"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2252_TC9451_US2337_TC10021_US2381_TC10074_verifyTextOverlayTheme(String theme) throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify text Overlay menu display."
				+ "<br> Verify Text Overlay  Menu  styling. <br>"+
				"Verify the New  text Overlay Menu display.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;


		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Loading the patient on viewer");
		viewerpage = helper.loadViewerDirectly(liver9Patient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		PagesTheme pageTheme= new PagesTheme(driver);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the text overlay icon is present");
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.textOverlayIcon), "Checkpoint[1/18]", "TextOverlay is displaying on Menu");

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the text overlay 4 options , Minimum, default, maximum and custom");
		List<String> options = textOverlay.getTextOverlaysOptions();
		viewerpage.assertEquals(options.size(),4, "Checkpoint[2/18]", "verifying that there are 4 options as a part of overlays");
		List<String> expectedList = Arrays.asList(new String[] {textOverlay.toTitleCase(ViewerPageConstants.MINIMUM_ANNOTATION),textOverlay.toTitleCase(ViewerPageConstants.DEFAULT_ANNOTATION),textOverlay.toTitleCase(ViewerPageConstants.FULL_ANNOTATION), textOverlay.toTitleCase(ViewerPageConstants.CUSTOM_ANNOTATION)});
		viewerpage.assertEquals(options,expectedList, "Checkpoint[3/18]", "verifying that there are 4 options as a part of overlays");
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.overlayContainer), "Checkpoint[4/18]", "Verifying the overlay container is displayed");
		viewerpage.assertTrue(pageTheme.verifyThemeForNotification(textOverlay.textOverlayPopup, ThemeConstants.TAB_ROUNDED_CORNER_POPUP,loadedTheme),"Checkpoint[5/18]", "Verifying the theme and rounded corner for overlay popup");

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Verifying the text labels and sliders presence");
		for(int i=0 ;i < textOverlay.textOverlayRadioButton.size();i++) {
			viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.textOverlayRadioButton.get(i)), "Checkpoint[6/18]", "Verifying the overlay container is displayed");
			viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.textOverlayLabels.get(i)), "Checkpoint[7/18]", "Verifying the overlay container is displayed");

		}

		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.openCustomMenuOptionArrow), "Checkpoint[8/18]", "Verifying the open arrow icon is displayed");
		textOverlay.selectTextOverlays(ViewerPageConstants.CUSTOM_ANNOTATION);
		textOverlay.waitForElementVisibility(textOverlay.customMenu);
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.closeCustomMenuOptionArrow), "Checkpoint[9/18]", "Verifying the close arrow icon is displayed");
		
		viewerpage.assertTrue(pageTheme.verifyThemeForNotification(textOverlay.textOverlayPopup, ThemeConstants.TAB_ROUNDED_CORNER_POPUP,loadedTheme), "Checkpoint[10/18]", "verifying the theme for custom overlay popup");
		viewerpage.assertEquals(textOverlay.customOverlayOptionsSlider.size(),textOverlay.customOverlayOptions.size(), "Checkpoint[11/18]", "verifying the total number of sliders and options are same");

		for(int i=0 ; i<10;i++) {

			viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(textOverlay.customOverlayOptions.get(i), loadedTheme),"Checkpoint[12/18]","verifying the theme on custom overlay labels");
			if(loadedTheme.equals(ThemeConstants.EUREKA_THEME_NAME))
				viewerpage.assertEquals(textOverlay.getBackgroundColor(textOverlay.customOverlayOptionsSlider.get(i)),ThemeConstants.EUREKA_ACTIVE_SLIDER_COLOR,"Checkpoint[13/18]","verifying the slider for Eureka theme");
			else
				viewerpage.assertEquals(textOverlay.getBackgroundColor(textOverlay.customOverlayOptionsSlider.get(i)),ThemeConstants.DARK_ACTIVE_SLIDER_COLOR,"Checkpoint[14/18]","Verifying the slider for Dark theme when slider");
		}
		textOverlay.click(textOverlay.customOverlayOptionsSlider.get(0));

		for(int i=0 ; i<10;i++) 			
			viewerpage.assertTrue(pageTheme.verifyThemeOnTableHeader(textOverlay.customOverlayOptionsSlider.get(i),NSGenericConstants.BACKGROUND_COLOR,loadedTheme),"Checkpoint[15/18]","verifying the background for enabled slider");
		
		textOverlay.click(textOverlay.customOverlayOptionsSlider.get(0));
		
		viewerpage.click(textOverlay.closeCustomMenuOptionArrow);
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.openCustomMenuOptionArrow), "Checkpoint[16/18]", "Verifying the arrow icon is changed to open after closing the custom menu");
		viewerpage.assertFalse(viewerpage.isElementPresent(textOverlay.closeCustomMenuOptionArrow), "Checkpoint[17/18]", "Verifying the close arrow icon is not displayed");
		viewerpage.assertFalse(viewerpage.isElementPresent(textOverlay.customMenu), "Checkpoint[18/18]", "Verifying the menu is also not displayed");
			
	}

	@Test(groups ={"Chrome", "IE11", "Edge","US2252","Positive","E2E","F1083"})
	public void test02_US2252_TC9452_verifyTextOverlayOptions() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that all available items appear in the menu overlay  list, except \"Lossy\", \"Results Applied\" and \" Orientation Markers\" .");

        preset=new ViewBoxToolPanel(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9Patient,username,password, 1);
		textOverlay = new ViewerTextOverlays(driver);
		db = new DatabaseMethods(driver);
		
		HashMap<String,Integer> valuesFromDB = db.getAnnotationNames();		
		List<String> annotationFromUI = textOverlay.getAllCustomAnnotations();		
		viewerpage.assertEquals(annotationFromUI.size(),valuesFromDB.size()+1, "Checkpoint[1/9]", "Verifying the total number of annotations are same on UI");
		
		List<String> annotationWithoutvalues = textOverlay.getAllCustomAnnotationsWithoutValues(annotationFromUI);
		
		for(String value : valuesFromDB.keySet()) {
			viewerpage.assertTrue(annotationWithoutvalues.contains(value), "Checkpoint[2/9]", "Verifying column overlays list is matching with DB");
			
		}
		
		viewerpage.assertFalse(annotationWithoutvalues.contains(ViewerPageConstants.LOSSY_TEXT), "Checkpoint[3/9]", "Verifying Lossy tag is not displayed in list");
		viewerpage.assertFalse(annotationWithoutvalues.contains(ViewerPageConstants.RESULT_APPLIED_TEXT), "Checkpoint[4/9]", "Verifying column result applied is not in list");
			
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.PATIENT_NAME+" : "+viewerpage.getPatientNameOverlayText(1)), "Checkpoint[5/9]", "Verifying column Patient Name with value is displayed in custom annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.PATIENT_ID+" : "+viewerpage.getPatientIDOverlayText(1).replace(ViewerPageConstants.PATIENTEXTERNALID_VALUE+":", "").trim()), "Checkpoint[6/9]", "Verifying column Patient ID is displayed in custom annotation list with value");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.WINDOW_CENTER.replace(":", "")+" : "+viewerpage.getValueOfWindowCenter(1)), "Checkpoint[7/9]", "Verifying column window center with value in annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.WINDOW_WIDTH.replace(":", "")+" : "+viewerpage.getValueOfWindowWidth(1)), "Checkpoint[8/9]", "Verifying column window width with value in annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.ZOOM_OVERLAY.replace(":", "")+" : "+preset.getZoomValue(1)+"%"), "Checkpoint[9/9]", "Verifying column zoom with value is displayed in custom list");
	}
		
	@Test(groups ={"US2252","Positive","F1083"})
	public void test03_US2252_TC9453_TC9455_verifyDBOnSelectionAndDeSelection() throws InterruptedException, SQLException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the changes in menu overlay  will reflect in DB."
				+ "<br> Verify Select all/  Disable all option.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9Patient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		db = new DatabaseMethods(driver);
		HashMap<String,Integer> valuesFromDB = db.getAnnotationNames();		
		List<String> annotationFromUI = textOverlay.getAllCustomAnnotations();		
		
		
		List<Integer> annotationID= new ArrayList<Integer>();
		
		int i=0;
		for(String value : valuesFromDB.keySet()) {
			textOverlay.enableSliderForCustomOverlays(value);
			annotationID.add(valuesFromDB.get(value));
			i++;
			if(i==3)
				break;
			
		}
		
		int accountID = db.getUserIDFromUserAccount(username);		
		List<Integer> annotationsEnabled = db.getAnnotationsEnabled(accountID);		
		textOverlay.assertEquals(annotationID, annotationsEnabled,"Checkpoint[1/9]", "Verifying that entry is made in DB for enabled annotation");
		
		i=0;
		for(String value : valuesFromDB.keySet()) {
			textOverlay.disableSliderForCustomOverlays(value);
			i++;
			if(i==3)
				break;
			
		}
		textOverlay.waitForTimePeriod(3000);
		textOverlay.assertTrue(db.getAnnotationsEnabled(accountID).isEmpty(),"Checkpoint[2/9]", "Verifying entry is removed when annotation is disabled");
		
			
		for(int j =1;j<annotationFromUI.size();j++)
			textOverlay.enableSliderForCustomOverlays(annotationFromUI.get(j));

		textOverlay.waitForTimePeriod(3000);
		textOverlay.assertEquals(db.getAnnotationsEnabled(accountID).size(), valuesFromDB.size(),"Checkpoint[3/9]", "Verifying the all the annotation entries are added when all the sliders are enabled");
		
		textOverlay.disableSliderForCustomOverlays(annotationFromUI.get(0));
		textOverlay.waitForTimePeriod(3000);
		textOverlay.assertTrue(db.getAnnotationsEnabled(accountID).isEmpty(),"Checkpoint[4/9]","verifying all the entries are removed when all slider is disabled");
		
		textOverlay.enableSliderForCustomOverlays(annotationFromUI.get(0));
		textOverlay.waitForTimePeriod(3000);
		
		textOverlay.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
		
		RegisterUserPage register = new RegisterUserPage(driver);
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest,"Checkpoint[5/9]", "Logging using user A");
		Header header = new Header(driver);		
		header.logout();
		
		loginPage = new LoginPage(driver);
		loginPage.login(username_1, username_1);
		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(liver9Patient, "", "", "");
		patientPage.clickOnStudy(1);
		textOverlay.waitForViewerpageToLoad();
//		helper.loadViewerDirectly(liver9Patient,username_1, username_1,1);		
		textOverlay.enableSliderForCustomOverlays(annotationFromUI.get(0));
		
		//wait is required to get the entry to update in db
		textOverlay.waitForTimePeriod(3000);
		textOverlay.assertEquals(db.getAnnotationsEnabled(db.getUserIDFromUserAccount(username)).size(), valuesFromDB.size(),"Checkpoint[6/9]", "Verifying that annotation overlays are user specific - user A");
		textOverlay.assertEquals(db.getAnnotationsEnabled(db.getUserIDFromUserAccount(username_1)).size(), valuesFromDB.size(),"Checkpoint[7/9]", "verifying that annoation overlays are displayed for Scan user");
		
		textOverlay.disableSliderForCustomOverlays(annotationFromUI.get(0));
		//wait is required to get the entry to update in db
		textOverlay.waitForTimePeriod(3000);
		textOverlay.assertTrue(db.getAnnotationsEnabled(db.getUserIDFromUserAccount(username_1)).isEmpty(),"Checkpoint[8/9]","Verifying the entries are removed for user A when slider is off");
		textOverlay.assertEquals(db.getAnnotationsEnabled(db.getUserIDFromUserAccount(username)).size(), valuesFromDB.size(),"Checkpoint[9/9]", "verifying that entries are still persisted for scan user");
		
	
		
	}
		
	@Test(groups ={"US2252","Positive","F1083"})
	public void test04_US2252_TC9456_verifyTooltipOnLongText() throws InterruptedException, SQLException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that that tooltip is displayed for items with long text in the text overlay menu.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9Patient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		textOverlay.assertTrue(textOverlay.verifyTooltipCustomAnnotations(),"Checkpoint[1/1]","Verifying the tooltip is displayed");		
				
	}
		
	@Test(groups ={"US2252","Positive","F1083"})
	public void test05_US2252_TC9641_TC9640_verifyOtherTagsAndLossyMode() throws InterruptedException, SQLException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that \"Orientation markers\" and \"Results applied\" tags  are getting always displayed on the viewer."
				+ "<br> Verify that \"Lossy mode\" is getting always displayed on viewer  when lossy rendering is used to render the image.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);

		helper=new HelperClass(driver);
		helper.loadViewerDirectly(liver9Patient, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		ViewerOrientation orin = new ViewerOrientation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		
		distance.selectDistanceFromQuickToolbar(1);
		for(int i=1;i<=layout.getNumberOfCanvasForLayout();i++) {
			layout.assertTrue(layout.isElementPresent(layout.getLossyAnnotation(i)),"Checkpoint[1/12]", "Verifying the lossy tag is displayed");
			orin.assertTrue(orin.verifyTopOrientationMarkerPresence(i),"Checkpoint[2/12]", "Verifying the top orientation is present");
			orin.assertTrue(orin.verifyBottomOrientationMarkerPresence(i),"Checkpoint[3/12]", "verifying the bottom orientation is present");
			orin.assertTrue(orin.verifyLeftOrientationMarkerPresence(i),"Checkpoint[4/12]", "Verifying the left orientation is present");
			orin.assertTrue(orin.verifyRightOrientationMarkerPresence(i),"Checkpoint[5/12]", "verifying the right orientation is present");
			
			distance.drawLine(i, -100, -50, 100, 100);
			distance.assertTrue(distance.verifyResultAppliedTextPresence(i),"Checkpoint[6/12]", "Verifying the result applied overlay is displayed after creating the annotation");
			distance.closingConflictMsg();
			
			
			
		}
		
		db = new DatabaseMethods(driver);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, 1, username);
		
		Header header = new Header(driver);		
		header.logout();
		
		loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(liver9Patient, "", "", "");
		patientPage.clickOnStudy(1);
		layout.waitForAllImagesToLoad();
		
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		distance.closingConflictMsg();
		layout.selectZoomFromQuickToolbar(1);
		layout.dragAndReleaseOnViewer(layout.getViewbox(1), 0, 0, 100, -100);
		
		for(int i=1;i<=layout.getNumberOfCanvasForLayout();i++) {
			layout.assertTrue(layout.isElementPresent(layout.getLossyAnnotation(i)),"Checkpoint[7/12]", "Verifying the lossy overlay is always displayed when mode is configured lossy though zoom >100");
			orin.assertTrue(orin.verifyTopOrientationMarkerPresence(i),"Checkpoint[8/12]", "verifying the top orientation");
			orin.assertTrue(orin.verifyBottomOrientationMarkerPresence(i),"Checkpoint[9/12]", "verifying the bottom orientation");
			orin.assertTrue(orin.verifyLeftOrientationMarkerPresence(i),"Checkpoint[10/12]", "verifying the left orientation");
			orin.assertTrue(orin.verifyRightOrientationMarkerPresence(i),"Checkpoint[11/12]", "verifying the right orientation");
			distance.assertTrue(distance.verifyResultAppliedTextPresence(i),"Checkpoint[12/12]", "verifying the Result applied orientation");
				
		}	
		
	}
		
	//US2253: Display custom overlays for a user
	@Test(groups ={"Chrome", "IE11", "Edge","US2252","US2381","Positive","F1081","E2E","F1083"})
	public void test06_US2253_TC10243_US2381_TC10105_verifyCustomTextOverlayOptionSelection() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify custom annotaion selections are saved per user and persisted on relogin. <br>"+
		"Verify that annotation list  stores  values when toggled off.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Loading patient "+liver9Patient+" on viewer.");
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
        preset=new ViewBoxToolPanel(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9Patient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		
		//enable slider from custom text overlay menu
		 for(int i=0;i<ViewerPageConstants.CUSTOM_OVERLAY.size();i++)
		textOverlay.enableSliderForCustomOverlays(ViewerPageConstants.CUSTOM_OVERLAY.get(i));
		
		String sliceValue=viewerpage.getText(viewerpage.getSliceInfo(1)).replace(ViewerPageConstants.OUTPUTPANEL_SLICE_LABEL+":", "").trim();
		
		List<String> annotationFromUI = textOverlay.getAllCustomAnnotations();		
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(0) +viewerpage.getPatientNameOverlayText(1)), "Checkpoint[1/10]", "Verifying column Patient Name with value is displayed in custom annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(1)+viewerpage.getPatientIDOverlayText(1).replace(ViewerPageConstants.PATIENTEXTERNALID_VALUE+":", "").trim()), "Checkpoint[2/10]", "Verifying column Patient ID is displayed in custom annotation list with value");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(2)+viewerpage.getPatientSexOverlayText(1)), "Checkpoint[3/10]", "Verifying column Patient Sex with value in annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(3)+sliceValue), "Checkpoint[4/10]", "Verifying column Slice with value in annotation list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(4)+preset.getZoomValue(1)+"%"), "Checkpoint[5/10]", "Verifying column zoom with value is displayed in custom list");
		viewerpage.assertTrue(annotationFromUI.contains(ViewerPageConstants.CUSTOM_OVERLAY.get(5)+viewerpage.getStudyDateTimeOverlayText(1)), "Checkpoint[6/10]", "Verifying column Study date time with value is displayed in custom list");
	
		textOverlay.closeCustomOverlay();
		//TC10105:Verify that annotation list  stores  values when toggled off.
		 for(int i=0;i<ViewerPageConstants.CUSTOM_OVERLAY.size();i++)
				viewerpage.assertTrue(textOverlay.verifyCustomOverlayIsSelected(ViewerPageConstants.CUSTOM_OVERLAY.get(i)), "Checkpoint[7/10]", "Verified that custom text overlay is selected for "+ViewerPageConstants.CUSTOM_OVERLAY.get(i));
		
		 loginPage.logout();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Loading patient "+pointMultiSeries+" on viewer for the same user.");
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username, password);
		patientPage.waitForPatientPageToLoad();
		 helper.loadViewerDirectly(pointMultiSeries, 1);
		 for(int i=0;i<ViewerPageConstants.CUSTOM_OVERLAY.size();i++)
		viewerpage.assertTrue(textOverlay.verifyCustomOverlayIsSelected(ViewerPageConstants.CUSTOM_OVERLAY.get(i)), "Checkpoint[8/10]", "Verified that custom text overlay is selected for "+ViewerPageConstants.CUSTOM_OVERLAY.get(i));
	    textOverlay.navigateToURL(URLConstants.BASE_URL+URLConstants.REGISTER_PAGE_URL);
			
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Create new user and load patient "+liver9Patient+" on viewer.");
		RegisterUserPage register = new RegisterUserPage(driver);
		register.createNewUser("UserA", "test", LoginPageConstants.SUPPORT_EMAIL,username_1, username_1, username_1);
		loginPage.logout();
		loginPage.waitForLoginPageToLoad();
		loginPage.login(username_1, username_1);
		patientPage = new PatientListPage(driver);
		helper.loadViewerDirectly(liver9Patient, 1);
		viewerpage.assertTrue(textOverlay.isSelectedOverlayDisplayed(ViewerPageConstants.DEFAULT_ANNOTATION,1), "Checkpoint[9/10]", "Verified that default text overlay is selected for new created user.");
		
		 for(int i=0;i<ViewerPageConstants.CUSTOM_OVERLAY.size();i++)
				viewerpage.assertFalse(textOverlay.verifyCustomOverlayIsSelected(ViewerPageConstants.CUSTOM_OVERLAY.get(i)), "Checkpoint[10/10]", "Verified that custom text overlay is not selected for "+ViewerPageConstants.CUSTOM_OVERLAY.get(i));
			
	}
	
	@Test(groups ={"Chrome", "IE11", "Edge","US2252","Positive","F1083"})
	public void test07_US2253_TC10338_verifySeriesDescrForNonDicom() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Series Description can be toggled for non-dicom series like SR, PDF and images like PNG/JPEG/BMP..");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
        preset=new ViewBoxToolPanel(driver);
		helper=new HelperClass(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Loading patient "+ChestCT1p25mmPatient+" on viewer and verify Date of birth When Full annotation text overlay selected.");
		
		viewerpage = helper.loadViewerDirectly(ChestCT1p25mmPatient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		
		//enable "All" from Custom text overlay.
		textOverlay.enableSliderForCustomOverlays("All");
		List<String> annotationFromUI = textOverlay.getAllCustomAnnotations();		

		 for(int i=0;i<annotationFromUI.size();i++)
				viewerpage.assertTrue(textOverlay.verifyCustomOverlayIsSelected(annotationFromUI.get(i)), "Checkpoint[1/6]", "Verified that custom overlay is selected after click on All for "+annotationFromUI.get(i));	
		 
		 textOverlay.disableSliderForCustomOverlays(ViewerPageConstants.CUSTOM_OVERLAY.get(6));
		 viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(1)),"Checkpoint[2/6]", "Verified that series textoverlay not visible when it toggle off from Custom overlay menu.");	
		 viewerpage.assertFalse(viewerpage.isElementPresent(viewerpage.getSeriesDescriptionOverlay(2)),"Checkpoint[3/6]", "Verified that series textoverlay not visible when it toggle off from Custom overlay menu");	
		 
		 //toggle off All filter for custom text overlay
		 textOverlay.disableSliderForCustomOverlays(ViewerPageConstants.ALL_CUSTOM_OVERLAY);
		 textOverlay.disableSliderForCustomOverlays(ViewerPageConstants.ALL_CUSTOM_OVERLAY);
		 
		 //for non-dicom
		 for(int i=1;i<=viewerpage.getNumberOfCanvasForLayout();i++)
		 viewerpage.assertTrue(viewerpage.isElementPresent(viewerpage.getViewboxNumber(i)),"Checkpoint[4/6]", "Verified that viewbox number is present on viewbox-"+i);	
			
		 //for DICOM viewbox
		 viewerpage.assertTrue(viewerpage.verifyResultAppliedTextPresence(2),"Checkpoint[5/6]", "Verified that result applied text presence on viewbox-2.");	
		 ViewerOrientation ori=new ViewerOrientation(driver);
		 ori.assertTrue(ori.verifyOrientationForPlane(2, ViewerPageConstants.AXIAL_KEY),"Checkpoint[6/6]", "Verified orientation marker for Axial plane. on viewbox-2.");	
		 
	}
	
	@Test(groups ={"Chrome", "IE11", "Edge","US2252","Positive","F1083"})
	public void test08_US2253_TC10339_verifyCustomOverlayForDateOfBirth() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DR2620[V1.0.4] - Verify previous patients DOB is not picked up when DOB is not available for current selection of patient.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
        preset=new ViewBoxToolPanel(driver);
		helper=new HelperClass(driver);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Loading patient "+ChestCT1p25mmPatient+" on viewer and verify Date of birth When Full annotation text overlay selected.");
		patientPage.searchPatient(ChestCT1p25mmPatient, "", "", "");
		textOverlay = new ViewerTextOverlays(driver);
		
		String dateOfBirth=patientPage.getText(patientPage.dateOfBirthHeaderValueInPatientInfo);
		patientPage.clickOntheFirstStudy();
		viewerpage=new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad(2);
		
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		textOverlay.assertTrue(textOverlay.getText(textOverlay.getPatientBirthDateOverlay(2)).contains(dateOfBirth), "Checkpoint[1/2]", "Verified that patient date of birth textoverlay is visible for "+ChestCT1p25mmPatient);
		textOverlay.click(textOverlay.patientsIcon);
		patientPage.waitForPatientPageToLoad();
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Loading patient "+liver9Patient+" on viewer and verify Date of birth not visible as DOB is not avaialble.");
		helper.loadViewerPageUsingSearch(liver9Patient, 1, 1);
		textOverlay.assertFalse(textOverlay.isElementPresent(textOverlay.getPatientBirthDateOverlay(1)), "Checkpoint[2/2]", "Verified that patient date of birth textoverlay is not visible for "+liver9Patient);
	
	}
	
	//US2381: Update text overlay menu to match Eureka styling
	@Test(groups ={"Chrome", "IE11", "Edge","US2381","Positive","F1081"})
	public void test02_US2381_TC10075_verifyTextOverlayOnMouseMov() throws InterruptedException{	

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Text overlay Menu and Layout menu are not getting Hidden after 0.5 seconds  mouse leave.");

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

		patientPage = new PatientListPage(driver);
        preset=new ViewBoxToolPanel(driver);
		helper=new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9Patient, 1);
		textOverlay = new ViewerTextOverlays(driver);
		db = new DatabaseMethods(driver);
		ViewerLayout layout=new ViewerLayout(driver);
	
		textOverlay.selectTextOverlays(ViewerPageConstants.CUSTOM_ANNOTATION);
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.closeCustomMenuOptionArrow), "Checkpoint[1/4]", "Verifying the arrow icon is changed to close after opening the custom menu");
		viewerpage.mouseHover(layout.gridIcon);
		viewerpage.waitForTimePeriod(500);
		viewerpage.assertTrue(viewerpage.isElementPresent(textOverlay.closeCustomMenuOptionArrow), "Checkpoint[2/4]", "Verifying that annotation list menu is not hidden after 0.5 sec.");
		
		layout.openLayoutContainer();
		viewerpage.assertFalse(viewerpage.isElementPresent(textOverlay.closeCustomMenuOptionArrow), "Checkpoint[3/4]", "Verifying annotation list is closed after opening Layout selector.");
		viewerpage.assertTrue(layout.isElementPresent(layout.gridLayoutBox), "Checkpoint[4/4]", "Verifying the Layout selector container is visible after click on Layout icon.");
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void cleanDB() throws SQLException {

		db = new DatabaseMethods(driver);
		db.truncateTable(Configurations.TEST_PROPERTIES.get("dbName"), NSDBDatabaseConstants.USERANNOTATION_TABLE);
		db.updateRenderingModeForUser(NSDBDatabaseConstants.DESKTOP_RENDERING_MODE, NSDBDatabaseConstants.NONADMINUSER, username);
	}


}
