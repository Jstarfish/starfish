package priv.starfish.mall.xpay.base.error;

import priv.starfish.common.model.Result.Type;

public class ErrorInfo {
	public Type type = Type.error;
	//
	public String code;
	public String message;

	@Override
	public String toString() {
		return "ErrorInfo [type=" + type + ", code=" + code + ", message=" + message + "]";
	}

}
