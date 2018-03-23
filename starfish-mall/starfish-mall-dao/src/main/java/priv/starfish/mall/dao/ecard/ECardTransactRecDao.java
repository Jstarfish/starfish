package priv.starfish.mall.dao.ecard;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.ecard.entity.ECardTransactRec;

@IBatisSqlTarget
public interface ECardTransactRecDao extends BaseDao<ECardTransactRec, Long> {
	ECardTransactRec selectById(Long id);

	int insert(ECardTransactRec eCardTransactRec);

	int update(ECardTransactRec eCardTransactRec);

	int deleteById(Long id);

	/**
	 * 分页查询e卡交易列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 下午2:34:38
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardTransactRec> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 通过e卡ID查询交易记录
	 * 
	 * @author wangdi
	 * @date 2015年11月3日 上午9:46:55
	 * 
	 * @param cardId
	 * @return
	 */
	List<ECardTransactRec> selectByCardId(Integer cardId);
	
	/**
	 * 查询e卡交易记录(不分页)
	 * 
	 * @author wangdi
	 * @date 2015年12月28日 下午6:29:44
	 * 
	 * @param filter
	 * @return
	 */
	List<ECardTransactRec> selectByFilterNormal(MapContext filter);
}
