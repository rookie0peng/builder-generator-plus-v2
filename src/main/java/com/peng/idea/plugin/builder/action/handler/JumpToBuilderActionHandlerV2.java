package com.peng.idea.plugin.builder.action.handler;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBList;
import com.peng.idea.plugin.builder.action.additional.GenerateBuilderAdditionalActionV2;
import com.peng.idea.plugin.builder.api.BuilderActionCommonDO;
import com.peng.idea.plugin.builder.rendener.ActionCellRenderer;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderVerifierUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/1 7:33
 * </pre>
 */
public class JumpToBuilderActionHandlerV2 extends AbstractBuilderActionHandlerV2 {

    public static final JumpToBuilderActionHandlerV2 INSTANCE = new JumpToBuilderActionHandlerV2();

    private static final String BUILDER_ALREADY_EXISTS = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_ALREADY_EXISTS;

    private static final String BUILDER_NOT_FOUND = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_NOT_FOUND;

    private static final List<GotoTargetHandler.AdditionalAction> BUILDER_NOT_FOUND_LIST = List.of(GenerateBuilderAdditionalActionV2.INSTANCE);

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        // 1.点击 Builder，最初的入口(qp1)
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        PsiClass editorPsiClass = PsiClassUtil.getCursorPsiClass(editor, project).orElse(null);
        PsiClass srcPsiClass = BuilderFinderUtil.findClassForBuilder(editorPsiClass);
        PsiClass dstPsiClass = BuilderFinderUtil.findBuilderForClass(editorPsiClass);
        BuilderActionCommonDO commonDO = BuilderActionCommonDO.builder()
                .project(project).editor(editor).editorPsiClass(editorPsiClass).srcPsiClass(srcPsiClass)
                .dstPsiClass(dstPsiClass)
                .build();
        this.beforeRun(commonDO);
    }

    public void beforeRun(BuilderActionCommonDO commonDO) {
        boolean existsBuilder = (nonNull(commonDO.getSrcPsiClass()) && BuilderVerifierUtil.isBuilder(commonDO.getEditorPsiClass()))
                || BuilderVerifierUtil.isBuilder(commonDO.getDstPsiClass());
        if (existsBuilder) {
            if ((nonNull(commonDO.getSrcPsiClass()) && BuilderVerifierUtil.isBuilder(commonDO.getEditorPsiClass()))) {
                PsiClassUtil.navigateToClass(commonDO.getSrcPsiClass());
            } else {
                PsiClassUtil.navigateToClass(commonDO.getDstPsiClass());
            }
        } else {
            JList<GotoTargetHandler.AdditionalAction> jList = new JBList<>(BUILDER_NOT_FOUND_LIST);
            jList.setCellRenderer(ActionCellRenderer.INSTANCE);

            Runnable runnable = () -> {
                GotoTargetHandler.AdditionalAction selectedValue = jList.getSelectedValue();
                AbstractBuilderActionHandlerV2 handlerV2 = BuilderActionHandlerFactory.DIALOG_NAME_TO_HANDLE_MAP.get(selectedValue.getText());
                if (nonNull(handlerV2))
                    handlerV2.run(commonDO);
            };

            PopupChooserBuilder<?> builder = new PopupChooserBuilder<>(jList);
            builder.setTitle(BUILDER_NOT_FOUND)
                    .setItemChoosenCallback(runnable)
                    .setMovable(true)
                    .createPopup()
                    .showInBestPositionFor(commonDO.getEditor());
        }

    }

    @Override
    public void run(BuilderActionCommonDO commonDO) {
        if ((nonNull(commonDO.getSrcPsiClass()) && BuilderVerifierUtil.isBuilder(commonDO.getEditorPsiClass()))) {
            PsiClassUtil.navigateToClass(commonDO.getSrcPsiClass());
        } else {
            PsiClassUtil.navigateToClass(commonDO.getDstPsiClass());
        }
    }
}
