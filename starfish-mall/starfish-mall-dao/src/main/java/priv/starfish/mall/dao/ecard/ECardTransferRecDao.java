package priv.starfish.mall.dao.ecard;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.ecard.entity.ECardTransferRec;

@IBatisSqlTarget
public interface ECardTransferRecDao extends BaseDao<ECardTransferRec, Integer> {
	ECardTransferRec selectById(Integer id);

	int insert(ECardTransferRec eCardTransferRec);

	int update(ECardTransferRec eCardTransferRec);

	int deleteById(Integer id);

	/**
	 * 分页查询e卡转赠列表
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 下午2:34:56
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardTransferRec> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据e卡ID和被赠予用户的ID查询转赠记录
	 * 
	 * @author wangdi
	 * @date 2015年11月4日 下午2:58:50
	 * 
	 * @param cardId
	 * @param userIdTo
	 * @return
	 */
	ECardTransferRec selectByCardIdAndUserIdTo(Integer cardId, Integer userIdTo);
	
	/**
	 * 查询e卡转赠记录(不分页)
	 * 
	 * @author wangdi
	 * @date 2015年12月3日 下午2:33:37
	 * 
	 * @param filter
	 * @return
	 */
	List<ECardTransferRec> selectByFilterNormal(MapContext filter);
	
	/**
	 * 删除e卡转赠记录（假删）
	 * 
	 * @author wangdi
	 * @date 2015年12月30日 下午3:21:34
	 * 
	 * @param eCardTransferRec
	 * @return
	 */
	int updateForDelete(ECardTransferRec eCardTransferRec);
}
