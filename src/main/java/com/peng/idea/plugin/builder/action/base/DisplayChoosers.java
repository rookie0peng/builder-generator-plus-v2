package com.peng.idea.plugin.builder.action.base;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.peng.idea.plugin.builder.gui.CreateBuilderDialog;
import com.peng.idea.plugin.builder.psi.PsiFieldSelector;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;
import com.peng.idea.plugin.builder.util.dialog.CreateBuilderDialogUtil;
import com.peng.idea.plugin.builder.util.dialog.MemberChooserDialogUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderGenerateUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import com.peng.idea.plugin.builder.util.psi.PsiFieldsForBuilderUtil;
import com.peng.idea.plugin.builder.writter.BuilderContext;
import com.peng.idea.plugin.builder.writter.BuilderWriter;

import java.util.List;

import static com.peng.idea.plugin.builder.util.CollectionUtil.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:04
 * </pre>
 */
public class DisplayChoosers {

    private PsiClass psiClassFromEditor;
    private Project project;
    private Editor editor;

    @SuppressWarnings("rawtypes")
    public void run(PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
        CreateBuilderDialog createBuilderDialog = this.showDialog(existingBuilder);
        if (createBuilderDialog.isOK()) {
            PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
            String className = createBuilderDialog.getClassName();
            String methodPrefix = createBuilderDialog.getMethodPrefix();
            boolean innerBuilder = createBuilderDialog.isInnerBuilder();
            boolean useSingleField = createBuilderDialog.useSingleField();
            boolean hasButMethod = createBuilderDialog.hasButMethod();
            List<PsiElementClassMember> fieldsToDisplay = this.getFieldsToIncludeInBuilder(psiClassFromEditor, innerBuilder, useSingleField, hasButMethod);
            MemberChooser<PsiElementClassMember> memberChooserDialog = MemberChooserDialogUtil.getMemberChooserDialog(fieldsToDisplay, project);
            memberChooserDialog.show();
            this.writeBuilderIfNecessary(
                    targetDirectory, className, methodPrefix, memberChooserDialog, createBuilderDialog,
                    existingBuilder, existingBuildMethods
            );
        }
    }

    @SuppressWarnings("rawtypes")
    public void runRemove(PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
        CreateBuilderDialog createBuilderDialog = this.showDialog(existingBuilder);
        if (createBuilderDialog.isOK()) {
            PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
            String className = createBuilderDialog.getClassName();
            String methodPrefix = createBuilderDialog.getMethodPrefix();
            boolean innerBuilder = createBuilderDialog.isInnerBuilder();
            boolean useSingleField = createBuilderDialog.useSingleField();
            boolean hasButMethod = createBuilderDialog.hasButMethod();
            List<PsiElementClassMember> fieldsToDisplay = this.getFieldsToIncludeInBuilder(psiClassFromEditor, innerBuilder, useSingleField, hasButMethod);
            MemberChooser<PsiElementClassMember> memberChooserDialog = MemberChooserDialogUtil.getMemberChooserDialog(fieldsToDisplay, project);
            memberChooserDialog.show();
            this.writeBuilderIfNecessary(
                    targetDirectory, className, methodPrefix, memberChooserDialog, createBuilderDialog,
                    existingBuilder, existingBuildMethods
            );
        }
    }

    @SuppressWarnings("rawtypes")
    private void writeBuilderIfNecessary(
            PsiDirectory targetDirectory, String className, String methodPrefix,
            MemberChooser<PsiElementClassMember> memberChooserDialog, CreateBuilderDialog createBuilderDialog,
            PsiClass existingBuilder, List<PsiMethod> existingBuildMethods
    ) {
        if (memberChooserDialog.isOK()) {
            List<PsiElementClassMember> selectedElements = safeList(memberChooserDialog.getSelectedElements());
            PsiFieldsForBuilder psiFieldsForBuilder = PsiFieldsForBuilderUtil.createPsiFieldsForBuilder(selectedElements, psiClassFromEditor);
            BuilderContext builderContext = BuilderContext.builder()
                    .project(project).psiFieldsForBuilder(psiFieldsForBuilder).targetDirectory(targetDirectory)
                    .className(className).builderMethodName(BuilderGenerateUtil.builderMethodName(className))

                    .psiClassFromEditor(psiClassFromEditor).methodPrefix(methodPrefix)
                    .srcClassBuilder(true).isInner(createBuilderDialog.isInnerBuilder()).hasButMethod(createBuilderDialog.hasButMethod())

                    .useSingleField(createBuilderDialog.useSingleField())
                    .build();
//            BuilderContext context = new BuilderContext(
//                    project, psiFieldsForBuilder, targetDirectory, className, psiClassFromEditor, methodPrefix, createBuilderDialog.isInnerBuilder(), createBuilderDialog.hasButMethod(), createBuilderDialog.useSingleField());
            BuilderWriter.writeBuilder(builderContext, existingBuilder, existingBuildMethods);
        }
    }

    private CreateBuilderDialog showDialog(PsiClass existingBuilder) {
        PsiDirectory srcDir = PsiClassUtil.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = PsiClassUtil.getPackage(srcDir);
        CreateBuilderDialog dialog = CreateBuilderDialogUtil.createBuilderDialog(psiClassFromEditor, project, srcPackage, existingBuilder);
        dialog.show();
        return dialog;
    }

    private CreateBuilderDialog showDialogRemove(PsiClass existingBuilder) {
        PsiDirectory srcDir = PsiClassUtil.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = PsiClassUtil.getPackage(srcDir);
        CreateBuilderDialog dialog = CreateBuilderDialogUtil.createBuilderDialog(psiClassFromEditor, project, srcPackage, existingBuilder);
        dialog.show();
        return dialog;
    }

    @SuppressWarnings("rawtypes")
    private List<PsiElementClassMember> getFieldsToIncludeInBuilder(PsiClass clazz, boolean innerBuilder, boolean useSingleField, boolean hasButMethod) {
        return PsiFieldSelector.selectFieldsToIncludeInBuilder(clazz, innerBuilder, useSingleField, hasButMethod);
    }

    public void setPsiClassFromEditor(PsiClass psiClassFromEditor) {
        this.psiClassFromEditor = psiClassFromEditor;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}

