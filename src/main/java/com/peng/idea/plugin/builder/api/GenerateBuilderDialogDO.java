package com.peng.idea.plugin.builder.api;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/16 11:37
 * </pre>
 */
public class GenerateBuilderDialogDO {

    /**
     * current project
     */
    private Project project;

    /**
     * editor of cursor
     */
    private Editor editor;

    /**
     * editor of package
     */
    private PsiPackage editorPackage;

    /**
     * PSI class of editor
     */
    private PsiClass editorPsiClass;

    /**
     * class
     */
    private PsiClass classToOperate;

    private List<PsiMethod> buildMethodToOperates;

    public static GenerateBuilderDialogDOBuilder builder() {
        return GenerateBuilderDialogDOBuilder.aGenerateBuilderDialogDO();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(@NotNull Project project) {
        this.project = project;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(@NotNull Editor editor) {
        this.editor = editor;
    }

    public PsiPackage getEditorPackage() {
        return editorPackage;
    }

    public void setEditorPackage(PsiPackage editorPackage) {
        this.editorPackage = editorPackage;
    }

    public PsiClass getEditorPsiClass() {
        return editorPsiClass;
    }

    public void setEditorPsiClass(PsiClass editorPsiClass) {
        this.editorPsiClass = editorPsiClass;
    }

    public PsiClass getClassToOperate() {
        return classToOperate;
    }

    public void setClassToOperate(PsiClass classToOperate) {
        this.classToOperate = classToOperate;
    }

    public List<PsiMethod> getBuildMethodToOperates() {
        return buildMethodToOperates;
    }

    public void setBuildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
        this.buildMethodToOperates = buildMethodToOperates;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenerateBuilderDialogDO.class.getSimpleName() + "[", "]")
                .add("project=" + project)
                .add("editor=" + editor)
                .add("editorPackage=" + editorPackage)
                .add("editorPsiClass=" + editorPsiClass)
                .add("classToOperate=" + classToOperate)
                .add("buildMethodToOperates=" + buildMethodToOperates)
                .toString();
    }

    public static final class GenerateBuilderDialogDOBuilder {
        private Project project;
        private Editor editor;
        private PsiPackage editorPackage;
        private PsiClass editorPsiClass;
        private PsiClass classToOperate;
        private List<PsiMethod> buildMethodToOperates;

        private GenerateBuilderDialogDOBuilder() {
        }

        public static GenerateBuilderDialogDOBuilder aGenerateBuilderDialogDO() {
            return new GenerateBuilderDialogDOBuilder();
        }

        public GenerateBuilderDialogDOBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public GenerateBuilderDialogDOBuilder editor(Editor editor) {
            this.editor = editor;
            return this;
        }

        public GenerateBuilderDialogDOBuilder editorPackage(PsiPackage editorPackage) {
            this.editorPackage = editorPackage;
            return this;
        }

        public GenerateBuilderDialogDOBuilder editorPsiClass(PsiClass editorPsiClass) {
            this.editorPsiClass = editorPsiClass;
            return this;
        }

        public GenerateBuilderDialogDOBuilder classToOperate(PsiClass classToOperate) {
            this.classToOperate = classToOperate;
            return this;
        }

        public GenerateBuilderDialogDOBuilder buildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
            this.buildMethodToOperates = buildMethodToOperates;
            return this;
        }

        public GenerateBuilderDialogDO build() {
            GenerateBuilderDialogDO generateBuilderDialogDO = new GenerateBuilderDialogDO();
            generateBuilderDialogDO.setProject(project);
            generateBuilderDialogDO.setEditor(editor);
            generateBuilderDialogDO.setEditorPackage(editorPackage);
            generateBuilderDialogDO.setEditorPsiClass(editorPsiClass);
            generateBuilderDialogDO.setClassToOperate(classToOperate);
            generateBuilderDialogDO.setBuildMethodToOperates(buildMethodToOperates);
            return generateBuilderDialogDO;
        }
    }
}
