package com.trn.ns.test.viewer.plane;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.CachingLogConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.Triangulation;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerOrientation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TextOverlaysPlaneTest extends TestBase {

	private HelperClass helper;
	private ViewBoxToolPanel viewBoxPanel;
	private ViewerTextOverlays overlays;
	private ViewerOrientation orin;

	private ExtentTest extentTest;

	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String rib = Configurations.TEST_PROPERTIES.get("Rib-AAA-2_Filepath");
	String ribPatient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, rib);


	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test01_US2326_TC9946_verifyOverlayMarkers() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Overlay data is not getting updated before image changes when switching planes.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		orin = new ViewerOrientation(driver);

		orin.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[1/5]","verifying the orientation marker");		
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		overlays.compareElementImage(protocolName, overlays.getViewPort(1), "Checkpoint[2/5]", "TC01_01");
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		overlays.compareElementImage(protocolName, overlays.getViewPort(1), "Checkpoint[3/5]", "TC01_02");
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		overlays.compareElementImage(protocolName, overlays.getViewPort(1), "Checkpoint[4/5]", "TC01_03");
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		overlays.compareElementImage(protocolName, overlays.getViewPort(1), "Checkpoint[5/5]", "TC01_04");




	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test02_US2326_TC9954_verifyDefaultOverlayMarkers() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text overlays with volume rendering - (Default Settings)");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.DEFAULT_ANNOTATION);
		List<String> overlaysValue = overlays.convertWebElementToTrimmedStringList(overlays.allOverlays);
		String zoom = overlays.getZoomLevelValue(1);
		int currentSlice = overlays.getCurrentScrollPositionOfViewbox(1);
		int maxScroll = overlays.getMaxNumberofScrollForViewbox(1);


		verifyOverlaysAfterPlane(ViewerPageConstants.CORONAL_KEY,ViewerPageConstants.DEFAULT_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[1/12]", "Verifying the overlays are same after change too - Default");

		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[2/12]","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[3/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[4/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.SAGITTAL_KEY,ViewerPageConstants.DEFAULT_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[5/12]", "Verifying the overlays are same after change too - Default");
		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[6/12","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[7/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[8/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.AXIAL_KEY,ViewerPageConstants.DEFAULT_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[9/12]", "Verifying the overlays are same after change too - Default ");
		overlays.assertEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[10/12]","Zoom changes after plane change");
		overlays.assertEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[11/12]","Scroll changes after plane change");
		overlays.assertEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[12/12]","total slices changes after plane change");

	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test03_US2326_TC9959_verifyMinimumOverlayMarkers() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text overlays with volume rendering - (Mininum Settings)");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.MINIMUM_ANNOTATION);
		List<String> overlaysValue = overlays.convertWebElementToTrimmedStringList(overlays.allOverlays);
		String zoom = overlays.getZoomLevelValue(1);
		int currentSlice = overlays.getCurrentScrollPositionOfViewbox(1);
		int maxScroll = overlays.getMaxNumberofScrollForViewbox(1);

		verifyOverlaysAfterPlane(ViewerPageConstants.CORONAL_KEY,ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[1/12]", "Verifying the overlays are same after change too - Default");

		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[2/12]","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[3/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[4/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.SAGITTAL_KEY,ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[5/12]", "Verifying the overlays are same after change too - Default");
		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[6/12","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[7/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[8/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.AXIAL_KEY,ViewerPageConstants.MINIMUM_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[9/12]", "Verifying the overlays are same after change too - Default ");
		overlays.assertEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[10/12]","Zoom changes after plane change");
		overlays.assertEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[11/12]","Scroll changes after plane change");
		overlays.assertEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[12/12]","total slices changes after plane change");

	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test04_US2326_TC9960_verifyMaximumOverlayMarkers() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Text overlays with volume rendering - (Full Settings)");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(patientName, username, password, 1);
		overlays = new ViewerTextOverlays(driver);
		overlays.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);		
		List<String> overlaysValue = overlays.convertWebElementToTrimmedStringList(overlays.allOverlays);
		String zoom = overlays.getZoomLevelValue(1);
		int currentSlice = overlays.getCurrentScrollPositionOfViewbox(1);
		int maxScroll = overlays.getMaxNumberofScrollForViewbox(1);


		verifyOverlaysAfterPlane(ViewerPageConstants.CORONAL_KEY,ViewerPageConstants.FULL_ANNOTATION);
		overlays.assertNotEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[1/12]", "Verifying the overlays are same after change too - Default");

		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[2/12]","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[3/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[4/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.SAGITTAL_KEY,ViewerPageConstants.FULL_ANNOTATION);
		overlays.assertNotEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[5/12]", "Verifying the overlays are same after change too - Default");
		overlays.assertNotEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[6/12","Zoom changes after plane change");
		overlays.assertNotEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[7/12]","Scroll changes after plane change");
		overlays.assertNotEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[8/12]","total slices changes after plane change");

		verifyOverlaysAfterPlane(ViewerPageConstants.AXIAL_KEY,ViewerPageConstants.FULL_ANNOTATION);
		overlays.assertEquals(overlays.allOverlays.size(), overlaysValue.size(), "Checkpoint[9/12]", "Verifying the overlays are same after change too - Default ");
		overlays.assertEquals(zoom ,overlays.getZoomLevelValue(1),"Checkpoint[10/12]","Zoom changes after plane change");
		overlays.assertEquals(currentSlice ,overlays.getCurrentScrollPositionOfViewbox(1),"Checkpoint[11/12]","Scroll changes after plane change");
		overlays.assertEquals(maxScroll ,overlays.getMaxNumberofScrollForViewbox(1),"Checkpoint[12/12]","total slices changes after plane change");

	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test05_US2326_TC9964_verifyMetaData() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that original instance meta data is getting used when switching back to native plane.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+patientName+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(rib,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);

		viewBoxPanel.scrollDownToSliceUsingKeyboard(5);
		List<String>  logs = viewBoxPanel.getConsoleLogs();
		Stream<String> logsV = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND));
		logsV.forEach(e ->viewBoxPanel.assertTrue(VerifyNumber(e.substring(e.indexOf("=")+1,e.length()-1).trim()),"Checkpoint[1/3]","RPD/lossy pixel data was found image\" values should be all Positive numbers ( Instance from DB )."));

		viewBoxPanel.doubleClickOnViewbox(1);
		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.clearConsoleLogs();
		viewBoxPanel.scrollDownToSliceUsingKeyboard(5);
		logs = viewBoxPanel.getConsoleLogs();
		logsV = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND));
		logsV.forEach(e ->viewBoxPanel.assertFalse(VerifyNumber(e.substring(e.indexOf("=")+1,e.length()-1).trim()),"Checkpoint[2/3]","RPD/lossy pixel data was found image\" values should be all Negative numbers ( Computed instance )."));

		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewBoxPanel.clearConsoleLogs();
		viewBoxPanel.scrollDownToSliceUsingKeyboard(5);
		logs = viewBoxPanel.getConsoleLogs();

		logsV = logs.stream().filter(e -> e.contains(CachingLogConstants.IMAGE_FOUND));
		logsV.forEach(e ->viewBoxPanel.assertTrue(VerifyNumber(e.substring(e.indexOf("=")+1,e.length()-1).trim()),"Checkpoint[3/3]","RPD/lossy pixel data was found image\" values should be all Positive numbers ( Instance from DB )."));


	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test06_US2326_TC9975_verifyTriangulation() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify triangulation functionality .");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ContentSelector cs = new ContentSelector(driver);

		String series = cs.getSeriesDescriptionOverlayText(1);
		cs.selectSeriesFromSeriesTab(2, series);
		cs.selectSeriesFromSeriesTab(3, series);
		cs.openAndCloseSeriesTab(false);

		viewBoxPanel.selectPlane(2, ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));		
		viewBoxPanel.selectPlane(3, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));

		Triangulation tool=new Triangulation(driver);
		tool.selectTraingulationFromQuickToolbar(1);


		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "When Tirangulation tool moved vertically");
		for(int i=20;i<50;i+=5)
		{
			tool.clickAt(10,i);
			tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[1/4]","Validate cross hair not visible in active viewbox. ");
			for(int j=2;j<=tool.getNumberOfCanvasForLayout();j++)
				tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[2."+j+"/4]", "Validate cross hair visible in viewbox-"+j);
		}

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "When Tirangulation tool moved Horizontally");
		for(int i=20;i<50;i+=5)
		{
			tool.clickAt(i,10);
			tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[3/4]","Validate cross hair not visible in active viewbox. ");
			for(int j=2;j<=tool.getNumberOfCanvasForLayout();j++)
				tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[4."+j+"/4]", "Validate cross hair visible in viewbox-"+j);
		}


	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test07_US2326_TC9977_verifyScrollSync() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Synchronization functionality - Scroll");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ContentSelector cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		ViewerLayout layout = new ViewerLayout(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		for(int i =1; i<=3;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(0));

		for(int i =4; i<=6;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(1));

		for(int i =7; i<=9;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(2));

		cs.openAndCloseSeriesTab(false);

		viewBoxPanel.selectPlane(5, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));		
		viewBoxPanel.selectPlane(8, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));

		List<Integer> scrollPos = new ArrayList<Integer>();

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			scrollPos.add(layout.getCurrentScrollPositionOfViewbox(i));

		layout.mouseHover(layout.getViewPort(1));
		viewBoxPanel.scrollDownToSliceUsingKeyboard(5);

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++) {

			if(viewBoxPanel.getPlaneOverlayText(i).equalsIgnoreCase(ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY)))
				viewBoxPanel.assertNotEquals(scrollPos.get(i-1), layout.getCurrentScrollPositionOfViewbox(i), "Checkpoint[1."+i+"/2]", "Verifying that axial plane are in sync and value changed after scroll");
			else
				viewBoxPanel.assertEquals(scrollPos.get(i-1), layout.getCurrentScrollPositionOfViewbox(i), "Checkpoint[1."+i+"/2]", "Verifying that other plane values are not changed");
		}


		layout.performSyncONorOFF();

		scrollPos = new ArrayList<Integer>();
		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			scrollPos.add(layout.getCurrentScrollPositionOfViewbox(i));
		viewBoxPanel.scrollDownToSliceUsingKeyboard(5);

		for(int i=2;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.assertEquals(scrollPos.get(i-1), layout.getCurrentScrollPositionOfViewbox(i), "Checkpoint[2/2]", "Verifying that after performing sync off other values are not changed");



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test08_01_US2326_TC9978_verifyLocalizerLine() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify localization functionality");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		ViewerPage viewerPage = helper.loadViewerDirectly(ribPatient,username, password,1);

		WebElement line1 = viewerPage.getLocalizerLine(2);
		String vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
		String vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);

		String vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
		String vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


		WebElement line2 = viewerPage.getLocalizerLine(3);
		String vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
		String vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);

		String vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
		String vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);


		for(int j =viewerPage.getCurrentScrollPositionOfViewbox(1); j<viewerPage.getMaxNumberofScrollForViewbox(1);j=j+10)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the  first viewbox using keyboard");
			viewerPage.scrollDownToSliceUsingKeyboard(10);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[1/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[2/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[3/32]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[4/32]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[5/32]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[6/32]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb2");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1))>Double.parseDouble(vb2y1), "Checkpoint[7/32]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2))>Double.parseDouble(vb2y2), "Checkpoint[8/32]", "Verifying that new y2 is greater than old Y2");


			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[9/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[10/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[11/32]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[12/32]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[13/32]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[14/32]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb3");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1))>Double.parseDouble(vb3y1), "Checkpoint[15/32]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2))>Double.parseDouble(vb3y2), "Checkpoint[16/32]", "Verifying that new y2 is greater than old Y2");

			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}

		for(int j =10; j>=1;j--)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling up the  first viewbox using keyboard");
			viewerPage.scrollUpToSliceUsingKeyboard(3);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[17/32]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[18/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[19/32]", "Verifying the y1 coordinate is not same");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[20/32", "Verifying the y2 coordinate is not same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[21/32]", "Verifying the x1 coordinate is same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[22/32]", "Verifying the x2 coordinate is same");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1))<Double.parseDouble(vb2y1), "Checkpoint[23/32]", "Verifying that x1 attribute has not changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2))<Double.parseDouble(vb2y2), "Checkpoint[24/32]", "Verifying that x2 attribute has not changed");

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[25/32]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[26/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[27/32]", "Verifying the y1 coordinate is not same");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[28/32]", "Verifying the y2 coordinate is not same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[29/32]", "Verifying the x1 coordinate is same");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[30/32]", "Verifying the x2 coordinate is same");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1))<Double.parseDouble(vb3y1), "Checkpoint[31/32]", "Verifying that x1 attribute has not changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2))<Double.parseDouble(vb3y2), "Checkpoint[32/32]", "Verifying that x2 attribute has not changed");


			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test08_02_US2326_TC9978_verifyLocalizerLine() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify localization functionality");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		ViewerPage viewerPage = helper.loadViewerDirectly(ribPatient,username, password,1);
		viewerPage.mouseHover(viewerPage.getViewPort(3));

		WebElement line1 = viewerPage.getLocalizerLine(2);
		String vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
		String vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);

		String vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
		String vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


		WebElement line2 = viewerPage.getLocalizerLine(1);
		String vb1y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
		String vb1y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);

		String vb1x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
		String vb1x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);



		for(int j =viewerPage.getCurrentScrollPositionOfViewbox(3); j<viewerPage.getMaxNumberofScrollForViewbox(3);j=j+10)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the  first viewbox using keyboard");
			viewerPage.scrollDownToSliceUsingKeyboard(10);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[1/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[2/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[3/32]", "Verifying that y1 attribute has not changed ");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[4/32]", "Verifying that y2 attribute has not changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[5/32]", "Verifying that x1 attribute has changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[6/32]", "Verifying that x2 attribute has changed  - meaning line is moving vertically in vb2");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1))>Double.parseDouble(vb2x1), "Checkpoint[7/32]", "Verifying that new x1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2))>Double.parseDouble(vb2x2), "Checkpoint[8/32]", "Verifying that new x2 is greater than old Y2");


			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[9/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(1),"Checkpoint[10/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1), vb1y1, "Checkpoint[11/32]", "Verifying that y1 attribute has not changed ");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2), vb1y2, "Checkpoint[12/32]", "Verifying that y2 attribute has not changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1), vb1x1, "Checkpoint[13/32]", "Verifying that x1 attribute has changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2), vb1x2, "Checkpoint[14/32]", "Verifying that x2 attribute has changed  - meaning line is moving vertically in vb1");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1))>Double.parseDouble(vb1x1), "Checkpoint[15/32]", "Verifying that new x1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2))>Double.parseDouble(vb1x2), "Checkpoint[16/32]", "Verifying that new x2 is greater than old Y2");

			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(1);
			vb1y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb1y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb1x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb1x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}

		for(int j =10; j>=1;j--)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling up the  first viewbox using keyboard");
			viewerPage.scrollUpToSliceUsingKeyboard(3);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(2), "Checkpoint[17/32]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(2),"Checkpoint[18/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y1), vb2y1, "Checkpoint[19/32]", "Verifying the y1 coordinate is  same");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.Y2), vb2y2, "Checkpoint[20/32]", "Verifying the y2 coordinate is  same");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1), vb2x1, "Checkpoint[21/32]", "Verifying the x1 coordinate is not same");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2), vb2x2, "Checkpoint[22/32]", "Verifying the x2 coordinate is not same");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X1))<Double.parseDouble(vb2x1), "Checkpoint[23/32]", "Verifying that x1 attribute has changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(2), ViewerPageConstants.X2))<Double.parseDouble(vb2x2), "Checkpoint[24/32]", "Verifying that x2 attribute has changed");

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[25/32]", "Localizer line should present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(1),"Checkpoint[26/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1), vb1y1, "Checkpoint[27/32]", "Verifying the y1 coordinate is  same");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2), vb1y2, "Checkpoint[28/32]", "Verifying the y2 coordinate is  same");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1), vb1x1, "Checkpoint[29/32]", "Verifying the x1 coordinate is not same");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2), vb1x2, "Checkpoint[30/32]", "Verifying the x2 coordinate is not same");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1))<Double.parseDouble(vb1x1), "Checkpoint[31/32]", "Verifying that x1 attribute has changed");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2))<Double.parseDouble(vb1x2), "Checkpoint[32/32]", "Verifying that x2 attribute has changed");


			line1 = viewerPage.getLocalizerLine(2);
			vb2y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb2y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb2x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb2x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


			line2 = viewerPage.getLocalizerLine(1);
			vb1y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb1y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb1x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb1x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test08_03_US2326_TC9978_verifyLocalizerLine() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify localization functionality");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		ViewerPage viewerPage = helper.loadViewerDirectly(ribPatient,username, password,1);
		viewerPage.mouseHover(viewerPage.getViewPort(2));

		WebElement line1 = viewerPage.getLocalizerLine(1);
		String vb1y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
		String vb1y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);

		String vb1x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
		String vb1x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);


		WebElement line2 = viewerPage.getLocalizerLine(3);
		String vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
		String vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);

		String vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
		String vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);



		for(int j =viewerPage.getCurrentScrollPositionOfViewbox(2); j<viewerPage.getMaxNumberofScrollForViewbox(2);j=j+10)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling down the  first viewbox using keyboard");
			viewerPage.scrollDownToSliceUsingKeyboard(10);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[1/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[2/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[3/32]", "Verifying that y1 attribute has not changed ");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[4/32]", "Verifying that y2 attribute has not changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[5/32]", "Verifying that x1 attribute has changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[6/32]", "Verifying that x2 attribute has changed  - meaning line is moving vertically in vb3");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1))<Double.parseDouble(vb3x1), "Checkpoint[7/32]", "Verifying that new x1 is greater than old x1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2))<Double.parseDouble(vb3x2), "Checkpoint[8/32]", "Verifying that new x2 is greater than old x2");


			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[9/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(1),"Checkpoint[10/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1), vb1y1, "Checkpoint[11/32]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2), vb1y2, "Checkpoint[12/32]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1), vb1x1, "Checkpoint[13/32]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2), vb1x2, "Checkpoint[14/32]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb1");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1))<Double.parseDouble(vb1y1), "Checkpoint[15/32]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2))<Double.parseDouble(vb1y2), "Checkpoint[16/32]", "Verifying that new y2 is greater than old Y2");

			line1 = viewerPage.getLocalizerLine(1);
			vb1y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb1y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb1x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb1x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);



			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}

		for(int j =10; j>=1;j--)
		{
			ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Scrolling up the  first viewbox using keyboard");
			viewerPage.scrollUpToSliceUsingKeyboard(3);

			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(3), "Checkpoint[17/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(3),"Checkpoint[18/32]","Verifying the line is of blue color");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y1), vb3y1, "Checkpoint[19/32]", "Verifying that y1 attribute has not changed ");	
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.Y2), vb3y2, "Checkpoint[20/32]", "Verifying that y2 attribute has not changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1), vb3x1, "Checkpoint[21/32]", "Verifying that x1 attribute has changed ");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2), vb3x2, "Checkpoint[22/32]", "Verifying that x2 attribute has changed  - meaning line is moving vertically in vb3");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X1))>Double.parseDouble(vb3x1), "Checkpoint[23/32]", "Verifying that new x1 is greater than old x1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(3), ViewerPageConstants.X2))>Double.parseDouble(vb3x2), "Checkpoint[24/32]", "Verifying that new x2 is greater than old x2");


			viewerPage.assertTrue(viewerPage.verifyLocalizerLinePresence(1), "Checkpoint[25/32]", "Localizer line should  be present.");
			viewerPage.assertTrue(viewerPage.verifyLocalizerLineColor(1),"Checkpoint[26/32]","Verifying the line is of blue color");
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1), vb1y1, "Checkpoint[27/32]", "Verifying that y1 attribute has changed ");	
			viewerPage.assertNotEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2), vb1y2, "Checkpoint[28/32]", "Verifying that y2 attribute has changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X1), vb1x1, "Checkpoint[29/32]", "Verifying that x1 attribute has not changed ");
			viewerPage.assertEquals(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.X2), vb1x2, "Checkpoint[30/32]", "Verifying that x2 attribute has not changed  - meaning line is moving horizontally in vb1");

			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y1))>Double.parseDouble(vb1y1), "Checkpoint[31/32]", "Verifying that new y1 is greater than old Y1");	
			viewerPage.assertTrue(Double.parseDouble(viewerPage.getAttributeValue(viewerPage.getLocalizerLine(1), ViewerPageConstants.Y2))>Double.parseDouble(vb1y2), "Checkpoint[32/32]", "Verifying that new y2 is greater than old Y2");

			line1 = viewerPage.getLocalizerLine(1);
			vb1y1= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y1);
			vb1y2= viewerPage.getAttributeValue(line1, ViewerPageConstants.Y2);
			vb1x1= viewerPage.getAttributeValue(line1, ViewerPageConstants.X1);
			vb1x2= viewerPage.getAttributeValue(line1, ViewerPageConstants.X2);



			line2 = viewerPage.getLocalizerLine(3);
			vb3y1= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y1);
			vb3y2= viewerPage.getAttributeValue(line2, ViewerPageConstants.Y2);
			vb3x1= viewerPage.getAttributeValue(line2, ViewerPageConstants.X1);
			vb3x2= viewerPage.getAttributeValue(line2, ViewerPageConstants.X2);

		}



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test09_US2326_TC9997_verifyZoomSync() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Synchronization functionality - Zoom");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ContentSelector cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		ViewerLayout layout = new ViewerLayout(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		for(int i =1; i<=3;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(0));

		for(int i =4; i<=6;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(1));

		for(int i =7; i<=9;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(2));

		cs.openAndCloseSeriesTab(false);

		viewBoxPanel.selectPlane(5, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));		
		viewBoxPanel.selectPlane(8, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));

		List<Integer> zoomVal = new ArrayList<Integer>();

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			zoomVal.add(viewBoxPanel.getZoomValue(i));

		layout.click(layout.getViewPort(1));
		viewBoxPanel.changeZoomNumber(1, 80);
		viewBoxPanel.waitForTimePeriod(3000); // it takes time to reflect for non native planes

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++) {

			if(viewBoxPanel.getPlaneOverlayText(i).equalsIgnoreCase(ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY)))
				viewBoxPanel.assertNotEquals(zoomVal.get(i-1), viewBoxPanel.getZoomValue(i), "Checkpoint[1/3]", "Verifying the sync after same plane change for some viewbox");
			else
				viewBoxPanel.assertEquals(zoomVal.get(i-1), viewBoxPanel.getZoomValue(i), "Checkpoint[2/3]", "verifying the sync for viewbox where plane is different");
		}


		layout.performSyncONorOFF();

		zoomVal = new ArrayList<Integer>();
		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			zoomVal.add(viewBoxPanel.getZoomValue(i));
		viewBoxPanel.changeZoomNumber(1, 80);

		for(int i=2;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.assertEquals(zoomVal.get(i-1), viewBoxPanel.getZoomValue(i), "Checkpoint[3/3]", "verifying the sync when sync is off");



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test10_US2326_TC9999_verifyWWWLSync() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Synchronization functionality - WW/WL");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ContentSelector cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		ViewerLayout layout = new ViewerLayout(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		for(int i =1; i<=3;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(0));

		for(int i =4; i<=6;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(1));

		for(int i =7; i<=9;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(2));

		cs.openAndCloseSeriesTab(false);

		viewBoxPanel.selectPlane(5, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));		
		viewBoxPanel.selectPlane(8, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));

		List<Integer> wwlVal = new ArrayList<Integer>();

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			wwlVal.add(viewBoxPanel.getValueOfWindowWidth(i));

		layout.click(layout.getViewPort(1));
		layout.selectWindowLevelFromQuickToolbar(1);
		layout.dragAndReleaseOnViewer(layout.getViewbox(1),0, 0, 100, -100);

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.assertNotEquals(wwlVal.get(i-1), viewBoxPanel.getValueOfWindowWidth(i), "Checkpoint[1/2]", "verifying the WWWL sync after plane change");

		layout.performSyncONorOFF();

		wwlVal = new ArrayList<Integer>();
		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++)
			wwlVal.add(viewBoxPanel.getValueOfWindowWidth(i));
		layout.dragAndReleaseOnViewer(layout.getViewbox(1),0, 0, 100, -100);

		for(int i=2;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.assertEquals(wwlVal.get(i-1), viewBoxPanel.getValueOfWindowWidth(i), "Checkpoint[2/2]", "verifying the WWWL sync when sync is off");



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153","E2E"})
	public void test10_US2326_TC10004_verifyOneUpAndLayoutChange() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify loading series from One up vs from contentSelector.");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);


		viewBoxPanel.selectPlane(1, ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.doubleClickOnViewbox(1);

		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[1/4] : One up scenario", "TC10_01");

		viewBoxPanel.doubleClickOnViewbox(1);		
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[2/4] : reset layout", "TC10_02");

		String series = viewBoxPanel.getSeriesDescriptionOverlayText(2);
		ViewerLayout layout = new ViewerLayout(driver);		

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[3/4] : 3x3 layout", "TC10_03");


		ContentSelector cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(1, series);
		viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(1), "Checkpoint[4/4] : after CS selection ", "TC10_04");


	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test11_US2326_TC9998_verifyPANSync() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Synchronization functionality - Pan");
		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );


		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);
		viewBoxPanel=new ViewBoxToolPanel(driver);
		ContentSelector cs = new ContentSelector(driver);
		List<String> series = cs.getAllSeries();
		ViewerLayout layout = new ViewerLayout(driver);

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		for(int i =1; i<=3;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(0));

		for(int i =4; i<=6;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(1));

		for(int i =7; i<=9;i++)
			cs.selectSeriesFromSeriesTab(i, series.get(2));

		cs.openAndCloseSeriesTab(false);

		viewBoxPanel.selectPlane(5, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));		
		viewBoxPanel.selectPlane(8, ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));


		layout.click(layout.getViewPort(1));
		layout.selectPanFromQuickToolbar(1);
		layout.dragAndReleaseOnViewer(layout.getViewbox(1),0, 0, 100, -100);

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(i), "Checkpoint[1/2] : Pan sync", "TC11_1_"+i);


		layout.performSyncONorOFF();

		layout.dragAndReleaseOnViewer(layout.getViewbox(1),0, 0, 100, -100);

		for(int i=1;i<layout.getNumberOfCanvasForLayout();i++) 
			viewBoxPanel.compareElementImage(protocolName, viewBoxPanel.getViewPort(i), "Checkpoint[2/2] : Pan Sync when sync off", "TC11_2_"+i);



	}

	@Test(groups ={"IE11","Chrome","Edge","US2326","Positive","F1153"})
	public void test12_US2326_TC9991_verifyCachingWhenSwitchingPlanes() throws InterruptedException, AWTException, SQLException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Caching when changing planes.");

		//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+ribPatient+"in viewer" );

		helper = new HelperClass(driver);
		helper.loadViewerDirectly(ribPatient,username, password,1);


		viewBoxPanel=new ViewBoxToolPanel(driver);
		orin = new ViewerOrientation(driver);
		ContentSelector cs=new ContentSelector(driver);
		List<String> allSeries=cs.getAllSeries();            
		cs.waitForTimePeriod(6000);

		List<String> logs = viewBoxPanel.getConsoleLogs();

		DatabaseMethods db=new DatabaseMethods(driver);        
		for(String series : allSeries) {
			String seriesUID=db.getSeriesInstanceUID(ribPatient, series);   
			long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for "+seriesUID)).count();
			viewBoxPanel.assertEquals(count, 1, "Checkpoint[1/7]", "Verifying worker completed caching active viewbox");
		}

		String seriesUID=db.getSeriesInstanceUID(ribPatient, allSeries.get(0));  
		cs.clearConsoleLogs();
		viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.CORONAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.CORONAL_KEY),"Checkpoint[2/7]","verifying the default plane change to "+ViewerPageConstants.CORONAL_KEY);

		//In order to get cache completed message we have to wait this much time.
		cs.waitForTimePeriod(10000);
		logs = cs.getConsoleLogs();
		long count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for {\"s\":\""+seriesUID)).count();
		viewBoxPanel.assertEquals(count, 1, "Checkpoint[3/7]", "Verifying worker completed caching active viewbox");

		cs.clearConsoleLogs();
		viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.SAGITTAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.SAGITTAL_KEY),"Checkpoint[4/7]","verifying the default plane change to "+ViewerPageConstants.SAGITTAL_KEY);
		cs.waitForTimePeriod(10000);
		logs = cs.getConsoleLogs();
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for {\"s\":\""+seriesUID)).count();
		viewBoxPanel.assertEquals(count, 1, "Checkpoint[5/7]", "Verifying worker completed caching active viewbox");

		cs.clearConsoleLogs();
		viewBoxPanel.selectPlane(1,  ViewerPageConstants.PLANES.get(ViewerPageConstants.AXIAL_KEY));
		viewBoxPanel.assertTrue(orin.verifyOrientationForPlane(1,ViewerPageConstants.AXIAL_KEY),"Checkpoint[6/7]","verifying the default plane change to "+ViewerPageConstants.AXIAL_KEY);
		cs.waitForTimePeriod(10000);
		logs = cs.getConsoleLogs();
		count = logs.stream().filter(e -> e.contains(CachingLogConstants.CACHE_COMPLETED+" for {\"s\":\""+seriesUID)).count();
		viewBoxPanel.assertEquals(count,0, "Checkpoint[7/7]", "Verifying caching is not happening when we switch back to native plane .");

	}

	private boolean VerifyNumber(String number) {

		return Integer.parseInt(number)>0;
	}


	private void verifyOverlaysAfterPlane(String plane, String overlay) throws InterruptedException {

		String patientID = overlays.getPatientIDOverlayText(1);
		String studyTime = overlays.getStudyDateTimeOverlayText(1);
		String series = overlays.getSeriesDescriptionOverlayText(1);

		
		String patientName = "", sex ="",ww="",wc="";
		if(!overlay.equalsIgnoreCase(ViewerPageConstants.MINIMUM_ANNOTATION)) {
			patientName = overlays.getPatientNameOverlayText(1);
			sex = overlays.getPatientSexOverlayText(1);
			ww = overlays.getWindowWidthValText(1);
			wc = overlays.getWindowCenterValueOverlayText(1);

		}
	
		overlays.selectPlane(1, ViewerPageConstants.PLANES.get(plane));

		overlays.assertEquals(patientID ,overlays.getPatientIDOverlayText(1),"SubCheckpoint[a]","Verifying patient ID is same after plane change");
		overlays.assertEquals(series,overlays.getSeriesDescriptionOverlayText(1),"SubCheckpoint[b]","Series desc is same after plane change");
		overlays.assertEquals(studyTime ,overlays.getStudyDateTimeOverlayText(1),"SubCheckpoint[c]","Study date time is same after plane change");
		
		if(!overlay.equalsIgnoreCase(ViewerPageConstants.MINIMUM_ANNOTATION)) {
			overlays.assertEquals(sex ,overlays.getPatientSexOverlayText(1),"SubCheckpoint[d]","Patient sex is same after plane change");
			overlays.assertEquals(patientName , overlays.getPatientNameOverlayText(1),"SubCheckpoint[e]","Patient name is same after plane change");
			overlays.assertEquals(ww ,overlays.getWindowWidthValText(1),"SubCheckpoint[f]","WW is same after plane change");
			overlays.assertEquals(wc ,overlays.getWindowCenterValueOverlayText(1),"SubCheckpoint[g]","WC is same after plane change");
		}

	}



}
