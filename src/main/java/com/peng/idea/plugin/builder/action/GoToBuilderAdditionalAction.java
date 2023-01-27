package com.peng.idea.plugin.builder.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.peng.idea.plugin.builder.action.base.AbstractBuilderAdditionalAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 15:03
 * </pre>
 */
public class GoToBuilderAdditionalAction extends AbstractBuilderAdditionalAction {

    public static final GoToBuilderAdditionalAction INSTANCE = new GoToBuilderAdditionalAction();

    private static final String TEXT = "Go to builder generator plus...";
    private static final Icon ICON = IconLoader.getIcon("/actions/intentionBulb.png", AllIcons.class);

    @NotNull
    @Override
    public String getText() {
        return TEXT;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @Override
    public void execute() {
    }
}
