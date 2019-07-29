package com.mastercard.testing;

import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import com.mastercard.utils.ExcelUtils;

public class PPWSTestBase {
	
	public JSONObject object;
	
	   @DataProvider
	    public Object[][] testDataParser() throws Exception{
	         Object[][] testObjArray = ExcelUtils.getTableArray("src/test/resources//testdata/Merchant.xlsx","Sheet1");
	         return (testObjArray);

			}

}
