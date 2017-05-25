package com.di.toolkit;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.di.toolkit.JdbcMetaUtil.Column;
import com.di.toolkit.JdbcMetaUtil.Table;

/**
 * @author d
 */
@SuppressWarnings("unused")
public class MvcGenerater {
	/**
	 * 持久层
	 */
	public static enum PersistenceEnum {
		HIBERNATE("hibernate"), JDBC("jdbc"), JDBC_MAPPER("jdbc_mapper"), JPA("jpa"), IBATIS("ibatis"), MYBATIS(
				"mybatis");
		PersistenceEnum(String name) {
			this.name = name;
		}

		private String name;

		public String getName() {
			return name;
		}

		public static PersistenceEnum getByName(String name) {
			for (PersistenceEnum p : PersistenceEnum.values()) {
				if (p.getName().equalsIgnoreCase(name)) {
					return p;
				}
			}
			return PersistenceEnum.JDBC;
		}
	}

	/**
	 * 控制层
	 */
	public static enum ControlEnum {
		SPRING_MVC("spring_mvc"), STRUTS("struts");
		private String name;

		public String getName() {
			return name;
		}

		ControlEnum(String name) {
			this.name = name;
		}

		public static ControlEnum getByName(String name) {
			for (ControlEnum p : ControlEnum.values()) {
				if (p.getName().equalsIgnoreCase(name)) {
					return p;
				}
			}
			return ControlEnum.SPRING_MVC;
		}
	}

	/**
	 * 视图层
	 */
	public static enum ViewEnum {
		FREEMARKER("freemarker"), JSP("jsp"), VELOCITY("velocity");
		private String name;

		public String getName() {
			return name;
		}

		ViewEnum(String name) {
			this.name = name;
		}

		public static ViewEnum getByName(String name) {
			for (ViewEnum p : ViewEnum.values()) {
				if (p.getName().equalsIgnoreCase(name)) {
					return p;
				}
			}
			return ViewEnum.JSP;
		}
	}

	public MvcGenerater() {
		super();
		path=getPath();
	}

	public MvcGenerater(String url, String username, String password) {
		JdbcMetaUtil.setConfig(url, username, password);
		path=getPath();
	}

	private PersistenceEnum persistence;
	private ControlEnum control;
	private ViewEnum view;

	private Class<?> entityBaseClass;
	private Class<?> mapperBaseClass;
	private Class<?> serviceBaseClass;
	private Class<?> controlBaseClass;

	public MvcGenerater setPersistence(PersistenceEnum persistence) {
		this.persistence = persistence;
		return this;
	}

	public MvcGenerater setControl(ControlEnum control) {
		this.control = control;
		return this;
	}

	public MvcGenerater setView(ViewEnum view) {
		this.view = view;
		return this;
	}

	public <T> MvcGenerater setEntityBaseClass(Class<T> entityBaseClass) {
		this.entityBaseClass = entityBaseClass;
		return this;
	}

	public <T> MvcGenerater setMapperBaseClass(Class<T> mapperBaseClass) {
		this.mapperBaseClass = mapperBaseClass;
		return this;
	}

	public <T> MvcGenerater setServiceBaseClass(Class<T> serviceBaseClass) {
		this.serviceBaseClass = serviceBaseClass;
		return this;
	}

	public <T> MvcGenerater setControlBaseClass(Class<T> controlBaseClass) {
		this.controlBaseClass = controlBaseClass;
		return this;
	}

	private List<Table> tables = new ArrayList<>();

	public MvcGenerater setTables(String... tableNames) {
		try {
			if (tableNames == null || tableNames.length == 0) {
				tables = JdbcMetaUtil.getAllTables();
			} else {
				for (String n : tableNames) {
					tables.add(JdbcMetaUtil.getTable(n));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public MvcGenerater createEntity(String entityPackage) {
		for (Table t : tables) {
			Str s = new Str();
			String className = StringUtil.trimUnderlinedFirstCharUpper(t.getName());
			className = StringUtil.firstCharUpper(className);
			s.add("package ").add(entityPackage).line(";").newLine();
			if (entityBaseClass == null) {
				s.line("/**").add(" * ").line(t.getComment()).line(" * @author MvcGenerator");
				s.add(" * @date ").line(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
				s.line(" */");
				s.add("public class ").add(className).line(" implements Serializable {");
				s.add("	private static final long serialVersionUID = ").add(IdWorker.nextId()).line("L;");
				for (Column c1 : t.getAllColumns()) {
					if (!c1.getRemark().isEmpty()) {
						s.line("    /**").add("	 * ").line(c1.getRemark()).line("	 */");
					}
					s.add("    private ").add(c1.getType().getJava()).add(" ");
					s.add(StringUtil.firstCharLower(StringUtil.trimUnderlinedFirstCharUpper(c1.getName()))).line(";");
				}
			} else {
				s.add("import ").add(entityBaseClass.getName()).line(";");
				s.line("/**").add(" * ").line(t.getComment()).line(" * @author MvcGenerator");
				s.add(" * @date ").line(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
				s.line(" */");
				s.add("public class ").add(className).add(" extends ").add(entityBaseClass.getSimpleName()).line(" {");
				s.add("	private static final long serialVersionUID = ").add(IdWorker.nextId()).line("L;");
				for (Column c1 : t.getAllColumns()) {
					String fn = StringUtil.firstCharLower(StringUtil.trimUnderlinedFirstCharUpper(c1.getName()));
					if (!contain(entityBaseClass, fn)) {
						if (!c1.getRemark().isEmpty()) {
							s.line("    /**").add("	 * ").line(c1.getRemark()).line("	 */");
						}
						s.add("    private ").add(c1.getType().getJava()).add(" ");
						s.add(fn).line(";");
					}
				}
			}
			s.add("}");
			String epath = getPath() + entityPackage.replace(".", "/");
			out(epath + "/" + className + ".java", s.toString());
		}
		return this;
	}

	private boolean contain(Class<?> o, String n) {
		boolean b = false;
		try {
			if (o.getSuperclass() != Object.class) {
				b = contain(o.getSuperclass(), n);
			}
			if (!b)
				b = o.getDeclaredField(n) != null;
		} catch (NoSuchFieldException | SecurityException e) {
		}
		return b;
	}

	public MvcGenerater createMapper(String pack) {
		return this;
	}

	public MvcGenerater createService(String pack) {
		return this;
	}

	public MvcGenerater createControl(String pack) {
		return this;
	}

	public MvcGenerater createView(String path) {
		return this;
	}

	private void out(String path, String content) {
		System.out.println(path);
		FileUtil.writeToFile(path, content);
	}

	String path = null;

	private String getPath() {
		if (path == null) {
			path = "";
			try {
				path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			if (path.indexOf("target") != -1) {
				path = path.substring(0, path.indexOf("target")) + "src/main/java/";
			}
		}
		return path;
	}
}
