/**
 * Create time 2012-5-8 上午9:27:02
 */
package priv.starfish.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 * 生成图形验证码
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @version 1.0
 */
public class CaptchaUtil {

	private static final Character[] CODES = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final int TEXT_LEN = 4;
	private static final int IMAGE_WIDTH = 60;
	private static final int IMAGE_HEIGHT = 20;
	private static final String FONT_FAMILY = "Times New Roman";

	/**
	 * 生成随机验证码
	 * 
	 * @return 验证码
	 */
	public static String createText() {
		Random random = new Random();
		StringBuilder text = new StringBuilder(TEXT_LEN);

		for (int i = 0; i < TEXT_LEN; i++) {
			Character c = CODES[random.nextInt(62)];
			text.append(c);
		}

		return text.toString();
	}

	/**
	 * 根据给定的验证码，生成验证码图片
	 * 
	 * @param 验证码文本
	 * @return 验证码图片
	 */
	public static BufferedImage createImage(String text) {
		Random random = new Random();
		BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		// 配置图片的基本信息
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		g.setFont(new Font(FONT_FAMILY, Font.PLAIN, 18));
		g.setColor(getRandColor(160, 200));

		// 生成干扰线
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(IMAGE_WIDTH);
			int y = random.nextInt(IMAGE_HEIGHT);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 绘制验证码字符
		for (int i = 0; i < TEXT_LEN; i++) {
			String rand = String.valueOf(text.charAt(i));
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}
		g.dispose();

		return image;
	}

	/*
	 * 生成随机颜色
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
