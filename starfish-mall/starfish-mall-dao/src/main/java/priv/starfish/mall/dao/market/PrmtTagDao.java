package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.PrmtTag;

@IBatisSqlTarget
public interface PrmtTagDao extends BaseDao<PrmtTag, Integer> {
	PrmtTag selectById(Integer id);

	PrmtTag selectByCode(String code);

	int insert(PrmtTag prmtTag);

	int update(PrmtTag prmtTag);

	int deleteById(Integer id);

	/**
	 * 分页查询促销标签列表
	 * @param paginatedFilter: like name
	 * @return PaginatedList<PrmtTag>
	 */
	PaginatedList<PrmtTag> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取所有的促销标签列表
	 * @return List<PrmtTag>
	 */
	List<PrmtTag> selectAll();
}