package com.peng.idea.plugin.builder.gui;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.RecentsManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.api.GenerateBuilderDialogDO;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.api.TripleComponentDO;
import com.peng.idea.plugin.builder.listener.ChooserDisplayerActionListener;
import com.peng.idea.plugin.builder.manager.BuilderSettingsManager;
import com.peng.idea.plugin.builder.manager.BuilderTemplateManager;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.Constants;
import com.peng.idea.plugin.builder.util.GsonUtil;
import com.peng.idea.plugin.builder.util.PanelUtil;
import com.peng.idea.plugin.builder.util.StyleUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import com.peng.idea.plugin.builder.util.psi.GuiHelperUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
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
 *  @date: 2023/5/1 12:28
 * </pre>
 */
public class GenerateBuilderDialogV2 extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveBuilderDialogV2.class);

    private static final String RECENTS_KEY = "GenerateBuilderDialogV2.RecentsKey";

    private final Project project;
    private PsiDirectory targetDirectory;
    private JPanel myMainPanel;
    private JRadioButton internalRadio;
    private JRadioButton customRadio;
    private final ComboBox<BuilderTemplate> customTemplateCombo = new ComboBox<>();
    private JTextField targetClassNameField;
    private JTextField targetMethodPrefix;
    private JCheckBox innerBuilder;
    private JCheckBox butMethod;
    private JCheckBox useSingleField;
    private ReferenceEditorComboWithBrowseButton targetPackageField;
    private final GenerateBuilderDialogDO generateDO;
    private final BuilderSettings builderSettings;
    private final List<BuilderTemplate> builderTemplates;
    private final BuilderTemplate internalBuilderTemplate = new BuilderTemplate();;

    public GenerateBuilderDialogV2(@NotNull Project project, @NotNull GenerateBuilderDialogDO generateDO) {
        super(project, true);
        this.project = project;
        this.generateDO = generateDO;
        this.builderSettings = BuilderSettingsManager.getInstance().getSettings();
        this.builderTemplates = safeList(BuilderTemplateManager.getInstance().getTemplates());
        internalBuilderTemplate.setClassName(generateDO.getEditorPsiClass().getName() + BuilderConstant.BUILDER_SUFFIX);
        internalBuilderTemplate.setMethodPrefix(BuilderConstant.METHOD_PREFIX);
        this.targetPackageField = new ReferenceEditorComboWithBrowseButton(
                null, generateDO.getEditorPackage().getQualifiedName(), project, true, RECENTS_KEY
        );
        this.targetPackageField.addActionListener(new ChooserDisplayerActionListener(targetPackageField, project));
        setTitle(BuilderConstant.GenerateBuilder.DIALOG_NAME);
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
    protected @Nullable JComponent createCenterPanel() {
        LOGGER.info("builderSettings: {}", GsonUtil.GSON.toJson(builderSettings));
        LOGGER.info("templates: {}", GsonUtil.GSON.toJson(builderTemplates));
        if (isNull(builderSettings) || isNull(builderSettings.getInternal()) || builderSettings.getInternal()
                || !safeCollection(builderTemplates).contains(builderSettings.getBuilderTemplate())) {
            return this.initForNull(builderTemplates);
        } else {
            return this.initForExists(builderSettings, builderTemplates);
        }

    }

    @Override
    protected void doOKAction() {
        registerEntry(RECENTS_KEY, targetPackageField.getText());
        Module module = PsiClassUtil.findModuleForPsiClass(generateDO.getEditorPsiClass(), project);
        if (module == null) {
            throw new IllegalStateException("Cannot find module for class " + generateDO.getEditorPsiClass().getName());
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

    private JPanel initForNull(List<BuilderTemplate> templates) {
        LOGGER.info("initForNull");
        PanelUtil builder = PanelUtil.builder();
        // Default
        JPanel chooseDefault = new JPanel();
        ButtonGroup chooseDefaultGroup = new ButtonGroup();

        internalRadio = new JRadioButton("Internal", true);
        internalRadio.setActionCommand("Internal");
        internalRadio.addActionListener(changeBelowFormListener());
        chooseDefaultGroup.add(internalRadio);
        chooseDefault.add(internalRadio);

        customRadio = new JRadioButton("Custom", false);
        customRadio.setActionCommand("Custom");
        customRadio.addActionListener(changeBelowFormListener());
        chooseDefaultGroup.add(customRadio);
        chooseDefault.add(customRadio);
        builder.addLabelComponent(new JLabel("Default: "), chooseDefault);
        // Default

        // Custom template
        safeCollection(templates).forEach(customTemplateCombo::addItem);
        customTemplateCombo.setSelectedItem(null);
        customTemplateCombo.addActionListener(selectTemplateListener());
//        customTemplate.addMouseListener(changeTemplatesListener());
        builder.addLabelComponent(new JLabel("Custom template: "), customTemplateCombo);
        // Custom template

        // Class name
        targetClassNameField = new JBTextField(internalBuilderTemplate.getClassName());
        StyleUtil.setPreferredSize(targetClassNameField);
        builder.addLabelComponent(new JLabel("Class name: "), targetClassNameField);
        // Class name

        // Method prefix
        targetMethodPrefix = new JBTextField(internalBuilderTemplate.getMethodPrefix());
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
        JPanel chooseDefault = new JPanel();
        ButtonGroup chooseDefaultGroup = new ButtonGroup();

        internalRadio = new JRadioButton("Internal", false);
        internalRadio.setActionCommand("Internal");
        internalRadio.addActionListener(changeBelowFormListener());
        chooseDefaultGroup.add(internalRadio);
        chooseDefault.add(internalRadio);

        customRadio = new JRadioButton("Custom", true);
        customRadio.setActionCommand("Custom");
        customRadio.addActionListener(changeBelowFormListener());
        chooseDefaultGroup.add(customRadio);
        chooseDefault.add(customRadio);

        builder.addLabelComponent(new JLabel("Default: "), chooseDefault);
        // Default

        // Custom template
        safeCollection(templates).forEach(customTemplateCombo::addItem);
        customTemplateCombo.setSelectedItem(srcBuilderTemplate);
        customTemplateCombo.addActionListener(selectTemplateListener());
//        customTemplate.addMouseListener(changeTemplatesListener());
        builder.addLabelComponent(new JLabel("Custom template: "), customTemplateCombo);
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

    private ActionListener changeBelowFormListener() {
        return e -> {
            boolean isInternal = internalRadio.isSelected();
            customTemplateCombo.setEnabled(!isInternal);
            if (isInternal) {
                customTemplateCombo.setSelectedItem(null);

                targetClassNameField.setText(internalBuilderTemplate.getClassName());
                targetMethodPrefix.setText(internalBuilderTemplate.getMethodPrefix());
                innerBuilder.setSelected(nullToFalse(internalBuilderTemplate.getInnerBuilder()));
                butMethod.setSelected(nullToFalse(internalBuilderTemplate.getButMethod()));
                useSingleField.setSelected(nullToFalse(internalBuilderTemplate.getUseSingleField()));
            } else {
                BuilderTemplate builderTemplate = builderSettings.getBuilderTemplate();
                customTemplateCombo.setSelectedItem(builderTemplate);
            }
        };
    }

    private ActionListener selectTemplateListener() {
        return e -> {
            boolean isInternal = internalRadio.isSelected();
            BuilderTemplate builderTemplate = (BuilderTemplate) customTemplateCombo.getSelectedItem();
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
        className = isEmpty(className) ? internalBuilderTemplate.getClassName() : className
                .replace(Constants.Template.INTERNAL_CLASS_NAME, nullToEmpty(internalBuilderTemplate.getClassName()))
                .replace(Constants.Template.SRC_CLASS_NAME,
                        safeObject(generateDO.getEditorPsiClass()).map(PsiClass::getName).orElse(""));
        return className;
    }

    private void checkIfSourceClassHasZeroArgsConstructorWhenUsingSingleField() {
        if (useSingleField()) {
            PsiClass editorPsiClass = generateDO.getEditorPsiClass();
            PsiMethod[] constructors = editorPsiClass.getConstructors();
            if(constructors.length == 0){
                return;
            }
            for (PsiMethod constructor : constructors) {
                if (constructor.getParameterList().getParametersCount() == 0) {
                    return;
                }
            }
            throw new IncorrectOperationException(
                    String.format("%s must define a default constructor", editorPsiClass.getName())
            );
        }
    }

    private void checkIfClassCanBeCreated(Module module) {
        if (!isInnerBuilder()) {
            Consumer<PsiDirectory> consumer = this::setTargetDirectory;
            SelectDirectory selectDirectory = new SelectDirectory(consumer, module, getPackageName(), getClassName(), generateDO.getEditorPsiClass());
            executeCommand(selectDirectory);
        }
    }

    void executeCommand(SelectDirectory selectDirectory) {
        CommandProcessor.getInstance().executeCommand(project, selectDirectory, CodeInsightBundle.message("create.directory.command"), null);
    }

    private String getPackageName() {
        String name = targetPackageField.getText();
        return (name != null) ? name.trim() : "";
    }

    private void registerEntry(String key, String entry) {
        RecentsManager.getInstance(project).registerRecentEntry(key, entry);
    }

    void callSuper() {
        super.doOKAction();
    }

    private PsiPackage getPackage(Project project, Editor editor) {
        PsiDirectory srcDir = PsiClassUtil.getPsiFileFromEditor(editor, project).getContainingDirectory();
        return PsiClassUtil.getPackage(srcDir);
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


}
