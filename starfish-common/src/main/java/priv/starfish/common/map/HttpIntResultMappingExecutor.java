package priv.starfish.common.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;

import priv.starfish.common.http.*;
import priv.starfish.common.util.ElUtil;

public class HttpIntResultMappingExecutor extends MappingExecutor {
	private static final String EXTRA_MAP_KEY_URL = "url";
	private static final String EXTRA_MAP_KEY_METHOD = "method";
	private static final String RESULT_CODE_KEY = "resultCode";
	// 单例对象
	private static final HttpIntResultMappingExecutor instance = new HttpIntResultMappingExecutor();

	public static HttpIntResultMappingExecutor getInstance() {
		return instance;
	}

	//
	@Override
	public Integer execute(List<ParamMapping> ioParamMappings, Object inConext, Map<String, Object> extraMap) {
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
		httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.TEXT_PLAIN_VALUE));
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
			outContext = new HashMap<String, Object>();
			// outContext.put(RESULT_CODE_KEY, Long.valueOf(resultStr));
			outContext.put(RESULT_CODE_KEY, resultStr);
			//
			// System.out.println("outContext :: " + JsonUtil.toFormattedJson(outContext));
		} catch (HttpException e) {
			e.printStackTrace();
			return -1;
		}
		// ...
		// 可以对outContext进行预处理...
		// 根据响应参数映射映射组装响应参数值
		Map<String, Object> oParams = this.handleOutParams(ioParamMappings, outContext);
		System.out.println("oPararams :: " + oParams);

		// 可以对oParams进行后处理（比如转换成自己的数据模型等）...
		return Integer.valueOf(String.valueOf(oParams.get(RESULT_CODE_KEY)));
	}

	public static void main(String[] args) {
		Map<String, Object> outContext = new HashMap<String, Object>();
		String v = "20151116135225,109\n1001116135225178600";
		outContext.put("resultCode", v);
		StringBuilder sb = new StringBuilder();
		sb.append("Object[] retValues = resultCode.split('\n'); Object status = retValues[0].split(',')[1];");
		String tmp = "if( status == '101') return 101;if( status == '102') return 102;if( status == '103') return 111;if( status == '104') return 111;if( status == '105') return 106;if( status == '106') return 106;if( status == '107') return 107;if( status == '108') return 107;if( status == '109') return 104;if( status == '110') return 111;if( status == '111') return 109;if( status == '116') return 110;if( status == '117') return 111;if( status == '118') return 105;if( status == '119') return 101;if( status == 0) return 100;";
		sb.append(tmp);
		Object value = ElUtil.eval(sb.toString(), outContext);
		System.out.println(value.toString());
	}
}
