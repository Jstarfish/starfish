package priv.starfish.common.xload;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

public class FileProviderMgr {
	private final Log logger = LogFactory.getLog(this.getClass());
	//
	private String name = "<未命名文件提供者管理器>";
	private List<FileProvider> fileProviders = new CopyOnWriteArrayList<FileProvider>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProviderClassNames(String[] providerClassNames) {
		this.setProviderClassNames(TypeUtil.arrayToList(providerClassNames));
	}

	@SuppressWarnings("unchecked")
	public void setProviderClassNames(List<String> providerClassNames) {
		for (String className : providerClassNames) {
			if (StrUtil.isNullOrBlank(className)) {
				continue;
			}
			try {
				className = className.trim();
				//
				Class<FileProvider> clazz = (Class<FileProvider>) Class.forName(className);
				FileProvider instance = clazz.newInstance();
				this.addProvider(instance);
			} catch (ClassNotFoundException e) {
				logger.warn("未找到指定的类：" + className);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setProviders(List<FileProvider> providers) {
		if (providers == null) {
			return;
		}
		for (int i = 0; i < providers.size(); i++) {
			this.addProvider(providers.get(i));
		}
	}

	// 添加文件提供者
	public boolean addProvider(FileProvider provider) {
		if (provider == null || fileProviders.contains(provider)) {
			return false;
		}
		fileProviders.add(provider);
		System.out.println("文件提供者已添加：" + provider.getName());
		return true;
	}

	public boolean removeProvider(FileProvider provider) {
		if (provider == null || !fileProviders.contains(provider)) {
			return false;
		}
		fileProviders.remove(provider);
		System.out.println("文件提供者已移除：" + provider.getName());
		return true;
	}

	public FileProvider pick(Map<String, Object> infoMap) {
		int count = fileProviders.size();
		for (int i = 0; i < count; i++) {
			FileProvider provider = fileProviders.get(i);
			if (provider != null && provider.willHandle(infoMap)) {
				return provider;
			}
		}
		return null;
	}
}
