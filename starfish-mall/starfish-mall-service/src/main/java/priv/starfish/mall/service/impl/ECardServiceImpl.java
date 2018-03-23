package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.dao.ecard.ECardDao;
import priv.starfish.mall.dao.ecard.ECardTransactRecDao;
import priv.starfish.mall.dao.ecard.ECardTransferRecDao;
import priv.starfish.mall.dao.ecard.UserECardDao;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.ecard.entity.ECardNo;
import priv.starfish.mall.ecard.entity.ECardTransactRec;
import priv.starfish.mall.ecard.entity.ECardTransferRec;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.dao.order.ECardOrderItemDao;
import priv.starfish.mall.order.entity.ECardOrderItem;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.dao.svcx.SvcxRankDiscDao;
import priv.starfish.mall.svcx.entity.Svcx;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;

@Service("eCardService")
public class ECardServiceImpl extends BaseServiceImpl implements ECardService {
	/** ecard编号数字长度 */
	private static final int LENGTH_OF_ECARD_NO = 9;

	@Resource
	ECardDao eCardDao;

	@Resource
	UserECardDao userECardDao;

	@Resource
	ECardOrderItemDao eCardOrderItemDao;

	@Resource
	SvcxRankDiscDao svcxRankDiscDao;

	@Resource
	ECardTransactRecDao eCardTransactRecDao;

	@Resource
	ECardTransferRecDao eCardTransferRecDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	UserDao userDao;

	@Override
	public String newCardNo() {
		String abbr = "E";
		Integer seqInt = eCardDao.newSeqInt(ECardNo.class);
		return abbr + StrUtil.leftPadding(seqInt.toString(), LENGTH_OF_ECARD_NO, '0');
	}

	// ------------------------e卡设置------------------------------
	@Override
	public PaginatedList<ECard> getECardsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ECard> pagListEcards = eCardDao.selectByFilter(paginatedFilter);
		// 设置LOGO查看路径
		for (ECard eCard : pagListEcards.getRows()) {
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			eCard.setFileBrowseUrl(browseUrl);
		}
		return pagListEcards;
	}

	@Override
	public boolean saveECard(ECard eCard) {
		int maxSeqNo = eCardDao.getEntityMaxSeqNo(ECard.class) + 1;
		eCard.setSeqNo(maxSeqNo);

		return eCardDao.insert(eCard) > 0;
	}

	@Override
	public boolean updateECard(ECard eCard) {
		return eCardDao.update(eCard) > 0;
	}

	@Override
	public boolean deleteECardById(String code) {
		return eCardDao.deleteById(code) > 0;
	}

	@Override
	public ECard getECardById(String code) {
		ECard eCard = eCardDao.selectById(code);
		if (eCard != null) {
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			eCard.setFileBrowseUrl(browseUrl);
		}
		return eCard;
	}

	@Override
	public Boolean existECardByName(String name) {
		return eCardDao.existsByName(name) > 0;
	}

	@Override
	public Boolean existECardByUserIdAndCardId(Integer userId, Integer cardId, Boolean invalid) {
		// 添加过滤条件参数
		MapContext filter = MapContext.newOne();
		filter.put("userId", userId);
		filter.put("cardId", cardId);
		filter.put("invalid", invalid);

		return userECardDao.selectCountByFilter(filter) > 0;
	}

	@Override
	public Boolean updateUserECardsForMerge(Integer cardIdToMerge, Integer cardIdToBeMerged) {
		Boolean result = false;

		UserECard cardToMerge = userECardDao.selectById(cardIdToMerge);
		BigDecimal remainValToMerge = cardToMerge.getRemainVal();
		// 如果余额为0直接返回
		if (remainValToMerge.equals(new BigDecimal(0))) {
			logger.debug("Remain value of the card to merge is zero.");
			return result;
		}

		// 执行目标卡余额合并
		MapContext params = MapContext.newOne();
		params.put("cardIdToUpdate", cardIdToBeMerged);
		params.put("remainValForIncrease", remainValToMerge);
		params.put("invalid", false);
		params.put("deleted", false);
		result = userECardDao.updateForMerge(params) > 0;
		if (!result) {
			logger.debug("Error occured during update user_ecard.");
			return result;
		}

		// 执行原卡余额清零
		params.clear();
		params.put("cardIdToUpdate", cardIdToMerge);
		params.put("remainValForIncrease", remainValToMerge.negate());
		params.put("invalid", true);
		params.put("deleted", false);
		result = userECardDao.updateForMerge(params) > 0;
		if (!result) {
			logger.warn("Error occured during update user_ecard.");
			return result;
		}

		// 临时变量，用于保存交易记录
		ECardTransactRec eCardTransactRec = new ECardTransactRec();
		Date theTime = new Date();
		// 保存原卡交易记录
		cardToMerge = userECardDao.selectById(cardIdToMerge);
		eCardTransactRec.setCardId(cardIdToMerge);
		eCardTransactRec.setSubject("valuetrans");
		eCardTransactRec.setDirectFlag(-1);
		eCardTransactRec.setUserId(cardToMerge.getUserId());
		eCardTransactRec.setTheTime(theTime);
		eCardTransactRec.setTheVal(remainValToMerge);
		eCardTransactRec.setRemainVal(cardToMerge.getRemainVal());
		result = eCardTransactRecDao.insert(eCardTransactRec) > 0;
		if (!result) {
			logger.warn("Error occured during insert record to ecard_transact_rec for card to merge.");
			return result;
		}

		// 获取合并余额后的目标卡信息
		UserECard cardToBeMerged = userECardDao.selectById(cardIdToBeMerged);
		// 保存目标卡交易记录
		eCardTransactRec.setCardId(cardIdToBeMerged);
		eCardTransactRec.setSubject("valuetrans");
		eCardTransactRec.setDirectFlag(1);
		eCardTransactRec.setUserId(cardToBeMerged.getUserId());
		eCardTransactRec.setTheTime(theTime);
		eCardTransactRec.setTheVal(remainValToMerge);
		eCardTransactRec.setRemainVal(cardToBeMerged.getRemainVal());
		result = eCardTransactRecDao.insert(eCardTransactRec) > 0;
		if (!result) {
			logger.warn("Error occured during insert record into ecard_transact_rec for card to be merged.");
			return result;
		}

		return result;
	}

	@Override
	public Boolean updateUserECardForPresent(Integer cardId, Integer userIdFrom, String userPhoneTo) {
		Boolean result = false;
		// 获取受赠用户信息
		User userTo = userDao.selectByPhoneNo(userPhoneTo);
		// 更新相关参数
		MapContext params = MapContext.newOne();
		params.put("cardId", cardId);
		params.put("userId", userTo.getId());
		// 更新用户E卡归属用户
		result = userECardDao.updateForPresent(params) > 0;
		if (!result) {
			logger.warn(String.format("Error occured during update user_ecard for card present. cardId:%d", cardId));
			return result;
		}

		// e卡转赠记录
		ECardTransferRec eCardTransferRec = new ECardTransferRec();
		eCardTransferRec.setCardId(cardId);
		User userFrom = userDao.selectById(userIdFrom);
		eCardTransferRec.setUserIdFrom(userIdFrom);
		eCardTransferRec.setUserNameFrom(userFrom.getNickName());
		eCardTransferRec.setUserPhoneFrom(userFrom.getPhoneNo());
		eCardTransferRec.setUserIdTo(userTo.getId());
		eCardTransferRec.setUserNameTo(userTo.getNickName());
		eCardTransferRec.setUserPhoneTo(userTo.getPhoneNo());
		UserECard userECard = userECardDao.selectById(cardId);
		eCardTransferRec.setRemainVal(userECard.getRemainVal());
		eCardTransferRec.setCardCode(userECard.getCardCode());
		eCardTransferRec.setFaceValue(userECard.getFaceValue());
		eCardTransferRec.setCardNo(userECard.getCardNo());
		result = eCardTransferRecDao.insert(eCardTransferRec) > 0;
		if (!result) {
			logger.warn(String.format("Error occured during insert record into ecard_transfer_rec for card present. cardId:%d", cardId));
			return result;
		}

		return result;
	}

	// TODO------------------------e卡列表------------------------------
	@Override
	public PaginatedList<UserECard> getUserECardsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserECard> userECards = userECardDao.selectByFilter(paginatedFilter);
		for (UserECard userECard : userECards.getRows()) {
			ECard eCard = eCardDao.selectById(userECard.getCardCode());
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			userECard.setFileBrowseUrl(browseUrl);
			userECard.seteCard(eCard);
		}
		return userECards;
	}

	@Override
	public Boolean saveUserECard(UserECard userECard) {
		return userECardDao.insert(userECard) > 0;
	}

	@Override
	public List<UserECard> getUserECardsByFilterNormal(MapContext filter) {
		List<UserECard> userECards = userECardDao.selectByFilterNormal(filter);
		if (userECards != null) {
			for (UserECard userECard : userECards) {
				ECard eCard = eCardDao.selectById(userECard.getCardCode());
				String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				userECard.setFileBrowseUrl(browseUrl);
				userECard.seteCard(eCard);
			}
		}
		return userECards;
	}

	@Override
	public PaginatedList<UserECard> getUserECardsByFilterBack(PaginatedFilter paginatedFilter) {
		return userECardDao.selectByFilterBack(paginatedFilter);
	}

	// TODO------------------------e卡交易列表------------------------------
	@Override
	public PaginatedList<ECardTransactRec> getECardTradeRecByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ECardTransactRec> paginatedList = eCardTransactRecDao.selectByFilter(paginatedFilter);
		List<ECardTransactRec> list = paginatedList.getRows();
		for (ECardTransactRec eCardTransactRec : list) {
			UserECard userECard = userECardDao.selectById(eCardTransactRec.getCardId());
			eCardTransactRec.setUserECard(userECard);
		}

		return eCardTransactRecDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<ECardTransactRec> getECardTransactRecsByCardIdAndUserId(Integer cardId, Integer userId) {
		MapContext filter = MapContext.newOne();
		filter.put("cardId", cardId);
		filter.put("userId", userId);
		return eCardTransactRecDao.selectByFilterNormal(filter);
	}

	// TODO------------------------e卡转赠列表------------------------------
	@Override
	public PaginatedList<ECardTransferRec> getECardTransferRecByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ECardTransferRec> eCardTransferRecs = eCardTransferRecDao.selectByFilter(paginatedFilter);
		for (ECardTransferRec eCardTransferRec : eCardTransferRecs.getRows()) {
			ECard eCard = eCardDao.selectById(eCardTransferRec.getCardCode());
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			eCardTransferRec.setFileBrowseUrl(browseUrl);
		}
		return eCardTransferRecs;
	}

	@Override
	public ECardTransferRec getECardTransferRecByCardIdAndUserIdTo(Integer cardId, Integer userIdTo) {
		return eCardTransferRecDao.selectByCardIdAndUserIdTo(cardId, userIdTo);
	}

	@Override
	public List<ECardTransferRec> getECardTransferRecs(MapContext filter) {
		List<ECardTransferRec> eCardTransferRecs = eCardTransferRecDao.selectByFilterNormal(filter);
		for (ECardTransferRec eCardTransferRec : eCardTransferRecs) {
			ECard eCard = eCardDao.selectById(eCardTransferRec.getCardCode());
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			eCardTransferRec.setFileBrowseUrl(browseUrl);
		}
		return eCardTransferRecs;
	}

	@Override
	public Boolean updateECardTransferRecForDelete(Integer eCardTransferRecId, Integer userIdFrom) {
		ECardTransferRec eCardTransferRec = new ECardTransferRec();
		eCardTransferRec.setId(eCardTransferRecId);
		eCardTransferRec.setUserIdFrom(userIdFrom);
		eCardTransferRec.setDeleted(true);
		return eCardTransferRecDao.updateForDelete(eCardTransferRec) > 0;
	}

	@Override
	public Boolean updateUserECardForDelete(Integer cardId) {
		MapContext params = MapContext.newOne();
		params.put("cardId", cardId);
		params.put("invalid", true);
		return userECardDao.updateForDelete(params) > 0;
	}

	@Override
	public List<ECard> getNormalECards() {
		List<ECard> listECard = eCardDao.selectNormalECards();
		if (listECard != null && listECard.size() > 0) {
			for (ECard eCard : listECard) {
				String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				eCard.setFileBrowseUrl(browseUrl);
			}
		}

		return listECard;
	}

	// TODO------------------------e卡订单项------------------------------
	@Override
	public Boolean saveECardOrderItem(ECardOrderItem eCardOrderItem) {
		return eCardOrderItemDao.insert(eCardOrderItem) > 0;
	}

	@Override
	public Boolean unbindUserECard(UserECard userECard) {
		userECard.setShopId(null);
		userECard.setShopName(null);
		return userECardDao.updateUnbind(userECard) > 0;
	}

	@Override
	public Boolean updateUserECardForChangeRemainVal(UserECard userECard) {
		if (userECard != null) {
			if (userECard.getDeleted() == true) {
				userECard.setDeleted(false);
			}
			if (userECard.getInvalid() == true) {
				userECard.setInvalid(false);
			}
		}
		return userECardDao.updateForChangeRemainVal(userECard) > 0;
	}

	// TODO------------------------e卡详情------------------------------

	@Override
	public UserECard getUserECardInfo(Integer cardId) {
		UserECard userECard = userECardDao.selectById(cardId);
		if (userECard != null) {
			ECard eCard = eCardDao.selectById(userECard.getCardCode());
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			userECard.setFileBrowseUrl(browseUrl);
			userECard.seteCard(eCard);
		}

		return userECard;
	}

	@Override
	public Map<Integer, Integer> getECardShopsByUserId(Integer userId) {
		return userECardDao.selectECardShopsByUserId(userId);
	}

	@Override
	public boolean updateECardForSeqNo(int seqNo, int nowSeqNo) {
		List<ECard> list = eCardDao.selectBySeqNo(seqNo, nowSeqNo);
		Integer count = 0;
		// 比较
		if (seqNo > nowSeqNo) {
			if (null != list && list.size() != 0) {
				for (ECard eCard : list) {
					eCardDao.updateForSeqNo(eCard.getCode(), eCard.getSeqNo() + 1);
					count++;
				}
			}
		} else {
			if (null != list && list.size() != 0) {
				for (ECard eCard : list) {
					eCardDao.updateForSeqNo(eCard.getCode(), eCard.getSeqNo() - 1);
					count++;
				}
			}
		}

		return list.size() == count;
	}

	@Override
	public List<SvcxRankDisc> getSvcRankDiscs(Integer shopId, Integer svcId) {
		return svcxRankDiscDao.selectByShopIdAndSvcIdAndRank(shopId, svcId);
	}

	@Override
	public boolean saveSvcRankDiscs(Svcx svcx) {
		boolean flag = true;
		List<SvcxRankDisc> svcxRankDiscs = svcx.getSvcxRankDiscs();
		if (svcxRankDiscs != null) {
			for (SvcxRankDisc svcxRankDisc : svcxRankDiscs) {
				svcxRankDisc.setSvcId(svcx.getId());
				svcxRankDisc.setSvcName(svcx.getName());
				svcxRankDisc.setSvcKindId(svcx.getKindId());
				svcxRankDisc.setDisabled(false);
				svcxRankDisc.setTs(new Date());
				SvcxRankDisc SvcxRank = svcxRankDiscDao.selectByShopIdAndSvcIdAndRank(svcxRankDisc.getShopId(), svcxRankDisc.getSvcId(), svcxRankDisc.getRank());
				if (SvcxRank != null) {
					if (svcxRankDisc.getRate() != null) {
						svcxRankDisc.setId(SvcxRank.getId());
						flag = (svcxRankDiscDao.update(svcxRankDisc) > 0) && flag;
					} else {
						svcxRankDiscDao.deleteById(SvcxRank.getId());
					}
				} else if (svcxRankDisc.getRate() != null) {
					flag = (svcxRankDiscDao.insert(svcxRankDisc) > 0) && flag;
				}

			}
		}
		return flag;
	}

	@Override
	public SvcxRankDisc getSvcRankDiscByFilter(Integer shopId, Integer svcId, Integer rank) {
		//
		return svcxRankDiscDao.selectByShopIdAndSvcIdAndRank(shopId, svcId, rank);
	}

	@Override
	public UserECard getUserECardByCardNo(String cardNo) {
		return userECardDao.selectByCardNo(cardNo);
	}
}
