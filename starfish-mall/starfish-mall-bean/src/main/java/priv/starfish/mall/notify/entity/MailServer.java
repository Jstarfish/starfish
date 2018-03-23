package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.notify.dict.MailType;

/**
 * 邮箱服务器
 * 
 * @author 毛智东
 * @date 2015年5月18日 下午3:44:09
 *
 */
@Table(name = "mail_server", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "邮箱服务器表")
public class MailServer implements Serializable {

	private static final long serialVersionUID = -1173508971133828375L;
	// 主键
	@Id(type = Types.INTEGER)
	private Integer id;
	// 邮箱服务器名字，唯一
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;
	// 邮箱类型：POP3, IMAP, Exchange
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private MailType type;
	// 邮件账号
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String account;
	// 密码
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String password;
	// 展示名字就是发信名称
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String dispName;
	// 收件服务器
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String recvServer;
	// 收件服务器SSL
	@Column(type = Types.BOOLEAN)
	private Boolean recvSSL;
	// 收件服务器端口号
	@Column(type = Types.INTEGER)
	private Integer recvPort;
	// 发件服务器
	@Column(type = Types.VARCHAR, length = 30)
	private String sendServer;
	// 发件服务器SSL
	@Column(type = Types.BOOLEAN)
	private Boolean sendSSL;
	// 发件服务器端口号
	@Column(type = Types.INTEGER)
	private Integer sendPort;
	// 是否可用
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean enabled;
	// 时间戳
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	// 发送邮件测试地址
	private String mailAddres;

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

	public MailType getType() {
		return type;
	}

	public void setType(MailType type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public String getRecvServer() {
		return recvServer;
	}

	public void setRecvServer(String recvServer) {
		this.recvServer = recvServer;
	}

	public Boolean getRecvSSL() {
		return recvSSL;
	}

	public void setRecvSSL(Boolean recvSSL) {
		this.recvSSL = recvSSL;
	}

	public Integer getRecvPort() {
		return recvPort;
	}

	public void setRecvPort(Integer recvPort) {
		this.recvPort = recvPort;
	}

	public String getSendServer() {
		return sendServer;
	}

	public void setSendServer(String sendServer) {
		this.sendServer = sendServer;
	}

	public Boolean getSendSSL() {
		return sendSSL;
	}

	public void setSendSSL(Boolean sendSSL) {
		this.sendSSL = sendSSL;
	}

	public Integer getSendPort() {
		return sendPort;
	}

	public void setSendPort(Integer sendPort) {
		this.sendPort = sendPort;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getMailAddres() {
		return mailAddres;
	}

	public void setMailAddres(String mailAddres) {
		this.mailAddres = mailAddres;
	}

}
