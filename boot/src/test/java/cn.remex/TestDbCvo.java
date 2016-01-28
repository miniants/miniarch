package cn.remex;

import cn.remex.core.util.Assert;
import cn.remex.core.util.StringHelper;

/**
 * Created by yangy on 2016/1/17 0017.
 */
public class TestDbCvo {
        public static void main(String... args) {
            final boolean[] end = {false};
            String regx = "([A-Za-z_][A-Za-z\\d_]*)$|(([A-Za-z_][A-Za-z\\d_]*)\\.)|(([A-Za-z_][A-Za-z\\d_]*)\\[\\]\\.)";
            String columnExprs = "aaa.bbb[].ccc1.111";
            StringHelper.forEachMatch(columnExprs, regx, (m, b) -> {
                String baseField = m.group(1), objField = m.group(3), listField = m.group(5);
                System.out.println(baseField + "_" + objField + "_" + listField + "_");
                end[0] = b;
            });
            Assert.isTrue(end[0], "表达式不合法，只允许name.   name[].  name 三种格式!");
        }
}
