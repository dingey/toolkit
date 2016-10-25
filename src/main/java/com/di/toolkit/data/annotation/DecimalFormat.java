package com.di.toolkit.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author di:
 * @date 创建时间：2016年10月25日 下午10:40:58
 * @version
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalFormat {
	String pattern() default ",###.00";
}
