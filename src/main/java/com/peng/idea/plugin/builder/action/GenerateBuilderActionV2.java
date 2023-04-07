package com.peng.idea.plugin.builder.action;

import com.peng.idea.plugin.builder.action.base.AbstractBuilderAction;
import com.peng.idea.plugin.builder.action.handler.GenerateBuilderActionHandler;
import com.peng.idea.plugin.builder.component.GenerateBuilderPopupListComponent;
import com.peng.idea.plugin.builder.gui.displayer.GenerateBuilderPopupDisplayer;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 13:15
 * </pre>
 */
public class GenerateBuilderActionV2 extends AbstractBuilderAction {

    private static final GenerateBuilderActionHandler generateBuilderActionHandler =
            new GenerateBuilderActionHandler(GenerateBuilderPopupDisplayer.INSTANCE, GenerateBuilderPopupListComponent.INSTANCE);

    protected GenerateBuilderActionV2() {
        super(generateBuilderActionHandler);
    }
}
