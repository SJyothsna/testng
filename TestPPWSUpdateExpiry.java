package com.mastercard.testing;

import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.mastercard.atmn.test.panprofile.ws.PanProfileWsTestBase;
import com.mastercard.utils.APIHelper;

public class TestPPWSUpdateExpiry extends PPWSTestBase{
	
	private static final Logger logger = LoggerFactory.getLogger(TestPPWSUpdateExpiry.class);
	
	@Test(dataProvider="testDataParser",description="Sample Test")
	public void TestUpdateExpiry(Map<String, String> testData)
	{
		logger.info("Jyothsna here");
		PPWSRequests.updateMerchantsAndValidate(testData);
		System.out.println("hey here");
	}

}
