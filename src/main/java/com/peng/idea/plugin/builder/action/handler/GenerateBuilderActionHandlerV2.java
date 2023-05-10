package com.peng.idea.plugin.builder.action.handler;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBList;
import com.peng.idea.plugin.builder.action.GoToBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RegenerateBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.RemoveBuilderAdditionalAction;
import com.peng.idea.plugin.builder.action.additional.*;
import com.peng.idea.plugin.builder.api.BuilderActionCommonDO;
import com.peng.idea.plugin.builder.api.GenerateBuilderDialogDO;
import com.peng.idea.plugin.builder.gui.GenerateBuilderDialogV2;
import com.peng.idea.plugin.builder.psi.PsiFieldSelector;
import com.peng.idea.plugin.builder.psi.model.PsiFieldsForBuilder;
import com.peng.idea.plugin.builder.rendener.ActionCellRenderer;
import com.peng.idea.plugin.builder.util.BuilderMethodFinderUtil;
import com.peng.idea.plugin.builder.util.Pair;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.dialog.MemberChooserDialogUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderFinderUtil;
import com.peng.idea.plugin.builder.util.psi.BuilderVerifierUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import com.peng.idea.plugin.builder.util.psi.PsiFieldsForBuilderUtil;
import com.peng.idea.plugin.builder.writter.BuilderContext;
import com.peng.idea.plugin.builder.writter.BuilderWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.Map;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeList;
import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/27
 * </pre>
 */
public class GenerateBuilderActionHandlerV2 extends AbstractBuilderActionHandlerV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateBuilderActionHandlerV2.class);

    public static final GenerateBuilderActionHandlerV2 INSTANCE = new GenerateBuilderActionHandlerV2();

    private static final String BUILDER_ALREADY_EXISTS = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_ALREADY_EXISTS;

    private static final String BUILDER_NOT_FOUND = BuilderConstant.GenerateBuilder.PopupChooserTitle.BUILDER_NOT_FOUND;

    private static final Map<Boolean, List<GotoTargetHandler.AdditionalAction>> EXISTS_BUILDER_TO_DIALOG_NAME_MAP = Map.of(
            true, List.of(
                    RegenerateBuilderAdditionalActionV2.INSTANCE,
                    JumpToBuilderAdditionalActionV2.INSTANCE,
                    GenerateBuilderAdditionalActionV2.INSTANCE,
                    RemoveBuilderAdditionalActionV2.INSTANCE
            ),
            false, List.of(
                    GenerateBuilderAdditionalActionV2.INSTANCE
            )
    );

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

    @Override
    public void run(BuilderActionCommonDO commonDO) {
//        if (
//                BuilderVerifierUtil.nonBuilder(commonDO.getEditorPsiClass())
//                        && BuilderVerifierUtil.nonBuilder(commonDO.getDstPsiClass())
//        ) {
//            Messages.showInfoMessage("Can't find builder class!", "Tips");
//            return;
//        }
        PsiPackage psiPackage = PsiClassUtil.getPackage(commonDO.getProject(), commonDO.getEditor());
        Pair<PsiClass, PsiClass> pair = getPossibleSrcClassAndBuilderClass(commonDO);
        GenerateBuilderDialogDO generateDO = GenerateBuilderDialogDO.builder()
                .project(commonDO.getProject()).editor(commonDO.getEditor()).editorPackage(psiPackage)
                .srcPsiClass(commonDO.getSrcPsiClass()).editorPsiClass(commonDO.getEditorPsiClass())
                .dstPsiClass(commonDO.getDstPsiClass())
                .possibleSrcPsiClass(pair.getFirst()).possibleBuilderPsiClass(pair.getSecond())
                .build();
        GenerateBuilderDialogV2 generateBuilderDialogV2 = new GenerateBuilderDialogV2(commonDO.getProject(), generateDO);
        generateBuilderDialogV2.show();
        if (generateBuilderDialogV2.isOK()) {
            PsiDirectory targetDirectory = generateBuilderDialogV2.getTargetDirectory();
            String className = generateBuilderDialogV2.getClassName();
            String builderMethodName = generateBuilderDialogV2.getBuilderMethodName();
            String methodPrefix = generateBuilderDialogV2.getMethodPrefix();
            boolean srcClassBuilder = generateBuilderDialogV2.getSrcClassBuilder();
            boolean innerBuilder = generateBuilderDialogV2.getInnerBuilder();
            boolean useSingleField = generateBuilderDialogV2.useSingleField();
            boolean hasButMethod = generateBuilderDialogV2.hasButMethod();
            List<PsiElementClassMember> fieldsToDisplay = PsiFieldSelector.getFieldsToIncludeInBuilder(
                    generateDO.getPossibleSrcPsiClass(), innerBuilder, useSingleField, hasButMethod
            );
            MemberChooser<PsiElementClassMember> memberChooserDialog = MemberChooserDialogUtil.getMemberChooserDialog(
                    fieldsToDisplay, generateDO.getProject()
            );
            memberChooserDialog.show();
            if (memberChooserDialog.isOK()) {
                List<PsiMethod> builderMethods = BuilderMethodFinderUtil.findBuilderMethodV2(generateDO.getPossibleSrcPsiClass());

                List<PsiElementClassMember> selectedElements = safeList(memberChooserDialog.getSelectedElements());
                PsiFieldsForBuilder psiFieldsForBuilder = PsiFieldsForBuilderUtil.createPsiFieldsForBuilder(selectedElements, generateDO.getPossibleSrcPsiClass());
                BuilderContext builderContext = BuilderContext.builder()
                        .project(generateDO.getProject()).psiFieldsForBuilder(psiFieldsForBuilder).targetDirectory(targetDirectory)
                        .className(className).builderMethodName(builderMethodName)

                        .psiClassFromEditor(generateDO.getPossibleSrcPsiClass()).methodPrefix(methodPrefix)
                        .srcClassBuilder(srcClassBuilder).isInner(innerBuilder).hasButMethod(hasButMethod)

                        .useSingleField(useSingleField)
                        .build();
//                BuilderContext context = new BuilderContext(
//                        generateDO.getProject(), psiFieldsForBuilder, targetDirectory, className, generateDO.getPossibleSrcPsiClass(), methodPrefix, innerBuilder, hasButMethod, useSingleField);
//                Application application = PsiClassUtil.getApplication();
//                application.runWriteAction(() -> {new BuilderWriterComputable(context, generateDO.getPossibleBuilderPsiClass(), builderMethods);});
                BuilderWriter.writeBuilder(builderContext, generateDO.getPossibleBuilderPsiClass(), builderMethods);
//                BuilderWriter.writeBuilder(context, existingBuilder, existingBuildMethods);
            }
        }
    }

    public void beforeRun(BuilderActionCommonDO commonDO) {
        boolean existsBuilder = (nonNull(commonDO.getSrcPsiClass()) && BuilderVerifierUtil.isBuilder(commonDO.getEditorPsiClass()))
                || BuilderVerifierUtil.isBuilder(commonDO.getDstPsiClass());
        List<GotoTargetHandler.AdditionalAction> popupChoosers = EXISTS_BUILDER_TO_DIALOG_NAME_MAP.get(existsBuilder);
        new JBList<>(List.of(
                RegenerateBuilderAdditionalAction.INSTANCE,
                GoToBuilderAdditionalAction.INSTANCE,
                RemoveBuilderAdditionalAction.INSTANCE
        ));
        JList<GotoTargetHandler.AdditionalAction> jList = new JBList<>(popupChoosers);
        jList.setCellRenderer(ActionCellRenderer.INSTANCE);

        Runnable runnable = () -> {
            GotoTargetHandler.AdditionalAction selectedValue = jList.getSelectedValue();
            AbstractBuilderActionHandlerV2 handlerV2 = BuilderActionHandlerFactory.DIALOG_NAME_TO_HANDLE_MAP.get(selectedValue.getText());
            if (nonNull(handlerV2))
                handlerV2.run(commonDO);
        };

        PopupChooserBuilder<?> builder = new PopupChooserBuilder<>(jList);
        builder.setTitle(existsBuilder ? BUILDER_ALREADY_EXISTS : BUILDER_NOT_FOUND).
                setItemChoosenCallback(runnable).
                setMovable(true).
                createPopup().showInBestPositionFor(commonDO.getEditor());
    }

    private Pair<PsiClass, PsiClass> getPossibleSrcClassAndBuilderClass(BuilderActionCommonDO commonDO) {
        if (nonNull(commonDO.getSrcPsiClass()) && BuilderVerifierUtil.isBuilder(commonDO.getEditorPsiClass())) {
            return Pair.of(commonDO.getSrcPsiClass(), commonDO.getEditorPsiClass());
        } else if (BuilderVerifierUtil.isBuilder(commonDO.getDstPsiClass())) {
            return Pair.of(commonDO.getEditorPsiClass(), commonDO.getDstPsiClass());
        } else {
            return Pair.of(commonDO.getEditorPsiClass(), null);
        }
    }
}
