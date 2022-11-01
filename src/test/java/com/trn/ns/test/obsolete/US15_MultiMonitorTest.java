//package com.trn.ns.test.obsolete;
//
//import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;
//
//import java.awt.Rectangle;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.Point;
//import org.openqa.selenium.support.PageFactory;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import com.trn.ns.page.factory.DashboardPage;
//import com.trn.ns.test.base.TestBase;
//import com.trn.ns.test.configs.Configurations;
//import com.trn.ns.utilities.ExtentManager;
//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//
////Protocol Execution Time (Required):  <time> e.g 10 mins
//
//public class US15_MultiMonitorTest extends TestBase{
//
//	
//	private ExtentTest extentTest;
//	String protocolName;
//	private DashboardPage homepage;
//
//	private static JavascriptExecutor js ;
//	private static ArrayList<String> tabs;	
//	private static String appURL="http://localhost:4200/dashboard";
//
//	@BeforeMethod(alwaysRun=true)
//	public void beforeMethod(){
//		
//		homepage = new DashboardPage(driver);
//		homepage.navigateToBaseURL();
//		js = (JavascriptExecutor) driver;
//		js.executeScript("window.open()");
//		tabs = new ArrayList<String>(driver.getWindowHandles());
//		driver.switchTo().window(tabs.get(1));
//		driver.get(appURL);
//		js.executeScript("localStorage.removeItem(\"windows\")");
//	}
//
//	// TC13: Verify that user has to drag the windows to multiple monitors manually a very first time
//	
//	@Test(groups ={"firefox"})
//	public void test01_TC13_verifyLaunchOfAppAndOpenNewChildWindows() throws InterruptedException {
//		
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user has to drag the windows to multiple monitors manually a very first time");
//		DashboardPage homepage = new DashboardPage(driver);		
//		String blankWindow = tabs.get(1);
//		ArrayList<Rectangle> allMonitors = homepage.getDetailsOfAttachedMonitors();
//		for(int i =0 ; i<allMonitors.size();i++){		
//			homepage.click(homepage.CLICKABILITY, homepage.openWindowButton);
//			extentTest.log(Configurations.INFO,"", "Openning the child window on monitor["+(i+1)+"]");
//		}
//		
//		List<String> allWindowsID = homepage.getAllOpenedWindowsIDs();		
//		allWindowsID.remove(blankWindow);
//	
//		//verifying that all the windows are opened in primary monitor 
//		Dimension windowSize = null;
//		Point windowLocation = null; 
//		List<Dimension> windowsDimension = new ArrayList<Dimension>();
//	
//		for(int i =0 ;i<allWindowsID.size();i++){
//			windowLocation= homepage.getWindowPosition(allWindowsID.get(i));
//			windowSize = homepage.getWindowSize(allWindowsID.get(i));						
//			homepage.assertTrue(windowSize.height<allMonitors.get(0).height, "Verifying the child window["+(i+1)+"] height:="+windowSize.height+" with monitor's height ="+allMonitors.get(0).height, "window["+(i+1)+"] height verification success");
//			homepage.assertTrue(windowSize.width<allMonitors.get(0).width, "Verifying the child window["+(i+1)+"] width:="+windowSize.width+" with monitor's width ="+allMonitors.get(0).width, "window["+(i+1)+"] width verification success");
//			homepage.assertTrue(windowLocation.x<allMonitors.get(0).height, "Verifying the child window["+(i+1)+"] x coordinate = "+windowLocation.x, "window["+(i+1)+"] x coordinate verification success");
//			homepage.assertTrue(windowLocation.y<allMonitors.get(0).width,"Verifying the child window["+(i+1)+"] y coordinate = "+windowLocation.y, "window["+(i+1)+"] y coordinate verification success");
//			windowsDimension.add(windowSize);
//		}
//		
//		// dragging the windows to respective monitors
//		Point p =null;
//		for(int i =1 ;i<allWindowsID.size();i++){						
//			  p = new Point(allMonitors.get(i-1).x,0);
//			  homepage.switchToWindow(allWindowsID.get(i));			  
//			  homepage.setWindowPosition(allWindowsID.get(i),p);	          
//		}
//		homepage.waitForTimePeriod("Medium");
//		
//		// verifying the height and width
//		for(int i =1 ;i<allWindowsID.size();i++){
//			windowLocation= homepage.getWindowPosition(allWindowsID.get(i));
//			windowSize = homepage.getWindowSize(allWindowsID.get(i));
////			homepage.assertTrue(windowLocation.x>allMonitors.get(i-1).x, "Verifying the window's x co-ordinate with monitor's x co-ordinate", "window's x co-ordinate with monitor's x co-ordinate matched");
////			homepage.assertTrue(windowLocation.y>allMonitors.get(i-1).y,"Verifying the window's y co-ordinate with monitor's y co-ordinate", "window's y co-ordinate with monitor's y co-ordinate matched");
//			homepage.assertEquals(windowSize.height,windowsDimension.get(i).height, "verifying the window's height with the height opened in primary monitor","window height same opened in primary monitor");
//			homepage.assertEquals(windowSize.width,windowsDimension.get(i).width, "verifying the window's width with the width opened in primary monitor", "window width same opened in primary monitor");
//		}
//	}
//	
//	//TC15	Verify that on close of single window instance leads to close all the instances of application
//	@Test(groups ={"firefox"})
//	public void test02_TC15_verifyAppClosedOnAnyWindowClosure() throws InterruptedException{
//
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that on close of single window instance leads to close all the instances of application");
//		DashboardPage homepage= new DashboardPage(driver);
//		String blankWindow = tabs.get(1);
//		ArrayList<Rectangle> allMonitors = homepage.getDetailsOfAttachedMonitors();
//		for(int i =0 ; i<allMonitors.size();i++){
//			homepage.click(homepage.CLICKABILITY, homepage.openWindowButton);
//			extentTest.log(Configurations.INFO,"", "Openning the child window on monitor["+(i+1)+"]");
//		}
//		List<String> allWindowsID = homepage.getAllOpenedWindowsIDs();
//		allWindowsID.remove(blankWindow);
//		
//		//Dragging the child windows to different monitors
//		Point p =null;
//		for(int i =1 ;i<allWindowsID.size();i++){
//			  p = new Point(allMonitors.get(i-1).x,0);
//			  homepage.switchToWindow(allWindowsID.get(i));			  
//			  extentTest.log(Configurations.INFO, "","Switched the child window["+i+"]");
//			  homepage.setWindowPosition(allWindowsID.get(i),p);
//	          extentTest.log(Configurations.INFO, "","Dragged the child window["+i+"] to monitor["+i+"]");
//		}
//		homepage.waitForTimePeriod("Low");
//		
//		// closing the child window
//		extentTest.log(Configurations.INFO, "","Closed the Child window");		
//		homepage.switchToWindow(allWindowsID.get(1));	
//		driver.close();
//		homepage.switchToWindow(tabs.get(0));		
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), 1, "Verifying application should be closed on close of child window", "Application is closed on close of child window - Success");
//		
//		// Re launching the application 
//		extentTest.log(Configurations.INFO, "", "Relaunching the application");
//		js.executeScript("window.open()");	
//		Set<String> allWindows = homepage.getAllOpenedWindowsID();
//		allWindows.remove(tabs.get(0));
//		
//		//Closing the parent window and validating whether application is closed	
//		extentTest.log(Configurations.INFO, "","Closed the Parent window");
//		driver.switchTo().window(allWindows.iterator().next());
//		driver.get(appURL);
//		homepage.waitForTimePeriod("Low");
//		driver.close();
//		homepage.waitForTimePeriod("Low");
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), 1, "Verifying application should be closed on close of parent window", "Application is closed on close of parent window- Success");
//		homepage.switchToWindow(tabs.get(0));
//	}
//
//	
//	//TC12	Verify that user is able to recover the same state as the user used the last time when the application was closed.
//	@Test(groups ={"firefox"})
//	public void test03_TC12_verifyAppStateIsRecoveredOnReLaunch() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that user is able to recover the same state as the user used the last time when the application was closed.");
//		DashboardPage homepage= PageFactory.initElements(driver, DashboardPage.class);
//		String blankWindow = tabs.get(1);
//		ArrayList<Rectangle> allMonitors = homepage.getDetailsOfAttachedMonitors();
//		for(int i =0 ; i<allMonitors.size();i++){
//			homepage.click(homepage.CLICKABILITY,homepage.openWindowButton);
//			extentTest.log(Configurations.INFO,"", "Openning the child window on monitor["+(i+1)+"]");
//		}
//		List<String> allWindowsID = homepage.getAllOpenedWindowsIDs();
//		allWindowsID.remove(blankWindow);
//		
//		//Dragging the child windows to different monitors
//		Point p =null;
//		for(int i =1 ;i<allWindowsID.size();i++){
//			  p = new Point(allMonitors.get(i-1).x,0);
//			  extentTest.log(Configurations.INFO, "","Switched the child window["+i+"]");
//			  homepage.switchToWindow(allWindowsID.get(i));					  
//			  homepage.setWindowPosition(allWindowsID.get(i),p);
//	          extentTest.log(Configurations.INFO, "","Dragged the child window["+i+"] to monitor["+i+"]");
//		}
//		homepage.waitForTimePeriod("Medium");
//		
//		// closing the child window and trying to recover the state
//		extentTest.log(Configurations.INFO, "","Closed the Child window");		
//		homepage.switchToWindow(allWindowsID.get(1));	
//		driver.close();
//		homepage.switchToWindow(tabs.get(0));		
//		
//		// Re launching the application 
//		extentTest.log(Configurations.INFO, "", "Relaunching the application");
//		js.executeScript("window.open()");	
//		Set<String> allWindows = homepage.getAllOpenedWindowsID();
//		allWindows.remove(tabs.get(0));
//		driver.switchTo().window(allWindows.iterator().next());
//		driver.get(appURL);
//		
//		// verifying the application state is recovered
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), allWindowsID.size()+1, "Verifying application state is recovered on relaunch of application", "Application State is recovered - Success");
//
//		// closing the parent window an trying to recover the state
//		
//		extentTest.log(Configurations.INFO, "","Closed the Parent window");
//		homepage.switchToWindow(allWindows.iterator().next());
//		driver.close();
//		homepage.waitForTimePeriod("Low");
//		driver.switchTo().window(tabs.get(0));
//		
//		// Re launching the application 
//		extentTest.log(Configurations.INFO, "","Relaunching the application");
//		js.executeScript("window.open()");	
//		homepage.waitForTimePeriod("Low");
//		allWindows = homepage.getAllOpenedWindowsID();
//		allWindows.remove(tabs.get(0));
//		driver.switchTo().window(allWindows.iterator().next());
//		driver.get(appURL);
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), allWindowsID.size()+1, "Verifying application state is recovered on relaunch of application", "Application State is recovered - Success");
//		homepage.waitForTimePeriod("Low");
//		int totalWindows= homepage.getAllOpenedWindowsIDs().size();
//		
//		// opening a new window and try to recover its state
//		driver.switchTo().window(allWindows.iterator().next());
//		homepage.click(homepage.CLICKABILITY,homepage.openWindowButton);
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), totalWindows+1, "Verifying that new window is opened", "New Window is opened - Success");
//		driver.switchTo().window(allWindows.iterator().next());
//		driver.close();
//		homepage.waitForTimePeriod("Low");
//		driver.switchTo().window(tabs.get(0));
//		extentTest.log(Configurations.INFO, "","Relaunching the application");
//		js.executeScript("window.open()");			
//		allWindows = homepage.getAllOpenedWindowsID();
//		allWindows.remove(tabs.get(0));
//		driver.switchTo().window(allWindows.iterator().next());
//		driver.get(appURL);
//		// verifying the windows size when state is recovered
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), totalWindows+1, "Verifying Application recovery", "Application State is recovered - Success");
//
//		
//	}
//	
//	//TC23	Verify that window size is same as it was closed prior	
//	@Test(groups ={"firefox"})
//	public void test04_TC23_verifyWindowsSizeAfterAppRecovery() throws InterruptedException{
//		extentTest = ExtentManager.getTestInstance();
//		extentTest.setDescription("Verify that window size is same as it was closed prior");
//		DashboardPage homepage= new DashboardPage(driver);
//		Dimension windowSizeTobeSet = new Dimension(400, 400);
//		String blankWindow = tabs.get(0);
//		ArrayList<Rectangle> allMonitors = homepage.getDetailsOfAttachedMonitors();
//		for(int i =0 ; i<allMonitors.size();i++){
//			homepage.click(homepage.CLICKABILITY, homepage.openWindowButton);
//			extentTest.log(Configurations.INFO,"", "Openning the child window on monitor["+(i+1)+"]");
//		}
//		List<String> allWindowsID = homepage.getAllOpenedWindowsIDs();
//		allWindowsID.remove(blankWindow);
//		
//		//verifying that all the windows are opened in primary monitor 
//		Dimension windowSize = null;
//		Point windowLocation = null; 
//		List<Dimension> windowsDimension = new ArrayList<Dimension>();
//		for(int i =0 ;i<allWindowsID.size();i++){
//			windowLocation= homepage.getWindowPosition(allWindowsID.get(i));
//			windowSize = homepage.getWindowSize(allWindowsID.get(i));
//			// verifying the child windows height, width and coordinates
//			homepage.assertTrue(windowSize.height<allMonitors.get(0).height, "Verifying the child window["+(i+1)+"] height:="+windowSize.height+" with monitor's height ="+allMonitors.get(0).height, "window["+(i+1)+"] height verification success");
//			homepage.assertTrue(windowSize.width<allMonitors.get(0).width, "Verifying the child window["+(i+1)+"] width:="+windowSize.width+" with monitor's width ="+allMonitors.get(0).width, "window["+(i+1)+"] width verification success");
//			homepage.assertTrue(windowLocation.x<allMonitors.get(0).height, "Verifying the child window["+(i+1)+"] x coordinate = "+windowLocation.x, "window["+(i+1)+"] x coordinate verification success");
//			homepage.assertTrue(windowLocation.y<allMonitors.get(0).width,"Verifying the child window["+(i+1)+"] y coordinate = "+windowLocation.y, "window["+(i+1)+"] y coordinate verification success");
//			windowsDimension.add(windowSize);
//		}
//		
//		//Dragging the child windows to different monitors
//		Point p =null;
//		for(int i =1 ;i<allWindowsID.size();i++){
//			  p = new Point(allMonitors.get(i-1).x,0);
//			  extentTest.log(Configurations.INFO, "","Switched the child window["+i+"]");
//			  homepage.switchToWindow(allWindowsID.get(i));			  
//			  extentTest.log(Configurations.INFO, "","Setting the window["+i+"] size to 400, 400");
//			  homepage.setWindowPosition(allWindowsID.get(i),p);
//			  homepage.setWindowSize(allWindowsID.get(i), windowSizeTobeSet);
//	          extentTest.log(Configurations.INFO, "","Dragged the child window["+i+"] to monitor["+i+"]");
//		}
//		
//		// closing the child window and trying to recover the state		
//		extentTest.log(Configurations.INFO, "","Closed the Child window");
//		homepage.switchToWindow(allWindowsID.get(1));
//		driver.close();
//		driver.switchTo().window(tabs.get(0));			
//			
//		// Relaunching the application 
//		extentTest.log(Configurations.INFO, "", "Relaunching the application");
//		driver.get(appURL);		
//		homepage.assertEquals(homepage.getAllOpenedWindowsIDs().size(), allWindowsID.size(), "Verifying application state is recovered on relaunch of application", "Application State is recovered - Success");
//		allWindowsID =homepage.getAllOpenedWindowsIDs();
//		allWindowsID.remove(tabs.get(0));
//		
//		// verifying the height and width
//		for(int i =0 ;i<allWindowsID.size();i++){
//			windowLocation= homepage.getWindowPosition(allWindowsID.get(i));			
//			windowSize = homepage.getWindowSize(allWindowsID.get(i));
//			// verifying the windows size on respective monitors
//			homepage.assertEquals(windowLocation.x,allMonitors.get(i).x, "Verifying the window's x co-ordinate with monitor's x co-ordinate", "window's x co-ordinate with monitor's x co-ordinate matched");
//			homepage.assertEquals(windowLocation.y,allMonitors.get(i).y,"Verifying the window's y co-ordinate with monitor's y co-ordinate", "window's y co-ordinate with monitor's y co-ordinate matched");
//			homepage.assertTrue(windowSize.height<400, "verifying the window's height with the height opened in primary monitor","window height same opened in primary monitor");
//			homepage.assertTrue(windowSize.width<400, "verifying the window's width with the width opened in primary monitor", "window width same opened in primary monitor");
////			homepage.assertEquals(windowSize.height,400, "verifying the window's height with the height opened in primary monitor","window height same opened in primary monitor");
////			homepage.assertEquals(windowSize.width,400, "verifying the window's width with the width opened in primary monitor", "window width same opened in primary monitor");
//			
//		}
//		
//		
//	}
//}
