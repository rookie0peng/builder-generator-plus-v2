package com.peng.idea.plugin.builder.gui.displayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 13:30
 * </pre>
 */
public class GoToBuilderPopupDisplayer extends AbstractPopupDisplayer {

    public static final GoToBuilderPopupDisplayer INSTANCE = new GoToBuilderPopupDisplayer();

    private static final String NEGATIVE_TITLE = "Builder not found";

    @Override
    protected String getTitle() {
        return NEGATIVE_TITLE;
    }
}
