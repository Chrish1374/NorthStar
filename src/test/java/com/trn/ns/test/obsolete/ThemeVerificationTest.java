//package com.trn.ns.test.obsolete;
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.URLConstants;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.StudyListPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class ThemeVerificationTest extends TestBase 
//{
//	private PatientListPage patientPage;
//	private LoginPage loginPage;
//	private SinglePatientStudyPage patientStudyPage;
//	private StudyListPage studyPage;
//	private ExtentTest extentTest;
//
//	String filePath = Configurations.TEST_PROPERTIES.get("03Dimensions103_filepath");
//	String Dimensions103Patient = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath);
//
//
//	private static String defaultTheme = "";
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//
//
//		loginPage = new LoginPage(driver);		
//		loginPage.navigateToBaseURL();		
//		loginPage.login(Configurations.TEST_PROPERTIES.get("nsUserName"), Configurations.TEST_PROPERTIES.get("nsPassword"));
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
//	public void test01_updateDababaseTheme(){
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Updating the database with light theme");
//
//		//Getting default theme
//		DatabaseMethods db = new DatabaseMethods(driver);
//		defaultTheme = db.getTheme();
//
//		patientPage = new PatientListPage(driver) ;
//
//		//Updating the theme to light theme
//		if(!db.getTheme().trim().equalsIgnoreCase(PatientPageConstants.LIGHT_THEME))
//			db.updateTheme(PatientPageConstants.LIGHT_THEME);
//
//		db.assertEquals(db.getTheme(), PatientPageConstants.LIGHT_THEME, "Verify the Light theme is updated in DB", "Database is updated with light theme");
//	}
//
//	//US97_TC_226-US97: Apply the UI/UX on top of the existing workflow for the patient list screen_IE11
//	//US97_TC_227-US97: Apply the UI/UX on top of the existing workflow for the patient list screen_Firefox
//	//US97_TC_228-US97: Apply the UI/UX on top of the existing workflow for the patient list screen_Chrome
//	//US97_TC_229-US97: Apply the UI/UX on top of the existing workflow for the patient list screen_Edge
//	//US367_TC1251    : Verify light theme on patient list page.
//	//DE85 - TC676    : Verify font color of the table header on patient, single patient study and studylist pages.
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"},dependsOnMethods = "test01_updateDababaseTheme")
//	public void test02_US97_TC226_TC227_TC228_TC229_US367_TC1251_DE85_TC676_verifyUISpecAsPerLightThemeForPatientlist(){
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify light theme on patient list page.");
//
//		patientPage = new PatientListPage(driver) ;
//		int i =1;
//		for (WebElement header : patientPage.columnHeaders) {		 
//
//			//verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of "+header.getText()+"");
//
//			patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(header)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+header.getText()+"","Font size of "+header.getText()+" rendered as expected");
//			patientPage.assertTrue(verifyFontFamily(patientPage.getFontFamilyOfWebElemnt(header),PatientPageConstants.LIGHT_THEME_HEADER_FONT_FAMILY), "Verifying the font family of "+header.getText()+"","Font family of "+header.getText()+" rendered as expected");
//			patientPage.verifyEquals(patientPage.getColorOfRows(header),PatientPageConstants.LIGHT_THEME_HEADER_COLOR, "Verifying the font color of "+header.getText()+"","Font color of "+header.getText()+" rendered as expected");
//			i++;
//		}
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : patientPage.rowCellValues){ 	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of patient list table");
//			patientPage.scrollIntoView(cell);
//			if(!cell.getText().trim().isEmpty()){	
//				if(patientPage.getAttributeValue(cell,"class").equalsIgnoreCase(PatientPageConstants.HIGHLITHED_COLUMN_CLASS))
//					patientPage.verifyEquals(patientPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//				else{
//					patientPage.verifyEquals(patientPage.getColorOfRows(cell),PatientPageConstants.LIGHT_THEME_ROW_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//					patientPage.verifyEquals(patientPage.formatDecimalNumber(patientPage.getFontsizeOfWebElement(cell)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//					patientPage.assertTrue(verifyFontFamily(patientPage.getFontFamilyOfWebElemnt(cell),PatientPageConstants.LIGHT_THEME_ROW_FONT_FAMILY), "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//				}
//			}
////			} else{
////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]", "No data is present in the first row of patient list table for column '"+patientPage.getHeaderNameForEmptyData(i)+"'");
////			}
//			i++;
//		}
//	}
//
//	//US367 - TC1252 : Verify light theme on Study list pages
//	//DE85 - TC676 : Verify font color of the table header on patient, single patient study and studylist pages.
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"},dependsOnMethods = "test01_updateDababaseTheme")
//	public void test03_US367_TC1252_DE85_TC676_verifyUISpecAsPerLightThemeForSinglePatientlist(){
//
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify light theme on Study list pages");
//
//		patientPage = new PatientListPage(driver) ;
//		patientPage.clickOnPatientRow(Dimensions103Patient);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//
//		int i =1;
//		for (WebElement header : patientStudyPage.columnHeaders) {		 
//
//			//verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of "+header.getText()+"");
//
//			patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.getFontsizeOfWebElement(header)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+header.getText()+"","Font size of "+header.getText()+" rendered as expected");
//			patientStudyPage.assertTrue(verifyFontFamily(patientStudyPage.getFontFamilyOfWebElemnt(header),PatientPageConstants.LIGHT_THEME_HEADER_FONT_FAMILY), "Verifying the font family of "+header.getText()+"","Font family of "+header.getText()+" rendered as expected");
//			patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(header),PatientPageConstants.LIGHT_THEME_HEADER_COLOR, "Verifying the font color of "+header.getText()+"","Font color of "+header.getText()+" rendered as expected");
//			i++;
//		}
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : patientStudyPage.rowCellValues) { 	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of patient list table");
//			if(!(cell.getText().trim()).isEmpty()){
//				if(patientStudyPage.getAttributeValue(cell,"class").equalsIgnoreCase(PatientPageConstants.HIGHLITHED_COLUMN_CLASS))
//					patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//				else {
//					patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(cell),PatientPageConstants.LIGHT_THEME_ROW_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//					patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.getFontsizeOfWebElement(cell)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//					patientStudyPage.assertTrue(verifyFontFamily(patientStudyPage.getFontFamilyOfWebElemnt(cell),PatientPageConstants.LIGHT_THEME_ROW_FONT_FAMILY), "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//				}
//			} 
//			else{
//				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]", "No data is present in the first row of Single patient list table for column '"+patientPage.getHeaderNameForEmptyData(i)+"'");
//			}
//			i++;
//		} 
//	}
//
//	//DE85 - TC676 : Verify font color of the table header on patient, single patient study and studylist pages.
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"},dependsOnMethods = "test01_updateDababaseTheme")
//	public void test04_DE85_TC676_verifyUISpecAsPerLightThemeForStudylist() {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify font color of the table header on studylist pages");
//		patientPage = new PatientListPage(driver) ;
//
//		patientPage.navigateToURL("http://"+TEST_PROPERTIES.get("nsHostName")+":"+TEST_PROPERTIES.get("nsPort")+"/"+"#/"+URLConstants.STUDY_LIST_URL); 
//		studyPage = new StudyListPage(driver) ;
//		int i =1;
//		for (WebElement header : studyPage.columnHeaders) {		
//
//			//verifying font size,font family and font color of table headers
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientPage.columnHeaders.size()+"]", "Verifying font size,font family and font color of "+header.getText()+"");
//			studyPage.verifyEquals(studyPage.formatDecimalNumber(studyPage.getFontsizeOfWebElement(header)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+header.getText()+"","Font size of "+header.getText()+" rendered as expected");
//			studyPage.assertTrue(verifyFontFamily(studyPage.getFontFamilyOfWebElemnt(header),PatientPageConstants.LIGHT_THEME_HEADER_FONT_FAMILY), "Verifying the font family of "+header.getText()+"","Font family of "+header.getText()+" rendered as expected");
//			studyPage.verifyEquals(studyPage.getColorOfRows(header),PatientPageConstants.LIGHT_THEME_HEADER_COLOR, "Verifying the font color of "+header.getText()+"","Font color of "+header.getText()+" rendered as expected");
//			i++;
//		}
//		//Verifying size,font family and font color of first row
//		i = 1;
//		for(WebElement cell : studyPage.rowCellValues){ 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]", "Verify font size,font family and font color of "+cell.getText()+" present in the first row of patient list table");
//
//			if(!cell.getText().trim().isEmpty()){
//
//				if(studyPage.getAttributeValue(cell,"class").equalsIgnoreCase(PatientPageConstants.HIGHLITHED_COLUMN_CLASS))
//					studyPage.verifyEquals(studyPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//				else{
//					studyPage.verifyEquals(studyPage.formatDecimalNumber(studyPage.getFontsizeOfWebElement(cell)),PatientPageConstants.FONT_SIZE, "Verifying the font size of "+ cell.getText()+"","Font size of "+ cell.getText()+" rendered as expected");
//					studyPage.assertTrue(verifyFontFamily(studyPage.getFontFamilyOfWebElemnt(cell),PatientPageConstants.LIGHT_THEME_ROW_FONT_FAMILY), "Verifying the font family of "+ cell.getText()+" ","Font family of "+ cell.getText()+" rendered as expected");
//					studyPage.verifyEquals(studyPage.getColorOfRows(cell),PatientPageConstants.LIGHT_THEME_ROW_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//				}
//			} 
////			else{
////				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /" + patientPage.rowCellValues.size() +"]","No data is present in the first row of Study list table for column '"+patientPage.getHeaderNameForEmptyData(i)+"'");
////			}
//			i++;
//		}
//	}
//
//	//Resetting theme to default one
//	@AfterClass
//	public void afterClass(){
//		DatabaseMethods db = new DatabaseMethods(driver);
//		db.updateTheme(defaultTheme);
//	}
//
//	private boolean verifyFontFamily(String actual, String expected){
//		return actual.contains(expected);		
//	}
//}
