package priv.starfish.mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.base.Converter;
import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.exception.BusinessException;
import priv.starfish.common.map.HttpIntResultMappingExecutor;
import priv.starfish.common.map.ParamMapping;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.sms.SmsMessage;
import priv.starfish.common.sms.SmsService;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.dao.notify.SmsApiDao;
import priv.starfish.mall.dao.notify.SmsApiParamDao;
import priv.starfish.mall.dao.notify.SmsBizRecordDao;
import priv.starfish.mall.notify.entity.SmsApi;
import priv.starfish.mall.notify.entity.SmsApiParam;
import priv.starfish.mall.notify.entity.SmsBizRecord;

//接口：priv.starfish.common.sms.SmsService
@Service("smsService")
public class SmsServiceImpl extends BaseServiceImpl implements SmsService, IRefreshable {

	@Resource
	SmsApiDao smsApiDao;

	@Resource
	SmsApiParamDao smsApiParamDao;

	@Resource
	SmsBizRecordDao smsBizRecordDao;

	// 把DB数据模型转化为通用模型
	Converter<SmsApiParam, ParamMapping> apiParamConverter = new Converter<SmsApiParam, ParamMapping>() {

		@Override
		public ParamMapping convert(SmsApiParam src, int index) {
			ParamMapping paramMapping = ParamMapping.newOne();
			TypeUtil.copyProperties(src, paramMapping);
			return paramMapping;
		}
	};

	class ApiInfo {
		public List<ParamMapping> ioParamMappings;
		public Map<String, Object> extraMap;
	}

	HttpIntResultMappingExecutor mappingExecutor = HttpIntResultMappingExecutor.getInstance();

	Random initRandom = NumUtil.getIntRandom();

	//
	List<ApiInfo> apiInfoList = new ArrayList<ApiInfo>();
	int apiInfoCount = apiInfoList.size();

	@Override
	public void refresh() {
		// 从数据库获取配置信息，缓存以备后用
		//
		List<SmsApi> apis = smsApiDao.selectSmsApis();
		for (SmsApi api : apis) {
			List<SmsApiParam> ps = smsApiParamDao.selectByApiId(api.getId());
			api.setPs(ps);
		}
		//
		apiInfoList.clear();

		//
		for (SmsApi api : apis) {
			List<SmsApiParam> srcList = api.getPs();
			List<ParamMapping> ioParamMappings = CollectionUtil.convertToList(srcList, apiParamConverter);
			//
			Map<String, Object> extraMap = new HashMap<String, Object>();
			extraMap.put("apiId", api.getId());
			extraMap.put("url", api.getUrl());
			extraMap.put("method", api.getMethod());
			//
			ApiInfo apiInfo = new ApiInfo();
			apiInfo.ioParamMappings = ioParamMappings;
			apiInfo.extraMap = extraMap;
			//
			apiInfoList.add(apiInfo);
		}
		apiInfoCount = apiInfoList.size();
	}

	@Override
	public SmsErrorCode sendSms(SmsMessage message, MapContext extra) {
		if (apiInfoCount == 0)
			throw new BusinessException("没有配置短信服务商!");
		//
		int randomNum = apiInfoCount < 2 ? 0 : NumUtil.getRandomInt(initRandom, 0, apiInfoCount - 1);
		ApiInfo apiInfo = apiInfoList.get(randomNum);

		Map<String, Object> inConext = new HashMap<String, Object>();
		//
		inConext.put("phoneNo", message.getReceiverNumber());
		inConext.put("content", message.getText());

		Integer resultCode = mappingExecutor.execute(apiInfo.ioParamMappings, inConext, apiInfo.extraMap);

		// 保存短信业务记录
		Object apiId = apiInfo.extraMap.get("apiId");
		logger.debug("sms api id =====>" + apiId.toString());
		if (apiId != null) {
			SmsBizRecord smsBizRecord = new SmsBizRecord();
			try {
				fillRecordFromExtra(smsBizRecord, extra);
				//
				smsBizRecord.setPhoneNo(message.getReceiverNumber());
				smsBizRecord.setContent(message.getText());
				smsBizRecord.setSendApiId(Integer.parseInt(apiId.toString()));
				smsBizRecord.setSendOk(resultCode == SmsErrorCode.OK.getValue());
				//
				smsBizRecordDao.insert(smsBizRecord);
			} catch (NumberFormatException e) {
				this.logger.error("短信业务记录保存失败...", e);
			}
		}
		return SmsErrorCode.fromValue(resultCode);
	}

	@Override
	public int getSmsRemain() {
		// 未知
		return -1;
	}

	// 取map中的值
	public void fillRecordFromExtra(SmsBizRecord smsBizRecord, MapContext extra) {
		if (extra == null) {
			smsBizRecord.setBizCode("unknown");
		}
		if (extra != null) {
			Object scope = extra.get("scope");
			Integer entityId = extra.getTypedValue("entityId", Integer.class);
			String bizCode = extra.getTypedValue("bizCode", String.class);
			Integer senderId = extra.getTypedValue("senderId", Integer.class);
			Date sendTime = extra.getTypedValue("sendTime", Date.class);
			//
			if (scope != null) {
				smsBizRecord.setScope(EnumUtil.valueOf(AuthScope.class, scope.toString()));
			}
			if (entityId != null) {
				smsBizRecord.setEntityId(entityId);
			}
			if (bizCode == null) {
				bizCode = "N/A";
			}
			smsBizRecord.setBizCode(bizCode.toString());
			if (senderId != null) {
				smsBizRecord.setSenderId(senderId);
			}
			if (sendTime != null) {
				smsBizRecord.setSendTime(sendTime);
			}
		}
	}
}
