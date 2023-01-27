package com.peng.idea.plugin.builder.manager;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.peng.idea.plugin.builder.model.BuilderTemplate;
import com.peng.idea.plugin.builder.util.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.peng.idea.plugin.builder.util.CollectionUtil.safeStream;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:06
 * </pre>
 */
@State(name = "BuilderTemplateManager", storages = @Storage(StoragePathMacros.CACHE_FILE))
public class BuilderTemplateManager implements PersistentStateComponent<BuilderTemplateManager.State> {

    private final Map<String, Runnable> key2ListenerMap = new ConcurrentHashMap<>();

    private final State persistenceState = new State();

    private final List<BuilderTemplate> currentTemplates = new CopyOnWriteArrayList<>();

    public static BuilderTemplateManager getInstance() {
        return ApplicationManager.getApplication().getService(BuilderTemplateManager.class);
    }

    @Nullable
    @Override
    public BuilderTemplateManager.State getState() {
        persistenceState.builderTemplates = currentTemplates.stream()
                .filter(Objects::nonNull).distinct().map(BuilderTemplate::new).collect(Collectors.toList());
        return persistenceState;
    }


    @Override
    public void loadState(@NotNull BuilderTemplateManager.State state) {
        State state1 = new State();
        XmlSerializerUtil.copyBean(state, state1);
        persistenceState.builderTemplates = state1.builderTemplates;

        State state2 = new State();
        XmlSerializerUtil.copyBean(state, state2);
        currentTemplates.clear();
        currentTemplates.addAll(state2.builderTemplates);
    }

    public List<BuilderTemplate> getTemplates() {
        return currentTemplates.stream().distinct().collect(Collectors.toList());
    }

    public void save(List<BuilderTemplate> builderTemplates) {
        currentTemplates.clear();
        currentTemplates.addAll(safeStream(builderTemplates).distinct().collect(Collectors.toList()));
    }

    public void add(BuilderTemplate builderTemplate) {
        if (builderTemplate != null && !currentTemplates.contains(builderTemplate)) {
//            currentTemplates.add(JSON.parseObject(JSON.toJSONString(builderTemplate), BuilderTemplate.class));
            currentTemplates.add(GsonUtil.GSON.fromJson(GsonUtil.GSON.toJson(builderTemplate), BuilderTemplate.class));
        }

        runListener();
    }

    public void edit(BuilderTemplate builderTemplate) {
        if (builderTemplate != null) {
            currentTemplates.remove(null);
            for (int i = 0; i < currentTemplates.size(); i++) {
                if (Objects.equals(currentTemplates.get(i).getId(), builderTemplate.getId())) {
//                    currentTemplates.set(i, JSON.parseObject(JSON.toJSONString(builderTemplate), BuilderTemplate.class));
                    currentTemplates.set(i, GsonUtil.GSON.fromJson(GsonUtil.GSON.toJson(builderTemplate), BuilderTemplate.class));
                }
            }

        }
    }

    public void remove(BuilderTemplate builderTemplate) {
        if (builderTemplate == null) {
            currentTemplates.remove(null);
        } else {
            currentTemplates.removeIf(temp -> Objects.equals(temp.getId(), builderTemplate.getId()));
        }
        runListener();
    }

    public void moveUp(BuilderTemplate builderTemplate) {
        if (builderTemplate != null) {
            int index = currentTemplates.indexOf(builderTemplate);
            if (index > 0) {
                BuilderTemplate selectedTemplate = currentTemplates.get(index);
                BuilderTemplate upTemplate = currentTemplates.get(index - 1);
                currentTemplates.set(index, upTemplate);
                currentTemplates.set(index - 1, selectedTemplate);
                runListener();
            }
        }
    }

    public void moveDown(BuilderTemplate builderTemplate) {
        if (builderTemplate != null) {
            int index = currentTemplates.indexOf(builderTemplate);
            if (index < currentTemplates.size() - 1) {
                BuilderTemplate selectedTemplate = currentTemplates.get(index);
                BuilderTemplate downTemplate = currentTemplates.get(index + 1);
                currentTemplates.set(index, downTemplate);
                currentTemplates.set(index + 1, selectedTemplate);
                runListener();
            }
        }
    }

    public void putListener(String key, Runnable listener) {
        key2ListenerMap.put(key, listener);
    }

    public void removeListener(String key) {
        key2ListenerMap.remove(key);
    }

    private void runListener() {
        key2ListenerMap.forEach((key, listener) -> {
            listener.run();
        });
    }

    public static final class State {

        @Tag("builderTemplates")
        private List<BuilderTemplate> builderTemplates;
    }
}
