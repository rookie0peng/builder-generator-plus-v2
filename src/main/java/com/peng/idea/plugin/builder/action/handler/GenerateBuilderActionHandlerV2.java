package com.peng.idea.plugin.builder.action.handler;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.peng.idea.plugin.builder.action.GoToBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RegenerateBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RemoveBuilderAdditionalAction;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.gui.RemoveBuilderDialog;
import com.peng.idea.plugin.builder.util.BuildMethodFinderUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderVerifierUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeStream;
import static java.util.Objects.isNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/27
 * </pre>
 */
public class GenerateBuilderActionHandlerV2 extends AbstractBuilderActionHandlerV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBuilderActionHandlerV2.class);

    private static final String TITLE = "Generator Builder";

    private static final String BUILDER_ALREADY_EXISTS = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_ALREADY_EXISTS;

    private static final String BUILDER_NOT_FOUND = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_NOT_FOUND;

    private static final Map<Boolean, List<String>> EXISTS_BUILDER_TO_DIALOG_NAME_MAP = Map.of(
            true, List.of(
                    BuilderConstant.RegenerateBuilder.DIALOG_NAME,
                    BuilderConstant.GotoBuilder.DIALOG_NAME,
                    BuilderConstant.GenerateBuilder.DIALOG_NAME,
                    BuilderConstant.RemoveBuilder.DIALOG_NAME
            ),
            false, List.of(
                    BuilderConstant.GenerateBuilder.DIALOG_NAME
            )
    );



    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        // 1.点击 Builder，最初的入口(qp1)
        Project project = (Project) dataContext.getData(CommonDataKeys.PROJECT.getName());
        PsiClass editorPsiClass = PsiClassUtil.getCursorPsiClass(editor, project).orElse(null);
        PsiClass srcPsiClass = BuilderFinderUtil.findClassForBuilder(editorPsiClass);
        PsiClass dstPsiClass = BuilderFinderUtil.findBuilderForClass(editorPsiClass);
//        if (BuilderVerifierUtil.nonBuilder(editorPsiClass) && BuilderVerifierUtil.nonBuilder(dstPsiClass)) {
//            Messages.showInfoMessage("Can't find builder class!", "Tips");
//            return;
//        }


    }

    public void displayPopupChooser(boolean existsBuilder, Editor editor, Runnable runnable) {
        List<String> popupChoosers = EXISTS_BUILDER_TO_DIALOG_NAME_MAP.get(existsBuilder);

        JList<?> list = new JBList<>(popupChoosers);

        PopupChooserBuilder<?> builder = new PopupChooserBuilder<>(list);
        builder.setTitle(existsBuilder ? BUILDER_ALREADY_EXISTS : BUILDER_NOT_FOUND).
                setItemChoosenCallback(runnable).
                setMovable(true).
                createPopup().showInBestPositionFor(editor);
    }


}
