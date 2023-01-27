package com.peng.idea.plugin.builder.psi;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.peng.idea.plugin.builder.util.psi.ClassFinderUtil;
import com.peng.idea.plugin.builder.util.verifier.PsiFieldVerifierUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:33
 * </pre>
 */
public class PsiFieldSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PsiFieldSelector.class);

    private static final String SERIAL_ID = "serialVersionUID";

    /**
     * skip serial version id field
     * @param psiField psiField
     * @return ture, skip; false, don't skip
     */
    private static boolean skipSerialId(PsiField psiField) {
        return !SERIAL_ID.equals(psiField.getName());
    }

//    /**
//     * skip static field
//     * @param psiField psiField
//     * @return ture, skip; false, don't skip
//     */
//    private static boolean skipStatic(PsiField psiField) {
//        return !psiField.hasModifier(JvmModifier.STATIC);
//    }
//
//    /**
//     * skip final field
//     * @param psiField psiField
//     * @return ture, skip; false, don't skip
//     */
//    private static boolean skipFinal(PsiField psiField) {
//        return !psiField.hasModifier(JvmModifier.FINAL);
//    }

    /**
     * get optional field member
     * @return optional field member
     */
    public static List<PsiElementClassMember> selectFieldsToIncludeInBuilder(final PsiClass psiClass, final boolean innerBuilder, final boolean useSingleField, final boolean hasButMethod) {
        List<PsiElementClassMember> result = new ArrayList<>();
        List<PsiField> psiFields = stream(psiClass.getAllFields())
                .filter(PsiFieldSelector::skipSerialId)
                .filter(PsiFieldVerifierUtil::isNotFinal)
                .filter(PsiFieldVerifierUtil::isNotStatic)
                .filter(psiField -> isAppropriate(psiClass, psiField, innerBuilder, useSingleField, hasButMethod))
                .collect(toList());
//        Iterable<PsiField> filtered = psiFields.stream()
//                .filter(psiField -> isAppropriate(psiClass, psiField, innerBuilder, useSingleField, hasButMethod))
//                .collect(toList());
        for (PsiField psiField : psiFields) {
            result.add(new PsiFieldMember(psiField));
        }
        return result;
    }

    private static boolean isAppropriate(
            PsiClass psiClass, PsiField psiField, boolean innerBuilder, boolean useSingleField, boolean hasButMethod
    ) {
        LOGGER.debug("isAppropriate, psiClass: {}, psiField: {}, innerBuilder: {}, useSingleField: {}, hasButMethod: {}",
                psiClass, psiField, innerBuilder, useSingleField, hasButMethod
        );
        if (useSingleField && hasButMethod) {
            return PsiFieldVerifierUtil.isSetInSetterMethod(psiField, psiClass) && PsiFieldVerifierUtil.hasGetterMethod(psiField, psiClass);
        } else if (useSingleField) {
            return PsiFieldVerifierUtil.isSetInSetterMethod(psiField, psiClass);
        } else if (!innerBuilder) {
            return PsiFieldVerifierUtil.isSetInSetterMethod(psiField, psiClass) || PsiFieldVerifierUtil.isSetInConstructor(psiField, psiClass);
        }
        return true;
    }

    public static void main(String[] args) {
        PsiClass apple = ClassFinderUtil.findClass("Apple", ProjectManager.getInstance().getDefaultProject());
        List<PsiElementClassMember> psiElementClassMembers = selectFieldsToIncludeInBuilder(apple, true, true, true);
        System.out.println("over");
    }

    public static final class Apple {

        private static final String H = "123";

        private String a;

        private String b;

        private String c;
    }
}

