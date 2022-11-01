package com.trn.ns.drivers;

import com.trn.ns.enums.DriverType;

public class DriverFactory {

	public IDriver getDriver(String executionType) {


		if (executionType == null) {
			return null;
		}
		if (DriverType.REMOTE.name().equals(executionType)) {
			return new RemoteDriver();
		} else if (DriverType.LOCAL.name().equals(executionType)) {
			return new LocalDriver();
		} else if (DriverType.MOBILE.name().equals(executionType)) {
			return new MobileDriver();
		} else if (DriverType.SAUCE.name().equals(executionType)){
			return new SauceDriver();
		}
		else{
			return new LocalDriver();
		}
	}

}
