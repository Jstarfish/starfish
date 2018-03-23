package priv.starfish.mall.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import priv.starfish.common.base.TypedField;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.TreeNode;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.xport.Importor;
import priv.starfish.common.xport.XlsImportor;
import priv.starfish.mall.categ.entity.BrandDef;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.BizParam;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.SysParam;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.dao.categ.BrandDefDao;
import priv.starfish.mall.dao.comn.BizParamDao;
import priv.starfish.mall.dao.comn.RegionDao;
import priv.starfish.mall.dao.comn.SysParamDao;
import priv.starfish.mall.dao.comn.UserAccountDao;
import priv.starfish.mall.dao.goods.ProductAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.dao.logistic.DeliveryWayDao;
import priv.starfish.mall.dao.logistic.LogisApiDao;
import priv.starfish.mall.dao.logistic.LogisApiParamDao;
import priv.starfish.mall.dao.logistic.LogisComDao;
import priv.starfish.mall.dao.market.*;
import priv.starfish.mall.dao.notify.*;
import priv.starfish.mall.dao.pay.PayWayDao;
import priv.starfish.mall.dao.pay.PayWayParamDao;
import priv.starfish.mall.dao.shop.ShopParamDao;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;
import priv.starfish.mall.logistic.dto.LogisApiDto;
import priv.starfish.mall.logistic.entity.DeliveryWay;
import priv.starfish.mall.logistic.entity.LogisApi;
import priv.starfish.mall.logistic.entity.LogisApiParam;
import priv.starfish.mall.logistic.entity.LogisCom;
import priv.starfish.mall.market.entity.SalesBrandShoppe;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.market.entity.SalesRegion;
import priv.starfish.mall.market.entity.SalesRegionGoods;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.dto.SmsApiDto;
import priv.starfish.mall.notify.entity.*;
import priv.starfish.mall.pay.entity.PayWay;
import priv.starfish.mall.pay.entity.PayWayParam;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.shop.entity.ShopParam;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("settingService")
public class SettingServiceImpl extends BaseServiceImpl implements SettingService {

	@Resource
	RegionDao regionDao;

	@Resource
	SysParamDao sysParamsDao;

	@Resource
	BizParamDao bizParamsDao;

	@Resource
	MailServerDao mailServerDao;

	@Resource
	LogisComDao logisticComDao;

	@Resource
	LogisApiDao logisApiDao;

	@Resource
	LogisApiParamDao logisApiParamDao;
	// WJJ
	@Resource
	PayWayDao payWayDao;
	@Resource
	PayWayParamDao payWayParamDao;

	@Resource
	UserAccountDao userAccountDao;

	@Resource
	SmsApiDao smsApiDao;

	@Resource
	SmsApiParamDao smsApiParamDao;

	@Resource
	MailTemplateDao mailTemplateDao;

	@Resource
	SmsTemplateDao smsTemplateDao;

	@Resource
	DeliveryWayDao deliveryWayDao;

	@Resource
	TplModelDao tplModelDao;

	@Resource
	SmsVerfCodeDao smsVerfCodeDao;

	@Resource
	AdvertDao advertDao;
	@Resource
	AdvertLinkDao advertLinkDao;
	@Resource
	AdvertPosDao advertPosDao;

	@Resource
	MailVerfCodeDao mailVerfCodeDao;

	@Resource
	SalesFloorDao salesFloorDao;

	@Resource
	SalesRegionDao salesRegionDao;

	@Resource
	SalesRegionGoodsDao salesRegionGoodsDao;

	@Resource
	ProductDao productDao;

	@Resource
	ProductAlbumImgDao productAlbumImgDao;

	@Resource
	SalesBrandShoppeDao salesBrandShoppeDao;

	@Resource
	BrandDefDao brandDefDao;

	@Resource
	ShopParamDao shopParamDao;

	private void sendRegionListChangeQueueMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.REGION_LIST;
		simpleMessageSender.sendQueueMessage(BaseConst.QueueNames.CACHE, messageToSend);
	}

	private void sendSysParamChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.SYS_PARAM;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	private void sendBizParamChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.BIZ_PARAM;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	private void sendMailServerChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.MAIL;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	private void sendSmsServerChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.SMS;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	private void sendFreemarkerServerChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.FREEMARKER;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.CONFIG, messageToSend);
	}

	// ----------------------------------地区------------------------------------
	@Override
	public Region getRegionById(Integer id) {
		return regionDao.selectById(id);
	}

	@Override
	public Region getRegionByCode(String code) {
		return regionDao.selectByCode(code);
	}

	@Override
	public Region getRegionByParentIdAndName(Integer parentId, String name) {
		return regionDao.selectByParentIdAndName(parentId, name);
	}

	@Override
	public Region getCountyByBdCityCodeAndName(Integer bdCityCode, String countyName) {
		return regionDao.selectCountyByBdCityCodeAndName(bdCityCode, countyName);
	}

	@Override
	public RegionParts getRegionPartsById(Integer id) {
		return regionDao.selectPartsById(id);
	}

	@Override
	public List<Region> getRegionsByParentId(Integer parentId) {
		return regionDao.selectByParentId(parentId);
	}

	@Override
	public List<TreeNode> getRegionsTree(Region region) {
		List<TreeNode> origList = new ArrayList<TreeNode>(0);
		List<TreeNode> resultList = new ArrayList<TreeNode>(0);

		List<Region> regionList = new ArrayList<Region>(0);
		String conName = "";
		if (region != null) {
			conName = region.getName();
			if (conName != null) {
				regionList = regionDao.selectByName(conName);
			} else {
				regionList = getRegionsByParentId(null);
			}
		}

		// 获取所有地区信息
		if (CollectionUtils.isEmpty(regionList)) {
			return origList;
		}

		// 转换数据
		TreeNode convertNode;
		for (Region tempRegion : regionList) {
			convertNode = new TreeNode();
			convertNode.setId(tempRegion.getId());
			convertNode.setName(tempRegion.getName());
			convertNode.setLevel(tempRegion.getLevel());
			convertNode.setParentId(tempRegion.getParentId());
			convertNode.setIdPath(tempRegion.getIdPath());
			origList.add(convertNode);
		}

		// 装载数据
		TreeNode loadNode;
		for (TreeNode tree : origList) {
			if (conName == null) {
				if (tree.getParentId() == -1) {
					loadNode = new TreeNode();
					loadNode.setId(tree.getId());
					loadNode.setName(tree.getName());
					loadNode.setParentId(tree.getParentId());
					loadNode.setLevel(tree.getLevel());
					if (tree.getLevel() == Region.MAX_LEVEL) {
						loadNode.setLeaf(false);
					} else {
						loadNode.setLeaf(true);
					}
					loadNode.setOpen(false);
					loadNode.setIdPath(tree.getIdPath());
					resultList.add(loadNode);
					fillTreeNode(origList, loadNode);
				}
			} else {
				loadNode = new TreeNode();
				loadNode.setId(tree.getId());
				loadNode.setName(tree.getName());
				loadNode.setParentId(tree.getParentId());
				loadNode.setLevel(tree.getLevel());
				if (tree.getLevel() == Region.MAX_LEVEL) {
					loadNode.setLeaf(false);
				} else {
					loadNode.setLeaf(true);
				}
				loadNode.setOpen(false);
				loadNode.setIdPath(tree.getIdPath());
				resultList.add(loadNode);
				fillTreeNode(origList, loadNode);
			}

		}

		return resultList;
	}

	// 填充子节点
	private void fillTreeNode(List<TreeNode> treeList, TreeNode tree) {
		for (TreeNode node : treeList) {
			if ((node.getParentId() != 0) && node.getParentId().toString().trim().equals(tree.getId().toString().trim())) {
				TreeNode t = new TreeNode();
				t.setId(node.getId());
				t.setName(node.getName());
				t.setLevel(node.getLevel());
				t.setParentId(node.getParentId());
				t.setIdPath(node.getIdPath());
				List<TreeNode> list = tree.getChildren();
				if (list == null) {
					list = new ArrayList<TreeNode>();
				}
				list.add(t);
				tree.setChildren(list);
				fillTreeNode(treeList, t);
			}
		}
	}

	private boolean saveRegion(Region region, boolean sendMsg) {
		// 设置id和code
		Integer id = region.getId();
		if (id == null) {
			id = regionDao.getMaxId() + 1;
			region.setId(id);
		}
		if (region.getCode() == null) {
			region.setCode(id.toString());
		}
		// 设置parentId和parentCode
		Integer parentId = region.getParentId();
		if (parentId == null) {
			parentId = -1;
			region.setParentId(parentId);
		}
		if (region.getParentCode() == null) {
			region.setParentCode("");
		}
		// 设置seqNo
		if (region.getSeqNo() == null) {
			int seqNo = regionDao.getEntityMaxSeqNo(Region.class, "parentId", region.getParentId()) + 1;
			region.setSeqNo(seqNo);
		}
		//
		int count = regionDao.insert(region);
		boolean success = count > 0;
		if (success && sendMsg) {
			this.sendRegionListChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean saveRegion(Region region) {
		return this.saveRegion(region, true);
	}

	@Override
	public boolean updateRegion(Region region) {
		int count = regionDao.update(region);
		boolean success = count > 0;
		if (success) {
			this.sendRegionListChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean deleteRegion(Integer id) {
		int count = regionDao.deleteById(id);
		boolean success = count > 0;
		if (success) {
			this.sendRegionListChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean deleteRegionsByIds(List<Integer> ids) {
		int count = regionDao.deleteByIds(ids);
		boolean success = count > 0;
		if (success) {
			this.sendRegionListChangeQueueMessage();
		}
		return success;
	}

	@Override
	public void importRegionsFromXls(InputStream xlsInputStream) throws Exception {
		// 列配置信息
		List<TypedField> columns = new ArrayList<TypedField>();
		TypedField col = new TypedField("地区编码", "str");
		columns.add(col);
		col = new TypedField("上级编码", "str");
		columns.add(col);
		col = new TypedField("地区名称", "str");
		columns.add(col);
		col = new TypedField("地区级别", "num", null, "int");
		columns.add(col);
		col = new TypedField("百度城市代码", "num", null, "int");
		columns.add(col);
		//
		Importor importor = new XlsImportor(xlsInputStream);
		importor.setColumns(columns);
		importor.prepare();
		int totalRows = importor.getTotalDataRows();
		int insertCount = 0;
		for (int i = 0; i < totalRows; i++) {
			Map<String, Object> dataRow = importor.readDataRowAsMap(i);
			String code = dataRow.get("地区编码").toString().trim();
			Integer id = Integer.parseInt(code);
			Region region = this.getRegionById(id);
			if (region != null) {
				continue;
			}
			String name = dataRow.get("地区名称").toString().trim();
			Integer level = Integer.valueOf(dataRow.get("地区级别").toString().trim());
			String parentCode = level == 1 ? "" : dataRow.get("上级编码").toString().trim();
			Integer parentId = level == 1 ? -1 : Integer.parseInt(parentCode);
			Region parentRegion = level == 1 ? null : this.getRegionById(parentId);
			Integer bdCityCode = null;
			if (level == 2) {
				String bdCityCodeStr = dataRow.get("百度城市代码").toString().trim();
				bdCityCode = StrUtil.hasText(bdCityCodeStr) ? Integer.parseInt(bdCityCodeStr) : null;
			} else if (level == 3) {
				bdCityCode = parentRegion.getBdCityCode();
			}
			String idPath = parentRegion == null ? id.toString() : parentRegion.getIdPath() + "," + id.toString();
			//
			int seqNo = regionDao.getEntityMaxSeqNo(Region.class, "parentId", parentId) + 1;
			//
			region = new Region();
			region.setId(id);
			region.setCode(code);
			region.setName(name);
			region.setPy(StrUtil.chsToPy(name));
			region.setLevel(level);
			region.setParentId(parentId);
			region.setParentCode(parentCode);
			region.setIdPath(idPath);
			region.setBdCityCode(bdCityCode);
			region.setSeqNo(seqNo);
			this.saveRegion(region, false);

			insertCount++;
		}
		if (insertCount > 0) {
			this.sendRegionListChangeQueueMessage();
		}
		importor.close();
	}

	// -------------------------------系统业务参数-----------------------------------
	@Override
	public SysParam getSysParamByCode(String code) {
		return sysParamsDao.selectByCode(code);
	}

	@Override
	public List<SysParam> getAllSysParams() {
		return sysParamsDao.selectAll();
	}

	private boolean saveSysParamInternal(SysParam sysParam, boolean batchMode) {
		int count = sysParamsDao.insert(sysParam);

		boolean success = count > 0;
		if (success && !batchMode) {
			// 只在非批量模式下发送消息
			this.sendSysParamChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean saveSysParam(SysParam sysParam) {
		return this.saveSysParamInternal(sysParam, false);
	}

	@Override
	public boolean saveSysParams(List<SysParam> sysParamList) {
		boolean anySuccess = false;
		boolean allSuccess = true;
		for (SysParam sysParam : sysParamList) {
			boolean tmpSuccess = this.saveSysParamInternal(sysParam, true);
			anySuccess = tmpSuccess || anySuccess;
			allSuccess = tmpSuccess && allSuccess;
		}

		if (anySuccess) {
			this.sendSysParamChangeTopicMessage();
		}
		return allSuccess;
	}

	private boolean updateSysParamInternal(SysParam sysParam, boolean batchMode) {
		int count = sysParamsDao.update(sysParam);

		boolean success = count > 0;
		if (success && !batchMode) {
			// 只在非批量模式下发送消息
			this.sendSysParamChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean updateSysParam(SysParam sysParam) {
		return this.updateSysParamInternal(sysParam, false);
	}

	@Override
	public boolean updateSysParams(List<SysParam> sysParamList) {
		boolean anySuccess = false;
		boolean allSuccess = true;
		for (SysParam sysParam : sysParamList) {
			boolean tmpSuccess = this.updateSysParamInternal(sysParam, true);
			anySuccess = tmpSuccess || anySuccess;
			allSuccess = tmpSuccess && allSuccess;
		}

		if (anySuccess) {
			this.sendSysParamChangeTopicMessage();
		}
		return allSuccess;
	}

	// -------------------------------商城业务参数-----------------------------------
	@Override
	public BizParam getBizParamByCode(String code) {
		return bizParamsDao.selectById(code);
	}

	@Override
	public BizParam getBizParamByName(String name) {
		return bizParamsDao.selectByName(name);
	}

	@Override
	public List<BizParam> getBizParams() {
		return bizParamsDao.selectAll();
	}

	private boolean saveBizParamInternal(BizParam bizParam, boolean batchMode) {
		int count = bizParamsDao.insert(bizParam);

		boolean success = count > 0;
		if (success && !batchMode) {
			// 只在非批量模式下发送消息
			this.sendBizParamChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean saveBizParam(BizParam bizParam) {
		return this.saveBizParamInternal(bizParam, false);
	}

	@Override
	public boolean saveBizParams(List<BizParam> bizParamList) {
		boolean anySuccess = false;
		boolean allSuccess = true;
		for (BizParam bizParam : bizParamList) {
			boolean tmpSuccess = this.saveBizParamInternal(bizParam, true);
			anySuccess = tmpSuccess || anySuccess;
			allSuccess = tmpSuccess && allSuccess;
		}

		if (anySuccess) {
			this.sendBizParamChangeTopicMessage();
		}
		return allSuccess;
	}

	private boolean updateBizParamInternal(BizParam bizParam, boolean batchMode) {
		int count = bizParamsDao.update(bizParam);

		boolean success = count > 0;
		if (success && !batchMode) {
			// 只在非批量模式下发送消息
			this.sendBizParamChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean updateBizParam(BizParam bizParam) {
		return this.updateBizParamInternal(bizParam, false);
	}

	@Override
	public boolean updateBizParams(List<BizParam> bizParamList) {
		boolean anySuccess = false;
		boolean allSuccess = true;
		for (BizParam bizParam : bizParamList) {
			boolean tmpSuccess = this.updateBizParamInternal(bizParam, true);
			anySuccess = tmpSuccess || anySuccess;
			allSuccess = tmpSuccess && allSuccess;
		}

		if (anySuccess) {
			this.sendBizParamChangeTopicMessage();
		}
		return allSuccess;
	}

	@Override
	public PaginatedList<MailServer> getMailServers(PaginatedFilter paginatedFilter) {
		return mailServerDao.selectList(paginatedFilter);
	}

	@Override
	public boolean deleteMailServerById(Integer id) {
		return mailServerDao.deleteById(id) > 0;
	}

	@Override
	public boolean existMailServerByName(String name) {
		return mailServerDao.selectByName(name) == null;
	}

	@Override
	public boolean saveMailServer(MailServer mailServer) {
		return mailServerDao.insert(mailServer) > 0;
	}

	@Override
	public void updateMailServersAllAsUnabled() {
		mailServerDao.updateAllUnabled();
	}

	@Override
	public MailServer getMailServerById(Integer id) {
		return mailServerDao.selectById(id);
	}

	@Override
	public boolean updateMailServer(MailServer mailServer) {
		return mailServerDao.update(mailServer) > 0;
	}

	// -------------------------------物流设置-----------------------------------
	@Override
	public PaginatedList<LogisCom> getLogisComsByFilter(PaginatedFilter paginatedFilter) {
		return logisticComDao.selectLogisComsByFilter(paginatedFilter);
	}

	@Override
	public boolean saveLogisCom(LogisCom logisCom) {
		int count = logisticComDao.insert(logisCom);
		return count > 0;
	}

	@Override
	public boolean deleteLogisComById(Integer id) {
		int count = logisticComDao.deleteById(id);
		return count > 0;
	}

	@Override
	public boolean upadteLogisCom(LogisCom logisCom) {
		int count = logisticComDao.update(logisCom);
		return count > 0;
	}

	@Override
	public PaginatedList<LogisApi> getLogisApisByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<LogisApi> apis = logisApiDao.selectLogisApisByFilter(paginatedFilter);
		for (LogisApi api : apis.getRows()) {
			List<LogisApiParam> ps = logisApiParamDao.selectByApiId(api.getId());
			api.setPs(ps);
		}
		return apis;
	}

	@Override
	public boolean saveLogisApi(LogisApiDto logisticApiDto) {
		LogisApi logisticApi = new LogisApi();
		TypeUtil.copyProperties(logisticApiDto, logisticApi);
		// 保存LogisApi信息
		int count = logisApiDao.insert(logisticApi);
		logisticApiDto.setId(logisticApi.getId());

		int bcount = 0;
		if (count > 0) {
			List<LogisApiParam> params = logisticApiDto.getParams();
			bcount = logisApiParamDao.batchInsertLogisApiParams(params);
		}
		return bcount > 0;
	}

	@Override
	public boolean deleteLogisApiById(Integer id) {
		//
		logisApiParamDao.deleteByApiId(id);
		//
		int count = logisApiDao.deleteById(id);
		//
		return count > 0;
	}

	@Override
	public boolean upadteLogisApi(LogisApiDto logisticApiDto) {
		//
		List<LogisApiParam> ps = logisticApiDto.getParams();
		for (LogisApiParam p : ps) {
			logisApiParamDao.update(p);
		}
		//
		LogisApi api = new LogisApi();
		TypeUtil.copyProperties(logisticApiDto, api);
		int count = logisApiDao.update(api);
		//
		return count > 0;
	}

	@Override
	public LogisApi getLogisApiById(Integer logisApiId) {
		return logisApiDao.selectById(logisApiId);
	}

	@Override
	public List<LogisApiParam> getLogisApiParamsByApiId(Integer logisApiId) {
		return logisApiParamDao.selectByApiId(logisApiId);
	}

	@Override
	public boolean saveSmsApi(SmsApiDto smsApiDto) {
		SmsApi smsApi = new SmsApi();
		TypeUtil.copyProperties(smsApiDto, smsApi);
		// 保存Api信息
		int count = smsApiDao.insert(smsApi);
		smsApiDto.setId(smsApi.getId());

		int bcount = 0;
		if (count > 0) {
			List<SmsApiParam> params = smsApiDto.getParams();
			bcount = smsApiParamDao.batchInsert(params);
		}
		return bcount > 0;
	}

	@Override
	public PaginatedList<SmsApi> getSmsApisByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SmsApi> apis = smsApiDao.selectSmsApisByFilter(paginatedFilter);
		for (SmsApi api : apis.getRows()) {
			api.setPs(smsApiParamDao.selectByApiId(api.getId()));
		}
		return apis;
	}

	@Override
	public List<SmsApi> getSmsApis() {
		return smsApiDao.selectSmsApis();
	}

	@Override
	public SmsApi getSmsApiById(Integer id) {
		return smsApiDao.selectById(id);
	}

	@Override
	public List<SmsApiParam> getSmsApiParamsByApiId(Integer apiId) {
		return smsApiParamDao.selectByApiId(apiId);
	}

	@Override
	public boolean deleteSmsApiById(Integer id) {
		int pcount = smsApiParamDao.deleteByApiId(id);
		int acount = 0;
		if (pcount > 0) {
			acount = smsApiDao.deleteById(id);
		}
		return acount > 0;
	}

	@Override
	public boolean updateSmsApi(SmsApiDto smsApiDto) {
		SmsApi smsApi = new SmsApi();
		TypeUtil.copyProperties(smsApiDto, smsApi);
		int acount = smsApiDao.update(smsApi);
		//
		List<Integer> paramIds = smsApiParamDao.selectParamIdsByApiId(smsApi.getId());

		List<SmsApiParam> params = smsApiDto.getParams();

		for (SmsApiParam p : params) {
			if (p.getId() == null) {
				smsApiParamDao.insert(p);
			} else {
				paramIds.remove(p.getId());
				smsApiParamDao.update(p);
			}
		}

		if (paramIds.size() > 0) {
			smsApiParamDao.batchDelete(paramIds);
		}

		return acount > 0;
	}

	@Override
	public List<LogisCom> getLogisComListAll() {
		return logisticComDao.selectListAll();
	}

	// -------------------------------------模板设置-------------------------------------------------------

	@Override
	public PaginatedList<MailTemplate> getMailTemplates(PaginatedFilter paginatedFilter) {
		return mailTemplateDao.selectList(paginatedFilter);
	}

	@Override
	public PaginatedList<SmsTemplate> getSmsTemplates(PaginatedFilter paginatedFilter) {
		return smsTemplateDao.selectList(paginatedFilter);
	}

	@Override
	public boolean saveMailTemplate(MailTemplate mailTemplate) {
		return mailTemplateDao.insert(mailTemplate) > 0;
	}

	@Override
	public boolean updateMailTemplate(MailTemplate mailTemplate) {
		return mailTemplateDao.update(mailTemplate) > 0;
	}

	@Override
	public boolean deleteMailTemplateById(Integer id) {
		return mailTemplateDao.deleteById(id) > 0;
	}

	@Override
	public boolean saveSmsTemplate(SmsTemplate smsTemplate) {
		return smsTemplateDao.insert(smsTemplate) > 0;
	}

	@Override
	public boolean updateSmsTemplate(SmsTemplate smsTemplate) {
		return smsTemplateDao.update(smsTemplate) > 0;
	}

	@Override
	public boolean deleteSmsTemplateById(Integer id) {
		return smsTemplateDao.deleteById(id) > 0;
	}

	@Override
	public MailTemplate getMailTemplateByName(String name) {
		return mailTemplateDao.selectByName(name);
	}

	@Override
	public SmsTemplate getSmsTemplateByName(String name) {
		return smsTemplateDao.selectByName(name);
	}

	@Override
	public MailTemplate getMailTemplateByCode(String code) {
		return mailTemplateDao.selectByCode(code);
	}

	@Override
	public SmsTemplate getSmsTemplateByCode(String code) {
		return smsTemplateDao.selectByCode(code);
	}

	@Override
	public List<TplModel> getTplModelsByCode(String code) {
		return tplModelDao.selectAll(code);
	}

	@Override
	public TplModel getTplModelByCode(String code) {
		return tplModelDao.selectByCode(code);
	}

	// -------------------------------------配送方式设置--------------------------------------------

	@Override
	public boolean saveDeliveryWay(DeliveryWay deliveryWay) {
		return deliveryWayDao.insert(deliveryWay) > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteDeliveryWay(Integer id) {
		List<ShopParam> shopParams = shopParamDao.selectByCode("delivery.way");
		for (ShopParam shopParam : shopParams) {
			String json = shopParam.getValue();
			Map<String, Object> map = JsonUtil.fromJson(json, TypeUtil.TypeRefs.StringObjectMapType);
			List<Integer> ids = (List<Integer>) map.get("ids");
			if (ids.size() == 1 && ids.get(0) == id) {
				shopParamDao.deleteById(shopParam.getId());
			} else {
				List<Integer> newIds = new ArrayList<Integer>();
				for (Integer index : ids) {
					if (index != id) {
						newIds.add(index);
					}
				}
				MapContext valueMap = MapContext.newOne();
				valueMap.put("ids", newIds);
				shopParam.setValue(JsonUtil.toJson(valueMap));
				shopParamDao.update(shopParam);
			}
		}
		return deliveryWayDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateDeliveryWay(DeliveryWay deliveryWay) {
		return deliveryWayDao.update(deliveryWay) > 0;
	}

	@Override
	public PaginatedList<DeliveryWay> getDeliveryWays(PaginatedFilter paginatedFilter) {
		return deliveryWayDao.selectList(paginatedFilter);
	}

	@Override
	public List<LogisCom> getLogisComsByGroup(Boolean lcDisabled, Boolean dwDisabled) {
		return logisticComDao.selectListByGroup(lcDisabled, dwDisabled);
	}

	@Override
	public DeliveryWay getDeliveryWayByName(String name, Integer comId) {
		return deliveryWayDao.selectByNameAndComId(name, comId);
	}

	// -------------------------------------------------短信验证码--------------------------------------------------------------

	@Override
	public boolean saveSmsVerfCode(SmsVerfCode smsVerfCode) {
		//smsVerfCodeDao.updateInvalidByPhoneNoAndUsage(smsVerfCode.getPhoneNo(), smsVerfCode.getUsage());
		return smsVerfCodeDao.insert(smsVerfCode) > 0;
	}
	
	@Override
	public boolean updateSmsVerfCode(SmsVerfCode smsVerfCode) {
		return smsVerfCodeDao.updateInvalidByPhoneNoAndUsage(smsVerfCode.getPhoneNo(), smsVerfCode.getUsage()) > 0;
	}

	@Override
	public boolean validSmsVerfCode(SmsVerfCode smsVerfCode) {
		try {
			String phoneNo = smsVerfCode.getPhoneNo();
			String vfCode = smsVerfCode.getVfCode();
			SmsUsage usage = smsVerfCode.getUsage();
			SmsVerfCode smsCode = smsVerfCodeDao.selectByPhoneNoAndVfCodeAndUsage(phoneNo, vfCode, usage);
			Date now = new Date();
			if (smsCode != null) {
				if (now.before(smsCode.getExpireTime())) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<SmsVerfCode> getSmsVerfCodes(String reqIp, String sendTime, SmsUsage[] limitUsages) {
		try {
			StringBuilder su = new StringBuilder();
			for (SmsUsage smsUsage : limitUsages) {
				su.append(" ");
				su.append(smsUsage);
			}
			String limitUsage = su.toString();
			return smsVerfCodeDao.selectByReqIpAndSendTime(reqIp, sendTime, limitUsage);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// -------------------------------------------------邮箱验证码--------------------------------------------------------------

	@Override
	public boolean saveMailVerfCode(MailVerfCode mailVerfCode) {
		return mailVerfCodeDao.insert(mailVerfCode) > 0;
	}

	@Override
	public boolean validMailVerfCode(MailVerfCode mailVerfCode) {
		try {
			String email = mailVerfCode.getEmail();
			String vfCode = mailVerfCode.getVfCode();
			return mailVerfCodeDao.selectByEmailAndVfCode(email, vfCode) != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// -------------------------------销售楼层-----------------------------------

	@Override
	public PaginatedList<SalesFloor> getSalesFloors(PaginatedFilter paginatedFilter) {
		PaginatedList<SalesFloor> salesFloors = salesFloorDao.selectSalesFloors(paginatedFilter);
		List<SalesFloor> floors = salesFloors.getRows();
		for (SalesFloor salesFloor : floors) {
			List<SalesRegion> salesRegions = salesRegionDao.selectByFloorNo(salesFloor.getNo());
			for (SalesRegion salesRegion : salesRegions) {
				List<SalesRegionGoods> salesRegionGoods = salesRegionGoodsDao.selectByRegionId(salesRegion.getId());
				for (SalesRegionGoods goods : salesRegionGoods) {
					Product product = productDao.selectById(goods.getProductId());
					List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
					product.setProductAlbumImgs(productAlbumImgs);
					goods.setProduct(product);
				}
				salesRegion.setSalesRegionGoods(salesRegionGoods);
			}
			salesFloor.setSalesRegions(salesRegions);
			List<SalesBrandShoppe> shoppes = salesBrandShoppeDao.selectByFloorNo(salesFloor.getNo());
			for (SalesBrandShoppe shoppe : shoppes) {
				BrandDef brandDef = brandDefDao.selectByCode(shoppe.getBrandCode());
				shoppe.setBrandDef(brandDef);
			}
			salesFloor.setSalesBrandShoppes(shoppes);
		}
		return salesFloors;
	}

	@Override
	public boolean saveSalesFloor(SalesFloor salesFloor) {
		boolean result = salesFloorDao.insert(salesFloor) > 0;
		Integer floorNo = salesFloor.getNo();
		List<SalesRegion> salesRegions = salesFloor.getSalesRegions();
		for (SalesRegion salesRegion : salesRegions) {
			salesRegion.setFloorNo(floorNo);
			salesRegionDao.insert(salesRegion);
			List<SalesRegionGoods> salesRegionGoods = salesRegion.getSalesRegionGoods();
			for (SalesRegionGoods regionGoods : salesRegionGoods) {
				regionGoods.setRegionId(salesRegion.getId());
				salesRegionGoodsDao.insert(regionGoods);
				Product product = productDao.selectById(regionGoods.getProductId());
				List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
				product.setProductAlbumImgs(productAlbumImgs);
				regionGoods.setProduct(product);
			}
		}
		List<SalesBrandShoppe> salesBrandShoppes = salesFloor.getSalesBrandShoppes();
		for (SalesBrandShoppe salesBrandShoppe : salesBrandShoppes) {
			salesBrandShoppe.setFloorNo(floorNo);
			salesBrandShoppeDao.insert(salesBrandShoppe);
			BrandDef brandDef = brandDefDao.selectByCode(salesBrandShoppe.getBrandCode());
			salesBrandShoppe.setBrandDef(brandDef);
		}
		return result;
	}

	@Override
	public boolean updateSalesFloor(SalesFloor salesFloor) {
		boolean result = salesFloorDao.update(salesFloor) > 0;
		if (result) {
			Integer floorNo = salesFloor.getNo();
			List<SalesRegion> salesRegions = salesFloor.getSalesRegions();
			List<Integer> uncontainRegions = new ArrayList<Integer>();
			List<Integer> uncontainShoppes = new ArrayList<Integer>();
			for (SalesRegion salesRegion : salesRegions) {
				salesRegion.setFloorNo(floorNo);
				if (salesRegion.getId() != null) {
					salesRegionDao.update(salesRegion);
				} else {
					salesRegionDao.insert(salesRegion);
				}
				uncontainRegions.add(salesRegion.getId());
				List<Integer> uncontainGoods = new ArrayList<Integer>();
				List<SalesRegionGoods> salesRegionGoods = salesRegion.getSalesRegionGoods();
				if (salesRegionGoods != null) {
					for (SalesRegionGoods regionGoods : salesRegionGoods) {
						regionGoods.setRegionId(salesRegion.getId());
						if (regionGoods.getId() != null) {
							salesRegionGoodsDao.update(regionGoods);
						} else {
							salesRegionGoodsDao.insert(regionGoods);
						}
						uncontainGoods.add(regionGoods.getId());
						Product product = productDao.selectById(regionGoods.getProductId());
						List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
						product.setProductAlbumImgs(productAlbumImgs);
						regionGoods.setProduct(product);
					}
				}
				salesRegionGoodsDao.deleteByRegionIdAndUncontainIds(salesRegion.getId(), uncontainGoods);
			}
			salesRegionDao.deleteByFloorNoAndUncontainIds(floorNo, uncontainRegions);
			List<SalesBrandShoppe> salesBrandShoppes = salesFloor.getSalesBrandShoppes();
			if (salesBrandShoppes != null) {
				for (SalesBrandShoppe salesBrandShoppe : salesBrandShoppes) {
					salesBrandShoppe.setFloorNo(floorNo);
					if (salesBrandShoppe.getId() != null) {
						salesBrandShoppeDao.update(salesBrandShoppe);
					} else {
						salesBrandShoppeDao.insert(salesBrandShoppe);
					}
					BrandDef brandDef = brandDefDao.selectByCode(salesBrandShoppe.getBrandCode());
					salesBrandShoppe.setBrandDef(brandDef);
					uncontainShoppes.add(salesBrandShoppe.getId());
				}
			}
			salesBrandShoppeDao.deleteByFloorNoAndUncontainIds(floorNo, uncontainShoppes);
		}
		return result;
	}

	@Override
	public boolean deleteSalesFloorByNo(Integer no) {
		List<SalesRegion> salesRegions = salesRegionDao.selectByFloorNo(no);
		for (SalesRegion salesRegion : salesRegions) {
			salesRegionGoodsDao.deleteByRegionId(salesRegion.getId());
		}
		salesRegionDao.deleteByFloorNo(no);
		salesBrandShoppeDao.deleteByFloorNo(no);
		boolean result = salesFloorDao.deleteById(no) > 0;
		return result;
	}

	@Override
	public boolean deleteSalesFloorByNos(List<Integer> nos) {
		boolean result = true;
		for (Integer no : nos) {
			result = this.deleteSalesFloorByNo(no) && result;
		}
		return result;
	}

	// -------begin-------------WJJ--------------------------------------------------------------
	@Override
	public PaginatedList<PayWay> getPayWayByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<PayWay> apis = payWayDao.selectPayWaysByFilter(paginatedFilter);
		for (PayWay api : apis.getRows()) {
			List<PayWayParam> payWayParamList = payWayParamDao.selectByPWayId(api.getId());
			api.setPayWayParamList(payWayParamList);

			UserAccount userAccount = userAccountDao.selectById(api.getAccountId());
			api.setUserAccount(userAccount);
		}
		return apis;
	}

	@Override
	public boolean savePayWay(PayWay payWay) {
		// 保存PayWay信息
		int count = payWayDao.insert(payWay);
		int payWayId = payWay.getId();
		if (count > 0) {
			List<PayWayParam> params = payWay.getPayWayParamList();
			// 插入支付方式参数
			for (PayWayParam payWayParam : params) {
				payWayParam.setPayWayId(payWayId);
				payWayParamDao.insert(payWayParam);
			}
		}
		return count > 0;
	}

	@Override
	public boolean deletePayWayById(Integer id) {
		//
		payWayParamDao.deleteByPWayId(id);
		//
		int count = payWayDao.deleteById(id);
		//
		return count > 0;
	}

	@Override
	public boolean updatePayWay(PayWay payWay) {
		// 修改支付方式
		int count = payWayDao.update(payWay);
		// 修改支付参数
		List<PayWayParam> ps = payWay.getPayWayParamList();
		for (PayWayParam p : ps) {
			payWayParamDao.update(p);
		}
		//
		return count > 0;
	}

	@Override
	public List<PayWay> getUsablePayWay() {
		return payWayDao.selectByStatus();
	}
	// -------end-------------WJJ--------------------------------------------------------------

}
