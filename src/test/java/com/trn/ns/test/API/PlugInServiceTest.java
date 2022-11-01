package com.trn.ns.test.API;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.trn.ns.page.API.factory.RESTUtil;
import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.factory.DatabaseMethods;
import com.trn.ns.page.factory.HelperClass;
import com.trn.ns.test.base.TestBase;
import com.trn.ns.test.configs.Configurations;
import com.trn.ns.utilities.DataReader;
import com.trn.ns.utilities.ExtentManager;
import com.trn.ns.utilities.GSONParser;

@Listeners(com.trn.ns.test.listeners.ItestCustomListener.class)
public class PlugInServiceTest extends TestBase{

	String protocolName;
	private ExtentTest extentTest;
	private DatabaseMethods db;

	
	String username=Configurations.TEST_PROPERTIES.get("nsUserName");
	String password=Configurations.TEST_PROPERTIES.get("nsPassword");
	
	String liverFilePath = Configurations.TEST_PROPERTIES.get("Liver_9_filepath");
	String liver9PatientName = DataReader.getPatientDetails(PatientXMLConstants.PATIENT_NAME, liverFilePath);
	

	int totalRequests = 4;
	   
	@BeforeClass(alwaysRun=true)
	public void updatePluginInfo() throws SQLException {
		DatabaseMethods db = new DatabaseMethods(driver);
		db.addPluginInfo(db.toTitleCase(OrthancAndAPIConstants.PLUGIN_NAME), OrthancAndAPIConstants.PLUGIN_TYPE, OrthancAndAPIConstants.PLUGIN_DLL);
		
	}	
	
	@Test(groups ={"Chrome", "IE11", "Edge", "Positive","US2900","US2839","F1254","US2901"})
    public void test01_US2900_TC11233_US2839_TC10925_US2901_TC11136_TC11177_verifyVolumeInfoUsingPluginService() throws SQLException, IOException, ParseException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Plugin service is able to retrieve the Volume information using PluginService."
				+ "<br> Verify instantiation, requesting info and destroying the instance of the plugin using plugin service."
				+ "<br> Re-execute TC10925: Verify instantiation, requesting info and destroying the instance of the plugin using plugin service."
				+ "<br> Verify the log messages generated when a plug-in is initiated.");
			

		db = new DatabaseMethods(driver);
		JSONParser jsonParser = new JSONParser();
		
		for(int i=1;i<=totalRequests;i++)
		{
			
			db.truncateLogTable();
			Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/step"+i+".json"));

			List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+OrthancAndAPIConstants.PLUGIN_NAME, jsonObject.toString());
			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/14]","verifying the response is 200 after sending the request to study");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[2/14]","verifying the response message is not empty");

			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/outputStep"+i+".json"));

			
			HashMap<String, Object> actualMessage = GSONParser.createHashMapFromJsonString(db.getFullMessageFromLogContainsString("%"+NSDBDatabaseConstants.VOLUME+"%"));
			HashMap<String, Object> expectedMessage = GSONParser.createHashMapFromJsonString(jsonObject.toString());
			
			db.assertTrue(actualMessage.equals(expectedMessage),"Checkpoint[3/14]","verifying the logs message");
			db.assertEquals(db.getLogsofEvent(NSDBDatabaseConstants.CREATE_PLUGIN_EVENT).get(0),OrthancAndAPIConstants.CREATE_PLUGIN(OrthancAndAPIConstants.PLUGIN_NAME),"Checkpoint[4/14]","verifying the create plugin event logged in db");
			
			List<String> logs = db.getLogsofEventContainsMsg(OrthancAndAPIConstants.PLUGIN_STARTED_MSG);
			db.assertEquals(logs.get(0),OrthancAndAPIConstants.PLUGIN_SERVICEID,"Checkpoint[5/14]","verifying the service id for plugin started log");
			db.assertTrue(logs.get(1).contains(OrthancAndAPIConstants.PLUGIN_NAME),"Checkpoint[6/14]","verifying the worflow id for plugin started log");
			db.assertEquals(logs.get(2),OrthancAndAPIConstants.PLUGIN_STARTED_MSG,"Checkpoint[7/14]","verifying the message for plugin started log");
						
			
			db.truncateLogTable();
			String guid = response.get(1).toString();
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/update.json"));
			response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid,jsonObject.toString());
			
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/outputUpdate.json"));
			
			actualMessage = GSONParser.createHashMapFromJsonString(response.get(1).toString());
			expectedMessage = GSONParser.createHashMapFromJsonString(jsonObject.toString());

			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[8/14]","verifying the response is 200 after updating the plugin");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[9/14]","verifying the response message is not empty");
			db.assertTrue(actualMessage.equals(expectedMessage),"Checkpoint[10/14]","verifying the response and message logged in db are same for plugin");
			
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.EXECUTE_COMMAND_ASYNC(guid),"Checkpoint[11/14]","verifying log after updating the plugin");
			
			db.truncateLogTable();
			response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid);
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[12/14]","verifying the response is 200");
			db.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[13/14]","verifying the response message is empty");		
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.DESTROY_PLUGIN(guid),"Checkpoint[14/14]","verifying the plugin is destroyed message logged in db");
					
		}
		
		
		
	    
	}
	
	@Test(groups ={"Chrome", "IE11", "Edge","Positive","US2900","US2839","F1254","US2901"})
    public void test02_US2900_TC11277_US2839_TC11043_US2901_TC11137_verifyVolumeInfoUsingWebServer() throws SQLException, IOException, ParseException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the Plugin service is able to retrieve the Volume information using the WebServer."
				+ "<br> Verify instantiation, requesting info and destroying the instance of the plugin using WebServer."
				+ "<br> Re-execute TC11043: Verify instantiation, requesting info and destroying the instance of the plugin using WebServer.");
			

		HelperClass helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, username, password, 1);	
		String authorizationVal = helper.getParamValFromNetworkRequestHeader(OrthancAndAPIConstants.LAYOUT_METHOD, OrthancAndAPIConstants.AUTHORIZATION_KEY);
				
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
		hm.put(OrthancAndAPIConstants.AUTHORIZATION_KEY, authorizationVal);
	
		
		db = new DatabaseMethods(driver);
		JSONParser jsonParser = new JSONParser();
		
		for(int i=1;i<=totalRequests;i++)
		{
			
			db.truncateLogTable();
			Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/step"+i+".json"));

			List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+OrthancAndAPIConstants.PLUGIN_NAME, jsonObject.toString());
			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/14]","verifying the response is 200 after sending request to plugin");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[2/14]","verifying GUID is returned");

			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/outputStep"+i+".json"));

			
			HashMap<String, Object> actualMessage = GSONParser.createHashMapFromJsonString(db.getFullMessageFromLogContainsString("%"+NSDBDatabaseConstants.VOLUME+"%"));
			HashMap<String, Object> expectedMessage = GSONParser.createHashMapFromJsonString(jsonObject.toString());
			
			db.assertTrue(actualMessage.equals(expectedMessage),"Checkpoint[3/14]","verifying the response recieved fro plugin and db are same");
			db.assertEquals(db.getLogsofEvent(NSDBDatabaseConstants.CREATE_PLUGIN_EVENT).get(0),OrthancAndAPIConstants.CREATE_PLUGIN(OrthancAndAPIConstants.PLUGIN_NAME),"Checkpoint[4/14]","verifying the create plugin event in db for plugin");
			
			List<String> logs = db.getLogsofEventContainsMsg(OrthancAndAPIConstants.PLUGIN_STARTED_MSG);
			db.assertEquals(logs.get(0),OrthancAndAPIConstants.PLUGIN_SERVICEID,"Checkpoint[5/14]","verifying the service id for plugin started event");
			db.assertTrue(logs.get(1).contains(OrthancAndAPIConstants.PLUGIN_NAME),"Checkpoint[6/14]","verifying the workflow id for plugin started event");
			db.assertEquals(logs.get(2),OrthancAndAPIConstants.PLUGIN_STARTED_MSG,"Checkpoint[7/14]","verifying the message for plugin started event");
			
			
			db.truncateLogTable();
			String guid = response.get(1).toString();
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/update.json"));
			response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid,jsonObject.toString());
			
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/outputUpdate.json"));
			
			actualMessage = GSONParser.createHashMapFromJsonString(response.get(1).toString());
			expectedMessage = GSONParser.createHashMapFromJsonString(jsonObject.toString());

			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[8/14]","verifying the response is 200 after updating the plugin");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[9/14]","verifying the response message is not empty");
			db.assertTrue(actualMessage.equals(expectedMessage),"Checkpoint[10/14]","verifying the response");
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.EXECUTE_COMMAND_ASYNC(guid),"Checkpoint[11/14]","verifying the db logs");
			
			db.truncateLogTable();
			response = RESTUtil.deleteAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid);
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[12/14]","verifying the response is 200 after sending destroy plugin call");
			db.assertTrue(response.get(1).toString().isEmpty(),"Checkpoint[13/14]","verifying the response message is empty");		
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.DESTROY_PLUGIN(guid),"Checkpoint[14/14]","verifying the db message");
					
		}
		
		
	    
	}
	
	@Test(groups ={"Chrome", "IE11", "Edge","Negative","US2900","US2839","F1254","US2901"})
    public void test03_US2839_TC11074_US2901_TC11140_verifyInvalidPluginInfo() throws SQLException, IOException, ParseException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the error message received with invalid command in json body of the request."
				+ "<br> Re-execute TC11074:  Verify the error message received with invalid command in json body of the request.");
			

		HelperClass helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, username, password, 1);	
		String authorizationVal = helper.getParamValFromNetworkRequestHeader(OrthancAndAPIConstants.LAYOUT_METHOD, OrthancAndAPIConstants.AUTHORIZATION_KEY);
				
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
		hm.put(OrthancAndAPIConstants.AUTHORIZATION_KEY, authorizationVal);
	
		
		db = new DatabaseMethods(driver);
		JSONParser jsonParser = new JSONParser();
		
		for(int i=1;i<=totalRequests;i++)
		{
			
			db.truncateLogTable();
			Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/step"+i+".json"));

			List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+OrthancAndAPIConstants.PLUGIN_NAME, jsonObject.toString());
			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/6]","verifying the response is 200");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[2/6]","verifying the response message is not empty");

						
			db.truncateLogTable();
			String guid = response.get(1).toString();
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/updateInvalidPlugin.json"));
			response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid,jsonObject.toString());
			
		
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.BAD_REQUEST_CODE,"Checkpoint[3/6]","verifying the response is 400 response code if incorrect plugin info sent");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[4/6]","verifying the response message is not empty");
			db.assertEquals(response.get(1).toString(),OrthancAndAPIConstants.NON_SUPPORTED_PLUGIN(guid, db.toTitleCase(OrthancAndAPIConstants.PLUGIN_NAME)),"Checkpoint[5/6]","verifying the response");
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.EXECUTE_COMMAND_ASYNC(guid),"Checkpoint[6/6]","verifying the db logs");
			
					
		}
		
		
		
		
	    
	}
	
	@Test(groups ={"Chrome", "IE11", "Edge","Negative","US2839","F1254","US2901"})
    public void test04_US2839_TC11072_US2901_TC11139_verifyInvalidGUID() throws SQLException, IOException, ParseException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the error message received when Plugin is executed with invalid handler."
				+ "<br> Re-execute TC11072:  Verify the error message received when Plugin is executed with invalid handler.");
			

		HelperClass helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, username, password, 1);	
		String authorizationVal = helper.getParamValFromNetworkRequestHeader(OrthancAndAPIConstants.LAYOUT_METHOD, OrthancAndAPIConstants.AUTHORIZATION_KEY);
				
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
		hm.put(OrthancAndAPIConstants.AUTHORIZATION_KEY, authorizationVal);
	
		db = new DatabaseMethods(driver);
		JSONParser jsonParser = new JSONParser();
		
		for(int i=1;i<=totalRequests;i++)
		{
			
			db.truncateLogTable();
			Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/step"+i+".json"));

			List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+OrthancAndAPIConstants.PLUGIN_NAME, jsonObject.toString());
			
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/6]","verifying the response is 200 after sending request to plugin");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[2/6]","verifying guid is returned in response");

						
			db.truncateLogTable();
			String invalidGuid = response.get(1).toString()+"123"; // updating the guid with incorrect value
			jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/update.json"));
			response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+invalidGuid,jsonObject.toString());
			
		
			db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Checkpoint[3/6]","verifying the response is 404 as incorrect update request is sent with invalid guid");
			db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[4/6]","verifying the response message is not empty");
			db.assertEquals(response.get(1).toString(),OrthancAndAPIConstants.PLUGIN_INSTANCE_NOT_FOUND,"Checkpoint[6/6]","verifying the response message");
			db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.EXECUTE_COMMAND_ASYNC(invalidGuid),"Checkpoint[6/6]","verifying the db logs");
			
					
		}
		
		
		
	    
	}


	@Test(groups ={"Chrome", "IE11", "Edge","US2900", "Negative","F1254","US2839","US2901"})
    public void test05_US2900_TC11250_US2839_TC11040_US2901_TC11138_verifyPluginGettingDestroyed() throws SQLException, IOException, ParseException, InterruptedException  {
		
		extentTest = ExtentManager.getTestInstance();
		extentTest.setDescription("Verify the plugin instance is destroyed when it is left idle for 10 min."
				+ "<br> Verify the plugin instance is destroyed when it is left idle for 10 min."
				+ "<br> Re-execute TC11040: Verify the plugin instance is destroyed when it is left idle for 10 min.");
			

		HelperClass helper = new HelperClass(driver);
		helper.loadViewerDirectly(liver9PatientName, username, password, 1);	
		String authorizationVal = helper.getParamValFromNetworkRequestHeader(OrthancAndAPIConstants.LAYOUT_METHOD, "Authorization");
				
		LinkedHashMap<String, String> hm=new LinkedHashMap<String,String>(); 
		hm.put(OrthancAndAPIConstants.AUTHORIZATION_KEY, authorizationVal);
	
		
		db = new DatabaseMethods(driver);
		JSONParser jsonParser = new JSONParser();
		
		int i =1;
		
		db.truncateLogTable();
		Object jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/step"+i+".json"));

		List<Object> response = RESTUtil.postAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/test",hm, jsonObject.toString());
	
		db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.SUCCESS_API_CODE,"Checkpoint[1/6]","verifying the response is 200 after sending the plugin request");
		db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[2/6]","verifying the guid is returned");
	
		String guid = response.get(1).toString();
		
		db.waitForTimePeriod(600000); // waiting for 10 minutes to system to idle as requires to destroy
		
		db.truncateLogTable();
		jsonObject = jsonParser.parse(new FileReader(OrthancAndAPIConstants.API_JSON_FOLDER_PATH+"TC11233/update.json"));
		response = RESTUtil.putAPIMethod(OrthancAndAPIConstants.PLUGIN_BASE_URL, OrthancAndAPIConstants.PLUGIN_URL+"/"+guid,jsonObject.toString());
				
		db.assertEquals(response.get(0).toString(),OrthancAndAPIConstants.NOT_FOUND_API_CODE,"Checkpoint[3/6]","Sending the request after keeping plugin idle and verifying the response");
		db.assertFalse(response.get(1).toString().isEmpty(),"Checkpoint[4/6]","verifying the response message is not empty");
		db.assertEquals(response.get(1).toString(),OrthancAndAPIConstants.PLUGIN_INSTANCE_NOT_FOUND,"Checkpoint[5/6]","verifying plugin is destroyed");
		db.assertEquals(db.getLogsMessage().get(0),OrthancAndAPIConstants.EXECUTE_COMMAND_ASYNC(guid),"Checkpoint[6/6]","verifying the db logs");
	
		
				
		
		
	    
	}
	
	
	
	
	
}
