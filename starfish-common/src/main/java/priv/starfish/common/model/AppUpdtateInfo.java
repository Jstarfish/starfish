package priv.starfish.common.model;

import java.util.Date;
import java.util.List;

public class AppUpdtateInfo {
	private Integer versionCode;
	private String versionName;
	private Date lastUpdate;
	private Boolean forced;
	private String fileName;
	private String downloadUrl;
	private String qrCodeUrl;
	private String description;
	private List<String> updates;
	private String copyRight;

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getForced() {
		return forced;
	}

	public void setForced(Boolean forced) {
		this.forced = forced;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getUpdates() {
		return updates;
	}

	public void setUpdates(List<String> updates) {
		this.updates = updates;
	}

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}
}
