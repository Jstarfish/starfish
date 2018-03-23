package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.dao.goods.GoodsDao;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.dao.interact.GoodsInquiryDao;
import priv.starfish.mall.dao.interact.GoodsInquiryReplyDao;
import priv.starfish.mall.dao.interact.OnlineServeNoDao;
import priv.starfish.mall.dao.interact.OnlineServeRecordDao;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.GoodsInquiry;
import priv.starfish.mall.interact.entity.GoodsInquiryReply;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.interact.entity.OnlineServeRecord;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.service.InteractService;

@Service("interactService")
public class InteractServiceImpl implements InteractService {

	@Resource
	GoodsInquiryDao goodsInquiryDao;
	@Resource
	GoodsInquiryReplyDao goodsInquiryReplyDao;
	@Resource
	MemberDao memberDao;
	@Resource
	GoodsDao goodsDao;
	@Resource
	OnlineServeNoDao onlineServeNoDao;
	@Resource
	OnlineServeRecordDao onlineServeRecordDao;

	// ------------------------------------商品咨询----------------------------------------------

	@Override
	public boolean saveGoodsInquiry(GoodsInquiry goodsInquiry) {
		return goodsInquiryDao.insert(goodsInquiry) > 0;
	}

	@Override
	public PaginatedList<GoodsInquiry> getGoodsInquiriesByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<GoodsInquiry> paginatedList = goodsInquiryDao.selectByFilter(paginatedFilter);
		List<GoodsInquiry> goodsInquirys = paginatedList.getRows();
		for (GoodsInquiry goodsInquiry : goodsInquirys) {
			GoodsInquiryReply goodsInquiryReply = getGoodsInquiryReplyByInquiryId(goodsInquiry.getId());
			Member member = memberDao.selectById(goodsInquiry.getUserId());
			Goods goods = goodsDao.selectById(goodsInquiry.getGoodsId());
			Boolean replyFlag = goodsInquiryReply != null;
			goodsInquiry.setGoodsInquiryReply(goodsInquiryReply);
			goodsInquiry.setMember(member);
			goodsInquiry.setGoods(goods);
			goodsInquiry.setReplyFlag(replyFlag);
		}
		paginatedList.setRows(goodsInquirys);
		return paginatedList;
	}

	// ----------------------------------------商品咨询回复--------------------------------------------

	@Override
	public boolean saveGoodsInquiryReply(GoodsInquiryReply goodsInquiryReply) {
		return goodsInquiryReplyDao.insert(goodsInquiryReply) > 0;
	}

	@Override
	public GoodsInquiryReply getGoodsInquiryReplyByInquiryId(long inquiryId) {
		return goodsInquiryReplyDao.selectByInquiryId(inquiryId);
	}

	// ----------------------------------------在线客服--------------------------------------------

	@Override
	public int insertOnlineServeNo(OnlineServeNo onlineServeNo) {
		return onlineServeNoDao.insert(onlineServeNo);
	}

	@Override
	public PaginatedList<InteractUserDto> getOnlineServeNos(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter) {
		return onlineServeNoDao.selectByFilter(authScope, entityId, paginatedFilter);
	}

	@Override
	public OnlineServeNo getOnlineServeNoByServantNo(String servantNo) {
		return onlineServeNoDao.selectByServantNo(servantNo);
	}

	@Override
	public OnlineServeNo getOnlineServeNoByServantName(String servantName) {
		return onlineServeNoDao.selectByServantName(servantName);
	}

	@Override
	public List<InteractUserDto> getOnlineServeNos(AuthScope authScope, Integer entityId) {
		return onlineServeNoDao.selectByFilter(authScope, entityId);
	}

	// -----WJJ----begin--------------------------客服历史--------------------------------------------

	@Override
	public PaginatedList<OnlineServeRecord> getRecordsByFilter(PaginatedFilter paginatedFilter) {
		return onlineServeRecordDao.selectByFilter(paginatedFilter);
	}

	// -----WJJ----end--------------------------客服历史----------------------------------------------
}
