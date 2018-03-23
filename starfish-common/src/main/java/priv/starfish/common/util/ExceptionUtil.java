/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package priv.starfish.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 * 
 * @author calvin
 */
public class ExceptionUtil {

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException asUnChecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	@SafeVarargs
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... exceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : exceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	public static String extractMsg(Exception ex) {
		String errMsg = null;
		//
		Throwable cause = ex.getCause();
		if (cause == null) {
			errMsg = ex.getMessage();
			if (errMsg == null) {
				errMsg = ex.getClass().getName();
			}
		} else {
			errMsg = cause.getMessage();
			if (errMsg == null) {
				errMsg = cause.getClass().getName();
			}
		}
		return errMsg;
	}
}
