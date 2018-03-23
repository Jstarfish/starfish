package priv.starfish.common.xload.impl;

import java.io.InputStream;
import java.util.Map;

import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.repo.FileOpResult;
import priv.starfish.common.repo.FileRepoConfig;
import priv.starfish.common.repo.FileRepoHelper;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.common.xload.FileReceiver;
import priv.starfish.common.xload.FileResult;

public class RepoFileSaver implements FileReceiver {
	@Override
	public String getName() {
		return "文件资源库保存者";
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
		// 只会处理新增上传和更新上传
		Boolean toUpdate = BoolUtil.isTrue(paramMap.getTypedValue(FileRepository.KEY_UPDATE, String.class));
		if (toUpdate) {
			String uuid = paramMap.getTypedValue(FileRepository.KEY_UUID, String.class);
			if (StrUtil.isNullOrBlank(uuid)) {
				return false;
			}
		} else {
			String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
			if (!fileRepoConfig.hasBaseDir(usage)) {
				return false;
			}
		}
		// String fileName = paramMap.getTypedValue("fileName", String.class);
		// String fileExt = FileHelper.extractFileNameExt(fileName);
		// 检查文件扩展名是否在支持之列
		return true;
	}

	@Override
	public FileResult receiveFile(Map<String, Object> infoMap, InputStream inputStream) {
		MapContext paramMap = MapContext.from(infoMap);
		//
		FileOpResult fileOpResult = null;
		FileResult fileResult = new FileResult();
		//
		FileRepository fileRepository = WebEnvHelper.getFileRepository();
		//
		Boolean toUpdate = BoolUtil.isTrue(paramMap.getTypedValue(FileRepository.KEY_UPDATE, String.class));
		if (toUpdate) {
			String uuid = paramMap.getTypedValue(FileRepository.KEY_UUID, String.class);
			fileOpResult = fileRepository.updateFile(uuid, inputStream, paramMap);
		} else {
			String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
			String subUsage = paramMap.getTypedValue(FileRepository.KEY_SUB_USAGE, String.class);
			//
			String relativeDir = FileRepoHelper.generateRelativeFileDir(subUsage);
			//
			String originalFileName = paramMap.getTypedValue("originalFileName", String.class);
			String fileName = paramMap.getTypedValue("fileName", String.class);
			String fileExt = FileHelper.extractFileNameExt(fileName);
			long fileSize = paramMap.getTypedValue("fileSize", Long.class);
			boolean reserveFileName = BoolUtil.isTrue(String.valueOf(paramMap.get(FileRepository.KEY_RESERVE_FILE_NAME)));
			fileName = reserveFileName ? fileName : FileRepoHelper.generateUniqueFileName(fileExt);
			String relativeFilePath = relativeDir + "/" + fileName;
			//
			System.out.println(">> usage : " + usage + " , relativeFilePath: " + relativeFilePath);
			//
			fileOpResult = fileRepository.saveFile(usage, relativeFilePath, inputStream, paramMap);
			//
			fileResult.setOriginalFileName(originalFileName);
			fileResult.setFileName(fileName);
			fileResult.setFileSize(fileSize);
		}
		fileResult.type = fileOpResult.type;
		fileResult.message = fileOpResult.message;
		fileResult.setFileUuid(fileOpResult.getUuid());
		fileResult.setFileUsage(fileOpResult.getUsage());
		fileResult.setFileRelPath(fileOpResult.getRelPath());
		fileResult.setFileBrowseUrl(fileOpResult.getBrowseUrl());
		fileResult.setFileDeleteUrl(fileOpResult.getDeleteUrl());
		fileResult.setExtra(fileOpResult.getExtra());
		//
		return fileResult;
	}
}
