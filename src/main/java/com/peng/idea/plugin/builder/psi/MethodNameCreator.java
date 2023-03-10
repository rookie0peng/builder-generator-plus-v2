package com.peng.idea.plugin.builder.psi;

import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:30
 * </pre>
 */
public class MethodNameCreator {

    public String createMethodName(String methodPrefix, String fieldName) {
        if (isEmpty(methodPrefix)) {
            return fieldName;
        } else {
            String fieldNameUppercase = capitalize(fieldName);
            return methodPrefix + fieldNameUppercase;
        }
    }
}
