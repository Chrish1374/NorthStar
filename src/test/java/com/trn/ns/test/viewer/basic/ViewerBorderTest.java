package com.trn.ns.test.viewer.basic;

import java.awt.AWTException;
import java.sql.SQLException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerBorderTest extends TestBase{


	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private ViewerLayout layout;

	String anonymous = Configurations.TEST_PROPERTIES.get("Anonymous_filepath");
	String anonymous_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, anonymous);

	String icometrix_filepath = Configurations.TEST_PROPERTIES.get("Icometrix");
	String Icometrix_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, icometrix_filepath);
	String icometrixStudyDetails = DataReader.getStudyDetails(PatientXMLConstants.NUMBER_OF_IMAGES,PatientXMLConstants.STUDY01_TEXTOVERLAY,  icometrix_filepath);
	
	String liver9 = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9);

	private ContentSelector cs;
	private MeasurementWithUnit lineWithUnit;
	private CircleAnnotation circle;
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;
	
	String loadedTheme =ThemeConstants.EUREKA_THEME_NAME;

	//US1258: Show borders around viewboxes with same batch as active viewbox

	@Test(groups={"firefox","Chrome","Edge","US1258","US2250","Positive","E2E","F1084"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US1258_TC6677_US2250_TC9480_verifyViewboxBorderWhenAllViewboxesBelongsToSameBatch(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("DICOM result and research use only being displayed in a banner and overlay - Content Qualification set to null.<br>"+
		"Verify the color of the active view box border and border displayed around the view boxes that belong to the same batch.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(anonymous_Patient, username,password, 1);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		
		//when mouse hover on first viewbox then corresponding source series should be highlighted in blue color .
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(1,2,theme), "Checkpoint[1/12]", "Verified display of blue border for first viewbox and white border for second viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[2/12]", "Verified that blue border and white border not display for the third viewbox. ");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[3/12]", "Verified that blue border and white border not display for the fourth viewbox. ");

		//when mousehover on second viewbox
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(2,1,theme), "Checkpoint[4/12]", "Verified display of blue border for second viewbox and white border when first viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[5/12]", "Verified that blue border and white border not display for the third viewbox. ");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[6/12]", "Verified that blue border and white border not display for the fourth viewbox. ");

		//when mousehover on 3rd viewbox then series viewbox with blue border visible
		viewerpage.mouseHover(viewerpage.getViewPort(3));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(3,2,theme), "Checkpoint[7/12]", "Verified display of blue border for third viewbox and white border for second viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(1,theme), "Checkpoint[8/12]", "Verified that blue border and white border not display for the first viewbox. ");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[9/12]", "Verified that blue border and white border not display for the fourth viewbox. ");

		//when mousehover on 4th viewbox then series viewbox with blue border visible
		viewerpage.mouseHover(viewerpage.getViewPort(4));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(4,2,theme), "Checkpoint[10/12]", "Verified display of blue border for fourth viewbox and white border for second viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(1,theme), "Checkpoint[11/12]", "Verified that blue border and white border not display for the first viewbox. ");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[12/12]", "Verified that blue border and white border not display for the third viewbox. ");

	}

	@Test(groups={"firefox","Chrome","Edge","US1258","Negative"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US1258_TC6694_verifyViewboxBorderForViewboxesWhenSameDataIsPresentInMultipleViewboxes(String theme) throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify that viewboxes don't have prominent border (blue) around them if same data is present in multiple viewboxes in any layout.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(anonymous_Patient,username,password, 1);

		//get series name and result name from content selector
		cs=new ContentSelector(driver);
		String seriesName=viewerpage.getSeriesDescriptionOverlayText(2);
		String resultToSelect1=cs.getAllResults().get(0);
		String resultToSelect2=cs.getAllResults().get(1);

		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		viewerpage.closeNotification();

		//Load same series on all 4 viewboxes and verify border color
		for(int i=1;i<=viewerpage.getNumberOfCanvasForLayout();i++)
			cs.selectSeriesFromSeriesTab(i, seriesName);

		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1, theme), "Checkpoint[1/11]", "Viewbox-1 border is highlighted blue color as it is a active viewbox");

		for(int i=2;i<=viewerpage.getNumberOfCanvasForLayout();i++)
			viewerpage.assertFalse(viewerpage.verifyBorderForActiveViewbox(i, theme), "Checkpoint[2/11]","Viewbox-"+i+" blue border not visible as viewbox is not active.");
		for(int i=1;i<=viewerpage.getNumberOfCanvasForLayout();i++)
			viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(i,theme), "Checkpoint[3/11]","Viewbox-"+i+"  border not highlighted in white since all viewboxes have the same series and all belong to same batch");


		//2 viewboxes have same series and Other viewboxes belong to a different batch.
		cs.selectResultFromSeriesTab(3, resultToSelect1);
		cs.selectResultFromSeriesTab(4, resultToSelect2);
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1,theme), "Checkpoint[4/11]", "Verified display of blue border for the first viewbox.");
		viewerpage.assertTrue(viewerpage.verifyWhiteBorderForViewbox(2,theme), "Checkpoint[5/11]", "Verified display of white border on viewbox-2 when mousehover on viewbox-1.");

		//verify outline border for empty viewbox.
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		viewerpage.mouseHover(viewerpage.getViewPort(6));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(6,theme), "Checkpoint[6/11]", "Verified that border of active empty viewbox is highlighted in blue.");
		viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(6,theme), "Checkpoint[7/11]", "Empty viewbox border not highlighted in white color ");

		//verified blue and white border when empty viewbox is active viewbox.
		for(int i=1;i<viewerpage.getNumberOfCanvasForLayout();i++)
		{
			viewerpage.assertFalse(viewerpage.verifyBorderForActiveViewbox(i,theme), "Checkpoint[8/11]", "Verified that blue border not visible for viewbox-"+i+" when empty viewbox is active");
			viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(i,theme), "Checkpoint[9/11]", "Verified that white border not visible for viewbox-"+i+" when empty viewbox is active");
		}

		//verify outline border when same series loaded in all viewbox along with one empty viewbox.
		for(int i=1;i<viewerpage.getNumberOfCanvasForLayout();i++)
			cs.selectSeriesFromSeriesTab(i, seriesName);

		for(int i=1;i<viewerpage.getNumberOfCanvasForLayout();i++)
		{   
			viewerpage.mouseHover(viewerpage.getViewPort(i));
			viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(i,theme), "Checkpoint[10/11]","Verified that blue border visible for viewbox-"+i+" when viewbox is active.");
			viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(i,theme), "Checkpoint[11/11]","Verified that white border not visible for viewbox-"+i+" because same series is loaded on all viewbox along with empty viewbox");

		}


	}

	@Test(groups={"firefox","Chrome","Edge","US1258","Negative"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_US1258_TC6692_verifyBorderWhenUserCreatedGSPSCopiesBelongsToDifferentSeries(String theme) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify border around respective viewboxes are highlighted in blue for viewboxes with user created GSPS copies for different series within the same study and multiple clones");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		LoginPage login = new LoginPage(driver);
		login.navigateToBaseURL();
		login.login(username, password);
		PatientListPage patient = new PatientListPage(driver);
		patient.clickOnPatientRow(Icometrix_Patient);
		
		patient.clickOnStudy("336");
		viewerpage = new ViewerPage(driver);
		viewerpage.waitForViewerpageToLoad();

		//get series name and result name from content selector
		cs=new ContentSelector(driver);
		String series1=cs.getAllSeries().get(0);
		String series2=cs.getAllSeries().get(1);
		String result1=cs.getAllResults().get(0);
		String result2=cs.getAllResults().get(1);

		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(4);
		circle.drawCircle(4,  5, 5,-80,-80);

		//Select Linear measurement from Radial Menu
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(5);
		lineWithUnit.drawLine(5, -50, -50, 100, 0);		

		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_1"), "Checkpoint[1/12]", "Verified that new GSPS clone created after drawing annotation on first series");
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_2"), "Checkpoint[2/12]", "Verified that new GSPS clone created after drawing annotation on second series");

		//Hover on one of the viewboxes with user-created GSPS to make it active and verify the viewboxes' border.
		viewerpage.closeWaterMarkIcon(1);
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_1");
		viewerpage.closeWaterMarkIcon(2);
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_2");
		viewerpage.closeWaterMarkIcon(3);
		cs.selectSeriesFromSeriesTab(3, series1);
		viewerpage.closeWaterMarkIcon(4);
		cs.selectSeriesFromSeriesTab(4, series2);
		viewerpage.closeWaterMarkIcon(5);
		cs.selectResultFromSeriesTab(5, result1);
		viewerpage.closeWaterMarkIcon(6);
		cs.selectResultFromSeriesTab(6, result2);

		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(1,3,theme), "Checkpoint[3/12]", "Viewbox blue  border for first viewbox and white border for the third viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[4/12]", "Verified that border is not visible as viewbox-4 is not active");

		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(2,4,theme), "Checkpoint[5/12]", "Viewbox blue  border for second viewbox and white border for the fourth viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[6/12]", "Verified that border is not visible as viewbox-3 is not active");

		//reload viewerpage and draw annotation1
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(Icometrix_Patient,username,password,1);
	
		viewerpage.click(viewerpage.getViewPort(1));
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		cs.selectSeriesFromSeriesTab(1, series1);
		viewerpage.closingConflictMsg();
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_1");
		viewerpage.closingConflictMsg();
		cs.selectResultFromSeriesTab(3, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_2");
		viewerpage.closingConflictMsg();
		cs.selectResultFromSeriesTab(4, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_3");

		//Hover on viewbox with source series and verify the viewbox border
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(1,2,theme), "Checkpoint[7/12]", "Viewbox blue border for first viewbox and white border for the second viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[8/12]", "Verified that border is not visible as viewbox-3 is not active");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[9/12]", "Verified that border is not visible as viewbox-4 is not active");

		//Hover on the viewbox containing the user-created GSPS in step 4 and verify the viewboxes border.
		viewerpage.mouseHover(viewerpage.getViewPort(4));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(4,3,theme), "Checkpoint[10/12]", "Viewbox blue border for fourth viewbox and white border for the third viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(1,theme), "Checkpoint[11/12]", "Verified that white border is not visible as viewbox-1 is not active");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(2,theme), "Checkpoint[12/12]", "Verified that white border is not visible as viewbox-2 is not active");


	}

	@Test(groups={"firefox","Chrome","Edge","US1258","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US1258_TC6714_verifyBorderWhenUserCreatedGSPSCopiesBelongsToSameSeries(String theme) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify border around respective viewboxes are highlighted in blue for viewboxes with user created GSPS copies for different series within the same study and multiple clones");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(anonymous_Patient, username,password, 1);

		//get series name and result name from content selector
		cs=new ContentSelector(driver);
		String series1=viewerpage.getSeriesDescriptionOverlayText(2);
		String result1=viewerpage.getSeriesDescriptionOverlayText(1);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		//Select Linear measurement from Radial Menu
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(2);
		lineWithUnit.drawLine(2, -50, -50, 100, 0);
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_1"), "Checkpoint[1/5]", "Verified new GSPS clone after drawing annotation on source series.");

		//reload viewerpage and draw annotation on same source series
		helper.loadViewerDirectly(anonymous_Patient, username,password, 1);
		viewerpage.waitForViewerpageToLoad(1);
		cs.selectSeriesFromSeriesTab(1, series1);

		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,  5, 5,-80,-80);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_2"), "Checkpoint[2/5]", "Verified second GSPS clone after drawing annotation on same source series after reload of viewer page.");

		//Hover on one of the viewboxes with user-created GSPS to make it active and verify the viewboxes' border.
		cs.selectResultFromSeriesTab(1, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_1");
		viewerpage.closingConflictMsg();
		cs.selectResultFromSeriesTab(2, ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+ username+"_2");
		viewerpage.closingConflictMsg();
		cs.selectSeriesFromSeriesTab(3, series1);
		viewerpage.closingConflictMsg();
		cs.selectResultFromSeriesTab(4, result1);

		//Hover on 1st viewbox containing user-created GSPS from step 1 and verify the viewboxes' border.
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(1, 3,theme), "Checkpoint[3/5]", "Viewbox blue border for first viewbox and white border for the third viewbox.");

		//Hover on 2nd viewbox containing the user-created GSPS in step 2 and verify the viewboxes border.
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(2,3,theme), "Checkpoint[4/5]", "Viewbox blue border for second and white border for the third viewbox.");

		//Hover on 3rd viewbox containing source series and verify the viewboxes border.
		viewerpage.mouseHover(viewerpage.getViewPort(3));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(3,2, theme), "Checkpoint[5/5]", "Viewbox blue border for third viewbox and white border for the second viewbox.");
	
	}

	@Test(groups={"firefox","Chrome","Edge","US1258","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test05_US1258_TC6724_verifyBorderWhenActiveViewboxIsSourceSeries(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify viewboxes border is highlighted in blue for all viewboxes belonging to same batch as the active viewbox and rest viewboxes' border remains unchanged (when active viewbox has source series)");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(anonymous_Patient, username,password, 1);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);

		//Hover on viewboxes containing the source series and verify the viewboxes border.
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(2,theme), "Checkpoint[1/4]", "Viewbox blue border for the second viewbox.");
		viewerpage.assertTrue(viewerpage.verifyWhiteBorderForViewbox(1,theme), "Checkpoint[2/4]", "Verified white border color for the first viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(3,theme), "Checkpoint[3/4]", "Viewbox blue and white border not visible for the third viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(4,theme), "Checkpoint[4/4]", "Viewbox blue and white border not visible for the fourth viewbox.");


	}

	@Test(groups={"firefox","Chrome","Edge","US1258","Positive"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test06_US1258_TC6722_verifyBorderWhenInOneUpPerform(String theme) throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Desktop]Verify viewboxes border is not highlighted in blue in 1-up, 1X2 and if there is only one viewbox belonging to a different batch in multi-viewbox layout.");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(anonymous_Patient, username,password, 2);

		//get series name and result name from content selector
		cs=new ContentSelector(driver);
		String series1=viewerpage.getSeriesDescriptionOverlayText(2);
		String result1=cs.getAllResults().get(1);

		// Viewboxes should not have prominent border (blue) around them when all viewboxes belong to the same batch irrespective of whether the batch is old or most recent.
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1,theme), "Checkpoint[1/17]", "Verified that blue border visible as is viewbox-1  active");
		viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(1,theme), "Checkpoint[2/17]", "Verified white border color not visible for the first viewbox.");
		viewerpage.assertFalse(viewerpage.verifyBorderForActiveViewbox(2,theme), "Checkpoint[3/17]", "Verified blue border color not visible for the second viewbox.");

		//Viewboxes should not have prominent border (blue) around them when both viewboxes belong to a different batch irrespective of whether the batch is old or most recent.
		cs.selectResultFromSeriesTab(2, result1);
		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(2,theme), "Checkpoint[4/17]", "Verified that blue border visible as is viewbox-2  active");
		viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(2,theme), "Checkpoint[5/17]", "Verified that white border is not visible as viewbox-2 is active");
		viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(1,theme), "Checkpoint[6/17]", "Verified that white border is not visible as viewbox-1 is not active");
		viewerpage.assertFalse(viewerpage.verifyBorderForActiveViewbox(1,theme), "Checkpoint[7/17]", "Verified blue border color not visible for the first viewbox.");

		//Active viewbox should not be highlighted in blue but it should be highlighted in white since it is active.
		//perform one up and verify blue border
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.waitForViewerpageToLoad(1);
		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Checkpoint[8/17]", "Verified that layout is 1*1 ");
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1,theme), "Checkpoint[9/17]", "Verified that blue border visible as is viewbox  active");
		viewerpage.assertFalse(viewerpage.verifyWhiteBorderForViewbox(1,theme), "Checkpoint[10/17]", "Verified white border color not visible for the viewbox.");

		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.assertNotEquals(viewerpage.getNumberOfCanvasForLayout(), 1, "Checkpoint[11/17]", "Verified that layout is 1*2 ");
		viewerpage.closingConflictMsg();
		//draw annotation on source series after one-up perform
		cs.selectSeriesFromSeriesTab(1, series1);
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1,  5, 5,-80,-80);
		layout=new ViewerLayout(driver);
		//reload viewer page
		helper.loadViewerDirectly(anonymous_Patient, username, password, 1);
	
		//draw second annotation on previous clone copy
		lineWithUnit = new MeasurementWithUnit(driver);
		viewerpage.click(viewerpage.getViewPort(1));
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		//change layout
		layout.selectLayout(layout.twoByOneLAndOneByOneRLayoutIcon);
		viewerpage.closingConflictMsg();
		cs.selectSeriesFromSeriesTab(3, series1);

		//mouse hover on third viewbox containing source series
		viewerpage.mouseHover(viewerpage.getViewPort(3));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(3, 1,theme), "Checkpoint[12/17]", "Verified display of blue border for third viewbox and white border for first viewbox.");
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(3,theme), "Checkpoint[13/17]", "Verified display of blue border on viewbox-3 when mousehover on viewbox-3");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(2,theme), "Checkpoint[14/17]", "Verified blue and white border not visible on viewbox-2");

		viewerpage.mouseHover(viewerpage.getViewPort(2));
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxActiveAndDifferentBatchLoaded(2, 3,theme), "Checkpoint[15/17]", "Verified display of blue border for second viewbox and white border for third viewbox.");
		viewerpage.assertTrue(viewerpage.verifyWhiteBorderForViewbox(3,theme), "Checkpoint[16/17]", "Verified display of white border on viewbox-3 when mousehover on viewbox-2");
		viewerpage.assertTrue(viewerpage.verifyBorderWhenViewboxIsInActive(1,theme), "Checkpoint[17/17]", "Verified blue and white border not visible on viewbox-1");

	}

	@Test(groups={"firefox","Chrome","Edge","US2250","Positive","E2E","F1084"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test07_US2250_TC9469_TC9477_verifyViewboxBorderAndRoundedCorners(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the spacing and color around the viewer and in between the view boxes.<br>"+
		"Verify the view boxes have rounded corners and color of the active view box border.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(liver9_Patient,username, password,1);
		viewerpage.assertTrue(viewerpage.verifyViewboxSpaceAndBackgroundColor(theme),"Checkpoint[1/6]","Verified viewbox border and background color for 2*2 layout.");
		
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1,theme),"Checkpoint[2/6]","Verified blue border and border radius for the active viewbox for 2*2 layout");
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertTrue(viewerpage.verifyViewboxSpaceAndBackgroundColor(theme),"Checkpoint[3/6]","Verified viewbox border and background color for 1*1 layout.");
		viewerpage.mouseHover(viewerpage.getViewPort(1));
		viewerpage.assertTrue(viewerpage.verifyBorderForActiveViewbox(1,theme),"Checkpoint[4/6]","Verified blue border and border radius for the active viewbox for 1*1 layout.");
		
		layout.selectLayout(layout.oneByOneLAndTwoByOneRLayoutIcon);
		viewerpage.assertTrue(viewerpage.verifyViewboxSpaceAndBackgroundColor(theme),"Checkpoint[5/6]","Verified viewbox border and background color for 1L*2R layout.");
		
		viewerpage.resizeBrowserWindow(500, 600);
		viewerpage.assertTrue(viewerpage.verifyViewboxSpaceAndBackgroundColor(theme),"Checkpoint[6/6]","Verified viewbox border and background color for 1L*2R layout.");
	}
	
	
	
	@AfterMethod
	public void revertDefaultTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
		
		
	}
}

