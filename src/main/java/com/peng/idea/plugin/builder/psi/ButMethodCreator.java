package com.peng.idea.plugin.builder.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.peng.idea.plugin.builder.settings.CodeStyleSettings2;
import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:30
 * </pre>
 */
public class ButMethodCreator {

    private final PsiElementFactory elementFactory;

    public ButMethodCreator(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiMethod butMethod(String builderClassName, PsiClass builderClass, PsiClass srcClass, String srcClassFieldName, boolean useSingleField) {
        PsiMethod[] methods = builderClass.getMethods();
        StringBuilder text = new StringBuilder("public " + builderClassName + " but() { return ");
        for (PsiMethod method : methods) {
            PsiParameterList parameterList = method.getParameterList();
            if (methodIsNotConstructor(builderClassName, method)) {
                appendMethod(text, method, parameterList, srcClassFieldName, useSingleField);
            }
        }
        deleteLastDot(text);
        text.append("; }");
        return elementFactory.createMethodFromText(text.toString(), srcClass);
    }

    private void appendMethod(StringBuilder text, PsiMethod method, PsiParameterList parameterList, String srcClassFieldName, boolean useSingleField) {
        if (isInitializingMethod(parameterList)) {
            text.append(method.getName()).append("().");
        } else {
            String parameterName = parameterList.getParameters()[0].getName();
            String parameterNamePrefix = CodeStyleSettings2.INSTANCE.getParameterNamePrefix();
            String parameterNameWithoutPrefix = parameterName.replaceFirst(parameterNamePrefix, "");
            String fieldNamePrefix = CodeStyleSettings2.INSTANCE.getFieldNamePrefix();
            text.append(method.getName()).append("(");
            if (useSingleField) {
                text.append(srcClassFieldName).append(".get").append(StringUtils.capitalize(parameterNameWithoutPrefix)).append("()");
            } else {
                text.append(fieldNamePrefix).append(parameterNameWithoutPrefix);
            }
            text.append(").");
        }
    }

    private boolean isInitializingMethod(PsiParameterList parameterList) {
        return parameterList.getParametersCount() <= 0;
    }

    private void deleteLastDot(StringBuilder text) {
        text.deleteCharAt(text.length() - 1);
    }

    private boolean methodIsNotConstructor(String builderClassName, PsiMethod method) {
        return !method.getName().equals(builderClassName);
    }
}

