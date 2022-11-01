package com.trn.ns.test.viewer.synchronization;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.HelperClass;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

//Regression Document - Functional.NS.I15 Synchronization-CF0304ARevD - revision-0
@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ScrollSyncFromActiveOverlay extends TestBase{

	private ViewerPage viewerpage;
	private ExtentTest extentTest;


	String filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);

	String filePath3 = Configurations.TEST_PROPERTIES.get("Imbio_Density_CTLung_Doe^Lilly_Filepath");
	String doeLillypatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath3);

	String filePath1 = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String ah4patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
	
	String filePath2 = Configurations.TEST_PROPERTIES.get("TeraRecon_BrainTDA_filepath");
	String tda = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);
	
	private String filePath4 = Configurations.TEST_PROPERTIES.get("AH4^sameOrie^diffFRUID_filepath");
	private String ah4_patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath4);
	private HelperClass helper;
	
	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	//TC1063 - Active overlay manual input of Image number (slice position) and synchronization - Direct Input Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover
	@Test(groups ={"firefox","Chrome","IE11","US339","US289","Sanity","BVT"})
	public void test01_US339_TC1063_US289_TC2374_verifyImageOverlayByManualInput() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of Image number (slice position) and synchronization - Direct Input Coverage"
				+ "<br> Verify scroll synchronization with series having same FrameReferenceUID and same Orientation");
		
		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName, username,password, 1);
		ViewerSliderAndFindingMenu findingMenu=new ViewerSliderAndFindingMenu(driver);
		
		for(int i = 1 ; i<=2 ;i++){

			String beforeImageValueViewbox = viewerpage.getCurrentScrollPosition(i);
              
			//Step 1 - verifying that Image values are changed for sync series for valid value
			viewerpage.mouseHover(findingMenu.getSliderLine(i));
			viewerpage.scrollToImage(i, 80);
			String currentImageValueFirstViewbox = viewerpage.getCurrentScrollPosition(i);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[1] & Checkpoint TC3[3] & Checkpoint[2/5]", "Verifying that Image values are changed for sync series after inputing values in input box" );
			viewerpage.assertNotEquals(beforeImageValueViewbox, viewerpage.getCurrentScrollPosition(i), "Verify that Image are changes to new values", "Image values are not same as previous");
			compareImageValues(i,true);
/*
			//Step 2 - verifying that Image values are not changed for sync series for invalid value
			viewerpage.scrollToImage(i, 200);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[2] & Checkpoint[3/5]", "Verifying that Image values are not changed for any series after inputing invalid values in input box" );
			viewerpage.assertEquals(currentImageValueFirstViewbox,  viewerpage.getCurrentScrollPosition(i), "Verify that Image are not changes to new values", "Image values are same as previous");
			compareImageValues(i, true);
*/
			//Step 3 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Before Sync Values -
			String beforeImageSyncFirstViewboxValue = viewerpage.getCurrentScrollPosition(i);

			//Perform manual input of Image number
			viewerpage.scrollToImage(i, 30);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[4] & Checkpoint[4/5]", "Verifying that the Image values are not same after inputing different values for Image input box for images not in sync" );
			//Verifying that the Image values are changed only for first viewbox
			viewerpage.assertNotEquals(beforeImageSyncFirstViewboxValue, viewerpage.getCurrentScrollPosition(i), "Verify that the Image values are changes to new values", "Image values are not same as previous");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC3[5] & Checkpoint[5/5]", "Verifying that the Image values are not change for series not in sync" );
			compareImageValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			viewerpage.scrollToImage(i, 1);
		}

	}

	//TC1064 - Active overlay manual input of Image number (slice position) and synchronization - Scrolling Tool Coverage
	//Limitation : This test script dosn't work in edge because of mouse move event - MouseHover and also doesn't work in IE11 because of DE397
	@Test(groups ={"firefox","Chrome","IE11","US339","Sanity","BVT"})
	public void test02_US339_TC1064_verifyImageOverlayByContextMenu() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Active overlay manual input of Image number (slice position) and synchronization - Scrolling Tool Coverage for viewbox");
				
		//Loading the patient on viewer
		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(patientName,username,password,1);

		// Enable the Scroll icon 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Performing the right click and enabling the scroll from Radial Menu for first series" );
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(1));	

		for(int i = 1 ; i<=2 ;i++){

			//Step1 - change the Image value from Scroll tool
			String defaultImageValueFirstViewbox = viewerpage.getCurrentScrollPosition(i);

			//Applying the Scroll
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(i), 0, 0, 0, 20);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[1] & Checkpoint TC4[3] & Checkpoint[3/4]", "Verifying Scroll icon is selected and applying the Scroll in first viewbox" );
			viewerpage.assertTrue(Integer.parseInt(defaultImageValueFirstViewbox) < Integer.parseInt(viewerpage.getCurrentScrollPosition(i)), "verifying Image level percentange", "Image value was "+defaultImageValueFirstViewbox+". After Scroll, value is ="+viewerpage.getCurrentScrollPosition(i));
			compareImageValues(i,true);

			//Step4 - After pressing space bar - Without sync-
			String afterImageValueFirstViewbox = viewerpage.getCurrentScrollPosition(i);
			//performing sync off
			viewerpage.performSyncONorOFF();

			//Applying the Scroll
			viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(i),0, 0, 0, 30);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint TC4[2] & Checkpoint[4/4]", "Verifying Scroll icon is selected and applying the scroll in first viewbox for images not in sync" );
			viewerpage.assertTrue(Integer.parseInt(afterImageValueFirstViewbox) < Integer.parseInt(viewerpage.getCurrentScrollPosition(i)), "verifying Image level percentange", "Image value was "+defaultImageValueFirstViewbox+". After Scroll, value is ="+viewerpage.getCurrentScrollPosition(i));
			compareImageValues(i,false);

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			viewerpage.scrollToImage(i, 1);
		}
	}
	
	@Test(groups ={"firefox","Chrome","IE11","US289","DE1874","Positive","BVT"})
	public void test03_US289_TC2375_DE1874_TC7503_verifyScrollSyncForDiffUIDandSameOrientation() throws InterruptedException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify scroll synchronization with series having different FrameReferenceUID and same Orientation. <br>"+
		"[Risk and Impact]: Verify the keyboard shortcuts, when user loaded the viewer page");
		
		//Loading the patient on viewer

		helper = new HelperClass(driver);
		viewerpage = helper.loadViewerDirectly(ah4_patientName,username,password, 1);
		ViewerSliderAndFindingMenu findingMenu=new ViewerSliderAndFindingMenu(driver);

		for(int i = 1 ; i<=2 ;i++){
			viewerpage.mouseHover(findingMenu.getSliderLine(i));
			String beforeImageValueViewbox = viewerpage.getCurrentScrollPosition(i);

			//Step 1 - verifying that Image values are changed for sync series for valid value
			viewerpage.scrollToImage(i , 10);
			String currentImageValueFirstViewbox = viewerpage.getCurrentScrollPosition(i);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verifying that Image values are changed for sync series after inputing values in input box" );
			viewerpage.assertNotEquals(beforeImageValueViewbox, viewerpage.getCurrentScrollPosition(i), "Verify that Image are changes to new values", "Image values are not same as previous");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
/*
			//Step 2 - verifying that Image values are not changed for sync series for invalid value
			viewerpage.scrollToImage(i, 35);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying that Image values are not changed for any series after inputing invalid values in input box" );
			viewerpage.assertEquals(currentImageValueFirstViewbox,  viewerpage.getCurrentScrollPosition(i), "Verify that Image are not changes to new values", "Image values are same as previous");
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
		*/	

			//Step 3 - After pressing space bar - Without sync-
			//performing sync off
			viewerpage.performSyncONorOFF();
			//Before Sync Values -
			String beforeImageSyncFirstViewboxValue = viewerpage.getCurrentScrollPosition(i);

			//Perform manual input of Image number
			viewerpage.scrollToImage(i, 30);

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verifying that the Image values are not same after inputing different values for Image input box for images not in sync" );
			//Verifying that the Image values are changed only for first viewbox
			viewerpage.assertNotEquals(beforeImageSyncFirstViewboxValue, viewerpage.getCurrentScrollPosition(i), "Verify that the Image values are changes to new values", "Image values are not same as previous");

			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying that the Image values are not change for series not in sync" );
			viewerpage.assertNotEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			

			//performing sync ON and Reset to original values
			viewerpage.performSyncONorOFF();
			viewerpage.scrollToImage(i, 15);
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "verifying both the series are synced", "verified");
			
		}

	}
	
	//To compare the updated values with other series values
	private void compareImageValues(int viewNum, Boolean validOrInvalid){
		for(int j=1 ;j<=4; j++){
			if(j!=viewNum){
				if(validOrInvalid){
					viewerpage.assertEquals(viewerpage.getCurrentScrollPosition(viewNum), viewerpage.getCurrentScrollPosition(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
							"Image values are "+viewerpage.getCurrentScrollPosition(viewNum)+" and "+viewerpage.getCurrentScrollPosition(j)+" ");
				}else{
					viewerpage.assertNotEquals(viewerpage.getCurrentScrollPosition(viewNum), viewerpage.getCurrentScrollPosition(j), "Verify that Image values for sync series"+viewNum+" and series"+j+" ", 
							"Image values are "+viewerpage.getCurrentScrollPosition(viewNum)+" and "+viewerpage.getCurrentScrollPosition(j)+" ");
				}
			}
		}
	}
}
