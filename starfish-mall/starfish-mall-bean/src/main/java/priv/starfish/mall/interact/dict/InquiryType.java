package priv.starfish.mall.interact.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 商品咨询类型
 * 
 * @author 毛智东
 * @date 2015年7月22日 上午11:00:26
 *
 */
@AsSelectList(name = "inquiryType")
@AsEnumVar()
public enum InquiryType {

	goods("商品咨询", 1), stock("库存及配送", 2), payment("支付问题", 3), invoice("发票及保修", 4), promotion("促销及赠品", 5);

	private String text;
	private Integer value;

	public String getText() {
		return text;
	}

	public Integer getValue() {
		return value;
	}

	InquiryType(String text, Integer value) {
		this.text = text;
		this.value = value;
	}
}
