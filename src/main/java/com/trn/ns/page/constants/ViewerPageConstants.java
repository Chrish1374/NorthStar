package com.trn.ns.page.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class ViewerPageConstants {


	// layout options
	public static final String ONE_BY_ONE_LAYOUT="1x1";
	public static final String TWO_BY_ONE_LAYOUT="2x1";
	public static final String ONE_BY_TWO_LAYOUT="1x2";
	public static final String TWO_BY_TWO_LAYOUT="2x2";
	public static final String THREE_BY_THREE_LAYOUT="3x3";
	public static final String THREE_BY_TWO_LAYOUT="3x2";
	public static final String ONE_BY_ONE_L_AND_TWO_BY_ONE_R_LAYOUT="1x1L-2x1R";
	public static final String ONE_BY_ONE_T_AND_ONE_BY_TWO_B_LAYOUT="1x1T-1x2B";
	public static final String TWO_BY_ONE_L_AND_ONE_BY_ONE_R_LAYOUT="2x1L-1x1R";
	public static final String TWO_BY_THREE_LAYOUT="2x3";
	public static final String ONE_BY_ONE_L_AND_THREE_BY_ONE_R_LAYOUT="1x1L-3x1R";
	public static final String THREE_BY_ONE_L_AND_ONE_BY_ONE_R_LAYOUT="3x1L-1x1R";

	//overlays levels
	public static final String MINIMUM_ANNOTATION="minimum";
	public static final String DEFAULT_ANNOTATION="default";
	public static final String FULL_ANNOTATION="full";
	public static final String CUSTOM_ANNOTATION="custom";

	//orientation markers
	public static final String ANTERIOR_OR_OVERLAY = "A"; 
	public static final String POSTERIOR_OR_OVERLAY = "P"; 
	public static final String LEFT_OR_OVERLAY = "L"; 
	public static final String RIGHT_OR_OVERLAY = "R"; 
	public static final String HEAD_OR_OVERLAY = "H"; 
	public static final String FOOT_OR_OVERLAY = "F"; 

	
	
	
	//Text Overlay values
	public static final String PATIENTEXTERNALID_VALUE = "ID";
	public static final String ACQUISITIONDEVICE_VALUE = "AquisitionVal";
	public static final String STUDYACQUISITIONDEVICESITE_VALUE = "AquSiteVal";
	public static final String IMAGEACQUISITIONDEVICEDATETIME_VALUE = "2016-11-11";
	public static final String TARGET_VALUE = "TartVal";
	public static final String DETECTOR_VALUE = "DetVal";
	public static final String CONTOUR_DESCRIPTION = "description";
	public static final String TEXT_OVERLAY ="Not for diagnostic use";
	
	//Ellipses for a text overlay 
	public static final String ELLIPSES = "...";

	public static final String SCROLLBAR_COMPONENT_BACKGROUND_COLOR="#cbcce2";
	public static final String SCROLLBAR_COMPONENT_BACKGROUND_COLOR_WHEN_FOCUSED="#6267a7";
	public static final String SCROLLBAR_SLIDER_BACKGROUND_COLOR="#252742";
	public static final String SCROLLBAR_SLIDER_BACKGROUND_COLOR_WHEN_FOCUSED="#252742";


	//Default Slice Position percentile
	public static final String SLICE_PERCENTILE = "0.5";

	//ResultAppliedAnnotationTest
	public static final String COLOUR_WHITE = "White";
	public static final String COLOUR_GREY = "grey";
	public static final String GREY_COLOR ="rgb(128, 128, 128)";
	public static final String CIRCLE_GREY_HANDLE="#C0C0C0";


	//From PRESET Overlay
	public static final String RESET = "RESET";
	public static final String INVERT = "INVERT";
	public static final String HEAD = "HEAD";
	public static final String T1 = "T1";

	// radial menu options

	public static final String PAN = "Pan";
	public static final String WINDOWING = "Windowing";
	public static final String CINE = "Cine";
	public static final String CINE4D="Cine4D";
	public static final String ZOOM = "Zoom";
	public static final String SCROLL = "Scroll";
	public static final String LINE = "Line";
	public static final String DISTANCE = "Distance";
	public static final String WINDOW_LEVEL = "Window Level";
	public static final String WINDOW_LEVEL_TITLE = "WW/WL";
	public static final String ELLIPSE = "Ellipse";
	public static final String CIRCLE = "Circle";
	public static final String LINEAR_MEASUREMENT = "LinearMeasurement";
	public static final String TRAINGULATION = "Triangulation";
	public static final String POINT = "Point";
	public static final String TEXT_ARROW = "Text Arrow";
	public static final String POLYLINE = "PolyLine";
	public static final String PMAP ="PMAP";
	public static final String PMAP_ICON ="PM";
	public static final String MEASUREMENT_TAB = "Measurement";
	public static final String MORE = "More";
	public static final String PLANE = "Plane";
	
	//Radial Menu
	public static final String RADIAL_BACKGROUND_COLOR="#2d2d2d";
	public static final String BACKGROUND_COLOR_OVERLAY = "#808080";


	//Context Menu Icon's title List
	public static final String TEXT = "Text";
	public static final String INVERT_ICON="Invert";

	public static final String X_INDEX="X";
	public static final String Y_INDEX="Y";
	public static final String Z_INDEX="Z";


	// Keyboard keys 
	public final static HashMap<String,String> KEYBOARD_SHORTCUTS = new HashMap<String,String>();
	static {
		KEYBOARD_SHORTCUTS.put("W", "SetWindowingMode");
		KEYBOARD_SHORTCUTS.put("C", "Cine");
		KEYBOARD_SHORTCUTS.put("ArrowUp", "ScrollReverse");
		KEYBOARD_SHORTCUTS.put("ArrowDown", "ScrollForward");
		KEYBOARD_SHORTCUTS.put("ArrowLeft", "GspsInSlicePre");
		KEYBOARD_SHORTCUTS.put("ArrowRight", "GspsInSliceNext");
		KEYBOARD_SHORTCUTS.put("PageUp", "GspsPreSlice");
		KEYBOARD_SHORTCUTS.put("PageDown", "GspsNextSlice");
		KEYBOARD_SHORTCUTS.put("SpaceBar", "SynchronizationMode");
		KEYBOARD_SHORTCUTS.put("G", "GspsToggle");
		KEYBOARD_SHORTCUTS.put("D", "SetDistance");
		KEYBOARD_SHORTCUTS.put(".", "NextPhase");
		KEYBOARD_SHORTCUTS.put(",", "PreviousPhase");
		KEYBOARD_SHORTCUTS.put("A", "Accept");
		KEYBOARD_SHORTCUTS.put("R", "Reject");
		KEYBOARD_SHORTCUTS.put("P", "Pending");
	}

	//Active viewbox
	public static final String ACTIVE_VIEWBOX = "viewbox active";


	// GSPS attributes

	public static final String FONT_SIZE_FOR_TEXT="16px";

	public static final String X1 = "x1";
	public static final String X2 = "x2";
	public static final String Y1 = "y1";
	public static final String Y2 = "y2";
	public static final String X = "x";
	public static final String Y = "y";

	public static final String ACTIVE_GSPS_WIDTH="2px";
	public static final String NON_ACTIVE_GSPS_WIDTH="2px";
	public static final String SHADOW_GSPS_WIDTH="3px";
	public static final String POINT_CIRCLE_RADIUS="5px";
	public static final String POINT_CIRCLE_RADIUS_WO_PX="5";
	public static final String HALO_CIRCLE_RADIUS="6px";
	public static final int POINT_LINES=4;
	public static final String NO_FINDING = "Please select a finding" ;
	public static final String ALREADY_FINDING = "A comment already exists" ;

	// GSPS findings colors

	public static final String ACCEPTED_COLOR="green";
	public static final String REJECTED_COLOR="red";
	public static final String SHADOW_COLOR="yellow";
	public static final String OTHER_SHADOW_COLOR="blue";
	public static final String FILL_COLOR_HANDLER="transparent";
	public static final String PENDING_COLOR="lightblue";


	public static final String CONTEXT_MENU_TAB_ACTIVE_BACKGROUND_COLOR = "rgba(45, 45, 45, 1)";

	//Accept checkbox color when selected
	public static final String ACCEPT_CHECKBOX_COLOR ="rgb(0, 128, 0)";
	public static final String REJECT_CHECKBOX_COLOR ="rgb(165, 42, 42)";	
	public static final String LOCALIZER_BLUE_COLOR="rgb(0, 0, 255)";
	public static final String HALO_RING_COLOR="#b6ff00";
	public static final String POINTER_PENDING_COLOR = "#add8e6" ;
	public static final String POINTER_ACCEPTED_COLOR = "#439a51" ;
	public static final String POINTER_REJECTED_COLOR = "#cf5757" ; 
	public static final String TEXT_OVERLAY_COLOR = "rgb(255, 165, 0)";

	//SendToPACS slider Enable/Disable color
	public static final String SLIDER_ENABLE_COLOR="rgba(74, 218, 100, 1)";
	public static final String SLIDER_DISABLE_COLOR="rgba(128, 128, 128, 1)";

	public static final String BLACK_COLOR_RGB="rgb(0, 0, 0)";
	public static final String BLACK_COLOR = "rgba(0, 0, 0, 1)";

	//border color for Accepted/Rejected/Pending button in OP
	public static final String REJECTED_BORDER_COLOR_OP="rgb(255, 0, 0)";
	public static final String ACCEPTED_FINDING_COLOR="rgba(0, 128, 1, 1)";
	public static final String REJECTED_FINDING_COLOR="rgba(162, 57, 63, 1)";
	public static final String PENDING_FINDING_COLOR="rgba(0, 153, 255, 1)";
	public static final String PENDING_RT_FINDING_COLOR="rgb(0, 153, 255)";
	public static final String MACHINE_FILTER_BACKGROUND_W="rgb(255, 255, 255)";


	//Label for GSPS Radial option
	public static final String GSPS_ACCEPT = "Accept";
	public static final String GSPS_REJECT = "Reject";
	public static final String GSPS_PREVIOUS = "Previous";
	public static final String GSPS_NEXT = "Next";
	public static final String GSPS_TEXT = "Add Text";
	public static final String GSPS_FINDING = "Findings";
	public static final String GSPS_DELETE = "Delete";

	//Finding Table

	public static final String LINEAR_FINDING_NAME = "LinearMeasurement_1" ; 
	public static final String POINT_FINDING_NAME = "CrossPoint_1" ; 
	public static final String ELLIPSE_FINDING_NAME = "Ellipse_1" ; 
	public static final String CIRCLE_FINDING_NAME = "Circle_1" ; 
	public static final String POLYLINE_FINDING_NAME = "PolyLine_1" ; 
	public static final String DISTANCE_FINDING_NAME = "Line_1" ; 
	public static final String TEXT_FINDING_NAME = "Unformattedtext_1" ; 
	public static final String FINDING_HEADER = " Finding" ; 
	public static final String FINDING_MULTIPLE_HEADER = " Findings" ; 
	public static final String POINT_FINDING_PREFIX = "CrossPoint_" ; 
	
	//viewer banner

	public static final String BANNER_TEXT = "Some element of this study is for research use only. Not for diagnostic or clinical use.";
	public static final String WARNING_SR_DATA_MSG="'Enhanced SR' is not supported";
	public static final String REJCTED_INSTANCE_SOPCLASSUID_ERROR = "'X-Ray Radiation Dose SR' is not supported" ;
	public static final String LOG_TABLE_SOPCLASSUID_ERROR = "X-Ray Radiation Dose SR is not supported" ;
	public static final String SOPCLASSUID_ERROR = "Warning! 'X-Ray Radiation Dose SR' is not supported" ;
	public static final String BANNER_TEXT_FOR_CROSS_SLICES="Finding cross slices";
	public static final String BANNER_TEXT_FOR_SEND_TO_PACS ="Error in sending results to Eureka AI";
	public static final String BATCH_CONFLICT_MESSAGE="Data from different machine runs are being displayed";


	//SendToPACS slider Enable/Disable color
	public static final String WARNING_POP_UP_HEADER_FOR_PENDING_RESULT= "There are 2 pending results. What would you like to do?";
	public static final String WARNING_POP_UP_CHECKBOX="Dont show again";
	public static final String SYNC_ICN = "sync-desktop-icon";
	public static final String SEND_TO_PACS_TEXT = "Send to PACS";

	public static final String NOTIFICATION_UI_TEXT="findings are sent to PACS";


	// Linear measurement text //Accuracy benchmark
	public static final String NO_PIXEL_SPACE ="no pixel spacing";	
	public static final String SQUARE_HORIZONTAl = "87.9";
	public static final String SQUARE_VERTICAL = "117.2";
	public static final String SQUARE_DAIGONAL = "124.3";
	public static final String OBLIQUE_HORIZONTAl="25.6";
	public static final String PIXEL_SPACING="50";
	public static final String IMAGER_SPACING="67";

	
	//*************
	//Output panel
	//*************
	public static final String DICOM_SC = "dicom"; 
	public static final String CREATED_ON_TEXT="Creation date:";
	public static final String CREATED_BY_TEXT="Creator:";
	public static final String RESULT_NAME="Result Name:";
	public static final String COUNT_OF_IMAGES="Number of Images:";
	public static final String COUNT_OF_PAGES="Number of Pages:";
	public static final String DISTANCE_MEASUREMENT="Distance:";
	public static final String PENDING_TEXT ="Pending";
	public static final String ACCEPTED_TEXT ="Accepted";
	public static final String REJECTED_TEXT ="Rejected";

	public static final String ID = "id";
	public static final String THUMBNAIL_WARNING_MSG= "series with this finding is not loaded.";
	public static final String OPACITY_FOR_THUMBNAIL="0.5" ;
	public static final String OPACITY_FOR_JUMP_ICON="1" ;
	public static final String OPACITY_ON_MOUSE_HOVERED="0.6" ;
	public static final String OPACITY_FOR_BATCH_CONFLICT="0.7" ;
	public static final String SCROLLBAR_WIDTH="11px";


	public static final String OUTPUT_PANEL_COMMENT_TEXT ="Comment: ";
	public static final String COMMENT_TAG= OUTPUT_PANEL_COMMENT_TEXT.trim();
	public static final String OUTPUT_PANEL_TEXT_LABEL="Text:";
	public static final String OUTPUTPANEL_SLICE_LABEL="Slice";
	public static final String VIEWER_SLICE_TEXT=OUTPUTPANEL_SLICE_LABEL+":";

	public static final String FLOAT="float";
	public static final String RIGHT="right";

	public static final String SELECT_ALL_ICON_SELECTED ="checked-background";
	public static final String SELECT_ALL_ICON_DESELECTED ="unchecked-outline";

	public static final String OUTPUTPANEL_PHASE_LABEL="Phase";
	public static final String OUTPUT_PANEL_OPEN_HEIGHT ="60px";
	public static final String OUTPUT_PANEL_CLOSE_HEIGHT ="50px";
	public static final String OUTPUT_PANEL_OPEN_WIDTH ="30px";
	public static final String OUTPUT_PANEL_CLOSE_WIDTH ="25px";
	public static final String OUTPUT_PANEL_OPEN_BORDER_RADIUS ="60px 0px 0px 60px";
	public static final String OUTPUT_PANEL_CLOSE_BORDER_RADIUS ="0px 50px 50px 0px";
	public static final String Type2="Type2";
	public static final String CENTER="center";
	public static final String SELECT_ALL_TEXT="Select All";
	public static final String FILTER_FINDINGS_TEXT="Filter findings";
	public static final String SYNCALL_FINDINGS_TEXT="Sync all findings";
	public static final String NO_STUDY_AVAILABLE_TEXT="Study name not available";
	public static final String ADD_COMMENT_TEXT="Add Comment";
	public static final String NOT_SENT_TO_PACS_TEXT="Not sent to PACS";
	public static final String SENT_TO_PACS_TEXT="Sent to PACS";

	//*************
	// content selector 
	//*************
	public static final String CONTENT_SERIES_BUTTON  ="Series";
	public static final String USER_CREATED_RESULT = "User Created Results";
	public static final String OBJECT_IMPORTER_TOOLTIP = "object-importer";
	public static final String CONTENT_SELECTOR_RESULT_PREFIX="GSPS_";
	public static final String CONTENT_SELECTOR_RESULT_PREFIX_MACHINE="_";

	public static final String BONEAGE_MACHINE_NAME="Bone Age";
	public static final String TOOLTIP_FIRSTROW_BONEAGE = "boneage";
	public static final String BONEAGE_MACHINE_NAME1="Bone Age 1";
	public static final String AIDOC_MACHINE_NAME1 = "Lesion Finder";
	public static final String AIDOC_MACHINE_NAME2 = "Lesion Finder 2";

	// Date formats -
	public static final String CONTENTSELECTOR_DATE_FORMAT = "MMM d, yyyy h:mm:s a";
	public static final String TOOLTIP_DATE_FORMAT ="MM/dd/yyyy, hh:mm aaa";
	public static final String STANDARDDOBFORMAT = "MMM dd, yyyy";
	public static final String OUTPUT_PANEL_DATEFORMAT ="MM/dd/yyyy";


	//*************
	// DICOMRT
	//*************
	public static final String CONTOUR_OPTION ="option_";

	// Console errors

	public static final String REFERENCEFRAMENUM_ERROR = "TypeError: Cannot read property '0' of null";
	public static final String SETTRANSFORM_CONSOLE_ERROR= "Cannot read property 'setTransform'";


	//Icon visibility
	public static final String ICON_DISABLE = "disabled";
	public static final String NS_IMAGE_DATA_FOLDER = "DataFolderPath";
	public static final String SOURCE_TAG = "Source";

	//keyboard shortcut for Accept,Reject and Pending
	public static final String ACCEPT_KEY ="\u0061";
	public static final String REJECT_KEY ="\u0072";
	public static final String PENDING_KEY ="\u0070";


	//**********
	//Bump Circle
	//**********

	public static final String BUMP_CIRCLE_STROKE_OPACITY ="1";
	public static final String BUMP_CIRCLE_FILL_OPACITY ="0.8";

	//banner message after saving the layout
	public static final String BANNER_MESSAGE_WHEN_LAYOUT_SAVED="Your layout preference for machine ";
	public static final String TOOLTIP_WHEN_DIFFERENT_BATCH_LOADED="Cannot save layout with different batch contents.";
	public static final String BANNER_MESSAGE_WHEN_LAYOUT_RESET="Your layout preference was reset to default for machine ";

	//Layout Options
	public static final String SAVE_LAYOUT_WITH_CONTENT="Save Layout with Content";
	public static final String SAVE_LAYOUT_WITHOUT_CONTENT="Save Layout without Content";
	public static final String RESET_LAYOUT="Reset Layout";

	// Polyline attribute
	public static final String ATTRIBUTE_D="d";
	public static final String NEW_RESULT_NOTIFICATION_MESSAGE="There are new result(s) available for this patient/study. Please check the Series tab to see the updated result(s).";
	public static final String NEW_RESULT_DIALOG_MESSAGE = "New results are available";

	// context menu options


	//Pmap Color Code


	public final static LinkedHashMap<String,String> LUT_COLOR = new LinkedHashMap<String,String>();
	static{
		LUT_COLOR.put("HOTIRON","Hot iron");
		LUT_COLOR.put("RAINBOW1","Rainbow 1");
		LUT_COLOR.put("RAINBOW2","Rainbow 2");
		LUT_COLOR.put("RAINBOW3","Rainbow 3");
		LUT_COLOR.put("BLUE","Blue");
		LUT_COLOR.put("ORANGE","Orange");
		LUT_COLOR.put("GRAY&WARM","Gray and warm");
		LUT_COLOR.put("GRAY","Gray");
		LUT_COLOR.put("RESET","Reset");
	}

	public static final String LUT_HIGHLIGHTEDORSELECTED_COLOR = "rgba(157, 157, 157, 1)" ;

	public static final String HOTIRON = "Hot iron" ;
	public static final String RAINBOW1 = "Rainbow 1" ;
	public static final String RAINBOW2 = "Rainbow 2" ;
	public static final String RAINBOW3 = "Rainbow 3" ;
	public static final String BLUE = "Blue" ;
	public static final String ORANGE = "Orange" ;
	public static final String GRAYWARM = "Gray and warm" ;
	public static final String GRAY = "Gray" ;
	public static final String RESETLUT = "Reset" ;


	public final static LinkedHashMap<String,String> LUT_COLOR_CODE = new LinkedHashMap<String,String>();
	static {


		LUT_COLOR_CODE.put("Hot iron","rgb(255, 0, 0) 76.0784%");
		LUT_COLOR_CODE.put("Rainbow 1","rgb(0, 255, 255) 66.2745%");
		LUT_COLOR_CODE.put("Rainbow 2","rgb(255, 152, 193) 0%");
		LUT_COLOR_CODE.put("Rainbow 3","rgb(0, 0, 255) 82.7451%");
		LUT_COLOR_CODE.put("Blue","rgb(0, 0, 255) 100%");
		LUT_COLOR_CODE.put("Orange","rgb(255, 128, 0) 49.8039%");
		LUT_COLOR_CODE.put("Gray and warm","rgb(102, 0, 0) 70.9804%");
		LUT_COLOR_CODE.put("Gray","rgb(0, 0, 0) 100%)");
	}

	public static final String CUT="Cut";
	public static final String PASTE="Paste";
	public static final String CANCEL="Cancel";
	public static final String COPY="Copy";
	
	public static final String MAX="max";
	public static final String MIN="min";
	public static final String MIDDLE="middle";

	public static final String CREATED_BY = "Created by";
	public static final String OPACITY_FOR_DISABLE_ICON="0.4" ;

	public static final String AXIAL_KEY = "AXIAL";
	public static final String CORONAL_KEY= "CORONAL";
	public static final String SAGITTAL_KEY = "SAGITTAL";

	public final static LinkedHashMap<String,String> PLANES = new LinkedHashMap<String,String>();
	static {


		PLANES.put(AXIAL_KEY,"Axial");
		PLANES.put(CORONAL_KEY,"Coronal");
		PLANES.put(SAGITTAL_KEY,"Sagittal");

	}

	public final static ArrayList<String> ALL_PLANES = new ArrayList<String>();
	static {


		ALL_PLANES.add(PLANES.get(AXIAL_KEY));
		ALL_PLANES.add(PLANES.get(CORONAL_KEY));
		ALL_PLANES.add(PLANES.get(SAGITTAL_KEY));

	}


	public static final ArrayList<String> ALL_OVERLAYS = new ArrayList<String>();
	static{
		ALL_OVERLAYS.add(FULL_ANNOTATION);
		ALL_OVERLAYS.add(MINIMUM_ANNOTATION);
		ALL_OVERLAYS.add(DEFAULT_ANNOTATION);

	}




	public static final String COMPLETED_EXIT_TEXT ="Completed... Enter to exit";
	public static final String OP_TAB_NAME ="Output Panel";
	public static final String SERIES_TAB_NAME="Series";

	public static final String WARNING_TITLE="Warning!";	
	public static final String SUCCESS="Success!";
	public static final String ERROR="Error!";
	public static final String INFO="Info!";

	//pending finding dialog popup constant
	public static final String ACCEPT_ALL="Accept All";
	public static final String REJECT_ALL="Reject All";
	public static final String NO_CHANGES="No Changes";
	public static final String DIALOG_HEADER="Would you like to continue?";
	public static final String DO_NOT_SHOW_CHECKBOX="Dont show again";
	public static final String CANCEL_BUTTON="Cancel";
	public static final String CONTINUE_BUTTON="Continue";

	public static final String LOSSY_TEXT="Lossy";
	public static final String RESULT_APPLIED_TEXT="Results Applied";
	public static final String PATIENT_NAME="Patient Name";
	public static final String PATIENT_ID="Patient ID";

	//PDF ,DICOM,SR icon
	public static final String PDF_ICON="PDF";
	public static final String SR_ICON="SR";
	public static final String DI_ICON="DI";
	public static final String DICOM="DICOM";
	public static final String GSPS_ICON="GS";
	public static final String GSPS="GSPS";
	public static final String SR_TOOLTIP="Structured Report";

	// View Box Tool panel

	public static final String VIEWBOX_TOOL_PANEL_CLOSED_BUTTON_TOOLTIP = "Click to open the viewbox tool panel";
	public static final String VIEWBOX_TOOL_PANEL_OPENED_BUTTON_TOOLTIP = "Click to close the viewbox tool panel";

	public static final String VIEWBOX_BUTTON_LEFT_ALIGNMENT_VALUE = "444.5px";
	public static final String VIEWBOX_POSITION_VALUE = "absolute";
	public static final String VIEWBOX_POSITION = "position";
	public static final String VIEWBOX_LEFT_ALIGNMENT = "left";
	
	public static final String TRANSFORM = "transform";
	public static final String TRANSFORM_VALUE = "scale(1, -1) translate(0, -7)";
	public static final String SLIDER_THUMB_BORDER_RADIUS="50% 50% 0px";
	
	// Text Overlays -
	
	public static final String ZOOM_OVERLAY = "Z:";
	public static final String WINDOW_WIDTH = "W:";
	public static final String WINDOW_CENTER = "C:";
	public static final String MR_MODALITY = "MR";
	public static final int ZOOM_TO_100_PERCENTAGE = 100;
	
	
	// Background color
	public static final String OLIVE_COLOR = "#808000";
	public static final String INDIANRED_COLOR = "#CD5C5C";
	public static final String TEAL_COLOR = "#008080";
	public static final String AQUA_COLOR = "#00ffff";
	
	
	
	// GSPS attribute
	
	public static final String LINE_ATTR = "ns-line";
	public static final String POLYLINE_ATTR = "ns-polyline";
	public static final String CIRCLE_ATTR = "ns-circle";
	public static final String ELLIPSE_ATTR = "ns-ellipse";
	public static final String POINT_ATTR = "ns-point";
	

	public static final ArrayList<String> CUSTOM_OVERLAY = new ArrayList<String>();
	static{
		CUSTOM_OVERLAY.add("Patient Name : ");
		CUSTOM_OVERLAY.add("Patient ID : ");
		CUSTOM_OVERLAY.add("Patient Sex : ");
		CUSTOM_OVERLAY.add("Slice : ");
		CUSTOM_OVERLAY.add("Z : ");
		CUSTOM_OVERLAY.add("Study DateTime : ");
		CUSTOM_OVERLAY.add("Series Description : ");
	
	}
	
	public static final String ALL_CUSTOM_OVERLAY = "All";
	
	
	// LIASION script constants -
	
	
	public static final String CPUMACHINENAME ="CPU Test";
	public static final String RANDOENTMACHINENAME ="RANDO^ENT";
		
	public static final String ICOMETRIXMACHINENAME ="icometrix";
	public static final String ICOMACHINENAMEV2 ="icometrix-icobrain-v2";
	public static final String ICOMACHINENAMEV2_2 ="icometrix-icobrain-v2_2";

	public static final String CONSOLE_ERROR_MESSAGE="ERROR TypeError: Cannot read property 'clientHeight' of undefined";
	
	
	// labels -  GSPS
	
	public static final String POINT_LABEL ="X";
	public static final String CIRCLE_LABEL ="C";
	public static final String POLYLINE_LABEL ="P";
	public static final String ELLIPSE_LABEL ="E";
	
	
	
	
	
}


