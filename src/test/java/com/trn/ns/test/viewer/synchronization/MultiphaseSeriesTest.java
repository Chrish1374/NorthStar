package com.trn.ns.test.viewer.synchronization;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewerSendToPACS;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class MultiphaseSeriesTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private ViewerSendToPACS sd;
	DatabaseMethods db= new DatabaseMethods(driver);



	// Get Patient Name
	String filePath = Configurations.TEST_PROPERTIES.get("MR_CARDIAC_filepath");
	String mrCardiacPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	String studyDesc=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY,"STUDY01", filePath);

	String filePath1 = TEST_PROPERTIES.get("AH4_pdf_filepath");
	String ah4PDFPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String tdaMapPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String filePath3 = TEST_PROPERTIES.get("Breast_Time_Intensity_filepath");
	String breastCarePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath4 = TEST_PROPERTIES.get("Cardiac_MR_T1T2_filepath");
	String cardiact1t2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);


	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private ContentSelector cs;
	private PointAnnotation point;
	private HelperClass helper;


	// Before method, handles the steps before loading the data for set up.
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {


		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);

	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US964", "Positive","Sanity","BVT"})
	public void test01_US964_TC4644_TC4684_verifyMultiPhaseBasicCondition()throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that in source tab multiple series should be grouped into one series when their sopClassUID, image position & number of instances are same [when series names are different]"
				+ "<br> Verify that results of multiple series should be navigate thru the phases");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(mrCardiacPatient);	
		patientListPage.clickOnStudy(studyDesc);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);
		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(2), "Checkpoint[1.1/6]", "verifying that data has label phase for multiphase data");

		DatabaseMethods db = new DatabaseMethods(driver);		
		List<List<String>> seriesFromDB = db.getSeriesAndInstanceInfoForMultiphase(mrCardiacPatient,studyDesc);

		viewerPage.closeNotification();
		cs = new ContentSelector(driver);
		List<String> seriesListFromCS = cs.getAllSeries();
		cs.assertEquals(seriesListFromCS.get(0),String.join(" ",seriesFromDB.get(0)), "Checkpoint[1.2/6]", "Also, Verify that name of grouped series are concatenated  and displayed as one string and one entry in source tab of content selector [when series names are different]");
		cs.assertEquals(cs.getNumberOfCanvasForLayout(),2, "Checkpoint[2/6]", "Should load in a 1x2 layout (PDF on left, MR on right)");
		cs.assertTrue(cs.isElementPresent(cs.getSeriesDescription(2)), "Checkpoint[2.1/6]", "Verifying the PDF");
		cs.assertTrue(cs.isElementPresent(cs.getViewerCanvas(2)), "Checkpoint[2.2/6]", "Verifying the DICOM on right");		


		cs.assertEquals(cs.getValueOfMaxPhase(2), seriesFromDB.get(0).size(), "Checkpoint[3/6]", "MR series has 2 phases of 8 slices");
		cs.assertEquals(cs.getMaxNumberofScrollForViewbox(2), db.getNumberOfSlice(cs.getSeriesDescriptionOverlayText(2)), "Checkpoint[4/6]", "MR series has 2 phases of 8 slices");


		DICOMRT rt = new DICOMRT(driver);
		rt.assertTrue(rt.isElementPresent(rt.legendOptions),"Checkpoint[5.1/6]", "RT Struct is applied to both phases");


		int DefaultPhasePosition=viewerPage.getValueOfCurrentPhase(2);
		String CurrentSlicePosition=viewerPage.getCurrentScrollPosition(2);
		String currentSeriesDesc = viewerPage.getSeriesDescriptionOverlayText(2);

		//Click on dot(.) from keyboard to change phase number
		viewerPage.click(viewerPage.getViewPort(2));
		viewerPage.pressKey(NSGenericConstants.DOT_KEY);	
		viewerPage.waitForAllImagesToLoad();
		int ChangePhasePosition=viewerPage.getValueOfCurrentPhase(2);

		//verify change phase number for first viewbox

		viewerPage.assertNotEquals(ChangePhasePosition,DefaultPhasePosition, "Checkpoint:[6.1/6]", "Able to navigate slices and phases");
		viewerPage.assertEquals(CurrentSlicePosition,viewerPage.getCurrentScrollPosition(2), "Checkpoint[6.2/6]", "Verified slice postion for "+CurrentSlicePosition);
		viewerPage.assertNotEquals(viewerPage.getSeriesDescriptionOverlayText(2),currentSeriesDesc, "Checkpoint[6.3/6]", "Verified series description is changed on changing the phase");

		rt.assertTrue(rt.isElementPresent(rt.legendOptions),"Checkpoint[5.2/6]", "RT Struct is applied to both phases");

		viewerPage.scrollDownToSliceUsingKeyboard(2,1);

		viewerPage.assertEquals(ChangePhasePosition,viewerPage.getValueOfCurrentPhase(2), "Checkpoint:[6.3/6]", "Able to navigate slices");
		viewerPage.assertNotEquals(CurrentSlicePosition,viewerPage.getCurrentScrollPosition(2), "Checkpoint[6.4/6]", "Verified slice postion for "+CurrentSlicePosition);


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US964", "Negative"})
	public void test02_US964_TC4645_verifySeriesNotClubWhenNoConditionMatched()throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that in source tab multiple series should not be grouped into one series when their sopClassUID, image position & number of instances are not same");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(ah4PDFPatientName);
		String study = patientListPage.getStudyDescription(0);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);

		for(int i =0;i<viewerPage.getNumberOfCanvasForLayout();i++)
			viewerPage.assertFalse(viewerPage.verifyPhaseTextPresence(i), "Checkpoint[1."+i+"/3]", "verifying that data has no label phase for multiphase data");

		DatabaseMethods db = new DatabaseMethods(driver);		
		List<List<String>> seriesFromDB = db.getSeriesAndInstanceInfoForMultiphase(ah4PDFPatientName,study);

		viewerPage.assertTrue(seriesFromDB.isEmpty(), "Checkpoint[2/3]", "Verify that multiple series which don't have same sopClassUID or image position or same number of instances should not be grouped into one series.");

		cs = new ContentSelector(driver);
		cs.assertEquals(cs.getAllSeries().size(), cs.getNumberOfCanvasForLayout(), "Checkpoint[3/3]", "Verifying there is no concatination of series displayed");



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US964", "Negative"})
	public void test03_US964_TC4646_TC4685_TC4686_verifyResultNotClubWhenBasicConditionIsMatched()throws InterruptedException, SQLException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that results of  multiple series should be grouped into one when their sopClassUID, image position & number of instances are same [when names are different]"
				+ "<br> Verify that in source tab Source series should group all phases into a single entry"
				+ "<br> Verify that results tab of multiple series should not be concatenated in one name when their sopClassUID, image position & number of instances are same");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(tdaMapPatientName);
		String study = patientListPage.getStudyDescription(0);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);


		DatabaseMethods db = new DatabaseMethods(driver);		
		List<List<String>> seriesFromDB = db.getSeriesAndInstanceInfoForMultiphase(tdaMapPatientName,study);
		Collections.sort(seriesFromDB.get(0));

		cs = new ContentSelector(driver);
		cs.assertEquals(cs.getAllSeries().get(0), String.join(" ",seriesFromDB.get(0)), "Checkpoint[1/3]", "Verifying that only source is combined");

		List<String> resultsFromDB = db.getResultsForPatient(tdaMapPatientName,study);
		Collections.sort(resultsFromDB);

		List<String> resultsFromCS = cs.getAllResults();
		Collections.sort(resultsFromCS);

		cs.assertEquals(resultsFromDB,resultsFromCS, "Checkpoint[2/3]", "Verifying that no combination of result");	

		cs.selectSeriesFromSeriesTab(1, String.join(" ",seriesFromDB.get(0)));		

		viewerPage.mouseHover(viewerPage.getViewPort(1));
		for(int i =0;i<seriesFromDB.get(0).size()-1;i++) {

			viewerPage.assertEquals(viewerPage.getSeriesDescriptionOverlayText(1), seriesFromDB.get(0).get(i), "Checkpoint[3/3]", "Verifying that on navigating the phase the series description is changing according to phase");
			viewerPage.pressKey(NSGenericConstants.DOT_KEY);
		

		}



	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US964", "Positive"})
	public void test04_US964_TC4683_verifySeriesNameIsSameSoNoMoreClubbing()throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that in source tab multiple series should be grouped into one series when their sopClassUID, image position & number of instances are same [when series names are same]");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(breastCarePatientName);
		String study = patientListPage.getStudyDescription(0);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(2);

		String seriesDesc = viewerPage.getSeriesDescriptionOverlayText(2);
		viewerPage.mouseHover(viewerPage.getViewPort(2));

		for(int i =0;i<viewerPage.getValueOfMaxPhase(2);i++) {
			viewerPage.pressKey(NSGenericConstants.DOT_KEY);
			viewerPage.assertEquals(seriesDesc, viewerPage.getSeriesDescriptionOverlayText(2), "Checkpoint[1/3]", "Verifying that on scroll of phase series desc is same as series name are same for all phase");


		}

		cs = new ContentSelector(driver);
		cs.assertTrue(cs.getAllSeries().contains(seriesDesc),"Checkpoint[2/3]","Verifying that no concatenation is done as all the phase has same name");

		DatabaseMethods db = new DatabaseMethods(driver);		
		List<List<String>> seriesFromDB = db.getSeriesAndInstanceInfoForMultiphase(breastCarePatientName,study);

		for(int i =0;i<seriesFromDB.get(0).size();i++) {
			viewerPage.assertEquals(seriesDesc, seriesFromDB.get(0).get(i), "Checkpoint[3/3]", "verifying that DB has no different series since all series are same");


		}


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1868", "Positive"})
	public void test05_DE1868_TC7485_TC7488_TC7496_verifyARToolbarPresenceAndFindingMenu()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that  findings menu in AR tool bar, is showing all the findings from all Phases."
				+ "<br> Verify that vertical sliding bar is showing the findings on phase level."
				+ "<br> Verify that findings in AR tool bar and sliding bar when annotation are drawn on different slices of different phases.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(cardiact1t2PatientName);
		patientListPage.clickOntheFirstStudy();

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();

		point.selectPointFromQuickToolbar(1);

		for(int phase =0;phase<point.getValueOfMaxPhase(1);phase++) {

			point.scrollToImage(1, 1);
			for(int slice =0 ; slice<point.getMaxNumberofScrollForViewbox(1);slice++) {
				point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
				point.scrollDownToSliceUsingKeyboard(1);

			}
			point.pressKey(NSGenericConstants.DOT_KEY);				


		}

		int totalFindings = point.getValueOfMaxPhase(1)* point.getMaxNumberofScrollForViewbox(1);		
		point.assertEquals(point.getFindingsCountFromFindingTable(),totalFindings,"Checkpoint[1/9]","Verifying that findings are displayed in finding table after creation of findings across slices and phases");

		List<String> allFindings = point.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		for(int findingNo=0;findingNo<allFindings.size()-1;findingNo++) {
			point.assertEquals(allFindings.get(findingNo),ViewerPageConstants.POINT_FINDING_PREFIX+(findingNo+1),"Checkpoint[7."+findingNo+"/8]","Verifying all the findings are displayed in finding menu after creating text annotation");
		}

		point.scrollToImage(1, 1);
		point.scrollToPhase(1, 1);
		point.mouseHover(point.getAcceptRejectToolBar(1));

		int findingOnSliderNo=1;

		for(int slice =1 ; slice<=point.getMaxNumberofScrollForViewbox(1);slice++) {

			for(int phase =1;phase<=point.getValueOfMaxPhase(1);phase++) {

				point.selectNextfromGSPSRadialMenu();
				point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[2/9]", "Verifying the point is getting selected after performing the GSPS next");
				point.assertEquals(point.getValueOfCurrentPhase(1), phase, "Checkpoint[3/9]", "verifying the phase are scrolling first");
				point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[4/9]", "verifying the slice is not changing");

				point.assertTrue(point.verifySliderPresence(1),"Checkpoint[5/9]","Verifying that slider is displayed");
				point.assertEquals(point.getFindingMarkersOnSlider(1).size(),point.getMaxNumberofScrollForViewbox(1),"Checkpoint[6/9]","verifying that phase specific findings are only displayed");
				point.assertTrue(point.verifyMarkerColorOnSlider(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[7/9]","Verifying the state of markers - green");		

				List<WebElement> marks = point.getFindingMarkersOnSlider(1);
				for(int slideMarker = 1 ; slideMarker<=marks.size();slideMarker++)
				{

					List<WebElement> values = point.getAllFindingsFromSliderContainer(1, slideMarker);
					point.assertEquals(values.size(),1,"Checkpoint[8/9]","verifying that there is only finding present in finding container");
					point.assertEquals(point.getText(values.get(0)),ViewerPageConstants.POINT_FINDING_PREFIX+findingOnSliderNo,"Checkpoint[9/9]","verifying the finding name");
					if(findingOnSliderNo==totalFindings)
						findingOnSliderNo=1;
					else
						findingOnSliderNo++;
				}
			}


		}


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "DE1868", "Positive"})
	public void test06_DE1868_TC7499_verifyTextAnnotationOnMultiphase()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on drawing text annotation on viewer getting displayed in AR tool bar finding menu when already annotation are present on different phases and slices.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(cardiact1t2PatientName);
		patientListPage.clickOntheFirstStudy();

		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();

		point.selectPointFromQuickToolbar(1);

		for(int phase =0;phase<point.getValueOfMaxPhase(1);phase++) {

			point.scrollToImage(1, 1);
			for(int slice =0 ; slice<point.getMaxNumberofScrollForViewbox(1);slice++) {
				point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
				point.scrollDownToSliceUsingKeyboard(1);

			}
			point.pressKey(NSGenericConstants.DOT_KEY);				


		}

		int totalFindings = point.getValueOfMaxPhase(1)* point.getMaxNumberofScrollForViewbox(1);		
		point.assertEquals(point.getFindingsCountFromFindingTable(),totalFindings,"Checkpoint[1/8]","Verifying all the findings are displayed created across slices and phases");

		point.scrollToImage(1, 1);
		point.scrollToPhase(1, 1);

		String text = "Annotation";
		TextAnnotation textAnno = new TextAnnotation(driver);
		textAnno.selectTextArrowFromQuickToolbar(1);

		textAnno.drawText(1, -100, -100, text);		

		point.selectPreviousfromGSPSRadialMenu();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[2/8]", "Verifying that point is focused after creating the text annotation and navigate prev");
		point.assertEquals(point.getValueOfCurrentPhase(1), 1, "Checkpoint[3/8]", "User stays on phase 1");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), 1, "Checkpoint[4/8]", "User stays on slice 1");
		point.assertEquals(textAnno.getTextAnnotations(1).size(), 1,  "Checkpoint[5/8]", "Verifying that there is text annotation present");

		List<String> allFindings = point.getFindingsName(1, ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		point.assertEquals(allFindings.size(),totalFindings+1,"Checkpoint[6/8]","Verifying that there are one more finding added in finding menu");

		for(int findingNo=0;findingNo<allFindings.size()-1;findingNo++) {
			point.assertEquals(allFindings.get(findingNo),ViewerPageConstants.POINT_FINDING_PREFIX+(findingNo+1),"Checkpoint[7."+findingNo+"/8]","Verifying all the findings are displayed in finding menu after creating text annotation");
		}
		point.assertEquals(allFindings.get(allFindings.size()-1),ViewerPageConstants.TEXT_FINDING_NAME,"Checkpoint[8/8]","Verifying that text annotation is also added in finding menu");


	}

	@Test(groups = { "Chrome", "IE11", "Edge", "US1263", "Positive"})
	public void test07_US1263_TC7200_verifyGroupingWhenImagePosDifferent()throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that a study with multiple series is grouped into one series when their sopClassUID, number of instances are same but with different image position(X,Y,Z) and series names.");

		// Loading the patient on viewer
		patientListPage = new PatientListPage(driver);
		patientListPage.clickOnPatientRow(cardiact1t2PatientName);
		String study = patientListPage.getStudyDescription(0);
		patientListPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(1);

		viewerPage.assertTrue(viewerPage.verifyPhaseTextPresence(1), "Checkpoint[1/3]", "verifying that data has label phase for multiphase data");

		DatabaseMethods db = new DatabaseMethods(driver);		
		List<List<String>> seriesFromDB = db.getGroupedForMultiphaseWhenImageposNotEqual(cardiact1t2PatientName,study);

		cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		cs.assertEquals(series.size(), seriesFromDB.size(), "Checkpoint[2/3]", "Verifying that content selector and DB has same series list");

		String finalText =series.get(0);
		for(int j = 0 ;j<seriesFromDB.get(0).size();j++) {
			finalText = finalText.replace(seriesFromDB.get(0).get(j), "").trim();
			seriesFromDB.get(0).remove(j);

		}
		cs.assertEquals(finalText, seriesFromDB.get(0).get(0), "Checkpoint[3/3]", "verifying content selector have concatinating series");


	}
		
	@Test(groups = { "Chrome", "IE11", "Edge", "US1263", "Positive"})
	public void test08_US1263_TC7242_verifyFindingInOPAndSendToPACS()throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the findings drawn on Phases  are seen in output panel and are present in the series after reload.");

		// Loading the patient on viewer
		
		helper = new HelperClass(driver);
		helper.loadViewerPageUsingSearch(cardiact1t2PatientName, 1, 1);
		
		point = new PointAnnotation(driver);
		point.waitForViewerpageToLoad();
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
		
		int phase = point.getValueOfCurrentPhase(1);
		int slice = point.getCurrentScrollPositionOfViewbox(1);
		
		point.scrollDownToSliceUsingKeyboard(1);
		point.pressKey(NSGenericConstants.DOT_KEY);	
		
		point.mouseHover(point.getViewPort(1));
		
		OutputPanel op = new OutputPanel(driver);
		
		op.enableFiltersInOutputPanel(true, true, true);
		op.assertEquals(op.thumbnailList.size(), 1, "", "");
		op.clickOnJumpIcon(1);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1), "Checkpoint[1/6]", "verifying point is selected on click of jump to icon");
		
		point.assertEquals(point.getValueOfCurrentPhase(1), phase, "Checkpoint[2/6]", "verifying the phase it is navigated to");
		point.assertEquals(point.getCurrentScrollPositionOfViewbox(1), slice, "Checkpoint[3/6]", "verifying the slice after jump to");
		
		point.selectAcceptfromGSPSRadialMenu();
		
		sd=new ViewerSendToPACS(driver);
		point.performMouseRightClick(point.getViewPort(1));

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Selecting the leave as is option after sending it to PACS");
		sd.openSendToPACSMenu(true,true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,true);

		point.assertFalse(sd.isElementPresent(sd.notificationMessage.get(0)), "Checkpoint[4/6]", "verifying the message is displayed with text");
		
		helper.browserBackAndReloadViewer(cardiact1t2PatientName, 1, 1);
		point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(1, 1), "Checkpoint[5/6]", "verifying or reload of viewer point is displayed");
		
		List<String> rejectedFindings = point.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = point.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = point.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		ViewerSendToPACS sd = new ViewerSendToPACS(driver);
		acceptedFindings.replaceAll(sd.new ReplaceVal());
		rejectedFindings.replaceAll(sd.new ReplaceVal());
		pendingFindings.replaceAll(sd.new ReplaceVal());
		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getSelectedResults().get(0);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[6/6]", "Verifying the orthanc server for findings");
		sd.verifyOrthancFindings(cardiact1t2PatientName, username, currentSelectResult, 1,acceptedFindings , rejectedFindings, pendingFindings);
	}

	
	@AfterMethod(alwaysRun=true)
	public void afterMethod(){
		

		RESTUtil.setBaseURI(OrthancAndAPIConstants.ORTHANC_BASE_URL);
		RESTUtil.setBasePath(OrthancAndAPIConstants.PATIENT_ORTHANC_URL);
		Response response = RESTUtil.getResponse();		

		for(int i =0;i<RESTUtil.getJsonPath(response).getList("").size();i++) {
			String patient_id = RESTUtil.getJsonPath(response).getList("").get(i).toString();
			RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.ORTHANC_BASE_URL, OrthancAndAPIConstants.PATIENT_ORTHANC_URL,patient_id).asString();
		}



	}



}






