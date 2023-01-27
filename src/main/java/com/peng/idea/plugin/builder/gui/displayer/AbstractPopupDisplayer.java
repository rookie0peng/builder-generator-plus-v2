package com.peng.idea.plugin.builder.gui.displayer;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 13:28
 * </pre>
 */
public abstract class AbstractPopupDisplayer {

    @SuppressWarnings("rawtypes")
    public void displayPopupChooser(Editor editor, JList list, Runnable runnable) {
        PopupChooserBuilder builder = new PopupChooserBuilder(list);
        builder.setTitle(getTitle()).
                setItemChoosenCallback(runnable).
                setMovable(true).
                createPopup().showInBestPositionFor(editor);
    }

    protected abstract String getTitle();
}
