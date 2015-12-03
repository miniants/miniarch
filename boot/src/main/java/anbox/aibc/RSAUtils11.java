package anbox.aibc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import Decoder.BASE64Encoder;
import cn.remex.exception.RIOException;
  
/**rsa加解密类
 * @author zhangaiguo
 * @since 2015-01-13
 */
public class RSAUtils11{
	final static int key_size = 1024;//块加密的大小，可以更改，但是不要太大，否则效率会低
	static Cipher cipher;
	static{
		
		try {
			cipher= Cipher.getInstance("RSA",new BouncyCastleProvider());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateKeyPair(String pubPath,String priPath){
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",new BouncyCastleProvider());
			keyPairGen.initialize(key_size);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();//公钥
			RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();//密钥	
			saveKeyPair(pubPath,publicKey);
			saveKeyPair(priPath,privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Object getKeyPair(String path){
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			ObjectInputStream oos = new ObjectInputStream(fis);
			Object obj= oos.readObject();
			oos.close();
			fis.close();
			return obj;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void saveKeyPair(String path,Object kp){
		 FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			//保存密钥
			oos.writeObject(kp);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * * 生成公钥 *
	 * @param modStr *模
	 * @param pubExpStr * 指数
	 * @return RSAPublicKey *
	 * @throws exception
	 */
	public static RSAPublicKey generaterRsaPublicKey(String modStr, String pubExpStr){
		KeyFactory keyFac;
		try {
			byte[] modulus = modStr.getBytes();
			byte[] publicexponent = pubExpStr.getBytes();
			keyFac = KeyFactory.getInstance("RSA",new BouncyCastleProvider());
			RSAPublicKeySpec pubKeysSec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicexponent));
			return (RSAPublicKey) keyFac.generatePublic(pubKeysSec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * * 生成私钥 *
	 * @param modStr *
	 * @param priExpStr *
	 * @return RSAPrivateKey *
	 */
	public static RSAPrivateKey generateRsaPrivateKey(String modStr,String priExpStr){
		KeyFactory keyFac;
		try {
			byte[] modulus = modStr.getBytes();
			byte[] privateExponent = priExpStr.getBytes();
			keyFac = KeyFactory.getInstance("RSA",new BouncyCastleProvider());
			RSAPrivateKeySpec prikeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
			return (RSAPrivateKey) keyFac.generatePrivate(prikeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * * 加密 *
	 * @param path 加密的密钥路径 *
	 * @param plainText 待加密的明文数据 *
	 * @return 加密后的数据 *
	 */
	public static String encrypt(String path, String plainText){
		RSAPublicKey publicKey = (RSAPublicKey) getKeyPair(path);
        try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 模长
			int key_len = publicKey.getModulus().bitLength() / 8;
			// 加密数据长度 <= 模长-11
			String[] datas = splitString(plainText, key_len - 11);
			String mi = "";
			//如果明文长度大于模长-11则要分组加密
			for (String s : datas) {
				mi += bcd2Str(cipher.doFinal(s.getBytes()));
			}
			return mi;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;
  
        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
  
            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }
	
	 /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*len, i*len+y);
            }else{
                str = string.substring(i*len, i*len+len);
            }
            strings[i] = str;
        }
        return strings;
    }
	
	
	/**
	 * * 解密 *
	 * @param path 解密的密钥路径 *
	 * @param raw 已经加密的数据 *
	 * @return 解密后的明文 *
	 */
	public static String decrypt(String path, String raw){
		RSAPrivateKey privateKey = (RSAPrivateKey) getKeyPair(path);
        try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			//模长   
			int key_len = privateKey.getModulus().bitLength() / 8;
			byte[] bytes = raw.getBytes();
			byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
//        System.err.println(bcd.length);
			//如果密文长度大于模长则要分组解密
			String ming = "";
			byte[][] arrays = splitArray(bcd, key_len);
			for(byte[] arr : arrays){ 
				ming += new String(cipher.doFinal(arr));
			}
			return ming;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
     *拆分数组
     */
    public static byte[][] splitArray(byte[] data,int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[len];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }
	 /**
     * ASCII码转BCD码
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }
    
    public static byte asc_to_bcd(byte asc) {
        byte bcd;
        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }
	
	 /**获取公钥和私钥的字符串
     * @param key
     * @return
     */
	public static String getKeyString(Key key){
    	byte[] keyBytes = key.getEncoded();
		return (new BASE64Encoder()).encode(keyBytes);
    }
	
	 public static String getFile(String path){
    	InputStream is = RSAUtils11.class.getResourceAsStream(path);

		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(ir);
		String line;
		StringBuffer content = new StringBuffer();
		try {
//				line = br.readLine();
			while (null != (line = br.readLine())) {
				content.append(line);
//					content.append('\n');
			}
		} catch (IOException e) {
			throw new RIOException("文件读取异常！", e);
		}

		return content.toString();
    }
	 public static void main(String[] args) {
//		 String aaa = encrypt(AiwbConsts.Rsa_PublicKeyPath,"hello 邦苏");
		 String aaa = encrypt(AiwbConsts.Rsa_PublicKeyPath,"qwer");
//		 System.out.println(aaa.length());
		 System.out.println(aaa);
////			String fdsa="7AC1BBCFEEB77C523EEF6B2E37155FF78B5031AD82946DA80EF0663A973BD27DCB845D031715B23109B89F270C4EA6829004AA981192EEB5D40405D5013A03B2D3A5729F798193B220F60F8C8A4295278B40500760FE1C70FC9608A9CB741714D7DED51B5EB9B403D55954EB49E03405462EDD8339B7992F241CD8FD50B7DA5B";
		String fdsa="436ccfe2f0fe67f031c01553a766bd0383084d5e3fa0282d55fdc49acd6d533fd5b3f46ea266ab1a628c183127d194069d60c1aacab7ca60fec146ad74b7576c28990236bbeb5216ff96b2febae89ac58fc15af15ccf28fc2e3727d1a4bcdab3cc5e8d742de02d9274534b8a7a2597bae7fad67bdaac99fb78aa9a846bdea480";
		String sss = decrypt(AiwbConsts.Rsa_PrivateKeyPath, fdsa);
////			String sss = decrypt(AiwbConsts.Rsa_PrivateKeyPath, aaa);
		System.out.println(sss);
//		generateKeyPair(AiwbConsts.Rsa_PublicKeyPath,AiwbConsts.Rsa_PrivateKeyPath);
		 
	}
}
