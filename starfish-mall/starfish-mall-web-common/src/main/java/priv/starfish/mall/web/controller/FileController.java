package priv.starfish.mall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.repo.FileOpResult;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.xload.FileDownloader;
import priv.starfish.common.xload.FileUploader;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Remark("文件上传、下载、操作")
@RequestMapping(value = "/file")
public class FileController extends BaseController {

	@Resource
	FileRepository fileRepository;

	@Remark("文件上传演示页面")
	@RequestMapping(value = { "/demo/jsp" }, method = RequestMethod.GET)
	public String toEntryIndexPage() {
		return "demo/file";
	}

	@Remark("文件上传入口")
	@RequestMapping(value = { "/upload" }, method = RequestMethod.POST)
	public void uploadFiles(HttpServletRequest request, HttpServletResponse response) {
		FileUploader fileUploader = FileUploader.getInstance();

		fileUploader.upload(request, response);
	}

	@Remark("文件资源删除")
	@RequestMapping(value = { "/delete" }, method = RequestMethod.GET)
	@ResponseBody
	public FileOpResult deleteFile(HttpServletRequest request, @RequestParam("uuid") String uuid, HttpServletResponse response) {
		//response.setHeader("Access-Control-Allow-Origin", "*");

		return fileRepository.deleteFile(uuid, null);
	}

	@Remark("文件下载入口")
	@RequestMapping(value = { "/download" })
	public void downloadFiles(HttpServletRequest request, HttpServletResponse response) {
		FileDownloader fileDownloader = FileDownloader.getInstance();

		fileDownloader.download(request, response);
	}

}
