package com.peng.idea.plugin.builder.util.psi;

import com.intellij.lang.jvm.JvmMethod;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Objects;

import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/23 1:18
 * </pre>
 */
public class BuilderFinderUtil {

    public static final String SEARCH_PATTERN = "Builder";
    public static final String EMPTY_STRING = "";

    public static PsiClass findBuilderForClass(PsiClass psiClass) {
        if (isNull(psiClass))
            return null;
        PsiClass innerBuilderClass = tryFindInnerBuilder(psiClass);
        if (innerBuilderClass != null) {
            return innerBuilderClass;
        } else {
            String searchName = psiClass.getName() + SEARCH_PATTERN;
            return findClass(psiClass, searchName);
        }
    }

    private static PsiClass tryFindInnerBuilder(PsiClass psiClass) {
        PsiClass innerBuilderClass = null;
        PsiClass[] allInnerClasses = psiClass.getAllInnerClasses();
        for (PsiClass innerClass : allInnerClasses) {
            if (Objects.requireNonNull(innerClass.getName()).contains(SEARCH_PATTERN)) {
                innerBuilderClass = innerClass;
                break;
            }
        }
        return innerBuilderClass;
    }

    public static PsiClass findClassForBuilder(PsiClass psiClass) {
        if (isNull(psiClass))
            return null;
        String searchName = Objects.requireNonNull(psiClass.getName()).replaceFirst(SEARCH_PATTERN, EMPTY_STRING);
        return findClass(psiClass, searchName);
    }

    public static PsiClass findClass(PsiClass psiClass, String searchName) {
        PsiClass result = null;
        if (typeIsCorrect(psiClass)) {
            result = ClassFinderUtil.findClass(searchName, psiClass.getProject());
        }

        return result;
    }

    private static boolean typeIsCorrect(PsiClass psiClass) {
        return !psiClass.isAnnotationType() && !psiClass.isEnum() && !psiClass.isInterface();
    }

    public static void main(String[] args) {
        System.out.println("over");
    }
}
