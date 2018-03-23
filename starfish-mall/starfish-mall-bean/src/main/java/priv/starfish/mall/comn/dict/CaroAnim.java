package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "caroAnim")
@AsEnumVar()
public enum CaroAnim {
	// 滑动效果
	fadeIn("淡入淡出"), slideLeft("向左滑动"), slideRight("向右滑动"), slideUp("向上滑动"), slideDown("向下滑动");

	private String text;

	private CaroAnim(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

}
