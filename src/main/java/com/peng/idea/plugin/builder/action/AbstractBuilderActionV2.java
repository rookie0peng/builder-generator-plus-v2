package com.peng.idea.plugin.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.peng.idea.plugin.builder.action.handler.GoToBuilderActionHandler;
import com.peng.idea.plugin.builder.action.handler.RemoveBuilderActionHandler;
import com.peng.idea.plugin.builder.component.GoToBuilderPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.GoToBuilderPopupDisplayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/16 10:34
 * </pre>
 */
public abstract class AbstractBuilderActionV2 extends EditorAction {

    protected AbstractBuilderActionV2(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    abstract public  String getTitle();
}
