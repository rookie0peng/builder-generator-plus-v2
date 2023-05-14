package com.peng.idea.plugin.builder.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.peng.idea.plugin.builder.gui.AddBuilderTemplateDialog;
import com.peng.idea.plugin.builder.gui.EditBuilderTemplateDialog;
import com.peng.idea.plugin.builder.manager.BuilderSettingsManager;
import com.peng.idea.plugin.builder.manager.BuilderTemplateManager;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeList;
import static java.util.Objects.nonNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 19:13
 * </pre>
 */
public class BuilderTemplateConfigurable implements Configurable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderTemplateConfigurable.class);

    private Project project;

    private JBList<BuilderTemplate> jbList;

    private DefaultListModel<BuilderTemplate> defaultListModel;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return BuilderConstant.Settings.BUILDER_TEMPLATE;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        Promise<DataContext> dataContextFromFocusAsync = DataManager.getInstance().getDataContextFromFocusAsync();
        DataContext dataContext = null;
        try {
            dataContext = dataContextFromFocusAsync.blockingGet(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("get dataContext fail, exception: ", e);
        }
        if (nonNull(dataContext)) {
            project = dataContext.getData(CommonDataKeys.PROJECT);
        }

        // 获取已存储的模板
        List<BuilderTemplate> templates = safeList(BuilderTemplateManager.getInstance().getTemplates());
        defaultListModel = new DefaultListModel<>();
        templates.forEach(template -> defaultListModel.addElement(template));
        jbList = new JBList<>(defaultListModel);

        // 修饰每一行的元素
        ColoredListCellRenderer<BuilderTemplate> coloredListCellRenderer = new ColoredListCellRenderer<>() {
            @Override
            protected void customizeCellRenderer(@NotNull JList<? extends BuilderTemplate> list, BuilderTemplate value, int index, boolean selected, boolean hasFocus) {
                append(value.getTemplateName());
            }
        };
        jbList.setCellRenderer(coloredListCellRenderer);

        // 触发快速查找
        new ListSpeedSearch<>(jbList);

        // 增加工具栏（新增按钮、删除按钮、上移按钮、下移按钮）
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(jbList);
        // 新增元素动作
        decorator.setAddAction(actionButton -> addAction());
        // 编辑元素
        decorator.setEditAction(editAction -> editAction());
        // 移除元素
        decorator.setRemoveAction(removeAction -> removeAction());
        // 上移元素
        decorator.setMoveUpAction(moveUpAction -> moveUpAction());
        // 下移元素
        decorator.setMoveDownAction(moveDownAction -> moveDownAction());
//        // 自定义按钮
//        decorator.addExtraAction(new ExtraButtonAction(defaultListModel,list));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(decorator.createPanel(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
//        Enumeration<BuilderTemplate> elements = defaultListModel.elements();
//        BuilderTemplate builderTemplate;
//        List<BuilderTemplate> builderTemplates = new ArrayList<>();
//        while (elements.hasMoreElements()) {
//            builderTemplate = elements.nextElement();
//            builderTemplates.add(builderTemplate);
//        }
//        BuilderTemplateManager.getInstance().save(builderTemplates);
    }

    private void addAction() {
        AddBuilderTemplateDialog addBuilderTemplateDialog = new AddBuilderTemplateDialog(project);
        addBuilderTemplateDialog.show();
        if (addBuilderTemplateDialog.isOK()) {
            BuilderTemplate template = addBuilderTemplateDialog.getTemplate();
            defaultListModel.addElement(template);
            BuilderTemplateManager.getInstance().add(template);
        }
    }

    private void editAction() {
        int index = jbList.getSelectedIndex();
        BuilderTemplate builderTemplate = defaultListModel.get(index);
        EditBuilderTemplateDialog editBuilderTemplateDialog = new EditBuilderTemplateDialog(project, builderTemplate);
        editBuilderTemplateDialog.show();
        if (editBuilderTemplateDialog.isOK()) {
            BuilderTemplate template = editBuilderTemplateDialog.getTemplate();
            defaultListModel.set(index, template);
            BuilderTemplateManager.getInstance().edit(template);
        }
    }

    private void removeAction() {
        int index = jbList.getSelectedIndex();
        BuilderTemplate builderTemplate = defaultListModel.get(index);
        BuilderSettings settings = BuilderSettingsManager.getInstance().getSettings();
        if (nonNull(settings) && Objects.equals(settings.getBuilderTemplate(), builderTemplate)) {
            Messages.showWarningDialog("This template is in used, can't remove!", "Remove Template Fail");
            return;
        }
        defaultListModel.remove(index);
        BuilderTemplateManager.getInstance().remove(builderTemplate);
    }

    private void moveUpAction() {
        int index = jbList.getSelectedIndex();
        if (index > 0) {
            BuilderTemplate selectedTemplate = defaultListModel.get(index);
            BuilderTemplate upTemplate = defaultListModel.get(index - 1);
            defaultListModel.set(index, upTemplate);
            defaultListModel.set(index - 1, selectedTemplate);
            BuilderTemplateManager.getInstance().moveUp(selectedTemplate);
        }
    }

    private void moveDownAction() {
        int index = jbList.getSelectedIndex();
        if (index < defaultListModel.size() - 1) {
            BuilderTemplate selectedTemplate = defaultListModel.get(index);
            BuilderTemplate downTemplate = defaultListModel.get(index + 1);
            defaultListModel.set(index, downTemplate);
            defaultListModel.set(index + 1, selectedTemplate);
            BuilderTemplateManager.getInstance().moveDown(selectedTemplate);
        }
    }
}

