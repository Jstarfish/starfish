package priv.starfish.mall.dao.interact.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.interact.GoodsInquiryReplyDao;
import priv.starfish.mall.interact.entity.GoodsInquiryReply;

@Component("goodsInquiryReplyDao")
public class GoodsInquiryReplyDaoImpl extends BaseDaoImpl<GoodsInquiryReply, Long> implements GoodsInquiryReplyDao {
	@Override
	public GoodsInquiryReply selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(GoodsInquiryReply goodsInquiryReply) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsInquiryReply);
	}

	@Override
	public int update(GoodsInquiryReply goodsInquiryReply) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsInquiryReply);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public GoodsInquiryReply selectByInquiryId(long inquiryId) {
		String sqlId = this.getNamedSqlId("selectByInquiryId");
		//
		return this.getSqlSession().selectOne(sqlId, inquiryId);
	}
}
