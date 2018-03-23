package priv.starfish.mall.dao.interact;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.interact.entity.GoodsInquiry;

@IBatisSqlTarget
public interface GoodsInquiryDao extends BaseDao<GoodsInquiry, Long> {
	GoodsInquiry selectById(Long id);

	int insert(GoodsInquiry goodsInquiry);

	int update(GoodsInquiry goodsInquiry);

	int deleteById(Long id);

	/**
	 * 分页查询商品咨询
	 * 
	 * @author WJJ
	 * @date 2015年9月25日 下午2:57:17
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<GoodsInquiry> selectByFilter(PaginatedFilter paginatedFilter);
}