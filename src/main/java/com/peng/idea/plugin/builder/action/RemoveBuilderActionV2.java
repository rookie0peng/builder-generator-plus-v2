package com.peng.idea.plugin.builder.action;

import com.peng.idea.plugin.builder.action.handler.RemoveBuilderActionHandlerV2;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/7 20:51
 * </pre>
 */
public class RemoveBuilderActionV2 extends AbstractBuilderActionV2 {

    private static final RemoveBuilderActionHandlerV2 REMOVE_BUILDER_ACTION_HANDLER =
            new RemoveBuilderActionHandlerV2();

    private static final String TITLE = "Builder not found";

    protected RemoveBuilderActionV2() {
        super(REMOVE_BUILDER_ACTION_HANDLER);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
