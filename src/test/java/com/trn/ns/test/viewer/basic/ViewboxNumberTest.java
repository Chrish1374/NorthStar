package com.trn.ns.test.viewer.basic;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewboxNumberTest extends TestBase{

	private ExtentTest extentTest;


	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^CT^Neck_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
	
	String filePath1 = Configurations.TEST_PROPERTIES.get("IBL_JohnDoe_Filepath");
	String patientName1 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);

	String filePath2 = Configurations.TEST_PROPERTIES.get("3ChestCT1p25mm_filepath");
	String patientName2 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath2);

	
	String loadedTheme =ThemeConstants.EUREKA_THEME_NAME; 
	private HelperClass helper;

	@Test(groups={"Chrome","IE11","Edge","US2251","Positive","E2E","F1084"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test01_US2251_TC9483_TC9525_verifyViewboxNumOnSymViewbox(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify numbering on symmetrical  view boxes."
				+ "<br> Verify numbering on non-symmetrical  view boxes.");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver);
		helper.loadViewerDirectly(patientName,username, password, 1);
		ViewerLayout layout = new ViewerLayout(driver);
	
		verifyViewboxNumber(1,2);
		
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		int viewbox = layout.getNumberOfCanvasForLayout();
		
		for(int i =1;i<=viewbox;i++) {
			layout.compareElementImage(protocolName, layout.getViewPort(i), "Checkpoint[2."+i+"/2]", loadedTheme+"_viewbox_"+i);
		}
		
	}
		
	@Test(groups={"Chrome","IE11","Edge","US2251","Positive","E2E","F1084"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test02_US2251_TC9628_verifyNonDICOMViewboxNum(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify numbering on the Non-DICOM view boxes.");
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;

		helper =new HelperClass(driver);
		helper.loadViewerDirectly(patientName1,username, password, 1);
		verifyViewboxNumber(1,2);		
		helper.loadViewerDirectly(patientName2, 1);
		verifyViewboxNumber(2,2);				
	}
		
	
	@AfterMethod
	public void revertDefaultTheme() {
		
		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.EUREKA_THEME_NAME,username);
		
		
	}

	
	private void verifyViewboxNumber(int checkpoint,int finalCheckpoint) {
		
		ViewerLayout layout = new ViewerLayout(driver);
		PagesTheme pageTheme = new PagesTheme(driver);
		int viewbox = layout.getNumberOfCanvasForLayout();
		for(int i =1;i<=viewbox;i++) {
			
			layout.assertEquals(layout.getViewboxNumberText(i),""+i,"Checkpoint["+checkpoint+".1/"+finalCheckpoint+".9]","Verifying the current viewbox number");
			layout.assertEquals(layout.getCssValue(layout.getViewboxNumber(i), NSGenericConstants.FILL),ThemeConstants.NUMBER_COLOR,"Checkpoint["+checkpoint+".2/"+finalCheckpoint+".9]","verifying the number color based on theme"+loadedTheme);
			layout.assertTrue(pageTheme.verifyFillForPopupBasedOnTheme(layout.getViewboxNumberBackground(i), loadedTheme),"Checkpoint["+checkpoint+".3/"+finalCheckpoint+".9]","verifying the number background color based on theme "+loadedTheme);
								
			layout.doubleClickOnViewbox(i);
			layout.assertEquals(layout.getViewboxNumberText(i),"1","Checkpoint["+checkpoint+".4/"+finalCheckpoint+".9]","verifying the viewer number is changed to 1 after one up");
			layout.assertEquals(layout.getCssValue(layout.getViewboxNumber(i), NSGenericConstants.FILL),ThemeConstants.NUMBER_COLOR,"Checkpoint["+checkpoint+".4/"+finalCheckpoint+".9]","verifying the number color based on theme after one up");
			layout.assertTrue(pageTheme.verifyFillForPopupBasedOnTheme(layout.getViewboxNumberBackground(i), loadedTheme),"Checkpoint["+checkpoint+".6/"+finalCheckpoint+".9]","Verifying the background color based on theme in one up");
			
			layout.doubleClickOnViewbox(i);
			layout.assertEquals(layout.getViewboxNumberText(i),""+i,"Checkpoint["+checkpoint+".7/"+finalCheckpoint+".9]","verifying the viewer number is changed back to original viewbox number when back to one up");
			layout.assertEquals(layout.getCssValue(layout.getViewboxNumber(i), NSGenericConstants.FILL),ThemeConstants.NUMBER_COLOR,"Checkpoint["+checkpoint+".8/"+finalCheckpoint+".9]","verifying the number color going back to original layout");
			layout.assertTrue(pageTheme.verifyFillForPopupBasedOnTheme(layout.getViewboxNumberBackground(i), loadedTheme),"Checkpoint["+checkpoint+".9/"+finalCheckpoint+".9]","verifying the background color going back to original layout");
			
			
		}
		
		
	}

}

