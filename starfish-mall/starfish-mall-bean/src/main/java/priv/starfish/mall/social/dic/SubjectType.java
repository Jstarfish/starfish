package priv.starfish.mall.social.dic;

import priv.starfish.common.annotation.AsEnumVar;

/**
 * 反馈标题类型
 * 
 * @author 邓华锋
 * @date 2016年1月30日 上午11:09:53
 *
 */
@AsEnumVar()
public enum SubjectType {
	mobapp("手机App"), webapp("web站点");

	private String text;


	private SubjectType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
