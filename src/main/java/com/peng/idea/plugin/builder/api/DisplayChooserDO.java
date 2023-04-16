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
public class DisplayChooserDO {

    private Project project;

    private Editor editor;

    private PsiClass psiClassFromEditor;

    private PsiClass classToOperate;

    private List<PsiMethod> buildMethodToOperates;

    public static DisplayChooserDOBuilder builder() {
        return DisplayChooserDOBuilder.aDisplayChooserDO();
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

    public PsiClass getPsiClassFromEditor() {
        return psiClassFromEditor;
    }

    public void setPsiClassFromEditor(PsiClass psiClassFromEditor) {
        this.psiClassFromEditor = psiClassFromEditor;
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
                ", psiClassFromEditor=" + psiClassFromEditor +
                ", classToOperate=" + classToOperate +
                ", buildMethodToOperates=" + buildMethodToOperates +
                '}';
    }

    public static final class DisplayChooserDOBuilder {
        private Project project;
        private Editor editor;
        private PsiClass psiClassFromEditor;
        private PsiClass classToOperate;
        private List<PsiMethod> buildMethodToOperates;

        private DisplayChooserDOBuilder() {
        }

        public static DisplayChooserDOBuilder aDisplayChooserDO() {
            return new DisplayChooserDOBuilder();
        }

        public DisplayChooserDOBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public DisplayChooserDOBuilder editor(Editor editor) {
            this.editor = editor;
            return this;
        }

        public DisplayChooserDOBuilder psiClassFromEditor(PsiClass psiClassFromEditor) {
            this.psiClassFromEditor = psiClassFromEditor;
            return this;
        }

        public DisplayChooserDOBuilder classToOperate(PsiClass classToOperate) {
            this.classToOperate = classToOperate;
            return this;
        }

        public DisplayChooserDOBuilder buildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
            this.buildMethodToOperates = buildMethodToOperates;
            return this;
        }

        public DisplayChooserDO build() {
            DisplayChooserDO displayChooserDO = new DisplayChooserDO();
            displayChooserDO.setProject(project);
            displayChooserDO.setEditor(editor);
            displayChooserDO.setPsiClassFromEditor(psiClassFromEditor);
            displayChooserDO.setClassToOperate(classToOperate);
            displayChooserDO.setBuildMethodToOperates(buildMethodToOperates);
            return displayChooserDO;
        }
    }
}
