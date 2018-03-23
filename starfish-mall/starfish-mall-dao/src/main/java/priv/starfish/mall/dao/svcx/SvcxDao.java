package priv.starfish.mall.dao.svcx;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.svcx.entity.Svcx;

@IBatisSqlTarget
public interface SvcxDao extends BaseDao<Svcx, Integer> {

	Svcx selectById(Integer id);

	int insert(Svcx svcx);

	int update(Svcx svcx);

	int updateForDelete(Integer id);

	/**
	 * 根据分组id更新是否启用状态
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午7:24:09
	 * 
	 * @param carSvc
	 * @return
	 */
	int updateByGroupIdAndState(Boolean disabled, Integer groupId);

	/**
	 * 
	 * 根据车辆服务分组ID删除车辆服务
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午2:16:06
	 * 
	 * @param groupId
	 * @return
	 */
	int updateForDeleteByGroupId(Integer groupId);

	/**
	 * 根据车辆服务分组ID查询车辆服务
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午2:17:33
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Svcx> selectByGroupIdAndState(Integer groupId, Boolean disabled);

	/**
	 * 
	 * 分页获取车辆服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月13日 下午2:27:28
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Svcx> selectByFilter(PaginatedFilter paginatedFilter);

	List<Svcx> select();

	/**
	 * 
	 * 获取某一店铺下的所有服务
	 */
	List<Svcx> selectByShopId(Integer shopId);

	Svcx selectByKindIdAndName(Integer kindId, String name);

}
