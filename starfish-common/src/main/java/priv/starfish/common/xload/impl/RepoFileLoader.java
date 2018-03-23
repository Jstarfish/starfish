package priv.starfish.common.xload.impl;

import java.util.Map;

import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.repo.FileOpResult;
import priv.starfish.common.repo.FileRepoConfig;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.common.xload.FileProvider;
import priv.starfish.common.xload.FileResult;

public class RepoFileLoader implements FileProvider {
	@Override
	public String getName() {
		return "文件资源库加载者";
	}

	String[] supportedFileExts = new String[] {};

	@Override
	public boolean willHandle(Map<String, Object> infoMap) {
		if (infoMap == null) {
			return false;
		}
		FileRepository fileRepository = WebEnvHelper.getFileRepository();
		FileRepoConfig fileRepoConfig = fileRepository.getFileRepoConfig();
		MapContext paramMap = MapContext.from(infoMap);
		// 只会处理以 uuid 或 usage + relPath 为参数的请求
		String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
		String relPath = paramMap.getTypedValue(FileRepository.KEY_REL_PATH, String.class);
		if (StrUtil.isNullOrBlank(usage)) {
			String uuid = paramMap.getTypedValue(FileRepository.KEY_UUID, String.class);
			if (StrUtil.isNullOrBlank(uuid)) {
				return false;
			}
		} else {
			if (!fileRepoConfig.hasBaseDir(usage)) {
				return false;
			}
			if (StrUtil.isNullOrBlank(relPath)) {
				return false;
			}
		}
		// String fileExt = paramMap.getTypedValue("fileExt", String.class);
		// 检查文件扩展名是否在支持之列
		return true;
	}

	@Override
	public FileResult provideFile(Map<String, Object> infoMap) {
		MapContext paramMap = MapContext.from(infoMap);
		FileOpResult fileOpResult = null;
		FileResult fileResult = new FileResult();
		//
		FileRepository fileRepository = WebEnvHelper.getFileRepository();
		//
		String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
		String relPath = paramMap.getTypedValue(FileRepository.KEY_REL_PATH, String.class);
		if (StrUtil.isNullOrBlank(usage)) {
			String uuid = paramMap.getTypedValue(FileRepository.KEY_UUID, String.class);
			fileOpResult = fileRepository.loadFile(uuid, paramMap);
		} else {
			fileOpResult = fileRepository.loadFile(usage, relPath, paramMap);
		}
		//
		fileResult.setInputStream(fileOpResult.getInputStream());
		//
		fileResult.type = fileOpResult.type;
		fileResult.message = fileOpResult.message;
		fileResult.setFileUuid(fileOpResult.getUuid());
		fileResult.setFileUsage(fileOpResult.getUsage());
		fileResult.setFileRelPath(fileOpResult.getRelPath());
		relPath = fileOpResult.getRelPath();
		if (StrUtil.hasText(relPath)) {
			String fileName = FileHelper.extractShortFileName(relPath);
			fileResult.setFileName(fileName);
		}
		fileResult.setFileBrowseUrl(fileOpResult.getBrowseUrl());
		fileResult.setFileDeleteUrl(fileOpResult.getDeleteUrl());
		fileResult.setExtra(fileOpResult.getExtra());
		//
		return fileResult;
	}
}
