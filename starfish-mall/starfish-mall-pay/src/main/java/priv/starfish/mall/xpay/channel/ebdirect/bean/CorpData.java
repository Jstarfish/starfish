package priv.starfish.mall.xpay.channel.ebdirect.bean;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.xpay.base.annotation.XmlMapped;

@XmlMapped
public class CorpData {
	// PsFlag
	public String largeFlag;
	// BookingFlag
	public String bookingFlag;
	// BookingDate
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date bookingDate;
	// BookingTime
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	public Date bookingTime;
	// UrgencyFlag
	public String urgencyFlag;
	// OthBankFlag
	public String otherBankFlag;

	// CrBankType
	public String otherBankType;
	// CrAccName
	public String crdAcctName;
	// CrBankName
	public String crdBankName;
	// CrBankNo
	public String crdBankNo;
	// DbAccName
	public String dbtAcctName;
	// WhyUse
	public String usage;
	// Postscript
	public String postscript;
	// WaitFlag
	public String waitFlag;

	public static CorpData newOne() {
		return new CorpData();
	}
}
