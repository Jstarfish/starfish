package priv.starfish.mall.web.misc;

import org.springframework.stereotype.Service;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.webols.OlsCustomer;
import priv.starfish.common.webols.OlsServant;
import priv.starfish.common.webols.OlsUserInfoProvider;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.mall.entity.Mall;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.service.MallService;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.shop.entity.Shop;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service("olsUserInfoProvider")
public class OlsUserInfoProviderImpl implements OlsUserInfoProvider {
	@Resource
	UserService userService;

	@Resource
	MemberService memberService;

	@Resource
	MallService mallService;

	@Resource
	ShopService shopService;

	@Resource
	FileRepository fileRepository;

	@Override
	public OlsCustomer getCustomer(String customerId, HttpServletRequest request) {
		OlsCustomer customer = new OlsCustomer();

		Member member = memberService.getMemberById(Integer.valueOf(customerId));
		customer.setId(member.getId().toString());
		customer.setName(member.getUser().getNickName());
		String logoUrl = fileRepository.getFileBrowseUrl(member.getHeadUsage(), member.getHeadPath());
		customer.setLogoUrl(logoUrl);

		return customer;
	}

	@Override
	public OlsServant getServant(String servantId, HttpServletRequest request) {
		OlsServant servant = new OlsServant();

		OnlineServeNo ols = userService.getOnlineServeNoById(Integer.valueOf(servantId));
		servant.setId(ols.getId().toString());
		servant.setName(ols.getServantName());
		servant.setNo(ols.getServantNo());
		servant.setOrgName(ols.getEntityName());
		// 会员或店铺Id
		AuthScope scope = ols.getScope();
		Integer entityId = ols.getEntityId();
		if (AuthScope.shop.equals(scope)) {
			Shop shop = shopService.getShopInfoById(entityId).getShop();
			String logoUrl = fileRepository.getFileBrowseUrl(shop.getLogoUsage(), shop.getLogoPath());
			servant.setLogoUrl(logoUrl);
		} else {
			Mall mall = mallService.getMallById(entityId);
			String logoUrl = fileRepository.getFileBrowseUrl(mall.getLogoUsage(), mall.getLogoPath());
			servant.setLogoUrl(logoUrl);
		}

		return servant;
	}

}
