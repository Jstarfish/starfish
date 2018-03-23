package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 楼层类型
 * 
 * @author 郝江奎
 * @date 2016年1月30日 下午14:59:41
 *
 */
@AsSelectList(name = "salesFloorType")
@AsEnumVar()
public enum SalesFloorType {
	good("商品楼层", 0),
	svc("服务楼层", 1),
	other("自定义楼层", -1); 

	private String text;
	
	private int value;

	private SalesFloorType(String text, int value) {
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
