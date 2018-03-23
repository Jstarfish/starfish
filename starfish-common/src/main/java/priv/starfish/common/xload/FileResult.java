package priv.starfish.common.xload;

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.MapContext;

public class FileResult {
	public Type type = Result.Type.info;
	public String message = null;
	//
	protected String originalFileName;
	// 文件名称
	protected String fileName;
	// 文件大小
	protected Long fileSize;
	// 资源文件注册表信息
	protected String fileUuid;
	protected String fileUsage;
	protected String fileRelPath;
	// 资源文件url信息
	protected String fileBrowseUrl;
	protected String fileDeleteUrl;

	// 其他信息
	protected Map<String, Object> extra;
	// 供读取的输入流 <=
	@JsonIgnore
	protected InputStream inputStream;

	//
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

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(String fileUuid) {
		this.fileUuid = fileUuid;
	}

	public String getFileUsage() {
		return fileUsage;
	}

	public void setFileUsage(String fileUsage) {
		this.fileUsage = fileUsage;
	}

	public String getFileRelPath() {
		return fileRelPath;
	}

	public void setFileRelPath(String fileRelPath) {
		this.fileRelPath = fileRelPath;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getFileDeleteUrl() {
		return fileDeleteUrl;
	}

	public void setFileDeleteUrl(String fileDeleteUrl) {
		this.fileDeleteUrl = fileDeleteUrl;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = new MapContext(extra);
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
