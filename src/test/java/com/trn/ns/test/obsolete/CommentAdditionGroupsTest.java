//package com.trn.ns.test.obsolete;
//
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.CircleAnnotation;
//import com.trn.ns.page.factory.EllipseAnnotation;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.TextAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class CommentAdditionGroupsTest extends TestBase  {
//
//	private ViewerPage viewerPage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	
//	private ExtentTest extentTest;
//
//	private TextAnnotation textAnn;
//	private CircleAnnotation circle ;
//	private EllipseAnnotation ellipse ;
//	private final String LinearmeasurementComment="Linear Measurement Comment";
//
//	String gaelKuhnFilePath =Configurations.TEST_PROPERTIES.get("GAEL^KUHN_filepath");
//	String GaelPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gaelKuhnFilePath);
//	String GaelPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, gaelKuhnFilePath);
//
//	String LIDCFilePath =Configurations.TEST_PROPERTIES.get("LIDC-IDRI-0012_filepath");
//	String LIDCPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, LIDCFilePath);
//
//	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//	String iMBIO =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbio_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iMBIO);
//
//	String liver9FilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9FilePath);
//
//
//	String longComment = "This is a sentence which is long ";
//
//	private final String editRulerComment="Edited Ruler comment";
//	private final String ellipseComment="Ellipse comment";
//	private final String editEllipseComment="Edit Ellipse comment";
//	private final String circleComment="Circle comment";
//	private final String ANNOTATION_TXT_1="ABC";
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"Chrome","IE11","Edge","US1075","Sanity"})
//	public void test21_US1075_TC5313_verifyAddCommentWhenImageCenterIsInViewbox() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Add comment functionlaity of DICOM SC tool bar when image is panned and zoomed but  image center is in the view box.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(imbio_PatientName);
//
//		
//		studyPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad(3);
//		textAnn=new TextAnnotation(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+imbio_PatientName+" in viewer" );
//		String zoomValue=viewerPage.getValueOfZoom(1);
//		viewerPage.addtext(1,ANNOTATION_TXT_1);
//		viewerPage.inputZoomNumber(500, 1);
//		viewerPage.selectPanFromQuickToolbar(1);
//		viewerPage.dragAndReleaseOnViewerWithoutHop(50, 50, 300, 0);
//		viewerPage.assertNotEquals(viewerPage.getValueOfZoom(1), zoomValue, "Checkpoin[1/4]", "Zoom value after applying Pan and Zoom on SC data");
//
//		viewerPage.addtext(1,ANNOTATION_TXT_1);
//		viewerPage.assertNotEquals(viewerPage.getValueOfZoom(1), zoomValue, "Checkpoin[2/4]", "Verified value of zoom after applying PAN and ZOOM on SC result");
//		viewerPage.assertTrue(textAnn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(1, 2, false), "Checkpoin[3/4]", "Verified that  text ananotation is active accepted GSPS");
//		viewerPage.assertFalse(viewerPage.isElementPresent(textAnn.getAnchorLinesOfTextAnnot(1, 1).get(0)), "Checkpoin[4/4]", "Verified that  comment is present on viewbox");
//
//
//	}
//	
//
//}
