package com.peng.idea.plugin.builder.rendener;

import com.intellij.codeInsight.navigation.GotoTargetHandler;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 14:01
 * </pre>
 */
public class ActionCellRenderer extends DefaultListCellRenderer {

    public static final DefaultListCellRenderer INSTANCE = new ActionCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            GotoTargetHandler.AdditionalAction action = (GotoTargetHandler.AdditionalAction) value;
            setText(action.getText());
            setIcon(action.getIcon());
        }
        return result;
    }

}
