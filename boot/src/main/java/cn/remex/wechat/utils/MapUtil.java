package cn.remex.wechat.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by guoqi on 2016/3/21.
 */
public class MapUtil {
    /**
     * Map key 排序
     * @param map
     * @return
     */
    public static Map<String,String> order(Map<String, String> map){
        HashMap<String, String> tempMap = new LinkedHashMap<>();
        List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
        Collections.sort(infoIds, (o1, o2) -> (o1.getKey()).compareTo(o2.getKey()));
        for (Map.Entry<String, String> item : infoIds) {
            tempMap.put(item.getKey(), item.getValue());
        }
        return tempMap;
    }


    /**
     * url 参数串连
     * @param map
     * @param keyLower
     * @param valueUrlencode
     * @return
     */
    public static String mapJoin(Map<String, String> map,boolean keyLower,boolean valueUrlencode){
        StringBuilder stringBuilder = new StringBuilder();
        map.keySet().stream().filter(key -> map.get(key) != null && !"".equals(map.get(key))).forEach(key -> {
            try {
                String temp = (key.endsWith("_") && key.length() > 1) ? key.substring(0, key.length() - 1) : key;
                stringBuilder.append(keyLower ? temp.toLowerCase() : temp)
                        .append("=")
                        .append(valueUrlencode ? URLEncoder.encode(map.get(key), "utf-8").replace("+", "%20") : map.get(key))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        if(stringBuilder.length()>0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }

}
