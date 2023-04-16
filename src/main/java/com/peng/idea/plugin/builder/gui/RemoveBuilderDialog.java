package com.peng.idea.plugin.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.api.TripleComponentDO;
import com.peng.idea.plugin.builder.util.Pair;
import com.peng.idea.plugin.builder.util.PanelUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.psi.GuiHelperUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.*;

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
        LOGGER.info("click OK action!");
        callSuper();
//        registerEntry(RECENTS_KEY, targetPackageField.getText());
//        Module module = PsiClassUtil.findModuleForPsiClass(sourceClass, project);
//        if (module == null) {
//            throw new IllegalStateException("Cannot find module for class " + sourceClass.getName());
//        }
//        try {
//            checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
//            checkIfClassCanBeCreated(module);
//            callSuper();
//        } catch (IncorrectOperationException e) {
//            GuiHelperUtil.showMessageDialog(project, e.getMessage(), CommonBundle.getErrorTitle(), Messages.getErrorIcon());
//        }
    }

    @Override
    protected void doHelpAction() {
        Messages.showInfoMessage(
                "You can configure Custom Template in this path: Settings/Tools/Builder Generator Plus",
                "Tips");
//        super.doHelpAction();
    }

    void callSuper() {
        super.doOKAction();
    }

    private JPanel initFields() {
        PanelUtil builder = PanelUtil.builder();

        // column name
        builder.addTripleComponent(new JLabel("class name"), new JLabel("qualified name"), new JLabel("check box"));
        // column name

        // builder class to remove
        List<Pair<String, PsiClass>> pairs = Stream.of(Pair.of("1", removeDO.getEditorPsiClass()), Pair.of("2", removeDO.getDstPsiClass()))
                .filter(pair -> nonNull(pair.getSecond()))
                .filter(pair -> nonNull(pair.getSecond().getName()) && pair.getSecond().getName().endsWith(BuilderConstant.BUILDER_SUFFIX))
                .toList();
        tripleComponents = pairs.stream().map(pair -> {
            String key = pair.getFirst();
            PsiClass psiClass = pair.getSecond();
            return TripleComponentDO.builder()
                    .clazzName(new JTextField(psiClass.getName()))
                    .qualifiedName(new JTextField(psiClass.getQualifiedName()))
                    .checkBox(generateWithDefaultValue(pairs.size() == 1 || key.equals("2")))
                    .build();
        }).toList();
        tripleComponents.forEach(triple -> builder.addTripleComponent(
                triple.getClazzName(), triple.getQualifiedName(), triple.getCheckBox()
        ));
        // builder class to remove

        myMainPanel = builder.getPanel();
        return myMainPanel;
    }

    private static JCheckBox generateWithDefaultValue(boolean isCheck) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(isCheck);
        return checkBox;
    }
}
