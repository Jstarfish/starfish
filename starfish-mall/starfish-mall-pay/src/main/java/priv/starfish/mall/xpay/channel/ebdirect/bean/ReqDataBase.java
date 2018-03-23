package priv.starfish.mall.xpay.channel.ebdirect.bean;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.xpay.base.annotation.XmlMapped;

//6.1.1	ERP2ICT
//ap
@XmlMapped
public class ReqDataBase {
	// CCTransCode
	public String transCode;
	// ProductID
	public String productId;
	// ChannelType
	public String channelType;
	// CorpNo
	public String corpNo;
	// OpNo
	public String corpOpNo;
	// AuthNo
	public String authNo;
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
	// Sign
	public String sign;
}
