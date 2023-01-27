package com.peng.idea.plugin.builder.action;

import com.peng.idea.plugin.builder.action.base.AbstractBuilderAction;
import com.peng.idea.plugin.builder.action.handler.GoToBuilderActionHandler;
import com.peng.idea.plugin.builder.component.GoToBuilderPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.GoToBuilderPopupDisplayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:03
 * </pre>
 */
public class GoToBuilderAction extends AbstractBuilderAction {

    private static final GoToBuilderActionHandler goToBuilderActionHandler =
            new GoToBuilderActionHandler(GoToBuilderPopupDisplayer.INSTANCE, GoToBuilderPopupListComponent.INSTANCE);

    public GoToBuilderAction() {
        super(goToBuilderActionHandler);
    }
}
