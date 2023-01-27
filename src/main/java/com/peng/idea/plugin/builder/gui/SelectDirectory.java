package com.peng.idea.plugin.builder.gui;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:17
 * </pre>
 */
public class SelectDirectory implements Runnable {

    private final CreateBuilderDialog createBuilderDialog;
    private final Module module;
    private final String packageName;
    private final String className;
    private final PsiClass existingBuilder;

    public SelectDirectory(
            CreateBuilderDialog createBuilderDialog, Module module, String packageName,
            String className, PsiClass existingBuilder
    ) {
        this.createBuilderDialog = createBuilderDialog;
        this.module = module;
        this.packageName = packageName;
        this.className = className;
        this.existingBuilder = existingBuilder;
    }

    @Override
    public void run() {
        PsiDirectory targetDirectory = PsiClassUtil.getDirectoryFromModuleAndPackageName(module, packageName);
        if (targetDirectory != null) {
            throwExceptionIfClassCannotBeCreated(targetDirectory);
            createBuilderDialog.setTargetDirectory(targetDirectory);
        }
    }

    private void throwExceptionIfClassCannotBeCreated(PsiDirectory targetDirectory) {
        if (!isClassToCreateSameAsBuilderToDelete(targetDirectory)) {
            String errorString = PsiClassUtil.checkIfClassCanBeCreated(targetDirectory, className);
            if (errorString != null) {
                throw new IncorrectOperationException(errorString);
            }
        }
    }

    private boolean isClassToCreateSameAsBuilderToDelete(PsiDirectory targetDirectory) {
        return existingBuilder != null
                && existingBuilder.getContainingFile() != null
                && existingBuilder.getContainingFile().getContainingDirectory() != null
                && existingBuilder.getContainingFile().getContainingDirectory().getName().equals(targetDirectory.getName())
                && existingBuilder.getName() != null
                && existingBuilder.getName().equals(className);
    }
}

