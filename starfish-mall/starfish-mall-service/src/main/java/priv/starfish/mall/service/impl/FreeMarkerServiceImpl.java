package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.cms.StringFreeMarkerServiceImpl;
import priv.starfish.mall.dao.notify.MailTemplateDao;
import priv.starfish.mall.dao.notify.SmsTemplateDao;
import priv.starfish.mall.notify.entity.MailTemplate;
import priv.starfish.mall.notify.entity.SmsTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//接口：priv.starfish.common.cms.FreeMarkerService
@Service("freeMarkerService")
public class FreeMarkerServiceImpl extends StringFreeMarkerServiceImpl implements IRefreshable {

	@Resource
	SmsTemplateDao smsTemplateDao;
	@Resource
	MailTemplateDao mailTemplateDao;

	List<SmsTemplate> smsTempList = new ArrayList<SmsTemplate>();
	List<MailTemplate> mailTempList = new ArrayList<MailTemplate>();
	int smsCount = smsTempList.size();
	int mailCount = mailTempList.size();

	@Override
	public void refresh() {
		smsTempList = smsTemplateDao.selectAll();

		mailTempList = mailTemplateDao.selectAll();

		for (SmsTemplate smsTemplate : smsTempList) {
			this.setTemplateContent("sms", smsTemplate.getCode(), smsTemplate.getContent());
		}

		for (MailTemplate mailTemplate : mailTempList) {
			this.setTemplateContent("mail", mailTemplate.getCode(), mailTemplate.getContent());
		}
	}

}
