package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;

public class Triangulation extends ViewerPage {

	private WebDriver driver;

	public Triangulation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css="#quickToolContainer div:nth-child(1) > span:nth-child(8) ns-quick-toolbox-tile svg")
	public WebElement traingulationIcon;
	
	public void selectTraingulationFromQuickToolbar(int viewbox){
		openQuickToolbar(getViewPort(viewbox));
		click(traingulationIcon);
		waitForElementInVisibility(quickToolbar);
	}
	
	public WebElement getTraingulationPoint(int whichViewbox){

		return (getElement(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-triangulationpoint]")));

		 
	}
	
	public List<WebElement> getTraingulationPointDetails(int whichViewbox){

		List<WebElement> pointDetails = new ArrayList<WebElement>();
		pointDetails.addAll(getTraingulationPoint(whichViewbox).findElements(By.cssSelector("line")));
		
		return pointDetails;
	}

	// rejected and non focused
	public boolean verifyCrossHairWhenTraingulationToolSelected(int whichViewbox, int whichPoint){

		boolean status = false;
		try{
		List<WebElement> pointsWithSpecificDetails = getTraingulationPointDetails(whichViewbox);
			boolean condition_cross_hair = (pointsWithSpecificDetails.get(0).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_BORDER_COLOR_OP))&&
					(pointsWithSpecificDetails.get(1).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_BORDER_COLOR_OP)) &&
					(pointsWithSpecificDetails.get(2).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_BORDER_COLOR_OP)) &&
					(pointsWithSpecificDetails.get(3).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.REJECTED_BORDER_COLOR_OP)) ;

		
			status=condition_cross_hair ;
		}
		catch (IndexOutOfBoundsException e) {
			
			status=false;
		}

			return status;


		}




}
