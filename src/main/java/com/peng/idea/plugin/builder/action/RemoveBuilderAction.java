package com.peng.idea.plugin.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.peng.idea.plugin.builder.action.base.AbstractBuilderAction;
import com.peng.idea.plugin.builder.action.handler.GenerateBuilderActionHandler;
import com.peng.idea.plugin.builder.action.handler.RemoveBuilderActionHandler;
import com.peng.idea.plugin.builder.component.GenerateBuilderPopupListComponent;
import com.peng.idea.plugin.builder.component.RemoveBuilderPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.GenerateBuilderPopupDisplayer;
import com.peng.idea.plugin.builder.gui.displayer.RemoveBuilderPopupDisplayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/7 20:51
 * </pre>
 */
public class RemoveBuilderAction extends AbstractBuilderAction {

    private static final RemoveBuilderActionHandler generateBuilderActionHandler =
            new RemoveBuilderActionHandler(RemoveBuilderPopupDisplayer.INSTANCE, RemoveBuilderPopupListComponent.INSTANCE);

    protected RemoveBuilderAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }
}
