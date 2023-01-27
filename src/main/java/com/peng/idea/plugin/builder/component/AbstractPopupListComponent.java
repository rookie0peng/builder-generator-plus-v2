package com.peng.idea.plugin.builder.component;

import com.peng.idea.plugin.builder.rendener.ActionCellRenderer;

import javax.swing.*;

/**
 * @description:
 * @author: qingpeng
 * @date: 2023/1/27 19:15
 */
public abstract class AbstractPopupListComponent<T> {

    private ActionCellRenderer actionCellRenderer;

    public JList<T> getPopupList() {
        JList<T> list = createList();
        list.setCellRenderer(cellRenderer());
        return list;
    }

    protected abstract JList<T> createList();

    private ActionCellRenderer cellRenderer() {
        if (actionCellRenderer == null) {
            actionCellRenderer = new ActionCellRenderer();
        }
        return actionCellRenderer;
    }
}
