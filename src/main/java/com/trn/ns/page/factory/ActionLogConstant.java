package com.trn.ns.page.factory;

public class ActionLogConstant {




	public static final String ONE_BY_ONE_LAYOUT="1X1";
	public static final String TWO_BY_ONE_LAYOUT="2x1";
	public static final String ONE_BY_TWO_LAYOUT="1X2";
	
	public static final String TWO_BY_TWO_LAYOUT="2X2";
	public static final String THREE_BY_THREE_LAYOUT="3X3";
	public static final String THREE_BY_TWO_LAYOUT="3x2";

	public static final String ONE_BY_ONE_VIEWBOXNO="1,1";
	public static final String TWO_BY_ONE_VIEWBOXNO="2,1";
	public static final String THREE_BY_ONE_VIEWBOXNO="3,1";
	public static final String ONE_BY_TWO_VIEWBOXNO="1,2";
	public static final String TWO_BY_TWO_VIEWBOXNO="2,2";
	public static final String THREE_BY_TWO_VIEWBOXNO="3,2";
	public static final String ONE_BY_THREE_VIEWBOXNO="1,3";
	public static final String TWO_BY_THREE_VIEWBOXNO="2,3";
	public static final String THREE_BY_THREE_VIEWBONO="3,3";




	// Database NsUsageAnalytics Payload key

	public static final String INSTANCE_INFO="instanceInfo";

	public static final String LOAD_STUDY_ACTION_ID="loadStudyActionId";

	public static final String LOAD_SERIES_ACTION_ID="loadSeriesActionId";

	public static final String VIEWBOX_NO="viewboxNo";

	public static final String LAYOUT="layout"; 

	public static final String STUDY_INSTANCE_UID="studyInstanceUid"; 
	public static final String STUDY_INSTANCE_UID_1="studyInstanceUID"; 

	public static final String SERIES_INSTANCE_UID="seriesInstanceUID"; 

	public static final String SERIES_DESCRIPTION_DB="seriesDescription"; 

	public static final String MACHINE_UID="machineUID";

	public static final String BATCH_UID="batchUID";

	public static final String MODALITY_DB="modality";
	public static final String MODALITIES = "modalities";
	
	public static final String MIME_TYPE="mimeType";

	public static final String IS_MULTIPhase="isMultiPhase";

	public static final String SOP_INSTANCE_UID="sopInstanceUID";

	public static final String SOP_CLASS_UID="sopClassUID";

	public static final String STATUS="status";

	public static final String ERROR_MESSAGE="errorMessage";

	public static final String SERIES_LOAD_TYPE= "seriesLoadType";
	public static final String BY_DEFAULT_ON_PAGE_LOAD="byDefaultOnPageLoad";
	public static final String FROM_CONTENT_SELECTOR="fromContentSelector";
	public static final String FROM_LAYOUT_CHANGED="whenLayoutChanged";
	public static final String FROM_OUTPUT_PANEL="fromOutputPanel";
	

	public static final String RESULT_INFO= "resultInfo";
	public static final String SERIES_INFO= "seriesInfo";	
	public static final String RESULT_SERIES_INFO= "resultSeriesInfo";
	public static final String SELECTED_RESULT_INFO="selectedResultInfo";

	public static final String USER_ACTION_LOAD_SERIES_START= "loadSeriesStart";
	public static final String USER_ACTION_LOAD_SERIES_END= "loadSeriesEnd";
	public static final String USER_ACTION_MENU_SELECTION= "menuSelection";
	public static final String USER_ACTION_ITEM_SELECTED= "itemSelected";

	public static final String USER_ACTION_LOAD_STUDY_START= "loadStudyStart";
	public static final String USER_ACTION_LOAD_STUDY_END= "loadStudyEnd";
	public static final String USER_ACTION_RESULT_STATUS_CHANGE= "resultStatusChange";
	public static final String USER_ACTION_FINDING_STATUS_CHANGE= "findingStatusChange";
	public static final String USER_ACTION_THUMBNAIL_JUMP= "thumbnailJump";
	public static final String USER_ACTION_LOGIN_END= "loginEnd";
	
	public static final String USER_ACTION_TABLE = "UserActionLog";
	
	public static final String APPLICATION_DICOM ="application/dicom";
	public static final String APPLICATION_PDF="application/pdf";
	public static final String APPLICATION_SR="application/sr";
	public static final String IMAGE_JPEG="image/jpeg";
	public static final String IMAGE_PNG="image/png";
	

	public static final String UNLOAD_SERIES= "unloadSeries";
	public static final String UNLOAD_STUDY = "unloadStudy";
	
	public static final String STUDY_DESCRIPTION="studyDescription";
	

	public static final String SR_MODALITY ="SR";
	public static final String IS_SERIES_LOADED= "isSeriesLoaded";
	public static final String NEW_STATE="newState";
	public static final String FINDING_NAVIGATION ="findingNavigation";
	public static final String NAVIGATION_TYPE ="navigationType";
	public static final String FINDING_NAME ="findingName";
	public static final String RESULT_TYPE ="resultType";
	public static final String NAVIGATED_RESULT_INFO="navigatedResultInfo"; 
	public static final String PREVIOUS_RESULT_INFO="previousResultInfo";
	public static final String GSPS="Gsps";
	public static final String GSPS_PREV="Pre";
	public static final String HIDE_RESULT="hideResult";
	public static final String USER_ACTION_HIDING_GSPS_RESULT="HideGSPSResults";
	public static final String FINDING_COUNTS="totalFindings";
	public static final String USER_ACTION_FINDINGS_DROPDOWN="openFindingsDropdown";
	public static final String USER_ACTION_SELECT_FINDING="selectFinding";
	
	//modality and Result Type
	public static final String SC_RESULT_TYPE="DicomSC";
	public static final String SR_RESULT_TYPE="DicomSR";
	public static final String SC_MODALITY="OT";
	public static final String CAD_SR_RESULT_TYPE="MammoCadSr";
	
	public static final String JUMPED_VIEWBOX_INFO = "jumpedViewboxInfo";
	
	//user action log for save layout
	public static final String USER_ACTION_FOR_SAVE_LAYOUT = "saveLayout";
	public static final String USER_ACTION_FOR_RESET_LAYOUT = "resetLayout";
	public static final String OLD_LAYOUT="oldLayout";
	public static final String NEW_LAYOUT="newLayout";
	public static final String WITH_CONTENT="withContent";
	public static final String USERNAME="userName";
	public static final String MACHINE_ID="machineId";
	
	public static final String IS_MFA_ENABLED="isMfaEnabled";
	public static final String USER_ACTION_FOR_ZOOM="zoom";
	public static final String USER_ACTION_FOR_PAN="pan";
	public static final String USER_ACTION_FOR_WINDOWING="windowing";
	public static final String USER_ACTION_FOR_PAGE_NAVIGATION="pageNavigationEnd";
	public static final String SUB_MENU="subMenu";
	public static final String PLANE_CHANGE="planeChange";
	public static final String NEW_PLANE="newPlane";
	public static final String DEFAULT_PLANE="defaultPlane";
	public static final String SCROLL_POSITION="scrollPosition";
	
}
