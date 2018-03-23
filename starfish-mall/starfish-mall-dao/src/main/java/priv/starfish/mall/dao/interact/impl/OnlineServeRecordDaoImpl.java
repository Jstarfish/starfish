package priv.starfish.mall.dao.interact.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.interact.OnlineServeRecordDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.interact.entity.OnlineServeRecord;

@Component("onlineServeRecordDao")
public class OnlineServeRecordDaoImpl extends BaseDaoImpl<OnlineServeRecord, Long> implements OnlineServeRecordDao {

	@Override
	public OnlineServeRecord selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<OnlineServeRecord> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		// 过滤like参数
		MapContext filterItems = paginatedFilter.getFilterItems();
		String servantName = filterItems.getTypedValue("servantName", String.class);
		filterItems.remove("servantName");
		if (StrUtil.hasText(servantName)) {
			servantName = SqlBuilder.likeStrVal(servantName);
			filterItems.put("servantName", servantName);
		}
		String memberName = filterItems.getTypedValue("memberName", String.class);
		filterItems.remove("memberName");
		if (StrUtil.hasText(memberName)) {
			memberName = SqlBuilder.likeStrVal(memberName);
			filterItems.put("memberName", memberName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<OnlineServeRecord> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<OnlineServeRecord> selectByMemberId(Integer memberId) {
		String sqlId = this.getNamedSqlId("selectByMemberId");
		//
		return this.getSqlSession().selectList(sqlId, memberId);
	}

	@Override
	public int insert(OnlineServeRecord onlineServeRecord) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, onlineServeRecord);
	}

	@Override
	public int update(OnlineServeRecord onlineServeRecord) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, onlineServeRecord);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

}
