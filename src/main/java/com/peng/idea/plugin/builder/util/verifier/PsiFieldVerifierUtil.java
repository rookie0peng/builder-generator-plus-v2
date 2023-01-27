package com.peng.idea.plugin.builder.util.verifier;

import com.intellij.psi.*;
import com.peng.idea.plugin.builder.settings.CodeStyleSettings2;
import org.apache.commons.lang.WordUtils;

import static org.apache.commons.lang.StringUtils.EMPTY;

import static java.util.Objects.*;

/**
 * <pre>
 *  @description: todo 转 util
 *  @author: qingpeng
 *  @date: 2023/1/19 16:22
 * </pre>
 */
public class PsiFieldVerifierUtil {

    static final String SET_PREFIX = "set";
    static final String GET_PREFIX = "get";

    public static boolean isSetInConstructor(PsiField psiField, PsiClass psiClass) {
        boolean result = false;
        PsiMethod[] constructors = psiClass.getConstructors();
        for (int i = 0; i < constructors.length && !result; i++) {
            result = checkConstructor(psiField, constructors[i]);
        }
        return result;
    }

    public static boolean checkConstructor(PsiField psiField, PsiMethod constructor) {
        PsiParameterList parameterList = constructor.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        return iterateOverParameters(psiField, parameters);
    }

    private static boolean iterateOverParameters(PsiField psiField, PsiParameter[] parameters) {
        boolean result = false;
        for (int i = 0; i < parameters.length && !result; i++) {
            result = checkParameter(psiField, parameters[i]);
        }
        return result;
    }

    private static boolean checkParameter(PsiField psiField, PsiParameter parameter) {
        boolean result = false;
        if (areNameAndTypeEqual(psiField, parameter)) {
            result = true;
        }
        return result;
    }

    public static boolean areNameAndTypeEqual(PsiField psiField, PsiParameter parameter) {
        String parameterNamePrefix = CodeStyleSettings2.INSTANCE.getParameterNamePrefix();
        String parameterName = parameter.getName();
        String parameterNameWithoutPrefix = parameterName.replace(parameterNamePrefix, "");
        String fieldNamePrefix = CodeStyleSettings2.INSTANCE.getFieldNamePrefix();
        String fieldName = psiField.getName();
        String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
        return parameterNameWithoutPrefix.equals(fieldNameWithoutPrefix) && parameter.getType().equals(psiField.getType());
    }

    public static boolean isSetInSetterMethod(PsiField psiField, PsiClass psiClass) {
        return methodIsNotPrivateAndHasProperPrefixAndProperName(psiField, psiClass, SET_PREFIX);
    }

    public static boolean hasGetterMethod(PsiField psiField, PsiClass psiClass) {
        return methodIsNotPrivateAndHasProperPrefixAndProperName(psiField, psiClass, GET_PREFIX);
    }

    private static boolean methodIsNotPrivateAndHasProperPrefixAndProperName(PsiField psiField, PsiClass psiClass, String prefix) {
        boolean result = false;
        for (PsiMethod method : psiClass.getAllMethods()) {
            if (methodIsNotPrivate(method) && methodHaProperPrefixAndProperName(psiField, method, prefix)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean methodIsNotPrivate(PsiMethod method) {
        PsiModifierList modifierList = method.getModifierList();
        return modifierListHasNoPrivateModifier(modifierList);
    }

    private static boolean methodHaProperPrefixAndProperName(PsiField psiField, PsiMethod method, String prefix) {
        String fieldNamePrefix = CodeStyleSettings2.INSTANCE.getFieldNamePrefix();
        String fieldNameWithoutPrefix = psiField.getName().replace(fieldNamePrefix, EMPTY);
        return method.getName().equals(prefix + WordUtils.capitalize(fieldNameWithoutPrefix));
    }

    private static boolean modifierListHasNoPrivateModifier(PsiModifierList modifierList) {
        return !modifierList.hasExplicitModifier(PsiModifier.PRIVATE);
    }

    /**
     * is private field
     * @param psiField psiField
     * @return ture, is; false, not
     */
    public static boolean isPrivate(PsiField psiField) {
        PsiModifierList modifierList = psiField.getModifierList();
        return !isNull(modifierList) && modifierList.hasExplicitModifier(PsiModifier.PRIVATE);
    }

    /**
     * is not private field
     * @param psiField psiField
     * @return ture, is not; false, is
     */
    public static boolean isNotPrivate(PsiField psiField) {
        return !isPrivate(psiField);
    }

    /**
     * is static field
     * @param psiField psiField
     * @return ture, is; false, not
     */
    public static boolean isStatic(PsiField psiField) {
        PsiModifierList modifierList = psiField.getModifierList();
        return !isNull(modifierList) && modifierList.hasExplicitModifier(PsiModifier.STATIC);
    }

    /**
     * is not static field
     * @param psiField psiField
     * @return ture, is not; false, is
     */
    public static boolean isNotStatic(PsiField psiField) {
        return !isStatic(psiField);
    }

    /**
     * is final field
     * @param psiField psiField
     * @return ture, is; false, not
     */
    public static boolean isFinal(PsiField psiField) {
        PsiModifierList modifierList = psiField.getModifierList();
        return !isNull(modifierList) && modifierList.hasExplicitModifier(PsiModifier.FINAL);
    }

    /**
     * is not final field
     * @param psiField psiField
     * @return ture, is not; false, is
     */
    public static boolean isNotFinal(PsiField psiField) {
        return !isFinal(psiField);
    }
}

