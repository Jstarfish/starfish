package priv.starfish.common.repo;

import priv.starfish.common.util.MapContext;

import java.io.InputStream;


public interface FileRepository {
	// 资源文件用途参数名
	public static final String KEY_USAGE = "usage";
	// 资源分支相对路径参数名
	public static final String KEY_REL_PATH = "relPath";
	// 资源文件细分用途参数名（将会在分支路径下创建文件夹）
	public static final String KEY_SUB_USAGE = "subUsage";
	// 上传时是否保留文件名参数名
	public static final String KEY_RESERVE_FILE_NAME = "reserveFileName";
	// 更新标记参数名 update : true
	public static final String KEY_UPDATE = "update";
	// 更新用的uuid参数名
	public static final String KEY_UUID = "uuid";

	public void setFileRepoConfig(FileRepoConfig fileRepoConfig);

	public FileRepoConfig getFileRepoConfig();

	//
	public String getFilePath(String uuid);

	public String getFilePath(String usage, String relPath);

	public String getFileBrowseUrl(String uuid);

	public String getFileBrowseUrl(String usage, String relPath);

	public String getFileDeleteUrl(String uuid);

	public String getFileDeleteUrl(String usage, String relPath);

	//
	public boolean existsFile(String usage, String relPath);

	public String getFileETag(String usage, String relPath);

	//
	public FileOpResult loadFile(String uuid, MapContext extra);

	public FileOpResult loadFile(String usage, String relPath, MapContext extra);

	//
	public FileOpResult saveFile(String usage, String relPath, InputStream inputStream, MapContext extra);

	//
	public FileOpResult updateFile(String uuid, InputStream inputStream, MapContext extra);

	public FileOpResult updateFile(String usage, String relPath, InputStream inputStream, MapContext extra);

	//
	public FileOpResult deleteFile(String uuid, MapContext extra);

	public FileOpResult deleteFile(String usage, String relPath, MapContext extra);

}
