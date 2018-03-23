package priv.starfish.mall.dao.comn.impl;

import org.springframework.stereotype.Component;
import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.RegionDao;

import java.util.List;
import java.util.Map;

@Component("regionDao")
public class RegionDaoImpl extends BaseDaoImpl<Region, Integer> implements RegionDao {

	@Override
	public int getMaxId() {
		String sqlId = this.getNamedSqlId("getMaxId");
		//
		return this.getSqlSession().selectOne(sqlId);
	}

	@Override
	public Region selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Region selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public Region selectByParentIdAndName(Integer parentId, String name) {
		String sqlId = this.getNamedSqlId("selectByParentIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("parentId", parentId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<Region> selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		Map<String, Object> params = this.newParamMap();
		if (StrUtil.hasText(name)) {
			params.put("name", SqlBuilder.likeStrVal(name));
		}
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Region> selectByParentId(Integer parentId) {
		String sqlId = this.getNamedSqlId("selectByParentId");
		//
		if (parentId == null) {
			parentId = -1;
		}
		return this.getSqlSession().selectList(sqlId, parentId);
	}

	@Override
	public Region selectCountyByBdCityCodeAndName(Integer bdCityCode, String countyName) {
		String sqlId = this.getNamedSqlId("selectCountyByBdCityCodeAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("bdCityCode", bdCityCode);
		params.put("countyName", countyName);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Region region) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, region);
	}

	@Override
	public int update(Region region) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, region);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		String sqlId = this.getNamedSqlId("deleteByIds");
		//
		return this.getSqlSession().delete(sqlId, ids);
	}

	@Override
	public RegionParts selectPartsById(Integer id) {
		Region region = this.selectById(id);
		if (region != null) {
			RegionParts regionParts = RegionParts.newOne();
			regionParts.id = region.getId();
			regionParts.name = region.getName();
			int level = region.getLevel();
			regionParts.level = level;
			// name
			String provinceName = "", cityName = "", countyName = "", townName = "";
			if (level == 1) {
				provinceName = region.getName();
			} else if (level == 2) {
				cityName = region.getName();
			} else if (level == 3) {
				countyName = region.getName();
			} else if (level == 4) {
				townName = region.getName();
			}
			// id
			regionParts.idPath = region.getIdPath();
			String[] idPathParts = regionParts.idPath.split(",");
			// 省
			regionParts.provinceId = Integer.valueOf(idPathParts[0]);
			//
			if (level > 1) {
				// 市
				regionParts.cityId = Integer.valueOf(idPathParts[1]);
				//
				Region tmpRegion = this.selectById(regionParts.provinceId);
				provinceName = tmpRegion.getName();
			}
			if (level > 2) {
				// 县/区
				regionParts.countyId = Integer.valueOf(idPathParts[2]);
				//
				Region tmpRegion = this.selectById(regionParts.cityId);
				cityName = tmpRegion.getName();
			}
			if (level > 3) {
				// 乡/镇
				regionParts.townId = Integer.valueOf(idPathParts[3]);
				//
				Region tmpRegion = this.selectById(regionParts.countyId);
				countyName = tmpRegion.getName();
			}
			// fullName
			regionParts.fullName = provinceName + cityName + countyName + townName;
			//
			return regionParts;
		}
		return null;
	}

}