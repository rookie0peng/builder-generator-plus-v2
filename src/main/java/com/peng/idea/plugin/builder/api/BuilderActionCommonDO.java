package com.peng.idea.plugin.builder.api;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/1 7:59
 * </pre>
 */
public class BuilderActionCommonDO {

    private Project project;

    private Editor editor;

    private PsiClass editorPsiClass;

    private PsiClass srcPsiClass;

    private PsiClass dstPsiClass;

    public static BuilderActionCommonDOBuilder builder() {
        return BuilderActionCommonDOBuilder.aBuilderActionCommonDO();
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

    public PsiClass getEditorPsiClass() {
        return editorPsiClass;
    }

    public void setEditorPsiClass(PsiClass editorPsiClass) {
        this.editorPsiClass = editorPsiClass;
    }

    public PsiClass getSrcPsiClass() {
        return srcPsiClass;
    }

    public void setSrcPsiClass(PsiClass srcPsiClass) {
        this.srcPsiClass = srcPsiClass;
    }

    public PsiClass getDstPsiClass() {
        return dstPsiClass;
    }

    public void setDstPsiClass(PsiClass dstPsiClass) {
        this.dstPsiClass = dstPsiClass;
    }

    @Override
    public String toString() {
        return "BuilderActionCommonDO{" +
                "project=" + project +
                ", editor=" + editor +
                ", editorPsiClass=" + editorPsiClass +
                ", srcPsiClass=" + srcPsiClass +
                ", dstPsiClass=" + dstPsiClass +
                '}';
    }

    public static final class BuilderActionCommonDOBuilder {
        private Project project;
        private Editor editor;
        private PsiClass editorPsiClass;
        private PsiClass srcPsiClass;
        private PsiClass dstPsiClass;

        private BuilderActionCommonDOBuilder() {
        }

        public static BuilderActionCommonDOBuilder aBuilderActionCommonDO() {
            return new BuilderActionCommonDOBuilder();
        }

        public BuilderActionCommonDOBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public BuilderActionCommonDOBuilder editor(Editor editor) {
            this.editor = editor;
            return this;
        }

        public BuilderActionCommonDOBuilder editorPsiClass(PsiClass editorPsiClass) {
            this.editorPsiClass = editorPsiClass;
            return this;
        }

        public BuilderActionCommonDOBuilder srcPsiClass(PsiClass srcPsiClass) {
            this.srcPsiClass = srcPsiClass;
            return this;
        }

        public BuilderActionCommonDOBuilder dstPsiClass(PsiClass dstPsiClass) {
            this.dstPsiClass = dstPsiClass;
            return this;
        }

        public BuilderActionCommonDO build() {
            BuilderActionCommonDO builderActionCommonDO = new BuilderActionCommonDO();
            builderActionCommonDO.setProject(project);
            builderActionCommonDO.setEditor(editor);
            builderActionCommonDO.setEditorPsiClass(editorPsiClass);
            builderActionCommonDO.setSrcPsiClass(srcPsiClass);
            builderActionCommonDO.setDstPsiClass(dstPsiClass);
            return builderActionCommonDO;
        }
    }
}
