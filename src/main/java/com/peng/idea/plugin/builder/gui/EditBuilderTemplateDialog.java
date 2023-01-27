package com.peng.idea.plugin.builder.gui;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.AutoCompleteUtil;
import com.peng.idea.plugin.builder.util.Constants;
import com.peng.idea.plugin.builder.util.PanelUtil;
import com.peng.idea.plugin.builder.util.StyleUtil;
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
 *  @date: 2023/1/19 19:14
 * </pre>
 */
public class EditBuilderTemplateDialog extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditBuilderTemplateDialog.class);

    private final Project project;
    private final BuilderTemplate builderTemplate;
    private JPanel myMainPanel;
    private JBTextField templateName;
    private JBTextField targetClassName;
    private JBTextField targetMethodPrefix;
    private ReferenceEditorComboWithBrowseButton targetPackageField;
    private JCheckBox innerBuilder;
    private JCheckBox butMethod;
    private JCheckBox useSingleField;

    public EditBuilderTemplateDialog(Project project, BuilderTemplate builderTemplate) {
        super(project, true);
        this.project = project;
        this.builderTemplate = builderTemplate == null ? new BuilderTemplate() : builderTemplate;
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
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
        LOGGER.info("click addBuilderTemplate ok");
        String templateNameText = templateName.getText();
        String targetClassNameText = targetClassName.getText();
        if (isEmpty(templateNameText) || isEmpty(targetClassNameText)) {
            Messages.showWarningDialog("'Template Name' and 'Class Name' are required!", "Fail to Edit");
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
    }

    private void initFields() {
        PanelUtil builder = PanelUtil.builder();

        // Template name
        templateName = new JBTextField(builderTemplate.getTemplateName());
        StyleUtil.setPreferredSize(templateName);
        builder.addLabelComponent(new JLabel("Template name: "), templateName);
        // Template name

        // Class name
        targetClassName = new JBTextField(builderTemplate.getClassName());
        StyleUtil.setPreferredSize(targetClassName);
        AutoCompleteUtil.setupAutoComplete(targetClassName, Constants.Template.getDynamicValues());
        builder.addLabelComponent(new JLabel("Class name: "), targetClassName);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField(builderTemplate.getMethodPrefix());
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
        innerBuilder.setSelected(Boolean.TRUE.equals(builderTemplate.getInnerBuilder()));
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        butMethod.setSelected(Boolean.TRUE.equals(builderTemplate.getButMethod()));
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        useSingleField.setSelected(Boolean.TRUE.equals(builderTemplate.getUseSingleField()));
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

        myMainPanel = builder.getPanel();
        setTitle("Add Builder");
    }

    public BuilderTemplate getTemplate() {
        String id = builderTemplate.getId();
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

