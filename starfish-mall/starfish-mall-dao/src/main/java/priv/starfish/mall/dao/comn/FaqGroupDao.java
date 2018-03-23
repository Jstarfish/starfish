package priv.starfish.mall.dao.comn;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.FaqGroup;

/**
 * 常见问题分组Dao
 * @author 邓华锋
 * @date 2015年9月19日 下午2:32:31
 *
 */
@IBatisSqlTarget
public interface FaqGroupDao extends BaseDao<FaqGroup, Integer> {
	FaqGroup selectById(Integer id);

	int insert(FaqGroup faqGroup);

	int update(FaqGroup faqGroup);

	int deleteById(Integer id);
	
	/**
	 * 常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午3:34:02
	 * 
	 * @param paginatedFilter 
	 * 						 like name,= catId
	 * @return
	 */
	PaginatedList<FaqGroup> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据常见问题分类ID获取常见问题分组集合
	 * @author 邓华锋
	 * @date 2015年9月19日 下午3:34:50
	 * 
	 * @param catId 分类ID
	 * @return
	 */
	List<FaqGroup> selectByCatId(Integer catId);
	
	/**
	 * 根据常见问题分类ID删除常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午3:35:33
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByCatId(Integer catId);
	
	/**
	 * 根据常见分类ID获取常见问题分组ID集合
	 * @author 邓华锋
	 * @date 2015年9月25日 下午3:35:53
	 * 
	 * @param catId 常见问题分类ID
	 * @return
	 */
	List<Integer> selectIds(Integer catId);
}