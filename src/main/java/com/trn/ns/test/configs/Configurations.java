package com.trn.ns.test.configs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Properties;

import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.utilities.PropertyManager;

public class Configurations {

	private static final Properties APPLICATIONPROPERTY = PropertyManager.loadApplicationPropertyFile();
	public static HashMap<String, String> TEST_PROPERTIES = new HashMap<String,String>();
	public static LogStatus INFO = LogStatus.INFO;
	public static LogStatus WARNING = LogStatus.WARNING;
	public static LogStatus PASS = LogStatus.PASS;
	public static LogStatus FAIL = LogStatus.FAIL;
	public static LogStatus SKIP = LogStatus.SKIP;
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String RALLY_STATUS_PASS = "Pass";
	public static final String RALLY_STATUS_FAIL = "Fail";

	static{

		//		configurations related to mobile
		TEST_PROPERTIES.put("logFolderPath", APPLICATIONPROPERTY.getProperty("logFolderPath"));		
		TEST_PROPERTIES.put("mobileApk",APPLICATIONPROPERTY.getProperty("mobileApk"));		
		TEST_PROPERTIES.put("appiumServerIP", APPLICATIONPROPERTY.getProperty("appiumServerIP"));
		TEST_PROPERTIES.put("appiumServerPort",APPLICATIONPROPERTY.getProperty("appiumServerPort"));

		//		Configurations related to Remote Driver : Grid
		if(System.getProperty("hubIP")!=null)
			TEST_PROPERTIES.put("hubIP", System.getProperty("hubIP").trim());
		else
			TEST_PROPERTIES.put("hubIP", APPLICATIONPROPERTY.getProperty("hubIP"));

		if(System.getProperty("hubPort")!=null)
			TEST_PROPERTIES.put("hubPort", System.getProperty("hubPort").trim());
		else
			TEST_PROPERTIES.put("hubPort",APPLICATIONPROPERTY.getProperty("hubPort"));

		//		Configurations related to SAUCE LAB		
		TEST_PROPERTIES.put("username", APPLICATIONPROPERTY.getProperty("username"));
		TEST_PROPERTIES.put("access_key",APPLICATIONPROPERTY.getProperty("access_key"));
		TEST_PROPERTIES.put("os_platform", APPLICATIONPROPERTY.getProperty("os_platform"));
		TEST_PROPERTIES.put("browser_version",APPLICATIONPROPERTY.getProperty("browser_version"));

		if(System.getProperty("executionType")!=null)
			TEST_PROPERTIES.put("executionType", System.getProperty("executionType").trim());
		else
			TEST_PROPERTIES.put("executionType", APPLICATIONPROPERTY.getProperty("executionType"));

		TEST_PROPERTIES.put("errorAppImages", APPLICATIONPROPERTY.getProperty("errorAppImages"));
		TEST_PROPERTIES.put("launchBrowser", APPLICATIONPROPERTY.getProperty("launchBrowser"));
		TEST_PROPERTIES.put("lang", APPLICATIONPROPERTY.getProperty("lang"));
		TEST_PROPERTIES.put("browserVersion", APPLICATIONPROPERTY.getProperty("browserVersion"));
		TEST_PROPERTIES.put("Browser", PropertyManager.getBrowser(APPLICATIONPROPERTY));
		TEST_PROPERTIES.put("browserName", APPLICATIONPROPERTY.getProperty("browserName"));
		TEST_PROPERTIES.put("nsUserName", APPLICATIONPROPERTY.getProperty("nsUserName"));
		TEST_PROPERTIES.put("nsInvalidPass", APPLICATIONPROPERTY.getProperty("nsInvalidPass"));
		TEST_PROPERTIES.put("skipLogin", APPLICATIONPROPERTY.getProperty("skipLogin"));
		TEST_PROPERTIES.put("nsPassword", APPLICATIONPROPERTY.getProperty("nsPassword"));
		TEST_PROPERTIES.put("run", APPLICATIONPROPERTY.getProperty("run"));
		TEST_PROPERTIES.put("os_platform", APPLICATIONPROPERTY.getProperty("os_platform"));
		TEST_PROPERTIES.put("randomMaxInteger", APPLICATIONPROPERTY.getProperty("randomMaxInteger"));
		TEST_PROPERTIES.put("DomainUser", APPLICATIONPROPERTY.getProperty("DomainUser"));
		TEST_PROPERTIES.put("DomainUserPassword", APPLICATIONPROPERTY.getProperty("DomainUserPassword"));
		TEST_PROPERTIES.put("iisResetBatPath", APPLICATIONPROPERTY.getProperty("iisResetBatPath"));
		TEST_PROPERTIES.put("mouseWheelUp", APPLICATIONPROPERTY.getProperty("mouseWheelUp"));
		TEST_PROPERTIES.put("mouseWheelDown", APPLICATIONPROPERTY.getProperty("mouseWheelDown"));

		if(System.getProperty("remoteHost")!=null) {
			TEST_PROPERTIES.put("nsHostName", System.getProperty("remoteHost").trim());
			TEST_PROPERTIES.put("execution", "REMOTE");
		}
		else {
			TEST_PROPERTIES.put("nsHostName", APPLICATIONPROPERTY.getProperty("nsHostName").trim());
			TEST_PROPERTIES.put("execution", "LOCAL");
		}

		TEST_PROPERTIES.put("nsPort", APPLICATIONPROPERTY.getProperty("nsPort"));
		TEST_PROPERTIES.put("nsBaseUrl", APPLICATIONPROPERTY.getProperty("nsBaseUrl"));
		TEST_PROPERTIES.put("dbName", APPLICATIONPROPERTY.getProperty("dbName"));
		TEST_PROPERTIES.put("nsAnalyticsdbName", APPLICATIONPROPERTY.getProperty("nsAnalyticsdbName"));		
		TEST_PROPERTIES.put("dbHostName", APPLICATIONPROPERTY.getProperty("dbHostName"));
		TEST_PROPERTIES.put("dbPort", APPLICATIONPROPERTY.getProperty("dbPort"));
		TEST_PROPERTIES.put("isDBIntegrated", APPLICATIONPROPERTY.getProperty("isDBIntegrated"));
		TEST_PROPERTIES.put("dbUserName", APPLICATIONPROPERTY.getProperty("dbUserName"));
		TEST_PROPERTIES.put("dbPassword", APPLICATIONPROPERTY.getProperty("dbPassword"));
		TEST_PROPERTIES.put("elementSearchTimeOut", APPLICATIONPROPERTY.getProperty("elementSearchTimeOut"));
		TEST_PROPERTIES.put("waitForElementLowTime", APPLICATIONPROPERTY.getProperty("waitForElementLowTime"));
		TEST_PROPERTIES.put("waitForElementMediumTime", APPLICATIONPROPERTY.getProperty("waitForElementMediumTime"));
		TEST_PROPERTIES.put("waitForElementHighTime", APPLICATIONPROPERTY.getProperty("waitForElementHighTime"));
		TEST_PROPERTIES.put("waitForElement", APPLICATIONPROPERTY.getProperty("waitForElement"));
		TEST_PROPERTIES.put("imagesPath", APPLICATIONPROPERTY.getProperty("imagesPath"));
		TEST_PROPERTIES.put("imageComparisondiffImages", APPLICATIONPROPERTY.getProperty("imageComparisondiffImages"));
		TEST_PROPERTIES.put("run", APPLICATIONPROPERTY.getProperty("run"));
		TEST_PROPERTIES.put("extractedFileFolderPath", APPLICATIONPROPERTY.getProperty("extractedFileFolderPath"));
		TEST_PROPERTIES.put("hop", APPLICATIONPROPERTY.getProperty("hop"));
		TEST_PROPERTIES.put("Picline_filepath", APPLICATIONPROPERTY.getProperty("Picline_filepath"));
		TEST_PROPERTIES.put("Doe_Lilly_filepath", APPLICATIONPROPERTY.getProperty("Doe_Lilly_filepath"));
		TEST_PROPERTIES.put("Head_CT_filepath", APPLICATIONPROPERTY.getProperty("Head_CT_filepath"));
		TEST_PROPERTIES.put("SQA_Testing", APPLICATIONPROPERTY.getProperty("SQA_Testing"));       
		TEST_PROPERTIES.put("Icometrix", APPLICATIONPROPERTY.getProperty("Icometrix_filepath"));
		TEST_PROPERTIES.put("Corticometrix_1", APPLICATIONPROPERTY.getProperty("Corticometrix_1_filepath"));
		TEST_PROPERTIES.put("LTA_Study_filepath", APPLICATIONPROPERTY.getProperty("LTA_Study_filepath"));           
		TEST_PROPERTIES.put("AH.4_filepath", APPLICATIONPROPERTY.getProperty("AH.4_filepath"));
		TEST_PROPERTIES.put("LiverTumor", APPLICATIONPROPERTY.getProperty("LiverTumor"));   
		TEST_PROPERTIES.put("AI_Change_MS", APPLICATIONPROPERTY.getProperty("AI_Change_MS"));
		TEST_PROPERTIES.put("AI_Change_Tumor", APPLICATIONPROPERTY.getProperty("AI_Change_Tumor"));
		TEST_PROPERTIES.put("Icometrix", APPLICATIONPROPERTY.getProperty("Icometrix_filepath"));
		TEST_PROPERTIES.put("Corticometrix_1", APPLICATIONPROPERTY.getProperty("Corticometrix_1_filepath"));
		TEST_PROPERTIES.put("Liver_9_filepath", APPLICATIONPROPERTY.getProperty("Liver_9_filepath"));
		TEST_PROPERTIES.put("MR_LSP_filepath", APPLICATIONPROPERTY.getProperty("MR_LSP_filepath"));
		TEST_PROPERTIES.put("CR_Thorax_Chest_filepath", APPLICATIONPROPERTY.getProperty("CR_Thorax_Chest_filepath"));
		TEST_PROPERTIES.put("AH4_pdf_filepath", APPLICATIONPROPERTY.getProperty("AH4_pdf_filepath"));
		TEST_PROPERTIES.put("XA_2_filepath", APPLICATIONPROPERTY.getProperty("XA_2_filepath"));
		TEST_PROPERTIES.put("Bone_Age_Study_filepath", APPLICATIONPROPERTY.getProperty("Bone_Age_Study_filepath"));
		TEST_PROPERTIES.put("NM_L65P733139591_filepath", APPLICATIONPROPERTY.getProperty("NM_L65P733139591_filepath"));
		TEST_PROPERTIES.put("JobsSteve_filepath", APPLICATIONPROPERTY.getProperty("JobsSteve_filepath"));
		TEST_PROPERTIES.put("CerebralAngio_XA1_filepath", APPLICATIONPROPERTY.getProperty("CerebralAngio_XA1_filepath")); 
		TEST_PROPERTIES.put("Mammo_irv5US_filepath", APPLICATIONPROPERTY.getProperty("Mammo_irv5US_filepath")); 
		TEST_PROPERTIES.put("MR_LSP_WithResult_filepath", APPLICATIONPROPERTY.getProperty("MR_LSP_WithResult_filepath")); 
		TEST_PROPERTIES.put("CR-THORAX-CHEST1-WithResult_filepath", APPLICATIONPROPERTY.getProperty("CR-THORAX-CHEST1-WithResult_filepath")); 
		TEST_PROPERTIES.put("03Dimensions103_filepath", APPLICATIONPROPERTY.getProperty("03Dimensions103_filepath")); 
		TEST_PROPERTIES.put("Mammo_irv5_withResults_filepath", APPLICATIONPROPERTY.getProperty("Mammo_irv5_withResults_filepath"));
		TEST_PROPERTIES.put("Subject_60_filepath", APPLICATIONPROPERTY.getProperty("Subject_60_filepath"));
		TEST_PROPERTIES.put("DX_Thorax_Ribs_filepath", APPLICATIONPROPERTY.getProperty("DX_Thorax_Ribs_filepath"));
		TEST_PROPERTIES.put("Dummy_16_filepath", APPLICATIONPROPERTY.getProperty("Dummy_16_filepath"));
		TEST_PROPERTIES.put("MSK_MG_Current_filepath", APPLICATIONPROPERTY.getProperty("MSK_MG_Current_filepath"));
		TEST_PROPERTIES.put("Qure_ai_XRay_Patient1_filepath", APPLICATIONPROPERTY.getProperty("Qure_ai_XRay_Patient1_filepath"));
		TEST_PROPERTIES.put("NorthStar_MR_Lspine_filepath", APPLICATIONPROPERTY.getProperty("NorthStar_MR_Lspine_filepath"));
		TEST_PROPERTIES.put("NorthStar^MR^Brain^WO^Contrast_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^MR^Brain^WO^Contrast_filepath"));
		TEST_PROPERTIES.put("AH4^sameOrie^diffFRUID_filepath", APPLICATIONPROPERTY.getProperty("AH4^sameOrie^diffFRUID_filepath"));
		TEST_PROPERTIES.put("NorthStar_MR_LSP_filepath", APPLICATIONPROPERTY.getProperty("NorthStar_MR_LSP_filepath"));
		TEST_PROPERTIES.put("Northstar_Multiframe_3fps_filepath", APPLICATIONPROPERTY.getProperty("Northstar_Multiframe_3fps_filepath"));
		TEST_PROPERTIES.put("Northstar_Multiframe_15fps_filepath", APPLICATIONPROPERTY.getProperty("Northstar_Multiframe_15fps_filepath"));
		TEST_PROPERTIES.put("NorthStar^Text^NoAnchor_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^Text^NoAnchor_filepath"));
		TEST_PROPERTIES.put("anchorPointBelow_filepath", APPLICATIONPROPERTY.getProperty("anchorPointBelow_filepath"));
		TEST_PROPERTIES.put("test-ct6132016_filepath", APPLICATIONPROPERTY.getProperty("test-ct6132016_filepath"));
		TEST_PROPERTIES.put("NorthStar^Text^With^AnchorPoint_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^Text^With^AnchorPoint_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^POINTS^MULTISERIES_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^POINTS^MULTISERIES_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^POINT_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^POINT_filepath"));
		TEST_PROPERTIES.put("Aidoc_filepath", APPLICATIONPROPERTY.getProperty("Aidoc_filepath"));
		TEST_PROPERTIES.put("TeraRecon_BrainTDA_filepath", APPLICATIONPROPERTY.getProperty("TeraRecon_BrainTDA_filepath"));
		TEST_PROPERTIES.put("Phantom_Of_Grid_filepath", APPLICATIONPROPERTY.getProperty("Phantom_Of_Grid_filepath"));
		TEST_PROPERTIES.put("TEST^Non_Square_Pixels_filepath", APPLICATIONPROPERTY.getProperty("TEST^Non_Square_Pixels_filepath"));
		TEST_PROPERTIES.put("Test^Pixel_Spacing_filepath", APPLICATIONPROPERTY.getProperty("Test^Pixel_Spacing_filepath"));
		TEST_PROPERTIES.put("OrientationSample_A", APPLICATIONPROPERTY.getProperty("OrientationSample_A")); 
		TEST_PROPERTIES.put("OrientationSample_F", APPLICATIONPROPERTY.getProperty("OrientationSample_F"));
		TEST_PROPERTIES.put("OrientationSample_H", APPLICATIONPROPERTY.getProperty("OrientationSample_H"));
		TEST_PROPERTIES.put("OrientationSample_L", APPLICATIONPROPERTY.getProperty("OrientationSample_L"));
		TEST_PROPERTIES.put("OrientationSample_P", APPLICATIONPROPERTY.getProperty("OrientationSample_P"));
		TEST_PROPERTIES.put("OrientationSample_R", APPLICATIONPROPERTY.getProperty("OrientationSample_R"));
		TEST_PROPERTIES.put("DX_THORAX_RIBS1_filepath", APPLICATIONPROPERTY.getProperty("DX_THORAX_RIBS1_filepath"));
		TEST_PROPERTIES.put("Breast_MR_2_filepath", APPLICATIONPROPERTY.getProperty("Breast_MR_2_filepath"));
		TEST_PROPERTIES.put("AAA2_filepath", APPLICATIONPROPERTY.getProperty("AAA2_filepath"));
		TEST_PROPERTIES.put("NMWithPDF_filepath", APPLICATIONPROPERTY.getProperty("NMWithPDF_filepath"));
		TEST_PROPERTIES.put("NorthStar^MR^MultiPhase^20x6_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^MR^MultiPhase^20x6_filepath"));
		TEST_PROPERTIES.put("NM_L65P733139591_WO_Result_filepath", APPLICATIONPROPERTY.getProperty("NM_L65P733139591_WO_Result_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^CIRCLE_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^CIRCLE_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^ELLIPSE_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^ELLIPSE_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^POINTS^MULTISERIES_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^POINTS^MULTISERIES_filepath"));
		TEST_PROPERTIES.put("Runoff_filepath", APPLICATIONPROPERTY.getProperty("Runoff_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^POLYLINE_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^POLYLINE_filepath"));
		TEST_PROPERTIES.put("NorthStar^CT^Neck_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^CT^Neck_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^PolyLine^Closed_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^PolyLine^Closed_filepath"));
		TEST_PROPERTIES.put("retry", APPLICATIONPROPERTY.getProperty("retry"));
		TEST_PROPERTIES.put("Quibim_BreastPerfusion_filepath", APPLICATIONPROPERTY.getProperty("Quibim_BreastPerfusion_filepath"));
		TEST_PROPERTIES.put("DigitalReferenceObject_filepath", APPLICATIONPROPERTY.getProperty("DigitalReferenceObject_filepath"));
		TEST_PROPERTIES.put("4Quan_LesionDetection_filepath", APPLICATIONPROPERTY.getProperty("4Quan_LesionDetection_filepath"));
		TEST_PROPERTIES.put("aidoc_CervicalSpine_DE686_filepath", APPLICATIONPROPERTY.getProperty("aidoc_CervicalSpine_DE686_filepath"));
		TEST_PROPERTIES.put("S10671CTSR_filepath", APPLICATIONPROPERTY.getProperty("S10671CTSR_filepath"));
		TEST_PROPERTIES.put("Boneage_filepath", APPLICATIONPROPERTY.getProperty("Boneage_filepath"));
		TEST_PROPERTIES.put("Imbio_Texture_CTLung_filepath", APPLICATIONPROPERTY.getProperty("Imbio_Texture_CTLung_filepath"));
		TEST_PROPERTIES.put("Axial_GrayScale_filepath", APPLICATIONPROPERTY.getProperty("Axial_GrayScale_filepath"));
		TEST_PROPERTIES.put("AXIAL_8BIT_filepath", APPLICATIONPROPERTY.getProperty("AXIAL_8BIT_filepath"));
		TEST_PROPERTIES.put("AXIAL_OddWidth_filepath", APPLICATIONPROPERTY.getProperty("AXIAL_OddWidth_filepath"));
		TEST_PROPERTIES.put("AXIAL_RGB_filepath", APPLICATIONPROPERTY.getProperty("AXIAL_RGB_filepath"));
		TEST_PROPERTIES.put("Anonymous_filepath", APPLICATIONPROPERTY.getProperty("Anonymous_filepath"));
		TEST_PROPERTIES.put("LPixel_filepath", APPLICATIONPROPERTY.getProperty("LPixel_filepath"));
		TEST_PROPERTIES.put("Qpqq~qt%_filepath", APPLICATIONPROPERTY.getProperty("Qpqq~qt%_filepath"));
		TEST_PROPERTIES.put("TEST^Modality_LUT_filepath", APPLICATIONPROPERTY.getProperty("TEST^Modality_LUT_filepath"));
		TEST_PROPERTIES.put("CRONIN^BARBARA_filepath", APPLICATIONPROPERTY.getProperty("CRONIN^BARBARA_filepath"));
		TEST_PROPERTIES.put("BONADONNA^NICHOLAS_filepath", APPLICATIONPROPERTY.getProperty("BONADONNA^NICHOLAS_filepath"));
		TEST_PROPERTIES.put("SPECTRA_DATABASE_filepath", APPLICATIONPROPERTY.getProperty("SPECTRA_DATABASE_filepath"));
		TEST_PROPERTIES.put("AH.4_US675_filepath", APPLICATIONPROPERTY.getProperty("AH.4_US675_filepath"));		
		TEST_PROPERTIES.put("DX_D55R573B101_filepath", APPLICATIONPROPERTY.getProperty("DX_D55R573B101_filepath"));		
		TEST_PROPERTIES.put("4Quan_HeadAndNeck_filepath", APPLICATIONPROPERTY.getProperty("4Quan_HeadAndNeck_filepath"));
		TEST_PROPERTIES.put("PICCONE_filepath", APPLICATIONPROPERTY.getProperty("PICCONE_filepath"));		
		TEST_PROPERTIES.put("Quibim_3DTrabecularBone_filepath", APPLICATIONPROPERTY.getProperty("Quibim_3DTrabecularBone_filepath"));
		TEST_PROPERTIES.put("LUNIT_CT_ChestPA_filepath", APPLICATIONPROPERTY.getProperty("LUNIT_CT_ChestPA_filepath"));
		TEST_PROPERTIES.put("TEST^VOILUT", APPLICATIONPROPERTY.getProperty("TEST^VOILUT_filepath"));
		TEST_PROPERTIES.put("RAND^ENT_filepath", APPLICATIONPROPERTY.getProperty("RAND^ENT_filepath"));
		TEST_PROPERTIES.put("TCGA_VP_A878_filepath", APPLICATIONPROPERTY.getProperty("TCGA_VP_A878_filepath"));
		TEST_PROPERTIES.put("MR_CARDIAC_2_filepath", APPLICATIONPROPERTY.getProperty("MR_CARDIAC_2_filepath"));
		TEST_PROPERTIES.put("TEST^SR_Tanaka_Hanako_filepath", APPLICATIONPROPERTY.getProperty("TEST^SR_Tanaka_Hanako_filepath"));
		TEST_PROPERTIES.put("TEST^SR_Wilkins_Charles_filepath", APPLICATIONPROPERTY.getProperty("TEST^SR_Wilkins_Charles_filepath"));
		TEST_PROPERTIES.put("DX_D55R573B101_filepath", APPLICATIONPROPERTY.getProperty("DX_D55R573B101_filepath"));
		TEST_PROPERTIES.put("AnonymizedByInferVISION2017-10-17_filepath", APPLICATIONPROPERTY.getProperty("AnonymizedByInferVISION2017-10-17_filepath"));
		TEST_PROPERTIES.put("PHANTOM^CHEST_filepath", APPLICATIONPROPERTY.getProperty("PHANTOM^CHEST_filepath"));
		TEST_PROPERTIES.put("ADC_philips_FilePath", APPLICATIONPROPERTY.getProperty("ADC_philips_FilePath"));
		TEST_PROPERTIES.put("CT_PET_Multiphase_filepath", APPLICATIONPROPERTY.getProperty("CT_PET_Multiphase_filepath"));
		TEST_PROPERTIES.put("Boneage_Multiple_Machine_filepath", APPLICATIONPROPERTY.getProperty("Boneage_Multiple_Machine_filepath"));
		TEST_PROPERTIES.put("GAEL^KUHN_filepath", APPLICATIONPROPERTY.getProperty("GAEL^KUHN_filepath"));
		TEST_PROPERTIES.put("LIDC-IDRI-0012_filepath", APPLICATIONPROPERTY.getProperty("LIDC-IDRI-0012_filepath"));
		TEST_PROPERTIES.put("AIDOC_MACHINE_filepath", APPLICATIONPROPERTY.getProperty("AIDOC_MACHINE_filepath"));
		TEST_PROPERTIES.put("LUNIT_MCA_2_filepath", APPLICATIONPROPERTY.getProperty("LUNIT_MCA_2_filepath"));
		TEST_PROPERTIES.put("LUNIT_PTX_1_filepath", APPLICATIONPROPERTY.getProperty("LUNIT_PTX_1_filepath"));
		TEST_PROPERTIES.put("LUNIT_PTX_2_filepath", APPLICATIONPROPERTY.getProperty("LUNIT_PTX_2_filepath"));
		TEST_PROPERTIES.put("LUNIT_PTX_3_filepath", APPLICATIONPROPERTY.getProperty("LUNIT_PTX_3_filepath"));
		TEST_PROPERTIES.put("AH.4DeletePatientAPI_filepath", APPLICATIONPROPERTY.getProperty("AH.4DeletePatientAPI_filepath"));
		TEST_PROPERTIES.put("AH.4DeleteSeriesAPI_filepath", APPLICATIONPROPERTY.getProperty("AH.4DeleteSeriesAPI_filepath"));
		TEST_PROPERTIES.put("AH.4TestExpiredToken_filepath", APPLICATIONPROPERTY.getProperty("AH.4TestExpiredToken_filepath"));
		TEST_PROPERTIES.put("Subject60_DeleteStudyAPI_filepath", APPLICATIONPROPERTY.getProperty("Subject60_DeleteStudyAPI_filepath"));
		TEST_PROPERTIES.put("PiccOneDeleteBatchAPI_filepath", APPLICATIONPROPERTY.getProperty("PiccOneDeleteBatchAPI_filepath"));
		TEST_PROPERTIES.put("VUNO_SIIM_CASE_032_Filepath", APPLICATIONPROPERTY.getProperty("VUNO_SIIM_CASE_032_Filepath"));
		TEST_PROPERTIES.put("John_005_S_0223_Filepath", APPLICATIONPROPERTY.getProperty("John_005_S_0223_Filepath"));
		TEST_PROPERTIES.put("John_Smith_1_CORSTAGE_old_Filepath", APPLICATIONPROPERTY.getProperty("John_Smith_1_CORSTAGE_old_Filepath"));
		TEST_PROPERTIES.put("IBL_JohnDoe_Filepath", APPLICATIONPROPERTY.getProperty("IBL_JohnDoe_Filepath"));
		TEST_PROPERTIES.put("3ChestCT1p25mm_filepath", APPLICATIONPROPERTY.getProperty("3ChestCT1p25mm_Filepath"));
		TEST_PROPERTIES.put("Anonymize_AH.4_Filepath", APPLICATIONPROPERTY.getProperty("Anonymize_AH.4_Filepath"));
		TEST_PROPERTIES.put("IHEMammoTest_Filepath", APPLICATIONPROPERTY.getProperty("IHEMammoTest_Filepath"));
		TEST_PROPERTIES.put("Imbio_Density_CTLung_Doe^Lilly_Filepath", APPLICATIONPROPERTY.getProperty("Imbio_Density_CTLung_Doe^Lilly_Filepath"));
		TEST_PROPERTIES.put("VidaCase02_Filepath", APPLICATIONPROPERTY.getProperty("VidaCase02_Filepath"));
		TEST_PROPERTIES.put("CMR_ITA_BER_FOR_filepath", APPLICATIONPROPERTY.getProperty("CMR_ITA_BER_FOR_filepath"));
		TEST_PROPERTIES.put("QIN-PROSTATE-01-0001_filepath", APPLICATIONPROPERTY.getProperty("QIN-PROSTATE-01-0001_filepath"));
		TEST_PROPERTIES.put("MR_Lspine_Filepath", APPLICATIONPROPERTY.getProperty("MR_Lspine_Filepath"));
		TEST_PROPERTIES.put("WJ_Filepath", APPLICATIONPROPERTY.getProperty("WJ_Filepath"));
	
		TEST_PROPERTIES.put("RTStruct_With_2Machine_Filepath", APPLICATIONPROPERTY.getProperty("RTStruct_With_2Machine_Filepath"));
		TEST_PROPERTIES.put("Anonymize_Cervical_Spine_filepath", APPLICATIONPROPERTY.getProperty("Anonymize_Cervical_Spine_filepath"));
		TEST_PROPERTIES.put("JIAJIE_filepath", APPLICATIONPROPERTY.getProperty("JIAJIE_filepath"));
		TEST_PROPERTIES.put("NorthStar^GSPS^MultiText^MultiSeries_filepath", APPLICATIONPROPERTY.getProperty("NorthStar^GSPS^MultiText^MultiSeries_filepath"));
		TEST_PROPERTIES.put("Cardiac_MR_T1T2_02_filepath", APPLICATIONPROPERTY.getProperty("Cardiac_MR_T1T2_02_filepath"));
		TEST_PROPERTIES.put("MR_CARDIAC_filepath", APPLICATIONPROPERTY.getProperty("MR_CARDIAC_filepath"));
		TEST_PROPERTIES.put("Breast_Time_Intensity_filepath", APPLICATIONPROPERTY.getProperty("Breast_Time_Intensity_filepath"));
		TEST_PROPERTIES.put("Cardiac_MR_T1T2_filepath", APPLICATIONPROPERTY.getProperty("Cardiac_MR_T1T2_filepath"));
		TEST_PROPERTIES.put("Brain_Perfusion_Filepath", APPLICATIONPROPERTY.getProperty("Brain_Perfusion_Filepath"));
		TEST_PROPERTIES.put("QureCXR1_Filepath", APPLICATIONPROPERTY.getProperty("QureCXR1_Filepath"));
		TEST_PROPERTIES.put("S2008-3CTP_Filepath", APPLICATIONPROPERTY.getProperty("S2008-3CTP_Filepath"));
		TEST_PROPERTIES.put("VIDA_LCS_COPD_Filepath", APPLICATIONPROPERTY.getProperty("VIDA_LCS_COPD_Filepath"));
		TEST_PROPERTIES.put("VIDA_Emphysema_ILD_Filepath", APPLICATIONPROPERTY.getProperty("VIDA_Emphysema_ILD_Filepath"));
		TEST_PROPERTIES.put("VIDA_LungPrint_Discovery_Filepath", APPLICATIONPROPERTY.getProperty("VIDA_LungPrint_Discovery_Filepath"));
		TEST_PROPERTIES.put("Lung_LIDC_0405_Filepath", APPLICATIONPROPERTY.getProperty("Lung_LIDC_0405_Filepath"));
		TEST_PROPERTIES.put("BrainPerfusion_EAI_Filepath", APPLICATIONPROPERTY.getProperty("BrainPerfusion_EAI_Filepath"));
		TEST_PROPERTIES.put("ryantest_Filepath", APPLICATIONPROPERTY.getProperty("ryantest_Filepath"));
		TEST_PROPERTIES.put("320fail_Filepath", APPLICATIONPROPERTY.getProperty("320fail_Filepath"));
		TEST_PROPERTIES.put("covid_Filepath", APPLICATIONPROPERTY.getProperty("covid_Filepath"));
		TEST_PROPERTIES.put("cpu_test_Filepath", APPLICATIONPROPERTY.getProperty("cpu_test_Filepath"));
		TEST_PROPERTIES.put("Rib-AAA-2_Filepath", APPLICATIONPROPERTY.getProperty("Rib-AAA-2_Filepath"));
		
		TEST_PROPERTIES.put("northstarImageFolder", APPLICATIONPROPERTY.getProperty("northstarImageFolder"));
		TEST_PROPERTIES.put("Anonymous2_Filepath", APPLICATIONPROPERTY.getProperty("Anonymous2_Filepath"));
		TEST_PROPERTIES.put("Anonymous1_Filepath", APPLICATIONPROPERTY.getProperty("Anonymous1_Filepath"));
		
	
		
		System.setProperty("java.library.path", "./src/main/resources/com/jdbcdll" );

		try {
			Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths" );
			fieldSysPath.setAccessible( true );
			fieldSysPath.set( null, null );
		} catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//		################
		//		Rally Variables
		//		################
		if(System.getProperty("updateRally")!=null&&System.getProperty("updateRally").equals(YES)){
			TEST_PROPERTIES.put("rallyFlag", System.getProperty("updateRally"));
			TEST_PROPERTIES.put("rallyUsername", System.getProperty("rallyUsername"));
			TEST_PROPERTIES.put("rallyPassword", System.getProperty("rallyPassword"));
		}
		else{
			TEST_PROPERTIES.put("rallyPassword", APPLICATIONPROPERTY.getProperty("rallyPassword"));
			TEST_PROPERTIES.put("rallyUsername", APPLICATIONPROPERTY.getProperty("rallyUsername"));
			TEST_PROPERTIES.put("rallyFlag", APPLICATIONPROPERTY.getProperty("updateRally"));
		}
		TEST_PROPERTIES.put("rallyURL", APPLICATIONPROPERTY.getProperty("rallyURL"));
		TEST_PROPERTIES.put("workspaceRef", APPLICATIONPROPERTY.getProperty("workspaceRef"));

		if(System.getProperty("domain")!=null){
			TEST_PROPERTIES.put("domain", System.getProperty("domain").trim());
			TEST_PROPERTIES.put("group", System.getProperty("group").trim());
		}
		else{
			TEST_PROPERTIES.put("domain", APPLICATIONPROPERTY.getProperty("domain").trim());
			TEST_PROPERTIES.put("group", APPLICATIONPROPERTY.getProperty("group").trim());
		}
		
		if(System.getProperty("liasionDataFolder")!=null)
			TEST_PROPERTIES.put("dataFolder", System.getProperty("liasionDataFolder").trim());
		else
			TEST_PROPERTIES.put("dataFolder", APPLICATIONPROPERTY.getProperty("liasionDataFolder").trim());
		
		TEST_PROPERTIES.put("cstoreFolder", APPLICATIONPROPERTY.getProperty("cstoreFolder").trim());
		


	}
}
