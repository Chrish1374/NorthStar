package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.Utilities;


public class ViewerTextOverlays extends ViewBoxToolPanel {
	private WebDriver driver;
	public ViewerTextOverlays(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	public By lossyAnnotationViewbox1 = By.id("Lossy-0");
	public By lossyAnnotationViewbox2 = By.id("Lossy-1");

	public By overlayContainer = By.cssSelector("div.ns-annotationlevelselectorcontainer");
	public By textoverlayIcon = By.id("textOverlayIcon");
	public By customMenu =By.cssSelector("div.menuBody");
	public By closeArrow =By.cssSelector("div.closeArrow");

	@FindBy(css="#viewer > ns-tool-bar > div > div.tool-bar.ng-star-inserted > div > div:nth-child(1) > div > span:nth-child(3)")
	public WebElement textOverlayIcon;

	@FindBy(id="default")
	public WebElement defaultTextOverlay; 

	@FindBy(id="full")
	public WebElement fullTextOverlay; 

	@FindBy(id="minimum")
	public WebElement minimumTextOverlay;

	@FindBy(id="custom")
	public WebElement customTextOverlay;

	@FindBys({@FindBy(css = ".list-group.row .ns-toggle-icon .defaultIcon")})
	public List<WebElement> toggleButton;

	@FindBy(css="div.ns-annotationlevelselectorcontainer")
	public WebElement textOverlayPopup;

	@FindBys(@FindBy(css="div.ns-annotationlevelselectorcontainer input"))
	public List<WebElement> textOverlayRadioButton;

	@FindBys(@FindBy(css="div.ns-annotationlevelselectorcontainer label"))
	public List<WebElement> textOverlayLabels;

	@FindBy(css="div.closeArrow path[d^='M.925']")
	public WebElement openCustomMenuOptionArrow;

	@FindBy(css="div.closeArrow path[d^='M4.407']")
	public WebElement closeCustomMenuOptionArrow;

	@FindBys(@FindBy(css="span.slider.round"))
	public List<WebElement> customOverlayOptionsSlider;

	@FindBys(@FindBy(css="div.text-content"))
	public List<WebElement> customOverlayOptions;

	@FindBy(css= "#viewer ns-textoverlaymenu perfect-scrollbar  div.ps__rail-y > div")
	public WebElement scrollThumb;

	@FindBy(css= "#viewer ns-textoverlaymenu perfect-scrollbar  div.ps__rail-y")
	public WebElement scrollBar;


	public boolean verifyTextOverlayIsHighlighted(WebElement textOverlay) throws InterruptedException
	{
		boolean status = false;
		openTextOverlayOptions();
		try{
			if(isElementSelected(textOverlay))
				status= true;

		}catch(NoSuchElementException e){
			status= false;
		}
		closeTextOverlayOptions();
		return status;
	}

	public boolean verifyTextOverlayDetail(int viewboxCount, String textOverlay)
	{
		boolean status=false;

		switch(textOverlay){
		case ViewerPageConstants.DEFAULT_ANNOTATION:
			for(int i=1;i<=viewboxCount;i++)
			{
				if (isElementPresent(getSeriesDescriptionOverlay(i)))
					status= isElementPresent(getSeriesDescriptionOverlay(i));
				else 
					status= isElementPresent(getPatientNameOverlay(i));
			}
			break;

		case ViewerPageConstants.MINIMUM_ANNOTATION:
		{
			for(int i=1;i<=viewboxCount;i++){
				if (isElementPresent(getSeriesDescriptionOverlay(i)))
					status=isElementPresent(getSeriesDescriptionOverlay(i));
				else
					status=isElementPresent(getPatientIDOverlay(i));
			}
		}  
		break;

		case ViewerPageConstants.FULL_ANNOTATION:
		{	
			for(int i=1;i<=viewboxCount;i++){
				if (isElementPresent(getSeriesDescriptionOverlay(i)))
					status=isElementPresent(getSeriesDescriptionOverlay(i));
				else
					status=isElementPresent(getImageMatrixOverlay(i));
			}

		}
		break;
		}
		return status;

	}    


	public Boolean isToolTipPresentForTextOveraly(WebElement element) 
	{   
		boolean status=false;
		LOGGER.info(Utilities.getCurrentThreadId()
				+ "check for presence of child title element inside text overlay");
		try{
			//check for presence of title element for text overlay
			WebElement temp = element.findElement(By.cssSelector("title"));
			String text = (String) ((JavascriptExecutor)driver).executeScript("return arguments[0].innerHTML;",temp);
			LOGGER.info(Utilities.getCurrentThreadId()+text);
			if (!text.isEmpty() && element.getText().contains(ViewerPageConstants.ELLIPSES))
				status = true;	
		}catch(NoSuchElementException e){
			status=false;
		}
		return status;
	}

	public void selectTextOverlays(String annoatationLevel) throws InterruptedException{
		clickUsingAction(textOverlayIcon);
		WebElement levelOfAnnotation = driver.findElement(By.id(annoatationLevel));
		waitForElementsVisibility(overlayContainer);
		click(levelOfAnnotation);
		waitForTimePeriod(1000);
	}

	public void openCustomOverlay() throws InterruptedException{
		clickUsingAction(textOverlayIcon);
		waitForElementsVisibility(overlayContainer);
		click(openCustomMenuOptionArrow);
		waitForElementsVisibility(customMenu);
	}

	public void closeCustomOverlay() throws InterruptedException{
		
		click(getElement(By.cssSelector("#viewer .closeArrow")));
	}

	public List<String> getTextOverlaysOptions() throws InterruptedException{
		clickUsingAction(textOverlayIcon);
		return convertWebElementToStringList(getElements(By.cssSelector(".ns-annotationlevelselectorcontainer label")));

	}


	public void clickTextOverlayIcon(){
		clickUsingAction(textOverlayIcon);
	}

	public void openTextOverlayOptions() throws InterruptedException{
		clickTextOverlayIcon();		
		waitForTimePeriod(1000);
	}

	public void closeTextOverlayOptions() throws InterruptedException{		
		clickTextOverlayIcon();	
	}
	//Adding wait for element to display
	public void waitForMinimumOverlayDisplay(int viewNum){
		waitForElementVisibility(getPatientIDOverlay(viewNum));
		waitForElementVisibility(getStudyDateTimeOverlay(viewNum));
		waitForElementVisibility(getZoomOverlay(viewNum));		
	}

	public void waitForDefaultOverlayDisplay(int viewNum){
		waitForMinimumOverlayDisplay(viewNum);
		waitForElementVisibility(getSeriesDescriptionOverlay(viewNum));
	}

	public void waitForFullOverlayDisplay(int viewNum){
		waitForMinimumOverlayDisplay(viewNum);
		waitForElementVisibility(getImageMatrixOverlay(viewNum));
		try{
			waitForTimePeriod(1000);
		}catch(InterruptedException e){}
	}

	public boolean compareTextOverlayValues(int viewNum , String filePath) {
		boolean status = false;

		//compare values from database with UI
		if(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath).equalsIgnoreCase(getPatientNameOverlay(viewNum).getText().trim()) && 
				("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(getPatientIDOverlay(viewNum).getText().trim())  && 
				DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, "STUDY01", "STUDY01_SERIES01", filePath).equalsIgnoreCase(getSeriesDescriptionOverlay(viewNum).getText().trim())  &&		
				DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(getStudyDescriptionOverlay(viewNum).getText().trim())  &&		


				DataReader.getPatientDetails(PatientXMLConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(getPatientSexOverlay(viewNum).getText().trim())  &&
				DataReader.getStudyDetails(PatientXMLConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).contains(getStudyDateTimeOverlayText(viewNum))  &&		
				//				DataReader.getPatientDetails(PatientXMLConstants.PATIENT_CLASS_TEXTOVERLAY, filePath).equalsIgnoreCase(getPatientClassOverlay(viewNum).getText().trim())  &&		
				DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_MATRIX_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getImageMatrixOverlay(viewNum).getText().trim())  &&		


				(DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getImagePositionOverlay(viewNum).getText().trim()) || DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).contains(getImagePositionOverlay(viewNum).getText().replace("...", "").trim()) )  &&
				(("Thickness: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICE_THICKNESS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getSliceThicknessOverlay(viewNum).getText().trim())|| ("Thickness: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICE_THICKNESS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).contains(getSliceThicknessOverlay(viewNum).getText().replace("...", "").trim()))  &&		
				(("Location: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICELOCATION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getSliceLocationOverlay(viewNum).getText().trim()) || ("Location: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICELOCATION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).contains(getSliceLocationOverlay(viewNum).getText().replace("...", "").trim())) &&		
				("Kvp: "+DataReader.getSeriesDesc(PatientXMLConstants.KVP_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getKvpOverlay(viewNum).getText().trim())  &&		
				("FOV: "+DataReader.getSeriesDesc(PatientXMLConstants.FOV_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getFOVOverlay(viewNum).getText().trim()) &&
				(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).trim().equalsIgnoreCase(getCurrentScrollPosition(viewNum))&&
				(DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getImageMaxScrollPositionOverlayText(viewNum)) &&

				("mAs: "+DataReader.getSeriesDesc(PatientXMLConstants.MAS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getmAsOverlay(viewNum).getText().trim())  &&
				getTypeOverlay(viewNum).getText().trim().contains(DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_TYPE_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath))  &&		
				(DataReader.getSeriesDesc(PatientXMLConstants.IMAGEACQUISITION_DATETIME_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).contains(getImageAcquisitionDateTimeOverlay(viewNum).getText().trim())  &&		
				DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getWindowWidthValText(viewNum))  &&		
				DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getWindowCenterText(viewNum)) &&
				getZoomOverlay(viewNum).getText().trim()!=null)
			status = true;

		return status;
	}	

	public boolean isSelectedOverlayDisplayed(String textOverlay,int viewNum) {
		boolean status = false ;
		if(textOverlay.equalsIgnoreCase(ViewerPageConstants.DEFAULT_ANNOTATION)){
			waitForElementVisibility(getPatientIDOverlay(viewNum));
			if(getPatientNameOverlay(viewNum).isDisplayed() && getPatientIDOverlay(viewNum).isDisplayed() && getSeriesDescriptionOverlay(viewNum).isDisplayed() &&
					getPatientSexOverlay(viewNum).isDisplayed() && getStudyDateTimeOverlay(viewNum).isDisplayed() &&
					getSliceInfo(viewNum).isDisplayed() && 
					getWindowWidthValueOverlay(viewNum).isDisplayed()&& getWindowCenterValueOverlay(viewNum).isDisplayed() && getZoomOverlay(viewNum).isDisplayed() 
)

				//	getAccessionNumberOverlay(viewNum).isDisplayed())
				if((getStudyDescriptionOverlay(viewNum) == null) && (getPatientExternalIDOverlay(viewNum)== null) && (getPatientBirthDateOverlay(viewNum)== null)&& (getReferringPhysiciansNameOverlay(viewNum)== null)&& (getAcquisitionDeviceOverlay(viewNum)== null)&&
						(getPatientClassOverlay(viewNum)== null) && (getImageMatrixOverlay(viewNum)== null) && (getStudyAcquisitionSiteOverlay(viewNum)== null)&&
						(getImagePositionOverlay(viewNum)== null) && (getSliceThicknessOverlay(viewNum)== null) && (getSliceLocationOverlay(viewNum)== null) && (getKvpOverlay(viewNum)== null) && (getFOVOverlay(viewNum)== null) && (getDetectorOverlay(viewNum)== null)&&
						(getmAsOverlay(viewNum)== null) && (getTypeOverlay(viewNum)== null) && (getImageAcquisitionDateTimeOverlay(viewNum)== null)&& (getTargetOverlay(viewNum)== null)){

					status = true;
				}else
					status = false;

		}
		else if(textOverlay.equalsIgnoreCase(ViewerPageConstants.MINIMUM_ANNOTATION)){

			if( getPatientIDOverlay(viewNum).isDisplayed()  && 
					getStudyDateTimeOverlay(viewNum).isDisplayed() && 
					getSliceInfo(viewNum).isDisplayed() &&  
					getZoomOverlay(viewNum).isDisplayed() && 
					getSeriesDescriptionOverlay(viewNum).isDisplayed()){
				if((getPatientNameOverlay(viewNum)== null && getStudyDescriptionOverlay(viewNum)== null && 
						getPatientExternalIDOverlay(viewNum)== null && 
						getPatientBirthDateOverlay(viewNum)== null&& getReferringPhysiciansNameOverlay(viewNum)== null&& 
						getAcquisitionDeviceOverlay(viewNum)== null&&
						getPatientSexOverlay(viewNum)== null && getPatientClassOverlay(viewNum)== null && 
						getImageMatrixOverlay(viewNum)== null && getAccessionNumberOverlay(viewNum)== null && 
						getStudyAcquisitionSiteOverlay(viewNum)== null&&
						getImagePositionOverlay(viewNum)== null && getSliceThicknessOverlay(viewNum)== null && 
						getSliceLocationOverlay(viewNum)== null && getKvpOverlay(viewNum)== null && 
						getFOVOverlay(viewNum)== null && getDetectorOverlay(viewNum)== null&&
						getWindowWidthValueOverlay(viewNum)== null && getWindowCenterValueOverlay(viewNum)== null && 
						getmAsOverlay(viewNum)== null && getTypeOverlay(viewNum)== null && 
						getImageAcquisitionDateTimeOverlay(viewNum)== null&& getTargetOverlay(viewNum)== null)){
					status = true;
				}else
					status = false;
			}else
				status = false;

		}
		else if(textOverlay.equalsIgnoreCase(ViewerPageConstants.FULL_ANNOTATION)){
			waitForElementVisibility(getmAsOverlay(viewNum));
			if(getPatientNameOverlay(viewNum).isDisplayed() && 
					getPatientIDOverlay(viewNum).isDisplayed() && 
					getSeriesDescriptionOverlay(viewNum).isDisplayed() && 
					getStudyDescriptionOverlay(viewNum).isDisplayed()  && 

					getPatientBirthDateOverlay(viewNum).isDisplayed()&& 
					getReferringPhysiciansNameOverlay(viewNum).isDisplayed()&& 

					getPatientSexOverlay(viewNum).isDisplayed() && 
					getStudyDateTimeOverlay(viewNum).isDisplayed() && 

					getImageMatrixOverlay(viewNum).isDisplayed() && 
					getAccessionNumberOverlay(viewNum).isDisplayed() && 

					getSliceInfo(viewNum).isDisplayed() &&
					getImagePositionOverlay(viewNum).isDisplayed() && 
					getSliceThicknessOverlay(viewNum).isDisplayed() && 
					getSliceLocationOverlay(viewNum).isDisplayed() && 
					getKvpOverlay(viewNum).isDisplayed() && 
					getFOVOverlay(viewNum).isDisplayed() && 
					getWindowWidthValueOverlay(viewNum).isDisplayed()&& 
					getZoomOverlay(viewNum).isDisplayed() &&
					getmAsOverlay(viewNum).isDisplayed() &&
					getTypeOverlay(viewNum).isDisplayed() )

			{
				status = true;
			}else
				status = false;

		}

		return status;	

	}

	public boolean compareTextOverlayValuesInFullMode(int viewNum, String filePath){

		boolean status = false;

		try {
			//compare values from database with UI
			if(DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, filePath).equalsIgnoreCase(getPatientNameOverlay(viewNum).getText().trim()) && 
					("ID: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_ID_TEXTOVERLAY, filePath)).equalsIgnoreCase(getPatientIDOverlay(viewNum).getText().trim())  && 
					//					DataReader.getSeriesDesc(PatientXMLConstants.PATIENT_EXTERNALID_TEXTOVERLAY, "STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getPatientExternalIDOverlay(viewNum).getText().trim())  &&
					DataReader.getSeriesDesc(PatientXMLConstants.SERIES_DESCRIPTION_TEXTOVERLAY, "STUDY01", "STUDY01_SERIES01", filePath).equalsIgnoreCase(getSeriesDescriptionOverlay(viewNum).getText().trim())  &&		
					("DOB: "+DataReader.getPatientDetails(PatientXMLConstants.PATIENT_BIRTHDATE_TEXTOVERLAY, filePath)).equalsIgnoreCase(getPatientBirthDateOverlay(viewNum).getText().trim())  &&		
					DataReader.getStudyDetails(PatientXMLConstants.STUDY_DESCRIPTION_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(getStudyDescriptionOverlay(viewNum).getText().trim())  &&		
					DataReader.getStudyDetails(PatientXMLConstants.REFERRING_PHYSICIANNAME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(getReferringPhysiciansNameOverlay(viewNum).getText().trim())  &&		
					//					DataReader.getPatientDetails(PatientXMLConstants.AQUISITION_DEVICE_TEXTOVERLAY, filePath).equalsIgnoreCase(getAcquisitionDeviceOverlay(viewNum).getText().trim()) &&		

					DataReader.getPatientDetails(PatientXMLConstants.PATIENT_SEX_TEXTOVERLAY, filePath).equalsIgnoreCase(getPatientSexOverlay(viewNum).getText().trim())  &&
					DataReader.getStudyDetails(PatientXMLConstants.STUDY_DATETIME_TEXTOVERLAY, "STUDY01", filePath).equalsIgnoreCase(getStudyDateTimeOverlay(viewNum).getText().trim())  &&		
					//					DataReader.getPatientDetails(PatientXMLConstants.PATIENT_CLASS_TEXTOVERLAY, filePath).equalsIgnoreCase(getPatientClassOverlay(viewNum).getText().trim())  &&		
					DataReader.getSeriesDesc(PatientXMLConstants.ACCESSION_NUMBER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getAccessionNumberOverlay(viewNum).getText().trim())  &&		
					DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_MATRIX_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getImageMatrixOverlay(viewNum).getText().trim())  &&		
					DataReader.getSeriesDesc(PatientXMLConstants.STUDY_ACQUISITION_SITE_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getStudyAcquisitionSiteOverlay(viewNum).getText().trim())  &&	

					DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getImagePositionOverlay(viewNum).getText().trim())  &&
					("Thickness: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICE_THICKNESS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getSliceThicknessOverlay(viewNum).getText().trim())  &&		
					//DataReader.getSeriesDesc(PatientXMLConstants.DETECTOR_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getDetectorOverlay(viewNum).getText().trim())  &&		
					("Location: "+DataReader.getSeriesDesc(PatientXMLConstants.SLICELOCATION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getSliceLocationOverlay(viewNum).getText().trim())  &&		
					("Kvp: "+DataReader.getSeriesDesc(PatientXMLConstants.KVP_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getKvpOverlay(viewNum).getText().trim())  &&		
					("FOV: "+DataReader.getSeriesDesc(PatientXMLConstants.FOV_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getFOVOverlay(viewNum).getText().trim()) &&
					DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMCURRENT_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getCurrentScrollPosition(viewNum))&&
					("/"+DataReader.getSeriesDesc(PatientXMLConstants.IMAGENUMMAX_SCROLL_POSITION_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getImageMaxScrollPositionOverlayText(viewNum)) &&

					("mAs: "+DataReader.getSeriesDesc(PatientXMLConstants.MAS_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath)).equalsIgnoreCase(getmAsOverlay(viewNum).getText().trim())  &&
					//DataReader.getSeriesDesc(PatientXMLConstants.TARGET_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getTargetOverlay(viewNum).getText().trim())  &&		
					//					DataReader.getSeriesDesc(PatientXMLConstants.IMAGE_TYPE_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getTypeOverlay(viewNum).getText().trim())  &&		
					//DataReader.getSeriesDesc(PatientXMLConstants.IMAGEACQUISITION_DATETIME_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getImageAcquisitionDateTimeOverlay(viewNum).getText().trim())  &&		
					DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_WIDTH_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getWindowWidthValueOverlay(viewNum).getText().trim())  &&		
					DataReader.getSeriesDesc(PatientXMLConstants.WINDOW_CENTER_TEXTOVERLAY,"STUDY01","STUDY01_SERIES01", filePath).equalsIgnoreCase(getWindowCenterValueOverlay(viewNum).getText().trim()) &&
					getZoomOverlay(viewNum).getText().trim()!=null)

				status = true;

		} catch(Exception e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}	
	
	public boolean isElementHighlighted(WebElement element) throws TimeoutException, InterruptedException{
		Boolean status = false;
		mouseHover(VISIBILITY, element);
		if(element.getAttribute(NSGenericConstants.FILTER).equalsIgnoreCase(NSGenericConstants.FILTER_VALUE))
			status = true;
		return status;
	}

	public boolean verifyTooltipCustomAnnotations() throws InterruptedException{

		openCustomOverlay();
		boolean status = false;
		for(WebElement option : customOverlayOptions) {
			if(!isElementPresent(option)) {
				scrollDownUsingPerfectScrollbar(option, scrollThumb);
				waitForTimePeriod(100);

			}
			mouseHover(option);
			status = isElementPresent(tooltip);
			if(status) {
				status = status && getText(option).contains(getText(tooltip));
				break;
			}



		}
		closeCustomOverlay();

		return status;
	}

	public List<String> getAllCustomAnnotations() throws InterruptedException{

		openCustomOverlay();
		List<String> values = new ArrayList<String>();
		for(WebElement option : customOverlayOptions) {
			if(!isElementPresent(option)) {
				scrollDownUsingPerfectScrollbar(option, scrollThumb);
				waitForTimePeriod(100);

			}
			values.add(getText(option));


		}
		closeCustomOverlay();
		return values;


	}

	public List<String> getAllCustomAnnotationsWithoutValues(List<String> annotationWithVal) throws InterruptedException{


		List<String> values = new ArrayList<String>();
		for(String option : annotationWithVal) {

			if(option.contains(":")) {
				int index = option.indexOf(":");
				values.add(option.substring(0,index-1));
			}

		}

		return values;


	}

	public void enableSliderForCustomOverlays(String option) throws InterruptedException {

		openCustomOverlay();
		WebElement slider = getElement(By.xpath("//div[contains(normalize-space(text()),'"+option+"')]/preceding-sibling::ns-slider/label/span"));

		if(getBackgroundColor(slider).equals(ThemeConstants.EUREKA_ACTIVE_SLIDER_COLOR)||getBackgroundColor(slider).equals(ThemeConstants.DARK_ACTIVE_SLIDER_COLOR)) {
			if(!isElementPresent(slider)) {
				scrollDownUsingPerfectScrollbar(slider, scrollThumb);
				waitForTimePeriod(100);

			}
			click(slider);
		}
		closeCustomOverlay();
	}

	public void disableSliderForCustomOverlays(String option) throws InterruptedException {

		openCustomOverlay();
		WebElement slider = getElement(By.xpath("//div[contains(text(),'"+option+"')]/preceding-sibling::ns-slider/label/span"));

		if(getBackgroundColor(slider).equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR)||getBackgroundColor(slider).equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR)) {
			if(!isElementPresent(slider)) {
				scrollDownUsingPerfectScrollbar(slider, scrollThumb);
				waitForTimePeriod(100);

			}
			click(slider);
		}
		closeCustomOverlay();
	}

	public boolean verifyCustomOverlayIsSelected(String textOverlay) throws InterruptedException
	{
		openCustomOverlay();
		WebElement slider = getElement(By.xpath("//div[contains(text(),'"+textOverlay+"')]/preceding-sibling::ns-slider/label/span"));
		boolean status=getBackgroundColor(slider).equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR)||getBackgroundColor(slider).equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
		closeCustomOverlay();
		return status;
		
	}
	
	
}

