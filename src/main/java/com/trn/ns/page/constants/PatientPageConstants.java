package com.trn.ns.page.constants;

import java.util.ArrayList;
import java.util.HashMap;
public final class PatientPageConstants {


	public static final String PATIENTID="PatientID";
	public static final String SEX="Sex";
	public static final String DATEOFBIRTH="Date of Birth";
	public static final String AGE="Age";
	public static final String CURRENTLOCATION="Current Location:";
	public static final String PRIMARYLOCATION="Primary Location:";
	public static final String SITE="Site:";
	public static final String RACE="Race";
	public static final String CLASS="Class:";


	// Single patient Study table header 

	public static final String ACESSION_NUMBER="Accession #";
	public static final String STUDY_DESCRPTION = "Study Description";
	public static final String AI_PLATFORM = "Eureka AI";
	public static final String NUMBER_OF_IMAGES = "# Images";
	public static final String MODALITY = "Modality";
	public static final String STUDY_DATE_TIME = "Study Date & Time";
	public static final String REFERING_PHYSICIAN = "Referring Physician";
	public static final String INSTITUTION_NAME = "Institution Name";
	public static final String PRIORITY = "Priority";
	public static final String UP_ARROW = "arrow-up";
	public static final String DOWN_ARROW = "arrow-down";
	public static final String DOB = "DOB";


	// background color
	public static final String ODD_ROWS_BACKGROUND_COLOR ="#141414";
	public static final String EVEN_ROWS_BACKGROUND_COLOR ="#000000";


	//Study list contents
	public static final String PATIENTID_TEXT = "Patient ID";
	public static final String LOADING_TEXT = "Loading";


	// Theme Value
	public static final String FONT_SIZE="15px";
	public static final String DARK_THEME ="dark";
	public static final String LIGHT_THEME ="light";
	public static final String LIGHT_THEME_HEADER_FONT_FAMILY ="SourceSansPro-Regular";
	public static final String LIGHT_THEME_ROW_FONT_FAMILY ="SourceSansPro-Light";
	public static final String LIGHT_THEME_HEADER_COLOR ="#140b0a";
	public static final String LIGHT_THEME_ROW_COLOR ="#5c5653";

	public static final String DARK_THEME_HEADER_FONT_COLOR ="#a7a7a7";
	public static final String DARK_THEME_HEADER_FONT_FAMILY="SourceSansPro-Regular";
	public static final String DARK_THEME_ROW_FONT_COLOR ="#dedede";
	public static final String DARK_THEME_ROW_FONT_FAMILY ="SourceSansPro-Light";
	public static final String HIGHLITHED_COLUMN_CLASS = "columnWordWrap highlight";
	public static final String DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR = "#389bfd";
	public static final String DARK_THEME_HIGHLIGHTED_ROW_COLOR = "#282828";
	public static final String DARK_THEME_BORDER_ROW_COLOR = "#2d2d2d";
	public static final String DARK_THEME_HEADER_TEXTANDVERSION_COLOR = "#f2f2ee";
	public static final String DARK_THEME_BACKGROUND_COLOR = "#000000";
	public static final String DARK_THEME_LABEL_COLOR = "#8e8e8e";


	public static final ArrayList<String> PATIENT_PAGE_COLUMN_HEADERS = new ArrayList<String>();
	static{
		PATIENT_PAGE_COLUMN_HEADERS.add("Patient ID");
		PATIENT_PAGE_COLUMN_HEADERS.add("Patient Name");
		PATIENT_PAGE_COLUMN_HEADERS.add("Sex");
		PATIENT_PAGE_COLUMN_HEADERS.add("DOB");
		PATIENT_PAGE_COLUMN_HEADERS.add("Acquisition Date");
		PATIENT_PAGE_COLUMN_HEADERS.add("Modality");
		PATIENT_PAGE_COLUMN_HEADERS.add("Site");
		PATIENT_PAGE_COLUMN_HEADERS.add("Machine");
	}
	
	public static final HashMap<String,Boolean> Patient_Header_Checkbox = new HashMap<String, Boolean>();
	static{
		Patient_Header_Checkbox.put("Patient ID",false);
		Patient_Header_Checkbox.put("Patient Name",false);
		Patient_Header_Checkbox.put("Sex",true);
		Patient_Header_Checkbox.put("DOB",true);
		Patient_Header_Checkbox.put("Acquisition Date",false);
		Patient_Header_Checkbox.put("Modality",true);
		Patient_Header_Checkbox.put("Site",true);
		Patient_Header_Checkbox.put("Machine",true);
	}
	
	public static final ArrayList<String> STUDY_PAGE_COLUMN_HEADERS = new ArrayList<String>();
	static{
		STUDY_PAGE_COLUMN_HEADERS.add("Accession #");
		STUDY_PAGE_COLUMN_HEADERS.add("Study Description");
		STUDY_PAGE_COLUMN_HEADERS.add("Eureka AI");
		STUDY_PAGE_COLUMN_HEADERS.add("# Images");
		STUDY_PAGE_COLUMN_HEADERS.add("Modality");
		STUDY_PAGE_COLUMN_HEADERS.add("Study Date & Time");
		STUDY_PAGE_COLUMN_HEADERS.add("Referring Physician");
		STUDY_PAGE_COLUMN_HEADERS.add("Institution Name");
	}
	
	
	public static final ArrayList<String> RECENT_SEARCH = new ArrayList<String>();
	static{
		RECENT_SEARCH.add("SEARCHTEXT");
		RECENT_SEARCH.add("GENDER");
		RECENT_SEARCH.add("MACHINE");
		RECENT_SEARCH.add("ACQDATE");
		RECENT_SEARCH.add("MODALITY");
	}
	
	public static final String MALE ="M";
	public static final String FEMALE ="F";

	public static final String INVALID_PATIENTID_SUID_COMBINATION_ERROR_MSG="Invalid URL(Patient ID and Study ID Combination)";
	public static final String SUID_TEXT = "suid" ;
	public static final String PATIENT_ISSUER_ID_TEXT = "isspatId";
	public static final String BUTTON_SELECTED = "selected";
	public static final String UNKNOWN_MODALITY ="UNKNOWN";

	public static final ArrayList<String> ACQUISITIONDATE = new ArrayList<String>();
	static{
		ACQUISITIONDATE.add("Today");
		ACQUISITIONDATE.add("Yesterday");
		ACQUISITIONDATE.add("This Week");
		ACQUISITIONDATE.add("Last Week");
		ACQUISITIONDATE.add("This Month");
		ACQUISITIONDATE.add("Last Month");


	}

	public static final String CHECK ="CHECK";
	public static final String UNCHECK ="UNCHECK";

	public static final String LASTWEEK="Last Week"; 
	public static final String TODAY="Today"; 
	public static final String YESTERDAY="Yesterday"; 
	public static final String THISWEEK="This Week"; 
	public static final String LASTMONTH="Last Month"; 
	public static final String THISMONTH="This Month"; 

	public static final String STUDYDATEFORMAT="yyyy-MM-dd"; 
	public static final String ACQUISITIONCOLUMNFORMAT="MMM d, yyyy"; 


	public static final String RESULT_STATUS ="Result Status:";
	public static final String PENDING_STATUS ="Running";
	public static final String EUREKA_RESULT_STATUS_DONE ="Done";
	
	public static final String MESSAGE_TYPE_INFO = "Info!";
	
	public static final String A_NEW_STUDY = "A new study for ";
	public static final String HAS_BEEN_RECEIVED = " has arrived";
	
	public static final String THE_STUDY = "Study for ";
	public static final String HAS_BEEN_UPDATED = " has been updated";	
	
	public static final String NEW_RESULT_RECIEVED_MESSAGE_1 = "There is a new result available for ";
	public static final String NEW_RESULT_RECIEVED_MESSAGE_2 = " and study: ";
	public static final String EMPTY_STUDY = "empty";
	
	
	
	//patients2 page constants
	
	public static final String EUREKA_ROW_BOTTOM_BORDER_COLOR = "1px solid rgb(28, 28, 47)";
	public static final String EUREKA_ROW_SELECTED_COLOR = "rgb(37, 39, 66) none repeat scroll 0% 0% / auto padding-box border-box";
	
	public static final String ASC_CONSTANT = "ASC";
	public static final String DSC_CONSTANT = "DSC";
	
	public static final String ASCENDING_ORDER_TEXT ="ascending order";
	public static final String DESCENDING_ORDER_TEXT ="descending order";
	
	public static final String BLUE_BACKGROUND_COLOR = "rgba(14, 17, 36, 1)";
	public static final String BLACK_BACKGROUND_COLOR = "rgba(5, 8, 22, 1)";
	public static final String ARIA_SELECTED = "aria-selected";
	
	public static final int MAX_ENTRY_IN_RECENTSEARCH_AND_VIEWED_TAB = 20;
	
	public static final String INFO_ICON ="M3.892 14.037c-.587.093-1.755.324-2.348.371-.502.04-.975-.233-1.264-.624-.29-.39-.36-.89-.187-1.34l2.335-6.083H0c-.002-1.268 1.002-2.2 2.285-2.557.612-.171 1.754-.405 2.347-.369.356.021.976.233 1.265.624.29.39.359.89.187 1.34l-2.335 6.083h2.427c0 1.266-.966 2.347-2.284 2.555zM4.118 2.745c-.758 0-1.373-.614-1.373-1.372C2.745.614 3.36 0 4.118 0S5.49.614 5.49 1.373c0 .758-.614 1.372-1.372 1.372z";
	public static final String USERMANUAL_ICON ="M4.315 0C6.675 0 9 1.038 9 3.521c0 2.29-2.748 3.171-3.338 3.999C5.219 8.135 5.367 9 4.149 9c-.793 0-1.18-.616-1.18-1.18 0-2.097 3.227-2.571 3.227-4.298 0-.95-.663-1.514-1.77-1.514-2.36 0-1.439 2.324-3.227 2.324C.554 4.332 0 3.962 0 3.258 0 1.531 2.065 0 4.315 0zM4.05 9.9c.74 0 1.35.608 1.35 1.35 0 .742-.61 1.35-1.35 1.35-.74 0-1.35-.607-1.35-1.35 0-.741.61-1.35 1.35-1.35z";
	public static final String LOGOUT_ICON ="M5.5 12.19c-1.408 0-2.816-.554-3.888-1.66C.572 9.459 0 8.034 0 6.517c0-1.515.573-2.94 1.612-4.012.406-.42 1.064-.42 1.47 0 .406.42.406 1.098 0 1.516-.647.667-1.002 1.553-1.002 2.495 0 .943.355 1.83 1.002 2.495.646.666 1.504 1.033 2.418 1.033.914 0 1.773-.367 2.418-1.033.647-.667 1.002-1.552 1.002-2.495 0-.942-.355-1.829-1.002-2.495-.407-.419-.407-1.098 0-1.516.406-.42 1.065-.42 1.47 0C10.428 3.575 11 5 11 6.517c0 1.517-.573 2.941-1.612 4.013-1.07 1.106-2.479 1.66-3.887 1.66zm.833-6.852V.858C6.333.384 5.96 0 5.501 0c-.459 0-.832.384-.832.858v4.48c0 .474.373.858.832.858.46 0 .832-.384.832-.858z";
	
	
	
}


