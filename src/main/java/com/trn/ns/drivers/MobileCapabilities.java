package com.trn.ns.drivers;

import io.appium.java_client.remote.MobileCapabilityType;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.io.File;
//import java.util.Properties;




import org.openqa.selenium.remote.DesiredCapabilities;

//import com.generic.property.PropertyManager;

public class MobileCapabilities {
	private MobileCapabilities(){

	}

	private static DesiredCapabilities capabilities;
	private static final String APKPATH = "src/test/resources/com/apk";

	public static DesiredCapabilities setAppiumNativeAppCapability() {
		
		File appDir = new File(APKPATH);
		File app = new File(appDir, TEST_PROPERTIES.get("mobileApk"));
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		return capabilities;
	}

	public static DesiredCapabilities setAppiumBrowserCapability() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		return capabilities;
	}

}
