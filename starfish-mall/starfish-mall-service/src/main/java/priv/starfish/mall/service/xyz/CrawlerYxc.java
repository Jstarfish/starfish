package priv.starfish.mall.service.xyz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
@SuppressWarnings("unused")
public class CrawlerYxc extends CrawlerBase<Integer> {

	@Override
	public String getSiteDomain() {
		return "yixiuche.com";
	}

	@Override
	public String getSiteBaseUrl() {
		return "http://www.yixiuche.com";
	}

	private Map<String, Object> getRootInfo() {
		Map<String, Object> retMap = null;
		HttpClientX<String> httpClient = getHttpClient(this.getSiteBaseUrl(), "/index.html");
		String html = getForString(httpClient, null, "html");
		// this.println("html", html);
		Document doc = Jsoup.parse(html);
		if (doc != null) {
			retMap = new LinkedHashMap<String, Object>();
			//String title = doc.title();
			// this.println("标题", title);
			Element body = doc.body();
			Element pinpaiDiv = body.select("div.pinpai").first();
			// this.println("品牌div", pinpaiDiv.outerHtml());
			Elements ulNodes = pinpaiDiv.select("ul");
			for (Element ulNode : ulNodes) {
				Element aNode = ulNode.select("a").first();
				String serieses = aNode.attr("keyvalue");
				// this.println("<a> 车系 keyvalue", serieses);
				Element spanNdoe = ulNode.select("li>span").first();
				String pinpaiName = spanNdoe.text().trim();
				// this.println("<span> 品牌名称", pinpaiName);
				retMap.put(serieses, pinpaiName);
			}
		}
		return retMap;
	}

	private String extractModelId(String jsText) {
		// selectcar(163944)
		int ind1 = jsText.indexOf("(");
		int ind2 = jsText.lastIndexOf(")");
		return jsText.substring(ind1 + 1, ind2).trim();
	}

	@Override
	public void extract() {
		// 获取品牌列表html
		Map<String, Object> brandsInfo = getRootInfo();
		if (brandsInfo == null) {
			return;
		}
		this.println("品牌信息", brandsInfo);
		HttpNameValuePair action = HttpNameValuePair.newOne("action", "selectcar");
		HttpNameValuePair ajaxcardata = HttpNameValuePair.newOne("ajaxcardata", "ajaxcardata");
		HttpNameValuePair ajax = HttpNameValuePair.newOne("ajax", "ajax");
		// 请求车系列表html
		HttpClientX<String> httpClientSerial = getHttpClient(this.getSiteBaseUrl(), "/ac/carmodel.html");
		List<HttpNameValuePair> paramsSerial = new ArrayList<HttpNameValuePair>();
		paramsSerial.add(action);
		paramsSerial.add(ajaxcardata);
		paramsSerial.add(ajax);
		HttpNameValuePair getserieses = HttpNameValuePair.newOne("getserieses", null);
		paramsSerial.add(getserieses);
		//
		Set<String> seriesesSet = brandsInfo.keySet();
		for (String serieses : seriesesSet) {
			String brandName = (String) brandsInfo.get(serieses);
			//
			Map<String, Object> brand = new HashMap<String, Object>();
			brand.put("name", brandName);
			brand.put("serialNo", serieses);
			this.handleBrandInfo(brand, null);
			//
			paramsSerial.remove(paramsSerial.size() - 1);
			paramsSerial.add(HttpNameValuePair.newOne("getserieses", serieses));
			String strRawSerials = getForString(httpClientSerial, paramsSerial, "html");
			Document docSerials = Jsoup.parseBodyFragment(strRawSerials);
			Elements serialNodes = docSerials.select("ul>a, ul>li");
			String serialPrefix = "";
			// 请求车型列表html
			HttpClientX<String> httpClientModel = getHttpClient(this.getSiteBaseUrl(), "/ac/carmodel.html");
			List<HttpNameValuePair> paramsModel = new ArrayList<HttpNameValuePair>();
			paramsModel.add(action);
			paramsModel.add(ajaxcardata);
			paramsModel.add(ajax);
			HttpNameValuePair getengines = HttpNameValuePair.newOne("getengines", null);
			paramsModel.add(getengines);
			//
			for (Element serialNode : serialNodes) {
				if (serialNode.tagName().equalsIgnoreCase("li")) {
					serialPrefix = serialNode.select("span").first().text().trim();
				} else if (serialNode.tagName().equalsIgnoreCase("a")) {
					String engines = serialNode.attr("keyvalue");
					// this.println("<a> 引擎（车型） keyvalue", engines);
					Element spanNdoe = serialNode.select("li>span").first();
					String serialName = spanNdoe.text().trim();
					// this.println("<span> 车系名称", brandName + " \\ " + serialPrefix + " " + serialName);
					//
					Map<String, Object> serial = new HashMap<String, Object>();
					serial.put("prefix", serialPrefix);
					serial.put("name", serialName);
					serial.put("engineNo", engines);
					this.handleSerialInfo(brand, null);
					//
					paramsModel.remove(paramsModel.size() - 1);
					paramsModel.add(HttpNameValuePair.newOne("getengines", engines));
					String strRawModels = getForString(httpClientModel, paramsModel, "html");
					Document docModels = Jsoup.parseBodyFragment(strRawModels);
					Elements modelNodes = docModels.select("ul>a, ul>li");
					String modelPrefix = "";
					//
					HttpClientX<String> httpClientManual = getHttpClient(this.getSiteBaseUrl(),
							"/packageproduct/detail/");
					for (Element modelNode : modelNodes) {
						if (modelNode.tagName().equalsIgnoreCase("li")) {
							modelPrefix = modelNode.select("span").first().text().trim();
						} else if (modelNode.tagName().equalsIgnoreCase("a")) {
							String clickText = modelNode.attr("onclick");
							String modelId = extractModelId(clickText);
							this.println("<a> 车型（手册id） clickText", clickText + "[" + modelId + "]");
							spanNdoe = modelNode.select("li>span").first();
							String modelName = spanNdoe.text().trim();
							this.println("<span> 车型名称", brandName + " \\ " + serialPrefix + " " + serialName + " \\ "
									+ modelPrefix + " " + modelName);
							//
							Map<String, Object> model = new HashMap<String, Object>();
							model.put("prefix", modelPrefix);
							model.put("name", modelName);
							model.put("modelId", modelId);
							this.handleModelInfo(model, null);
							//
							String strManualPage = getForString(httpClientManual, modelId + "/1.html", paramsModel,
									"html");							
							Document docManual = Jsoup.parse(strManualPage);
							if (docManual != null) {
								String title = docManual.title();
								this.println("保养手册标题", title);
								Element body = docManual.body();
								Element handbookDiv = body.select("div.maintain-handbook").first();
								this.println("保养手册html", handbookDiv.outerHtml());
							}
						}
					}
				}
			}
		}
	}
	
	
	private static final String Indicator = "●";
	private void handleManualInfo(){
		//
	}

	@Override
	public Integer handleBrandInfo(Map<String, Object> reqParams, String zm) {

		return null;
	}

	@Override
	public Integer handleSerialInfo(Map<String, Object> reqParams, Integer brandResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer handleModelInfo(Map<String, Object> reqParams, Integer serialResult) {
		// TODO Auto-generated method stub
		return null;
	}

}
