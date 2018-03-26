/*
package priv.starfish.mall.manager.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.AnnotatedClassScanner;
import priv.starfish.common.cms.FreeMarkerService;
import priv.starfish.common.cms.StringFreeMarkerServiceImpl;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.SelectList;
import priv.starfish.common.util.*;
import priv.starfish.mall.web.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@RequestMapping("/js")
public class JsController extends BaseController {
	private FreeMarkerService freeMarkerSvc;
	//
	private long lastModifiedTime = -1L;
	//
	private Map<String, String> jsContentMap = new HashMap<String, String>();

	public JsController() {
		super();
		//
		StringFreeMarkerServiceImpl serviceImpl = new StringFreeMarkerServiceImpl();
		//
		String templateContent = null;
		templateContent = FileHelper.readResourceAsAsString("/template/js/dictSelectLists.ftl");
		serviceImpl.setTemplateContent("js", "dictSelectLists", templateContent);
		//
		templateContent = FileHelper.readResourceAsAsString("/template/js/dictEnumVars.ftl");
		serviceImpl.setTemplateContent("js", "dictEnumVars", templateContent);
		//
		templateContent = FileHelper.readResourceAsAsString("/template/js/imageSizeDefs.ftl");
		serviceImpl.setTemplateContent("js", "imageSizeDefs", templateContent);
		//
		freeMarkerSvc = serviceImpl;
		//TODO: 加密这块先注释了
		//initEncryptJs();
		//
		initDictSelectListsJs();
		//
		initDictEnumVarsJs();
		//
		initImageSizeDefJs();
		//
		lastModifiedTime = System.currentTimeMillis();
	}

	//
	private void initDictSelectListsJs() {
		Map<String, SelectList> dictSelectLists = SelectList.scanDictSelectLists("priv.starfish.mall");
		//
		String[] dictSelectListNames = dictSelectLists.keySet().toArray(new String[0]);
		//
		Map<String, String> dictSelectListJsons = new HashMap<String, String>();
		for (String listName : dictSelectListNames) {
			SelectList selectList = dictSelectLists.get(listName);
			dictSelectListJsons.put(listName, JsonUtil.toJson(selectList, true));
		}
		//
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("dictSelectListNames", dictSelectListNames);
		model.put("dictSelectListJsons", dictSelectListJsons);

		String jsContent = freeMarkerSvc.renderContent("js", "dictSelectLists", model);

		jsContentMap.put("dictSelectLists.js", jsContent);
		//
		logger.debug("dictSelectLists.js >> " + jsContent);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> scanDictEnumVars(String... packagesToScan) {
		Map<String, Map<String, Object>> retEnumVarsMap = new LinkedHashMap<String, Map<String, Object>>();
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, AsEnumVar.class);
		try {
			System.out.println("--  扫描@AsEnumVar  -- ");
			Set<Class<?>> dictClasses = classScanner.getClassSet();
			for (Class<?> dictClass : dictClasses) {
				AsEnumVar anno = dictClass.getAnnotation(AsEnumVar.class);
				//
				String valueField = anno.valueField();
				Map<String, Object> nameValueMap = EnumUtil.toNameValueMap((Class<? extends Enum<?>>) dictClass, valueField);
				//
				String varName = anno.varName();
				if (StrUtil.isNullOrBlank(varName)) {
					varName = dictClass.getSimpleName();
				}
				retEnumVarsMap.put(varName, nameValueMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retEnumVarsMap;
	}

	private void initDictEnumVarsJs() {
		Map<String, Map<String, Object>> retEnumVarsMap = scanDictEnumVars("priv.starfish.mall");
		//
		// System.out.println(JsonUtil.toFormattedJson(retEnumVarsMap));
		//
		String[] enumVarNames = retEnumVarsMap.keySet().toArray(new String[0]);
		//
		Map<String, String> dictEnumVarJsons = new HashMap<String, String>();
		for (String enumVarName : enumVarNames) {
			Map<String, Object> enumMap = retEnumVarsMap.get(enumVarName);
			dictEnumVarJsons.put(enumVarName, JsonUtil.toJson(enumMap, true));
		}
		//
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("enumVarNames", enumVarNames);
		model.put("dictEnumVarJsons", dictEnumVarJsons);

		String jsContent = freeMarkerSvc.renderContent("js", "dictEnumVars", model);

		jsContentMap.put("dictEnumVars.js", jsContent);
		//
		logger.debug("dictEnumVars.js >> " + jsContent);
	}

	//
	private void initImageSizeDefJs() {
		String imageSizeDefs = FileHelper.readResourceAsAsString("/conf/image-size-defs.json");
		//
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("imageSizeDefs", imageSizeDefs);

		String jsContent = freeMarkerSvc.renderContent("js", "imageSizeDefs", model);

		jsContentMap.put("imageSizeDefs.js", jsContent);
		//
		logger.debug("imageSizeDefs.js >> " + jsContent);
	}

	//
	private long encryptMarkedTime = -1L;

	private void initEncryptJs() {
		StringBuilder jsBuilder = new StringBuilder();
		RSACrypter.PublicKeyInfo keyInfo = RSACrypter.getPublicKeyInfo();
		jsBuilder.append("var cryptPubKeyInfo = ");
		jsBuilder.append(JsonUtil.toJson(keyInfo));
		jsBuilder.append(";").append("\n");
		//
		jsBuilder.append("function encryptStr(plain, noEncoding) {").append("\n");
		jsBuilder.append("	var maxDigits = cryptPubKeyInfo.keySize * 2 / 16 + 3;").append("\n");
		jsBuilder.append("	setMaxDigits(maxDigits);").append("\n");
		jsBuilder.append("	var keyPair = new RSAKeyPair(cryptPubKeyInfo.exponent, '', cryptPubKeyInfo.modulus, cryptPubKeyInfo.keySize);").append("\n");
		jsBuilder.append("	var encoded = noEncoding == true ? plain : encodeURIComponent(plain);").append("\n");
		jsBuilder.append("	return encryptedString(keyPair, encoded);").append("\n");
		jsBuilder.append("}").append("\n");
		//
		String jsContent = jsBuilder.toString();
		jsContentMap.put("encrypt.js", jsContent);
		//
		encryptMarkedTime = System.currentTimeMillis();
		//
		logger.debug("encrypt.js >> " + jsContent);
	}

	// ETag方式（集群环境下不太保险）
	// String reqETag = request.getHeader("If-None-Match");
	// response.setHeader("ETag", lastModifiedETag);
	// ------------------------------------------------------------------------------------------
	private ReentrantLock initEncryptJsLock = new ReentrantLock();

@Remark("获取加密脚本")
	@RequestMapping(value = "/encrypt/get", method = RequestMethod.GET)
	public void getEncryptJs(HttpServletRequest request, HttpServletResponse response) {
		if (encryptMarkedTime < RSACrypter.lastLoadTime) {
			// 密钥文件有所改动
			try {
				initEncryptJsLock.lock();
				if (encryptMarkedTime < RSACrypter.lastLoadTime) {
					this.initEncryptJs();
				}
			} finally {
				initEncryptJsLock.unlock();
			}
		}
		//
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		if (headerLastModified >= encryptMarkedTime) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(encryptMarkedTime));
			//
			String jsContent = jsContentMap.get("encrypt.js");
			try {
				PrintWriter writer = WebUtil.responseAsJavascript(response, jsContent);
				writer.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}


	@Remark("获取（用于下拉列表的）枚举字典Js")
	@RequestMapping(value = "/dictSelectLists/get", method = RequestMethod.GET)
	public void getDictSelectListsJs(HttpServletRequest request, HttpServletResponse response) {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		if (headerLastModified >= lastModifiedTime) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(lastModifiedTime));
			//
			String jsContent = jsContentMap.get("dictSelectLists.js");
			try {
				PrintWriter writer = WebUtil.responseAsJavascript(response, jsContent);
				writer.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

	@Remark("获取（用于枚举变量对应于数据库保存值的）枚举项的名称->值Js")
	@RequestMapping(value = "/dictEnumVars/get", method = RequestMethod.GET)
	public void getDictEnumVarsJs(HttpServletRequest request, HttpServletResponse response) {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		if (headerLastModified >= lastModifiedTime) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			response.addDateHeader("Last-Modified", DateUtil.ceilToSecond(lastModifiedTime));
			//
			String jsContent = jsContentMap.get("dictEnumVars.js");
			try {
				PrintWriter writer = WebUtil.responseAsJavascript(response, jsContent);
				writer.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

	@Remark("获取图片尺寸大小Js")
	@RequestMapping(value = "/imageSizeDefs/get", method = RequestMethod.GET)
	public void getImageSizeDefsJs(HttpServletRequest request, HttpServletResponse response) {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		if (headerLastModified >= lastModifiedTime) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(lastModifiedTime));
			//
			String jsContent = jsContentMap.get("imageSizeDefs.js");
			try {
				PrintWriter writer = WebUtil.responseAsJavascript(response, jsContent);
				writer.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

}
*/
