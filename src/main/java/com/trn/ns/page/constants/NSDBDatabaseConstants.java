package com.trn.ns.page.constants;

import java.util.LinkedHashMap;
public final class NSDBDatabaseConstants {



	// WWWL SYNC MODE

	public static final String WWWL_SYNC_RELATIVE = "RELATIVE";
	public static final String WWWL_SYNC_ABSOLUTE = "ABSOLUTE";

	//Database logging parameter
	public static final String NS_LOG_TABLE = "Log";

	//Log table column name
	public static final String LOGID_COLUMN ="LogID";
	public static final String DATE_COLUMN ="DateTime";
	public static final String ACCOUNTID_COLUMN ="AccountID";
	public static final String SERVICEID_COLUMN ="ServiceID";
	public static final String WORKFLOWID_COLUMN ="WorkFlowID";
	public static final String ERRORLEVELID_COLUMN ="ErrorLevelID";
	public static final String MESSAGE_COLUMN ="Message";
	public static final String EXECPTION_COLUMN ="Exception";
	public static final String STACK_TRACE_COLUMN ="StackTrace";

	public static final String LOGENTRYDATE = "date";
	public static final String LOGENTRYUSER	= "user";
	public static final String LOGENTRYARCHIVE = "archive";
	public static final String LOGENTRYSTATUSREJECT = "reject";
	public static final String LOGENTRYSTATUSACCEPT = "accept";

	//logging service name
	public static final String LAYOUT_SERVICE ="LayoutService";
	public static final String STUDY_SERVICE ="StudyService";
	public static final String SETTING_SERVICE ="SettingsService";
	public static final String AUTHENTICATION_SERVICE ="AuthenticationService";
	public static final String CONFIG_SERVICE ="ConfigurationService";
	public static final String WIAGATE_SERVICE ="WiaGateAccessService";

	//Sync toggle
	public static final String SYNC_OFF ="false";
	public static final String SYNC_ON ="true";


	//UserAccount table contents
	public static final int ADMINUSER = 1 ;
	public static final int NONADMINUSER = 0 ;

	//DB Errors 

	public static final String FOREIGNS_KEY_CONSTRAINT ="conflicted with the FOREIGN KEY constraint";
	public static final String UNIQUE_KEY_CONSTRAINT ="Violation of UNIQUE KEY constraint";
	public static final String PRIMARY_KEY_CONSTRAINT ="Violation of PRIMARY KEY constraint";

	public static final String BATCH_MACHCINEID = "BatchMachineID";
	public static final String BATCH_ID = "BatchID";
	
	public static final LinkedHashMap<String, String> ACTION_BUTTONID = new LinkedHashMap<String, String>();

	static {
		ACTION_BUTTONID.put("1", ViewerPageConstants.PAN);
		ACTION_BUTTONID.put("2", ViewerPageConstants.SCROLL);
		ACTION_BUTTONID.put("3", ViewerPageConstants.WINDOWING);
		ACTION_BUTTONID.put("4", ViewerPageConstants.ZOOM);
	}

	public static final String INVALID_ACTION_NAME_ERROR="Invalid Configuration: All viewer actions are not configured in dbo.EventMap. Default configurations Scroll= left, Pan = right, Windowing = leftright, and Zoom = center have been configured" ;

	public static final String WIARESULTTABLE ="WiaResultElements";
	public static final String USERFEEDBACKTABLE="UserFeedback";
	public static final String USERFEEDBACKPREFERENCESTABLE="UserFeedbackPreferences";


	public static final String SERIES_LEVEL="SeriesLevel";
	public static final String SERIES_LEVEL_ID="SeriesLevelID";


	public static final String GSPS_TABLE_PRESENTATION_CREATION_DATE="PresentationCreationDate";
	public static final String GSPS_TABLE_PRESENTATION_CREATION_TIME="PresentationCreationTime";

	public static final String COPYFILTER_TABLE="CopyFilter";
	public static final String COPYFILTER_ID_COLUMN="CopyFilterId";
	public static final String USERACCOUNT_ID_COLUMN="UserAccountId";
	public static final String COPYFILTERS_COLUMN="CopyFilters";

	public static final String  PMAP_DB_MAX_COLUMN_NAME= "RealWorldValueLastValueMapped";
	public static final String  PMAP_DB_MIN_COLUMN_NAME= "RealWorldValueFirstValueMapped";

	public static final String SEND_TO_PACS_LOG="SendUserFeedbackToEnvoy";


	// View box sync value
	public static final String DICOM_ONLY ="DicomOnly";
	public static final String ALL="All";
	public static final int DEFAULT_DATA_LOCK_EXPIRE_TIME = 5;
	public static final String KEY_SHADOW_COLOR="ShadowColor";
	public static final String PATIENT_LEVEL_ID="PatientLevelID";
	public static final String MACHINE_NAME="Machine";

	public static final String INVALID_CONFIGURATION_ERROR_MSG="Invalid Configuration";
	public static final String DEFAULT_SYNC_MODE ="DefaultSyncMode";

	public static final String USER_DEFINED_MACHINE ="northstar-user-defined";

	public static final String MAXIMUM_CACHE_SIZE_LIMIT = "MaxCacheSizeLimit";
	public static final String MAXIMUM_CACHE_SIZE_100 = "100";
	public static final String MAXIMUM_CACHE_SIZE_1_GB = "1000000000";

	public static final String RECENTSEARCH="RecentSearch"; 
 
	//DICOM tag value for context Info tag
	public static final String CONTEXT_INFO_TAG="ContextInfoTag";
	public static final String CONTEXT_INFO_TAG_VALUE="0079,10E1";
	public static final String CONTEXT_INFO="ryan@fromorbit.net";
	public static final String CONTEXT_INFO_ENABLED="ContextInfoEnabled";
	
	//PMAP Storage table
	public static final String LUT_ENTRIES_COUNT="lutEntriesCount";
	public static final String LUT_FIRST_INPUT_VALUE="lutFirstInputValue";
	public static final String PMAP_LOOKUP_TABLE="pmapLookupTable";

	public static final String MFA_COLUMN_NAME="MfaSecretKey";
	public static final String USER_ACCOUNT_TABLE="UserAccount";
	

	public static final String  DISPLAY_TABLE_USER_PREF = "DisplayTableUserPreferences";
	public static final String DESKTOP_RENDERING_MODE="DesktopRenderingMode";
	public static final String MOBILE_RENDERING_MODE="MobileRenderingMode";
	
	// Recently Viewed Patient table
	public static final String RECENTLY_VIEWED_PATIENT_TABLE="RecentlyViewedPatient";
	public static final String STUDY_LEVEL_ID_COLUMN="StudyLevelId";
	public static final String ACCOUNT_ID_COLUMN="AccountId";
	public static final String ID_COLUMN="Id";
	public static final String VIEWED_DATE_TIME_COLUMN="ViewedDateTime";
	

	public static final String DEVMODE="DevMode";
	public static final String MAXVOLIDLETIME="MaxVolumeIdleTime";
	
	public static final String SOP_CLASS_UID_COLUMN = "SOPClassUID";
	public static final String FRAME_NUM_COLUMN = "FrameNumber";
	public static final String PIXEL_SPACING_ROW = "PixelSpacingRow";
	public static final String PIXEL_SPACING_COL = "PixelSpacingCol";
	public static final String ROWS = "Rows";
	public static final String COLUMNS = "Columns";
	public static final String IMAGENUMBER = "ImageNumber";
	
	public static final String WINDOWCENTER_COL = "WindowCenter";
	public static final String WINDOWWIDTH_COL = "WindowWidth";
	public static final String USERANNOTATION_TABLE = "UserAnnotation";
	
	
	// plugin constants 
	
	public static final String CREATE_PLUGIN_EVENT = "CreatePlugIn";
	public static final String VOLUME = "volumes";
	
	
}


