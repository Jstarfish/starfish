package priv.starfish.mall.web.servlet;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Couple;
import priv.starfish.common.repo.FileOpResult;
import priv.starfish.common.repo.FileRepoConfig;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.repo.FileRepositoryReferer;
import priv.starfish.common.util.ExceptionUtil;
import priv.starfish.common.web.WebEnvHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Servlet implementation class RepoFileServlet
 */
@WebServlet(name = "repoFileServlet", asyncSupported = true, urlPatterns = { "/repo/*" })
public class RepoFileServlet extends HttpServlet implements FileRepositoryReferer {
	private static final long serialVersionUID = 1L;
	//
	FileRepository fileRepository;
	FileRepoConfig fileRepoConfig;

	@Override
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
		this.fileRepoConfig = this.fileRepository.getFileRepoConfig();
		//
		String noImageUrl = this.fileRepoConfig.getNoImageUrl();
		try {
			URL url = new URL(noImageUrl);
			noImageFileBytes = IOUtils.toByteArray(url);
		} catch (IOException e) {
			System.out.println(">> 读取无图图片[" + noImageUrl + "]时出错：" + ExceptionUtil.extractMsg(e));
		}
	}

	//
	String urlPatternBase = "/repo";
	String contextPath = null;
	int contextPathLen = -1;
	byte[] noImageFileBytes = new byte[0];

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//
		contextPath = config.getServletContext().getContextPath();
		contextPathLen = contextPath.length();
		// 注册以接收FileRepository
		WebEnvHelper.addFileRepositoryReferer(this);
	}

	public RepoFileServlet() {
		super();
	}

	private void responseAsNoImage(HttpServletResponse response) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		IOUtils.write(noImageFileBytes, outputStream);
		IOUtils.closeQuietly(outputStream);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestUri = URLDecoder.decode(request.getRequestURI(), FileHelper.ENCODING_UTF8);
		System.out.println("requestUri : " + requestUri);
		String fileUrlPath = requestUri.substring(contextPathLen);
		System.out.println("filePath : " + fileUrlPath);
		//
		int end = fileUrlPath.indexOf(';');
		if (end == -1) {
			end = fileUrlPath.indexOf('?');
			if (end == -1) {
				end = fileUrlPath.length();
			}
		}
		fileUrlPath = fileUrlPath.substring(0, end);
		//
		String fileName = FileHelper.extractShortFileName(fileUrlPath);
		String contentType = FileHelper.getContentType(fileName);
		boolean isImageFile = contentType.startsWith("image/");
		//
		Couple<String, String> usageRelPath = this.fileRepoConfig.parseUrlUsageRelPath(fileUrlPath);
		if (usageRelPath != null) {
			String usage = usageRelPath.getFirst();
			String relPath = usageRelPath.getSecond();
			//
			String fileETag = this.fileRepository.getFileETag(usage, relPath);
			if (fileETag != null) {
				String reqETag = request.getHeader("If-None-Match");
				if (fileETag.equals(reqETag)) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				} else {
					OutputStream outputStream = null;
					InputStream inputStream = null;
					try {
						// ======================================================
						FileOpResult fileOpResult = this.fileRepository.loadFile(usage, relPath, null);
						// ======================================================
						if (fileOpResult != null) {
							inputStream = fileOpResult.getInputStream();
							if (inputStream != null) {
								response.setHeader("ETag", fileETag);
								//
								response.setContentType(contentType);
								outputStream = response.getOutputStream();
								IOUtils.copy(inputStream, outputStream);
							}
						} else {
							response.setStatus(HttpStatus.SC_NOT_FOUND);
						}
					} catch (Exception e) {
						System.err.println(ExceptionUtil.extractMsg(e));
					} finally {
						if (inputStream != null) {
							IOUtils.closeQuietly(inputStream);
						}
						if (outputStream != null) {
							IOUtils.closeQuietly(outputStream);
						}
					}
				}
			} else {
				if (isImageFile) {
					responseAsNoImage(response);
				} else {
					response.setStatus(HttpStatus.SC_NOT_FOUND);
				}
			}
		} else {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			if (isImageFile) {
				responseAsNoImage(response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Allow", "GET");
		response.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
	}

}
