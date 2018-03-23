package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.Region;

@IBatisSqlTarget
public interface RegionDao extends BaseDao<Region, Integer> {

	/**
	 * 获取地区表最大id
	 * 
	 * @author 王少辉
	 * @date 2015年8月13日 下午9:14:10
	 * 
	 * @return 返回最大id
	 */
	int getMaxId();

	/**
	 * 根据地区主键获取地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param id
	 *            地区主键
	 * @return 返回地区
	 */
	Region selectById(Integer id);

	/**
	 * 根据地区code获取地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param code
	 *            地区code
	 * @return 返回地区
	 */
	Region selectByCode(String code);

	/**
	 * 根据地区父id和名称获取地区信息
	 * 
	 * @author koqiui
	 * @date 2016年1月4日 下午6:43:24
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	Region selectByParentIdAndName(Integer parentId, String name);

	/**
	 * 根据地区name获取地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param name
	 *            地区name
	 * @return 返回地区
	 */
	List<Region> selectByName(String name);

	/**
	 * 根据地区父id和级别查询下一级地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param level
	 *            地区父id
	 * @param parentId
	 *            地区级别
	 * @return 返回下一级地区
	 */
	List<Region> selectByParentId(Integer parentId);

	/**
	 * 根据所属百度城市代码和名称获取对应的县级地区
	 * 
	 * @author koqiui
	 * @date 2016年1月4日 下午8:20:04
	 * 
	 * @param bdCityCode
	 *            所属百度城市代码
	 * @param countyName
	 *            县名
	 * @return
	 */
	Region selectCountyByBdCityCodeAndName(Integer bdCityCode, String countyName);

	/**
	 * 新增一条地区信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param region
	 *            要新增的地区
	 * @return int 返回新增成功数
	 */
	int insert(Region region);

	/**
	 * 更新一条地区信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param region
	 *            要更新的地区
	 * @return int 返回更新成功数
	 */
	int update(Region region);

	/**
	 * 根据主键删除地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:01:03
	 * 
	 * @param id
	 *            地区主键
	 * @return int 返回删除成功数
	 */
	int deleteById(Integer id);

	/**
	 * 批量删除
	 * 
	 * @author 毛智东
	 * @date 2015年5月25日 下午9:02:48
	 * 
	 * @param list
	 * @return
	 */
	int deleteByIds(List<Integer> list);

	/**
	 * 返回地区的部分信息
	 * 
	 * @author koqiui
	 * @date 2015年11月3日 下午1:30:23
	 * 
	 * @param id
	 * @return
	 */
	RegionParts selectPartsById(Integer id);

}