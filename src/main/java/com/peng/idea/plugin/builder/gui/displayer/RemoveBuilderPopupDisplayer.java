package com.peng.idea.plugin.builder.gui.displayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 13:29
 * </pre>
 */
public class RemoveBuilderPopupDisplayer extends AbstractPopupDisplayer {

    private static final String NEGATIVE_TITLE = "Builder already exists";

    public static final RemoveBuilderPopupDisplayer INSTANCE = new RemoveBuilderPopupDisplayer();

    @Override
    protected String getTitle() {
        return NEGATIVE_TITLE;
    }
}
