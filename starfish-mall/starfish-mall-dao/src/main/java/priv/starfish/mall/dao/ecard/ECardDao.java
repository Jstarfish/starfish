package priv.starfish.mall.dao.ecard;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.ecard.entity.ECard;

@IBatisSqlTarget
public interface ECardDao extends BaseDao<ECard, String> {
	ECard selectById(String code);

	ECard selectByName(String name);

	ECard selectByRank(Integer rank);

	int insert(ECard eCard);

	int update(ECard eCard);

	int deleteById(String code);

	/**
	 * 分页查询e卡类型
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午4:06:23
	 * 
	 * @param paginatedFilter
	 *            like name , = disabled , = deleted
	 * @return
	 */
	PaginatedList<ECard> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据name判断是否已存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午5:14:49
	 * 
	 * @param name
	 * @return
	 */
	Integer existsByName(String name);

	/**
	 * 返回所有可用的e卡
	 * 
	 * @author koqiui
	 * @date 2015年12月23日 下午2:41:25
	 * 
	 * @return
	 */
	List<ECard> selectNormalECards();

	/**
	 * 查找此范围内的E卡
	 * 
	 * @author "WJJ"
	 * @date 2016年1月6日 下午2:57:27
	 * 
	 * @param seqNo
	 * @param nowSeqNo
	 * @return
	 */
	List<ECard> selectBySeqNo(int seqNo, int nowSeqNo);

	/**
	 * 根据code，更改seqNo
	 * 
	 * @author "WJJ"
	 * @date 2016年1月6日 下午3:38:28
	 * 
	 * @param code
	 * @param seqNo
	 * @return
	 */
	int updateForSeqNo(String code, int seqNo);

}