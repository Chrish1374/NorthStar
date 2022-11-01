/*package com.trn.ns.test.obsolete;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.EllipseAnnotation;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerSendToPACS;
import com.trn.ns.utilities.ExtentManager;

public class SendToPACSForSRTest {

	
	@Test(groups ={"Chrome","Edge","IE11","DE1852","positive"})	
	public void test07_DE1852_TC7435_verifySendToPACSAfterStateChangeFromMachineFilter() throws InterruptedException{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that state of GSPS findings getting changed on viewer, in findings menu, on vertical scroll bar and in Output panel after Accept All or Reject All from Output panel.");
		patientPage = new PatientListPage(driver);
		helper=new HelperClass(driver);
		helper.loadViewerDirectly(ChestCT1p25mm, 2);
		
		panel=new OutputPanel(driver);
		panel.click(panel.banner);
		sd=new ViewerSendToPACS(driver);
		layout=new ViewerLayout(driver);
		layout.selectLayout(layout.twoByTwoLayoutIcon);
		ellipse=new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(3);
		ellipse.drawEllipse(3, 20, 80, 80, 30);
		ellipse.selectAcceptfromGSPSRadialMenu();
		
		//accept all machine and user finding from Output panel
		panel.enableFiltersInOutputPanel(true, false, true);
		panel.clickAcceptAllFilter(0);
		panel.clickAcceptAllFilter(1);
		
       //draw annoatation on series
		sd.openSendToPACSMenu();
		sd.enableSendToPACSFindingOptions(true, true, true);
		
		//get notification UI after click on send
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[1/7]", "Send to pacs and choose accept all as option from Pending finding dialog");
		String message=sd.sendToPacsAndSelectOptionsFromPendingBox(false,false,false);
		panel.waitForElementVisibility(panel.banner);
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[2/7]", "Getting the total pending findings from output panel");
		panel.click(panel.getViewPort(1));
		panel.enableFiltersInOutputPanel(true, false, false);
		int acceptedCount =panel.thumbnailList.size();
		panel.openAndCloseOutputPanel(false);
		
		//31 findings are sent to PACS (31 as accepted )
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[3/7]", "Verify count of accepted findings from Notification UI");
		panel.assertEquals(message,acceptedCount+" findings are sent to PACS ( "+acceptedCount+" as accepted )","Checkpoint[4/7]","Verified message post sending the findings to PACS by selecting Accept all option");		
		
		panel.click(panel.getViewPort(2));
		cs = new ContentSelector(driver);
		String currentSelectResultForSR = cs.getSelectedResults(2).get(1);
		String currentSelectResult = cs.getSelectedResults(2).get(0);
		
		panel.mouseHover(panel.getViewPort(1));
		panel.assertTrue(panel.verifyResultsAreAccepted(1), "Checkpoint[4/7]", "Verified that SR report is in accepted state on viewer");
		panel.assertEquals(panel.getStateSpecificFindings(2, ViewerPageConstants.ACCEPTED_COLOR).size()+1, acceptedCount, "Checkpoint[5/7]", "Verified count of accepted finding in Finding menu.");
		
		int marks =panel.getStateSpecificFindings(2, ViewerPageConstants.ACCEPTED_COLOR).size();
		int count=0;
		for(int slideMarker = 1 ; slideMarker<marks ;slideMarker++)
		{ count=count+panel.getFindingsFromSliderContainer(2, slideMarker, ViewerPageConstants.ACCEPTED_COLOR).size();
		}
		panel.assertEquals(count, acceptedCount, "Checkpoint[6/7]", "Verified state of GSPS finding on scroll slider.");
		
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "Checkpoint[7/7]", "Verifying the orthanc for findings of SR");
	    verifyOrthancFindingsForSRAndPR(ChestCT1p25mm, username, currentSelectResult,currentSelectResultForSR, 2);
	   
	}
}
*/