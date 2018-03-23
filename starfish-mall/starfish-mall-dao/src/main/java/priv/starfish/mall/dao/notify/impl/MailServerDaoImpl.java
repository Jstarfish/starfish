package priv.starfish.mall.dao.notify.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.notify.MailServerDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.MailServer;

@Component("mailServerDao")
public class MailServerDaoImpl extends BaseDaoImpl<MailServer, Integer> implements MailServerDao {
	@Override
	public MailServer selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public MailServer selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(MailServer mailServer) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, mailServer);
	}

	@Override
	public int update(MailServer mailServer) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, mailServer);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<MailServer> selectList(PaginatedFilter paginatedFilter) {
		// 分页参数
		String sqlId = this.getNamedSqlId("selectMailServers");
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<MailServer> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public void updateAllUnabled() {
		String sqlId = this.getNamedSqlId("updateAllUnabled");
		//
		this.getSqlSession().update(sqlId);
	}

	@Override
	public MailServer selectEnabled() {
		String sqlId = this.getNamedSqlId("selectEnabled");
		return this.getSqlSession().selectOne(sqlId);
	}
}