package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.pay.unionpay.util.DateUtils;
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.dao.merchant.MerchantSettleAcctDao;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.service.CmbService;
import priv.starfish.mall.dao.settle.SettleOrderDao;
import priv.starfish.mall.dao.settle.SettleProcessDao;
import priv.starfish.mall.settle.entity.SettleProcess;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("cmbService")
public class CmbServiceImpl extends BaseServiceImpl implements CmbService {

	@Resource
	MerchantDao merchantDao;

	@Resource
	SaleOrderDao saleOrderDao;

	@Resource
	MerchantSettleAcctDao merchantSettleAcctDao;

	@Resource
	SettleProcessDao settleProcessDao;

	@Resource
	SettleOrderDao settleOrderDao;

	@Override
	public void createSettleInfo1() {
		//
		Date settleDay = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date beforeSettleDay = DateUtils.getBeforeMonthToday(settleDay);

		// 生成结算单
		SettleProcess settleProcess = new SettleProcess();
		settleProcess.setSettleDay(settleDay);

		// 查询所有启用的商户 
		Boolean disabled = false;
		List<Merchant> merList = merchantDao.selectMerchantsAsEnabled(disabled);
		// 查询所有今日可结算的商户:启用的商户的结算周期 <= (当前日期 - 商户最后一次生成结算单的日期)  
		List<Merchant> merchantList = new ArrayList<>();
		//
		for (Merchant merchant : merList) {
			//商户最后一次生成结算单的日期 TODO 如果此方法不行，则在商户表（或关联表）里，增加个最后一次生成结算单的字段。
			Date maxSettleDay = settleProcessDao.selectMaxCreateDay(merchant.getId());
			if(null != maxSettleDay){
				Long diffDay = DateUtils.getDiffDay(maxSettleDay, settleDay);
				// TODO 临时写的结算周期 ,应从商户获得。
				Integer zhouqi = 5;
				if(zhouqi.intValue() <= diffDay.intValue()){
					merchantList.add(merchant);
				}
			}else{
				merchantList.add(merchant);
			}
		}
		//
		/*if (null != merchantList && merchantList.size() != 0) {
			for (Merchant merchant : merchantList) {

				// 查询所有订单:已完成、未生成结算单、merchantId为此商户的。
				List<SaleOrder> list = saleOrderDao.selectForCreateSettleInfo(merchant.getId());
				settleProcess.setMerchantId(merchant.getId());
				// 根据商户ID，查询商户结算账户
				List<MerchantSettleAcct> merchantSettleAccts = merchantSettleAcctDao.selectByMerchantId(merchant.getId());
				if (null != merchantSettleAccts && merchantSettleAccts.size() != 0) {
					// TODO 目前是选择的第一个，以后根据需求更改，最好是：这个表里就设定一个结算账户。
					// 且是银行卡的账户。不能是支付宝或微信等其他的。
					settleProcess.setAcctName(merchantSettleAccts.get(0).getAcctName());
					settleProcess.setAcctNo(merchantSettleAccts.get(0).getAcctNo());
				}
				// 计算结算金额
				BigDecimal settleAmount = new BigDecimal("0.00");
				if (null != list && list.size() != 0) {
					for (SaleOrder saleOrder : list) {
						settleAmount = settleAmount.add(saleOrder.getSettleAmount());
					}
				}
				settleProcess.setSettleAmount(String.valueOf(settleAmount));
				settleProcessDao.insert(settleProcess);
				// 插入结算单、订单关联
				if (null != list && list.size() != 0) {
					for (SaleOrder saleOrder : list) {
						SettleOrder settleOrder = new SettleOrder();
						settleOrder.setProcessId(settleProcess.getId());
						settleOrder.setNo(saleOrder.getNo());
						settleOrderDao.insert(settleOrder);
					}
				}

			}
		}*/
	}

	@Override
	public void settleDo() {

	}

}
