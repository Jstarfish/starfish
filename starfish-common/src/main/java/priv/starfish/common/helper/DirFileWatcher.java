package priv.starfish.common.helper;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 
 * @author Hu Changwei
 * @date 2013-12-11
 * 
 *       <pre>
 *       功能：监视给定目录下符合给定文件名匹配条件的文件，并可返回当前的文件名列表
 *        => 支持的匹配模式：1-通配符，2-前缀，3-后缀，4-正则表达式
 * </pre>
 */
public class DirFileWatcher {
	public static enum FileNameMatchMode {
		WildChar, Prefix, Suffix, Regexp;
	}

	//
	static private final int MILLIS_IN_SEC = 1000;
	static public final int DEFAULT_INTERVAL = 10;// in seconds
	static private final IOCase FIXED_FILE_NAMECASE = IOCase.SYSTEM;
	static private final Comparator<File> FIXED_FILE_COMPARATOR = NameFileComparator.NAME_SYSTEM_COMPARATOR;
	private String watchDir;
	private File watchDirObj;
	private FileFilter fileFilter;
	private int watchInterval;
	//
	private DirFileWatcher theWatcher = this;// !!! self ref
	private FileAlterationObserver fileObserver;
	private FileAlterationListener fileListener;
	private boolean isRunning = false;
	private Thread watchThread;
	//
	private ConcurrentLinkedQueue<String> curFileNames;
	private Date lastStartTime = null;
	private Date lastStopTime = null;

	//
	private static class WatchRunner implements Runnable {
		DirFileWatcher dirWatcher;

		public WatchRunner(DirFileWatcher dirWatcher) {
			this.dirWatcher = dirWatcher;
		}

		public void run() {
			while (this.dirWatcher.isRunning) {
				this.dirWatcher.fileObserver.checkAndNotify();
				if (!this.dirWatcher.isRunning) {
					break;
				}
				try {
					Thread.sleep(this.dirWatcher.watchInterval
							* DirFileWatcher.MILLIS_IN_SEC);
				} catch (final InterruptedException ignored) {
				}
			}
		}
	}

	public DirFileWatcher(String directory, String fileNamePattern,
			FileNameMatchMode fileNameMatchMode, int interval) {
		this.watchDirObj = new File(directory);
		// correct dir
		this.watchDir = this.watchDirObj.getAbsolutePath();
		// this.watchDir = this.watchDirObj.exists() ?
		// this.watchDirObj.getAbsolutePath() : directory;
		//
		FileFilter filterFile = FileFilterUtils.fileFileFilter();
		//
		if (fileNameMatchMode == null && StringUtils.isBlank(fileNamePattern)) {
			this.fileFilter = filterFile;
		} else {
			if (fileNameMatchMode == null) {
				fileNameMatchMode = FileNameMatchMode.WildChar;
			}
			fileNamePattern = fileNamePattern.trim();
			FileFilter filterX = null;
			switch (fileNameMatchMode) {
			case WildChar:
				filterX = new WildcardFileFilter(fileNamePattern);
				break;
			case Prefix:
				filterX = new PrefixFileFilter(fileNamePattern);
				break;
			case Suffix:
				if (!fileNamePattern.startsWith(".")) {
					fileNamePattern = "." + fileNamePattern;
				}
				filterX = new SuffixFileFilter(fileNamePattern);
				break;
			case Regexp:
				filterX = new RegexFileFilter(fileNamePattern);
				break;
			default:
				throw new IllegalArgumentException(
						"fileNameMatchMode must in {WildChar, Prefix, Suffix, Regexp} !");
			}
			this.fileFilter = FileFilterUtils.and(
					FileFilterUtils.asFileFilter(filterFile),
					FileFilterUtils.asFileFilter(filterX));
		}
		//
		this.watchInterval = interval;
		//
		this.fileObserver = new FileAlterationObserver(this.watchDir,
				this.fileFilter, FIXED_FILE_NAMECASE);
		this.fileListener = new FileAlterationListener() {
			public void onStart(FileAlterationObserver observer) {
				// System.out.println("checking files in \"" +
				// theWatcher.watchDir
				// + "\"");
			}

			public void onDirectoryCreate(File directory) {
				// System.out.println("observer on Directory Create " +
				// directory.getName());
			}

			public void onDirectoryChange(File directory) {
				// System.out.println("observer on Directory Change " +
				// directory.getName());
			}

			public void onDirectoryDelete(File directory) {
				// System.out.println("observer on Directory Delete " +
				// directory.getName());
			}

			public void onFileCreate(File file) {
				String fileName = file.getName();
				System.out.println("File ++ : " + fileName);
				//
				if (!theWatcher.curFileNames.contains(fileName)) {
					theWatcher.curFileNames.add(fileName);
					System.out.println("Current File Count : "
							+ theWatcher.curFileNames.size());
				}
			}

			public void onFileChange(File file) {
				// re-save the last changed file
				String fileName = file.getName();
				if (!theWatcher.curFileNames.contains(fileName)) {
					theWatcher.curFileNames.add(fileName);
					System.out.println(fileName + " Changed !");
				}
				// System.out.println("File Change : " + file.getName());
			}

			public void onFileDelete(File file) {
				String fileName = file.getName();
				System.out.println("File -- : " + fileName);
				//
				if (theWatcher.curFileNames.contains(fileName)) {
					theWatcher.curFileNames.remove(fileName);
					System.out.println("Current File Count : "
							+ theWatcher.curFileNames.size());
				}
			}

			public void onStop(FileAlterationObserver observer) {
				// System.out.println("checking over in dir \"" +
				// theWatcher.watchDir + "\"");
			}
		};
		this.fileObserver.addListener(this.fileListener);
		//
		this.curFileNames = new ConcurrentLinkedQueue<String>();
	}

	public DirFileWatcher(String directory, String fileNamePattern,
			FileNameMatchMode fileNameMatchMode) {
		this(directory, fileNamePattern, fileNameMatchMode, DEFAULT_INTERVAL);
	}

	public DirFileWatcher(String directory, String fileNamePattern, int interval) {
		this(directory, fileNamePattern, null, interval);
	}

	public DirFileWatcher(String directory, String fileNamePattern) {
		this(directory, fileNamePattern, DEFAULT_INTERVAL);
	}

	public DirFileWatcher(String directory, int interval) {
		this(directory, null, interval);
	}

	public DirFileWatcher(String directory) {
		this(directory, DEFAULT_INTERVAL);
	}

	private void saveInitDirFileNames() {
		//
		File[] files = new File[0];
		if (this.watchDirObj.exists() && this.watchDirObj.isDirectory()) {
			files = this.watchDirObj.listFiles(this.fileFilter);
		}
		if (files.length > 1) {
			Arrays.sort(files, FIXED_FILE_COMPARATOR);
		}
		curFileNames.clear();
		for (File file : files) {
			curFileNames.add(file.getName());
		}
		System.out.println("Start-time File Count : "
				+ this.curFileNames.size());
	}

	public synchronized List<String> start() throws Exception {
		if (this.isRunning) {
			System.out.println("DirectoryWatcher is started already on "
					+ this.lastStartTime + " !");
			return null;
		} else {
			this.isRunning = true;
			this.watchThread = new Thread(new WatchRunner(this));
			lastStartTime = new Date(System.currentTimeMillis());
			//
			System.out.println("Watcher Started on " + this.lastStartTime
					+ " >>>");
			this.fileObserver.initialize();
			this.saveInitDirFileNames();
			this.watchThread.start();
		}
		return null;
	}

	public String getWatchDir() {
		return this.watchDir;
	}

	public boolean hasMoreFileNames() {
		return !this.curFileNames.isEmpty();
	}

	public String peekNextFileName() {
		return this.curFileNames.peek();
	}

	public String pollNextFileName() {
		return this.curFileNames.poll();
	}

	public synchronized void stop() throws Exception {
		stop(this.watchInterval);
	}

	public synchronized void stop(long stopInterval) throws Exception {
		if (this.isRunning == false) {
			System.out.println("DirectoryWatcher is Stopped already on "
					+ this.lastStopTime + " !");
		}
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(watchDir).append(fileFilter)
				.append(watchInterval).append(fileObserver)
				.append(fileListener).append(isRunning).append(watchThread)
				.append(curFileNames).append(lastStartTime)
				.append(lastStopTime).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		DirFileWatcher another = (DirFileWatcher) obj;
		return new EqualsBuilder().append(watchDir, another.watchDir)
				.append(fileFilter, another.fileFilter)
				.append(watchInterval, another.watchInterval)
				.append(fileObserver, another.fileObserver)
				.append(fileListener, another.fileListener)
				.append(isRunning, another.isRunning)
				.append(watchThread, another.watchThread)
				.append(curFileNames, another.curFileNames)
				.append(lastStartTime, another.lastStartTime)
				.append(lastStopTime, another.lastStopTime).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("watchDir", watchDir)
				.append("watchInterval", watchInterval)
				.append("isRunning", isRunning)
				.append("curFileNames", curFileNames)
				.append("lastStartTime", lastStartTime)
				.append("lastStopTime", lastStopTime).toString();
	}

}
