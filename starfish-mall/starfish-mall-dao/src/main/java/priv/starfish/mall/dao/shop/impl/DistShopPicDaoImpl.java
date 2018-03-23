package priv.starfish.mall.dao.shop.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.shop.DistShopPicDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.DistShopPic;

@Component("distShopPicDao")
public class DistShopPicDaoImpl extends BaseDaoImpl<DistShopPic, Long> implements DistShopPicDao {
	@Override
	public DistShopPic selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(DistShopPic distShopPic) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (distShopPic.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(DistShopPic.class) + 1;
			distShopPic.setSeqNo(seqNo);
		}
		//
		return this.getSqlSession().insert(sqlId, distShopPic);
	}

	@Override
	public int update(DistShopPic distShopPic) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, distShopPic);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<DistShopPic> selectByDistShopId(Integer id) {
		String sqlId = this.getNamedSqlId("selectByDistShopId");
		//
		return this.getSqlSession().selectList(sqlId, id);
	}
}