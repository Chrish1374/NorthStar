package com.trn.ns.page.API.factory;

import static io.restassured.RestAssured.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import com.trn.ns.page.constants.OrthancAndAPIConstants;
import com.trn.ns.utilities.Logg;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RESTUtil  {

	private static String path="";
	private static Headers headers;

	protected static final Logger LOGGER = Logg.createLogger();



	public static Response getMethod(String url){		
		return get(url);

	}

	public static void setTokenQuery(String username, String password) {
		path = "?username=" + username + "&password=" +password;
	}

	public static List<Object> getMethod(String basePath,String relativePath,Map<String,String> header,LinkedHashMap<String, String> keyValue){	

		setBaseURI(basePath);
		setBasePath(relativePath);
		String URL=setQueryWithParameter(relativePath, keyValue);
		Response response = with().headers(header).get(URL);
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after getting "+URL);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}
	

	public static List<Object> deleteAPIMethod(String url,HashMap<String,String> header){


		Response response = with().headers(header).delete(url);

		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after deleting URL "+ url);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}

	public static List<Object> deleteAPIMethod(String url){


		Response response = delete(url);		
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after deleting URL "+ url);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}

	public static List<Object> deleteAPIMethod(String basePath,String relativePath,String url, Map<String,String> header){


		setBaseURI(basePath);
		setBasePath(relativePath);				
		Response response = with().headers(header).delete(url);
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after deleting "+relativePath);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}

	public static Response deleteAPIMethod(String basePath,String relativePath,String url){


		setBaseURI(basePath);
		setBasePath(relativePath);				
		Response response = delete(url);
		LOGGER.info("delete API Method called "+url +" and response "+response);
		resetBasePath();
		resetBaseURI();
		return response;
	}
	/*
	 ***Sets Base URI***
	    Before starting the test, we should set the RestAssured.baseURI
	 */
	public static void setBaseURI (String baseURI){
		RestAssured.baseURI = baseURI;
		LOGGER.info("Setting baseURI "+baseURI);
	}

	/*
	 ***Sets base path***
	    Before starting the test, we should set the RestAssured.basePath
	 */
	public static void setBasePath(String basePathTerm){
		RestAssured.basePath = basePathTerm;
		LOGGER.info("Setting basePath "+basePathTerm);
	}

	/*
	 ***Reset Base URI (after test)***
	    After the test, we should reset the RestAssured.baseURI
	 */
	public static void resetBaseURI (){
		RestAssured.baseURI = null;
		LOGGER.info("Reset baseURI");
	}

	/*
	 ***Reset base path (after test)***
	    After the test, we should reset the RestAssured.basePath
	 */
	public static void resetBasePath(){
		RestAssured.basePath = null;
		LOGGER.info("Reset basePath");
	}

	/*
	 ***Sets ContentType***
	    We should set content type as JSON or XML before starting the test
	 */
	public static void setContentType (ContentType Type){
		given().contentType(Type);
		LOGGER.info("Setting contentType "+Type);
	}

	public static Headers setheader(Map<String, String> header) {


		List<Header> headerlist = new ArrayList<Header>();

		for (Entry<String, String> entry : header.entrySet())  
			headerlist.add(new Header(entry.getKey() ,entry.getValue()));

		headers = new Headers(headerlist);
		LOGGER.info("Setting Headers "+header);
		return headers;
	}

	/*
	 ***Returns response***
	    We send "path" as a parameter to the Rest Assured'a "get" method
	    and "get" method returns response of API
	 */
	public static Response getResponse() {

		LOGGER.info("Getting response based on query "+path);
		return get(path);

	}


	public static Response getResponse(String basePath,String relativePath,String url){


		setBaseURI(basePath);
		setBasePath(relativePath);				
		Response response = get(url);
		LOGGER.info("Getting response based on query "+url);

		resetBasePath();
		resetBaseURI();
		return response;
	}

	public static Response getResponse(String basePath,String relativePath){


		setBaseURI(basePath);
		setBasePath(relativePath);				
		Response response = get();
		LOGGER.info("Getting response without parameter");
		resetBasePath();
		resetBaseURI();
		return response;
	}

	public static Response getResponse(String basePath,String relativePath,Map<String,String> header,LinkedHashMap<String, String> keyValue){	

		setBaseURI(basePath);
		setBasePath(relativePath);
		String URL=setQueryWithParameter(relativePath, keyValue);
		Response response = with().headers(header).get(URL);
		resetBasePath();
		resetBaseURI();
		return response;
	}

	/*
	 ***Returns JsonPath object***
	 * First convert the API's response to String type with "asString()" method.
	 * Then, send this String formatted json response to the JsonPath class and return the JsonPath
	 */
	public static JsonPath getJsonPath (Response res) {
		String json = res.asString();
		LOGGER.info("Getting JSONPath"+json);
		return new JsonPath(json);
	}

	public static String getToken(String basePath, String relative, String user, String password) {

		setBaseURI(basePath);
		setBasePath(relative);
		setTokenQuery(user, password);
		String response = getResponse().asString();
		LOGGER.info("Getting token "+ response);
		resetBasePath();
		resetBaseURI();
		return response.replaceAll("\"", "");
	}

	public static Map<String,String> getTokenWithKey(String basePath, String relative, String user, String password, String key){

		String token = getToken(basePath,relative,user,password);

		Map<String, String> keyVal = new HashMap<String,String>();		
		keyVal.put(key, token);
		LOGGER.info("Getting token in key value pair"+ keyVal);
		return keyVal;

	}

	public static String getResponseValue(Response response, String path) {

		String parmVal = getJsonPath(response).get(path).toString();
		LOGGER.info("Getting parameter "+path+" value "+parmVal);

		return parmVal;

	}

	public static String setQueryWithParameter(String pageRelativeURL, LinkedHashMap<String, String> keyValue) {

		String baseURL;
		if(pageRelativeURL.substring(pageRelativeURL.length() - 1).equalsIgnoreCase("&"))
			baseURL = OrthancAndAPIConstants.API_BASE_URL+pageRelativeURL;
		else
			if (pageRelativeURL.contains("?"))
				baseURL = OrthancAndAPIConstants.API_BASE_URL+pageRelativeURL+"&";
			else if(keyValue.size()!=0)
				baseURL = OrthancAndAPIConstants.API_BASE_URL+pageRelativeURL+"?";
			else
				baseURL = OrthancAndAPIConstants.API_BASE_URL+pageRelativeURL;

		List<String> keys = keyValue.keySet().stream().collect(Collectors.toList());

		int i=0;

		for(String key : keys) {

			baseURL = baseURL + key+"="+keyValue.get(key);
			i++;
			if(i<keys.size()) {
				baseURL = baseURL + "&";
			}

		}
		return baseURL;
	}

    public static List<Object> putAPIMethod(String basePath,String relativePath, Map<String,String> header,LinkedHashMap<String, String> keyValue){
		
		setBaseURI(basePath);
		setBasePath(relativePath);
		header.put(OrthancAndAPIConstants.CONTENT_TYPE, OrthancAndAPIConstants.APPLICATION_JSON);
		Response response = with().headers(header).body(keyValue).put();
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after put request "+relativePath);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}
    
    public static List<Object> putAPIMethod(String basePath,String relativePath, String keyValue){
		
		setBaseURI(basePath);
		setBasePath(relativePath);
		Response response = with().headers(OrthancAndAPIConstants.CONTENT_TYPE, OrthancAndAPIConstants.APPLICATION_JSON).body(keyValue).put();
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after put request "+relativePath);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}
    
    
    public static List<Object> postAPIMethod(String basePath,String relativePath, Map<String,String> header,LinkedHashMap<String, String> keyValue){
 		
		setBaseURI(basePath);
		setBasePath(relativePath);
		header.put(OrthancAndAPIConstants.CONTENT_TYPE, OrthancAndAPIConstants.APPLICATION_JSON);
		Response response = with().headers(header).body(keyValue).post();
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after put request "+relativePath);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}
    
    public static List<Object> postAPIMethod(String basePath,String relativePath, Map<String,String> header,String body){
 		
		setBaseURI(basePath);
		setBasePath(relativePath);
		header.put(OrthancAndAPIConstants.CONTENT_TYPE, OrthancAndAPIConstants.APPLICATION_JSON);
		Response response = with().headers(header).body(body).post();
		resetBasePath();
		resetBaseURI();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after post request "+relativePath);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	}

	public static List<Object> postAPIMethod(String url, String relativeURL,String payload){		

		setBaseURI(url);
		setBasePath(relativeURL);
		
		Response response = RestAssured.given().contentType(ContentType.JSON)
				.body(payload).
				when()
				.post()
				.then().extract()
				.response();
		resetBasePath();
		resetBaseURI();
		
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after posting the request "+ url);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	} 

	public static List<Object> importDCMOnOrthanc(String url, String relativeURL,String dcmLocation) throws IOException{		


		InputStream inputStream = new FileInputStream(dcmLocation);
		byte[] dcm = new byte[inputStream.available()];
		inputStream.read(dcm);

		RestAssured.baseURI = url ;

		Response response = RestAssured.given()
				.body(dcm).
				when()
				.post(relativeURL)
				.then().extract()
				.response();
		int statusCode = response.statusCode();
		LOGGER.info("Status code "+statusCode+" received after Importing URL "+ url);
		String responseMessage = response.asString();
		return Arrays.asList(statusCode, responseMessage);
	} 
	
	
	 public static List<Object> deleteAPIMethod(String basePath,String relativePath){
	 		
			setBaseURI(basePath);
			setBasePath(relativePath);
			
			
			Response response = with().delete();
			resetBasePath();
			resetBaseURI();
			int statusCode = response.statusCode();
			LOGGER.info("Status code "+statusCode+" received after put request "+relativePath);
			String responseMessage = response.asString();
			return Arrays.asList(statusCode, responseMessage);
		}
	
	
}