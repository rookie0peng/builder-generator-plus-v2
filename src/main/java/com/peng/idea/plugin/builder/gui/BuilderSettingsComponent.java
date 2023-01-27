package com.peng.idea.plugin.builder.gui;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import com.peng.idea.plugin.builder.manager.BuilderSettingsManager;
import com.peng.idea.plugin.builder.manager.BuilderTemplateManager;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.Constants;
import com.peng.idea.plugin.builder.util.PanelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeCollection;
import static com.peng.idea.plugin.builder.util.ObjectUtil.safeObject;
import static com.peng.idea.plugin.builder.util.StringUtil.nullToEmpty;
import static java.util.Objects.isNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:05
 * </pre>
 */
public class BuilderSettingsComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderSettingsComponent.class);

    private static final String LISTENER_KEY = "BuilderSettingsComponent";

    private final Project project;
    private JPanel myMainPanel;
    private final JPanel myDefault = new JPanel();
    private final ButtonGroup myDefaultGroup = new ButtonGroup();
    private JRadioButton myInternalRadio;
    private JRadioButton myCustomRadio;
    private final ComboBox<BuilderTemplate> customTemplate = new ComboBox<>();
    private JTextField targetClassNameField;
    private JTextField targetMethodPrefix;
    private JCheckBox innerBuilder;
    private JCheckBox butMethod;
    private JCheckBox useSingleField;
    private ReferenceEditorComboWithBrowseButton targetPackageField;
//    private EditorComboBox targetPackageField;
//    private final JBTextField myUserNameText = new JBTextField();
//    private final JBCheckBox myIdeaUserStatus = new JBCheckBox("Do you use IntelliJ IDEA? ");

    public BuilderSettingsComponent(Project project) {
        this.project = project;
        init();
//        myInternalRadio = new JRadioButton("Internal", true);

    }

    public JPanel getPanel() {
        return myMainPanel;
    }

//    public JComponent getPreferredFocusedComponent() {
//        return myUserNameText;
//    }
//
//    @NotNull
//    public String getUserNameText() {
//        return myUserNameText.getText();
//    }
//
//    public void setUserNameText(@NotNull String newText) {
//        myUserNameText.setText(newText);
//    }
//
//    public boolean getIdeaUserStatus() {
//        return myIdeaUserStatus.isSelected();
//    }
//
//    public void setIdeaUserStatus(boolean newStatus) {
//        myIdeaUserStatus.setSelected(newStatus);
//    }

    private void init() {
        BuilderSettings builderSettings = BuilderSettingsManager.getInstance().getSettings();
        List<BuilderTemplate> templates = BuilderTemplateManager.getInstance().getTemplates();
//        LOGGER.info("builderSettings: {}", JSON.toJSONString(builderSettings));
//        LOGGER.info("templates: {}", JSON.toJSONString(templates));
        if (isNull(builderSettings) || isNull(builderSettings.getInternal()) || builderSettings.getInternal()
                || !safeCollection(templates).contains(builderSettings.getBuilderTemplate())) {
            this.initForNull(templates);
        } else {
            this.initForExists(builderSettings, templates);
        }

    }

    private void initForNull(List<BuilderTemplate> templates) {
        LOGGER.info("initForNull");
        PanelUtil builder = PanelUtil.builder();

        // Default
        myInternalRadio = new JRadioButton("Internal", true);
        myInternalRadio.setActionCommand("Internal");
        myInternalRadio.addActionListener(changeBelowFormListener());
        myDefaultGroup.add(myInternalRadio);
        myDefault.add(myInternalRadio);

        myCustomRadio = new JRadioButton("Custom", false);
        myCustomRadio.setActionCommand("Custom");
        myCustomRadio.addActionListener(changeBelowFormListener());
        myDefaultGroup.add(myCustomRadio);
        myDefault.add(myCustomRadio);

        builder.addLabelComponent(new JLabel("Default: "), myDefault);
        // Default

        // Custom template
        safeCollection(templates).forEach(customTemplate::addItem);
        customTemplate.setSelectedItem(null);
        customTemplate.addActionListener(selectTemplateListener());
//        customTemplate.addMouseListener(changeTemplatesListener());
        builder.addLabelComponent(new JLabel("Custom template: "), customTemplate);
        // Custom template

        // Class name
        targetClassNameField = new JBTextField();
        targetClassNameField.setEnabled(false);
        builder.addLabelComponent(new JLabel("Class name: "), targetClassNameField);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField();
        targetMethodPrefix.setEnabled(false);
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
        builder.addLabelComponent(new JLabel(CodeInsightBundle.message("package.dependencies.progress.title")), targetPackageField);
        // Destination package

        // Inner builder
        innerBuilder = new JCheckBox();
        innerBuilder.setSelected(false);
        innerBuilder.setEnabled(false);
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        butMethod.setSelected(false);
        butMethod.setEnabled(false);
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        useSingleField.setSelected(false);
        useSingleField.setEnabled(false);
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

//        // Edit template
//        builder.addButton(new JButton("Edit template"));
//        // Edit template

        builder.addComponentFillVertically(new JPanel());
        myMainPanel = builder.getPanel();
    }

    private void initForExists(BuilderSettings builderSettings, List<BuilderTemplate> templates) {
        LOGGER.info("initForExists");
        BuilderTemplate srcBuilderTemplate = builderSettings.getBuilderTemplate();
        BuilderTemplate builderTemplate = srcBuilderTemplate == null ? new BuilderTemplate() : srcBuilderTemplate;

        PanelUtil builder = PanelUtil.builder();

        // Default
        boolean isInternal = Boolean.TRUE.equals(builderSettings.getInternal());
        myInternalRadio = new JRadioButton("Internal", isInternal);
        myInternalRadio.setActionCommand("Internal");
        myInternalRadio.addActionListener(changeBelowFormListener());

        myDefaultGroup.add(myInternalRadio);
        myDefault.add(myInternalRadio);
        myCustomRadio = new JRadioButton("Custom", !isInternal);
        myCustomRadio.setActionCommand("Custom");
        myCustomRadio.addActionListener(changeBelowFormListener());
        myDefaultGroup.add(myCustomRadio);
        myDefault.add(myCustomRadio);
        builder.addLabelComponent(new JLabel("Default: "), myDefault);
        // Default

        // Custom template
        safeCollection(templates).forEach(customTemplate::addItem);
        customTemplate.setSelectedItem(srcBuilderTemplate);
        customTemplate.setEnabled(!isInternal);
        customTemplate.addActionListener(selectTemplateListener());
//        customTemplate.addMouseListener(changeTemplatesListener());
        builder.addLabelComponent(new JLabel("Custom template: "), customTemplate);
        // Custom template

        // Class name
        targetClassNameField = new JBTextField(nullToEmpty(builderTemplate.getClassName()));
        targetClassNameField.setEnabled(false);
        builder.addLabelComponent(new JLabel("Class name: "), targetClassNameField);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField(nullToEmpty(builderTemplate.getMethodPrefix()));
        targetMethodPrefix.setEnabled(false);
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
        innerBuilder.setEnabled(false);
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        butMethod.setSelected(Boolean.TRUE.equals(builderTemplate.getButMethod()));
        butMethod.setEnabled(false);
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        useSingleField.setSelected(Boolean.TRUE.equals(builderTemplate.getUseSingleField()));
        useSingleField.setEnabled(false);
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

//        // Edit template
//        builder.addButton(new JButton("Edit template"));
//        // Edit template

        builder.addComponentFillVertically(new JPanel());
        myMainPanel = builder.getPanel();
    }

    private void addInnerPanelForDestinationPackageField(JPanel panel, GridBagConstraints gbConstraints) {
        JPanel innerPanel = createInnerPanelForDestinationPackageField();
        panel.add(innerPanel, gbConstraints);
    }

    private JPanel createInnerPanelForDestinationPackageField() {
        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(targetPackageField, BorderLayout.CENTER);
        return innerPanel;
    }

    private ActionListener getPreferenceRadioListener(ComboBox<BuilderTemplate> customTemplate) {
        return e -> {
            // 1.通过eActionCommand
            String eActionCommand = e.getActionCommand();
            System.out.printf("e.getActionCommand() is %s\n", eActionCommand);
            customTemplate.setEnabled("Custom".equals(eActionCommand));
        };
    }

    private ActionListener changeBelowFormListener() {
        return e -> {
            boolean isInternal = myInternalRadio.isSelected();
            customTemplate.setEnabled(!isInternal);
            if (!isInternal) {
                List<BuilderTemplate> templates = BuilderTemplateManager.getInstance().getTemplates();
                customTemplate.removeAllItems();
                safeCollection(templates).forEach(customTemplate::addItem);
            }
            customTemplate.setSelectedItem(null);
            targetClassNameField.setText("");
            targetMethodPrefix.setText("");
            innerBuilder.setSelected(false);
            butMethod.setSelected(false);
            useSingleField.setSelected(false);
        };
    }

    private ActionListener selectTemplateListener() {
        return e -> {
            boolean isInternal = myInternalRadio.isSelected();
            BuilderTemplate builderTemplate = (BuilderTemplate) customTemplate.getSelectedItem();
            if (!isInternal && builderTemplate != null) {
                targetClassNameField.setText(builderTemplate.getClassName());
                targetMethodPrefix.setText(builderTemplate.getMethodPrefix());
                innerBuilder.setSelected(Boolean.TRUE.equals(builderTemplate.getInnerBuilder()));
                butMethod.setSelected(Boolean.TRUE.equals(builderTemplate.getButMethod()));
                useSingleField.setSelected(Boolean.TRUE.equals(builderTemplate.getUseSingleField()));
            }
        };
    }

    public BuilderSettings getSettings() {
        boolean isInternal = myInternalRadio.isSelected();
        BuilderTemplate builderTemplate = (BuilderTemplate) customTemplate.getSelectedItem();
        if (isInternal || builderTemplate == null) {
            return BuilderSettings.builder().isInternal(true).build();
        } else {
            return BuilderSettings.builder()
                    .isInternal(false).templateId(builderTemplate.getId())
                    .builderTemplate(builderTemplate)
                    .build();
        }
    }

    public void addListenerToBuilderTemplate() {
        Runnable autoChangeCustomTemplate = () -> {
            if (isNull(myInternalRadio))
                return;
            boolean isInternal = myInternalRadio.isSelected();
            customTemplate.setEnabled(!isInternal);
            if (!isInternal) {
                BuilderSettings settings = BuilderSettingsManager.getInstance().getSettings();
                List<BuilderTemplate> templates = BuilderTemplateManager.getInstance().getTemplates();
                customTemplate.removeAllItems();
                safeCollection(templates).forEach(customTemplate::addItem);
                customTemplate.setSelectedItem(safeObject(settings).map(BuilderSettings::getBuilderTemplate).orElse(null));
            }
        };
        BuilderTemplateManager.getInstance().putListener(LISTENER_KEY, autoChangeCustomTemplate);
    }

    public void removeListenerFromBuilderTemplate() {
        BuilderTemplateManager.getInstance().removeListener(LISTENER_KEY);
    }
}


