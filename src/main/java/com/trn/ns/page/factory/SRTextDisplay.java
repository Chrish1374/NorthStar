package com.trn.ns.page.factory;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.ViewerPageConstants;

public class SRTextDisplay extends ViewerPage {

	
	private WebDriver driver;
	public SRTextDisplay(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public WebElement getSRTextReport (int viewNum) {
		return getElement(By.xpath("//div[@id='viewer']//*[starts-with(@id, 'viewbox-"+(viewNum-1)+"')]"));
}

	public WebElement getName (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[1]/table/tr[2]/td"));
}

	public WebElement getID (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[1]/table/tr[3]/td"));
}
	
	public WebElement getDOB (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[1]/table/tr[4]/td"));
}
	public WebElement getSEX (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[1]/table/tr[5]/td"));
}
	
	public WebElement getStudyDescription (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[2]/table/tr[3]/td"));
}

	public WebElement getAccessionNumber (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[2]/table/tr[5]/td"));
}

	public WebElement getCompletionFlag (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[3]/table/tr[2]/td"));
		
}
	public WebElement getVerificationFlag (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/table/tr/td[3]/table/tr[3]/td"));
}
	
	public WebElement getRequest (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr[1]/li"));
}
	
	public WebElement getHistory (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr[2]/li"));
}
	public WebElement getProcedure (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr[3]/li"));
}
	
	public WebElement getFindings1 (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-container-sr/ul/ns-contentitem-sr[3]/li"));
}

	public WebElement getFindings2 (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-container-sr/ul/ns-contentitem-sr[4]/li"));
}
	
	public WebElement getFinding1Infervision (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr/li"));
}
	public WebElement getFinding2Infervision (int viewNum) {
		return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr/ul"));
}

	public WebElement LastItemOnSRReport(int viewNum)
	{
	return getElement(By.xpath("//*[@id='viewbox-"+(viewNum-1)+"']/ns-sr-view/perfect-scrollbar/div/div/div[1]/div[2]/ul/ns-contentitem-sr/ul/ns-contentitem-sr[6]/ul/ns-contentitem-sr[20]/li"));
    }

	
	@FindBy(css="label.jump-to-image")
	public List<WebElement> JumpToImageLink;
	
	public int getNumberOfImageLinkOnSRText(){
		return JumpToImageLink.size();	
	}
	@FindBy(xpath="//*[@class='jump-to-image']/parent::li")
	public List<WebElement> JumpToImageNumber;
	
	public int getJumpToImageNumber(){
		return JumpToImageNumber.size();
		
	}
	@FindBy(xpath="//div[@id='viewer']//*[starts-with(@id, 'viewbox-')]")
	public List<WebElement> totalNumberOfLayoutDisplayOnViewer;
	
	public int getNumberOfLayoutDisplayOnViewer(){
		return totalNumberOfLayoutDisplayOnViewer.size();
		
	}

	@FindBy(css="ns-contentitem-sr>li")
	public List<WebElement> listOfNoduleOnSRData;
	
	public WebElement getCopyTextForSRReport (int viewNum) {
		performMouseRightClick(getViewPort(viewNum));
		return getElement(By.cssSelector("#viewbox-"+(viewNum-1)+"> ns-sr-view > ns-contextmenu > div > div > div > div"));
}
		
}
