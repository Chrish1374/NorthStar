//package com.trn.ns.test.obsolete;
//
//import java.text.ParseException;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.sql.SQLException;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.LoginPageConstants;
//import com.trn.ns.page.constants.NSGenericConstants;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.Header;
//import com.trn.ns.page.factory.LoginPage;
//import com.trn.ns.page.factory.PatientListPage;
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class StudyListTest extends TestBase 
//{
//	private StudyListPage studyPage;
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private ExtentTest extentTest;
//
//	String gspsTextFilepath =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
//	String PatientNameGSPSText = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsTextFilepath);
//
//	
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod() throws InterruptedException{
//
//		loginPage = new LoginPage(driver);
//		loginPage.navigateToBaseURL();
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));	
//		patientPage = new PatientListPage(driver);		
//		patientPage.waitForPatientPageToLoad();	
//
//		String newURL="http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL;
//		patientPage.navigateToURL(newURL); 
//
//
//		studyPage = new StudyListPage(driver) ;
//		studyPage.waitForStudyListToLoad();
//
//		if(driver.getCurrentUrl().contains("login")){
//			loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//			studyPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL); 
//		}
//
//	}	
//	//US33_TC_7-US33: Display the list of studies in the system_IE11
//	//US33_TC_198-US33: Display the list of studies in the system_Firefox
//	//US33_TC_199-US33: Display the list of studies in the system_Chrome
//	//US33_TC_200-US33: Display the list of studies in the system_Edge
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_TC198_TC199_TC7_TC200_verifySortIconPresenceOnStudyList() throws InterruptedException, ClassNotFoundException, SQLException
//	{	 
//		extentTest = ExtentManager.getTestInstance();
//		if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("firefox") )			
//			extentTest.setDescription("US33_TC_198 : Display the list of studies in the system_Firefox");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Chrome") )
//			extentTest.setDescription("US33_TC_199 : Display the list of studies in the system_Chrome");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("IE11") )
//			extentTest.setDescription("US33_TC_7 : Display the list of studies in the system_IE11");	
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Edge") )
//			extentTest.setDescription("US33_TC_200 : Display the list of studies in the system_Edge");	
//
//		studyPage = new StudyListPage(driver) ;
//		studyPage.waitForElementVisibility(studyPage.PatientIDHeader);
//
//		// Verifying by default ascending arrow is present on PATIENT ID column
//		studyPage.verifyEquals(studyPage.sortedAscColumnHeader.getText(),"PATIENT ID", "Verify by default ascending arrow is present on PATIENT ID column.","Verified PATIENT ID is default sorted column");
//
//		// Verifying the correct sorting image(up and down arrow) is getting displayed on study list page.   
//		int i =1;
//		for (WebElement header : studyPage.columnHeaders) {		 
//
//			String colHeader=header.getText();		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+studyPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : ascending sort");
//			header.click();
//			studyPage.verifyEquals(studyPage.sortedAscColumnHeader.getText(), colHeader,"Verify Up arrow icon of column header- "+colHeader+"" , "Rendering of Up Arrow icon of column header "+colHeader+" successful. ");		
//			//			studyPage.compareElementImage(protocolName, studyPage.upArrow, "Verify up arrow image of column header - "+colHeader+"", header.getText().trim()+"StudyList" + "upArrow", new int[]{(int) studyPage.getImageCoordinate(studyPage.upArrowIcon).get("X"), (int) studyPage.getImageCoordinate(studyPage.upArrowIcon).get("Y"), (int) studyPage.getImageCoordinate(studyPage.upArrowIcon).get("Width"), (int) studyPage.getImageCoordinate(studyPage.upArrowIcon).get("Height")},new Boolean(false));
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b/"+studyPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : descending sort");	
//			header.click();	
//			studyPage.verifyEquals(studyPage.sortedDscColumnHeader.getText(),colHeader,"Verify Down arrow icon of column headers - "+colHeader+"" , "Rendering of Down Arrow icon of column headers-PASS ");
//			//			studyPage.compareElementImage(protocolName, studyPage.downArrow, "Verify down arrow image of of column headers - "+colHeader+"", header.getText().trim()+ "StudyList" +"dwnArrow", new int[]{(int) studyPage.getImageCoordinate(studyPage.downArrowIcon).get("X"), (int) studyPage.getImageCoordinate(studyPage.downArrowIcon).get("Y"), (int) studyPage.getImageCoordinate(studyPage.downArrowIcon).get("Width"), (int) studyPage.getImageCoordinate(studyPage.downArrowIcon).get("Height")},new Boolean(false));
//
//			//Verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".c /"+studyPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of"+header.getText()+"");		
//			studyPage.verifyEquals(studyPage.formatDecimalNumber(header.getCssValue("font-size")),"15px", "Verifying the font size of "+header.getText()+"","font size of "+header.getText()+" rendered as expected");
//			studyPage.verifyEquals(header.getCssValue("font-family").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "Verifying the font family of "+header.getText()+"","font family of "+header.getText()+" rendered as expected");
//			studyPage.verifyEquals(studyPage.getHexColorValue(header.getCssValue("color")),"#5c5653", "Verifying the font color of "+header.getText()+"","font color of "+header.getText()+" rendered as expected");
//			i++;
//
//
//		}
//
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : studyPage.rowCellValues)
//		{ 		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+"" + studyPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of study list table");
//			studyPage.verifyEquals(studyPage.formatDecimalNumber(cell.getCssValue("font-size")),"15px", "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//			studyPage.verifyEquals(cell.getCssValue("font-family"),"Source Sans Pro Light", "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//			studyPage.verifyEquals(studyPage.getHexColorValue(cell.getCssValue("color")),"#140b0a", "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//			i++;
//		} 
//
//		//Rendering of viewer page on double click on first row  	
//		studyPage.click(studyPage.rowCellValues.get(1));
//		ViewerPage viewerpage = new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		studyPage.assertTrue(viewerpage.viewer.isDisplayed(), "Verify viewer page is rendered when double clicked on any row in study list page.","Viewer page is rendered");		
//
//	}
//
//	//US31_TC_6: Sorting the list of patient
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test02_US31_TC6_verifySortingAndTooltips() throws InterruptedException, ParseException
//	{
//		//US31_TC_6: Sorting the list of patient
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Sorting the list of patient");
//		StudyListPage patientstudypage = new StudyListPage(driver);
//		patientstudypage.waitForStudyListToLoad();
//
////		for (int i =0 ;i< patientstudypage.columnHeaders.size();i++){
//		for (int i =0 ;i< 4;i++){
//
//			//Verifying tooltips on sorted columns
//			patientstudypage.performAscendingSort(i);
//			patientstudypage.assertEquals(patientstudypage.getTooltipColHeader(i), patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+" column when it is sorted in ascedning order..","Verified tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i)) +" column when it is sorted in ascedning order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".a /" + patientstudypage.columnHeaders.size() +"]", "Verify tooltip on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//			// After sorting in ascending order by clicking on column header
//			patientstudypage.assertEquals(patientstudypage.fetchColumn(i),patientstudypage.ascSortingCol(i), "Verify column is sorted in ascending order","Verified column is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".b /" + patientstudypage.columnHeaders.size() +"]", "Verify column is sorted in ascending order.");				
//			patientstudypage.performDescendingSort(i);
//			patientstudypage.assertEquals(patientstudypage.getTooltipColHeader(i), patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+ "," + " descending order", "Verify tooltip on column when it is sorted in descedning order.","Verified tooltip on column when it is sorted in descedning order..");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".c /" + patientstudypage.columnHeaders.size() +"]", "Verify tooltip on column when it is sorted in descedning order..");	
//			//Descending sort
//			patientstudypage.assertEquals(patientstudypage.fetchColumn(i),patientstudypage.dscSortingCol(i), "Verify column is sorted in descending order", "Verfied column is sorted in descending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".d /" + patientstudypage.columnHeaders.size() +"]", "Verify column is sorted in descending order.");
//		}
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test03_DE234_TC1139_verifyWrapOfDataOnColumnHeader() throws InterruptedException 
//	{    
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Column Headers and data in the column on Study List screen");                
//
//		StudyListPage patientstudypage = new StudyListPage(driver);
//		patientstudypage.waitForStudyListToLoad(); 
//		for (int i =0 ;i< patientstudypage.columnHeadersTextOverFlow.size();i++)
//		{
//			patientstudypage.assertTrue(patientstudypage.verifyTextOverFlowForDataWraping(patientstudypage.columnHeadersTextOverFlow.get(i)), "Verify Ellipsis are enable on Column Header", "The Ellipsis are enable on Column Header"); 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+"/" + patientstudypage.columnHeadersTextOverFlow.size() +"]", "Verify Ellipsis on "+ patientstudypage.getText("visibility",patientstudypage.columnHeaders.get(i))+" column");  	 
//		}    
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome"})
//	public void test04_DE83_TC989_colorOfAlternateRowsOfStudyList() throws InterruptedException
//	{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Check the color in alternate rows and table alignment on StudyList screen.");
//		studyPage = new StudyListPage(driver) ;
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the alternate Background color of the row of Study List page is as per required");
//
//		studyPage.assertEquals(studyPage.getBackgroundColorOfStudyList(1),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//		studyPage.assertEquals(studyPage.getBackgroundColorOfStudyList(3),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//		studyPage.assertEquals(studyPage.getBackgroundColorOfStudyList(2),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//		studyPage.assertEquals(studyPage.getBackgroundColorOfStudyList(4),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test05_DE88_TC802_verifySortingArrowOnBrowserResize()
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on study list page sort icon(up arrow or down arrow) is displayed beside the column name when browser window resized.");
//		studyPage = new StudyListPage(driver);
//		studyPage.resizeBrowserWindow(1200,900);
//		int i=1;
//		for (WebElement header : studyPage.columnHeaders) {
//			studyPage.click(header);
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i)+"/" + studyPage.columnHeaders.size() +"]", "Verify up arrow is displayed beside the column name- " + header.getText()+ " on browser resize.");
//			studyPage.assertTrue((studyPage.getImageCoordinate(studyPage.upSortArrow).get("X") + studyPage.getImageCoordinate(studyPage.upSortArrow).get("Width")) <= ((studyPage.getImageCoordinate(studyPage.sortedAscColumnHeader).get("X") + studyPage.getImageCoordinate(studyPage.sortedAscColumnHeader).get("Width")+1)),"Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header","Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header");
//			studyPage.assertTrue( (studyPage.getImageCoordinate(studyPage.upSortArrow).get("X")+1) >= (studyPage.getImageCoordinate(studyPage.columnHeaderText).get("X") + studyPage.getImageCoordinate(studyPage.columnHeaderText).get("Width")), "Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header", "Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header");
//			studyPage.assertTrue( (studyPage.getImageCoordinate(studyPage.upSortArrow).get("Y") + studyPage.getImageCoordinate(studyPage.upSortArrow).get("Height") ) < (studyPage.getImageCoordinate(studyPage.columnHeaderText).get("Y") + studyPage.getImageCoordinate(studyPage.columnHeaderText).get("Height")), "Verify y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header", "Verified y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header");
//			i++;
//		}
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test07_US586_TC1888_verifyAboutLinkFromStudyList() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify licensing Information using About link from Study List Page");
//		studyPage = new StudyListPage(driver);
//		Header header = new Header(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify Scan drop down menu is present on top left on page");
//		patientPage.assertTrue(header.userInfo.isDisplayed(), "Verifying that Scan drop down menu is present on top left of Patient List Page", "Scan drop down menu is present on left corner of page");
//		header.viewAboutPage();
//		header.switchToNewWindow(2);
//		header.maximizeWindow();
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/5]", "Verify About page opens in a new tab");
//		header.assertTrue(header.getCurrentPageURL().contains(URLConstants.ABOUT_PAGE_URL), "Verifying the About Page is open", "User is on About page "+ loginPage.getCurrentPageURL());
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify Hammer JS licensing Information is present on About page");
//		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.HAMMERJS), "Verifying Hammer JS licensing information is present", "The Hammer JS licensing information is present on page");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify ng-bootstrap licensing Information is present on About page");
//		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.BOOTSTARP), "Verifying ng-bootstrap licensing information is present", "The ng-bootstrap licensing information is present on page");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5/5]", "Verify Newtonsoft.Json licensing Information is present on About page");
//		header.assertTrue(header.getTextForPage().contains(LoginPageConstants.NEWTONSOFT), "Verifying Newtonsoft.Json licensing information is present", "The Newtonsoft.Json licensing information is present on page");
//	}
//	
//	@Test(groups ={"Chrome"})
//	public void test8_US675_TC2418_verifyMultipleStudiesOfSamePatientButDiffAssigningAuthority() throws InterruptedException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Add Assigning Authority field to Patient data- Data with IssuerofPatientID");
//
//		String filePath1=Configurations.TEST_PROPERTIES.get("AH.4_US675_filepath");
//		String AH4_US675 = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath1);
//
//		studyPage = new StudyListPage(driver);
//
//		studyPage.getStudyListOfPatientwhichHasSameName(AH4_US675);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify three studies are present for patient " + AH4_US675);
//		studyPage.verifyEquals(studyPage.getStudyListOfPatientwhichHasSameName(AH4_US675).size(),3, "Verifying three studies are present for patient " + AH4_US675, "Three studies are present for patient " + AH4_US675);	
//	}	
//	
//	@Test(groups ={"Chrome","Edge","IE11","DE1933", "Positive"})
//	public void test9_DE1933_TC7810_TC7811_verifyDatesSortingAtStudyListAndStudyPage() throws InterruptedException, ParseException
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that sorting is correctly loaded  for study Date & Time column on study list page. <br>"+
//		"Verify that sorting is correctly loaded  for study Date & Time column on patient's study  page.");
//
//		patientPage = new PatientListPage(driver) ;
//		studyPage = new StudyListPage(driver);
//	
//		//TC7810
//		//Verify if column is in descending order click on validate the ascending order and again validate the descending order by clicking on date header
//
//		//If column changes, so it will find the date and time column in the table	
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.studyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, descending order"), "Checkpoint[1/12]", "Verifying the tooltip of studylist date and time, which should be the descending order");
//
//		//dates by default should be in the descending order
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(studyPage.studyListDates, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[2/12]", "Verifying that default date and time is in descending order");
//				
//		//click on the header and validating that date and time are in ascending order
//		studyPage.click(studyPage.studyTimeHeader);
//		studyPage.assertTrue(patientPage.getAttributeValue(studyPage.studyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, ascending order"), "Checkpoint[3/12]", "Verifying the tooltip of studylist date and time, when user clicks on the header");
//		studyPage.assertTrue(patientPage.dateAscendingOrderValidation(studyPage.studyListDates, ViewerPageConstants.STANDARDDATEFORMAT), "Checkpoint[4/12]", "Verifying that date and time is in ascending order, when user clicks on the header");
//					
//		//click on the header and validating that date and time are in descending order
//		studyPage.click(studyPage.studyTimeHeader);
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.studyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, descending order"), "Checkpoint[5/12]", "Verifying the tooltip of studylist date and time, when user clicks on the header");
//		studyPage.assertTrue(patientPage.dateDescendingOrderValidation(studyPage.studyListDates, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[6/12]", "Verifying that date and time is in descending order, when user clicks on the header");
//
//		//TC7811
//		//user is navigating to patient page
//		studyPage.navigateToURL("http://localhost/#/patient/AH.4_US675?issuerOfPatientId=frhprod");
//		patientPage.waitForPatientPageToLoad();	
//
//
//		//If column changes, so it will find the date and time column in the table
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.multiStudyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, descending order"), "Checkpoint[7/12]", "Verifying the tooltip of studylist date and time, which should be the descending order");
//			
//		//dates by default should be in the descending order
//		patientPage.assertTrue(patientPage.dateDescendingOrderValidation(studyPage.multiStudyListDates, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[8/12]", "Verifying that default date and time is in descending order");
//				
//		//click on the header and validating that date and time are in ascending order
//		studyPage.click(studyPage.multiStudyTimeHeader);
//		studyPage.assertTrue(patientPage.getAttributeValue(studyPage.multiStudyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, ascending order"), "Checkpoint[9/12]", "Verifying the tooltip of studylist date and time, when user clicks on the header");
//		studyPage.assertTrue(patientPage.dateAscendingOrderValidation(studyPage.multiStudyListDates, ViewerPageConstants.STANDARDDATEFORMAT), "Checkpoint[10/12]", "Verifying that date and time is in ascending order, when user clicks on the header");
//					
//		//click on the header and validating that date and time are in descending order
//		studyPage.click(studyPage.multiStudyTimeHeader);			
//		studyPage.assertTrue(studyPage.getAttributeValue(studyPage.multiStudyTimeHeader, NSGenericConstants.TITLE).equalsIgnoreCase("Study Date & Time, descending order"), "Checkpoint[11/12]", "Verifying the tooltip of studylist date and time, when user clicks on the header");
//		studyPage.assertTrue(patientPage.dateDescendingOrderValidation(studyPage.multiStudyListDates, ViewerPageConstants.STANDARDDOBFORMAT), "Checkpoint[12/12]", "Verifying that date and time is in descending order, when user clicks on the header");
//       
//	}
//	
//	//US1986:Update study list Envoy AI column and icon
//	@Test(groups ={"Chrome","Edge","IE11", "US1986", "Positive"})
//	public void test12_US1986_TC8822_verifyEurekaIconOnStudyListPage() throws InterruptedException
//		{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the new 'Eureka AI' for 'Envoy AI' icon on 'PatientStudyList' and 'StudyList' page.");
//		
//		studyPage = new StudyListPage(driver);
//
//		studyPage.mouseHover(studyPage.studylistColumnHeader.get(4));
//		studyPage.assertEquals(studyPage.getText(studyPage.studylistColumnHeader.get(4)), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[1/4]", "Verified Eureka AI header on study List page.");
//		
//		for(int i=0;i<studyPage.patientNameList.size();i++)
//		{
//			if(studyPage.getText(studyPage.patientNameList.get(i)).equalsIgnoreCase(PatientNameGSPSText));
//			{
//				studyPage.mouseHover(studyPage.allEurekaIcon.get(i));
//				studyPage.assertTrue(studyPage.isElementPresent(studyPage.allEurekaIcon.get(i)), "Checkpoint[2/4]", "Verified that Eureka AI Icon is visible for "+PatientNameGSPSText);
//				studyPage.assertEquals(studyPage.convertIntoInt(studyPage.getText(studyPage.numberOfResults)), 2, "Checkpoint[3/4]", "Verified number of results visible for "+PatientNameGSPSText);
//				break;
//			}
//			
//		}
//		studyPage.performMouseRightClick(studyPage.columnHeadersTextOverFlow.get(3));	
//		studyPage.assertTrue(studyPage.convertWebElementToStringList(studyPage.columnHeadersTextOverFlow).contains(PatientPageConstants.STUDY_LIST_PAGE_COLUMN_HEADERS.get(4)), "Checkpoint[4/4]", "Verifying the column name is present in the list");
//		  
//		}
//
//Obsolete as studylist url is deprecated now
/*	@Test(groups ={"Chrome","Edge","IE11","US1409","Positive"})
	public void test06_US1409_TC7995_verifyPwdEncryptedOnSingleStudyPageAccess() 	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in client side when user tries to access study list page (Direct URL access)");
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.SINGLE_PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientStudyPage = new SinglePatientStudyPage(driver);
		patientStudyPage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying the URL is present in the Authenticate request when accessed the NON UI Patient's study  page with Password");
		patientStudyPage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying the password is not present in URL hence encrypted");	
		

	}*/
//Obsolete as study list is deprecated 
/*	@Test(groups ={"Chrome","Edge","IE11","US1409","Negative"})
	public void test09_US1409_TC8002_verifyPwdEncryptedOnSingleStudyPageByIssuerID() throws SQLException 	{
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify password is encrypted in the client side for  non UI login accessing  PatientStudy using the URL launch");
		loginPage = new LoginPage(driver);
		
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>();  
		hm.put(LoginPageConstants.PASSWORD,password); 
		hm.put(LoginPageConstants.USERNAME,username);
		hm.put(OrthancAndAPIConstants.ISSUER_OF_PATIENT_ID,db.getIssuerOfPatientID(patientID));
		
		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, " ", "Verifying the launching of study page from non-ui login");
		String myURL = loginPage.getNonUILaunchURL(URLConstants.SINGLE_PATIENT_LIST_URL+"/"+patientID, hm);
		loginPage.navigateToURL(myURL);
		
		patientStudyPage = new SinglePatientStudyPage(driver);
		

		patientStudyPage.assertTrue(loginPage.verifyPasswordEncryption(url),"Checkpoint[1/2]","Verifying the URL is present in the Authenticate request when accessed the NON UI Study page with issuer ID with Password");
		patientStudyPage.assertFalse(loginPage.verifyPasswordEncryption(url+password),"Checkpoint[2/2]","Verifying the password is not present in URL hence encrypted");	
		

	}*/
	
//	
//}