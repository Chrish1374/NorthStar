package com.trn.ns.test.viewer.GSPS;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ActionLogConstant;
import com.trn.ns.page.factory.CircleAnnotation;
import com.trn.ns.page.factory.DatabaseMethodsADB;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;

import com.trn.ns.page.factory.PatientListPage;

import com.trn.ns.page.factory.Triangulation;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;


@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class TriangulationToolTest extends TestBase {

	private LoginPage loginPage;
	private PatientListPage patientPage;
	
	private ExtentTest extentTest;
	private CircleAnnotation circle;
	private DatabaseMethodsADB db;
	String nsAnalyticsDBName = Configurations.TEST_PROPERTIES.get("nsAnalyticsdbName");
	
	String filePath=Configurations.TEST_PROPERTIES.get("MR_Lspine_Filepath");
	String MrLspinePatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,filePath);
	
	String ADCphilips_FilePath = Configurations.TEST_PROPERTIES.get("ADC_philips_FilePath");
	String ADC_philips_Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, ADCphilips_FilePath);



	private Triangulation tool;
	private HelperClass helper;

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod() {

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
	}

	//	TC1707	Image annotation: circle
	@Test(groups ={"Chrome","IE11","Edge","US1256","Positive"})
	public void test01_US1256_TC7229_TC7230_TC7272_TC7262_verifyTraingulationToolInRadialAndContextMenu() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user is able to view the triangulation tool in the radial menu.<br>"+
		"Verify that ALT+Left mouse click selects the triangulation tool.<br>"+
		"Verify that Triangulation icon is still visible in the inner arc of a radial menu, when user clicks three dots to open the outer arc.<br>"+
		"Verify that the triangulation tool is not displayed in the context menu.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);
	

		tool=new Triangulation(driver);
		tool.openQuickToolbar(tool.getViewPort(1));
	
		tool.assertTrue(tool.isElementPresent(tool.traingulationIcon), "Checkpoint[1/9]", "Verify presence of traingulation Icon in radial menu");
		tool.compareElementImage(protocolName, tool.traingulationIcon,"Checkpoint[2/9]","TC01_TraingulatioIcon");
		
		
		//Verify that ALT+Left mouse click selects the triangulation tool.
		tool.click(tool.getViewPort(2));
		tool.click(tool.getViewPort(1));
		tool.pressAltlLeftWithOffset(tool.getViewPort(1),0,0);
		tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(2, 1), "Checkpoint[4/9]","Verified that cross hair visible in second viewbox.");
		tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(3, 1), "Checkpoint[5/9]","Verified that cross hair visible in third viewbox.");
		tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(4, 1), "Checkpoint[6/9]","Verified that cross hair visible in fourth viewbox.");
		
	
	}


	@Test(groups ={"Chrome","IE11","Edge","F13","US1256","US2329","DR2531","Positive","BVT","F1090","E2E"})
	public void test02_US1256_TC7233_TC7247_TC7289_US2329_TC10165_DR2531_TC10604_verifyCrossHairForLeftClickAndForDifferentAnnotationSelected() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the left click and hold/release functionality for the triangulation tool.<br>"+
		"Verify that the point location(Cross hairs) are no longer present when the user selects any other annotation.<br>"+
		"Verify that the localizer lines are not displayed when triangulation is active.<br>"+
		"[Risk and Impact]: Verify the existing functionalities of tools present in viewer toolbar and quick toolbox, in native plane. <br>"+
		"Verify that triangulation point is displayed when clicked on any region in source image.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);
		circle=new CircleAnnotation(driver);
		tool.selectTraingulationFromQuickToolbar(1);
		for(int i=20;i<50;i+=10)
		{
	    tool.clickAt(10,i);
	    tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[1.1/5]","Validate cross hair not visible in active viewbox. ");
	    for(int j=2;j<=tool.getNumberOfCanvasForLayout();j++)
	    tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[1."+j+"/5]", "Validate cross hair visible in viewbox-"+j);
		}
	  
		//verify cross hair when click and hold release
		tool.mouseHover(tool.getViewPort(1));
		tool.clickAndHoldReleaseQuick(10, 50);
		for(int i=1;i<=tool.getNumberOfCanvasForLayout();i++)
			 tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(i, 1), "Checkpoint[2."+i+"/5]","Validate cross hair not visible in  viewbox-"+i);
		
        tool.assertTrue(tool.verifyLocalizerLinePresence(4), "Checkpoint[3/5]", "Verified that localizer line visible in fourth viewbox.");
      
        //verify cross hair after selecting another annotation
        circle.selectCircleFromQuickToolbar(1);
		for(int i=1;i<=tool.getNumberOfCanvasForLayout();i++)
			 tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(i, 1), "Checkpoint[4."+i+"/5]","Validate cross hair not visible in  viewbox-"+i);
		
        tool.assertTrue(tool.verifyLocalizerLinePresence(4), "Checkpoint[5/5]", "Verified that localizer line visible in fourth viewbox.");
        
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1256","Negative"})
	public void test03_US1256_TC7247_verifyCrossHairWhenOtherAnnotationSelected() throws  InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the point location(Cross hairs) are no longer present when the user selects any other annotation.");
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);
		circle=new CircleAnnotation(driver);
		
		tool.waitForViewerpageToLoad(1);
		tool.selectTraingulationFromQuickToolbar(1);
		for(int i=20;i<50;i+=10)
		{
	    tool.clickAt(10,i);
	    tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[1.1/3]","Validate cross hair not visible in active viewbox. ");
	    for(int j=2;j<=tool.getNumberOfCanvasForLayout();j++)
	    tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[1."+j+"/3]", "Validate cross hair visible in viewbox-"+j);
		}
	  
		//verify cross hair when different annotation selected from radial menu
		circle.selectCircleFromQuickToolbar(1);
		for(int i=1;i<=tool.getNumberOfCanvasForLayout();i++)
			 tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(i, 1), "Checkpoint[2."+i+"/3]","Validate cross hair not visible in  viewbox-"+i);
		
        tool.assertTrue(tool.verifyLocalizerLinePresence(4), "Checkpoint[3/3]", "Verified that localizer line visible in fourth viewbox.");
		
	

	}

	@Test(groups ={"Chrome","IE11","Edge","US1256","Positive"})
	public void test04_US1256_TC7250_verifyUserActionLogForTraingulation() throws SQLException, InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the user action log is generated for the triangulation tool selection.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);
		db = new DatabaseMethodsADB(driver);
		
		tool.waitForViewerpageToLoad(1);
		tool.selectTraingulationFromQuickToolbar(1);
		
		List<String> menuSelection = db.getPayload(ActionLogConstant.USER_ACTION_MENU_SELECTION,ActionLogConstant.USER_ACTION_ITEM_SELECTED);
		db.assertTrue(db.verifyActionLogForTraingulationToolSelection(menuSelection,ViewerPageConstants.TRAINGULATION), "Checkpoint[1/1]", "verifying the user action log for Traingulation tool.");
	

	}

	@Test(groups ={"Chrome","IE11","Edge","US1256","Positive","BVT"})
	public void test05_US1256_TC7257_verifyTraingulationRemainActiveBeforeAfterOneUp() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the triangulation remains active before and after the 1-up");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);
	
		tool.waitForViewerpageToLoad(1);
		tool.selectTraingulationFromQuickToolbar(1);
		tool.clickAt(10,20);
		tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[1.1/4]","Validate cross hair not visible in active viewbox. ");
	    for(int i=2;i<=tool.getNumberOfCanvasForLayout();i++)
	    tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(i, 1),"Checkpoint[1."+i+"/4]", "Validate cross hair visible in viewbox-"+i);
		
		tool.doubleClick(tool.getViewPort(1));
		tool.assertEquals(tool.getNumberOfCanvasForLayout(), 1, "Checkpoint[2/4]", "Verified viewer page loaded in 1*1 layout.");
		
		tool.doubleClick(tool.getViewPort(1));
		tool.waitForAllImagesToLoad();
		tool.assertEquals(tool.getNumberOfCanvasForLayout(), 4, "Checkpoint[3/4]", "Verified that viewer page loaded in default layout .(2*2)");
		
		tool.mouseHover(tool.getViewPort(1));
		for(int i=20;i<=50;i+=10)
		{
	    tool.clickAt(10,i);
	    tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[4.1/4]","Validate cross hair not visible in active viewbox. ");
	    for(int j=2;i<tool.getNumberOfCanvasForLayout();i++)
	    tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[4."+j+"/4]", "Validate cross hair visible in viewbox-"+j);
		}
	
	}

	@Test(groups ={"Chrome","IE11","Edge","US1256","Negative","BVT"})
	public void test06_US1256_TC7270_verifyTraingulationInDifferentLayout() throws InterruptedException  
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the triangulation functionality in a different layout.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);
		tool=new Triangulation(driver);
		tool.waitForViewerpageToLoad(1);

		ViewerLayout layout = new ViewerLayout(driver);
		//select layout other than 2*2
		layout.selectLayout(layout.twoByThreeLayoutIcon);
		tool.selectTraingulationFromQuickToolbar(1);
		
		for(int i=20;i<30;i+=5)
		{
		    tool.clickAt(30,-i);
		    tool.assertFalse(tool.verifyCrossHairWhenTraingulationToolSelected(1, 1), "Checkpoint[1/2]","Validate cross hair not visible in active viewbox after layout change");
		    for(int j=2;j<tool.getNumberOfCanvasForLayout();j++)
		    	tool.assertTrue(tool.verifyCrossHairWhenTraingulationToolSelected(j, 1),"Checkpoint[2."+j+"/2]", "Validate cross hair visible in viewbox-"+j);
		    
		}

	}

	@Test(groups ={"Chrome","IE11","Edge","US1256","Negative","Negative"})
	public void test07_US1256_TC7260_verifykeyboardAndMouseShortcut() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the existing keyboard & mouse shortcuts are not impacted when the triangulation tool is enabled from ALT+Left click option.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(ADC_philips_Patient, 1);


		tool=new Triangulation(driver);
		
		String slice2=tool.getCurrentScrollPosition(2);
		String slice3=tool.getCurrentScrollPosition(3);
		String slice4=tool.getCurrentScrollPosition(4);
		tool.pressAltlLeftWithOffset(tool.getViewPort(1), 10, 50);

        tool.assertNotEquals(tool.getCurrentScrollPosition(2), slice2, "Checkpoint[1/7]", "Verified scroll position after enabling of traingulation tool in second viewbox.");
        tool.assertNotEquals(tool.getCurrentScrollPosition(3), slice3, "Checkpoint[2/7]", "Verified scroll position after enabling of traingulation tool in third viewbox.");
        tool.assertNotEquals(tool.getCurrentScrollPosition(4), slice4, "Checkpoint[3/7]", "Verified scroll position after enabling of traingulation tool in fourth viewbox.");
        
        //phase scroll using shortcut
        String phaseno=tool.getImageCurrentPhasePosition(4).getText();
        tool.mouseHover(tool.getViewPort(4));
        tool.dragAndReleaseOnViewer(tool.getViewPort(4), 10, 30, 40, 30);
        tool.assertNotEquals(tool.getImageCurrentPhasePosition(4).getText(), phaseno, "Checkpoint[4/7]", "Verified that phase scroll.");
		
        //cine using shortcut key
        tool.playOrStopCineUsingKeyboardCKey();
		String position = tool.getCurrentScrollPosition(4);
		tool.waitForTimePeriod(5000);
		// purposely waiting for some time
		tool.playOrStopCineUsingKeyboardCKey();
		tool.assertNotEquals(tool.getCurrentScrollPosition(1),position ,"Checkpoint[5/7]","Verified that cine works fine using shortcut key.");
		
		//WWWL using shortcut key
		String currentWidth = tool.getWindowWidthValueOverlayText(4);
		String currentCenter = tool.getWindowCenterValueOverlayText(4);
		tool.enableOrDisableWWWLUsingKeyboardWKey();		
		tool.dragAndReleaseOnViewerWithoutHop(tool.getViewbox(4),0, 0, 100, 50);		
		tool.assertNotEquals(tool.getWindowWidthValueOverlayText(4),currentWidth ,"Checkpoint[6/7]","WW/WL is working fine using keyboard shortcut.");
		tool.assertNotEquals(tool.getWindowCenterValueOverlayText(4),currentCenter ,"Checkpoint[7/7]","WW/WL is working fine using keyboard shortcut.");

	
	}

	@Test(groups ={"Chrome","IE11","Edge","US1256","Positive"})
	public void test08_US1256_TC7248_verifyPANWhenTraingulationPresent() throws InterruptedException 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that no PAN is applied when the point you triangulate is present on ALL series /orientation");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);


		tool=new Triangulation(driver);
	
		tool.waitForViewerpageToLoad(1);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		tool.takeElementScreenShot(tool.mainViewer, newImagePath+"/actualImages/beforeTraingulationApplied.png");

		//Verify Rotation/flip 
		tool.selectTraingulationFromQuickToolbar(1);
		tool.takeElementScreenShot(tool.mainViewer, newImagePath+"/actualImages/afterTraingulationSelected.png");

		String beforeTraingulationImage = newImagePath+"/actualImages/beforeTraingulationApplied.png";
		String afterTraingulationImage = newImagePath+"/actualImages/afterTraingulationSelected.png";
		String diffImage = newImagePath+"/actualImages/traingulatiomImage.png";

		boolean cpStatusA =  tool.compareimages(beforeTraingulationImage, afterTraingulationImage, diffImage);
		tool.assertTrue(cpStatusA, "Verify that the actual and Expected image are not same","No PAN is applied when traingulate is present on all series.");	
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1256","Negative"})
	public void test09_US1256_TC7249_verifyPANWhenCursonMovedPOutOfTheFocusViewbox() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that image panning does not occur when the cursor is moved out of the focused viewbox or at the border of the active view box.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);
		tool.selectPanFromQuickToolbar(1);
		tool.dragAndReleaseOnViewer(tool.getViewbox(1), 0, 0, -100,-100);
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		tool.takeElementScreenShot(tool.getViewPort(2), newImagePath+"/actualImages/panBeforeTraingulation.png");

		//Verify Rotation/flip 
		tool.selectTraingulationFromQuickToolbar(1);
		tool.clickAt(10, 40);
		tool.dragAndReleaseOnViewer(tool.getViewbox(2), -100,-150, 200, 50);
		tool.takeElementScreenShot(tool.getViewPort(2), newImagePath+"/actualImages/panAfterTraingulation.png");

		String panBeforeTraingulation = newImagePath+"/actualImages/panBeforeTraingulation.png";
		String panAfterTraingulation = newImagePath+"/actualImages/panAfterTraingulation.png";
		String diffImage = newImagePath+"/actualImages/panImage.png";

		boolean cpStatusA =  tool.compareimages(panBeforeTraingulation, panAfterTraingulation, diffImage);
		tool.assertTrue(cpStatusA, "Verify that the actual and Expected image are same","No PAN is applied when cursor is moved out of the focused viewbox.");	
		
	}
	
	@Test(groups ={"Chrome","IE11","Edge","US1256","Negative"})
	public void test10_US1256_TC7264_verifyImageWhenCrossHairNotInFocusedArea() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that the image is rendered to the center when the cross point is not in the focused area.");

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"", "Loading the Patient " + MrLspinePatientName + "in viewer");
		
		patientPage = new PatientListPage(driver);
		helper = new  HelperClass(driver);
		helper.loadViewerDirectly(MrLspinePatientName, 1);

		tool=new Triangulation(driver);	
		tool.selectPanFromQuickToolbar(1);
		tool.dragAndReleaseOnViewer(tool.getViewbox(1), 0, 0, -100,-150);
		tool.waitForAllImagesToLoad();
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		tool.takeElementScreenShot(tool.mainViewer, newImagePath+"/actualImages/BeforeTraingulation.png");

		//Verify Rotation/flip 
		tool.selectTraingulationFromQuickToolbar(1);
		tool.clickAt(0,-190);
		tool.waitForAllImagesToLoad();
		tool.takeElementScreenShot(tool.mainViewer, newImagePath+"/actualImages/AfterTraingulation.png");

		String panBeforeTraingulation = newImagePath+"/actualImages/BeforeTraingulation.png";
		String panAfterTraingulation = newImagePath+"/actualImages/AfterTraingulation.png";
		String diffImage = newImagePath+"/actualImages/panImage.png";

		boolean cpStatusA =  tool.compareimages(panBeforeTraingulation, panAfterTraingulation, diffImage);
		tool.assertFalse(cpStatusA, "Verify that the actual and Expected image are not same","Image is rendered to the centre when cross point not in the focused area.");	
		
	}
	
	
	@AfterMethod(alwaysRun=true)
    public void clearUserActionTable() throws SQLException {

	db = new DatabaseMethodsADB(driver);
	db.truncateTable(nsAnalyticsDBName,ActionLogConstant.USER_ACTION_TABLE);
	
}
}

