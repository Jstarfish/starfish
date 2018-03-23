package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.FaqCatDao;
import priv.starfish.mall.dao.comn.FaqDao;
import priv.starfish.mall.dao.comn.FaqGroupDao;
import priv.starfish.mall.comn.entity.Faq;
import priv.starfish.mall.comn.entity.FaqCat;
import priv.starfish.mall.comn.entity.FaqGroup;
import priv.starfish.mall.service.ComnService;

@Service("comnService")
public class ComnServiceImpl implements ComnService {
	@Resource
	FaqDao faqDao;

	@Resource
	FaqCatDao faqCatDao;

	@Resource
	FaqGroupDao faqGroupDao;

	// ---------------------------------------常见问题分类--------------------------------------------
	@Override
	public FaqCat getFaqCatById(Integer id) {
		return faqCatDao.selectById(id);
	}

	@Override
	public boolean existFaqCatByName(String name) {
		return faqCatDao.selectCountByName(name) > 0;
	}

	@Override
	public boolean saveFaqCat(FaqCat faqCat) {
		return faqCatDao.insert(faqCat) > 0;
	}

	@Override
	public boolean updateFaqCat(FaqCat faqCat) {
		return faqCatDao.update(faqCat) > 0;
	}

	@Override
	public boolean deleteFaqCatById(Integer id) {
		List<Integer> groupIds = faqGroupDao.selectIds(id);// 获取常见问题分组ID集合
		faqDao.deleteByGroupIds(groupIds);// 根据常见问题分组ID集合删除所属常见问题
		faqGroupDao.deleteByCatId(id);// 根据常见问题分类ID删除常见问题分组
		return faqCatDao.deleteById(id) > 0;// 删除常见问题分类
	}

	@Override
	public PaginatedList<FaqCat> getFaqCatsByFilter(PaginatedFilter paginatedFilter) {
		return faqCatDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<FaqCat> getFaqCats() {
		List<FaqCat> faqCatList =faqCatDao.selectAll();
		for (FaqCat fc : faqCatList) {
			fc.setFaqGroups(getFaqGroupsByCatId(fc.getId()));
		}
		return faqCatList;
	}

	// ---------------------------------------常见问题分组--------------------------------------------
	@Override
	public PaginatedList<FaqGroup> getFaqGroupsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<FaqGroup> paginatedList = faqGroupDao.selectByFilter(paginatedFilter);
		List<FaqGroup> faqGroupList = paginatedList.getRows();
		for (FaqGroup fg : faqGroupList) {
			fg.setFaqCat(faqCatDao.selectById(fg.getCatId()));
		}
		return paginatedList;
	}

	@Override
	public List<FaqGroup> getFaqGroupsByCatId(Integer catId) {
		return faqGroupDao.selectByCatId(catId);
	}

	@Override
	public FaqGroup getFaqGroupById(Integer groupId) {
		return faqGroupDao.selectById(groupId);
	}

	@Override
	public boolean saveFaqGroup(FaqGroup faqGroup) {
		return faqGroupDao.insert(faqGroup) > 0;
	}

	@Override
	public boolean updateFaqGroup(FaqGroup faqGroup) {
		return faqGroupDao.update(faqGroup) > 0;
	}

	@Override
	public boolean deleteFaqGroupById(Integer groupId) {
		faqDao.deleteByGroupId(groupId);// 根据常见问题分组ID删除常见问题
		return faqGroupDao.deleteById(groupId) > 0;// 删除常见问题分组
	}

	// ---------------------------------------常见问题--------------------------------------------
	@Override
	public PaginatedList<Faq> getFaqsByFilter(PaginatedFilter paginatedFilter) {
		MapContext filter = paginatedFilter.getFilterItems();
		if(filter!=null){
			if(StrUtil.hasText(paginatedFilter.getKeywords())){
				filter.put("keywords", paginatedFilter.getKeywords());
			}
		}
		PaginatedList<Faq> paginatedList = faqDao.selectByFilter(paginatedFilter);
		List<Faq> faqList = paginatedList.getRows();
		for (Faq f : faqList) {
			f.setFaqGroup(faqGroupDao.selectById(f.getGroupId()));
		}
		return paginatedList;
	}

	@Override
	public Faq getFaqById(Integer id) {
		return faqDao.selectById(id);
	}

	@Override
	public boolean saveFaq(Faq faq) {
		return faqDao.insert(faq) > 0;
	}

	@Override
	public boolean updateFaq(Faq faq) {
		return faqDao.update(faq) > 0;
	}

	@Override
	public boolean deleteFaqById(Integer id) {
		return faqDao.deleteById(id) > 0;
	}

	// -----------------------------------------------------------------------------------
}
