package com.trn.ns.test.viewer.loadingAndDataFormatSupport;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class SliceOrderTest extends TestBase {
	private ViewerPage viewerPage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	int totalSlices,currentSlice;
	private HelperClass helper;
	private ViewerTextOverlays textOverlay;


	// Get Patient Name

	String filePathAidocMachine = Configurations.TEST_PROPERTIES.get("AIDOC_MACHINE_filepath");
	String GSPS_PatientName_Aidoc_machine = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePathAidocMachine);

	String MRLSP_filePath=Configurations.TEST_PROPERTIES.get("MR_LSP_filepath");
	String LSPPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,MRLSP_filePath);

	String filePath = Configurations.TEST_PROPERTIES.get("AH.4_filepath");
	String aH4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("cpu_test_Filepath");
	String cpuTestPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	private ContentSelector contentSelector;

	String username = Configurations.TEST_PROPERTIES.get("nsUserName");
	String password = Configurations.TEST_PROPERTIES.get("nsPassword");

	//BeforeMethod is not available as using the code for restarting the IISServer
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() throws SQLException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
		patientPage = new PatientListPage(driver);
	}

	@Test(groups ={"Chrome","IE11","Edge","US1616","Positive","BVT","E2E","F306"})
	public void test1_US1616_TC8421_VerifyZIndexIsDecendingOnAxialPlane() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Z index is in descending order when user performs a mouse scroll down on a axial plane.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(LSPPatientName, 1);
		
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		totalSlices=viewerPage.getMaxNumberofScrollForViewbox(4);
		viewerPage.scrollToImage(4, 1);
		List<Double> baseList = new ArrayList<Double>();
		for(int i=1;i<=totalSlices;i++){

			baseList.add(viewerPage.getIndexValue(4,ViewerPageConstants.Z_INDEX));
			viewerPage.scrollDownToSliceUsingKeyboard(1);
		}
		List<Double> sortedList = new ArrayList<Double>(baseList);
		Collections.sort(sortedList,Collections.reverseOrder());
		viewerPage.assertEquals(baseList,sortedList, "Checkpoint[1/1]","Verified Z index decreases on mouse scroll down at axial plane");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1616","Positive","BVT","E2E","F306"})
	public void test2_US1616_TC8431_VerifyYIndexIsDecendingOnCoronalPlane() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Y index is in descending order when user performs a mouse scroll down on a coronal plane..");
		
		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Aidoc_machine, 1);
		

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		viewerPage.scrollDownToSliceUsingKeyboard(1);

		totalSlices=viewerPage.getMaxNumberofScrollForViewbox(2);
		viewerPage.scrollToImage(2, 1);
		List<Double> baseList = new ArrayList<Double>();
		for(int i=1;i<=totalSlices;i++){

			baseList.add(viewerPage.getIndexValue(2,ViewerPageConstants.Y_INDEX));
			viewerPage.scrollDownToSliceUsingKeyboard(1);
		}
		List<Double> sortedList = new ArrayList<Double>(baseList);
		Collections.sort(sortedList,Collections.reverseOrder());
		viewerPage.assertEquals(baseList,sortedList, "Checkpoint[1/1]","Verified that Y index decreases on mouse scroll down at coronal plane");

	}	
	
	@Test(groups ={"Chrome","IE11","Edge","US1616","Positive","BVT","E2E","F306"})
	public void test3_US1616_TC8432_VerifyXIndexIsAscendingOnSagittalPlane() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that X index is in ascending order when user performs a mouse scroll down on a sagittal plane.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(GSPS_PatientName_Aidoc_machine, 1);

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		totalSlices=viewerPage.getMaxNumberofScrollForViewbox(3);
		viewerPage.scrollToImage(3, 1);

		List<Double> baseList = new ArrayList<Double>();
		for(int i=1;i<=totalSlices;i++){

			baseList.add(viewerPage.getIndexValue(3,ViewerPageConstants.X_INDEX));
			viewerPage.scrollDownToSliceUsingKeyboard(1);
		}
		List<Double> sortedList = new ArrayList<Double>(baseList);
		Collections.sort(sortedList);
		viewerPage.assertEquals(baseList,sortedList, "Checkpoint[1/1]","Verified that X index increases on mouse scroll down at sagittal plane");

	}	
	
	@Test(groups ={"Chrome","IE11","Edge","US1616","Positive","BVT","E2E","F306"})
	public void test4_US1616_TC8480_VerifyImageNumberIsAscendingOnDefaultPlane() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that image number is in ascending order when user performs a mouse scroll down on a plane other than coronal/sagittal/axial .");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(aH4PatientName, 1);

		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);

		totalSlices=viewerPage.getMaxNumberofScrollForViewbox(1);
		viewerPage.scrollToImage(1,1);

		List<Integer> baseList = new ArrayList<Integer>();
		for(int i=1;i<=totalSlices;i++){

			baseList.add(viewerPage.convertIntoInt(viewerPage.getCurrentScrollPosition(1)));
			viewerPage.scrollDownToSliceUsingKeyboard(1);
		}	
		List<Integer> sortedList = new ArrayList<Integer>(baseList);
		Collections.sort(sortedList);
		viewerPage.assertEquals(baseList,sortedList, "Checkpoint[1/1]","Verified that image number increases on mouse scroll down at default plane");

	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1616","Positive","BVT","E2E","F306","F777","DR2627"})
	public void test5_US1616_TC8483_DR2627_TC10397_VerifyIndexValIsRetainedOnOneUpAndReload() throws InterruptedException, AWTException{
		double zIndexBefore,zIndexAfter;
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that index value is retained when user performs one up or loads a data in different viewbox."
				+ "<br>Verify that index value is retained when user performs one up or loads a data in different viewbox.");

		helper = new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(LSPPatientName, 1);
		
		contentSelector = new ContentSelector(driver);
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		viewerPage.scrollToImage(1, 1);
		zIndexBefore=viewerPage.getIndexValue(1,ViewerPageConstants.Z_INDEX);

		viewerPage.scrollDownToSliceUsingKeyboard(1);

		zIndexAfter=viewerPage.getIndexValue(1,ViewerPageConstants.Z_INDEX);
		String seriesDesc=viewerPage.getSeriesDescriptionOverlayText(1);
		viewerPage.assertTrue(zIndexBefore>zIndexAfter, "Checkpoint[1/3]","Verified Z index decreases on mouse scroll down at axial plane");

		contentSelector.selectSeriesFromSeriesTab(3, seriesDesc);
		double thirdViewboxIndex=viewerPage.getIndexValue(3,ViewerPageConstants.Z_INDEX);
		viewerPage.assertEquals(zIndexAfter,thirdViewboxIndex, "Checkpoint[2/3]","Verified Z index value remains same after loading the series in different viewbox");

		viewerPage.doubleClickOnViewbox(3);
		viewerPage.assertEquals(thirdViewboxIndex,viewerPage.getIndexValue(3,ViewerPageConstants.Z_INDEX),"Checkpoint[3/3]","Verified Z index value remains same after performing a 1-up on the viewbox");

	}
	
	
	@Test(groups ={"Chrome","IE11","Edge","DR2378","Positive","F306"})
	public void test6_DR2378_TC9315_VerifyZIndexIsDecendingOnAxialPlaneForCPUTest() throws InterruptedException{

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify axial series is sorted from head to foot");

		helper=new HelperClass(driver);
		viewerPage = helper.loadViewerDirectly(cpuTestPatientName, 1);
		
		textOverlay=new ViewerTextOverlays(driver);
		textOverlay.selectTextOverlays(ViewerPageConstants.FULL_ANNOTATION);
		totalSlices=viewerPage.getMaxNumberofScrollForViewbox(1);
		viewerPage.scrollToImage(1,1);
		List<Double> baseList = new ArrayList<Double>();
		for(int i=1;i<=totalSlices;i++){

			baseList.add(viewerPage.getIndexValue(1,ViewerPageConstants.Z_INDEX));
			viewerPage.scrollDownToSliceUsingKeyboard(1);
		}
		List<Double> sortedList = new ArrayList<Double>(baseList);
		Collections.sort(sortedList,Collections.reverseOrder());
		viewerPage.assertEquals(baseList,sortedList, "Checkpoint[1/1]","Verified Z index decreases on mouse scroll down at axial plane");

	}
	
	
	
	
	
}
