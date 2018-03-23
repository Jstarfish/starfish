package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.User;

@Table(name = "user_verf_status", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "aspect" }) }, desc = "用户验证状态")
public class UserVerfStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc="主键")
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id", desc="用户编号")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 30, nullable = false, desc="验证的方面")
	private VerfAspect aspect;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false", desc="是否验证通过")
	private Boolean flag;
	
	@Column(type = Types.INTEGER, nullable = false, desc="安全等级")
	private Integer secureLevel;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc="时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public VerfAspect getAspect() {
		return aspect;
	}

	public void setAspect(VerfAspect aspect) {
		this.aspect = aspect;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Integer getSecureLevel() {
		return secureLevel;
	}

	public void setSecureLevel(Integer secureLevel) {
		this.secureLevel = secureLevel;
	}

	@Override
	public String toString() {
		return "UserVerfStatus [id=" + id + ", userId=" + userId + ", aspect=" + aspect + ", flag=" + flag + ", secureLevel=" + secureLevel + ", ts=" + ts + "]";
	}

}
