package com.peng.idea.plugin.builder.util.psi;

import com.intellij.psi.PsiClass;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;

import java.util.Objects;

import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/23 2:11
 * </pre>
 */
public class BuilderVerifierUtil {

    public static boolean isBuilder(PsiClass psiClass) {
        return nonNull(psiClass) && nonNull(psiClass.getName()) && psiClass.getName().endsWith(BuilderConstant.BUILDER_SUFFIX);
    }

    public static boolean nonBuilder(PsiClass psiClass) {
        return !isBuilder(psiClass);
    }
}
