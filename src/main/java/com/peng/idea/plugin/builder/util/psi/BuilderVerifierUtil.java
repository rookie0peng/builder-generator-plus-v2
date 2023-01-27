package com.peng.idea.plugin.builder.util.psi;

import com.intellij.psi.PsiClass;

import java.util.Objects;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/23 2:11
 * </pre>
 */
public class BuilderVerifierUtil {

    private static final String SUFFIX = "Builder";

    public static boolean isBuilder(PsiClass psiClass) {
        return Objects.requireNonNull(psiClass.getName()).endsWith(SUFFIX);
    }
}
