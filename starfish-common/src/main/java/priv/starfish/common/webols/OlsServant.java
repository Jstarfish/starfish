package priv.starfish.common.webols;

public class OlsServant {
	public String id;
	public String name;
	public String logoUrl;
	public String no;
	public String orgName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
	public String toString() {
		return "OlsServant [id=" + id + ", name=" + name + ", logoUrl=" + logoUrl + ", no=" + no + ", orgName=" + orgName + "]";
	}

	
}
