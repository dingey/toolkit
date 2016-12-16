package com.di.toolkit;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author di create by toolkit
 */
public class ClassCreate {
	public static void createClass(Map<String, Object> m, String packag, String path, String name) {
		Str s = new Str();
		s.add("package ").add(packag).line(";").newLine();
		Set<String> imps = getImports(m);
		s.line("import com.di.toolkit.data.annotation.Alias;");
		if (imps != null && imps.size() > 0) {
			for (String s0 : imps) {
				s.add("import ").add(s0).line(";");
			}
			s.newLine();
		}
		s.line("/**").line(" * @author di create by toolkit").line(" */");
		s.add(createInnerClass(m, name));
		s.replaceFirst("static ", "");
		System.out.println(s.toString());
	}

	public static Str createInnerClass(Map<?, ?> m, String name) {
		Str s = new Str();
		if (m.get("element name") != null && !m.get("element name").toString().isEmpty()) {
			name = m.get("element name").toString();
		}
		s.add("@Alias(\"").add(name).add("\")").newLine();
		name = StringUtil.trimUnderlinedFirstCharUpper(name);
		s.add("public static class ").add(StringUtil.firstCharUpper(name)).line(" {");
		Str innerClass = new Str();
		Str getset = new Str();
		for (Object k : m.keySet()) {
			String k0 = StringUtil.trimUnderlinedFirstCharUpper(k.toString());
			Object v = m.get(k);
			if (v == null || "element name".equals(k) || v.toString().isEmpty() || k0.isEmpty()) {
				continue;
			}
			if (k0.equals(k)) {
				s.add("	private ").add(getFieldClassType(k0, v)).add(" ").add(k0).add(";").newLine();
			} else {
				s.add("	@Alias(\"").add(k).line("\")");
				s.add("	private ").add(getFieldClassType(k0, v)).add(" ").add(k0).add(";").newLine();
			}
			if (v.getClass() == java.util.Map.class || v.getClass() == java.util.HashMap.class) {
				innerClass.line(createInnerClass((Map<?, ?>) v, k.toString()).toString());
			}
			getset.add("	public ").add(getFieldClassType(k0, v)).add(" get").add(StringUtil.firstCharUpper(k0))
					.line("() {");
			getset.add("		return ").add(k0).line(";");
			getset.line("	}");
			getset.add("	public void set").add(StringUtil.firstCharUpper(k0)).add("(").add(getFieldClassType(k0, v))
					.add(" ").add(k0).add(") {").newLine();
			getset.add("		this.").add(k0).add(" = ").add(k0).line(";");
			getset.line("	}");
			if (v.getClass() == java.util.List.class || v.getClass() == java.util.ArrayList.class) {
				List<?> vs = (List<?>) v;
				if (vs.size() > 0) {
					Map<?, ?> m0 = (Map<?, ?>) vs.get(0);
					innerClass.add(createInnerClass(m0, name));
				}
			}
		}
		s.add(getset).add(innerClass).add("}");
		return s;
	}

	private static String getFieldClassType(String k, Object o) {
		if (o.getClass() == java.util.List.class || o.getClass() == java.util.ArrayList.class) {
			String n = k.toString();
			List<?> l = (List<?>) o;
			if (!l.isEmpty() && ((Map<?, ?>) l.get(0)).get("element name") != null
					&& !((Map<?, ?>) l.get(0)).get("element name").toString().isEmpty()) {
				n = ((Map<?, ?>) l.get(0)).get("element name").toString();
			} else if (n.endsWith("s")) {
				n = n.substring(0, n.length() - 1);
			} else if (n.endsWith("List")) {
				n = n.substring(0, n.length() - 4);
			}
			return "java.util.List<" + StringUtil.firstCharUpper(n) + ">";
		} else if (o.getClass() == java.util.Map.class || o.getClass() == java.util.HashMap.class) {
			return StringUtil.firstCharUpper(k);
		} else if (o.getClass() == java.lang.String.class) {
			return "String";
		} else {
			return o.getClass().getName();
		}
	}

	private static Set<String> getImports(Map<?, ?> m) {
		return null;
	}

	public static void createFromXml(String xml, String packag, String path) {
		Map<String, Object> map = XmlUtil.toMap(xml);
		createClass(map, packag, path, xml.substring(xml.indexOf("<") + 1, xml.indexOf(">")));
	}

	public static void createFromJson(String json, String packag, String path) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.toObject(json, Map.class);
		createClass(map, packag, path, "root");
	}
}
