package priv.starfish.mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.market.AdvertDao;
import priv.starfish.mall.dao.market.AdvertLinkDao;
import priv.starfish.mall.dao.market.AdvertPosDao;
import priv.starfish.mall.market.entity.Advert;
import priv.starfish.mall.market.entity.AdvertLink;
import priv.starfish.mall.market.entity.AdvertPos;
import priv.starfish.mall.service.AdvertService;

@Service("advertService")
public class AdvertServiceImpl extends BaseServiceImpl implements AdvertService {

	@Resource
	AdvertDao advertDao;

	@Resource
	AdvertLinkDao advertLinkDao;

	@Resource
	AdvertPosDao advertPosDao;

	@Resource
	FileRepository fileRepository;

	@Override
	public Advert getAdvertByName(String advertName) {
		return advertDao.selectByName(advertName);
	}

	@Override
	public boolean deleteAdvertLinkById(Integer advertLinkId) {
		int count = advertLinkDao.deleteById(advertLinkId);
		return count > 0;
	}

	@Override
	public boolean deleteAdvertById(Integer advertId) {
		boolean flag = true;
		List<AdvertLink> advertLinks = advertLinkDao.selectByAdvertId(advertId);
		for (AdvertLink advertLink : advertLinks) {
			Integer advertLinkId = advertLink.getId();
			advertLinkDao.deleteById(advertLinkId);
		}
		flag = advertDao.deleteById(advertId) > 0 && flag;
		return flag;
	}

	@Override
	public boolean deleteAdvertByIds(List<Integer> advertIds) {
		boolean flag = true;
		for (Integer advertId : advertIds) {
			flag = this.deleteAdvertById(advertId) && flag;
		}
		return flag;
	}

	@Override
	public boolean saveAdvert(Advert advert) {
		boolean flag = true;
		flag = advertDao.insert(advert) > 0;
		if (flag) {
			List<AdvertLink> listLiks = advert.getAdvertLinks();
			for (AdvertLink advertLink : listLiks) {
				advertLink.setAdvertId(advert.getId());
				flag = advertLinkDao.insert(advertLink) > 0 && flag;
			}
		}
		//
		return flag;
	}

	@Override
	public PaginatedList<Advert> getAdvertsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Advert> pagListAdvert = advertDao.selectByFilter(paginatedFilter);
		List<Advert> listAdvert = pagListAdvert.getRows();
		//
		for (Advert advert : listAdvert) {
			String posCode = advert.getPosCode();
			AdvertPos advertPos = advertPosDao.selectById(posCode);
			advert.setAdvertPos(advertPos);
			//
			List<AdvertLink> advertLinks = advertLinkDao.selectByAdvertId(advert.getId());
			if (advertLinks != null) {
				// 设置图片浏览路径
				for (AdvertLink advertLink : advertLinks) {
					String browseUrl = fileRepository.getFileBrowseUrl(advertLink.getImageUsage(), advertLink.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					advertLink.setImageUrl(browseUrl);
				}
				//
				advert.setAdvertLinks(advertLinks);
				AdvertLink advertLink = advertLinkDao.selectMinMaxDate(advert.getId());
				if (advertLink != null) {
					Date minDate = advertLink.getMinDate();
					advert.setStartDate(minDate);
					Date maxDate = advertLink.getMaxDate();
					advert.setEndDate(maxDate);
				}

			}
		}
		return pagListAdvert;
	}

	@Override
	public List<AdvertPos> getAdvertPoss() {
		return advertPosDao.selectAdvertPoss();
	}

	@Override
	public boolean updateAdvert(Advert advert) {
		boolean flag = true;
		List<AdvertLink> listLiks = advert.getAdvertLinks();
		if (listLiks.size() > 1 && advert.getCaroAnim() != null && advert.getCaroIntv() != null) {
			advert.setCaroFlag(true);
		} else {
			advert.setCaroFlag(false);
		}
		//
		flag = advertDao.update(advert) > 0;
		//
		for (AdvertLink advertLink : listLiks) {
			advertLink.setAdvertId(advert.getId());
			if (advertLink.getId() == null) {
				flag = advertLinkDao.insert(advertLink) > 0 && flag;
			} else {
				flag = advertLinkDao.update(advertLink) > 0 && flag;
			}
		}
		return flag;
	}

	@Override
	public Advert getAdvertByPosCode(String posCode) {
		Advert advert = advertDao.selectByPosCode(posCode);
		if (advert != null) {
			List<AdvertLink> advertLinks = advertLinkDao.selectByAdvertId(advert.getId());
			// 生成最终图片路径
			for (AdvertLink advertLink : advertLinks) {
				String logoUsage = advertLink.getImageUsage();
				String logoPath = advertLink.getImagePath();
				String imageUrl = fileRepository.getFileBrowseUrl(logoUsage, logoPath);
				if (StrUtil.isNullOrBlank(imageUrl)) {
					imageUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				advertLink.setImageUrl(imageUrl);
			}
			advert.setAdvertLinks(advertLinks);
		}
		return advert;
	}

	@Override
	public List<Advert> getAdverts() {
		List<Advert> adverts = advertDao.selectAll();
		for (Advert advert : adverts) {
			List<AdvertLink> advertLinks = advertLinkDao.selectByAdvertId(advert.getId());
			// 生成最终图片路径
			for (AdvertLink advertLink : advertLinks) {
				String logoUsage = advertLink.getImageUsage();
				String logoPath = advertLink.getImagePath();
				String imageUrl = fileRepository.getFileBrowseUrl(logoUsage, logoPath);
				if (StrUtil.isNullOrBlank(imageUrl)) {
					imageUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				advertLink.setImageUrl(imageUrl);
			}
			advert.setAdvertLinks(advertLinks);
		}
		return adverts;
	}

}
