package priv.starfish.mall.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.RegionDao;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.dao.demo.ShopxDao;
import priv.starfish.mall.demo.entity.Shopx;
import priv.starfish.mall.service.DemoService;

@Service("demoService")
public class DemoServiceImpl extends BaseServiceImpl implements DemoService {
	@Resource
	ShopxDao shopxDao;

	@Resource
	RegionDao regionDao;

	@Override
	public Shopx getShopxById(Integer id) {
		return shopxDao.selectById(id);
	}

	@Override
	public Shopx getShopxByName(String name) {
		return shopxDao.selectByName(name);
	}

	@Override
	public boolean saveShopx(Shopx shopx) {
		if (shopx.getId() != null) {
			return this.updateShopx(shopx);
		}
		// 设置简拼
		String py = StrUtil.chsToPy(shopx.getName());
		shopx.setPy(py);
		// 设置region id 信息
		RegionParts regionParts = regionDao.selectPartsById(shopx.getRegionId());
		shopx.setRegionName(regionParts.fullName);
		shopx.setProvinceId(regionParts.provinceId);
		shopx.setCityId(regionParts.cityId);
		shopx.setCountyId(regionParts.countyId);
		shopx.setTownId(regionParts.townId);
		// 检查街道信息
		String street = shopx.getStreet();
		if (street == null) {
			street = "";
			shopx.setStreet(street);
		}
		// 检查地址信息
		String address = shopx.getAddress();
		if (StrUtil.isNullOrBlank(address)) {
			address = shopx.getRegionName() + street;
			shopx.setAddress(address);
		}
		// 设置注册时间
		Date now = new Date();
		shopx.setRegTime(now);
		// 其他默认值
		shopx.setClosed(false);
		shopx.setDisabled(false);

		return shopxDao.insert(shopx) > 0;
	}

	@Override
	public boolean updateShopx(Shopx shopx) {
		// 重要变更标记
		boolean hasChanges = false;
		//
		String name = shopx.getName();
		if (name != null) {
			// 设置简拼
			String py = StrUtil.chsToPy(name);
			shopx.setPy(py);
			//
			hasChanges = true;
		}
		//
		Integer regionId = shopx.getRegionId();
		if (regionId != null) {
			// 设置region id 信息
			RegionParts regionParts = regionDao.selectPartsById(regionId);
			shopx.setRegionName(regionParts.fullName);
			shopx.setProvinceId(regionParts.provinceId);
			shopx.setCityId(regionParts.cityId);
			shopx.setCountyId(regionParts.countyId);
			shopx.setTownId(regionParts.townId);
			//
			hasChanges = true;
		}
		//
		if (shopx.getDisabled() != null || shopx.getClosed() != null || shopx.getAddress() != null || shopx.getAuditStatus() != null) {
			//
			hasChanges = true;
		}
		//
		if (hasChanges) {
			Date now = new Date();
			shopx.setChangeTime(now);
		}

		return shopxDao.update(shopx) > 0;
	}

	@Override
	public boolean deleteShopxById(Integer id) {
		return shopxDao.deleteById(id) > 0;
	}

	@Override
	public boolean existShopxByName(String name) {
		return shopxDao.selectByName(name) != null;
	}

	@Override
	public PaginatedList<Shopx> getShopxsByFilter(PaginatedFilter paginatedFilter) {
		return shopxDao.selectByFilter(paginatedFilter);
	}

	@Override
	public PaginatedList<Shopx> getShopxsByScrolling(PaginatedFilter pager) {
		return shopxDao.selectByScrolling(pager);
	}

	@Override
	public PaginatedList<Shopx> getShopxsByLatestChanges(PaginatedFilter pager) {
		return shopxDao.selectByLatestChanges(pager);
	}

	@Override
	public boolean updateShopxAsIndexed(Integer id, Date indexTime) {
		assert indexTime != null;
		//
		return shopxDao.updateAsIndexed(id, indexTime) > 0;
	}

}
