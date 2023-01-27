package com.peng.idea.plugin.builder.util;

import java.util.Optional;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2021/7/5 0:54
 *  </pre>
 */
public class ObjectUtil {

    public static <T> Optional<T> safeObject(T t) {
        return t == null ? Optional.empty() : Optional.of(t);
    }
}
