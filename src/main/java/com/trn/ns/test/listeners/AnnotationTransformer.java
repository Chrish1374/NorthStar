package com.trn.ns.test.listeners;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformer implements IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass,
			Constructor testConstructor, Method testMethod) {
		/* This method is used to set Retry annotation for 
		 * every test method at run time
		 */
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
		
	}

}
