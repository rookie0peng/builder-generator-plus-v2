package com.peng.idea.plugin.builder.psi;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;

import java.util.List;

import static com.peng.idea.plugin.builder.util.CollectionUtil.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:31
 * </pre>
 */
public class PsiFieldsModifier {

    public void modifyFields(List<PsiField> psiFieldsForSetters, List<PsiField> psiFieldsForConstructor, PsiClass builderClass) {
        safeCollection(psiFieldsForSetters).forEach(psiField -> removeModifiers(psiField, builderClass));
        safeCollection(psiFieldsForConstructor).forEach(psiField -> removeModifiers(psiField, builderClass));
    }

    public void modifyFieldsForInnerClass(List<PsiField> allFileds, PsiClass innerBuilderClass) {
        safeCollection(allFileds).forEach(psiField -> removeModifiers(psiField, innerBuilderClass));
    }

    private void removeModifiers(PsiField psiField, PsiClass builderClass) {
        PsiElement copy = psiField.copy();
        removeAnnotationsFromElement(copy);
        removeFinalModifierFromElement(copy);
        removeComments(copy);
        builderClass.add(copy);
    }

    private void removeComments(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiDocComment docComment = ((PsiField) psiElement).getDocComment();
            if (docComment != null) {
                docComment.delete();
            }
        }
    }

    private void removeFinalModifierFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null && modifierList.hasExplicitModifier(PsiModifier.FINAL)) {
                modifierList.setModifierProperty(PsiModifier.FINAL, false);
            }
        }
    }

    private void removeAnnotationsFromElement(PsiElement psiElement) {
        if (psiElement instanceof PsiField) {
            PsiModifierList modifierList = ((PsiField) psiElement).getModifierList();
            if (modifierList != null) {
                deleteAnnotationsFromModifierList(modifierList);
            }
        }
    }

    private void deleteAnnotationsFromModifierList(PsiModifierList modifierList) {
        for (PsiAnnotation annotation : modifierList.getAnnotations()) {
            annotation.delete();
        }
    }
}

