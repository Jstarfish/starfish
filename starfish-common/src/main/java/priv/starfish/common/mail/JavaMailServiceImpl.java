package priv.starfish.common.mail;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import priv.starfish.common.exception.ResultException;
import priv.starfish.common.exception.ValidationException;
import priv.starfish.common.util.ExceptionUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

public class JavaMailServiceImpl implements MailService {
	protected final Log logger = LogFactory.getLog(this.getClass());

	//
	protected JavaMailSender javaMailSender;

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendMail(SimpleMailMessage mailMessage, boolean isHtml, MapContext extra, String... attachmentFilePaths) {
		String mailFrom = mailMessage.getFrom();
		String[] mailTo = mailMessage.getTo();
		String mailSubject = mailMessage.getSubject();
		String mailText = mailMessage.getText();
		if (StrUtil.isNullOrBlank(mailFrom)) {
			throw new ValidationException("邮件发送者地址不能为空");
		}
		if (TypeUtil.isNullOrEmpty(mailTo)) {
			throw new ValidationException("邮件接收者地址不能为空");
		}
		if (StrUtil.isNullOrBlank(mailSubject)) {
			throw new ValidationException("邮件主题不能为空");
		}
		if (StrUtil.isNullOrBlank(mailText)) {
			mailText = StrUtil.EmptyStr;
		}
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			//
			mimeMessageHelper.setFrom(mailFrom);
			mimeMessageHelper.setTo(mailTo);
			mimeMessageHelper.setSubject(mailSubject);
			mimeMessageHelper.setText(mailText, isHtml);
			//
			for (String attachmentFilePath : attachmentFilePaths) {
				FileSystemResource fileResource = new FileSystemResource(attachmentFilePath);
				String attachmentFilename = fileResource.getFilename();
				mimeMessageHelper.addAttachment(attachmentFilename, fileResource);
			}
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new ResultException(ExceptionUtil.extractMsg(e));
		}
	}
}
