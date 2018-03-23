package priv.starfish.mall.service.impl;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.repo.FileOpResult;
import priv.starfish.common.repo.FileRepoConfig;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.ExceptionUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.entity.RepoFile;
import priv.starfish.mall.dao.comn.RepoFileDao;
import priv.starfish.mall.service.BaseService;

import javax.annotation.Resource;
import java.io.*;
import java.text.MessageFormat;
import java.util.UUID;

//接口：priv.starfish.common.repo.FileRepository
@Service("fileRepository")
public class FileRepositoryImpl extends BaseServiceImpl implements FileRepository, BaseService {
	@Resource
	private RepoFileDao repoFileDao;

	private FileRepoConfig fileRepoConfig;

	public void setFileRepoConfig(FileRepoConfig fileRepoConfig) {
		this.fileRepoConfig = fileRepoConfig;
	}

	public FileRepoConfig getFileRepoConfig() {
		return fileRepoConfig;
	}

	@Override
	public String getFilePath(String uuid) {
		RepoFile repoFile = repoFileDao.selectByUuid(uuid);
		if (repoFile != null) {
			String usage = repoFile.getUsage();
			String relPath = repoFile.getRelPath();
			return getFilePath(usage, relPath);
		}
		return null;
	}

	@Override
	public String getFilePath(String usage, String relPath) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		if (baseDir == null) {
			return null;
		}
		return baseDir + (relPath == null ? "" : "/" + relPath);
	}

	@Override
	public String getFileBrowseUrl(String uuid) {
		RepoFile repoFile = repoFileDao.selectByUuid(uuid);
		if (repoFile != null) {
			String usage = repoFile.getUsage();
			String relPath = repoFile.getRelPath();
			return getFileBrowseUrl(usage, relPath);
		}
		return null;
	}

	@Override
	public String getFileBrowseUrl(String usage, String relPath) {
		String baseUrl = this.fileRepoConfig.getBaseUrl(usage);
		if (baseUrl == null) {
			return null;
		}
		return baseUrl + (relPath == null ? "" : "/" + relPath);
	}

	@Override
	public String getFileDeleteUrl(String uuid) {
		String deleteUrl = this.fileRepoConfig.getDeleteUrl();
		return deleteUrl == null ? null : MessageFormat.format(deleteUrl, uuid);
	}

	@Override
	public String getFileDeleteUrl(String usage, String relPath) {
		RepoFile repoFile = repoFileDao.selectByUsageAndRelPath(usage, relPath);
		if (repoFile != null) {
			String uuid = repoFile.getUuid();
			return getFileDeleteUrl(uuid);
		}
		return null;
	}

	@Override
	public boolean existsFile(String usage, String relPath) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		String filePath = baseDir + "/" + relPath;
		File targetFile = new File(filePath);
		return targetFile.exists() && targetFile.isFile();
	}

	@Override
	public String getFileETag(String usage, String relPath) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		String filePath = baseDir + "/" + relPath;
		File targetFile = new File(filePath);
		boolean fileExists = targetFile.exists() && targetFile.isFile();
		if (fileExists) {
			long lastModified = targetFile.lastModified();
			long fileSize = targetFile.length();
			return StrUtil.generateETag(lastModified, fileSize);
		}
		return null;
	}

	@Override
	public FileOpResult loadFile(String uuid, MapContext extra) {
		RepoFile repoFile = repoFileDao.selectByUuid(uuid);
		if (repoFile != null) {
			String usage = repoFile.getUsage();
			String relPath = repoFile.getRelPath();
			return this.loadFile(usage, relPath, extra);
		} else {
			FileOpResult fileOpResult = new FileOpResult();
			fileOpResult.setUuid(uuid);
			fileOpResult.setExtra(extra);
			fileOpResult.type = Type.warn;
			fileOpResult.message = "未找到指定UUID的文件（可能已删除）";
			return fileOpResult;
		}
	}

	@Override
	public FileOpResult loadFile(String usage, String relPath, MapContext extra) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		String filePath = baseDir + "/" + relPath;
		//
		FileOpResult fileOpResult = new FileOpResult();
		fileOpResult.setUsage(usage);
		fileOpResult.setRelPath(relPath);
		fileOpResult.setExtra(extra);
		File targetFile = new File(filePath);
		boolean fileExists = targetFile.exists() && targetFile.isFile();
		if (!fileExists) {
			fileOpResult.type = Type.error;
			fileOpResult.message = "指定的文件不存在或已删除";
		} else {
			try {
				FileInputStream fileStream = new FileInputStream(targetFile);
				fileOpResult.setInputStream(fileStream);
			} catch (FileNotFoundException e) {
				fileOpResult.type = Type.error;
				fileOpResult.message = "未找到指定的文件";
			}
		}
		return fileOpResult;
	}

	@Override
	public FileOpResult saveFile(String usage, String relPath, InputStream inputStream, MapContext extra) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		String filePath = baseDir + "/" + relPath;
		//
		FileOpResult fileOpResult = new FileOpResult();
		fileOpResult.setUsage(usage);
		fileOpResult.setRelPath(relPath);
		fileOpResult.setExtra(extra);
		//
		FileOutputStream outputStream = null;
		File targetFile = null;
		boolean fileExists = false;
		boolean regExists = false;
		try {
			targetFile = new File(filePath);
			fileExists = targetFile.exists() && targetFile.isFile();
			if (!fileExists) {
				FileHelper.makeDirsForFile(filePath);
			}
			//
			RepoFile repoFile = repoFileDao.selectByUsageAndRelPath(usage, relPath);
			if (repoFile != null) {
				regExists = true;
			}
			//
			outputStream = new FileOutputStream(targetFile);
			IOUtils.copy(inputStream, outputStream);
			//
			if (regExists) {
				repoFileDao.update(repoFile);
			} else {
				repoFile = new RepoFile();
				//TODO 插入的id从哪里获取的,先随便设置一个
				repoFile.setId(1L);
				repoFile.setUuid(UUID.randomUUID().toString());
				repoFile.setUsage(usage);
				repoFile.setRelPath(relPath);
				//
				repoFileDao.insert(repoFile);
			}
			//
			fileOpResult.setUuid(repoFile.getUuid());
			fileOpResult.setBrowseUrl(this.getFileBrowseUrl(usage, relPath));
			fileOpResult.setDeleteUrl(this.getFileDeleteUrl(repoFile.getUuid()));
		} catch (Exception e) {
			if (!fileExists && !regExists) {
				if (targetFile != null && targetFile.exists() && targetFile.isFile()) {
					targetFile.delete();
				}
			}
			//
			fileOpResult.type = Type.error;
			fileOpResult.message = "文件保存失败：" + ExceptionUtil.extractMsg(e);
		} finally {
			if (inputStream != null) {
				IOUtils.closeQuietly(inputStream);
			}
			if (outputStream != null) {
				IOUtils.closeQuietly(outputStream);
			}
		}
		//
		return fileOpResult;
	}

	@Override
	public FileOpResult updateFile(String uuid, InputStream inputStream, MapContext extra) {
		RepoFile repoFile = repoFileDao.selectByUuid(uuid);
		if (repoFile != null) {
			String usage = repoFile.getUsage();
			String relPath = repoFile.getRelPath();
			return this.updateFile(usage, relPath, inputStream, extra);
		} else {
			FileOpResult fileOpResult = new FileOpResult();
			fileOpResult.setUuid(uuid);
			fileOpResult.setExtra(extra);
			fileOpResult.type = Type.warn;
			fileOpResult.message = "未找到指定UUID的文件（可能已删除）";
			return fileOpResult;
		}
	}

	@Override
	public FileOpResult updateFile(String usage, String relPath, InputStream inputStream, MapContext extra) {
		return this.saveFile(usage, relPath, inputStream, extra);
	}

	@Override
	public FileOpResult deleteFile(String uuid, MapContext extra) {
		RepoFile repoFile = repoFileDao.selectByUuid(uuid);
		if (repoFile != null) {
			String usage = repoFile.getUsage();
			String relPath = repoFile.getRelPath();
			return this.deleteFile(usage, relPath, extra);
		} else {
			FileOpResult fileOpResult = new FileOpResult();
			fileOpResult.setUuid(uuid);
			fileOpResult.setExtra(extra);
			fileOpResult.type = Type.warn;
			fileOpResult.message = "未找到指定UUID的文件（可能已删除）";
			return fileOpResult;
		}
	}

	@Override
	public FileOpResult deleteFile(String usage, String relPath, MapContext extra) {
		String baseDir = this.fileRepoConfig.getBaseDir(usage);
		String filePath = baseDir + "/" + relPath;
		//
		FileOpResult fileOpResult = new FileOpResult();
		fileOpResult.setUsage(usage);
		fileOpResult.setRelPath(relPath);
		fileOpResult.setExtra(extra);
		//
		File targetFile = null;
		boolean fileExists = false;
		boolean regExists = false;
		try {
			targetFile = new File(filePath);
			fileExists = targetFile.exists() && targetFile.isFile();
			//
			RepoFile repoFile = repoFileDao.selectByUsageAndRelPath(usage, relPath);
			if (repoFile != null) {
				regExists = true;
			}
			//
			if (fileExists) {
				targetFile.delete();
			}
			//
			if (regExists) {
				repoFileDao.deleteById(repoFile.getId());
			}
			//
			fileOpResult.setUuid(repoFile.getUuid());
		} catch (Exception e) {
			fileOpResult.type = Type.error;
			fileOpResult.message = "文件删除失败：" + ExceptionUtil.extractMsg(e);
		}
		//
		return fileOpResult;
	}

}
