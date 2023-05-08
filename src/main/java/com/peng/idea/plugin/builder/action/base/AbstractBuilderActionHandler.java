package com.peng.idea.plugin.builder.action.base;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.component.AbstractPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.AbstractPopupDisplayer;
import com.peng.idea.plugin.builder.util.BuilderMethodFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderVerifierUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 11:16
 * </pre>
 */
public abstract class AbstractBuilderActionHandler extends EditorActionHandler {

    protected AbstractPopupDisplayer popupDisplayer;
    protected AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> popupListComponent;
    protected DisplayChoosers displayChoosers = new DisplayChoosers();

    public AbstractBuilderActionHandler(
            AbstractPopupDisplayer popupDisplayer,
            AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> popupListComponent
    ) {
        this.popupDisplayer = popupDisplayer;
        this.popupListComponent = popupListComponent;
    }

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        // 1.点击 Builder，最初的入口(qp1)
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        PsiClass psiClassFromEditor = PsiClassUtil.getCursorPsiClass(editor, project).orElse(null);
        prepareDisplayChoosers(editor, psiClassFromEditor, dataContext);
        if (psiClassFromEditor != null) {
            forwardToSpecificAction(editor, psiClassFromEditor, dataContext);
        }
    }

    private void prepareDisplayChoosers(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        displayChoosers.setEditor(editor);
        displayChoosers.setProject(project);
        displayChoosers.setPsiClassFromEditor(psiClassFromEditor);
    }

    private void forwardToSpecificAction(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        boolean isBuilder = BuilderVerifierUtil.isBuilder(psiClassFromEditor);
        PsiClass classToGo = findClassToGo(psiClassFromEditor, isBuilder);
        List<PsiMethod> buildMethods = findBuilderMethod(isBuilder ? classToGo : psiClassFromEditor);

        if (classToGo != null) {
            doActionWhenClassToGoIsFound(editor, psiClassFromEditor, dataContext, isBuilder, classToGo, buildMethods);
        } else {
            doActionWhenClassToGoIsNotFound(editor, psiClassFromEditor, dataContext, isBuilder, buildMethods);
        }
    }

    private PsiClass findClassToGo(PsiClass psiClassFromEditor, boolean isBuilder) {
        if (isBuilder) {
            return BuilderFinderUtil.findClassForBuilder(psiClassFromEditor);
        }
        return BuilderFinderUtil.findBuilderForClass(psiClassFromEditor);
    }

    private List<PsiMethod> findBuilderMethod(PsiClass dstPsiClass) {
        if (dstPsiClass == null)
            return List.of();
        return BuilderMethodFinderUtil.findBuilderMethodV2(dstPsiClass);
    }

    protected abstract void doActionWhenClassToGoIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo, List<PsiMethod> buildMethods);

    protected abstract void doActionWhenClassToGoIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, List<PsiMethod> buildMethods);

}


