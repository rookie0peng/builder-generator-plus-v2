package com.peng.idea.plugin.builder.psi;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.settings.CodeStyleSettings2;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:30
 * </pre>
 */
public class MethodCreator {

    private final MethodNameCreator methodNameCreator = new MethodNameCreator();
    private final PsiElementFactory elementFactory;
    private final String builderClassName;

    public MethodCreator(PsiElementFactory elementFactory, String builderClassName) {
        this.elementFactory = elementFactory;
        this.builderClassName = builderClassName;
    }

    public PsiMethod createMethod(PsiField psiField, String methodPrefix, String srcClassFieldName, boolean useSingleField) {
        String fieldName = psiField.getName();
        String fieldType = psiField.getType().getPresentableText();
        String fieldNamePrefix = CodeStyleSettings2.INSTANCE.getFieldNamePrefix();
        String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
        String parameterNamePrefix = CodeStyleSettings2.INSTANCE.getParameterNamePrefix();
        String parameterName = parameterNamePrefix + fieldNameWithoutPrefix;
        String methodName = methodNameCreator.createMethodName(methodPrefix, fieldNameWithoutPrefix);
        String methodText;
        if(useSingleField){
            String setterName = methodNameCreator.createMethodName("set", fieldNameWithoutPrefix);
            methodText = "public " + builderClassName + " " + methodName + "(" + fieldType + " " + parameterName + ") { "
                    + srcClassFieldName + "." + setterName + "(" + fieldName + "); return this; }";
        } else {
            methodText = "public " + builderClassName + " " + methodName + "(" + fieldType + " " + parameterName + ") { this."
                    + fieldName + " = " + parameterName + "; return this; }";
        }
        return elementFactory.createMethodFromText(methodText, psiField);
    }
}

