package com.trn.ns.test.viewer.PMAP;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.awt.AWTException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.dataProviders.DataProviderArguments;
import com.trn.ns.dataProviders.ExcelDataProvider;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.ContentSelector;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.LoginPage;
import com.trn.ns.page.factory.PMAP;
import com.trn.ns.page.factory.PatientListPage;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewBoxToolPanel;
import com.trn.ns.page.factory.ViewerSliderAndFindingMenu;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PMAPLutBarColorTest extends TestBase{

	private ViewerPage viewerpage;
	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ExtentTest extentTest;
	private ContentSelector cs;
	private HelperClass helper;
	private PMAP pmap;
	private ViewerLayout layout;
	private ViewBoxToolPanel presentMenu;
	private ViewerSliderAndFindingMenu findingMenu;


	String filePath1 = Configurations.TEST_PROPERTIES.get("QIN-PROSTATE-01-0001_filepath");
	String pmapPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath1);

	String filepath2 =Configurations.TEST_PROPERTIES.get("covid_Filepath");
	String covidPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath2);
	String covidPatientID = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filepath2);

	String filepath3 =Configurations.TEST_PROPERTIES.get("Brain_Perfusion_Filepath");
	String brainPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filepath3);
	

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(){

		loginPage = new LoginPage(driver);
		loginPage.navigateToBaseURL();
		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));


	}


	@Test(groups ={"IE11","Chrome","Edge","Positive","US1473","F888"})
	public void test01_US1473_US1045_TC7823_TC7825_TC7827_TC8097_verifyPmapLutAndContextMenuNewDesign() throws  Exception 
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that new LUT template selector is  visible for PMAP patient."
				+ "<br> Verify that user is able to open context menu on clicking LUT bar and can select different available color as well."
				+ "<br> Verify that on selecting color from LUT context menu,  context menu is getting closed"+"<br>Verify that default 'Rainbow1' color  is applied over view box image");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/6]", "Verify Default 'Rainbow1' color is selected.");

		// TC7823 Verify that new LUT template selector is  visible for PMAP patient.

		pmap=new PMAP(driver);
		int originalmiddleValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIDDLE);
		int originalmaxValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MAX);
		int originalminValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIN);


		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is selected", "'Rainbow1' color is selected");
		viewerpage.closeNotification();
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Checkpoint[2/6]'verifying color defaulted loaded is Rainbow1'","test01_01");

		viewerpage.assertTrue(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[3/6]","Verified maximum value, middle and minimum value of LUT bar");

		// TC7825 Verify that user is able to open context menu on clicking LUT bar and can select different available color as well.

		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		Iterator<String> iter1 = ViewerPageConstants.LUT_COLOR.keySet().iterator();

		int i=0;
		while(iter1.hasNext()) {

			String color=ViewerPageConstants.LUT_COLOR.get( iter1.next());

			viewerpage.assertEquals(viewerpage.getText(pmap.lutColorTemplates.get(i)), color, "Checkpoint[4/6]","Verifying that 'Hot iron', 'Rainbow 1' , 'Rainbow 2' , 'Rainbow 3' , 'Blue' , 'Orange' , 'Gray and warm' , 'Gray' 'Reset' colors are present");
			i++;
		}
		pmap.openOrCloseLutBar(1, NSGenericConstants.CLOSE);

		// TC7827 Verify that on selecting color from LUT context menu,  context menu is getting closed.

		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.lutColorContainer),"Checkpoint[5/6]", "Verifying that Lut context menu is not present after selecting color.");
		viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON)), "Verifying '"+ViewerPageConstants.HOTIRON+"' color is selected'", "'"+ViewerPageConstants.HOTIRON+"' color is selected");
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1), NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[6/6]","Verifying the background image of LUT bar.");
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1473","F888"})
	public void test02_US1473_US1045_TC7955_TC7956_TC7828_TC7829_TC8101_verifyPmapLutIntensityAndBrowserResize() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Pmap LUT selector color is not getting changed to default color on OneUp and OndDown."
				+ "<br> Verify that Pmap LUT selector is getting closed on browser resizing."
				+ "<br> Verify that Window level is changing the intensity of LUT template selector."+ "<br> Verify that reset button is working on LUT template selector"+"<br>Verify that reset button is changing the intensity to default intensity on View box.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();	

		pmap=new PMAP(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/7]", "Verify Default 'Rainbow1' color is selected.");

		//TC7956 Performing one up and one down and verifying color is not getting changed.
        layout=new ViewerLayout(driver);
        layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.closeNotification();
		pmap.selectColorFromLUT(1, ViewerPageConstants.GRAY);
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAY)), "Checkpoint[2/7]", "Verifying'"+ViewerPageConstants.GRAY+"' color is selected after one up");
		viewerpage.doubleClick(viewerpage.getViewPort(1));
		viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAY)), "Checkpoint[3/7]", "Verifying'"+ViewerPageConstants.GRAY+"' color is selected after one down");

		//TC7955 Resizing browser and verifying that LUT context menu gets closed on resizing.

		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.resizeBrowserWindow(700,700);
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.lutColorContainer),"Checkpoint[4/7]", "Verifying that Lut context menu is getting closed after browser resizing.");
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.maximizeWindow();
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.lutColorContainer),"Checkpoint[5/7]", "Verifying that Lut context menu is getting closed after browser resizing.");


		//TC7828 Performing window level and changing intensity comparing 

		int originalmiddleValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIDDLE);
		int originalmaxValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MAX);
		int originalminValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIN);

		pmap.selectColorFromLUT(1, ViewerPageConstants.RAINBOW2);
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),50, 50, 100 , 100);
		viewerpage.assertFalse(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[6/7]","Verified maximum value of LUT bar with DB");

		// TC7829 Selecting reset option and comparing intensity changed to default set intensity on pressing RESET button.

		pmap.selectColorFromLUT(1, ViewerPageConstants.RESETLUT);
		viewerpage.assertTrue(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[7/7]", "Verified maximum value of LUT bar");
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1473","F888"})
	public void test03_US1473_TC7982_TC7846_TC7837_TC8039_verifyPmapLutContextMenu() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that LUT selector is getting closed on opening Layout selector."
				+ "<br> Verify that LUT template's context menu option getting highlighted on mouser hovering."
				+ "<br> Verify that Context menu of LUT bar is getting adjusted in all type layout and on browser resizing."+ "<br> Verify that color context menu is getting opened in that view box only on which right or left click has been performed to open LUT bar.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
		cs = new ContentSelector (driver);

		//TC7982 Verify that LUT selector is getting closed on opening Layout selector.
		viewerpage.closeNotification();
		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.click(layout.gridIcon);
		viewerpage.assertFalse(viewerpage.isElementPresent(pmap.lutColorContainer),"Checkpoint[1/6]", "Verifying that Lut context menu is not present on clicking on terarecon logo.");
		viewerpage.click(viewerpage.getViewPort(1));

		//TC7846 hovering mouse on color option and verifying color background color change on hovering.

		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		for(int i=0; i<pmap.lutColorTemplates.size();i++)
		{
			viewerpage.mouseHover(pmap.lutColorTemplatesRow.get(i));
			viewerpage.assertEquals(viewerpage.getCssValue(pmap.lutColorTemplatesRow.get(i), NSGenericConstants.BACKGROUND_COLOR), ViewerPageConstants.LUT_HIGHLIGHTEDORSELECTED_COLOR,"[Checkpoint[2.("+(i+1)+")/6]] Verify Background color on mouse hovering on color option", "Color is getting changed on mouse hovering.");
		}

		//TC7837 by reducing size of browser window taking width of view box and LUT context menu, and comparing.
		viewerpage.closeNotification();
		viewerpage.resizeBrowserWindow(400,400);

		int viewBoxWidth=viewerpage.getWidthOfWebElement(viewerpage.getViewPort(1));

		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);

		int lutContainerWidth=viewerpage.getWidthOfWebElement(pmap.lutColorContainer);
		int lutContainerXCordinate= viewerpage.getValueOfXCoordinate(pmap.lutColorContainer);
		int totalWidth=(lutContainerWidth+lutContainerXCordinate);
		viewerpage.assertTrue(totalWidth<viewBoxWidth, "Checkpoint[3/6]", "Width of context menu is inside the view box.");
		viewerpage.maximizeWindow();

		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewBoxWidth=viewerpage.getWidthOfWebElement(viewerpage.getViewPort(1));
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		lutContainerWidth=viewerpage.getWidthOfWebElement(pmap.lutColorContainer);
		lutContainerXCordinate= viewerpage.getValueOfXCoordinate(pmap.lutColorContainer);
		totalWidth=(lutContainerWidth+lutContainerXCordinate);
		viewerpage.assertTrue(totalWidth<viewBoxWidth, "Checkpoint[4/6]", "Width of context menu is inside the view box in 3X3 layout.");


		// TC8039 Opening LUT context menu in 1st and 2nd view box one by one and image comparing.

		viewerpage.compareElementImage(protocolName, viewerpage.mainViewer,"Checkpoint[5/6]'Verifying that LUT context menu is getting opened in 1st view box'","test03_01");


		pmap.openOrCloseLutBar(1, NSGenericConstants.CLOSE);

		List<String> results = cs.getAllResults();
		cs.selectResultFromSeriesTab(2, results.get(0));
		pmap.openOrCloseLutBar(2, NSGenericConstants.OPEN);
		viewerpage.compareElementImage(protocolName,viewerpage.mainViewer,"Checkpoint[6/6]'Verifying that LUT context menu is getting opened in 2nd view box'","test03_02");

	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1473","F888"})
	public void test04_US1473_TC7826_verifyPmapColorOnViewBox() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that color of LUT selector template getting changed on selecting color from options.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();

		viewerpage.closeNotification();

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		
		Iterator<String> iter1 = ViewerPageConstants.LUT_COLOR.keySet().iterator();
		Iterator<String> iter2=ViewerPageConstants.LUT_COLOR_CODE.keySet().iterator();

		int i=0;
		while(iter1.hasNext()&&iter2.hasNext()) {

			String color=ViewerPageConstants.LUT_COLOR.get( iter1.next());
			String colorRGB=ViewerPageConstants.LUT_COLOR_CODE.get( iter2.next());


			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/defaultLoadedImage_"+i+".png");

			//Selecting the Color option from Lut context menu
            pmap=new PMAP(driver);
			pmap.selectColorFromLUT(1, color);
			viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,color)), "Checkpoint[1/3] 'Verifying '"+color+"' color is selected", "'"+color+"' color is selected");
			viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(colorRGB),"Checkpoint[2/3]","Verifying the background image of LUT bar.");

			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterColorSelection_"+i+".png");

			String defaultLoadedImage = newImagePath+"/actualImages/defaultLoadedImage_"+i+".png";
			String afterColorSelection = newImagePath+"/actualImages/afterColorSelection_"+i+".png";
			String diffImagePath = newImagePath+"/actualImages/diff.png";

			boolean cpStatus =  viewerpage.compareimages(defaultLoadedImage, afterColorSelection, diffImagePath);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying selected color getting applied on view box" );
			viewerpage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that "+color+" and next selected color images are not same");
			i++;

		}

	}

	//US1621: Allow display/selection of embedded LUT from LUT context menu
	@Test(groups ={"IE11","Chrome","Edge","Positive","US1621","F631","E2E","F888"})
	public void test05_US1621_TC8644_TC8645_verifyDisplayOfBuiltInLUTBar() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the display/selection of the built in  LUT from LUT context menu.<br>"+
				"Verify that user is able to select any other LUT from the context menu");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();

		ContentSelector cs=new ContentSelector(driver);
		String resultName=cs.getAllResults().get(0);

		pmap=new PMAP(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading "+covidPatientName+" on viewer." );
		viewerpage.closeNotification();
		viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,resultName)), "Checkpoint[1/5] 'Verifying '"+resultName+"' color is selected", "'"+resultName+"' color is selected");
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Checkpoint[2/5]'verifying color defaulted loaded is "+resultName,"test05_02_Build_In_LUT_Bar");	
		pmap.openOrCloseLutBar(1, NSGenericConstants.CLOSE);

		Iterator<String> iter1 = ViewerPageConstants.LUT_COLOR.keySet().iterator();
		Iterator<String> iter2=ViewerPageConstants.LUT_COLOR_CODE.keySet().iterator();
		int i=0;
		while(iter1.hasNext()&&iter2.hasNext()) {

			String color=ViewerPageConstants.LUT_COLOR.get( iter1.next());
			String colorRGB=ViewerPageConstants.LUT_COLOR_CODE.get( iter2.next());

			String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/defaultLoadedImage_"+i+".png");

			//Selecting the Color option from Lut context menu

			pmap.selectColorFromLUT(1, color);
			viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,color)), "Checkpoint[3/5] 'Verifying '"+color+"' color is selected", "'"+color+"' color is selected");
			viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(colorRGB),"Checkpoint[4/5]","Verifying the background image of LUT bar.");

			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterColorSelection_"+i+".png");

			String defaultLoadedImage = newImagePath+"/actualImages/defaultLoadedImage_"+i+".png";
			String afterColorSelection = newImagePath+"/actualImages/afterColorSelection_"+i+".png";
			String diffImagePath = newImagePath+"/actualImages/diff.png";

			boolean cpStatus =  viewerpage.compareimages(defaultLoadedImage, afterColorSelection, diffImagePath);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying selected color getting applied on view box" );
			viewerpage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that "+color+" and next selected color images are not same");
			i++;

		}

	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1621","E2E","F631"})
	public void test06_US1621_TC8647_verifyResetButtonAfterChangOfWWWC() throws SQLException, InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Reset Button in context menu after changing window level/width");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectly(covidPatientName,1);
		viewerpage.closeNotification();	

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading "+covidPatientName+" on viewer." );
		ContentSelector cs=new ContentSelector(driver);
		String resultName=cs.getAllResults().get(0);

		viewerpage.closeNotification();
		db=new DatabaseMethods(driver);
		pmap=new PMAP(driver);

		int originalminValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIN);
		int originalmiddleValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIDDLE);
		int originalmaxValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MAX);

		String dbMaxValue = db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MAX_COLUMN_NAME,covidPatientName,"",resultName);
		String dbMinValue = db.getMaxOrMinValueOfLUTBar(NSDBDatabaseConstants.PMAP_DB_MIN_COLUMN_NAME,covidPatientName,"",resultName);
		float maxValue =viewerpage.convertIntoFloat(dbMaxValue);
		float minValue =viewerpage.convertIntoFloat(dbMinValue);

		viewerpage.assertEquals(originalmaxValue, viewerpage.convertIntoInt(dbMaxValue), "Checkpoint[1/5]","Verified maximum value of LUT bar with DB");
		viewerpage.assertEquals(originalminValue, viewerpage.convertIntoInt(dbMinValue) , "Checkpoint[2/5]","Verified minimum value of LUT bar with DB");

		viewerpage.assertEquals(originalmiddleValue,Math.round((maxValue+minValue)/2), "Checkpoint[3/5]","Verified middle value of LUT bar with DB");

		pmap.selectColorFromLUT(1,ViewerPageConstants.RAINBOW2);
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),100, 100, 200 , 150);
		viewerpage.assertFalse(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[4/5]","Verified max,min and middle value of LUT bar on viewer are change after applying WWWC.");

		pmap.selectColorFromLUT(1, ViewerPageConstants.RESETLUT);
		viewerpage.assertTrue(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[5/5]", "Verified max,min and middle value of LUT bar on viewer are reset after click on reset button.");

	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1045","F494"})
	public void test07_US1045_TC8103_TC8098_TC8102_verifyPmapOnReloadAndLayoutChange() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that on reloading from study page color changes are not getting retained"+"<br>Verify that selected color by user from color context menu is getting applied over view box image."
				+"<br>Verify that selected color from color context menu is getting persisted on layout change for that view box.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);

		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/15]", "Verify on revisiting color not getting persisted.");

		int originalmiddleValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIDDLE);
		int originalmaxValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MAX);
		int originalminValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIN);

		// TC7823, TC8102 Selecting Hotiron from LUT and revisiting viewer page.
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		helper=new HelperClass(driver);
		helper.browserBackAndReloadViewer(pmapPatientID, 1, 1);

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is selected", "Checkpoint[2/15]'Rainbow1' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.RAINBOW1)),"Checkpoint[3/15]","Verifying the background image of LUT bar.");
		viewerpage.closeNotification();
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[4/15]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[5/15]","Verifying the background image of LUT bar.");
		helper.browserBackAndReloadViewer(pmapPatientID, 1, 1);

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is selected", "Checkpoint[6/15]'Rainbow1' color is selected");

		viewerpage.closeNotification();
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[7/15]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[8/15]","Verifying the background image of LUT bar.");

		pmap.selectColorFromLUT(1, ViewerPageConstants.BLUE);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.BLUE), "Verifying 'Blue' color is selected", "Checkpoint[9/15]'Blue' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.BLUE)),"Checkpoint[10/15]","Verifying the background image of LUT bar is Blue.");

		layout.selectLayout(layout.oneByOneLayoutIcon);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.BLUE), "Verifying 'Blue' color is selected", "Checkpoint[11/15]'Blue' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.BLUE)),"Checkpoint[12/15]","Verifying the background image of LUT bar is Blue.");

		// TC8098 
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[13/15]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[14/15]","Verifying the background image of LUT bar.");
		viewerpage.assertTrue(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[15/15]","Verified maximum value, middle and minimum value of LUT bar");
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1045","F494"})
	public void test08_US1045_TC8099_TC8100_verifyPmapOnOneUpAndOnIntensityChange() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that view box image color is not getting changed to default 'Rainbow 1' color on OneUp and OndDown."+"<br>Verify that Window level is changing the intensity of LUT template selector and on View box image as well.");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);

		int originalmiddleValue =pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIDDLE);
		int originalmaxValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MAX);
		int originalminValue = pmap.getMaxMinAndMiddleValueFromLUTBar(1, ViewerPageConstants.MIN);

		// TC8099 Selecting Gray from LUT and revisiting viewer page and checking color by oneup and onedown.


		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is selected", "Checkpoint[1/10]'Rainbow1' color is selected");
		viewerpage.assertTrue(pmap.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.RAINBOW1)),"Checkpoint[2/10]","Verifying the background image of LUT bar is Rainbow1.");
		viewerpage.closeNotification();
		layout.selectLayout(layout.threeByThreeLayoutIcon);
		pmap.selectColorFromLUT(1, ViewerPageConstants.GRAY);

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAY), "Verifying 'GRAY' color is selected", "Checkpoint[3/10]'GRAY' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAY)),"Checkpoint[4/10]","Verifying the background image of LUT bar is GRAY.");

		viewerpage.doubleClick(viewerpage.getViewPort(1));

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAY), "Verifying 'GRAY' color is selected", "Checkpoint[5/10]'GRAY' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAY)),"Checkpoint[6/10]","Verifying the background image of LUT bar is GRAY.");

		viewerpage.doubleClick(viewerpage.getViewPort(1));

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAY), "Verifying 'GRAY' color is selected", "Checkpoint[7/10]'GRAY' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAY)),"Checkpoint[8/10]","Verifying the background image of LUT bar is GRAY.");

		layout.selectLayout(layout.oneByOneLayoutIcon);


		// Selecting color Hot iron changing intensity and verifying intensity on LUT and viewbox image.
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.selectWindowLevelFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1),100, 100, 200 , 150);
		viewerpage.assertFalse(pmap.verifyMaxMinValueForLutBar(1, originalmaxValue, originalmiddleValue, originalminValue), "Checkpoint[9/10]","Verified maximum value of LUT bar with DB");
		viewerpage.compareElementImage(protocolName, viewerpage.getViewPort(1),"Checkpoint[10/10]'verifying color  Hotiron intensity getting changed on viewbox image'","test08_01");


	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","US1045","F494"})
	public void test09_US1045_TC8172_TC8167_verifyPmapForSyncAndNonSyncViewBox() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that Pmap result series with already applied color  is not getting loaded if view boxes are not in sync."+"<br>Verify that Pmap result series with already applied color is getting loaded if view boxes are in sync..");

		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();
		cs = new ContentSelector (driver);

		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		// TC8167 Changing layout to 2X1 and loading Default Pmap from content selector in 2nd view box.

		layout.selectLayout(layout.twoByOneLayoutIcon);
		List<String> results = cs.getAllResults();
		cs.selectResultFromSeriesTab(2, results.get(0));

		//Loading HotIron in 1st view box and verifying Hotiron is getting applied in both the view box.

		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[1/24]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[2/24]","Verifying the background image of LUT bar.");

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(2,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is getting changed", "Checkpoint[3/24]'Hot iron' color is changed in 2nd view box also due to sync behavior.");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(2),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[4/24]","Verifying the background image of LUT bar.");

		// Changing layout to 3X3 and Load series from content selector in 3rd view box and verifying which series is loaded.
		layout.selectLayout(layout.threeByThreeLayoutIcon);

		cs.selectResultFromSeriesTab(3, results.get(0));
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(3,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[5/24]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(3),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[6/24]","Verifying the background image of LUT bar.");

		pmap.selectColorFromLUT(3, ViewerPageConstants.GRAYWARM);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(3,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is selected", "Checkpoint[7/24]'GrayWarm' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(3),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[8/24]","Verifying the background image of LUT bar is GrayWarm.");

		cs.selectResultFromSeriesTab(4, results.get(0));
		cs.selectResultFromSeriesTab(5, results.get(0));

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(4,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is selected", "Checkpoint[9/24] 'GrayWarm' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(4),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[10/24]","Verifying the background image of LUT bar is GrayWarm.");

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(5,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is selected", "Checkpoint[11/24]'GrayWarm' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(5),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[12/24]","Verifying the background image of LUT bar is GrayWarm.");

		//TC8172 Changing layout to 2X1 and loading Default Pmap from content selector when view boxes are not in sync 
		helper.browserBackAndReloadViewer(pmapPatientID, 1, 1);

		layout.selectLayout(layout.twoByOneLayoutIcon);
		viewerpage.click(viewerpage.getViewPort(1));
		viewerpage.performSyncONorOFF();

		cs.selectResultFromSeriesTab(2, results.get(0));

		//Loading HotIron in 1st view box and verifying Hotiron is getting applied in both the view box.

		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Verifying 'Hot iron' color is selected", "Checkpoint[13/24]'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[14/24]","Verifying the background image of LUT bar is Hot iron.");

		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(2,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is getting changed", "Checkpoint[15/24] 'Rainbow1' color is changed in 2nd view box also due to sync behavior.");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(2),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.RAINBOW1)),"Checkpoint[16/24]","Verifying the background image of LUT bar is Rainbow1.");

		// Changing layout to 3X3 and Load series from content selector in 3rd view box and verifying which series is loaded.

		layout.selectLayout(layout.threeByThreeLayoutIcon);

		cs.selectResultFromSeriesTab(3, results.get(0));
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(3,ViewerPageConstants.RAINBOW1), "Verifying 'Rainbow1' color is selected", "Checkpoint[17/24]'Rainbow1' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(3),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.RAINBOW1)),"Checkpoint[18/24]","Verifying the background image of LUT bar is Rainbow1.");

		pmap.selectColorFromLUT(3, ViewerPageConstants.GRAYWARM);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(3,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is selected", "Checkpoint[19/24]'GrayWarm' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(3),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[20/24]","Verifying the background image of LUT bar is GrayWarm.");

		viewerpage.assertFalse(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is not selected", "Checkpoint[21/24]'GrayWarm' color is not selected");
		viewerpage.assertFalse(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[22/24]","Verifying the background image of LUT bar is not GrayWarm.");

		viewerpage.assertFalse(pmap.verifyColorSelectedOnPmap(2,ViewerPageConstants.GRAYWARM), "Verifying 'GrayWarm' color is not selected", "Checkpoint[23/24]'GrayWarm' color is not selected");
		viewerpage.assertFalse(viewerpage.getCssValue(pmap.getGradientColor(2),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.GRAYWARM)),"Checkpoint[24/24]","Verifying the background image of LUT bar is not GrayWarm.");



	}

	//DR2193: PMAP LUT bar not visible after selecting color  from LUT template selector.
	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2193","F494"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test10_Pmap_Data"})
	public void test10_DR2193_TC8712_verifyPmapColorOnViewBoxAfterLayoutChange(String filepath) throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP LUT bar is visible after color selection from LUT template selector.");

		String patientID=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get(filepath));
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(pmapPatientID,1);
		viewerpage.closeNotification();

		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);
		layout.selectLayout(layout.twoByOneLayoutIcon);

		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;

		Iterator<String> iter1 = ViewerPageConstants.LUT_COLOR.keySet().iterator();
		Iterator<String> iter2=ViewerPageConstants.LUT_COLOR_CODE.keySet().iterator();

		int i=0;
		while(iter1.hasNext()&&iter2.hasNext()) {

			String color=ViewerPageConstants.LUT_COLOR.get( iter1.next());
			String colorRGB=ViewerPageConstants.LUT_COLOR_CODE.get( iter2.next());


			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/defaultLoadedImage_"+i+".png");

			//Selecting the Color option from Lut context menu

			pmap.selectColorFromLUT(1, color);
			viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(1,color)), "Checkpoint[1/3] 'Verifying '"+color+"' color is selected", "'"+color+"' color is selected");
			viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(colorRGB),"Checkpoint[2/3]","Verifying the background image of LUT bar.");

			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/actualImages/afterColorSelection_"+i+".png");

			String defaultLoadedImage = newImagePath+"/actualImages/defaultLoadedImage_"+i+".png";
			String afterColorSelection = newImagePath+"/actualImages/afterColorSelection_"+i+".png";
			String diffImagePath = newImagePath+"/actualImages/diff.png";

			boolean cpStatus =  viewerpage.compareimages(defaultLoadedImage, afterColorSelection, diffImagePath);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying selected color getting applied on view box" );
			viewerpage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that "+color+" and next selected color images are not same");
			i++;

		}

	}
	
	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2193","F494"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test10_Pmap_Data"})
	public void test11_DR2193_TC8713_verifySynchronizationForPMAPColorCode(String filepath) throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP LUT bar is visible after color selection from LUT template selector.");

		String patientID=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get(filepath));
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(patientID,1);
		viewerpage.closeNotification();
		
		layout=new ViewerLayout(driver);
		pmap=new PMAP(driver);

		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);
		
		String resultName=cs.getAllResults().get(0);
		String seriesName=cs.getAllSeries().get(0);
		
		cs.selectResultFromSeriesTab(3, resultName);
		cs.selectResultFromSeriesTab(5, resultName);
		
		for(int j=4;j<=cs.getNumberOfCanvasForLayout();j++)
		{
			if(j%2==0)
			cs.selectSeriesFromSeriesTab(j, seriesName);
			
		}
		

		Iterator<String> iter1 = ViewerPageConstants.LUT_COLOR.keySet().iterator();
		Iterator<String> iter2=ViewerPageConstants.LUT_COLOR_CODE.keySet().iterator();
	
		int i=0;
		while(iter1.hasNext()&&iter2.hasNext()) {

			String color=ViewerPageConstants.LUT_COLOR.get( iter1.next());
			String colorRGB=ViewerPageConstants.LUT_COLOR_CODE.get( iter2.next());

			String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
			viewerpage.takeElementScreenShot(viewerpage.getViewbox(1), newImagePath+"/goldImages/defaultLoadedResultImage.png");
			viewerpage.takeElementScreenShot(viewerpage.getViewbox(2), newImagePath+"/goldImages/defaultLoadedSeriesImage.png");

			//Selecting the Color option from Lut context menu
			pmap.selectColorFromLUT(1, color);
			for(int j=1;j<=cs.getNumberOfCanvasForLayout();j++)
			{
				if(!(j%2==0))
				{
				
			viewerpage.assertTrue((pmap.verifyColorSelectedOnPmap(j,color)), "Checkpoint[1/5] 'Verifying '"+color+"' color is selected", "'"+color+"' color is selected on viewbox-"+j);
			viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(j),NSGenericConstants.BACKGROUND_IMAGE).contains(colorRGB),"Checkpoint[2/5]","Verifying the background image of LUT bar on viewbox-"+j);

		   viewerpage.takeElementScreenShot(viewerpage.getViewbox(j), newImagePath+"/actualImages/afterColorSelectionForResult_"+j+".png");

			String defaultLoadedResultImage = newImagePath+"/goldImages/defaultLoadedResultImage.png";
			String afterColorSelectionResult = newImagePath+"/actualImages/afterColorSelectionForResult_"+j+".png";
			String diffImagePath = newImagePath+"/diffImages/diffResult.png";

			boolean cpStatus =  viewerpage.compareimages(defaultLoadedResultImage, afterColorSelectionResult, diffImagePath);
			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verifying selected color getting applied on view box-"+j );
			viewerpage.assertFalse(cpStatus,"The actual and Expected image are not same." ,"Verify that "+color+" and next selected color images are not same on viewbox-"+j);
				}
				
			else {
			
				viewerpage.assertFalse(viewerpage.isElementPresent(pmap.getGradientColor(j)),"Checkpoint[4/5]","Verifying that gradient LUT bar not visible on viewbox-"+j);

				viewerpage.takeElementScreenShot(viewerpage.getViewbox(j), newImagePath+"/actualImages/afterColorSelectionForSeries_"+j+".png");

				String expectedImagePath = newImagePath+"/goldImages/defaultLoadedSeriesImage.png";
				String actualImagePath = newImagePath+"/actualImages/afterColorSelectionForSeries_"+j+".png";
				String diffImagePath = newImagePath+"/diffImages/diffSeries.png";

				boolean cpStatus1 =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verifying selected color not getting applied on view box in which series is loaded" );
				viewerpage.assertTrue(cpStatus1,"The actual and Expected image are  same." ,"Verify that no color code in applied on viewbox-"+j);
				
			}
			
			
				i++;

			} 
		
		}	
	
	}

	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2193","F494"}, dataProvider="getDataFromExcelFile",dataProviderClass=ExcelDataProvider.class)
	@DataProviderArguments({"filePath=src/test/resources/covidData.xls", "sheetName=test10_Pmap_Data"})
	public void test12_DR2193_TC8713_verifyZoomPanWWWCForPMAP(String filePath) throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify synchronization works on view boxes loaded with PMAP when a combination of PMAP and non-PMAP series are loaded on viewer.");

		String patientID=DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, Configurations.TEST_PROPERTIES.get(filePath));
		
		helper=new HelperClass(driver);
		viewerpage =helper.loadViewerDirectlyUsingID(patientID,1);
		viewerpage.closeNotification();

		pmap=new PMAP(driver);
		layout=new ViewerLayout(driver);
		presentMenu=new ViewBoxToolPanel(driver);
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading patient "+patientID+" on viewer." );
		layout.selectLayout(layout.threeByTwoLayoutIcon);
		viewerpage.closeNotification();
		cs=new ContentSelector(driver);
		
		String resultName=cs.getAllResults().get(0);
		String seriesName=cs.getAllSeries().get(0);
		
		//load result in 1,3 and 5th viewbox.
		cs.selectResultFromSeriesTab(3, resultName);
		cs.selectResultFromSeriesTab(5, resultName);
		
		//load series in 2,4 and 6th viewbox.
		for(int j=4;j<=cs.getNumberOfCanvasForLayout();j++)
		{
			if(j%2==0)
			cs.selectSeriesFromSeriesTab(j, seriesName);
			
		}
		
		presentMenu=new ViewBoxToolPanel(driver);
		String zoomValue=presentMenu.getZoomLevelValue(1);
		String windowWidth=viewerpage.getWindowWidthValueOverlayText(1);
		String windowCentre=viewerpage.getWindowCenterValueOverlayText(1);
		String imageNumber=viewerpage.getCurrentScrollPosition(1);
		
		//verify zoom 
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/4]", "Verify zoom synchronization for PMAP Source and result series." );
		viewerpage.selectZoomFromQuickToolbar(viewerpage.getViewPort(1));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewPort(1), 0,0,50,50);
	    for(int i=1;i<=cs.getNumberOfCanvasForLayout();i++)
		viewerpage.assertNotEquals(presentMenu.getZoomLevelValue(i), zoomValue,"Verify zoom sync for viewbox-"+i,"Verified");
		
		//verify WWWC 
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/4]", "Verify WW and WC synchronization for PMAP Source and result series." );
		viewerpage.click(viewerpage.getViewPort(3));
		
		  if(patientID.equalsIgnoreCase(pmapPatientID))
			  presentMenu.selectPresetValue(3, ViewerPageConstants.T1);
		else
			presentMenu.selectPresetValue(3, ViewerPageConstants.HEAD);
		
	    for(int i=1;i<=cs.getNumberOfCanvasForLayout();i++)
		{
		viewerpage.assertNotEquals(viewerpage.getWindowWidthValueOverlayText(i), windowWidth,"Verify WW for viewbox-"+i,"Verified");
		viewerpage.assertNotEquals(viewerpage.getWindowCenterValueOverlayText(i), windowCentre,"Verify WC for viewbox-"+i,"Verified");
		}
		
		//verify scroll
	    ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/4]", "Verify scroll synchronization for PMAP Source and result series.");
		viewerpage.selectScrollFromQuickToolbar(viewerpage.getViewPort(5));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(5), 0, 0, 0, 20);
		for(int i=1;i<=cs.getNumberOfCanvasForLayout();i++)
		viewerpage.assertNotEquals(viewerpage.getCurrentScrollPosition(i), imageNumber,"Verify scroll sync for viewbox-"+i,"Verified");
	
		//verify PAN sync
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/4]", "Verify PAMP synchronization for PMAP Source and result series.");
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		viewerpage.takeElementScreenShot(viewerpage.mainViewer, newImagePath+"/goldImages/test12_ImageWithoutPAN.png");
		viewerpage.selectPanFromQuickToolbar(viewerpage.getViewPort(5));
		viewerpage.dragAndReleaseOnViewer(viewerpage.getViewbox(5), 0, 0, -50,-50);
	
		viewerpage.takeElementScreenShot(viewerpage.mainViewer, newImagePath+"/actualImages/test12_ImageWithPAN.png");

		String expectedImagePath = newImagePath+"/goldImages/test12_ImageWithoutPAN.png";
		String actualImagePath = newImagePath+"/actualImages/test12_ImageWithPAN.png";
		String diffImagePath = newImagePath+"/diffImages/diff.png";

		boolean cpStatus1 =  viewerpage.compareimages(expectedImagePath, actualImagePath, diffImagePath);
		viewerpage.assertFalse(cpStatus1,"The actual and Expected image are not same." ,"Verify that PAN applied successfuly on all viewboxes.");
	
		
	}
	
	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2350","F141"})
	public void test13_DR2350_TC9302_verifyLUTbarOnBrainPerfusion() throws InterruptedException, SQLException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP is displayed only on the series whose SOPInstanceUID  is mapped with PMAP's SOPInstanceUID.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage =  helper.loadViewerDirectly(brainPatientName, 1);
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/";
		String actualImagePath = newImagePath+"/actualImages/";
		String imageName="viewport_1.png";
			
		pmap=new PMAP(driver);
		viewerpage.assertTrue(pmap.verifyPresenceOfGradientBar(1), "Checkpoint[1/7]", "Verifying that LUT bar is displayed");
		viewerpage.assertFalse(pmap.verifyPresenceOfGradientBar(2), "Checkpoint[2/7]", "Verifying that LUT bar is not displayed on source");
		viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), expectedImagePath+imageName);
		
		pmap.selectColorFromLUT(1, ViewerPageConstants.HOTIRON);
		viewerpage.assertTrue(pmap.verifyColorSelectedOnPmap(1,ViewerPageConstants.HOTIRON), "Checkpoint[3/7]","Verifying 'Hot iron' color is selected");
		viewerpage.assertTrue(viewerpage.getCssValue(pmap.getGradientColor(1),NSGenericConstants.BACKGROUND_IMAGE).contains(ViewerPageConstants.LUT_COLOR_CODE.get(ViewerPageConstants.HOTIRON)),"Checkpoint[4/7]","Verifying the background image of LUT bar.");
		viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), actualImagePath+imageName);
		
		boolean cpStatus =  viewerpage.compareimages(expectedImagePath+imageName, actualImagePath+imageName, actualImagePath+imageName);
		viewerpage.assertFalse(cpStatus, "Checkpoint[5/7] ","The actual and Expected image should not be same after applying color ");
		
		db = new DatabaseMethods(driver);
		db.assertEquals(db.getPMAPInstance(viewerpage.getSeriesDescriptionOverlayText(1)).size(),db.getInstanceLevelID(viewerpage.getSeriesDescriptionOverlayText(1)).size(),"Checkpoint[6/7]","verifying the series whose SOPInstanceUID  is mapped with PMAP's SOPInstanceUID.");
		db.assertTrue(db.getPMAPInstance(viewerpage.getSeriesDescriptionOverlayText(2)).isEmpty(),"Checkpoint[7/7]","verifying there wont be instance, series whose SOPInstanceUID  is not mapped with PMAP's SOPInstanceUID.");

	}
	
	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2350","F141"})
	public void test14_DR2350_TC9317_verifySourceAndPMAPOverlay() throws InterruptedException, AWTException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify PMAP overlay is displayed on correct image.");

		patientPage = new PatientListPage(driver);
		helper = new HelperClass(driver);
		viewerpage =  helper.loadViewerDirectly(brainPatientName, 1);
		viewerpage.closeNotification();
		cs = new ContentSelector(driver);
		cs.selectSeriesFromSeriesTab(2, cs.getSeriesDescriptionOverlayText(1));
		
		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
		String expectedImagePath = newImagePath+"/goldImages/";
		String actualImagePath = newImagePath+"/actualImages/";
		
		pmap=new PMAP(driver);
			
		viewerpage.assertTrue(pmap.verifyPresenceOfGradientBar(1), "Checkpoint[1/7]", "Verifying that LUT bar is displayed");
		viewerpage.assertFalse(pmap.verifyPresenceOfGradientBar(2), "Checkpoint[2/7]", "Verifying that LUT bar is not displayed");
		
		
		for(int i =viewerpage.getCurrentScrollPositionOfViewbox(1);i<viewerpage.getMaxNumberofScrollForViewbox(1);i++) {
			
			String imageNamePMAP="viewport_1_PMAP_"+viewerpage.getCurrentScrollPositionOfViewbox(1)+".png";
			String imageName1="viewport_1_"+viewerpage.getCurrentScrollPositionOfViewbox(1)+".png";
			String imageName2="viewport_2_"+viewerpage.getCurrentScrollPositionOfViewbox(2)+".png";
			
			viewerpage.assertEquals(viewerpage.getCurrentScrollPositionOfViewbox(1), viewerpage.getCurrentScrollPositionOfViewbox(2), "Checkpoint[3/7]", "verifying the slice number is same");
			viewerpage.assertEquals(viewerpage.getMaxNumberofScrollForViewbox(1), viewerpage.getMaxNumberofScrollForViewbox(2), "Checkpoint[4/7]", "Verifying total images are same");
			viewerpage.assertEquals(viewerpage.getValueOfCurrentPhase(1), viewerpage.getValueOfCurrentPhase(2), "Checkpoint[5/7]", "verifying the phase is also same");
			viewerpage.assertEquals(viewerpage.getImageNumberLabelValue(1), viewerpage.getImageNumberLabelValue(2), "Checkpoint[6/7]", "verifying image number is also same");

			viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), expectedImagePath+imageNamePMAP,new int[] {100,50,500,490});
			viewerpage.mouseHover(viewerpage.getViewPort(1));
			viewerpage.click(viewerpage.resultApplied(1));
			viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), expectedImagePath+imageName1,new int[] {100,50,500,490});
			viewerpage.takeElementScreenShot(viewerpage.getViewPort(1), actualImagePath+imageName2,new int[] {100,50,500,490});
			
			
			boolean cpStatus =  viewerpage.compareimages(expectedImagePath+imageName1, actualImagePath+imageName2, actualImagePath+"diff_"+imageName2);
			viewerpage.assertTrue(cpStatus, "Checkpoint[7.1/7] ","The actual and Expected image should be same as where PMAP applied and source series - image is in sync");

			cpStatus =  viewerpage.compareimages(expectedImagePath+imageNamePMAP, actualImagePath+imageName2, actualImagePath+"diff_"+imageName2);
			viewerpage.assertFalse(cpStatus, "Checkpoint[7.2/7] ","verifying that pmap is also applied");

			viewerpage.click(viewerpage.resultApplied(1));
			viewerpage.scrollDownToSliceUsingKeyboard(1);	
		}	
	}
	
	//DR2429:Vertical and horizontal scroll bar are visible in LUT template selector without minimizing the browser size.
	
	@Test(groups ={"IE11","Chrome","Edge","Positive","DR2429","F494"})
	public void test15_DR2429_TC9617_verifyScrollbarForMinimizeBrowser() throws InterruptedException
	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify that no scroll bar is visible in LUT template selector without minimizing the browser size.");

		helper = new HelperClass(driver);
		viewerpage =  helper.loadViewerDirectlyUsingID(pmapPatientID, 1);
		
		findingMenu=new ViewerSliderAndFindingMenu(driver);
		viewerpage.waitForElementVisibility(pmap.lutbar);
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.assertFalse(viewerpage.isElementPresent(findingMenu.verticalScrollBar), "Checkpoint[1/3]", "Verified that no scrollbar is present when browser window maximize");
		
		viewerpage.resizeBrowserWindow(600, 400);
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.assertTrue(viewerpage.isElementPresent(findingMenu.verticalScrollBar), "Checkpoint[2/3]", "Verified that scrollbar is present when browser window minimize");

		viewerpage.maximizeWindow();
		pmap.openOrCloseLutBar(1, NSGenericConstants.OPEN);
		viewerpage.assertFalse(viewerpage.isElementPresent(findingMenu.verticalScrollBar), "Checkpoint[3/3]", "Verified that no scrollbar is present when browser window maximize");
	}
}










