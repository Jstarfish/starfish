package priv.starfish.mall.service.misc;

import java.math.BigDecimal;
import java.util.List;

import priv.starfish.mall.cart.dto.MiscAmountInfo;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.market.dict.CouponType;
import priv.starfish.mall.market.entity.UserCoupon;
import priv.starfish.mall.market.entity.UserSvcCoupon;

public final class CalcSaleHelper {
	// 计算返回
	public static SaleCart calcSaleCart(List<SaleCartSvc> saleCartSvcList, List<SaleCartGoods> goods, String flagType) {
		SaleCart saleCart = SaleCart.newOne();
		BigDecimal goodsAmount = BigDecimal.ZERO;// 商品总价格
		BigDecimal svcAmount = BigDecimal.ZERO;// 服务总价格
		BigDecimal settlePrice = BigDecimal.ZERO;// 结算金额
		BigDecimal amount = BigDecimal.ZERO;// 应付
		BigDecimal saleAmount = BigDecimal.ZERO;// 销售总金额
		BigDecimal discAmount = BigDecimal.ZERO;// 总扣减
		Integer goodsCount = 0;// 选中商品数量
		Integer svcCount = 0;// 服务数量
		// 服务
		if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
			for (SaleCartSvc saleCartSvc : saleCartSvcList) {
				if (saleCartSvc != null) {
					//
					calcSaleCartSvc(saleCartSvc);// 计算服务
					MiscAmountInfo amountInfo = saleCartSvc.getAmountInfo();
					if (amountInfo != null) {
						if ("all".equals(flagType)) {
							saleAmount = saleAmount.add(amountInfo.getSaleAmount());
							svcAmount = svcAmount.add(amountInfo.getSaleAmount());
							svcCount += 1;
							//
							settlePrice = settlePrice.add(amountInfo.getSettlePrice());
							discAmount = discAmount.add(amountInfo.getDiscAmount());
						} else {
							Boolean checkFlog = saleCartSvc.getCheckFlag();
							if (checkFlog != null && checkFlog == true) {
								saleAmount = saleAmount.add(amountInfo.getSaleAmount());
								svcAmount = svcAmount.add(amountInfo.getSaleAmount());
								svcCount += 1;
								// 暂时不考虑优惠券
								settlePrice = settlePrice.add(saleCartSvc.getSalePrice());
								discAmount = discAmount.add(amountInfo.getDiscAmount());
							}
						}
					}
				}
			}
		}
		// 商品
		if (goods != null && goods.size() > 0) {
			for (SaleCartGoods saleCartGoods : goods) {
				if (saleCartGoods != null) {
					// 计算会员等级折扣 预留位置
					//
					calcSaleCartGoods(saleCartGoods); // 计算商品
					MiscAmountInfo amountInfo = saleCartGoods.getAmountInfo();
					if (amountInfo != null) {
						if ("all".equals(flagType)) {
							goodsAmount = goodsAmount.add(amountInfo.getSaleAmount());
							saleAmount = saleAmount.add(amountInfo.getSaleAmount());
							// 暂时不考虑优惠券
							settlePrice = settlePrice.add(amountInfo.getSaleAmount());
							discAmount = discAmount.add(amountInfo.getDiscAmount());
							goodsCount += saleCartGoods.getQuantity();
						} else {
							Boolean checkFlog = saleCartGoods.getCheckFlag();
							if (checkFlog != null && checkFlog == true) {
								goodsAmount = goodsAmount.add(amountInfo.getSaleAmount());
								saleAmount = saleAmount.add(amountInfo.getSaleAmount());
								// 暂时不考虑优惠券
								settlePrice = settlePrice.add(amountInfo.getSaleAmount());
								discAmount = discAmount.add(amountInfo.getDiscAmount());
								goodsCount += saleCartGoods.getQuantity();
							}
						}
					}
				}
			}
		}
		//
		MiscAmountInfo amountInfo = new MiscAmountInfo();
		amountInfo.setAmount(saleAmount.subtract(discAmount));
		amountInfo.setDiscAmount(discAmount);
		amountInfo.setSaleAmount(saleAmount);
		amountInfo.setSettlePrice(settlePrice);
		saleCart.setAmountInfo(amountInfo);
		saleCart.setSvcAmount(svcAmount);
		saleCart.setGoodsAmount(goodsAmount);
		saleCart.setGoodsCount(goodsCount);
		saleCart.setSaleCartSvcList(saleCartSvcList);
		saleCart.setSaleCartGoods(goods);
		saleCart.setSvcCount(svcCount);
		//
		return saleCart;
	}

	// 计算服务 单个
	private static void calcSaleCartSvc(SaleCartSvc saleCartSvc) {
		if (saleCartSvc != null) {
			MiscAmountInfo amountInfo = new MiscAmountInfo();
			BigDecimal saleAmount = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal discAmount = BigDecimal.ZERO;
			BigDecimal settlePrice = BigDecimal.ZERO;
			BigDecimal rateAmount = BigDecimal.ZERO;// 折扣后金额
			// 小计(服务没有数量)
			saleAmount = saleCartSvc.getSalePrice();
			// ----------计算会员折扣----------------------------------------------------
			BigDecimal discRate = saleCartSvc.getDiscRate();
			if (discRate != null) {
				// 折扣后金额
				rateAmount = calcMemDisc(discRate, saleAmount);
				discAmount = discAmount.add(saleAmount.subtract(rateAmount));
			} else {
				rateAmount = saleAmount;
			}
			// ------------计算是否使用优惠券--------------------------------------------------
			UserSvcCoupon svcCoupon = saleCartSvc.getUserCoupon();
			if (svcCoupon != null) {
				discAmount = discAmount.add(calcCoupon(svcCoupon, rateAmount));
				// 暂时不判断店铺优惠
				settlePrice = settlePrice.add(svcCoupon.getSettlePrice());
			} else {
				// 暂时不判断店铺优惠
				settlePrice = settlePrice.add(saleAmount);
			}
			amount = amount.add(saleAmount.subtract(discAmount));
			// ----------------------保存--------------------------------------------
			amountInfo.setDiscAmount(discAmount);
			amountInfo.setAmount(amount);
			amountInfo.setSettlePrice(settlePrice);
			amountInfo.setSaleAmount(saleAmount);
			//
			saleCartSvc.setAmountInfo(amountInfo);
		}
	}

	// 计算服务优惠券 (svcAmount =服务金额)
	private static BigDecimal calcCoupon(UserSvcCoupon userCoupon, BigDecimal svcAmount) {
		// 优惠券减去金额
		BigDecimal discAmount = BigDecimal.ZERO;
		if (userCoupon != null) {
			BigDecimal couponPrice = userCoupon.getPrice();
			// 判断优惠券类型
			if (userCoupon.getType().equals(CouponType.deduct.toString())) {
				// 抵金券 优惠金额是抵金券金额
				int result = svcAmount.compareTo(couponPrice);
				if (result == 0 || result == 1) {
					discAmount = discAmount.add(couponPrice);
				} else {
					discAmount = discAmount.add(svcAmount);
				}
			} else if (userCoupon.getType().equals(CouponType.nopay.toString())) {
				// 免付券 优惠金额是商品单价
				discAmount = discAmount.add(svcAmount);
			} else if (userCoupon.getType().equals(CouponType.sprice.toString())) {
				// 特价券 优惠金额是商品金额-特价后金额
				discAmount = discAmount.add(getSvcDiscAmount(couponPrice, svcAmount));
			}
		}
		return discAmount;
	}

	// 计算商品 单个
	private static void calcSaleCartGoods(SaleCartGoods goods) {
		if (goods != null) {
			//
			MiscAmountInfo amountInfo = new MiscAmountInfo();
			BigDecimal saleAmount = BigDecimal.ZERO;
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal discAmount = BigDecimal.ZERO;
			BigDecimal settlePrice = BigDecimal.ZERO;
			//
			BigDecimal productAmount = goods.getProductAmount();
			saleAmount = saleAmount.add(getGoodsAmount(goods.getQuantity(), productAmount));
			// 计算是否使用优惠券
			UserCoupon goodsCoupon = goods.getUserCoupon();
			if (goodsCoupon != null) {
				discAmount = discAmount.add(calcCoupon(goodsCoupon, productAmount));
				// 暂时不判断店铺优惠
				BigDecimal couponSettlePrice = goodsCoupon.getSettlePrice();
				settlePrice = settlePrice.add(saleAmount.subtract(productAmount.subtract(couponSettlePrice)));
			} else {
				// 暂时不判断会员等级优惠和店铺优惠
				settlePrice = settlePrice.add(saleAmount);
			}
			amount = amount.add(saleAmount.subtract(discAmount));
			//
			amountInfo.setSaleAmount(saleAmount);
			amountInfo.setDiscAmount(discAmount);
			amountInfo.setSettlePrice(settlePrice);
			amountInfo.setAmount(amount);
			goods.setAmountInfo(amountInfo);
		}
	}

	// 计算商品优惠券 (productAmount =商品售价金额 单个)
	private static BigDecimal calcCoupon(UserCoupon userCoupon, BigDecimal productAmount) {
		// 优惠券减去金额
		BigDecimal discAmount = BigDecimal.ZERO;
		if (userCoupon != null) {
			BigDecimal couponPrice = userCoupon.getPrice();
			// 判断优惠券类型
			if (userCoupon.getType().equals(CouponType.deduct.toString())) {
				// 抵金券 优惠金额是抵金券金额
				int result = productAmount.compareTo(couponPrice);
				if (result == 0 || result == 1) {
					discAmount = discAmount.add(couponPrice);
				} else {
					discAmount = discAmount.add(productAmount);
				}
			} else if (userCoupon.getType().equals(CouponType.nopay.toString())) {
				// 免付券 优惠金额是商品单价
				discAmount = discAmount.add(productAmount);
			} else if (userCoupon.getType().equals(CouponType.sprice.toString())) {
				// 特价券 优惠金额是商品金额-特价后金额
				discAmount = discAmount.add(getGoodsDiscAmount(couponPrice, productAmount));
			}
		}
		return discAmount;
	}

	// 计算服务会员折扣后金额（会员折扣*服务价）
	public static BigDecimal calcMemDisc(BigDecimal rate, BigDecimal salePrice) {
		BigDecimal price = BigDecimal.ZERO;
		if (rate != null && salePrice != null) {
			price = rate.multiply(salePrice);
		}
		return price;
	}

	// 计算优惠券优惠金额 商品（特价券）
	private static BigDecimal getGoodsDiscAmount(BigDecimal price, BigDecimal goodsAmount) {
		BigDecimal discAmount = goodsAmount.subtract(price);
		int result = discAmount.compareTo(BigDecimal.ZERO);
		if (result == 0 || result == 1) {
			return discAmount;
		} else {
			return new BigDecimal(0.00);
		}
	}

	// 计算优惠券优惠金额 服务 （特价券）
	private static BigDecimal getSvcDiscAmount(BigDecimal price, BigDecimal saleAmount) {
		BigDecimal discAmount = saleAmount.subtract(price);
		int result = discAmount.compareTo(BigDecimal.ZERO);
		if (result == 0 || result == 1) {
			return discAmount;
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 计算购物车单个商品金额
	 * 
	 * @param quantity
	 * @param salePrice
	 * @return
	 */
	private static BigDecimal getGoodsAmount(Integer quantity, BigDecimal salePrice) {
		BigDecimal goodsAmount = salePrice.multiply(BigDecimal.valueOf(quantity));
		return goodsAmount;
	}

}
