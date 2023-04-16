package com.peng.idea.plugin.builder.action.handler;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.api.DisplayChooserDO;
import com.peng.idea.plugin.builder.util.Pair;
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
 *  @date: 2023/4/16 10:33
 * </pre>
 */
public class RemoveBuilderActionHandler extends AbstractBuilderActionHandlerV2 {



    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        // 1.点击 Builder，最初的入口(qp1)
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        PsiClass psiClassFromEditor = PsiClassUtil.getCursorPsiClass(editor, project).orElse(null);


        DisplayChooserDO build = DisplayChooserDO.builder()
                .project(project).editor(editor).psiClassFromEditor(psiClassFromEditor)
                .build();
//        prepareDisplayChoosers(editor, psiClassFromEditor, dataContext);
        if (psiClassFromEditor != null) {
            forwardToSpecificAction(editor, psiClassFromEditor, dataContext);
        }
    }

    private void forwardToSpecificAction(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext) {
        boolean isBuilder = BuilderVerifierUtil.isBuilder(psiClassFromEditor);

//        Pair<PsiClass, PsiClass> psiClassPair = this.findClassToOperate(psiClassFromEditor);
//        List<PsiMethod> buildMethods = findBuilderMethod(isBuilder ? classToGo : psiClassFromEditor);
//
//        if (classToGo != null) {
//            doActionWhenClassToGoIsFound(editor, psiClassFromEditor, dataContext, isBuilder, classToGo, buildMethods);
//        } else {
//            doActionWhenClassToGoIsNotFound(editor, psiClassFromEditor, dataContext, isBuilder, buildMethods);
//        }
    }

    /**
     * find source class of current class and builder class of current class
     * @param psiClassFromEditor PSI class of cursor editor
     * @return source class of current class and builder class of current class
     */
    private Pair<PsiClass, PsiClass> findClassToOperate(PsiClass psiClassFromEditor) {
        return Pair.of(BuilderFinderUtil.findClassForBuilder(psiClassFromEditor), BuilderFinderUtil.findBuilderForClass(psiClassFromEditor));
    }



}
