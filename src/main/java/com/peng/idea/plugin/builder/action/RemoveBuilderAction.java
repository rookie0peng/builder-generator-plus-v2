package com.peng.idea.plugin.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.peng.idea.plugin.builder.action.handler.RemoveBuilderActionHandler;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/7 20:51
 * </pre>
 */
public class RemoveBuilderAction extends AbstractBuilderActionV2 {

    private static final RemoveBuilderActionHandler REMOVE_BUILDER_ACTION_HANDLER =
            new RemoveBuilderActionHandler();

    private static final String TITLE = "Builder not found";

    protected RemoveBuilderAction() {
        super(REMOVE_BUILDER_ACTION_HANDLER);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
