package com.trn.ns.test.viewer.basic;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.PatientPageConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.Header;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.page.factory.PagesTheme;
import com.trn.ns.page.factory.ViewerLayout;
import com.trn.ns.page.factory.ViewerPage;
import com.trn.ns.page.factory.ViewerTextOverlays;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class ViewerThemeTest extends TestBase{


	private ViewerPage viewerpage;
	private ExtentTest extentTest;

	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String liver9filePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String patientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liver9filePath);

	String loadedTheme =ThemeConstants.EUREKA_THEME_NAME; 
	private HelperClass helper;

	@Test(groups={"Chrome","Edge","DR2360","US2145","Positive","US2337","F931","E2E","F1081"})
	public void test01_DR2360_TC9274_US2145_TC9537_US2337_TC10019_verifyEurekaThemeOnEurekaPopup() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Eureka theme is applied on Viewer toolbar/Eureka dropdown."+
		"<br> Verify Eureka theme for layout menu"
		+ "<br> Verify Layout Menu styling.");

		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		
		viewerpage.clickUsingAction(layout.gridIcon);		
		viewerpage.assertEquals(viewerpage.getBackgroundColor(layout.layoutContainer)
				, ThemeConstants.PAGE_BACKGROUND,"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
		viewerpage.assertTrue(viewerpage.getCssValue(layout.layoutContainer, NSGenericConstants.BOX_SHADOW).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR),"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
		viewerpage.click(viewerpage.getViewPort(1)); 
		
		
		overlays.openTextOverlayOptions();		
		viewerpage.assertEquals(viewerpage.getBackgroundColor(viewerpage.getElement(overlays.overlayContainer))
				, ThemeConstants.PAGE_BACKGROUND,"Checkpoint[2/2]","Verifying the Eureka theme is applied on Overlay Dropdown");
	
		viewerpage.assertTrue(viewerpage.getCssValue(viewerpage.getElement(overlays.overlayContainer), NSGenericConstants.BOX_SHADOW).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR),"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
		
	}
	
	@Test(groups={"Chrome","Edge","DR2360","US2145","Positive","US2337","F931","E2E","F1081"})
	public void test02_DR2360_TC9274_US2145_TC9537_US2337_TC10019_verifyDarkThemeOnEurekaPopup() throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Eureka theme is applied on Viewer toolbar/Eureka dropdown.<br>"+
		"<br> Verify Dark theme for layout menu"
		+ "<br> Verify Layout Menu styling.");

		db = new DatabaseMethods(driver);
		db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		ViewerLayout layout = new ViewerLayout(driver);
		ViewerTextOverlays overlays = new ViewerTextOverlays(driver);
		
		viewerpage.waitForAllChangesToLoad();
		viewerpage.clickUsingAction(layout.gridIcon);		
		
		viewerpage.assertEquals(viewerpage.getBackgroundColor(layout.layoutContainer)
				, ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
		viewerpage.assertTrue(viewerpage.getCssValue(layout.layoutContainer, NSGenericConstants.BOX_SHADOW).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR),"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
		viewerpage.click(viewerpage.getViewPort(1));
		
		overlays.openTextOverlayOptions();		
		viewerpage.assertEquals(viewerpage.getBackgroundColor(viewerpage.getElement(overlays.overlayContainer))
				, ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR,"Checkpoint[2/2]","Verifying the Eureka theme is applied on Overlay Dropdown");
	
		viewerpage.assertTrue(viewerpage.getCssValue(viewerpage.getElement(overlays.overlayContainer), NSGenericConstants.BOX_SHADOW).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR),"Checkpoint[1/2]","Verifying the Eureka theme is applied on layout Dropdown");
	
	}

	//DR2423:Icons displayed on header do not match the Eureka theme
	@Test(groups={"Chrome","Edge","DR2423","Positive","US2337","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test03_DR2423_TC9602_US2337_TC9748_verifyThemeOnHeaderIcons(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the icons displayed on header matches Eureka and Dark theme."
				+ "<br> Verify that all icons and text in the header appear consistent with the selected theme.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		Header header=new Header(driver);
		
			
		viewerpage.mouseHover(header.userInfo);
		viewerpage.assertTrue(pageTheme.verifyThemeForHeaderIcons(header.userInfo,theme),"Checkpoint[2/4]","Verifying the theme for User info icon.");
		
		//resize browser and verify theme
		viewerpage.resizeBrowserWindow(300, 600);
		
		viewerpage.mouseHover(header.userInfo);
		viewerpage.assertTrue(pageTheme.verifyThemeForHeaderIcons(header.userInfo,theme),"Checkpoint[4/4]","Verifying the theme for User info icon when browser is minimize.");
			
	}
		
	@Test(groups={"Chrome","Edge","US2337","Positive","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US2337_TC10005_verifyThemeOnUserMenuIcons(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User Menu styling.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		Header header=new Header(driver);
		
			
		viewerpage.click(header.userInfo);
		
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.helpIcon, loadedTheme),"Checkpoint[1/10]","Verifying the helpIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.userManualIcon, loadedTheme),"Checkpoint[2/10]","Verifying the user manualIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.logoutIcon, loadedTheme),"Checkpoint[3/10]","verifying logout icon");

		
		viewerpage.assertTrue(header.getAttributeValue(header.helpIcon,ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.INFO_ICON),"Checkpoint[4/10]","verifying help icon is correcyly displayed");
		viewerpage.assertTrue(header.getAttributeValue(header.userManualIcon, ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.USERMANUAL_ICON),"Checkpoint[5/10]","verifying user manual icon is correcyly displayed");
		viewerpage.assertTrue(header.getAttributeValue(header.logoutIcon, ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.LOGOUT_ICON),"Checkpoint[6/10]","verifying logout icon is correcyly displayed");

		
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.about, loadedTheme),"Checkpoint[7/10]","verifying the theme on text - about");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.help, loadedTheme),"Checkpoint[8/10]","verifying the theme on text - help");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.logOut, loadedTheme),"Checkpoint[9/10]","verifying the theme on text - logout");
		
		
		viewerpage.assertTrue(pageTheme.verifyThemeForDialogPopUP(header.optionMenu,loadedTheme),"Checkpoint[10/10]","Verifying the theme for user menu");
		
		viewerpage.click(header.userInfo);
	}
	
	@Test(groups={"Chrome","Edge","US2337","Positive","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test04_US2337_TC10006_verifyUserMenuOnMinimized(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User Menu styling.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		Header header=new Header(driver);
		
		String usernameText = header.getText(header.userInfo);
		
		viewerpage.resizeBrowserWindow(500, 500);
		viewerpage.assertEquals(header.userInfoSandwichIcon.size(), 3,"Checkpoint[1/15]","verifying that on browser resize sandwich icon is displayed");
		
		viewerpage.assertEquals(header.getCssValue(header.userInfoSandwichIcon.get(0),NSGenericConstants.FILL),ThemeConstants.USER_SANDWICH_ICON_COLOR,"Checkpoint[2/15]","verifying the filling color");
		viewerpage.assertTrue(!header.getText(header.userInfo).contains(usernameText),"Checkpoint[3/15]","verifying no name is displayed");
	
		viewerpage.click(header.userInfo);
		
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.helpIcon, loadedTheme),"Checkpoint[4/15]","Verifying the helpIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.userManualIcon, loadedTheme),"Checkpoint[5/15]","Verifying the user manualIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.logoutIcon, loadedTheme),"Checkpoint[6/15]","verifying logout icon");

		
		viewerpage.assertTrue(header.getAttributeValue(header.helpIcon,ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.INFO_ICON),"Checkpoint[7/15]","verifying help icon is correcyly displayed");
		viewerpage.assertTrue(header.getAttributeValue(header.userManualIcon, ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.USERMANUAL_ICON),"Checkpoint[8/15]","verifying user manual icon is correcyly displayed");
		viewerpage.assertTrue(header.getAttributeValue(header.logoutIcon, ViewerPageConstants.ATTRIBUTE_D).equalsIgnoreCase(PatientPageConstants.LOGOUT_ICON),"Checkpoint[9/15]","verifying logout icon is correcyly displayed");

		
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.about, loadedTheme),"Checkpoint[10/15]","verifying the theme on text - about");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.help, loadedTheme),"Checkpoint[11/15]","verifying the theme on text - help");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.logOut, loadedTheme),"Checkpoint[12/15]","verifying the theme on text - logout");
		
		
		viewerpage.assertTrue(pageTheme.verifyThemeForDialogPopUP(header.optionMenu,loadedTheme),"Checkpoint[13/15]","Verifying the theme for user menu");
		viewerpage.click(header.userInfo);
		
		viewerpage.maximizeWindow();
		
		viewerpage.assertTrue(header.userInfoSandwichIcon.isEmpty(),"Checkpoint[14/15]","verifying that on browser maximize the sandwich icon is not displayed");
		viewerpage.assertEquals(header.getText(header.userInfo), usernameText,"Checkpoint[15/15]","verifying the first and last name is displayed");
		
	}
	
	@Test(groups={"Chrome","Edge","US2337","Positive","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test05_US2337_TC10007_verifyUserMenuOnHovered(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify User Menu styling.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		Header header=new Header(driver);
		
	
		viewerpage.click(header.userInfo);
		
		for(int i =0;i<header.usermenuOptions.size();i++) {
			
		header.mouseHover(header.usermenuOptions.get(i));
		viewerpage.assertEquals(header.getCssValue(header.usermenuOptions.get(i), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_ON_MOUSE_HOVERED,"Checkpoint[1/8]","Verifying that on mousehover options are getting grayed out");
		
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.helpIcon, loadedTheme),"Checkpoint[2/8]","Verifying the helpIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.userManualIcon, loadedTheme),"Checkpoint[3/8]","Verifying the user manualIcon");
		viewerpage.assertTrue(pageTheme.verifyThemeForBorder(header.logoutIcon, loadedTheme),"Checkpoint[4/8]","verifying logout icon");

		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.about, loadedTheme),"Checkpoint[5/8]","verifying the theme on text - about");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.help, loadedTheme),"Checkpoint[6/8]","verifying the theme on text - help");
		viewerpage.assertTrue(pageTheme.verifyThemeOnLabel(header.logOut, loadedTheme),"Checkpoint[7/8]","verifying the theme on text - logout");
		
			for(int j =0;j<header.usermenuOptions.size();j++) {
			
				if(j!=i)
					viewerpage.assertEquals(header.getCssValue(header.usermenuOptions.get(j), NSGenericConstants.OPACITY),ViewerPageConstants.OPACITY_FOR_JUMP_ICON,"Checkpoint[8/8]","Verifying that other options are not getting changed");
					
			}
		}
		viewerpage.click(header.userInfo);
		
		
	}
	
	@Test(groups={"Chrome","Edge","US2337","Positive","F1081"},dataProvider="ThemeName",dataProviderClass=com.trn.ns.page.factory.PagesTheme.class)
	public void test06_US2337_TC10019_verifyLayoutMenuStyling(String theme) throws InterruptedException {

		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify Layout Menu  styling.");

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			db = new DatabaseMethods(driver);
			db.updateTheme(ThemeConstants.DARK_THEME_NAME,username);
			loadedTheme=ThemeConstants.DARK_THEME_NAME;
		}else
			loadedTheme=ThemeConstants.EUREKA_THEME_NAME;
	
		
		helper =new HelperClass(driver);
		viewerpage=helper.loadViewerDirectly(patientName,username, password, 1);
		PagesTheme pageTheme=new PagesTheme(driver);
		ViewerLayout layout = new ViewerLayout(driver);
		
		for(int i =0 ;i<layout.totalNumberOfLayoutIcons.size();i++) {
			
			layout.selectLayout(layout.totalNumberOfLayoutIcons.get(i));
			layout.openLayoutContainer();
			for(int j =0 ;j<layout.totalNumberOfLayoutIcons.size();j++) {
				
				if(j!=i) {
					viewerpage.assertTrue(pageTheme.verifyButtonIsFilled(layout.layoutOptionsbox(j+1).get(0),loadedTheme),"Checkpoint[1."+j+"/2]","Verifying that layout icon's background when not selected "+loadedTheme);
					viewerpage.assertTrue(pageTheme.verifyButtonIsNotFilled(layout.layoutOptionsbox(j+1).get(1),loadedTheme),"Checkpoint[2."+j+"/2]","Verifying that layout icon's filled when not selected "+loadedTheme);
				}else {
					
					viewerpage.assertTrue(pageTheme.verifyButtonIsNotFilled(layout.layoutOptionsbox(j+1).get(0),loadedTheme),"Checkpoint[1."+j+"/2]","Verifying that layout icon's background when selected "+loadedTheme);
					viewerpage.assertTrue(pageTheme.verifyButtonIsFilled(layout.layoutOptionsbox(j+1).get(1),loadedTheme),"Checkpoint[2."+j+"/2]","Verifying that layout icon's filled when selected "+loadedTheme);
			
					
				}
			}
			layout.closeLayoutContainer();
				
		}
		
		
	}


}

