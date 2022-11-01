package com.trn.ns.test.viewer.contentSelector;

import java.awt.AWTException;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ContentSelectorTest02 extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private HelperClass helper;
	private DICOMRT rt;
	private ViewerLayout layout;
	private CircleAnnotation circle;
	private PagesTheme pageTheme;
	private String loadedTheme;
	private ViewerARToolbox arToolbar;

	private String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	private String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);
	
	private String tcgafilePath = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	private String tcgaPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcgafilePath);
	
	String imbioFilepath = Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilepath);
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);	
	}
	
	@Test(groups = { "Chrome","IE","Edge","US2380","Positive","F1086","E2E"})
	public void test01_US2380_TC9949_verifyEyeIconForLoadedUnloadedSeriesTab() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify eye icon gets updated when series are loaded/unloaded from Series tab");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(liver9PatientName, 1);
	    
	    cs=new ContentSelector(driver);
	    List<String> seriesName=cs.getAllSeries();
	    String newSeriesToLoad=seriesName.get(seriesName.size()-1);
	    
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify eye icon when series loaded and unloaded from series tab." );
	    //verify eye icon for loaded series
	    for(int i=0;i<seriesName.size()-1;i++)
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName.get(i)), "Checkpoint[1/5]", "Verified that eye icon is visible for loaded series "+seriesName.get(i));
	    
	    //load unaloaded series and verify  eye Icon
	    cs.assertFalse(cs.verifyPresenceOfEyeIcon(newSeriesToLoad), "Checkpoint[2/5]", "Verified that eye icon is not visible for unloaded series "+newSeriesToLoad);
	    cs.openAndCloseSeriesTab(true);
	    cs.dragAndDropSeriesOrResult(1, cs.getSeriesElement(newSeriesToLoad));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(newSeriesToLoad), "Checkpoint[3/5]", "Verified that eye icon is visible after loading of series "+newSeriesToLoad);

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify eye icon when series unloaded series loaded in empty viewbox.");
	    //after layout change load series in empty viewbox and verify eye icon
	    String series1=seriesName.get(0);
	    layout=new ViewerLayout(driver);
	    layout.selectLayout(layout.threeByThreeLayoutIcon);
	    cs.openAndCloseSeriesTab(true);
	    cs.dragAndDropSeriesOrResult(5, cs.getSeriesElement(newSeriesToLoad));

	    cs.assertFalse(cs.verifyPresenceOfEyeIcon(series1), "Checkpoint[4/5]", "Verified that eye icon is not visible for unloaded series "+series1);
	    cs.openAndCloseSeriesTab(true);
	    cs.dragAndDropSeriesOrResult(cs.getNumberOfCanvasForLayout(), cs.getSeriesElement(series1));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(series1), "Checkpoint[5/5]", "Verified that eye icon is  visible for loaded series in empty viebox "+series1);
	    
	}
	
	@Test(groups = { "Chrome","IE","Edge","US2380","Positive","F1086"})
	public void test02_US2380_TC9952_VerifyEyeIconAfterChangingTheLayout() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify eye icon is displayed for the series which are loaded in the viewer after changing layout");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(liver9PatientName, 1);
	    layout=new ViewerLayout(driver);
	    layout.selectLayout(layout.twoByOneLayoutIcon);
	    cs=new ContentSelector(driver);
	    List<String> seriesName=cs.getAllSeries();
	   
	    //verify eye icon for loaded series
	    for(int i=0;i<=1;i++)
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName.get(i)), "Checkpoint[1/4]", "Verified that eye icon is display for "+seriesName.get(i));
	    
	    //verify eye icon for unloaded series
	    for(int i=2;i<seriesName.size();i++)
	    cs.assertFalse(cs.verifyPresenceOfEyeIcon(seriesName.get(i)), "Checkpoint[2/4]", "Verified that eye icon is not display for unloaded "+seriesName.get(i));
	    
	    cs.doubleClick(cs.getViewPort(1));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName.get(0)), "Checkpoint[3/4]", "Verified that eye icon is display for "+seriesName.get(0)+" when one up perform.");
        cs.openAndCloseSeriesTab(false);

	    layout.selectLayout(layout.threeByThreeLayoutIcon);
	    for(int i=0;i<seriesName.size();i++)
		    cs.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName.get(i)), "Checkpoint[4/4]", "Verified that eye icon is display for all series  "+seriesName.get(i)); 
	}
	
	@Test(groups = { "Chrome","IE","Edge","US2380","Positive","F1086","E2E"})
	public void test03_US2380_TC9957_TC9956_verifyEyeIconForUserCreatedResults() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify new clone entry gets displayed in the Series tab when Series tab is open and clone is created. <br>"+
		"Verify new clone entry gets deleted  when annotation is deleted");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(liver9PatientName, 1);
	    cs=new ContentSelector(driver);
	   
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify clone in series tab when series is tab closed.");
	    circle=new CircleAnnotation(driver);
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 5, 5,-80,-80);
	    
	    cs.openAndCloseSeriesTab(true);
	    List<String> clone=cs.getResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT);
	    cs.assertEquals(clone.get(0), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[1/6]", "Verified result name for the clone as "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[2/6]", "Verified eye icon for the newly created clone.");

	    circle.deleteAllAnnotation(1);
	    cs.assertFalse(cs.isElementPresent(cs.userCreatedResults),"Checkpoint[3/6]", "Verified the machine name for user created findings is not visible after deleting the annotation.");
	   
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify clone in series tab when series is tab open.");
	    cs.openAndCloseSeriesTab(true);
	    circle.selectCircleFromQuickToolbar(1);
	    circle.drawCircle(1, 5, 5,-80,-80);
	    clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT));
	    cs.assertEquals(clone.get(0), ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1", "Checkpoint[4/6]", "Verified result name for the clone as "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX+username+"_1"), "Checkpoint[5/6]", "Verified eye icon for the newly created clone.");

	    circle.deleteAllAnnotation(1);
	    cs.assertFalse(cs.isElementPresent(cs.userCreatedResults), "Checkpoint[6/6]", "Verified the machine name for user created findings is not visible after deleting the annotation.");
	        
	}
	
	@Test(groups = { "Chrome","IE","Edge","US2380","Positive","F1086"})
	public void test04_US2380_TC9958_verifyParentNodesAfterAddingCloneWhenSeriesTabOpen() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify parent node remain expanded even after adding clone copy under collapsed parent node.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+tcgaPatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    rt=helper.loadViewerPageForRTUsingSearch(tcgaPatientName, 1,1);
	    cs=new ContentSelector(driver);
	   
	    cs.openAndCloseSeriesTab(true);
	    String machineName=cs.getMachineName().get(0);
	    String resultName=cs.getAllResults().get(0);
	    
	    //collapse all toggle button if it is expanded already.
	    cs.openAndCloseSeriesTab(true);
		for(int i=0;i<cs.toggleButton.size();i++) {
			if(cs.getAttributeValue(cs.toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE))
				cs.click(cs.toggleButton.get(i));	
		}
	    
		//create a clone by accepting the RT contour when series tab is open
	    rt.navigateToFirstContourOfSegmentation(1);
	    arToolbar=new ViewerARToolbox(driver);
	    arToolbar.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
	    //clone creation is taking time
	    arToolbar.waitForTimePeriod(5000);
	    
	    List<String>clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(machineName,1));
	    cs.assertEquals(clone.get(0), resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", "Checkpoint[1/3]","Verified result name for the clone "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clone.get(0)), "Checkpoint[2/3]", "Verified eye icon for the newly created clone.");
	    for(int i=0;i<cs.toggleButton.size();i++) 
			cs.assertTrue(cs.getAttributeValue(cs.toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE),"Checkpoint[3/3]","Verified that parent node is expanded after craeting a clone when series tab is opened.");
	}
	
	@Test(groups = { "Chrome","IE","Edge","US2380","Negative","F1086","E2E"})
	public void test05_US2380_TC9958_verifyParentNodeAfterAddingCloneWhenSeriesTabClosed() throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify parent node remain expanded even after adding clone copy under collapsed parent node.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    rt=helper.loadViewerPageForRTUsingSearch(tcgaPatientName, 1,1);
	    cs=new ContentSelector(driver);
	   
	    cs.openAndCloseSeriesTab(true);
	    String machineName=cs.getMachineName().get(0);
	    String resultName=cs.getAllResults().get(0);
	    
	    //collapse all toggle button if it is expanded already.
	    cs.openAndCloseSeriesTab(true);
		for(int i=0;i<cs.toggleButton.size();i++) {
			if(cs.getAttributeValue(cs.toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE))
				cs.click(cs.toggleButton.get(i));	
		}
	    
		//create a clone by accepting the RT contour when series tab is closed
		cs.openAndCloseSeriesTab(false);
	    rt.navigateToFirstContourOfSegmentation(1);
	    arToolbar=new ViewerARToolbox(driver);
	    arToolbar.pressKeyboardShortcutsForARP(1,ViewerPageConstants.GSPS_ACCEPT);
	    arToolbar.waitForTimePeriod(5000);
	    
	    cs.openAndCloseSeriesTab(true);
	    List<String>clone=cs.convertWebElementToStringList(cs.getAllResultsForSpecificMachine(machineName,1));
	    cs.assertEquals(clone.get(0), resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1", "Checkpoint[1/3]","Verified result name for the clone which is created "+clone.get(0));
	    cs.assertTrue(cs.verifyPresenceOfEyeIcon(clone.get(0)), "Checkpoint[2/3]", "Verified eye icon for the newly created clone.");
	    for(int i=0;i<cs.toggleButton.size();i++) 
			cs.assertTrue(cs.getAttributeValue(cs.toggleButton.get(i), NSGenericConstants.ARIA_EXPANDED).equalsIgnoreCase(NSGenericConstants.BOOLEAN_TRUE),"Checkpoint[3/3]","Verified that parent node is expanded after craeting a clone when series tab is opened.");
	}
	
	@Test(groups = { "Chrome","Edge","US2380","Positive","F1086","E2E"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test06_US2380_TC9947_TC9948_verifyThemeForEyeIcon(String theme) throws InterruptedException, AWTException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify eye icon is displayed for the series which are loaded in the viewer (for all data types) for eureka theme. <br>"+
		"Verify eye icon is displayed for the series which are loaded in the viewer (for all data types) for dark theme");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbioPatientName+" in viewer" );
		 if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
				db = new DatabaseMethods(driver);
				db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
				loadedTheme=ThemeConstants.DARK_THEME_NAME;
			}else
				loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
		 patientPage = new PatientListPage(driver); 
	    loginPage = new LoginPage(driver);
	    loginPage.navigateToBaseURL();
	    loginPage.waitForLoginPageToLoad();
	    loginPage.login(username,password);
		patientPage = new PatientListPage(driver);
	    helper=new HelperClass(driver);
	    helper.loadViewerDirectly(imbioPatientName, 3);
	    cs=new ContentSelector(driver);
	    
	    pageTheme=new PagesTheme(driver);
	    List<String>series=cs.getAllSeries();
	    List<String>result=cs.getAllResults();
	    
	    cs.openAndCloseSeriesTab(true);
	    cs.assertTrue(pageTheme.verifyThemeForEyeIcon(cs.getWebElementForEyeIcon(series.get(0)), loadedTheme), "Checkpoint[1/2]", "Verified theme for eye icon for the "+series.get(0));
	    
	    for(int i=0;i<result.size();i++)
	    	cs.assertTrue(pageTheme.verifyThemeForEyeIcon(cs.getWebElementForEyeIcon(result.get(i)),loadedTheme),"Checkpoint[2/2]", "Verified theme for eye icon for the "+result.get(i));

	   
	}
	
	@AfterMethod
	public void revertDefaultTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
		
		
	}
}



