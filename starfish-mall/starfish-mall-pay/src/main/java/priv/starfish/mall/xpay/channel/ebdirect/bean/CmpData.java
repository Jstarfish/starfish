package priv.starfish.mall.xpay.channel.ebdirect.bean;


import priv.starfish.mall.xpay.base.annotation.XmlMapped;

//cmp
@XmlMapped
public class CmpData {
	// DbProv
	public String dbtProv;
	// DbAccNo
	public String dbtAcctNo;
	// DbLogAccNo
	public String dbtLogicAcctNo;

	// DbCur
	public String dbtCur;

	// CrProv
	public String crdProv;
	// CrAccNo
	public String crdAcctNo;
	// CrLogAccNo
	public String crdLogicAcctNo;

	// CrCur
	public String crdCur;

	// ConFlag
	public String chkFlag;

	// RespPrvData
	public String rspPrivateData;

	// BatchFileName
	public String batFileName;

	public static CmpData newOne() {
		return new CmpData();
	}

}
