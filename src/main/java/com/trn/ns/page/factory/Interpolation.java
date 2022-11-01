package com.trn.ns.page.factory;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;

public class Interpolation extends PolyLineAnnotation {

	private WebDriver driver;

	public Interpolation(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.driver=driver;
	}

	
	@FindBys(@FindBy(css = "[ns-polyline] circle["+NSGenericConstants.FILL+"='"+ViewerPageConstants.OTHER_SHADOW_COLOR+"']"))
	public List<WebElement> blueDots;
		
	public boolean performInterPolationOnViewbox(int whichViewbox, int whichPolyline,int whichline , int[] coordinates) throws InterruptedException {

		
		WebElement line = getLinesOfPolyLine(whichViewbox, whichPolyline).get(whichline-1);
		Actions action = new Actions(driver);
		enableInterpolation();
		action.click(line).perform();
		waitForTimePeriod(1000);
		
		boolean status = true;
		for(int i =0 , j=1;i<coordinates.length-1; j++) {				
			action.moveToElement(getViewPort(whichViewbox),coordinates[i], coordinates[i+1]).click().perform();
			i=i+2;
			status = status && getBlueDots(whichViewbox).size()==j;
				
		}
		disableInterpolation();
		return status;
		
	}
	
	public boolean performInterPolationOffsetWay(int whichViewbox, int whichPolyline,int whichline , int[] coordinates) throws InterruptedException {

		
		WebElement line = getLinesOfPolyLine(whichViewbox, whichPolyline).get(whichline-1);
		enableInterpolation();
		click(line);
		waitForTimePeriod(1000);
		
		boolean status = true;
		for(int i =0 , j=1;i<coordinates.length-1; j++) {				

			Actions action = new Actions(driver);
			action.moveToElement(getViewPort(whichViewbox)).moveByOffset(coordinates[i], coordinates[i+1]).click().build().perform();
			waitForTimePeriod(1000);
			i=i+2;
			status = status && getBlueDots(whichViewbox).size()==j;
			
			
		}
		disableInterpolation();
		return status;
		
	}
	
	public boolean performInterPolationOffsetWay(int whichViewbox, int whichPolyline, int[] coordinates) throws InterruptedException {

		
		enableInterpolation();
		waitForTimePeriod(1000);
		
		boolean status = true;
		for(int i =0 , j=1;i<coordinates.length-1; j++) {				

			click(coordinates[i], coordinates[i+1]);
			waitForTimePeriod(100);
			i=i+2;
			status = status && getBlueDots(whichViewbox).size()==j;
			
			
		}
		disableInterpolation();
		return status;
		
	}
	
	public void enableInterpolation() throws InterruptedException {
		
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).build().perform();
		waitForTimePeriod(1000);
	}
	
	public void disableInterpolation() throws InterruptedException {

		Actions action = new Actions(driver);
		action.keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).perform();
		waitForTimePeriod(1000);
	}
	
	public List<WebElement> getBlueDots(int whichViewbox){
		
		return getElements(By.cssSelector("#viewbox-"+(whichViewbox-1)+" [ns-polyline] circle["+NSGenericConstants.FILL+"='"+ViewerPageConstants.OTHER_SHADOW_COLOR+"']"));
		
	}

	public void performInterpolationBetweenTwoSlices(int viewbox,int startSlice, int whichStartPoly,int[] startCoordinates,int startHand1,int startHand2,int endSlice,
			int whichEndPoly,int[] endCoordinates,int endHand1,int endHand2) throws IOException, InterruptedException
	{
		holdShiftKeyPressed();
		List<WebElement>handles=getLinesOfPolyLine(viewbox, whichStartPoly);
		editPolyLine(handles.get(startHand1), startCoordinates, handles.get(startHand2));
		
		//Scroll down till the end slice
		for(int i=startSlice;i<endSlice;i++)
	         performMouseWheelDown(1);
		
		//perform interpolation on end slice and release shift key
		handles=getLinesOfPolyLine(viewbox, whichEndPoly);
		editPolyLine(handles.get(endHand1), endCoordinates, handles.get(endHand2));
		releaseShiftKeyPressed();
	}
}






