//package com.peng.idea.plugin.builder.action.handler;
//
//import com.intellij.codeInsight.navigation.GotoTargetHandler;
//import com.intellij.openapi.actionSystem.DataContext;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.psi.PsiClass;
//import com.intellij.psi.PsiMethod;
//import com.peng.idea.plugin.builder.action.base.AbstractBuilderActionHandler;
//import com.peng.idea.plugin.builder.component.AbstractPopupListComponent;
//import com.peng.idea.plugin.builder.gui.displayer.AbstractPopupDisplayer;
//import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
//
//import javax.swing.*;
//import java.util.List;
//
///**
// * <pre>
// *  @description:
// *  @author: qingpeng
// *  @date: 2023/4/7 20:21
// * </pre>
// */
//public class JumpToBuilderActionHandler extends AbstractBuilderActionHandler {
//
//    public JumpToBuilderActionHandler(
//            AbstractPopupDisplayer popupDisplayer,
//            AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> popupListComponent
//    ) {
//        super(popupDisplayer, popupListComponent);
//    }
//
//    @Override
//    protected void doActionWhenClassToJumpIsFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, PsiClass classToGo, List<PsiMethod> buildMethods) {
//        PsiClassUtil.navigateToClass(classToGo);
//    }
//
//    @Override
//    protected void doActionWhenClassToJumpIsNotFound(Editor editor, PsiClass psiClassFromEditor, DataContext dataContext, boolean isBuilder, List<PsiMethod> buildMethods) {
//        if (!isBuilder) {
//            displayPopup(editor, buildMethods);
//        }
//    }
//
//    private void displayPopup(Editor editor, List<PsiMethod> buildMethods) {
////        JList popupList = popupListFactory.getPopupList();
//        JList<GotoTargetHandler.AdditionalAction> popupList = popupListComponent.getPopupList();
//        popupDisplayer.displayPopupChooser(editor, popupList, () -> displayChoosers.run(null, buildMethods));
//    }
//}
//
