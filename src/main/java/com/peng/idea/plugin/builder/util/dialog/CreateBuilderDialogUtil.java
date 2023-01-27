package com.peng.idea.plugin.builder.util.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import com.peng.idea.plugin.builder.gui.CreateBuilderDialog;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/20 14:43
 * </pre>
 */
public class CreateBuilderDialogUtil {

    private static final String BUILDER_SUFFIX = "Builder";
    private static final String METHOD_PREFIX = "with";
    private static final String DIALOG_NAME = "CreateBuilder";

    public static CreateBuilderDialog createBuilderDialog(PsiClass sourceClass, Project project, PsiPackage srcPackage, PsiClass existingBuilder) {
        return new CreateBuilderDialog(project, DIALOG_NAME, sourceClass, sourceClass.getName() + BUILDER_SUFFIX, METHOD_PREFIX, srcPackage, existingBuilder);
    }
}
