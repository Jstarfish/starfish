package priv.starfish.mall.dao.logistic;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.logistic.entity.LogisCom;

@IBatisSqlTarget
public interface LogisComDao extends BaseDao<LogisCom, Integer> {

	LogisCom selectById(Integer id);

	LogisCom selectByCode(String code);

	int insert(LogisCom logisCom);

	int update(LogisCom logisCom);

	int deleteById(Integer id);

	/**
	 * 分页查询物流公司集合
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午4:41:06
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<LogisticCom>
	 */
	PaginatedList<LogisCom> selectLogisComsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 得到所有的快递公司
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:42:20
	 * 
	 * @return
	 */
	List<LogisCom> selectListAll();

	/**
	 * 分组查询所有的快递公司及其配置方式
	 * 
	 * @author 毛智东
	 * @date 2015年6月6日 下午3:47:51
	 * 
	 * @param lcDisabled
	 * @param dwDisabled
	 * @return
	 */
	List<LogisCom> selectListByGroup(Boolean lcDisabled, Boolean dwDisabled);

}
