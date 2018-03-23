package priv.starfish.mall.comn.dto;

/**
 * 区划组成部分信息
 * 
 * @author koqiui
 * @date 2015年11月3日 上午11:03:19
 *
 */
public class RegionParts {
	public Integer id;
	public String name;
	public Integer level;
	public String idPath;
	//
	public String fullName;

	public Integer provinceId;
	public Integer cityId;
	public Integer countyId;
	public Integer townId;

	//
	public static RegionParts newOne() {
		return new RegionParts();
	}

	@Override
	public String toString() {
		return "RegionParts [id=" + id + ", name=" + name + ", level=" + level + ", idPath=" + idPath + ", fullName=" + fullName + ", provinceId=" + provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", townId=" + townId + "]";
	}
}
