package priv.starfish.mall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.service.VendorService;
import priv.starfish.mall.service.util.CodeUtil;
import priv.starfish.mall.dao.vendor.VendorDao;
import priv.starfish.mall.vendor.entity.Vendor;

@Service("vendorService")
public class VendorServiceImpl extends BaseServiceImpl implements VendorService {

	@Resource
	VendorDao vendorDao;

	@Resource
	MallDao mallDao;

	@Resource
	UserDao userDao;

	@Resource
	FileRepository fileRepository;
	
	@Resource
	MemberDao memberDao;

	@Override
	public Boolean existVendorByName(String name) {
		return vendorDao.selectCountByName(name) > 0;
	}

	@Override
	public PaginatedList<Vendor> getVendorsByFilter(PaginatedFilter paginatedFilter) {

		PaginatedList<Vendor> pagListVendors = vendorDao.selectByFilter(paginatedFilter);
		// 设置LOGO查看路径
		for (Vendor vendor : pagListVendors.getRows()) {
			String browseUrl = fileRepository.getFileBrowseUrl(vendor.getLogoUsage(), vendor.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			vendor.setFileBrowseUrl(browseUrl);
		}
		return pagListVendors;

	}

	@Override
	public boolean saveVendor(Vendor vendor) {
		User user = vendor.getUser();
		if (user.getId() == null) {
			userDao.insert(user);
			//添加会员信息
			Member member = new Member();
			member.setId(user.getId());
			member.setDisabled(false);
			memberDao.insert(member);
		}
		// TODO 确定code的获取方法
		vendor.setCode(CodeUtil.newVendorCode());
		vendor.setPy(StrUtil.chsToPy(vendor.getName()));

		return vendorDao.insert(vendor) > 0;
	}

	@Override
	public boolean updateVendor(Vendor vendor) {
		return vendorDao.update(vendor) > 0;
	}

	@Override
	public boolean deleteVendorById(Integer id) {
		// TODO 删除供应商照片

		//
		return vendorDao.deleteById(id) > 0;
	}

	@Override
	public Vendor getVendorById(Integer id) {
		return vendorDao.selectById(id);
	}

	@Override
	public Vendor getVendorByPhone(String phoneNo) {
		return vendorDao.selectByPhoneNo(phoneNo);
	}

}
