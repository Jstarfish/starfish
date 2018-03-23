package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 活动类型
 * 
 * @author koqiui
 * @date 2015年10月26日 上午12:25:49
 *
 */
//type : money("金额"), coupon("优惠券"), goods("商品"), svc("服务"); 
//flag : -1,  0:商品 / 1：服务,  -1,  -1,  
//value  金额数, 优惠券id, 货品id, 服务id  ==> long
@AsSelectList(name = "activityType")
@AsEnumVar()
public enum ActivityType {
	mjje("减免金额", "满减金额"), // 满减
	mzsp("赠送商品", "满赠商品"), mzfw("赠送服务", "满赠服务"), // 满赠
	msyhq("送优惠券", "满送优惠券"), // 满送
	mxzk("享受折扣", "满享折扣"); // 满折

	private ActivityType(String text, String name) {
		this.text = text;
		this.name = name;
	}

	private String text;
	private String name;

	public String getText() {
		return this.text;
	}

	public String getName() {
		return this.name;
	}

}
