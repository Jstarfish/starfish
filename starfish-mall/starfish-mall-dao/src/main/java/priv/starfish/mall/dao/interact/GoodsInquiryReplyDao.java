package priv.starfish.mall.dao.interact;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.interact.entity.GoodsInquiryReply;

@IBatisSqlTarget
public interface GoodsInquiryReplyDao extends BaseDao<GoodsInquiryReply, Long> {
	GoodsInquiryReply selectById(Long id);

	int insert(GoodsInquiryReply goodsInquiryReply);

	int update(GoodsInquiryReply goodsInquiryReply);

	int deleteById(Long id);

	/**
	 * 根据咨询id查找咨询回复
	 * 
	 * @author 毛智东
	 * @date 2015年7月27日 上午11:25:00
	 * 
	 * @param inquiryId
	 * @return
	 */
	GoodsInquiryReply selectByInquiryId(long inquiryId);
}