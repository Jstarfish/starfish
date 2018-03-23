package priv.starfish.common.xload;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;

public class FileReceiverMgr {
	private final Log logger = LogFactory.getLog(this.getClass());
	//
	private static SinkFileReceiver sinkReceiver = new SinkFileReceiver();
	//
	private String name = "<未命名文件接收者管理器>";
	private List<FileReceiver> fileReceivers = new CopyOnWriteArrayList<FileReceiver>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReceiverClassNames(String[] receiverClassNames) {
		this.setReceiverClassNames(TypeUtil.arrayToList(receiverClassNames));
	}

	@SuppressWarnings("unchecked")
	public void setReceiverClassNames(List<String> receiverClassNames) {
		for (String className : receiverClassNames) {
			if (StrUtil.isNullOrBlank(className)) {
				continue;
			}
			try {
				className = className.trim();
				//
				Class<FileReceiver> clazz = (Class<FileReceiver>) Class.forName(className);
				FileReceiver instance = clazz.newInstance();
				this.addReceiver(instance);
			} catch (ClassNotFoundException e) {
				logger.warn("未找到指定的类：" + className);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setReceivers(List<FileReceiver> receivers) {
		if (receivers == null) {
			return;
		}
		for (int i = 0; i < receivers.size(); i++) {
			this.addReceiver(receivers.get(i));
		}
	}

	// 添加文件接收、处理者
	public boolean addReceiver(FileReceiver receiver) {
		if (receiver == null || fileReceivers.contains(receiver)) {
			return false;
		}
		fileReceivers.add(receiver);
		System.out.println("文件接收者已添加：" + receiver.getName());
		return true;
	}

	public boolean removeReceiver(FileReceiver receiver) {
		if (receiver == null || !fileReceivers.contains(receiver)) {
			return false;
		}
		fileReceivers.remove(receiver);
		System.out.println("文件接收者已移除：" + receiver.getName());
		return true;
	}

	public FileReceiver pick(Map<String, Object> infoMap) {
		int count = fileReceivers.size();
		for (int i = 0; i < count; i++) {
			FileReceiver receiver = fileReceivers.get(i);
			if (receiver != null && receiver.willHandle(infoMap)) {
				return receiver;
			}
		}
		return sinkReceiver;
	}
}

class SinkFileReceiver implements FileReceiver {
	private String name = "无条件文件接受者";

	@Override
	public String getName() {
		return this.name;
	}

	// 无条件接收
	@Override
	public boolean willHandle(Map<String, Object> infoMap) {
		return true;
	}

	@Override
	public FileResult receiveFile(Map<String, Object> infoMap, InputStream inputStream) {
		if (inputStream != null) {
			IOUtils.closeQuietly(inputStream);
		}
		//
		MapContext paramMap = MapContext.from(infoMap);
		String originalFileName = paramMap.getTypedValue("originalFileName", String.class);
		String fileName = paramMap.getTypedValue("fileName", String.class);
		Long fileSize = paramMap.getTypedValue("fileSize", Long.class);
		//
		FileResult fileResult = new FileResult();
		fileResult.setOriginalFileName(originalFileName);
		fileResult.setFileName(fileName);
		fileResult.setFileSize(fileSize);
		//
		System.out.println("文件被 " + this.getName() + " 忽略：" + fileResult.getFileName());
		return fileResult;
	}

}
