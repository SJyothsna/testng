package com.mastercard.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringSubstitutor;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;

import com.ibm.disthub2.impl.util.Assert;
import com.mastercard.atmn.platform.pdm.ws.panprofile.entities.PanProfileReqMerchant;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIHelper {

	public static void readJSONFromFile(String filePath) {

	}

	public static JSONObject buildJSON(String jsonTemplate, Map<String, String> inputData) {
		String jsonAsString = null;
		try {
			jsonAsString = new String(Files.readAllBytes(Paths.get(jsonTemplate)));

			StringSubstitutor strSubstitutor = new StringSubstitutor(inputData);
			jsonAsString = strSubstitutor.replace(jsonAsString);
			System.out.println("String from file:\n"+jsonAsString);
			JSONObject obj = new JSONObject(jsonAsString);
			Object jsonModified = removeNullFields(obj);
		//	PanProfileReqMerchant body=removeNullFields(obj);
			System.out.println("Modified JSON :\n"+jsonModified);
			return (JSONObject) jsonModified;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Object removeNullFields(Object object) throws JSONException {
	    if (object instanceof JSONArray) {
	        JSONArray array = (JSONArray) object;
	        for (int i = 0; i < array.length(); ++i) removeNullFields(array.get(i));
	    } else if (object instanceof JSONObject) {
	        JSONObject json = (JSONObject) object;
	        JSONArray names = json.names();
	        if (names == null) return null;
	        for (int i = 0; i < names.length(); ++i) {
	            String key = names.getString(i);
	            if (json.isNull(key)) {
	                json.remove(key);
	            } else {
	                removeNullFields(json.get(key));
	            }
	            
	            if (json.get(key).equals("%DELETE%")) {
	            	json.remove(key);
	            } else {
	            	removeNullFields(json.get(key));
	            }
	        }
	    }
	    return object;
	}
	
	public static void sendRequest(String method)
	{
		RestAssured.baseURI="http://10.157.129.192:26003/dmp/pan-profile/health/v1/check";
		RequestSpecification req = RestAssured.given();
		Response res = req.request("GET");
		int status = res.getStatusCode();
		System.out.println("Health check Status code :"+status);
	}
	
	public static void validateResponse(Response res,Map<String, String> resultsToValidate)
	{
		JsonPath jsonPathEval = res.jsonPath();
		
		for (Map.Entry<String, String> e:resultsToValidate.entrySet())
		{
			String eValue = e.getValue();
			Object aValue = jsonPathEval.get(e.getKey());
			if (e.getKey().equals("Status"))
			{
				res.then().assertThat().statusCode(Integer.parseInt(eValue));
			}
			if(aValue instanceof Integer)
			{
				aValue = String.valueOf(aValue);
			//	org.testng.Assert.assertEquals(aValue,Integer.parseInt(eValue));
			}
			org.testng.Assert.assertEquals(aValue,eValue,"Failed to validate "+e.getKey());
			System.out.println("Actual Value ="+aValue+"\n Expected Value ="+eValue);
	//		if(!(aValue.equals(eValue)))
		//	{
			//	org.testng.Assert.fail("Actual Value ->"+aValue +"does not match with Expected Value ->"+eValue);
			//}
		}
	}
}
