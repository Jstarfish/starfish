package priv.starfish.mall.dao.svcx.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.svcx.SvcPackItemDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.svcx.entity.SvcPackItem;

@Component("svcPackItemDao")
public class SvcPackItemDaoImpl extends BaseDaoImpl<SvcPackItem, Integer> implements SvcPackItemDao {
	@Override
	public SvcPackItem selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public SvcPackItem selectBySvcIdAndSvcPackId(Integer svcId, Integer svcPackId) {
		String sqlId = this.getNamedSqlId("selectBySvcIdAndSvcPackId");
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("svcId", svcId);
		map.put("svcPackId", svcPackId);
		return this.getSqlSession().selectOne(sqlId, map);
	}

	@Override
	public int insert(SvcPackItem svcPackItem) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, svcPackItem);
	}

	@Override
	public int update(SvcPackItem svcPackItem) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, svcPackItem);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SvcPackItem> selectByPackId(Integer packId) {
		String sqlId = this.getNamedSqlId("selectByPackId");
		//
		return this.getSqlSession().selectList(sqlId, packId);
	}
}
