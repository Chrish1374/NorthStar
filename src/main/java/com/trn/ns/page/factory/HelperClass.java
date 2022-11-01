package com.trn.ns.page.factory;

import java.util.Random;
import org.openqa.selenium.WebDriver;
import com.relevantcodes.extentreports.LogStatus;
import com.trn.ns.page.constants.URLConstants;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.web.page.WebActions;

public class HelperClass extends WebActions{


	private LoginPage loginPage;
	private PatientListPage patientPage;
	private ViewerPage viewerPage;
	private EllipseAnnotation ellipse;
	private CircleAnnotation circle;
	private PointAnnotation point;
	private MeasurementWithUnit line;
	private DICOMRT rt;
	private DatabaseMethods db;

	public HelperClass(WebDriver driver) {
		super(driver);

	}

	public void annotationCreationAndVerification(String username, String pwd, String patientName, int whichViewbox,int circleCount, int ellipseCount, int measurCount,int pointCount,String checkpointNumber) throws InterruptedException {


		Header header = new Header(driver);		
		header.logout();

		ExtentManager.customExtentReportLog(LogStatus.INFO, extentTest, "", "Login to Northstar application with "+username);
		loginPage = new LoginPage(driver);
		loginPage.login(username, username);
		patientPage = new PatientListPage(driver);
		patientPage.clickOnPatientRow(patientName);
		patientPage.clickOntheFirstStudy();

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(whichViewbox);
		ellipse = new EllipseAnnotation(driver);
		circle = new CircleAnnotation(driver);
		line = new MeasurementWithUnit(driver);
		point = new PointAnnotation(driver);


		int x= 0;
		int y= 0;
		int xoffset= 40;
		int yoffset=50;

		if(ellipseCount>0) {

			ellipse.selectEllipseFromQuickToolbar(whichViewbox);

			for(int i =0;i<ellipseCount;i++) {	

				x= generateRandomIntIntRange(50,150);
				y= generateRandomIntIntRange(50,150);

				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint["+(i+1)+".a]","Ellipse creation");
				ellipse.drawEllipse(whichViewbox, x, (y*-1), xoffset, yoffset);			
				ellipse.assertTrue(ellipse.verifyEllipseAnnotationIsCurrentActiveAcceptedGSPS(whichViewbox, (i+1)),checkpointNumber+" Verifying the ellipse state", "verified");
			}

			ellipse.assertEquals(ellipse.getAllEllipses(whichViewbox).size(),ellipseCount ,checkpointNumber+"Verifying the ellipse creation", "verified");
		}


		if(pointCount>0) {

			point.selectPointFromQuickToolbar(whichViewbox);
			for(int i =0;i<pointCount;i++) {	

				x= generateRandomIntIntRange(50,150);
				y= generateRandomIntIntRange(50,150);

				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint["+(i+1)+".a]","Point creation");
				point.drawPointAnnotationMarkerOnViewbox(whichViewbox, (x*-1), y);				
				point.assertTrue(point.verifyPointAnnotationIsCurrentAcceptedActiveGSPS(whichViewbox, (i+1)),checkpointNumber+"Verifying the point state", "verified");
			}
			point.assertEquals(point.getAllPoints(whichViewbox).size(),pointCount ,checkpointNumber+"Verifying the points presence", "verified");
		}

		if(circleCount>0) {

			circle.selectCircleFromQuickToolbar(whichViewbox);
			for(int i =0;i<circleCount;i++) {	
				x= generateRandomIntIntRange(50,150);
				y= generateRandomIntIntRange(50,150);
				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint["+(i+1)+".a]","Circle creation");
				circle.drawCircle(whichViewbox, (x*-1), (y*-1), xoffset, yoffset);	
			}
			circle.assertTrue(circle.getAllCircles(whichViewbox).size()>=circleCount ,checkpointNumber+"Verifying the circle creation", "verified");
		}

		if(measurCount>0) {

			line.selectDistanceFromQuickToolbar(whichViewbox);
			for(int i =0;i<measurCount;i++) {	
				x= generateRandomIntIntRange(150,350);
				y= generateRandomIntIntRange(150,350);
				ExtentManager.customExtentReportLog(Configurations.INFO, extentTest,"Checkpoint["+(i+1)+".a]","Measurement creation");
				line.drawLine(whichViewbox, x, y, 70, 70);	
				line.assertTrue(line.verifyLinearMeasurementAnnotationIsActiveAcceptedGSPS(whichViewbox, (i+1)),checkpointNumber+"Verifying the line state", "verified");
			}
			line.assertEquals(line.getAllLinearMeasurements(whichViewbox).size(),measurCount ,checkpointNumber+"Verifying the line creation", "verified");
		}
	}

	private static int generateRandomIntIntRange(int min, int max) {
		Random r = new Random();
		return ((r.nextInt((max - min) + 1)) + min) * (r .nextBoolean() ? -1 : 1);
	}

	public ViewerPage loadViewerPage(String patientName,String patientID, int whichStudy, int whichViewbox) throws InterruptedException {


		patientPage = new PatientListPage(driver);
		if(!patientName.isEmpty())
			patientPage.clickOnPatientRow(patientName);
		else
			patientPage.clickOnPatientUsingID(patientID);

		patientPage.waitForStudyToLoad();
		patientPage.clickOnStudy(whichStudy);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForRespectedViewboxToLoad(whichViewbox);

		return viewerPage;
	}

	public ViewerPage loadViewerPageUsingSearch(String patientName, int whichStudy, int whichViewbox) throws InterruptedException {

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(patientName, "", "","");
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOnStudy(whichStudy);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(whichViewbox);

		return viewerPage;

	}

	public ViewerPage loadViewerPageUsingSearch(String patientName, String whichStudy, int whichViewbox) throws InterruptedException {

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(patientName, "", "","");
		patientPage.clickOnPatientRow(patientName);		
		patientPage.clickOnStudy(whichStudy);
		viewerPage = new ViewerPage(driver);
		viewerPage.waitForAllImagesToLoad();
		viewerPage.waitForRespectedViewboxToLoad(whichViewbox);

		return viewerPage;

	}

	public DICOMRT loadViewerPageForRTUsingSearch(String patientName, int whichStudy, int whichViewbox) throws InterruptedException {

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(patientName, "", "","");
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOnStudy(whichStudy);

		rt = new DICOMRT(driver);
		rt.waitForDICOMRTToLoad(whichViewbox);

		return rt;

	}

	public void browserBackAndReloadViewer(String patientName, int whichStudy, int whichViewbox)throws InterruptedException{

		viewerPage = new ViewerPage(driver);
		viewerPage.browserBackWebPage();
		waitForTimePeriod(100);
		patientPage = new PatientListPage(driver);
		patientPage.waitForStudyToLoad();

		loadViewerPageUsingSearch(patientName, whichStudy, whichViewbox);



	}

	public void browserBackAndReloadViewer(String patientName, String whichStudy, int whichViewbox)throws InterruptedException{

		viewerPage = new ViewerPage(driver);
		viewerPage.browserBackWebPage();		
		patientPage = new PatientListPage(driver);
		patientPage.waitForStudyToLoad();		
		loadViewerPageUsingSearch(patientName, whichStudy, whichViewbox);		

	}

	public void browserBackAndReloadViewer(String patientName, String patientID, int whichStudy, int whichViewbox)throws InterruptedException{

		viewerPage = new ViewerPage(driver);
		viewerPage.browserBackWebPage();

		patientPage = new PatientListPage(driver);
		patientPage.waitForStudyToLoad();

		loadViewerPage(patientName, patientID ,whichStudy, whichViewbox);


	}

	public void browserBackAndReloadRTData(String patientName, int whichStudy, int whichViewbox)throws InterruptedException{

		viewerPage = new ViewerPage(driver);
		viewerPage.browserBackWebPage();

		patientPage = new PatientListPage(driver);
		patientPage.waitForStudyToLoad();

		loadViewerPageForRTUsingSearch(patientName, whichStudy, whichViewbox);



	}

	public ViewerPage loadViewerDirectly(String patientName, int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUID(patientName);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID);

			viewerPage = new ViewerPage(driver);
			viewerPage.waitForAllImagesToLoad();
			viewerPage.waitForRespectedViewboxToLoad(whichViewbox);
		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return viewerPage;

	}

	public ViewerPage loadViewerDirectly(String patientName, String username, String password , int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUID(patientName);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID+"?username="+username+"&password="+password);

			viewerPage = new ViewerPage(driver);
			viewerPage.waitForAllImagesToLoad();
			viewerPage.waitForRespectedViewboxToLoad(whichViewbox);
		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return viewerPage;

	}
	
	
	public ViewerPage loadViewerPageUsingSearch(String username, String password, String patientName, int whichStudy, int whichViewbox) throws InterruptedException {

		loginPage = new LoginPage(driver);
		loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL+"?username="+username+"&password="+password);

		patientPage = new PatientListPage(driver);
		patientPage.searchPatient(patientName, "", "","");
		patientPage.clickOnPatientRow(patientName);

		patientPage.clickOnStudy(whichStudy);

		viewerPage = new ViewerPage(driver);
		viewerPage.waitForViewerpageToLoad(whichViewbox);

		return viewerPage;

	}

	public ViewerPage loadViewerDirectlyUsingID(String patientID, int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUIDUsingPatientID(patientID);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID);

			viewerPage = new ViewerPage(driver);
			viewerPage.waitForAllImagesToLoad();
			viewerPage.waitForRespectedViewboxToLoad(whichViewbox);
		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return viewerPage;

	}

	public ViewerPage loadViewerDirectlyUsingID(String patientID, String username, String password, int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUIDUsingPatientID(patientID);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID+"?username="+username+"&password="+password);

			viewerPage = new ViewerPage(driver);
			viewerPage.waitForAllImagesToLoad();
			viewerPage.waitForRespectedViewboxToLoad(whichViewbox);
		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return viewerPage;

	}

	public DICOMRT loadRTDirectly(String patientName, String username, String password , int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUID(patientName);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID+"?username="+username+"&password="+password);

			rt = new DICOMRT(driver);
			rt.waitForDICOMRTToLoad(whichViewbox);

		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return rt;

	}

	public DICOMRT loadRTDirectly(String patientName, int whichViewbox) {

		try {
			db = new DatabaseMethods(driver);
			String studyInstanceUID = db.getStudyInstanceUID(patientName);
			db.navigateToURL(URLConstants.BASE_URL+URLConstants.VIEWER_PAGE_URL+"/"+studyInstanceUID);

			rt = new DICOMRT(driver);
			rt.waitForDICOMRTToLoad(whichViewbox);

		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return rt;

	}
	
	public PatientListPage loadPatientsPageDirectly(String username, String password) {

		try {
			loginPage = new LoginPage(driver);
			loginPage.navigateToURL(URLConstants.BASE_URL+URLConstants.PATIENT_LIST_URL+"?username="+username+"&password="+password);
			
		}catch (Exception e) {
			LOGGER.info("loading viewer has some failure "+e.getMessage());
		}
		return new PatientListPage(driver);

	}

}
