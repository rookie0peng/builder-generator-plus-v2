package com.peng.idea.plugin.builder.action.handler;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.action.GoToBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RegenerateBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.base.AbstractBuilderActionHandler;
import com.peng.idea.plugin.builder.component.AbstractPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.AbstractPopupDisplayer;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;

import javax.swing.*;
import java.util.List;

/**
 * @description:
 * @author: qingpeng
 * @date: 2022/10/29 21:17
 */
public class GenerateBuilderActionHandler extends AbstractBuilderActionHandler {

    public GenerateBuilderActionHandler(
            AbstractPopupDisplayer popupDisplayer,
            AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> popupListComponent
    ) {
        super(popupDisplayer, popupListComponent);
    }

    @Override
    protected void doActionWhenClassToGoIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo, List<PsiMethod> buildMethods) {
        // 点击 Builder
        if (!isBuilder) {
            displayPopup(editor, classToGo, buildMethods);
        }
    }

    @Override
    protected void doActionWhenClassToGoIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, List<PsiMethod> buildMethods) {
        // 点击 Builder
        if (!isBuilder) {
            displayChoosers.run(null, buildMethods);
        }
    }

    private void displayPopup(Editor editor, PsiClass classToGo, List<PsiMethod> buildMethods) {
//        JList popupList = popupListFactory.getPopupList();
        JList<GotoTargetHandler.AdditionalAction> popupList = popupListComponent.getPopupList();
        popupDisplayer.displayPopupChooser(editor, popupList, () -> {
            if (popupList.getSelectedValue() instanceof GoToBuilderAdditionalAction) {
                PsiClassUtil.navigateToClass(classToGo);
            } else if (popupList.getSelectedValue() instanceof RegenerateBuilderAdditionalAction) {
                displayChoosers.run(classToGo, buildMethods);
            }
        });
    }
}

