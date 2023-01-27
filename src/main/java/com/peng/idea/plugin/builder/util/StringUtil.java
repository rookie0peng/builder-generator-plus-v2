package com.peng.idea.plugin.builder.util;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2020/10/31 15:09
 *  </pre>
 */
public class StringUtil {

    private volatile static StringUtil instance;
    private StringUtil() {}
    public static StringUtil getInstance() {
        if (instance == null) {
            synchronized (StringUtil.class) {
                if (instance == null) {
                    instance = new StringUtil();
                }
            }
        }
        return instance;
    }

    public static String objectToString(Object value) {
        return value == null ? null : value.toString();
    }

    public static String emptyToNull(String value) {
        if (value == null || value.isEmpty())
            return null;
        return value;
    }

    public static String nullToEmpty(String value) {
        if (value == null)
            return "";
        return value;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
