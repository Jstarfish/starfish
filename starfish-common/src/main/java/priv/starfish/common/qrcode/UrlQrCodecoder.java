package priv.starfish.common.qrcode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import priv.starfish.common.util.StrUtil;

public class UrlQrCodecoder {
	private static int DEFAULT_WIDTH = 280;
	private static int DEFAULT_HEIGHT = 280;
	// 二维码的图片格式
	private static String DEFAULT_FORMAT = "png";

	public static void encodeToStream(String url, int width, int height, String format, OutputStream stream) throws IOException, WriterException {
		if (!StrUtil.hasText(format)) {
			format = DEFAULT_FORMAT;
		}
		if (width < 100) {
			width = DEFAULT_WIDTH;
		}
		if (height < 100) {
			height = DEFAULT_HEIGHT;
		}
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 内容所使用编码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置边白
		hints.put(EncodeHintType.MARGIN, 1);

		BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
		// 生成二维码
		MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
	}
}
