package priv.starfish.common.helper;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.ResourceUtils;

/**
 * 
 * 监听指定目录下的（指定的）文件变化
 * 
 * @author koqiui
 * @date 2016年1月30日 上午1:32:48
 *
 */
public class DirFileChangeWatcher {
	public static interface FileChangeListener {
		void onFileModified(String fileName, String filePath);
	}

	static private final int MILLIS_IN_SEC = 1000;
	static public final int DEFAULT_INTERVAL = 5;// in seconds

	//
	private Path fileDir = null;
	private List<String> fileNameList = null;
	//
	private boolean isRunning = false;
	private Thread watchThread;
	private int watchInterval = DEFAULT_INTERVAL;
	private Date lastStartTime = null;
	private Date lastStopTime = null;
	//
	private List<FileChangeListener> fileChangeListeners = new ArrayList<FileChangeListener>();

	public void addFileChangeListener(FileChangeListener fileChangeListener) {
		if (!fileChangeListeners.contains(fileChangeListener)) {
			fileChangeListeners.add(fileChangeListener);
		}
	}

	public void removeFileChangeListener(FileChangeListener fileChangeListener) {
		if (fileChangeListeners.contains(fileChangeListener)) {
			fileChangeListeners.remove(fileChangeListener);
		}
	}

	private void notifyFileModified(String fileName, String filePath) {
		if (this.fileNameList == null || this.fileNameList.contains(fileName)) {
			for (int i = 0; i < fileChangeListeners.size(); i++) {
				fileChangeListeners.get(i).onFileModified(fileName, filePath);
			}
		}
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	private static class WatchRunner implements Runnable {
		DirFileChangeWatcher fileWatcher;

		public WatchRunner(DirFileChangeWatcher fileWatcher) {
			this.fileWatcher = fileWatcher;
		}

		public void run() {
			// System.out.println("running...");
			WatchService watcher = null;
			try {
				watcher = FileSystems.getDefault().newWatchService();
				WatchKey targetKey = fileWatcher.fileDir.register(watcher, ENTRY_MODIFY);
				//
				while (this.fileWatcher.isRunning) {
					WatchKey watchKey;
					try {
						watchKey = watcher.take();
					} catch (InterruptedException x) {
						this.fileWatcher.stopInThread();
						return;
					}
					if (!watchKey.equals(targetKey)) {
						System.out.println("忽略了" + watchKey);
						continue;
					}
					//
					for (WatchEvent<?> event : watchKey.pollEvents()) {
						Kind<?> kind = event.kind();
						//
						if (kind == OVERFLOW) {
							continue;
						}
						//
						WatchEvent<Path> eventX = cast(event);
						//
						Path fileName = eventX.context();
						Path filePath = fileWatcher.fileDir.resolve(fileName);
						//
						// System.out.format("%s : %s [ %s ]\n", kind.name(), fileName, filePath);
						//
						fileWatcher.notifyFileModified(fileName.toString(), filePath.toString());
					}
					//
					boolean valid = watchKey.reset();
					if (!valid) {
						this.fileWatcher.stopInThread();
						break;
					}
					//
					try {
						Thread.sleep(this.fileWatcher.watchInterval * DirFileChangeWatcher.MILLIS_IN_SEC);
					} catch (final InterruptedException ignored) {
						this.fileWatcher.stopInThread();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				//
				this.fileWatcher.stopInThread();
			} finally {
				if (watcher != null) {
					try {
						watcher.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void create(int watchInterval, Path dir, String... fileNames) {
		this.watchInterval = watchInterval;
		this.fileDir = dir;
		System.out.println(">> 要监视目录 " + dir.toString() + " 中如下文件的变化：");
		if (fileNames.length > 0) {
			this.fileNameList = new ArrayList<String>();
			//
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = fileNames[i];
				this.fileNameList.add(fileName);
				//
				System.out.println("   " + (i + 1) + ". " + fileName);
			}
		}
	}

	public DirFileChangeWatcher(int watchInterval, Path dir, String... fileNames) {
		this.create(watchInterval, dir, fileNames);
	}

	public DirFileChangeWatcher(Path dir, String... fileNames) {
		this(DEFAULT_INTERVAL, dir, fileNames);
	}

	public DirFileChangeWatcher(int watchInterval, File dir, String... fileNames) {
		if (!dir.isDirectory()) {
			System.err.println("给定的第一个参数不是目录！");
		}
		String dirPath = dir.getAbsolutePath();
		// System.out.println(dirPath);
		Path dirX = Paths.get(dirPath);
		//
		this.create(watchInterval, dirX, fileNames);
	}

	public DirFileChangeWatcher(File dir, String... fileNames) {
		this(DEFAULT_INTERVAL, dir, fileNames);
	}

	public DirFileChangeWatcher(int watchInterval, String dir, String... fileNames) throws FileNotFoundException {
		File fileDir = ResourceUtils.getFile(dir);
		if (!fileDir.isDirectory()) {
			System.err.println("给定的第一个参数不是目录！");
		}
		//
		String dirPath = fileDir.getAbsolutePath();
		// System.out.println(dirPath);
		Path dirX = Paths.get(dirPath);
		//
		this.create(watchInterval, dirX, fileNames);
	}

	public DirFileChangeWatcher(String dir, String... fileNames) throws FileNotFoundException {
		this(DEFAULT_INTERVAL, dir, fileNames);
	}

	public void start() {
		if (this.isRunning) {
			System.out.println("Watcher is started already on " + this.lastStartTime + " !");
		} else {
			this.isRunning = true;
			this.watchThread = new Thread(new WatchRunner(this));
			lastStartTime = new Date(System.currentTimeMillis());
			//
			System.out.println("Watcher Started on " + this.lastStartTime + " >>>");
			this.watchThread.start();
		}
	}

	private void stopInThread() {
		this.isRunning = false;
		this.lastStopTime = new Date(System.currentTimeMillis());
		//
		System.out.println("Watcher Exception on " + this.lastStopTime + " <<<");
	}

	public void stop(long stopInterval) {
		if (this.isRunning == false) {
			System.out.println("Watcher is Stopped already on " + this.lastStopTime + " !");
		} else {
			this.isRunning = false;
			try {
				this.watchThread.join(stopInterval * MILLIS_IN_SEC);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			this.lastStopTime = new Date(System.currentTimeMillis());
			//
			System.out.println("Watcher Stopped on " + this.lastStopTime + " <<<");
		}
	}

}
