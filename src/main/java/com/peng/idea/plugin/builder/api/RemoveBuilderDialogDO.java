package com.peng.idea.plugin.builder.api;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/9
 * </pre>
 */
public class RemoveBuilderDialogDO {

    /**
     * current project
     * <br>
     * required nonnull
     */
    private Project project;

    /**
     * editor of cursor
     */
    private Editor editor;

    /**
     * source PSI class of editor PSI class
     */
    private PsiClass srcPsiClass;

    /**
     * PSI class of editor
     */
    private PsiClass editorPsiClass;

    /**
     * destination PSI class of editor PSI class
     */
    private PsiClass dstPsiClass;

    private List<PsiMethod> buildMethodToOperates;

    public static RemoveBuilderDialogDOBuilder builder() {
        return RemoveBuilderDialogDOBuilder.aRemoveBuilderDialogDO();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public PsiClass getSrcPsiClass() {
        return srcPsiClass;
    }

    public void setSrcPsiClass(PsiClass srcPsiClass) {
        this.srcPsiClass = srcPsiClass;
    }

    public PsiClass getEditorPsiClass() {
        return editorPsiClass;
    }

    public void setEditorPsiClass(PsiClass editorPsiClass) {
        this.editorPsiClass = editorPsiClass;
    }

    public PsiClass getDstPsiClass() {
        return dstPsiClass;
    }

    public void setDstPsiClass(PsiClass dstPsiClass) {
        this.dstPsiClass = dstPsiClass;
    }

    public List<PsiMethod> getBuildMethodToOperates() {
        return buildMethodToOperates;
    }

    public void setBuildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
        this.buildMethodToOperates = buildMethodToOperates;
    }

    @Override
    public String toString() {
        return "RemoveBuilderDialogDO{" +
                "project=" + project +
                ", editor=" + editor +
                ", srcPsiClass=" + srcPsiClass +
                ", editorPsiClass=" + editorPsiClass +
                ", dstPsiClass=" + dstPsiClass +
                ", buildMethodToOperates=" + buildMethodToOperates +
                '}';
    }

    public static final class RemoveBuilderDialogDOBuilder {
        private Project project;
        private Editor editor;
        private PsiClass srcPsiClass;
        private PsiClass editorPsiClass;
        private PsiClass dstPsiClass;
        private List<PsiMethod> buildMethodToOperates;

        private RemoveBuilderDialogDOBuilder() {
        }

        public static RemoveBuilderDialogDOBuilder aRemoveBuilderDialogDO() {
            return new RemoveBuilderDialogDOBuilder();
        }

        public RemoveBuilderDialogDOBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public RemoveBuilderDialogDOBuilder editor(Editor editor) {
            this.editor = editor;
            return this;
        }

        public RemoveBuilderDialogDOBuilder srcPsiClass(PsiClass srcPsiClass) {
            this.srcPsiClass = srcPsiClass;
            return this;
        }

        public RemoveBuilderDialogDOBuilder editorPsiClass(PsiClass editorPsiClass) {
            this.editorPsiClass = editorPsiClass;
            return this;
        }

        public RemoveBuilderDialogDOBuilder dstPsiClass(PsiClass dstPsiClass) {
            this.dstPsiClass = dstPsiClass;
            return this;
        }

        public RemoveBuilderDialogDOBuilder buildMethodToOperates(List<PsiMethod> buildMethodToOperates) {
            this.buildMethodToOperates = buildMethodToOperates;
            return this;
        }

        public RemoveBuilderDialogDO build() {
            RemoveBuilderDialogDO removeBuilderDialogDO = new RemoveBuilderDialogDO();
            removeBuilderDialogDO.setProject(project);
            removeBuilderDialogDO.setEditor(editor);
            removeBuilderDialogDO.setSrcPsiClass(srcPsiClass);
            removeBuilderDialogDO.setEditorPsiClass(editorPsiClass);
            removeBuilderDialogDO.setDstPsiClass(dstPsiClass);
            removeBuilderDialogDO.setBuildMethodToOperates(buildMethodToOperates);
            return removeBuilderDialogDO;
        }
    }
}
