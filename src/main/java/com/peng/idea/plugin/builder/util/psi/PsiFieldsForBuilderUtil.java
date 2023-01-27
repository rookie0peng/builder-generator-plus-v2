package com.peng.idea.plugin.builder.util.psi;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.psi.ConstructorSelector;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;
import com.peng.idea.plugin.builder.util.verifier.PsiFieldVerifierUtil;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/20 15:10
 * </pre>
 */
public class PsiFieldsForBuilderUtil {

    @SuppressWarnings("rawtypes")
    public static PsiFieldsForBuilder createPsiFieldsForBuilder(List<PsiElementClassMember> psiElementClassMembers, PsiClass psiClass) {
        List<PsiField> allSelectedPsiFields = Lists.newArrayList();
        List<PsiField> psiFieldsFoundInSetters = Lists.newArrayList();
        for (PsiElementClassMember psiElementClassMember : psiElementClassMembers) {
            PsiElement psiElement = psiElementClassMember.getPsiElement();
            if (psiElement instanceof PsiField) {
                PsiField psiField = (PsiField) psiElement;
                allSelectedPsiFields.add(psiField);
                if (PsiFieldVerifierUtil.isSetInSetterMethod(psiField, psiClass)) {
                    psiFieldsFoundInSetters.add(psiField);
                }
            }
        }
        List<PsiField> psiFieldsToFindInConstructor = getSubList(allSelectedPsiFields, psiFieldsFoundInSetters);
        List<PsiField> psiFieldsForConstructor = Lists.newArrayList();
        PsiMethod bestConstructor = ConstructorSelector._getBestConstructor(psiFieldsToFindInConstructor, psiClass);
        if (bestConstructor != null) {
            buildPsiFieldsForConstructor(psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
        }
        List<PsiField> psiFieldsForSetters = getSubList(psiFieldsFoundInSetters, psiFieldsForConstructor);

        return new PsiFieldsForBuilder(psiFieldsForSetters, psiFieldsForConstructor, allSelectedPsiFields, bestConstructor);
    }

    private static void buildPsiFieldsForConstructor(List<PsiField> psiFieldsForConstructor, List<PsiField> allSelectedPsiFields, PsiMethod bestConstructor) {
        for (PsiField selectedPsiField : allSelectedPsiFields) {
            if (PsiFieldVerifierUtil.checkConstructor(selectedPsiField, bestConstructor)) {
                psiFieldsForConstructor.add(selectedPsiField);
            }
        }
    }

    private static List<PsiField> getSubList(List<PsiField> inputList, List<PsiField> listToRemove) {
        List<PsiField> newList = Lists.newArrayList();
        for (PsiField inputPsiField : inputList) {
            boolean setterMustBeAdded = true;
            for (PsiField psiFieldToRemove : listToRemove) {
                if (psiFieldToRemove.getName().equals(inputPsiField.getName())) {
                    setterMustBeAdded = false;
                }
            }
            if (setterMustBeAdded) {
                newList.add(inputPsiField);
            }
        }
        return newList;
    }
}
