package com.peng.idea.plugin.builder.manager;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.peng.idea.plugin.builder.model.BuilderSettings;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:06
 * </pre>
 */
@State(name = "BuilderSettingsManager", storages = @Storage(StoragePathMacros.CACHE_FILE))
public class BuilderSettingsManager implements PersistentStateComponent<BuilderSettings> {

    private final BuilderSettings persistenceConfig = new BuilderSettings();

    private final BuilderSettings currentSettings = new BuilderSettings();

    public static BuilderSettingsManager getInstance() {
        return ApplicationManager.getApplication().getService(BuilderSettingsManager.class);
    }

    @Nullable
    @Override
    public BuilderSettings getState() {
        Boolean internal = currentSettings.getInternal();
        persistenceConfig.setInternal(internal == null || internal);
        if (Boolean.FALSE.equals(persistenceConfig.getInternal())) {
            persistenceConfig.setTemplateId(currentSettings.getTemplateId());
            persistenceConfig.setBuilderTemplate(new BuilderTemplate(currentSettings.getBuilderTemplate()));
        }
        return persistenceConfig;
    }

    @Override
    public void loadState(@NotNull BuilderSettings state) {
        XmlSerializerUtil.copyBean(state, persistenceConfig);
        XmlSerializerUtil.copyBean(state, currentSettings);
    }

    public BuilderSettings getSettings() {
//        return JSON.parseObject(JSON.toJSONString(currentSettings), BuilderSettings.class);
        return GsonUtil.GSON.fromJson(GsonUtil.GSON.toJson(currentSettings), BuilderSettings.class);
    }

    public void save(BuilderSettings builderSettings) {
        if (builderSettings != null) {
//            BuilderSettings cloneBuilderSettings = JSON.parseObject(JSON.toJSONString(builderSettings), BuilderSettings.class);
            BuilderSettings cloneBuilderSettings = GsonUtil.GSON.fromJson(GsonUtil.GSON.toJson(builderSettings), BuilderSettings.class);
            currentSettings.setInternal(cloneBuilderSettings.getInternal());
            currentSettings.setTemplateId(cloneBuilderSettings.getTemplateId());
            currentSettings.setBuilderTemplate(cloneBuilderSettings.getBuilderTemplate());
        }
    }
}
