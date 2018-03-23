package priv.starfish.common.repo;

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;

public class FileOpResult {
	public Type type = Result.Type.info;
	public String message = null;
	// 资源注册表信息
	protected String usage;
	protected String relPath;
	protected String uuid;
	// URL信息
	protected String browseUrl;
	protected String deleteUrl;
	// 其他信息
	protected Map<String, Object> extra;
	// 供读取的输入流 <=
	@JsonIgnore
	protected InputStream inputStream;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getRelPath() {
		return relPath;
	}

	public void setRelPath(String relPath) {
		this.relPath = relPath;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getBrowseUrl() {
		return browseUrl;
	}

	public void setBrowseUrl(String browseUrl) {
		this.browseUrl = browseUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
