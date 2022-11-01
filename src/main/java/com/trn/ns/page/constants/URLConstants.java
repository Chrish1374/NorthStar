package com.trn.ns.page.constants;

import com.trn.ns.test.configs.Configurations;

public final class URLConstants {
	
	
	// Configuration page URL
	public static final String BASE_URL = "http://"+Configurations.TEST_PROPERTIES.get("nsHostName")+":"+Configurations.TEST_PROPERTIES.get("nsPort")+"/"+"#/";
	public static final String STUDY_LIST_URL = "studylist";
	public static final String PASSWORD_POLICY_URL = "passwordPolicy";
	public static final String PATIENT_LIST_URL = "patients";
	public static final String LOGIN_PAGE_URL = "login"; 
	public static final String ABOUT_PAGE_URL = "licenseInfo"; 
	public static final String REGISTER_PAGE_URL = "register";
	public static final String USER_PAGE_URL = "user";
	public static final String VIEWER_PAGE_URL = "viewer";
	public static final String CONTACT_ADMINISTRATOR_PAGE_URL = "contactAdministrator";
	public static final String LOGOUT_PAGE_URL = "logout";
	public static final String ACTIVE_DIRECTORY_PAGE_URL = "activeDirectory";
	public static final String GOOGLE_URL ="https://www.google.co.in";
	public static final String HELP_PAGE_URL = "userManual"; 
	public static final String URL_LAUNCH = "urlLaunch";
	

	public static final String RPD_IMAGE_URL = "getinstance";
	public static final String LOSSY_IMAGE_URL = "getlossyimage";
	
	

}


