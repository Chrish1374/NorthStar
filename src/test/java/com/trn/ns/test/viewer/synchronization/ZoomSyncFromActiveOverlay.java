package com.trn.ns.test.viewer.synchronization;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0
//Functional.NS.I29_Viewer-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ZoomSyncFromActiveOverlay extends TestBase{

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerpage;
	private ExtentTest extentTest;
	private ViewBoxToolPanel preset;
	private ViewerLayout layout;
	
	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String PatientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath1);

	String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);

	//Get Patient Name
	String filePath2 = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
	String subject60Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");
	private HelperClass helper;

	


	//TC1067 - Active overlay manual input of zoom percentage or select from zoom options and synchronization - Zoom Tool Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover and also doesn't work in IE11 because of DE397
	@Test(groups ={"firefox","Chrome","IE11","US340"})
	public void test01_US340_TC1067_verifyZoomOverlayByToolCoverage() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of zoom percentage or select from zoom options and synchronization - Zoom Tool Coverage");
		//Loading the patient on viewer
		
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);
        preset=new ViewBoxToolPanel(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Loading the Patient "+liver9PatientName+" in viewer" );
		
		// Enable the Window level icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Performing the right click and enabling the zoom from Radial Menu" );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));	

		for(int i = 1 ; i<=2 ;i++){

			//Step1 - change the Zoom value from zoom tool
			String defaultZoomValueFirstViewbox = preset.getZoomLevelValue(i);

			//Applying the zoom
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i),0, 0, 0, -30);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC7[1] & Checkpoint TC7[2] & Checkpoint[3/4]", "Verifying zoom icon is selected and applying the zoom in first viewbox" );
			viewerpage.assertTrue(Integer.parseInt(defaultZoomValueFirstViewbox) < preset.getZoomValue(i), "verifying zoom level percentange", "Image zoom percentage was "+defaultZoomValueFirstViewbox+". After zoom, percentage is ="+preset.getZoomLevelValue(i));
			compareZoomValues(i,true);

			//Step4 - After pressing space bar - Without sync-
			String afterZoomValueFirstViewbox = preset.getZoomLevelValue(i);
			//performing sync off
			viewerpage.performSyncONorOFF();

			//Applying the zoom
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i),0, 0, 0, -50);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC7[3] & Checkpoint[4/4]", "Verifying zoom icon is selected and applying the zoom in first viewbox for images not in sync" );
			viewerpage.assertTrue(Integer.parseInt(afterZoomValueFirstViewbox) < preset.getZoomValue(i), "verifying zoom level percentange", "Image zoom percentage was "+defaultZoomValueFirstViewbox+". After zoom, percentage is ="+preset.getZoomLevelValue(i));
			compareZoomValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			preset=new ViewBoxToolPanel(driver);
			preset.changeZoomNumber(i,90);

		}
	}

	@Test(groups ={"firefox","Chrome","IE11"})
	public void test02_US622_TC2087_verifyZoomAfterCineAndScroll() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of zoom percentage or select from zoom options and synchronization - Context Menu Coverage ");

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(liver9PatientName,username, password, 1);

        preset=new ViewBoxToolPanel(driver);
		String beforeZoomValue = preset.getZoomLevelValue(1);

		preset.changeZoomNumber(1, 100);

		String afterZoomValue = preset.getZoomLevelValue(1);

		viewerpage.assertNotEquals(beforeZoomValue, afterZoomValue, "Verify that zoom is enabled and applied on series", "Zoom is enabled and applied on series");

		viewerpage.selectCineFromQuickToolbar(viewerpage.getViewPort(1));

		int currentImage = viewerpage.getCurrentScrollPositionOfViewbox(1);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/goldImages/cineplayImage.png");

		viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/cineplayImage.png");
		int imageAfterCine = viewerpage.getCurrentScrollPositionOfViewbox(1);
		viewerpage.assertNotEquals(currentImage, imageAfterCine, "verifying the cine play is stopped", "cine is stopped and working fine");

		String expectedImagePath = newImagePath+"/goldImages/cineplayImage.png";
		String actualImagePath = newImagePath+"/actualImages/cineplayImage.png";
		String diffImagePath = newImagePath+"/actualImages/cineplayImage.png";

		boolean cpStatus =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerpage.assertFalse(cpStatus, "The actual and Expected image are same.","");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Verifying cine is played on WWL images", "Successfully verified checkpoint with image comparison.<br>Image name is cineplayImage.png");

		//performing scroll
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(1), 0, 0, 10, 50);

		//Capturing default values for window center and window width value and verifying that the WWWL retains after Cine
		for(int i = 1; i<5; i++)
		{
			viewerpage.assertEquals(preset.getZoomLevelValue(i) , afterZoomValue, "Verify the Zoom is retained after Cine and Scroll", "Zoom is retained after Cine and Scroll");		
		}

	}

	//To compare the updated values with other series values
	private void compareZoomValues(int viewNum, Boolean validOrInvalid){
		preset=new ViewBoxToolPanel(driver);
		for(int j=1 ;j<=4; j++){
			if(j!=viewNum){
				if(validOrInvalid){
					viewerpage.assertEquals(preset.getZoomLevelValue(viewNum), preset.getZoomLevelValue(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
							"Image values are "+preset.getZoomLevelValue(viewNum)+" and "+preset.getZoomLevelValue(j)+" ");
				}else{
					viewerpage.assertNotEquals(preset.getZoomLevelValue(viewNum), preset.getZoomLevelValue(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
							"Image values are "+preset.getZoomLevelValue(viewNum)+" and "+preset.getZoomLevelValue(j)+" ");
				}
			}
		}
	}

	@Test(groups ={"firefox","Chrome","IE11","US289"})
	public void test03_US289_TC2375_TC2413_verifyZoomOverlayByManualInputforSameOrientation() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having different FrameReferenceUID and same Orientation."
				+ "<br> Verify Sync ON/OFF for Zoom.");

			//Loading the patient on viewer
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Loading the Patient "+ah4_patientName+" in viewer" );
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ah4_patientName,username, password, 1);
		preset=new ViewBoxToolPanel(driver);
		for(int i = 1 ; i<=2 ;i++){
			String beforeZoomValueFirstViewbox = preset.getZoomLevelValue(i);

			//Step 1 - verifying that zoom values are changed for sync series for valid value
			viewerpage.mouseHover(viewerpage.getViewPort(i));
			preset= new ViewBoxToolPanel(driver);
			preset.changeZoomNumber(i,100);
			String currentZoomValue = preset.getZoomLevelValue(i);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying that zoom values are changed for sync series after inputing values in input box" );
			viewerpage.assertNotEquals(beforeZoomValueFirstViewbox,  preset.getZoomLevelValue(i), "Verify that zoom are changes to new values", "Before zoom value is "+beforeZoomValueFirstViewbox+"  after appying zoom the updated value is "+preset.getZoomLevelValue(i)+"");
			viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same");


			//Step 2 - verifying that zoom values are not changed for sync series for invalid value
			preset.click(preset.zoomMinIcon);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying that zoom values are not changed for any series after inputing invalid values in input box" );
			viewerpage.assertEquals(currentZoomValue,  preset.getZoomLevelValue(i), "Verify that zoom are not changes to new values", "Zoom values are "+currentZoomValue+" and "+preset.getZoomLevelValue(i)+" ");
			viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same");


			//Step 3 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Before Sync Values -
			String beforeZoomSyncValue = preset.getZoomLevelValue(i);

			//Perform zoom
			preset.changeZoomNumber(i,150);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying that the zoom values are not same after inputing different values for zoom input box for images not in sync" );
			//Verifying that the Zoom values are changed only for first viewbox
			viewerpage.assertNotEquals(beforeZoomSyncValue, preset.getZoomLevelValue(i), "Verify that the zoom values are changes to new values", "Zoom values before zoom "+beforeZoomSyncValue+" and after zoom is "+preset.getZoomLevelValue(i)+" ");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying that the zoom values are not change for series not in sync" );
			//Verifying that the Zoom values are not changed for other viewbox
			viewerpage.assertNotEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same");


			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			preset.changeZoomNumber(i,120);
			viewerpage.assertEquals(preset.getZoomLevelValue(1),preset.getZoomLevelValue(2),"Check Zoom Level on both View Boxes remain Same","The Zoom level on both View Boxes is Same");

		}
	}
}
