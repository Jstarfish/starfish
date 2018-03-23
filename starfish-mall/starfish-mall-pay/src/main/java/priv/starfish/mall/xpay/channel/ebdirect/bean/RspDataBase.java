package priv.starfish.mall.xpay.channel.ebdirect.bean;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.xpay.base.annotation.XmlMapped;

//6.1.2	ICT2ERP
//ap
@XmlMapped
public class RspDataBase {
	// CCTransCode
	public String transCode;
	// ReqSeqNo
	public String reqSeqNo;
	// ReqDate
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date reqDate;
	// ReqTime
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date reqTime;

	// RespSource
	public String rspSrc;
	// RespSeqNo
	public String rspSeqNo;

	// RespDate
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date rspDate;
	// RespTime
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date rspTime;
	// RespCode
	public String rspCode;

	// RespInfo
	public String rspInfo;

	// RxtInfo
	public String rspInfoX;

	// FileFlag
	public String fileFlag;

	// Cme
	public CmeData cmeData;

	// Cmp
	public CmpData cmpData;
}
