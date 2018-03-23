package priv.starfish.mall.service.util;

import java.util.List;
import java.util.Map;

import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.VolumeUnit;
import priv.starfish.mall.goods.entity.ProductSpecVal;

public class SxUtil {
	// ------------------------------------- 车辆型号 --------------------------------------------
	private static final String ABBR_L = VolumeUnit.dm3.getAbbr();
	private static final int LENGTH_L_DEC = 1;

	/**
	 * 保证排量最多保留一位小数
	 * 
	 * @author koqiui
	 * @date 2015年10月29日 上午1:23:43
	 * 
	 * @param swept
	 * @return
	 */
	public static Double toSweptVol(Double swept) {
		return NumUtil.roundTo(swept, LENGTH_L_DEC);
	}

	/**
	 * 把排量转换为xx.x升(L)
	 * 
	 * @author koqiui
	 * @date 2015年10月29日 上午1:16:49
	 * 
	 * @param swept
	 * @return
	 */
	public static String toSweptVolStr(Double swept) {
		Double rawVal = NumUtil.roundTo(swept, LENGTH_L_DEC);
		//
		Integer intVal = rawVal.intValue();
		Double decVal = rawVal - intVal;
		if (decVal > 0.001) {
			return rawVal.toString() + ABBR_L;
		} else {
			return intVal + ABBR_L;
		}
	}

	// ------------------------------------- 商品/货品 --------------------------------------------
	public static String makeProductTitleX(String goodsName, List<ProductSpecVal> specVals) {
		if (specVals == null || specVals.isEmpty()) {
			return goodsName;
		}
		StringBuilder sb = new StringBuilder();
		for (ProductSpecVal specVal : specVals) {
			sb.append(" ");
			sb.append(specVal.getSpecVal());
		}
		return goodsName + sb.toString();
	}

	public static String makeProductTitle(String goodsName, List<String> specVals) {
		if (specVals == null || specVals.isEmpty()) {
			return goodsName;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < specVals.size(); i++) {
			sb.append(" ");
			sb.append(specVals.get(i));
		}
		return goodsName + sb.toString();
	}

	public static String toSpecificItemIdStr(List<Integer> specItemIds) {
		if (specItemIds == null || specItemIds.isEmpty()) {
			return StrUtil.EmptyStr;
		}
		return StrUtil.join(specItemIds, ",");
	}

	public static String toSpecificStr(Map<String, Integer> specCodeToItemIds) {
		if (specCodeToItemIds == null || specCodeToItemIds.isEmpty()) {
			return StrUtil.EmptyStr;
		}
		//
		Map<String, Integer> sortedMap = TypeUtil.sortMapByKey(specCodeToItemIds, TypeUtil.DictionaryComparator);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(";");
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		return sb.toString();
	}

	public static String toSpecificJson(Map<String, Integer> specCodeToItemIds) {
		if (specCodeToItemIds == null || specCodeToItemIds.isEmpty()) {
			return "{}";
		}
		//
		Map<String, Integer> sortedMap = TypeUtil.sortMapByKey(specCodeToItemIds, TypeUtil.DictionaryComparator);
		return JsonUtil.toJson(sortedMap);
	}
}
