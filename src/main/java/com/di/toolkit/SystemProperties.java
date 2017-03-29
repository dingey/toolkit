package com.di.toolkit;

/**
 * @author di
 */
public class SystemProperties {
	/**
	 * 用户的当前工作目录
	 */
	public static String JAVA_VERSION=System.getProperty("java.version");
	/**
	 * 默认的临时文件路径
	 */
	public static String JAVA_IO_TMPDIR=System.getProperty("java.io.tmpdir");
	/**
	 * 要使用的 JIT 编译器的名称
	 */
	public static String JAVA_COMPILER=System.getProperty("java.compiler");
	/**
	 * 操作系统的名称
	 */
	public static String OS_NAME=System.getProperty("os.name");
	/**
	 * 操作系统的架构
	 */
	public static String OS_ARCH=System.getProperty("os.arch");
	/**
	 * 操作系统的版本
	 */
	public static String OS_VERSION=System.getProperty("os.version");
	/**
	 * 文件分隔符（在 UNIX 系统中是“/”）
	 */
	public static String FILE_SEPARATOR=System.getProperty("file.separator");
	/**
	 * 路径分隔符（在 UNIX 系统中是“:”）
	 */
	public static String PATH_SEPARATOR=System.getProperty("path.separator");
	/**
	 * 行分隔符（在 UNIX 系统中是“/n”）
	 */
	public static String LINE_SEPARATOR=System.getProperty("line.separator");
	/**
	 * 用户的账户名称
	 */
	public static String USER_NAME=System.getProperty("user.name");
	/**
	 * 用户的主目录
	 */
	public static String USER_HOME=System.getProperty("user.home");
	/**
	 * 用户的当前工作目录
	 */
	public static String USER_DIR=System.getProperty("user.dir");
}
