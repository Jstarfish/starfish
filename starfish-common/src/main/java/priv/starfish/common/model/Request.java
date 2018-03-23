package priv.starfish.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.util.JsonRSACrypter;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;

public class Request {
	public Integer code = RequestCode.Unknown;
	public String codeName = null;
	public String message = null;
	public String cryptKey = null;
	//
	public Map<String, Object> data = new HashMap<String, Object>();
	//
	public String dataStr = null;

	//
	@Override
	public String toString() {
		return "Request [code=" + code + ", codeName=" + codeName + ", message=" + message + ", cryptKey=" + cryptKey + ", data=" + data + ", dataStr=" + dataStr + "]";
	}

	public Request echo() {
		System.out.println(this.toString());
		//
		return this;
	}

	public static Request newOne() {
		return new Request();
	}

	public Request dataItem(String key, Object value) {
		this.data.put(key, value);
		//
		return this;
	}

	public Request dataToStr() {
		if (this.cryptKey != null) {
			this.dataStr = JsonRSACrypter.encode(this.data, this.cryptKey);
		} else {
			this.dataStr = JsonUtil.toJson(this.data, true);
		}
		//
		return this;
	}

	public Request dataFromStr() {
		if (this.cryptKey != null) {
			this.data = JsonRSACrypter.decode(this.dataStr, this.cryptKey);
		} else {
			this.data = JsonUtil.fromJson(this.dataStr, TypeUtil.TypeRefs.StringObjectMapType);
		}
		//
		return this;
	}

	//
	public List<HttpNameValuePair> toHttpNameValuePairs(boolean dataAsStr) {
		List<HttpNameValuePair> nameValuePairs = new ArrayList<HttpNameValuePair>();
		if (this.code != null) {
			nameValuePairs.add(HttpNameValuePair.newOne("code", this.code));
		}
		if (this.codeName != null) {
			nameValuePairs.add(HttpNameValuePair.newOne("codeName", this.codeName));
		}
		if (this.message != null) {
			nameValuePairs.add(HttpNameValuePair.newOne("message", this.message));
		}
		if (this.cryptKey != null) {
			nameValuePairs.add(HttpNameValuePair.newOne("cryptKey", this.cryptKey));
		}
		//
		if (dataAsStr) {
			this.dataToStr();
			//
			nameValuePairs.add(HttpNameValuePair.newOne("dataStr", dataStr));
		} else {
			nameValuePairs.add(HttpNameValuePair.newOne("data", data));
		}
		//
		return nameValuePairs;
	}
}
