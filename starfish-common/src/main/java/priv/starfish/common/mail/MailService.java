package priv.starfish.common.mail;

import org.springframework.mail.SimpleMailMessage;
import priv.starfish.common.util.MapContext;


/**
 * Email发送服务
 * 
 * @author zhangjunjun
 * 
 */
public interface MailService {

	void sendMail(SimpleMailMessage mailMessage, boolean isHtml, MapContext extra, String... attachmentFilePaths);

}
