package com.peng.idea.plugin.builder.action.handler;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.peng.idea.plugin.builder.api.BuilderActionCommonDO;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.gui.RemoveBuilderDialogV2;
import com.peng.idea.plugin.builder.util.BuilderMethodFinderUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderVerifierUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeStream;
import static java.util.Objects.isNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/16 10:33
 * </pre>
 */
public class RemoveBuilderActionHandlerV2 extends AbstractBuilderActionHandlerV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveBuilderActionHandlerV2.class);

    public static final RemoveBuilderActionHandlerV2 INSTANCE = new RemoveBuilderActionHandlerV2();

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        // 1.点击 Builder，最初的入口(qp1)
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        PsiClass editorPsiClass = PsiClassUtil.getCursorPsiClass(editor, project).orElse(null);
        PsiClass srcPsiClass = BuilderFinderUtil.findClassForBuilder(editorPsiClass);
        PsiClass dstPsiClass = BuilderFinderUtil.findBuilderForClass(editorPsiClass);
        BuilderActionCommonDO commonDO = BuilderActionCommonDO.builder()
                .project(project).editor(editor).editorPsiClass(editorPsiClass).srcPsiClass(srcPsiClass)
                .dstPsiClass(dstPsiClass)
                .build();
        this.run(commonDO);
    }

    @Override
    public void run(BuilderActionCommonDO commonDO) {
        if (
                BuilderVerifierUtil.nonBuilder(commonDO.getEditorPsiClass())
                        && BuilderVerifierUtil.nonBuilder(commonDO.getDstPsiClass())
        ) {
            Messages.showInfoMessage("Can't find builder class!", "Tips");
            return;
        }
        RemoveBuilderDialogDO removeDO = RemoveBuilderDialogDO.builder()
                .project(commonDO.getProject()).editor(commonDO.getEditor()).srcPsiClass(commonDO.getSrcPsiClass())
                .editorPsiClass(commonDO.getEditorPsiClass()).dstPsiClass(commonDO.getDstPsiClass())
                .build();
        RemoveBuilderDialogV2 removeBuilderDialogV2 = new RemoveBuilderDialogV2(commonDO.getProject(), removeDO);
        removeBuilderDialogV2.show();
        if (removeBuilderDialogV2.isOK()) {
            LOGGER.info("Delete builder class success!");
            Runnable runnable = () -> safeStream(removeBuilderDialogV2.getTripleComponents()).forEach(component -> {
                if (isNull(component.getKey()))
                    return;
                if (!component.getCheckBox().isSelected())
                    return;
                switch (component.getKey()) {
                    case BuilderConstant.RemoveBuilder.DialogComponentKey.EDITOR_PSI_CLASS -> {
                        Optional.ofNullable(removeDO.getSrcPsiClass())
                                .map(BuilderMethodFinderUtil::findBuilderMethodV2)
                                .ifPresent(psiMethods -> psiMethods.forEach(PsiElement::delete));
                        Optional.ofNullable(removeDO.getEditorPsiClass()).ifPresent(PsiElement::delete);
                    }
                    case BuilderConstant.RemoveBuilder.DialogComponentKey.DST_PSI_CLASS -> {
                        Optional.ofNullable(removeDO.getEditorPsiClass())
                                .map(BuilderMethodFinderUtil::findBuilderMethodV2)
                                .ifPresent(psiMethods -> psiMethods.forEach(PsiElement::delete));
                        Optional.ofNullable(removeDO.getDstPsiClass()).ifPresent(PsiElement::delete);
                    }
                }
            });
            Application application = PsiClassUtil.getApplication();
            application.runWriteAction(runnable);
        }
        super.run(commonDO);
    }

    //    private void forwardToSpecificAction(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
//        boolean isBuilder = BuilderVerifierUtil.isBuilder(psiClassFromEditor);
//
//        Pair<PsiClass, PsiClass> psiClassPair = this.findClassToOperate(psiClassFromEditor);
//        List<PsiMethod> buildMethods = findBuilderMethod(isBuilder ? classToGo : psiClassFromEditor);
//
//        if (classToGo != null) {
//            doActionWhenClassToGoIsFound(editor, psiClassFromEditor, dataContext, isBuilder, classToGo, buildMethods);
//        } else {
//            doActionWhenClassToGoIsNotFound(editor, psiClassFromEditor, dataContext, isBuilder, buildMethods);
//        }
//    }
//
//    /**
//     * find source class of current class and builder class of current class
//     * @param psiClassFromEditor PSI class of cursor editor
//     * @return source class of current class and builder class of current class
//     */
//    private Pair<PsiClass, PsiClass> findClassToOperate(PsiClass psiClassFromEditor) {
//        return Pair.of(BuilderFinderUtil.findClassForBuilder(psiClassFromEditor), BuilderFinderUtil.findBuilderForClass(psiClassFromEditor));
//    }



}
