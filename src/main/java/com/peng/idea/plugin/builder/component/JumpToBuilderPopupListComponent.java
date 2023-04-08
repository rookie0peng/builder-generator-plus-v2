//package com.peng.idea.plugin.builder.component;
//
//import com.intellij.codeInsight.navigation.GotoTargetHandler;
//import com.intellij.ui.components.JBList;
//import com.peng.idea.plugin.builder.action.GenerateBuilderAdditionalAction;
//
//import javax.swing.*;
//
//import static java.util.Collections.singletonList;
//
///**
// * @description:
// * @author: qingpeng
// * @date: 2023/1/27 19:57
// */
//public class JumpToBuilderPopupListComponent extends AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> {
//
//    public static final JumpToBuilderPopupListComponent INSTANCE = new JumpToBuilderPopupListComponent();
//
//    @Override
//    protected JList<GotoTargetHandler.AdditionalAction> createList() {
//        return new JBList<>(singletonList(GenerateBuilderAdditionalAction.INSTANCE));
//    }
//}
