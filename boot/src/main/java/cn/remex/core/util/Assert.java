/*
 * 文 件 名 : Assert.java
 * CopyRright (c) since 2012:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2012-10-18
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */
package cn.remex.core.util;

import cn.remex.RemexConstants;
import cn.remex.core.exception.NestedException;

import java.util.Collection;
import java.util.Map;

/**
 * 本断言类用于在运行时检查异常和错误。
 * 例如，如果一个公共方法，它的一行代码中的某个变量不能为空，此类可以用来验证。<br>
 * 这个类是类似的断言主张。 如果一个变量值被视为无效， （通常）就会抛出一个
 * <code>IllegalArgumentException</code> 。
 * 比如说,Assert.notNull（obj）
 *
 * @author Hengyang Liu yangyang8599@163.com
 * @since 2012-5-18
 */

public abstract class Assert implements RemexConstants {

    //空检测系列方法
    public static void isNull(final Object object, final String errorCode, final String errorMsg) {
        if (object != null) {
            throw new NestedException(errorCode, errorMsg);
        }
    }

    public static void isNull(final Object object, final String errorCode, final String errorMsg, final Class<? extends RuntimeException> exceptionClassForThrow) {
        if (object != null) {
            throw createRuntimeException(errorCode, errorMsg, exceptionClassForThrow);
        }
    }

    public static void notNull(final Object object, final String errorCode, final String errorMsg) {
        if (object == null) {
            throw new NestedException(errorCode, errorMsg);
        }
    }

    public static void notNull(final Object object, final String errorCode, final String errorMsg, final Class<? extends RuntimeException> exceptionClassForThrow) {
        if (object == null) {
            throw createRuntimeException(errorCode, errorMsg, exceptionClassForThrow);
        }
    }
    public static void notNullAndEmpty(final Object stringOrObject,  final String errorCode, final String errorMsg) {
        if (stringOrObject == null || (stringOrObject instanceof String && "".equals(((String) stringOrObject).trim()))) {
            throw new NestedException(errorCode, errorMsg);
        }
    }
    public static void nullOrEmpty(final Object stringOrObject,  final String errorCode, final String errorMsg) {
        if (stringOrObject != null && (stringOrObject instanceof String && !"".equals(((String) stringOrObject).trim()))) {
            throw new NestedException(errorCode, errorMsg);
        }
    }

    //逻辑判断系列方法
    public static void isTrue(final boolean expression, final String errorCode, final String errorMsg) {
        if (!expression) {
            throw new NestedException(errorCode, errorMsg);
        }
    }
    public static void isTrue(final boolean expression, final String errorCode, final String message, final Class<? extends RuntimeException> exceptionClassForThrow) {
        if (!expression) {
            throw createRuntimeException(errorCode, message, exceptionClassForThrow);
        }
    }


    //特殊场景用途的断言方法
    public static void isSimpleBean(final Object object, final String errorCode, final String errorMsg) {
        if (object instanceof Map || object instanceof Collection) {
            throw new NestedException(errorCode, errorMsg);
        }
    }




    //私有方法
    private static RuntimeException createRuntimeException(String errorCode, final String message, final Class<? extends RuntimeException> exceptionClassForThrow) {
        try {
            if (NestedException.class.isAssignableFrom(exceptionClassForThrow))
                return exceptionClassForThrow.getConstructor(String.class, String.class).newInstance(message);
            else
                return exceptionClassForThrow.getConstructor(String.class).newInstance(message);

        } catch (Exception e) {
            return new IllegalArgumentException("自定义异常类" + exceptionClassForThrow.getName() + "无法构建，调用默认异常IllegalArgumentException，处理断言。此处异常信息为：" + message);
        }
    }
}
