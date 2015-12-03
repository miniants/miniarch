package anbox.aibc.bl;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;

import anbox.aibc.AiwbConsts;
import anbox.aibc.appBeans.AncarExtend;
import anbox.aibc.appBeans.security.ValidateCode;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.core.CoreSvo;
import cn.remex.util.Judgment;


/**
 * 生成验证码
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class ValidateCodeBs implements AiwbConsts {
	// 放到session中的key,这个值不是固定的,可以自己随便写
		public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";
		private Random random = new Random();
	// 随机产生的字符串
		private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private int width = 80;// 图片宽
		private int height = 30;// 图片高
		private int lineSize = 40;// 干扰线数量
		private int stringNum = 4;// 随机产生字符数量

	@BsAnnotation(bsRvoBodyClass = ValidateCode.class,bsRvoExtendClass = AncarExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		ValidateCode validateCode = new ValidateCode();
		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		// 绘制随机字符
		String randomString = "";
		for (int i = 1; i <= stringNum; i++) {
			randomString = drowString(g, randomString, i);
		}
		setSessionCode(randomString);
		g.dispose();
		//用来存放字节 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageOutputStream ios = null;
		InputStream input = null;
		byte[] b = new byte[1024*500];
		try {
			ios = ImageIO.createImageOutputStream(baos);
			// 将内存中的图片通过流动形式输出到客户端
			ImageIO.write(image, "JPG", ios);
			input = new ByteArrayInputStream(baos.toByteArray());
			input.read(b);
			ios.close();
			input.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}
		validateCode.setImgByteStream(b);
		bsRvo.setBody(b);
		
		bsRvo.setExtend(new AncarExtend(true,"OK"));
		return bsRvo;
		
		//判断是否与输入的验证码相等时用到
		//String a =getSessionRandomString();
	}
	
	/*获得字体 */
//	private Font getFont() {
//		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
//	}

	/*获得颜色*/
	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}
	
	/**生成随机图片*/
	public void getRandcode(HttpServletRequest request,HttpServletResponse response) {
//		HttpSession session = request.getSession();
//		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
//		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
//// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
//		Graphics g = image.getGraphics();
//		g.fillRect(0, 0, width, height);
//		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
//		g.setColor(getRandColor(110, 133));
//		// 绘制干扰线
//		for (int i = 0; i <= lineSize; i++) {
//			drowLine(g);
//		}
//		// 绘制随机字符
//		String randomString = "";
//		for (int i = 1; i <= stringNum; i++) {
//			randomString = drowString(g, randomString, i);
//		}
//		session.removeAttribute(RANDOMCODEKEY);
//		session.setAttribute(RANDOMCODEKEY, randomString);
//		System.out.println(randomString);
//		g.dispose();
//		try {
//			// 将内存中的图片通过流动形式输出到客户端
//			ImageIO.write(image, "JPEG", response.getOutputStream());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	/*绘制字符串 */
	private String drowString(Graphics g, String randomString, int i) {
		g.setFont(new Font("Fixedsys", Font.CENTER_BASELINE, 18));
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
//		String rand = String.valueOf(getRandomString(random.nextInt(randString
//				.length())));
		String rand = String.valueOf(String.valueOf(randString.charAt(random.nextInt(randString
				.length()))));
		randomString += rand;
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(rand, 13 * i, 16);
		return randomString;
	}
	
	/** 绘制干扰线*/
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/** 获取随机的字符*/
//	public String getRandomString(int num) {
//		return String.valueOf(randString.charAt(num));
//	}
	
	//Action中直接调用Util包中的方法:
	public String getImg() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		// 设置相应类型,告诉浏览器输出的内容为图片
		response.setContentType("image/jpeg");
		// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		try {
			getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//验证输入验证码是否正确:
//	public String validateImg() throws IOException {
//		HttpServletRequest request = ServletActionContext.getRequest();
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setCharacterEncoding("UTF-8");
//		request.setCharacterEncoding("UTF-8");
//		// 在创建图片时,session的Attribute的值是由自己所设置，
//		String resultStr = (String) request.getSession().getAttribute("RANDOMVALIDATECODEKEY");
//		// 设置不区分大小写,同意转换成大写.
//		this.setValidateStr(validateStr.toUpperCase());
//		if (resultStr.equals(validateStr)) {
//			response.getWriter().write("{success:true,result:'成功'}");
//		} else {
//			response.getWriter().write("{success:false,result:'失败'}");
//		}
//		response.getWriter().flush();
//		response.getWriter().close();
//		return null;
//	}
		//验证码加密
		public static void setSessionCode(String randomString){
			long now = System.currentTimeMillis();
			//三段-分割的格式
			String st = new String(Base64.encodeBase64(randomString.getBytes()))
					+"-"+new String(Base64.encodeBase64(String.valueOf(now).getBytes()))
					+"-"+new String(Base64.encodeBase64(String.valueOf(now+randomString.hashCode()).getBytes()));
			st = st.replaceAll("=", "");
			//根据cookie取，cookie要加密 TODO
			CoreSvo.$SC("Code", randomString);
			CoreSvo.$SC("NowTime", String.valueOf(now));
			CoreSvo.$SC("CodeAndNT", st);
			
		}
		//获取session会话
		public static String getSessionRandomString(){
			//根据cookie取，cookie要加密 TODO
			String st = (String) CoreSvo.$VC("CodeAndNT");
			String ts = (String) CoreSvo.$VC("NowTime");
			final String code = (String) CoreSvo.$VC("Code");
			if(Judgment.nullOrBlank(st) || Judgment.nullOrBlank(code)){
				return null;
			}
			String[] sts = st.split("-");
			if(sts.length!=3)//三段-分割的格式
				return null;
			//第三段检测合法性
			long sts3 = Long.parseLong(new String(Base64.decodeBase64(sts[2])));
			long tsl = Long.parseLong(ts);
			if(tsl != sts3-code.hashCode()){
				return null;
			}
			
			return code; 
		}
	
	@Test
	public void test(){
		execute(new BsCvo() {
			@Override
			protected <T> T unpack(Class<T> clazz, String nodeName) {
				return null;
			}}, new BsRvo() {});
		
	}
	
}
