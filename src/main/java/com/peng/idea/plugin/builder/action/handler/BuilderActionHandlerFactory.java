package com.peng.idea.plugin.builder.action.handler;

import com.peng.idea.plugin.builder.util.constant.BuilderConstant;

import java.util.Map;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/1 7:44
 * </pre>
 */
public class BuilderActionHandlerFactory {

    public static final Map<String, AbstractBuilderActionHandlerV2> DIALOG_NAME_TO_HANDLE_MAP = Map.of(
            BuilderConstant.RegenerateBuilder.POPUP_NAME, RegenerateBuilderActionHandlerV2.INSTANCE,
            BuilderConstant.JumpToBuilder.POPUP_NAME, JumpToBuilderActionHandlerV2.INSTANCE,
            BuilderConstant.GenerateBuilder.POPUP_NAME, GenerateBuilderActionHandlerV2.INSTANCE,
            BuilderConstant.RemoveBuilder.POPUP_NAME, RemoveBuilderActionHandlerV2.INSTANCE
    );
}
