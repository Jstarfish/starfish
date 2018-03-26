package priv.starfish.mall.manager.misc;

import org.apache.commons.io.IOUtils;
import priv.starfish.common.base.TypedField;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.model.Result;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.common.xload.FileReceiver;
import priv.starfish.common.xload.FileResult;
import priv.starfish.common.xport.XlsImportor;
import priv.starfish.mall.merchant.dto.MerchantDto;
import priv.starfish.mall.service.MerchantService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MerchantFileExtractor implements FileReceiver {

	@Override
	public String getName() {
		return "商户信息提取器";
	}

	private static final String TargetUsage = "merchant.extract";

	@Override
	public boolean willHandle(Map<String, Object> infoMap) {
		if (infoMap == null) {
			return false;
		}
		MapContext paramMap = MapContext.from(infoMap);
		String usage = paramMap.getTypedValue(FileRepository.KEY_USAGE, String.class);
		return TargetUsage.equalsIgnoreCase(usage);
	}

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
		if (fileExt.toLowerCase().endsWith(".xls")) {
			List<MerchantDto> merchants = new ArrayList<MerchantDto>();
			String dataName = "商户信息";
			List<TypedField> columns = setXlsColums();
			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
			try {
				maps = XlsImportor.readDataFrom(inputStream, dataName, TypeUtil.listToArray(columns, TypedField.class));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					IOUtils.closeQuietly(inputStream);
				}
			}
			Map<String, String> merchColums = setMerchColums();
			for (Map<String, Object> map : maps) {
				String jsonStr = JsonUtil.toJson(map);
				for (String key : merchColums.keySet()) {
					jsonStr = jsonStr.replaceAll(key, merchColums.get(key));
				}
				MerchantDto merchantDto = JsonUtil.fromJson(jsonStr, MerchantDto.class);
				merchants.add(merchantDto);
			}
			Boolean flag = true;
			if (flag == true) {
				fileResult.message = "导入成功!";
			} else {
				fileResult.message = "导入失败";
				fileResult.type = Result.Type.error;
			}
		} else {
			fileResult.type = Result.Type.warn;
			fileResult.message = "上传的文件类型错误！";
		}
		return fileResult;
	}

	/**
	 * 设置导入导出列信息
	 * 
	 * @author 廖晓远
	 * @date 2015-5-23 下午4:25:23
	 * 
	 * @return
	 */
	List<TypedField> setXlsColums() {
		// 列配置信息
		List<TypedField> columns = new ArrayList<TypedField>();
		TypedField col = new TypedField("商户编号", "num");
		columns.add(col);

		col = new TypedField("商户昵称", "str");
		columns.add(col);

		col = new TypedField("商户姓名", "str");
		columns.add(col);

		col = new TypedField("手机号码", "str");
		columns.add(col);

		col = new TypedField("身份证号", "str");
		columns.add(col);

		col = new TypedField("银行名称", "str");
		columns.add(col);

		col = new TypedField("银行账户", "str");
		columns.add(col);

		col = new TypedField("开户名", "str");
		columns.add(col);

		col = new TypedField("是否可用", "bool", "是/否");
		columns.add(col);

		col = new TypedField("注册时间", "date", "yyyy'年'M'月'd'日'H'点'm'分'");
		columns.add(col);

		col = new TypedField("备注", "str");
		columns.add(col);

		return columns;
	}

	/**
	 * 设置导入匹配字段
	 * 
	 * @author 廖晓远
	 * @date 2015-5-23 下午6:47:35
	 * 
	 * @return
	 */
	Map<String, String> setMerchColums() {
		// 列配置信息
		Map<String, String> columns = new HashMap<String, String>();
		columns.put("商户编号", "id");
		columns.put("商户昵称", "nickName");
		columns.put("商户姓名", "realName");
		columns.put("手机号码", "phoneNo");
		columns.put("身份证号", "idCertNo");
		columns.put("银行名称", "bankName");
		columns.put("银行账户", "acctNo");
		columns.put("开户名", "acctName");
		columns.put("是否可用", "disabled");
		columns.put("注册时间", "regTime");
		columns.put("备注", "memo");

		return columns;
	}

	public MerchantService getMerchantService() {
		return WebEnvHelper.getSpringBean(MerchantService.class);
	}
}
