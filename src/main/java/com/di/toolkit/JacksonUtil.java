package com.di.toolkit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * @author di:
 * @date 创建时间：2016年10月23日 下午4:34:20
 * @version
 */
public class JacksonUtil {
	static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static final XmlMapper XML_MAPPER = new XmlMapper();
	static final CsvMapper CSV_MAPPER = new CsvMapper();

	/*
	 * JAVA对象转JSON[JSON序列化]
	 */
	public static String pojoToJson(Object o) {
		String json = null;
		try {
			return OBJECT_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	/*
	 * JSON转Java类[JSON反序列化]
	 */
	public static <T> T jsonToPojo(String json, Class<T> c) {
		T o = null;
		try {
			o = OBJECT_MAPPER.readValue(json, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	public static <T> T xmlToPojo(String xml, Class<T> c) {
		T o = null;
		try {
			o = XML_MAPPER.readValue(xml, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}

	public static String pojoToXml(Object o) {
		String s = null;
		try {
			s = XML_MAPPER.writeValueAsString(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static <T> String pojoToCsv(T o) {
		try {
			CsvSchema csvSchema = CSV_MAPPER.schemaFor(o.getClass());
			return CSV_MAPPER.writer(csvSchema).writeValueAsString(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> csvToPojo(String csvContent, Class<T> clazz) {
		try {
			CsvSchema schema = CSV_MAPPER.schemaFor(clazz);
			MappingIterator<T> mappingIterator = CSV_MAPPER.readerFor(clazz).with(schema).readValues(csvContent);
			return mappingIterator.readAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Map<String, String>> csvWithHeadToMap(String csvContent) {
		try {
			CsvSchema schema = CsvSchema.emptySchema().withHeader();
			MappingIterator<Map<String, String>> mappingIterator = CSV_MAPPER.readerFor(Map.class).with(schema)
					.readValues(csvContent);
			return mappingIterator.readAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> String pojoToCsvWithHead(String[] columns, T o) {
		try {
			StringBuilder sb=new StringBuilder();
			for(String s:columns){
				sb.append(s).append(",");
			}
			CsvSchema csvSchema = CSV_MAPPER.schemaFor(o.getClass());	
			String s=sb.substring(0, sb.length()-1)+System.getProperty("line.separator", "\n")+CSV_MAPPER.writer(csvSchema).writeValueAsString(o);			
			return s;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
