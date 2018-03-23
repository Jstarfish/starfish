package priv.starfish.common.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.AnnotatedClassScanner;
import priv.starfish.common.util.TypeUtil;

public class SpringHelper {

	@SuppressWarnings("unchecked")
	public static void printRequestMappingUrls(String... packagesToScan) {
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, Controller.class);
		try {
			System.out.println("--  扫描的controller的url路径和方法 -- ");
			Set<Class<?>> controllerClasses = classScanner.getClassSet();
			for (Class<?> controllerClass : controllerClasses) {
				Remark remark = controllerClass.getAnnotation(Remark.class);
				System.out.println(">> " + (remark == null ? "" : remark.value() + " ") + controllerClass.getSimpleName());
				String urlPrefix = "";
				String[] urls = null;
				RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
				if (requestMapping != null) {
					urls = requestMapping.value();
					if (!TypeUtil.isNullOrEmpty(urls)) {
						urlPrefix = urls[0];
					}
				}
				System.out.println(">> " + urlPrefix);
				//
				Method[] methods = controllerClass.getDeclaredMethods();
				for (Method method : methods) {
					// 只提取PUBLIC方法
					if (!Modifier.isPublic(method.getModifiers())) {
						continue;
					}
					String url = "";
					requestMapping = method.getAnnotation(RequestMapping.class);
					if (requestMapping != null) {
						remark = method.getAnnotation(Remark.class);
						// System.out.println("[ " + (description == null ? "" : description.value() + " ") + method.getName() + " ]");
						urls = requestMapping.value();
						if (!TypeUtil.isNullOrEmpty(urls)) {
							for (int i = 0; i < urls.length; i++) {
								url = urls[i];
								System.out.println(urlPrefix + url + (remark == null ? "" : "  " + remark.value()) + (remark == null ? "" : "[" + remark.code() + "]"));
							}
						}
						System.out.println();
					}
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
