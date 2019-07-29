package com.mastercard.testing;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mastercard.utils.APIHelper;
import com.mastercard.utils.AppConstants;

import aQute.bnd.osgi.Constants;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WSRequests {
	private static final Logger logger = LoggerFactory.getLogger(PPWSRequests.class);
	public static RequestSpecification req ;
	public static String 		url = AppConstants.baseURL;
	public static String action;
	public static void healthCheck()
	{
		RestAssured.baseURI=AppConstants.healthCheckURL;
		RequestSpecification req = RestAssured.given();
		Response res = req.request("GET");
		int status = res.getStatusCode();
		System.out.println("Health check Status code :"+status);
	}
	
	public static Response runRequest(JSONObject object)
	{
		action = "PUT";
		buildRequest();
		logger.info("Request being sent :\n"+ req.toString());
		
		Response res =req.body(object.toString())
		.request(action);
		logger.info("Response recieved :\n"+ res.getBody().asString());		
		return res;
	}
	public static void buildRequest()
	{
		req = RestAssured.given();
		req.baseUri(url);
		req.header("Content-Type","application/json")
		.header("consumerId","TEST123");
	}
	public static Response updateMerchants(JSONObject object)
	{
		url = AppConstants.baseURL;
		action = "PUT";
		buildRequest();
		logger.info("Request being sent :\n"+ req.toString());
		
		Response res =req.body(object.toString())
		.request(action);
		logger.info("Response recieved :\n"+ res.getBody().asString());
		
		JsonPath jsonPathEval = res.jsonPath();
		
		int status = res.getStatusCode();
		System.out.println("Update MErchants Status code :"+status);
		res.body().prettyPrint();
		System.out.println("*****"+jsonPathEval.get("name"));
		return res;
	}
	
	public static JSONObject getJsonObject(String template, Map<String, String> testData)
	{
		return APIHelper.buildJSON(template, testData);
			
	}
	public static Response getMerchants(Map<String, String> testData)
	{
		JSONObject object = APIHelper.buildJSON("src/test/resources//testdata/PPWSTemplate.txt", testData);
		return null;
	}
	
	public static void updateMerchantsAndValidate(Map<String, String> testData)
	{
		
		JSONObject object = APIHelper.buildJSON("src/test/resources//testdata/PPWSTemplate.txt", testData);
		Response res = updateMerchants(object);

		String resultToValidate = testData.get("tovalidate");
	    Map<String, String> resultsToValidate = Arrays.stream(resultToValidate.split(","))
	            .map(s -> s.split("="))
	            .collect(Collectors.toMap(s -> s[0], s -> s[1]));
	    logger.info("Tags to verify :\n"+resultToValidate.toString());
	    APIHelper.validateResponse(res, resultsToValidate);
	}
}
