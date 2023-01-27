package com.peng.idea.plugin.builder.action.base;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 11:15
 * </pre>
 */
public abstract class AbstractBuilderAction extends EditorAction {

    protected AbstractBuilderAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }
}
