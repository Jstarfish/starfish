package priv.starfish.common.model;

import java.io.Serializable;

public class ScopeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Integer id;
	private String name;
	private String scope;//mall,shop,wxshop
	private String logoUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public static ScopeEntity newOne() {
		return new ScopeEntity();
	}

	@Override
	public String toString() {
		return "ScopeEntity [id=" + id + ", name=" + name + ", scope=" + scope + ", logoUrl=" + logoUrl + "]";
	}

}
