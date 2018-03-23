package priv.starfish.mall.xpay.channel.ebdirect.bean;


import priv.starfish.mall.xpay.base.annotation.XmlMapped;

@XmlMapped
public class RspData extends RspDataBase {
	// Corp
	public CorpData corpData;
	
	public static RspData newOne(){
		return new RspData();
	}
}
