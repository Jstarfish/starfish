package priv.starfish.common.repo;

import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Couple;
import priv.starfish.common.util.StrUtil;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class FileRepoConfig {
	public static final String CONFIG_PREFIX = "file.repo.";
	//
	public static final String KEY_ROOT_URL = CONFIG_PREFIX + "root.url";
	public static final String KEY_NO_IMAGE_URL = CONFIG_PREFIX + "image.noimg.url";
	public static final String KEY_DELETE_URL = CONFIG_PREFIX + "delete.url";
	//
	public static final String SUFFIX_BASE_DIR = ".base.dir";
	public static final String SUFFIX_BASE_URL = ".base.url";

	// 文件用途代码
	public static final class Usage {
		public static final String image_goods = "image.goods";
		public static final String image_logo = "image.logo";
		public static final String image_icon = "image.icon";
		public static final String image_act = "image.act";
		public static final String image_advert = "image.advert";
		public static final String image_misc = "image.misc";
		public static final String image_shop = "image.shop";
		public static final String image_license = "image.license";
		public static final String image_head = "image.head";
		public static final String html_product = "html.product";
		public static final String file_misc = "file.misc";
		public static final String image_car = "image.car";
		public static final String image_svc = "image.svc";
		public static final String image_blog = "image.blog";
		public static final String image_photo = "image.photo";

		public static final String[] all = new String[] { image_goods, image_logo, image_icon, image_act, image_advert, image_misc, image_shop, image_license, image_head, image_car, image_svc, image_blog, file_misc, html_product,
				image_photo };
	}

	// 按字符串长度倒排
	private Comparator<String> strLenReverseComparator = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			int ret = o2.length() - o1.length();
			return ret == 0 ? o1.compareTo(o2) : ret;
		}

	};
	// 资源默认根url
	private String rootUrl;

	// 无图的容错图片
	private String noImageUrl;
	// 资源库文件删除url
	private String deleteUrl;

	//
	private Map<String, String> usageBaseDirMap = new LinkedHashMap<String, String>();
	private Map<String, String> usageBaseUrlMap = new LinkedHashMap<String, String>();
	private Map<String, String> baseUrlUsageMap = new TreeMap<String, String>(strLenReverseComparator);

	// 绝对url路径
	private String getAbsoluteUrl(String url) {
		return this.rootUrl + url;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
		System.out.println(">> FileRepoConfig rootUrl 设置为：" + rootUrl);
	}

	//
	public String getNoImageUrl() {
		return noImageUrl;
	}

	public void setNoImageUrl(String noImageUrl) {
		if (StrUtil.isNullOrBlank(noImageUrl)) {
			return;
		}
		//
		if (!noImageUrl.startsWith("http")) {
			noImageUrl = this.getAbsoluteUrl(noImageUrl);
		}
		this.noImageUrl = noImageUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		if (StrUtil.isNullOrBlank(deleteUrl)) {
			return;
		}
		//
		this.deleteUrl = deleteUrl;
	}

	public void setBaseDir(String usage, String baseDir) {
		if (StrUtil.isNullOrBlank(baseDir)) {
			return;
		}
		//
		this.usageBaseDirMap.put(usage, baseDir);
		// 确保目录已创建
		FileHelper.makeDirs(baseDir);
	}

	public String getBaseDir(String usage) {
		return this.usageBaseDirMap.get(usage);
	}

	public boolean hasBaseDir(String usage) {
		return usage == null ? false : this.usageBaseDirMap.containsKey(usage);
	}

	public void setBaseUrl(String usage, String baseUrl) {
		if (StrUtil.isNullOrBlank(baseUrl)) {
			return;
		}
		//
		this.usageBaseUrlMap.put(usage, this.getAbsoluteUrl(baseUrl));
		//
		this.baseUrlUsageMap.put(baseUrl, usage);
	}

	public String getBaseUrl(String usage) {
		return this.usageBaseUrlMap.get(usage);
	}

	public boolean hasBaseUrl(String usage) {
		return usage == null ? false : this.usageBaseUrlMap.containsKey(usage);
	}

	public Couple<String, String> parseUrlUsageRelPath(String urlPath) {
		Couple<String, String> retCouple = null;
		if (urlPath != null) {
			for (Entry<String, String> baseUrlUsage : this.baseUrlUsageMap.entrySet()) {
				String baseUrl = baseUrlUsage.getKey();
				if (urlPath.startsWith(baseUrl + "/")) {
					retCouple = Couple.newOne();
					retCouple.setFirst(baseUrlUsage.getValue());
					retCouple.setSecond(urlPath.substring(baseUrl.length() + 1));
					break;
				}
			}
		}
		return retCouple;
	}
}
