package com.peng.idea.plugin.builder.util.dialog;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.peng.idea.plugin.builder.api.CreateBuilderDialogDO;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.gui.CreateBuilderDialog;
import com.peng.idea.plugin.builder.gui.RemoveBuilderDialog;
import com.peng.idea.plugin.builder.psi.PsiFieldSelector;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import com.peng.idea.plugin.builder.util.psi.PsiFieldsForBuilderUtil;
import com.peng.idea.plugin.builder.writter.BuilderContext;
import com.peng.idea.plugin.builder.writter.BuilderWriter;

import java.util.List;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeList;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/16 11:42
 * </pre>
 */
public class DisplayChooserUtil {

    @SuppressWarnings("rawtypes")
    public static void run(CreateBuilderDialogDO chooserDO) {
        CreateBuilderDialog createBuilderDialog = showDialog(chooserDO);
        if (createBuilderDialog.isOK()) {
            boolean innerBuilder = createBuilderDialog.isInnerBuilder();
            boolean useSingleField = createBuilderDialog.useSingleField();
            boolean hasButMethod = createBuilderDialog.hasButMethod();
            List<PsiElementClassMember> fieldsToDisplay = getFieldsToIncludeInBuilder(chooserDO.getEditorPsiClass(), innerBuilder, useSingleField, hasButMethod);
            MemberChooser<PsiElementClassMember> memberChooserDialog = MemberChooserDialogUtil.getMemberChooserDialog(fieldsToDisplay, chooserDO.getProject());
            memberChooserDialog.show();
            writeBuilderIfNecessary(memberChooserDialog, createBuilderDialog, chooserDO);
        }

    }

//    public void run(PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
//        CreateBuilderDialog createBuilderDialog = showDialog(existingBuilder);
//        if (createBuilderDialog.isOK()) {
//            PsiDirectory targetDirectory = createBuilderDialog.getTargetDirectory();
//            String className = createBuilderDialog.getClassName();
//            String methodPrefix = createBuilderDialog.getMethodPrefix();
//            boolean innerBuilder = createBuilderDialog.isInnerBuilder();
//            boolean useSingleField = createBuilderDialog.useSingleField();
//            boolean hasButMethod = createBuilderDialog.hasButMethod();
//            List<PsiElementClassMember> fieldsToDisplay = this.getFieldsToIncludeInBuilder(psiClassFromEditor, innerBuilder, useSingleField, hasButMethod);
//            MemberChooser<PsiElementClassMember> memberChooserDialog = MemberChooserDialogUtil.getMemberChooserDialog(fieldsToDisplay, project);
//            memberChooserDialog.show();
//            this.writeBuilderIfNecessary(
//                    targetDirectory, className, methodPrefix, memberChooserDialog, createBuilderDialog,
//                    existingBuilder, existingBuildMethods
//            );
//        }
//    }

    private static CreateBuilderDialog showDialog(CreateBuilderDialogDO chooserDO) {
        Editor editor = chooserDO.getEditor();
        Project project = chooserDO.getProject();
        PsiClass psiClassFromEditor = chooserDO.getEditorPsiClass();
        PsiClass classToOperate = chooserDO.getClassToOperate();
        PsiDirectory srcDir = PsiClassUtil.getPsiFileFromEditor(editor, project).getContainingDirectory();
        PsiPackage srcPackage = PsiClassUtil.getPackage(srcDir);
        CreateBuilderDialog dialog = CreateBuilderDialogUtil.createBuilderDialog(psiClassFromEditor, project, srcPackage, classToOperate);
        dialog.show();
        return dialog;
    }

//    private static CreateBuilderDialog showDialog(PsiClass existingBuilder) {
//        PsiDirectory srcDir = PsiClassUtil.getPsiFileFromEditor(editor, project).getContainingDirectory();
//        PsiPackage srcPackage = PsiClassUtil.getPackage(srcDir);
//        CreateBuilderDialog dialog = CreateBuilderDialogUtil.createBuilderDialog(psiClassFromEditor, project, srcPackage, existingBuilder);
//        dialog.show();
//        return dialog;
//    }

    @SuppressWarnings("rawtypes")
    private static List<PsiElementClassMember> getFieldsToIncludeInBuilder(PsiClass clazz, boolean innerBuilder, boolean useSingleField, boolean hasButMethod) {
        return PsiFieldSelector.selectFieldsToIncludeInBuilder(clazz, innerBuilder, useSingleField, hasButMethod);
    }

    @SuppressWarnings("rawtypes")
    private static void writeBuilderIfNecessary(
            MemberChooser<PsiElementClassMember> memberDialog, CreateBuilderDialog createDialog, CreateBuilderDialogDO chooserDO
    ) {
        if (memberDialog.isOK()) {
            Project project = chooserDO.getProject();
            PsiClass psiClassFromEditor = chooserDO.getEditorPsiClass();
            PsiClass classToOperate = chooserDO.getClassToOperate();
            List<PsiMethod> buildMethodToOperates = chooserDO.getBuildMethodToOperates();
            List<PsiElementClassMember> selectedElements = safeList(memberDialog.getSelectedElements());
            PsiFieldsForBuilder psiFieldsForBuilder = PsiFieldsForBuilderUtil.createPsiFieldsForBuilder(selectedElements, psiClassFromEditor);
            BuilderContext context = BuilderContext.builder()
                    .project(project).psiFieldsForBuilder(psiFieldsForBuilder).targetDirectory(createDialog.getTargetDirectory())
                    .className(createDialog.getClassName()).psiClassFromEditor(psiClassFromEditor)

                    .methodPrefix(createDialog.getMethodPrefix()).isInner(createDialog.isInnerBuilder())
                    .hasButMethod(createDialog.hasButMethod()).useSingleField(createDialog.useSingleField())

                    .build();
            BuilderWriter.writeBuilder(context, classToOperate, buildMethodToOperates);
        }
    }
}
