package com.di.toolkit;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author d
 */
public class JdbcMetaUtil {
	static Connection conn;
	public static String driver, url, username, password;

	public static void setConfig(String url, String username, String password) {
		JdbcMetaUtil.url = url;
		JdbcMetaUtil.username = username;
		JdbcMetaUtil.password = password;
	}

	private static Connection getConn() {
		if (conn == null) {
			Property property = new Property("jdbc.properties");
			if (url == null && url.isEmpty()) {
				url = property.get("jdbc.url");
			}
			if (username == null || username.isEmpty()) {
				username = property.get("jdbc.username");
			}
			if (password == null || password.isEmpty()) {
				password = property.get("jdbc.password");
			}
			if(driver==null||driver.isEmpty()){
				driver = DriverEnum.getByURL(url).name;
			}
			try {
				Driver driver1 = (Driver) Class.forName(driver).newInstance();
				DriverManager.registerDriver(driver1);
				conn = DriverManager.getConnection(url, username, password);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static Table getTable(String tableName) throws SQLException {
		String catalog = getConn().getCatalog();
		Table table = new Table();
		table.setName(tableName);
		// 主键
		ResultSet primaryKeyResultSet = getConn().getMetaData().getPrimaryKeys(null, null, tableName);
		Map<String, String> primaryKeyMap = new HashMap<>();
		while (primaryKeyResultSet.next()) {
			String primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
			primaryKeyMap.put(primaryKeyColumnName, primaryKeyColumnName);
		}
		// 外键
		ResultSet foreignKeyResultSet = getConn().getMetaData().getImportedKeys(catalog, null, tableName);
		Map<String, ImportKey> foreignKeyMap = new HashMap<>();
		while (foreignKeyResultSet.next()) {
			ImportKey importKey = new ImportKey();
			importKey.setName(foreignKeyResultSet.getString("FKCOLUMN_NAM"));
			importKey.setPkTableName(foreignKeyResultSet.getString("PKTABLE_NAME"));
			importKey.setPkColumnName(foreignKeyResultSet.getString("PKCOLUMN_NAME"));
			foreignKeyMap.put(importKey.getName(), importKey);
		}
		// 提取表内的字段的名字和类型
		ResultSet columnSet = getConn().getMetaData().getColumns(null, "%", tableName, "%");
		List<Column> columns = new ArrayList<>();
		List<Column> primaryColumns = new ArrayList<>();
		List<Column> allColumns = new ArrayList<>();
		while (columnSet.next()) {
			Column c = new Column();
			c.setName(columnSet.getString("COLUMN_NAME"));
			c.setType(Type.getBySql(columnSet.getString("TYPE_NAME")));
			c.setNullable(columnSet.getInt("NULLABLE") == 1);
			c.setRemark(columnSet.getString("REMARKS"));
			c.setPrimaryKey(primaryKeyMap.get(c.getName()) != null);
			c.setImportKey(foreignKeyMap.get(c.getName()));
			if (c.isPrimaryKey()) {
				primaryColumns.add(c);
			} else {
				columns.add(c);
			}
			allColumns.add(c);
		}
		table.setColumns(columns);
		table.setPrimaryKeys(primaryColumns);
		table.setAllColumns(allColumns);
		table.setComment(getComment(tableName));
		return table;
	}

	public static List<Table> getAllTables() throws SQLException {
		List<Table> tables = new ArrayList<>();
		String catalog = getConn().getCatalog();
		ResultSet tablesResultSet = getConn().getMetaData().getTables(catalog, null, null, new String[] { "TABLE" });
		while (tablesResultSet.next()) {
			String tableName = tablesResultSet.getString("TABLE_NAME");
			tables.add(getTable(tableName));
		}
		return tables;
	}

	private static String getComment(String table) {
		String comment = "";
		try {
			ResultSet rs = getConn().createStatement().executeQuery("SHOW CREATE TABLE " + table);
			if (rs != null && rs.next()) {
				String create = rs.getString(2);
				int index = create.indexOf("COMMENT='");
				if (index < 0) {
					return "";
				}
				comment = create.substring(index + 9);
				comment = comment.substring(0, comment.length() - 1);
				try {
					comment = new String(comment.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (SQLException e) {
		}
		return comment;
	}

	public static class Table {
		private String name;
		private List<Column> primaryKeys;
		private List<Column> columns;
		private List<Column> allColumns;
		private String comment;

		public List<Column> getAllColumns() {
			return allColumns;
		}

		public void setAllColumns(List<Column> allColumns) {
			this.allColumns = allColumns;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Column> getColumns() {
			return columns;
		}

		public void setColumns(List<Column> columns) {
			this.columns = columns;
		}

		public List<Column> getPrimaryKeys() {
			return primaryKeys;
		}

		public void setPrimaryKeys(List<Column> primaryKeys) {
			this.primaryKeys = primaryKeys;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}
	}

	public static class Column {
		private String name;
		private boolean primaryKey;
		private Type type;
		private String remark;
		private ImportKey importKey;
		private boolean nullable;

		public boolean isNullable() {
			return nullable;
		}

		public void setNullable(boolean nullable) {
			this.nullable = nullable;
		}

		public ImportKey getImportKey() {
			return importKey;
		}

		public void setImportKey(ImportKey importKey) {
			this.importKey = importKey;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isPrimaryKey() {
			return primaryKey;
		}

		public void setPrimaryKey(boolean primaryKey) {
			this.primaryKey = primaryKey;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	public static class ImportKey {
		private String name;
		private String pkTableName;
		private String pkColumnName;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPkTableName() {
			return pkTableName;
		}

		public void setPkTableName(String pkTableName) {
			this.pkTableName = pkTableName;
		}

		public String getPkColumnName() {
			return pkColumnName;
		}

		public void setPkColumnName(String pkColumnName) {
			this.pkColumnName = pkColumnName;
		}
	}

	public static enum Type {
		INT("int", "int"), CHAR("char", "String"), VARCHAR("varchar", "String"), TIME_STAMP("timestamp",
				"java.util.Date"), DATE_TIME("datetime", "java.util.Date"), TINYINT("tinyint", "int"), BIT("bit",
						"boolean"), BIGINT("bigint", "long"), DOUBLE("double",
								"double"), DECIMAL("decimal", "java.math.BigDecimal"), FLOAT("float", "float");
		private String sql;
		private String java;

		private Type(String sql, String java) {
			this.java = java;
			this.sql = sql;
		}

		public String getSql() {
			return sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public String getJava() {
			return java;
		}

		public void setJava(String java) {
			this.java = java;
		}

		public static Type getBySql(String sql) {
			for (Type t : Type.values()) {
				if (t.getSql().equalsIgnoreCase(sql)) {
					return t;
				}
			}
			return Type.VARCHAR;
		}
	}

	public static enum DriverEnum {
		MYSQL("com.mysql.jdbc.Driver","jdbc:mysql"),
		MS_SQL("net.sourceforge.jtds.jdbc.Driver","jdbc:jtds:sqlserver"),
		MS_SQL1("net.sourceforge.jtds.jdbc.Driver","jdbc:jtds:sybase"),
		ORACLE("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin"),
		SYBASE("com.sybase.jdbc2.jdbc.SybDriver","jdbc:sybase:Tds"),
		POSTGRESQL("org.postgresql.Driver","jdbc:postgresql");

		private String name;
		private String prefix;
		
		public String getName() {
			return name;
		}

		public String getPrefix() {
			return prefix;
		}

		private DriverEnum(String name,String prefix) {
			this.name = name;
			this.prefix=prefix;
		}
		
		public static DriverEnum getByURL(String url) {
			for (DriverEnum d : DriverEnum.values()) {
				if (url.startsWith(d.getPrefix())) {
					return d;
				}
			}
			return DriverEnum.MYSQL;
		}
	}
}
