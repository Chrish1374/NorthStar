package com.trn.ns.test.obsolete;
//package com.terarecon.northstar.test.obsolete;
//import java.util.List;
//import java.util.Set;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//
//import com.terarecon.northstar.page.factory.LoginPage;
//import com.terarecon.northstar.page.factory.NSConstants;
//import com.terarecon.northstar.page.factory.PatientListPage;
//
//import com.terarecon.northstar.page.factory.SinglePatientStudyPage;
//import com.terarecon.northstar.page.factory.ViewerPage;
//import com.terarecon.northstar.test.base.TestBase;
//import com.terarecon.northstar.test.configs.Configurations;
//import com.terarecon.northstar.utilities.DataReader;
//import com.terarecon.northstar.utilities.ExtentManager;
//@Listeners(com.terarecon.northstar.test.listeners.ItestCustomListener.class)
//public class ContentSelectorTest extends TestBase {
//
//	private ViewerPage viewerpage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//
//	private String firstSeriesDescriptionDoeLilly = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String secondSeriesDescriptionDoeLilly = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));		
//	private String firstResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String secondResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String thirdResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, NSConstants.STUDY01_RESULT03_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//	private String fourthResultDescriptionDoeLilly = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT04_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath"));
//
//	String firstSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));
//	String secondSeriesDescriptionAH4 = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES02_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get("AH.4_filepath"));	
//
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() {
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
//	}
//
//	
//
//	//TC1093Content Selector to access other series and studies content - Edge Cases-Layout Change and TC1229-Content selector should show check marks only for the currently visible series
//	// Patient data = Bone Age study 
//	@Test(groups = { "Chrome","firefox","multimonitor","US323"})
//	public void test03_US323_TC1093_TC1229_verifyContentSelectorNonDICOM_LayoutChange() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify content selector on layout change");
//		//Loading the patient on viewer
//		String filePath = Configurations.TEST_PROPERTIES.get("Bone_Age_Study_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();		
//
//		viewerpage = new ViewerPage(driver);
//		viewerpage.waitForPdfToRenderInViewbox(1);	
//
//		viewerpage.openOrCloseChildWindows(2);
//		List<String> windows = viewerpage.getAllOpenedWindowsIDs();
//		viewerpage.switchToWindow(windows.get(0));
//		//Layout change
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon,true);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying Layout change applied and result and image "
//				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
//		String firstSeriesDescription = DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_SERIES01_TEXTOVERLAY, filePath);
//		String secondResultDescription = DataReader.getResultDesc(PatientXMLConstants.RESULT_DESCRIPTION, PatientXMLConstants.STUDY01_TEXTOVERLAY, PatientXMLConstants.STUDY01_RESULT02_TEXTOVERLAY, filePath);
//
//		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
//		viewerpage.selectSeriesFromContentSelector(1,firstSeriesDescription);
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescription), "Verifying newly displayed series on viewbox is higlighted on content selector", "Verified newly displayed series on viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescription), "Verifying result which is not displayed on viewbox is not highlited on content selector", "Verifying result which is not displayed on viewbox is not highlited on content selector");
//	}
//
//	//TC976-Content Selector to access other series and studies content - Edge Cases-Layout Change
//	// Patient data = Doe Lilly
//	//Verify content selector on layout change on parent and child window
//	@Test(groups = { "Chrome","firefox","multimonitor","US238"})
//	public void test04_US238_TC976_verifyContentSelectorOnLayoutChangeOnParentAndChildWindow() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "Verify content selector on layout change");
//		//Loading the patient on viewer 
//		String filePath = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.oneByOneLayoutIcon,true);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.ONE_BY_ONE_LAYOUT), "Verifying Layout change applied and result and image "
//				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
//
//		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
//		viewerpage.selectSeriesFromContentSelector(1,firstSeriesDescriptionDoeLilly);
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fourthResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly), "Verifying series not displayed on any viewbox is not higlighted on content selector", "Verified series not displayed on any viewbox is not higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly), "Verifying result not displayed on any viewbox is not higlighted on content selector", "Verified result not displayed on any viewbox is not higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescriptionDoeLilly), "Verifying result not displayed on any viewbox is not higlighted on content selector", "Verified result not displayed on any viewbox is not higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateSeriesIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly), "Verifying series not displayed on any viewbox is not higlighted on content selector", "Verified series not displayed on any viewbox is not higlighted on content selector");
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {				
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//				viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon,true);
//				viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.TWO_BY_TWO_LAYOUT), "Verifying Layout change applied and result and image "
//						+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");	
//
//				//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
//				viewerpage.selectSeriesFromContentSelector(1,firstResultDescriptionDoeLilly);
//				viewerpage.openNorthstarLogo();
//				viewerpage.selectAnnotation(ViewerPageConstants.FULL_ANNOTATION);
//				viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
//				viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
//				viewerpage.assertFalse(viewerpage.validateSeriesIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly), "Verifying Checkbox appears next to newly displayed  series in viewbox 1", "Verified Checkbox appears next to newly displayed  series in viewbox 1");
//			}				
//		}
//	}			
//
//	@Test(groups = { "Chrome","firefox","multimonitor","DE450"})
//	public void test17_DE450_TC2039_VerifySeriesPushedSelectedCS() throws InterruptedException  {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "Layout change is not working on multimonitor- Content Selector Persistence");
//
//		//Loading the patient on viewer 
//		String filePath = Configurations.TEST_PROPERTIES.get("Doe_Lilly_filepath");
//		String PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);		
//		viewerpage.waitForPdfToRenderInViewbox(3);	
//
//		String parentWindow = viewerpage.getCurrentWindowID();
//		viewerpage.openOrCloseChildWindows(2);
//		viewerpage.maximizeWindow();
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.twoByTwoLayoutIcon,true);
//
//		viewerpage.selectSeriesFromContentSelector(4,firstSeriesDescriptionDoeLilly);
//
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fourthResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//
//		viewerpage.selectLayout(viewerpage.oneByOneLAndTwoByOneRLayoutIcon, false);
//		viewerpage.assertEquals(viewerpage.getNumberOfCanvasForLayout(), viewerpage.getExpectedNumberOfCanvasForLayout(NSGenericConstants.ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT), "Verifying Layout change applied and result and image "
//				+ "content can be loaded in each viewport.", "Verified layout change applied and result and image content can be loaded in each viewport.");		
//
//		//Verify Indication of the Currently displayed Series/Content on any of the view port, any monitor as  a check icon
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fourthResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//
//
//
//		Set<String> childWinHandles = viewerpage.getAllOpenedWindowsID();
//		for (String childWindow : childWinHandles) {				
//			if(!childWindow.equals(parentWindow)){
//				viewerpage.switchToWindow(childWindow);
//				viewerpage.maximizeWindow();
//
//				viewerpage.selectResultFromContentSelector(3,thirdResultDescriptionDoeLilly);
//				viewerpage.selectSeriesFromContentSelector(4,fourthResultDescriptionDoeLilly);
//
//				viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//				viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//				viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//				viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//				viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//				viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fourthResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//
//
//			}				
//		}
//
//		viewerpage.switchToWindow(parentWindow);
//		viewerpage.selectLayout(viewerpage.threeByThreeLayoutIcon, true);
//
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, firstSeriesDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, secondSeriesDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateResultIsSelectedOnContentSelector(1, firstResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, secondResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertFalse(viewerpage.validateResultIsSelectedOnContentSelector(1, thirdResultDescriptionDoeLilly),"Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//		viewerpage.assertTrue(viewerpage.validateSeriesIsSelectedOnContentSelector(1, fourthResultDescriptionDoeLilly), "Verifying series displayed on any viewbox is higlighted on content selector", "Verified series displayed on any viewbox is higlighted on content selector");
//
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(1),firstSeriesDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(2),secondSeriesDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getResultDescriptionOverlayText(3),firstResultDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getResultDescriptionOverlayText(4),firstSeriesDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getResultDescriptionOverlayText(5),thirdResultDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getResultDescriptionOverlayText(6),thirdResultDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//		viewerpage.assertEquals(viewerpage.getSeriesDescriptionOverlayText(7),fourthResultDescriptionDoeLilly, "Validate additional series that cannot fit in Parent window moves to first viewbox", firstSeriesDescriptionDoeLilly+" series is successfully loaded in first viewbox");
//
//
//
//
//	}			
//
//       @Test(groups = { "Chrome","firefox","Edge","IE11","US765"})
//         public void test12_US765_TC2740_VerifySpinningWheelDisplay() throws InterruptedException {
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription( "Verify spinning wheel display in content selector ");
//		//Loading the patient on viewer 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+liver9PatientName+" in viewer" );
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(doeLillyPatientName);
//		
//		studyPage.clickOntheFirstStudy();		
//		viewerpage = new ViewerPage(driver);
//		contentSelector = new ContentSelector(driver);
//		viewerpage.waitForViewerpageToLoad();
		//Call verifySpinnerOnSeriesSelector method which checks for the spinner
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1]", "Verified the spinner during load of series selecttor");
//		viewerpage.assertTrue(contentSelector.verifySpinnerOnSeriesSelector(1),"Verify the spinner during load of series selector", "verified");


//	}	
//}
//
//
//
