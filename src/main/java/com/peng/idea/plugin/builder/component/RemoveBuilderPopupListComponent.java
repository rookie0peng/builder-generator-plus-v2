package com.peng.idea.plugin.builder.component;

import com.intellij.codeInsight.navigation.GotoTargetHandler;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/8
 * </pre>
 */
public class RemoveBuilderPopupListComponent extends AbstractPopupListComponent<GotoTargetHandler.AdditionalAction> {

    public static final RemoveBuilderPopupListComponent INSTANCE = new RemoveBuilderPopupListComponent();

    @Override
    protected JList<GotoTargetHandler.AdditionalAction> createList() {
        return null;
    }
}
