package priv.starfish.mall.xpay.channel.ebdirect.bean;

import priv.starfish.mall.xpay.base.annotation.XmlMapped;

import java.math.BigDecimal;


@XmlMapped
public class ReqData extends ReqDataBase {
	// Amt
	public BigDecimal amount;

	// Cmp
	public CmpData cmpData;

	// Corp
	public CorpData corpData;
	
	public static ReqData newOne(){
		return new ReqData();
	}
}
