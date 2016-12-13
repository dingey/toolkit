package com.di.toolkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author di
 */
public class Dom4jUtil {
	public static HashMap<String, Object> xmlStringToMap(String xml) {
		HashMap<String, Object> m = new HashMap<String, Object>();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element r = document.getRootElement();
			m.put(r.getName(), getDate(r));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return m;
	}

	private static Map<String, Object> getDate(Element ele) {
		HashMap<String, Object> m = new HashMap<String, Object>();
		if (ele.attributeCount() > 0) {
			HashMap<String, String> att = new HashMap<String, String>();
			for (int i = 0; i < ele.attributeCount(); i++) {
				Attribute a = ele.attribute(i);
				att.put(a.getName(), a.getValue());
			}
			m.put("attributes", att);
		}
		if (!ele.elements().isEmpty()) {
			HashMap<String, Object> es = new HashMap<String, Object>();
			@SuppressWarnings("rawtypes")
			Iterator it = ele.elementIterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				es.put(e.getName(), getDate(e));
			}
			m.put("elements", es);
		}
		if (!ele.getStringValue().isEmpty()) {
			m.put("value", ele.getText());
		}
		return m;
	}
}
