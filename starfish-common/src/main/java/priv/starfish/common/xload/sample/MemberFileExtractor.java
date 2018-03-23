package priv.starfish.common.xload.sample;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.xload.FileReceiver;
import priv.starfish.common.xload.FileResult;

public class MemberFileExtractor implements FileReceiver {

	@Override
	public String getName() {
		return "项目组成员提取器";
	}

	private static final String TargetUsage = "member.extract";

	@Override
	public boolean willHandle(Map<String, Object> infoMap) {
		if (infoMap == null) {
			return false;
		}
		MapContext paramMap = MapContext.from(infoMap);
		String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
		return TargetUsage.equalsIgnoreCase(usage);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FileResult receiveFile(Map<String, Object> infoMap, InputStream inputStream) {
		MapContext paramMap = MapContext.from(infoMap);
		String originalFileName = paramMap.getTypedValue("originalFileName", String.class);
		String fileName = paramMap.getTypedValue("fileName", String.class);
		String fileExt = FileHelper.extractFileNameExt(fileName);
		long fileSize = paramMap.getTypedValue("fileSize", Long.class);
		//
		FileResult fileResult = new FileResult();
		fileResult.setOriginalFileName(originalFileName);
		fileResult.setFileName(fileName);
		fileResult.setFileSize(fileSize);
		//
		if (fileExt.toLowerCase().endsWith(".xml")) {
			SAXBuilder sb = new SAXBuilder();
			try {
				Document xmlDoc = sb.build(inputStream);
				Element rootElem = xmlDoc.getRootElement();
				List<Element> elems = rootElem.getChildren();
				int elemCount = elems.size();
				String title = null;
				String author = null;
				List<HashMap<String, String>> members = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < elemCount; i++) {
					Element elem = elems.get(i);
					String tagName = elem.getName();
					if (tagName.equalsIgnoreCase("author")) {
						Element le = elem.getChild("lastname");
						Element fe = elem.getChild("firstname");
						author = le.getTextTrim() + fe.getTextTrim();
					} else if (tagName.equalsIgnoreCase("title")) {
						title = elem.getTextTrim();
					} else if (tagName.equalsIgnoreCase("member")) {
						Element nameE = elem.getChild("name");
						Element genderE = elem.getChild("gender");
						Element positionE = elem.getChild("position");
						String name = nameE == null ? null : nameE.getTextTrim();
						String gender = genderE == null ? null : genderE.getTextTrim();
						String position = positionE == null ? null : positionE.getTextTrim();
						HashMap<String, String> member = new HashMap<String, String>();
						member.put("name", name);
						member.put("gender", gender);
						member.put("position", position);
						members.add(member);
					}
				}
				//
				HashMap<String, Object> extra = new LinkedHashMap<String, Object>();
				extra.put("title", title);
				extra.put("author", author);
				extra.put("members", members);
				//
				fileResult.setExtra(extra);
			} catch (Exception e) {
				fileResult.type = Type.error;
				fileResult.message = "解析 XML文件时出错！";
			} finally {
				if (inputStream != null) {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} else {
			fileResult.type = Type.warn;
			fileResult.message = "上传的文件不是 XML文件！";
		}
		//
		return fileResult;
	}

}
