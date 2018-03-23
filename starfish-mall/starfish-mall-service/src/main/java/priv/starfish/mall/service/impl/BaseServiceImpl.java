package priv.starfish.mall.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.misc.HasIcon;
import priv.starfish.mall.comn.misc.HasImage;
import priv.starfish.mall.comn.misc.HasLogo;
import priv.starfish.mall.service.BaseService;
import priv.starfish.mall.service.misc.XOrderMessageProxy;

public abstract class BaseServiceImpl implements BaseService {
	protected final Log logger = LogFactory.getLog(this.getClass());
	//

	@Autowired(required = false)
	protected SimpleMessageSender simpleMessageSender;
	
	protected XOrderMessageProxy xOrderMessageProxy = XOrderMessageProxy.getInstance();

	@Autowired(required = false)
	protected FileRepository fileRepository;

	@SuppressWarnings("unchecked")
	protected <T> T filterFileBrowseUrl(HasImage xEntity) {
		if (xEntity != null) {
			String browseUrl = null;
			if (StrUtil.hasText(xEntity.getImageUsage()) && StrUtil.hasText(xEntity.getImagePath())) {
				browseUrl = fileRepository.getFileBrowseUrl(xEntity.getImageUsage(), xEntity.getImagePath());
			} else {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			xEntity.setFileBrowseUrl(browseUrl);
		}
		return (T) xEntity;
	}

	@SuppressWarnings("unchecked")
	protected <T> T filterFileBrowseUrl(HasLogo xEntity) {
		if (xEntity != null) {
			String browseUrl = null;
			if (StrUtil.hasText(xEntity.getLogoUsage()) && StrUtil.hasText(xEntity.getLogoPath())) {
				browseUrl = fileRepository.getFileBrowseUrl(xEntity.getLogoUsage(), xEntity.getLogoPath());
			} else {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			xEntity.setFileBrowseUrl(browseUrl);
		}
		return (T) xEntity;
	}

	@SuppressWarnings("unchecked")
	protected <T> T filterFileBrowseUrl(HasIcon xEntity) {
		if (xEntity != null) {
			String browseUrl = null;
			if (StrUtil.hasText(xEntity.getIconUsage()) && StrUtil.hasText(xEntity.getIconPath())) {
				browseUrl = fileRepository.getFileBrowseUrl(xEntity.getIconUsage(), xEntity.getIconPath());
			} else {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			xEntity.setFileBrowseUrl(browseUrl);
		}
		return (T) xEntity;
	}
}
