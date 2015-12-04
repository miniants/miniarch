package zbh.aibc;

import java.util.Random;

public class RandomUtils {

    private static Random ran = new Random();

    /**
     * 产生一个指定位数的随机码(由数字或大小写字母构成)
     * @param randomCodeLen 指定位数
     * @param hasLetter 是否包括字母,true包括,false不包括
     * @return 随机码
     * @author zhangaiguo
     */
    public static String createRandomCode(int randomCodeLen, boolean hasLetter){
        // 存放结果
        StringBuilder result = null;
        if(randomCodeLen >= 1){
            result = new StringBuilder();
            if(hasLetter){
                for(int i = 0; i < randomCodeLen; i++){
                    // 产生0-2的随机数
                    int random = ran.nextInt(3);
                    switch(random){
                        case 0:
                            // 数字
                            result.append(createRandomNum());
                            break;
                        case 1:
                            // 小写字母
                            result.append(createRandomLowerLetter());
                            break;
                        case 2:
                            // 大写字母
                            result.append(createRandomUpperLetter());
                            break;
                    }
                }
            }else if(!hasLetter){
                for(int i = 0; i < randomCodeLen; i++){
                    result.append(createRandomNum());
                }
            }
            return result.toString();
        }
        return null;
    }

    /**
     * 产生0-9的随机数
     * @return 0-9
     * @author zhangaiguo
     */
    private static String createRandomNum(){
        return String.valueOf(ran.nextInt(10));
    }

    /**
     * 产生a-z中任意小写字母
     * @return a-z,任意字母
     * @author zhangaiguo
     * @date 2013-11-28 下午3:13:38
     */
    private static String createRandomLowerLetter(){
        // char类型a，转换为byte，数值为97
        int a = 97;
        // 97+26位随机数，小写字母byte范围
        int random = a + ran.nextInt(26);
        return String.valueOf((char)(byte)random);
    }

    /**
     * 产生A-Z中任意大写字母
     * @return A-Z中任意字母
     * @author zhangaiguo
     */
    private static String createRandomUpperLetter(){
        // char类型A，转换为byte，数值为65
        byte A = 65;
        // 65+26位随机数，小写字母byte范围
        int random = A + ran.nextInt(26);
        return String.valueOf((char)(byte)random);
    }

}
