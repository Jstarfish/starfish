package priv.starfish.common.model;

import priv.starfish.common.util.JsonRSACrypter;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 数据载体类型固定的（Map<String, Object>的）结果信息（常与Request配合使用）
 * 
 * @author koqiui
 * @date 2016年2月3日 下午3:06:16
 *
 */
public class ResultX extends Result<Map<String, Object>> {
	public String cryptKey = null;
	//
	public String dataStr = null;

	public ResultX() {
		super();
		//
		this.data = new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public static ResultX newOne() {
		return new ResultX();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResultX [");
		if (requestCode != null && RequestCode.Unknown != requestCode) {
			builder.append("requestCode = " + requestCode);
			builder.append(", requestCodeName = " + requestCodeName);
			builder.append(", ");
		}
		builder.append("type = " + type);
		builder.append(", code = " + code);
		builder.append(", codeName = " + codeName);
		builder.append(", message = " + message);
		builder.append(", data = " + data);
		builder.append(", cryptKey = " + cryptKey);
		builder.append(", dataStr = " + dataStr);
		builder.append("]");

		return builder.toString();
	}

	public ResultX dataItem(String key, Object value) {
		this.data.put(key, value);
		//
		return this;
	}

	public ResultX dataToStr() {
		if (this.cryptKey != null) {
			this.dataStr = JsonRSACrypter.encode(this.data, this.cryptKey);
		} else {
			this.dataStr = JsonUtil.toJson(this.data, true);
		}
		this.data = null;
		//
		return this;
	}

	public ResultX dataFromStr() {
		if (this.cryptKey != null) {
			this.data = JsonRSACrypter.decode(this.dataStr, this.cryptKey);
		} else {
			this.data = JsonUtil.fromJson(this.dataStr, TypeUtil.TypeRefs.StringObjectMapType);
		}
		this.dataStr = null;
		//
		return this;
	}

}
