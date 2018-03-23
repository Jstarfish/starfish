package priv.starfish.mall.web.jms;

import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.mall.comn.misc.BizParamInfo;
import priv.starfish.mall.comn.misc.SysParamInfo;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.web.base.AppBase;

import java.io.Serializable;

/**
 * 服务配置信息变更消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:21:37
 *
 */
public class ConfigMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Topic;
	private final String targetTopic = BaseConst.TopicNames.CONFIG;

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		String category = message.category;
		if (targetType == messageType && targetTopic.equals(category)) {
			this.doHandleMessage(message);
		} else {
			logger.warn("未知类型消息（已忽略） : " + message);
		}
	}

	private void doHandleMessage(SimpleMessage message) {
		String subject = message.subject;
		if (BaseConst.SubjectNames.MALL_INFO.equals(subject)) {
			AppBase.clearMallInfo();
		} else if (BaseConst.SubjectNames.MAIL.equals(subject)) {
			IRefreshable mailService = WebEnvHelper.getSpringBean("mailService", IRefreshable.class);
			mailService.refresh();
		} else if (BaseConst.SubjectNames.SMS.equals(subject)) {
			IRefreshable smsService = WebEnvHelper.getSpringBean("smsService", IRefreshable.class);
			smsService.refresh();
		} else if (BaseConst.SubjectNames.SETTLE_WAY.equals(subject)) {
			AppBase.clearSettleWays();
		} else if (BaseConst.SubjectNames.FREEMARKER.equals(subject)) {
			IRefreshable freeMarkerService = WebEnvHelper.getSpringBean("freeMarkerService", IRefreshable.class);
			freeMarkerService.refresh();
		} else if (BaseConst.SubjectNames.SYS_PARAM.equals(subject)) {
			SettingService settingService = WebEnvHelper.getSpringBean(SettingService.class);
			SysParamInfo.fromParams(settingService.getAllSysParams());
		} else if (BaseConst.SubjectNames.BIZ_PARAM.equals(subject)) {
			SettingService settingService = WebEnvHelper.getSpringBean(SettingService.class);
			BizParamInfo.fromParams(settingService.getBizParams());
		}
		// ...
	}
}
