package priv.starfish.mall.dao.market;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.PrmtTagSvc;

@IBatisSqlTarget
public interface PrmtTagSvcDao extends BaseDao<PrmtTagSvc, Integer> {
	PrmtTagSvc selectById(Integer id);

	PrmtTagSvc selectByTagIdAndSvcId(Integer tagId, Integer svcId);

	int insert(PrmtTagSvc prmtTagSvc);

	int update(PrmtTagSvc prmtTagSvc);

	int deleteById(Integer id);

	/**
	 * 删除促销标签服务
	 * @param tagId
	 * @param svcId
	 * @return int
	 */
	int deleteByTagIdAndSvcId(Integer tagId, Integer svcId);

	/**
	 * 分页查询促销标签服务
	 * @param paginatedFilter like svc.name, = prmtTag.id
	 * @return PaginatedList<PrmtTagSvc>
	 */
	PaginatedList<PrmtTagSvc> selectByFilter(PaginatedFilter paginatedFilter);
}
