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
public class RegenerateBuilderAdditionalActionV2 extends AbstractBuilderAdditionalActionV2 {

    public static final RegenerateBuilderAdditionalActionV2 INSTANCE = new RegenerateBuilderAdditionalActionV2();

    @Override
    public @NlsActions.ActionText @NotNull String getText() {
        return BuilderConstant.RegenerateBuilder.POPUP_NAME;
    }

    @Override
    public Icon getIcon() {
        return BuilderConstant.RegenerateBuilder.ICON;
    }

    @Override
    public void execute() {

    }
}
