package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.DistShopSvc;

/**
 * 卫星店服务
 * 
 * @author 邓华锋
 * @date 2016年02月20日 15:31:54
 *
 */
@IBatisSqlTarget
public interface DistShopSvcDao extends BaseDao<DistShopSvc, Long> {

	int insert(DistShopSvc distShopSvc);

	int deleteById(Long id);

	int update(DistShopSvc distShopSvc);

	DistShopSvc selectById(Long id);

	DistShopSvc selectByDistShopIdAndSvcId(Integer distShopId, Integer svcId);

	/**
	 * 卫星店服务分页
	 * 
	 * @author 邓华锋
	 * @date 2016年02月20日 03:31:54
	 * 
	 * @param paginatedFilter
	 *            like auditorName,like auditDesc,=applyFlag,=auditStatus
	 * @return
	 */
	PaginatedList<DistShopSvc> selectByFilter(PaginatedFilter paginatedFilter);

	List<DistShopSvc> selectByFilter(MapContext request);

	int deleteByDistShopId(Integer distShopId);

	int deleteBySvcId(Integer svcId);

}