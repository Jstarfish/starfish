package priv.starfish.common.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;

import priv.starfish.common.http.ContentTypes;
import priv.starfish.common.http.HttpMethod;
import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.http.StringResponseHandler;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;

public class HttpMapResultMappingExecutor extends MappingExecutor {
	private static final String EXTRA_MAP_KEY_URL = "url";
	private static final String EXTRA_MAP_KEY_METHOD = "method";
	// 单例对象
	private static final HttpMapResultMappingExecutor instance = new HttpMapResultMappingExecutor();

	public static HttpMapResultMappingExecutor getInstance() {
		return instance;
	}

	@Override
	public Map<String, Object> execute(List<ParamMapping> ioParamMappings, Object inConext, Map<String, Object> extraMap) {
		// 可以对inContext进行预处理...
		// 根据请求参数映射组装请求参数值
		System.out.println("ioParamMappings :: " + ioParamMappings);
		Map<String, Object> iPararams = this.handleInParams(ioParamMappings, inConext);
		System.out.println("iPararams :: " + iPararams);
		// 请求及响应
		Map<String, Object> outContext = null;
		//
		String url = extraMap.get(EXTRA_MAP_KEY_URL).toString();
		String method = extraMap.get(EXTRA_MAP_KEY_METHOD).toString();
		//
		HttpClientX<String> httpClient = new HttpClientX<String>(new StringResponseHandler());
		//
		httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.APPLICATION_JSON_VALUE));
		//
		try {
			List<HttpNameValuePair> params = new ArrayList<HttpNameValuePair>();
			for (String paramName : iPararams.keySet()) {
				params.add(HttpNameValuePair.newOne(paramName, iPararams.get(paramName)));
			}
			String resultStr = "";
			if (HttpMethod.POST.getValue().equalsIgnoreCase(method)) {
				resultStr = httpClient.doPostRequest(url, params);
			} else {
				resultStr = httpClient.doGetRequest(url, params);
			}
			System.out.println("result :: " + resultStr);
			//
			outContext = JsonUtil.fromJson(resultStr, TypeUtil.TypeRefs.StringObjectMapType);
			//
			System.out.println("outContext :: " + JsonUtil.toFormattedJson(outContext));
		} catch (HttpException e) {
			e.printStackTrace();
		}
		// ...
		// 可以对outContext进行预处理...
		// 根据响应参数映射映射组装响应参数值
		Map<String, Object> oParams = this.handleOutParams(ioParamMappings, outContext);
		System.out.println("oPararams :: " + oParams);

		// 可以对oParams进行后处理（比如转换成自己的数据模型等）...
		return oParams;
	}
}
