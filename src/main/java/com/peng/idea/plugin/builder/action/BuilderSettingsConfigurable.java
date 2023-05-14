package com.peng.idea.plugin.builder.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.peng.idea.plugin.builder.gui.BuilderSettingsComponent;
import com.peng.idea.plugin.builder.manager.BuilderSettingsManager;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 19:12
 * </pre>
 */
public class BuilderSettingsConfigurable implements Configurable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderSettingsConfigurable.class);

    private BuilderSettingsComponent mySettingsComponent;

    private Project project;

    public static BuilderSettingsConfigurable getInstance() {
        return ApplicationManager.getApplication().getService(BuilderSettingsConfigurable.class);
    }

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return BuilderConstant.Settings.BUILDER;
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
        mySettingsComponent = new BuilderSettingsComponent(project);
        mySettingsComponent.addListenerToBuilderTemplate();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {
        LOGGER.info("BuilderSettingsConfigurable.apply()");
        BuilderSettings builderSettings = mySettingsComponent.getSettings();
        BuilderSettingsManager.getInstance().save(builderSettings);
    }

    @Override
    public void disposeUIResources() {
        LOGGER.info("BuilderSettingsConfigurable.disposeUIResources()");
        if (mySettingsComponent != null) {
            mySettingsComponent.removeListenerFromBuilderTemplate();
            mySettingsComponent = null;
        }
    }
}

