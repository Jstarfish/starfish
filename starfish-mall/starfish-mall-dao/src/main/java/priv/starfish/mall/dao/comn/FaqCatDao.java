package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.FaqCat;

/**
 * 常见问题分类
 * @author 邓华锋
 * @date 2015年9月19日 下午3:44:07
 *
 */
@IBatisSqlTarget
public interface FaqCatDao extends BaseDao<FaqCat, Integer> {
	FaqCat selectById(Integer id);
	
	int selectCountByName(String name);
	
	int insert(FaqCat faqCat);
	
	int update(FaqCat faqCat);
	
	int deleteById(Integer id);
	
	/**
	 * 常见问题分类分页
	 * @author 邓华锋
	 * @date  2015年9月19日 下午4:10:07
	 * 
	 * @param paginatedFilter 
	 * 						like name
	 * @return
	 */
	PaginatedList<FaqCat> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 
	 * @author 邓华锋
	 * @date 2015年9月30日 下午10:06:38
	 * 
	 * @return
	 */
	public List<FaqCat> selectAll();
}