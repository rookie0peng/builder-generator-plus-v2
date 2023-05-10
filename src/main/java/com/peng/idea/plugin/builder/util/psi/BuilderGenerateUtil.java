package com.peng.idea.plugin.builder.util.psi;

import com.peng.idea.plugin.builder.util.constant.BuilderConstant;

import java.util.Locale;

import static com.intellij.openapi.util.text.StringUtil.isVowel;
import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/11
 * </pre>
 */
public class BuilderGenerateUtil {

    public static String builderMethodName(String srcClassName) {
        if (isNull(srcClassName))
            return null;
        String prefix = isVowel(srcClassName.toLowerCase(Locale.ENGLISH).charAt(0)) ? BuilderConstant.AN_PREFIX : BuilderConstant.A_PREFIX;
        return prefix + srcClassName;
    }
}
