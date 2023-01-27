package com.peng.idea.plugin.builder.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/20 17:02
 * </pre>
 */
public class BuildMethodFinderUtil {

    private static final String METHOD_NAME = "builder";

//    public static List<PsiMethod> findBuilderMethod(PsiClass psiBuilderClass) {
//        JvmMethod[] jvmMethods = psiBuilderClass.findMethodsByName(METHOD_NAME);
//        return Stream.of(jvmMethods)
//                .filter(jvmMethod -> jvmMethod instanceof PsiMethod)
//                .map(jvmMethod -> (PsiMethod) jvmMethod)
//                .collect(Collectors.toList());
//    }

    public static List<PsiMethod> findBuilderMethodV2(PsiClass psiBuilderClass) {
        PsiMethod[] psiMethods = psiBuilderClass.findMethodsByName(METHOD_NAME, false);
        return Stream.of(psiMethods)
                .collect(Collectors.toList());
    }
}
