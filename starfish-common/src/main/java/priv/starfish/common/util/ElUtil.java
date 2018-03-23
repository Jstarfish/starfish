package priv.starfish.common.util;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * 基于MVEL2的表达式计算工具
 * 
 * @author koqiui
 * @date 2015年5月21日 下午5:21:17
 *
 */
public class ElUtil {

	public static Object eval(String expression) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.eval(expression, null, vars);
	}

	public static Object eval(String expression, Map<String, Object> vars) {
		return MVEL.eval(expression, null, vars);
	}

	public static Object eval(String expression, Object ctx) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.eval(expression, ctx, vars);
	}

	public static Object eval(String expression, Object ctx, Map<String, Object> vars) {
		return MVEL.eval(expression, ctx, vars);
	}

	public static <T> T eval(String expression, Class<T> resultType) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.eval(expression, null, vars, resultType);
	}

	public static <T> T eval(String expression, Object ctx, Class<T> resultType) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.eval(expression, ctx, vars, resultType);
	}

	public static <T> T eval(String expression, Map<String, Object> vars, Class<T> resultType) {
		return MVEL.eval(expression, null, vars, resultType);
	}

	public static <T> T evala(String expression, Object ctx, Map<String, Object> vars, Class<T> resultType) {
		return MVEL.eval(expression, ctx, vars, resultType);
	}

	// ---------------------- shortcuts
	public static Boolean evalToBool(String expression, Object ctx) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.evalToBoolean(expression, ctx, vars);
	}

	public static Boolean evalToBool(String expression, Map<String, Object> vars) {
		return MVEL.evalToBoolean(expression, null, vars);
	}

	public static Boolean evalToBool(String expression, Object ctx, Map<String, Object> vars) {
		return MVEL.evalToBoolean(expression, ctx, vars);
	}

	public static String evalToString(String expression, Object ctx) {
		Map<String, Object> vars = new HashMap<String, Object>();
		return MVEL.evalToString(expression, ctx, vars);
	}

	public static String evalToString(String expression, Map<String, Object> vars) {
		return MVEL.evalToString(expression, null, vars);
	}

	public static String evalToString(String expression, Object ctx, Map<String, Object> vars) {
		return MVEL.evalToString(expression, ctx, vars);
	}
}
