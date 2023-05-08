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

    /**
     * possible src PSI class of editor PSI class
     */
    private PsiClass possibleSrcPsiClass;

    /**
     * possible builder PSI class of editor PSI class
     */
    private PsiClass possibleBuilderPsiClass;

    public static GenerateBuilderDialogDOBuilder builder() {
        return GenerateBuilderDialogDOBuilder.aGenerateBuilderDialogDO();
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

    public PsiPackage getEditorPackage() {
        return editorPackage;
    }

    public void setEditorPackage(PsiPackage editorPackage) {
        this.editorPackage = editorPackage;
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

    public PsiClass getPossibleSrcPsiClass() {
        return possibleSrcPsiClass;
    }

    public void setPossibleSrcPsiClass(PsiClass possibleSrcPsiClass) {
        this.possibleSrcPsiClass = possibleSrcPsiClass;
    }

    public PsiClass getPossibleBuilderPsiClass() {
        return possibleBuilderPsiClass;
    }

    public void setPossibleBuilderPsiClass(PsiClass possibleBuilderPsiClass) {
        this.possibleBuilderPsiClass = possibleBuilderPsiClass;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenerateBuilderDialogDO.class.getSimpleName() + "[", "]")
                .add("project=" + project)
                .add("editor=" + editor)
                .add("editorPackage=" + editorPackage)
                .add("srcPsiClass=" + srcPsiClass)
                .add("editorPsiClass=" + editorPsiClass)
                .add("dstPsiClass=" + dstPsiClass)
                .add("possibleSrcPsiClass=" + possibleSrcPsiClass)
                .add("possibleBuilderPsiClass=" + possibleBuilderPsiClass)
                .toString();
    }

    public static final class GenerateBuilderDialogDOBuilder {
        private Project project;
        private Editor editor;
        private PsiPackage editorPackage;
        private PsiClass srcPsiClass;
        private PsiClass editorPsiClass;
        private PsiClass dstPsiClass;
        private PsiClass possibleSrcPsiClass;
        private PsiClass possibleBuilderPsiClass;

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

        public GenerateBuilderDialogDOBuilder srcPsiClass(PsiClass srcPsiClass) {
            this.srcPsiClass = srcPsiClass;
            return this;
        }

        public GenerateBuilderDialogDOBuilder editorPsiClass(PsiClass editorPsiClass) {
            this.editorPsiClass = editorPsiClass;
            return this;
        }

        public GenerateBuilderDialogDOBuilder dstPsiClass(PsiClass dstPsiClass) {
            this.dstPsiClass = dstPsiClass;
            return this;
        }

        public GenerateBuilderDialogDOBuilder possibleSrcPsiClass(PsiClass possibleSrcPsiClass) {
            this.possibleSrcPsiClass = possibleSrcPsiClass;
            return this;
        }

        public GenerateBuilderDialogDOBuilder possibleBuilderPsiClass(PsiClass possibleBuilderPsiClass) {
            this.possibleBuilderPsiClass = possibleBuilderPsiClass;
            return this;
        }

        public GenerateBuilderDialogDO build() {
            GenerateBuilderDialogDO generateBuilderDialogDO = new GenerateBuilderDialogDO();
            generateBuilderDialogDO.setProject(project);
            generateBuilderDialogDO.setEditor(editor);
            generateBuilderDialogDO.setEditorPackage(editorPackage);
            generateBuilderDialogDO.setSrcPsiClass(srcPsiClass);
            generateBuilderDialogDO.setEditorPsiClass(editorPsiClass);
            generateBuilderDialogDO.setDstPsiClass(dstPsiClass);
            generateBuilderDialogDO.setPossibleSrcPsiClass(possibleSrcPsiClass);
            generateBuilderDialogDO.setPossibleBuilderPsiClass(possibleBuilderPsiClass);
            return generateBuilderDialogDO;
        }
    }
}
