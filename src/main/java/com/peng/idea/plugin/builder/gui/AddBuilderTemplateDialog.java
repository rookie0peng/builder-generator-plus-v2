package com.peng.idea.plugin.builder.gui;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.peng.idea.plugin.builder.util.StringUtil.isEmpty;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 19:13
 * </pre>
 */
public class AddBuilderTemplateDialog extends DialogWrapper {

    private final Logger logger = LoggerFactory.getLogger(AddBuilderTemplateDialog.class);

    private final Project project;
    private JPanel myMainPanel;
    private JBTextField templateName;
    private JBTextField targetClassName;
    private JBTextField targetMethodPrefix;
    private ReferenceEditorComboWithBrowseButton targetPackageField;
    private JCheckBox innerBuilder;
    private JCheckBox butMethod;
    private JCheckBox useSingleField;

    public AddBuilderTemplateDialog(Project project) {
        super(project, true);
        this.project = project;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        initFields();
        return myMainPanel;
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
//        registerEntry(RECENTS_KEY, targetPackageField.getText());
//        Module module = psiHelper.findModuleForPsiClass(sourceClass, project);
//        if (module == null) {
//            throw new IllegalStateException("Cannot find module for class " + sourceClass.getName());
//        }
//        try {
//            checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
//            checkIfClassCanBeCreated(module);
//            callSuper();
//        } catch (IncorrectOperationException e) {
//            guiHelper.showMessageDialog(project, e.getMessage(), CommonBundle.getErrorTitle(), Messages.getErrorIcon());
//        }

        logger.info("click addBuilderTemplate ok");
        String templateNameText = templateName.getText();
        String targetClassNameText = targetClassName.getText();
        if (isEmpty(templateNameText) || isEmpty(targetClassNameText)) {
            Messages.showWarningDialog("'Template Name' and 'Class Name' are required!", "Fail to Add");
            return;
        }
        super.doOKAction();
    }

    @Override
    protected void doHelpAction() {
        Messages.showInfoMessage(
                "You can use dynamic value in 'Class Name': " +
                        "\n1.${internalClassName}: this value will replace with internal class name." +
                        "\n2.${srcClassName}: this value will replace with current class name.",
                "Tips");
//        super.doHelpAction();
    }

    private void initFields() {
        PanelUtil builder = PanelUtil.builder();

        // Template name
        templateName = new JBTextField();
        StyleUtil.setPreferredSize(templateName);

        builder.addLabelComponent(new JLabel("Template name: "), templateName);
        // Template name

        // Class name
        targetClassName = new JBTextField();
        StyleUtil.setPreferredSize(targetClassName);
        AutoCompleteUtil.setupAutoComplete(targetClassName, Constants.Template.getDynamicValues());
        builder.addLabelComponent(new JLabel("Class name: "), targetClassName);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField();
        StyleUtil.setPreferredSize(targetMethodPrefix);
        builder.addLabelComponent(new JLabel("Method prefix: "), targetMethodPrefix);
        // Method prefix

        // Destination package
        targetPackageField = new ReferenceEditorComboWithBrowseButton(null, null, this.project, true, Constants.BUILDER_SETTINGS_RECENTS_KEY);
        targetPackageField.setEnabled(false);
        AnAction clickAction = new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent e) {
                targetPackageField.setButtonVisible(true);
//                targetPackageField.getButton().doClick();
            }
        };
        clickAction.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK)),
                targetPackageField.getChildComponent());
        builder.addLabelComponent(new JLabel(CodeInsightBundle.message("action.flatten.packages")), targetPackageField);
        // Destination package

        // Inner builder
        innerBuilder = new JCheckBox();
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

        myMainPanel = builder.getPanel();
        myMainPanel.setBorder(IdeBorderFactory.createBorder());
        setTitle("Add Builder");
    }

    public BuilderTemplate getTemplate() {
        String id = UUIDUtil.randomUUID();
        String templateName = this.templateName.getText();
        String targetClassName = this.targetClassName.getText();
        String targetMethodPrefix = this.targetMethodPrefix.getText();
        boolean innerBuilder = this.innerBuilder.isSelected();
        boolean butMethod = this.butMethod.isSelected();
        boolean useSingleField = this.useSingleField.isSelected();
        return BuilderTemplate.builder()
                .id(id).templateName(templateName).className(targetClassName).methodPrefix(targetMethodPrefix)
                .innerBuilder(innerBuilder).butMethod(butMethod).useSingleField(useSingleField)
                .build();
    }
}

