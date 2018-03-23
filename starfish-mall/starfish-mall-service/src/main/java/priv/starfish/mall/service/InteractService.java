package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.GoodsInquiry;
import priv.starfish.mall.interact.entity.GoodsInquiryReply;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.interact.entity.OnlineServeRecord;

public interface InteractService extends BaseService {

	// --------------------------------------商品咨询----------------------------------------------

	boolean saveGoodsInquiry(GoodsInquiry goodsInquiry);

	/**
	 * 分页查询商品咨询
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 下午2:50:27
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<GoodsInquiry> getGoodsInquiriesByFilter(PaginatedFilter paginatedFilter);

	// ----------------------------------------商品咨询回复--------------------------------------------

	/**
	 * 保存商品回复
	 * 
	 * @author 毛智东
	 * @date 2015年7月27日 上午11:23:14
	 * 
	 * @param goodsInquiryReply
	 * @return
	 */
	boolean saveGoodsInquiryReply(GoodsInquiryReply goodsInquiryReply);

	/**
	 * 查询在线客服编号
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午6:59:06
	 * 
	 * @param paginatedFilter
	 *            过滤条件：like servantNo, like servantName, like realName
	 * @return
	 */
	PaginatedList<InteractUserDto> getOnlineServeNos(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter);

	/**
	 * 查询在线客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月19日 上午11:59:06
	 * 
	 * @param
	 * @return
	 */
	List<InteractUserDto> getOnlineServeNos(AuthScope authScope, Integer entityId);

	/**
	 * 根据咨询id查找咨询回复
	 * 
	 * @author 毛智东
	 * @date 2015年7月27日 上午11:25:00
	 * 
	 * @param inquiryId
	 * @return
	 */
	GoodsInquiryReply getGoodsInquiryReplyByInquiryId(long inquiryId);

	/**
	 * 添加客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月7日 下午6:36:13
	 * 
	 * @param goodsInquiry
	 * @return
	 */
	int insertOnlineServeNo(OnlineServeNo onlineServeNo);

	OnlineServeNo getOnlineServeNoByServantNo(String servantNo);

	OnlineServeNo getOnlineServeNoByServantName(String servantName);

	/**
	 * 分页查询客服历史记录
	 * 
	 * @author WJJ
	 * @date 2015年9月24日 下午2:35:02
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<OnlineServeRecord>
	 */
	PaginatedList<OnlineServeRecord> getRecordsByFilter(PaginatedFilter paginatedFilter);

}
