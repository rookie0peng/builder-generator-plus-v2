package com.peng.idea.plugin.builder.component;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.ui.components.JBList;
import com.peng.idea.plugin.builder.action.GoToBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RegenerateBuilderAdditionalAction;

import javax.swing.*;

import java.util.List;

/**
 * @description:
 * @author: qingpeng
 * @date: 2023/1/27 19:50
 */
public class GenerateBuilderPopupListComponent extends AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> {

    public static final GenerateBuilderPopupListComponent INSTANCE = new GenerateBuilderPopupListComponent();

    @Override
    protected JList<GotoTargetHandler.AdditionalAction> createList() {
        return new JBList<>(List.of(GoToBuilderAdditionalAction.INSTANCE, RegenerateBuilderAdditionalAction.INSTANCE));
    }
}
