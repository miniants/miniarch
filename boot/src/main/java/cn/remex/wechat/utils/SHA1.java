package cn.remex.wechat.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by guoqi on 2016/2/27.
 */
public class SHA1 {
    /**
     * 串接arr参数，生成sha1 digest
     * @param arr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String gen(String... arr) throws NoSuchAlgorithmException {
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String a : arr) {
            sb.append(a);
        }
        return DigestUtils.shaHex(sb.toString());
    }
}
