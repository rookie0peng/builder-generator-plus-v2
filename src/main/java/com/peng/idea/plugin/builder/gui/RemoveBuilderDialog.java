package com.peng.idea.plugin.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.api.TripleComponentDO;
import com.peng.idea.plugin.builder.util.BuildMethodFinderUtil;
import com.peng.idea.plugin.builder.util.Pair;
import com.peng.idea.plugin.builder.util.PanelUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.dialog.GuiUtil;
import com.peng.idea.plugin.builder.util.psi.GuiHelperUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.*;
import static com.peng.idea.plugin.builder.util.CollectionUtil.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/8
 * </pre>
 */
public class RemoveBuilderDialog extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveBuilderDialog.class);

    private final Project project;

    private final RemoveBuilderDialogDO removeDO;

    private JPanel myMainPanel;

    private List<TripleComponentDO> tripleComponents;

    public RemoveBuilderDialog(@Nullable Project project, @Nullable RemoveBuilderDialogDO removeDO) {
        super(project, true);
        this.project = project;
        this.removeDO = removeDO;
        setTitle(BuilderConstant.RemoveBuilder.DIALOG_NAME);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return initFields();
    }



    @Override
    public void show() {
        super.init();
        super.show();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
    }

    @Override
    protected void doOKAction() {
//        LOGGER.info("click OK action!");
//        try {
//        } catch (Exception e) {
//            LOGGER.error("Click Remove Builder Dialog Exception: ", e);
//            GuiHelperUtil.showMessageDialog(project, e.getMessage(), CommonBundle.getErrorTitle(), Messages.getErrorIcon());
//        }
        callSuper();
    }

    @Override
    protected void doHelpAction() {
        Messages.showInfoMessage(
                "The selected Builder class will be removed!",
                "Tips");
//        super.doHelpAction();
    }

    void callSuper() {
        super.doOKAction();
    }

    private JPanel initFields() {
        PanelUtil builder = PanelUtil.builder();

        // column name
        builder.addTripleComponent(new JLabel("Class name"), new JLabel("Qualified name"), new JLabel("Select"));
        // column name

        // builder class to remove
        List<Pair<String, PsiClass>> pairs = Stream.of(
                        Pair.of(BuilderConstant.RemoveBuilder.DialogComponentKey.EDITOR_PSI_CLASS, removeDO.getEditorPsiClass()),
                        Pair.of(BuilderConstant.RemoveBuilder.DialogComponentKey.DST_PSI_CLASS, removeDO.getDstPsiClass())
                )
                .filter(pair -> nonNull(pair.getSecond()))
                .filter(pair -> nonNull(pair.getSecond().getName()) && pair.getSecond().getName().endsWith(BuilderConstant.BUILDER_SUFFIX))
                .toList();
        tripleComponents = pairs.stream().map(pair -> {
            String key = pair.getFirst();
            PsiClass psiClass = pair.getSecond();
            return TripleComponentDO.builder()
                    .key(pair.getFirst())
                    .clazzName(GuiUtil.jTextField(psiClass.getName(), false))
                    .qualifiedName(GuiUtil.jTextField(psiClass.getQualifiedName(), false))
                    .checkBox(GuiUtil.jCheckBox(pairs.size() == 1 || key.equals("2")))
                    .build();
        }).toList();
        tripleComponents.forEach(triple -> builder.addTripleComponent(
                triple.getClazzName(), triple.getQualifiedName(), triple.getCheckBox()
        ));
        // builder class to remove

        myMainPanel = builder.getPanel();
        return myMainPanel;
    }

    public RemoveBuilderDialogDO getRemoveDO() {
        return removeDO;
    }

    public List<TripleComponentDO> getTripleComponents() {
        return tripleComponents;
    }
}
