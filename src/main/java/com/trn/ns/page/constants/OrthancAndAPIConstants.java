package com.trn.ns.page.constants;

import com.trn.ns.test.configs.Configurations;

public final class OrthancAndAPIConstants {

	public static final String API_BASE_URL = "http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort");
	public static final String API_DATA_DELETE_BASE_URL = API_BASE_URL+"/api/Data";
	public static final String API_WIA_DELETE_BASE_URL = API_BASE_URL+"/api/wia";
	public static final String ORTHANC_PORT="8042";

	public static final String PLUGIN_PORT = "53790";
	public static final String PLUGIN_URL = "plugins";
	public static final String PLUGIN_BASE_URL = "http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+PLUGIN_PORT;
	public static final String PLUGIN_NAME = "test";
	
	
	
	
	public static final String ORTHANC_BASE_URL = "http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+ORTHANC_PORT;

	public static final String PATIENT_ORTHANC_URL="patients";
	public static final String STUDY_ORTHANC_URL="studies";
	public static final String SERIES_ORTHANC_URL="series";
	public static final String INSTANCES_ORTHANC_URL="instances";
	public static final String ORTHANC_CONTENT_TAG ="content";

	//ORTHANC RESPONSE Key
	public static final String ORTHANC_PATIENT_NAME_TAG ="MainDicomTags.PatientName";
	public static final String ORTHANC_PATIENT_ID_TAG ="MainDicomTags.PatientID";
	public static final String ORTHANC_SERIES_DESCRIPTION_TAG ="MainDicomTags.SeriesDescription";
	public static final String ORTHANC_MODALITY_TAG ="MainDicomTags.Modality";
	public static final String ORTHANC_STUDY_DESCRIPTION_TAG ="MainDicomTags.StudyDescription";


	public static final String STUDY_ORTHANC_TAG="Studies";
	public static final String SERIES_ORTHANC_TAG="Series";
	public static final String INSTANCES_ORTHANC_TAG="Instances";
	public static final String ORTHANC_SIMPLIFIED_TAG_URL ="/simplified-tags";

	public static final String ORTHANC_SIMPLIFIED_STUDY_DESC_TAG="StudyDescription";
	public static final String ORTHANC_SIMPLIFIED_SERIES_DESC_TAG="SeriesDescription";
	public static final String ORTHANC_SIMPLIFIED_PATIENT_NAME_TAG="PatientName";


	public static final String ORTHANC_ITEMS_TAG ="0070-0001/0/0070-0009";
	public static final String ORTHANC_TEXTANNOTATION_TAG="0070-0001/0/0070-0008";
	public static final String ORTHANC_TEXT_VALUE_TAG ="0070,0006";
	public static final String ORTHANC_FINDING_NAME_TAG ="0070-0023";
	public static final String ORTHANC_USERNAME_TAG ="0070,0084";
	public static final String ORTHANC_PR_VALUE="PR";
	public static final String ORTHANC_SR_VALUE="SR";
	public static final String ORTHANC_RT_VALUE="RTSTRUCT";
	public static final String ORTHANC_OT_VALUE="OT";
	public static final String ORTHANC_CT_VALUE="CT";
	public static final String ORTHANC_XA_VALUE="XA";
	public static final String ORTHANC_NM_VALUE="NM";
	public static final String ORTHANC_CR_VALUE="CR";
	public static final String ORTHANC_MG_VALUE="MG";
	public static final String ORTHANC_DX_VALUE="DX";
	public static final String ORTHANC_PT_VALUE="PT";
	public static final String ORTHANC_MR_VALUE="MR";
	public static final String ORTHANC_INSTANCES_SERIES_DESCRP_TAG ="0008-103e";
	public static final String ORTHANC_TEXT_ANN_VAL ="UnformattedText";

	public static final String SUCCESS_API_CODE="200";
	public static final String UNAUTHORIZED_API_CODE="401";
	public static final String CONFLICT_API_CODE="409";
	public static final String NOT_FOUND_API_CODE="404";
	public static final String BAD_REQUEST_CODE="400";

	public static final String TOKEN_URL = "token";
	public static final String HEADER_KEY = "Authorization";


	public static final String DELETE_PATIENT_404_ERROR_MESSAGE = "Patient does not exist";
	public static final String DELETE_STUDY_404_ERROR_MESSAGE = "Study does not exist";
	public static final String DELETE_SERIES_404_ERROR_MESSAGE = "Series does not exist";
	public static final String DELETE_BATCH_404_ERROR_MESSAGE = "Batch does not exist";

	public static final String DELETE_PATIENT_409_ERROR_MESSAGE = "Unable to delete patient. A study is being locked";
	public static final String DELETE_SERIES_409_ERROR_MESSAGE = "Unable to delete series. The series belongs to a study being locked";
	public static final String DELETE_STUDY_409_ERROR_MESSAGE = "Unable to delete study. Study is being locked";
	public static final String DELETE_BATCH_409_ERROR_MESSAGE = "Unable to delete batch. The study belonging to the batch is locked";

	public static final String DELETE_401_ERROR_MESSAGE = "The token is expired";

	public static final String INTEGRATION_TYPE_URL = "?integrationType=";
	public static final String THEME_URL = "&theme=";

	public static final String PATIENT_ID_FOR_API="patientId";
	public static final String ISSUER_OF_PATIENT_ID="issuerOfPatientId";

	//Findings Report response Variable

	public static final String BATCHID ="\"\".batch-id";	
	public static final String BATCHID_UID =BATCHID+".Uid";
	public static final String BATCHID_STATUS =BATCHID+".Status";	
	public static final String BATCHID_FINISHEDAT =BATCHID+".FinishedAt";

	public static final String MACHINE_ID ="\"\".machine-id";
	public static final String MACHINEID_INFO =MACHINE_ID+".info";

	public static final String RESULT_SUMMARY ="\"\".result-summary";
	public static final String RESULT_SUMMARY_CREATION_DATE =RESULT_SUMMARY+".creation-date";
	public static final String RESULT_SUMMARY_UPDATED_STATE =RESULT_SUMMARY+".last-updated-date";
	public static final String RESULT_SUMMARY_LAST_UPDATED_USER =RESULT_SUMMARY+".last-updated-user";

	public static final String RESULT_SUMMARY_FINDING_STATE =RESULT_SUMMARY+".finding-latest-state";
	public static final String RESULT_SUMMARY_FINDING_STATE_GSPS =RESULT_SUMMARY_FINDING_STATE+".gsps";	

	public static final String RESULT_SUMMARY_FINDING_STATE_GSPS_ACCEPTED =RESULT_SUMMARY_FINDING_STATE_GSPS+"."+ViewerPageConstants.ACCEPTED_TEXT.toLowerCase();
	public static final String RESULT_SUMMARY_FINDING_STATE_GSPS_REJECTED =RESULT_SUMMARY_FINDING_STATE_GSPS+"."+ViewerPageConstants.REJECTED_TEXT.toLowerCase();
	public static final String RESULT_SUMMARY_FINDING_STATE_GSPS_PENDING =RESULT_SUMMARY_FINDING_STATE_GSPS+"."+ViewerPageConstants.PENDING_TEXT.toLowerCase();

	public static final String NULL_STRING ="null";

	public static final String API_DATA_GET_BASE_URL="/api/findingsReport";
	public static final String DONE="Done";
	public static final String FINDINGS_SUMMARY="finding-summary";
	public static final String ACCESSION_NUMBER="accessionNumber";


	public static final String INVALID_ACCESSION_NO_ERROR_MESSAGE="Invalid URL(Accession Number)";
	public static final String ERROR_MESSAGE_WHEN_PARAMETERS_NOT_UNIQUE="Multiple patients found for search criteria";
	public static final String ERROR_MESSAGE_FOR_INCORRECT_PARAMETERS="No patient found for search criteria";
	public static final String ERROR_MESSAGE_WHEN_NO_FINDING_SUMMARY_AVAILABLE="There are no findings for given search criteria";
	public static final String ERROR_MESSAGE_WHEN_PARAMETERS_NOT_PROVIDED="Invalid URL(Parameters are not provided)";
	public static final String PATIENT_LEVEL_ID="PatientLevelID";
	public static final String MACHINE_NAME="Machine";
	public static final String STUDY_UID_FOR_API="studyuid";
	public static final String SERIES_UID_FOR_API="seriesuid";

	// password Encryption -	
	public static final String NETWORK_METHOD = "Network.requestWillBeSent";
	public static final String PARAMS = "params";
	public static final String REQUEST = "request";
	public static final String URL = "url";
	public static final String MESSAGE = "message";
	public static final String METHOD = "method";
	public static final String POST_DATA ="postData";
	public static final String AUTHENTICATE = "authenticate";
	public static final String HEADER = "headers";
	public static final String USERS = "/Users/";
	public static final String  PASSWORD_IS_ENCRYPTED="GetToken: password is encrypted";
	public static final String NETWORK_METHOD_2 = "Network.requestWillBeSentExtraInfo";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_ENCODING_VAL = "gzip, deflate, br";
	
	


	// Liasion URLS 
	public static final String WIA_GATE_ACCESS_PORT="60050";
	public static final String WIA_GATE_ACCESS_URL ="http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+WIA_GATE_ACCESS_PORT;
	public static final String NEW_STUDY_URL = "/studies/new-study";
	public static final String NEW_RESULT_URL = "/results/new-result";
	public static final String NEW_BATCH_URL = "/batch/new-batch";
	public static final String NEW_BATCH_RESULT_URL = "/results/new-batch-result";




	//reset password API
	public static final String RESET_PASSWORD_API="/users/resetpassword";
	public static final String CREATE_USER_API="/users/newuser";
	public static final String CONTENT_TYPE="Content-Type";
	public static final String APPLICATION_JSON="application/json";

	public static final String ERROR_MESSAGE_WHEN_USERNAME_NOT_PROVIDED="Username cannot be empty";
	public static final String ERROR_MESSAGE_WHEN_USERNAME_NOT_CREATED="User doesn't exist";
	public static final String ERROR_MESSAGE_WHEN_LENGTH_OF_PASSWORD_IS_MORE="Password Does Not Meet Requirements";
	public static final String STATUS_CODE_400="400";

	// update users 
	public static final String UPDATE_USER_URL = "/users/updateUser";
	public static final String UPDATE_FIELD_TYPE = "UpdateFieldType";
	public static final String TOKEN_EXPIRED = "Lifetime validation failed. The token is expired.";
	public static final String ERROR_TAG = "error";
	public static final String ERROR_LEVEL = "errorLevel";
	public static final String API_JSON_FOLDER_PATH = "./src/test/resources/com/testdata/API/" ;


	// pmap 

	public static final String PMAP_BASE_URL ="http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":63820";
	public static final String PMAP_URL ="/pmap/";


	// Orthanc Tags 
	
	public static final String DisplayedAreaSelectionSequence = "DisplayedAreaSelectionSequence";
	public static final String SoftcopyVOILUTSequence = "SoftcopyVOILUTSequence";
	public static final String DisplayedAreaTopLeftHandCorner_key = "DisplayedAreaTopLeftHandCorner";
	public static final String DisplayedAreaBottomRightHandCorner_key = "DisplayedAreaBottomRightHandCorner";

	public static final String SoftcopyVOILUTSequence_tag = "/0028-3110";
	public static final String ReferencedImageSequence="/0008-1140";
	public static final String ReferencedSOPClassUID ="/0008-1150";
	public static final String ReferencedSOPInstanceUID ="/0008-1155";
	public static final String ReferencedFrameNumber ="/0008-1160";

	public static final String WindowCenter ="/0028-1050";
	public static final String WindowWidth ="/0028-1051";
	public static final String VOILUTFunction ="/0028-1056";
	public static final String VOILUTFunction_val = "LINEAR";

	public static final String DisplayedAreaSelectionSequence_tag ="/0070-005a";
	public static final String PixelOriginInterpretation = "/0048-0301";
	public static final String PixelOriginInterpretation_val = "FRAME";
	public static final String DisplayedAreaTopLeftHandCorner ="/0070-0052" ;
	public static final String DisplayedAreaBottomRightHandCorner ="/0070-0053";
	public static final String PresentationSizeMode ="/0070-0100";
	public static final String PresentationPixelSpacing ="/0070-0101";
	public static final String PresentationSizeMode_val ="SCALE TO FIT";
	
	public static final String ReferencedSeriesSequence ="/0008-1115";
	public static final String SeriesInstanceUID = "/0020-000e"; 
	
	
	//plugin Constants
	
	public static final String PLUGIN_TYPE = "PlugIn";	
	public static final String PLUGIN_DLL = "TeraRecon.Explorer.PlugIn.dll";
	
	
	public static final String AUTHORIZATION_KEY = "Authorization";
	public static final String LAYOUT_METHOD = "GetDefaultLayout";
	
	public static final String PLUGIN_SERVICEID = "PlugInService";
	public static final String PLUGIN_STARTED_MSG = "Plug in has started.";
	
	
	public static String EXECUTE_COMMAND_ASYNC(String guid) {
		return "ExecuteCommandAsync("+guid+") called.";
	}
	
	public static String DESTROY_PLUGIN(String guid) {
		return "DestroyPlugIn("+guid+") called.";
	}

	public static String CREATE_PLUGIN(String pluginName) {
		return "CreatePlugIn("+pluginName+") called.";
	}
	
	public static String NON_SUPPORTED_PLUGIN(String guid,String pluginName) {
		return "{\"appErrCode\":0,\"statusCode\":400,\"devMsg\":\"Plugin "+guid+" does not support command '"+pluginName+"'.\"}";
	}
	
	public static String PLUGIN_INSTANCE_NOT_FOUND ="{\"appErrCode\":0,\"statusCode\":404,\"devMsg\":\"Plug-in instance not found.\\r\\nParameter name: plugInHandle\"}";
	
}


