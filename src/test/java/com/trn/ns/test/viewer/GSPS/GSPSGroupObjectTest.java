package com.trn.ns.test.viewer.GSPS;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.OutputPanel;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.PointAnnotation;

import com.trn.ns.page.factory.TextAnnotation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class GSPSGroupObjectTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	
	private ExtentTest extentTest;

	private PointAnnotation point ;
	private CircleAnnotation circle ;
	private TextAnnotation textAn;	
	private ContentSelector cs;
	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	String myText="TEXT_1";

	String filepath =Configurations.TEST_PROPERTIES.get("BrainPerfusion_EAI_Filepath");
	String brainPerfusionPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath);
	
	int viewboxNo=9;
	private HelperClass helper;
	
	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(username, password);
	}
	
	//US1612: Move a GSPS group of a label and arrow in sync
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Positive","BVT","E2E","F782"})
	public void test01_US1612_TC8402_TC8405_TC8406_verifyObjectFromGSPSGroupCannotSeperate() throws InterruptedException      
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Northstar is supporting the GSPS result which is group containing text label and arrow.<br>"+
		"Verify that user is not able to separate the object from gsps group. <br>"+
				"Verify that all the object of group are getting selected on selecting any of the group object.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName,9);
	
		
	    point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		cs=new ContentSelector(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		point.assertEquals(point.getNumberOfCanvasForLayout(), layout.getExpectedNumberOfCanvasForLayout(ViewerPageConstants.THREE_BY_THREE_LAYOUT), "Checkpoint[1/11]", "Verified that default loading of patient is 3*3");
		cs.closeNotification();
		String seriesName=cs.getAllSeries().get(0);
		point.assertTrue(cs.verifyPresenceOfEyeIcon(seriesName), "Checkpoint[2/11]", "Verified that series is selected in Content selector and loaded on viewer.");
			
		point.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(viewboxNo, 1), "Checkpoint[3/11]", "Verified that Point annotation is pending GSPS.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentPendingInactive(viewboxNo, 1, true), "Checkpoint[4/11]", "Verified that text annotation is pending GSPS.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify both point and text annotation is highlighted when selected from Finding menu.");
		point.selectFindingFromGroupOfTable(viewboxNo,1, 1);
		point.assertTrue(point.verifyPointAnnotationIsPendingActiveGSPS(viewboxNo, 1), "Checkpoint[5/11]", "Verified that Point annotation is pending active GSPS.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActivePendingGSPS(viewboxNo, 1, true), "Checkpoint[6/11]", "Verified that text annotation is pending active GSPS.");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(viewboxNo, 1), "Artery", "Checkpoint[7/11]", "Verified text of text annotation on viewer.");
		
		//verify finding is highlighted in group
		point.assertTrue(point.verifyFindingIsHighlightedWithinGroup(1, 1), "Checkpoint[8/11]", "Verified that point annotation is highlighted in finding menu under Group.");
		point.assertTrue(point.verifyFindingIsHighlightedWithinGroup(1, 2), "Checkpoint[9/11]", "Verified that text annotation is highlighted in finding menu under Group.");
		
		//cx and cy value before moving point annotation for handle 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify point and text ann both are in sync when move the point .");
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		point.movePoint(viewboxNo, 1, 20, 20);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenPointIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[10/11]", "Verified that point and dotted line can not be seperate when point annotation is moved.");
		
		//cx and cy value before moving text  annotation for handle 1
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify point and text ann both are in sync when handle of text annotation ");
		cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		textAn.moveTextAnnotation(viewboxNo, 1, 100, 100);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenTextIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[11/11]", "Verified that point and dotted line can not be seperate when text annotation is moved.");
			
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Positive","BVT","E2E","F782"})
	public void test02_US1612_TC8407_verifyMovementOfTextOfGSPSGroup() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to move the text of GSPS group");

	    helper = new HelperClass(driver);
	    helper.loadViewerPageUsingSearch(brainPerfusionPatientName, 1, 1);

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		
		//Load patient on viewer
		point.waitForViewerpageToLoad(viewboxNo);
		point.click(point.getViewPort(viewboxNo));
		point.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(viewboxNo, 1), "Checkpoint[1/10]", "Verified that point annotation is pending GSPS.");
		
		//cx and cy value before moving text  annotation for handle 1
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
		//move text of text annotation 
		point.click(textAn.getAllTextElementFromTextAnnotation(viewboxNo, 1).get(0));
		point.moveElement(textAn.getAllTextElementFromTextAnnotation(viewboxNo, 1).get(0), 50, 70);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[2/10]", "Verified that point annotation is current active accepted GSPS.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(viewboxNo, 1, true), "Checkpoint[3/10]", "Verified that text annotation is current active accepted GSPS.");
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenTextIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[4/10]", "Verified that user is not able to seperate object from GSPS group.");
		
		int xOffsetForPoint_Before=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForPoint_Before=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CY)));
	
		int xOffsetForHandle1_Before=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForHandle1_Before=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CY)));
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewerpage and verify location and state of annotation.");
	    helper.browserBackAndReloadViewer(brainPerfusionPatientName, 1, 9);

        point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[5/10]", "Verified that point annotation is current accepted active GSPS after reload.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 1, true), "Checkpoint[6/10]", "Verified that text annotation is accepted GSPS after reload.");
		
		//verify location of point is same after reload as well
        
		int xOffsetForPoint_After = point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForPoint_After=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CY)));
	
		int xOffsetForHandle1_After=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForHandle1_After=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CY)));
		
        point.assertEquals(xOffsetForPoint_After,xOffsetForPoint_Before, "Checkpoint[7/10]", "Verified x co-ordinate of point annotation after reload of viewerpage.");
		point.assertEquals(yOffsetForPoint_After, yOffsetForPoint_Before, "Checkpoint[8/10]", "Verified y co-ordinate of point annotation after reload of viewerpage.");
		
		point.assertEquals(xOffsetForHandle1_After, xOffsetForHandle1_Before, "Checkpoint[9/10]", "Verified x co-ordinate of dotted line handle 1 after reload of viewerpage.");
		point.assertEquals(yOffsetForHandle1_After, yOffsetForHandle1_Before, "Checkpoint[10/10]", "Verified y co-ordinate of dotted line handle 1 after reload of viewerpage.");
			
	}

	@Test(groups ={"IE11","Chrome","Edge","US1612","Positive","F782"})
	public void test03_US1612_TC8415_verifyMovementOfPointFromGSPSGroup() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to move the text of GSPS group");

		helper = new HelperClass(driver);
		helper.loadViewerPage(brainPerfusionPatientName, "", 1, viewboxNo);

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		viewerPage = new ViewerPage(driver);
		
		//Load patient on viewer
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		
		viewerPage.click(viewerPage.getViewPort(viewboxNo));
		point.assertTrue(point.verifyPointAnnotationIsPendingInActiveGSPS(9, 1), "Checkpoint[1/10]", "Verified that point annotation is pending GSPS.");
		
		//cx and cy value before moving text  annotation for handle 1
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
		//move point on viewer
		point.selectFindingFromGroupOfTable(viewboxNo,1, 1);
		point.movePoint(viewboxNo, 1,-30, -30);
		
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[2/10]", "Verified that point annotation is current active accepted GSPS.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(viewboxNo, 1, true),"Checkpoint[3/10]", "Verified that text annotation is current active accepted GSPS.");
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenPointIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[4/10]", "Verified that user is not able to seperate object from GSPS group.");
		
		int xOffsetForPoint_Before=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForPoint_Before=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CY)));
	
		int xOffsetForHandle1_Before=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForHandle1_Before=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CY)));
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewerpage and verify location and state of annotation.");
		helper.browserBackAndReloadViewer(brainPerfusionPatientName, 1, viewboxNo);
		
        point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[5/10]", "Verified that point annotation is current accepted active GSPS after reload.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 1, true), "Checkpoint[6/10]", "Verified that text annotation is accepted GSPS after reload.");
		
		//verify location of point is same after reload as well
        
		int xOffsetForPoint_After =point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForPoint_After=point.roundOffValue(point.convertIntoFloat(point.getCentreForPoint(viewboxNo, 1).getAttribute(NSGenericConstants.CY)));
	
		int xOffsetForHandle1_After=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CX)));
		int yOffsetForHandle1_After=point.roundOffValue(point.convertIntoFloat(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(1).getAttribute(NSGenericConstants.CY)));
		
        point.assertEquals(xOffsetForPoint_After,xOffsetForPoint_Before, "Checkpoint[7/10]", "Verified x co-ordinate of point annotation after reload of viewerpage.");
		point.assertEquals(yOffsetForPoint_After, yOffsetForPoint_Before,"Checkpoint[8/10]", "Verified y co-ordinate of point annotation after reload of viewerpage.");
		
		point.assertEquals(xOffsetForHandle1_After, xOffsetForHandle1_Before, "Checkpoint[9/10]", "Verified x co-ordinate of dotted line handle 1 after reload of viewerpage.");
		point.assertEquals(yOffsetForHandle1_After, yOffsetForHandle1_Before, "Checkpoint[10/10]", "Verified y co-ordinate of dotted line handle 1 after reload of viewerpage.");
			

	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Negative","F782"})
	public void test04_US1612_TC8417_verifyEditionOfTextOfGSPSGroupObject() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to edit the text of gsps group object.");

		helper = new HelperClass(driver);
		helper.loadViewerPage(brainPerfusionPatientName, "", 1, viewboxNo);

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		cs.closeNotification();
		String resultName=cs.getAllResults().get(cs.getAllResults().size()-1);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Update the text of text annotation and verify clone in CS and state of annotation.");
		textAn.updateTextOnTextAnnotation(viewboxNo, 1, myText);
		point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(viewboxNo, 1), "Checkpoint[1/15]", "Verified that state of point annotation is active accepted GSPS after updating text.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 1, true), "Checkpoint[2/15]", "Verified that text annotation is accepted GSPS.");
		textAn.assertEquals(textAn.getTextFromTextAnnotation(viewboxNo, 1),myText, "Checkpoint[3/15]", "Verified text of text annotaton is updated with the new value.");
		
		point.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[4/15]", "Verified that new clone is created.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify state of GSPS group in finding menu,Scroll slider and in Output panel.");
		List<String> groups = point.getListOfGroupsInFindingMenu(viewboxNo);
		List<String> groupFindings = point.getListOfFindingsFromFindingMenu(1);
		point.assertTrue(point.verifyFindingsStateUnderGroup(viewboxNo,1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[5/15]", "Verified state of finding inside group as Accepted.");
		point.assertTrue(point.verifyGroupOnSliderContainerWithoutWarningIcon(viewboxNo,1, 1, groups.get(0), groupFindings, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[6/15]","Verified state of finding as Accepted on scroll slider");
		
		OutputPanel panel=new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[7/15]", "Verified finding under Accepted tab in finding menu.");
		panel.assertTrue(panel.getTextLabelValFromThumbnail(1).contains(ViewerPageConstants.TEXT), "Checkpoint[8/15]", "Verified the updated text in output panel.");
		panel.openAndCloseOutputPanel(false);
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Reload viewer page and verify state of annotation and text of text annotation after reload.");
		helper.browserBackAndReloadViewer(brainPerfusionPatientName, "", 1, viewboxNo);
		point.closeNotification();
		
        point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[9/15]", "Verified that state of point annotation is active accepted GSPS after reload viewer.");
        textAn.assertEquals(textAn.getTextFromTextAnnotation(viewboxNo, 1),myText, "Checkpoint[10/15]", "Verified text of text annotaton after reload of viewer.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 1, true), "Checkpoint[11/15]", "Verified that text annotation is accepted GSPS.");
		
		//verify location of point is same after reload as well
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify state of GSPS group in finding menu,Scroll slider and in Output panel after reload.");
       
		point.assertTrue(point.verifyFindingsStateUnderGroup(viewboxNo,1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[12/15]", "Verified state of finding inside group as Accepted.");
		point.assertTrue(point.verifyGroupOnSliderContainerWithoutWarningIcon(viewboxNo, 1, 1, groups.get(0), groupFindings, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[13/15]","Verified state of finding as Accepted on scroll slider");
		
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[14/15]", "Verified finding under Accepted tab in finding menu after viewer reload.");
		panel.assertTrue(panel.getTextLabelValFromThumbnail(1).contains(ViewerPageConstants.TEXT), "Checkpoint[15/15]", "Verified state of finding as Accepted on scroll slider after viewer reload.");
		
	
	}
 
	@Test(groups ={"IE11","Chrome","Edge","US1612","Negative","BVT","E2E","F782"})
	public void test05_US1612_TC8419_verifyUserDrawnAnnotationBehaviour() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to add another gsps annotation on gsps result group of object slice.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName,viewboxNo);
	

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		
		
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw new annotation and verify behaviour of GSPS group as well as user drawn annotation.");
		circle=new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(viewboxNo);
		circle.drawCircle(viewboxNo, 5, 5,-40,-40);
		circle.selectAcceptfromGSPSRadialMenu();
		circle.selectPreviousfromGSPSRadialMenu();
		
		circle.assertTrue(point.verifyLocationOfPointAndTextAnnWhenDefaultLoaded(viewboxNo, 1, 1),"Checkpoint[1/5]","Verified that default loaded of GSPS group.");
		
		//move point of GSPS group and cx and cy value before moving point annotation for handle 1
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
		point.movePoint(viewboxNo, 1, 20, 20);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenPointIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[2/5]", "Verified location of GSPS group after moving point annotation.");
		point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/5]", "Verified that GSPS group is accepted afte move action perform.");
		
		//move user drawn annotation
		circle.click(circle.getAllCircles(viewboxNo).get(0));
		circle.moveSelectedCircle(viewboxNo, 50, 50);
		circle.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(viewboxNo, 1), "Checkpoint[4/5]", "Verified that state of user drawn annotation after movment of circle.");
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenPointIsMoved(viewboxNo, 1, 1,cx_before, cy_before), "Checkpoint[5/5]", "Verified that location of GSPS group remain as it is during movment of user drawn annotation.");
	
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Negative","F782"})
	public void test06_US1612_TC8420_verifyBehaviourOfuserDrawnPointAndTextAnnotation() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user drawn cross point + Text annotation with anchor point will not behave like GSPS group object.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName,viewboxNo);
	
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		cs=new ContentSelector(driver);
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Draw point and text annotataion.");
        point.selectPointFromQuickToolbar(viewboxNo);
        point.drawPointAnnotationMarkerOnViewbox(viewboxNo, 5, 5);
		
        textAn.selectTextArrowFromQuickToolbar(viewboxNo);
        textAn.drawText(viewboxNo, -30, -30, myText);
    
        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select user drawn text annotation and verify behaviour");
        textAn.selectTextAnnotation(viewboxNo, 2);
        textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveAcceptedGSPS(viewboxNo, 2, true), "Checkpoint[1/8]", "Verified that text annotation is current active accepted GSPS.");
        point.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(viewboxNo, 2), "Checkpoint[2/8]", "Verified point annotation is accepted GSPS.");
        
        ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select user drawn point annotation and verify behaviour");
        point.selectPoint(viewboxNo, 2);
        point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 2), "Checkpoint[3/8]", "Verified that point annotation is current active accepted GSPS.");
        textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 2, true), "Checkpoint[4/8]", "Verified that text annotation is accepted GSPS.");
     
    	String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select user drawn text annotation and verify behaviour");
        point.movePoint(viewboxNo, 2, 20, 20);
		point.assertFalse(point.verifyLocationOfPointAndTextAnnWhenPointIsMoved(viewboxNo, 2, 2,cx_before, cy_before), "Checkpoint[5/8]", "Verified that point and text annotataion are not in sync for user drawn annotation.");
		point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[6/8]", "Verified that finding is accepted.");
        
		textAn.moveTextAnnotation(viewboxNo, 2, 50, 50);
		point.assertFalse(point.verifyLocationOfPointAndTextAnnWhenTextIsMoved(viewboxNo, 2, 2,cx_before, cy_before), "Checkpoint[7/8]", "Verified that point and text annotataion are not in sync for user drawn annotation");
		point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[8/8]", "Verified that finding is accepted.");

	
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Positive","E2E","F782"})
	public void test07_US1612_TC8422_verifyUserCanChangeStateOfGSPSResultGroupOfObject() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("[Risk and Impact] Verify that user is able to change the state of gsps result group of object");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName,viewboxNo);
	
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
	
	
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user can change state of GSPS group object from A/R toolbar to Accepted.");
	    point.selectAcceptfromGSPSRadialMenu();
	    point.selectPreviousfromGSPSRadialMenu();
		point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(viewboxNo, 1), "Checkpoint[1/12]", "Verified that state of point annotation is accepted.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentAcceptedInactive(viewboxNo, 1, true), "Checkpoint[2/12]", "Verified state of text annotation is accepted. ");
		textAn.assertTrue(textAn.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/12]", "Verified that accept GSPS radial menu is displayed on viewer.");

		List<String> groups = point.getListOfGroupsInFindingMenu(viewboxNo);
		List<String> groupFindings = point.getListOfFindingsFromFindingMenu(1);
		point.assertTrue(point.verifyFindingsStateUnderGroup(viewboxNo,1, ViewerPageConstants.ACCEPTED_FINDING_COLOR), "Checkpoint[4/12]", "Verified state of finding as Accepted in finding menu.");
		point.assertTrue(point.verifyGroupOnSliderContainerWithoutWarningIcon(viewboxNo,1, 1, groups.get(0), groupFindings, ViewerPageConstants.ACCEPTED_FINDING_COLOR),"Checkpoint[5/12]","Verified state of group finding as Accepted on scroll slider.");
		OutputPanel panel=new OutputPanel(driver);
		panel.enableFiltersInOutputPanel(true, false, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[6/12]", "Verified finding under accepted tab in output panel.");
		panel.openAndCloseOutputPanel(false);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify user can change state of GSPS group object from A/R toolbar to Rejected.");
		point.selectFindingFromGroupOfTable(viewboxNo,1, 1);
		point.selectRejectfromGSPSRadialMenu();
	    point.selectPreviousfromGSPSRadialMenu();
		point.assertTrue(point.verifyPointAnnotationIsCurrentRejectedActiveGSPS(viewboxNo, 1), "Checkpoint[7/12]", "Verified that state of point annotation is rejected.");
		textAn.assertTrue(textAn.verifyTextAnnotationIsCurrentActiveRejectedGSPS(viewboxNo, 1, true), "Checkpoint[8/12]", "Verified state of text annotation is rejected. ");
		textAn.assertTrue(textAn.verifyRejectGSPSRadialMenu(), "Checkpoint[9/12]", "Verified that reject GSPS radial menu is displayed on viewer.");
		point.assertTrue(point.verifyFindingsStateUnderGroup(viewboxNo,1, ViewerPageConstants.REJECTED_FINDING_COLOR), "Checkpoint[10/12]", "Verified state of finding as Rejected in finding menu.");
		point.assertTrue(point.verifyGroupOnSliderContainerWithoutWarningIcon(viewboxNo,1, 1, groups.get(0), groupFindings, ViewerPageConstants.REJECTED_FINDING_COLOR),"Checkpoint[11/12]","Verified state of group finding as Rejected on scroll slider.");
		panel.enableFiltersInOutputPanel(false, true, false);
		panel.assertEquals(panel.thumbnailList.size(), 1, "Checkpoint[12/12]", "Verified finding under rejected tab in output panel.");
		panel.openAndCloseOutputPanel(false);
	}
 
	@Test(groups ={"IE11","Chrome","Edge","US1612","Negative","F782"})
	public void test08_US1612_TC8416_verifyUserCanAdjustAngleOfDottedLine() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that user is able to adjust the angle of dotted line.");

		helper = new HelperClass(driver);
		helper.loadViewerPage(brainPerfusionPatientName, "", 1, viewboxNo);

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		cs=new ContentSelector(driver);
		cs.closeNotification();
		String resultName=cs.getAllResults().get(cs.getAllResults().size()-1);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenDefaultLoaded(viewboxNo, 1, 1),"Checkpoint[1/9]","Verified default loading of Group object.");
		
		//move point of GSPS group and cx and cy value before moving point annotation for handle 1
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Move handle of text annotation to adjust the angle of dotted line.");
		point.click(point.getViewPort(viewboxNo));
		point.mouseHover(point.getGSPSHoverContainer(viewboxNo));
		point.selectFindingFromGroupOfTable(viewboxNo,1, 2);
		textAn.moveElement(textAn.getSelectedTextAnnotLineHandles(viewboxNo, 1).get(0), 100, 50);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenTextIsMoved(9, 1, 1,cx_before, cy_before), "Checkpoint[2/9]", "Verified location of Group object when text is moved.");
		point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[3/9]", "Verified that Accept GSPS toolbar displayed on viewer.");
		
		String cx_after=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_after=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		
	    point.assertNotEquals(cx_after, cx_before, "Checkpoint[4/9]", "Verified that x co-ordinate of handle 1 is not equal after moving text annotation.");
	    point.assertNotEquals(cy_after, cy_before, "Checkpoint[5/9]", "Verified that y co-ordinate of handle 1 is not equal after moving text annotation.");
	   
		point.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[6/9]", "Verified that new clone is created after moving handle to adjust the angle of dotted line.");
	
		//reload viewer page and verify state 
		helper.browserBackAndReloadViewer(brainPerfusionPatientName, "", 1, viewboxNo);
		
	    point.assertNotEquals(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX),cx_before, "Checkpoint[7/9]", "Verified x co-ordinate value of handle 1 after reload of viewer.");
	    point.assertNotEquals(textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY),cy_before, "Checkpoint[8/9]", "Verified y co-ordinate value of handle 1 after reload of viewer.");
	    point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[9/9]", "Verified that Accept GSPS toolbar displayed on viewer reload.");
	}
	
	@Test(groups ={"IE11","Chrome","Edge","US1612","Positive","F782"})
	public void test09_US1612_TC8463_verifyUserCanAddCommentToGroupGSPSObject() throws InterruptedException     
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that comment added to cross point of gsps group of object, will also be group component.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		helper.loadViewerDirectly(brainPerfusionPatientName,viewboxNo);
	

	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+brainPerfusionPatientName+" on viewer.");
		point=new PointAnnotation(driver);
		textAn=new TextAnnotation(driver);
		cs=new ContentSelector(driver);
		cs.closeNotification();
		String resultName=cs.getAllResults().get(cs.getAllResults().size()-1);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Add comment from A/R toolbar  to point  and verify added comment at group level" );
		point.selectFindingFromGroupOfTable(viewboxNo,1, 2);
		textAn.addResultComment(viewboxNo, myText);
		point.assertTrue(point.verifyAcceptGSPSRadialMenu(), "Checkpoint[1/9]", "Verified state of GROUP GSPS after adding comment is Accepted.");
		point.assertEquals(point.getTextCommentForPoint(viewboxNo, 1), myText, "Checkpoint[2/9]", "Verified comment on viewer page.");
		
		String groupName=point.getListOfGroupsInFindingMenu(viewboxNo).get(0);
		String findingName=point.getListOfFindingsFromFindingMenu(1).get(0);
		point.assertTrue(point.verifyCommentForGroupFindingInFindingMenu(viewboxNo, groupName, findingName, myText), "Checkpoint[3/9]", "Verified comment at group level in finding menu.");
		
		int xOffset=point.resultComment.get(1).getLocation().getX();
		int yOffset=point.resultComment.get(1).getLocation().getY();
		
		String cx_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CX);
		String cy_before=textAn.getTextAnnotLineHandles(viewboxNo, 1).get(0).getAttribute(NSGenericConstants.CY);
		point.assertTrue(cs.verifyPresenceOfEyeIcon(resultName+ViewerPageConstants.CONTENT_SELECTOR_RESULT_PREFIX_MACHINE+username+"_1"), "Checkpoint[4/9]", "Verified that new clone is created after adding comment to Group finding.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Move text annotation and verify location of comment." );
		point.selectFindingFromGroupOfTable(viewboxNo,1, 2);
		textAn.moveElement(textAn.getSelectedTextAnnotLineHandles(viewboxNo, 1).get(0), 100, 50);
		point.assertTrue(point.verifyLocationOfPointAndTextAnnWhenTextIsMoved(9, 1, 1,cx_before, cy_before), "Checkpoint[5/8]", "Verified that both point and dotted line are in sync after movement of text annotation.");
		point.assertEquals(point.resultComment.get(1).getLocation().getX(), xOffset, "Checkpoint[6/9]", "Verified x offset of comment text after movement of text annotation.");
		point.assertEquals(point.resultComment.get(1).getLocation().getY(), yOffset, "Checkpoint[7/9]", "Verified y offset of comment text after movement of text annotation.");
	   
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Change state of GSPS Group to rejected and then verify comment." );
		point.selectFindingFromGroupOfTable(viewboxNo,1, 2);
		point.selectRejectfromGSPSRadialMenu();
		point.assertTrue(point.verifyRejectGSPSRadialMenu(), "Checkpoint[8/9]", "Verified that reject GSPS radial menu visible on viewer.");
        point.assertEquals(point.getAttributeValue(point.resultComment.get(1), NSGenericConstants.FILL),ViewerPageConstants.REJECTED_COLOR, "Checkpoint[9/9]", "The result comment is in rejected state");

		
	}
	
	

}















