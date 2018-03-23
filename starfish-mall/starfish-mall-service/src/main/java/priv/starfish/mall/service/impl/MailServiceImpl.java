package priv.starfish.mall.service.impl;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.mail.JavaMailServiceImpl;
import priv.starfish.mall.dao.notify.MailServerDao;
import priv.starfish.mall.notify.entity.MailServer;

//接口：priv.starfish.common.mail.MailService
@Service("mailService")
public class MailServiceImpl extends JavaMailServiceImpl implements IRefreshable {

	@Resource
	MailServerDao mailServerDao;

	@Override
	public void refresh() {
		MailServer mailServer = mailServerDao.selectEnabled();
		if (mailServer != null) {
			JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
			javaMailSender.setProtocol(mailServer.getType().name());
			javaMailSender.setHost(mailServer.getSendServer());
			if (mailServer.getSendPort() != null) {
				javaMailSender.setPort(mailServer.getSendPort());
			} else {
				javaMailSender.setPort(-1);
			}
			javaMailSender.setUsername(mailServer.getAccount());
			javaMailSender.setPassword(mailServer.getPassword());
			javaMailSender.setDefaultEncoding("UTF-8");

			Properties javaMailProps = new Properties();
			javaMailProps.put("mail.smtp.auth", "true");
			javaMailProps.put("mail.debug", "true");

			javaMailSender.setJavaMailProperties(javaMailProps);

			this.setJavaMailSender(javaMailSender);
		}
	}
}
