package com.peng.idea.plugin.builder.psi;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.util.IntegerUtil;
import com.peng.idea.plugin.builder.util.verifier.PsiFieldVerifierUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:26
 * </pre>
 */
public class ConstructorSelector {

    private List<ExtraData> equalParameterCount;
    private TreeSet<ExtraData> higherParameterCount;
    private List<ExtraData> lowerParameterCount;

    public static PsiMethod _getBestConstructor(Collection<PsiField> psiFieldsToFindInConstructor, PsiClass psiClass) {
        return new ConstructorSelector().getBestConstructor(psiFieldsToFindInConstructor, psiClass);
    }

    public PsiMethod getBestConstructor(Collection<PsiField> psiFieldsToFindInConstructor, PsiClass psiClass) {
        int fieldsToFindCount = psiFieldsToFindInConstructor.size();
        createConstructorLists(psiFieldsToFindInConstructor, psiClass);

        computeNumberOfMatchingFields(equalParameterCount, psiFieldsToFindInConstructor);
        PsiMethod bestConstructor = findConstructorWithAllFieldsToFind(equalParameterCount, fieldsToFindCount);
        if (bestConstructor != null) {
            return bestConstructor;
        }

        computeNumberOfMatchingFields(higherParameterCount, psiFieldsToFindInConstructor);
        bestConstructor = findConstructorWithAllFieldsToFind(higherParameterCount, fieldsToFindCount);
        if (bestConstructor != null) {
            return bestConstructor;
        }

        computeNumberOfMatchingFields(lowerParameterCount, psiFieldsToFindInConstructor);
        return findConstructorWithMaximumOfFieldsToFind();
    }

    private void createConstructorLists(Collection<PsiField> psiFieldsToFindInConstructor, PsiClass psiClass) {
        equalParameterCount = Lists.newArrayList();
        higherParameterCount = Sets.newTreeSet();
        lowerParameterCount = Lists.newArrayList();
        PsiMethod[] constructors = psiClass.getConstructors();
        for (PsiMethod constructor : constructors) {
            int parameterCount = constructor.getParameterList().getParametersCount();
            if (parameterCount > psiFieldsToFindInConstructor.size()) {
                higherParameterCount.add(new ExtraData(constructor));
            } else if (parameterCount == psiFieldsToFindInConstructor.size()) {
                equalParameterCount.add(new ExtraData(constructor));
            } else if (parameterCount >= 0) {
                lowerParameterCount.add(new ExtraData(constructor));
            }
        }
    }

    private void computeNumberOfMatchingFields(Iterable<ExtraData> constuctorsWithExtraData, Iterable<PsiField> psiFieldsToFindInConstructor) {
        for (ExtraData extraData : constuctorsWithExtraData) {
            int matchingFieldsCount = 0;
            for (PsiField psiField : psiFieldsToFindInConstructor) {
                if (PsiFieldVerifierUtil.checkConstructor(psiField, extraData.getConstructor())) {
                    matchingFieldsCount++;
                }
            }
            extraData.setMatchingFieldsCount(matchingFieldsCount);
        }
    }

    private PsiMethod findConstructorWithAllFieldsToFind(Iterable<ExtraData> constructorsWithExtraData, int fieldsToFindCount) {
        for (ExtraData extraData : constructorsWithExtraData) {
            if (extraData.getMatchingFieldsCount() == fieldsToFindCount) {
                return extraData.getConstructor();
            }
        }
        return null;
    }

    private PsiMethod findConstructorWithMaximumOfFieldsToFind() {
        Iterable<ExtraData> allConstructors = Iterables.concat(equalParameterCount, higherParameterCount, lowerParameterCount);
        int matchingFieldCount = -1;
        int parameterCount = 0;
        PsiMethod bestConstructor = null;
        for (ExtraData constructor : allConstructors) {
            int tempMatchingCount = IntegerUtil.nullToZero(constructor.getMatchingFieldsCount());
            int tempParametersCount = IntegerUtil.nullToZero(constructor.getParametersCount());
            if (tempMatchingCount > matchingFieldCount || (tempMatchingCount == matchingFieldCount && tempParametersCount < parameterCount)) {
                bestConstructor = constructor.getConstructor();
                matchingFieldCount = tempMatchingCount;
                parameterCount = tempParametersCount;
            }
        }
        return bestConstructor;
    }

    private static class ExtraData implements Comparable<ExtraData> {
        private final PsiMethod constructor;
        private Integer matchingFieldsCount;

        ExtraData(PsiMethod constructor) {
            this.constructor = constructor;
        }

        @Override
        public int compareTo(@NotNull ConstructorSelector.ExtraData dst) {
            if (isNull(this.getParametersCount()) && isNull(dst.getParametersCount())) {
                return 0;
            } else if (isNull(this.getParametersCount())) {
                return -1;
            } else if (isNull(dst.getParametersCount())) {
                return 1;
            } else {
                return this.getParametersCount().compareTo(dst.getParametersCount());
            }
        }

        PsiMethod getConstructor() {
            return constructor;
        }

        Integer getMatchingFieldsCount() {
            return matchingFieldsCount;
        }

        void setMatchingFieldsCount(Integer matchingFieldsCount) {
            this.matchingFieldsCount = matchingFieldsCount;
        }

        Integer getParametersCount() {
            return constructor == null ? null : constructor.getParameterList().getParametersCount();
        }
    }
}

