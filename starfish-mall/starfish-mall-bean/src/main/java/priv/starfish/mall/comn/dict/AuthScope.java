package priv.starfish.mall.comn.dict;

import java.util.HashMap;
import java.util.Map;

import priv.starfish.common.annotation.AsSelectList;
import priv.starfish.common.util.EnumUtil;

@AsSelectList(name = "authScope")
public enum AuthScope {
	sys("系统", false), //
	mall("商城", false), shop("店铺", false), agency("代理处", false), //
	vendor("供应商", true), wxshop("卫星店", true), merchant("商户", true), agent("代理商", true);

	private String text;
	/**
	 * 对应的实体是否为用户扩展而来（比如供应商、卫星店、商户、代理商就是用户扩展而来，他们的id与用户id一致；而商城、店铺、代理处则不是，系统是个特殊个体）
	 */
	private boolean userExtended;

	public boolean isUserExtended() {
		return userExtended;
	}

	private AuthScope(String text, boolean userExtended) {
		this.text = text;
		this.userExtended = userExtended;
	}

	public String getText() {
		return this.text;
	}

	//
	private static Map<String, Boolean> nameToUserExtendedMap = new HashMap<String, Boolean>();

	static {
		Enum<AuthScope>[] enumElems = AuthScope.class.getEnumConstants();
		for (int i = 0; i < enumElems.length; i++) {
			Enum<AuthScope> authScopeX = enumElems[i];
			AuthScope authScope = EnumUtil.valueOf(AuthScope.class, authScopeX.name());
			nameToUserExtendedMap.put(authScope.name(), authScope.isUserExtended());
		}
	}

	public static boolean isUserExtended(String scopeName) {
		return nameToUserExtendedMap.get(scopeName);
	}

	//
	public String toString() {
		return this.name() + "， 用我这个 toString() 你就死定了";
	}

	public static void main(String[] args) {
		// 注意枚举的name 和 toString的区别
		System.out.println(".name() == " + AuthScope.mall.name());
		System.out.println(".toString() == " + AuthScope.mall.toString());

	}
}
