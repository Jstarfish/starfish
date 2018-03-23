package priv.starfish.common.webols;

public class OlsCustomer {
	public String id;
	public String name;
	public String logoUrl;
	
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

	@Override
	public String toString() {
		return "OlsCustomer [id=" + id + ", name=" + name + ", logoUrl=" + logoUrl + "]";
	}

}
