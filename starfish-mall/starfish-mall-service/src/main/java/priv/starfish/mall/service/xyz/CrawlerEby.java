package priv.starfish.mall.service.xyz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.service.CarService;

public class CrawlerEby extends CrawlerBase<Integer> {
	@Override
	public String getSiteDomain() {
		return "www.ebaoyang.cn";
	}

	@Override
	public String getSiteBaseUrl() {
		return "http://www.ebaoyang.cn";
	}

	private CarService carService;

	public void setCarService(CarService carService) {
		this.carService = carService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void extract() {
		assert this.carService != null;
		//
		HttpClientX<String> httpClientBrand = getHttpClient(this.getSiteBaseUrl(), "/basic/car/collectCarModelInfo");
		//
		HttpClientX<String> httpClientSerial = getHttpClient(this.getSiteBaseUrl(), "/basic/car/selectSerial");
		httpClientSerial.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		//
		HttpClientX<String> httpClientModel = getHttpClient(this.getSiteBaseUrl(), "/basic/car/selectModel");
		httpClientModel.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		//		
		String jsonBrands = getForString(httpClientBrand, null, "json");
		println("品牌列表", jsonBrands);
		//
		List<Map<String, Object>> rawBrandsInfo = JsonUtil.fromJson(jsonBrands, TypeUtil.TypeRefs.StringObjectMapListType);
		for (int i = 0; i < rawBrandsInfo.size(); i++) {
			Map<String, Object> rawBrandInfo = rawBrandsInfo.get(i);
			String zm = (String) rawBrandInfo.get("letter");
			System.out.println(zm + " ----------------");
			List<Map<String, Object>> brandsInfo = (List<Map<String, Object>>) rawBrandInfo.get("info");
			for (int i2 = 0; i2 < brandsInfo.size(); i2++) {
				Map<String, Object> brandInfo = brandsInfo.get(i2);
				Integer brandRefId = (Integer) brandInfo.get("id");
				String brandName = (String) brandInfo.get("name");
				String logo = (String) brandInfo.get("logo");
				System.out.println(brandRefId + ", " + brandName + ", " + logo);
				//
				Map<String, Object> brandParams = new HashMap<String, Object>();
				brandParams.put("refId", brandRefId);
				brandParams.put("name", brandName);
				brandParams.put("logo", logo);
				Integer brandId = this.handleBrandInfo(brandParams, zm);
				if (brandId != null) {
					List<HttpNameValuePair> _serialParams = TypeUtil.newList(HttpNameValuePair.class);
					_serialParams.add(HttpNameValuePair.newOne("brand_id", brandRefId));
					String jsonSerials = postForString(httpClientSerial, _serialParams, "json");
					println("系列列表", jsonSerials);
					//
					List<Map<String, Object>> serialsInfo = JsonUtil.fromJson(jsonSerials, TypeUtil.TypeRefs.StringObjectMapListType);
					for (int j = 0; j < serialsInfo.size(); j++) {
						Map<String, Object> serialInfo = serialsInfo.get(j);
						Integer serialRefId = (Integer) serialInfo.get("id");
						String serialName = (String) serialInfo.get("name");
						System.out.println(serialRefId + ", " + serialName);
						//
						Map<String, Object> serialParams = new HashMap<String, Object>();
						serialParams.put("refId", serialRefId);
						serialParams.put("name", serialName);
						Integer serialId = this.handleSerialInfo(serialParams, brandId);
						if (serialId != null) {
							List<HttpNameValuePair> _modelParams = TypeUtil.newList(HttpNameValuePair.class);
							_modelParams.add(HttpNameValuePair.newOne("serial_id", serialRefId));
							String jsonModels = postForString(httpClientModel, _modelParams, "json");
							println("年款列表", jsonModels);
							//
							//
							List<Map<String, Object>> modelsInfo = JsonUtil.fromJson(jsonModels, TypeUtil.TypeRefs.StringObjectMapListType);
							for (int k = 0; k < modelsInfo.size(); k++) {
								Map<String, Object> modelInfo = modelsInfo.get(k);
								Integer modelRefId = (Integer) modelInfo.get("id");
								String modelName = (String) modelInfo.get("name");
								System.out.println(modelRefId + ", " + modelName);
								//
								Map<String, Object> modelParams = new HashMap<String, Object>();
								modelParams.put("refId", modelRefId);
								modelParams.put("name", modelName);
								Integer modelId = this.handleModelInfo(modelParams, serialId);
								// ..
							}
						}
					}
				}

			}
		}
	}

	@Override
	public Integer handleBrandInfo(Map<String, Object> reqParams, String zm) {
		Integer brandId = (Integer) reqParams.get("refId");
		//
		return brandId;
	}

	@Override
	public Integer handleSerialInfo(Map<String, Object> reqParams, Integer brandResult) {
		Integer serialId = (Integer) reqParams.get("refId");
		//
		return serialId;
	}

	@Override
	public Integer handleModelInfo(Map<String, Object> reqParams, Integer serialResult) {
		Integer modelId = (Integer) reqParams.get("refId");
		//
		return modelId;
	}

}
