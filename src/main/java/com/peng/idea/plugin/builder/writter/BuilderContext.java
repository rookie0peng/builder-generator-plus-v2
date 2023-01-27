package com.peng.idea.plugin.builder.writter;

import com.google.common.base.Objects;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:27
 * </pre>
 */
public class BuilderContext {

    private final Project project;
    private final PsiFieldsForBuilder psiFieldsForBuilder;
    private final PsiDirectory targetDirectory;
    private final String className;
    private final PsiClass psiClassFromEditor;
    private final String methodPrefix;
    private final boolean isInner;
    private final boolean hasButMethod;
    private final boolean useSingleField;

    public BuilderContext(Project project, PsiFieldsForBuilder psiFieldsForBuilder,
                          PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor,
                          String methodPrefix, boolean isInner, boolean hasButMethod, boolean useSingleField) {
        this.project = project;
        this.psiFieldsForBuilder = psiFieldsForBuilder;
        this.targetDirectory = targetDirectory;
        this.className = className;
        this.psiClassFromEditor = psiClassFromEditor;
        this.methodPrefix = methodPrefix;
        this.isInner = isInner;
        this.hasButMethod = hasButMethod;
        this.useSingleField = useSingleField;
    }

    public Project getProject() {
        return project;
    }

    public PsiFieldsForBuilder getPsiFieldsForBuilder() {
        return psiFieldsForBuilder;
    }

    public PsiDirectory getTargetDirectory() {
        return targetDirectory;
    }

    public String getClassName() {
        return className;
    }

    public PsiClass getPsiClassFromEditor() {
        return psiClassFromEditor;
    }

    public String getMethodPrefix() {
        return methodPrefix;
    }

    boolean isInner() {
        return isInner;
    }

    boolean hasButMethod() {
        return hasButMethod;
    }

    public boolean useSingleField() {
        return useSingleField;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BuilderContext other = (BuilderContext) obj;
        return Objects.equal(this.project, other.project)
                && Objects.equal(this.psiFieldsForBuilder, other.psiFieldsForBuilder)
                && Objects.equal(this.targetDirectory, other.targetDirectory)
                && Objects.equal(this.className, other.className)
                && Objects.equal(this.psiClassFromEditor, other.psiClassFromEditor)
                && Objects.equal(this.methodPrefix, other.methodPrefix);
    }
}

