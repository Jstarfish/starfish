package priv.starfish.mall.xpay.channel.ebdirect.bean;


import priv.starfish.mall.xpay.base.annotation.XmlMapped;

//Cme
@XmlMapped
public class CmeData {
	// RecordNum
	public String recordNum;

	// FieldNum
	public Integer fieldNum;

	
	public static CmeData newOne(){
		return new CmeData();
	}
}
