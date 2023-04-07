package com.peng.idea.plugin.builder.action;

import com.peng.idea.plugin.builder.action.base.AbstractBuilderAction;
import com.peng.idea.plugin.builder.action.handler.GoToBuilderActionHandler;
import com.peng.idea.plugin.builder.action.handler.JumpToBuilderActionHandler;
import com.peng.idea.plugin.builder.component.GoToBuilderPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.GoToBuilderPopupDisplayer;
import com.peng.idea.plugin.builder.gui.displayer.JumpToBuilderPopupDisplayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:03
 * </pre>
 */
public class JumpToBuilderAction extends AbstractBuilderAction {

    private static final JumpToBuilderActionHandler jumpToBuilderActionHandler =
            new JumpToBuilderActionHandler(JumpToBuilderPopupDisplayer.INSTANCE, GoToBuilderPopupListComponent.INSTANCE);

    public JumpToBuilderAction() {
        super(jumpToBuilderActionHandler);
    }
}
