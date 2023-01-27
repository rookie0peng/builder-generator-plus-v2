package com.peng.idea.plugin.builder.util;

import static java.util.Objects.*;

/**
 * @description:
 * @author: qingpeng
 * @date: 2023/1/27 21:03
 */
public class IntegerUtil {

    public static int nullToZero(Integer num) {
        return isNull(num) ? 0 : num;
    }
}
