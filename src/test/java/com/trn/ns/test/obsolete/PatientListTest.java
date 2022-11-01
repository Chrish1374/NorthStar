//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//import java.text.ParseException;
//import java.util.List;
//
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.enums.BrowserType;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.OrthancAndAPIConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class PatientListTest extends TestBase 
//{
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	private ExtentTest extentTest;
//
//	String ah4Filepath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String patientNameAh4 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,ah4Filepath);
//
//	String randentFilepath=Configurations.TEST_PROPERTIES.get("RAND^ENT_filepath");
//	String patientNameRandoENT = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,randentFilepath);
//	
//	String gspsMultiSeriesFilepath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String patientNameGSPSMultiSeries = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,gspsMultiSeriesFilepath);
//	
//	
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//
//	}	
//
//	//US97_TC_226-US97: Apply the UI/UX on top of the existing worflow for the patient list screen_IE11
//	//US97_TC_227-US97: Apply the UI/UX on top of the existing worflow for the patient list screen_Firefox
//	//US97_TC_228-US97: Apply the UI/UX on top of the existing worflow for the patient list screen_Chrome
//	//US97_TC_229-US97: Apply the UI/UX on top of the existing worflow for the patient list screen_Edge
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_US97_TC226_TC227_TC228_TC229_verifySortIconPresenceOnPatientTable() throws InterruptedException
//	{	 
//		extentTest = ExtentManager.getTestInstance();
//
//		if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("firefox") )			
//			extentTest.setDescription("US97_TC227 : Apply the UI/UX on top of the existing worflow for the patient list screen_Firefox");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Chrome") )
//			extentTest.setDescription("US97_TC228 : Apply the UI/UX on top of the existing worflow for the patient list screen_Firefox");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("IE11") )
//			extentTest.setDescription("US97_TC226 : Apply the UI/UX on top of the existing worflow for the patient list screen_Firefox");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Edge") )
//			extentTest.setDescription("US97_TC229 : Apply the UI/UX on top of the existing worflow for the patient list screen_Firefox");	
//
//		patientPage = new PatientListPage(driver) ;
//		patientPage.waitForElementVisibility(patientPage.PatientIDHeader);	
//
//		// Verifying by default ascending arrow is present on PATIENT NAME column
//		patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(),"Patient Name", "Verify by default ascending arrow is present on PATIENT NAME column.","Verified PATIENT NAME is default sorted column");
//
//
//		// Verifying the correct sorting image is getting displayed on Patient list sorting.
//		int i =1;
//		for (WebElement header : patientPage.columnHeaders) {		 
//			String colHeader=header.getText();		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : ascending sort");
//			header.click();
//			patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(), colHeader,"Verify Up arrow icon of column header- "+colHeader+"" , "Rendering of Up Arrow icon of column header "+colHeader+" successful. ");		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b/"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : descending sort");	
//			header.click();	
//			patientPage.verifyEquals(patientPage.sortedDscColumnHeader.getText(),colHeader,"Verify Down arrow icon of column headers - "+colHeader+"" , "Rendering of Down Arrow icon of column headers-PASS ");
//
//			//verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".c /"+patientPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of"+header.getText()+"");
//
//			patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(header)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+header.getText()+"","font size of "+header.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getFontFamilyOfWebElemnt(header),PatientPageConstants.LIGHT_THEME_HEADER_FONT_FAMILY, "Verifying the font family of "+header.getText()+"","font family of "+header.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getColorOfRows(header),PatientPageConstants.LIGHT_THEME_HEADER_COLOR, "Verifying the font color of "+header.getText()+"","font color of "+header.getText()+" rendered as expected");
//			i++;
//		}
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : patientPage.rowCellValues)
//		{ 		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".d /" + patientPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of patient list table");
//			patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(cell)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getFontFamilyOfWebElemnt(cell),PatientPageConstants.LIGHT_THEME_ROW_FONT_FAMILY, "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getColorOfRows(cell),PatientPageConstants.LIGHT_THEME_ROW_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//			i++;
//		}  	
//
//	}
//
//	//US31_TC_6: Sorting the list of patient
//	//US31_TC_6: Sorting the list of patient
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test02_US31_TC6_verifySortingAndTooltips() throws InterruptedException, ParseException
//	{
//		//US31_TC_6: Sorting the list of patient
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Sorting the list of patient");
//
//		patientPage = new PatientListPage(driver) ;
//
//		for (int i =0 ;i< patientPage.columnHeaders.size();i++){
//			patientPage.assertEquals(patientPage.getTooltipColHeader(i), patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order..","Verified tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".a /" + patientPage.columnHeaders.size() +"]", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//
//			patientPage.assertEquals(patientPage.fetchColumn(i),patientPage.ascSortingCol(i), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(i))+  "column is sorted in ascending order",patientPage.getText("visibility",patientPage.columnHeaders.get(i)) + "is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".b /" + patientPage.columnHeaders.size() +"]", "Verify "+patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column is sorted in ascending order.");		
//
//			patientPage.assertEquals(patientPage.getTooltipColHeader(i), patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "," + " descending order", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order.","Verified tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order..");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".c /" + patientPage.columnHeaders.size() +"]", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when it is sorted in descedning order..");
//
//			patientPage.assertEquals(patientPage.fetchColumn(i),patientPage.dscSortingCol(i), "Verify" + patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "column is sorted in ascending order",patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ " is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".d /" + patientPage.columnHeaders.size() +"]", "Verify "+patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column is sorted in descending order.");
//
//		}
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test03_US30_TC76_verifyRowSelection() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Validation of Selection of Rows ");
//		patientPage = new PatientListPage(driver) ;
//		Actions builder = new Actions(driver);
//		builder.moveToElement(patientPage.patientRows.get(0)).perform();
//		patientPage.waitForTimePeriod("Low");
//		patientPage.verifyEquals(patientPage.patientRows.get(0).getCssValue("background"),"#C8DFF7", "Verifying the highlighted row color of first row","Highlighted row color is as expected");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test04_DE234_TC1137_verifyWrapOfDataOnColumnHeader() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Column Headers and data in the column on Patient List screen");
//
//		patientPage = new PatientListPage(driver) ;
//		for (int i =0 ;i< patientPage.columnHeadersTextOverFlow.size();i++)
//		{
//			patientPage.assertTrue(patientPage.verifyTextOverFlowForDataWraping(patientPage.columnHeadersTextOverFlow.get(i)), "Verify Ellipsis are enable on Column Header", "The Ellipsis are enable on Column Header"); 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+"/" + patientPage.columnHeadersTextOverFlow.size() +"]", "Verify Ellipsis on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column");
//
//		}    
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test05_DE234_TC1144_verifyWrapOfDataOnColumnHeaderOnWindowResize() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Column Headers and data in the column on Patient List screen on Window Resizing");
//		patientPage = new PatientListPage(driver) ;
//		patientPage.resizeBrowserWindow(800, 500);
//		for (int i =0 ;i< patientPage.columnHeadersTextOverFlow.size();i++)
//		{
//			patientPage.assertTrue(patientPage.verifyTextOverFlowForDataWraping(patientPage.columnHeadersTextOverFlow.get(i)), "Verify Ellipsis are enable on Column Header", "The Ellipsis are enable on Column Header"); 
//			patientPage.assertEquals(patientPage.getTooltipColHeader(i), patientPage.getText("visibility",patientPage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when window is resized.","Verified tooltip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column when window is resized.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+"/" + patientPage.columnHeadersTextOverFlow.size() +"]", "Verify Ellipsis and Tool Tip on "+ patientPage.getText("visibility",patientPage.columnHeaders.get(i))+" column");
//
//		}    
//	}
//
//	//TC1249 - Step5 - Verify dark theme BG color patient list page.
//	//Verify the window leveling on layout change
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome"})
//	public void test06_DE83_TC988_colorOfAlternateRowsOfPatientList() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Check the color in alternate rows and table alignment on Patient List screen.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the alternate Background color of the row of Patient List page is as per required");
//
//		patientPage.assertEquals(patientPage.getBackgroundColorOfPatientList(1),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//		patientPage.assertEquals(patientPage.getBackgroundColorOfPatientList(3),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//		patientPage.assertEquals(patientPage.getBackgroundColorOfPatientList(4),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//		patientPage.assertEquals(patientPage.getBackgroundColorOfPatientList(2),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test06_DE88_TC800_verifySortingArrowOnBrowserResize()
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on patient list page sort icon(up arrow or down arrow) is displayed beside the column name when browser window resized.");
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.resizeBrowserWindow(700,600);
//		int i=1;
//		for (WebElement header : patientPage.columnHeaders) {
//			patientPage.click(header);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i)+"/" + patientPage.columnHeaders.size() +"]", "Verify up arrow is displayed beside the column name- " + header.getText()+ " on browser resize.");
//			patientPage.assertTrue((patientPage.getImageCoordinate(patientPage.upArrowIcon).get("X") + patientPage.getImageCoordinate(patientPage.upArrowIcon).get("Width")) <= ((patientPage.getImageCoordinate(patientPage.sortedAscColumnHeader).get("X") + patientPage.getImageCoordinate(patientPage.sortedAscColumnHeader).get("Width")+1)),"Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header","Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header");
//			patientPage.assertTrue( (patientPage.getImageCoordinate(patientPage.upArrowIcon).get("X")+1) >= (patientPage.getImageCoordinate(patientPage.columnHeaderText).get("X") + patientPage.getImageCoordinate(patientPage.columnHeaderText).get("Width")), "Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header", "Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header");
//			patientPage.assertTrue( (patientPage.getImageCoordinate(patientPage.upArrowIcon).get("Y") + patientPage.getImageCoordinate(patientPage.upArrowIcon).get("Height") ) < (patientPage.getImageCoordinate(patientPage.columnHeaderText).get("Y") + patientPage.getImageCoordinate(patientPage.columnHeaderText).get("Height")), "Verify y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header", "Verified y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header");        	
//			i++;
//		}
//	}
//
//	//TC144 : Scrolling to display more rows of a List - patient or study_on IE
//	//TC145 : Scrolling to display more rows of a List - patient or study_on Firefox
//	//TC146 : Scrolling to display more rows of a List - patient or study_on Google Chrome
//	//TC149 : Scrolling to display more rows of a List - patient or study_on Edge
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test07_US95_TC144_TC145_TC146_TC149_verifyVerticalScrollbar() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Scrolling to display more rows of a List");
//		patientPage = new PatientListPage(driver);
//
//		//Set dimension to resize the window
//		Dimension dimension = new Dimension(800, 600);
//		String parentWindow = driver.getWindowHandle();
//
//		patientPage.setWindowSize(parentWindow,dimension);
//		//need mouse hover on first row to display scroll bar
//		patientPage.mouseHover(patientPage.patientNameList.get(0));
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying scrollbar is displaying for more studies on Patient list page" );
//		patientPage.assertTrue(patientPage.verticalScrollbar.isDisplayed(), "Verify scrollbar presence on Patient list screen", "Scroll is present");	
//
//		int lastRowNum = patientPage.patientNameList.size()-1;
//
//		//Scroll down and Mouse hover on last data to verify that last row data is accessible
//		patientPage.scrollIntoView(patientPage.patientNameList.get(lastRowNum));
//		patientPage.waitForPatientPageToLoad();
//		String browserName=Configurations.TEST_PROPERTIES.get("browserName");
//		if(!browserName.equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) 
//			patientPage.mouseHover(patientPage.patientNameList.get(lastRowNum));
//
//		//Taking screenshot for before image of last row
//		String newImagePath= TEST_PROPERTIES.get("imagesPath") + "Windows10/"+ TEST_PROPERTIES.get("Browser") + "/" + protocolName;
//		patientPage.takeElementScreenShot(patientPage.patientNameList.get(lastRowNum), newImagePath+"/actualImages/beforeImage.png");
//
//		//Verifying that header is present 
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying that column header are visible after scrolling down" );
//		patientPage.assertTrue(patientPage.verifyHeader(), "Verify that the column headers are visible","Column headers are displaying successfully after scrolling down");
//
//		String beforeImage = newImagePath+"/actualImages/beforeImage.png";
//		String afterImage = newImagePath+"/actualImages/afterImage.png";
//		String diffImage = newImagePath+"/actualImages/diffImage.png";
//
//		//Moving to Single patient list page by clicking on last displayed patient
//		patientPage.clickOnPatientRow(patientPage.getPatientName(lastRowNum));
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.browserBackWebPage();
//		patientPage.waitForPatientPageToLoad();
//
//		//Again Scroll down and Mouse hover on last data to verify that last row data is accessible
//		patientPage.scrollIntoView(patientPage.patientNameList.get(lastRowNum));
//		patientPage.waitForPatientPageToLoad();
//		if(!browserName.equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) 
//			patientPage.mouseHover(patientPage.patientNameList.get(lastRowNum));
//
//		patientPage.takeElementScreenShot(patientPage.patientNameList.get(lastRowNum), newImagePath+"/actualImages/afterImage.png");
//
//		//Comparing before and after image of last row
//		boolean cpStatus =  patientPage.compareimages(beforeImage, afterImage, diffImage);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying that last row is visible after scrolling down" );
//		patientPage.assertTrue(cpStatus, "Verify that the before and after image are same","The before and after image are same");
//
//		patientPage.maximizeWindow();
//	}
//
//	//US367_TC1249 : Verify dark theme BG color patient list page.
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test08_US367_TC1249_verifyUISpecAsPerDarkTheme() throws TimeoutException, InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify dark theme BG color Study list page");
//
//		patientPage = new PatientListPage(driver) ;
//		int i =1;
//		for (WebElement header : patientPage.columnHeaders) {		 
//			/*String colHeader=header.getText();		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : ascending sort");
//			header.click();
//			patientPage.verifyEquals(patientPage.sortedAscColumnHeader.getText(), colHeader,"Verify Up arrow icon of column header- "+colHeader+"" , "Rendering of Up Arrow icon of column header "+colHeader+" successful. ");		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b/"+patientPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : descending sort");	
//			header.click();	
//			patientPage.verifyEquals(patientPage.sortedDscColumnHeader.getText(),colHeader,"Verify Down arrow icon of column headers - "+colHeader+"" , "Rendering of Down Arrow icon of column headers-PASS ");
//			 */
//			//verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".c /"+patientPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of"+header.getText()+"");
//
//			patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(header)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+header.getText()+"","font size of "+header.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getFontFamilyOfWebElemnt(header),PatientPageConstants.DARK_THEME_HEADER_FONT_FAMILY, "Verifying the font family of "+header.getText()+"","font family of "+header.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getColorOfRows(header),PatientPageConstants.DARK_THEME_HEADER_FONT_COLOR, "Verifying the font color of "+header.getText()+"","font color of "+header.getText()+" rendered as expected");
//
//			i++;
//		}
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : patientPage.rowCellValues)
//		{ 	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".d /" + patientPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of patient list table");
//			patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(cell)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getFontFamilyOfWebElemnt(cell),PatientPageConstants.DARK_THEME_ROW_FONT_FAMILY, "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getBackgroundColorOfRows(cell),PatientPageConstants.DARK_THEME_BACKGROUND_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//
//			if(patientPage.getAttributeValue(cell,"class").equalsIgnoreCase(PatientPageConstants.HIGHLITHED_COLUMN_CLASS))
//				patientPage.verifyEquals(patientPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//
//			else
//				patientPage.verifyEquals(patientPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_ROW_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//			i++;
//		} 
//		patientPage.mouseHover(patientPage.patientNameList.get(0));
//		patientPage.verifyEquals(patientPage.getBackgroundColorOfPatientList(1),PatientPageConstants.DARK_THEME_HIGHLIGHTED_ROW_COLOR,"Verifying background color of highlighted row","Background color of "+patientPage.patientNameList.get(0)+" rendered as expected");
//		patientPage.verifyEquals(patientPage.getBorderColorOfWebElemnt(patientPage.getXpathOfRowsOfPatientList(1)),PatientPageConstants.DARK_THEME_BORDER_ROW_COLOR,"Verifying border color of row","Border color of row is rendered as expected");
//
//		//		//Verify header text and version color
//		//		patientPage.verifyEquals(patientPage.getColorOfRows(patientPage.headerTextAndVersion),PatientPageConstants.DARK_THEME_HEADER_TEXTANDVERSION_COLOR, "Verifying the font color of "+ patientPage.headerTextAndVersion.getText()+"","Font color of "+  patientPage.headerTextAndVersion.getText()+" rendered as expected");
//	}
//
//	
//
//
//	
//	
//	
//	
//}
