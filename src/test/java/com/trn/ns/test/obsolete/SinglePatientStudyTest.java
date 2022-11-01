//package com.trn.ns.test.obsolete;
//import java.sql.SQLException;
//import java.text.ParseException;
//
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.WebElement;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.relevantcodes.extentreports.ExtentTest;
//import com.trn.ns.enums.BrowserType;
//import com.trn.ns.page.constants.PatientPageConstants;
//import com.trn.ns.page.constants.PatientXMLConstants;
//import com.trn.ns.page.constants.ViewerPageConstants;
//import com.trn.ns.page.factory.ContentSelector;
//import com.trn.ns.page.factory.DatabaseMethods;
//import com.trn.ns.page.factory.LoginPage;
//
//import com.trn.ns.page.factory.PatientListPage;
//
//import com.trn.ns.page.factory.ViewerPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.DataReader;
//import com.trn.ns.utilities.ExtentManager;
//@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
//public class SinglePatientStudyTest extends TestBase 
//{
//	private LoginPage loginPage;
//	private PatientListPage patientPage;
//	private DatabaseMethods databaseMethod;
//	private ExtentTest extentTest;
//
//	String subject60Filepath = Configurations.TEST_PROPERTIES.get("Subject_60_filepath");
//	String subject60PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, subject60Filepath);
//
//	private String aAA2Filepath = Configurations.TEST_PROPERTIES.get("AAA2_filepath");
//	String aAA2PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, aAA2Filepath);
//	String studyDescription = DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, aAA2Filepath);
//	
//	String liver9Filepath=Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
//	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9Filepath);
//
//	String imbioFilepath =Configurations.TEST_PROPERTIES.get("Imbio_Texture_CTLung_filepath");
//	String imbioPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, imbioFilepath);
//
//	String gspsTextFilepath =Configurations.TEST_PROPERTIES.get("NorthStar^Text^With^AnchorPoint_filepath");
//	String gspsTextPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, gspsTextFilepath);
//
//	String ah4Filepath=Configurations.TEST_PROPERTIES.get("AH.4_filepath");
//	String ah4PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,ah4Filepath);
//	String ah4StudyDescription=DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, PatientXMLConstants.STUDY01_TEXTOVERLAY, ah4Filepath);
//
//	String gspsFilepath=Configurations.TEST_PROPERTIES.get("NorthStar^GSPS^POINTS^MULTISERIES_filepath");
//	String gspsPatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME,gspsFilepath);
//
//
//	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
//	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
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
//	//TC_223:Apply the UI/UX on top of the existing workflow for the study list screen_Firefox
//	//TC_224:Apply the UI/UX on top of the existing workflow for the study list screen_Chrome
//	//TC_211:Apply the UI/UX on top of the existing workflow for the study list screen_IE11
//	//TC_225:Apply the UI/UX on top of the existing workflow for the study list screen_Edge
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test01_US98_TC223_TC224_TC211_TC225_verifyUIUXOnpatientStudyPage() throws InterruptedException 
//	{
//		extentTest = ExtentManager.getTestInstance();
//		
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.clickOnPatientRow(subject60PatientName);
//		
//		if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("firefox") )                  
//			extentTest.setDescription("US98_TC_223 : Apply the UI/UX on top of the existing workflow for the study list screen_Firefox");   
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Chrome") )
//			extentTest.setDescription("US98_TC_224 :Apply the UI/UX on top of the existing workflow for the study list screen_Chrome");   
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("IE11") )
//			extentTest.setDescription("US98_TC_211 :Apply the UI/UX on top of the existing workflow for the study list screen_IE11");   
//		else if(Configurations.TEST_PROPERTIES.get("Browser").equalsIgnoreCase("Edge") )
//			extentTest.setDescription("US98_TC_225 : Apply the UI/UX on top of the existing workflow for the study list screen_Edge");   
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/5]", "Verify font size,font family and font color of Northstar logo , back arrow and patient name");
//
//		//verifying font size,font family and font color of NorthStar logo
//
////		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.title.getCssValue("font-size")),"15px", "verifying the font size of NorthStar logo","font size rendered as expected");
////		patientStudyPage.verifyEquals(patientStudyPage.title.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of NorthStar logo","font family rendered as expected");
////		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.title.getCssValue("color")),"#f2f2ee", "verifying the font color of NorthStar logo","color rendered as expected");
//
//
//		//verifying font size,font family and font color of Sex 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.sex.getCssValue("font-size")),"15px", "verifying the font size of the sex","font size of sex rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.sex.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient sex","font family of patient sex rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.sex.getCssValue("color")),"#5c5653", "verifying the font color of the patient sex","font color of patient sex rendered as expected");
//
//
//		//verifying font size,font family and font color of Sex value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.sexValue.getCssValue("font-size")),"15px", "verifying the font size of the sex value","font size of sex value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.sexValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient sex value","font family of patient sex value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.sexValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient sex value","font color of patient sex value rendered as expected");
//
//
//
//		//verifying font size,font family and font color of dateOfBirth 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.dateOfBirth.getCssValue("font-size")),"15px", "verifying the font size of the dateOfBirth","font size of dateOfBirth rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.dateOfBirth.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient dateOfBirth","font family of patient dateOfBirth rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.dateOfBirth.getCssValue("color")),"#5c5653", "verifying the font color of the patient dateOfBirth","font color of patient dateOfBirth rendered as expected");
//
//		//verifying font size,font family and font color of dateOfBirth value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.dateOfBirthValue.getCssValue("font-size")),"15px", "verifying the font size of the dateOfBirth value","font size of dateOfBirth value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.dateOfBirthValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  dateOfBirth value","font family of  dateOfBirth value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.dateOfBirthValue.getCssValue("color")),"#140b0a", "verifying the font color of the dateOfBirth value","font color of dateOfBirth value rendered as expected");
//
//
//		//verifying font size,font family and font color of age 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.age.getCssValue("font-size")),"15px", "verifying the font size of the age","font size of age rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.age.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient age","font family of patient age rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.age.getCssValue("color")),"#5c5653", "verifying the font color of the patient age","font color of patient age rendered as expected");
//
//		//verifying font size,font family and font color of age value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.ageValue.getCssValue("font-size")),"15px", "verifying the font size of the age value","font size of age value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.ageValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient age value","font family of patient age value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.ageValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient age value","font color of patient age value rendered as expected");
//
//		//verifying font size,font family and font color of currentLocation 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.currentLocation.getCssValue("font-size")),"15px", "verifying the font size of the current location","font size of currentLocation rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.currentLocation.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient current location","font family of patient currentLocation rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.currentLocation.getCssValue("color")),"#5c5653", "verifying the font color of the patient current location","font color of patient currentLocation rendered as expected");
//
//
//		//verifying font size,font family and font color of currentLocation value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.currentLocationValue.getCssValue("font-size")),"15px", "verifying the font size of the current location value","font size of currentLocation value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.currentLocationValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient current location value","font family of patient currentLocation value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.currentLocationValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient current location value","font color of patient currentLocation value rendered as expected");
//
//		//verifying font size,font family and font color of primaryLocation 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.primaryLocation.getCssValue("font-size")),"15px", "verifying the font size of the primary location","font size of primaryLocation rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.primaryLocation.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient primary location","font family of patient primaryLocation rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.primaryLocation.getCssValue("color")),"#5c5653", "verifying the font color of the patient primary location","font color of patient primaryLocation rendered as expected");
//
//
//		//verifying font size,font family and font color of primaryLocation value 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.primaryLocationValue.getCssValue("font-size")),"15px", "verifying the font size of the primary location value","font size of primaryLocation value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.primaryLocationValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient primary location value","font family of patient primaryLocation value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.primaryLocationValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient primary location value","font color of patient primaryLocation value rendered as expected");
//
//		//verifying font size,font family and font color of site 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.site.getCssValue("font-size")),"15px", "verifying the font size of the site","font size of site rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.site.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient site","font family of patient site rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.site.getCssValue("color")),"#5c5653", "verifying the font color of the patient site","font color of patient site rendered as expected");
//
//		//verifying font size,font family and font color of site value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.siteValue.getCssValue("font-size")),"15px", "verifying the font size of the site value","font size of site value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.siteValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient site value","font family of patient site value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.siteValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient site value","font color of patient site value rendered as expected");
//
//
//		//verifying font size,font family and font color of Class 
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.Class.getCssValue("font-size")),"15px", "verifying the font size of the class","font size of class rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.Class.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient Class","font family of patient Class rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.Class.getCssValue("color")),"#5c5653", "verifying the font color of the patient Class","font color of patient Class rendered as expected");
//
//
//		//verifying font size,font family and font color of Class value
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.classValue.getCssValue("font-size")),"15px", "verifying the font size of the class value","font size of class value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.classValue.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the patient Class value","font family of patient Class value rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.classValue.getCssValue("color")),"#140b0a", "verifying the font color of the patient Class value","font color of patient Class value rendered as expected");
//
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/5]", "Verify font size,font family and font color of patient study list table header");
//
//
//		//verifying font size,font family and font color of table header accession  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.accession.getCssValue("font-size")),"10px", "verifying the font size of the accession","font size of accession rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.accession.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  accession","font family of patient accession rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.accession.getCssValue("color")),"#5c5653", "verifying the font color of the accession","font color of patient accession rendered as expected");
//
//		//verifying font size,font family and font color of table header studyDescription  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.studyDescription.getCssValue("font-size")),"10px", "verifying the font size of the study description","font size of studyDescription rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.studyDescription.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  study description","font family of  studyDescription rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.studyDescription.getCssValue("color")),"#5c5653", "verifying the font color of the study description","font color of studyDescription rendered as expected");
//
//
//		//verifying font size,font family and font color of table header wiaCloud  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.EnvoyAl.getCssValue("font-size")),"10px", "verifying the font size of the wiaCloud","font size of wiaCloud rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.EnvoyAl.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  wiaCloud","font family of wiaCloud rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.EnvoyAl.getCssValue("color")),"#5c5653", "verifying the font color of the wiaCloud","font color of  wiaCloud rendered as expected");
//
//		//verifying font size,font family and font color of table header images  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.images.getCssValue("font-size")),"10px", "verifying the font size of the images","font size of images rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.images.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  images","font family of patient images rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.images.getCssValue("color")),"#5c5653", "verifying the font color of the images","font color of patient images rendered as expected");
//
//		//verifying font size,font family and font color of table header modality  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.modality.getCssValue("font-size")),"10px", "verifying the font size of the modality","font size of modality rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.modality.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the modality","font family of modality rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.modality.getCssValue("color")),"#5c5653", "verifying the font color of the modality","font color of  modality rendered as expected");
//
//		//verifying font size,font family and font color of table header studyDateTime  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.studyDateTime.getCssValue("font-size")),"10px", "verifying the font size of the studyDateTime","font size of studyDateTime rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.studyDateTime.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  studyDateTime","font family of studyDateTime");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.studyDateTime.getCssValue("color")),"#5c5653", "verifying the font color of the studyDateTime","font color of  studyDateTime");
//
//		//verifying font size,font family and font color of table header referringPhysician  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.referringPhysician.getCssValue("font-size")),"10px", "verifying the font size of the referringPhysician","font size of referringPhysician rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.referringPhysician.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the referringPhysician","font family of referringPhysician rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.referringPhysician.getCssValue("color")),"#5c5653", "verifying the font color of the referringPhysician","font color of  referringPhysician rendered as expected");
//
//		//verifying font size,font family and font color of table header institutionName  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.institutionName.getCssValue("font-size")),"10px", "verifying the font size of the institutionName","font size of institutionName rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.institutionName.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  institutionName","font family of institutionName rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.institutionName.getCssValue("color")),"#5c5653", "verifying the font color of the institutionName","font color of institutionName rendered as expected");
//
//		//verifying font size,font family and font color of table header priority  
//
//		patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(patientStudyPage.priority.getCssValue("font-size")),"10px", "verifying the font size of the priority","font size of priority rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.priority.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Regular", "verifying the font family of the  priority","font family of priority rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(patientStudyPage.priority.getCssValue("color")),"#5c5653", "verifying the font color of the priority","font color of  priority rendered as expected");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[4/5]", "Verify font size,font family and font color of study list column values");
//
//		//verifying size,font family and font color of column values
//
//		for(WebElement column : patientStudyPage.rowCellValues)
//		{ 	
//			patientStudyPage.verifyEquals(patientStudyPage.formatDecimalNumber(column.getCssValue("font-size")),"15px", "verifying the font size of column value","font size of the column value rendered as expected");
//			patientStudyPage.verifyEquals(column.getCssValue("fontFamily").replaceAll("^\"|\"$", ""),"Source Sans Pro Light", "verifying the font family of the column value","font family of the column value rendered as expected");
//			patientStudyPage.verifyEquals(patientStudyPage.getHexColorValue(column.getCssValue("color")),"#140b0a", "verifying the font color of the column value","font color of the column value rendered as expected");
//
//
//		}
//
//		// Verifying the correct image is getting displayed for ascending/descending sort icon. 
//
//		int i =1;
//		String colHeader ="";
//		for (WebElement header : patientStudyPage.columnHeaders) {
//
//			colHeader=header.getText();	
//			if(!colHeader.equals(patientStudyPage.accession.getText()))
//				header.click();
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[ 5."+i+" / 5."+patientStudyPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : ascending sort");
//			patientStudyPage.verifyEquals(patientStudyPage.sortedAscColumnHeader.getText(), colHeader,"Verify Up arrow icon of column headers " , "Rendering of Up Arrow icon of column headers ");
//			//patientStudyPage.compareElementImage(protocolName, patientStudyPage.upArrow, "Verify up arrow image of column headers", header.getText()+"-upArrow", new int[]{(int) patientStudyPage.getImageCoordinate(patientStudyPage.upArrowIcon).get("X"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.upArrowIcon).get("Y"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.upArrowIcon).get("Width"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.upArrowIcon).get("Height")},new Boolean(false));
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[5."+i+" / 5."+patientStudyPage.columnHeaders.size()+"]", "Clicking on the column "+header.getText()+" : descending sort");
//			header.click();
//			patientStudyPage.verifyEquals(patientStudyPage.sortedDscColumnHeader.getText(),colHeader,"Verify Down arrow icon of column headers " , "Rendering of Down Arrow icon of column headers-PASS ");
//			//patientStudyPage.compareElementImage(protocolName, patientStudyPage.downArrow, "Verify down arrow image of of column headers", header.getText()+"-dwnArrow", new int[]{(int) patientStudyPage.getImageCoordinate(patientStudyPage.downArrowIcon).get("X"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.downArrowIcon).get("Y"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.downArrowIcon).get("Width"), (int) patientStudyPage.getImageCoordinate(patientStudyPage.downArrowIcon).get("Height")},new Boolean(false));
//			i++;
//		}
//	}
//
//	//US31_TC_6: Sorting the list of patient
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test02_US31_TC6_verifySortingAndTooltips() throws InterruptedException, ParseException
//	{
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.clickOnPatientRow(subject60PatientName);
//		//US31_TC_6: Sorting the list of patient
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Sorting the list of patient");
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//
//		for (int i =0 ;i< patientStudyPage.columnHeaders.size();i++){
//
//			//Verifying tooltips on sorted columns
//			patientStudyPage.performAscendingSort(i);
//			patientStudyPage.assertEquals(patientStudyPage.getTooltipColHeader(i), patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i))+ "," + " ascending order", "Verify tooltip on "+ patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i))+" column when it is sorted in ascedning order..","Verified tooltip on "+ patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i)) +" column when it is sorted in ascedning order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".a /" + patientStudyPage.columnHeaders.size() +"]", "Verify tooltip on "+ patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i))+" column when it is sorted in ascedning order.");
//			// After sorting in ascending order by clicking on column header
//			patientStudyPage.assertEquals(patientStudyPage.fetchColumn(i),patientStudyPage.ascSortingCol(i), "Verify column is sorted in ascending order","Verified column is sorted in ascending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".b /" + patientStudyPage.columnHeaders.size() +"]", "Verify column is sorted in ascending order.");				
//			patientStudyPage.performDescendingSort(i);
//			patientStudyPage.assertEquals(patientStudyPage.getTooltipColHeader(i), patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i))+ "," + " descending order", "Verify tooltip on column when it is sorted in descedning order.","Verified tooltip on column when it is sorted in descedning order..");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".c /" + patientStudyPage.columnHeaders.size() +"]", "Verify tooltip on column when it is sorted in descedning order..");	
//			//Descending sort
//			patientStudyPage.assertEquals(patientStudyPage.fetchColumn(i),patientStudyPage.dscSortingCol(i), "Verify column is sorted in descending order", "Verfied column is sorted in descending order.");
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+".d /" + patientStudyPage.columnHeaders.size() +"]", "Verify column is sorted in descending order.");
//		}
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test03_DE234_TC1138_verifyWrapOfDataOnColumnHeader() throws InterruptedException 
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify Column Headers and data in the column on Single Patient screen");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.clickOnPatientRow(subject60PatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		for (int i =0 ;i< patientStudyPage.columnHeadersTextOverFlow.size();i++)
//		{
//			patientStudyPage.assertTrue(patientStudyPage.verifyTextOverFlowForDataWraping(patientStudyPage.columnHeadersTextOverFlow.get(i)), "Verify Ellipsis are enable on Column Header", "The Ellipsis are enable on Column Header"); 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i+1)+"/" + patientStudyPage.columnHeadersTextOverFlow.size() +"]", "Verify Ellipsis on "+ patientStudyPage.getText("visibility",patientStudyPage.columnHeaders.get(i))+" column");
//
//		}    
//	}
//
//	@Test(groups ={"IE11","Chrome","firefox", "Chrome"})
//	public void test04_DE83_TC990_colorOfAlternateRowsOfSinglePatientList () throws InterruptedException 
//	{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Check the color in alternate rows and table alignment on Single Patient List screen.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.clickOnPatientRow(subject60PatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/1]", "Verify that the alternate Background color of the row of Single Patient study page is as per required");
//
//		patientStudyPage.assertEquals(patientStudyPage.getBackgroundColorOfSingleStudyList(1),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//		patientStudyPage.assertEquals(patientStudyPage.getBackgroundColorOfSingleStudyList(2),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required ");
//
//	}
//
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test05_DE88_TC801_verifySortingArrowOnBrowserResize() throws TimeoutException, InterruptedException
//	{   
//		patientPage = new PatientListPage(driver);
//		patientPage.waitForElementVisibility(patientPage.patientListTable);
//		patientPage.clickOnPatientRow(subject60PatientName);
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify on single patient study list page sort icon(up arrow or down arrow) is displayed beside the column name when browser window resized.");
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		patientStudyPage.resizeBrowserWindow(700,600);
//		patientPage.mouseHover(patientPage.patientNameList.get(0));
//		int i=1;
//		for (WebElement header : patientStudyPage.columnHeaders) {
//			if(!(header.getText().equals(patientStudyPage.accession.getText()) && (patientStudyPage.upSortArrow.getAttribute("class")).equalsIgnoreCase("arrow-up")))
//				patientStudyPage.click(header);
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+(i)+"/" + patientStudyPage.columnHeaders.size() +"]", "Verify up arrow is displayed beside the column name- " + header.getText()+ " on browser resize.");
//			patientStudyPage.assertTrue((patientStudyPage.getImageCoordinate(patientStudyPage.upSortArrow).get("X") + patientStudyPage.getImageCoordinate(patientStudyPage.upSortArrow).get("Width")) <= ((patientStudyPage.getImageCoordinate(patientStudyPage.sortedAscColumnHeader).get("X") + patientStudyPage.getImageCoordinate(patientStudyPage.sortedAscColumnHeader).get("Width")+1)),"Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header","Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of text inside column header plus width of text inside column header");
//			patientStudyPage.assertTrue( (patientStudyPage.getImageCoordinate(patientStudyPage.upSortArrow).get("X")+1) >= (patientStudyPage.getImageCoordinate(patientStudyPage.columnHeaderText).get("X") + patientStudyPage.getImageCoordinate(patientStudyPage.columnHeaderText).get("Width")), "Verify x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header", "Verified x co-ordinate of the up arrow should be greater than or equal to x co-ordinate of column header plus width of column header");
//			patientStudyPage.assertTrue( (patientStudyPage.getImageCoordinate(patientStudyPage.upSortArrow).get("Y") + patientStudyPage.getImageCoordinate(patientStudyPage.upSortArrow).get("Height") ) < (patientStudyPage.getImageCoordinate(patientStudyPage.columnHeaderText).get("Y") + patientStudyPage.getImageCoordinate(patientStudyPage.columnHeaderText).get("Height")), "Verify y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header", "Verified y co-ordinate plus height of the up arrow should be less than y co-ordinate of text inside column header plus height of text inside column header");
//			i++;
//		}
//	}
//
//	//TC346 -   Verify that there is no page scroll present on patient study page
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test06_DE68_TC346_verifyVerticalScrollbarForLessStudies() throws TimeoutException, InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that there is no page scroll present on patient study page");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aAA2PatientName+"in viewer" );
//		patientPage = new PatientListPage(driver);
//
//		//Set dimension to resize the window
//		Dimension dimension = new Dimension(800, 600);
//		String parentWindow = driver.getWindowHandle();
//
//		patientPage.setWindowSize(parentWindow,dimension);
//		patientPage.waitForPatientPageToLoad();
//		patientPage.mouseHover(patientPage.patientNameList.get(0));
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verifying scroll for more studies present on Patient list page" );
//		patientPage.assertTrue(patientPage.verticalScrollbar.isDisplayed(), "Verify scroll presence for more study on Patient list screen", "Scroll is present");	
//	
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verifying scroll for more studies present on Patient list page with low resolution" );
//		patientPage.assertTrue(patientPage.verticalScrollbar.isDisplayed(),"Verify scroll presence for more study on Patient list screen on low resolution", "Scroll is present");
//		//Set window size to maximize
//		patientPage.maximizeWindow();
//		patientPage.waitForPatientPageToLoad();
//		patientPage.clickOnPatientRow(aAA2PatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verifying scroll for less studies present on Patient Study page" );
//		patientStudyPage.assertFalse(patientStudyPage.verifyPresenceOfVerticalScrollbar(patientStudyPage.patientStudyDataTable), "Verify scroll presence for less study on Patient Study screen", "Scroll is not present");
//
//	}
//
//	//TC344 - 	Verify that table scroll is displayed when there are more studies present for patient
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
//	public void test07_DE68_TC344_verifyVerticalScrollbarForMoreStudies() throws InterruptedException {
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that table scroll is displayed when there are more studies present for patient Study page");
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aAA2PatientName+"in viewer" );
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(aAA2PatientName);
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//
//		//Add extra studies in database to verify the vertical scroll bar
//		databaseMethod = new DatabaseMethods(driver);
//		databaseMethod.addStudiesInDB(aAA2PatientName, studyDescription);
//
//		if(!Configurations.TEST_PROPERTIES.get("browserName").equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) {
//			//Moving back to refresh the Patient study page
//			patientStudyPage.browserBackWebPage();
//			patientPage.waitForPatientPageToLoad();
//			patientPage.clickOnPatientRow(aAA2PatientName);
//
//			patientStudyPage.waitForSingleStudyToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying scroll for more studies present on Patient Study page" );
//			patientStudyPage.mouseHover(patientPage.verticalScrollbar);
//			patientStudyPage.assertTrue(patientPage.verticalScrollbar.isDisplayed(), "Verify scroll presence for more study on Patient Study screen", "Scrollbar is present");
//
//			//Set dimension to resize the window
//			Dimension dimension = new Dimension(800, 600);
//			String parentWindow = driver.getWindowHandle();
//
//			patientStudyPage.setWindowSize(parentWindow,dimension);
//			patientStudyPage.waitForSingleStudyToLoad();
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying scrollbar for more studies present on Patient list page with low resolution" );
//			patientStudyPage.mouseHover(patientPage.verticalScrollbar);
//			patientStudyPage.assertTrue(patientPage.verticalScrollbar.isDisplayed(),"Verify scrollbar presence for more study on Patient list screen on low resolution", "Scrollbar is present");
//			//Set window size to maximize
//			patientStudyPage.maximizeWindow();
//			patientStudyPage.waitForSingleStudyToLoad();
//
//			//Remove added studies
//			databaseMethod.removeAddedStudiesInDB(aAA2PatientName, studyDescription);
//		}
//	}
//
//	//Applicable only for IE as browser is not getting refresh after addition of new studies from test02
//	//Prerequisite - Run test02_DE68_TC343_verifyVerticalScrollbarForMoreStudies before running this test case
//	@Test(groups ={"IE11","dbConfig"},dependsOnMethods = "test07_DE68_TC344_verifyVerticalScrollbarForMoreStudies")
//	public void test08_DE68_TC343_verifyVerticalScrollbarForMoreStudiesForIE() throws InterruptedException {
//
//		if (Configurations.TEST_PROPERTIES.get("browserName").equalsIgnoreCase(BrowserType.INTERNETEXPLORER.getBrowserValue())) {
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("Verify that table scroll is displayed when there are more studies present for patient only for IE browser");
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Loading the Patient "+aAA2PatientName+"in viewer" );
//
//			patientPage = new PatientListPage(driver);
//			patientPage.clickOnPatientRow(aAA2PatientName);
//
//			patientStudyPage = new SinglePatientStudyPage(driver);
//
//			//Set dimension to resize the window
//			Dimension dimension = new Dimension(800, 600);
//			String parentWindow = driver.getWindowHandle();
//
//			patientStudyPage.setWindowSize(parentWindow,dimension);
//			patientStudyPage.waitForSingleStudyToLoad();
//			patientPage.mouseHover(patientPage.patientNameList.get(0));
//			
//			//Verify the vertical scroll bar
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verifying scroll for more studies present on Patient Study page" );
//			patientStudyPage.assertTrue(patientStudyPage.verifyPresenceOfVerticalScrollbar(patientStudyPage.patientStudyDataTable), "Verify scroll presence for more study on Patient Study screen", "Scroll is present");
//
//			
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verifying scrollbar for more studies present on Patient list page with low resolution" );
//			patientStudyPage.assertTrue(patientPage.verifyPresenceOfVerticalScrollbar(patientPage.patientListDataTable),"Verify scrollbar presence for more study on Patient list screen on low resolution", "Scrollbar is present");
//			//Set window size to maximize
//			patientStudyPage.maximizeWindow();
//			patientStudyPage.waitForSingleStudyToLoad();
//
//			//Remove added studies from database
//			databaseMethod.removeAddedStudiesInDB(aAA2PatientName, studyDescription);
//		}
//	}
//
//	//TC1250 : Verify dark theme BG color Study list page.
//	@Test(groups ={"firefox","Chrome","Edge","IE11"})
//	public void test09_US367_TC1250_verifyUISpecAsPerDarkTheme() throws TimeoutException, InterruptedException{
//
//
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify dark theme BG color Study list page");
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//
//		int i =1;
//		//Verifying the color of headers on single patient page screen
//		for (WebElement header : patientStudyPage.columnHeaders) {		 
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".a /"+patientStudyPage.columnHeaders.size()+"]", "Verifying font color of"+header.getText()+"");
//			patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(header),PatientPageConstants.DARK_THEME_HEADER_FONT_COLOR, "Verifying the font color of "+header.getText()+"","font color of "+header.getText()+" rendered as expected");
//			i++;
//		}
//
//		i = 1;
//		//Verifying the color of labels on single patient page screen
//		for(WebElement label : patientStudyPage.studyListTableLabels){
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".b /"+patientStudyPage.studyListTableLabels.size()+"]", "Verifying font color of"+label.getText()+"");
//			patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(label),PatientPageConstants.DARK_THEME_LABEL_COLOR, "Verifying the font color of "+ label.getText()+"","Font color of "+ label.getText()+" rendered as expected");
//			i++;
//
//		}
//
//		i = 1;
//		//Verifying the color of values on single patient page screen
//		for(WebElement value : patientStudyPage.studyListTableValues){	
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".c /"+patientStudyPage.studyListTableValues.size()+"]", "Verifying font color of"+value.getText()+"");
//			patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(value),PatientPageConstants.DARK_THEME_ROW_FONT_COLOR, "Verifying the font color of "+ value.getText()+"","Font color of "+ value.getText()+" rendered as expected");
//			i++;
//		}
//
//		//Verifying the color of alternate rows
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/3]", "Verify that the alternate Background color of the row of Single Patient List page is as per required");
//		patientStudyPage.assertEquals(patientStudyPage.getBackgroundColorOfSingleStudyList(1),PatientPageConstants.ODD_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//		patientStudyPage.assertEquals(patientStudyPage.getBackgroundColorOfSingleStudyList(2),PatientPageConstants.EVEN_ROWS_BACKGROUND_COLOR, "Verifying Background color", "Background color is as per required  ");
//
//		//Verifying font color of first row
//		i = 1;
//		for(WebElement cell : patientStudyPage.rowCellValues)
//		{ 	
//
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint["+i+".d /" + patientStudyPage.rowCellValues.size() +"]", "Verify font color and background color of "+cell.getText()+" present in the first row of patient list table");
//			patientStudyPage.verifyEquals(patientStudyPage.getBackgroundColorOfRows(cell),PatientPageConstants.DARK_THEME_BACKGROUND_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//
//			if(patientStudyPage.getAttributeValue(cell,"class").equalsIgnoreCase(PatientPageConstants.HIGHLITHED_COLUMN_CLASS))
//				patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_HIGHLIGHTED_COLUMN_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//			else
//				patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(cell),PatientPageConstants.DARK_THEME_ROW_FONT_COLOR, "Verifying the font color of "+ cell.getText()+"","Font color of "+ cell.getText()+" rendered as expected");
//			i++;
//		} 
//		patientStudyPage.mouseHover(patientStudyPage.studyDescriptionList.get(0));
//		//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/3]", "Verify that background color and border color of the highlighted row");
//		patientStudyPage.verifyEquals(patientStudyPage.getBackgroundColorOfSingleStudyList(1),PatientPageConstants.DARK_THEME_HIGHLIGHTED_ROW_COLOR,"Verifying background color of highlighted row","Background color of "+patientStudyPage.patientNameList.get(0)+" rendered as expected");
//		patientStudyPage.verifyEquals(patientStudyPage.getBorderColorOfWebElemnt(patientStudyPage.getXpathOfRowsOfSingleStudyList(1)),PatientPageConstants.DARK_THEME_BORDER_ROW_COLOR,"Verifying border color of row","Border color of row is rendered as expected");
//
//		//Verify header text and version color
////		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[3/3]", "Verify that color of the Northstar text and version");
////		patientStudyPage.verifyEquals(patientStudyPage.getColorOfRows(patientStudyPage.headerTextAndVersion),PatientPageConstants.DARK_THEME_HEADER_TEXTANDVERSION_COLOR, "Verifying the font color of "+ patientStudyPage.headerTextAndVersion.getText()+"","Font color of "+  patientStudyPage.headerTextAndVersion.getText()+" rendered as expected");
//	}
//
//	//TC1253 : Verify that Default theme should be "dark" for patient and study list page
//	@Test(groups ={"firefox","Chrome","Edge","IE11","dbConfig"})
//	public void test10_US367_TC1253_verifyDefaultTheme(){
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Updating the database with light theme");
//		
//		patientPage = new PatientListPage(driver) ;
//		DatabaseMethods db = new DatabaseMethods(driver);
//		//Verify default theme should be dark
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify the default theme on Patient list screen");
//		patientPage.assertEquals(db.getTheme().trim(), PatientPageConstants.DARK_THEME, "Verifying by default theme on Patient list screen", "Default theme is "+db.getTheme()+" rendering on Patient list screen");
//
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify the default theme on Single Patient list screen");
//		patientStudyPage.assertEquals(db.getTheme().trim(), PatientPageConstants.DARK_THEME, "Verifying by default theme on Single Patient list screen", "Default theme is "+db.getTheme()+" rendering on Single Patinet list screen");
//	}
//	@Test(groups ={"Chrome","US1195"})
//	public void test11_US1195_TC6178_verifyPatientDesc()
//	{                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the Patient description details on Study List page.");
//		patientPage = new PatientListPage(driver) ;
//		patientPage.waitForPatientPageToLoad();
//        patientPage.clickOnPatientRow(1);
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[1/2]", "Verify study list table headers");
//		patientStudyPage.assertTrue(patientStudyPage.convertWebElementToStringList(patientStudyPage.columnHeaders).equals(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS),"Verifying study list table headers","Study list table headers are displayed");
//
//		ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "Checkpoint[2/2]", "Verify patient description on study list page");
//		
//		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.dateOfBirth), "Verifying 'Date of Birth' is displayed", "Date of Birth is displayed");		
//		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.age), "Verifying 'Age' is displayed", "Age is displayed");		
//		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.site), "Verifying 'Site' is displayed", "Site is displayed");		
//		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.sex), "Verifying 'Sex' is displayed", "Sex is displayed");
//		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.currentLocation), "Verifying Current Location is NOT displayed", "Current Location is NOT displayed");
//		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.primaryLocation), "Verifying Primary Location is NOT displayed", "Primary Location is NOT displayed");
//		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.Class), "Verifying Class is NOT displayed", "Class is NOT displayed");
//		patientStudyPage.assertFalse(patientStudyPage.isElementPresent(patientStudyPage.race), "Verifying  Race is NOT displayed", "Race is NOT displayed");
//
//	}
//
//	//US1986:Update study list Envoy AI column and icon
//	@Test(groups ={"Chrome","Edge","IE11", "US1986", "Positive"})
//	public void test12_US1986_TC8822_verifyEurekaIconOnPatientListPage() throws InterruptedException
//	  {                      
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify the new 'Eureka AI' for 'Envoy AI' icon on 'PatientStudyList' page.");
//
//		patientPage = new PatientListPage(driver);
//		patientPage.clickOnPatientRow(imbioPatientName);
//			
//		patientStudyPage = new SinglePatientStudyPage(driver);
//		patientStudyPage.waitForSingleStudyToLoad();
//		
//		patientStudyPage.clickOntheFirstStudy();
//			
//		ViewerPage viewerpage=new ViewerPage(driver);
//		viewerpage.waitForViewerpageToLoad();
//		
//		ContentSelector cs=new ContentSelector(driver);
//		String machineName=cs.getText(cs.allContentSelectorMachine.get(0));
//		
//		viewerpage.navigateBackToStudyListPage();
//		patientStudyPage.waitForSingleStudyToLoad();
//		
//		patientStudyPage.mouseHover(patientStudyPage.eurekaAI);
//		patientStudyPage.assertEquals(patientStudyPage.getText(patientStudyPage.eurekaAI), PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(2), "Checkpoint[1/5]", "Verified Eureka AI header tooltip on study list page.");
//		
//		patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.eurekaIcon), "Checkpoint[2/5]", "Verified that Eureka Icon is visible on Study list.");
//		patientStudyPage.mouseHover("presence",patientStudyPage.getEnvoyAlCell(1)); 
//		String str = patientStudyPage.getEurekaAITooltip();
//		
//		patientStudyPage.assertTrue(str.contains(ViewerPageConstants.RESULT_STATUS), "Checkpoint[3/5]", "Verified result status on mouse hover on Eureka AI Icon.");
//		patientStudyPage.assertTrue(str.contains(machineName), "Checkpoint[4/5]", "Verified machine name on mouse hover on Eureka AI Icon.");
//		
//		patientStudyPage.performMouseRightClick(patientStudyPage.columnHeadersTextOverFlow.get(3));		
//		patientPage.assertFalse(patientPage.verifyCheckboxIsEnabled(PatientPageConstants.STUDY_PAGE_COLUMN_HEADERS.get(2)),"Checkpoin[5/5]","Verified Eureka AI column is visible on column header.");
//	    
//		}
//
//	@Test(groups ={"Chrome","Edge","IE11","Positive","US1986"})
//	public void test05_US1986_TC8838_verifyEurekaIconOnStudyListPane() throws InterruptedException, SQLException
//		{	 
//			extentTest = ExtentManager.getTestInstance();
//			extentTest.setDescription("[DR2211] Verify Eureka AI icon is displayed on PatientStudyList and StudyList page.");	
//
//			patientPage = new PatientListPage(driver);
//			
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Eureka Icon and number of results on study page for "+gspsTextPatientName);
//			patientPage.clickOnPatientRow(gspsTextPatientName);
//			patientStudyPage = new SinglePatientStudyPage(driver);
//			patientPage.assertTrue(patientPage.isElementPresent(patientStudyPage.eurekaIcon),"Checkpoint[1/6]","Verified Eureka icon on study list page.");
//			patientPage.assertEquals(patientPage.convertIntoInt(patientPage.getText(patientStudyPage.numberOfResults)),2,"Checkpoint[2/6]","Verified number of results displayed on Eureka icon");
//		
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Eureka Icon on study page for "+gspsPatientName);
//			patientStudyPage.navigateToBack();
//			patientPage.waitForPatientPageToLoad();
//			patientPage.clickOnPatientRow(gspsPatientName);
//			patientStudyPage.waitForSingleStudyToLoad();
//			patientPage.assertTrue(patientPage.isElementPresent(patientStudyPage.eurekaIcon),"Checkpoint[3/6]","Verified Eureka icon on study list page.");
//			
//			//pending icon
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Pending Icon on study page for "+ah4PatientName);
//			patientStudyPage.navigateToBack();
//			patientPage.waitForPatientPageToLoad();
//			patientPage.clickOnPatientRow(ah4PatientName);
//			patientStudyPage.waitForSingleStudyToLoad();
//			patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.pendingIcon),"Checkpoint[4/6]","Verified Pending icon on study list page.");
//		
//			//warning icon
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Warning Icon on study page for "+ah4PatientName);
//			DatabaseMethods	db=new DatabaseMethods(driver);
//			db.updateWiaCloudResultInStudyTable(ah4StudyDescription, 2);
//			loginPage.logout();
//			loginPage.login(username,password);
//			patientPage.waitForPatientPageToLoad();
//			patientPage.clickOnPatientRow(ah4PatientName);
//			patientStudyPage.waitForSingleStudyToLoad();
//			patientStudyPage.assertTrue(patientStudyPage.isElementPresent(patientStudyPage.warningIcon),"Checkpoint[5/6]","Verified warning icon on study list page.");
//			
//			//disable warning icon
//			ExtentManager.customExtentReportLog(Configurations.INFO, extentTest, "", "Verify Disable Eureka Icon on study page for "+ah4PatientName);
//			db.updateWiaCloudResultInStudyTable(ah4StudyDescription, 4);
//			loginPage.logout();
//			loginPage.login(username, password);
//			patientPage.waitForPatientPageToLoad();
//			patientPage.clickOnPatientRow(ah4PatientName);
//			patientStudyPage.waitForSingleStudyToLoad();
//			patientPage.assertTrue(patientPage.isElementPresent(patientStudyPage.disableEurekaIcon),"Checkpoint[6/6]","Verified disable Eureka icon on study list page.");
//	}
//	
//
//	@AfterMethod(alwaysRun=true)
//	public void afterMethod() throws SQLException{
//
//		db=new DatabaseMethods(driver);	
//		db.updateWiaCloudResultInStudyTable(ah4StudyDescription, 1);
//	}	
//
//}
