package com.peng.idea.plugin.builder.action.additional;

import com.intellij.openapi.util.NlsActions;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/9
 * </pre>
 */
public class JumpToBuilderAdditionalActionV2 extends AbstractBuilderAdditionalActionV2 {

    public static final JumpToBuilderAdditionalActionV2 INSTANCE = new JumpToBuilderAdditionalActionV2();

    @Override
    public @NlsActions.ActionText @NotNull String getText() {
        return BuilderConstant.JumpToBuilder.POPUP_NAME;
    }

    @Override
    public Icon getIcon() {
        return BuilderConstant.JumpToBuilder.ICON;
    }

    @Override
    public void execute() {

    }
}
