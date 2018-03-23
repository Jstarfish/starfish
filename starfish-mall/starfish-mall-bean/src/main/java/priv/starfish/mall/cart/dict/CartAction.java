package priv.starfish.mall.cart.dict;


import priv.starfish.common.annotation.AsEnumVar;

@AsEnumVar()
public enum CartAction {
	add("增加"), minus("减少"), update("更新");

	private CartAction(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}
}
