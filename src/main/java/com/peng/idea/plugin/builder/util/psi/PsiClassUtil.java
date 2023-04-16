package com.peng.idea.plugin.builder.util.psi;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.util.RefactoringMessageUtil;

import java.util.Optional;

import static com.intellij.ide.util.EditSourceUtil.getDescriptor;
import static java.util.Objects.isNull;

/**
 * <pre>
 *  @description: PSI class util
 *  @author: qingpeng
 *  @date: 2022/5/14 16:44
 * </pre>
 */
public class PsiClassUtil {

    /**
     * Get the class where the cursor is located
     * @param editor editor of cursor
     * @param project current project
     * @return class
     */
    public static Optional<PsiClass> getCursorPsiClass(Editor editor, Project project) {
        if (isNull(editor) || isNull(project))
            return Optional.empty();
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (isNull(psiFile))
            return Optional.empty();
        PsiElement element = PsiUtilBase.getElementAtOffset(psiFile, editor.getCaretModel().getOffset());
        PsiElement currentElement = element;
        PsiElement parentElement;
        while (true) {
            if (currentElement instanceof PsiClass) {
                return Optional.of((PsiClass) currentElement);
            } else if (currentElement instanceof PsiDirectory) {
                return Optional.empty();
            } else {
                parentElement = getParent(currentElement);
                if (parentElement == null || parentElement.equals(currentElement))
                    return Optional.empty();
                currentElement = parentElement;
            }
        }
    }

    /**
     * Get the parent PSI element of PSI element
     * @param element PSI element
     * @return parent PSI element
     */
    private static PsiElement getParent(PsiElement element) {
        return isNull(element) ? null : element.getParent();
    }

    public static PsiFile getPsiFileFromEditor(Editor editor, Project project) {
        return getPsiFile(editor, project);
    }

    public static PsiClass getPsiClassFromEditor(Editor editor, Project project) {
        PsiClass psiClass = null;
        PsiFile psiFile = getPsiFile(editor, project);
        if (psiFile instanceof PsiClassOwner) {
            PsiClass[] classes = ((PsiClassOwner) psiFile).getClasses();
            if (classes.length == 1) {
                psiClass = classes[0];
            }
        }
        return psiClass;
    }

    private static PsiFile getPsiFile(Editor editor, Project project) {
        return PsiUtilBase.getPsiFileInEditor(editor, project);
    }

    public static PsiShortNamesCache getPsiShortNamesCache(Project project) {
        return PsiShortNamesCache.getInstance(project);
    }

    public static PsiDirectory getDirectoryFromModuleAndPackageName(Module module, String packageName) {
        PsiDirectory baseDir = PackageUtil.findPossiblePackageDirectoryInModule(module, packageName);
        return PackageUtil.findOrCreateDirectoryForPackage(module, packageName, baseDir, true);
    }

    public static void navigateToClass(PsiClass psiClass) {
        if (psiClass != null) {
            Navigatable navigatable = getDescriptor(psiClass);
            if (navigatable != null) {
                navigatable.navigate(true);
            }
        }
    }

    public static String checkIfClassCanBeCreated(PsiDirectory targetDirectory, String className) {
        return RefactoringMessageUtil.checkCanCreateClass(targetDirectory, className);
    }

    public static JavaDirectoryService getJavaDirectoryService() {
        return JavaDirectoryService.getInstance();
    }

    public static PsiPackage getPackage(PsiDirectory psiDirectory) {
        return getJavaDirectoryService().getPackage(psiDirectory);
    }

    public static JavaPsiFacade getJavaPsiFacade(Project project) {
        return JavaPsiFacade.getInstance(project);
    }

    public static CommandProcessor getCommandProcessor() {
        return CommandProcessor.getInstance();
    }

    public static Application getApplication() {
        return ApplicationManager.getApplication();
    }

    public static Module findModuleForPsiClass(PsiClass psiClass, Project project) {
        return ModuleUtil.findModuleForFile(psiClass.getContainingFile().getVirtualFile(), project);
    }
}
