package com.trn.ns.page.factory;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ViewerPageConstants;

public class PointAnnotation extends ViewerSliderAndFindingMenu {

	private WebDriver driver;

	public PointAnnotation(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// Functions related to Point

	

	public void selectPointFromQuickToolbar(int whichViewbox){
		openQuickToolbar(getViewPort(whichViewbox));
		waitForElementVisibility(pointIcon);
		click(pointIcon);
		waitForElementInVisibility(quickToolbar);

	}

	// creating a point
	public void drawPointAnnotationMarkerOnViewbox(int whichViewbox, int x, int y) throws InterruptedException {

		mouseHoverWithClick(PRESENCE, getViewPort(whichViewbox), x, y);
		waitForTimePeriod(1000);	

	}

	// verifying that is point selected
	public boolean isPointSelected(int whichViewbox, int whichPoint){

		boolean status = false;	
		List<WebElement> points = getAllPoints(whichViewbox);
		try{
			if(points.size()>0)

				if(isElementPresent(points.get(whichPoint-1).findElement(By.cssSelector("circle.highlightCircleHalo+circle[fill='"+ViewerPageConstants.CIRCLE_GREY_HANDLE+"']"))))
					status=true;

		}catch(NoSuchElementException e){}

		return status;
	}


	public void selectPoint(int whichViewbox, int whichPoint) throws  InterruptedException{	
		mouseHover(getViewPort(whichViewbox));
		List<WebElement> point = getPointDetails(whichViewbox,whichPoint);			
		mouseHoverWithClick(PRESENCE,point.get(point.size()-1));
		waitForTimePeriod(3000);
	}

	public void deletePoint(int whichViewbox, int whichPoint) throws  InterruptedException{


		selectPoint(whichViewbox,whichPoint);
		pressKeys(Keys.DELETE);
		waitForTimePeriod(3000);
	}

	public List<WebElement> getLinesOfPoint(int whichViewbox,int whichPoint){

		return getAllPoints(whichViewbox).get(whichPoint-1).findElements(By.cssSelector("g:nth-child(1) > line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.FILL_COLOR_HANDLER+"]"));

	}

	public List<WebElement> getFousedLinesOfPoint(int whichViewbox,int whichPoint){

		return getAllPoints(whichViewbox).get(whichPoint-1).findElements(By.cssSelector("g:nth-child(1) > line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.SHADOW_COLOR+"]"));

	}

	public List<WebElement> getHandlesOfPoint(int whichViewbox,int whichPoint){

		return getAllPoints(whichViewbox).get(whichPoint-1).findElements(By.cssSelector("circle"));

	}

	// Get All line and circle of a point
	public List<WebElement> getPoint(int whichViewbox, int whichPoint){

		List<WebElement> listOfPoints = getAllPoints(whichViewbox);
		List<WebElement> pointDetails = new ArrayList<WebElement>();

		WebElement point = listOfPoints.get(whichPoint-1);
		pointDetails.addAll(point.findElements(By.cssSelector("line")));
		pointDetails.addAll(point.findElements(By.cssSelector("circle")));
		return pointDetails;
	}

	// get only 4 green lines and 1 circle
	public List<WebElement> getPointDetails(int whichViewbox, int whichPoint){

		List<WebElement> listOfPoints = getAllPoints(whichViewbox);
		List<WebElement> pointDetails = new ArrayList<WebElement>();

		pointDetails.addAll(listOfPoints.get(whichPoint-1).findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.FILL_COLOR_HANDLER+"']")));
		pointDetails.addAll(listOfPoints.get(whichPoint-1).findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.ACCEPTED_COLOR+"'] , line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.REJECTED_COLOR+"'], line["+NSGenericConstants.STROKE+"='"+ViewerPageConstants.PENDING_COLOR+"']")));
		pointDetails.addAll(listOfPoints.get(whichPoint-1).findElements(By.cssSelector("circle.selectableCircleArea")));
		return pointDetails;
	}

	// accepted and focused
	public boolean verifyPointAnnotationIsCurrentAcceptedActiveGSPS(int whichViewbox, int whichPoint){

		return verifyPointAnnotationIsCurrentActiveGSPS(whichViewbox,whichPoint,ViewerPageConstants.ACCEPTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);


	}

	//rejected and focused
	public boolean verifyPointAnnotationIsCurrentRejectedActiveGSPS(int whichViewbox, int whichPoint){

		return verifyPointAnnotationIsCurrentActiveGSPS(whichViewbox,whichPoint,ViewerPageConstants.REJECTED_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}


	public int  getPointWhichIsRejectedAndActive(int whichViewbox){

		List<WebElement> myPoint = getAllPoints(whichViewbox);
		int index =0;

		for(int i =1;i<=myPoint.size();i++) {

			if(verifyPointAnnotationIsCurrentRejectedActiveGSPS(whichViewbox, i)) {
				index = i;
				break;
			}

		}

		return index;
	}

	public int  getPointWhichIsPendingAndActive(int whichViewbox){

		List<WebElement> myPoint = getAllPoints(whichViewbox);
		int index =0;

		for(int i =1;i<=myPoint.size();i++) {

			if(verifyPointAnnotationIsPendingActiveGSPS(whichViewbox, i)) {
				index = i;
				break;
			}

		}

		return index;
	}

	public int  getPointWhichIsAcceptedAndActive(int whichViewbox){

		List<WebElement> myPoint = getAllPoints(whichViewbox);
		int index =0;

		for(int i =1;i<=myPoint.size();i++) {

			if(verifyPointAnnotationIsCurrentAcceptedActiveGSPS(whichViewbox, i)) {
				index = i;
				break;
			}

		}

		return index;
	}

	// rejected and non focused
	public boolean verifyPointAnnotationIsRejectedInActiveGSPS(int whichViewbox, int whichPoint){

		return verifyPointAnnotationIsCurrentInActiveGSPS(whichViewbox, whichPoint, ViewerPageConstants.REJECTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);

	}

	// accepted and non focused
	public boolean verifyPointAnnotationIsAcceptedInActiveGSPS(int whichViewbox, int whichPoint){

		return verifyPointAnnotationIsCurrentInActiveGSPS(whichViewbox, whichPoint, ViewerPageConstants.ACCEPTED_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	// pending and focused
	public boolean verifyPointAnnotationIsPendingActiveGSPS(int whichViewbox, int whichPoint){


		return verifyPointAnnotationIsCurrentActiveGSPS(whichViewbox,whichPoint,ViewerPageConstants.PENDING_COLOR,ViewerPageConstants.OPACITY_FOR_JUMP_ICON);


	}

	// pending and non focused
	public boolean verifyPointAnnotationIsPendingInActiveGSPS(int whichViewbox, int whichPoint){

		return verifyPointAnnotationIsCurrentInActiveGSPS(whichViewbox, whichPoint, ViewerPageConstants.PENDING_COLOR, ViewerPageConstants.OPACITY_FOR_JUMP_ICON);
	}

	public boolean isPointPresent(int whichViewbox, int whichPoint){

		boolean status = false;
		try{
			if(getPoint(whichViewbox, whichPoint).size()>0)
				status = true;

		}catch(NoSuchElementException | IndexOutOfBoundsException e)
		{
			status = false;
		}

		return status;
	}

	public boolean verifyPointAnnotation(int whichViewbox, int whichPoint){

		boolean status = false;

		List<WebElement> point = getPointDetails(whichViewbox, whichPoint);


		//Left and right Lines x coordinate difference

		double xLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1)));
		double xRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1)));
		boolean condition2_diffOfXaxis = xLeftDiff1 == xRightDiff2;
		boolean condition2_diffOfXAxisGreaterThanZero = (xRightDiff2 >0) && (xLeftDiff1>0);

		//Left and right Lines y coordinate difference
		double yLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1)));
		double yRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1)));
		boolean condition2_diffOfYaxis = yLeftDiff1 == yRightDiff2;
		boolean condition2_diffOfYAxisEqualsToZero = (yRightDiff2 ==0)&&( yLeftDiff1==0);

		//Top and Bottom Line x coordinate difference
		xLeftDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1)));
		xRightDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1)));
		boolean condition3_diffOfXaxis = xLeftDiff1 == xRightDiff2;
		boolean condition3_diffOfXAxisEqualsToZero = (xLeftDiff1 ==0)&&( xRightDiff2==0);

		//Top and Bottom Line y coordinate difference
		yLeftDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)));
		yRightDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));
		boolean condition4_diffOfYaxis = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)))==(Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));
		boolean condition4_diffOfYAxisGreaterThanZero = (yLeftDiff1 >0) && (yRightDiff2>0);

		boolean condition5_radiusOfCircle = point.get(point.size()-1).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX);

		if(condition2_diffOfXaxis && condition2_diffOfYaxis && condition2_diffOfXAxisGreaterThanZero && 
				condition3_diffOfXaxis && condition4_diffOfYaxis && condition5_radiusOfCircle && condition2_diffOfYAxisEqualsToZero &&
				condition3_diffOfXAxisEqualsToZero && condition4_diffOfYAxisGreaterThanZero)
			status=true;

		return status;
	}

	public void movePoint(int whichViewbox, int whichPoint, int xOffset, int yOffset) throws InterruptedException{

		Actions action = new Actions(driver);
		List<WebElement> point = getPoint(whichViewbox,whichPoint);

		action.clickAndHold(getPoint(whichViewbox,whichPoint).get(point.size()-1)).perform();
		LOGGER.info("drag and drop...clickAndHold");
		waitForTimePeriod(1000);
		action.moveByOffset(xOffset, yOffset).perform();
		LOGGER.info("drag and drop...moveByOffset");
		waitForTimePeriod(1000);
		action.release().moveByOffset(1, 1).build().perform();
		LOGGER.info("drag and drop...completed");
		waitForTimePeriod(1000);

	}

	public List<WebElement> getPointHandles(int whichViewbox){

		return driver.findElements(By.cssSelector("#svg-"+(whichViewbox-1)+" > g > g  line:nth-child(8) + circle+circle"));

	}

	public void deleteSelectedPoint() throws InterruptedException{
		pressKeys(Keys.DELETE); 
		waitForTimePeriod(2000);
	}

	public boolean verifyPendingPointAnnotation(int whichViewbox, int whichPoint){

		boolean status = false;

		List<WebElement> point = getPointDetails(whichViewbox, whichPoint);

		boolean condition_color = (point.get(4).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR))&&
				(point.get(5).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR)) &&
				(point.get(6).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR)) &&
				(point.get(7).getAttribute(NSGenericConstants.STROKE).equals(ViewerPageConstants.PENDING_COLOR)); // Fix for DE-552-Selection area changed from 4px to 6px


		//Left and right Lines x coordinate difference

		double xLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.X1)));
		double xRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.X1)));
		boolean condition2_diffOfXaxis = xLeftDiff1 == xRightDiff2;
		boolean condition2_diffOfXAxisGreaterThanZero = (xRightDiff2 >0) && (xLeftDiff1>0);

		//Left and right Lines y coordinate difference
		double yLeftDiff1 = (Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(0).getAttribute(ViewerPageConstants.Y1)));
		double yRightDiff2 = (Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(1).getAttribute(ViewerPageConstants.Y1)));
		boolean condition2_diffOfYaxis = yLeftDiff1 == yRightDiff2;
		boolean condition2_diffOfYAxisEqualsToZero = (yRightDiff2 ==0)&&( yLeftDiff1==0);

		//Top and Bottom Line x coordinate difference
		xLeftDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.X1)));
		xRightDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.X1)));
		boolean condition3_diffOfXaxis = xLeftDiff1 == xRightDiff2;
		boolean condition3_diffOfXAxisEqualsToZero = (xLeftDiff1 ==0)&&( xRightDiff2==0);

		//Top and Bottom Line y coordinate difference
		yLeftDiff1 = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)));
		yRightDiff2 = (Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));
		boolean condition4_diffOfYaxis = (Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(2).getAttribute(ViewerPageConstants.Y1)))==(Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y2)) - Double.parseDouble(point.get(3).getAttribute(ViewerPageConstants.Y1)));
		boolean condition4_diffOfYAxisGreaterThanZero = (yLeftDiff1 >0) && (yRightDiff2>0);

		boolean condition5_radiusOfCircle = point.get(point.size()-1).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX);

		if(condition2_diffOfXaxis && condition2_diffOfYaxis && condition2_diffOfXAxisGreaterThanZero && 
				condition3_diffOfXaxis && condition4_diffOfYaxis && condition5_radiusOfCircle && condition2_diffOfYAxisEqualsToZero &&
				condition3_diffOfXAxisEqualsToZero && condition4_diffOfYAxisGreaterThanZero && condition_color)
			status=true;

		return status;
	}

	public List<WebElement> getAllPoints(int whichViewbox){

		List<WebElement> allElements = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-point]"));
		List<WebElement> listOfPoints = new ArrayList<WebElement>();

		for(WebElement element : allElements) {
			listOfPoints.add(element);

		}
		return listOfPoints;
	}

	public List<WebElement> getTextCommentsForAllPoints(int whichViewbox){

		List<WebElement> allElements = getAllPoints(whichViewbox);
		List<WebElement> listOfPoints = new ArrayList<WebElement>();

		for(WebElement element : allElements) {

			try{	

				listOfPoints.add(element.findElement(By.cssSelector("g > text + text:last-child >tspan")));
			}

			catch(NoSuchElementException e){
				continue;

			}

		}


		return listOfPoints;
	}

	public String getTextCommentForPoint(int whichViewbox,int whichPoint){

		List<WebElement> allElements = getAllPoints(whichViewbox);
		return getText(allElements.get(whichPoint-1).findElement(By.cssSelector("g > text + text:last-child >tspan")));

	}

	public boolean verifyPointAnnotationIsCurrentActiveGSPS(int whichViewbox, int whichPoint,String whichColor, String opacity){

		boolean status = false;

		WebElement myPoint = getAllPoints(whichViewbox).get(whichPoint-1);

		List<WebElement> shadowLinecomp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.SHADOW_COLOR+"]"));
		List<WebElement> pointColoredLineComp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+whichColor+"]"));
		List<WebElement> transparentLinecomp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.FILL_COLOR_HANDLER+"]"));

		status= shadowLinecomp.size()==4 && pointColoredLineComp.size()==4 && transparentLinecomp.size()==4;		

		if(pointColoredLineComp.size()>0) {
			status = status && pointColoredLineComp.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.ACTIVE_GSPS_WIDTH);
			for(int i =0 ; i<pointColoredLineComp.size();i++)
				status = status && pointColoredLineComp.get(i).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);
		}
		if(shadowLinecomp.size()>0) {
			status = status && shadowLinecomp.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.SHADOW_GSPS_WIDTH);

			for(int i =0 ; i<shadowLinecomp.size();i++)
				status = status && shadowLinecomp.get(i).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);

		}

		status = status && myPoint.findElements(By.cssSelector("circle.selectableCircleArea")).size()==1;


		if(myPoint.findElements(By.cssSelector("circle")).size()>3) {

			//Halo circle color
			status = status && myPoint.findElement(By.cssSelector("circle.highlightCircleHalo")).getCssValue(NSGenericConstants.STROKE).equals(ViewerPageConstants.HALO_RING_COLOR);
			//Halo circle radius
			status = status && myPoint.findElement(By.cssSelector("circle.highlightCircleHalo")).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.HALO_CIRCLE_RADIUS);			
			//Handle circle color
			status = status && myPoint.findElement(By.cssSelector("circle["+NSGenericConstants.FILL+"='"+ViewerPageConstants.CIRCLE_GREY_HANDLE+"']")).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.POINT_CIRCLE_RADIUS);

		}

		return status;


	}

	public boolean verifyPointAnnotationIsCurrentInActiveGSPS(int whichViewbox, int whichPoint,String whichColor, String opacity){

		boolean status = false;
		WebElement myPoint = getAllPoints(whichViewbox).get(whichPoint-1);

		List<WebElement> shadowLinecomp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.SHADOW_COLOR+"]"));
		List<WebElement> pointColoredLineComp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+whichColor+"]"));
		List<WebElement> transparentLinecomp = myPoint.findElements(By.cssSelector("line["+NSGenericConstants.STROKE+"="+ViewerPageConstants.FILL_COLOR_HANDLER+"]"));

		status= shadowLinecomp.size()==0 && pointColoredLineComp.size()==4 && transparentLinecomp.size()==4;		

		if(pointColoredLineComp.size()>0) {
			status = status && pointColoredLineComp.get(0).getAttribute(NSGenericConstants.STROKE_WIDTH).equals(ViewerPageConstants.NON_ACTIVE_GSPS_WIDTH);
			for(int i =0 ; i<pointColoredLineComp.size();i++)
				status = status && pointColoredLineComp.get(i).getAttribute(NSGenericConstants.STROKE_OPACITY).equals(opacity);
		}

		List<WebElement> circle = myPoint.findElements(By.cssSelector("circle.selectableCircleArea"));
		status = status && circle.size()==1;
		status = status && circle.get(0).getAttribute(NSGenericConstants.R).equals(ViewerPageConstants.POINT_CIRCLE_RADIUS_WO_PX);

		return status;


	}

	
	public WebElement getCentreForPoint(int whichViewbox,int whichPoint)
    {
    	List<WebElement> pointsWithAllDetails = getPoint(whichViewbox, whichPoint);
    	WebElement centreOfPoint=pointsWithAllDetails.get(pointsWithAllDetails.size()-1);
		
		return centreOfPoint;
    }
	
	public boolean verifyLabeling(int whichViewbox, int whichPoint, String label) {
			
		return getLabel(whichViewbox, whichPoint).equalsIgnoreCase(label);
		
	}
	
	public String getLabel(int whichViewbox, int whichPoint) {
		
		WebElement tag = getElements(By.cssSelector("#svg-"+(whichViewbox-1)+" g[ns-point] + g[ns-roilabel] text:nth-child(2)")).get(whichPoint-1);
		
		return getText(tag);
		
	}
	
}
