package com.peng.idea.plugin.builder.action;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.peng.idea.plugin.builder.action.handler.JumpToBuilderActionHandlerV2;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/9
 * </pre>
 */
public class JumpToBuilderActionV2 extends EditorAction {

    private static final JumpToBuilderActionHandlerV2 jumpToBuilderActionHandlerV2 = new JumpToBuilderActionHandlerV2();

    protected JumpToBuilderActionV2() {
        super(jumpToBuilderActionHandlerV2);
    }
}
