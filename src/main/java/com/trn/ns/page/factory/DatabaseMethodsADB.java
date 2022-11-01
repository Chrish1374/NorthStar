package com.trn.ns.page.factory;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;

import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.DBUtil;
import com.trn.ns.utilities.GSONParser;
import com.trn.ns.utilities.Utilities;


public class DatabaseMethodsADB extends DatabaseMethods {

	public DatabaseMethodsADB(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}


	//	################################################
	//	NSUsageDB
	//	################################################

	public List<String> getPayload(String userAction, String... condition) throws SQLException {

		String query= "Select Payload  from UserActionLog where UserActionType='"+userAction+"' ";
		for(int i=0;i<condition.length;i++)
			query = query + "and Payload like '%"+condition[i]+"%'";

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query , TEST_PROPERTIES.get("nsAnalyticsdbName"));
		List<String> values = new ArrayList<String>();
		while(result.next())
			values.add(result.getString(1));

		DBUtil.closeDBConnection();
		return values;

	}

	public List<String> getPayload() throws SQLException {

		String query= "Select Payload from UserActionLog";

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query , TEST_PROPERTIES.get("nsAnalyticsdbName"));
		List<String> values = new ArrayList<String>();
		while(result.next())
			values.add(result.getString(1));

		DBUtil.closeDBConnection();
		return values;

	}

	public ArrayList<String> getCopyFiltersValue(String userAccountID, String... condition) throws SQLException {

		String query= "Select CopyFilters  from CopyFilter where UserAccountId='"+userAccountID+"' ";
		for(int i=0;i<condition.length;i++)
			query = query + "and CopyFilters like '%"+condition[i]+"%'";

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query , TEST_PROPERTIES.get("dbName"));
		ArrayList<String> values = new ArrayList<String>();
		while(result.next())
			values.add(result.getString(1));

		DBUtil.closeDBConnection();
		return values;
	}

	public String getKeyValue(String payload,String... arraykeys) {

		String value="";

		payload = payload.replace("\\", "");
		HashMap<String, Object> kv = GSONParser.createHashMapFromJsonString(payload);
		if(arraykeys.length == 2)
		{
			if(kv.get(arraykeys[0]) instanceof HashMap<?, ?>)
				LOGGER.info(Utilities.getCurrentThreadId() + "Getting key value out of payload "+payload +" keys "+arraykeys[0]+ " subkey "+arraykeys[1]);

			kv = (HashMap<String, Object>) kv.get(arraykeys[0]);
			value=kv.get(arraykeys[1]).toString();
		}

		else if(arraykeys.length == 3)
		{
			if(kv.get(arraykeys[0]) instanceof HashMap<?, ?>)
				LOGGER.info(Utilities.getCurrentThreadId() + "Getting key value out of payload "+payload +" keys "+arraykeys[0]+ " subkey "+arraykeys[1]);

			kv = (HashMap<String, Object>) kv.get(arraykeys[0]);
			kv = (HashMap<String, Object>) kv.get(arraykeys[1]);
			value=kv.get(arraykeys[2]).toString();
		}
		else{
			LOGGER.info(Utilities.getCurrentThreadId() + "Getting key value out of payload "+payload +" key "+arraykeys[0]);
			value =  kv.get(arraykeys[0]).toString();

		}
		return value;

	}

	public boolean verifySeriesStartAndEnd(List<String> startPayload, List<String> endPayload, String studyActionID,String onPageLoad, String onLayoutChange, String onContentSelector,String mimeType, String layout) throws SQLException {

		boolean studyIDSame = getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID).equals(studyActionID) && getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID).equals(studyActionID);		
		LOGGER.info(Utilities.getCurrentThreadId() + "Validating the studyID is same for series start and series end expected: "+studyActionID+" actual: "+getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "studyID flag "+studyIDSame);

		boolean loadSeriesActionID = getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID).equals(getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Validating the series instanceID is same for series start value: "+getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID)+" series end value: "+getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Load series instance id flag "+loadSeriesActionID);

		boolean flagOnDefaultLoad= getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD).equals(onPageLoad) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD).equals(onPageLoad);

		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on default "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD));
		LOGGER.info(Utilities.getCurrentThreadId() + "On default loading series load flag "+ flagOnDefaultLoad);

		boolean flagOnLayoutChangeLoad= getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED).equals(onLayoutChange) && 
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED).equals(onLayoutChange);
		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on layout change "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED));
		LOGGER.info(Utilities.getCurrentThreadId() + "On layout change series load flag "+ flagOnLayoutChangeLoad);

		boolean flagOnContentSelectorLoad = getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR).equals(onContentSelector) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR).equals(onContentSelector);
		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on content selector change "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR));
		LOGGER.info(Utilities.getCurrentThreadId() + "On content selector series load flag "+ flagOnContentSelectorLoad);

		boolean flagOnMime = getKeyValue(startPayload.get(0),ActionLogConstant.SERIES_INFO, ActionLogConstant.MIME_TYPE).equals(mimeType) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_INFO, ActionLogConstant.MIME_TYPE).equals(mimeType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Mime type in payload" + getKeyValue(startPayload.get(0),ActionLogConstant.SERIES_INFO, ActionLogConstant.MIME_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Mime verification flag "+flagOnMime);

		boolean flagOnLayout = getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout) &&
				getKeyValue(endPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Layout in payload "+getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Layout verification flag "+flagOnLayout);

		return (studyIDSame && loadSeriesActionID && flagOnDefaultLoad && flagOnMime && flagOnLayout && flagOnLayoutChangeLoad && flagOnContentSelectorLoad);

	}


	public boolean verifyResulltStartAndEnd(List<String> startPayload, List<String> endPayload, String studyActionID,String onPageLoad, String onLayoutChange, String onContentSelector,String mimeType, String layout) throws SQLException {

		boolean studyIDSame = getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID).equals(studyActionID) && getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID).equals(studyActionID);		
		LOGGER.info(Utilities.getCurrentThreadId() + "Validating the studyID is same for series start and series end expected: "+studyActionID+" actual: "+getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "studyID flag "+studyIDSame);

		boolean loadSeriesActionID = getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID).equals(getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Validating the series instanceID is same for series start value: "+getKeyValue(startPayload.get(0), ActionLogConstant.LOAD_SERIES_ACTION_ID)+" series end value: "+getKeyValue(endPayload.get(0), ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Load series instance id flag "+loadSeriesActionID);

		boolean flagOnDefaultLoad= getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD).equals(onPageLoad) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD).equals(onPageLoad);

		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on default "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.BY_DEFAULT_ON_PAGE_LOAD));
		LOGGER.info(Utilities.getCurrentThreadId() + "On default loading series load flag "+ flagOnDefaultLoad);

		boolean flagOnLayoutChangeLoad= getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED).equals(onLayoutChange) && 
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED).equals(onLayoutChange);
		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on layout change "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_LAYOUT_CHANGED));
		LOGGER.info(Utilities.getCurrentThreadId() + "On layout change series load flag "+ flagOnLayoutChangeLoad);

		boolean flagOnContentSelectorLoad = getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR).equals(onContentSelector) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR).equals(onContentSelector);
		LOGGER.info(Utilities.getCurrentThreadId() + "Verifying that series is loaded on content selector change "+getKeyValue(startPayload.get(0), ActionLogConstant.SERIES_LOAD_TYPE,ActionLogConstant.FROM_CONTENT_SELECTOR));
		LOGGER.info(Utilities.getCurrentThreadId() + "On content selector series load flag "+ flagOnContentSelectorLoad);

		boolean flagOnMime = getKeyValue(startPayload.get(0),ActionLogConstant.RESULT_INFO, ActionLogConstant.RESULT_SERIES_INFO,ActionLogConstant.MIME_TYPE).equals(mimeType) &&
				getKeyValue(endPayload.get(0), ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_SERIES_INFO, ActionLogConstant.MIME_TYPE).equals(mimeType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Mime type in payload" + getKeyValue(startPayload.get(0),ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_SERIES_INFO, ActionLogConstant.MIME_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Mime verification flag "+flagOnMime);

		boolean flagOnLayout = getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout) &&
				getKeyValue(endPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Layout in payload "+getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Layout verification flag "+flagOnLayout);

		return (studyIDSame && loadSeriesActionID && flagOnDefaultLoad && flagOnMime && flagOnLayout && flagOnLayoutChangeLoad && flagOnContentSelectorLoad);

	}


	public boolean verifyUnloadSeries(List<String> unloadSeriesPayload, String seriesUID,String mimeType, String viewboxNumber) throws SQLException {

		String serUID = getKeyValue(unloadSeriesPayload.get(0),ActionLogConstant.SERIES_INFO, ActionLogConstant.SERIES_INSTANCE_UID);
		boolean flagForSeriesUnloaded = serUID.equals(seriesUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Series instanceUID passed "+seriesUID+" and the series UID recieved in JSON "+serUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for series unloaded "+flagForSeriesUnloaded);

		String mimeTy = getKeyValue(unloadSeriesPayload.get(0),ActionLogConstant.SERIES_INFO, ActionLogConstant.MIME_TYPE);
		boolean flagOnMime =mimeTy.equals(mimeType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Mime Type passed "+mimeType+ " and mime type recieved in JSON"+mimeTy);
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for mime type "+flagOnMime);

		String viewboxNum = getKeyValue(unloadSeriesPayload.get(0),ActionLogConstant.VIEWBOX_NO);
		boolean flagOnViewboxNumber = viewboxNum.equalsIgnoreCase(viewboxNumber);
		LOGGER.info(Utilities.getCurrentThreadId() + "viewbox number passed "+viewboxNumber+" and recieved in JSON "+viewboxNum);
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag on viewbox number "+flagOnViewboxNumber);

		return (flagForSeriesUnloaded && flagOnMime && flagOnViewboxNumber);

	}

	public boolean verifyStudyStartAndEnd(List<String> startPayload, List<String> endPayload,String studyInstanceID, String layout, String description) {

		boolean studyInstanceIDSame = getKeyValue(startPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1).equals(studyInstanceID) &&
				getKeyValue(endPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1).equals(studyInstanceID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Start payload "+getKeyValue(startPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1)+" and end payload value "+getKeyValue(endPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Study Instance UID "+studyInstanceIDSame);


		boolean studyIDSame = getKeyValue(startPayload.get(0),ActionLogConstant.LOAD_STUDY_ACTION_ID).equals(getKeyValue(endPayload.get(0),ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Start payload value "+getKeyValue(startPayload.get(0),ActionLogConstant.LOAD_STUDY_ACTION_ID)+" end payload value "+getKeyValue(endPayload.get(0),ActionLogConstant.LOAD_STUDY_ACTION_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Study ID "+studyIDSame);

		boolean studyDescription = getKeyValue(startPayload.get(0),ActionLogConstant.STUDY_DESCRIPTION).equals(description) &&
				getKeyValue(endPayload.get(0),ActionLogConstant.STUDY_DESCRIPTION).equals(description);
		LOGGER.info(Utilities.getCurrentThreadId() + "Study description passed "+studyDescription+" and recieved in JSON "+getKeyValue(startPayload.get(0),ActionLogConstant.STUDY_DESCRIPTION));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Study description"+studyDescription);

		boolean flagOnLayout = getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout) &&
				getKeyValue(endPayload.get(0),ActionLogConstant.LAYOUT).equalsIgnoreCase(layout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Layout passed "+layout+ " and recieved in JSON "+getKeyValue(startPayload.get(0),ActionLogConstant.LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for layout defined for study  "+flagOnLayout);


		return (studyInstanceIDSame && studyIDSame && studyDescription && flagOnLayout);

	}


	public boolean verifyUnloadStudy(List<String> unloadStudyPayload, String studyUID,String description, String modalities) throws SQLException {

		boolean flagForStudyUnloaded = getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1).equals(studyUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Study UId "+studyUID+" and recieved in JSON "+getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for study Unload "+flagForStudyUnloaded);

		boolean flagOnDescription = getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.STUDY_DESCRIPTION).equals(description);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed study description "+description+" recieved in JSON "+getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.STUDY_DESCRIPTION));
		LOGGER.info(Utilities.getCurrentThreadId() + "flag on study description "+flagOnDescription);

		boolean flagOnModalities = getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.MODALITIES).equalsIgnoreCase(modalities);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed modalities "+modalities+" and recieved in JSON "+getKeyValue(unloadStudyPayload.get(0),ActionLogConstant.MODALITIES));
		LOGGER.info(Utilities.getCurrentThreadId() + "flag on modalities "+ flagOnModalities);

		return (flagForStudyUnloaded && flagOnDescription && flagOnModalities);

	}

	public boolean verifyResultStatusChange(List<String> resultStatusChange, String sopInstanceUID,String sopClassUID,String studyInstanceUID,String seriesInstanceUID,String seriesDesc,String batchUID,String mimeType, String newState,String resultType) throws SQLException {

		boolean flagForSopInstanceUID = getKeyValue(resultStatusChange.get(0),ActionLogConstant.INSTANCE_INFO,ActionLogConstant.SOP_INSTANCE_UID).equals(sopInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed SOP Instance UId "+sopInstanceUID+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.INSTANCE_INFO,ActionLogConstant.SOP_INSTANCE_UID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for SOP Instance UID "+flagForSopInstanceUID);

		boolean flagForSopClassUID = getKeyValue(resultStatusChange.get(0),ActionLogConstant.INSTANCE_INFO,ActionLogConstant.SOP_CLASS_UID).equals(sopClassUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed SOP Class UId "+sopClassUID+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.INSTANCE_INFO,ActionLogConstant.SOP_CLASS_UID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for SOP class UID "+flagForSopClassUID);

		boolean flagForResultType = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE).equals(resultType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+resultType+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForResultType);

		boolean flagForNewState = getKeyValue(resultStatusChange.get(0),ActionLogConstant.NEW_STATE).equals(newState);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed feedback "+newState+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.NEW_STATE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for feedback "+flagForNewState);

		boolean flagForSeriesInstanceUID = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO).contains(seriesInstanceUID);
		System.out.println(getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Series Instance UId "+seriesInstanceUID+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Series Instance UId "+flagForSeriesInstanceUID);

		boolean flagForStudyInstanceUID = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO).contains(studyInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Study Instance UId "+studyInstanceUID+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Study Instance UId "+flagForStudyInstanceUID);

		boolean flagForSeriesdesc = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO).contains(seriesDesc);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Series Description "+seriesDesc+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Series Description "+flagForSeriesdesc);

		boolean flagForBatchUID = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO).contains(batchUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed batch UID "+batchUID+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for batch UID "+flagForBatchUID);

		boolean flagForMimeType = getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO).contains(mimeType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Mime type "+mimeType+" and recieved in JSON "+getKeyValue(resultStatusChange.get(0),ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Mime type "+flagForMimeType);

		return (flagForSopInstanceUID && flagForSopClassUID && flagForStudyInstanceUID && flagForSeriesInstanceUID && flagForSeriesdesc && flagForBatchUID && flagForMimeType && flagForResultType && flagForNewState );





	}

	public boolean verifyFindingNavigationLogs(List<String> findingNavigation, String navigationType,String prevFindingName,String prevFindingResultType,String nextFindingName,String nextFindingResultType)  {

		boolean flagForNavigationType = getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATION_TYPE).equals(navigationType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Navigation Type "+navigationType+" and recieved in JSON "+getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATION_TYPE).equals(navigationType));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for SOP Instance UID "+flagForNavigationType);

		boolean flagForPrevFindingName = getKeyValue(findingNavigation.get(0),ActionLogConstant.PREVIOUS_RESULT_INFO,ActionLogConstant.FINDING_NAME).equals(prevFindingName);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Previous Finding Name "+prevFindingName+" and recieved in JSON "+getKeyValue(findingNavigation.get(0),ActionLogConstant.PREVIOUS_RESULT_INFO,ActionLogConstant.FINDING_NAME));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Previous Finding Name "+flagForPrevFindingName);

		boolean flagForPrevResultType = getKeyValue(findingNavigation.get(0),ActionLogConstant.PREVIOUS_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE).equals(prevFindingResultType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Previous Result Type"+prevFindingResultType+" and recieved in JSON "+getKeyValue(findingNavigation.get(0),ActionLogConstant.PREVIOUS_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Previous Result Type "+flagForPrevResultType);

		boolean flagForNextFindingName = getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATED_RESULT_INFO,ActionLogConstant.FINDING_NAME).equals(nextFindingName);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Next Finding Name "+nextFindingName+" and recieved in JSON "+getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATED_RESULT_INFO,ActionLogConstant.FINDING_NAME));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Next Finding Name "+flagForPrevFindingName);

		boolean flagForNextResultType = getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE).equals(nextFindingResultType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Next Result Type "+prevFindingResultType+" and recieved in JSON "+getKeyValue(findingNavigation.get(0),ActionLogConstant.NAVIGATED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Next Result Type"+flagForPrevResultType);

		return (flagForNavigationType && flagForPrevFindingName && flagForPrevResultType && flagForNextFindingName && flagForNextResultType);

	}

	public boolean verifyHidingGSPSResultActionLog(List<String> hidingGSPSResult, String value)  {

		boolean flagForHideGSPSResult = getKeyValue(hidingGSPSResult.get(0),ActionLogConstant.HIDE_RESULT).equals(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed hide GSPS Result as  "+value+" and recieved in JSON "+getKeyValue(hidingGSPSResult.get(0),ActionLogConstant.HIDE_RESULT).equals(value));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Hide GSPS Result"+flagForHideGSPSResult);

		return flagForHideGSPSResult;


	}

	public boolean verifySelectFindingActionLog(List<String> selectFinding, String resultType)  {

		boolean flagForResultType = getKeyValue(selectFinding.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE).equals(resultType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed  Result Type "+resultType+" and recieved in JSON "+getKeyValue(selectFinding.get(0),"selectedResultInfo",ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for  Result Type"+flagForResultType);

		return flagForResultType;

	}

	public boolean verifyFindingDropdownActionLog(List<String> findingDropdown, String findingCount)  {

		boolean flagForFindingCount = getKeyValue(findingDropdown.get(0),ActionLogConstant.FINDING_COUNTS).equals(findingCount);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Finding Count"+findingCount+" and recieved in JSON "+getKeyValue(findingDropdown.get(0),ActionLogConstant.FINDING_COUNTS));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Finding Count"+flagForFindingCount);

		return flagForFindingCount;

	}

	public boolean verifyActionLogForTraingulationToolSelection(List<String> menuSelection, String value)  {

		boolean flagForMenuSelection = getKeyValue(menuSelection.get(0),ActionLogConstant.USER_ACTION_ITEM_SELECTED).contains(value);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed hide GSPS Result as  "+value+" and recieved in JSON "+getKeyValue(menuSelection.get(0),ActionLogConstant.USER_ACTION_ITEM_SELECTED).contains(value));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Hide GSPS Result"+flagForMenuSelection);

		return flagForMenuSelection;


	}

	public boolean verifyFindingStatusChange(List<String> findingStatusChange,String studyInstanceUID,String seriesInstanceUID,String seriesDesc,String batchUID,String mimeType, String newState,String resultType) throws SQLException {

		boolean flagForResultType = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE).equals(resultType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+resultType+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_TYPE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForResultType);

		boolean flagForNewState = getKeyValue(findingStatusChange.get(0),ActionLogConstant.NEW_STATE).equals(newState);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed feedback "+newState+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.NEW_STATE));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for feedback "+flagForNewState);

		boolean flagForSeriesInstanceUID = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO).contains(seriesInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Series Instance UId "+seriesInstanceUID+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Series Instance UId "+flagForSeriesInstanceUID);

		boolean flagForStudyInstanceUID = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO).contains(studyInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Study Instance UId "+studyInstanceUID+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Study Instance UId "+flagForStudyInstanceUID);

		boolean flagForSeriesdesc = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO).contains(seriesDesc);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Series Description "+seriesDesc+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Series Description "+flagForSeriesdesc);

		boolean flagForBatchUID = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO).contains(batchUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed batch UID "+batchUID+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for batch UID "+flagForBatchUID);

		boolean flagForMimeType = getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO).contains(mimeType);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Mime type "+mimeType+" and recieved in JSON "+getKeyValue(findingStatusChange.get(0),ActionLogConstant.SELECTED_RESULT_INFO,ActionLogConstant.RESULT_INFO,ActionLogConstant.RESULT_SERIES_INFO));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Mime type "+flagForMimeType);

		return (flagForStudyInstanceUID && flagForSeriesInstanceUID && flagForSeriesdesc && flagForBatchUID && flagForMimeType && flagForResultType && flagForNewState );





	}

	public boolean verifyActionLogForSaveLayout(List<String> saveLayout,String oldLayout,String newLayout,String username, String studyInstanceUID,String withOrWithoutContent) throws SQLException {

		boolean flagForOldLayout = getKeyValue(saveLayout.get(0),ActionLogConstant.OLD_LAYOUT).equals(oldLayout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+oldLayout+" and recieved in JSON "+getKeyValue(saveLayout.get(0),ActionLogConstant.OLD_LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForOldLayout);

		boolean flagForNewLayout = getKeyValue(saveLayout.get(0),ActionLogConstant.NEW_LAYOUT).equals(newLayout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+newLayout+" and recieved in JSON "+getKeyValue(saveLayout.get(0),ActionLogConstant.NEW_LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForNewLayout);

		boolean flagForUserName = getKeyValue(saveLayout.get(0),ActionLogConstant.USERNAME).equals(username);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+username+" and recieved in JSON "+getKeyValue(saveLayout.get(0),ActionLogConstant.USERNAME));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForUserName);

		boolean flagForStudyInstanceUID = getKeyValue(saveLayout.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1).equals(studyInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+studyInstanceUID+" and recieved in JSON "+getKeyValue(saveLayout.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForStudyInstanceUID);

		boolean flagForWithOrWithoutContent = getKeyValue(saveLayout.get(0),ActionLogConstant.WITH_CONTENT).equals(withOrWithoutContent);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+withOrWithoutContent+" and recieved in JSON "+getKeyValue(saveLayout.get(0),ActionLogConstant.WITH_CONTENT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForWithOrWithoutContent);


		return flagForOldLayout && flagForNewLayout && flagForUserName && flagForStudyInstanceUID && flagForWithOrWithoutContent ;


	}

	public boolean verifyActionLogForResetLayout(List<String> resetLayout,String oldLayout,String newLayout,String username, String studyInstanceUID,String machineID) throws SQLException {

		boolean flagForOldLayout = getKeyValue(resetLayout.get(0),ActionLogConstant.OLD_LAYOUT).equals(oldLayout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+oldLayout+" and recieved in JSON "+getKeyValue(resetLayout.get(0),ActionLogConstant.OLD_LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForOldLayout);

		boolean flagForNewLayout = getKeyValue(resetLayout.get(0),ActionLogConstant.NEW_LAYOUT).equals(newLayout);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+newLayout+" and recieved in JSON "+getKeyValue(resetLayout.get(0),ActionLogConstant.NEW_LAYOUT));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForNewLayout);

		boolean flagForUserName = getKeyValue(resetLayout.get(0),ActionLogConstant.USERNAME).equals(username);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+username+" and recieved in JSON "+getKeyValue(resetLayout.get(0),ActionLogConstant.USERNAME));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForUserName);

		boolean flagForStudyInstanceUID = getKeyValue(resetLayout.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1).equals(studyInstanceUID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+studyInstanceUID+" and recieved in JSON "+getKeyValue(resetLayout.get(0),ActionLogConstant.STUDY_INSTANCE_UID_1));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForStudyInstanceUID);

		boolean flagForMachineID = getKeyValue(resetLayout.get(0),ActionLogConstant.MACHINE_ID).equals(machineID);
		LOGGER.info(Utilities.getCurrentThreadId() + "Passed Result Type "+machineID+" and recieved in JSON "+getKeyValue(resetLayout.get(0),ActionLogConstant.MACHINE_ID));
		LOGGER.info(Utilities.getCurrentThreadId() + "Flag for Result Type "+flagForMachineID);


		return flagForOldLayout && flagForNewLayout && flagForUserName && flagForStudyInstanceUID && flagForMachineID  ;


	}


	public boolean verifyActionForPlaneChange(String selectedItem, String planeBeforeChng, String originalPlane, String scrollPos) throws SQLException {
		

		List<String>  startPayload = getPayload(ActionLogConstant.PLANE_CHANGE);
		String itemSeleted = getKeyValue(startPayload.get(0),ActionLogConstant.NEW_PLANE);
		String original = getKeyValue(startPayload.get(0),ActionLogConstant.DEFAULT_PLANE);
		String scroll = getKeyValue(startPayload.get(0),ActionLogConstant.SCROLL_POSITION);

		boolean status = convertIntoInt(itemSeleted).equals(ViewerPageConstants.ALL_PLANES.indexOf(ViewerPageConstants.PLANES.get(selectedItem)));
		status = status && convertIntoInt(original).equals(ViewerPageConstants.ALL_PLANES.indexOf(ViewerPageConstants.PLANES.get(originalPlane)));
		status = status && scroll.equalsIgnoreCase(scrollPos);
		
		return status;
	}


}
