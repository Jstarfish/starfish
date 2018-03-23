package priv.starfish.mall.comn.misc;

import java.util.List;

import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.StrUtil;

public class SysParamInfo extends XParamInfo {
	/** 参数代码定义 */
	public final static class Code {
		public static final String goodsDefindedOnlyByMall = "goods.definded.only.by.mall";
		public static final String goodsCategLevels = "goods.categ.levels";
	}

	/** 商品是否仅由商城定义 */
	public static boolean goodsDefindedOnlyByMall = false;
	/** 商品分类级数 */
	public static int goodsCategLevels = 3;

	/**
	 * 从实体填充
	 * 
	 * @author koqiui
	 * @date 2015年10月13日 下午3:28:06
	 * 
	 * @param sysParams
	 */
	public static void fromParams(List<? extends XParam> sysParams) {
		XParam tmpParam = null;
		String tmpValue = null;
		//
		tmpParam = findParamByCodeInList(sysParams, Code.goodsDefindedOnlyByMall);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (!StrUtil.isNullOrBlank(tmpValue)) {
				goodsDefindedOnlyByMall = BoolUtil.isTrue(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(sysParams, Code.goodsCategLevels);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (!StrUtil.isNullOrBlank(tmpValue)) {
				goodsCategLevels = Integer.parseInt(tmpValue.trim());
			}
		}
		// ...

	}

}
