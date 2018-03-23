package priv.starfish.mall.dao.mall;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.mall.entity.MallNotice;

@IBatisSqlTarget
public interface MallNoticeDao extends BaseDao<MallNotice, Integer> {

	/**
	 * 根据商城主键获取商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午09:57:28
	 * 
	 * @param id
	 *            商城公告主键
	 * @return 返回商城公告
	 */
	MallNotice selectById(Integer id);

	/**
	 * 分页获取商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月21日 上午10:35:09
	 * 
	 * @param paginatedFilter
	 *            分页条件
	 * @return 返回分页结果
	 */
	PaginatedList<MallNotice> selectByFitler(PaginatedFilter paginatedFilter);

	/**
	 * 新增一条商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午09:57:28
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回新增结果
	 */
	int insert(MallNotice mallNotice);

	/**
	 * 更新一条商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午09:57:28
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回修改结果
	 */
	int update(MallNotice mallNotice);

	/**
	 * 根据商城主键删除商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午09:57:28
	 * 
	 * @param id
	 *            商城公告主键
	 * @return 返回删除结果
	 */
	int deleteById(Integer id);

	/**
	 * 根据商城主键删除商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:45:41
	 * 
	 * @param ids
	 *            商城公告主键
	 * @return 返回删除结果
	 */
	int deleteByIds(List<Integer> ids);

}
