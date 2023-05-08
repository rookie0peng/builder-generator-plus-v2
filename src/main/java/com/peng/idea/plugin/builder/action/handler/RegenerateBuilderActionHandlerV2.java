package com.peng.idea.plugin.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.peng.idea.plugin.builder.api.BuilderActionCommonDO;
import com.peng.idea.plugin.builder.api.GenerateBuilderDialogDO;
import com.peng.idea.plugin.builder.gui.GenerateBuilderDialogV2;
import com.peng.idea.plugin.builder.psi.PsiFieldSelector;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;
import com.peng.idea.plugin.builder.util.BuilderMethodFinderUtil;
import com.peng.idea.plugin.builder.util.dialog.MemberChooserDialogUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import com.peng.idea.plugin.builder.util.psi.PsiFieldsForBuilderUtil;
import com.peng.idea.plugin.builder.writter.BuilderContext;
import com.peng.idea.plugin.builder.writter.BuilderWriterComputable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeList;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/1 7:31
 * </pre>
 */
public class RegenerateBuilderActionHandlerV2 extends AbstractBuilderActionHandlerV2 {

    public static final RegenerateBuilderActionHandlerV2 INSTANCE = new RegenerateBuilderActionHandlerV2();

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
        this.run(commonDO);
    }

    @Override
    public void run(BuilderActionCommonDO commonDO) {
        GenerateBuilderActionHandlerV2.INSTANCE.run(commonDO);
    }
}
