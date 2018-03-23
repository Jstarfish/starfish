package priv.starfish.common.repo;

import priv.starfish.common.util.StrUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件类型
 * @author koqiui
 * @date 2015年5月22日 上午11:12:52
 *
 */
public enum FileType {
	// Text("文本文件")
	txt(FileTypeCat.Text, ".txt"), //
	csv(FileTypeCat.Text, ".csv"), //
	xml(FileTypeCat.Text, ".xml", ".xslt"), //
	jsp(FileTypeCat.Text, ".jsp"), //
	json(FileTypeCat.Text, ".json"), //
	html(FileTypeCat.Text, ".html", ".htm"), //
	css(FileTypeCat.Text, ".css"), //
	js(FileTypeCat.Text, ".js"), //
	// Office("办公文档")
	doc(FileTypeCat.Office, ".doc", ".docx", ".wps", ".odt"), //
	xls(FileTypeCat.Office, ".xls", ".xlsx", ".et", ".ods"), //
	ppt(FileTypeCat.Office, ".ppt", ".pptx", ".dps", ".odp"), //
	// Image("图片")
	jpg(FileTypeCat.Image, ".jpg", ".jpeg"), //
	png(FileTypeCat.Image, ".png"), //
	gif(FileTypeCat.Image, ".gif"), //
	bmp(FileTypeCat.Image, ".bmp"), //
	ico(FileTypeCat.Image, ".ico"), //
	// Font("字体")
	eot(FileTypeCat.Font, ".eot"), //
	otf(FileTypeCat.Font, ".otf"), //
	ttf(FileTypeCat.Font, ".ttf"), //
	woff(FileTypeCat.Font, ".woff", ".woff2"), //

	// Archive("归档")
	zip(FileTypeCat.Archive, ".zip"), //
	rar(FileTypeCat.Archive, ".rar"), //
	zip7(FileTypeCat.Archive, ".7zip"), //
	//
	;
	private FileTypeCat cat;
	private String[] exts;

	public FileTypeCat getCat() {
		return cat;
	}

	public String[] getExts() {
		return exts;
	}

	public String getName() {
		return this.name();
	}

	private FileType(FileTypeCat cat, String... exts) {
		this.cat = cat;
		this.exts = exts;
	}

	private static Map<String, FileType> extFileTypeMap;
	static {
		extFileTypeMap = new LinkedHashMap<String, FileType>();
		//
		FileType[] enumElems = FileType.class.getEnumConstants();
		for (int i = 0; i < enumElems.length; i++) {
			FileType fileType = enumElems[i];
			String[] exts = fileType.exts;
			for (String ext : exts) {
				extFileTypeMap.put(ext, fileType);
				// System.out.println(ext + " => " + fileType);
			}
		}
	}

	public static FileType fromExt(String fileText) {
		fileText = fileText.toLowerCase().trim();
		return extFileTypeMap.get(fileText);
	}

	public String toString() {
		return cat + " - " + this.name() + "(" + StrUtil.join(exts) + ")";
	}
}
