/*
import org.testng.annotations.Test;

import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.PointAnnotation;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerPresetMenu;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;

//import org.testng.annotations.Test;
//
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//
///*import org.testng.annotations.Test;
//
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.PointAnnotation;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;*/
//
////import org.testng.annotations.Test;
////
////
////import com.trn.ns.page.factory.TextAnnotation;
////import com.trn.ns.page.factory.ViewerPage;
////import com.trn.ns.test.configs.Configurations;
////import com.trn.ns.utilities.ExtentManager;
////
//////package com.trn.ns.test.obsolete;
//////import java.awt.AWTException;
//////import java.util.Set;
//////
//////import org.testng.annotations.BeforeMethod;
//////import org.testng.annotations.Listeners;
//////import org.testng.annotations.Test;
//////import com.relevantcodes.extentreports.ExtentTest;
//////import com.trn.ns.page.factory.CircleAnnotation;
//////import com.trn.ns.page.factory.EllipseAnnotation;
//////import com.trn.ns.page.factory.LoginPage;
//////import com.trn.ns.page.factory.MeasurementWithUnit;
//////
//////import com.trn.ns.page.factory.PatientListPage;
//////import com.trn.ns.page.factory.PointAnnotation;
//////import com.trn.ns.page.factory.PolyLineAnnotation;
//////import com.trn.ns.page.factory.SimpleLine;
//////
//////import com.trn.ns.page.factory.TextAnnotation;
//////import com.trn.ns.page.factory.ViewerPage;
//////import com.trn.ns.test.base.TestBase;
//////import com.trn.ns.test.configs.Configurations;
//////import com.trn.ns.utilities.DataReader;
//////import com.trn.ns.utilities.ExtentManager;
//////
//////
//////
//////@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//////public class ExternalCommandTest extends TestBase  {
//////
//////	private ViewerPage viewerPage;
//////	private LoginPage loginPage;
//////	private PatientListPage patientPage;
//////	
//////	private ExtentTest extentTest;
//////
//////	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//////	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, liver9filePath);
//////
//////	String filePath = Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//////	String GSPS_PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME_TEXTOVERLAY, filePath);
//////	private PointAnnotation point;
//////	private CircleAnnotation circle;
//////	private EllipseAnnotation ellipse;
//////	private MeasurementWithUnit lineWithUnit;
//////	private SimpleLine line;
//////	private PolyLineAnnotation poly;
//////	private TextAnnotation textAnn;
//////
//////	@BeforeMethod(alwaysRun=true)
//////	public void beforeMethod() throws InterruptedException{
//////
//////		loginPage = new LoginPage(driver);
//////		loginPage.navigateToBaseURL();
//////		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//////
//////		patientPage = new PatientListPage(driver);
//////		patientPage.waitForPatientPageToLoad();
//////
//////	}
//////
//////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US476"})
//////	public void test30_US476_TC1959_VerifyScrollCommandOnMultiMointorOperation() throws AWTException, InterruptedException 
//////	{
//////		extentTest = ExtentManager.getTestInstance();
//////		extentTest.setDescription("Verify that the scroll command on Multi Mointor operation");
//////
//////		patientPage.clickOnPatientRow(patientName);
//////
//////		
//////		studyPage.waitForSingleStudyToLoad();
//////		studyPage.clickOntheFirstStudy();
//////
//////
//////		viewerPage = new ViewerPage(driver);
//////		viewerPage.waitForViewerpageToLoad();
//////
//////		//start cine from keyboard 
//////		viewerPage.playOrStopCineUsingKeyboardCKey();
//////		viewerPage.waitForSilicesToChange(1);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/8]", "Verify Cine is being played on Viewbox1");
//////		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
//////
//////		//open a child window and verify cine does not stop on parent window
//////		viewerPage.openOrCloseChildWindows(2);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/8]", "Verify Cine is stopped on opening child window");
//////		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on opening child window","Cine is stopped on viewbox1");
//////
//////		//switch to parent window and start scroll
//////		viewerPage.switchToNewWindow(1);	
//////		viewerPage.playOrStopCineUsingKeyboardCKey();
//////		viewerPage.waitForSilicesToChange(1);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/8]", "Verify Cine is being played on Viewbox1");
//////		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
//////
//////		//change layout to 1x2 so that study get forward child window
////////		viewerPage.selectLayout(viewerPage.oneByTwoLayoutIcon, true);
//////
//////		//verify Cine is stopped on parent window after layout change
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/8]", "Verify Cine is stopped on changing layout on parent window");
//////		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on changing layout on parent window","Cine is stopped on viewbox1 in parent window");
//////
//////		//start cine from keyboard
//////		viewerPage.playOrStopCineUsingKeyboardCKey();
//////		viewerPage.waitForSilicesToChange(1);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/8]", "Verify Cine is being played on Viewbox1 on parent window");
//////		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
//////
//////		//increase the number of Child window and verify Cine is stopped
//////		viewerPage.openOrCloseChildWindows(3);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/8]", "Verify Cine is stopped on increasing child window");
//////		viewerPage.assertTrue(viewerPage.isCineStopped(1),"Verify Cine is stopped on increasing child window","Cine is stopped on viewbox1");
//////
//////		//switch to parent window and start scroll
//////		viewerPage.switchToNewWindow(1);	
//////		viewerPage.playOrStopCineUsingKeyboardCKey();
//////		viewerPage.waitForSilicesToChange(1);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/8]", "Verify Cine is being played on Viewbox1");
//////		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");
//////
//////		//close one of the child window and verify cine is not stopped
//////		viewerPage.openOrCloseChildWindows(2);
//////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/8]", "Verify Cine does not stopped on decreasing child window");
//////		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is stopped on decreasing a child window","Cine is being played on viewbox1");
//////	}
//////
//////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimointor","US476"})
//////	public void test34_US476_TC2048_01_VerifyAnnotationsUnSelectedOnNavigation() throws AWTException, InterruptedException 
//////	{
//////		extentTest = ExtentManager.getTestInstance();
//////		extentTest.setDescription("Annotation-Navigation- Verify that annotation commands should get stopped while Navigation");
//////
//////		patientPage.clickOnPatientRow(patientName);
//////		
//////		studyPage.clickOntheFirstStudy();
//////
//////		viewerPage = new ViewerPage(driver);
//////		viewerPage.waitForViewerpageToLoad();
//////
//////		//Opening child window for navigation
//////		viewerPage.openOrCloseChildWindows(2);
////////		String parentWindow = viewerPage.getCurrentWindowID();
////////		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
////////		for (String childWindow : childWinHandles) {				
////////			if(!childWindow.equals(parentWindow)){
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.maximizeWindow();
////////
////////				viewerPage.switchToWindow(parentWindow);
////////
////////				//Verifying with ellipse annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Ellipse" );
//////////				viewerPage.selectEllipseAnnotationFromContextMenu(1);
////////				ellipse.drawEllipse(1, -200, -50, -100,-150);	
////////
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/14]", "Verify that the Ellipse is selected");
////////				viewerPage.assertTrue(ellipse.isEllipseSelected(1,1), "Verifying the ellipse is selected", "Ellipse is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/14]", "Verify that the Ellipse is not selected");
////////				viewerPage.assertFalse(ellipse.isEllipseSelected(1,1),  "Verifying the ellipse is not selected", "Ellipse is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete drawn Ellipse" );
////////				ellipse.selectEllipse(1, 1);
////////
//////////				viewerPage.deleteSelectedEllipse();
////////
////////				//Verifying with circle annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Circle" );
//////////				viewerPage.selectCircleFromQuickToolbar(1);
////////				circle.drawCircle(1, -200, -50, -100,-100);	
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/14]", "Verify that the Circle is selected");
////////				viewerPage.assertTrue(circle.isCircleSelected(1,1), "Verifying the circle is selected", "Circle is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/14]", "Verify that the Circle is not selected");
////////				viewerPage.assertFalse(circle.isCircleSelected(1,1),  "Verifying the circle is not selected", "Circle is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete Circle" );
////////				viewerPage.selectCircle(1, 1);
////////				viewerPage.deleteSelectedCircle();
////////
////////				//Verifying with linear measurement annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Measurement" );
////////				viewerPage.selectRulerMeasurementFromContextMenu(1);
////////				viewerPage.drawLine(1, 60, 60, 150, 150);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/14]", "Verify that the Linear measurement is selected");
////////				viewerPage.assertTrue(lineWithUnit.isLinearMeasurementSelected(1,1), "Verify that the linear measurement is selected", "Linear measurement is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/14]", "Verify that the Linear measurement is not selected");
////////				viewerPage.assertFalse(lineWithUnit.isLinearMeasurementSelected(1,1), "Verify that the linear measurement is not selected", "Linear measurement is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and delete measurement" );
////////				lineWithUnit.selectLinearMeasurement(1, 1);
////////				viewerPage.deleteAllAnnotation(1);
////////
////////				//Verifying with line annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Line" );
////////				line.selectLinearMeasurementFromContextMenu(1);
////////				viewerPage.drawLine(1,20,0,80,0);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/14]", "Verify that the Line is selected");
////////				viewerPage.assertTrue(line.isLineSelected(1,1), "Verify that the line is selected", "Line is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[8/14]", "Verify that the Line is not selected");
////////				viewerPage.assertFalse(line.isLineSelected(1,1), "Verify that the line is not selected", "Line is not selected");
////////
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn line" );
////////				line.selectLine(1, 1);
////////				viewerPage.deleteSelectedLine();
////////
////////				//Verifying with Point annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw Point" );
////////				viewerPage.selectPointAnnotationFromContextMenu(1);
////////				point.drawPointAnnotationMarkerOnViewbox(1,100,-100);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[9/14]", "Verify that the point is selected");
////////				viewerPage.assertTrue(point.isPointSelected(1,1), "Verify that the point is selected", "Point is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[10/14]", "Verify that the point is not selected");
////////				viewerPage.assertFalse(point.isPointSelected(1,1), "Verify that the point is not selected", "Point is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn point" );
////////				viewerPage.selectPoint(1, 1);
////////				viewerPage.deleteSelectedPoint();
////////
////////				//Verifying with Polyline annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw polyline" );
////////				viewerPage.selectPolylineFromContextMenu(1);
////////				poly.drawPolyLineExitUsingESCKey(1, new int[] {25,44,20,-32,37,-23,37,-9});
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[11/14]", "Verify that the polyline is selected");
////////				viewerPage.assertTrue(poly.isPolylineSelected(1), "Verify that the polyline is selected", "Polyline is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[12/14]", "Verify that the polyline is not selected");
////////				viewerPage.assertFalse(poly.isPolylineSelected(1), "Verify that the polyline is not selected", "Polyline is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn polyline" );
////////				poly.selectPolyline(1, 1);
////////				viewerPage.deleteSelectedPolyline();
////////
////////				//Verifying with text annotation annotation
////////				//Step 1  Draw annotation and verify that it is selected
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Select and draw text annotation" );
////////				textAnn.selectTextArrowFromContextMenu(1);
////////				textAnn.drawText(1, -250, -50, "Viewbox1_Text");
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[13/14]", "Verify that the text annotation is selected");
////////				viewerPage.assertTrue(textAnn.isTextAnnotationSelected(1,1), "Verify that the text annotation is selected", "Text annotation is selected");
////////
////////				//Navigate to the child window
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
////////				viewerPage.switchToWindow(childWindow);
////////				viewerPage.switchToWindow(parentWindow);
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[14/14]", "Verify that the text annotation is not selected");
////////				viewerPage.assertFalse(textAnn.isTextAnnotationSelected(1,1), "Verify that the text annotation is not selected", "Text annotation is not selected");
////////
////////				//Deleting the drawn annotation
////////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Delete drawn text annotation" );
////////				viewerPage.selectTextAnnotLine(1, 1);
////////				viewerPage.deleteSelectedTextAnnotation();
////////			}
////////		}
////////		viewerPage.openOrCloseChildWindows(1);
//////	}
//////
//////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US476"})
//////	public void test34_US476_TC2048_02_VerifyShowHideEditboxUnSelectedOnNavigation() throws AWTException, InterruptedException 
//////	{
//////		extentTest = ExtentManager.getTestInstance();
//////		extentTest.setDescription("Annotation-Navigation- Verify that annotation commands should get stopped while Navigation");
//////
//////		patientPage.clickOnPatientRow(patientName);
//////		
//////		studyPage.clickOntheFirstStudy();
//////
//////		viewerPage = new ViewerPage(driver);
//////		viewerPage.waitForViewerpageToLoad();
//////
//////		//Opening child window for navigation
//////		viewerPage.openOrCloseChildWindows(2);
//////		String parentWindow = viewerPage.getCurrentWindowID();
//////		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//////		for (String childWindow : childWinHandles) {				
//////			if(!childWindow.equals(parentWindow)){
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.maximizeWindow();
//////
//////				viewerPage.switchToWindow(parentWindow);
//////				//Enable Image number editbox on viewbox1 and change active viewbox
//////				viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the image number input box is highlighted","TC2048_checkpoint1");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the image number input box is not highlighted","TC2048_checkpoint2");
//////
//////				//Enable zoom editbox on viewbox1 and change active viewbox
//////				viewerPage.click(viewerPage.getZoomValueOverlay(1));
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the zoom input box is highlighted","TC2048_checkpoint3");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the zoom input box is not highlighted","TC2048_checkpoint4");
//////
//////				//Enable window center editbox on viewbox1 and change active viewbox
//////				viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the window center input box is highlighted","TC2048_checkpoint5");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the window center input box is not highlighted","TC2048_checkpoint6");
//////
//////				//Enable window width editbox on viewbox1 and change active viewbox
//////				viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the window width input box is highlighted","TC2048_checkpoint7");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the window width input box is not highlighted","TC2048_checkpoint8");
//////			}
//////		}
//////	}
//////
//////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US476"})
//////	public void test34_US476_TC2048_03_VerifyAnnotationGSPSFocusDisappearOnOnNavigation() throws AWTException, InterruptedException {
//////		extentTest = ExtentManager.getTestInstance();
//////		extentTest.setDescription("Annotation-Navigation- Verify that annotation commands should get stopped while Navigation.(\"GSPS Focus Finding\")");
//////		patientPage.clickOnPatientRow(patientName);
//////
//////		
//////		studyPage.waitForSingleStudyToLoad();
//////		studyPage.clickOntheFirstStudy();
//////
//////		viewerPage = new ViewerPage(driver);
//////		viewerPage.waitForViewerpageToLoad();
//////
//////		//Opening child window for navigation
//////		viewerPage.openOrCloseChildWindows(2);
//////		String parentWindow = viewerPage.getCurrentWindowID();
//////		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//////		for (String childWindow : childWinHandles) {				
//////			if(!childWindow.equals(parentWindow)){
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.maximizeWindow();
//////
//////				viewerPage.switchToWindow(parentWindow);
//////
//////				/*		
//////				 * Checking for TextAnnotation
//////				 * 
//////				 */	
//////				String textAnnotation = "Automation_text";
//////				//Create Text annotation on viewbox1 and opening radial menu
//////				textAnn.selectTextArrowFromContextMenu(1);
//////				textAnn.drawText(1, 10, 20, textAnnotation);
//////				viewerPage.openGSPSRadialMenu(textAnn.getTextAnnotation(1, 1));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[1]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[2]","verifying GSPS menu absence after navigation");
//////				viewerPage.assertTrue(textAnn.verifyAcceptedTextAnnotationPresence(1, 1, true),"Verify Text annotation is loses GSPS foucs on navigation","Text annotation is not highlighted");
//////
//////				/*
//////				 * Checking for Point
//////				 * 
//////				 */	
//////				point.selectPointAnnotationIconFromRadialMenu(1);
//////				point.drawPointAnnotationMarkerOnViewbox(1, -100, 100);
//////				viewerPage.openGSPSRadialMenu(point.getPoint(1, 1).get(1));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[3]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[4]","verifying GSPS menu absence before navigation");
//////				viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"Verify Point annotation is loses GSPS foucs on navigation","Point annotation is not highlighted");
//////
//////				/*
//////				 * Checking for polyline
//////				 * 
//////				 */	
//////				poly.selectPolylineFromRadialMenu(1);		
//////				poly.drawPolyLineExitUsingESCKey(2, new int[] {25,44,20,-32,37,-23,37,-9});
//////				viewerPage.openGSPSRadialMenu(poly.getLinesOfPolyLine(2,1).get(0));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[5]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[6]","verifying GSPS menu absence before navigation");
////////				viewerPage.assertTrue(viewerPage.verifyPolyLineAnnotationIsAcceptedGSPS(2, 1),"Verify Point annotation is loses GSPS foucs on navigation","Point annotation is not highlighted");
//////				viewerPage.deleteAllAnnotation(1);
//////				/*
//////				 * Checking for Line Segment
//////				 * 
//////				 */	
//////				line.selectLinearMeasurementFromContextMenu(1);		
//////				viewerPage.dragAndReleaseOnViewerWithClick(-100, 70, 70,0);
//////				viewerPage.click(viewerPage.getViewPort(1));
//////				viewerPage.openGSPSRadialMenu(line.getLine(1,1).get(0));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[7]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[8]","verifying GSPS menu absence before navigation");
//////
//////				/*
//////				 * Checking for LinearMeasurement
//////				 * 
//////				 */	
//////				lineWithUnit.selectDistanceMeasurementFromRadial(1);		
//////				viewerPage.dragAndReleaseOnViewerWithClick(-70, 0, 50,0);
//////				viewerPage.openGSPSRadialMenu(lineWithUnit.getLinearMeasurementsText(1).get(0));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[9]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[10]","verifying GSPS menu absence before navigation");
//////				viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Verify Measurement annotation is loses GSPS foucs on navigation","Measurement annotation is not highlighted");
//////
//////				/*
//////				 * Checking for ellipse
//////				 * 
//////				 */	
//////				ellipse.selectEllipseAnnotationFromContextMenu(1);
//////				ellipse.drawEllipse(1, -100, -100,50,60);
//////				viewerPage.click(viewerPage.getViewPort(1));
//////				viewerPage.openGSPSRadialMenu(ellipse.getEllipses(1).get(0));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[11]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[12]","verifying GSPS menu absence before navigation");
//////				viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"Verify Ellipse annotation is loses GSPS foucs on navigation","Ellipse annotation is not highlighted");
//////				viewerPage.deleteAllAnnotation(1);
//////
//////				/*
//////				 * Checking for circle
//////				 * 
//////				 */
//////				circle.selectCircleAnnotationIconFromRadialMenu(1);
//////				circle.drawCircle(1, 60, 50,30,30);
//////				viewerPage.click(viewerPage.getViewPort(1));
//////				viewerPage.openGSPSRadialMenu(circle.getAllCircles(1).get(0));
//////				viewerPage.assertTrue(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[13]","verifying GSPS menu presence before navigation");
//////				//Navigate to the child window and back to parent window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertFalse(viewerPage.verifyRejectGSPSRadialMenu(),"Checkpoint[14]","verifying GSPS menu absence before navigation");
//////				viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"Verify Circle annotation is loses GSPS foucs on navigation","Circle annotation is not highlighted");
//////
//////			}
//////		}
//////	}
//////
//////	@Test(groups ={"IE11","Chrome","firefox","Edge","multimonitor","US476"})
//////	public void test34_US476_TC2048_04_VerifyAnnotationGSPSFindingStateDisappearOnNavigation() throws AWTException, InterruptedException 
//////	{
//////		extentTest = ExtentManager.getTestInstance();
//////		extentTest.setDescription("Annotation-Navigation- Verify that annotation commands should get stopped while Navigation.(\"Finding State\")");
//////
//////		patientPage.clickOnPatientRow(patientName);
//////
//////		
//////		studyPage.waitForSingleStudyToLoad();
//////		studyPage.clickOntheFirstStudy();
//////
//////		viewerPage = new ViewerPage(driver);
//////		viewerPage.waitForViewerpageToLoad();
//////
//////		//Opening child window for navigation
//////		viewerPage.openOrCloseChildWindows(2);
//////		String parentWindow = viewerPage.getCurrentWindowID();
//////		Set<String> childWinHandles = viewerPage.getAllOpenedWindowsID();
//////		for (String childWindow : childWinHandles) {				
//////			if(!childWindow.equals(parentWindow)){
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.maximizeWindow();
//////
//////				viewerPage.switchToWindow(parentWindow);
//////
//////				// Drawing  TextAnnotation
//////				String textAnnotation = "Automation_text";
//////				textAnn.selectTextArrowFromContextMenu(1);
//////				textAnn.drawText(1, 150, 90, textAnnotation);
//////
//////				// Drawing  Linear Distance Measurement
//////				lineWithUnit.selectDistanceMeasurementFromRadial(1);		
//////				viewerPage.dragAndReleaseOnViewerWithClick(-130, 0, 50,0);
//////
//////				// Drawing  ellipse
//////				ellipse.selectEllipseAnnotationFromContextMenu(1);
//////				ellipse.drawEllipse(1, -100, -100,50,60);
//////
//////				// Drawing  point		
//////				point.selectPointAnnotationIconFromRadialMenu(1);
//////				point.drawPointAnnotationMarkerOnViewbox(1, 80, 90);
//////
//////				//Drawing Circle
//////				circle.selectCircleAnnotationIconFromRadialMenu(1);
//////				circle.drawCircle(1, 100, 100,90,90);
//////
//////				//Verifying with circle annotation
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verifying that on navigating to child window, Circle annotation loses its GSPS focus");
//////
//////				viewerPage.navigateGSPSBackUsingKeyboard();	
//////				viewerPage.assertTrue(circle.verifyCircleAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Circle annotation having GSPS focus","Circle annotation is highlighted");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertTrue(circle.verifyCircleAnnotationIsAcceptedGSPS(1, 1),"Verify Circle annotation is loses GSPS foucs on navigation","Circle annotation is not highlighted");
//////				viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Verify GSPS radial menu close on performing navigation", "GSPS radial menu close on performing navigation");
//////
//////
//////				//verifying with point annotation
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/6]", "Verifying that on navigating to child window, Point annotation loses its GSPS focus");
//////
//////				viewerPage.navigateGSPSBackUsingKeyboard();		
//////				viewerPage.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(1, 1),"Verify Point annotation having GSPS focus","Point annotation is highlighted");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertTrue(point.verifyPointAnnotationIsAcceptedInActiveGSPS(1, 1),"Verify Point annotation is loses GSPS foucs on navigation","Point annotation is not highlighted");
//////				viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Verify GSPS radial menu close on performing navigation", "GSPS radial menu close on performing navigation");
//////
//////				//Verifying with Ellipse annotation
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/6]", "Verifying that on navigating to child window, Ellipse annotation loses its GSPS focus");
//////
//////				viewerPage.navigateGSPSBackUsingKeyboard();		
//////				viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(1, 1),"Verify Ellipse annotation having GSPS focus","Ellipse annotation is highlighted");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertTrue(ellipse.verifyEllipseAnnotationIsAcceptedGSPS(1, 1),"Verify Ellipse annotation is loses GSPS foucs on navigation","Ellipse annotation is not highlighted");
//////				viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Verify GSPS radial menu close on performing navigation", "GSPS radial menu close on performing navigation");
//////
//////				//Verifying with measurement annotation
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/6]", "Verifying that on navigating to child window, Measurement annotation loses its GSPS focus");
//////
//////				viewerPage.navigateGSPSBackUsingKeyboard();		
//////				viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(1, 1),"Verify Measurement annotation having GSPS focus","Measurement annotation is highlighted");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertTrue(lineWithUnit.verifyLinearMeasurementAnnotationIsAcceptedGSPS(1, 1),"Verify Measurement annotation is loses GSPS foucs on navigation","Measurement annotation is not highlighted");
//////				viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Verify GSPS radial menu close on performing navigation", "GSPS radial menu close on performing navigation");
//////
//////				//Verifying with text annotation
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/6]", "Verifying that on navigating to child window, Text annotation loses its GSPS focus");
//////
//////				viewerPage.navigateGSPSBackUsingKeyboard();
//////				viewerPage.assertTrue(textAnn.verifyAcceptedTextAnnotationPresence(1, 1, true),"Verify Text annotation having GSPS focus","Text annotation is highlighted");
//////				//Navigate to the child window
//////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Navigate to child window and back to the parent window" );
//////				viewerPage.switchToWindow(childWindow);
//////				viewerPage.switchToWindow(parentWindow);
//////				viewerPage.assertTrue(textAnn.verifyAcceptedTextAnnotationPresence(1, 1, true),"Verify Text annotation is loses GSPS foucs on navigation","Text annotation is not highlighted");
//////				viewerPage.assertFalse(viewerPage.isElementPresent(viewerPage.gspsPrevious), "Verify GSPS radial menu close on performing navigation", "GSPS radial menu close on performing navigation");
//////
//////			}
//////		}
//////		viewerPage.openOrCloseChildWindows(1);
//////
//////	}
//////
//////}
////
////@Test(groups ={"IE11","Chrome","firefox","Edge","US476"},enabled=false)
////	//This test case is no longer valid after fix for DE798
////	public void test01_US476_TC1906_VerifyTextAnnotationCommandClosesOnCine() throws  InterruptedException 
////	{
////		extentTest = ExtentManager.getTestInstance();
////		extentTest.setDescription("Verify that text annotation command is getting closed on playing cine");
////
////		patientPage.clickOnPatientRow(patientName);
////
////		
////		studyPage.waitForSingleStudyToLoad();
////		studyPage.clickOntheFirstStudy();
////
////
////		viewerPage = new ViewerPage(driver);
////		viewerPage.waitForViewerpageToLoad();
////
////		textAn = new TextAnnotation(driver);
////
////		//Select text annotation from radial menu and draw text annotation marker without entering text
////		textAn.selectTextArrowFromRadialMenu(1);
////		viewerPage.mouseHoverWithClick("presence", viewerPage.getViewPort(1),80, 90);
////
////		//Start playing Cine using keyboard shortcut
////		int currentScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
////		viewerPage.playOrStopCineUsingKeyboardCKey();
////		viewerPage.waitForSilicesToChange(1);
////
////		//stop playing slice
////		viewerPage.playOrStopCineUsingKeyboardCKey();
////		int forwardScrollPosition = viewerPage.getCurrentScrollPositionOfViewbox(1);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify cine is getting played on Viewbox1" );
////		viewerPage.assertNotEquals(currentScrollPosition,forwardScrollPosition, "Verifying Cine is being played on Viewbox1", "Cine is getting played on viewbox1");
////
////		//Verify that text annotation command stop and no text annotation is drawn on Viewbox1
////		viewerPage.inputImageNumber(1, 1);
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify text annotation command stop when cine is played" );
////		viewerPage.assertFalse(textAn.isTextAnnotationPresent(1), "Verify text annotation is not drawn on viewbox1", "There is no instance of text annotation on viewbox1");
////	}
//
// /*   @Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
//	public void test04_US476_TC1919_VerifyNorthStarMenuCloseOnOpeningOtherMenu() throws  InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that NorthStar Menu close after opening other menu");
//
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//
//		patientPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//		//verify North star Menu closes on opening radial Menu
//		viewerPage.openEurekaLogo();
//		viewerPage.openRadialMenu(viewerPage.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Northstar Menu closes on opening radial menu");
//		viewerPage.assertFalse(viewerPage.gridIcon.isDisplayed(),"Verify North Star Menu closes on opening Radial Menu","NorthStar menu is not present");
//
//		//verify North star Menu close on opening Zoom overlay
//		viewerPage.openEurekaLogo();
//		viewerPage.click(viewerPage.getZoomLabelOverlay(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Northstar Menu closes on opening overlay menu");
//		viewerPage.assertFalse(viewerPage.gridIcon.isDisplayed(),"Verify Northstar Menu closes on opening overlay menu","NorthStar menu is not present");
//
//		//verify NorthStar menu close on opening content selector
//		viewerPage.openEurekaLogo();
//		contentSelector.openContentSelector(viewerPage.getViewbox(2), viewerPage.getSeriesDescription(2));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Northstar Menu closes on opening content selector");
//		viewerPage.assertFalse(viewerPage.gridIcon.isDisplayed(),"Verify Radial Menu closes on opening content selector","NorthStar menu is not present");
//
//		//verify NorthStar menu close on opening Context Menu
//		viewerPage.openEurekaLogo();
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify Northstar Menu closes on opening Toolbox menu");
//		viewerPage.assertFalse(viewerPage.gridIcon.isDisplayed(),"Verify Northstar Menu closes on opening Toolbox menu","NorthStar menu is not present");
//
//		//verify NorthStar menu close on opening GSPS Radial Menu
//		viewerPage.openEurekaLogo();
//		point = new PointAnnotation(driver);
//		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Northstar Menu close on opening GSPS radial menu");
//		viewerPage.assertFalse(viewerPage.gridIcon.isDisplayed(),"Verify Northstar Menu close on opening GSPS radial menu","NorthStar menu is not present");
//	}*/
////
//
//
//@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
//	public void test02_US476_TC1919_VerifyToolBoxCloseOnOpeningOtherMenu() throws  InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that Tool Box close after opening other menu");
//
//		patientPage.clickOnPatientRow(GSPS_PatientName);
//
//		patientPage.clickOntheFirstStudy();
//
//		viewerPage = new ViewerPage(driver);
//		viewerPage.waitForViewerpageToLoad();
//		contentSelector = new ContentSelector(driver);
//
//		//verify context menu close on North star Menu
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Tool box closes on opening Northstar logo");
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(),"Verify Tool box closes on opening Northstar logo","Tool box is not present");
//
//		//verify context menu close on opening Zoom overlay
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		viewerPage.click(viewerPage.getZoomLabelOverlay(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify Tool box closes on opening overlay menu");
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(),"Verify Tool box closes on opening overlay menu","Tool box is not present");
//
//		//verify context menu close on opening content selector
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		contentSelector.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Tool box closes on opening content selector");
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(),"Verify Tool box closes on opening content selector","Tool box is not present");
//
//		//verify context menu close on opening radial Menu
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		viewerPage.openRadialMenu(viewerPage.getViewPort(1));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify Tool box closes on opening radial menu");
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(),"Verify Tool box closes on opening radial menu","Tool box is not present");
//
//		//verify context menu close on opening GSPS radial Menu
//		viewerPage.openContextMenu(viewerPage.getViewPort(1));
//		point = new PointAnnotation(driver);
//		viewerPage.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Tool box closes on opening GSPS radial menu");
//		viewerPage.assertFalse(viewerPage.contextMenu.isDisplayed(),"Verify Tool box closes on opening GSPS radial menu","Tool box is not present");
//	}
/*
@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test06_US476_TC1919_VerifyContentSelector_oldMenuCloseOnOpeningOtherMenu() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Content Selector Menu close after opening other menu");

		patientPage.clickOnPatientRow(GSPS_PatientName);

		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		//verify Content selector closes on opening radial Menu
		cs.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Content Selector Menu closes on opening radial menu");
		viewerPage.assertFalse(cs.contentcontainer.isDisplayed(),"Verify Content Selector Menu closes on opening Radial Menu","Content Selector menu is not present");

		//verify Content selector Menu  close on opening content selector
		cs.openContentSelector(viewerPage.getViewbox(2), viewerPage.getSeriesDescription(2));
		viewerPage.click(viewerPage.getZoomLabelOverlay(1));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Content Selector Menu closes on opening content selector");
		viewerPage.assertFalse(cs.contentcontainer.isDisplayed(),"Verify Content Selector Menu closes on opening content selector","Content Selector menu is not present");

		
		//verify Content selector  Menu close on opening GSPS Radial Menu
		cs.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
		point = new PointAnnotation(driver);
		point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Content Selector Menu close on opening GSPS radial menu");
		viewerPage.assertFalse(cs.contentcontainer.isDisplayed(),"Verify Content Selector Menu close on opening GSPS radial menu","Content Selector menu is not present");
	}

@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
public void test05_US476_TC1919_VerifyTextOverlayMenuCloseOnOpeningOtherMenu() throws  InterruptedException 
{
	extentTest = ExtentManager.getTestInstance();
	extentTest.setDescription("Verify that Text overlay Menu close after opening other menu");

	patientPage.clickOnPatientRow(GSPS_PatientName);

	patientPage.clickOntheFirstStudy();

	viewerPage = new ViewerPage(driver);
	viewerPage.waitForViewerpageToLoad();
	cs = new ContentSelector(driver);
	preset = new ViewerPresetMenu(driver);
	//verify Text overlay Menu closes on opening radial Menu
	viewerPage.click(viewerPage.getZoomLabelOverlay(1));
	viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Text overlay Menu closes on opening radial menu");
	viewerPage.assertFalse(preset.OverlayMenuTable.isDisplayed(),"Verify Text overlay Menu closes on opening Radial Menu","Text overlay menu is not present");

	//verify Text overlay Menu  close on opening content selector
	viewerPage.click(viewerPage.getZoomLabelOverlay(1));
	cs.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Text overlay Menu closes on opening content selector");
	viewerPage.assertFalse(preset.OverlayMenuTable.isDisplayed(),"Verify Text overlay Menu closes on opening content selector","Text overlay menu is not present");

	
	//verify Text overlay Menu close on opening GSPS Radial Menu
	viewerPage.click(viewerPage.getZoomLabelOverlay(1));
	point = new PointAnnotation(driver);
	point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Text overlay Menu close on opening GSPS radial menu");
	viewerPage.assertFalse(preset.OverlayMenuTable.isDisplayed(),"Verify Text overlay Menu close on opening GSPS radial menu","Text overlay menu is not present");
}

@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
public void test07_US476_TC1919_VerifyGSPSRadialMenuCloseOnOpeningOtherMenu() throws  InterruptedException 
{
	extentTest = ExtentManager.getTestInstance();
	extentTest.setDescription("Verify that Content Selector Menu close after opening other menu");

	patientPage.clickOnPatientRow(GSPS_PatientName);

	patientPage.clickOntheFirstStudy();

	viewerPage = new ViewerPage(driver);
	viewerPage.waitForViewerpageToLoad();
	point = new PointAnnotation(driver);
	cs = new ContentSelector(driver);

	//verify GSPS radial menu  Menu close on opening Context menu
	point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
	viewerPage.click(viewerPage.getZoomLabelOverlay(1));
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify GSPS Radial Menu closes on opening content selector");
	viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsNext),"Verify GSPS Radial Menu closes on opening content selector","GSPS Radial Menu is not present");

	//verify GSPS radial menu close on opening Tool box  Menu
	point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
	point.openGSPSRadialMenu(1);
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify GSPS Radial Menu closes on opening Toolbox menu");
	viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsNext),"Verify GSPS Radial Menu closes on opening Toolbox menu","GSPS Radial Menu is not present");

	//verify GSPS radial menu close on opening Content Selector Menu
	point.openGSPSRadialMenu(point.getHandlesOfPoint(1,1).get(0));
	cs.openContentSelector(viewerPage.getViewbox(1), viewerPage.getSeriesDescription(1));
	ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify GSPS Radial Menu close on opening Content Selector menu");
	viewerPage.assertFalse(viewerPage.isElementPresent(point.gspsNext),"Verify GSPS Radial Menu close on opening Content Selector menu","GSPS Radial Menu is not present");
}

	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test26_US476_TC2041_03_VerifyShowHideEditboxUnselectedOnMenuOpening() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Menu - Verify that annotation commands should get stopped while performing menu operations. Show-Hide Edit box");

		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		cs = new ContentSelector(driver);
		//Create Text annotation on viewbox1 and opening radial menu
		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", " Edit box to Menu opening tests");

		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the current image input box is highlighted","TC26_checkpoint1");
		viewerPage.performMouseRightClick(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the current image input box is not highlighted","TC26_checkpoint2");

		//Create Text annotation on viewbox1 and opening context menu
		viewerPage.click(viewerPage.getZoomValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[3] : Verifying the zoom value input box is highlighted","TC26_checkpoint3");
		viewerPage.openQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[4] : Verifying the zoom value input box is not highlighted","TC26_checkpoint4");

		//Create Text annotation on viewbox1 and Northstar logo
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[5] : Verifying the window center input box is highlighted","TC26_checkpoint5");
		viewerPage.click(viewerPage.EurekaLogo);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[6] : Verifying the window center input box is not highlighted","TC26_checkpoint6");

		//Create Text annotation on viewbox1 and opening window level present menu
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[7] : Verifying the window width input box is highlighted","TC26_checkpoint7");
		viewerPage.click(viewerPage.getWindowCenterLabelOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[8] : Verifying the window width input box is not highlighted","TC26_checkpoint8");

		//Create Text annotation on viewbox1 and opening window level present menu		
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[9] : Verifying the scrolled image input box is highlighted + menu window width preset is clicked","TC26_checkpoint9");
		viewerPage.click(viewerPage.getWindowWidthLabelOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[10] : Verifying the scrolled image input box is not highlighted post menu is opened","TC26_checkpoint10");

		//Create Text annotation on viewbox1 and opening zoom present menu
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[11] : Verifying the scrolled image input box is highlighted + menu zoom preset is clicked","TC26_checkpoint11");
		viewerPage.click(viewerPage.getZoomLabelOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[12] : Verifying the scrolled image input box is not highlighted post menu is opened","TC26_checkpoint12");

		//Create Text annotation on viewbox1 and opening content selector menu		
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[13] : Verifying the scrolled image input box is highlighted + menu content selector is clicked","TC26_checkpoint13");
		cs.openAndCloseSeriesTab(true);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[14] : Verifying the scrolled image input box is not highlighted post menu is opened","TC26_checkpoint14");

		//Create Text annotation on viewbox1 and opening log off menu
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[15] : Verifying the scrolled image input box is highlighted + menu logoff is clicked","TC26_checkpoint15");
		Header header = new Header(driver);
		header.openLogoffMenu();
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[16] : Verifying the scrolled image input box is not highlighted post menu is opened","TC26_checkpoint16");

	}
	
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test35_US476_TC2051_02_VerifyShowHideEditboxUnSelectedOnPerformingEmptyCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Empty Command - Verify that annotation commands should get stopped on performing empty command");

		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		//Enable Image number editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1/8] : Verifying the image number input box is highlighted","TC2051_checkpoint1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse click on viewbox" );
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2/8] : Verifying the image number input box is not highlighted","TC2051_checkpoint2");

		//Enable zoom editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getZoomValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[3/8] : Verifying the zoom input box is highlighted","TC2051_checkpoint3");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse click on viewbox" );
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[4/8] : Verifying the zoom input box is not highlighted","TC2051_checkpoint4");

		//Enable window center editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[5/8] : Verifying the window center input box is highlighted","TC2051_checkpoint5");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse click on viewbox" );
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[6/8] : Verifying the window center input box is not highlighted","TC2051_checkpoint6");

		//Enable window width editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[7/8] : Verifying the window width input box is highlighted","TC2051_checkpoint7");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse click on viewbox" );
		viewerPage.click(viewerPage.getViewPort(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[8/8] : Verifying the window width input box is not highlighted","TC2051_checkpoint8");

	} 
	
		@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test37_US476_TC2044_02_VerifyShowHideEditboxUnselectedOnViewerOperations() throws  InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Show-Hide Edit box : Annotation-Rendering- Verify that annotation commands should get stopped while Rendering.");

		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		//Rendering should disable Show-Hide Edit Box while performing zoom, WW and Pan from selecting through radial menu or through mouse events. 
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));		
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the image input box is highlighted","TC34_checkpoint1");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the image input box is not highlighted","TC34_checkpoint2");

		// applying the window leveling when zoom input box is highlighted
		viewerPage.click(viewerPage.getZoomValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[3] : Verifying the zoom value input box is highlighted","TC34_checkpoint3");
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1),0, 0, 100, -100);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[4] : Verifying the zoom value input box is not highlighted","TC34_checkpoint4");

		//applying the pan when WC input box is highlighted
		viewerPage.selectPanFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[5] : Verifying the window center input box is highlighted","TC34_checkpoint5");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[6] : Verifying the window center input box is not highlighted","TC34_checkpoint6");

		// applying zoom when window width input box is highlighted
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[7] : before applying the zoom, window width input box is highlighted","TC34_checkpoint7");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[8] : Verifying the window width input box is not highlighted","TC34_checkpoint8");

		//applying zoom when current scrolled image box is highlighted
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[9] : before applying the zoom,current scrolled input box is highlighted","TC34_checkpoint9");
		viewerPage.dragAndReleaseOnViewer(0, 0, 100, -50);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[10] : Verifying the input box is not highlighted","TC34_checkpoint10");


	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test39_US476_TC2046_02_VerifyShowHideEditboxUnSelectedOnMesurementOperation() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Mesurement - Verify that show edit box should get close while performing mesurement operation");

		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		//select linear measurement from radial menu and draw a linear measurement
		lineWithUnit = new MeasurementWithUnit(driver);
		lineWithUnit.selectDistanceFromQuickToolbar(1);
		lineWithUnit.drawLine(1, -20, -30, -40, 90);

		//select Point annotation from radial menu and draw a point annotation
		point = new PointAnnotation(driver);
		point.selectPointFromQuickToolbar(1);
		point.drawPointAnnotationMarkerOnViewbox(1, -100, -50);

		//select elipse from radial menu and draw a ellipse 
		ellipse = new EllipseAnnotation(driver);
		ellipse.selectEllipseFromQuickToolbar(1);
		ellipse.drawEllipse(1, 100, -50, 40, -50);

		//select Circle Annotation from radial menu and draw a circle
		circle = new CircleAnnotation(driver);
		circle.selectCircleFromQuickToolbar(1);
		circle.drawCircle(1, 70, 70, 90, 90);

		//Enable Image number editbox on viewbox1 and move linear measurement
		viewerPage.click(viewerPage.getImageCurrentScrollPositionOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1/8] : Verifying the image number input box is highlighted","TC2046_checkpoint1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "move linear measurement" );
		lineWithUnit.moveLinearMeasurement(1, 1, 20, 20);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2/8] : Verifying the image number input box is not highlighted","TC2046_checkpoint2");

		//Enable Zoom editbox on viewbox1 and resize linear measurement
		viewerPage.click(viewerPage.getZoomValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[3/8] : Verifying the zoom input box is highlighted","TC2046_checkpoint3");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "resize linear measurement" );
		lineWithUnit.selectLinearMeasurement(1, 1);
		lineWithUnit.resizeSelectedLinearMeasurement(1, 2, 30, 20);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[4/8] : Verifying the zoom input box is not highlighted","TC2046_checkpoint4");

		//Enable window center editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[5/8] : Verifying the window center input box is highlighted","TC2046_checkpoint5");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "move point annotation" );
		point.movePoint(1, 1, 10, 20);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[6/8] : Verifying the window center input box is not highlighted","TC2046_checkpoint6");

		//Enable window width editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[7/8] : Verifying the window width input box is highlighted","TC2046_checkpoint7");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "move circle annotation" );
		circle.selectCircle(1, 1);
		circle.moveSelectedCircle(1, -10, 20);
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[8/8] : Verifying the window width input box is not highlighted","TC2046_checkpoint8");

	}
	
		@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test24_US476_TC2050_02_VerifyShowHideEditboxUnSelectedOnChangingActiveViewbox() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Annotation-Active Viewbox - Verify that annotation commands should get stopped while changing active viewbox.");

		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();

		//Enable Image number editbox on viewbox1 and change active viewbox
		viewerPage.click(viewerPage.getSliceInfo(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the image number input box is highlighted","TC2050_checkpoint1");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the image number input box is not highlighted","TC2050_checkpoint2");

		//Enable zoom editbox on viewbox1 and change active viewbox
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getZoomValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the zoom input box is highlighted","TC2050_checkpoint3");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the zoom input box is not highlighted","TC2050_checkpoint4");

		//Enable window center editbox on viewbox1 and change active viewbox
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getWindowCenterValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the window center input box is highlighted","TC2050_checkpoint5");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the window center input box is not highlighted","TC2050_checkpoint6");

		//Enable window width editbox on viewbox1 and change active viewbox
		viewerPage.mouseHover(viewerPage.getViewPort(1));
		viewerPage.click(viewerPage.getWindowWidthValueOverlay(1));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[1] : Verifying the window width input box is highlighted","TC2050_checkpoint7");
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Mouse hover on other viewbox" );
		viewerPage.mouseHover(viewerPage.getViewPort(2));
		viewerPage.compareElementImage(protocolName,viewerPage.getViewPort(1), "Checkpoint[2] : Verifying the window width input box is not highlighted","TC2050_checkpoint8");

	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test21_US476_TC1945_VerifyScrollCommandNotStoppedOnPerformingZoom() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should not stop while performing zoom operation");

		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		preset = new ViewerPresetMenu(driver);
		
		//select Zoom from radial menu
		viewerPage.selectZoomFromQuickToolbar(viewerPage.getViewPort(1));

		//start cine from keyboard and perform a Zoom operation
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//variable to store current zoom percentage value
		int beforeZoom = viewerPage.getZoomLevel(1);

		//Perform Zoom operation
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 60, 60, 80, 80);

		//variable to store current zoom percentage value
		int afterZoom = viewerPage.getZoomLevel(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify zoom is performed using Mouse drag");
		viewerPage.assertNotEquals(beforeZoom, afterZoom, "Verify the zoom level perventage", "The Zoom level percentage change from "+beforeZoom+" to "+afterZoom);

		//verify Cine is not stopped while applying Zoom
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify Cine is being played on Viewbox1 while performing zoom operation");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing zoom operation","Cine is getting played on Viewbox1");

		//input zoom from overlay
		viewerPage.inputZoomNumber(50, 1);;

		//variable to store current zoom percentage value
		afterZoom = viewerPage.getZoomLevel(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify zoom is performed by manual input of zoom percentage");
		viewerPage.assertEquals(50, afterZoom, "Verify the zoom level perventage", "The Zoom level percentage change to 50");

		//verify Cine is not stopped on applying zoom through manual input
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify Cine is being played on Viewbox1 while performing zoom by manual input");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing zoom by manual input","Cine is getting played on Viewbox1");

		//select zoom to 100% from overlay and verify cine is stopped
		preset.selectZoomOverlay(1, ViewerPageConstants.ZOOM_TO_100_PERCENTAGE);

		//variable to store current zoom percentage value
		afterZoom = viewerPage.getZoomLevel(1);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify zoom is performed by selecting value from preset menu");
		viewerPage.assertEquals(100, afterZoom, "Verify the zoom level perventage", "The Zoom level percentage change to 100");

		//verify Cine is not stopped on applying Window leveling through manual input
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify Cine is being played on Viewbox1 while performing zoom using preset overlay");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing zoom using preset overlay","Cine is getting played on Viewbox1");

	}
	
	@Test(groups ={"IE11","Chrome","firefox","Edge","US476"})
	public void test20_US476_TC1945_VerifyScrollCommandNotStoppedOnPerformingWindowLevelingCommand() throws  InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the scroll command should not stop while performing window leveling");

		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOntheFirstStudy();


		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad();
		preset = new ViewerPresetMenu(driver);
		//select WL from radial menu
		viewerPage.selectWindowLevelFromQuickToolbar(viewerPage.getViewPort(1));

		//start cine from keyboard and perform a Window leveling
		viewerPage.playOrStopCineUsingKeyboardCKey();
		viewerPage.waitForSilicesToChange(1);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify Cine is being played on Viewbox1 using Radial command");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1","Cine is getting played on Viewbox1");

		//variable to store current WC and WW value
		int widthBefore = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
		int windowCenterBefore=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));

		//Perform Window leveling
		viewerPage.dragAndReleaseOnViewer(viewerPage.getViewPort(1), 60, 60, 120, 120);

		//variable to store current WC and WW value
		int widthafter = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
		int windowCenterafter=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/7]", "Verify window leveling is performed using Mouse");
		viewerPage.assertNotEquals(widthBefore, widthafter, "Verify the window width changes on window leveling", "Verified");
		viewerPage.assertNotEquals(windowCenterBefore, windowCenterafter, "Verify the window center changes on window leveling", "Verified");

		//verify Cine is not stopped on applying Window leveling
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/7]", "Verify Cine is being played on Viewbox1 while performing window leveling");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing window leveling","Cine is getting played on Viewbox1");

		//input WW and WC value from overlay
		viewerPage.inputWWNumber(220,1);
		viewerPage.inputWCNumber(100,1);

		//variable to store current WC and WW value
		widthafter = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
		windowCenterafter=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/7]", "Verify window leveling is performed by manual input of WW and WL");
		viewerPage.assertEquals(220, widthafter, "Verify the window width changes on window leveling", "Verified");
		viewerPage.assertEquals(100, windowCenterafter, "Verify the window center changes on window leveling", "Verified");

		//verify Cine is not stopped on applying Window leveling through manual input
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/7]", "Verify Cine is being played on Viewbox1 while performing window leveling by manual input of WW and WL");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing window leveling by manual input of WW and WL","Cine is getting played on Viewbox1");

		//select window level from Overlay menu
		preset.selectPresetValue(viewerPage.getWindowWidthLabelOverlay(1), ViewerPageConstants.HEAD,1);

		//variable to store current WC and WW value
		widthafter = Integer.parseInt(viewerPage.getValueOfWindowWidth(1));
		windowCenterafter=Integer.parseInt(viewerPage.getValueOfWindowCenter(1));

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[6/7]", "Verify window leveling is performed by selecting option from overlay");
		viewerPage.assertEquals(100, widthafter, "Verify the window width changes on window leveling", "Verified");
		viewerPage.assertEquals(45, windowCenterafter, "Verify the window center changes on window leveling", "Verified");

		//verify Cine is not stopped on applying Window leveling through manual input
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[7/7]", "Verify Cine is being played on Viewbox1 while performing window leveling using preset overlay");
		viewerPage.assertFalse(viewerPage.isCineStopped(1),"Verify Cine is being played on Viewbox1 while performing window leveling using preset overlay","Cine is getting played on Viewbox1");

	}

*/