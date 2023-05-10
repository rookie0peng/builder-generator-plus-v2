package com.peng.idea.plugin.builder.writter;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;

import java.util.Objects;

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
    private String builderMethodName;

    private PsiClass psiClassFromEditor;
    private String methodPrefix;
    private boolean srcClassBuilder;
    private boolean isInner;
    private boolean hasButMethod;

    private boolean useSingleField;

    public static BuilderContextBuilder builder() {
        return BuilderContextBuilder.aBuilderContext();
    }

//    public BuilderContext() {
//    }

//    public BuilderContext(Project project, PsiFieldsForBuilder psiFieldsForBuilder,
//                          PsiDirectory targetDirectory, String className, PsiClass psiClassFromEditor,
//                          String methodPrefix, boolean isInner, boolean hasButMethod, boolean useSingleField) {
//        this.project = project;
//        this.psiFieldsForBuilder = psiFieldsForBuilder;
//        this.targetDirectory = targetDirectory;
//        this.className = className;
//        this.psiClassFromEditor = psiClassFromEditor;
//        this.methodPrefix = methodPrefix;
//        this.isInner = isInner;
//        this.hasButMethod = hasButMethod;
//        this.useSingleField = useSingleField;
//    }

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

    public String getBuilderMethodName() {
        return builderMethodName;
    }

    public void setBuilderMethodName(String builderMethodName) {
        this.builderMethodName = builderMethodName;
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

    public boolean isSrcClassBuilder() {
        return srcClassBuilder;
    }

    public void setSrcClassBuilder(boolean srcClassBuilder) {
        this.srcClassBuilder = srcClassBuilder;
    }

    public boolean isInner() {
        return isInner;
    }

    public void setIsInner(boolean inner) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuilderContext that = (BuilderContext) o;
        return srcClassBuilder == that.srcClassBuilder && isInner == that.isInner && hasButMethod == that.hasButMethod && useSingleField == that.useSingleField && Objects.equals(project, that.project) && Objects.equals(psiFieldsForBuilder, that.psiFieldsForBuilder) && Objects.equals(targetDirectory, that.targetDirectory) && Objects.equals(className, that.className) && Objects.equals(builderMethodName, that.builderMethodName) && Objects.equals(psiClassFromEditor, that.psiClassFromEditor) && Objects.equals(methodPrefix, that.methodPrefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, psiFieldsForBuilder, targetDirectory, className, builderMethodName, psiClassFromEditor, methodPrefix, srcClassBuilder, isInner, hasButMethod, useSingleField);
    }

    public static final class BuilderContextBuilder {
        private Project project;
        private PsiFieldsForBuilder psiFieldsForBuilder;
        private PsiDirectory targetDirectory;
        private String className;
        private String builderMethodName;
        private PsiClass psiClassFromEditor;
        private String methodPrefix;
        private boolean srcClassBuilder;
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

        public BuilderContextBuilder builderMethodName(String builderMethodName) {
            this.builderMethodName = builderMethodName;
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

        public BuilderContextBuilder srcClassBuilder(boolean srcClassBuilder) {
            this.srcClassBuilder = srcClassBuilder;
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
            BuilderContext builderContext = new BuilderContext();
            builderContext.setProject(project);
            builderContext.setPsiFieldsForBuilder(psiFieldsForBuilder);
            builderContext.setTargetDirectory(targetDirectory);
            builderContext.setClassName(className);
            builderContext.setBuilderMethodName(builderMethodName);
            builderContext.setPsiClassFromEditor(psiClassFromEditor);
            builderContext.setMethodPrefix(methodPrefix);
            builderContext.setSrcClassBuilder(srcClassBuilder);
            builderContext.setHasButMethod(hasButMethod);
            builderContext.setUseSingleField(useSingleField);
            builderContext.setIsInner(isInner);
            return builderContext;
        }
    }
}

