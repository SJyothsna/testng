package com.mastercard.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ExcelUtils {
	private static XSSFSheet sheet;
	private static XSSFWorkbook wb;
	//private static XSSFCell Cell;
	private static XSSFRow Row;
	public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
		String[][] tabArray = null;
		//try {
		//	FileInputStream ExcelFile = new FileInputStream(FilePath);
			FileInputStream fis = new FileInputStream(FilePath);

		    wb = new XSSFWorkbook(fis);
		    sheet = wb.getSheetAt(0);
		    wb.close();
		    int lastRowNum = sheet.getLastRowNum() ;
		    int lastCellNum = sheet.getRow(0).getLastCellNum();
		    Object[][] obj = new Object[lastRowNum][1];

		    for (int i = 0; i < lastRowNum; i++) {
		      Map<Object, Object> datamap = new HashMap<>();
		      for (int j = 0; j < lastCellNum; j++) {
		        datamap.put(getCellData(0,j),getCellData(i+1,j));//sheet.getRow(0).getCell(j).toString(), sheet.getRow(i+1).getCell(j).toString());
		      }
		      obj[i][0] = datamap;

		    }
		    return  obj;

	}
public static String getCellData(int RowNum, int ColNum) throws Exception {
	try{
		Cell cell = sheet.getRow(RowNum).getCell(ColNum);
		String cellText=null;
		if (cell == null || cell.getCellType()==Cell.CELL_TYPE_BLANK)
		{
			cellText = "%DELETE%";
		}
		else
		switch (cell.getCellType())
		{
		case Cell.CELL_TYPE_NUMERIC:
	//		System.out.println("XXXXXXXXXXx"+cell.getNumericCellValue());
			DataFormatter df = new DataFormatter();
			cellText = df.formatCellValue(cell);
		//	System.out.println("ZZZZZZZZZZ"+cellText);
			break;
		case Cell.CELL_TYPE_STRING:
			//System.out.println("String value"+cell.getStringCellValue());
			cellText=cell.getStringCellValue();
			break;
		}
		return cellText;
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			throw (e);
		}
}
}
