package com.peng.idea.plugin.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.ui.*;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.listener.ChooserDisplayerActionListener;
import com.peng.idea.plugin.builder.manager.BuilderSettingsManager;
import com.peng.idea.plugin.builder.manager.BuilderTemplateManager;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.*;
import com.peng.idea.plugin.builder.util.psi.GuiHelperUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import static com.peng.idea.plugin.builder.util.BooleanUtil.nullToFalse;
import static com.peng.idea.plugin.builder.util.CollectionUtil.safeCollection;
import static com.peng.idea.plugin.builder.util.CollectionUtil.safeList;
import static com.peng.idea.plugin.builder.util.ObjectUtil.safeObject;
import static com.peng.idea.plugin.builder.util.StringUtil.isEmpty;
import static com.peng.idea.plugin.builder.util.StringUtil.nullToEmpty;
import static java.util.Objects.isNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:05
 * </pre>
 */
public class CreateBuilderDialog extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBuilderDialog.class);

    static final String RECENTS_KEY = "CreateBuilderDialog.RecentsKey";

    private Project project;
    private PsiDirectory targetDirectory;
    private PsiClass sourceClass;
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
//    private ReferenceEditorComboWithBrowseButton targetPackageField2;
    private PsiClass existingBuilder;
    private String targetPackageName;
    private final BuilderTemplate internalTemplateValue = new BuilderTemplate();
    private final BuilderSettings builderSettings;
    private final List<BuilderTemplate> builderTemplates;

    public CreateBuilderDialog(Project project,
                               String title,
                               PsiClass sourceClass,
                               String targetClassName,
                               String methodPrefix,
                               PsiPackage targetPackage,
                               PsiClass existingBuilder) {
        super(project, true);
        this.project = project;
        this.sourceClass = sourceClass;
        this.existingBuilder = existingBuilder;
        this.internalTemplateValue.setClassName(targetClassName);
        this.internalTemplateValue.setMethodPrefix(methodPrefix);
        this.builderSettings = BuilderSettingsManager.getInstance().getSettings();
        this.builderTemplates = safeList(BuilderTemplateManager.getInstance().getTemplates());
        this.targetPackageName = (targetPackage != null) ? targetPackage.getQualifiedName() : "";
//        targetPackageField = referenceEditorComboWithBrowseButtonFactory.getReferenceEditorComboWithBrowseButton(project, targetPackageName, RECENTS_KEY);
        targetPackageField = new ReferenceEditorComboWithBrowseButton(null, targetPackageName, project, true, RECENTS_KEY);
        targetPackageField.addActionListener(new ChooserDisplayerActionListener(targetPackageField, project));
        setTitle(title);
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
    protected JComponent createCenterPanel() {
        LOGGER.info("builderSettings: {}", GsonUtil.GSON.toJson(builderSettings));
        LOGGER.info("templates: {}", GsonUtil.GSON.toJson(builderTemplates));
        if (isNull(builderSettings) || isNull(builderSettings.getInternal()) || builderSettings.getInternal()
                || !safeCollection(builderTemplates).contains(builderSettings.getBuilderTemplate())) {
            return this.initForNull(builderTemplates);
        } else {
            return this.initForExists(builderSettings, builderTemplates);
        }
    }

    private JPanel initForNull(List<BuilderTemplate> templates) {
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
        targetClassNameField = new JBTextField(internalTemplateValue.getClassName());
        StyleUtil.setPreferredSize(targetClassNameField);
        builder.addLabelComponent(new JLabel("Class name: "), targetClassNameField);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField(internalTemplateValue.getMethodPrefix());
        StyleUtil.setPreferredSize(targetMethodPrefix);
        builder.addLabelComponent(new JLabel("Method prefix: "), targetMethodPrefix);
        // Method prefix

        // Destination package
//        targetPackageField = new ReferenceEditorComboWithBrowseButton(null, targetPackageName, project, true, Constants.BUILDER_SETTINGS_RECENTS_KEY);
//        targetPackageField.addActionListener(new ChooserDisplayerActionListener(targetPackageField, project));
        AnAction clickAction = new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent e) {
                targetPackageField.setButtonVisible(true);
//                targetPackageField.getButton().doClick();
            }
        };
        clickAction.registerCustomShortcutSet(
                new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK)),
                targetPackageField.getChildComponent()
        );
        // action.flatten.packages
        // dialog.create.class.destination.package.label
        builder.addLabelComponent(new JLabel(CodeInsightBundle.message("action.flatten.packages")), targetPackageField);
        // Destination package

//        // Destination package
////        new ComboBox<String>().addac
//        targetPackageField2 = new ReferenceEditorComboWithBrowseButton(null, targetPackageName, project, true, Constants.BUILDER_SETTINGS_RECENTS_KEY);
//        ExtendableTextComponent.Extension browseExtension =
//                ExtendableTextComponent.Extension.create(AllIcons.General.OpenDisk, AllIcons.General.OpenDiskHover,
//                        "Open file", () -> {
//                    System.out.println("Browse file clicked");
//                        });
//        ReferenceEditorComboWithBrowseButton refButton = new ReferenceEditorComboWithBrowseButton(null, targetPackageName, project, true, Constants.BUILDER_SETTINGS_RECENTS_KEY);
//        targetPackageField2.setEditor(refButton);
//        targetPackageField2.setEditor(new BasicComboBoxEditor(){
//            @Override
//            protected JTextField createEditorComponent() {
//                ExtendableTextField ecbEditor = new ExtendableTextField();
//                ecbEditor.addExtension(browseExtension);
//                ecbEditor.setBorder(null);
//                return ecbEditor;
//            }
//        });
//        builder.addLabelComponent(new JLabel(CodeInsightBundle.message("action.flatten.packages")), targetPackageField2);
//        // Destination package

        // Inner builder
        innerBuilder = new JCheckBox();
        innerBuilder.setSelected(false);
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        butMethod.setSelected(false);
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        useSingleField.setSelected(false);
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

        myMainPanel = builder.getPanel();

        return myMainPanel;
    }

    private JPanel initForExists(BuilderSettings settings, List<BuilderTemplate> templates) {
        LOGGER.info("initForExists");
        BuilderTemplate srcBuilderTemplate = settings.getBuilderTemplate();
        BuilderTemplate builderTemplate = srcBuilderTemplate == null ? new BuilderTemplate() : srcBuilderTemplate;

        PanelUtil builder = PanelUtil.builder();
        // Default
        myInternalRadio = new JRadioButton("Internal", false);
        myInternalRadio.setActionCommand("Internal");
        myInternalRadio.addActionListener(changeBelowFormListener());
        myDefaultGroup.add(myInternalRadio);
        myDefault.add(myInternalRadio);

        myCustomRadio = new JRadioButton("Custom", true);
        myCustomRadio.setActionCommand("Custom");
        myCustomRadio.addActionListener(changeBelowFormListener());
        myDefaultGroup.add(myCustomRadio);
        myDefault.add(myCustomRadio);

        builder.addLabelComponent(new JLabel("Default: "), myDefault);
        // Default

        // Custom template
        safeCollection(templates).forEach(customTemplate::addItem);
        customTemplate.setSelectedItem(srcBuilderTemplate);
        customTemplate.addActionListener(selectTemplateListener());
//        customTemplate.addMouseListener(changeTemplatesListener());
        builder.addLabelComponent(new JLabel("Custom template: "), customTemplate);
        // Custom template

        // Class name
        targetClassNameField = new JBTextField(this.generateTemplateClassName(builderTemplate.getClassName()));
        StyleUtil.setPreferredSize(targetClassNameField);
        builder.addLabelComponent(new JLabel("Class name: "), targetClassNameField);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField(nullToEmpty(builderTemplate.getMethodPrefix()));
        StyleUtil.setPreferredSize(targetMethodPrefix);
        builder.addLabelComponent(new JLabel("Method prefix: "), targetMethodPrefix);
        // Method prefix

        // Destination package
//        targetPackageField = new ReferenceEditorComboWithBrowseButton(null, targetPackageName, project, true, Constants.BUILDER_SETTINGS_RECENTS_KEY);
//        targetPackageField.addActionListener(new ChooserDisplayerActionListener(targetPackageField, project));
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
        innerBuilder.setSelected(nullToFalse(builderTemplate.getInnerBuilder()));
        builder.addLabelComponent(new JLabel("Inner builder: "), innerBuilder);
        // Inner builder

        // but method
        butMethod = new JCheckBox();
        butMethod.setSelected(nullToFalse(builderTemplate.getButMethod()));
        builder.addLabelComponent(new JLabel("'but' method: "), butMethod);
        // but method

        // useSingleField
        useSingleField = new JCheckBox();
        useSingleField.setSelected(nullToFalse(builderTemplate.getUseSingleField()));
        builder.addLabelComponent(new JLabel("Use single field: "), useSingleField);
        // useSingleField

        myMainPanel = builder.getPanel();

        return myMainPanel;
    }

    @Override
    protected void doOKAction() {
        registerEntry(RECENTS_KEY, targetPackageField.getText());
        Module module = PsiClassUtil.findModuleForPsiClass(sourceClass, project);
        if (module == null) {
            throw new IllegalStateException("Cannot find module for class " + sourceClass.getName());
        }
        try {
            checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField();
            checkIfClassCanBeCreated(module);
            callSuper();
        } catch (IncorrectOperationException e) {
            GuiHelperUtil.showMessageDialog(project, e.getMessage(), CommonBundle.getErrorTitle(), Messages.getErrorIcon());
        }
    }

    @Override
    protected void doHelpAction() {
        Messages.showInfoMessage(
                "You can configure Custom Template in this path: Settings/Tools/Builder Generator Plus",
                "Tips");
//        super.doHelpAction();
    }

    void checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField() {
        if (useSingleField()) {
            PsiMethod[] constructors = sourceClass.getConstructors();
            if(constructors.length == 0){
                return;
            }
            for (PsiMethod constructor : constructors) {
                if (constructor.getParameterList().getParametersCount() == 0) {
                    return;
                }
            }
            throw new IncorrectOperationException(String.format("%s must define a default constructor", sourceClass.getName()));
        }
    }

    void checkIfClassCanBeCreated(Module module) {
        if (!isInnerBuilder()) {
            Consumer<PsiDirectory> consumer = this::setTargetDirectory;
            SelectDirectory selectDirectory = new SelectDirectory(consumer, module, getPackageName(), getClassName(), existingBuilder);
            executeCommand(selectDirectory);
        }
    }

    void registerEntry(String key, String entry) {
        RecentsManager.getInstance(project).registerRecentEntry(key, entry);
    }

    void callSuper() {
        super.doOKAction();
    }

    void executeCommand(SelectDirectory selectDirectory) {
        CommandProcessor.getInstance().executeCommand(project, selectDirectory, CodeInsightBundle.message("create.directory.command"), null);
    }

    private String getPackageName() {
        String name = targetPackageField.getText();
        return (name != null) ? name.trim() : "";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return targetClassNameField;
    }

    public String getClassName() {
        return targetClassNameField.getText();
    }

    public String getMethodPrefix() {
        return targetMethodPrefix.getText();
    }

    public boolean isInnerBuilder() {
        return innerBuilder.isSelected();
    }

    public boolean hasButMethod() {
        return butMethod.isSelected();
    }

    public boolean useSingleField() {
        return useSingleField.isSelected();
    }

    public PsiDirectory getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(PsiDirectory targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    private ActionListener changeBelowFormListener() {
        return e -> {
            boolean isInternal = myInternalRadio.isSelected();
            customTemplate.setEnabled(!isInternal);
            if (isInternal) {
                customTemplate.setSelectedItem(null);
                targetClassNameField.setText(internalTemplateValue.getClassName());
                targetMethodPrefix.setText(internalTemplateValue.getMethodPrefix());
                innerBuilder.setSelected(false);
                butMethod.setSelected(false);
                useSingleField.setSelected(false);
            } else {
                BuilderTemplate builderTemplate = builderSettings.getBuilderTemplate();
                customTemplate.setSelectedItem(builderTemplate);
            }
        };
    }

    private ActionListener selectTemplateListener() {
        return e -> {
            boolean isInternal = myInternalRadio.isSelected();
            BuilderTemplate builderTemplate = (BuilderTemplate) customTemplate.getSelectedItem();
            if (!isInternal && builderTemplate != null) {
                targetClassNameField.setText(this.generateTemplateClassName(builderTemplate.getClassName()));
                targetMethodPrefix.setText(builderTemplate.getMethodPrefix());
                innerBuilder.setSelected(nullToFalse(builderTemplate.getInnerBuilder()));
                butMethod.setSelected(nullToFalse(builderTemplate.getButMethod()));
                useSingleField.setSelected(nullToFalse(builderTemplate.getUseSingleField()));
            }
        };
    }

    private String generateTemplateClassName(String className) {
        className = isEmpty(className) ? internalTemplateValue.getClassName() : className
                .replace(Constants.Template.INTERNAL_CLASS_NAME, nullToEmpty(internalTemplateValue.getClassName()))
                .replace(Constants.Template.SRC_CLASS_NAME, safeObject(sourceClass).map(PsiClass::getName).orElse(""));
        return className;
    }

    private static ActionListener getPreferenceRadioListener(ComboBox<BuilderTemplate> customTemplate) {
        return e -> {
            // 1.通过eActionCommand
            String eActionCommand = e.getActionCommand();
            System.out.printf("e.getActionCommand() is %s\n",
                    eActionCommand);
            if ("Default".equals(eActionCommand)) {
                customTemplate.setEnabled(false);
            } else if ("Custom".equals(eActionCommand)) {
                customTemplate.setEnabled(true);
            } else {
                customTemplate.setEnabled(false);
            }
        };
    }
}
