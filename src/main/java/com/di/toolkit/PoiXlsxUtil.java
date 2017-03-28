package com.di.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiXlsxUtil {
	public static List<LinkedHashMap<String, String>> getColumns(String path) throws IOException {
		List<LinkedHashMap<String, String>> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));
		XSSFSheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		XSSFRow row0 = sheet.getRow(0);
		for (int i = 1; i < lastRowNum; i++) {
			XSSFRow row1 = sheet.getRow(i);
			LinkedHashMap<String, String> m = new LinkedHashMap<>();
			for (int j = 0; j < row0.getLastCellNum(); j++) {
				try{
				m.put(row0.getCell(j).getStringCellValue(), row1.getCell(j).getStringCellValue());
				}catch(IllegalStateException ill){
					m.put(row0.getCell(j).getStringCellValue(), row1.getCell(j).getNumericCellValue()+"");
				}
			}
			list.add(m);
		}
		workbook.close();
		return list;
	}

	public static void writeToXlsx(List<LinkedHashMap<String, String>> list,String path) throws FileNotFoundException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row0=sheet.createRow(0);
		int columnIndex=0;
		for(String s:list.get(0).keySet()){
			row0.createCell(columnIndex++).setCellValue(s);;
		}
		int rowindex=1;
		for (LinkedHashMap<String, String> m : list) {
			XSSFRow row1=sheet.createRow(rowindex++);
			int i=0;
			for(String s:m.keySet()){
				row1.createCell(i++).setCellValue(m.get(s));
			}
		}
		File f=new File(path);
		workbook.write(new FileOutputStream(f));
		workbook.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String path="C:/Users/Administrator/Desktop/orders.xlsx";
		List<LinkedHashMap<String, String>> l=getColumns(path);
		writeToXlsx(l, "d:/out.xlsx");
	}
}
