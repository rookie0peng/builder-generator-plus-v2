package com.peng.idea.plugin.builder.util;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/3 2:01
 * </pre>
 */
public class BooleanUtil {

    public static boolean nullToFalse(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean nullToTrue(Boolean value) {
        return value == null || value;
    }
}
