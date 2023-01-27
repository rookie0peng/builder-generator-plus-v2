package com.peng.idea.plugin.builder.gui.displayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 13:29
 * </pre>
 */
public class GenerateBuilderPopupDisplayer extends AbstractPopupDisplayer {

    private static final String NEGATIVE_TITLE = "Builder already exists";

    public static final GenerateBuilderPopupDisplayer INSTANCE = new GenerateBuilderPopupDisplayer();

    @Override
    protected String getTitle() {
        return NEGATIVE_TITLE;
    }
}
