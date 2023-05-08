package com.peng.idea.plugin.builder.util.psi;

import com.intellij.lang.jvm.JvmMethod;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     * todo need to find inner class Builder
     * @param psiClass
     * @return
     */
    public static PsiClass findClassForBuilder(PsiClass psiClass) {
        if (isNull(psiClass))
            return null;
        String className = Optional.ofNullable(psiClass.getName()).orElse("");
        if (Objects.equals(className, SEARCH_PATTERN)) {
            PsiElement parentPsiElement = psiClass.getParent();
            if (parentPsiElement instanceof PsiClass parentPsiClass) {
                return parentPsiClass;
            } else {
                return findClassByBuildMethod(psiClass);
            }
        } else {
            if (!className.contains(SEARCH_PATTERN)) {
                return null;
            }
            String searchName = className.replaceFirst(SEARCH_PATTERN, EMPTY_STRING);
            return findClass(psiClass, searchName);
        }
    }

    public static PsiClass findClassByBuildMethod(PsiClass psiClass) {
        return Stream.of(psiClass.getMethods())
                .filter(psiMethod -> "build".equals(psiMethod.getName()))
                .map(PsiMethod::getReturnType)
                .filter(psiType -> psiType instanceof PsiClass)
                .map(PsiClass.class::cast)
                .findAny().orElse(null);
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
