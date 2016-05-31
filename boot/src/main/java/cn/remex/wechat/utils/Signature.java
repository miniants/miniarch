package cn.remex.wechat.utils;

import cn.remex.wechat.config.WeChatConfig;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by guoqi on 2016/3/21.
 */
public class Signature {
    	/**
	 * 生成sign MD5 加密 toUpperCase
	 * @param map
	 * @param paternerKey
	 * @return
	 */
	public static String generateSign(Map<String, String> map,String paternerKey){
		Map<String, String> tmap = MapUtil.order(map);
		if(tmap.containsKey("sign")){
			tmap.remove("sign");
		}
		String str = MapUtil.mapJoin(tmap, false, false);
		return DigestUtils.md5Hex(str+"&key="+paternerKey).toUpperCase();
	}

	/**
	 * 验证
	 * @param map
	 * @param key
     * @return
     */
	public static boolean validateSign(Map<String,String> map,String key){
		return map.get("sign").equals(generateSign(map,key));
	}

}
