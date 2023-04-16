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

    private Project project;
    private PsiFieldsForBuilder psiFieldsForBuilder;
    private PsiDirectory targetDirectory;
    private String className;
    private PsiClass psiClassFromEditor;

    private String methodPrefix;
    private boolean isInner;
    private boolean hasButMethod;
    private boolean useSingleField;

    public BuilderContext() {
    }

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

    public static BuilderContextBuilder builder() {
        return BuilderContextBuilder.aBuilderContext();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public PsiFieldsForBuilder getPsiFieldsForBuilder() {
        return psiFieldsForBuilder;
    }

    public void setPsiFieldsForBuilder(PsiFieldsForBuilder psiFieldsForBuilder) {
        this.psiFieldsForBuilder = psiFieldsForBuilder;
    }

    public PsiDirectory getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(PsiDirectory targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public PsiClass getPsiClassFromEditor() {
        return psiClassFromEditor;
    }

    public void setPsiClassFromEditor(PsiClass psiClassFromEditor) {
        this.psiClassFromEditor = psiClassFromEditor;
    }

    public String getMethodPrefix() {
        return methodPrefix;
    }

    public void setMethodPrefix(String methodPrefix) {
        this.methodPrefix = methodPrefix;
    }

    public boolean isInner() {
        return isInner;
    }

    public void setInner(boolean inner) {
        isInner = inner;
    }

    public boolean isHasButMethod() {
        return hasButMethod;
    }

    public void setHasButMethod(boolean hasButMethod) {
        this.hasButMethod = hasButMethod;
    }

    public boolean isUseSingleField() {
        return useSingleField;
    }

    public void setUseSingleField(boolean useSingleField) {
        this.useSingleField = useSingleField;
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

    public static final class BuilderContextBuilder {
        private Project project;
        private PsiFieldsForBuilder psiFieldsForBuilder;
        private PsiDirectory targetDirectory;
        private String className;
        private PsiClass psiClassFromEditor;
        private String methodPrefix;
        private boolean isInner;
        private boolean hasButMethod;
        private boolean useSingleField;

        private BuilderContextBuilder() {
        }

        public static BuilderContextBuilder aBuilderContext() {
            return new BuilderContextBuilder();
        }

        public BuilderContextBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public BuilderContextBuilder psiFieldsForBuilder(PsiFieldsForBuilder psiFieldsForBuilder) {
            this.psiFieldsForBuilder = psiFieldsForBuilder;
            return this;
        }

        public BuilderContextBuilder targetDirectory(PsiDirectory targetDirectory) {
            this.targetDirectory = targetDirectory;
            return this;
        }

        public BuilderContextBuilder className(String className) {
            this.className = className;
            return this;
        }

        public BuilderContextBuilder psiClassFromEditor(PsiClass psiClassFromEditor) {
            this.psiClassFromEditor = psiClassFromEditor;
            return this;
        }

        public BuilderContextBuilder methodPrefix(String methodPrefix) {
            this.methodPrefix = methodPrefix;
            return this;
        }

        public BuilderContextBuilder isInner(boolean isInner) {
            this.isInner = isInner;
            return this;
        }

        public BuilderContextBuilder hasButMethod(boolean hasButMethod) {
            this.hasButMethod = hasButMethod;
            return this;
        }

        public BuilderContextBuilder useSingleField(boolean useSingleField) {
            this.useSingleField = useSingleField;
            return this;
        }

        public BuilderContext build() {
            return new BuilderContext(project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix, isInner, hasButMethod, useSingleField);
        }
    }
}

