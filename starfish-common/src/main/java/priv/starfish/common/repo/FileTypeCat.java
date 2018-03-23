package priv.starfish.common.repo;

/**
 * 文件类型分类
 * 
 * @author koqiui
 * @date 2015年5月22日 上午11:12:32
 *
 */
public enum FileTypeCat {
	Text("文本"), Office("办公"), Image("图片"), Font("字体"), Audio("音频"), Video("视频"), Exec("可执行"), Archive("归档");

	private String text;

	private FileTypeCat(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public String getName() {
		return this.name();
	}

	public String toString() {
		return this.name() + "(" + this.text + ")";
	}
}
