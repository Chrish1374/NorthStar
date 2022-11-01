/*package com.trn.ns.test.obsolete;

import java.sql.SQLException;

import org.testng.annotations.Test;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.SimpleLine;
import com.trn.ns.page.factory.ViewerARToolbox;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

public class SendToPACSConfigUITest {

	//US956 Display dialog when user clicks Send To Pacs when there are pending results
	
	@Test(groups ={"Chrome","Edge","IE11","US956","sanity","positive","BVT"})
	public void test07_US956_TC4324_verifyDialogUIWhenPendingFindingEnable() throws  InterruptedException, SQLException {
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verification of Display dialog- UI");

		patientPage = new PatientListPage(driver);

	    helper=new HelperClass(driver);
	 	viewerPage =helper.loadViewerDirectly(patientName, 1);
	 	
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);

		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
	
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);		
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingBannerAndWaterMark();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		viewerPage.closingBannerAndWaterMark();

		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingBannerAndWaterMark();
		
		line.selectLineFromQuickToolbar(viewerPage.getViewPort(1));
		line.drawLine(1,10,10, 100, 100);

		DatabaseMethods db = new DatabaseMethods(driver);
		
		//Open output panel
		panel.enableFiltersInOutputPanel(false, false, true);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/6]","Verfiy pending findings in output panel for 'scan' user");
		viewerPage.assertEquals(panel.thumbnailList.size(), 2, "Verify pending findings in output panel", "Verified");
		
		//set sendPendingFindingDefault=1 in DB
		db.updateShowPendingResultsDialog(Configurations.TEST_PROPERTIES.get("nsUserName"), NSGenericConstants.BOOLEAN_TRUE);
		
		//Enable send pending finding result on Config UI
		sd.openSendToPACSMenu(true,false, false, true);
	
		//verify UI of pending finding dialog
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/6]","Verify UI of pending Finding dialog visible or not");
	    viewerPage.click(sd.sendToPacs);
	    sd.waitForPendingFindingDialoglToLoad();
	    viewerPage.assertTrue(verifyUIForPendingFindingDialog(), "Verify UI of Pending finding dialog visible ", "Verified"); 
	    
	    //verify header and text
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[3/6]","Verify header for Pending result dialog");
	    viewerPage.assertEquals(viewerPage.getText(sd.pacsPendingResultDialogHeader),ViewerPageConstants.WARNING_POP_UP_HEADER_FOR_PENDING_RESULT, "Verify header on Pending result dialog", "Verified");
	    
	    //verify checkbox along with text
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[4/6]","Verify checkbox on show pending result dialog");
	    viewerPage.assertEquals(viewerPage.getText(sd.pacsPendingResultDialogCheckbox), ViewerPageConstants.WARNING_POP_UP_CHECKBOX,"Verify text display alaong with checkbox on pending result dialog" , "Verified");
		
	    //verify border color for the options available on pop-up
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[5/6]","Verify border color for all button when pop-up open");
	    viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.CSS_PROP_BORDER_COLOR),(ViewerPageConstants.ACCEPT_ALL_BORDER_COLOR),"Verify Green color border appear for Accept All button" , "Verified");
	    viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultRejectAll,NSGenericConstants.CSS_PROP_BORDER_COLOR),(ViewerPageConstants.REJECT_ALL_BORDER_COLOR),"Verify Red color border appear for  Reject All button" , "Verified");
	    viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultNoChanges,NSGenericConstants.CSS_PROP_BORDER_COLOR),(ViewerPageConstants.LEAVE_AS_IS_BORDER_COLOR),"Verify Blue color border appear for Leave as is  button" , "Verified");
	    
	    //verify background color for button as well as text color
	    String color_value=viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.CSS_PROP_COLOR);
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[6/6]","Verify background color for all button once mouse hover action perform Also check colour of text as white");
	    viewerPage.mouseHover(sd.pacsPendingResultAcceptAll);
	    viewerPage.assertTrue(viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.BACKGROUND_COLOR).startsWith(ViewerPageConstants.ACCEPT_ALL_COLOR),"Verify Green color appear in background for Accept All button on  mouse hover" , "Verified");
	    viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.CSS_PROP_COLOR),color_value,"Verify text still display in white color" , "Verified");
	    
	    viewerPage.mouseHover(sd.pacsPendingResultRejectAll);
        viewerPage.assertTrue(viewerPage.getCssValue(sd.pacsPendingResultRejectAll, NSGenericConstants.BACKGROUND_COLOR).startsWith(ViewerPageConstants.REJECT_ALL_COLOR),"Verify Red color appear in background for Reject All button on  mouse hover" , "Verified");
        viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.CSS_PROP_COLOR),color_value,"Verify text still display in white color" , "Verified");
        
        viewerPage.mouseHover(sd.pacsPendingResultNoChanges);
	    viewerPage.assertTrue(viewerPage.getCssValue(sd.pacsPendingResultNoChanges, NSGenericConstants.BACKGROUND_COLOR).startsWith(ViewerPageConstants.LEAVE_AS_IS_COLOR),"Verify Lightblue color appear in background for Leave as is  button on  mouse hover" , "Verified");
	    viewerPage.assertEquals(viewerPage.getCssValue(sd.pacsPendingResultAcceptAll,NSGenericConstants.CSS_PROP_COLOR),color_value,"Verify text still display in white color" , "Verified");
	   
	}

	@Test(groups ={"Chrome","Edge","IE11","US956","negative"})
	public void test11_US956_TC4339_verifyBannerForErrorMessage() throws  InterruptedException, SQLException {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Error in sending results to Envoy AI- Banner functionality for send to PACS");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
	    viewerPage = helper.loadViewerDirectly(patientName, 1);
	    
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new SimpleLine(driver);
		panel= new OutputPanel(driver);
		sd=new ViewerSendToPACS(driver);
		arToolbar=new ViewerARToolbox(driver);
   
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);
		arToolbar.selectRejectfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 20, 20, -100,-100);		
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		line.selectLineFromQuickToolbar(1);
		line.drawLine(1,10,10, 100, 100);
		viewerPage.closingBannerAndWaterMark();
		
		ellipse.selectEllipseFromQuickToolbar(2);
		ellipse.drawEllipse(2, 100, -50, 40, -50);
		arToolbar.selectAcceptfromGSPSRadialMenu();
		
		circle.selectCircleFromQuickToolbar(2);
		circle.drawCircle(2, 20, 20, -100,-100);	
		arToolbar.selectRejectfromGSPSRadialMenu();
		viewerPage.closingBannerAndWaterMark();
	
		//Click on send to PACS and verify UI of pending finding dialog
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[1/2]","Verify Error banner when user click on send To PACS");
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		sd.sendToPacsAndSelectOptionsFromPendingBox(false, false, true);
	    viewerPage.assertTrue(sd.verifyBannerAfterSendToPacs(ViewerPageConstants.BANNER_TEXT_FOR_SEND_TO_PACS), "Verify Error banner when user click on send to PACS" , "Verified");
		
		//ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint[2/2]","Verify backdround color(orange) of error banner when user click on send To PACS");
		//viewerPage.assertEquals(viewerPage.getBackgroundColorOfRows(viewerPage.banner),ViewerPageConstants.BANNER_COLOR,"verifying the background color","verified");
	
	}

}
*/