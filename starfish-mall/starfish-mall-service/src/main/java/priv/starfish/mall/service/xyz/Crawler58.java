package priv.starfish.mall.service.xyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import priv.starfish.common.base.TargetJudger;
import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.car.entity.CarBrand;
import priv.starfish.mall.car.entity.CarModel;
import priv.starfish.mall.car.entity.CarSerial;
import priv.starfish.mall.service.CarService;

/*
 SELECT parent_id, REPLACE(NAME, " ", "") AS NAME , COUNT(1)
 FROM co_car_type
 GROUP BY parent_id, REPLACE(NAME, " ", "")
 HAVING COUNT(1) > 1
 */
public class Crawler58 extends CrawlerBase<Integer> {
	@Override
	public String getSiteDomain() {
		return "58.com";
	}

	@Override
	public String getSiteBaseUrl() {
		// http://post.58.com//1/29/s5
		return "http://post.58.com";
	}

	private final List<Map<String, Object>> _emptyMapList = new ArrayList<Map<String, Object>>();

	private List<Map<String, Object>> getEmptyMapList() {
		return _emptyMapList;
	}

	private Map<String, Object> getRootInfo() {
		String rootParaName = "$.cmcs.rootPara";
		String rootParaJson = null;
		HttpClientX<String> httpClient = getHttpClient(this.getSiteBaseUrl(), "/1/29/s5");
		String html = getForString(httpClient, null, "html");
		Document doc = Jsoup.parse(html);
		if (doc != null) {
			String title = doc.title();
			System.out.println("标题：" + title);
			Element body = doc.body();
			Elements scripts = body.select("script");
			for (Element script : scripts) {
				String content = script.html();
				int index = content.indexOf(rootParaName);
				if (index != -1) {
					int index2 = content.indexOf("\n", index + 1);
					String jsonStr = content.substring(index, index2);
					System.out.println("基础信息：-----------------\n" + jsonStr);
					rootParaJson = extractJsonExpr(jsonStr);
					if (rootParaJson != null) {
						System.out.println(rootParaJson);
						List<Map<String, Object>> rootPara = this.getEmptyMapList();
						rootPara = JsonUtil.fromJson(rootParaJson, TypeUtil.TypeRefs.StringObjectMapListType);
						return CollectionUtil.search(rootPara, new TargetJudger<Map<String, Object>>() {
							@Override
							public boolean isTarget(Map<String, Object> toBeChecked) {
								String name = (String) toBeChecked.get("name");
								return "字母".equals(name);
							}
						});
					}
				}
			}
		}
		return null;
	}

	private CarService carService;

	public void setCarService(CarService carService) {
		this.carService = carService;
	}

	// 提取的级别1：到品牌，2：到车系，3：到车型
	private int levels = 2;

	public void setLevels(int levels) {
		this.levels = levels;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void extract() {
		Map<String, Object> rootInfo = this.getRootInfo();
		System.out.println("品牌Zm基础信息：-----------------\n" + rootInfo);
		List<Map<String, Object>> brandZms = (List<Map<String, Object>>) rootInfo.get("value");
		HttpNameValuePair action = HttpNameValuePair.newOne("action", "getchildrenpara");
		HttpNameValuePair cityid = HttpNameValuePair.newOne("cityid", 1);
		HttpNameValuePair cateid = HttpNameValuePair.newOne("cateid", 29);
		//
		HttpClientX<String> httpClientBrandZm = getHttpClient(this.getSiteBaseUrl(), "/ajax/");
		HttpClientX<String> httpClientBrand = getHttpClient(this.getSiteBaseUrl(), "/ajax/");
		HttpClientX<String> httpClientSerial = getHttpClient(this.getSiteBaseUrl(), "/ajax/");
		//
		String nameId1 = NumUtil.parseInteger(rootInfo.get("nameid").toString()).toString();
		HttpNameValuePair nameidBrandZm = HttpNameValuePair.newOne("nameid", nameId1);
		for (int i = 0; i < brandZms.size(); i++) {
			Map<String, Object> brandZm = brandZms.get(i);
			String zm = brandZm.get("t").toString();
			System.out.println(zm + " ============");
			List<HttpNameValuePair> paramBrandZms = new ArrayList<HttpNameValuePair>();
			paramBrandZms.add(action);
			paramBrandZms.add(cityid);
			paramBrandZms.add(cateid);
			//
			paramBrandZms.add(nameidBrandZm);
			paramBrandZms.add(HttpNameValuePair.newOne("vid", brandZm.get("i")));
			paramBrandZms.add(HttpNameValuePair.newOne("key", brandZm.get("t")));
			// 根据字母获取字母开头对应的品牌信息
			String strRawBrands = getForString(httpClientBrandZm, paramBrandZms, "json");
			// System.out.println(strRawBrands);
			List<Map<String, Object>> rawBrands = this.getEmptyMapList();
			rawBrands = JsonUtil.fromJson(strRawBrands, rawBrands.getClass());
			if (rawBrands != null && rawBrands.size() > 0) {
				Map<String, Object> rawBrand = CollectionUtil.search(rawBrands, new TargetJudger<Map<String, Object>>() {
					@Override
					public boolean isTarget(Map<String, Object> toBeChecked) {
						String name = (String) toBeChecked.get("name");
						return "品牌".equals(name);
					}
				});
				List<Map<String, Object>> brands = rawBrand == null ? this.getEmptyMapList() : (List<Map<String, Object>>) rawBrand.get("value");
				//
				String nameId2 = NumUtil.parseInteger(rawBrand.get("nameid").toString()).toString();
				HttpNameValuePair nameidBrand = HttpNameValuePair.newOne("nameid", nameId2);
				for (int j = 0; j < brands.size(); j++) {
					Map<String, Object> brand = brands.get(j);
					Integer brandId = this.handleBrandInfo(brand, zm);
					if (this.levels < 2 || brandId == null) {
						continue;
					}
					//
					List<HttpNameValuePair> paramsBrand = new ArrayList<HttpNameValuePair>();
					paramsBrand.add(action);
					paramsBrand.add(cityid);
					paramsBrand.add(cateid);
					//
					paramsBrand.add(nameidBrand);
					paramsBrand.add(HttpNameValuePair.newOne("vid", brand.get("i")));
					paramsBrand.add(HttpNameValuePair.newOne("key", brand.get("t")));
					// 根据品牌获取品牌对应的车系
					String strRawSerials = getForString(httpClientBrand, paramsBrand, "json");
					// System.out.println(strRawSerials);
					List<Map<String, Object>> rawSerials = this.getEmptyMapList();
					rawSerials = JsonUtil.fromJson(strRawSerials, rawSerials.getClass());
					if (rawSerials != null && rawSerials.size() > 0) {
						Map<String, Object> rawSerial = CollectionUtil.search(rawSerials, new TargetJudger<Map<String, Object>>() {
							@Override
							public boolean isTarget(Map<String, Object> toBeChecked) {
								String name = (String) toBeChecked.get("name");
								return "车系".equals(name);
							}
						});
						List<Map<String, Object>> serails = rawSerial == null ? this.getEmptyMapList() : (List<Map<String, Object>>) rawSerial.get("value");
						//
						String nameId3 = NumUtil.parseInteger(rawSerial.get("nameid").toString()).toString();
						HttpNameValuePair nameidSerial = HttpNameValuePair.newOne("nameid", nameId3);
						for (int k = 0; k < serails.size(); k++) {
							Map<String, Object> serail = serails.get(k);
							Integer serialId = this.handleSerialInfo(serail, brandId);
							if (this.levels < 3 || serialId == null) {
								continue;
							}
							//
							List<HttpNameValuePair> paramsSerial = new ArrayList<HttpNameValuePair>();
							paramsSerial.add(action);
							paramsSerial.add(cityid);
							paramsSerial.add(cateid);
							//
							paramsSerial.add(nameidSerial);
							paramsSerial.add(HttpNameValuePair.newOne("vid", serail.get("i")));
							paramsSerial.add(HttpNameValuePair.newOne("key", serail.get("t")));
							// 根据车系获取车系对应的车型
							String strRawModels = getForString(httpClientSerial, paramsSerial, "json");
							// System.out.println(strRawModels);
							List<Map<String, Object>> rawModels = this.getEmptyMapList();
							rawModels = JsonUtil.fromJson(strRawModels, rawModels.getClass());
							if (rawModels != null && rawModels.size() > 0) {
								Map<String, Object> rawModel = CollectionUtil.search(rawModels, new TargetJudger<Map<String, Object>>() {
									@Override
									public boolean isTarget(Map<String, Object> toBeChecked) {
										String name = (String) toBeChecked.get("name");
										return "车型".equals(name);
									}
								});
								List<Map<String, Object>> models = rawModel == null ? this.getEmptyMapList() : (List<Map<String, Object>>) rawModel.get("value");
								//
								for (int v = 0; v < models.size(); v++) {
									Map<String, Object> model = models.get(v);
									this.handleModelInfo(model, serialId);
								}
							}
						}

					}

				}
			}
		}
		System.out.println(">> 新插入品牌：" + insertBrands);
		System.out.println(">> 新插入车系：" + insertSerials);
		System.out.println(">> 新插入车型：" + insertModels);
	}

	private String lastBrandName, lastSerialName, lastModelName;
	// private int insertBrands = 0;
	private int insertBrands = 0, insertSerials = 0, insertModels = 0;

	@Override
	public Integer handleBrandInfo(Map<String, Object> reqParams, String zm) {
		Integer srcId = Integer.valueOf(reqParams.get("i").toString());
		String name = reqParams.get("t").toString();// key
		//
		// System.out.println("品牌 => " + name);
		lastBrandName = name;
		CarBrand carType = this.carService.getCarBrandByRefId(srcId);
		if (carType == null) {
			carType = this.carService.getCarBrandByName(name);
		}
		if (carType == null) {
			carType = new CarBrand();
			carType.setName(name);
			carType.setZm(zm.charAt(0));
			carType.setRefId(srcId);
			carType.setRefName(name);
			//
			this.carService.saveCarBrand(carType);

			System.out.println("新增 品牌  => " + lastBrandName);
			insertBrands++;
		} else if (carType.getRefId() == null) {
			carType.setRefId(srcId);
			carType.setRefName(name);
			//
			this.carService.saveCarBrand(carType);

			System.out.println("绑定 品牌 srcId => [" + srcId + "]" + lastBrandName);
		}
		//
		return carType == null || carType.getDisabled() ? null : carType.getId();
	}

	@Override
	public Integer handleSerialInfo(Map<String, Object> reqParams, Integer brandId) {
		Integer srcId = Integer.valueOf(reqParams.get("i").toString());
		String name = reqParams.get("t").toString();// key
		lastSerialName = name;
		CarSerial carType = null;
		if (brandId != null) {
			carType = this.carService.getCarSerialByRefId(srcId);
			if (carType == null) {
				carType = this.carService.getCarSerialByBrandIdAndName(brandId, name);
				if (carType == null) {
					carType = new CarSerial();
					carType.setBrandId(brandId);
					carType.setName(name);
					carType.setRefId(srcId);
					carType.setRefName(name);
					//
					this.carService.saveCarSerial(carType);

					System.out.println("新增 车系 |||=> " + lastBrandName + " \\ " + lastSerialName);
					insertSerials++;
				} else {
					carType.setRefId(srcId);
					carType.setRefName(name);

					this.carService.saveCarSerial(carType);

					System.out.println("绑定 车系 srcId => [" + srcId + "]" + lastBrandName + " \\ " + lastSerialName);
				}
			}
		}
		//
		return carType == null || carType.getDisabled() ? null : carType.getId();
	}

	@Override
	public Integer handleModelInfo(Map<String, Object> reqParams, Integer serialId) {
		// int id58 = Integer.parseInt(reqParams.get("i").toString()); // vid
		Integer srcId = Integer.valueOf(reqParams.get("i").toString());
		String name = reqParams.get("t").toString();// key
		lastModelName = name;
		System.out.println(">>> [" + lastModelName + "]");
		CarModel carType = null;
		if (serialId != null) {
			carType = this.carService.getCarModelByRefId(srcId);
			if (carType == null) {
				carType = this.carService.getCarModelBySerialIdAndName(serialId, name);
				if (carType == null) {
					carType = new CarModel();
					carType.setSerialId(serialId);
					carType.setName(name);
					carType.setRefId(srcId);
					carType.setRefName(name);

					this.carService.saveCarModel(carType);

					System.out.println("新增 车型  => " + lastBrandName + " \\ " + lastSerialName + " \\ " + lastModelName);
					insertModels++;
				} else {
					carType.setRefId(srcId);
					carType.setRefName(name);

					this.carService.saveCarModel(carType);

					System.out.println("绑定 车型 srcId => [" + srcId + "]" + lastBrandName + " \\ " + lastSerialName + " \\ " + lastModelName);
				}
			}
		}
		//
		return carType == null || carType.getDisabled() ? null : carType.getId();
	}

}
