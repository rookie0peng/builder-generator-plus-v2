package com.peng.idea.plugin.builder.util.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/23 1:08
 * </pre>
 */
public class ClassFinderUtil {

    public static PsiClass findClass(String pattern, Project project) {
        PsiClass result;
        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(project);
        PsiClass[] classesArray = psiShortNamesCache.getClassesByName(pattern, projectScope);
        result = getPsiClass(classesArray);
        return result;
    }

    private static PsiClass getPsiClass(PsiClass[] classesArray) {
        return (classesArray != null && classesArray.length != 0) ? classesArray[0] : null;
    }
}
