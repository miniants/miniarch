package zbh.aibc.bl;

import java.security.interfaces.RSAPublicKey;

import zbh.aibc.AiwbConsts;
import zbh.aibc.RSAUtils11;
import zbh.aibc.appBeans.login.KeyCvo;
import zbh.aibc.appBeans.login.KeyRvo;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.aibc.AiwbConsts;
import zbh.aibc.RSAUtils11;
import zbh.aibc.appBeans.login.KeyCvo;
import zbh.aibc.appBeans.login.KeyRvo;
import zbh.aibc.appBeans.login.LoginExtend;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

/**前端登录注册时获取rsa加密的公钥
 * @author zhangaiguo
 *
 */
@BsAnnotation
public class KeyObtainBs  implements AiwbConsts {
	@BsAnnotation(bsCvoBodyClass=KeyCvo.class,bsRvoBodyClass=KeyRvo.class)
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		KeyCvo keyCvo = new KeyCvo();
		keyCvo.getUsername();
		
		//获取公钥和modulus
		RSAPublicKey publicKey = (RSAPublicKey) RSAUtils11.getKeyPair(Rsa_PublicKeyPath);
		String publicKeyStr = RSAUtils11.getKeyString(publicKey);
		String modStr = publicKey.getModulus().toString(16);
			
		if("".equals(publicKeyStr)){
			bsRvo.setExtend(new LoginExtend(false, "未查到密钥信息，请联系技术人员!!"));
			return bsRvo;
		}
		if("".equals(modStr)){
			bsRvo.setExtend(new LoginExtend(false, "未查到modulus信息，请联系技术人员!!"));
			return bsRvo;
		}
		KeyRvo keyRvo = new KeyRvo();
		keyRvo.setKey(publicKeyStr);
		keyRvo.setMod(modStr);
		keyRvo.setStatus(true);
		bsRvo.setBody(keyRvo);
		bsRvo.setExtend(new LoginExtend(true, "秘钥查询完毕!"));
		return bsRvo;
	}
}