package priv.starfish.mall.agent.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

/**
 * 代理商实体
 * 
 * @author WJJ
 * @date 2015年9月10日 下午3:38:42
 *
 */
@Table(name = "agent")
public class Agent implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键==用户Id */
	@Id(auto = false, type = Types.INTEGER)
	private Integer id;

	/** 用户 */
	private User user;

	/** 是否不可用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 备注 */
	@Column(type = Types.VARCHAR, length = 250)
	private String memo;

	/** 代理处集合 */
	private List<Agency> agencies;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Agency> getAgencies() {
		return agencies;
	}

	public void setAgencies(List<Agency> agencies) {
		this.agencies = agencies;
	}

	@Override
	public String toString() {
		return "Agent [id=" + id + ", disabled=" + disabled + ", memo=" + memo + ", agencies=" + agencies + ", user=" + user + "]";
	}

}
