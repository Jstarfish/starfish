package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.SvcGroup;

@IBatisSqlTarget
public interface SvcGroupDao extends BaseDao<SvcGroup, Integer> {

	SvcGroup selectById(Integer id);

	int insert(SvcGroup svcGroup);

	int update(SvcGroup svcGroup);

	int selectCountByNameAndKindId(String name, Integer kindId);

	int updateForDelete(Integer id);

	/**
	 * 获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午4:34:11
	 * 
	 * @param name
	 * @return
	 */
	public List<SvcGroup> select();

	/**
	 * 
	 * 分页获取车辆服务分组列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午9:02:25
	 * 
	 * @param paginatedFilter
	 * @return
	 */

	PaginatedList<SvcGroup> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 查询分组前台用
	 * 
	 * @author 李超杰
	 * @date 2015年10月12日 下午9:02:25
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public List<SvcGroup> selectFront();

}
