package priv.starfish.mall.comn.misc;

import java.util.List;

import priv.starfish.common.util.TypeUtil;

public class XParamInfo {
	protected static XParam findParamByCodeInList(List<? extends XParam> params, String paramCode) {
		XParam retParam = null;
		if (!TypeUtil.isNullOrEmpty(params)) {
			for (int i = 0; i < params.size(); i++) {
				XParam tmpParam = params.get(i);
				if (tmpParam.getCode().equals(paramCode)) {
					retParam = tmpParam;
					break;
				}
			}
		}
		return retParam;
	}
}
