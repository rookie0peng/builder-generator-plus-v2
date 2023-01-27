package com.peng.idea.plugin.builder.model;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:06
 * </pre>
 */
public class BuilderSettings implements Entity {

    private Boolean isInternal;

    private String templateId;

    private BuilderTemplate builderTemplate;

    public BuilderSettings() {

    }

    public BuilderSettings(BuilderSettings builderSettings) {
        this.isInternal = builderSettings.getInternal();
        this.templateId = builderSettings.getTemplateId();
        this.builderTemplate = new BuilderTemplate(builderSettings.getBuilderTemplate());
    }

    public static BuilderSettingsBuilder builder() {
        return BuilderSettingsBuilder.aBuilderSettings();
    }

    public Boolean getInternal() {
        return isInternal;
    }

    public void setInternal(Boolean internal) {
        isInternal = internal;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public BuilderTemplate getBuilderTemplate() {
        return builderTemplate;
    }

    public void setBuilderTemplate(BuilderTemplate builderTemplate) {
        this.builderTemplate = builderTemplate;
    }

    @Override
    public String toString() {
        return "BuilderSettings{" +
                "isInternal=" + isInternal +
                ", templateId='" + templateId + '\'' +
                ", builderTemplate=" + builderTemplate +
                '}';
    }

    public static final class BuilderSettingsBuilder {
        private Boolean isInternal;
        private String templateId;
        private BuilderTemplate builderTemplate;

        private BuilderSettingsBuilder() {
        }

        public static BuilderSettingsBuilder aBuilderSettings() {
            return new BuilderSettingsBuilder();
        }

        public BuilderSettingsBuilder isInternal(Boolean isInternal) {
            this.isInternal = isInternal;
            return this;
        }

        public BuilderSettingsBuilder templateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public BuilderSettingsBuilder builderTemplate(BuilderTemplate builderTemplate) {
            this.builderTemplate = builderTemplate;
            return this;
        }

        public BuilderSettings build() {
            BuilderSettings builderSettings = new BuilderSettings();
            builderSettings.setTemplateId(templateId);
            builderSettings.setBuilderTemplate(builderTemplate);
            builderSettings.isInternal = this.isInternal;
            return builderSettings;
        }
    }
}

