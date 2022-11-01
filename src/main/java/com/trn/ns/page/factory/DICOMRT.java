package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;


public class DICOMRT extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public DICOMRT(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}

	@FindBy(id="legend-body-container")
	public WebElement legendOptions;

	@FindBy(css="#legend-body-container .segmentationText")
	public List<WebElement> legendOptionsList;

	@FindBys({@FindBy(css="#legend-body-container .ns-acceptRejectIcon svg")})
	public List<WebElement> segmentstateicon;
	
	@FindBys({@FindBy(css="#legend-body-container .ns-acceptRejectIcon svg use")})
	public List<WebElement> pendingStateicon;
	
	@FindBys({@FindBy(css="#legend-body-container .ns-acceptRejectIcon svg g:not([fill='none'])")})
	public List<WebElement> rejectedStateIcon;
	
	@FindBys({@FindBy(css="#legend-body-container .ns-acceptRejectIcon svg path[fill]")})
	public List<WebElement> acceptedStateIcon;
	
	@FindBys({@FindBy(css="div.icnDisplay > ns-icon > div > svg > g")})
	public List<WebElement> segmentEyeIcon;
	
	@FindBys({@FindBy(css="div.icnDisplay > ns-icon > div > svg")})
	public List<WebElement> segmentEyeIconSvg;
	
	@FindBys({@FindBy(css="#legend-body-container  div.segmentationColor")})
	public List<WebElement> segmentColorIcon;

	public By spinnerIcon = By.cssSelector("#outerBox > ns-spinner > div");
	
	public String getSelectedContourColor() {
		
		String val ="";
		
		PolyLineAnnotation poly= new PolyLineAnnotation(driver);
		List<WebElement> polylines = poly.getAllPolylines(1);
		
		for(WebElement ele : polylines) {
			
			try {
			
				if(ele.findElement(By.cssSelector("svg > path")).getAttribute("stroke").equalsIgnoreCase(ViewerPageConstants.SHADOW_COLOR)) {
					val = ele.findElement(By.cssSelector("svg > path:nth-child(2)")).getAttribute("stroke");
				break;
			}
			}catch (NoSuchElementException e) {
				continue;
			}
		}
		return val;
		
	}
	
	public String getColorOfSegment(int legendOptionNumber){
		return(segmentColorIcon.get(legendOptionNumber-1).getCssValue(NSGenericConstants.BACKGROUND_COLOR));
	}
	
	public WebElement segmentName(String name) {
		return getElement(By.xpath("//*[@id='legend-body-container']//*[contains(text(),'"+ name +"')]"));
	}

	public int getCountOfAllNavigationArrows() {
		return legendOptionsList.size();
	}

	public void navigateToFirstContourOfSegmentation(int legendOptionNumber)throws InterruptedException {
		
		click(legendOptionsList.get(legendOptionNumber-1));
		waitForTimePeriod(4000);

	}

	public boolean verifyRejectedRTSegment(int legendOptionNumber){
		boolean status = false;
		legendOptionNumber=legendOptionNumber-1;
		try {
			if (segmentstateicon.get(legendOptionNumber).isDisplayed() && segmentstateicon.get(legendOptionNumber).findElement(By.cssSelector("path")).getCssValue(NSGenericConstants.FILL).equals(ThemeConstants.ERROR_ICON_COLOR))
				status = true ;
		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public boolean verifyPendingRTSegment(int legendOptionNumber){
		boolean status = false;
		legendOptionNumber=legendOptionNumber-1;
		try {
			if (segmentstateicon.get(legendOptionNumber).isDisplayed() && segmentstateicon.get(legendOptionNumber).findElement(By.cssSelector("use")).getCssValue(NSGenericConstants.FILL).equals(ViewerPageConstants.PENDING_RT_FINDING_COLOR))
				status = true ;
		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public boolean verifyAcceptedRTSegment(int legendOptionNumber){
		boolean status = false;
		legendOptionNumber=legendOptionNumber-1;
		try {
			if (segmentstateicon.get(legendOptionNumber).isDisplayed() && segmentstateicon.get(legendOptionNumber).findElement(By.cssSelector("path")).getCssValue(NSGenericConstants.FILL).equals(ThemeConstants.SUCCESS_ICON_COLOR));
				status = true ;
		}catch(NoSuchElementException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public String getNameOfAcceptedRejectedSegment(int legendOptionNumber) {
		return getText(legendOptionsList.get(legendOptionNumber-1));		
	}

		
	public void waitForDICOMRTToLoad() {
		waitForElementInVisibility(spinnerIcon);
		waitForElementVisibility(legendOptions);
		
	}
	
	public void waitForDICOMRTToLoad(int whichViewbox) {
		waitForElementInVisibility(spinnerIcon);
		waitForElementVisibility(getLegends(whichViewbox));
		
	}
	

	public List<WebElement> getLegendOptionsList(int viewNum){
	return getElements(By.cssSelector( "#viewbox-"+(viewNum-1)+" #legend-body-container .segmentationText"));

}

	public void rejectSegment(int legendOptionNum) throws InterruptedException {
		
		navigateToFirstContourOfSegmentation(legendOptionNum);
		selectRejectfromGSPSRadialMenu();

	}
	
	public void acceptSegment(int legendOptionNum) throws InterruptedException {
		
		navigateToFirstContourOfSegmentation(legendOptionNum);
		selectAcceptfromGSPSRadialMenu();
	}
		
	public List<String> getStateSpecificSegmentNames(int whichViewbox, String state) {

		List<String> findingsL = new ArrayList<String>();
				
			if(state.equalsIgnoreCase(ViewerPageConstants.PENDING_RT_FINDING_COLOR))
			{

				segmentstateicon = getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #legend-body-container .ns-acceptRejectIcon svg g use[fill]"));
				for(int i=0;i<segmentstateicon.size();i++){
				if(segmentstateicon.get(i).getCssValue(NSGenericConstants.FILL).equalsIgnoreCase(state))
				findingsL.add(segmentstateicon.get(i).findElement(By.xpath("ancestor::div[contains(@class,'legendItem')]//*[@class='segmentationText']")).getText());
				}
			}
			else if(state.equalsIgnoreCase(ThemeConstants.ERROR_ICON_COLOR))
			{
				segmentstateicon = getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #legend-body-container .ns-acceptRejectIcon svg g:not([fill='none'])"));
				for(int i=0;i<segmentstateicon.size();i++){
				if(segmentstateicon.get(i).getAttribute(NSGenericConstants.FILL).equalsIgnoreCase(getHexColorValue(state)))
					findingsL.add(segmentstateicon.get(i).findElement(By.xpath("ancestor::div[contains(@class,'legendItem')]//*[@class='segmentationText']")).getText());
				}
			}
			else if(state.equalsIgnoreCase(ThemeConstants.SUCCESS_ICON_COLOR))
			{
				segmentstateicon = getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #legend-body-container .ns-acceptRejectIcon svg path[fill]"));
				for(int i=0;i<segmentstateicon.size();i++){
				if(segmentstateicon.get(i).getAttribute(NSGenericConstants.FILL).equalsIgnoreCase(getHexColorValue(state)))
					findingsL.add(segmentstateicon.get(i).findElement(By.xpath("ancestor::div[contains(@class,'legendItem')]//*[@class='segmentationText']")).getText());
				}
			}


		return findingsL;
	}

	public String getLegendOptionColor(String legendName) {
		
		List<String> names = convertWebElementToStringList(legendOptionsList);
		WebElement legend = null;
		
		for(int i =0;i<names.size();i++) {
			
			if(names.get(i).equalsIgnoreCase(legendName)) {
				
				legend = segmentColorIcon.get(i);
				break;
				
			}
			
		}
		
		return getCssValue(legend,NSGenericConstants.BACKGROUND_COLOR);
		
	}
		
	public void clickOnSegementStateIcon(int whichViewbox, int legendoption) throws InterruptedException {
		
		mouseHover(getViewPort(whichViewbox));
		List<WebElement> stateIcon = getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" #legend-body-container .segmentationText"));
		click(stateIcon.get(legendoption-1));
	}
	
	private By getLegends(int whichViewbox){
		return By.cssSelector("#viewbox-"+(whichViewbox-1)+" #legend-body-container .segmentationText");
	}
		
	public List<String> getLegendOptions(int whichViewbox) throws InterruptedException {
		
		mouseHover(getViewPort(whichViewbox));
		List<WebElement> legends = getElements(getLegends(whichViewbox));
		return convertWebElementToStringList(legends);
	}
	
}






