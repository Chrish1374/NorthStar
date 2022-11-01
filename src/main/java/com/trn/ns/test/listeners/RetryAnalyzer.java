package com.trn.ns.test.listeners;
import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.trn.ns.utilities.Logg;
import com.trn.ns.utilities.Utilities;

public class RetryAnalyzer implements IRetryAnalyzer {
	int counter=0;
	int limit=Integer.parseInt(TEST_PROPERTIES.get("retry"));
	protected static final Logger LOGGER = Logg.createLogger();
	
	@Override
	public boolean retry(ITestResult result) {
		
		/* This method decide how many times test cases need to be retried using a 
		 * configurable variable from application property file
		 * TestNG will call this method every time a test fails. 
		 */ 
		if(counter < limit)
		{   
			LOGGER.info(Utilities.getCurrentThreadId() +"Retrying Test:"+result.getName()+"again and count is "+(counter+1));
			counter++;
			return true;
		}	
		return false;
	}

}
