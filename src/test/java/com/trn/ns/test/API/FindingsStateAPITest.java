package com.trn.ns.test.API;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

import io.restassured.response.Response;

//This class should be used to verify the JSONResponse and application viewer content
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class FindingsStateAPITest extends TestBase {

	private ExtentTest extentTest;
	private PatientListPage patientListPage;
	private ViewerPage viewerPage;
	private DatabaseMethods db;
	String flag = "false";


	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath); 

	String filePath1 = TEST_PROPERTIES.get("Cardiac_MR_T1T2_filepath");
	String cardiact1t2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);



	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	private PointAnnotation point;
	private HelperClass helper;
	private ContentSelector cs;

	@Test(groups ={"IE11","Chrome","Edge","Positive","DE1859"})
	public void test01_DE1859_TC7460_verifyPendingFindingsOnFindingMenuAndJSONResponse() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the annotations present on the series against finding menu and JSON data.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + GSPS_PatientName + "in viewer");
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName, username, password, 1);
		
		db= new DatabaseMethods(driver);
		ViewerSliderAndFindingMenu findingMenu = new  ViewerSliderAndFindingMenu(driver);
		String series_instance_id="";
		List<String> seriesNumberList= new ArrayList<String>();
		HashMap<String, Integer> seriesNumberFindingMenuCount = new HashMap<String, Integer>();

		for(int i=1;i<=viewerPage.getNumberOfCanvasForLayout();i++){

			seriesNumberList.add(viewerPage.getSeriesDescriptionOverlayText(i));
			viewerPage.mouseHover(viewerPage.getViewPort(i));
			seriesNumberFindingMenuCount.put(viewerPage.getSeriesDescriptionOverlayText(i), (findingMenu.getBadgeCountFromToolbar(i)));
		}

		for (Map.Entry<String,Integer> entry : seriesNumberFindingMenuCount.entrySet())
		{


			series_instance_id=db.getSeriesInstanceID(GSPS_PatientName, entry.getKey());
			LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
			hm.put(PatientXMLConstants.SERIES_ID,series_instance_id);

			Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
			List<Object> response =RESTUtil.getMethod(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal,hm);

			viewerPage.assertTrue(viewerPage.containsIgnoreCase(response.toString(),("\"pending\": "+entry.getValue()+"")),"Checkpoint[1/1]","Verify the pending count on viewer finding menu and JSON response");




		}
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1263"})
	public void test02_US1263_TC7460_verifyFindingsCreatedOnPhase() throws InterruptedException, SQLException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify findingsReport response when new annotations are drawn on Phases");

		helper = new HelperClass(driver);
		viewerPage =  helper.loadViewerDirectly(cardiact1t2PatientName, username, password, 1);
		
		// Loading the patient on viewer
		point = new PointAnnotation(driver);
		
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);

		point.scrollDownToSliceUsingKeyboard(1);
		point.pressKey(NSGenericConstants.DOT_KEY);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
		point.selectRejectfromGSPSRadialMenu();		

		point.scrollDownToSliceUsingKeyboard(2);
		point.pressKey(NSGenericConstants.DOT_KEY);
		point.drawPointAnnotationMarkerOnViewbox(1, 100, 100);
		point.selectAcceptfromGSPSRadialMenu();		

		List<String> rejectedFindings = point.getFindingsName(1,ViewerPageConstants.REJECTED_FINDING_COLOR);
		List<String> acceptedFindings = point.getFindingsName(1,ViewerPageConstants.ACCEPTED_FINDING_COLOR);
		List<String> pendingFindings = point.getFindingsName(1,ViewerPageConstants.PENDING_FINDING_COLOR);

		cs = new ContentSelector(driver);
		String currentSelectResult = cs.getResultsForSpecificMachine(ViewerPageConstants.USER_CREATED_RESULT).get(0);
		
		db= new DatabaseMethods(driver);
		String studyUID = db.getStudyInstanceUID(cardiact1t2PatientName);
		String batchUID = db.getBatchUIDFromBatchTable(cardiact1t2PatientName);
		String creationDate = db.getGSPSLevelInfo(currentSelectResult,NSDBDatabaseConstants.GSPS_TABLE_PRESENTATION_CREATION_DATE);
		String creationTime = db.getGSPSLevelInfo(currentSelectResult,NSDBDatabaseConstants.GSPS_TABLE_PRESENTATION_CREATION_TIME);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(PatientXMLConstants.STUDY_UID_TEXTOVERLAY,studyUID);
		hm.put(PatientPageConstants.PATIENTID,cardiact1t2PatientName);

		
		Map<String, String> keyVal = RESTUtil.getTokenWithKey(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.TOKEN_URL,username,password,OrthancAndAPIConstants.HEADER_KEY);
		Response response =RESTUtil.getResponse(OrthancAndAPIConstants.API_BASE_URL,OrthancAndAPIConstants.API_DATA_GET_BASE_URL,keyVal,hm);
				
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.BATCHID_UID).replace("[", "").replace("]", ""),batchUID,"Checkpoint[1/10]","Verifying the batch id same as DB");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.BATCHID_STATUS).replace("[", "").replace("]", ""),OrthancAndAPIConstants.DONE,"Checkpoint[2/10]","verifying the batch status");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.BATCHID_FINISHEDAT).replace("[", "").replace("]", ""),creationDate+"T"+creationTime,"Checkpoint[3/10]","verifying the creation time");
		
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.MACHINEID_INFO).replace("[", "").replace("]", ""),OrthancAndAPIConstants.NULL_STRING,"Checkpoint[4/10]","verifying the machine info");
	
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_CREATION_DATE).replace("[", "").replace("]", ""),creationDate+"T"+creationTime,"Checkpoint[5/10]","verifying the summary creation date");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_UPDATED_STATE).replace("[", "").replace("]", ""),OrthancAndAPIConstants.NULL_STRING,"Checkpoint[6/10]","verifying result summary state");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_LAST_UPDATED_USER).replace("[", "").replace("]", ""),OrthancAndAPIConstants.NULL_STRING,"Checkpoint[7/10]","verifying result summary updated user");
		

		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_FINDING_STATE_GSPS_ACCEPTED).replace("[", "").replace("]", ""),acceptedFindings.size()+"","Checkpoint[8/10]","verifying the accepted count");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_FINDING_STATE_GSPS_REJECTED).replace("[", "").replace("]", ""),rejectedFindings.size()+"","Checkpoint[9/10]","verifying the rejected count");
		point.assertEquals(RESTUtil.getResponseValue(response,OrthancAndAPIConstants.RESULT_SUMMARY_FINDING_STATE_GSPS_PENDING).replace("[", "").replace("]", ""),pendingFindings.size()+"","Checkpoint[10/10]","verifying the pending count");
		
		


	}

}