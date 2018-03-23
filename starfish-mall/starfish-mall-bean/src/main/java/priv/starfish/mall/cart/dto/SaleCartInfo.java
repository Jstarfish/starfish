package priv.starfish.mall.cart.dto;

import java.util.List;

import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.comn.dict.DeviceType;

public class SaleCartInfo {

	// 购物车服务列表（购物车服务商品）
	// List<SaleCartSvc>
	private List<SaleCartSvc> saleCartSvcList;

	private List<SaleCartGoods> saleCartGoods;
	// 设备类型
	// DeviceType
	private DeviceType deviceType;

	public List<SaleCartSvc> getSaleCartSvcList() {
		return saleCartSvcList;
	}

	public void setSaleCartSvcList(List<SaleCartSvc> saleCartSvcList) {
		this.saleCartSvcList = saleCartSvcList;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public List<SaleCartGoods> getSaleCartGoods() {
		return saleCartGoods;
	}

	public void setSaleCartGoods(List<SaleCartGoods> saleCartGoods) {
		this.saleCartGoods = saleCartGoods;
	}

	@Override
	public String toString() {
		return "SaleCartInfo [saleCartSvcList=" + saleCartSvcList + ", saleCartGoods=" + saleCartGoods + ", deviceType=" + deviceType + "]";
	}

}
