package com.peng.idea.plugin.builder.api;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/16 11:37
 * </pre>
 */
public class CreateBuilderDialogDO {

    /**
     * current project
     */
    private Project project;

    /**
     * editor of cursor
     */
    private Editor editor;

    /**
     * PSI class of editor
     */
    private PsiClass editorPsiClass;

    /**
     * class
     */
    private PsiClass classToOperate;

    private List<PsiMethod> buildMethodToOperates;

    public static CreateBuilderDialogDOBuilder builder() {
        return CreateBuilderDialogDOBuilder.aCreateBuilderDialogDO();
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
        return "DisplayChooserDO{" +
                "project=" + project +
                ", editor=" + editor +
                ", psiClassFromEditor=" + editorPsiClass +
                ", classToOperate=" + classToOperate +
                ", buildMethodToOperates=" + buildMethodToOperates +
                '}';
    }

    public static final class CreateBuilderDialogDOBuilder {
        private Project project;
        private Editor editor;
        private PsiClass psiClassFromEditor;
        private PsiClass classToOperate;
        private List<PsiMethod> buildMethodToOperates;

        private CreateBuilderDialogDOBuilder() {
        }

        public static CreateBuilderDialogDOBuilder aCreateBuilderDialogDO() {
            return new CreateBuilderDialogDOBuilder();
        }

        public CreateBuilderDialogDOBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public CreateBuilderDialogDOBuilder editor(Editor editor) {
            this.editor = editor;
            return this;
        }

        public CreateBuilderDialogDOBuilder psiClassFromEditor(PsiClass psiClassFromEditor) {
            this.psiClassFromEditor = psiClassFromEditor;
            return this;
        }

        public CreateBuilderDialogDOBuilder classToOperate(PsiClass classToOperate) {
            this.classToOperate = classToOperate;
            return this;
        }

        public CreateBuilderDialogDOBuilder buildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
            this.buildMethodToOperates = buildMethodToOperates;
            return this;
        }

        public CreateBuilderDialogDO build() {
            CreateBuilderDialogDO createBuilderDialogDO = new CreateBuilderDialogDO();
            createBuilderDialogDO.setProject(project);
            createBuilderDialogDO.setEditor(editor);
            createBuilderDialogDO.setEditorPsiClass(psiClassFromEditor);
            createBuilderDialogDO.setClassToOperate(classToOperate);
            createBuilderDialogDO.setBuildMethodToOperates(buildMethodToOperates);
            return createBuilderDialogDO;
        }
    }
}
