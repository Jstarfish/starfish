package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.Faq;

/**
 * 常见问题dao
 * 
 * @author 邓华锋
 * @date 2015年9月19日 下午3:18:47
 *
 */
@IBatisSqlTarget
public interface FaqDao extends BaseDao<Faq, Integer> {

	Faq selectById(Integer id);

	int insert(Faq faq);

	int update(Faq faq);

	int deleteById(Integer id);

	/**
	 * 根据常见问题分组ID删除常见问题
	 * @author 邓华锋
	 * @date 2015年9月19日 下午4:04:25
	 * 
	 * @param groupId 分组ID
	 * @return
	 */
	int deleteByGroupId(Integer groupId);

	/**
	 * 根据常见问题分组ID集合删除常见问题
	 * @author 邓华锋
	 * @date 2015年9月19日 下午4:10:42
	 * 
	 * @param groupIds 常见问题分组ID集合
	 * @return 
	 */
	int deleteByGroupIds(List<Integer> groupIds);

	/**
	 * 常见问题分页
	 * @author 邓华锋
	 * @date 2015年9月19日 下午4:10:45
	 * 
	 * @param paginatedFilter
	 *  					question  like '%keyword%' OR answer  like '%keyword%'
	 * @return
	 */
	PaginatedList<Faq> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据常见问题分组ID获取常见问题集合
	 * @author 邓华锋
	 * @date 2015年9月19日 下午3:34:50
	 * 
	 * @param groupId 分组ID
	 * @return
	 */
	public List<Faq> selectByGroupId(Integer groupId);
}