package cn.remex.core.util;

import cn.remex.RemexConstants;
import cn.remex.core.exception.FileException;
import cn.remex.core.exception.RIOException;

import java.io.*;
/**
 * 文件处理工具类
 */
public class FileHelper {
	private static final int BUFFER_SIZE = 16 * 1024;
	/**
	 * 复制文件
	 * @param src 原地址
	 * @param dst 目的地址
	 */
	public static void copy(final File src, final File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 将文件从src复制到dst
	 * 
	 * @param src 可以是文件名或者File.class的实例
	 * @param dst 可以是文件名或者File.class的实例
	 */
	public static void copy(final Object src, final Object dst) {
		Assert.isTrue(null!=src && null!=dst, "要复制的文件必须为File.class类型或者String.class类型，且不能为空！");
		File srcFile = src instanceof String?createFile((String) src):src instanceof File?(File)src:null;
		File dstFile = dst instanceof String?createFile((String) dst):dst instanceof File?(File)dst:null;
		Assert.isTrue(null!=src && null!=dst, "要复制的文件必须为File.class类型或者String.class类型，且不能为空！");
		
		copy(srcFile, dstFile);
	}
	
	/**
	 * 判断是否创建文件路径,不存在则创建
	 * @param truePath	文件路径
	 * @return boolean 布尔值
	 * @throws IOException
	 */
	public static boolean createFilePath(String truePath){
		return createFile(truePath).exists();
	}
	
	
	/**
	 * 如果文件不存在则创建文件/目录。
	 * 如果存在则返回。
	 * 
	 * 路径中默认使用File.separatorChar分隔，也可以采用“/”
	 * 
	 * @param truePath
	 * @return
	 */
	public static File createFile(String truePath){
		String finalPath = truePath.toString();int i =-1;
		String finalContentPath = finalPath.substring(0, (i=finalPath.lastIndexOf(File.separatorChar))>=0?i:finalPath.lastIndexOf("/"));
		File contentPath = new File(finalContentPath);
		
		if (!(contentPath.exists())) {
			contentPath.mkdirs();
		}
		
		if(!(contentPath.exists()))
			throw new FileException("文件路径创建失败！");
		
		File myFilePath = new File(finalPath);
		if (!(myFilePath.exists())) {
			try {
				myFilePath.createNewFile();
			} catch (IOException e) {
				throw new FileException("文件创建失败!", e);
			}
		}
		
		return myFilePath;
	}
	
	
	
	
	
	/**
	 * 保存文件内容
	 * @param fileName 文件名
	 * @param fileContent	文件内容
	 */
	public static void saveFileContent(String fileName, Object fileContent) {
		saveFileContent(fileName, fileContent, null);
	}
	/**
	 * 设置编码方式,保存文件内容
	 * @param fileName	文件名
	 * @param fileContent 文件内容
	 * @param charset 字符集
	 */
	public static void saveFileContent(String fileName, Object fileContent,String charset) {
		BufferedOutputStream bosFile = null;
		try {
			// 创建失败
			createFilePath(fileName);
			// 创建成功
			bosFile = new BufferedOutputStream(new FileOutputStream(fileName));
			// 判断传入的对象类型 如果是String 则直接保存 其他的则认为是JAXB对象
			if (fileContent instanceof String) {
				bosFile.write(null==charset?fileContent.toString().getBytes():fileContent.toString().getBytes(charset));
			}
			// 如果是流文件
			else if (fileContent instanceof ByteArrayOutputStream) {
				((ByteArrayOutputStream) fileContent).writeTo(bosFile);
				// }
				// JAXB对象
				// else
				// if(fileContent.getClass().isAnnotationPresent(XmlRootElement.class)){
				// ByteArrayOutputStream xmlbyteos =
				// marshall(fileContent.getClass(), fileContent);
				// xmlbyteos.writeTo(bosFile);
			} else {

			}
			bosFile.close();
			if (RemexConstants.logger.isDebugEnabled()) {
				StringBuilder msg = new StringBuilder("文件保存成功，路径：【").append(fileName).append("】");
				RemexConstants.logger.debug(msg);
			}
		} catch (Exception e) {
			String msg = new StringBuilder("保存报文失败!报文路径是:").append(fileName).toString();
			RemexConstants.logger.error(msg, e);
			throw new RIOException(msg, e);
		} finally {
			if (bosFile != null) {
				try {
					bosFile.close();
				} catch (IOException e) {
					RemexConstants.logger.error(e);
				}
			}
		}
	}

	/**
	 * 根据文件名获取文件内容,默认编码方式为GBK
	 * @param fileName 文件名
	 * @return String 文件内容
	 */
	public static String getFileContent(String fileName) {
		return getFileContent(fileName, "GBK");
	}
	/**
	 * 设置编码方式, 根据文件名获取文件内容,默认编码方式为GBK
	 * @param fileName 文件名
	 * @param charset 字符集
	 * @return String 文件内容
	 */
	public static String getFileContent(String fileName,String charset) {
		FileInputStream fis;
		String tresult = null;
		try {
			fis = new FileInputStream(fileName);
			int size = fis.available();
			byte[] b = new byte[size];
			fis.read(b);
			fis.close();
			tresult = new String(b, charset);
		} catch (Exception e) {
			throw new RIOException("文件读取异常！", e);
		}
		return tresult;
	}

	/**
	 * 读取PropertiesFile里面的内容，以String的方式返回其内容。
	 * @param o
	 * @param propertyFile
	 *            再o对象的路径的文件名
	 * @return String 文件内容
	 */
	public static String getFileContent(final Object o, final String propertyFile) {
		InputStream is = o.getClass().getResourceAsStream(propertyFile);

		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		String line;
		StringBuffer content = new StringBuffer();
		try {
			while((line = br.readLine())!=null){
				content.append(line);
//				content.append('\n');
			}
		} catch (IOException e) {
			throw new RIOException("文件读取异常！", e);
		}

		return content.toString();

	}

}
