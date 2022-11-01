package com.trn.ns.test.viewer.outputPanel;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.BumpToolCircle;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DICOMRT;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.Interpolation;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.MeasurementWithUnit;

import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.PolyLineAnnotation;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerDynamicOPTest extends TestBase {

	private ExtentTest extentTest;
	private LoginPage loginPage;
	private PatientListPage patientListPage;
	private MeasurementWithUnit lineWithUnit;
	private OutputPanel panel;
	private HelperClass helper;
	String comment = "Sample Text";


	String point = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
	String pointMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, point); 

	String liver9 =Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9);

	String sr = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String srPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, sr);

	String tcga = Configurations.TEST_PROPERTIES.get("TCGA_VP_A878_filepath");
	String patientNameDICOMRT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, tcga);

	String iblFilePath = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String iblPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, iblFilePath);

	String pmap = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, pmap);

	String imbio =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbio);

	String cad = Configurations.TEST_PROPERTIES.get("IHEMammoTest_Filepath");
	String cadPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, cad);

	String phase = TEST_PROPERTIES.get("Cardiac_MR_T1T2_filepath");
	String cardiact1t2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, phase);

	String brain =Configurations.TEST_PROPERTIES.get("BrainPerfusion_EAI_Filepath");
	String brainPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, brain);


	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws InterruptedException {
		// Begin on the Login Page, and log in.
		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username,password);
		patientListPage = new PatientListPage(driver);


	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test01_US2341_TC10047_verifyDynamicEntries()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify user created GSPS are visible on Output Panel dynamically with out closing and reopening it.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false,false);


		lineWithUnit=new MeasurementWithUnit(driver);		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		panel.waitForThumbnailToGetDisplayed();

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"","Verifying the dynamic OP");
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[1/32]", "Verifying that thumbnail is displayed after creating GSPS");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[2/32]","Verifying the line is displayed in thumbnail");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[3/32]","verifying the correct state is displayed");

		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[4/32]", "verifying there is no pending thumbnail");

		lineWithUnit.mouseHover(lineWithUnit.getViewPort(1));
		lineWithUnit.selectLinearMeasurementWithLeftClick(1, 1);
		lineWithUnit.selectAcceptfromGSPSRadialMenu();

		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[5/32]", "verifying that on state change the thumbnail is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[6/32]","verifying the line is displayed in thumbnail");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(1),"Checkpoint[7/32]","verifying the thumbnail state");

		panel.addCommentFromOutputPanel(1,comment);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[8/32]", "verifying that state is changed and thumbnail is moved to accepted");
		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Checkpoint[9/32]","");
		panel.assertEquals(lineWithUnit.getText(lineWithUnit.getTextCommentAddedForLinearMeasurement(1).get(0)), comment, "Checkpoint[10/32]", "verifying the state is changed of GSPS annotation");

		panel.enableFiltersInOutputPanel(true, false,false);	
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[11/32]", "verifying the thumbnail is displayed under accepted filter");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[12/32]","verifying that line is still present");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[13/32]","verifying the state of thumbnail");

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[14/32]", "verifying that there is no finding under rejected filter");

		lineWithUnit.selectLinearMeasurementWithLeftClick(1, 1);
		lineWithUnit.selectRejectfromGSPSRadialMenu();
		panel.waitForThumbnailToGetDisplayed();

		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[15/32]", "Verifying that after rejecting the finding it started getting displayed in OP");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[16/32]","verifying the line is displayed in thumbnail");
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[17/32]","verifying the thumbnail state is rejected");

		panel.openAndCloseOutputPanel(false);

		panel.browserBackWebPage();
		patientListPage.waitForPatientPageToLoad();
		helper.loadViewerDirectly(liver9PatientName, 1);

		panel.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveRejectedGSPS(1, 1), "Checkpoint[18/32]", "Verifying that after reloading the viewer line is displayed as active rejected");

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[19/32]", "verifying no thumbnail under accepted filter");

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[20/32]", "Verifying there is no thumbnail under pending filter");

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[21/32]", "Verifying that thumbnail is displayed under rejected filter");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[22/32]","verifying the line is present in thumbnail");
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[23/32]","verifying its state as rejected");

		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, -90, -90, 200, 200);

		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[24/32]", "Verifying after creating new GSPS thumbnail is not updated as rejected is selected");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[25/32]","Verifying the line is present under thumbnail");
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[26/32]","verifying the state is persistent");

		panel.enableFiltersInOutputPanel(true, false, false);

		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[27/32]", "verifying the thumbnail is present under accpted filter for new finding");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[28/32]","verifying the circle is displayed in thumbnail");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[29/32]","verifying the state is accepted");


		ContentSelector cs = new ContentSelector(driver);
		cs.selectResultFromSeriesTab(1, cs.getAllResults().get(0));

		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[30/32]", "verifying that there is thumbnail present when first clone is selected");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[31/32]","verifying that line is displayed under thumbnail");
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[32/32]","verifying its state is rejected");

		panel.openAndCloseOutputPanel(false);
	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test02_01_US2341_TC10048_verifyDynamicEntriesNonDicom()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify updated state of the findings is reflected on Output panel dynamically when state is updated on viewer. - Status of non-dicom, SC and SR");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(iblPatientName, 1);
		panel = new OutputPanel(driver);
		ContentSelector cs = new ContentSelector(driver);

		int totalNonDICOM =cs.getAllResults().size();

		panel.enableFiltersInOutputPanel(true, false,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[1/6]", "verifying that there is no thumbnail present");

		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(),totalNonDICOM, "Checkpoint[2/6]", "verifying all the thumbnail is displayed");
		for(int i=1;i<=totalNonDICOM;i++)
			panel.assertTrue(panel.verifyPendingStateInThumbnail(i),"Checkpoint[3/6]","verifying the state of all NON-DICOM");

		panel.enableFiltersInOutputPanel(true, false,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[4/6]", "verifying that there is no thumbnail present");

		for(int i=1;i<=totalNonDICOM;i++)
		{
			panel.click(panel.getViewPort(i));
			panel.acceptResult(i);
			panel.waitForThumbnailToGetDisplayed();
			panel.assertEquals(panel.thumbnailList.size(),i, "Checkpoint[5."+i+"/6]", "verifying the thumbnail is reflected in OP under accepted filter");


			for(int j =1;j<=i;j++) {
				panel.assertTrue(panel.verifyAcceptedStateInThumbnail(j),"Checkpoint[6."+j+"/6]","verifying the state of thumbnails");
			}
		}

		panel.openAndCloseOutputPanel(false);

	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test02_02_US2341_TC10048_verifyDynamicEntriesPMAPSCSR()throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify updated state of the findings is reflected on Output panel dynamically when state is updated on viewer. - Status of non-dicom, SC and SR");

		String patients [] = {pmapPatientID,imbioPatientName,srPatientName};

		helper = new HelperClass(driver);
		for(int i =0 ;i<patients.length;i++) {
			if(i==0)
				helper.loadViewerDirectlyUsingID(patients[i], 1);
			else
				helper.loadViewerDirectly(patients[i], 1);

			panel = new OutputPanel(driver);
			panel.closeNotification();
			panel.enableFiltersInOutputPanel(false, false,true);
			int size = panel.thumbnailList.size();
			for(int j=1;j<=size;j++)
				panel.assertTrue(panel.verifyPendingStateInThumbnail(j),"Checkpoint[1."+i+"."+j+"/5]","verifying that thumbnail is present under pending filter");

			panel.enableFiltersInOutputPanel(true, false,false);
			panel.assertTrue(panel.thumbnailList.isEmpty(), "Checkpoint[2."+i+"/5]", "verifying that there is no thumbnail present under accepted tab");
			panel.click(panel.getViewPort(1));
			panel.acceptResult(1);
			panel.waitForThumbnailToGetDisplayed();
			panel.assertEquals(panel.thumbnailList.size(),1, "Checkpoint[3."+i+"/5]", "verifying that after accepting that thumbnail is also reflected in OP");
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[4."+i+"/5]","verifying the state is also changed");

			panel.enableFiltersInOutputPanel(false, false,true);
			panel.assertEquals(panel.thumbnailList.size(),size-1,"Checkpoint[5."+i+"/5]","verifying that thumbnail is not displayed under pending filter");

			panel.openAndCloseOutputPanel(false);
		}

	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test03_US2341_TC10049_verifySizeChangesOfGSPS()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the size of the finding is updated on Output Panel thumbnail when the edit is made on the viewer. - Resize");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false,false);

		lineWithUnit=new MeasurementWithUnit(driver);		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		panel.waitForThumbnailToGetDisplayed();			
		panel.assertEquals(lineWithUnit.getText(lineWithUnit.getLinearMeasurementsText(1).get(0)),panel.getMeasurementTextFromThumnail(1),"Checkpoint[1/4]","verifying the measurement is same");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[2/4]:verifying the thumbnail", "TC03_01");

		lineWithUnit.resizeSelectedLinearMeasurement(1, 1, 30, 90);
		panel.assertEquals(lineWithUnit.getText(lineWithUnit.getLinearMeasurementsText(1).get(0)),panel.getMeasurementTextFromThumnail(1),"Checkpoint[3/4]","verifying the measurement is same after resize");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[4/4]:verifying the thumbnail after resize", "TC03_02");


		panel.openAndCloseOutputPanel(false);
	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test04_US2341_TC10050_verifylocationChangesOfGSPS()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the size of the finding is updated on Output Panel thumbnail when the edit is made on the viewer. - Resize");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false,false);

		lineWithUnit=new MeasurementWithUnit(driver);		
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -50, -50, 100, 0);

		panel.waitForThumbnailToGetDisplayed();			
		panel.assertEquals(lineWithUnit.getText(lineWithUnit.getLinearMeasurementsText(1).get(0)),panel.getMeasurementTextFromThumnail(1),"Checkpoint[1/8]","verifying the measurement is same");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[2/8]:Verifying the thumbnail after creation", "TC04_01");

		lineWithUnit.moveLinearMeasurement(1, 1, 30, 90);
		panel.assertEquals(lineWithUnit.getText(lineWithUnit.getLinearMeasurementsText(1).get(0)),panel.getMeasurementTextFromThumnail(1),"Checkpoint[3/8]","verifying the measurement is same though moved the location but thumbnail is changed");
		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[4/8]:verifying the thumbnail when gsps is moved to different location which also changes the thumbnail", "TC04_02");

		helper.loadViewerDirectly(pointMultiSeries, 1);
		panel.waitForOutputPanelToLoad();

		PointAnnotation point = new PointAnnotation(driver);
		point.selectAcceptfromGSPSRadialMenu();
		point.selectPreviousfromGSPSRadialMenu();
		panel.waitForThumbnailToGetDisplayed();

		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[5/8]", "TC04_03");
		int x = panel.getXCoordinate(panel.getAnnotationFromThumbnail(1));
		int y = panel.getYCoordinate(panel.getAnnotationFromThumbnail(1));

		point.movePoint(1, 1, -200, -200);
		panel.waitForOutputPanelToLoad();		

		panel.compareElementImage(protocolName, panel.thumbnailList.get(0), "Checkpoint[6/8]", "TC04_04");
		panel.assertEquals(panel.getXCoordinate(panel.getAnnotationFromThumbnail(1)), x, "Checkpoint[7/8]", "verifying the location is changed");
		panel.assertEquals(panel.getYCoordinate(panel.getAnnotationFromThumbnail(1)), y, "Checkpoint[8/8]", "verifying the location is changed after movement");

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive"})
	public void test05_US2341_TC10051_verifyStateFindingOP()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify state of the finding is updated and displayed under correct filter on Output Panel when a comment is added to Pending finding from viewer. - Add comment to machine result GSPS");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(cadPatientName, 1);

		ContentSelector cs = new ContentSelector(driver);

		List<String> results = cs.getAllResults();
		cs.selectResultFromSeriesTab(1, results.get(0));

		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.addResultComment(1, comment);		

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false,false);
		panel.waitForThumbnailToGetDisplayed();			
		panel.assertEquals(poly.getLinesOfPolyLine(1, 1).size(),panel.getCountOfLinesInPolylineInThumbnail(1),"Checkpoint[1/2]","verifying after adding comment the polyline state is changed to accepted and it is same as displayed in viewer");


		cs.selectResultFromSeriesTab(1, results.get(0),1);
		panel.enableFiltersInOutputPanel(true, false,false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[2/2]","verifying that no thumbnail is displayed when user loads the original result");		

		panel.openAndCloseOutputPanel(false);


	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test06_US2341_TC10083_verifyOrderInOP()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the order of the findings displayed  in Output Panel.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		SimpleLine line = new SimpleLine(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		PointAnnotation point = new PointAnnotation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);

		ContentSelector cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);

		int whichViewbox = 1;

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);		

		distance.selectDistanceFromQuickToolbar((1));	
		distance.drawLine(whichViewbox, 100, 0, 200, 0);
		distance.drawLine(whichViewbox, 100, 10, 200, 0);

		line.selectLineFromQuickToolbar(1);
		line.drawLine(whichViewbox,100,35,200,0); 
		line.drawLine(whichViewbox,100,45,200,0); 

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -50,  70, -60,-60);

		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, -100, -15,-30,-30);
		circle.drawCircle(whichViewbox, 100, -15,-30,-30);

		distance.selectLinearMeasurementWithLeftClick(1, 1);
		distance.selectAcceptfromGSPSRadialMenu();

		line.selectLine(1, 1);
		line.selectRejectfromGSPSRadialMenu();

		cs.assertEquals(cs.getAllResults().size(), 1, "Checkpoint[1/16]", "verifying that there is one clone created after creating the GSPS");


		int totalFindings = panel.getFindingsCountFromFindingTable();
		int totalAcceptedFindings = panel.getTotalAcceptedFindings(1);
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertEquals(panel.thumbnailList.size(), totalFindings-1, "Checkpoint[2/16]", "verifying the total findings are displayed same as finding menu in OP under accepted filter");
		for(int i=1;i<=totalAcceptedFindings;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[3.1."+i+"]","verifying the order when both accepted and pending filters are enabled - first accepted are diaplayed");		
		panel.assertTrue(panel.verifyPendingStateInThumbnail(totalAcceptedFindings+1),"Checkpoint[3.2]","verifying the order when both accepted and pending filters are enabled - last pending are displayed");


		panel.enableFiltersInOutputPanel(true, true, true);		
		panel.assertEquals(panel.thumbnailList.size(), totalFindings, "Checkpoint[4/16]", "verifying the total findings are same as finding menu as all filters are displayed");
		for(int i=1;i<=totalAcceptedFindings;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[5.1."+i+"/16]","verifying the order when all filters are enabled - first accepted are displayed");

		panel.assertTrue(panel.verifyRejectedStateInThumbnail(totalAcceptedFindings+1),"Checkpoint[5.2/16]","verifying the order when all filters are enabled - second rejected are displayed");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(totalAcceptedFindings+2),"Checkpoint[6/16]","verifying the order when all filters are enabled - last pending are displayed");

		circle.selectRejectfromGSPSRadialMenu(circle.getAllCircles(1).get(0));
		totalAcceptedFindings = panel.getTotalAcceptedFindings(1);

		panel.assertEquals(panel.thumbnailList.size(), (totalAcceptedFindings+3), "Checkpoint[7/16]", "verifying that total findings are same after rejecting the one more finding");
		for(int i=totalAcceptedFindings+1;i<=totalAcceptedFindings+2;i++) 
			panel.assertTrue(panel.verifyRejectedStateInThumbnail(i),"Checkpoint[8/16]","verifying the rejected finding state after accpted findings");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(totalAcceptedFindings+1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[9/16]","verifying the order of rejected finding - circle is displayed which is latest finding");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(totalAcceptedFindings+2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[10/16]","verifying the order of rejected finding - line");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(totalAcceptedFindings+3),"Checkpoint[11]","verifying the pending finding state");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(totalAcceptedFindings+3)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[12/16]","verifying the line is displayed as pending finding");


		poly.selectPolylineFromQuickToolbar(1);
		int[] abc = new int[] {10,5,20,10,30,15,-10,20,-20,40,-30,-50};
		poly.drawFreehandPolyLine(1, abc);
		totalAcceptedFindings = panel.getTotalAcceptedFindings(1);

		panel.assertEquals(panel.thumbnailList.size(), (totalAcceptedFindings+3), "Checkpoint[13/16]", "verifying total findings");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[14/16]","verifying the latest finding created is displayed first as an accepted finding");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POLYLINE_ATTR),"Checkpoint[15/16]","Verifying latest finding is displayed as first finding - polyline");

		List<String> attributes = new ArrayList<String>();
		for(int i=1;i<=panel.thumbnailList.size();i++) 
			attributes.add(panel.getAllAttributes(panel.getAnnotationFromThumbnail(i)));


		panel.browserBackWebPage();
		helper.loadViewerDirectly(liver9PatientName, 1);
		panel.enableFiltersInOutputPanel(true, true, true);

		for(int i=1;i<=panel.thumbnailList.size();i++) 
			panel.assertEquals(panel.getAllAttributes(panel.getAnnotationFromThumbnail(i)), attributes.get(i-1),"Checkpoint[16/16]","verifying that findings order is same after reload");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test07_01_US2341_TC10111_verifyDeletionInOP()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated  when a finding is deleted from a viewer/series. - Delete");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		SimpleLine line = new SimpleLine(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		PointAnnotation point = new PointAnnotation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);

		ContentSelector cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);

		int whichViewbox = 1;

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);		
		panel.waitForThumbnailToGetDisplayed();
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[1/49]","Creating point and it is displayed in thumbnail");

		distance.selectDistanceFromQuickToolbar(whichViewbox);	
		distance.drawLine(whichViewbox, 100, 0, 200, 0);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[2/49]","Creating distance and checking it is displayed in first thumbnail");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[3/49]","Point is moved to second thumbnail");

		line.selectLineFromQuickToolbar(1);
		line.drawLine(whichViewbox,100,35,200,0); 
		panel.waitForThumbnailToGetDisplayed();
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[4/49]","Simple line is displayed-1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[5/49]","Distance is displayed-2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[6/49]","Point is displayed-3");

		ellipse.selectEllipseFromQuickToolbar(whichViewbox);
		ellipse.drawEllipse(whichViewbox, -50,  70, -60,-60);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[7/49]","Ellipse is displayed-1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[8/49]","Line is displayed-2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[9/49]","Displayed is displayed-3");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[10/49]","Point is displayed-4");

		circle.selectCircleFromQuickToolbar(whichViewbox);
		circle.drawCircle(whichViewbox, -100, -15,-30,-30);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[11/49]","Circle is displayed-1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[12/49]","Ellipse is displayed-2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[13/49]","Line is displayed-3");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[14/49]","Displayed is displayed-4");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(5)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[15/49]","Point is displayed-5");

		distance.selectLinearMeasurementWithLeftClick(whichViewbox, 1);
		distance.selectAcceptfromGSPSRadialMenu();

		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "verifying the order of GSPS after accepting and rejecting");
		line.selectLine(whichViewbox, 1);
		line.selectRejectfromGSPSRadialMenu();

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[16/49]","Circle is displayed -1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[17/49]","Ellipse is displayed -2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[18/49]","POint is displayed -3");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[19/49]","Line is displayed -4");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(5)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[20/49]","Distance is displayed -5");


		cs.assertEquals(cs.getAllResults().size(), 1, "Checkpoint[21/49]", "verifying the clone is also created");
		int totalFindings = panel.getFindingsCountFromFindingTable();
		int totalAcceptedFindings = panel.getTotalAcceptedFindings(1);
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.assertEquals(panel.thumbnailList.size(), totalFindings-1, "Checkpoint[22/49]", "verifying the thumbnail for accepted and pending filter");
		for(int i=1;i<=totalAcceptedFindings;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[23/49]","verifying that first accepted findings are displayed");		
		panel.assertTrue(panel.verifyPendingStateInThumbnail(totalAcceptedFindings+1),"Checkpoint[24/49]","verifying pending findings are displayed last");


		panel.enableFiltersInOutputPanel(true, true, true);		
		panel.assertEquals(panel.thumbnailList.size(), totalFindings, "Checkpoint[25/49]", "verifying the thumbnail when all filters are displayed");
		for(int i=1;i<=totalAcceptedFindings;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[26/49]","verifying accepted findings are displayed");

		panel.assertTrue(panel.verifyRejectedStateInThumbnail(totalAcceptedFindings+1),"Checkpoint[27/49]","Verifying that rejected findings are displayed");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(totalAcceptedFindings+2),"Checkpoint[28/49]","verifying that last pending findings are displayed");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[29/49]","Circle is displayed-1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[30/49]","Ellipse is displayed-2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[31/49]","POint is displayed-3");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[32/49]","Line is displayed-4");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(5)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[33/49]","Distance is displayed-5");


		line.deleteLine(1, 1);		
		panel.assertEquals(panel.thumbnailList.size(), totalFindings-1, "Checkpoint[34/49]", "verifying the thumbnails after deletion of rejected finding");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(panel.thumbnailList.size()),"Checkpoint[35/49]","verifying the pending state is still persist");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[36/49]","Circle is displayed-1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[37/49]","Ellipse is displayed-2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[38/49]","POint is displayed-3");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[39/49]","Distance is displayed-4");



		circle.deleteCircle(1, 1);
		panel.assertEquals(panel.thumbnailList.size(), totalFindings-2, "Checkpoint[40/49]", "verifying the thumbnails after deletion of accepted finding");
		panel.assertTrue(panel.verifyPendingStateInThumbnail(panel.thumbnailList.size()),"Checkpoint[41/49]","Verifying pending finding still persists");
		for(int i=1;i<=2;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[42/49]","verifying the accepted findings");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[43/49]","Ellipse is displayed -1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[44/49]","POint is displayed -2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[45/49]","Distance is displayed -3");

		distance.deleteLinearMeasurement(1, 1);		
		panel.assertEquals(panel.thumbnailList.size(), totalFindings-3, "Checkpoint[46/49]", "verifying the thumbnails after deletion of pending finding");
		for(int i=1;i<=2;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[47/49]","verifying the accepted findings");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[48/49]","Ellipse is displayed -1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[49/49]","POint is displayed -2");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive"})
	public void test07_02_US2341_TC10111_verifyDeletionMachineFinding()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated  when a finding is deleted from a viewer/series. - Delete");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(srPatientName, 1);

		panel = new OutputPanel(driver);
		panel.closeNotification();
		panel.enableFiltersInOutputPanel(false, true, false);

		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.selectCircle(2, 1);
		panel.selectDeletefromGSPSRadialMenu(2);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[1/3]", "verifying that on deletion, the finding comes under rejected filter");
		panel.assertTrue(panel.verifyRejectedStateInThumbnail(1),"Checkpoint[2/3]","verifying the status");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[3/3]","verifying the finding is displayed in thumbnail");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test08_US2341_TC10121_verifyOPRefreshesOnClones()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is refreshed with the findings from the series the is loaded on viewer using drag and drop from series tab.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		SimpleLine line = new SimpleLine(driver);
		CircleAnnotation circle = new CircleAnnotation(driver);
		PointAnnotation point = new PointAnnotation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);

		ContentSelector cs = new ContentSelector(driver);
		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);

		int whichViewbox = 1;

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);		

		distance.selectDistanceFromQuickToolbar((1));	
		distance.drawLine(whichViewbox, 100, 0, 200, 0);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -50,  70, -60,-60);

		ellipse.browserBackWebPage();
		helper.loadViewerDirectly(liver9PatientName, 1);

		line.selectLineFromQuickToolbar(1);
		line.drawLine(whichViewbox,100,35,200,0);


		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(whichViewbox, -100, -15,-30,-30);

		List<String> allClones = cs.getAllResults();
		cs.assertEquals(allClones.size(), 2, "Checkpoint[1/7]", "verified two clones are created");

		cs.selectResultFromSeriesTab(1, allClones.get(0));

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(),3 ,"Checkpoint[2/7]", "verifying that only results from first clone is displayed");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[3.1/7]","Ellipse is displayed -1");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[3.2/7]","Line is displayed -2");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[3.3/7]","POint is displayed -3");

		for(int i=1;i<=3;i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[4/7]","verifying the state of all findings");


		cs.selectResultFromSeriesTab(1, allClones.get(1));

		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(),5 ,"Checkpoint[5/7]", "verifying the thumbnails after selecting another clone");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[6.1/7]","Circle is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[6.2/7]","Line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[6.3/7]","Ellipse is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(4)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[6.4/7]","Line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(5)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[6.5/7]","Point is displayed");


		for(int i=1;i<=panel.thumbnailList.size();i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[7/7]","verifying the state of all the findings");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test09_01_US2341_TC10120_verifyOPOnCutPasteSlice()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated when cut and paste is applied on a GSPS. - Cut and Paste.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		PointAnnotation point = new PointAnnotation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);

		int whichViewbox = 1;

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);	
		point.selectAcceptfromGSPSRadialMenu();
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/14]", "verifying that no finnding under accepted filter");

		point.selectCutUsingContextMenu(point.getAllPoints(1).get(0));
		point.scrollDownToSliceUsingKeyboard(3);
		point.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);

		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[2/14]", "Verifying after paste the state is changed and OP is refreshed with finding");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[3.1/14]","verifying the finding is displayed");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[3.2/14]","verifying the state of thumbnail");

		distance.selectDistanceFromQuickToolbar((1));	
		distance.drawLine(whichViewbox, 100, 0, 200, 0);
		distance.selectRejectfromGSPSRadialMenu();

		panel.enableFiltersInOutputPanel(false,true, false);

		point.selectCutUsingContextMenu(distance.getLinearMeasurements(1, 1).get(0));		
		panel.assertFalse(panel.thumbnailList.isEmpty(),"Checkpoint[4/14]", "verifying there is no rejected finding");
		point.scrollDownToSliceUsingKeyboard(3);
		point.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);
		panel.waitForThumbnailToGetDisplayed();

		panel.enableFiltersInOutputPanel(true, true, false);		
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[5/14]", "verifying that after paste OP is refreshed");		
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[6/14]","Verifying the line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[7/14]","verifying the point is displayed");

		for(int i=1;i<=panel.thumbnailList.size();i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[8/14]","verifying the accepted state is displayed");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -50,  70, -60,-60);
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(),3 ,"Checkpoint[9/14]", "verifying that OP is refreshed after creating new finding");

		ellipse.selectEllipse(1, 1);
		ellipse.performCNTRLX();		
		ellipse.scrollDownToSliceUsingKeyboard(3);
		ellipse.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);

		panel.assertEquals(panel.thumbnailList.size(),3 ,"Checkpoint[10/14]", "verifying that OP is refreshed after paste");		

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[11/14]","verifying the pasted finding is displayed first");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[12/14]","Line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[13/14]","point is displayed");


		for(int i=1;i<=panel.thumbnailList.size();i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[14/14]","verifying the state of findings");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test09_02_US2341_TC10120_verifyOPOnCutPastePhase()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated when cut and paste is applied on a GSPS. - Cut and Paste.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(cardiact1t2PatientName, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		layout.selectLayout(layout.oneByOneLayoutIcon);

		PointAnnotation point = new PointAnnotation(driver);
		MeasurementWithUnit distance = new MeasurementWithUnit(driver);
		EllipseAnnotation ellipse = new EllipseAnnotation(driver);

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);

		int whichViewbox = 1;

		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(whichViewbox,-100,-100);	
		point.selectAcceptfromGSPSRadialMenu();

		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/15]", "verifying there is no finding");

		point.selectCutUsingContextMenu(point.getAllPoints(1).get(0));
		point.pressKey(NSGenericConstants.DOT_KEY);	
		point.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);

		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[2/15]", "verifying the OP is refreshed with finding");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[3/15]","verifying the group finding");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[4/15]","verifying the state");

		distance.selectDistanceFromQuickToolbar((1));	
		distance.drawLine(whichViewbox, 100, 0, 200, 0);
		distance.selectRejectfromGSPSRadialMenu();

		panel.enableFiltersInOutputPanel(false,true, false);

		point.selectCutUsingContextMenu(distance.getLinearMeasurements(1, 1).get(0));		
		panel.assertFalse(panel.thumbnailList.isEmpty(),"Checkpoint[5/15]", "verifying that finding is displayed after rejection");
		point.pressKey(NSGenericConstants.DOT_KEY);
		point.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);
		
		panel.enableFiltersInOutputPanel(true, true, false);	
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[6/15]", "verifying the OP is refreshed after paste of rejected group finding");		
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[7/15]","verifying the line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[8/15]","verifying point is displayed");

		for(int i=1;i<=panel.thumbnailList.size();i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[9/15]","verifying the state of findings");

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(whichViewbox, -50,  70, -60,-60);
		panel.enableFiltersInOutputPanel(true, true, true);
		panel.assertEquals(panel.thumbnailList.size(),3 ,"Checkpoint[10/15]", "verifying the OP is refreshed with new finding");

		ellipse.selectEllipse(1, 1);
		ellipse.performCNTRLX();		
		ellipse.pressKey(NSGenericConstants.DOT_KEY);
		ellipse.selectPasteOrCancelUsingContextMenu(1, ViewerPageConstants.PASTE);

		panel.assertEquals(panel.thumbnailList.size(),3 ,"Checkpoint[11/15]", "verifying the has same findings after paste");		

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.ELLIPSE_ATTR),"Checkpoint[12/15]","verifying the ellipse is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(2)).contains(ViewerPageConstants.LINE_ATTR),"Checkpoint[13/15]","verifying the line is displayed");
		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(3)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[14/15]","verifying the point is displayed");


		for(int i=1;i<=panel.thumbnailList.size();i++)
			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[15/15]","verifying the State of findings");



	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test09_03_US2341_TC10120_verifyOPOnCutPasteMachineFinding()throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated when cut and paste is applied on a GSPS. - Cut and Paste.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(srPatientName, 1);


		CircleAnnotation circle = new CircleAnnotation(driver);
		circle.closeNotification();		

		panel = new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/9]", "verifying there is no finding");

		circle.selectCutUsingContextMenu(2,1);
		circle.scrollDownToSliceUsingKeyboard(3);
		circle.selectPasteOrCancelUsingContextMenu(2, ViewerPageConstants.PASTE);

		panel.waitForThumbnailToGetDisplayed();

		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[2/9]", "verifying the op is refreshed after paste pending finding");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[3/9]","verifying circle is displayed in thumbnail");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[4/9]","verifying the state");

		circle.scrollUpToSliceUsingKeyboard(2);
		circle.selectCircle(2, 1);
		circle.selectRejectfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();

		circle.performCNTRLX();
		panel.enableFiltersInOutputPanel(false,true, false);
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[5/9]", "verifying the rejected finding");
		circle.scrollDownToSliceUsingKeyboard(3);
		circle.selectPasteOrCancelUsingContextMenu(2, ViewerPageConstants.PASTE);
		panel.waitForThumbnailToGetDisplayed();

		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[6/9]", "verifying after paste finding is not displayed as its state is changed");
		panel.enableFiltersInOutputPanel(true, true, false);		
		panel.assertEquals(panel.thumbnailList.size(),2,"Checkpoint[7/9]", "verifying the thumbnails are displayed under accepted filter");		

		for(int i=1;i<=panel.thumbnailList.size();i++) {

			panel.assertTrue(panel.verifyAcceptedStateInThumbnail(i),"Checkpoint[8/9]","verifying the state");
			panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(i)).contains(ViewerPageConstants.CIRCLE_ATTR),"Checkpoint[9/9]","verifying the finding is displayed");

		}

	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test09_04_US2341_TC10120_verifyOPOnCutPasteGroupFinding()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Output Panel is updated when cut and paste is applied on a GSPS. - Cut and Paste.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPatientName, 9);

		PointAnnotation point = new PointAnnotation(driver);
		TextAnnotation textAn = new TextAnnotation(driver);
		panel = new OutputPanel(driver);
		panel.doubleClickOnViewbox(9);
		panel.closeNotification();

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/9]", "verifying no finding is displayed");


		point.selectCutUsingContextMenu(point.getHandlesOfPoint(9, 1).get(0));
		point.scrollDownToSliceUsingKeyboard(3);
		point.selectPasteOrCancelUsingContextMenu(9, ViewerPageConstants.PASTE);
		panel.waitForThumbnailToGetDisplayed();
		
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[2/9]", "verifying the OP is refreshed");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POINT_ATTR),"Checkpoint[3/9]","verifying the finding is displayed");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[4/9]","verifying the state for thumbnail");

		// Need to add image comparison

		int currentSlice = point.getCurrentScrollPositionOfViewbox(9);
		point.scrollDownToSliceUsingKeyboard(3);

		panel.clickOnJumpIcon(1);

		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(9, 1), "Checkpoint[5/9]", "verifying the object is highlighed on new slice after jump to icon");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(9, 1, true), "Checkpoint[6/9]", "verifying the object is highlighed on new slice after jump to icon");
		panel.assertEquals(point.getCurrentScrollPositionOfViewbox(9), currentSlice, "Checkpoint[7/9]", "verifying the new scroll where it is being pasted");

		ContentSelector cs = new ContentSelector(driver);
		List<String> results = cs.getSelectedResults();

		cs.selectResultFromSeriesTab(9, results.get(0));
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[8/9]", "verifying the finding after selecting original result");

		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertTrue(panel.thumbnailList.size()>1,"Checkpoint[9/9]", "verifying that original results are displayed");


	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test10_01_US2341_TC10119_verifyEditingPendingRTBumpTool()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the edits made on RT contour is reflected on Output Panel thumbnails. - RT Contours.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameDICOMRT, 1);

		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		ContentSelector cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/7]", "verifying no finding is displayed");		

		panel.enableFiltersInOutputPanel(false, false, true);
		int size = panel.thumbnailList.size();
		panel.clickOnJumpIcon(1);

		BumpToolCircle  bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.moveBumpTool(1, -100,10 ,-150, 10);
		// RT takes time to edit its status
		panel.waitForTimePeriod(3000);
		panel.assertEquals(panel.thumbnailList.size(), size-1, "Checkpoint[2/7]", "verifying that after edit of contours the OP is refreshed");

		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[3/7]", "verifying that contours are displayed under OP");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POLYLINE_ATTR),"Checkpoint[4/7]","verifying contour is displayed");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[5/7]","verifying the state");

		cs.selectResultFromSeriesTab(1, results.get(0));

		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[6/7]", "verifying the OP after selecting original results");		

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), size, "Checkpoint[7/7]", "Verifying original results are displayed");

	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125"})
	public void test10_02_US2341_TC10119_verifyEditingRejectedRTBumpTool()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the edits made on RT contour is reflected on Output Panel thumbnails. - RT Contours.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameDICOMRT, 1);

		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		ContentSelector cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/8]", "No finding is displayed");		

		panel.enableFiltersInOutputPanel(false, false, true);
		int size = panel.thumbnailList.size();
		panel.clickOnJumpIcon(1);
		panel.selectRejectfromGSPSRadialMenu();
		// RT takes time to edit its status
		panel.waitForTimePeriod(3000);
		panel.assertEquals(panel.thumbnailList.size(), size-1, "Checkpoint[2/8]", "verifying that OP is refreshed after change of state from pending to rejected");

		panel.enableFiltersInOutputPanel(false,true , false);
		panel.clickOnJumpIcon(1);

		BumpToolCircle  bumpToolCircle = new BumpToolCircle(driver);
		bumpToolCircle.moveBumpTool(1, -100,10 ,-150, 10);
		// RT takes time to edit its status
		panel.waitForTimePeriod(3000);

		panel.enableFiltersInOutputPanel(false, false,true);
		panel.assertEquals(panel.thumbnailList.size(), size-1, "Checkpoint[3/8]", "verifying that after editing pending findings are same");

		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[4/8]", "updated contour is displayed in Accepted state");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POLYLINE_ATTR),"Checkpoint[5/8]","verifying contour is displayed in thumbnail");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[6/8]","verifying the state");

		cs.selectResultFromSeriesTab(1, results.get(0));
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[7/8]", "Verifying the result after selecting the original result");		

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), size, "Checkpoint[8/8]", "verifying the original results are displayed");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test10_03_US2341_TC10119_verifyEditingPendingRTInterpolation()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the edits made on RT contour is reflected on Output Panel thumbnails. - RT Contours.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameDICOMRT, 1);

		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		ContentSelector cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		panel = new OutputPanel(driver);
		panel.openAndCloseOutputPanel(true);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/6]", "No finding is displayed");		

		Interpolation  inter = new Interpolation(driver);
		int[] coordinates = new int[] {80, -80 , 100, 0, 150, 0};
		
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 30, coordinates),"Checkpoint[2.1/6]","Verifying the interpolation is enabled and blue dots are displayed during interpolation");
			
		// RT takes time to edit its status
		panel.waitForTimePeriod(3000);
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[2.2/6]", "verifying after interpolation OP is refreshed");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POLYLINE_ATTR),"Checkpoint[3/6]","verifying the Contour is displayed");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[4/6]","verifying the state");

		cs.selectResultFromSeriesTab(1, results.get(0));
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[5/6]", "Verifying the result after selecting the original result");		

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), rt.legendOptionsList.size(), "Checkpoint[6/6]", "verifying the original results are displayed");

	}

	@Test(groups ={"Chrome","IE11","Edge","US2341","Positive","F1125","E2E"})
	public void test10_04_US2341_TC10119_verifyEditingRejectedRTInterpolation()throws InterruptedException, AWTException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the edits made on RT contour is reflected on Output Panel thumbnails. - RT Contours.");

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientNameDICOMRT, 1);

		DICOMRT rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad();

		ContentSelector cs = new ContentSelector(driver);
		List<String> results = cs.getAllResults();

		panel = new OutputPanel(driver);		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[1/9]", "No finding is displayed -accepted");		

		panel.enableFiltersInOutputPanel(false,true, false);
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[2/9]", "No finding is displayed - rejected");
		PolyLineAnnotation poly = new PolyLineAnnotation(driver);
		poly.selectPolyline(1, 1, 30);
		panel.selectRejectfromGSPSRadialMenu();
			
		// RT takes time to edit its status
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[3/9]", "OP is refreshed after state change to rejected");

		Interpolation  inter = new Interpolation(driver);
		int[] coordinates = new int[] {80, -80 , 100, 0, 150, 0};		
		inter.assertTrue(inter.performInterPolationOffsetWay(1, 1, 30, coordinates),"Checkpoint[4/9]","Verifying the interpolation is enabled and blue dots are displayed during interpolation");
			
		// RT takes time to edit its status
		panel.waitForTimePeriod(3000);
		panel.enableFiltersInOutputPanel(true, false, false);		
		panel.waitForThumbnailToGetDisplayed();
		panel.assertEquals(panel.thumbnailList.size(),1,"Checkpoint[5/9]","Finding is displayed under correct filter");

		panel.assertTrue(panel.getAllAttributes(panel.getAnnotationFromThumbnail(1)).contains(ViewerPageConstants.POLYLINE_ATTR),"Checkpoint[6/9]","verifying the contour is displayed in thumbnail");
		panel.assertTrue(panel.verifyAcceptedStateInThumbnail(1),"Checkpoint[7/9]","verifying its state");

		cs.selectResultFromSeriesTab(1, results.get(0));
		panel.assertTrue(panel.thumbnailList.isEmpty(),"Checkpoint[8/9]", "Verifying the result after selecting the original result");		

		panel.enableFiltersInOutputPanel(false, false, true);
		panel.assertEquals(panel.thumbnailList.size(), rt.legendOptionsList.size(), "Checkpoint[9/9]", "verifying the original results are displayed");

	}


}