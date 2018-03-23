package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 活动对象
 * 
 * @author 郝江奎
 * @date 2016年1月28日 下午3:17:53
 *
 */
@AsSelectList(name = "activityTarget")
@AsEnumVar()
public enum ActivityTarget {
	All("所有人", -1),
	Member("会员", 0),
	Shop("加盟店", 1), 
	WxShop("卫星店", 2); 

	private String text;
	
	private int value;

	private ActivityTarget(String text, int value) {
		this.text = text;
		this.value = value;
	}
	
	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}

}
